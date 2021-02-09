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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumTiposCalificacionDiplomado;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_calificacion_diplomado", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cadPk", scope = SgCalificacionDiplomado.class)
@Audited
public class SgCalificacionDiplomado implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cad_pk", nullable = false)
    private Long cadPk;
    
    @JoinColumn(name = "cad_modulo_diplomado_fk", referencedColumnName = "mdi_pk")
    @ManyToOne
    private SgModuloDiplomado cadModuloDiplomado;
    
    @JoinColumn(name = "cad_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede cadSedeFk;
    
    @JoinColumn(name = "cad_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo cadAnioLectivoFk;
    
    @Column(name = "cad_numero_periodo")
    private Integer cadNumeroPeriodo;
    
    @Column(name = "cad_tipo_calificacion")
    @Enumerated(EnumType.STRING)
    private EnumTiposCalificacionDiplomado cadTipoCalificacion;
   
    @Column(name = "cad_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cadUltModFecha;

    @Size(max = 45)
    @Column(name = "cad_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cadUltModUsuario;

    @Column(name = "cad_version")
    @Version
    private Integer cadVersion;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH}, mappedBy = "cdeCalificacionDiplomadoFk", orphanRemoval = true)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgCalificacionDiplomadoEstudiante> cadCalificacionDiplomadoEstudiantes;

    public SgCalificacionDiplomado() {
    }


    public Long getCadPk() {
        return cadPk;
    }

    public void setCadPk(Long cadPk) {
        this.cadPk = cadPk;
    }


    public LocalDateTime getCadUltModFecha() {
        return cadUltModFecha;
    }

    public void setCadUltModFecha(LocalDateTime cadUltModFecha) {
        this.cadUltModFecha = cadUltModFecha;
    }

    public String getCadUltModUsuario() {
        return cadUltModUsuario;
    }

    public void setCadUltModUsuario(String cadUltModUsuario) {
        this.cadUltModUsuario = cadUltModUsuario;
    }

    public Integer getCadVersion() {
        return cadVersion;
    }

    public void setCadVersion(Integer cadVersion) {
        this.cadVersion = cadVersion;
    }

    public SgModuloDiplomado getCadModuloDiplomado() {
        return cadModuloDiplomado;
    }

    public void setCadModuloDiplomado(SgModuloDiplomado cadModuloDiplomado) {
        this.cadModuloDiplomado = cadModuloDiplomado;
    }

    public Integer getCadNumeroPeriodo() {
        return cadNumeroPeriodo;
    }

    public void setCadNumeroPeriodo(Integer cadNumeroPeriodo) {
        this.cadNumeroPeriodo = cadNumeroPeriodo;
    }

    public EnumTiposCalificacionDiplomado getCadTipoCalificacion() {
        return cadTipoCalificacion;
    }

    public void setCadTipoCalificacion(EnumTiposCalificacionDiplomado cadTipoCalificacion) {
        this.cadTipoCalificacion = cadTipoCalificacion;
    }

    public List<SgCalificacionDiplomadoEstudiante> getCadCalificacionDiplomadoEstudiantes() {
        return cadCalificacionDiplomadoEstudiantes;
    }

    public void setCadCalificacionDiplomadoEstudiantes(List<SgCalificacionDiplomadoEstudiante> cadCalificacionDiplomadoEstudiantes) {
        this.cadCalificacionDiplomadoEstudiantes = cadCalificacionDiplomadoEstudiantes;
    }

    public SgSede getCadSedeFk() {
        return cadSedeFk;
    }

    public void setCadSedeFk(SgSede cadSedeFk) {
        this.cadSedeFk = cadSedeFk;
    }

    public SgAnioLectivo getCadAnioLectivoFk() {
        return cadAnioLectivoFk;
    }

    public void setCadAnioLectivoFk(SgAnioLectivo cadAnioLectivoFk) {
        this.cadAnioLectivoFk = cadAnioLectivoFk;
    }
    
    @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        }else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "cadSedeFk.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedServicioEducativo.sduSeccion.secAsociacion.asoPk", o.getContext());
        } 
        else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext());
            //No hacer join con otra tabla. Al hacer joins si la persona tiene muchos ambitos, se forma query con ORs degradando la performance
        
        }
        else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else  {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calPk", -1L);
        }
    }
    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cadPk);
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
        final SgCalificacionDiplomado other = (SgCalificacionDiplomado) obj;
        if (!Objects.equals(this.cadPk, other.cadPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalificacionDiplomado{" + "cadPk=" + cadPk + ", cadUltModFecha=" + cadUltModFecha + ", cadUltModUsuario=" + cadUltModUsuario + ", cadVersion=" + cadVersion + '}';
    }
    
    

}
