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
import java.util.List;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumRegimenSeccion;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAsociacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.infraestructura.SgAula;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_secciones", uniqueConstraints = {
    @UniqueConstraint(name = "sec_nombre_jornada_anio_uk",
            columnNames = {"sec_nombre", "sec_jornada_fk", "sec_anio_lectivo_fk"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "secPk", scope = SgSeccion.class)
@Audited
public class SgSeccion implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sec_pk", nullable = false)
    private Long secPk;

    @Size(max = 60)
    @Column(name = "sec_codigo")
    private String secCodigo;

    @Size(max = 255)
    @Column(name = "sec_nombre")
    private String secNombre;

    @Column(name = "sec_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumSeccionEstado secEstado;

    @JoinColumn(name = "sec_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne(optional = false)
    private SgAnioLectivo secAnioLectivo;

    @JoinColumn(name = "sec_servicio_educativo_fk", referencedColumnName = "sdu_pk")
    @ManyToOne(optional = false)
    private SgServicioEducativo secServicioEducativo;

    @JoinColumn(name = "sec_plan_estudio_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPlanEstudio secPlanEstudio;

    @JoinColumn(name = "sec_jornada_fk", referencedColumnName = "jla_pk")
    @ManyToOne
    private SgJornadaLaboral secJornadaLaboral;

    @Column(name = "sec_acceso_internet")
    private Boolean secAccesoInternet;

    @Column(name = "sec_acceso_computadora")
    private Boolean secAccesoComputadora;

    @Column(name = "sec_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime secUltModFecha;

    @Size(max = 45)
    @Column(name = "sec_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String secUltModUsuario;

    @Column(name = "sec_version")
    @Version
    private Integer secVersion;

    @OneToMany(mappedBy = "matSeccion")
    @NotAudited
    private List<SgMatricula> secMatricula;

    @Column(name = "sec_copiada_anio_siguiente")
    private Boolean secCopiadaAnioSiguiente;

    @JoinColumn(name = "sec_aula_fk", referencedColumnName = "aul_pk")
    @ManyToOne
    private SgAula secAula;

    @Column(name = "sec_integrada")
    private Boolean secIntegrada;

    @JoinColumn(name = "sec_asociacion_fk")
    @ManyToOne
    private SgAsociacion secAsociacion; //Dato sec modalidad flexible

    @Column(name = "sec_regimen")
    @Enumerated(value = EnumType.STRING)
    private EnumRegimenSeccion secRegimen; //Dato sec modalidad flexible

    @Column(name = "sec_cantidad_estudiantes_no_retirados", insertable = false, updatable = false) //Se genera mediante procesamiento externo
    private Long secCantidadEstudiantesNoRetirados;

    //Las secciones pueden ser anuales o pertencer a un periodo especifico del año
    //Cuando no es anual se especifica a que periodo del año pertenece con secNumeroPeriodo
    @Column(name = "sec_tipo_periodo")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoPeriodoSeccion secTipoPeriodo;

    @Column(name = "sec_numero_periodo")
    private Integer secNumeroPeriodo;
    
    @Column(name = "sec_fecha_cierre_seccion")
    private LocalDate secFechaCierreSeccion;
    
    @Column(name = "sec_todas_promociones_grado_realizadas", insertable = false, updatable = false) //Actualizada por query nativa
    private Boolean secTodasPromocionesGradoRealizadas; 

    public SgSeccion() {
    }

    public SgSeccion(Long secPk, Integer secVersion) {
        this.secPk = secPk;
        this.secVersion = secVersion;
    }

    public Long getSecPk() {
        return secPk;
    }

    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
    }

    public EnumSeccionEstado getSecEstado() {
        return secEstado;
    }

    public void setSecEstado(EnumSeccionEstado secEstado) {
        this.secEstado = secEstado;
    }

    public SgAnioLectivo getSecAnioLectivo() {
        return secAnioLectivo;
    }

    public void setSecAnioLectivo(SgAnioLectivo secAnioLectivo) {
        this.secAnioLectivo = secAnioLectivo;
    }

    public SgPlanEstudio getSecPlanEstudio() {
        return secPlanEstudio;
    }

    public void setSecPlanEstudio(SgPlanEstudio secPlanEstudio) {
        this.secPlanEstudio = secPlanEstudio;
    }

    public SgServicioEducativo getSecServicioEducativo() {
        return secServicioEducativo;
    }

    public void setSecServicioEducativo(SgServicioEducativo secServicioEducativo) {
        this.secServicioEducativo = secServicioEducativo;
    }

    public LocalDateTime getSecUltModFecha() {
        return secUltModFecha;
    }

    public void setSecUltModFecha(LocalDateTime secUltModFecha) {
        this.secUltModFecha = secUltModFecha;
    }

    public String getSecUltModUsuario() {
        return secUltModUsuario;
    }

    public void setSecUltModUsuario(String secUltModUsuario) {
        this.secUltModUsuario = secUltModUsuario;
    }

    public Integer getSecVersion() {
        return secVersion;
    }

    public void setSecVersion(Integer secVersion) {
        this.secVersion = secVersion;
    }

    public SgJornadaLaboral getSecJornadaLaboral() {
        return secJornadaLaboral;
    }

    public void setSecJornadaLaboral(SgJornadaLaboral secJornadaLaboral) {
        this.secJornadaLaboral = secJornadaLaboral;
    }

    public List<SgMatricula> getSecMatricula() {
        return secMatricula;
    }

    public void setSecMatricula(List<SgMatricula> secMatricula) {
        this.secMatricula = secMatricula;
    }

    public Boolean getSecAccesoInternet() {
        return secAccesoInternet;
    }

    public void setSecAccesoInternet(Boolean secAccesoInternet) {
        this.secAccesoInternet = secAccesoInternet;
    }

    public Boolean getSecAccesoComputadora() {
        return secAccesoComputadora;
    }

    public void setSecAccesoComputadora(Boolean secAccesoComputadora) {
        this.secAccesoComputadora = secAccesoComputadora;
    }

    public Boolean getSecCopiadaAnioSiguiente() {
        return secCopiadaAnioSiguiente;
    }

    public void setSecCopiadaAnioSiguiente(Boolean secCopiadaAnioSiguiente) {
        this.secCopiadaAnioSiguiente = secCopiadaAnioSiguiente;
    }

    public SgAula getSecAula() {
        return secAula;
    }

    public void setSecAula(SgAula secAula) {
        this.secAula = secAula;
    }

    public Long getSecCantidadEstudiantesNoRetirados() {
        return secCantidadEstudiantesNoRetirados;
    }

    public void setSecCantidadEstudiantesNoRetirados(Long secCantidadEstudiantesNoRetirados) {
        this.secCantidadEstudiantesNoRetirados = secCantidadEstudiantesNoRetirados;
    }

    public Boolean getSecIntegrada() {
        return secIntegrada;
    }

    public void setSecIntegrada(Boolean secIntegrada) {
        this.secIntegrada = secIntegrada;
    }

    public SgAsociacion getSecAsociacion() {
        return secAsociacion;
    }

    public void setSecAsociacion(SgAsociacion secAsociacion) {
        this.secAsociacion = secAsociacion;
    }

    public EnumRegimenSeccion getSecRegimen() {
        return secRegimen;
    }

    public void setSecRegimen(EnumRegimenSeccion secRegimen) {
        this.secRegimen = secRegimen;
    }

    public EnumTipoPeriodoSeccion getSecTipoPeriodo() {
        return secTipoPeriodo;
    }

    public void setSecTipoPeriodo(EnumTipoPeriodoSeccion secTipoPeriodo) {
        this.secTipoPeriodo = secTipoPeriodo;
    }

    public Integer getSecNumeroPeriodo() {
        return secNumeroPeriodo;
    }

    public void setSecNumeroPeriodo(Integer secNumeroPeriodo) {
        this.secNumeroPeriodo = secNumeroPeriodo;
    }

    public LocalDate getSecFechaCierreSeccion() {
        return secFechaCierreSeccion;
    }

    public void setSecFechaCierreSeccion(LocalDate secFechaCierreSeccion) {
        this.secFechaCierreSeccion = secFechaCierreSeccion;
    }

    public Boolean getSecTodasPromocionesGradoRealizadas() {
        return secTodasPromocionesGradoRealizadas;
    }

    public void setSecTodasPromocionesGradoRealizadas(Boolean secTodasPromocionesGradoRealizadas) {
        this.secTodasPromocionesGradoRealizadas = secTodasPromocionesGradoRealizadas;
    }
    
    

    @Override
    public String securityAmbitCreate() {
        return "secServicioEducativo.sduSede";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "secServicioEducativo.sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secAsociacion.asoPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.secPk);
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
        final SgSeccion other = (SgSeccion) obj;
        if (!Objects.equals(this.secPk, other.secPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSeccion{" + "secPk=" + secPk + ", secVersion=" + secVersion + '}';
    }

}
