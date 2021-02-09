/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoTraslado;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioLite;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_solicitudes_traslado", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sotPk", scope = SgSolicitudTraslado.class)
@Audited
public class SgSolicitudTraslado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sot_pk")
    private Long sotPk;

    @JoinColumn(name = "sot_sede_solicitante_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSede sotSedeSolicitante;

    @JoinColumn(name = "sot_estudiante_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudianteLite sotEstudiante;

    @JoinColumn(name = "sot_servicio_educativo_solicitado_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgServicioEducativo sotServicioEducativoSolicitado;

    @JoinColumn(name = "sot_sede_origen_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSede sotSedeOrigen;

    //TODO: reemplazar sede y servicios por entidades Lite
    @JoinColumn(name = "sot_servicio_educativo_origen_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgServicioEducativo sotServicioEducativoOrigen;

    @Column(name = "sot_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSolicitudTraslado sotEstado;

    @JoinColumn(name = "sot_usuario_solicitud_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgUsuarioLite sotUsuarioSolicitud;

    @Column(name = "sot_fecha_solicitud")
    private LocalDate sotFechaSolicitud;

    @Size(max = 500)
    @Column(name = "sot_observacion", length = 500)
    private String sotObservacion;

    @Size(max = 500)
    @Column(name = "sot_resolucion", length = 500)
    private String sotResolucion;

    @JoinColumn(name = "sot_motivo_traslado_fk")
    @ManyToOne
    private SgMotivoTraslado sotMotivoTraslado;

    @Column(name = "sot_fecha_traslado")
    private LocalDate sotFechaTraslado;

    @JoinColumn(name = "sot_seccion_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion sotSeccion;

    @Column(name = "sot_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sotUltModFecha;

    @Size(max = 45)
    @Column(name = "sot_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sotUltModUsuario;

    @Column(name = "sot_version")
    @Version
    private Integer sotVersion;
    
    @Column(name = "sot_fecha_confirmacion")
    private LocalDateTime sotFechaConfirmacion;
    
    @Size(max = 45)
    @Column(name = "sot_usuario_confirmacion",length = 45)
    private String sotUsuarioConfirmacion;
    
    @JoinColumn(name = "sot_archivo_firmado_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo sotArchivoFirmado;
    
    @Column(name = "sot_firmada")
    private Boolean sotFirmada;
    
    @JoinColumn(name = "sot_confirmacion_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgConfirmacionSolicitudTraslado sotConfirmacion;
    
    public SgSolicitudTraslado() {
    }

    public SgSolicitudTraslado(Long sotPk) {
        this.sotPk = sotPk;
    }
    
    @PrePersist
    public void prePersist(){
        this.sotFirmada = Boolean.FALSE;
    }

    public Long getSotPk() {
        return sotPk;
    }

    public void setSotPk(Long sotPk) {
        this.sotPk = sotPk;
    }

    public String getSotObservacion() {
        return sotObservacion;
    }

    public void setSotObservacion(String sotObservacion) {
        this.sotObservacion = sotObservacion;
    }

    public String getSotResolucion() {
        return sotResolucion;
    }

    public void setSotResolucion(String sotResolucion) {
        this.sotResolucion = sotResolucion;
    }

    public String getSotUltModUsuario() {
        return sotUltModUsuario;
    }

    public void setSotUltModUsuario(String sotUltModUsuario) {
        this.sotUltModUsuario = sotUltModUsuario;
    }

    public Integer getSotVersion() {
        return sotVersion;
    }

    public void setSotVersion(Integer sotVersion) {
        this.sotVersion = sotVersion;
    }

    public SgSede getSotSedeSolicitante() {
        return sotSedeSolicitante;
    }

    public void setSotSedeSolicitante(SgSede sotSedeSolicitante) {
        this.sotSedeSolicitante = sotSedeSolicitante;
    }

    public SgEstudianteLite getSotEstudiante() {
        return sotEstudiante;
    }

    public void setSotEstudiante(SgEstudianteLite sotEstudiante) {
        this.sotEstudiante = sotEstudiante;
    }

    public SgServicioEducativo getSotServicioEducativoSolicitado() {
        return sotServicioEducativoSolicitado;
    }

    public void setSotServicioEducativoSolicitado(SgServicioEducativo sotServicioEducativoSolicitado) {
        this.sotServicioEducativoSolicitado = sotServicioEducativoSolicitado;
    }

    public SgSede getSotSedeOrigen() {
        return sotSedeOrigen;
    }

    public void setSotSedeOrigen(SgSede sotSedeOrigen) {
        this.sotSedeOrigen = sotSedeOrigen;
    }

    public SgServicioEducativo getSotServicioEducativoOrigen() {
        return sotServicioEducativoOrigen;
    }

    public void setSotServicioEducativoOrigen(SgServicioEducativo sotServicioEducativoOrigen) {
        this.sotServicioEducativoOrigen = sotServicioEducativoOrigen;
    }

    public EnumEstadoSolicitudTraslado getSotEstado() {
        return sotEstado;
    }

    public void setSotEstado(EnumEstadoSolicitudTraslado sotEstado) {
        this.sotEstado = sotEstado;
    }

    public SgUsuarioLite getSotUsuarioSolicitud() {
        return sotUsuarioSolicitud;
    }

    public void setSotUsuarioSolicitud(SgUsuarioLite sotUsuarioSolicitud) {
        this.sotUsuarioSolicitud = sotUsuarioSolicitud;
    }

    public LocalDate getSotFechaSolicitud() {
        return sotFechaSolicitud;
    }

    public void setSotFechaSolicitud(LocalDate sotFechaSolicitud) {
        this.sotFechaSolicitud = sotFechaSolicitud;
    }

    public SgMotivoTraslado getSotMotivoTraslado() {
        return sotMotivoTraslado;
    }

    public void setSotMotivoTraslado(SgMotivoTraslado sotMotivoTraslado) {
        this.sotMotivoTraslado = sotMotivoTraslado;
    }

    public LocalDateTime getSotUltModFecha() {
        return sotUltModFecha;
    }

    public void setSotUltModFecha(LocalDateTime sotUltModFecha) {
        this.sotUltModFecha = sotUltModFecha;
    }

    public LocalDate getSotFechaTraslado() {
        return sotFechaTraslado;
    }

    public void setSotFechaTraslado(LocalDate sotFechaTraslado) {
        this.sotFechaTraslado = sotFechaTraslado;
    }

    public SgSeccion getSotSeccion() {
        return sotSeccion;
    }

    public void setSotSeccion(SgSeccion sotSeccion) {
        this.sotSeccion = sotSeccion;
    }

    public LocalDateTime getSotFechaConfirmacion() {
        return sotFechaConfirmacion;
    }

    public void setSotFechaConfirmacion(LocalDateTime sotFechaConfirmacion) {
        this.sotFechaConfirmacion = sotFechaConfirmacion;
    }

    public String getSotUsuarioConfirmacion() {
        return sotUsuarioConfirmacion;
    }

    public void setSotUsuarioConfirmacion(String sotUsuarioConfirmacion) {
        this.sotUsuarioConfirmacion = sotUsuarioConfirmacion;
    }

    public SgArchivo getSotArchivoFirmado() {
        return sotArchivoFirmado;
    }

    public void setSotArchivoFirmado(SgArchivo sotArchivoFirmado) {
        this.sotArchivoFirmado = sotArchivoFirmado;
    }

    public Boolean getSotFirmada() {
        return sotFirmada;
    }

    public void setSotFirmada(Boolean sotFirmada) {
        this.sotFirmada = sotFirmada;
    }

    public SgConfirmacionSolicitudTraslado getSotConfirmacion() {
        return sotConfirmacion;
    }

    public void setSotConfirmacion(SgConfirmacionSolicitudTraslado sotConfirmacion) {
        this.sotConfirmacion = sotConfirmacion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sotPk != null ? sotPk.hashCode() : 0);
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
        final SgSolicitudTraslado other = (SgSolicitudTraslado) obj;
        if (!Objects.equals(this.sotPk, other.sotPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado[ sotPk=" + sotPk + " ]";
    }

}
