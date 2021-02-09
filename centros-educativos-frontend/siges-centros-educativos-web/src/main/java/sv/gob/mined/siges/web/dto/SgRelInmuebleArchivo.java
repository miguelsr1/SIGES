/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "riaPk", scope = SgRelInmuebleArchivo.class)
public class SgRelInmuebleArchivo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long riaPk;

    private LocalDateTime riaUltModFecha;

    private String riaUltModUsuario;

    private Integer riaVersion;
    
   private SgInmueblesSedes riaInmuebleSede;

    private SgArchivo riaArchivo;
    
    
    public SgRelInmuebleArchivo() {
   
    }

    public Long getRiaPk() {
        return riaPk;
    }

    public void setRiaPk(Long riaPk) {
        this.riaPk = riaPk;
    }

  
    public LocalDateTime getRiaUltModFecha() {
        return riaUltModFecha;
    }

    public void setRiaUltModFecha(LocalDateTime riaUltModFecha) {
        this.riaUltModFecha = riaUltModFecha;
    }

    public String getRiaUltModUsuario() {
        return riaUltModUsuario;
    }

    public void setRiaUltModUsuario(String riaUltModUsuario) {
        this.riaUltModUsuario = riaUltModUsuario;
    }

    public Integer getRiaVersion() {
        return riaVersion;
    }

    public void setRiaVersion(Integer riaVersion) {
        this.riaVersion = riaVersion;
    }
    
    public SgInmueblesSedes getRiaInmuebleSede() {
        return riaInmuebleSede;
    }

    public void setRiaInmuebleSede(SgInmueblesSedes riaInmuebleSede) {
        this.riaInmuebleSede = riaInmuebleSede;
    }

    public SgArchivo getRiaArchivo() {
        return riaArchivo;
    }

    public void setRiaArchivo(SgArchivo riaArchivo) {
        this.riaArchivo = riaArchivo;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riaPk != null ? riaPk.hashCode() : 0);
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
        final SgRelInmuebleArchivo other = (SgRelInmuebleArchivo) obj;
        if (!Objects.equals(this.riaPk, other.riaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleArchivo[ riaPk=" + riaPk + " ]";
    }
    
}
