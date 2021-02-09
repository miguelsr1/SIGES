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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "einPk", scope = SgAfEstadosInventario.class)
public class SgAfEstadosInventario implements Serializable {
   private static final long serialVersionUID = 1L;

    private Long einPk;
    private String einCodigo;
    private String einNombre;
    private String einNombreBusqueda;
    private Boolean einHabilitado;
    private LocalDateTime einUltModFecha;
    private String einUltModUsuario;
    private Integer einVersion;

    public SgAfEstadosInventario() {
        einHabilitado = Boolean.TRUE;
    }

    public Long getEinPk() {
        return einPk;
    }

    public void setEinPk(Long einPk) {
        this.einPk = einPk;
    }

    public String getEinCodigo() {
        return einCodigo;
    }

    public void setEinCodigo(String einCodigo) {
        this.einCodigo = einCodigo;
    }

    public String getEinNombre() {
        return einNombre;
    }

    public void setEinNombre(String einNombre) {
        this.einNombre = einNombre;
    }

    public String getEinNombreBusqueda() {
        return einNombreBusqueda;
    }

    public void setEinNombreBusqueda(String einNombreBusqueda) {
        this.einNombreBusqueda = einNombreBusqueda;
    }

    public Boolean getEinHabilitado() {
        return einHabilitado;
    }

    public void setEinHabilitado(Boolean einHabilitado) {
        this.einHabilitado = einHabilitado;
    }

    public LocalDateTime getEinUltModFecha() {
        return einUltModFecha;
    }

    public void setEinUltModFecha(LocalDateTime einUltModFecha) {
        this.einUltModFecha = einUltModFecha;
    }

    public String getEinUltModUsuario() {
        return einUltModUsuario;
    }

    public void setEinUltModUsuario(String einUltModUsuario) {
        this.einUltModUsuario = einUltModUsuario;
    }

    public Integer getEinVersion() {
        return einVersion;
    }

    public void setEinVersion(Integer einVersion) {
        this.einVersion = einVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.einPk);
        hash = 47 * hash + Objects.hashCode(this.einCodigo);
        hash = 47 * hash + Objects.hashCode(this.einNombre);
        hash = 47 * hash + Objects.hashCode(this.einNombreBusqueda);
        hash = 47 * hash + Objects.hashCode(this.einHabilitado);
        hash = 47 * hash + Objects.hashCode(this.einUltModFecha);
        hash = 47 * hash + Objects.hashCode(this.einUltModUsuario);
        hash = 47 * hash + Objects.hashCode(this.einVersion);
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
        final SgAfEstadosInventario other = (SgAfEstadosInventario) obj;
        if (!Objects.equals(this.einPk, other.einPk)) {
            return false;
        }
        return true;
    }
    
}
