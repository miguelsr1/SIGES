/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSustitucionMiembroOAE;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "smoPk", scope = SgSustitucionMiembroOAE.class)
public class SgSustitucionMiembroOAE implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long smoPk;
    
    private LocalDate smoFecha;
    
    
    private SgOrganismoAdministracionEscolar smoOaeFk;
    
    
    private EnumEstadoSustitucionMiembroOAE smoEstado;

    
    private String smoNumeroResolucion;
    
   
    private String smoMotivoRechazo;

    
    private LocalDate smoFechaResolucion;

    
    private LocalDateTime smoUltModFecha;

    
    private String smoUltModUsuario;

    
    private Integer smoVersion;
    
  
    private List<SgRelSustitucionMiembroOAE> smoRelSustitucionMiembroOAE;
    
    private Integer cantidadMiembrosaSustituir;
    
    
    public SgSustitucionMiembroOAE() {
    }

    public Long getSmoPk() {
        return smoPk;
    }

    public void setSmoPk(Long smoPk) {
        this.smoPk = smoPk;
    }

    public LocalDate getSmoFecha() {
        return smoFecha;
    }

    public void setSmoFecha(LocalDate smoFecha) {
        this.smoFecha = smoFecha;
    }

    public SgOrganismoAdministracionEscolar getSmoOaeFk() {
        return smoOaeFk;
    }

    public void setSmoOaeFk(SgOrganismoAdministracionEscolar smoOaeFk) {
        this.smoOaeFk = smoOaeFk;
    }

    public EnumEstadoSustitucionMiembroOAE getSmoEstado() {
        return smoEstado;
    }

    public void setSmoEstado(EnumEstadoSustitucionMiembroOAE smoEstado) {
        this.smoEstado = smoEstado;
    }

    public String getSmoNumeroResolucion() {
        return smoNumeroResolucion;
    }

    public void setSmoNumeroResolucion(String smoNumeroResolucion) {
        this.smoNumeroResolucion = smoNumeroResolucion;
    }

    public String getSmoMotivoRechazo() {
        return smoMotivoRechazo;
    }

    public void setSmoMotivoRechazo(String smoMotivoRechazo) {
        this.smoMotivoRechazo = smoMotivoRechazo;
    }

    public LocalDate getSmoFechaResolucion() {
        return smoFechaResolucion;
    }

    public void setSmoFechaResolucion(LocalDate smoFechaResolucion) {
        this.smoFechaResolucion = smoFechaResolucion;
    }

    public List<SgRelSustitucionMiembroOAE> getSmoRelSustitucionMiembroOAE() {
        return smoRelSustitucionMiembroOAE;
    }

    public void setSmoRelSustitucionMiembroOAE(List<SgRelSustitucionMiembroOAE> smoRelSustitucionMiembroOAE) {
        this.smoRelSustitucionMiembroOAE = smoRelSustitucionMiembroOAE;
    }


    public LocalDateTime getSmoUltModFecha() {
        return smoUltModFecha;
    }

    public void setSmoUltModFecha(LocalDateTime smoUltModFecha) {
        this.smoUltModFecha = smoUltModFecha;
    }

    public String getSmoUltModUsuario() {
        return smoUltModUsuario;
    }

    public void setSmoUltModUsuario(String smoUltModUsuario) {
        this.smoUltModUsuario = smoUltModUsuario;
    }

    public Integer getSmoVersion() {
        return smoVersion;
    }

    public void setSmoVersion(Integer smoVersion) {
        this.smoVersion = smoVersion;
    }

    public Integer getCantidadMiembrosaSustituir() {
        return cantidadMiembrosaSustituir;
    }

    public void setCantidadMiembrosaSustituir(Integer cantidadMiembrosaSustituir) {
        this.cantidadMiembrosaSustituir = cantidadMiembrosaSustituir;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (smoPk != null ? smoPk.hashCode() : 0);
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
        final SgSustitucionMiembroOAE other = (SgSustitucionMiembroOAE) obj;
        if (!Objects.equals(this.smoPk, other.smoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgSustitucionMiembroOAE[ smoPk=" + smoPk + " ]";
    }
    
}
