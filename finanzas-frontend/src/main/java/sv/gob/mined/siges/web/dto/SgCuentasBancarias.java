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
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cbcPk", scope = SgCuentasBancarias.class)
public class SgCuentasBancarias implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cbcPk;

    private String cbcNumeroCuenta;

    private String cbcTitular;

    private SgBancos cbcBancoFk;

    private String cbcComentario;

    private SgSede cbcSedeFk;

    private List<SgRelCuentaTipoCuenta> cbcCuentaTipoCuenta;

    private Boolean cbcHabilitado;
    
    private Boolean cbcOtroIngreso;

    private LocalDateTime cbcUltModFecha;

    private String cbcUltModUsuario;

    private Integer cbcVersion;

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public SgCuentasBancarias() {
        this.cbcHabilitado = Boolean.TRUE;
    }

    public Long getCbcPk() {
        return cbcPk;
    }

    public void setCbcPk(Long cbcPk) {
        this.cbcPk = cbcPk;
    }

    public String getCbcNumeroCuenta() {
        return cbcNumeroCuenta;
    }

    public void setCbcNumeroCuenta(String cbcNumeroCuenta) {
        this.cbcNumeroCuenta = cbcNumeroCuenta;
    }

    public String getCbcTitular() {
        return cbcTitular;
    }

    public void setCbcTitular(String cbcTitular) {
        this.cbcTitular = cbcTitular;
    }

    public SgBancos getCbcBancoFk() {
        return cbcBancoFk;
    }

    public void setCbcBancoFk(SgBancos cbcBancoFk) {
        this.cbcBancoFk = cbcBancoFk;
    }

    public String getCbcComentario() {
        return cbcComentario;
    }

    public void setCbcComentario(String cbcComentario) {
        this.cbcComentario = cbcComentario;
    }

    public SgSede getCbcSedeFk() {
        return cbcSedeFk;
    }

    public void setCbcSedeFk(SgSede cbcSedeFk) {
        this.cbcSedeFk = cbcSedeFk;
    }

    public LocalDateTime getCbcUltModFecha() {
        return cbcUltModFecha;
    }

    public void setCbcUltModFecha(LocalDateTime cbcUltModFecha) {
        this.cbcUltModFecha = cbcUltModFecha;
    }

    public String getCbcUltModUsuario() {
        return cbcUltModUsuario;
    }

    public void setCbcUltModUsuario(String cbcUltModUsuario) {
        this.cbcUltModUsuario = cbcUltModUsuario;
    }

    public Integer getCbcVersion() {
        return cbcVersion;
    }

    public void setCbcVersion(Integer cbcVersion) {
        this.cbcVersion = cbcVersion;
    }

    public Boolean getCbcHabilitado() {
        return cbcHabilitado;
    }

    public void setCbcHabilitado(Boolean cbcHabilitado) {
        this.cbcHabilitado = cbcHabilitado;
    }

    public List<SgRelCuentaTipoCuenta> getCbcCuentaTipoCuenta() {
        return cbcCuentaTipoCuenta;
    }

    public void setCbcCuentaTipoCuenta(List<SgRelCuentaTipoCuenta> cbcCuentaTipoCuenta) {
        this.cbcCuentaTipoCuenta = cbcCuentaTipoCuenta;
    }
    
    public String getCuentaLabelCombo(){
        if(this.cbcBancoFk!=null){
            return this.cbcNumeroCuenta.concat(" - ").concat(this.cbcBancoFk.getBncNombre());
        }
        else{
            return this.cbcNumeroCuenta;
        }
    }

    public Boolean getCbcOtroIngreso() {
        return cbcOtroIngreso;
    }

    public void setCbcOtroIngreso(Boolean cbcOtroIngreso) {
        this.cbcOtroIngreso = cbcOtroIngreso;
    }
    
    
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cbcPk != null ? cbcPk.hashCode() : 0);
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
        final SgCuentasBancarias other = (SgCuentasBancarias) obj;
        if (!Objects.equals(this.cbcPk, other.cbcPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgCuentasBancarias[ cbcPk=" + cbcPk + " ]";
    }

    // </editor-fold>
}
