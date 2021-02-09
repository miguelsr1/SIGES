/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ttrPk", scope = SgTipoTrabajo.class)
public class SgTipoTrabajo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ttrPk;

    private String ttrCodigo;

    private String ttrNombre;

    private Boolean ttrHabilitado;

    private LocalDateTime ttrUltModFecha;

    private String ttrUltModUsuario;

    private Integer ttrVersion;
    
    
    public SgTipoTrabajo() {
        this.ttrHabilitado = Boolean.TRUE;
    }

    public Long getTtrPk() {
        return ttrPk;
    }

    public void setTtrPk(Long ttrPk) {
        this.ttrPk = ttrPk;
    }

    public String getTtrCodigo() {
        return ttrCodigo;
    }

    public void setTtrCodigo(String ttrCodigo) {
        this.ttrCodigo = ttrCodigo;
    }

    public String getTtrNombre() {
        return ttrNombre;
    }

    public void setTtrNombre(String ttrNombre) {
        this.ttrNombre = ttrNombre;
    }

    public LocalDateTime getTtrUltModFecha() {
        return ttrUltModFecha;
    }

    public void setTtrUltModFecha(LocalDateTime ttrUltModFecha) {
        this.ttrUltModFecha = ttrUltModFecha;
    }

    public String getTtrUltModUsuario() {
        return ttrUltModUsuario;
    }

    public void setTtrUltModUsuario(String ttrUltModUsuario) {
        this.ttrUltModUsuario = ttrUltModUsuario;
    }

    public Integer getTtrVersion() {
        return ttrVersion;
    }

    public void setTtrVersion(Integer ttrVersion) {
        this.ttrVersion = ttrVersion;
    }

    
     public Boolean getTtrHabilitado() {
        return ttrHabilitado;
    }

    public void setTtrHabilitado(Boolean ttrHabilitado) {
        this.ttrHabilitado = ttrHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ttrPk != null ? ttrPk.hashCode() : 0);
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
        final SgTipoTrabajo other = (SgTipoTrabajo) obj;
        if (!Objects.equals(this.ttrPk, other.ttrPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoTrabajo[ ttrPk=" + ttrPk + " ]";
    }
    
}
