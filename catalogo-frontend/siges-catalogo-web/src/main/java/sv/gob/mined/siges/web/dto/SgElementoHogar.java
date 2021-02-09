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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ehoPk", scope = SgElementoHogar.class)
public class SgElementoHogar implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ehoPk;

    private String ehoCodigo;

    private String ehoNombre;

    private Boolean ehoHabilitado;

    private LocalDateTime ehoUltModFecha;

    private String ehoUltModUsuario;

    private Integer ehoVersion;
    
    
    public SgElementoHogar() {
        this.ehoHabilitado = Boolean.TRUE;
    }

    public Long getEhoPk() {
        return ehoPk;
    }

    public void setEhoPk(Long ehoPk) {
        this.ehoPk = ehoPk;
    }

    public String getEhoCodigo() {
        return ehoCodigo;
    }

    public void setEhoCodigo(String ehoCodigo) {
        this.ehoCodigo = ehoCodigo;
    }

    public String getEhoNombre() {
        return ehoNombre;
    }

    public void setEhoNombre(String ehoNombre) {
        this.ehoNombre = ehoNombre;
    }

    public LocalDateTime getEhoUltModFecha() {
        return ehoUltModFecha;
    }

    public void setEhoUltModFecha(LocalDateTime ehoUltModFecha) {
        this.ehoUltModFecha = ehoUltModFecha;
    }

    public String getEhoUltModUsuario() {
        return ehoUltModUsuario;
    }

    public void setEhoUltModUsuario(String ehoUltModUsuario) {
        this.ehoUltModUsuario = ehoUltModUsuario;
    }

    public Integer getEhoVersion() {
        return ehoVersion;
    }

    public void setEhoVersion(Integer ehoVersion) {
        this.ehoVersion = ehoVersion;
    }

    
     public Boolean getEhoHabilitado() {
        return ehoHabilitado;
    }

    public void setEhoHabilitado(Boolean ehoHabilitado) {
        this.ehoHabilitado = ehoHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ehoPk != null ? ehoPk.hashCode() : 0);
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
        final SgElementoHogar other = (SgElementoHogar) obj;
        if (!Objects.equals(this.ehoPk, other.ehoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgElementoHogar[ ehoPk=" + ehoPk + " ]";
    }
    
}
