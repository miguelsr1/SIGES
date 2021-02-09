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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ccuPk", scope = SgCategoriaCurso.class)
public class SgCategoriaCurso implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ccuPk;

    private String ccuCodigo;

    private String ccuNombre;

    private Boolean ccuHabilitado;

    private LocalDateTime ccuUltModFecha;

    private String ccuUltModUsuario;

    private Integer ccuVersion;
    
    
    public SgCategoriaCurso() {
        this.ccuHabilitado = Boolean.TRUE;
    }

    public Long getCcuPk() {
        return ccuPk;
    }

    public void setCcuPk(Long ccuPk) {
        this.ccuPk = ccuPk;
    }

    public String getCcuCodigo() {
        return ccuCodigo;
    }

    public void setCcuCodigo(String ccuCodigo) {
        this.ccuCodigo = ccuCodigo;
    }

    public String getCcuNombre() {
        return ccuNombre;
    }

    public void setCcuNombre(String ccuNombre) {
        this.ccuNombre = ccuNombre;
    }

    public LocalDateTime getCcuUltModFecha() {
        return ccuUltModFecha;
    }

    public void setCcuUltModFecha(LocalDateTime ccuUltModFecha) {
        this.ccuUltModFecha = ccuUltModFecha;
    }

    public String getCcuUltModUsuario() {
        return ccuUltModUsuario;
    }

    public void setCcuUltModUsuario(String ccuUltModUsuario) {
        this.ccuUltModUsuario = ccuUltModUsuario;
    }

    public Integer getCcuVersion() {
        return ccuVersion;
    }

    public void setCcuVersion(Integer ccuVersion) {
        this.ccuVersion = ccuVersion;
    }

    
     public Boolean getCcuHabilitado() {
        return ccuHabilitado;
    }

    public void setCcuHabilitado(Boolean ccuHabilitado) {
        this.ccuHabilitado = ccuHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccuPk != null ? ccuPk.hashCode() : 0);
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
        final SgCategoriaCurso other = (SgCategoriaCurso) obj;
        if (!Objects.equals(this.ccuPk, other.ccuPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCategoriaCurso[ ccuPk=" + ccuPk + " ]";
    }
    
}
