/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.simple;


public class SgCambioDomicilio{

    Long sedePk;
    String identificacion;
    String numero;
    String nombreCompleto;
    Long zonaPk;
    Long departamentoNuevoPk;
    Long municipioNuevoPk;
    Long cantonNuevoPk;
    String caserioNuevo;
    String direccionNuevo;
    String telefonoNuevo;
    
    public SgCambioDomicilio() {
        this.identificacion = null;
        this.numero = null;
        this.nombreCompleto = null;
        this.zonaPk= null;
        this.departamentoNuevoPk= null;
        this.municipioNuevoPk= null;
        this.cantonNuevoPk= null;
        this.caserioNuevo= null;
        this.direccionNuevo= null;
        this.telefonoNuevo= null;        
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getZonaPk() {
        return zonaPk;
    }

    public void setZonaPk(Long zonaPk) {
        this.zonaPk = zonaPk;
    }

    public Long getDepartamentoNuevoPk() {
        return departamentoNuevoPk;
    }

    public void setDepartamentoNuevoPk(Long departamentoNuevoPk) {
        this.departamentoNuevoPk = departamentoNuevoPk;
    }

    public Long getMunicipioNuevoPk() {
        return municipioNuevoPk;
    }

    public void setMunicipioNuevoPk(Long municipioNuevoPk) {
        this.municipioNuevoPk = municipioNuevoPk;
    }

    public Long getCantonNuevoPk() {
        return cantonNuevoPk;
    }

    public void setCantonNuevoPk(Long cantonNuevoPk) {
        this.cantonNuevoPk = cantonNuevoPk;
    }

    public String getCaserioNuevo() {
        return caserioNuevo;
    }

    public void setCaserioNuevo(String caserioNuevo) {
        this.caserioNuevo = caserioNuevo;
    }

    public String getDireccionNuevo() {
        return direccionNuevo;
    }

    public void setDireccionNuevo(String direccionNuevo) {
        this.direccionNuevo = direccionNuevo;
    }

    public String getTelefonoNuevo() {
        return telefonoNuevo;
    }

    public void setTelefonoNuevo(String telefonoNuevo) {
        this.telefonoNuevo = telefonoNuevo;
    }
    
    
}
