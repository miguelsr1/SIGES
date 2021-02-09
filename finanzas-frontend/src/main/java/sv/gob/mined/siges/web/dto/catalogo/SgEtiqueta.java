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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etiPk", scope = SgEtiqueta.class)
public class SgEtiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long etiPk;

    private String etiCodigo;

    private String etiValor;

    private Boolean etiHabilitado;

    private LocalDateTime etiUltModFecha;

    private String etiUltModUsuario;

    private Integer etiVersion;

    public SgEtiqueta() {
        this.etiHabilitado = Boolean.TRUE;
    }

    public Long getEtiPk() {
        return etiPk;
    }

    public void setEtiPk(Long etiPk) {
        this.etiPk = etiPk;
    }

    public String getEtiCodigo() {
        return etiCodigo;
    }

    public void setEtiCodigo(String etiCodigo) {
        this.etiCodigo = etiCodigo;
    }

    public String getEtiValor() {
        return etiValor;
    }

    public void setEtiValor(String etiValor) {
        this.etiValor = etiValor;
    }

    public LocalDateTime getEtiUltModFecha() {
        return etiUltModFecha;
    }

    public void setEtiUltModFecha(LocalDateTime etiUltModFecha) {
        this.etiUltModFecha = etiUltModFecha;
    }

    public String getEtiUltModUsuario() {
        return etiUltModUsuario;
    }

    public void setEtiUltModUsuario(String etiUltModUsuario) {
        this.etiUltModUsuario = etiUltModUsuario;
    }

    public Integer getEtiVersion() {
        return etiVersion;
    }

    public void setEtiVersion(Integer etiVersion) {
        this.etiVersion = etiVersion;
    }

    public Boolean getEtiHabilitado() {
        return etiHabilitado;
    }

    public void setEtiHabilitado(Boolean etiHabilitado) {
        this.etiHabilitado = etiHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (etiPk != null ? etiPk.hashCode() : 0);
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
        final SgEtiqueta other = (SgEtiqueta) obj;
        if (!Objects.equals(this.etiPk, other.etiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgEtiqueta[ etiPk=" + etiPk + " ]";
    }

}
