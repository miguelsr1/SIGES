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
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;

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

    private SgSede bccSedeFk;
    
    private SsGesPresEs bccSubcomponenteFk;
    
    private SgAnioLectivo bccAnioFk;
    
    private Boolean bccOtroIngreso;

    private Boolean bccHabilitado;
    
    private SgCuentasBancarias bccCuentaBancFk;

    private LocalDateTime bccUltModFecha;

    private String bccUltModUsuario;

    private Integer bccVersion;

    public SgCajaChica() {
        this.bccHabilitado = Boolean.TRUE;
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">

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

    public SgSede getBccSedeFk() {
        return bccSedeFk;
    }

    public void setBccSedeFk(SgSede bccSedeFk) {
        this.bccSedeFk = bccSedeFk;
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

    public SsGesPresEs getBccSubcomponenteFk() {
        return bccSubcomponenteFk;
    }

    public void setBccSubcomponenteFk(SsGesPresEs bccSubcomponenteFk) {
        this.bccSubcomponenteFk = bccSubcomponenteFk;
    }

    public Boolean getBccOtroIngreso() {
        return bccOtroIngreso;
    }

    public void setBccOtroIngreso(Boolean bccOtroIngreso) {
        this.bccOtroIngreso = bccOtroIngreso;
    }

    public SgAnioLectivo getBccAnioFk() {
        return bccAnioFk;
    }

    public void setBccAnioFk(SgAnioLectivo bccAnioFk) {
        this.bccAnioFk = bccAnioFk;
    }

    public SgCuentasBancarias getBccCuentaBancFk() {
        return bccCuentaBancFk;
    }

    public void setBccCuentaBancFk(SgCuentasBancarias bccCuentaBancFk) {
        this.bccCuentaBancFk = bccCuentaBancFk;
    }
    
    
    

    // </editor-fold>
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
