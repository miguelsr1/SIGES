/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte de disponibilidad presupuestaria
 *
 * @author Sofis Solutions
 */
public class DataReporteDisponibilidadPresupuestariaProceso extends DataReporteTemplate{

    private List<DataReporteDisponibilidadPresupuestaria> listaDisponibilidadPresupuestariaMontosFuenteInsumo;
    private String totalPresupuesto;    
    private String numero;
    private String fechaEmision;
    private String fechaAprobacion;
    private String estado;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public List<DataReporteDisponibilidadPresupuestaria> getListaDisponibilidadPresupuestariaMontosFuenteInsumo() {
        return listaDisponibilidadPresupuestariaMontosFuenteInsumo;
    }
    
    public void setListaDisponibilidadPresupuestariaMontosFuenteInsumo(List<DataReporteDisponibilidadPresupuestaria> listaDisponibilidadPresupuestariaMontosFuenteInsumo) {
        this.listaDisponibilidadPresupuestariaMontosFuenteInsumo = listaDisponibilidadPresupuestariaMontosFuenteInsumo;
    }
    
    public String getTotalPresupuesto() {
        return totalPresupuesto;
    }

    public void setTotalPresupuesto(String totalPresupuesto) {
        this.totalPresupuesto = totalPresupuesto;
    }
    
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // </editor-fold>
}
