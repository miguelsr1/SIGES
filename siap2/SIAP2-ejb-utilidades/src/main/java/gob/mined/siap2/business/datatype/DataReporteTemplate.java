/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos del reporte de retención de impuestos por proveedor.
 *
 * @author rgonzalez
 */
public class DataReporteTemplate {

    private String tituloMinisterio;
    private String tituloAreaMinisterio;
    private String tituloNombreReporte;
    private String fechaHoraImpresionReporte;
    private String usuarioImprimeReporte;
    private String aclaracionMonedaMontos;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getTituloMinisterio() {
        return tituloMinisterio;
    }

    public void setTituloMinisterio(String tituloMinisterio) {
        this.tituloMinisterio = tituloMinisterio;
    }

    public String getTituloAreaMinisterio() {
        return tituloAreaMinisterio;
    }

    public void setTituloAreaMinisterio(String tituloAreaMinisterio) {
        this.tituloAreaMinisterio = tituloAreaMinisterio;
    }

    public String getTituloNombreReporte() {
        return tituloNombreReporte;
    }

    public void setTituloNombreReporte(String tituloNombreReporte) {
        this.tituloNombreReporte = tituloNombreReporte;
    }

    public String getFechaHoraImpresionReporte() {
        return fechaHoraImpresionReporte;
    }

    public void setFechaHoraImpresionReporte(String fechaHoraImpresionReporte) {
        this.fechaHoraImpresionReporte = fechaHoraImpresionReporte;
    }

    public String getUsuarioImprimeReporte() {
        return usuarioImprimeReporte;
    }

    public void setUsuarioImprimeReporte(String usuarioImprimeReporte) {
        this.usuarioImprimeReporte = usuarioImprimeReporte;
    }

    public String getAclaracionMonedaMontos() {
        return aclaracionMonedaMontos;
    }

    public void setAclaracionMonedaMontos(String aclaracionMonedaMontos) {
        this.aclaracionMonedaMontos = aclaracionMonedaMontos;
    }    
    
    // </editor-fold>
        
}
