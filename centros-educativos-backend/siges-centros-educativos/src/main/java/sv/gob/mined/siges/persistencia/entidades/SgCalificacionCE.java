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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_calificaciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCalificacionCE.class)
@Audited
public class SgCalificacionCE implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cal_pk", nullable = false)
    private Long calPk;

    @JoinColumn(name = "cal_componente_plan_estudio_fk", referencedColumnName = "cpe_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgComponentePlanEstudio calComponentePlanEstudio; //Puede ser null si periodo GRA

    @JoinColumn(name = "cal_rango_fecha_fk", referencedColumnName = "rfe_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgRangoFecha calRangoFecha;

    @JoinColumn(name = "cal_seccion_fk", referencedColumnName = "sec_pk")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion calSeccion;

    @Column(name = "cal_fecha")
    private LocalDate calFecha;

    @Column(name = "cal_tipo_periodo_calificacion")
    @Enumerated(EnumType.STRING)
    private EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion;

    @Column(name = "cal_tipo_calendario_escolar")
    @Enumerated(EnumType.STRING)
    private EnumCalendarioEscolar calTipoCalendarioEscolar;

    @Column(name = "cal_numero")
    private Integer calNumero;

    @Column(name = "cal_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime calUltModFecha;

    @Size(max = 45)
    @Column(name = "cal_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String calUltModUsuario;

    @Column(name = "cal_version")
    @Version
    private Integer calVersion;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "caeCalificacion", orphanRemoval = true)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgCalificacionEstudiante> calCalificacionesEstudiante;

    @Column(name = "cal_cerrado")
    private Boolean calCerrado;

    @Column(name = "cal_cant_estudiantes_calificados")
    private Integer calCantEstudiantesCalificados;

    @Column(name = "cal_promedio_calificaciones")
    private Double calPromedioCalificaciones; //Promedio cuando las calificaciones son numericas

    @Column(name = "cal_promedio_calificaciones_masculino")
    private Double calPromedioCalificacionesMasculino; //Promedio masculino cuando las calificaciones son numericas

    @Column(name = "cal_promedio_calificaciones_femenino")
    private Double calPromedioCalificacionesFemenino; //Promedio femenino cuando las calificaciones son numericas
    
    @Column(name = "cal_promedio_moda_desactualizados")
    private Boolean calPromedioModaDesactualizados;

    @JoinColumn(name = "cal_moda_calificaciones_conceptuales")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgCalificacion calModaCalificacionesConceptuales; //moda cuando las calificaciones son conceptuales

    @JoinColumn(name = "cal_moda_calificaciones_conceptuales_masculino")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgCalificacion calModaCalificacionesConceptualesMasculino; //moda masculino cuando las calificaciones son conceptuales

    @JoinColumn(name = "cal_moda_calificaciones_conceptuales_femenino")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgCalificacion calModaCalificacionesConceptualesFemenino; //moda femenino cuando las calificaciones son conceptuales

    @Column(name = "cal_estado_procesamiento_promocion")
    @Enumerated(EnumType.STRING)
    private EnumEstadoProcesamientoCalificacionPromocion calEstadoProcesamientoPromocion;

    @JoinColumn(name = "cal_info_procesamiento_calificacion_fk", referencedColumnName = "ipc_pk")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgInfoProcesamientoCalificacion calInfoProcesamientoCalificacionFk;
     
    @Column(name = "cal_todas_promociones_grado_realizadas")
    private Boolean calTodasPromocionesGradoRealizadas; //Utilizado solamente para cabezales de tipo GRA. En true si todas son PROMOVIDAS o NO_PROMOVIDAS. En false si tiene alguna null o PENDIENTE.
   
    @Transient
    private Boolean calTieneEstudiantesPendientes; //TODO: no utilizar mas. Utilizar calTodasPromocionesGradoRealizadas

    public SgCalificacionCE() {
        this.calCalificacionesEstudiante = new ArrayList<>();
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public SgComponentePlanEstudio getCalComponentePlanEstudio() {
        return calComponentePlanEstudio;
    }

    public void setCalComponentePlanEstudio(SgComponentePlanEstudio calComponentePlanEstudio) {
        this.calComponentePlanEstudio = calComponentePlanEstudio;
    }

    public SgSeccion getCalSeccion() {
        return calSeccion;
    }

    public void setCalSeccion(SgSeccion calSeccion) {
        this.calSeccion = calSeccion;
    }

    public SgRangoFecha getCalRangoFecha() {
        return calRangoFecha;
    }

    public void setCalRangoFecha(SgRangoFecha calRangoFecha) {
        this.calRangoFecha = calRangoFecha;
    }

    public LocalDate getCalFecha() {
        return calFecha;
    }

    public void setCalFecha(LocalDate calFecha) {
        this.calFecha = calFecha;
    }

    public LocalDateTime getCalUltModFecha() {
        return calUltModFecha;
    }

    public void setCalUltModFecha(LocalDateTime calUltModFecha) {
        this.calUltModFecha = calUltModFecha;
    }

    public String getCalUltModUsuario() {
        return calUltModUsuario;
    }

    public void setCalUltModUsuario(String calUltModUsuario) {
        this.calUltModUsuario = calUltModUsuario;
    }

    public Integer getCalVersion() {
        return calVersion;
    }

    public void setCalVersion(Integer calVersion) {
        this.calVersion = calVersion;
    }

    public List<SgCalificacionEstudiante> getCalCalificacionesEstudiante() {
        return calCalificacionesEstudiante;
    }

    public void setCalCalificacionesEstudiante(List<SgCalificacionEstudiante> calCalificacionesEstudiante) {
        this.calCalificacionesEstudiante = calCalificacionesEstudiante;
    }

    public EnumTiposPeriodosCalificaciones getCalTipoPeriodoCalificacion() {
        return calTipoPeriodoCalificacion;
    }

    public void setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion) {
        this.calTipoPeriodoCalificacion = calTipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getCalTipoCalendarioEscolar() {
        return calTipoCalendarioEscolar;
    }

    public void setCalTipoCalendarioEscolar(EnumCalendarioEscolar calTipoCalendarioEscolar) {
        this.calTipoCalendarioEscolar = calTipoCalendarioEscolar;
    }

    public Integer getCalNumero() {
        return calNumero;
    }

    public void setCalNumero(Integer calNumero) {
        this.calNumero = calNumero;
    }

    public Boolean getCalCerrado() {
        return calCerrado;
    }

    public void setCalCerrado(Boolean calCerrado) {
        this.calCerrado = calCerrado;
    }

    public Integer getCalCantEstudiantesCalificados() {
        return calCantEstudiantesCalificados;
    }

    public void setCalCantEstudiantesCalificados(Integer calCantEstudiantesCalificados) {
        this.calCantEstudiantesCalificados = calCantEstudiantesCalificados;
    }

    public Double getCalPromedioCalificaciones() {
        return calPromedioCalificaciones;
    }

    public void setCalPromedioCalificaciones(Double calPromedioCalificaciones) {
        this.calPromedioCalificaciones = calPromedioCalificaciones;
    }

    public EnumEstadoProcesamientoCalificacionPromocion getCalEstadoProcesamientoPromocion() {
        return calEstadoProcesamientoPromocion;
    }

    public void setCalEstadoProcesamientoPromocion(EnumEstadoProcesamientoCalificacionPromocion calEstadoProcesamientoPromocion) {
        this.calEstadoProcesamientoPromocion = calEstadoProcesamientoPromocion;
    }

    public Boolean getCalTieneEstudiantesPendientes() {
        return calTieneEstudiantesPendientes;
    }

    public void setCalTieneEstudiantesPendientes(Boolean calTieneEstudiantesPendientes) {
        this.calTieneEstudiantesPendientes = calTieneEstudiantesPendientes;
    }

    public SgInfoProcesamientoCalificacion getCalInfoProcesamientoCalificacionFk() {
        return calInfoProcesamientoCalificacionFk;
    }

    public void setCalInfoProcesamientoCalificacionFk(SgInfoProcesamientoCalificacion calInfoProcesamientoCalificacionFk) {
        this.calInfoProcesamientoCalificacionFk = calInfoProcesamientoCalificacionFk;
    }

    public SgCalificacion getCalModaCalificacionesConceptuales() {
        return calModaCalificacionesConceptuales;
    }

    public void setCalModaCalificacionesConceptuales(SgCalificacion calModaCalificacionesConceptuales) {
        this.calModaCalificacionesConceptuales = calModaCalificacionesConceptuales;
    }

    public Double getCalPromedioCalificacionesMasculino() {
        return calPromedioCalificacionesMasculino;
    }

    public void setCalPromedioCalificacionesMasculino(Double calPromedioCalificacionesMasculino) {
        this.calPromedioCalificacionesMasculino = calPromedioCalificacionesMasculino;
    }

    public Double getCalPromedioCalificacionesFemenino() {
        return calPromedioCalificacionesFemenino;
    }

    public void setCalPromedioCalificacionesFemenino(Double calPromedioCalificacionesFemenino) {
        this.calPromedioCalificacionesFemenino = calPromedioCalificacionesFemenino;
    }

    public SgCalificacion getCalModaCalificacionesConceptualesMasculino() {
        return calModaCalificacionesConceptualesMasculino;
    }

    public void setCalModaCalificacionesConceptualesMasculino(SgCalificacion calModaCalificacionesConceptualesMasculino) {
        this.calModaCalificacionesConceptualesMasculino = calModaCalificacionesConceptualesMasculino;
    }

    public SgCalificacion getCalModaCalificacionesConceptualesFemenino() {
        return calModaCalificacionesConceptualesFemenino;
    }

    public void setCalModaCalificacionesConceptualesFemenino(SgCalificacion calModaCalificacionesConceptualesFemenino) {
        this.calModaCalificacionesConceptualesFemenino = calModaCalificacionesConceptualesFemenino;
    }

    public Boolean getCalPromedioModaDesactualizados() {
        return calPromedioModaDesactualizados;
    }

    public void setCalPromedioModaDesactualizados(Boolean calPromedioModaDesactualizados) {
        this.calPromedioModaDesactualizados = calPromedioModaDesactualizados;
    }

    public Boolean getCalTodasPromocionesGradoRealizadas() {
        return calTodasPromocionesGradoRealizadas;
    }

    public void setCalTodasPromocionesGradoRealizadas(Boolean calTodasPromocionesGradoRealizadas) {
        this.calTodasPromocionesGradoRealizadas = calTodasPromocionesGradoRealizadas;
    }
    
    

    @JsonIgnore
    public Double calcularPromedioCalificaciones(Long sexoPk) {    
        long cant = Optional.ofNullable(this.getCalCalificacionesEstudiante()
                .stream()
                .filter(e -> !StringUtils.isBlank(e.getCaeCalificacionNumericaEstudiante())).count()).orElse(0L);
        if (cant == 0L){
            return null;
        }
        return Optional.ofNullable(this.getCalCalificacionesEstudiante()
                .stream()
                .filter(e -> !StringUtils.isBlank(e.getCaeCalificacionNumericaEstudiante()))
                .filter((sexoPk != null) ? e -> e.getCaeEstudiante().getEstPersona().getPerSexo().getSexPk().equals(sexoPk) : s -> true)
                .map(c -> Double.parseDouble(c.getCaeCalificacionNumericaEstudiante() + "d"))
                .collect(Collectors.averagingDouble(a -> a))).orElse(null);
    }

    @JsonIgnore
    public SgCalificacion calcularModaCalificacionesConceptuales(Long sexoPk) {
        return Optional.ofNullable(this.getCalCalificacionesEstudiante()
                .stream()
                .filter((sexoPk != null) ? e -> e.getCaeEstudiante().getEstPersona().getPerSexo().getSexPk().equals(sexoPk) : s -> true)
                .map(e -> e.getCaeCalificacionConceptualEstudiante())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null)).orElse(null);
    }

    @Override
    public String securityAmbitCreate() {
        return "calSeccion";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "calSeccion.secServicioEducativo.sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secAsociacion.asoPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.calPk);
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
        final SgCalificacionCE other = (SgCalificacionCE) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalificacion{" + "calPk=" + calPk + ", calUltModFecha=" + calUltModFecha + ", calUltModUsuario=" + calUltModUsuario + ", calVersion=" + calVersion + '}';
    }

}
