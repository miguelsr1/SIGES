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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "insPk", scope = SgInstitucion.class)
public class SgInstitucion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long insPk;

    private String insCodigo;

    private String insNombre;

    private Boolean insHabilitado;

    private LocalDateTime insUltModFecha;

    private String insUltModUsuario;

    private Integer insVersion;

    public SgInstitucion() {
        this.insHabilitado = Boolean.TRUE;
    }

    public Long getInsPk() {
        return insPk;
    }

    public void setInsPk(Long insPk) {
        this.insPk = insPk;
    }

    public String getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(String insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public Boolean getInsHabilitado() {
        return insHabilitado;
    }

    public void setInsHabilitado(Boolean insHabilitado) {
        this.insHabilitado = insHabilitado;
    }

    public LocalDateTime getInsUltModFecha() {
        return insUltModFecha;
    }

    public void setInsUltModFecha(LocalDateTime insUltModFecha) {
        this.insUltModFecha = insUltModFecha;
    }

    public String getInsUltModUsuario() {
        return insUltModUsuario;
    }

    public void setInsUltModUsuario(String insUltModUsuario) {
        this.insUltModUsuario = insUltModUsuario;
    }

    public Integer getInsVersion() {
        return insVersion;
    }

    public void setInsVersion(Integer insVersion) {
        this.insVersion = insVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insPk != null ? insPk.hashCode() : 0);
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
        final SgInstitucion other = (SgInstitucion) obj;
        if (!Objects.equals(this.insPk, other.insPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgInstitucion[ insPk=" + insPk + " ]";
    }

}
