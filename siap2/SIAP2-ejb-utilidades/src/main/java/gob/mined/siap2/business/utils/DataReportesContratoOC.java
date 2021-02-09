/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.DataReporteTemplate;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataReportesContratoOC extends DataReporteTemplate{
    private String fechaEmisionStr;
    private String nombreProveedor;
    private String proveedorNIT;
    private List<DataInsumo> insumos;
    private String cifradoPresupuestario;
    private String totalCompra;
    private String plazoDias;
    private String fechaInicio;
    private String fechaFin;
    private String nroContrato;
    private String totalLetras;
    private String total;
    private String subTotal;
    private String observaciones;
    private String firmaRepresentanteMINED;
    private List<DataItem> items;
    private String fuentes;
    private String textoFinal;
    private String nombrePrograma;

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public List<DataInsumo> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<DataInsumo> insumos) {
        this.insumos = insumos;
    }

    public String getCifradoPresupuestario() {
        return cifradoPresupuestario;
    }

    public void setCifradoPresupuestario(String cifradoPresupuestario) {
        this.cifradoPresupuestario = cifradoPresupuestario;
    }

    public String getTotalCompra() {
        return totalCompra;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
    }
    

    public void setTotalCompra(String totalCompra) {
        this.totalCompra = totalCompra;
    }

    public String getFechaEmisionStr() {
        return fechaEmisionStr;
    }

    public void setFechaEmisionStr(String fechaEmisionStr) {
        this.fechaEmisionStr = fechaEmisionStr;
    }

    public String getProveedorNIT() {
        return proveedorNIT;
    }

    public void setProveedorNIT(String proveedorNIT) {
        this.proveedorNIT = proveedorNIT;
    }

    public String getPlazoDias() {
        return plazoDias;
    }

    public void setPlazoDias(String plazoDias) {
        this.plazoDias = plazoDias;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

   
    

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFuentes() {
        return fuentes;
    }

    public void setFuentes(String fuentes) {
        this.fuentes = fuentes;
    }
    

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
    }

    

    public String getTotalLetras() {
        return totalLetras;
    }

    public void setTotalLetras(String totalLetras) {
        this.totalLetras = totalLetras;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFirmaRepresentanteMINED() {
        return firmaRepresentanteMINED;
    }

    public void setFirmaRepresentanteMINED(String firmaRepresentanteMINED) {
        this.firmaRepresentanteMINED = firmaRepresentanteMINED;
    }

    public List<DataItem> getItems() {
        return items;
    }

    public void setItems(List<DataItem> items) {
        this.items = items;
    }
    
    
}
