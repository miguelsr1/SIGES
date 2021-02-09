/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcbPk", scope = SgTipoCuentaBancaria.class)
public class SgTipoCuentaBancaria implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tcbPk;
    
    private String tcbCodigo;
    
    private String tcbNombre;
    
    private String tcbNombreBusqueda;
    
    private Boolean tcbHabilitado;

    private LocalDateTime tcbUltModFecha;

    private String tcbUltModUsuario;

    private Integer tcbVersion;
    
    
    public SgTipoCuentaBancaria() {
        this.tcbHabilitado = Boolean.TRUE;
    }

    public Long getTcbPk() {
        return tcbPk;
    }

    public void setTcbPk(Long tcbPk) {
        this.tcbPk = tcbPk;
    }

    public String getTcbCodigo() {
        return tcbCodigo;
    }

    public void setTcbCodigo(String tcbCodigo) {
        this.tcbCodigo = tcbCodigo;
    }

    public String getTcbNombre() {
        return tcbNombre;
    }

    public void setTcbNombre(String tcbNombre) {
        this.tcbNombre = tcbNombre;
    }

    public String getTcbNombreBusqueda() {
        return tcbNombreBusqueda;
    }

    public void setTcbNombreBusqueda(String tcbNombreBusqueda) {
        this.tcbNombreBusqueda = tcbNombreBusqueda;
    }

    public Boolean getTcbHabilitado() {
        return tcbHabilitado;
    }

    public void setTcbHabilitado(Boolean tcbHabilitado) {
        this.tcbHabilitado = tcbHabilitado;
    }

    public LocalDateTime getTcbUltModFecha() {
        return tcbUltModFecha;
    }

    public void setTcbUltModFecha(LocalDateTime tcbUltModFecha) {
        this.tcbUltModFecha = tcbUltModFecha;
    }

    public String getTcbUltModUsuario() {
        return tcbUltModUsuario;
    }

    public void setTcbUltModUsuario(String tcbUltModUsuario) {
        this.tcbUltModUsuario = tcbUltModUsuario;
    }

    public Integer getTcbVersion() {
        return tcbVersion;
    }

    public void setTcbVersion(Integer tcbVersion) {
        this.tcbVersion = tcbVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.tcbPk);
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
        final SgTipoCuentaBancaria other = (SgTipoCuentaBancaria) obj;
        if (!Objects.equals(this.tcbPk, other.tcbPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoCuentaBancaria{" + "tcbPk=" + tcbPk + '}';
    }
    
    
}
