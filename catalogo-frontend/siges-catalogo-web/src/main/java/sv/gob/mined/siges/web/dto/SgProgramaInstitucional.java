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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pinPk", scope = SgProgramaInstitucional.class)
public class SgProgramaInstitucional implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long pinPk;

    private String pinCodigo;

    private String pinNombre;

    private Boolean pinHabilitado;

    private LocalDateTime pinUltModFecha;

    private String pinUltModUsuario;

    private Integer pinVersion;
    
    
    public SgProgramaInstitucional() {
        this.pinHabilitado = Boolean.TRUE;
    }

    public Long getPinPk() {
        return pinPk;
    }

    public void setPinPk(Long pinPk) {
        this.pinPk = pinPk;
    }

    public String getPinCodigo() {
        return pinCodigo;
    }

    public void setPinCodigo(String pinCodigo) {
        this.pinCodigo = pinCodigo;
    }

    public String getPinNombre() {
        return pinNombre;
    }

    public void setPinNombre(String pinNombre) {
        this.pinNombre = pinNombre;
    }

    public LocalDateTime getPinUltModFecha() {
        return pinUltModFecha;
    }

    public void setPinUltModFecha(LocalDateTime pinUltModFecha) {
        this.pinUltModFecha = pinUltModFecha;
    }

    public String getPinUltModUsuario() {
        return pinUltModUsuario;
    }

    public void setPinUltModUsuario(String pinUltModUsuario) {
        this.pinUltModUsuario = pinUltModUsuario;
    }

    public Integer getPinVersion() {
        return pinVersion;
    }

    public void setPinVersion(Integer pinVersion) {
        this.pinVersion = pinVersion;
    }

    
     public Boolean getPinHabilitado() {
        return pinHabilitado;
    }

    public void setPinHabilitado(Boolean pinHabilitado) {
        this.pinHabilitado = pinHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pinPk != null ? pinPk.hashCode() : 0);
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
        final SgProgramaInstitucional other = (SgProgramaInstitucional) obj;
        if (!Objects.equals(this.pinPk, other.pinPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgProgramaInstitucional[ pinPk=" + pinPk + " ]";
    }
    
}
