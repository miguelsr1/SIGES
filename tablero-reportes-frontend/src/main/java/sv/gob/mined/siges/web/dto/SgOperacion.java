/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "opePk", scope = SgOperacion.class)
public class SgOperacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long opePk;

    private String opeCodigo;

    private String opeNombre;

    private Boolean opeHabilitado;

    private LocalDateTime opeUltModFecha;

    private String opeUltModUsuario;

    private Integer opeVersion;
    
        
    
    public SgOperacion() {
        this.opeHabilitado = Boolean.TRUE;
    }

    public Long getOpePk() {
        return opePk;
    }

    public void setOpePk(Long opePk) {
        this.opePk = opePk;
    }

    public String getOpeCodigo() {
        return opeCodigo;
    }

    public void setOpeCodigo(String opeCodigo) {
        this.opeCodigo = opeCodigo;
    }

    public String getOpeNombre() {
        return opeNombre;
    }

    public void setOpeNombre(String opeNombre) {
        this.opeNombre = opeNombre;
    }

    public LocalDateTime getOpeUltModFecha() {
        return opeUltModFecha;
    }

    public void setOpeUltModFecha(LocalDateTime opeUltModFecha) {
        this.opeUltModFecha = opeUltModFecha;
    }

    public String getOpeUltModUsuario() {
        return opeUltModUsuario;
    }

    public void setOpeUltModUsuario(String opeUltModUsuario) {
        this.opeUltModUsuario = opeUltModUsuario;
    }

    public Integer getOpeVersion() {
        return opeVersion;
    }

    public void setOpeVersion(Integer opeVersion) {
        this.opeVersion = opeVersion;
    }
    
    public Boolean getOpeHabilitado() {
        return opeHabilitado;
    }

    public void setOpeHabilitado(Boolean opeHabilitado) {
        this.opeHabilitado = opeHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opePk != null ? opePk.hashCode() : 0);
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
        final SgOperacion other = (SgOperacion) obj;
        
        return this.opePk != null && Objects.equals(this.opePk, other.opePk);
            
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgOperacion[ opePk=" + opePk + " ]";
    }

    
}
