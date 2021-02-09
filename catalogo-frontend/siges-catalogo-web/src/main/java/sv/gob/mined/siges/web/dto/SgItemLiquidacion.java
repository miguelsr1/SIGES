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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iliPk", scope = SgItemLiquidacion.class)
public class SgItemLiquidacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long iliPk;

    private Integer iliCodigo;

    private String iliNombre;

    private Boolean iliHabilitado;

    private LocalDateTime iliUltModFecha;

    private String iliUltModUsuario;

    private Integer iliVersion;
    
    
    public SgItemLiquidacion() {
        this.iliHabilitado = Boolean.TRUE;
    }

    public Long getIliPk() {
        return iliPk;
    }

    public void setIliPk(Long iliPk) {
        this.iliPk = iliPk;
    }

    public Integer getIliCodigo() {
        return iliCodigo;
    }

    public void setIliCodigo(Integer iliCodigo) {
        this.iliCodigo = iliCodigo;
    }

    public String getIliNombre() {
        return iliNombre;
    }

    public void setIliNombre(String iliNombre) {
        this.iliNombre = iliNombre;
    }

    public LocalDateTime getIliUltModFecha() {
        return iliUltModFecha;
    }

    public void setIliUltModFecha(LocalDateTime iliUltModFecha) {
        this.iliUltModFecha = iliUltModFecha;
    }

    public String getIliUltModUsuario() {
        return iliUltModUsuario;
    }

    public void setIliUltModUsuario(String iliUltModUsuario) {
        this.iliUltModUsuario = iliUltModUsuario;
    }

    public Integer getIliVersion() {
        return iliVersion;
    }

    public void setIliVersion(Integer iliVersion) {
        this.iliVersion = iliVersion;
    }

    
     public Boolean getIliHabilitado() {
        return iliHabilitado;
    }

    public void setIliHabilitado(Boolean iliHabilitado) {
        this.iliHabilitado = iliHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iliPk != null ? iliPk.hashCode() : 0);
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
        final SgItemLiquidacion other = (SgItemLiquidacion) obj;
        if (!Objects.equals(this.iliPk, other.iliPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgItemLiquidacion[ iliPk=" + iliPk + " ]";
    }
    
}
