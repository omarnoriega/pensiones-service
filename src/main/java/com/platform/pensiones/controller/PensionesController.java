package com.platform.pensiones.controller;

import com.platform.pensiones.dto.ApiResponse;
import com.platform.pensiones.dto.EntidadResponse;
import com.platform.pensiones.service.PensionesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pensiones")
@Tag(name = "Pensiones", description = "Afiliados al Sistema General de Pensiones de Colombia")
public class PensionesController {

    private final PensionesService pensionesService;

    public PensionesController(PensionesService pensionesService) {
        this.pensionesService = pensionesService;
    }

    // ── GET /api/v1/pensiones ─────────────────────────────────────────────────

    @GetMapping
    @Operation(
        summary = "Listar todas las entidades",
        description = """
            Retorna la lista completa de entidades administradoras de pensiones con
            sus estadísticas de **cotizantes**, **no cotizantes**, **activos**,
            **inactivos** y **total** de afiliados.
            
            Incluye filas de totales agregados (ej: *Total Prima Media*, *Total Afiliados Al Sistema*).
            """
    )
    public ResponseEntity<ApiResponse<List<EntidadResponse>>> listarTodas() {
        List<EntidadResponse> datos = pensionesService.listarTodas();
        return ResponseEntity.ok(
                ApiResponse.ok("Total de registros: " + datos.size(), datos));
    }

    // ── GET /api/v1/pensiones/nombres ─────────────────────────────────────────

    @GetMapping("/nombres")
    @Operation(
        summary = "Listar nombres de entidades",
        description = "Retorna solo los nombres de las entidades disponibles. Útil para conocer las claves de búsqueda válidas."
    )
    public ResponseEntity<ApiResponse<List<String>>> listarNombres() {
        List<String> nombres = pensionesService.listarNombres();
        return ResponseEntity.ok(
                ApiResponse.ok(nombres.size() + " entidades disponibles", nombres));
    }

    // ── GET /api/v1/pensiones/buscar?nombre=xxx ───────────────────────────────

    @GetMapping("/buscar")
    @Operation(
        summary = "Buscar entidad por nombre",
        description = """
            Busca una entidad administradora por su nombre.
            
            - La búsqueda es **case-insensitive** y tolerante a espacios extras.
            - Si no se encuentra una coincidencia exacta, se intenta **búsqueda parcial** (contiene).
            
            **Ejemplos válidos:**
            - `?nombre=COLPENSIONES`
            - `?nombre=colpensiones`
            - `?nombre=Porvenir`
            - `?nombre=FONPRECON - Ley 100/93`
            - `?nombre=total afiliados`
            """
    )
    public ResponseEntity<ApiResponse<EntidadResponse>> buscarPorNombre(
            @Parameter(description = "Nombre (o parte del nombre) de la entidad administradora",
                       example = "COLPENSIONES",
                       required = true)
            @RequestParam String nombre) {

        EntidadResponse entidad = pensionesService.buscarPorNombre(nombre);
        return ResponseEntity.ok(
                ApiResponse.ok("Entidad encontrada", entidad));
    }

    // ── GET /api/v1/pensiones/{nombre} ────────────────────────────────────────

    @GetMapping("/{nombre}")
    @Operation(
        summary = "Obtener entidad por nombre (path variable)",
        description = """
            Alternativa al endpoint `/buscar`. Recibe el nombre directamente en la URL.
            
            **Ejemplos:**
            - `/api/v1/pensiones/COLPENSIONES`
            - `/api/v1/pensiones/Porvenir`
            - `/api/v1/pensiones/Protección`
            """
    )
    public ResponseEntity<ApiResponse<EntidadResponse>> obtenerPorNombre(
            @Parameter(description = "Nombre de la entidad administradora", example = "Porvenir")
            @PathVariable String nombre) {

        EntidadResponse entidad = pensionesService.buscarPorNombre(nombre);
        return ResponseEntity.ok(
                ApiResponse.ok("Entidad encontrada", entidad));
    }
}
