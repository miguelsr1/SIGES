/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte del acta de recepción.
 * @author Sofis Solutions
 */
public class DataReporteActaDeRecepcion extends DataReporteTemplate{
    
    private String fechaEmisionStr ;
    private List<DataReporteActaDeRecepcionItem>  items;
    private String  estadoActa;
    private String  nroActa;
    private String  fechaDeRecepcion;
    private String  nroContrato;
    private String  nitProveedor;
    private String  horaDeRecepcion;
    private String  fechaDeContrato;
    private String  lugarDeRecepcion;
    private String  proveedor;
    private String  totalEnLetras;
    private String  total;
    private String  nombreAdminContrato;
    private String  fuentes;
    private String  nombreProyecto;
    private String  montoContrato;
    private String  procesoCompra;
    private String  fecha;

    public String getFechaEmisionStr() {
        return fechaEmisionStr;
    }

    public void setFechaEmisionStr(String fechaEmisionStr) {
        this.fechaEmisionStr = fechaEmisionStr;
    }

    public List<DataReporteActaDeRecepcionItem> getItems() {
        return items;
    }

    public void setItems(List<DataReporteActaDeRecepcionItem> insumos) {
        this.items = insumos;
    }

    public String getEstadoActa() {
        return estadoActa;
    }

    public void setEstadoActa(String estadoActa) {
        this.estadoActa = estadoActa;
    }

    public String getNroActa() {
        return nroActa;
    }

    public void setNroActa(String nroActa) {
        this.nroActa = nroActa;
    }

    public String getFechaDeRecepcion() {
        return fechaDeRecepcion;
    }

    public void setFechaDeRecepcion(String fechaDeRecepcion) {
        this.fechaDeRecepcion = fechaDeRecepcion;
    }

    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getHoraDeRecepcion() {
        return horaDeRecepcion;
    }

    public void setHoraDeRecepcion(String horaDeRecepcion) {
        this.horaDeRecepcion = horaDeRecepcion;
    }

    public String getFechaDeContrato() {
        return fechaDeContrato;
    }

    public void setFechaDeContrato(String fechaDeContrato) {
        this.fechaDeContrato = fechaDeContrato;
    }

    public String getLugarDeRecepcion() {
        return lugarDeRecepcion;
    }

    public void setLugarDeRecepcion(String lugarDeRecepcion) {
        this.lugarDeRecepcion = lugarDeRecepcion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getTotalEnLetras() {
        return totalEnLetras;
    }

    public void setTotalEnLetras(String totalEnLetras) {
        this.totalEnLetras = totalEnLetras;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNombreAdminContrato() {
        return nombreAdminContrato;
    }

    public void setNombreAdminContrato(String nombreAdminContrato) {
        this.nombreAdminContrato = nombreAdminContrato;
    }
    
    public String getFuentes() {
        return fuentes;
    }

    public void setFuentes(String fuentes) {
        this.fuentes = fuentes;
    }
    
    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
    
    public String getMontoContrato() {
        return montoContrato;
    }

    public void setMontoContrato(String montoContrato) {
        this.montoContrato = montoContrato;
    }
    
    public String getProcesoCompra() {
        return procesoCompra;
    }

    public void setProcesoCompra(String procesoCompra) {
        this.procesoCompra = procesoCompra;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}
