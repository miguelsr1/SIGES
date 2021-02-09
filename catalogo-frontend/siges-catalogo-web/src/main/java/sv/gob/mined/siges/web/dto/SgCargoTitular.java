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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ctiPk", scope = SgCargoTitular.class)
public class SgCargoTitular implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ctiPk;

    private String ctiCodigo;

    private String ctiNombre;

    private Boolean ctiHabilitado;

    private LocalDateTime ctiUltModFecha;

    private String ctiUltModUsuario;

    private Integer ctiVersion;
    
    private Boolean ctiEsTitular;
    
    
    public SgCargoTitular() {
        this.ctiHabilitado = Boolean.TRUE;
        this.ctiEsTitular=Boolean.FALSE;
    }

    public Long getCtiPk() {
        return ctiPk;
    }

    public void setCtiPk(Long ctiPk) {
        this.ctiPk = ctiPk;
    }

    public String getCtiCodigo() {
        return ctiCodigo;
    }

    public void setCtiCodigo(String ctiCodigo) {
        this.ctiCodigo = ctiCodigo;
    }

    public String getCtiNombre() {
        return ctiNombre;
    }

    public void setCtiNombre(String ctiNombre) {
        this.ctiNombre = ctiNombre;
    }

    public LocalDateTime getCtiUltModFecha() {
        return ctiUltModFecha;
    }

    public void setCtiUltModFecha(LocalDateTime ctiUltModFecha) {
        this.ctiUltModFecha = ctiUltModFecha;
    }

    public String getCtiUltModUsuario() {
        return ctiUltModUsuario;
    }

    public void setCtiUltModUsuario(String ctiUltModUsuario) {
        this.ctiUltModUsuario = ctiUltModUsuario;
    }

    public Integer getCtiVersion() {
        return ctiVersion;
    }

    public void setCtiVersion(Integer ctiVersion) {
        this.ctiVersion = ctiVersion;
    }

    
     public Boolean getCtiHabilitado() {
        return ctiHabilitado;
    }

    public void setCtiHabilitado(Boolean ctiHabilitado) {
        this.ctiHabilitado = ctiHabilitado;
    }

    public Boolean getCtiEsTitular() {
        return ctiEsTitular;
    }

    public void setCtiEsTitular(Boolean ctiEsTitular) {
        this.ctiEsTitular = ctiEsTitular;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctiPk != null ? ctiPk.hashCode() : 0);
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
        final SgCargoTitular other = (SgCargoTitular) obj;
        if (!Objects.equals(this.ctiPk, other.ctiPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCargoTitular[ ctiPk=" + ctiPk + " ]";
    }
    
}
