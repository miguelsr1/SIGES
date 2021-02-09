/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de financiación para ser incluidos en el reporte de proyecto.
 * @author Sofis Solutions
 */
public class DataReporteProyectoFinanciacion {
    String fuenteFinanciamiento ="";
    String fuenteRecursos="";
    String convenio="";
    String categoria="";
    String monto="";
    String porcentaje="";
    String financiacion="";

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(String fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public String getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(String fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getFinanciacion() {
        return financiacion;
    }

    public void setFinanciacion(String financiacion) {
        this.financiacion = financiacion;
    }
    
    
    // </editor-fold>
    
    
}
