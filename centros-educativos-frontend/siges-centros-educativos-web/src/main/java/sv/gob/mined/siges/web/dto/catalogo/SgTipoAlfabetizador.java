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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "talPk", scope = SgTipoAlfabetizador.class)
public class SgTipoAlfabetizador implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long talPk;

    private String talCodigo;

    private String talNombre;

    private Boolean talHabilitado;

    private LocalDateTime talUltModFecha;

    private String talUltModUsuario;

    private Integer talVersion;
    
    
    public SgTipoAlfabetizador() {
        this.talHabilitado = Boolean.TRUE;
    }

    public Long getTalPk() {
        return talPk;
    }

    public void setTalPk(Long talPk) {
        this.talPk = talPk;
    }

    public String getTalCodigo() {
        return talCodigo;
    }

    public void setTalCodigo(String talCodigo) {
        this.talCodigo = talCodigo;
    }

    public String getTalNombre() {
        return talNombre;
    }

    public void setTalNombre(String talNombre) {
        this.talNombre = talNombre;
    }

    public LocalDateTime getTalUltModFecha() {
        return talUltModFecha;
    }

    public void setTalUltModFecha(LocalDateTime talUltModFecha) {
        this.talUltModFecha = talUltModFecha;
    }

    public String getTalUltModUsuario() {
        return talUltModUsuario;
    }

    public void setTalUltModUsuario(String talUltModUsuario) {
        this.talUltModUsuario = talUltModUsuario;
    }

    public Integer getTalVersion() {
        return talVersion;
    }

    public void setTalVersion(Integer talVersion) {
        this.talVersion = talVersion;
    }

    
     public Boolean getTalHabilitado() {
        return talHabilitado;
    }

    public void setTalHabilitado(Boolean talHabilitado) {
        this.talHabilitado = talHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (talPk != null ? talPk.hashCode() : 0);
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
        final SgTipoAlfabetizador other = (SgTipoAlfabetizador) obj;
        if (!Objects.equals(this.talPk, other.talPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoAlfabetizador[ talPk=" + talPk + " ]";
    }
    
}
