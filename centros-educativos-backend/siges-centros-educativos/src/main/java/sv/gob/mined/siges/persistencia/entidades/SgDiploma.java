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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_diplomas", schema= Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dilPk", scope = SgDiploma.class)
@Audited
public class SgDiploma implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dil_pk", nullable = false)
    private Long dilPk;
        
    @JoinColumn(name = "dil_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede dilSedeFk;
    
    @JoinColumn(name = "dil_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo dilAnioLectivoFk;
    
    @JoinColumn(name = "dil_diplomado_fk", referencedColumnName = "dip_pk")
    @ManyToOne
    private SgDiplomado dilDiplomadoFk;

    @Column(name = "dil_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dilUltModFecha;

    @Size(max = 45)
    @Column(name = "dil_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dilUltModUsuario;

    @Column(name = "dil_version")
    @Version
    private Integer dilVersion;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "dieDiplomaFk",orphanRemoval = true)
    @NotAudited
    private List<SgDiplomaEstudiante> diplomaEstudiantes;
    
    @JoinColumn(name = "dil_sello_firma_fk")
    @ManyToOne
    private SgSelloFirma dilSelloDirector;
    
    @JoinColumn(name = "dil_sello_firma_titular_ministro_fk")
    @ManyToOne
    private SgSelloFirmaTitular dilSelloMinistro;

    public SgDiploma() {
    }

    public Long getDilPk() {
        return dilPk;
    }

    public void setDilPk(Long dilPk) {
        this.dilPk = dilPk;
    }

    public SgSede getDilSedeFk() {
        return dilSedeFk;
    }

    public void setDilSedeFk(SgSede dilSedeFk) {
        this.dilSedeFk = dilSedeFk;
    }

    public SgAnioLectivo getDilAnioLectivoFk() {
        return dilAnioLectivoFk;
    }

    public void setDilAnioLectivoFk(SgAnioLectivo dilAnioLectivoFk) {
        this.dilAnioLectivoFk = dilAnioLectivoFk;
    }

    public SgDiplomado getDilDiplomadoFk() {
        return dilDiplomadoFk;
    }

    public void setDilDiplomadoFk(SgDiplomado dilDiplomadoFk) {
        this.dilDiplomadoFk = dilDiplomadoFk;
    }

    public LocalDateTime getDilUltModFecha() {
        return dilUltModFecha;
    }

    public void setDilUltModFecha(LocalDateTime dilUltModFecha) {
        this.dilUltModFecha = dilUltModFecha;
    }

    public String getDilUltModUsuario() {
        return dilUltModUsuario;
    }

    public void setDilUltModUsuario(String dilUltModUsuario) {
        this.dilUltModUsuario = dilUltModUsuario;
    }

    public Integer getDilVersion() {
        return dilVersion;
    }

    public void setDilVersion(Integer dilVersion) {
        this.dilVersion = dilVersion;
    }

    public List<SgDiplomaEstudiante> getDiplomaEstudiantes() {
        return diplomaEstudiantes;
    }

    public void setDiplomaEstudiantes(List<SgDiplomaEstudiante> diplomaEstudiantes) {
        this.diplomaEstudiantes = diplomaEstudiantes;
    }

    public SgSelloFirma getDilSelloDirector() {
        return dilSelloDirector;
    }

    public void setDilSelloDirector(SgSelloFirma dilSelloDirector) {
        this.dilSelloDirector = dilSelloDirector;
    }

    public SgSelloFirmaTitular getDilSelloMinistro() {
        return dilSelloMinistro;
    }

    public void setDilSelloMinistro(SgSelloFirmaTitular dilSelloMinistro) {
        this.dilSelloMinistro = dilSelloMinistro;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.dilPk);
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
        final SgDiploma other = (SgDiploma) obj;
        if (!Objects.equals(this.dilPk, other.dilPk)) {
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
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        }else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "dilSedeFk.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedServicioEducativo.sduSeccion.secAsociacion.asoPk", o.getContext());
        }else  if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext());
            //No hacer join con otra tabla. Al hacer joins si la persona tiene muchos ambitos, se forma query con ORs degradando la performance
        
        }else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else  {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dilSedeFk", -1L);
        }
    }

    
    

}
