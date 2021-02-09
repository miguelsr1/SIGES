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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sgcPk", scope = SgSistemaGestionContenido.class)
public class SgSistemaGestionContenido implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long sgcPk;
    
    private String sgcCodigo;
    
    private Boolean sgcHabilitado;
    
    private String sgcNombre;
    
    private String sgcNombreBusqueda;
    
    private LocalDateTime sgcUltModFecha;
    
    private String sgcUltModUsuario;
    
    private Integer sgcVersion;

    public SgSistemaGestionContenido() {
        this.sgcHabilitado = Boolean.TRUE;
    }

    public SgSistemaGestionContenido(Long sgcPk) {
        this.sgcPk = sgcPk;
    }

    public Long getSgcPk() {
        return sgcPk;
    }

    public void setSgcPk(Long sgcPk) {
        this.sgcPk = sgcPk;
    }

    public String getSgcCodigo() {
        return sgcCodigo;
    }

    public void setSgcCodigo(String sgcCodigo) {
        this.sgcCodigo = sgcCodigo;
    }

    public Boolean getSgcHabilitado() {
        return sgcHabilitado;
    }

    public void setSgcHabilitado(Boolean sgcHabilitado) {
        this.sgcHabilitado = sgcHabilitado;
    }

    public String getSgcNombre() {
        return sgcNombre;
    }

    public void setSgcNombre(String sgcNombre) {
        this.sgcNombre = sgcNombre;
    }

    public String getSgcNombreBusqueda() {
        return sgcNombreBusqueda;
    }

    public void setSgcNombreBusqueda(String sgcNombreBusqueda) {
        this.sgcNombreBusqueda = sgcNombreBusqueda;
    }

    public LocalDateTime getSgcUltModFecha() {
        return sgcUltModFecha;
    }

    public void setSgcUltModFecha(LocalDateTime sgcUltModFecha) {
        this.sgcUltModFecha = sgcUltModFecha;
    }

    public String getSgcUltModUsuario() {
        return sgcUltModUsuario;
    }

    public void setSgcUltModUsuario(String sgcUltModUsuario) {
        this.sgcUltModUsuario = sgcUltModUsuario;
    }

    public Integer getSgcVersion() {
        return sgcVersion;
    }

    public void setSgcVersion(Integer sgcVersion) {
        this.sgcVersion = sgcVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sgcPk != null ? sgcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSistemaGestionContenido)) {
            return false;
        }
        SgSistemaGestionContenido other = (SgSistemaGestionContenido) object;
        if ((this.sgcPk == null && other.sgcPk != null) || (this.sgcPk != null && !this.sgcPk.equals(other.sgcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSistemaGestionContenido[ sgcPk=" + sgcPk + " ]";
    }
    
}
