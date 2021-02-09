/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "terPk", scope = SgTerapia.class)
public class SgTerapia implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long terPk;

    private String terCodigo;

    private String terNombre;

    private Boolean terHabilitado;

    private LocalDateTime terUltModFecha;

    private String terUltModUsuario;

    private Integer terVersion;
    
    
    public SgTerapia() {
        this.terHabilitado = Boolean.TRUE;
    }

    public Long getTerPk() {
        return terPk;
    }

    public void setTerPk(Long terPk) {
        this.terPk = terPk;
    }

    public String getTerCodigo() {
        return terCodigo;
    }

    public void setTerCodigo(String terCodigo) {
        this.terCodigo = terCodigo;
    }

    public String getTerNombre() {
        return terNombre;
    }

    public void setTerNombre(String terNombre) {
        this.terNombre = terNombre;
    }

    public LocalDateTime getTerUltModFecha() {
        return terUltModFecha;
    }

    public void setTerUltModFecha(LocalDateTime terUltModFecha) {
        this.terUltModFecha = terUltModFecha;
    }

    public String getTerUltModUsuario() {
        return terUltModUsuario;
    }

    public void setTerUltModUsuario(String terUltModUsuario) {
        this.terUltModUsuario = terUltModUsuario;
    }

    public Integer getTerVersion() {
        return terVersion;
    }

    public void setTerVersion(Integer terVersion) {
        this.terVersion = terVersion;
    }

    
     public Boolean getTerHabilitado() {
        return terHabilitado;
    }

    public void setTerHabilitado(Boolean terHabilitado) {
        this.terHabilitado = terHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (terPk != null ? terPk.hashCode() : 0);
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
        final SgTerapia other = (SgTerapia) obj;
        if (!Objects.equals(this.terPk, other.terPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTerapia[ terPk=" + terPk + " ]";
    }
    
}
