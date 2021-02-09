/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ocuPk", scope = SgOcupacion.class)
public class SgOcupacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ocuPk;

    private String ocuCodigo;

    private String ocuNombre;

    private Boolean ocuHabilitado;

    private LocalDateTime ocuUltModFecha;

    private String ocuUltModUsuario;

    private Integer ocuVersion;

    public SgOcupacion() {
        this.ocuHabilitado = Boolean.TRUE;
    }

    public Long getOcuPk() {
        return ocuPk;
    }

    public void setOcuPk(Long ocuPk) {
        this.ocuPk = ocuPk;
    }

    public String getOcuCodigo() {
        return ocuCodigo;
    }

    public void setOcuCodigo(String ocuCodigo) {
        this.ocuCodigo = ocuCodigo;
    }

    public String getOcuNombre() {
        return ocuNombre;
    }

    public void setOcuNombre(String ocuNombre) {
        this.ocuNombre = ocuNombre;
    }

    public LocalDateTime getOcuUltModFecha() {
        return ocuUltModFecha;
    }

    public void setOcuUltModFecha(LocalDateTime ocuUltModFecha) {
        this.ocuUltModFecha = ocuUltModFecha;
    }

    public String getOcuUltModUsuario() {
        return ocuUltModUsuario;
    }

    public void setOcuUltModUsuario(String ocuUltModUsuario) {
        this.ocuUltModUsuario = ocuUltModUsuario;
    }

    public Integer getOcuVersion() {
        return ocuVersion;
    }

    public void setOcuVersion(Integer ocuVersion) {
        this.ocuVersion = ocuVersion;
    }

    public Boolean getOcuHabilitado() {
        return ocuHabilitado;
    }

    public void setOcuHabilitado(Boolean ocuHabilitado) {
        this.ocuHabilitado = ocuHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocuPk != null ? ocuPk.hashCode() : 0);
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
        final SgOcupacion other = (SgOcupacion) obj;
        if (!Objects.equals(this.ocuPk, other.ocuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgOcupacion[ ocuPk=" + ocuPk + " ]";
    }

}
