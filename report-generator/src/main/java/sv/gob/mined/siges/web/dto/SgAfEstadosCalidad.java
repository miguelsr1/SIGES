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
/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ecaPk", scope = SgAfEstadosCalidad.class)
public class SgAfEstadosCalidad implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long ecaPk;
    private String ecaCodigo;
    private String ecaNombre;
    private String ecaNombreBusqueda;
    private Boolean ecaHabilitado;
    private LocalDateTime ecaUltModFecha;
    private String ecaUltModUsuario;
    private Integer ecaVersion;

    public SgAfEstadosCalidad() {
        ecaHabilitado = Boolean.TRUE;
    }

    public Long getEcaPk() {
        return ecaPk;
    }

    public void setEcaPk(Long ecaPk) {
        this.ecaPk = ecaPk;
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }

    public String getEcaNombre() {
        return ecaNombre;
    }

    public void setEcaNombre(String ecaNombre) {
        this.ecaNombre = ecaNombre;
    }

    public String getEcaNombreBusqueda() {
        return ecaNombreBusqueda;
    }

    public void setEcaNombreBusqueda(String ecaNombreBusqueda) {
        this.ecaNombreBusqueda = ecaNombreBusqueda;
    }

    public Boolean getEcaHabilitado() {
        return ecaHabilitado;
    }

    public void setEcaHabilitado(Boolean ecaHabilitado) {
        this.ecaHabilitado = ecaHabilitado;
    }

    public LocalDateTime getEcaUltModFecha() {
        return ecaUltModFecha;
    }

    public void setEcaUltModFecha(LocalDateTime ecaUltModFecha) {
        this.ecaUltModFecha = ecaUltModFecha;
    }

    public String getEcaUltModUsuario() {
        return ecaUltModUsuario;
    }

    public void setEcaUltModUsuario(String ecaUltModUsuario) {
        this.ecaUltModUsuario = ecaUltModUsuario;
    }

    public Integer getEcaVersion() {
        return ecaVersion;
    }

    public void setEcaVersion(Integer ecaVersion) {
        this.ecaVersion = ecaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.ecaPk);
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
        final SgAfEstadosCalidad other = (SgAfEstadosCalidad) obj;
        if (!Objects.equals(this.ecaPk, other.ecaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAfEstadosCalidad{" + "ecaPk=" + ecaPk + '}';
    }

    
}
