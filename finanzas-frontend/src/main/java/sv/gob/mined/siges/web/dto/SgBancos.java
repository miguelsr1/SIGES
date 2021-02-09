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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bncPk", scope = SgBancos.class)
public class SgBancos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bncPk;

    private String bncCodigo;

    private String bncNombre;

    private String bncCodigoBusqueda;

    private String bncNombreBusqueda;

    private Boolean bncHabilitado;

    private LocalDateTime bncUltModFecha;

    private String bncUltModUsuario;

    private Integer bncVersion;

    public SgBancos() {
        this.bncHabilitado = Boolean.TRUE;
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">

    public String getCodigoNombre() {
        return bncCodigo + " - " + bncNombre;
    }

    public Long getBncPk() {
        return bncPk;
    }

    public void setBncPk(Long bncPk) {
        this.bncPk = bncPk;
    }

    public String getBncCodigo() {
        return bncCodigo;
    }

    public void setBncCodigo(String bncCodigo) {
        this.bncCodigo = bncCodigo;
    }

    public String getBncNombre() {
        return bncNombre;
    }

    public void setBncNombre(String bncNombre) {
        this.bncNombre = bncNombre;
    }

    public String getBncCodigoBusqueda() {
        return bncCodigoBusqueda;
    }

    public void setBncCodigoBusqueda(String bncCodigoBusqueda) {
        this.bncCodigoBusqueda = bncCodigoBusqueda;
    }

    public String getBncNombreBusqueda() {
        return bncNombreBusqueda;
    }

    public void setBncNombreBusqueda(String bncNombreBusqueda) {
        this.bncNombreBusqueda = bncNombreBusqueda;
    }

    public Boolean getBncHabilitado() {
        return bncHabilitado;
    }

    public void setBncHabilitado(Boolean bncHabilitado) {
        this.bncHabilitado = bncHabilitado;
    }

    public LocalDateTime getBncUltModFecha() {
        return bncUltModFecha;
    }

    public void setBncUltModFecha(LocalDateTime bncUltModFecha) {
        this.bncUltModFecha = bncUltModFecha;
    }

    public String getBncUltModUsuario() {
        return bncUltModUsuario;
    }

    public void setBncUltModUsuario(String bncUltModUsuario) {
        this.bncUltModUsuario = bncUltModUsuario;
    }

    public Integer getBncVersion() {
        return bncVersion;
    }

    public void setBncVersion(Integer bncVersion) {
        this.bncVersion = bncVersion;
    }

    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bncPk != null ? bncPk.hashCode() : 0);
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
        final SgBancos other = (SgBancos) obj;
        if (!Objects.equals(this.bncPk, other.bncPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCuentasBancarias[ bncPk=" + bncPk + " ]";
    }

}
