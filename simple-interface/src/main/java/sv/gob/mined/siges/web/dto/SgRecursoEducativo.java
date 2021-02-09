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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "redPk", scope = SgRecursoEducativo.class)
public class SgRecursoEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long redPk;
    
    private String redCodigo;
    
    private Boolean redHabilitado;
    
    private String redNombre;
    
    private String redNombreBusqueda;
    
    private LocalDateTime redUltModFecha;
    
    private String redUltModUsuario;
    
    private Integer redVersion;

    public SgRecursoEducativo() {
        this.redHabilitado = Boolean.TRUE;
    }

    public SgRecursoEducativo(Long redPk) {
        this.redPk = redPk;
    }

    public Long getRedPk() {
        return redPk;
    }

    public void setRedPk(Long redPk) {
        this.redPk = redPk;
    }

    public String getRedCodigo() {
        return redCodigo;
    }

    public void setRedCodigo(String redCodigo) {
        this.redCodigo = redCodigo;
    }

    public Boolean getRedHabilitado() {
        return redHabilitado;
    }

    public void setRedHabilitado(Boolean redHabilitado) {
        this.redHabilitado = redHabilitado;
    }

    public String getRedNombre() {
        return redNombre;
    }

    public void setRedNombre(String redNombre) {
        this.redNombre = redNombre;
    }

    public String getRedNombreBusqueda() {
        return redNombreBusqueda;
    }

    public void setRedNombreBusqueda(String redNombreBusqueda) {
        this.redNombreBusqueda = redNombreBusqueda;
    }

    public LocalDateTime getRedUltModFecha() {
        return redUltModFecha;
    }

    public void setRedUltModFecha(LocalDateTime redUltModFecha) {
        this.redUltModFecha = redUltModFecha;
    }

    public String getRedUltModUsuario() {
        return redUltModUsuario;
    }

    public void setRedUltModUsuario(String redUltModUsuario) {
        this.redUltModUsuario = redUltModUsuario;
    }

    public Integer getRedVersion() {
        return redVersion;
    }

    public void setRedVersion(Integer redVersion) {
        this.redVersion = redVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (redPk != null ? redPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRecursoEducativo)) {
            return false;
        }
        SgRecursoEducativo other = (SgRecursoEducativo) object;
        if ((this.redPk == null && other.redPk != null) || (this.redPk != null && !this.redPk.equals(other.redPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRecursoEducativo[ redPk=" + redPk + " ]";
    }
    
}
