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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oexPk", scope = SgInfObraExterior.class)
public class SgInfObraExterior implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long oexPk;

    private String oexCodigo;

    private String oexNombre;

    private Boolean oexHabilitado;

    private LocalDateTime oexUltModFecha;

    private String oexUltModUsuario;

    private Integer oexVersion;
    
    private Boolean oexAplicaOtros;
    
    private Integer oexOrden;
    
    
    public SgInfObraExterior() {
        this.oexHabilitado = Boolean.TRUE;
    }

    public Long getOexPk() {
        return oexPk;
    }

    public void setOexPk(Long oexPk) {
        this.oexPk = oexPk;
    }

    public String getOexCodigo() {
        return oexCodigo;
    }

    public void setOexCodigo(String oexCodigo) {
        this.oexCodigo = oexCodigo;
    }

    public String getOexNombre() {
        return oexNombre;
    }

    public void setOexNombre(String oexNombre) {
        this.oexNombre = oexNombre;
    }

    public LocalDateTime getOexUltModFecha() {
        return oexUltModFecha;
    }

    public void setOexUltModFecha(LocalDateTime oexUltModFecha) {
        this.oexUltModFecha = oexUltModFecha;
    }

    public String getOexUltModUsuario() {
        return oexUltModUsuario;
    }

    public void setOexUltModUsuario(String oexUltModUsuario) {
        this.oexUltModUsuario = oexUltModUsuario;
    }

    public Integer getOexVersion() {
        return oexVersion;
    }

    public void setOexVersion(Integer oexVersion) {
        this.oexVersion = oexVersion;
    }

    
     public Boolean getOexHabilitado() {
        return oexHabilitado;
    }

    public void setOexHabilitado(Boolean oexHabilitado) {
        this.oexHabilitado = oexHabilitado;
    }

    public Boolean getOexAplicaOtros() {
        return oexAplicaOtros;
    }

    public void setOexAplicaOtros(Boolean oexAplicaOtros) {
        this.oexAplicaOtros = oexAplicaOtros;
    }

    public Integer getOexOrden() {
        return oexOrden;
    }

    public void setOexOrden(Integer oexOrden) {
        this.oexOrden = oexOrden;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oexPk != null ? oexPk.hashCode() : 0);
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
        final SgInfObraExterior other = (SgInfObraExterior) obj;
        if (!Objects.equals(this.oexPk, other.oexPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfObraExterior[ oexPk=" + oexPk + " ]";
    }
    
}
