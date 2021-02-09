/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etrPk", scope = SgAfEstadosTraslado.class)
public class SgAfEstadosTraslado implements Serializable {
   private static final long serialVersionUID = 1L;

    private Long etrPk;
    private String etrCodigo;
    private String etrNombre;
    private String etrNombreBusqueda;
    private Boolean etrHabilitado;
    private LocalDateTime etrUltModFecha;
    private String etrUltModUsuario;
    private Integer etrVersion;

    public SgAfEstadosTraslado() {
        etrHabilitado = Boolean.TRUE;
    }

    public Long getEtrPk() {
        return etrPk;
    }

    public void setEtrPk(Long etrPk) {
        this.etrPk = etrPk;
    }

    public String getEtrCodigo() {
        return etrCodigo;
    }

    public void setEtrCodigo(String etrCodigo) {
        this.etrCodigo = etrCodigo;
    }

    public String getEtrNombre() {
        return etrNombre;
    }

    public void setEtrNombre(String etrNombre) {
        this.etrNombre = etrNombre;
    }

    public String getEtrNombreBusqueda() {
        return etrNombreBusqueda;
    }

    public void setEtrNombreBusqueda(String etrNombreBusqueda) {
        this.etrNombreBusqueda = etrNombreBusqueda;
    }

    public Boolean getEtrHabilitado() {
        return etrHabilitado;
    }

    public void setEtrHabilitado(Boolean etrHabilitado) {
        this.etrHabilitado = etrHabilitado;
    }

    public LocalDateTime getEtrUltModFecha() {
        return etrUltModFecha;
    }

    public void setEtrUltModFecha(LocalDateTime etrUltModFecha) {
        this.etrUltModFecha = etrUltModFecha;
    }

    public String getEtrUltModUsuario() {
        return etrUltModUsuario;
    }

    public void setEtrUltModUsuario(String etrUltModUsuario) {
        this.etrUltModUsuario = etrUltModUsuario;
    }

    public Integer getEtrVersion() {
        return etrVersion;
    }

    public void setEtrVersion(Integer etrVersion) {
        this.etrVersion = etrVersion;
    } 

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.etrPk);
        hash = 59 * hash + Objects.hashCode(this.etrCodigo);
        hash = 59 * hash + Objects.hashCode(this.etrNombre);
        hash = 59 * hash + Objects.hashCode(this.etrNombreBusqueda);
        hash = 59 * hash + Objects.hashCode(this.etrHabilitado);
        hash = 59 * hash + Objects.hashCode(this.etrUltModFecha);
        hash = 59 * hash + Objects.hashCode(this.etrUltModUsuario);
        hash = 59 * hash + Objects.hashCode(this.etrVersion);
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
        final SgAfEstadosTraslado other = (SgAfEstadosTraslado) obj;
        if (!Objects.equals(this.etrPk, other.etrPk)) {
            return false;
        }
        return true;
    }
    
}
