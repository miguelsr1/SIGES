/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * DAtos para el reporte Quedan
 *
 * @author rgonzalez
 */
public class DataReporteQUEDAN extends DataReporteTemplate{

    private String numeroComprobanteRecepcionPago;
    private String nombreProveedor;
    private String nitProveedor;
    private String nroTelefonoProveedor;
    private String agente;
    private String nroContratoOC;
    private List<DataReporteQUEDANFuentes> fuentes;
    private List<DataReporteQUEDANCategorias> categorias;
    private List<DataReporteQuedanFactura> facturas;
    private String fechaProgramadaDeDeposito;
    private String fechaDeEmision;
    private String descripcionItems;
    
    private List<DataReporteQUEDANImpuestos> impuestos;
    private List<DataReporteQUEDANRetenciones> retenciones;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNumeroComprobanteRecepcionPago() {
        return numeroComprobanteRecepcionPago;
    }

    public void setNumeroComprobanteRecepcionPago(String numeroComprobanteRecepcionPago) {
        this.numeroComprobanteRecepcionPago = numeroComprobanteRecepcionPago;
    }

    public List<DataReporteQUEDANImpuestos> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<DataReporteQUEDANImpuestos> impuestos) {
        this.impuestos = impuestos;
    }

    public List<DataReporteQUEDANRetenciones> getRetenciones() {
        return retenciones;
    }

    public void setRetenciones(List<DataReporteQUEDANRetenciones> retenciones) {
        this.retenciones = retenciones;
    }

    
    
    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getNroTelefonoProveedor() {
        return nroTelefonoProveedor;
    }

    public void setNroTelefonoProveedor(String nroTelefonoProveedor) {
        this.nroTelefonoProveedor = nroTelefonoProveedor;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getNroContratoOC() {
        return nroContratoOC;
    }

    public void setNroContratoOC(String nroContratoOC) {
        this.nroContratoOC = nroContratoOC;
    }

    public List<DataReporteQUEDANFuentes> getFuentes() {
        return fuentes;
    }

    public void setFuentes(List<DataReporteQUEDANFuentes> fuentes) {
        this.fuentes = fuentes;
    }

    public List<DataReporteQUEDANCategorias> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<DataReporteQUEDANCategorias> categorias) {
        this.categorias = categorias;
    }

    public String getFechaProgramadaDeDeposito() {
        return fechaProgramadaDeDeposito;
    }

    public void setFechaProgramadaDeDeposito(String fechaProgramadaDeDeposito) {
        this.fechaProgramadaDeDeposito = fechaProgramadaDeDeposito;
    }

    public String getFechaDeEmision() {
        return fechaDeEmision;
    }

    public void setFechaDeEmision(String fechaDeEmision) {
        this.fechaDeEmision = fechaDeEmision;
    }

    public String getDescripcionItems() {
        return descripcionItems;
    }

    public void setDescripcionItems(String descripcionItems) {
        this.descripcionItems = descripcionItems;
    }

    public List<DataReporteQuedanFactura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<DataReporteQuedanFactura> facturas) {
        this.facturas = facturas;
    }

      // </editor-fold>
}
