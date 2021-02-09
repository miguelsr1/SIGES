/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "odePk", scope = SgAfOpcionesDescargo.class)
public class SgAfOpcionesDescargo implements Serializable {
   private static final long serialVersionUID = 1L;

    private Long odePk;
    private String odeCodigo;
    private String odeNombre;
    private String odeNombreBusqueda;
    private Boolean odeHabilitado;
    private LocalDateTime odeUltModFecha;
    private String odeUltModUsuario;
    private Integer odeVersion;

    public SgAfOpcionesDescargo() {
        odeHabilitado = Boolean.TRUE;
    }

    public Long getOdePk() {
        return odePk;
    }

    public void setOdePk(Long odePk) {
        this.odePk = odePk;
    }

    public String getOdeCodigo() {
        return odeCodigo;
    }

    public void setOdeCodigo(String odeCodigo) {
        this.odeCodigo = odeCodigo;
    }

    public String getOdeNombre() {
        return odeNombre;
    }

    public void setOdeNombre(String odeNombre) {
        this.odeNombre = odeNombre;
    }

    public String getOdeNombreBusqueda() {
        return odeNombreBusqueda;
    }

    public void setOdeNombreBusqueda(String odeNombreBusqueda) {
        this.odeNombreBusqueda = odeNombreBusqueda;
    }

    public Boolean getOdeHabilitado() {
        return odeHabilitado;
    }

    public void setOdeHabilitado(Boolean odeHabilitado) {
        this.odeHabilitado = odeHabilitado;
    }

    public LocalDateTime getOdeUltModFecha() {
        return odeUltModFecha;
    }

    public void setOdeUltModFecha(LocalDateTime odeUltModFecha) {
        this.odeUltModFecha = odeUltModFecha;
    }

    public String getOdeUltModUsuario() {
        return odeUltModUsuario;
    }

    public void setOdeUltModUsuario(String odeUltModUsuario) {
        this.odeUltModUsuario = odeUltModUsuario;
    }

    public Integer getOdeVersion() {
        return odeVersion;
    }

    public void setOdeVersion(Integer odeVersion) {
        this.odeVersion = odeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.odePk);
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
        final SgAfOpcionesDescargo other = (SgAfOpcionesDescargo) obj;
        if (!Objects.equals(this.odePk, other.odePk)) {
            return false;
        }
        return true;
    }
}
