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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_evaluaciones_liquidaciones_otros", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eliPk", scope = SgEvaluacionLiquidacionOtro.class)
@Audited
public class SgEvaluacionLiquidacionOtro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eli_pk", nullable = false)
    private Long eliPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="eli_liquidacion_otros_fk" , referencedColumnName = "loi_pk", nullable = false)
    private SgLiquidacionOtroIngreso eliLiquidacionOtrosFk;
    
    @Size(max = 50)
    @Column(name = "eli_reembolso_cheque", length = 50)
    private String eliReembolsoCheque;

    @Column(name = "eli_reembolso_monto")
    private BigDecimal eliReembolsoMonto;
    
    @Column(name = "eli_reintegro_monto")
    private BigDecimal eliReintegroMonto;

    @Size(max = 4000)
    @Column(name = "eli_comentario", length = 4000)
    private String eliComentario;


    @Column(name = "eli_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eliUltModFecha;

    @Size(max = 45)
    @Column(name = "eli_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eliUltModUsuario;

    @Column(name = "eli_version")
    @Version
    private Integer eliVersion;

    public SgEvaluacionLiquidacionOtro() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getEliPk() {
        return eliPk;
    }

    public void setEliPk(Long eliPk) {
        this.eliPk = eliPk;
    }

    public SgLiquidacionOtroIngreso getEliLiquidacionOtrosFk() {
        return eliLiquidacionOtrosFk;
    }

    public void setEliLiquidacionOtrosFk(SgLiquidacionOtroIngreso eliLiquidacionOtrosFk) {
        this.eliLiquidacionOtrosFk = eliLiquidacionOtrosFk;
    }

    public String getEliReembolsoCheque() {
        return eliReembolsoCheque;
    }

    public void setEliReembolsoCheque(String eliReembolsoCheque) {
        this.eliReembolsoCheque = eliReembolsoCheque;
    }

    public BigDecimal getEliReembolsoMonto() {
        return eliReembolsoMonto;
    }

    public void setEliReembolsoMonto(BigDecimal eliReembolsoMonto) {
        this.eliReembolsoMonto = eliReembolsoMonto;
    }

    public BigDecimal getEliReintegroMonto() {
        return eliReintegroMonto;
    }

    public void setEliReintegroMonto(BigDecimal eliReintegroMonto) {
        this.eliReintegroMonto = eliReintegroMonto;
    }

    public String getEliComentario() {
        return eliComentario;
    }

    public void setEliComentario(String eliComentario) {
        this.eliComentario = eliComentario;
    }


    public LocalDateTime getEliUltModFecha() {
        return eliUltModFecha;
    }

    public void setEliUltModFecha(LocalDateTime eliUltModFecha) {
        this.eliUltModFecha = eliUltModFecha;
    }

    public String getEliUltModUsuario() {
        return eliUltModUsuario;
    }

    public void setEliUltModUsuario(String eliUltModUsuario) {
        this.eliUltModUsuario = eliUltModUsuario;
    }

    public Integer getEliVersion() {
        return eliVersion;
    }

    public void setEliVersion(Integer eliVersion) {
        this.eliVersion = eliVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.eliPk);
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
        final SgEvaluacionLiquidacionOtro other = (SgEvaluacionLiquidacionOtro) obj;
        if (!Objects.equals(this.eliPk, other.eliPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEvaluacionLiquidacionOtro{" + "eliPk=" + eliPk + ", eliLiquidacionOtrosFk=" + eliLiquidacionOtrosFk + ", eliReembolsoCheque=" + eliReembolsoCheque + ", eliReembolsoMonto=" + eliReembolsoMonto + ", eliReintegroMonto=" + eliReintegroMonto + ", eliComentario=" + eliComentario + ", eliUltModFecha=" + eliUltModFecha + ", eliUltModUsuario=" + eliUltModUsuario + ", eliVersion=" + eliVersion + '}';
    }

    
    
    

}
