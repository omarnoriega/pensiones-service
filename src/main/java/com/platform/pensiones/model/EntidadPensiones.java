package com.platform.pensiones.model;

/**
 * Representa una fila del CSV de afiliados al sistema general de pensiones.
 */
public class EntidadPensiones {

    private String entidadAdministradora;
    private long cotizantes;
    private long noCotizantes;
    private long activos;
    private long inactivos;
    private long total;

    public EntidadPensiones() {}

    public EntidadPensiones(String entidadAdministradora,
                             long cotizantes,
                             long noCotizantes,
                             long activos,
                             long inactivos,
                             long total) {
        this.entidadAdministradora = entidadAdministradora;
        this.cotizantes = cotizantes;
        this.noCotizantes = noCotizantes;
        this.activos = activos;
        this.inactivos = inactivos;
        this.total = total;
    }

    public String getEntidadAdministradora() { return entidadAdministradora; }
    public void setEntidadAdministradora(String entidadAdministradora) {
        this.entidadAdministradora = entidadAdministradora;
    }

    public long getCotizantes() { return cotizantes; }
    public void setCotizantes(long cotizantes) { this.cotizantes = cotizantes; }

    public long getNoCotizantes() { return noCotizantes; }
    public void setNoCotizantes(long noCotizantes) { this.noCotizantes = noCotizantes; }

    public long getActivos() { return activos; }
    public void setActivos(long activos) { this.activos = activos; }

    public long getInactivos() { return inactivos; }
    public void setInactivos(long inactivos) { this.inactivos = inactivos; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
}
