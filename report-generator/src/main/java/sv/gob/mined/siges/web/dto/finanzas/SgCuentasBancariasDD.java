/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.finanzas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.SgBancos;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cbdPk", scope = SgCuentasBancariasDD.class)
public class SgCuentasBancariasDD implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cbdPk;

    private String cbdNumeroCuenta;
    
    private String cbdTitular;
    
    private SgBancos cbdBancoFk;

    private Boolean cbdHabilitado;
    
    private String cbdComentario;

    private SgDireccionDepartamental cbdDirDepFk;

    private LocalDateTime cbdUltModFecha;

    private String cbdUltModUsuario;

    private Integer cbdVersion;
    
    
    public SgCuentasBancariasDD() {
        this.cbdHabilitado = Boolean.TRUE;
    }

    public Long getCbdPk() {
        return cbdPk;
    }

    public void setCbdPk(Long cbdPk) {
        this.cbdPk = cbdPk;
    }

    public String getCbdNumeroCuenta() {
        return cbdNumeroCuenta;
    }

    public void setCbdNumeroCuenta(String cbdNumeroCuenta) {
        this.cbdNumeroCuenta = cbdNumeroCuenta;
    }

    public String getCbdTitular() {
        return cbdTitular;
    }

    public void setCbdTitular(String cbdTitular) {
        this.cbdTitular = cbdTitular;
    }

    public SgBancos getCbdBancoFk() {
        return cbdBancoFk;
    }

    public void setCbdBancoFk(SgBancos cbdBancoFk) {
        this.cbdBancoFk = cbdBancoFk;
    }

    public String getCbdComentario() {
        return cbdComentario;
    }

    public void setCbdComentario(String cbdComentario) {
        this.cbdComentario = cbdComentario;
    }

    public SgDireccionDepartamental getCbdDirDepFk() {
        return cbdDirDepFk;
    }

    public void setCbdDirDepFk(SgDireccionDepartamental cbdDirDepFk) {
        this.cbdDirDepFk = cbdDirDepFk;
    }

    public LocalDateTime getCbdUltModFecha() {
        return cbdUltModFecha;
    }

    public void setCbdUltModFecha(LocalDateTime cbdUltModFecha) {
        this.cbdUltModFecha = cbdUltModFecha;
    }

    public String getCbdUltModUsuario() {
        return cbdUltModUsuario;
    }

    public void setCbdUltModUsuario(String cbdUltModUsuario) {
        this.cbdUltModUsuario = cbdUltModUsuario;
    }

    public Integer getCbdVersion() {
        return cbdVersion;
    }

    public void setCbdVersion(Integer cbdVersion) {
        this.cbdVersion = cbdVersion;
    }

    
     public Boolean getCbdHabilitado() {
        return cbdHabilitado;
    }

    public void setCbdHabilitado(Boolean cbdHabilitado) {
        this.cbdHabilitado = cbdHabilitado;
    }
    
    public String getNumeroCuentaTitular() {
        String result = new String("N/A");
        if(StringUtils.isNotBlank(this.cbdNumeroCuenta)){
            result = this.cbdNumeroCuenta;
        }
        
        if(StringUtils.isNotBlank(this.cbdTitular)){
          result = result.concat(" - ").concat(this.cbdTitular);
        }
        return result;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cbdPk != null ? cbdPk.hashCode() : 0);
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
        final SgCuentasBancariasDD other = (SgCuentasBancariasDD) obj;
        if (!Objects.equals(this.cbdPk, other.cbdPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCuentasBancariasDD[ cbdPk=" + cbdPk + " ]";
    }
    
}
