/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sinPk", scope = SgServicioInfraestructura.class)
public class SgServicioInfraestructura implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sinPk;

    private String sinCodigo;

    private String sinNombre;

    private Boolean sinHabilitado;

    private LocalDateTime sinUltModFecha;

    private String sinUltModUsuario;

    private Integer sinVersion;

    public SgServicioInfraestructura() {
        this.sinHabilitado = Boolean.TRUE;
    }

    public Long getSinPk() {
        return sinPk;
    }

    public void setSinPk(Long sinPk) {
        this.sinPk = sinPk;
    }

    public String getSinCodigo() {
        return sinCodigo;
    }

    public void setSinCodigo(String sinCodigo) {
        this.sinCodigo = sinCodigo;
    }

    public String getSinNombre() {
        return sinNombre;
    }

    public void setSinNombre(String sinNombre) {
        this.sinNombre = sinNombre;
    }

    public LocalDateTime getSinUltModFecha() {
        return sinUltModFecha;
    }

    public void setSinUltModFecha(LocalDateTime sinUltModFecha) {
        this.sinUltModFecha = sinUltModFecha;
    }

    public String getSinUltModUsuario() {
        return sinUltModUsuario;
    }

    public void setSinUltModUsuario(String sinUltModUsuario) {
        this.sinUltModUsuario = sinUltModUsuario;
    }

    public Integer getSinVersion() {
        return sinVersion;
    }

    public void setSinVersion(Integer sinVersion) {
        this.sinVersion = sinVersion;
    }

    public Boolean getSinHabilitado() {
        return sinHabilitado;
    }

    public void setSinHabilitado(Boolean sinHabilitado) {
        this.sinHabilitado = sinHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sinPk != null ? sinPk.hashCode() : 0);
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
        final SgServicioInfraestructura other = (SgServicioInfraestructura) obj;
        if (!Objects.equals(this.sinPk, other.sinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgServicioInfraestructura[ sinPk=" + sinPk + " ]";
    }

}
