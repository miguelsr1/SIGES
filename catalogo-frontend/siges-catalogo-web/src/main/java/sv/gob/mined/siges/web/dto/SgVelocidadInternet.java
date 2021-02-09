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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "vinPk", scope = SgVelocidadInternet.class)
public class SgVelocidadInternet implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long vinPk;

    private String vinCodigo;

    private String vinNombre;

    private Boolean vinHabilitado;

    private LocalDateTime vinUltModFecha;

    private String vinUltModUsuario;

    private Integer vinVersion;
    
    
    public SgVelocidadInternet() {
        this.vinHabilitado = Boolean.TRUE;
    }

    public Long getVinPk() {
        return vinPk;
    }

    public void setVinPk(Long vinPk) {
        this.vinPk = vinPk;
    }

    public String getVinCodigo() {
        return vinCodigo;
    }

    public void setVinCodigo(String vinCodigo) {
        this.vinCodigo = vinCodigo;
    }

    public String getVinNombre() {
        return vinNombre;
    }

    public void setVinNombre(String vinNombre) {
        this.vinNombre = vinNombre;
    }

    public LocalDateTime getVinUltModFecha() {
        return vinUltModFecha;
    }

    public void setVinUltModFecha(LocalDateTime vinUltModFecha) {
        this.vinUltModFecha = vinUltModFecha;
    }

    public String getVinUltModUsuario() {
        return vinUltModUsuario;
    }

    public void setVinUltModUsuario(String vinUltModUsuario) {
        this.vinUltModUsuario = vinUltModUsuario;
    }

    public Integer getVinVersion() {
        return vinVersion;
    }

    public void setVinVersion(Integer vinVersion) {
        this.vinVersion = vinVersion;
    }

    
     public Boolean getVinHabilitado() {
        return vinHabilitado;
    }

    public void setVinHabilitado(Boolean vinHabilitado) {
        this.vinHabilitado = vinHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vinPk != null ? vinPk.hashCode() : 0);
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
        final SgVelocidadInternet other = (SgVelocidadInternet) obj;
        if (!Objects.equals(this.vinPk, other.vinPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgVelocidadInternet[ vinPk=" + vinPk + " ]";
    }
    
}
