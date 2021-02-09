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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "grePk", scope = SgGradoRiesgo.class)
public class SgGradoRiesgo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long grePk;

    private String greCodigo;

    private String greNombre;

    private Boolean greHabilitado;

    private Boolean greRiesgoAmbiental;

    private Boolean greRiesgoSocial;

    private LocalDateTime greUltModFecha;

    private String greUltModUsuario;

    private Integer greVersion;
    
    
    public SgGradoRiesgo() {
        this.greHabilitado = Boolean.TRUE;
    }

    public Long getGrePk() {
        return grePk;
    }

    public void setGrePk(Long grePk) {
        this.grePk = grePk;
    }

    public String getGreCodigo() {
        return greCodigo;
    }

    public void setGreCodigo(String greCodigo) {
        this.greCodigo = greCodigo;
    }

    public String getGreNombre() {
        return greNombre;
    }

    public void setGreNombre(String greNombre) {
        this.greNombre = greNombre;
    }

    public LocalDateTime getGreUltModFecha() {
        return greUltModFecha;
    }

    public void setGreUltModFecha(LocalDateTime greUltModFecha) {
        this.greUltModFecha = greUltModFecha;
    }

    public String getGreUltModUsuario() {
        return greUltModUsuario;
    }

    public void setGreUltModUsuario(String greUltModUsuario) {
        this.greUltModUsuario = greUltModUsuario;
    }

    public Integer getGreVersion() {
        return greVersion;
    }

    public void setGreVersion(Integer greVersion) {
        this.greVersion = greVersion;
    }

    
    public Boolean getGreHabilitado() {
        return greHabilitado;
    }

    public void setGreHabilitado(Boolean greHabilitado) {
        this.greHabilitado = greHabilitado;
    }

    public Boolean getGreRiesgoAmbiental() {
        return greRiesgoAmbiental;
    }

    public void setGreRiesgoAmbiental(Boolean greRiesgoAmbiental) {
        this.greRiesgoAmbiental = greRiesgoAmbiental;
    }

    public Boolean getGreRiesgoSocial() {
        return greRiesgoSocial;
    }

    public void setGreRiesgoSocial(Boolean greRiesgoSocial) {
        this.greRiesgoSocial = greRiesgoSocial;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grePk != null ? grePk.hashCode() : 0);
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
        final SgGradoRiesgo other = (SgGradoRiesgo) obj;
        if (!Objects.equals(this.grePk, other.grePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgGradoRiesgo[ grePk=" + grePk + " ]";
    }
    
}
