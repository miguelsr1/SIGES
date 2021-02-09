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
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_organismo_administracion_escolar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "oaePk", scope = SgOrganismoAdministracionEscolarLite.class)
@Audited
public class SgOrganismoAdministracionEscolarLite implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oae_pk", nullable = false)
    private Long oaePk;

    @Column(name = "oae_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoOrganismoAdministrativo oaeEstado;

    @Column(name = "oae_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime oaeUltModFecha;

    @Size(max = 45)
    @Column(name = "oae_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String oaeUltModUsuario;

    @Column(name = "oae_version")
    @Version
    private Integer oaeVersion;
    
 
    
    public SgOrganismoAdministracionEscolarLite() {
    }

    public Long getOaePk() {
        return oaePk;
    }

    public void setOaePk(Long oaePk) {
        this.oaePk = oaePk;
    }

    public LocalDateTime getOaeUltModFecha() {
        return oaeUltModFecha;
    }

    public void setOaeUltModFecha(LocalDateTime oaeUltModFecha) {
        this.oaeUltModFecha = oaeUltModFecha;
    }

    public String getOaeUltModUsuario() {
        return oaeUltModUsuario;
    }

    public void setOaeUltModUsuario(String oaeUltModUsuario) {
        this.oaeUltModUsuario = oaeUltModUsuario;
    }

    public Integer getOaeVersion() {
        return oaeVersion;
    }

    public void setOaeVersion(Integer oaeVersion) {
        this.oaeVersion = oaeVersion;
    }

    public EnumEstadoOrganismoAdministrativo getOaeEstado() {
        return oaeEstado;
    }

    public void setOaeEstado(EnumEstadoOrganismoAdministrativo oaeEstado) {
        this.oaeEstado = oaeEstado;
    }

    
    @Override
    public String securityAmbitCreate() {
        return "oaeSede";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaePk", -1L);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.oaePk);
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
        final SgOrganismoAdministracionEscolarLite other = (SgOrganismoAdministracionEscolarLite) obj;
        if (!Objects.equals(this.oaePk, other.oaePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOrganismoAdministracionEscolar{" + "oaePk=" + oaePk + ", oaeUltModFecha=" + oaeUltModFecha + ", oaeUltModUsuario=" + oaeUltModUsuario + ", oaeVersion=" + oaeVersion + '}';
    }

}
