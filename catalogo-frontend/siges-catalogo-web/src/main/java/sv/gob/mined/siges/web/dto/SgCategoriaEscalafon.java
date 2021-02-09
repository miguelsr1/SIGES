/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cesPk", scope = SgCategoriaEscalafon.class)
public class SgCategoriaEscalafon implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cesPk;
    
    private String cesCodigo;
    
    private Boolean cesHabilitado;
    
    private String cesNombre;
    
    private String cesNombreBusqueda;
    
    private LocalDateTime cesUltModFecha;
    
    private String cesUltModUsuario;
    
    private Integer cesVersion;

    public SgCategoriaEscalafon() {
    }

    public SgCategoriaEscalafon(Long cesPk) {
        this.cesPk = cesPk;
    }

    public Long getCesPk() {
        return cesPk;
    }

    public void setCesPk(Long cesPk) {
        this.cesPk = cesPk;
    }

    public String getCesCodigo() {
        return cesCodigo;
    }

    public void setCesCodigo(String cesCodigo) {
        this.cesCodigo = cesCodigo;
    }

    public Boolean getCesHabilitado() {
        return cesHabilitado;
    }

    public void setCesHabilitado(Boolean cesHabilitado) {
        this.cesHabilitado = cesHabilitado;
    }

    public String getCesNombre() {
        return cesNombre;
    }

    public void setCesNombre(String cesNombre) {
        this.cesNombre = cesNombre;
    }

    public String getCesNombreBusqueda() {
        return cesNombreBusqueda;
    }

    public void setCesNombreBusqueda(String cesNombreBusqueda) {
        this.cesNombreBusqueda = cesNombreBusqueda;
    }

    public LocalDateTime getCesUltModFecha() {
        return cesUltModFecha;
    }

    public void setCesUltModFecha(LocalDateTime cesUltModFecha) {
        this.cesUltModFecha = cesUltModFecha;
    }

    public String getCesUltModUsuario() {
        return cesUltModUsuario;
    }

    public void setCesUltModUsuario(String cesUltModUsuario) {
        this.cesUltModUsuario = cesUltModUsuario;
    }

    public Integer getCesVersion() {
        return cesVersion;
    }

    public void setCesVersion(Integer cesVersion) {
        this.cesVersion = cesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cesPk != null ? cesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaEscalafon)) {
            return false;
        }
        SgCategoriaEscalafon other = (SgCategoriaEscalafon) object;
        if ((this.cesPk == null && other.cesPk != null) || (this.cesPk != null && !this.cesPk.equals(other.cesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaEscalafon[ cesPk=" + cesPk + " ]";
    }
    
}
