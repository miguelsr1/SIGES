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
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_asignacion_perfil", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "apePk", scope = SgAsignacionPerfil.class)
@Audited
public class SgAsignacionPerfil implements Serializable, DataSecurity  {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ape_pk", nullable = false)
    private Long apePk;

 
    @JoinColumn(name = "ape_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSede apeSedeFk;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "appAsignacionPerfilFk")
    private List<SgAsignacionPerfilPersonal> apeAsignacionesPerfilPersonal;   


    @Column(name = "ape_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime apeUltModFecha;

    @Size(max = 45)
    @Column(name = "ape_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String apeUltModUsuario;

    @Column(name = "ape_version")
    @Version
    private Integer apeVersion;

    public SgAsignacionPerfil() {
    }


    public Long getApePk() {
        return apePk;
    }

    public void setApePk(Long apePk) {
        this.apePk = apePk;
    }

    public SgSede getApeSedeFk() {
        return apeSedeFk;
    }

    public void setApeSedeFk(SgSede apeSedeFk) {
        this.apeSedeFk = apeSedeFk;
    }

    public List<SgAsignacionPerfilPersonal> getApeAsignacionesPerfilPersonal() {
        return apeAsignacionesPerfilPersonal;
    }

    public void setApeAsignacionesPerfilPersonal(List<SgAsignacionPerfilPersonal> apeAsignacionesPerfilPersonal) {
        this.apeAsignacionesPerfilPersonal = apeAsignacionesPerfilPersonal;
    }
   
  
    public LocalDateTime getApeUltModFecha() {
        return apeUltModFecha;
    }

    public void setApeUltModFecha(LocalDateTime apeUltModFecha) {
        this.apeUltModFecha = apeUltModFecha;
    }

    public String getApeUltModUsuario() {
        return apeUltModUsuario;
    }

    public void setApeUltModUsuario(String apeUltModUsuario) {
        this.apeUltModUsuario = apeUltModUsuario;
    }

    public Integer getApeVersion() {
        return apeVersion;
    }

    public void setApeVersion(Integer apeVersion) {
        this.apeVersion = apeVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.apePk);
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
        final SgAsignacionPerfil other = (SgAsignacionPerfil) obj;
        if (!Objects.equals(this.apePk, other.apePk)) {
            return false;
        }
        return true;
    }


     @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcPk", -1L);
        }
    }

}
