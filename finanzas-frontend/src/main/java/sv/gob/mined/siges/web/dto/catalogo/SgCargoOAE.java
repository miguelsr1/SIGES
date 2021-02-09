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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "coaPk", scope = SgCargoOAE.class)
public class SgCargoOAE implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long coaPk;

    private String coaCodigo;

    private String coaNombre;

    private Boolean coaHabilitado;

    private LocalDateTime coaUltModFecha;

    private String coaUltModUsuario;

    private Integer coaVersion;
    
    
    public SgCargoOAE() {
        this.coaHabilitado = Boolean.TRUE;
    }

    public Long getCoaPk() {
        return coaPk;
    }

    public void setCoaPk(Long coaPk) {
        this.coaPk = coaPk;
    }

    public String getCoaCodigo() {
        return coaCodigo;
    }

    public void setCoaCodigo(String coaCodigo) {
        this.coaCodigo = coaCodigo;
    }

    public String getCoaNombre() {
        return coaNombre;
    }

    public void setCoaNombre(String coaNombre) {
        this.coaNombre = coaNombre;
    }

    public LocalDateTime getCoaUltModFecha() {
        return coaUltModFecha;
    }

    public void setCoaUltModFecha(LocalDateTime coaUltModFecha) {
        this.coaUltModFecha = coaUltModFecha;
    }

    public String getCoaUltModUsuario() {
        return coaUltModUsuario;
    }

    public void setCoaUltModUsuario(String coaUltModUsuario) {
        this.coaUltModUsuario = coaUltModUsuario;
    }

    public Integer getCoaVersion() {
        return coaVersion;
    }

    public void setCoaVersion(Integer coaVersion) {
        this.coaVersion = coaVersion;
    }

    
     public Boolean getCoaHabilitado() {
        return coaHabilitado;
    }

    public void setCoaHabilitado(Boolean coaHabilitado) {
        this.coaHabilitado = coaHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coaPk != null ? coaPk.hashCode() : 0);
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
        final SgCargoOAE other = (SgCargoOAE) obj;
        if (!Objects.equals(this.coaPk, other.coaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCargoOAE[ coaPk=" + coaPk + " ]";
    }
    
}
