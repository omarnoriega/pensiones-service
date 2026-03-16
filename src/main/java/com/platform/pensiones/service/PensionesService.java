package com.platform.pensiones.service;

import com.opencsv.CSVReader;
import com.platform.pensiones.dto.EntidadResponse;
import com.platform.pensiones.exception.EntidadNotFoundException;
import com.platform.pensiones.model.EntidadPensiones;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Carga el CSV de pensiones al arrancar y expone métodos de consulta.
 * Los datos viven en memoria — no se requiere base de datos.
 */
@Service
public class PensionesService {

    private static final Logger log = LoggerFactory.getLogger(PensionesService.class);
    private static final String CSV_PATH = "pensiones.csv";

    // Mapa: nombre en minúsculas → entidad (para búsqueda case-insensitive)
    private final Map<String, EntidadPensiones> entidades = new LinkedHashMap<>();

    @PostConstruct
    public void cargarDatos() {
        log.info("Cargando datos de pensiones desde {}", CSV_PATH);
        try {
            var resource = new ClassPathResource(CSV_PATH);
            // BOM-safe reader: el CSV tiene BOM UTF-8 (\uFEFF)
            try (var reader = new CSVReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                String[] linea;
                boolean primeraLinea = true;

                while ((linea = reader.readNext()) != null) {
                    // Saltar encabezado
                    if (primeraLinea) {
                        primeraLinea = false;
                        continue;
                    }
                    // Saltar filas vacías (solo comas)
                    if (linea.length == 0 || linea[0].trim().isEmpty()) {
                        continue;
                    }
                    // Necesitamos al menos 6 columnas
                    if (linea.length < 6) {
                        continue;
                    }

                    try {
                        // Limpiar BOM del primer campo si existe
                        String nombre = linea[0].replace("\uFEFF", "").trim();
                        if (nombre.isEmpty()) continue;

                        long cotizantes   = parseLong(linea[1]);
                        long noCotizantes = parseLong(linea[2]);
                        long activos      = parseLong(linea[3]);
                        long inactivos    = parseLong(linea[4]);
                        long total        = parseLong(linea[5]);

                        EntidadPensiones entidad = new EntidadPensiones(
                                nombre, cotizantes, noCotizantes, activos, inactivos, total);

                        entidades.put(nombre.toLowerCase(), entidad);
                        log.debug("Cargada: {} | cotizantes={} | total={}", nombre, cotizantes, total);

                    } catch (NumberFormatException e) {
                        log.warn("Fila ignorada por formato inválido: {}", Arrays.toString(linea));
                    }
                }
            }
            log.info("Datos cargados exitosamente: {} entidades", entidades.size());

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el CSV de pensiones: " + e.getMessage(), e);
        }
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

    /**
     * Retorna todas las entidades cargadas.
     */
    public List<EntidadResponse> listarTodas() {
        return entidades.values().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca una entidad por nombre exacto (case-insensitive, tolerante a espacios).
     */
    public EntidadResponse buscarPorNombre(String nombre) {
        String clave = nombre.trim().toLowerCase();

        // Búsqueda exacta
        EntidadPensiones entidad = entidades.get(clave);
        if (entidad != null) return toResponse(entidad);

        // Búsqueda parcial (contiene)
        Optional<EntidadPensiones> parcial = entidades.values().stream()
                .filter(e -> e.getEntidadAdministradora().toLowerCase().contains(clave))
                .findFirst();

        return parcial
                .map(this::toResponse)
                .orElseThrow(() -> new EntidadNotFoundException(nombre));
    }

    /**
     * Lista los nombres de todas las entidades disponibles.
     */
    public List<String> listarNombres() {
        return entidades.values().stream()
                .map(EntidadPensiones::getEntidadAdministradora)
                .collect(Collectors.toList());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private EntidadResponse toResponse(EntidadPensiones e) {
        return new EntidadResponse(
                e.getEntidadAdministradora(),
                e.getCotizantes(),
                e.getNoCotizantes(),
                e.getActivos(),
                e.getInactivos(),
                e.getTotal()
        );
    }

    private long parseLong(String valor) {
        if (valor == null) return 0L;
        String limpio = valor.trim().replace(",", "").replace(".", "");
        if (limpio.isEmpty()) return 0L;
        return Long.parseLong(limpio);
    }
}
