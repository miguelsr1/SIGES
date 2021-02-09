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
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "ebiPk", scope = SgAfEstadosBienes.class)
public class SgAfEstadosBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ebiPk;
    private String ebiCodigo;
    private String ebiNombre;
    private String ebiNombreBusqueda;
    private Boolean ebiHabilitado;
    private String ebiEstilo;
    private String[] ebiAplicaPara;
    private LocalDateTime ebiUltModFecha;
    private String ebiUltModUsuario;
    private Integer ebiVersion;

    public SgAfEstadosBienes() {
        ebiHabilitado = Boolean.TRUE;
    }

    public Long getEbiPk() {
        return ebiPk;
    }

    public void setEbiPk(Long ebiPk) {
        this.ebiPk = ebiPk;
    }

    public String getEbiCodigo() {
        return ebiCodigo;
    }

    public void setEbiCodigo(String ebiCodigo) {
        this.ebiCodigo = ebiCodigo;
    }

    public String getEbiNombre() {
        return ebiNombre;
    }

    public void setEbiNombre(String ebiNombre) {
        this.ebiNombre = ebiNombre;
    }

    public String getEbiNombreBusqueda() {
        return ebiNombreBusqueda;
    }

    public void setEbiNombreBusqueda(String ebiNombreBusqueda) {
        this.ebiNombreBusqueda = ebiNombreBusqueda;
    }

    public Boolean getEbiHabilitado() {
        return ebiHabilitado;
    }

    public void setEbiHabilitado(Boolean ebiHabilitado) {
        this.ebiHabilitado = ebiHabilitado;
    }

    public LocalDateTime getEbiUltModFecha() {
        return ebiUltModFecha;
    }

    public void setEbiUltModFecha(LocalDateTime ebiUltModFecha) {
        this.ebiUltModFecha = ebiUltModFecha;
    }

    public String getEbiUltModUsuario() {
        return ebiUltModUsuario;
    }

    public void setEbiUltModUsuario(String ebiUltModUsuario) {
        this.ebiUltModUsuario = ebiUltModUsuario;
    }

    public Integer getEbiVersion() {
        return ebiVersion;
    }

    public void setEbiVersion(Integer ebiVersion) {
        this.ebiVersion = ebiVersion;
    }

    public String[] getEbiAplicaPara() {
        return ebiAplicaPara;
    }

    public void setEbiAplicaPara(String[] ebiAplicaPara) {
        this.ebiAplicaPara = ebiAplicaPara;
    }

    public String getEbiEstilo() {
        return ebiEstilo;
    }

    public void setEbiEstilo(String ebiEstilo) {
        this.ebiEstilo = ebiEstilo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.ebiPk);
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
        final SgAfEstadosBienes other = (SgAfEstadosBienes) obj;
        if (!Objects.equals(this.ebiPk, other.ebiPk)) {
            return false;
        }
        return true;
    }
    
    
}

