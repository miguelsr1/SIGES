/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos correspondientes al compromiso presupuestario.
 * @author rgonzalez
 */
public class DataReporteCompromisoPresupuestario extends DataReporteTemplate {
    
    
    private String estado;
    private String noCompromiso;
    private String nroProceso;
    private String fechaEmision;
    private String total;
    private String tipo;//Proceso, Modificativa
    private String nroContrato;
    private String concepto;
    private String convenio;
    private String ejercicioPresupuestario;
    private String organismo;
    
    private List<DataReporteCompromisoPresupuestarioInsumo> insumos;    
    private List<DataFlujoCajaAnioParaReporte> aniosProgramacionFinanciera;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNoCompromiso() {
        return noCompromiso;
    }

    public void setNoCompromiso(String noCompromiso) {
        this.noCompromiso = noCompromiso;
    }

    public String getNroProceso() {
        return nroProceso;
    }

    public void setNroProceso(String nroProceso) {
        this.nroProceso = nroProceso;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List getInsumos() {
        return insumos;
    }

    public void setInsumos(List insumos) {
        this.insumos = insumos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getEjercicioPresupuestario() {
        return ejercicioPresupuestario;
    }

    public void setEjercicioPresupuestario(String ejercicioPresupuestario) {
        this.ejercicioPresupuestario = ejercicioPresupuestario;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public List<DataFlujoCajaAnioParaReporte> getAniosProgramacionFinanciera() {
        return aniosProgramacionFinanciera;
    }

    public void setAniosProgramacionFinanciera(List<DataFlujoCajaAnioParaReporte> aniosProgramacionFinanciera) {
        this.aniosProgramacionFinanciera = aniosProgramacionFinanciera;
    }
    
        
}
