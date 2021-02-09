/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "claPk", scope = SgClasificacion.class)
public class SgClasificacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long claPk;

    private String claCodigo;

    private String claNombre;
    
    private String claNombreBusqueda;

    private Boolean claHabilitado;

    private LocalDateTime claUltModFecha;

    private String claUltModUsuario;

    private Integer claVersion;
    
    
    public SgClasificacion() {
        this.claHabilitado = Boolean.TRUE;
    }

    public Long getClaPk() {
        return claPk;
    }

    public void setClaPk(Long claPk) {
        this.claPk = claPk;
    }

    public String getClaCodigo() {
        return claCodigo;
    }

    public void setClaCodigo(String claCodigo) {
        this.claCodigo = claCodigo;
    }

    public String getClaNombre() {
        return claNombre;
    }

    public void setClaNombre(String claNombre) {
        this.claNombre = claNombre;
    }

    public String getClaNombreBusqueda() {
        return claNombreBusqueda;
    }

    public void setClaNombreBusqueda(String claNombreBusqueda) {
        this.claNombreBusqueda = claNombreBusqueda;
    }

    public Boolean getClaHabilitado() {
        return claHabilitado;
    }

    public void setClaHabilitado(Boolean claHabilitado) {
        this.claHabilitado = claHabilitado;
    }

    public LocalDateTime getClaUltModFecha() {
        return claUltModFecha;
    }

    public void setClaUltModFecha(LocalDateTime claUltModFecha) {
        this.claUltModFecha = claUltModFecha;
    }

    public String getClaUltModUsuario() {
        return claUltModUsuario;
    }

    public void setClaUltModUsuario(String claUltModUsuario) {
        this.claUltModUsuario = claUltModUsuario;
    }

    public Integer getClaVersion() {
        return claVersion;
    }

    public void setClaVersion(Integer claVersion) {
        this.claVersion = claVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claPk != null ? claPk.hashCode() : 0);
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
        final SgClasificacion other = (SgClasificacion) obj;
        if (!Objects.equals(this.claPk, other.claPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgClasificacion[ claPk=" + claPk + " ]";
    }
    
}
