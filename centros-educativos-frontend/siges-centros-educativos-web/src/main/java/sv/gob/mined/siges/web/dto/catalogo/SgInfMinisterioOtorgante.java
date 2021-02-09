/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mioPk", scope = SgInfMinisterioOtorgante.class)
public class SgInfMinisterioOtorgante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mioPk;

    private String mioCodigo;

    private String mioNombre;

    private Boolean mioHabilitado;

    private LocalDateTime mioUltModFecha;

    private String mioUltModUsuario;

    private Integer mioVersion;
    
    
    public SgInfMinisterioOtorgante() {
        this.mioHabilitado = Boolean.TRUE;
    }

    public Long getMioPk() {
        return mioPk;
    }

    public void setMioPk(Long mioPk) {
        this.mioPk = mioPk;
    }

    public String getMioCodigo() {
        return mioCodigo;
    }

    public void setMioCodigo(String mioCodigo) {
        this.mioCodigo = mioCodigo;
    }

    public String getMioNombre() {
        return mioNombre;
    }

    public void setMioNombre(String mioNombre) {
        this.mioNombre = mioNombre;
    }

    public LocalDateTime getMioUltModFecha() {
        return mioUltModFecha;
    }

    public void setMioUltModFecha(LocalDateTime mioUltModFecha) {
        this.mioUltModFecha = mioUltModFecha;
    }

    public String getMioUltModUsuario() {
        return mioUltModUsuario;
    }

    public void setMioUltModUsuario(String mioUltModUsuario) {
        this.mioUltModUsuario = mioUltModUsuario;
    }

    public Integer getMioVersion() {
        return mioVersion;
    }

    public void setMioVersion(Integer mioVersion) {
        this.mioVersion = mioVersion;
    }

    
     public Boolean getMioHabilitado() {
        return mioHabilitado;
    }

    public void setMioHabilitado(Boolean mioHabilitado) {
        this.mioHabilitado = mioHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mioPk != null ? mioPk.hashCode() : 0);
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
        final SgInfMinisterioOtorgante other = (SgInfMinisterioOtorgante) obj;
        if (!Objects.equals(this.mioPk, other.mioPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfMinisterioOtorgante[ mioPk=" + mioPk + " ]";
    }
    
}
