/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "entPk", scope = SgEnfermedadNoTransmisible.class)
public class SgEnfermedadNoTransmisible implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long entPk;

    private String entCodigo;

    private String entNombre;

    private String entNombreBusqueda;

    private Boolean entHabilitado;

    private LocalDateTime entUltModFecha;

    private String entUltModUsuario;

    private Integer entVersion;

    public SgEnfermedadNoTransmisible() {
        this.entHabilitado = Boolean.TRUE;
    }

    public Long getEntPk() {
        return entPk;
    }

    public void setEntPk(Long entPk) {
        this.entPk = entPk;
    }

    public String getEntCodigo() {
        return entCodigo;
    }

    public void setEntCodigo(String entCodigo) {
        this.entCodigo = entCodigo;
    }

    public String getEntNombre() {
        return entNombre;
    }

    public void setEntNombre(String entNombre) {
        this.entNombre = entNombre;
    }

    public String getEntNombreBusqueda() {
        return entNombreBusqueda;
    }

    public void setEntNombreBusqueda(String entNombreBusqueda) {
        this.entNombreBusqueda = entNombreBusqueda;
    }

    public Boolean getEntHabilitado() {
        return entHabilitado;
    }

    public void setEntHabilitado(Boolean entHabilitado) {
        this.entHabilitado = entHabilitado;
    }

    public LocalDateTime getEntUltModFecha() {
        return entUltModFecha;
    }

    public void setEntUltModFecha(LocalDateTime entUltModFecha) {
        this.entUltModFecha = entUltModFecha;
    }

    public String getEntUltModUsuario() {
        return entUltModUsuario;
    }

    public void setEntUltModUsuario(String entUltModUsuario) {
        this.entUltModUsuario = entUltModUsuario;
    }

    public Integer getEntVersion() {
        return entVersion;
    }

    public void setEntVersion(Integer entVersion) {
        this.entVersion = entVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entPk != null ? entPk.hashCode() : 0);
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
        final SgEnfermedadNoTransmisible other = (SgEnfermedadNoTransmisible) obj;
        if (!Objects.equals(this.entPk, other.entPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgEnfermedadNoTransmisible[ entPk=" + entPk + " ]";
    }

}
