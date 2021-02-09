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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tddPk", scope = SgTipoDocumentoDocente.class)
public class SgTipoDocumentoDocente implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tddPk;

    private String tddCodigo;

    private String tddNombre;

    private Boolean tddHabilitado;

    private LocalDateTime tddUltModFecha;

    private String tddUltModUsuario;

    private Integer tddVersion;
    
    
    public SgTipoDocumentoDocente() {
        this.tddHabilitado = Boolean.TRUE;
    }

    public Long getTddPk() {
        return tddPk;
    }

    public void setTddPk(Long tddPk) {
        this.tddPk = tddPk;
    }

    public String getTddCodigo() {
        return tddCodigo;
    }

    public void setTddCodigo(String tddCodigo) {
        this.tddCodigo = tddCodigo;
    }

    public String getTddNombre() {
        return tddNombre;
    }

    public void setTddNombre(String tddNombre) {
        this.tddNombre = tddNombre;
    }

    public LocalDateTime getTddUltModFecha() {
        return tddUltModFecha;
    }

    public void setTddUltModFecha(LocalDateTime tddUltModFecha) {
        this.tddUltModFecha = tddUltModFecha;
    }

    public String getTddUltModUsuario() {
        return tddUltModUsuario;
    }

    public void setTddUltModUsuario(String tddUltModUsuario) {
        this.tddUltModUsuario = tddUltModUsuario;
    }

    public Integer getTddVersion() {
        return tddVersion;
    }

    public void setTddVersion(Integer tddVersion) {
        this.tddVersion = tddVersion;
    }

    
     public Boolean getTddHabilitado() {
        return tddHabilitado;
    }

    public void setTddHabilitado(Boolean tddHabilitado) {
        this.tddHabilitado = tddHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tddPk != null ? tddPk.hashCode() : 0);
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
        final SgTipoDocumentoDocente other = (SgTipoDocumentoDocente) obj;
        if (!Objects.equals(this.tddPk, other.tddPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoDocumentoDocente[ tddPk=" + tddPk + " ]";
    }
    
}
