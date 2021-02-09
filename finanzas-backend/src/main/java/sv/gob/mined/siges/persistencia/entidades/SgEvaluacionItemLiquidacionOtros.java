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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgItemLiquidacion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_evaluacion_liquidacion_otros_items", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eloPk", scope = SgEvaluacionItemLiquidacionOtros.class)
@Audited
public class SgEvaluacionItemLiquidacionOtros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "elo_pk", nullable = false)
    private Long eloPk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elo_loi_fk", referencedColumnName = "loi_pk")        
    private SgLiquidacionOtroIngreso eloLoiFk;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elo_item_fk", referencedColumnName = "ili_pk")        
    private SgItemLiquidacion eloItemFk;

    @Column(name = "elo_marcado")
    private Boolean eloMarcado;
    
    @Column(name = "elo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eloUltModFecha;

    @Size(max = 45)
    @Column(name = "elo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eloUltModUsuario;

    @Column(name = "elo_version")
    @Version
    private Integer eloVersion;

    public SgEvaluacionItemLiquidacionOtros() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getEloPk() {
        return eloPk;
    }

    public void setEloPk(Long eloPk) {
        this.eloPk = eloPk;
    }

    public SgLiquidacionOtroIngreso getEloLoiFk() {
        return eloLoiFk;
    }

    public void setEloLoiFk(SgLiquidacionOtroIngreso eloLoiFk) {
        this.eloLoiFk = eloLoiFk;
    }

    public SgItemLiquidacion getEloItemFk() {
        return eloItemFk;
    }

    public void setEloItemFk(SgItemLiquidacion eloItemFk) {
        this.eloItemFk = eloItemFk;
    }

    public Boolean getEloMarcado() {
        return eloMarcado;
    }

    public void setEloMarcado(Boolean eloMarcado) {
        this.eloMarcado = eloMarcado;
    }    

    public LocalDateTime getEloUltModFecha() {
        return eloUltModFecha;
    }

    public void setEloUltModFecha(LocalDateTime eloUltModFecha) {
        this.eloUltModFecha = eloUltModFecha;
    }

    public String getEloUltModUsuario() {
        return eloUltModUsuario;
    }

    public void setEloUltModUsuario(String eloUltModUsuario) {
        this.eloUltModUsuario = eloUltModUsuario;
    }

    public Integer getEloVersion() {
        return eloVersion;
    }

    public void setEloVersion(Integer eloVersion) {
        this.eloVersion = eloVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.eloPk);
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
        final SgEvaluacionItemLiquidacionOtros other = (SgEvaluacionItemLiquidacionOtros) obj;
        if (!Objects.equals(this.eloPk, other.eloPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEvaluacionItemLiquidacionOtros{" + "eloPk=" + eloPk + ", eloItemFk=" + eloItemFk + ", eloMarcado=" + eloMarcado + ", eloUltModFecha=" + eloUltModFecha + ", eloUltModUsuario=" + eloUltModUsuario + ", eloVersion=" + eloVersion + '}';
    }

    
    
    

}
