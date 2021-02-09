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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tbiPk", scope = SgAfTipoBienes.class)
public class SgAfTipoBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tbiPk;
    private String tbiCodigo;
    private String tbiNombre;
    private String tbiNombreBusqueda;
    private Integer tbiVidaUtil;
    private Boolean tbiHabilitado;
    private SgAfCategoriaBienes tbiCategoriaBienes;
    private LocalDateTime tbiUltModFecha;
    private String tbiUltModUsuario;
    private Integer tbiVersion;

    public SgAfTipoBienes() {
        this.tbiHabilitado = Boolean.TRUE;
        this.tbiCategoriaBienes = new SgAfCategoriaBienes();
    }

    public Long getTbiPk() {
        return tbiPk;
    }

    public void setTbiPk(Long tbiPk) {
        this.tbiPk = tbiPk;
    }

    public String getTbiCodigo() {
        return tbiCodigo;
    }

    public void setTbiCodigo(String tbiCodigo) {
        this.tbiCodigo = tbiCodigo;
    }

    public String getTbiNombre() {
        return tbiNombre;
    }

    public void setTbiNombre(String tbiNombre) {
        this.tbiNombre = tbiNombre;
    }

    public String getTbiNombreBusqueda() {
        return tbiNombreBusqueda;
    }

    public void setTbiNombreBusqueda(String tbiNombreBusqueda) {
        this.tbiNombreBusqueda = tbiNombreBusqueda;
    }

    public Integer getTbiVidaUtil() {
        return tbiVidaUtil;
    }

    public void setTbiVidaUtil(Integer tbiVidaUtil) {
        this.tbiVidaUtil = tbiVidaUtil;
    }

    public Boolean getTbiHabilitado() {
        return tbiHabilitado;
    }

    public void setTbiHabilitado(Boolean tbiHabilitado) {
        this.tbiHabilitado = tbiHabilitado;
    }

    public SgAfCategoriaBienes getTbiCategoriaBienes() {
        return tbiCategoriaBienes;
    }

    public void setTbiCategoriaBienes(SgAfCategoriaBienes tbiCategoriaBienes) {
        this.tbiCategoriaBienes = tbiCategoriaBienes;
    }

    public LocalDateTime getTbiUltModFecha() {
        return tbiUltModFecha;
    }

    public void setTbiUltModFecha(LocalDateTime tbiUltModFecha) {
        this.tbiUltModFecha = tbiUltModFecha;
    }

    public String getTbiUltModUsuario() {
        return tbiUltModUsuario;
    }

    public void setTbiUltModUsuario(String tbiUltModUsuario) {
        this.tbiUltModUsuario = tbiUltModUsuario;
    }

    public Integer getTbiVersion() {
        return tbiVersion;
    }

    public void setTbiVersion(Integer tbiVersion) {
        this.tbiVersion = tbiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.tbiPk);
        hash = 31 * hash + Objects.hashCode(this.tbiCodigo);
        hash = 31 * hash + Objects.hashCode(this.tbiNombre);
        hash = 31 * hash + Objects.hashCode(this.tbiNombreBusqueda);
        hash = 31 * hash + Objects.hashCode(this.tbiVidaUtil);
        hash = 31 * hash + Objects.hashCode(this.tbiHabilitado);
        hash = 31 * hash + Objects.hashCode(this.tbiCategoriaBienes);
        hash = 31 * hash + Objects.hashCode(this.tbiUltModFecha);
        hash = 31 * hash + Objects.hashCode(this.tbiUltModUsuario);
        hash = 31 * hash + Objects.hashCode(this.tbiVersion);
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
        final SgAfTipoBienes other = (SgAfTipoBienes) obj;
        if (!Objects.equals(this.tbiPk, other.tbiPk)) {
            return false;
        }
        return true;
    }
 
    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgTipoBienes[ tbiPk =" + tbiPk + " ]";
    }

}
