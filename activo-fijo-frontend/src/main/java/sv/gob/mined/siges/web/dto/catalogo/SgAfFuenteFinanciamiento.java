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
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "ffiPk", scope = SgAfFuenteFinanciamiento.class)
public class SgAfFuenteFinanciamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long ffiPk;
    private String ffiCodigo;
    private String ffiNombre;
    private String ffiNombreBusqueda;
    private Boolean ffiHabilitado;
    private Boolean ffiRequiereProyecto;
    private LocalDateTime ffiUltModFecha;
    private String ffiUltModUsuario;
    private String[] ffiAplicaPara;
    private Integer ffiVersion;
    
    public SgAfFuenteFinanciamiento() {
        ffiHabilitado = Boolean.TRUE;
    }


    public Long getFfiPk() {
        return ffiPk;
    }

    public void setFfiPk(Long ffiPk) {
        this.ffiPk = ffiPk;
    }

    public String getFfiCodigo() {
        return ffiCodigo;
    }

    public void setFfiCodigo(String ffiCodigo) {
        this.ffiCodigo = ffiCodigo;
    }

    public String getFfiNombre() {
        return ffiNombre;
    }

    public void setFfiNombre(String ffiNombre) {
        this.ffiNombre = ffiNombre;
    }

    public String getFfiNombreBusqueda() {
        return ffiNombreBusqueda;
    }

    public void setFfiNombreBusqueda(String ffiNombreBusqueda) {
        this.ffiNombreBusqueda = ffiNombreBusqueda;
    }

    public Boolean getFfiHabilitado() {
        return ffiHabilitado;
    }

    public void setFfiHabilitado(Boolean ffiHabilitado) {
        this.ffiHabilitado = ffiHabilitado;
    }

    public LocalDateTime getFfiUltModFecha() {
        return ffiUltModFecha;
    }

    public void setFfiUltModFecha(LocalDateTime ffiUltModFecha) {
        this.ffiUltModFecha = ffiUltModFecha;
    }

    public String getFfiUltModUsuario() {
        return ffiUltModUsuario;
    }

    public void setFfiUltModUsuario(String ffiUltModUsuario) {
        this.ffiUltModUsuario = ffiUltModUsuario;
    }

    public Integer getFfiVersion() {
        return ffiVersion;
    }

    public void setFfiVersion(Integer ffiVersion) {
        this.ffiVersion = ffiVersion;
    }

    public String[] getFfiAplicaPara() {
        return ffiAplicaPara;
    }

    public void setFfiAplicaPara(String[] ffiAplicaPara) {
        this.ffiAplicaPara = ffiAplicaPara;
    }

    public Boolean getFfiRequiereProyecto() {
        return ffiRequiereProyecto;
    }

    public void setFfiRequiereProyecto(Boolean ffiRequiereProyecto) {
        this.ffiRequiereProyecto = ffiRequiereProyecto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ffiPk != null ? ffiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfFuenteFinanciamiento)) {
            return false;
        }
        SgAfFuenteFinanciamiento other = (SgAfFuenteFinanciamiento) object;
        if ((this.ffiPk == null && other.ffiPk != null) || (this.ffiPk != null && !this.ffiPk.equals(other.ffiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfFuenteFinanciamiento[ ffiPk=" + ffiPk + " ]";
    }
    
}