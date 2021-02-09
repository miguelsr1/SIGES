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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticPk", scope = SgInfTipologiaConstruccion.class)
public class SgInfTipologiaConstruccion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ticPk;

    private String ticCodigo;

    private String ticNombre;

    private Boolean ticHabilitado;

    private LocalDateTime ticUltModFecha;

    private String ticUltModUsuario;

    private Integer ticVersion;
    
    
    public SgInfTipologiaConstruccion() {
        this.ticHabilitado = Boolean.TRUE;
    }

    public Long getTicPk() {
        return ticPk;
    }

    public void setTicPk(Long ticPk) {
        this.ticPk = ticPk;
    }

    public String getTicCodigo() {
        return ticCodigo;
    }

    public void setTicCodigo(String ticCodigo) {
        this.ticCodigo = ticCodigo;
    }

    public String getTicNombre() {
        return ticNombre;
    }

    public void setTicNombre(String ticNombre) {
        this.ticNombre = ticNombre;
    }

    public LocalDateTime getTicUltModFecha() {
        return ticUltModFecha;
    }

    public void setTicUltModFecha(LocalDateTime ticUltModFecha) {
        this.ticUltModFecha = ticUltModFecha;
    }

    public String getTicUltModUsuario() {
        return ticUltModUsuario;
    }

    public void setTicUltModUsuario(String ticUltModUsuario) {
        this.ticUltModUsuario = ticUltModUsuario;
    }

    public Integer getTicVersion() {
        return ticVersion;
    }

    public void setTicVersion(Integer ticVersion) {
        this.ticVersion = ticVersion;
    }

    
     public Boolean getTicHabilitado() {
        return ticHabilitado;
    }

    public void setTicHabilitado(Boolean ticHabilitado) {
        this.ticHabilitado = ticHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ticPk != null ? ticPk.hashCode() : 0);
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
        final SgInfTipologiaConstruccion other = (SgInfTipologiaConstruccion) obj;
        if (!Objects.equals(this.ticPk, other.ticPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfTipologiaConstruccion[ ticPk=" + ticPk + " ]";
    }
    
}
