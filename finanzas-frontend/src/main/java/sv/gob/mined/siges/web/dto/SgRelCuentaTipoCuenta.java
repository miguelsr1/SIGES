/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCuentaBancaria;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "relPk", scope = SgRelCuentaTipoCuenta.class)
public class SgRelCuentaTipoCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long relPk;

    private SgTipoCuentaBancaria cbcTipoCuentaBacFk;

    private SgCuentasBancarias cbcCuentaBancariaFk;

    private LocalDateTime relUltModFecha;

    private String relUltModUsuario;

    private Integer relVersion;

    public SgRelCuentaTipoCuenta() {
    }

    public Long getRelPk() {
        return relPk;
    }

    public void setRelPk(Long relPk) {
        this.relPk = relPk;
    }

    public SgTipoCuentaBancaria getCbcTipoCuentaBacFk() {
        return cbcTipoCuentaBacFk;
    }

    public void setCbcTipoCuentaBacFk(SgTipoCuentaBancaria cbcTipoCuentaBacFk) {
        this.cbcTipoCuentaBacFk = cbcTipoCuentaBacFk;
    }

    public SgCuentasBancarias getCbcCuentaBancariaFk() {
        return cbcCuentaBancariaFk;
    }

    public void setCbcCuentaBancariaFk(SgCuentasBancarias cbcCuentaBancariaFk) {
        this.cbcCuentaBancariaFk = cbcCuentaBancariaFk;
    }

    public LocalDateTime getRelUltModFecha() {
        return relUltModFecha;
    }

    public void setRelUltModFecha(LocalDateTime relUltModFecha) {
        this.relUltModFecha = relUltModFecha;
    }

    public String getRelUltModUsuario() {
        return relUltModUsuario;
    }

    public void setRelUltModUsuario(String relUltModUsuario) {
        this.relUltModUsuario = relUltModUsuario;
    }

    public Integer getRelVersion() {
        return relVersion;
    }

    public void setRelVersion(Integer relVersion) {
        this.relVersion = relVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.relPk);
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
        final SgRelCuentaTipoCuenta other = (SgRelCuentaTipoCuenta) obj;
        if (!Objects.equals(this.relPk, other.relPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelCbcTcb{" + "relPk=" + relPk + '}';
    }

}
