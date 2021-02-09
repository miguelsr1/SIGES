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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nexPk", scope = SgEstNombreExtraccion.class)
public class SgEstNombreExtraccion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long nexPk;

    private String nexCodigo;

    private String nexNombre;

    private Boolean nexHabilitado;

    private LocalDateTime nexUltModFecha;

    private String nexUltModUsuario;

    private Integer nexVersion;
    
    
    public SgEstNombreExtraccion() {
        this.nexHabilitado = Boolean.TRUE;
    }

    public Long getNexPk() {
        return nexPk;
    }

    public void setNexPk(Long nexPk) {
        this.nexPk = nexPk;
    }

    public String getNexCodigo() {
        return nexCodigo;
    }

    public void setNexCodigo(String nexCodigo) {
        this.nexCodigo = nexCodigo;
    }

    public String getNexNombre() {
        return nexNombre;
    }

    public void setNexNombre(String nexNombre) {
        this.nexNombre = nexNombre;
    }

    public LocalDateTime getNexUltModFecha() {
        return nexUltModFecha;
    }

    public void setNexUltModFecha(LocalDateTime nexUltModFecha) {
        this.nexUltModFecha = nexUltModFecha;
    }

    public String getNexUltModUsuario() {
        return nexUltModUsuario;
    }

    public void setNexUltModUsuario(String nexUltModUsuario) {
        this.nexUltModUsuario = nexUltModUsuario;
    }

    public Integer getNexVersion() {
        return nexVersion;
    }

    public void setNexVersion(Integer nexVersion) {
        this.nexVersion = nexVersion;
    }

    
     public Boolean getNexHabilitado() {
        return nexHabilitado;
    }

    public void setNexHabilitado(Boolean nexHabilitado) {
        this.nexHabilitado = nexHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nexPk != null ? nexPk.hashCode() : 0);
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
        final SgEstNombreExtraccion other = (SgEstNombreExtraccion) obj;
        if (!Objects.equals(this.nexPk, other.nexPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgNombreExtracciones[ nexPk=" + nexPk + " ]";
    }
    
}
