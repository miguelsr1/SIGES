/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mfaPk", scope = SgMotivoFallecimiento.class)
public class SgMotivoFallecimiento implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mfaPk;

    private String mfaCodigo;

    private String mfaNombre;

    private Boolean mfaHabilitado;

    private LocalDateTime mfaUltModFecha;

    private String mfaUltModUsuario;

    private Integer mfaVersion;
    
    
    public SgMotivoFallecimiento() {
        this.mfaHabilitado = Boolean.TRUE;
    }

    public Long getMfaPk() {
        return mfaPk;
    }

    public void setMfaPk(Long mfaPk) {
        this.mfaPk = mfaPk;
    }

    public String getMfaCodigo() {
        return mfaCodigo;
    }

    public void setMfaCodigo(String mfaCodigo) {
        this.mfaCodigo = mfaCodigo;
    }

    public String getMfaNombre() {
        return mfaNombre;
    }

    public void setMfaNombre(String mfaNombre) {
        this.mfaNombre = mfaNombre;
    }

    public LocalDateTime getMfaUltModFecha() {
        return mfaUltModFecha;
    }

    public void setMfaUltModFecha(LocalDateTime mfaUltModFecha) {
        this.mfaUltModFecha = mfaUltModFecha;
    }

    public String getMfaUltModUsuario() {
        return mfaUltModUsuario;
    }

    public void setMfaUltModUsuario(String mfaUltModUsuario) {
        this.mfaUltModUsuario = mfaUltModUsuario;
    }

    public Integer getMfaVersion() {
        return mfaVersion;
    }

    public void setMfaVersion(Integer mfaVersion) {
        this.mfaVersion = mfaVersion;
    }

    
     public Boolean getMfaHabilitado() {
        return mfaHabilitado;
    }

    public void setMfaHabilitado(Boolean mfaHabilitado) {
        this.mfaHabilitado = mfaHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mfaPk != null ? mfaPk.hashCode() : 0);
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
        final SgMotivoFallecimiento other = (SgMotivoFallecimiento) obj;
        if (!Objects.equals(this.mfaPk, other.mfaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMotivoFallecimiento[ mfaPk=" + mfaPk + " ]";
    }
    
}
