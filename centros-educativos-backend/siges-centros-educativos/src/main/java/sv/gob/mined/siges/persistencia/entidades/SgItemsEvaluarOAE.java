/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgItemEvaluarOrganismo;

@Entity
@Table(name = "sg_oae_items_evaluar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "oaiPk", scope = SgItemsEvaluarOAE.class)
@Audited
public class SgItemsEvaluarOAE implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oai_pk")
    private Long oaiPk;
    
    @JoinColumn(name = "oai_item_fk", referencedColumnName = "ieo_pk")
    @ManyToOne
    private SgItemEvaluarOrganismo oaiItemFk;
    
    @JoinColumn(name = "oai_organismo_fk", referencedColumnName = "oae_pk")
    @ManyToOne
    private SgOrganismoAdministracionEscolar oaiOrganismoFk;
    
    @Column(name = "oai_ult_mod_fecha")
    @AtributoUltimaModificacion 
    private LocalDateTime oaiUltModFecha;
    
    @Size(max = 45)
    @Column(name = "oai_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String oaiUltModUsuario;
    
    @Column(name = "oai_version")
    @Version
    private Integer oaiVersion;
    
    public SgItemsEvaluarOAE() {
    }

    public Long getOaiPk() {
        return oaiPk;
    }

    public void setOaiPk(Long oaiPk) {
        this.oaiPk = oaiPk;
    }

    public SgItemEvaluarOrganismo getOaiItemFk() {
        return oaiItemFk;
    }

    public void setOaiItemFk(SgItemEvaluarOrganismo oaiItemFk) {
        this.oaiItemFk = oaiItemFk;
    }

    public SgOrganismoAdministracionEscolar getOaiOrganismoFk() {
        return oaiOrganismoFk;
    }

    public void setOaiOrganismoFk(SgOrganismoAdministracionEscolar oaiOrganismoFk) {
        this.oaiOrganismoFk = oaiOrganismoFk;
    }

    public LocalDateTime getOaiUltModFecha() {
        return oaiUltModFecha;
    }

    public void setOaiUltModFecha(LocalDateTime oaiUltModFecha) {
        this.oaiUltModFecha = oaiUltModFecha;
    }

    public String getOaiUltModUsuario() {
        return oaiUltModUsuario;
    }

    public void setOaiUltModUsuario(String oaiUltModUsuario) {
        this.oaiUltModUsuario = oaiUltModUsuario;
    }

    public Integer getOaiVersion() {
        return oaiVersion;
    }

    public void setOaiVersion(Integer oaiVersion) {
        this.oaiVersion = oaiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.oaiPk);
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
        final SgItemsEvaluarOAE other = (SgItemsEvaluarOAE) obj;
        if (!Objects.equals(this.oaiPk, other.oaiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgItemsEvaluarOAE{" + "oaiPk=" + oaiPk + ", oaiItemFk=" + oaiItemFk + ", oaiOrganismoFk=" + oaiOrganismoFk + ", oaiUltModFecha=" + oaiUltModFecha + ", oaiUltModUsuario=" + oaiUltModUsuario + ", oaiVersion=" + oaiVersion + '}';
    }
    
}
