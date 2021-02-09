/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoTraslado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "sotPk", scope = SgSolicitudTraslado.class)
public class SgSolicitudTraslado implements Serializable {

    private Long sotPk;
    
    private SgSede sotSedeSolicitante;
    
    private SgEstudiante sotEstudiante;
    
    private SgServicioEducativo sotServicioEducativoSolicitado;
    
    private SgSede sotSedeOrigen;
    
    private SgServicioEducativo sotServicioEducativoOrigen;
    
    private EnumEstadoSolicitudTraslado sotEstado;
    
    private SgUsuario sotUsuarioSolicitud;
    
    private LocalDate sotFechaSolicitud;
    
    private String sotObservacion;
    
    private String sotResolucion;
    
    private SgMotivoTraslado sotMotivoTraslado;
    
    private LocalDate sotFechaTraslado;
    
    private SgSeccion sotSeccion;
    
    private LocalDateTime sotUltModFecha;
    
    private String sotUltModUsuario;
    
    private Integer sotVersion;
    
    private SgConfirmacionSolicitudTraslado sotConfirmacion;

    public SgSolicitudTraslado() {
        this.sotFechaSolicitud = LocalDate.now();
        this.sotEstado = EnumEstadoSolicitudTraslado.PENDIENTE;
    }

    public SgSolicitudTraslado(Long sotPk) {
        this.sotPk = sotPk;
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

    public SgEstudiante getSotEstudiante() {
        return sotEstudiante;
    }

    public void setSotEstudiante(SgEstudiante sotEstudiante) {
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

    public SgUsuario getSotUsuarioSolicitud() {
        return sotUsuarioSolicitud;
    }

    public void setSotUsuarioSolicitud(SgUsuario sotUsuarioSolicitud) {
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSolicitudTraslado)) {
            return false;
        }
        SgSolicitudTraslado other = (SgSolicitudTraslado) object;
        if ((this.sotPk == null && other.sotPk != null) || (this.sotPk != null && !this.sotPk.equals(other.sotPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado[ sotPk=" + sotPk + " ]";
    }
    
}
