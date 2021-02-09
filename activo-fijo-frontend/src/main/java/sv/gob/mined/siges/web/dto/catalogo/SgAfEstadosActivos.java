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
import sv.gob.mined.siges.web.enumerados.EnumEstados;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "eacPk", scope = SgAfEstadosActivos.class)
public class SgAfEstadosActivos implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long eacPk;
    private Integer eacCodigo;
    private String eacNombre;
    private Boolean eacHabilitado;
    private EnumEstados eacProceso;
    private LocalDateTime eacUltmodFecha;
    private String eacUltmodUsuario;
    private Integer eacVersion;

    public Long getEacPk() {
        return eacPk;
    }

    public void setEacPk(Long eacPk) {
        this.eacPk = eacPk;
    }

    public Integer getEacCodigo() {
        return eacCodigo;
    }

    public void setEacCodigo(Integer eacCodigo) {
        this.eacCodigo = eacCodigo;
    }

    public String getEacNombre() {
        return eacNombre;
    }

    public void setEacNombre(String eacNombre) {
        this.eacNombre = eacNombre;
    }

    public Boolean getEacHabilitado() {
        return eacHabilitado;
    }

    public void setEacHabilitado(Boolean eacHabilitado) {
        this.eacHabilitado = eacHabilitado;
    }

    public EnumEstados getEacProceso() {
        return eacProceso;
    }

    public void setEacProceso(EnumEstados eacProceso) {
        this.eacProceso = eacProceso;
    }

    public LocalDateTime getEacUltmodFecha() {
        return eacUltmodFecha;
    }

    public void setEacUltmodFecha(LocalDateTime eacUltmodFecha) {
        this.eacUltmodFecha = eacUltmodFecha;
    }

    public String getEacUltmodUsuario() {
        return eacUltmodUsuario;
    }

    public void setEacUltmodUsuario(String eacUltmodUsuario) {
        this.eacUltmodUsuario = eacUltmodUsuario;
    }

    public Integer getEacVersion() {
        return eacVersion;
    }

    public void setEacVersion(Integer eacVersion) {
        this.eacVersion = eacVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final SgAfEstadosActivos other = (SgAfEstadosActivos) obj;
        if (!Objects.equals(this.eacPk, other.eacPk)) {
            return false;
        }
        return true;
    }
}
