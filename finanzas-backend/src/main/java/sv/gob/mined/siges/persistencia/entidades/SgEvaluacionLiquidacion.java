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
 * Entidad correspondiente a las evaluaciones de liquidaciones
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_evaluaciones_liquidaciones",schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "elqPk", scope = SgEvaluacionLiquidacion.class)
@Audited
public class SgEvaluacionLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "elq_pk", nullable = false)
    private Long elqPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="elq_liquidacion_fk" , referencedColumnName = "liq_pk", nullable = false)
    private SgLiquidacion elqLiquidacionFk;
    
    @Size(max = 50)
    @Column(name = "elq_reembolso_cheque", length = 50)
    private String elqReembolsoCheque;

    @Column(name = "elq_reembolso_monto")
    private BigDecimal elqReembolsoMonto;
    
    @Column(name = "elq_reintegro_monto")
    private BigDecimal elqReintegroMonto;

    @Size(max = 4000)
    @Column(name = "elq_comentario", length = 4000)
    private String elqComentario;

    @Column(name = "elq_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime elqUltModFecha;

    @Size(max = 45)
    @Column(name = "elq_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String elqUltModUsuario;

    @Column(name = "elq_version")
    @Version
    private Integer elqVersion;

    public SgEvaluacionLiquidacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getElqPk() {
        return elqPk;
    }

    public void setElqPk(Long elqPk) {
        this.elqPk = elqPk;
    }

    public SgLiquidacion getElqLiquidacionFk() {
        return elqLiquidacionFk;
    }

    public void setElqLiquidacionFk(SgLiquidacion elqLiquidacionFk) {
        this.elqLiquidacionFk = elqLiquidacionFk;
    }

    public String getElqReembolsoCheque() {
        return elqReembolsoCheque;
    }

    public void setElqReembolsoCheque(String elqReembolsoCheque) {
        this.elqReembolsoCheque = elqReembolsoCheque;
    }

    public BigDecimal getElqReembolsoMonto() {
        return elqReembolsoMonto;
    }

    public void setElqReembolsoMonto(BigDecimal elqReembolsoMonto) {
        this.elqReembolsoMonto = elqReembolsoMonto;
    }

    public BigDecimal getElqReintegroMonto() {
        return elqReintegroMonto;
    }

    public void setElqReintegroMonto(BigDecimal elqReintegroMonto) {
        this.elqReintegroMonto = elqReintegroMonto;
    }

    public String getElqComentario() {
        return elqComentario;
    }

    public void setElqComentario(String elqComentario) {
        this.elqComentario = elqComentario;
    }

    

    public LocalDateTime getElqUltModFecha() {
        return elqUltModFecha;
    }

    public void setElqUltModFecha(LocalDateTime elqUltModFecha) {
        this.elqUltModFecha = elqUltModFecha;
    }

    public String getElqUltModUsuario() {
        return elqUltModUsuario;
    }

    public void setElqUltModUsuario(String elqUltModUsuario) {
        this.elqUltModUsuario = elqUltModUsuario;
    }

    public Integer getElqVersion() {
        return elqVersion;
    }

    public void setElqVersion(Integer elqVersion) {
        this.elqVersion = elqVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.elqPk);
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
        final SgEvaluacionLiquidacion other = (SgEvaluacionLiquidacion) obj;
        if (!Objects.equals(this.elqPk, other.elqPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEvaluacionLiquidacion{" + "elqPk=" + elqPk + ", elqReembolsoCheque=" + elqReembolsoCheque + ", elqReembolsoMonto=" + elqReembolsoMonto + ", elqReintegroMonto=" + elqReintegroMonto + ", elqComentario=" + elqComentario + ", elqUltModFecha=" + elqUltModFecha + ", elqUltModUsuario=" + elqUltModUsuario + ", elqVersion=" + elqVersion + '}';
    }

    
    
    

}
