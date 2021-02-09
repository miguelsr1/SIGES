/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mtrPk", scope = SgMedioTransporte.class)
public class SgMedioTransporte implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mtrPk;

    private String mtrCodigo;

    private String mtrNombre;

    private Boolean mtrHabilitado;

    private LocalDateTime mtrUltModFecha;

    private String mtrUltModUsuario;

    private Integer mtrVersion;

    public SgMedioTransporte() {
        this.mtrHabilitado = Boolean.TRUE;
    }
    
    public Long getMtrPk() {
        return mtrPk;
    }

    public void setMtrPk(Long mtrPk) {
        this.mtrPk = mtrPk;
    }

    public String getMtrCodigo() {
        return mtrCodigo;
    }

    public void setMtrCodigo(String mtrCodigo) {
        this.mtrCodigo = mtrCodigo;
    }

    public String getMtrNombre() {
        return mtrNombre;
    }

    public void setMtrNombre(String mtrNombre) {
        this.mtrNombre = mtrNombre;
    }

    public Boolean getMtrHabilitado() {
        return mtrHabilitado;
    }

    public void setMtrHabilitado(Boolean mtrHabilitado) {
        this.mtrHabilitado = mtrHabilitado;
    }

    public LocalDateTime getMtrUltModFecha() {
        return mtrUltModFecha;
    }

    public void setMtrUltModFecha(LocalDateTime mtrUltModFecha) {
        this.mtrUltModFecha = mtrUltModFecha;
    }

   
    public String getMtrUltModUsuario() {
        return mtrUltModUsuario;
    }

    public void setMtrUltModUsuario(String mtrUltModUsuario) {
        this.mtrUltModUsuario = mtrUltModUsuario;
    }

    public Integer getMtrVersion() {
        return mtrVersion;
    }

    public void setMtrVersion(Integer mtrVersion) {
        this.mtrVersion = mtrVersion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mtrPk != null ? mtrPk.hashCode() : 0);
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
        final SgMedioTransporte other = (SgMedioTransporte) obj;
        if (!Objects.equals(this.mtrPk, other.mtrPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMedioTransporte[ mtrPk=" + mtrPk + " ]";
    }
    
}
