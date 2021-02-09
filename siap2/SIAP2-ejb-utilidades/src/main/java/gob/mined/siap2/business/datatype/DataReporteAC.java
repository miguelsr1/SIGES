/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos correspondientes al reporte de una Accion Central.
 * @author Sofis Solutions
 */
public class DataReporteAC extends DataReporteTemplate{

    private List<DataReporteANPActividades> actividades;
    private List<DataReporteANPClasificadorFuncional> clasificadores;

    private String metaH = "";
    private String anio1H = "";
    private String anio2H = "";
    private String anio3H = "";
    private String anio4H = "";
    
    private String totalMeta = "";
    private String totalAnio1 = "";
    private String totalAnio2 = "";
    private String totalAnio3 = "";
    private String totalAnio4 = "";
    
    private String fecha="";
    private String usuario="";

    private List<DataReporteANPMontos> montos;

    
    /**
     * Lista de las actividades de la acción central.
     * @return 
     */
    public List<DataReporteANPActividades> getActividades() {
        return actividades;
    }

    /**
     * Lista de las actividades de la acción central.
     * @param actividades 
     */
    public void setActividades(List<DataReporteANPActividades> actividades) {
        this.actividades = actividades;
    }

    /**
     * Lista de los Clasificadores funcionales del gasto.
     * @return 
     */
    public List<DataReporteANPClasificadorFuncional> getClasificadores() {
        return clasificadores;
    }

    /**
     * Lista de los clasificadores funcionales del gasto.
     * @param clasificadores 
     */
    public void setClasificadores(List<DataReporteANPClasificadorFuncional> clasificadores) {
        this.clasificadores = clasificadores;
    }

    /**
     * 
     * @return 
     */
    public String getMetaH() {
        return metaH;
    }

    public void setMetaH(String metaH) {
        this.metaH = metaH;
    }

    public String getAnio1H() {
        return anio1H;
    }

    public void setAnio1H(String anio1H) {
        this.anio1H = anio1H;
    }

    public String getAnio2H() {
        return anio2H;
    }

    public void setAnio2H(String anio2H) {
        this.anio2H = anio2H;
    }

    public String getAnio3H() {
        return anio3H;
    }

    public void setAnio3H(String anio3H) {
        this.anio3H = anio3H;
    }

    public String getAnio4H() {
        return anio4H;
    }

    public void setAnio4H(String anio4H) {
        this.anio4H = anio4H;
    }

    public String getTotalMeta() {
        return totalMeta;
    }

    public void setTotalMeta(String totalMeta) {
        this.totalMeta = totalMeta;
    }

    public String getTotalAnio1() {
        return totalAnio1;
    }

    public void setTotalAnio1(String totalAnio1) {
        this.totalAnio1 = totalAnio1;
    }

    public String getTotalAnio2() {
        return totalAnio2;
    }

    public void setTotalAnio2(String totalAnio2) {
        this.totalAnio2 = totalAnio2;
    }

    public String getTotalAnio3() {
        return totalAnio3;
    }

    public void setTotalAnio3(String totalAnio3) {
        this.totalAnio3 = totalAnio3;
    }

    public String getTotalAnio4() {
        return totalAnio4;
    }

    public void setTotalAnio4(String totalAnio4) {
        this.totalAnio4 = totalAnio4;
    }

    

    public List<DataReporteANPMontos> getMontos() {
        return montos;
    }

    public void setMontos(List<DataReporteANPMontos> montos) {
        this.montos = montos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
    

}
