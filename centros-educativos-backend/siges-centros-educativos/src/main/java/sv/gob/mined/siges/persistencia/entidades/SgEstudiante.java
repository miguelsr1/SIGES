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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDependenciaEconomica;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_estudiantes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "estPk", scope = SgEstudiante.class)
public class SgEstudiante implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "est_pk")
    private Long estPk;

    @Column(name = "est_dis_km_centro")
    private Double estDisKmCentro;

    @Size(max = 255)
    @Column(name = "est_fac_riesgo", length = 255)
    private String estFacRiesgo;

    @Column(name = "est_embarazo")
    private Boolean estEmbarazo;

    @JoinColumn(name = "est_medio_transporte_fk", referencedColumnName = "mtr_pk")
    @ManyToOne
    private SgMedioTransporte estMedioTransporte;

    @JoinColumn(name = "est_dependencia_economica_fk")
    @ManyToOne
    private SgDependenciaEconomica estDependenciaEconomica;

    @Column(name = "est_habilitado")
    @AtributoHabilitado
    private Boolean estHabilitado;

    @Column(name = "est_realizo_servicio_social")
    private Boolean estRealizoServicioSocial;

    @Column(name = "est_fecha_servicio_social")
    private LocalDate estFechaServicioSocial;

    @Column(name = "est_cantidad_horas_servicio_social")
    private Integer estCantidadHorasServicioSocial;

    @Column(name = "est_descripcion_servicio_social")
    private String estDescripcionServicioSocial;

    @Column(name = "est_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime estUltModFecha;

    @Size(max = 45)
    @Column(name = "est_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String estUltModUsuario;

    @Column(name = "est_version")
    @Version
    private Integer estVersion;

    @JoinColumn(name = "est_persona", updatable = false)
    @OneToOne(optional = false)
    private SgPersona estPersona;

// TODO: no se utiliza. Cuando se utilice, analizar posibilidad de que estudiante tenga ficha y no al revés. De esta forma podemos hacer que la relación sea lazy.
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "fsaEstudiante")
//    private SgFichaSalud estFichaSalud;
    @OneToMany(mappedBy = "mviEstudiante")
    @NotAudited
    private List<SgManifestacionViolencia> estManifestacionesViolencia;

    @OneToMany(mappedBy = "esvEstudiante")
    @NotAudited
    private List<SgEstudianteValoracion> estValoraciones;

    @OneToMany(mappedBy = "caeEstudiante")
    @NotAudited
    private List<SgCalificacionEstudiante> estCalificaciones;

    @OneToMany(mappedBy = "matEstudiante")
    private List<SgMatricula> estMatriculas;

    //Generada con trigger
    @JoinColumn(name = "est_ultima_matricula_fk", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgMatricula estUltimaMatricula;
    
    @Column(name = "est_ultima_encuesta_fk")
    private Long estUltimaEncuesta;

    //Generada con trigger
    @Column(name = "est_ultima_sede_fk", insertable = false, updatable = false)
    private Long estUltimaSedePk;

    //Generada con trigger
    @Column(name = "est_ultima_seccion_fk", insertable = false, updatable = false)
    private Long estUltimaSeccionPk;

    @NotAudited
    @OneToMany(mappedBy = "asiEstudiante")
    private List<SgAsistencia> estAsistencia;
    
    @Column(name = "est_sintoniza_canal_10")
    private Boolean estSintonizaCanal10;
    
    @Column(name = "est_sintoniza_franja_educativa")
    private Boolean estSintonizaFranjaEducativa;
    
    //El boolean esDeTramite es true solo cuando el guardado de estudiante se hace desde 
    //registrofichaescolaridad, se utiliza para dejar afuera ciertas validaciones
    @Transient
    private Boolean estEsDeTramite;

    public SgEstudiante() {
        this.estPersona = new SgPersona();
        this.estHabilitado = Boolean.TRUE;
        this.estRealizoServicioSocial = Boolean.FALSE;
    }

    public SgEstudiante(Long estPk, Integer estVersion) {
        this.estPk = estPk;
        this.estVersion = estVersion;
    }

    public Long getEstPk() {
        return estPk;
    }

    public void setEstPk(Long estPk) {
        this.estPk = estPk;
    }

    public Double getEstDisKmCentro() {
        return estDisKmCentro;
    }

    public void setEstDisKmCentro(Double estDisKmCentro) {
        this.estDisKmCentro = estDisKmCentro;
    }

    public String getEstFacRiesgo() {
        return estFacRiesgo;
    }

    public void setEstFacRiesgo(String estFacRiesgo) {
        this.estFacRiesgo = estFacRiesgo;
    }

    public Boolean getEstEmbarazo() {
        return estEmbarazo;
    }

    public void setEstEmbarazo(Boolean estEmbarazo) {
        this.estEmbarazo = estEmbarazo;
    }

    public SgMedioTransporte getEstMedioTransporte() {
        return estMedioTransporte;
    }

    public void setEstMedioTransporte(SgMedioTransporte estMedioTransporte) {
        this.estMedioTransporte = estMedioTransporte;
    }

    public Boolean getEstHabilitado() {
        return estHabilitado;
    }

    public void setEstHabilitado(Boolean estHabilitado) {
        this.estHabilitado = estHabilitado;
    }

    public LocalDateTime getEstUltModFecha() {
        return estUltModFecha;
    }

    public void setEstUltModFecha(LocalDateTime estUltModFecha) {
        this.estUltModFecha = estUltModFecha;
    }

    public String getEstUltModUsuario() {
        return estUltModUsuario;
    }

    public void setEstUltModUsuario(String estUltModUsuario) {
        this.estUltModUsuario = estUltModUsuario;
    }

    public Integer getEstVersion() {
        return estVersion;
    }

    public void setEstVersion(Integer estVersion) {
        this.estVersion = estVersion;
    }

    public SgPersona getEstPersona() {
        return estPersona;
    }

    public void setEstPersona(SgPersona estPersona) {
        this.estPersona = estPersona;
    }

    public List<SgManifestacionViolencia> getEstManifestacionesViolencia() {
        return estManifestacionesViolencia;
    }

    public void setEstManifestacionesViolencia(List<SgManifestacionViolencia> estManifestacionesViolencia) {
        this.estManifestacionesViolencia = estManifestacionesViolencia;
    }

    public List<SgMatricula> getEstMatriculas() {
        return estMatriculas;
    }

    public void setEstMatriculas(List<SgMatricula> estMatriculas) {
        this.estMatriculas = estMatriculas;
    }

    public SgDependenciaEconomica getEstDependenciaEconomica() {
        return estDependenciaEconomica;
    }

    public void setEstDependenciaEconomica(SgDependenciaEconomica estDependenciaEconomica) {
        this.estDependenciaEconomica = estDependenciaEconomica;
    }

    public List<SgAsistencia> getEstAsistencia() {
        return estAsistencia;
    }

    public void setEstAsistencia(List<SgAsistencia> estAsistencia) {
        this.estAsistencia = estAsistencia;
    }

    public SgMatricula getEstUltimaMatricula() {
        return estUltimaMatricula;
    }

    public void setEstUltimaMatricula(SgMatricula estUltimaMatricula) {
        this.estUltimaMatricula = estUltimaMatricula;
    }

    public Long getEstUltimaSedePk() {
        return estUltimaSedePk;
    }

    public void setEstUltimaSedePk(Long estUltimaSedePk) {
        this.estUltimaSedePk = estUltimaSedePk;
    }

    public List<SgEstudianteValoracion> getEstValoraciones() {
        return estValoraciones;
    }

    public void setEstValoraciones(List<SgEstudianteValoracion> estValoraciones) {
        this.estValoraciones = estValoraciones;
    }

    public List<SgCalificacionEstudiante> getEstCalificaciones() {
        return estCalificaciones;
    }

    public void setEstCalificaciones(List<SgCalificacionEstudiante> estCalificaciones) {
        this.estCalificaciones = estCalificaciones;
    }

    public Long getEstUltimaSeccionPk() {
        return estUltimaSeccionPk;
    }

    public void setEstUltimaSeccionPk(Long estUltimaSeccionPk) {
        this.estUltimaSeccionPk = estUltimaSeccionPk;
    }

    public Boolean getEstRealizoServicioSocial() {
        return estRealizoServicioSocial;
    }

    public void setEstRealizoServicioSocial(Boolean estRealizoServicioSocial) {
        this.estRealizoServicioSocial = estRealizoServicioSocial;
    }

    public LocalDate getEstFechaServicioSocial() {
        return estFechaServicioSocial;
    }

    public void setEstFechaServicioSocial(LocalDate estFechaServicioSocial) {
        this.estFechaServicioSocial = estFechaServicioSocial;
    }

    public Integer getEstCantidadHorasServicioSocial() {
        return estCantidadHorasServicioSocial;
    }

    public void setEstCantidadHorasServicioSocial(Integer estCantidadHorasServicioSocial) {
        this.estCantidadHorasServicioSocial = estCantidadHorasServicioSocial;
    }

    public String getEstDescripcionServicioSocial() {
        return estDescripcionServicioSocial;
    }

    public void setEstDescripcionServicioSocial(String estDescripcionServicioSocial) {
        this.estDescripcionServicioSocial = estDescripcionServicioSocial;
    }

    public Long getEstUltimaEncuesta() {
        return estUltimaEncuesta;
    }

    public void setEstUltimaEncuesta(Long estUltimaEncuesta) {
        this.estUltimaEncuesta = estUltimaEncuesta;
    }

    public Boolean getEstEsDeTramite() {
        return estEsDeTramite;
    }

    public void setEstEsDeTramite(Boolean estEsDeTramite) {
        this.estEsDeTramite = estEsDeTramite;
    }

    public Boolean getEstSintonizaCanal10() {
        return estSintonizaCanal10;
    }

    public void setEstSintonizaCanal10(Boolean estSintonizaCanal10) {
        this.estSintonizaCanal10 = estSintonizaCanal10;
    }

    public Boolean getEstSintonizaFranjaEducativa() {
        return estSintonizaFranjaEducativa;
    }

    public void setEstSintonizaFranjaEducativa(Boolean estSintonizaFranjaEducativa) {
        this.estSintonizaFranjaEducativa = estSintonizaFranjaEducativa;
    }
    
    

    @Override
    public String securityAmbitCreate() {
        return null; //Estudiantes se crean mediante matricula.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secAsociacion.asoPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaSeccionPk", o.getContext());
            //No hacer join con otra tabla. Al hacer joins si la persona tiene muchos ambitos, se forma query con ORs degradando la performance
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estPk != null ? estPk.hashCode() : 0);
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
        final SgEstudiante other = (SgEstudiante) obj;
        if (!Objects.equals(this.estPk, other.estPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudiante[ estPk=" + estPk + " ]";
    }

}
