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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cavPk", scope = SgCategoriaViolencia.class)
public class SgCategoriaViolencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cavPk;

    private String cavCodigo;

    private String cavNombre;

    private Boolean cavHabilitado;

    private LocalDateTime cavUltModFecha;

    private String cavUltModUsuario;

    private Integer cavVersion;
    
    
    public SgCategoriaViolencia() {
        this.cavHabilitado = Boolean.TRUE;
    }

    public Long getCavPk() {
        return cavPk;
    }

    public void setCavPk(Long cavPk) {
        this.cavPk = cavPk;
    }

    public String getCavCodigo() {
        return cavCodigo;
    }

    public void setCavCodigo(String cavCodigo) {
        this.cavCodigo = cavCodigo;
    }

    public String getCavNombre() {
        return cavNombre;
    }

    public void setCavNombre(String cavNombre) {
        this.cavNombre = cavNombre;
    }

    public LocalDateTime getCavUltModFecha() {
        return cavUltModFecha;
    }

    public void setCavUltModFecha(LocalDateTime cavUltModFecha) {
        this.cavUltModFecha = cavUltModFecha;
    }

    public String getCavUltModUsuario() {
        return cavUltModUsuario;
    }

    public void setCavUltModUsuario(String cavUltModUsuario) {
        this.cavUltModUsuario = cavUltModUsuario;
    }

    public Integer getCavVersion() {
        return cavVersion;
    }

    public void setCavVersion(Integer cavVersion) {
        this.cavVersion = cavVersion;
    }

    
     public Boolean getCavHabilitado() {
        return cavHabilitado;
    }

    public void setCavHabilitado(Boolean cavHabilitado) {
        this.cavHabilitado = cavHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cavPk != null ? cavPk.hashCode() : 0);
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
        final SgCategoriaViolencia other = (SgCategoriaViolencia) obj;
        if (!Objects.equals(this.cavPk, other.cavPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCategoriaViolencia[ cavPk=" + cavPk + " ]";
    }
    
}
