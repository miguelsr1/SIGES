/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumDesembolsoEstado;

/**
 * Entidad correspondiente a los desembolsos de centros educativos
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_desembolsos_ced", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dcePk", scope = SgDesembolsoCed.class)
@Audited
public class SgDesembolsoCed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dce_pk", nullable = false)
    private Long dcePk;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "dce_det_desembolso_fk", referencedColumnName = "ded_pk")
    private SgDetalleDesembolso dceDetDesembolsoFk;
    
    @JoinColumn(name = "dce_req_fondo_ced_fk", referencedColumnName = "rfc_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgReqFondoCed dceReqFondoCedFk;
    
    @Column(name = "dce_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumDesembolsoEstado dceEstado;
    
    @Column(name = "dce_monto")
    private BigDecimal dceMonto;
    
    @Column(name = "dce_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dceUltModFecha;

    @Size(max = 45)
    @Column(name = "dce_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dceUltModUsuario;

    @Column(name = "dce_version")
    @Version
    private Integer dceVersion;

    public SgDesembolsoCed() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getDcePk() {
        return dcePk;
    }

    public void setDcePk(Long dcePk) {
        this.dcePk = dcePk;
    }

    public SgDetalleDesembolso getDceDetDesembolsoFk() {
        return dceDetDesembolsoFk;
    }

    public void setDceDetDesembolsoFk(SgDetalleDesembolso dceDetDesembolsoFk) {
        this.dceDetDesembolsoFk = dceDetDesembolsoFk;
    }

    public SgReqFondoCed getDceReqFondoCedFk() {
        return dceReqFondoCedFk;
    }

    public void setDceReqFondoCedFk(SgReqFondoCed dceReqFondoCedFk) {
        this.dceReqFondoCedFk = dceReqFondoCedFk;
    }

    public EnumDesembolsoEstado getDceEstado() {
        return dceEstado;
    }

    public void setDceEstado(EnumDesembolsoEstado dceEstado) {
        this.dceEstado = dceEstado;
    }
    
    public BigDecimal getDceMonto() {
        return dceMonto;
    }

    public void setDceMonto(BigDecimal dceMonto) {
        this.dceMonto = dceMonto;
    }

    public LocalDateTime getDceUltModFecha() {
        return dceUltModFecha;
    }

    public void setDceUltModFecha(LocalDateTime dceUltModFecha) {
        this.dceUltModFecha = dceUltModFecha;
    }

    public String getDceUltModUsuario() {
        return dceUltModUsuario;
    }

    public void setDceUltModUsuario(String dceUltModUsuario) {
        this.dceUltModUsuario = dceUltModUsuario;
    }

    public Integer getDceVersion() {
        return dceVersion;
    }

    public void setDceVersion(Integer dceVersion) {
        this.dceVersion = dceVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.dcePk);
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
        final SgDesembolsoCed other = (SgDesembolsoCed) obj;
        if (!Objects.equals(this.dcePk, other.dcePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDesembolsoCed{" + "dcePk=" + dcePk + ", dceDetDesembolsoFk=" + dceDetDesembolsoFk + ", dceEstado=" + dceEstado + ", dceMonto=" + dceMonto + ", dceUltModFecha=" + dceUltModFecha + ", dceUltModUsuario=" + dceUltModUsuario + ", dceVersion=" + dceVersion + '}';
    }

    
    
    

}
