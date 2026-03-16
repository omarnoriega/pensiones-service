package com.platform.pensiones.exception;

/**
 * Excepción lanzada cuando no se encuentra una entidad administradora.
 */
public class EntidadNotFoundException extends RuntimeException {

    public EntidadNotFoundException(String entidad) {
        super("No se encontró la entidad administradora: '" + entidad + "'. " +
              "Use GET /api/v1/pensiones para ver todas las entidades disponibles.");
    }
}
