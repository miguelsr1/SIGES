/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_diplomados_estudiante", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "depPk", scope = SgDiplomadosEstudiante.class)
@Audited
public class SgDiplomadosEstudiante implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dep_pk", nullable = false)
    private Long depPk;
    
    @JoinColumn(name = "dep_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo depAnioLectivo;
    
    @JoinColumn(name = "dep_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante depEstudiante;
    
    @JoinColumn(name = "dep_diplomado_fk", referencedColumnName = "dip_pk")
    @ManyToOne
    private SgDiplomado depDiplomado;
    
    @JoinColumn(name = "dep_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede depSedeFk;
    
    @Column(name = "dep_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime depUltModFecha;

    @Size(max = 45)
    @Column(name = "dep_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String depUltModUsuario;

    @Column(name = "dep_version")
    @Version
    private Integer depVersion;
    
    @Transient
    private Long depNieSeleccionado;

    public Long getDepPk() {
        return depPk;
    }

    public void setDepPk(Long depPk) {
        this.depPk = depPk;
    }

    public SgAnioLectivo getDepAnioLectivo() {
        return depAnioLectivo;
    }

    public void setDepAnioLectivo(SgAnioLectivo depAnioLectivo) {
        this.depAnioLectivo = depAnioLectivo;
    }

    public SgEstudiante getDepEstudiante() {
        return depEstudiante;
    }

    public void setDepEstudiante(SgEstudiante depEstudiante) {
        this.depEstudiante = depEstudiante;
    }

    public SgDiplomado getDepDiplomado() {
        return depDiplomado;
    }

    public void setDepDiplomado(SgDiplomado depDiplomado) {
        this.depDiplomado = depDiplomado;
    }

    public LocalDateTime getDepUltModFecha() {
        return depUltModFecha;
    }

    public void setDepUltModFecha(LocalDateTime depUltModFecha) {
        this.depUltModFecha = depUltModFecha;
    }

    public String getDepUltModUsuario() {
        return depUltModUsuario;
    }

    public void setDepUltModUsuario(String depUltModUsuario) {
        this.depUltModUsuario = depUltModUsuario;
    }

    public Integer getDepVersion() {
        return depVersion;
    }

    public void setDepVersion(Integer depVersion) {
        this.depVersion = depVersion;
    }

    public SgSede getDepSedeFk() {
        return depSedeFk;
    }

    public void setDepSedeFk(SgSede depSedeFk) {
        this.depSedeFk = depSedeFk;
    }

    public Long getDepNieSeleccionado() {
        return depNieSeleccionado;
    }

    public void setDepNieSeleccionado(Long depNieSeleccionado) {
        this.depNieSeleccionado = depNieSeleccionado;
    }
    
    @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        }else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "depSedeFk.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedServicioEducativo.sduSeccion.secAsociacion.asoPk", o.getContext());
        }else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depEstudiante.estPersona.perPk", o.getContext());
        }  
        else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext());
            //No hacer join con otra tabla. Al hacer joins si la persona tiene muchos ambitos, se forma query con ORs degradando la performance
        
        }else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else  {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.depPk);
        hash = 89 * hash + Objects.hashCode(this.depAnioLectivo);
        hash = 89 * hash + Objects.hashCode(this.depEstudiante);
        hash = 89 * hash + Objects.hashCode(this.depDiplomado);
        hash = 89 * hash + Objects.hashCode(this.depUltModFecha);
        hash = 89 * hash + Objects.hashCode(this.depUltModUsuario);
        hash = 89 * hash + Objects.hashCode(this.depVersion);
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
        final SgDiplomadosEstudiante other = (SgDiplomadosEstudiante) obj;
        if (!Objects.equals(this.depPk, other.depPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDiplomadosEstudiante{" + "depPk=" + depPk + '}';
    }
}
