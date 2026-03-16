package com.platform.pensiones;

import com.platform.pensiones.dto.EntidadResponse;
import com.platform.pensiones.exception.EntidadNotFoundException;
import com.platform.pensiones.service.PensionesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitarios para PensionesService.
 * Usa @BeforeEach para llamar a cargarDatos() directamente
 * sin necesitar Spring context (más rápido).
 */
class PensionesServiceTest {

    private PensionesService service;

    @BeforeEach
    void setUp() {
        service = new PensionesService();
        service.cargarDatos();
    }

    @Test
    @DisplayName("listarTodas: debe retornar todas las entidades del CSV")
    void listarTodas_retornaTodasLasEntidades() {
        List<EntidadResponse> entidades = service.listarTodas();
        // El CSV tiene 15 filas de datos (excluyendo header y filas vacías)
        assertThat(entidades).isNotEmpty();
        assertThat(entidades.size()).isGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("listarNombres: debe retornar lista de nombres no vacía")
    void listarNombres_retornaNombres() {
        List<String> nombres = service.listarNombres();
        assertThat(nombres).isNotEmpty();
        assertThat(nombres).anyMatch(n -> n.contains("COLPENSIONES"));
    }

    @Test
    @DisplayName("buscarPorNombre: búsqueda exacta case-insensitive funciona")
    void buscarPorNombre_exacto_caseInsensitive() {
        EntidadResponse resultado = service.buscarPorNombre("colpensiones");
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEntidadAdministradora()).isEqualTo("COLPENSIONES");
        assertThat(resultado.getCotizantes()).isEqualTo(3_045_937L);
        assertThat(resultado.getTotal()).isEqualTo(7_082_216L);
    }

    @Test
    @DisplayName("buscarPorNombre: búsqueda parcial funciona (contiene)")
    void buscarPorNombre_parcial_funciona() {
        EntidadResponse resultado = service.buscarPorNombre("porvenir");
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEntidadAdministradora()).isEqualToIgnoringCase("Porvenir");
    }

    @Test
    @DisplayName("buscarPorNombre: entidad no encontrada lanza EntidadNotFoundException")
    void buscarPorNombre_noExiste_lanzaExcepcion() {
        assertThatThrownBy(() -> service.buscarPorNombre("ENTIDAD_INEXISTENTE_XYZ"))
                .isInstanceOf(EntidadNotFoundException.class)
                .hasMessageContaining("ENTIDAD_INEXISTENTE_XYZ");
    }

    @Test
    @DisplayName("Datos de COLPENSIONES: cotizantes + noCotizantes = total")
    void colpensiones_sumaCuadra() {
        EntidadResponse e = service.buscarPorNombre("COLPENSIONES");
        assertThat(e.getCotizantes() + e.getNoCotizantes())
                .isEqualTo(e.getTotal());
    }

    @Test
    @DisplayName("Datos de Porvenir: activos + inactivos = total")
    void porvenir_activosMasInactivosIgualTotal() {
        EntidadResponse e = service.buscarPorNombre("Porvenir");
        assertThat(e.getActivos() + e.getInactivos())
                .isEqualTo(e.getTotal());
    }

    @Test
    @DisplayName("Valores formateados tienen separadores de miles")
    void valoresFormateados_tienenSeparadores() {
        EntidadResponse e = service.buscarPorNombre("COLPENSIONES");
        // 3,045,937 debe contener coma
        assertThat(e.getCotizantesFormateado()).contains(",");
        assertThat(e.getTotalFormateado()).contains(",");
    }

    @Test
    @DisplayName("buscarPorNombre: búsqueda con espacios extras funciona")
    void buscarPorNombre_conEspacios() {
        EntidadResponse resultado = service.buscarPorNombre("  CAXDAC  ");
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEntidadAdministradora().trim()).containsIgnoringCase("CAXDAC");
    }
}
