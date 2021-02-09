/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ocuPk", scope = SgOcupacion.class)
public class SgOcupacion implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long ocuPk;
    private String ocuCodigo;
    private String ocuNombre;
    private String ocuNombreBusqueda;
    private Boolean ocuHabilitado;
    private LocalDateTime ocuUltModFecha;
    private String ocuUltModUsuario;
    private Integer ocuVersion;

    public SgOcupacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ocuNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ocuNombre);
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

    public String getOcuNombreBusqueda() {
        return ocuNombreBusqueda;
    }

    public void setOcuNombreBusqueda(String ocuNombreBusqueda) {
        this.ocuNombreBusqueda = ocuNombreBusqueda;
    }

    public Boolean getOcuHabilitado() {
        return ocuHabilitado;
    }

    public void setOcuHabilitado(Boolean ocuHabilitado) {
        this.ocuHabilitado = ocuHabilitado;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(this.ocuPk);
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
        return "SgOcupacion{" + "ocuPk=" + ocuPk + ", ocuCodigo=" + ocuCodigo + ", ocuNombre=" + ocuNombre + ", ocuNombreBusqueda=" + ocuNombreBusqueda + ", ocuHabilitado=" + ocuHabilitado + ", ocuUltModFecha=" + ocuUltModFecha + ", ocuUltModUsuario=" + ocuUltModUsuario + ", ocuVersion=" + ocuVersion + '}';
    }

}
