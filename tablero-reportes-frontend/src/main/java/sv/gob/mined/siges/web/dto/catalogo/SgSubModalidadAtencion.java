/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "smoPk", scope = SgSubModalidadAtencion.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SgSubModalidadAtencion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long smoPk;

    private String smoCodigo;

    private String smoNombre;

    private String smoNombreBusqueda;

    private String smoDescripcion;

    private Boolean smoHabilitado;
    
    private Boolean smoIntegrada;

    private LocalDateTime smoUltModFecha;

    private String smoUltModUsuario;

    private Integer smoVersion;

    private SgModalidadAtencion smoModalidadFk;

    public Long getSmoPk() {
        return smoPk;
    }

    public void setSmoPk(Long smoPk) {
        this.smoPk = smoPk;
    }

    public String getSmoCodigo() {
        return smoCodigo;
    }

    public void setSmoCodigo(String smoCodigo) {
        this.smoCodigo = smoCodigo;
    }

    public String getSmoNombre() {
        return smoNombre;
    }

    public void setSmoNombre(String smoNombre) {
        this.smoNombre = smoNombre;
    }

    public String getSmoNombreBusqueda() {
        return smoNombreBusqueda;
    }

    public void setSmoNombreBusqueda(String smoNombreBusqueda) {
        this.smoNombreBusqueda = smoNombreBusqueda;
    }

    public String getSmoDescripcion() {
        return smoDescripcion;
    }

    public void setSmoDescripcion(String smoDescripcion) {
        this.smoDescripcion = smoDescripcion;
    }

    public Boolean getSmoHabilitado() {
        return smoHabilitado;
    }

    public void setSmoHabilitado(Boolean smoHabilitado) {
        this.smoHabilitado = smoHabilitado;
    }

    public LocalDateTime getSmoUltModFecha() {
        return smoUltModFecha;
    }

    public void setSmoUltModFecha(LocalDateTime smoUltModFecha) {
        this.smoUltModFecha = smoUltModFecha;
    }

    public String getSmoUltModUsuario() {
        return smoUltModUsuario;
    }

    public void setSmoUltModUsuario(String smoUltModUsuario) {
        this.smoUltModUsuario = smoUltModUsuario;
    }

    public Integer getSmoVersion() {
        return smoVersion;
    }

    public void setSmoVersion(Integer smoVersion) {
        this.smoVersion = smoVersion;
    }

    public SgModalidadAtencion getSmoModalidadFk() {
        return smoModalidadFk;
    }

    public void setSmoModalidadFk(SgModalidadAtencion smoModalidadFk) {
        this.smoModalidadFk = smoModalidadFk;
    }

    public Boolean getSmoIntegrada() {
        return smoIntegrada;
    }

    public void setSmoIntegrada(Boolean smoIntegrada) {
        this.smoIntegrada = smoIntegrada;
    }
    
    public SgSubModalidadAtencion() {
        this.smoHabilitado = Boolean.TRUE;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (smoPk != null ? smoPk.hashCode() : 0);
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
        final SgSubModalidadAtencion other = (SgSubModalidadAtencion) obj;
        if (!Objects.equals(this.smoPk, other.smoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgSubModalidad[ smoPk=" + smoPk + " ]";
    }

}
