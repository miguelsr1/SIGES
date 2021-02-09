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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pdtPk", scope = SgPropietariosTerreno.class)
public class SgPropietariosTerreno implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long pdtPk;

    private String pdtCodigo;

    private String pdtNombre;

    private Boolean pdtHabilitado;

    private LocalDateTime pdtUltModFecha;

    private String pdtUltModUsuario;

    private Integer pdtVersion;
    
    
    public SgPropietariosTerreno() {
        this.pdtHabilitado = Boolean.TRUE;
    }

    public Long getPdtPk() {
        return pdtPk;
    }

    public void setPdtPk(Long pdtPk) {
        this.pdtPk = pdtPk;
    }

    public String getPdtCodigo() {
        return pdtCodigo;
    }

    public void setPdtCodigo(String pdtCodigo) {
        this.pdtCodigo = pdtCodigo;
    }

    public String getPdtNombre() {
        return pdtNombre;
    }

    public void setPdtNombre(String pdtNombre) {
        this.pdtNombre = pdtNombre;
    }

    public LocalDateTime getPdtUltModFecha() {
        return pdtUltModFecha;
    }

    public void setPdtUltModFecha(LocalDateTime pdtUltModFecha) {
        this.pdtUltModFecha = pdtUltModFecha;
    }

    public String getPdtUltModUsuario() {
        return pdtUltModUsuario;
    }

    public void setPdtUltModUsuario(String pdtUltModUsuario) {
        this.pdtUltModUsuario = pdtUltModUsuario;
    }

    public Integer getPdtVersion() {
        return pdtVersion;
    }

    public void setPdtVersion(Integer pdtVersion) {
        this.pdtVersion = pdtVersion;
    }

    
     public Boolean getPdtHabilitado() {
        return pdtHabilitado;
    }

    public void setPdtHabilitado(Boolean pdtHabilitado) {
        this.pdtHabilitado = pdtHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdtPk != null ? pdtPk.hashCode() : 0);
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
        final SgPropietariosTerreno other = (SgPropietariosTerreno) obj;
        if (!Objects.equals(this.pdtPk, other.pdtPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgPropietariosTerreno[ pdtPk=" + pdtPk + " ]";
    }
    
}
