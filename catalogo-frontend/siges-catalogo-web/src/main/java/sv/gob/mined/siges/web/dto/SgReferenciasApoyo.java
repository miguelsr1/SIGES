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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reaPk", scope = SgReferenciasApoyo.class)
public class SgReferenciasApoyo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long reaPk;

    private String reaCodigo;

    private String reaNombre;

    private Boolean reaHabilitado;

    private LocalDateTime reaUltModFecha;

    private String reaUltModUsuario;

    private Integer reaVersion;
    
    
    public SgReferenciasApoyo() {
        this.reaHabilitado = Boolean.TRUE;
    }

    public Long getReaPk() {
        return reaPk;
    }

    public void setReaPk(Long reaPk) {
        this.reaPk = reaPk;
    }

    public String getReaCodigo() {
        return reaCodigo;
    }

    public void setReaCodigo(String reaCodigo) {
        this.reaCodigo = reaCodigo;
    }

    public String getReaNombre() {
        return reaNombre;
    }

    public void setReaNombre(String reaNombre) {
        this.reaNombre = reaNombre;
    }

    public LocalDateTime getReaUltModFecha() {
        return reaUltModFecha;
    }

    public void setReaUltModFecha(LocalDateTime reaUltModFecha) {
        this.reaUltModFecha = reaUltModFecha;
    }

    public String getReaUltModUsuario() {
        return reaUltModUsuario;
    }

    public void setReaUltModUsuario(String reaUltModUsuario) {
        this.reaUltModUsuario = reaUltModUsuario;
    }

    public Integer getReaVersion() {
        return reaVersion;
    }

    public void setReaVersion(Integer reaVersion) {
        this.reaVersion = reaVersion;
    }

    
     public Boolean getReaHabilitado() {
        return reaHabilitado;
    }

    public void setReaHabilitado(Boolean reaHabilitado) {
        this.reaHabilitado = reaHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reaPk != null ? reaPk.hashCode() : 0);
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
        final SgReferenciasApoyo other = (SgReferenciasApoyo) obj;
        if (!Objects.equals(this.reaPk, other.reaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgReferenciasApoyo[ reaPk=" + reaPk + " ]";
    }
    
}
