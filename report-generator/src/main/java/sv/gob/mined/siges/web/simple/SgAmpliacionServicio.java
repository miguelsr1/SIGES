/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.simple;


public class SgAmpliacionServicio{

    String departamento;           
    String municipio;
    String sede;
    String identificacion;
    String numero;
    String nombreCompleto;
    String correoElectronico;
    String telefonoContacto;
    String estadoSolicitud;
    Long sedId;   
    
    public SgAmpliacionServicio() {
        this.departamento = null;           
        this.municipio = null;
        this.sede = null;
        this.identificacion = null;
        this.numero = null;
        this.nombreCompleto = null;
        this.correoElectronico = null;
        this.telefonoContacto = null;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
       this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public Long getSedId() {
        return sedId;
    }

    public void setSedId(Long sedId) {
        this.sedId = sedId;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }    
    
}
