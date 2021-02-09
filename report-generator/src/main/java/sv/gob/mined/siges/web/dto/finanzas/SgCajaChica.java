/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bccPk", scope = SgCajaChica.class)
public class SgCajaChica implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bccPk;

    private String bccNumeroCuenta;

    private String bccTitular;

    private String bccComentario;

    private Boolean bccHabilitado;

    private LocalDateTime bccUltModFecha;

    private String bccUltModUsuario;

    private Integer bccVersion;

    public SgCajaChica() {
        this.bccHabilitado = Boolean.TRUE;
    }

    public Long getBccPk() {
        return bccPk;
    }

    public void setBccPk(Long bccPk) {
        this.bccPk = bccPk;
    }

    public String getBccNumeroCuenta() {
        return bccNumeroCuenta;
    }

    public void setBccNumeroCuenta(String bccNumeroCuenta) {
        this.bccNumeroCuenta = bccNumeroCuenta;
    }

    public String getBccTitular() {
        return bccTitular;
    }

    public void setBccTitular(String bccTitular) {
        this.bccTitular = bccTitular;
    }

    public String getBccComentario() {
        return bccComentario;
    }

    public void setBccComentario(String bccComentario) {
        this.bccComentario = bccComentario;
    }

    public LocalDateTime getBccUltModFecha() {
        return bccUltModFecha;
    }

    public void setBccUltModFecha(LocalDateTime bccUltModFecha) {
        this.bccUltModFecha = bccUltModFecha;
    }

    public String getBccUltModUsuario() {
        return bccUltModUsuario;
    }

    public void setBccUltModUsuario(String bccUltModUsuario) {
        this.bccUltModUsuario = bccUltModUsuario;
    }

    public Integer getBccVersion() {
        return bccVersion;
    }

    public void setBccVersion(Integer bccVersion) {
        this.bccVersion = bccVersion;
    }

    public Boolean getBccHabilitado() {
        return bccHabilitado;
    }

    public void setBccHabilitado(Boolean bccHabilitado) {
        this.bccHabilitado = bccHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bccPk != null ? bccPk.hashCode() : 0);
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
        final SgCajaChica other = (SgCajaChica) obj;
        if (!Objects.equals(this.bccPk, other.bccPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCuentasBancariasCC[ bccPk=" + bccPk + " ]";
    }

}
