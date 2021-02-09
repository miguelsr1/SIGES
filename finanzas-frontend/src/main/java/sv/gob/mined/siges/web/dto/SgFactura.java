/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.web.beans.FacturaBean;
import sv.gob.mined.siges.web.dto.siap2.SsProveedor;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoDocumentoPago;
import sv.gob.mined.siges.web.enumerados.EnumTipoItemFactura;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "facPk", scope = SgFactura.class)
public class SgFactura implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FacturaBean.class.getName());
    private static final long serialVersionUID = 1L;

    private Long facPk;

    private String facNumero;

    private SgSede facSedeFk;

    private SgPago pago;

    private SsProveedor facProveedorFk;

    private LocalDate facFechaFactura;

    private BigDecimal facSubTotal;

    private BigDecimal facDeducciones;

    private EnumTipoItemFactura facTipoItem;

    private SgPlanCompras facItemPlanCompra;

    private SgMovimientos facItemMovimiento;

    private EnumFacturaEstado facEstado;

    private BigDecimal facTotal;

    private LocalDate facFechaNotaCredito;

    private String facNotaCredito;

    private LocalDateTime facUltModFecha;

    private String facUltModUsuario;

    private Integer facVersion;

    private SgAnioFiscal facAnioFiscalId;

    private EnumTipoDocumentoPago facTipoDocumento;

    public SgFactura() {
    }

    @JsonIgnore
    public String getDatosAnulacionTooltip() {
        if (this.facEstado.equals(EnumFacturaEstado.ANULACION)) {
            return this.facNotaCredito + " - " + this.facFechaNotaCredito;
        }
        return null;
    }

    public String getFacturaComplete() {
        return this.facNumero;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getFacPk() {
        return facPk;
    }

    public void setFacPk(Long facPk) {
        this.facPk = facPk;
    }

    public String getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(String facNumero) {
        this.facNumero = facNumero;
    }

    public LocalDate getFacFechaFactura() {
        return facFechaFactura;
    }

    public void setFacFechaFactura(LocalDate facFechaFactura) {
        this.facFechaFactura = facFechaFactura;
    }

    public BigDecimal getFacSubTotal() {
        return facSubTotal;
    }

    public void setFacSubTotal(BigDecimal facSubTotal) {
        this.facSubTotal = facSubTotal;
    }

    public SgPago getPago() {
        return pago;
    }

    public void setPago(SgPago pago) {
        this.pago = pago;
    }

    public BigDecimal getFacDeducciones() {
        return facDeducciones;
    }

    public void setFacDeducciones(BigDecimal facDeducciones) {
        this.facDeducciones = facDeducciones;
    }

    public EnumTipoItemFactura getFacTipoItem() {
        return facTipoItem;
    }

    public void setFacTipoItem(EnumTipoItemFactura facTipoItem) {
        this.facTipoItem = facTipoItem;
    }

    public SgPlanCompras getFacItemPlanCompra() {
        return facItemPlanCompra;
    }

    public void setFacItemPlanCompra(SgPlanCompras facItemPlanCompra) {
        this.facItemPlanCompra = facItemPlanCompra;
    }

    public SgMovimientos getFacItemMovimiento() {
        return facItemMovimiento;
    }

    public void setFacItemMovimiento(SgMovimientos facItemMovimiento) {
        this.facItemMovimiento = facItemMovimiento;
    }

    public EnumFacturaEstado getFacEstado() {
        return facEstado;
    }

    public void setFacEstado(EnumFacturaEstado facEstado) {
        this.facEstado = facEstado;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public LocalDateTime getFacUltModFecha() {
        return facUltModFecha;
    }

    public void setFacUltModFecha(LocalDateTime facUltModFecha) {
        this.facUltModFecha = facUltModFecha;
    }

    public String getFacUltModUsuario() {
        return facUltModUsuario;
    }

    public void setFacUltModUsuario(String facUltModUsuario) {
        this.facUltModUsuario = facUltModUsuario;
    }

    public SgSede getFacSedeFk() {
        return facSedeFk;
    }

    public void setFacSedeFk(SgSede facSedeFk) {
        this.facSedeFk = facSedeFk;
    }

    public SsProveedor getFacProveedorFk() {
        return facProveedorFk;
    }

    public void setFacProveedorFk(SsProveedor facProveedorFk) {
        this.facProveedorFk = facProveedorFk;
    }

    public Integer getFacVersion() {
        return facVersion;
    }

    public void setFacVersion(Integer facVersion) {
        this.facVersion = facVersion;
    }

    public EnumTipoDocumentoPago getFacTipoDocumento() {
        return facTipoDocumento;
    }

    public void setFacTipoDocumento(EnumTipoDocumentoPago facTipoDocumento) {
        this.facTipoDocumento = facTipoDocumento;
    }

    public LocalDate getFacFechaNotaCredito() {
        return facFechaNotaCredito;
    }

    public void setFacFechaNotaCredito(LocalDate facFechaNotaCredito) {
        this.facFechaNotaCredito = facFechaNotaCredito;
    }

    public String getFacNotaCredito() {
        return facNotaCredito;
    }

    public void setFacNotaCredito(String facNotaCredito) {
        this.facNotaCredito = facNotaCredito;
    }

    public SgAnioFiscal getFacAnioFiscalId() {
        return facAnioFiscalId;
    }

    public void setFacAnioFiscalId(SgAnioFiscal facAnioFiscalId) {
        this.facAnioFiscalId = facAnioFiscalId;
    }

    @PrePersist
    @PreUpdate
    public void calcularTotales() {
        if (facSubTotal != null && facDeducciones != null) {
            facTotal = facSubTotal.add(facDeducciones);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.facPk);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgFactura other = (SgFactura) obj;
        if (!Objects.equals(this.facPk, other.facPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgFactura[ facPk=" + facPk + " ]";
    }
    // </editor-fold>
}
