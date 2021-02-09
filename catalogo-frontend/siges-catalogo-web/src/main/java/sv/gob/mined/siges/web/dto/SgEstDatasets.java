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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "datPk", scope = SgEstDatasets.class)
public class SgEstDatasets implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long datPk;

    private String datCodigo;

    private String datNombre;

    private Boolean datHabilitado;

    private LocalDateTime datUltModFecha;

    private String datUltModUsuario;

    private Integer datVersion;
    
    
    public SgEstDatasets() {
        this.datHabilitado = Boolean.TRUE;
    }

    public Long getDatPk() {
        return datPk;
    }

    public void setDatPk(Long datPk) {
        this.datPk = datPk;
    }

    public String getDatCodigo() {
        return datCodigo;
    }

    public void setDatCodigo(String datCodigo) {
        this.datCodigo = datCodigo;
    }

    public String getDatNombre() {
        return datNombre;
    }

    public void setDatNombre(String datNombre) {
        this.datNombre = datNombre;
    }

    public LocalDateTime getDatUltModFecha() {
        return datUltModFecha;
    }

    public void setDatUltModFecha(LocalDateTime datUltModFecha) {
        this.datUltModFecha = datUltModFecha;
    }

    public String getDatUltModUsuario() {
        return datUltModUsuario;
    }

    public void setDatUltModUsuario(String datUltModUsuario) {
        this.datUltModUsuario = datUltModUsuario;
    }

    public Integer getDatVersion() {
        return datVersion;
    }

    public void setDatVersion(Integer datVersion) {
        this.datVersion = datVersion;
    }

    
     public Boolean getDatHabilitado() {
        return datHabilitado;
    }

    public void setDatHabilitado(Boolean datHabilitado) {
        this.datHabilitado = datHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datPk != null ? datPk.hashCode() : 0);
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
        final SgEstDatasets other = (SgEstDatasets) obj;
        if (!Objects.equals(this.datPk, other.datPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDatasets[ datPk=" + datPk + " ]";
    }
    
}
