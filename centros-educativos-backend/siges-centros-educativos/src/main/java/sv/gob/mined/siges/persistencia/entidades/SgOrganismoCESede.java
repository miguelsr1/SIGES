/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgOrganismoCoordinacionEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_organismos_ce_sedes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ocsPk", scope = SgOrganismoCESede.class)
public class SgOrganismoCESede implements Serializable, DataSecurity {
             
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ocs_pk")
    private Long ocsPk;

    @JoinColumn(name = "ocs_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede ocsSede;
    
    @JoinColumn(name = "ocs_org_coordinacion_escolar_fk", referencedColumnName = "oce_pk")
    @ManyToOne
    private SgOrganismoCoordinacionEscolar ocsOrgCoordinacionEscolar;
    
    @Column(name = "ocs_consejo_consultivo")
    private Boolean ocsConsejoConsultivo;
    
    @Column(name = "ocs_funcionando")
    private Boolean ocsFuncionando;
    
    
    
    
    
    @Column(name = "ocs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ocsUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ocs_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ocsUltModUsuario;
    
    @Column(name = "ocs_version")
    @Version
    private Integer ocsVersion;

    public SgOrganismoCESede() {
    }

    public SgOrganismoCESede(Long ocsPk) {
        this.ocsPk = ocsPk;
    }

    public Long getOcsPk() {
        return ocsPk;
    }

    public void setOcsPk(Long ocsPk) {
        this.ocsPk = ocsPk;
    }

    public SgSede getOcsSede() {
        return ocsSede;
    }

    public void setOcsSede(SgSede ocsSede) {
        this.ocsSede = ocsSede;
    }

    public SgOrganismoCoordinacionEscolar getOcsOrgCoordinacionEscolar() {
        return ocsOrgCoordinacionEscolar;
    }

    public void setOcsOrgCoordinacionEscolar(SgOrganismoCoordinacionEscolar ocsOrgCoordinacionEscolar) {
        this.ocsOrgCoordinacionEscolar = ocsOrgCoordinacionEscolar;
    }

    public Boolean getOcsConsejoConsultivo() {
        return ocsConsejoConsultivo;
    }

    public void setOcsConsejoConsultivo(Boolean ocsConsejoConsultivo) {
        this.ocsConsejoConsultivo = ocsConsejoConsultivo;
    }

    public Boolean getOcsFuncionando() {
        return ocsFuncionando;
    }

    public void setOcsFuncionando(Boolean ocsFuncionando) {
        this.ocsFuncionando = ocsFuncionando;
    }

    public LocalDateTime getOcsUltModFecha() {
        return ocsUltModFecha;
    }

    public void setOcsUltModFecha(LocalDateTime ocsUltModFecha) {
        this.ocsUltModFecha = ocsUltModFecha;
    }

    public String getOcsUltModUsuario() {
        return ocsUltModUsuario;
    }

    public void setOcsUltModUsuario(String ocsUltModUsuario) {
        this.ocsUltModUsuario = ocsUltModUsuario;
    }

    public Integer getOcsVersion() {
        return ocsVersion;
    }

    public void setOcsVersion(Integer ocsVersion) {
        this.ocsVersion = ocsVersion;
    }



    @Override
    public String securityAmbitCreate() {
       return "ocsSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ocsSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ocsSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ocsPk", -1L);
        } 
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocsPk != null ? ocsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgOrganismoCESede)) {
            return false;
        }
        SgOrganismoCESede other = (SgOrganismoCESede) object;
        if ((this.ocsPk == null && other.ocsPk != null) || (this.ocsPk != null && !this.ocsPk.equals(other.ocsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgOrganismoCoordinacionEscolar[ ocsPk=" + ocsPk + " ]";
    }
    
}
