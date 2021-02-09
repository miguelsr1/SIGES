/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos de retención de impuestos por proyecto
 *
 * @author rgonzalez
 */
public class DataReporteRetencionDeImpuestosPorProyecto extends DataReporteTemplate{

    private String desde;
    private String hasta;
    private String proyecto;
    private String impuesto;
    private String fuentes;
    private String totalIngresosSujetosDeRetencion;
    private String totalImpuestoRetenido;
    private String totalTotal;
    private String nitDelMinisterio;
    

    private List<DataTablaReporteRetencionDeImpuestosPorProyecto> pagos;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public String getFuentes() {
        return fuentes;
    }

    public void setFuentes(String fuentes) {
        this.fuentes = fuentes;
    }

    public String getTotalIngresosSujetosDeRetencion() {
        return totalIngresosSujetosDeRetencion;
    }

    public void setTotalIngresosSujetosDeRetencion(String totalIngresosSujetosDeRetencion) {
        this.totalIngresosSujetosDeRetencion = totalIngresosSujetosDeRetencion;
    }

    public String getTotalImpuestoRetenido() {
        return totalImpuestoRetenido;
    }

    public void setTotalImpuestoRetenido(String totalImpuestoRetenido) {
        this.totalImpuestoRetenido = totalImpuestoRetenido;
    }

    public String getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(String totalTotal) {
        this.totalTotal = totalTotal;
    }

    public List<DataTablaReporteRetencionDeImpuestosPorProyecto> getPagos() {
        return pagos;
    }

    public void setPagos(List<DataTablaReporteRetencionDeImpuestosPorProyecto> pagos) {
        this.pagos = pagos;
    }

    public String getNitDelMinisterio() {
        return nitDelMinisterio;
    }

    public void setNitDelMinisterio(String nitDelMinisterio) {
        this.nitDelMinisterio = nitDelMinisterio;
    }
    

    // </editor-fold>
}
