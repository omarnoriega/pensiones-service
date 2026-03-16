package com.platform.pensiones.dto;

/**
 * DTO de salida para los datos de una entidad administradora de pensiones.
 * Incluye los valores numéricos y sus representaciones formateadas.
 */
public class EntidadResponse {

    private String entidadAdministradora;
    private long cotizantes;
    private long noCotizantes;
    private long activos;
    private long inactivos;
    private long total;

    // Valores formateados con separador de miles para mejor lectura
    private String cotizantesFormateado;
    private String noCotizantesFormateado;
    private String activosFormateado;
    private String inactivosFormateado;
    private String totalFormateado;

    public EntidadResponse() {}

    public EntidadResponse(String entidadAdministradora,
                            long cotizantes,
                            long noCotizantes,
                            long activos,
                            long inactivos,
                            long total) {
        this.entidadAdministradora = entidadAdministradora;
        this.cotizantes   = cotizantes;
        this.noCotizantes = noCotizantes;
        this.activos      = activos;
        this.inactivos    = inactivos;
        this.total        = total;

        this.cotizantesFormateado   = String.format("%,d", cotizantes);
        this.noCotizantesFormateado = String.format("%,d", noCotizantes);
        this.activosFormateado      = String.format("%,d", activos);
        this.inactivosFormateado    = String.format("%,d", inactivos);
        this.totalFormateado        = String.format("%,d", total);
    }

    public String getEntidadAdministradora()  { return entidadAdministradora; }
    public long getCotizantes()               { return cotizantes; }
    public long getNoCotizantes()             { return noCotizantes; }
    public long getActivos()                  { return activos; }
    public long getInactivos()                { return inactivos; }
    public long getTotal()                    { return total; }
    public String getCotizantesFormateado()   { return cotizantesFormateado; }
    public String getNoCotizantesFormateado() { return noCotizantesFormateado; }
    public String getActivosFormateado()      { return activosFormateado; }
    public String getInactivosFormateado()    { return inactivosFormateado; }
    public String getTotalFormateado()        { return totalFormateado; }
}
