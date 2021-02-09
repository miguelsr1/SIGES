/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "copPk", scope = SgCategoriaOperacion.class)
public class SgCategoriaOperacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long copPk;

    private String copCodigo;

    private String copNombre;
    
    private String copDescripcion;

    private Boolean copHabilitado;

    private LocalDateTime copUltModFecha;

    private String copUltModUsuario;

    private Integer copVersion;
    
    private List<SgOperacion> copOperacion;
    
    
    public SgCategoriaOperacion() {
        this.copHabilitado = Boolean.TRUE;
    }

    public Long getCopPk() {
        return copPk;
    }

    public void setCopPk(Long copPk) {
        this.copPk = copPk;
    }

    public String getCopCodigo() {
        return copCodigo;
    }

    public void setCopCodigo(String copCodigo) {
        this.copCodigo = copCodigo;
    }

    public String getCopNombre() {
        return copNombre;
    }

    public void setCopNombre(String copNombre) {
        this.copNombre = copNombre;
    }

    public String getCopDescripcion() {
        return copDescripcion;
    }

    public void setCopDescripcion(String copDescripcion) {
        this.copDescripcion = copDescripcion;
    }

    public LocalDateTime getCopUltModFecha() {
        return copUltModFecha;
    }

    public void setCopUltModFecha(LocalDateTime copUltModFecha) {
        this.copUltModFecha = copUltModFecha;
    }

    public String getCopUltModUsuario() {
        return copUltModUsuario;
    }

    public void setCopUltModUsuario(String copUltModUsuario) {
        this.copUltModUsuario = copUltModUsuario;
    }

    public Integer getCopVersion() {
        return copVersion;
    }

    public void setCopVersion(Integer copVersion) {
        this.copVersion = copVersion;
    }

    
     public Boolean getCopHabilitado() {
        return copHabilitado;
    }

    public void setCopHabilitado(Boolean copHabilitado) {
        this.copHabilitado = copHabilitado;
    }

    public List<SgOperacion> getCopOperacion() {
        return copOperacion;
    }

    public void setCopOperacion(List<SgOperacion> copOperacion) {
        this.copOperacion = copOperacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (copPk != null ? copPk.hashCode() : 0);
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
        final SgCategoriaOperacion other = (SgCategoriaOperacion) obj;
        if (!Objects.equals(this.copPk, other.copPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCategoriaOperacion[ copPk=" + copPk + " ]";
    }
    
}
