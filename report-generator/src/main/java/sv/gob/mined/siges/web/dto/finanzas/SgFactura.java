/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsProveedor;

/**
 *
 * @author Sofis Solutions
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "facPk", scope = SgFactura.class)
public class SgFactura implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long facPk;

    private String facNumero;

    private SgSede facSedeFk;

    private SsProveedor facProveedorFk;

    private SgPago pago;

    private LocalDate facFechaFactura;

    private BigDecimal facSubTotal;

    private BigDecimal facDeducciones;

    private SgMovimientos facItemMovimiento;

    private BigDecimal facTotal;

    private LocalDate facFechaNotaCredito;

    private String facNotaCredito;

    private LocalDateTime facUltModFecha;

    private String facUltModUsuario;

    private Integer facVersion;

    public SgFactura() {
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

    public SgMovimientos getFacItemMovimiento() {
        return facItemMovimiento;
    }

    public void setFacItemMovimiento(SgMovimientos facItemMovimiento) {
        this.facItemMovimiento = facItemMovimiento;
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

    public Integer getFacVersion() {
        return facVersion;
    }

    public void setFacVersion(Integer facVersion) {
        this.facVersion = facVersion;
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

    public SsProveedor getFacProveedorFk() {
        return facProveedorFk;
    }

    public void setFacProveedorFk(SsProveedor facProveedorFk) {
        this.facProveedorFk = facProveedorFk;
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
