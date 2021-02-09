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
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_solicitudes_impresion",  schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "simPk", scope = SgSolicitudImpresionLite.class)
@Audited
public class SgSolicitudImpresionLite implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sim_pk", nullable = false)
    private Long simPk;
    
    @Column(name = "sim_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSolicitudImpresion simEstado;

    @Column(name = "sim_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime simUltModFecha;

    @Size(max = 45)
    @Column(name = "sim_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String simUltModUsuario;

    @Column(name = "sim_version")
    @Version
    private Integer simVersion;
    


    public SgSolicitudImpresionLite(Long simPk, Integer simVersion) {
        this.simPk = simPk;
        this.simVersion = simVersion;
    }
        
    public SgSolicitudImpresionLite() {
    }

   

    public Long getSimPk() {
        return simPk;
    }

    public void setSimPk(Long simPk) {
        this.simPk = simPk;
    }

    public EnumEstadoSolicitudImpresion getSimEstado() {
        return simEstado;
    }

    public void setSimEstado(EnumEstadoSolicitudImpresion simEstado) {
        this.simEstado = simEstado;
    }
    public LocalDateTime getSimUltModFecha() {
        return simUltModFecha;
    }

    public void setSimUltModFecha(LocalDateTime simUltModFecha) {
        this.simUltModFecha = simUltModFecha;
    }

    public String getSimUltModUsuario() {
        return simUltModUsuario;
    }

    public void setSimUltModUsuario(String simUltModUsuario) {
        this.simUltModUsuario = simUltModUsuario;
    }

    public Integer getSimVersion() {
        return simVersion;
    }

    public void setSimVersion(Integer simVersion) {
        this.simVersion = simVersion;
    }


    @Override
    public String securityAmbitCreate() {
        return null; //TODO: para permitir crear adscriptas en un departamento distinto al de la sede padre, hay que modificar el DAO
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secPk", -1L);
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.simPk);
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
        final SgSolicitudImpresionLite other = (SgSolicitudImpresionLite) obj;
        if (!Objects.equals(this.simPk, other.simPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSolicitudImpresion{" + "simPk=" + simPk + ", simUltModFecha=" + simUltModFecha + ", simUltModUsuario=" + simUltModUsuario + ", simVersion=" + simVersion + '}';
    }
    
    

}
