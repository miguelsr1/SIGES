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
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumProcesoCreacion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_control_asistencia_cabezal", uniqueConstraints = {
    @UniqueConstraint(name = "cac_fecha_seccion_uk", columnNames = {"cac_fecha", "cac_seccion_fk"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cacPk", scope = SgControlAsistenciaCabezal.class)
public class SgControlAsistenciaCabezal implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cac_pk")
    private Long cacPk;

    @JoinColumn(name = "cac_seccion_fk", referencedColumnName = "sec_pk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion cacSeccion;

    @Column(name = "cac_fecha")
    private LocalDate cacFecha;
    
    @Column(name = "cac_estudiantes_presentes")
    private Integer cacEstudiantesPresentes;

    @Column(name = "cac_estudiantes_en_lista")
    private Integer cacEstudiantesEnLista;

    @Column(name = "cac_estudiantes_ausentes_justificados")
    private Integer cacEstudiantesAusentesJustificados;

    @Column(name = "cac_estudiantes_ausentes_sin_justificar")
    private Integer cacEstudiantesAusentesSinJustificar;

    @Column(name = "cac_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cacUltModFecha;

    @Size(max = 45)
    @Column(name = "cac_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cacUltModUsuario;

    @Column(name = "cac_version")
    @Version
    private Integer cacVersion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asiControl", orphanRemoval = true)
    @NotAudited
    private List<SgAsistencia> cacAsistencia;

    @Column(name = "cac_proceso_de_creacion")
    @Enumerated(value = EnumType.STRING)
    private EnumProcesoCreacion cacProcesoDeCreacion;

    public SgControlAsistenciaCabezal() {
        cacAsistencia = new ArrayList<>();
        cacEstudiantesAusentesJustificados = 0;
        cacEstudiantesAusentesSinJustificar = 0;
    }

    public SgControlAsistenciaCabezal(Long cacPk) {
        this.cacPk = cacPk;
    }

    public Long getCacPk() {
        return cacPk;
    }

    public void setCacPk(Long cacPk) {
        this.cacPk = cacPk;
    }

    public LocalDate getCacFecha() {
        return cacFecha;
    }

    public void setCacFecha(LocalDate cacFecha) {
        this.cacFecha = cacFecha;
    }

    public Integer getCacEstudiantesPresentes() {
        return cacEstudiantesPresentes;
    }

    public void setCacEstudiantesPresentes(Integer cacEstudiantesPresentes) {
        this.cacEstudiantesPresentes = cacEstudiantesPresentes;
    }

    public Integer getCacEstudiantesEnLista() {
        return cacEstudiantesEnLista;
    }

    public void setCacEstudiantesEnLista(Integer cacEstudiantesEnLista) {
        this.cacEstudiantesEnLista = cacEstudiantesEnLista;
    }

    public Integer getCacEstudiantesAusentesJustificados() {
        return cacEstudiantesAusentesJustificados;
    }

    public void setCacEstudiantesAusentesJustificados(Integer cacEstudiantesAusentesJustificados) {
        this.cacEstudiantesAusentesJustificados = cacEstudiantesAusentesJustificados;
    }

    public Integer getCacEstudiantesAusentesSinJustificar() {
        return cacEstudiantesAusentesSinJustificar;
    }

    public void setCacEstudiantesAusentesSinJustificar(Integer cacEstudiantesAusentesSinJustificar) {
        this.cacEstudiantesAusentesSinJustificar = cacEstudiantesAusentesSinJustificar;
    }

    public LocalDateTime getCacUltModFecha() {
        return cacUltModFecha;
    }

    public void setCacUltModFecha(LocalDateTime cacUltModFecha) {
        this.cacUltModFecha = cacUltModFecha;
    }

    public String getCacUltModUsuario() {
        return cacUltModUsuario;
    }

    public void setCacUltModUsuario(String cacUltModUsuario) {
        this.cacUltModUsuario = cacUltModUsuario;
    }

    public Integer getCacVersion() {
        return cacVersion;
    }

    public void setCacVersion(Integer cacVersion) {
        this.cacVersion = cacVersion;
    }

    @Override
    public String securityAmbitCreate() {
        return "cacSeccion";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "cacSeccion.secServicioEducativo.sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secAsociacion.asoPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cacPk != null ? cacPk.hashCode() : 0);
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
        final SgControlAsistenciaCabezal other = (SgControlAsistenciaCabezal) obj;
        if (!Objects.equals(this.cacPk, other.cacPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgControlAsistenciaCabezal{" + "cacPk=" + cacPk + ", cacSeccion=" + cacSeccion + ", cacFecha=" + cacFecha + ", cacEstudiantesPresentes=" + cacEstudiantesPresentes + ", cacEstudiantesEnLista=" + cacEstudiantesEnLista + ", cacEstudiantesAusentesJustificados=" + cacEstudiantesAusentesJustificados + ", cacEstudiantesAusentesSinJustificar=" + cacEstudiantesAusentesSinJustificar + ", cacUltModFecha=" + cacUltModFecha + ", cacUltModUsuario=" + cacUltModUsuario + ", cacVersion=" + cacVersion + ", cacAsistencia=" + cacAsistencia + '}';
    }

    public SgSeccion getCacSeccion() {
        return cacSeccion;
    }

    public void setCacSeccion(SgSeccion cacSeccion) {
        this.cacSeccion = cacSeccion;
    }

    public List<SgAsistencia> getCacAsistencia() {
        return cacAsistencia;
    }

    public void setCacAsistencia(List<SgAsistencia> cacAsistencia) {
        this.cacAsistencia = cacAsistencia;
    }

    public EnumProcesoCreacion getCacProcesoDeCreacion() {
        return cacProcesoDeCreacion;
    }

    public void setCacProcesoDeCreacion(EnumProcesoCreacion cacProcesoDeCreacion) {
        this.cacProcesoDeCreacion = cacProcesoDeCreacion;
    }

}
