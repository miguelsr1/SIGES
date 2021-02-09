/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rfcPk", scope = SgReqFondoCed.class)
public class SgReqFondoCed implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rfcPk;

    private SgTransferenciaACed rfcTransferenciaCedFk;

    private BigDecimal rfcMonto;

    private Boolean rfcHabilitado;

    private LocalDateTime rfcUltModFecha;

    private String rfcUltModUsuario;

    private Integer rfcVersion;

    private String compromisoPresupuestario;

    public SgReqFondoCed() {
        this.rfcHabilitado = Boolean.TRUE;
    }

    public Long getRfcPk() {
        return rfcPk;
    }

    public void setRfcPk(Long rfcPk) {
        this.rfcPk = rfcPk;
    }

    public SgTransferenciaACed getRfcTransferenciaCedFk() {
        return rfcTransferenciaCedFk;
    }

    public void setRfcTransferenciaCedFk(SgTransferenciaACed rfcTransferenciaCedFk) {
        this.rfcTransferenciaCedFk = rfcTransferenciaCedFk;
    }


    public BigDecimal getRfcMonto() {
        return rfcMonto;
    }

    public void setRfcMonto(BigDecimal rfcMonto) {
        this.rfcMonto = rfcMonto;
    }

    public LocalDateTime getRfcUltModFecha() {
        return rfcUltModFecha;
    }

    public void setRfcUltModFecha(LocalDateTime rfcUltModFecha) {
        this.rfcUltModFecha = rfcUltModFecha;
    }

    public String getRfcUltModUsuario() {
        return rfcUltModUsuario;
    }

    public void setRfcUltModUsuario(String rfcUltModUsuario) {
        this.rfcUltModUsuario = rfcUltModUsuario;
    }

    public Integer getRfcVersion() {
        return rfcVersion;
    }

    public void setRfcVersion(Integer rfcVersion) {
        this.rfcVersion = rfcVersion;
    }

    public String getCompromisoPresupuestario() {
        return compromisoPresupuestario;
    }

    public void setCompromisoPresupuestario(String compromisoPresupuestario) {
        this.compromisoPresupuestario = compromisoPresupuestario;
    }

    public Boolean getRfcHabilitado() {
        return rfcHabilitado;
    }

    public void setRfcHabilitado(Boolean rfcHabilitado) {
        this.rfcHabilitado = rfcHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfcPk != null ? rfcPk.hashCode() : 0);
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
        final SgReqFondoCed other = (SgReqFondoCed) obj;
        if (!Objects.equals(this.rfcPk, other.rfcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgReqFondoCed[ rfcPk=" + rfcPk + " ]";
    }

}
