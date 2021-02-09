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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_calendarios", uniqueConstraints = {
    @UniqueConstraint(name = "cal_codigo_uk", columnNames = {"cal_codigo"})
    ,
    @UniqueConstraint(name = "cal_nombre_uk", columnNames = {"cal_nombre"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "calPk", scope = SgCalendario.class)
@Audited
public class SgCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cal_pk", nullable = false)
    private Long calPk;

    @Size(max = 4)
    @Column(name = "cal_codigo", length = 4)
    @AtributoCodigo
    private String calCodigo;

    @Size(max = 255)
    @Column(name = "cal_nombre", length = 255)
    @AtributoNormalizable
    private String calNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cal_nombre_busqueda", length = 255)
    private String calNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "cal_descripcion",length = 500)
    @AtributoDescripcion
    private String calDescripcion;    

    @Column(name = "cal_habilitado")
    @AtributoHabilitado
    private Boolean calHabilitado;
    
    @Column(name = "cal_fecha_inicio")
    private LocalDate calFechaInicio;
    
   
    @Column(name = "cal_fecha_fin")
    private LocalDate calFechaFin;
    
    @Column(name = "cal_cantidad_dias_lectivo")
    private Integer calCantidadDiasLectivo;

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
    
    @JoinColumn(name = "cal_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo calAnioLectivo;
    
    @JoinColumn(name = "cal_tipo_calendario_fk", referencedColumnName = "tce_pk")
    @ManyToOne
    private SgTipoCalendarioEscolar calTipoCalendario;
     
    @OneToMany(mappedBy = "acaCalendario")
    @NotAudited
    private List<SgActividadCalendario> calActividadesCalendario;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pcaCalendario")
    private List<SgPeriodoCalificacion> calPeriodosCalificacion;   

    @Column(name = "cal_permite_calcular_nota_institucional")
    private Boolean calPermiteCalcularNotaInstitucional;   

    @Column(name = "cal_permite_calcular_nota_aprobacion")
    private Boolean calPermiteCalcularNotaAprobacion;
    
    @Column(name = "cal_permite_cierre_anio")
    private Boolean calPermiteCierreAnio;   

    @Column(name = "cal_permite_matricular_siguiente_anio")
    private Boolean calPermiteMatricularSiguienteAnio;
    
    @Column(name = "cal_permite_copiar_seccion")
    private Boolean calPermiteCopiarSeccion;   

    public SgCalendario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.calNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.calNombre);
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public String getCalCodigo() {
        return calCodigo;
    }

    public void setCalCodigo(String calCodigo) {
        this.calCodigo = calCodigo;
    }

    public String getCalNombre() {
        return calNombre;
    }

    public void setCalNombre(String calNombre) {
        this.calNombre = calNombre;
    }

    public String getCalNombreBusqueda() {
        return calNombreBusqueda;
    }

    public void setCalNombreBusqueda(String calNombreBusqueda) {
        this.calNombreBusqueda = calNombreBusqueda;
    }

    public String getCalDescripcion() {
        return calDescripcion;
    }

    public void setCalDescripcion(String calDescripcion) {
        this.calDescripcion = calDescripcion;
    }
    
    public Boolean getCalHabilitado() {
        return calHabilitado;
    }

    public void setCalHabilitado(Boolean calHabilitado) {
        this.calHabilitado = calHabilitado;
    }

    public LocalDate getCalFechaInicio() {
        return calFechaInicio;
    }

    public void setCalFechaInicio(LocalDate calFechaInicio) {
        this.calFechaInicio = calFechaInicio;
    }

    public LocalDate getCalFechaFin() {
        return calFechaFin;
    }

    public void setCalFechaFin(LocalDate calFechaFin) {
        this.calFechaFin = calFechaFin;
    }

    public Integer getCalCantidadDiasLectivo() {
        return calCantidadDiasLectivo;
    }

    public void setCalCantidadDiasLectivo(Integer calCantidadDiasLectivo) {
        this.calCantidadDiasLectivo = calCantidadDiasLectivo;
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

    public SgAnioLectivo getCalAnioLectivo() {
        return calAnioLectivo;
    }

    public void setCalAnioLectivo(SgAnioLectivo calAnioLectivo) {
        this.calAnioLectivo = calAnioLectivo;
    }

    public SgTipoCalendarioEscolar getCalTipoCalendario() {
        return calTipoCalendario;
    }

    public void setCalTipoCalendario(SgTipoCalendarioEscolar calTipoCalendario) {
        this.calTipoCalendario = calTipoCalendario;
    }  

    public List<SgActividadCalendario> getCalActividadesCalendario() {
        return calActividadesCalendario;
    }

    public void setCalActividadesCalendario(List<SgActividadCalendario> calActividadesCalendario) {
        this.calActividadesCalendario = calActividadesCalendario;
    }

    public List<SgPeriodoCalificacion> getCalPeriodosCalificacion() {
        return calPeriodosCalificacion;
    }

    public void setCalPeriodosCalificacion(List<SgPeriodoCalificacion> calPeriodosCalificacion) {
        this.calPeriodosCalificacion = calPeriodosCalificacion;
    }

    public Boolean getCalPermiteCalcularNotaInstitucional() {
        return calPermiteCalcularNotaInstitucional;
    }

    public void setCalPermiteCalcularNotaInstitucional(Boolean calPermiteCalcularNotaInstitucional) {
        this.calPermiteCalcularNotaInstitucional = calPermiteCalcularNotaInstitucional;
    }

    public Boolean getCalPermiteCalcularNotaAprobacion() {
        return calPermiteCalcularNotaAprobacion;
    }

    public void setCalPermiteCalcularNotaAprobacion(Boolean calPermiteCalcularNotaAprobacion) {
        this.calPermiteCalcularNotaAprobacion = calPermiteCalcularNotaAprobacion;
    }

    public Boolean getCalPermiteCierreAnio() {
        return calPermiteCierreAnio;
    }

    public void setCalPermiteCierreAnio(Boolean calPermiteCierreAnio) {
        this.calPermiteCierreAnio = calPermiteCierreAnio;
    }

    public Boolean getCalPermiteMatricularSiguienteAnio() {
        return calPermiteMatricularSiguienteAnio;
    }

    public void setCalPermiteMatricularSiguienteAnio(Boolean calPermiteMatricularSiguienteAnio) {
        this.calPermiteMatricularSiguienteAnio = calPermiteMatricularSiguienteAnio;
    }

    public Boolean getCalPermiteCopiarSeccion() {
        return calPermiteCopiarSeccion;
    }

    public void setCalPermiteCopiarSeccion(Boolean calPermiteCopiarSeccion) {
        this.calPermiteCopiarSeccion = calPermiteCopiarSeccion;
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
        final SgCalendario other = (SgCalendario) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalendario{" + "calPk=" + calPk + ", calCodigo=" + calCodigo + ", calNombre=" + calNombre + ", calNombreBusqueda=" + calNombreBusqueda + ", calHabilitado=" + calHabilitado + ", calUltModFecha=" + calUltModFecha + ", calUltModUsuario=" + calUltModUsuario + ", calVersion=" + calVersion + '}';
    }
    
    
    
    

}
