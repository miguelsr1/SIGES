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
 * Entidad correspondiente a la evaluación de items de liquidaciones
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_evaluacion_liquidacion_items", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eilPk", scope = SgEvaluacionItemLiquidacion.class)
@Audited
public class SgEvaluacionItemLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eil_pk", nullable = false)
    private Long eilPk;
    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eil_liq_fk", referencedColumnName = "liq_pk")        
    private SgLiquidacion eilLiqFk;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eil_item_fk", referencedColumnName = "ili_pk")        
    private SgItemLiquidacion eilItemFk;

    @Column(name = "eil_marcado")
    private Boolean eilMarcado;

    @Column(name = "eil_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eilUltModFecha;

    @Size(max = 45)
    @Column(name = "eil_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eilUltModUsuario;

    @Column(name = "eil_version")
    @Version
    private Integer eilVersion;

    public SgEvaluacionItemLiquidacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getEilPk() {
        return eilPk;
    }

    public void setEilPk(Long eilPk) {
        this.eilPk = eilPk;
    }

    public SgLiquidacion getEilLiqFk() {
        return eilLiqFk;
    }

    public void setEilLiqFk(SgLiquidacion eilLiqFk) {
        this.eilLiqFk = eilLiqFk;
    }

    public SgItemLiquidacion getEilItemFk() {
        return eilItemFk;
    }

    public void setEilItemFk(SgItemLiquidacion eilItemFk) {
        this.eilItemFk = eilItemFk;
    }

    public Boolean getEilMarcado() {
        return eilMarcado;
    }

    public void setEilMarcado(Boolean eilMarcado) {
        this.eilMarcado = eilMarcado;
    }


    public LocalDateTime getEilUltModFecha() {
        return eilUltModFecha;
    }

    public void setEilUltModFecha(LocalDateTime eilUltModFecha) {
        this.eilUltModFecha = eilUltModFecha;
    }

    public String getEilUltModUsuario() {
        return eilUltModUsuario;
    }

    public void setEilUltModUsuario(String eilUltModUsuario) {
        this.eilUltModUsuario = eilUltModUsuario;
    }

    public Integer getEilVersion() {
        return eilVersion;
    }

    public void setEilVersion(Integer eilVersion) {
        this.eilVersion = eilVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.eilPk);
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
        final SgEvaluacionItemLiquidacion other = (SgEvaluacionItemLiquidacion) obj;
        if (!Objects.equals(this.eilPk, other.eilPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "SgEvaluacionItemLiquidacion{" + "eilPk=" + eilPk + ", eilLiqFk=" + eilLiqFk + ", eilUltModFecha=" + eilUltModFecha + ", eilUltModUsuario=" + eilUltModUsuario + ", eilVersion=" + eilVersion + '}';
    }
    
    
    

}
