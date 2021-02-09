/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos del reporte de reprogramación.
 *
 * @author Eduardo
 */
public class DataReporteReprogramacion extends DataReporteTemplate{

    private String tipoPOA;
    private String nombre;
    private String unidadEjecutora;
    private String fechaReprogramacion;
    private String numeroReprogramacion;
    private String estado;
    private List<DataReporteReprogramacionInsumos> listaInsumos;
    private String total;
    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 

    public String getTipoPOA() {
        return tipoPOA;
    }

    public void setTipoPOA(String tipoPOA) {
        this.tipoPOA = tipoPOA;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadEjecutora() {
        return unidadEjecutora;
    }

    public void setUnidadEjecutora(String unidadEjecutora) {
        this.unidadEjecutora = unidadEjecutora;
    }

    public String getFechaReprogramacion() {
        return fechaReprogramacion;
    }

    public void setFechaReprogramacion(String fechaReprogramacion) {
        this.fechaReprogramacion = fechaReprogramacion;
    }

    public String getNumeroReprogramacion() {
        return numeroReprogramacion;
    }

    public void setNumeroReprogramacion(String numeroReprogramacion) {
        this.numeroReprogramacion = numeroReprogramacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DataReporteReprogramacionInsumos> getListaInsumos() {
        return listaInsumos;
    }

    public void setListaInsumos(List<DataReporteReprogramacionInsumos> listaInsumos) {
        this.listaInsumos = listaInsumos;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

      // </editor-fold>
}
