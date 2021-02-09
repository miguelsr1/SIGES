
package org.agesic.firma.datatypes;

import java.util.Date;

/**
 * @author Sofis Solutions
 */
public class InfoFirma {
    
    private final int id;
    private final String nombre;
    private final Date fecha;
    private final String dn;
    private final boolean valida;
    private final String mensaje;
    private final String emisor;
    private final byte[] cert;
    private final String serialNumber;

    public InfoFirma(int id, String nombre, Date fecha, String dn, boolean valida, String mensaje, String emisor, byte[] cert, String serialNumber) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.dn = dn;
        this.valida = valida;
        this.mensaje = mensaje;
        this.emisor = emisor;
        this.cert = cert;
        this.serialNumber = serialNumber;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDn() {
        return dn;
    }

    public boolean isValida() {
        return valida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getEmisor() {
        return emisor;
    }

    public byte[] getCert() {
        return cert;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    
}
