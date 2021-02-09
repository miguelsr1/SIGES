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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mnePk", scope = SgMaximoNivelEducativoAlcanzado.class)
public class SgMaximoNivelEducativoAlcanzado implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mnePk;

    private String mneCodigo;

    private String mneNombre;

    private Boolean mneHabilitado;

    private LocalDateTime mneUltModFecha;

    private String mneUltModUsuario;

    private Integer mneVersion;
    
    
    public SgMaximoNivelEducativoAlcanzado() {
        this.mneHabilitado = Boolean.TRUE;
    }

    public Long getMnePk() {
        return mnePk;
    }

    public void setMnePk(Long mnePk) {
        this.mnePk = mnePk;
    }

    public String getMneCodigo() {
        return mneCodigo;
    }

    public void setMneCodigo(String mneCodigo) {
        this.mneCodigo = mneCodigo;
    }

    public String getMneNombre() {
        return mneNombre;
    }

    public void setMneNombre(String mneNombre) {
        this.mneNombre = mneNombre;
    }

    public LocalDateTime getMneUltModFecha() {
        return mneUltModFecha;
    }

    public void setMneUltModFecha(LocalDateTime mneUltModFecha) {
        this.mneUltModFecha = mneUltModFecha;
    }

    public String getMneUltModUsuario() {
        return mneUltModUsuario;
    }

    public void setMneUltModUsuario(String mneUltModUsuario) {
        this.mneUltModUsuario = mneUltModUsuario;
    }

    public Integer getMneVersion() {
        return mneVersion;
    }

    public void setMneVersion(Integer mneVersion) {
        this.mneVersion = mneVersion;
    }

    
     public Boolean getMneHabilitado() {
        return mneHabilitado;
    }

    public void setMneHabilitado(Boolean mneHabilitado) {
        this.mneHabilitado = mneHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mnePk != null ? mnePk.hashCode() : 0);
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
        final SgMaximoNivelEducativoAlcanzado other = (SgMaximoNivelEducativoAlcanzado) obj;
        if (!Objects.equals(this.mnePk, other.mnePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMaximoNivelEducativoAlcanzado[ mnePk=" + mnePk + " ]";
    }
    
}
