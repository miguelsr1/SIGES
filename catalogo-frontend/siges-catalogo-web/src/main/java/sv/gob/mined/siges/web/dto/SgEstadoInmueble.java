/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "einPk", scope = SgEstadoInmueble.class)
public class SgEstadoInmueble implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long einPk;

    private String einCodigo;

    private String einNombre;

    private Boolean einHabilitado;
    
    private Boolean einHabilitaDatosInscripcion;

    private LocalDateTime einUltModFecha;

    private String einUltModUsuario;

    private Integer einVersion;
    
    
    public SgEstadoInmueble() {
        this.einHabilitado = Boolean.TRUE;
        this.einHabilitaDatosInscripcion= Boolean.FALSE;
    }

    public Long getEinPk() {
        return einPk;
    }

    public void setEinPk(Long einPk) {
        this.einPk = einPk;
    }

    public String getEinCodigo() {
        return einCodigo;
    }

    public void setEinCodigo(String einCodigo) {
        this.einCodigo = einCodigo;
    }

    public String getEinNombre() {
        return einNombre;
    }

    public void setEinNombre(String einNombre) {
        this.einNombre = einNombre;
    }

    public LocalDateTime getEinUltModFecha() {
        return einUltModFecha;
    }

    public void setEinUltModFecha(LocalDateTime einUltModFecha) {
        this.einUltModFecha = einUltModFecha;
    }

    public String getEinUltModUsuario() {
        return einUltModUsuario;
    }

    public void setEinUltModUsuario(String einUltModUsuario) {
        this.einUltModUsuario = einUltModUsuario;
    }

    public Integer getEinVersion() {
        return einVersion;
    }

    public void setEinVersion(Integer einVersion) {
        this.einVersion = einVersion;
    }

    
     public Boolean getEinHabilitado() {
        return einHabilitado;
    }

    public void setEinHabilitado(Boolean einHabilitado) {
        this.einHabilitado = einHabilitado;
    }

    public Boolean getEinHabilitaDatosInscripcion() {
        return einHabilitaDatosInscripcion;
    }

    public void setEinHabilitaDatosInscripcion(Boolean einHabilitaDatosInscripcion) {
        this.einHabilitaDatosInscripcion = einHabilitaDatosInscripcion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (einPk != null ? einPk.hashCode() : 0);
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
        final SgEstadoInmueble other = (SgEstadoInmueble) obj;
        if (!Objects.equals(this.einPk, other.einPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstadoInmueble[ einPk=" + einPk + " ]";
    }
    
}
