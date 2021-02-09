/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.enumerados.EnumVariableAlerta;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_alertas", schema = Constantes.SCHEMA_ALERTAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "alePk", scope = SgAlerta.class)
public class SgAlerta implements DataSecurity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ale_pk", nullable = false)
    private Long alePk;

    @Column(name = "ale_variable")
    @Enumerated(EnumType.STRING)
    private EnumVariableAlerta aleVariable;

    @Column(name = "ale_riesgo")
    @Enumerated(EnumType.STRING)
    private EnumRiesgoAlerta aleRiesgo;

    @Column(name = "ale_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aleUltModFecha;
    
    @JoinColumn(name = "ale_estudiante_fk")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudiante aleEstudiante;


    public SgAlerta() {
    }

    public Long getAlePk() {
        return alePk;
    }

    public void setAlePk(Long alePk) {
        this.alePk = alePk;
    }

    public EnumVariableAlerta getAleVariable() {
        return aleVariable;
    }

    public void setAleVariable(EnumVariableAlerta aleVariable) {
        this.aleVariable = aleVariable;
    }

    public EnumRiesgoAlerta getAleRiesgo() {
        return aleRiesgo;
    }

    public void setAleRiesgo(EnumRiesgoAlerta aleRiesgo) {
        this.aleRiesgo = aleRiesgo;
    }

    public LocalDateTime getAleUltModFecha() {
        return aleUltModFecha;
    }

    public void setAleUltModFecha(LocalDateTime aleUltModFecha) {
        this.aleUltModFecha = aleUltModFecha;
    }

    public SgEstudiante getAleEstudiante() {
        return aleEstudiante;
    }

    public void setAleEstudiante(SgEstudiante aleEstudiante) {
        this.aleEstudiante = aleEstudiante;
    }
    
    @Override
    public String securityAmbitCreate() {
       return null;
    }
    
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaSeccionPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "alePk", -1L);
        }
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.alePk);
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
        final SgAlerta other = (SgAlerta) obj;
        if (!Objects.equals(this.alePk, other.alePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEstAlerta{" + "alePk=" + alePk + '}';
    }

    

}
