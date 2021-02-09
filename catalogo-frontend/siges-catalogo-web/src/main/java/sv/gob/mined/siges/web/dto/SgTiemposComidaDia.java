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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcdPk", scope = SgTiemposComidaDia.class)
public class SgTiemposComidaDia implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tcdPk;

    private String tcdCodigo;

    private String tcdNombre;

    private Boolean tcdHabilitado;

    private LocalDateTime tcdUltModFecha;

    private String tcdUltModUsuario;

    private Integer tcdVersion;
    
    
    public SgTiemposComidaDia() {
        this.tcdHabilitado = Boolean.TRUE;
    }

    public Long getTcdPk() {
        return tcdPk;
    }

    public void setTcdPk(Long tcdPk) {
        this.tcdPk = tcdPk;
    }

    public String getTcdCodigo() {
        return tcdCodigo;
    }

    public void setTcdCodigo(String tcdCodigo) {
        this.tcdCodigo = tcdCodigo;
    }

    public String getTcdNombre() {
        return tcdNombre;
    }

    public void setTcdNombre(String tcdNombre) {
        this.tcdNombre = tcdNombre;
    }

    public LocalDateTime getTcdUltModFecha() {
        return tcdUltModFecha;
    }

    public void setTcdUltModFecha(LocalDateTime tcdUltModFecha) {
        this.tcdUltModFecha = tcdUltModFecha;
    }

    public String getTcdUltModUsuario() {
        return tcdUltModUsuario;
    }

    public void setTcdUltModUsuario(String tcdUltModUsuario) {
        this.tcdUltModUsuario = tcdUltModUsuario;
    }

    public Integer getTcdVersion() {
        return tcdVersion;
    }

    public void setTcdVersion(Integer tcdVersion) {
        this.tcdVersion = tcdVersion;
    }

    
     public Boolean getTcdHabilitado() {
        return tcdHabilitado;
    }

    public void setTcdHabilitado(Boolean tcdHabilitado) {
        this.tcdHabilitado = tcdHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcdPk != null ? tcdPk.hashCode() : 0);
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
        final SgTiemposComidaDia other = (SgTiemposComidaDia) obj;
        if (!Objects.equals(this.tcdPk, other.tcdPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTiemposComidaDia[ tcdPk=" + tcdPk + " ]";
    }
    
}
