/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.simple;


public class SgCambioNominacion{

    Long sedePk;
    String identificacion;
    String numero;
    String nombreCompleto;
    String nombreAprobado;
    
    
    String telefonoNuevo;
    
    public SgCambioNominacion() {
        this.identificacion = null;
        this.numero = null;
        this.nombreCompleto = null;

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

    public String getNombreAprobado() {
        return nombreAprobado;
    }

    public void setNombreAprobado(String nombreAprobado) {
        this.nombreAprobado = nombreAprobado;
    }
    
    public String getTelefonoNuevo() {
        return telefonoNuevo;
    }

    public void setTelefonoNuevo(String telefonoNuevo) {
        this.telefonoNuevo = telefonoNuevo;
    }
    
    
}
