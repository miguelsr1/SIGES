/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "uadPk", scope = SgAfUnidadesAdministrativas.class)
public class SgAfUnidadesAdministrativas implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long uadPk;
    private String uadCodigo;
    private String uadNombre;
    private String uadNombreBusqueda;
    private Boolean uadHabilitado;
    private String uadDireccion;
    private String uadNombreDirector;
    private String uadCargoDirector;
    private String uadTelefono;
    private String uadResponsable;
    private LocalDate uadFechaInventario;
    private LocalDateTime uadUltModFecha;
    private String uadUltModUsuario;
    private Integer uadVersion;
    private SgAfUnidadesActivoFijo uadUnidadActivoFijoFk;
    
    public SgAfUnidadesAdministrativas() {
        uadHabilitado = Boolean.TRUE;
        uadUnidadActivoFijoFk = new SgAfUnidadesActivoFijo();
    }

    public String getCodigoNombre() {
        return uadCodigo + " - " + uadNombre;
    }
    
    public Long getUadPk() {
        return uadPk;
    }

    public void setUadPk(Long uadPk) {
        this.uadPk = uadPk;
    }

    public String getUadCodigo() {
        return uadCodigo;
    }

    public void setUadCodigo(String uadCodigo) {
        this.uadCodigo = uadCodigo;
    }

    public String getUadNombre() {
        return uadNombre;
    }

    public void setUadNombre(String uadNombre) {
        this.uadNombre = uadNombre;
    }

    public String getUadNombreBusqueda() {
        return uadNombreBusqueda;
    }

    public void setUadNombreBusqueda(String uadNombreBusqueda) {
        this.uadNombreBusqueda = uadNombreBusqueda;
    }

    public Boolean getUadHabilitado() {
        return uadHabilitado;
    }

    public void setUadHabilitado(Boolean uadHabilitado) {
        this.uadHabilitado = uadHabilitado;
    }

    public String getUadDireccion() {
        return uadDireccion;
    }

    public void setUadDireccion(String uadDireccion) {
        this.uadDireccion = uadDireccion;
    }

    public String getUadNombreDirector() {
        return uadNombreDirector;
    }

    public void setUadNombreDirector(String uadNombreDirector) {
        this.uadNombreDirector = uadNombreDirector;
    }

    public String getUadCargoDirector() {
        return uadCargoDirector;
    }

    public void setUadCargoDirector(String uadCargoDirector) {
        this.uadCargoDirector = uadCargoDirector;
    }

    public String getUadTelefono() {
        return uadTelefono;
    }

    public void setUadTelefono(String uadTelefono) {
        this.uadTelefono = uadTelefono;
    }

    public String getUadResponsable() {
        return uadResponsable;
    }

    public void setUadResponsable(String uadResponsable) {
        this.uadResponsable = uadResponsable;
    }

    public LocalDate getUadFechaInventario() {
        return uadFechaInventario;
    }

    public void setUadFechaInventario(LocalDate uadFechaInventario) {
        this.uadFechaInventario = uadFechaInventario;
    }

    public LocalDateTime getUadUltModFecha() {
        return uadUltModFecha;
    }

    public void setUadUltModFecha(LocalDateTime uadUltModFecha) {
        this.uadUltModFecha = uadUltModFecha;
    }

    public String getUadUltModUsuario() {
        return uadUltModUsuario;
    }

    public void setUadUltModUsuario(String uadUltModUsuario) {
        this.uadUltModUsuario = uadUltModUsuario;
    }

    public Integer getUadVersion() {
        return uadVersion;
    }

    public void setUadVersion(Integer uadVersion) {
        this.uadVersion = uadVersion;
    }

    public SgAfUnidadesActivoFijo getUadUnidadActivoFijoFk() {
        return uadUnidadActivoFijoFk;
    }

    public void setUadUnidadActivoFijoFk(SgAfUnidadesActivoFijo uadUnidadActivoFijoFk) {
        this.uadUnidadActivoFijoFk = uadUnidadActivoFijoFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uadPk != null ? uadPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfUnidadesAdministrativas)) {
            return false;
        }
        SgAfUnidadesAdministrativas other = (SgAfUnidadesAdministrativas) object;
        if ((this.uadPk == null && other.uadPk != null) || (this.uadPk != null && !this.uadPk.equals(other.uadPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas[ uadPk=" + uadPk + " ]";
    }
    
}
