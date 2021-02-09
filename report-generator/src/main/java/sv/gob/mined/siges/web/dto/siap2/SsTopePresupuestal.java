/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.SgSede;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tpId", scope = SsTransferenciaComponente.class)
public class SsTopePresupuestal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tpId;

    private SsGesPresEs subComponente;

    private SsCuenta subCuenta;

    private SgSede sede;

    private BigDecimal monto;

    private BigDecimal montoAprobado;

    private Integer version;

    public SsTopePresupuestal() {
    }

    public Long getTpId() {
        return tpId;
    }

    public void setTpId(Long tpId) {
        this.tpId = tpId;
    }

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }

    public SsCuenta getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(SsCuenta subCuenta) {
        this.subCuenta = subCuenta;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final SsTopePresupuestal other = (SsTopePresupuestal) obj;
        if (!Objects.equals(this.tpId, other.tpId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsTopePresupuestal{" + "tpId=" + tpId + '}';
    }

}
