/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pedPk", scope = SgProgramaEducativo.class) 
public class SgProgramaEducativo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long pedPk;
   
    private String pedCodigo;

    private Boolean pedHabilitado;

    private String pedNombre;

    private String pedNombreBusqueda;
 
    private String pedDescripcion;

    private LocalDateTime pedUltModFecha;
  
    private String pedUltModUsuario;
    
    private Integer pedVersion;
    
    
    public SgProgramaEducativo() {
        this.pedHabilitado = Boolean.TRUE;
    }

    public Long getPedPk() {
        return pedPk;
    }

    public void setPedPk(Long pedPk) {
        this.pedPk = pedPk;
    }

    public String getPedCodigo() {
        return pedCodigo;
    }

    public void setPedCodigo(String pedCodigo) {
        this.pedCodigo = pedCodigo;
    }

    public Boolean getPedHabilitado() {
        return pedHabilitado;
    }

    public void setPedHabilitado(Boolean pedHabilitado) {
        this.pedHabilitado = pedHabilitado;
    }

    public String getPedNombre() {
        return pedNombre;
    }

    public void setPedNombre(String pedNombre) {
        this.pedNombre = pedNombre;
    }

    public String getPedNombreBusqueda() {
        return pedNombreBusqueda;
    }

    public void setPedNombreBusqueda(String pedNombreBusqueda) {
        this.pedNombreBusqueda = pedNombreBusqueda;
    }

    public String getPedDescripcion() {
        return pedDescripcion;
    }

    public void setPedDescripcion(String pedDescripcion) {
        this.pedDescripcion = pedDescripcion;
    }

    public LocalDateTime getPedUltModFecha() {
        return pedUltModFecha;
    }

    public void setPedUltModFecha(LocalDateTime pedUltModFecha) {
        this.pedUltModFecha = pedUltModFecha;
    }

    public String getPedUltModUsuario() {
        return pedUltModUsuario;
    }

    public void setPedUltModUsuario(String pedUltModUsuario) {
        this.pedUltModUsuario = pedUltModUsuario;
    }

    public Integer getPedVersion() {
        return pedVersion;
    }

    public void setPedVersion(Integer pedVersion) {
        this.pedVersion = pedVersion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedPk != null ? pedPk.hashCode() : 0);
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
        final SgProgramaEducativo other = (SgProgramaEducativo) obj;
        if (!Objects.equals(this.pedPk, other.pedPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgProgramasEducativos[ pedPk=" + pedPk + " ]";
    }
    
}
