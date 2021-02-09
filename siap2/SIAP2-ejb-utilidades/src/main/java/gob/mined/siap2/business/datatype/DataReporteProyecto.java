/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte de proyecto.
 * @author Sofis Solutions
 */
public class DataReporteProyecto extends DataReporteTemplate {

    String nombre;
    String tipo;
    String programaInstitucional = "";
    String programaPresupuestario = "";
    String subprogramaPresupuestario = "";
    String codigoSiip;
    String montoGlobal;
    String objetivo;
    String descripcion;
    String pep;
    String fechaInicio;
    String fechaHasta;
    String correspondePOG;
    List<DataReporteProyectoFinanciacion> financiacion;
    List<DataReporteProyectoIndicador> indicadores;

    List<DataReporteProyectoEstructura> macroactividades;
    List<DataReporteProyectoEstructuraMontoFuente> montosMacroActividades;

    List<DataReporteProyectoEstructura> componentes;
    List<DataReporteProyectoEstructuraMontoFuente> montosComponentes;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(String programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
    }

    public List<DataReporteProyectoEstructura> getMacroactividades() {
        return macroactividades;
    }

    public void setMacroactividades(List<DataReporteProyectoEstructura> macroactividades) {
        this.macroactividades = macroactividades;
    }

    public String getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(String programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public String getSubprogramaPresupuestario() {
        return subprogramaPresupuestario;
    }

    public List<DataReporteProyectoEstructuraMontoFuente> getMontosMacroActividades() {
        return montosMacroActividades;
    }

    public void setMontosMacroActividades(List<DataReporteProyectoEstructuraMontoFuente> montosMacroActividades) {
        this.montosMacroActividades = montosMacroActividades;
    }

    public void setSubprogramaPresupuestario(String subprogramaPresupuestario) {
        this.subprogramaPresupuestario = subprogramaPresupuestario;
    }

    public String getCodigoSiip() {
        return codigoSiip;
    }

    public void setCodigoSiip(String codigoSiip) {
        this.codigoSiip = codigoSiip;
    }

    public List<DataReporteProyectoFinanciacion> getFinanciacion() {
        return financiacion;
    }

    public void setFinanciacion(List<DataReporteProyectoFinanciacion> financiacion) {
        this.financiacion = financiacion;
    }

    public String getMontoGlobal() {
        return montoGlobal;
    }

    public List<DataReporteProyectoEstructura> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<DataReporteProyectoEstructura> componentes) {
        this.componentes = componentes;
    }

    public List<DataReporteProyectoEstructuraMontoFuente> getMontosComponentes() {
        return montosComponentes;
    }

    public void setMontosComponentes(List<DataReporteProyectoEstructuraMontoFuente> montosComponentes) {
        this.montosComponentes = montosComponentes;
    }

    public void setMontoGlobal(String montoGlobal) {
        this.montoGlobal = montoGlobal;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public List<DataReporteProyectoIndicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<DataReporteProyectoIndicador> indicadores) {
        this.indicadores = indicadores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPep() {
        return pep;
    }

    public void setPep(String pep) {
        this.pep = pep;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getCorrespondePOG() {
        return correspondePOG;
    }

    public void setCorrespondePOG(String correspondePOG) {
        this.correspondePOG = correspondePOG;
    }
 // </editor-fold>
}
