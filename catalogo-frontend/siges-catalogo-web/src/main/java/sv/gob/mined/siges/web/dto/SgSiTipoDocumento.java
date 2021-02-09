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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tidPk", scope = SgSiTipoDocumento.class)
public class SgSiTipoDocumento implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tidPk;

    private String tidCodigo;

    private String tidNombre;

    private Boolean tidHabilitado;

    private LocalDateTime tidUltModFecha;

    private String tidUltModUsuario;

    private Integer tidVersion;

    public Long getTidPk() {
        return tidPk;
    }

    public void setTidPk(Long tidPk) {
        this.tidPk = tidPk;
    }

    public String getTidCodigo() {
        return tidCodigo;
    }

    public void setTidCodigo(String tidCodigo) {
        this.tidCodigo = tidCodigo;
    }

    public String getTidNombre() {
        return tidNombre;
    }

    public void setTidNombre(String tidNombre) {
        this.tidNombre = tidNombre;
    }

    public Boolean getTidHabilitado() {
        return tidHabilitado;
    }

    public void setTidHabilitado(Boolean tidHabilitado) {
        this.tidHabilitado = tidHabilitado;
    }

    public LocalDateTime getTidUltModFecha() {
        return tidUltModFecha;
    }

    public void setTidUltModFecha(LocalDateTime tidUltModFecha) {
        this.tidUltModFecha = tidUltModFecha;
    }

    public String getTidUltModUsuario() {
        return tidUltModUsuario;
    }

    public void setTidUltModUsuario(String tidUltModUsuario) {
        this.tidUltModUsuario = tidUltModUsuario;
    }

    public Integer getTidVersion() {
        return tidVersion;
    }

    public void setTidVersion(Integer tidVersion) {
        this.tidVersion = tidVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tidPk != null ? tidPk.hashCode() : 0);
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
        final SgSiTipoDocumento other = (SgSiTipoDocumento) obj;
        if (!Objects.equals(this.tidPk, other.tidPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfTipoDocumento[ tidPk=" + tidPk + " ]";
    }
    
}
