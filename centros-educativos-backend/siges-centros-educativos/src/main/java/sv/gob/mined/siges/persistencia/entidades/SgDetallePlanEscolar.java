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
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgRecurso;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_detalle_plan_escolar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dpePk", scope = SgDetallePlanEscolar.class)
public class SgDetallePlanEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dpe_pk")
    private Long dpePk;

    @JoinColumn(name = "dpe_plan_escolar_anual_fk", referencedColumnName = "pea_pk")
    @ManyToOne
    private SgPlanEscolarAnual dpePlanEscolarAnual;

    @Size(max = 500)
    @Column(name = "dpe_actividad", length = 500)
    private String dpeActividad;

    @Size(max = 255)
    @Column(name = "dpe_responsables", length = 255)
    private String dpeResponsables;

    @JoinColumn(name = "dpe_recursos_fk", referencedColumnName = "rcs_pk")
    @ManyToOne
    private SgRecurso dpeRecursos;

    @Column(name = "dpe_costo_estimado")
    private Double dpeCostoEstimado;

    @JoinColumn(name = "dpe_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgFuenteFinanciamiento dpeFuenteFinanciamiento;

    @Column(name = "dpe_fecha_inicio")
    private LocalDate dpeFechaInicio;

    @Column(name = "dpe_fecha_fin")
    private LocalDate dpeFechaFin;

    @Column(name = "dpe_validado")
    private Boolean dpeValidado;

    @Size(max = 255)
    @Column(name = "dpe_comentario_valido", length = 255)
    private String dpeComentarioValido;

    @Column(name = "dpe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dpeUltModFecha;

    @Size(max = 45)
    @Column(name = "dpe_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dpeUltModUsuario;

    @Column(name = "dpe_version")
    @Version
    private Integer dpeVersion;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "sg_actividad_fotos",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "dpe_pk"),
            inverseJoinColumns = @JoinColumn(name = "ach_pk"))
    @NotAudited
    private List<SgArchivo> dpeFotos;

    @Column(name = "dpe_aplica_sistemas_integrados")
    private Boolean dpeAplicaSistemasIntegrados;

    public SgDetallePlanEscolar() {
    }

    public SgDetallePlanEscolar(Long dpePk) {
        this.dpePk = dpePk;
    }

    public Long getDpePk() {
        return dpePk;
    }

    public void setDpePk(Long dpePk) {
        this.dpePk = dpePk;
    }

    public SgPlanEscolarAnual getDpePlanEscolarAnual() {
        return dpePlanEscolarAnual;
    }

    public void setDpePlanEscolarAnual(SgPlanEscolarAnual dpePlanEscolarAnual) {
        this.dpePlanEscolarAnual = dpePlanEscolarAnual;
    }

    public String getDpeActividad() {
        return dpeActividad;
    }

    public void setDpeActividad(String dpeActividad) {
        this.dpeActividad = dpeActividad;
    }

    public String getDpeResponsables() {
        return dpeResponsables;
    }

    public void setDpeResponsables(String dpeResponsables) {
        this.dpeResponsables = dpeResponsables;
    }

    public SgRecurso getDpeRecursos() {
        return dpeRecursos;
    }

    public void setDpeRecursos(SgRecurso dpeRecursos) {
        this.dpeRecursos = dpeRecursos;
    }

    public Double getDpeCostoEstimado() {
        return dpeCostoEstimado;
    }

    public void setDpeCostoEstimado(Double dpeCostoEstimado) {
        this.dpeCostoEstimado = dpeCostoEstimado;
    }

    public SgFuenteFinanciamiento getDpeFuenteFinanciamiento() {
        return dpeFuenteFinanciamiento;
    }

    public void setDpeFuenteFinanciamiento(SgFuenteFinanciamiento dpeFuenteFinanciamiento) {
        this.dpeFuenteFinanciamiento = dpeFuenteFinanciamiento;
    }

    public LocalDate getDpeFechaInicio() {
        return dpeFechaInicio;
    }

    public void setDpeFechaInicio(LocalDate dpeFechaInicio) {
        this.dpeFechaInicio = dpeFechaInicio;
    }

    public LocalDate getDpeFechaFin() {
        return dpeFechaFin;
    }

    public void setDpeFechaFin(LocalDate dpeFechaFin) {
        this.dpeFechaFin = dpeFechaFin;
    }

    public Boolean getDpeValidado() {
        return dpeValidado;
    }

    public void setDpeValidado(Boolean dpeValidado) {
        this.dpeValidado = dpeValidado;
    }

    public String getDpeComentarioValido() {
        return dpeComentarioValido;
    }

    public void setDpeComentarioValido(String dpeComentarioValido) {
        this.dpeComentarioValido = dpeComentarioValido;
    }

    public LocalDateTime getDpeUltModFecha() {
        return dpeUltModFecha;
    }

    public void setDpeUltModFecha(LocalDateTime dpeUltModFecha) {
        this.dpeUltModFecha = dpeUltModFecha;
    }

    public String getDpeUltModUsuario() {
        return dpeUltModUsuario;
    }

    public void setDpeUltModUsuario(String dpeUltModUsuario) {
        this.dpeUltModUsuario = dpeUltModUsuario;
    }

    public Integer getDpeVersion() {
        return dpeVersion;
    }

    public void setDpeVersion(Integer dpeVersion) {
        this.dpeVersion = dpeVersion;
    }

    public List<SgArchivo> getDpeFotos() {
        return dpeFotos;
    }

    public void setDpeFotos(List<SgArchivo> dpeFotos) {
        this.dpeFotos = dpeFotos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dpePk != null ? dpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDetallePlanEscolar)) {
            return false;
        }
        SgDetallePlanEscolar other = (SgDetallePlanEscolar) object;
        if ((this.dpePk == null && other.dpePk != null) || (this.dpePk != null && !this.dpePk.equals(other.dpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar[ dpePk=" + dpePk + " ]";
    }

    public Boolean getDpeAplicaSistemasIntegrados() {
        return dpeAplicaSistemasIntegrados;
    }

    public void setDpeAplicaSistemasIntegrados(Boolean dpeAplicaSistemasIntegrados) {
        this.dpeAplicaSistemasIntegrados = dpeAplicaSistemasIntegrados;
    }
}
