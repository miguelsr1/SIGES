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


/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mliPk", scope = SgMotivoLicencia.class)
public class SgMotivoLicencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long mliPk;
    
    private String mliCodigo;
    
    private Boolean mliHabilitado;
    
    private String mliNombre;
    
    private String mliNombreBusqueda;
    
    private String mliDescripcion;
    
    private LocalDateTime mliUltModFecha;
    
    private String mliUltModUsuario;
    
    private Integer mliVersion;

    public SgMotivoLicencia() {
        this.mliHabilitado = Boolean.TRUE;
    }

    public SgMotivoLicencia(Long mliPk) {
        this.mliPk = mliPk;
    }

    public Long getMliPk() {
        return mliPk;
    }

    public void setMliPk(Long mliPk) {
        this.mliPk = mliPk;
    }

    public String getMliCodigo() {
        return mliCodigo;
    }

    public void setMliCodigo(String mliCodigo) {
        this.mliCodigo = mliCodigo;
    }

    public Boolean getMliHabilitado() {
        return mliHabilitado;
    }

    public void setMliHabilitado(Boolean mliHabilitado) {
        this.mliHabilitado = mliHabilitado;
    }

    public String getMliNombre() {
        return mliNombre;
    }

    public void setMliNombre(String mliNombre) {
        this.mliNombre = mliNombre;
    }

    public String getMliNombreBusqueda() {
        return mliNombreBusqueda;
    }

    public void setMliNombreBusqueda(String mliNombreBusqueda) {
        this.mliNombreBusqueda = mliNombreBusqueda;
    }

    public String getMliDescripcion() {
        return mliDescripcion;
    }

    public void setMliDescripcion(String mliDescripcion) {
        this.mliDescripcion = mliDescripcion;
    }

    public LocalDateTime getMliUltModFecha() {
        return mliUltModFecha;
    }

    public void setMliUltModFecha(LocalDateTime mliUltModFecha) {
        this.mliUltModFecha = mliUltModFecha;
    }

    public String getMliUltModUsuario() {
        return mliUltModUsuario;
    }

    public void setMliUltModUsuario(String mliUltModUsuario) {
        this.mliUltModUsuario = mliUltModUsuario;
    }

    public Integer getMliVersion() {
        return mliVersion;
    }

    public void setMliVersion(Integer mliVersion) {
        this.mliVersion = mliVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mliPk != null ? mliPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoLicencia)) {
            return false;
        }
        SgMotivoLicencia other = (SgMotivoLicencia) object;
        if ((this.mliPk == null && other.mliPk != null) || (this.mliPk != null && !this.mliPk.equals(other.mliPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoLicencia[ mliPk=" + mliPk + " ]";
    }
    
}
