/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
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

    private String pedNombre;
 
    private String pedDescripcion;

    private Integer pedVersion;
    
    
    public SgProgramaEducativo() {
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

    public String getPedNombre() {
        return pedNombre;
    }

    public void setPedNombre(String pedNombre) {
        this.pedNombre = pedNombre;
    }

    public String getPedDescripcion() {
        return pedDescripcion;
    }

    public void setPedDescripcion(String pedDescripcion) {
        this.pedDescripcion = pedDescripcion;
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
