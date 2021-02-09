/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.datatype;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoContrato;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataContrato {
    private Integer contratoId;
    private Integer nroContrato;       
    private ProcesoAdquisicionItem Item;
    private ProcesoAdquisicionProveedor procesoAdquisicionProveedor;
    private BigDecimal montoAdjudicado;
    private Integer plazoEntrega;
    private Date fechaInicio;
    private Date fechaFin;
    private SsUsuario administrador;
    private Boolean anticipo;
    private Boolean devolucion;
    private Proveedor proveedor;
    private String observaciones;
    private Boolean ordenInicio;
    private TipoContrato tipoContrato;
    private UnidadTecnica unidadTecnica;
    private Boolean impuestoRenta;
    private Boolean IVA;
    private Integer porcentajeAnticipo;
    private Integer porcentajeDevolucion;
    private List<ProcesoAdquisicionItem> itemsProveedor;
    
    
    public DataContrato(){
        impuestoRenta = true;
        IVA = true;
    }

    public Integer getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(Integer nroContrato) {
        this.nroContrato = nroContrato;
    }
    
   

    public ProcesoAdquisicionItem getItem() {
        return Item;
    }

    public void setItem(ProcesoAdquisicionItem Item) {
        this.Item = Item;
    }

    public ProcesoAdquisicionProveedor getProcesoAdquisicionProveedor() {
        return procesoAdquisicionProveedor;
    }

    public void setProcesoAdquisicionProveedor(ProcesoAdquisicionProveedor procesoAdquisicionProveedor) {
        this.procesoAdquisicionProveedor = procesoAdquisicionProveedor;
    }

    public BigDecimal getMontoAdjudicado() {
        return montoAdjudicado;
    }

    public void setMontoAdjudicado(BigDecimal montoAdjudicado) {
        this.montoAdjudicado = montoAdjudicado;
    }

    public Integer getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(Integer plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public Integer getPorcentajeAnticipo() {
        return porcentajeAnticipo;
    }

    public void setPorcentajeAnticipo(Integer porcentajeAnticipo) {
        this.porcentajeAnticipo = porcentajeAnticipo;
    }

    public Integer getPorcentajeDevolucion() {
        return porcentajeDevolucion;
    }

    public void setPorcentajeDevolucion(Integer porcentajeDevolucion) {
        this.porcentajeDevolucion = porcentajeDevolucion;
    }

    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public SsUsuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(SsUsuario administrador) {
        this.administrador = administrador;
    }

    public Boolean getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(Boolean anticipo) {
        this.anticipo = anticipo;
    }

    public Boolean getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Boolean devolucion) {
        this.devolucion = devolucion;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getOrdenInicio() {
        return ordenInicio;
    }

    public void setOrdenInicio(Boolean ordenInicio) {
        this.ordenInicio = ordenInicio;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    
    public Boolean getImpuestoRenta() {
        return impuestoRenta;
    }

    public void setImpuestoRenta(Boolean impuestoRenta) {
        this.impuestoRenta = impuestoRenta;
    }

    public Boolean getIVA() {
        return IVA;
    }

    public void setIVA(Boolean IVA) {
        this.IVA = IVA;
    }

    public List<ProcesoAdquisicionItem> getItemsProveedor() {
        return itemsProveedor;
    }

    public void setItemsProveedor(List<ProcesoAdquisicionItem> itemsProveedor) {
        this.itemsProveedor = itemsProveedor;
    }

    public Integer getContratoId() {
        return contratoId;
    }

    public void setContratoId(Integer contratoId) {
        this.contratoId = contratoId;
    }
    
    
    
    
}
