/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "espPk", scope = SgEspecialidad.class)
public class SgEspecialidad implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long espPk;
    
    private String espCodigo;
    
    private Boolean espHabilitado;
    
    private String espNombre;
    
    private String espNombreBusqueda;
    
    private LocalDateTime espUltModFecha;
    
    private String espUltModUsuario;
    
    private Integer espVersion;

    public SgEspecialidad() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.espNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.espNombre);
    }

    public SgEspecialidad(Long espPk) {
        this.espPk = espPk;
    }

    public Long getEspPk() {
        return espPk;
    }

    public void setEspPk(Long espPk) {
        this.espPk = espPk;
    }

    public String getEspCodigo() {
        return espCodigo;
    }

    public void setEspCodigo(String espCodigo) {
        this.espCodigo = espCodigo;
    }

    public Boolean getEspHabilitado() {
        return espHabilitado;
    }

    public void setEspHabilitado(Boolean espHabilitado) {
        this.espHabilitado = espHabilitado;
    }

    public String getEspNombre() {
        return espNombre;
    }

    public void setEspNombre(String espNombre) {
        this.espNombre = espNombre;
    }

    public String getEspNombreBusqueda() {
        return espNombreBusqueda;
    }

    public void setEspNombreBusqueda(String espNombreBusqueda) {
        this.espNombreBusqueda = espNombreBusqueda;
    }

    public LocalDateTime getEspUltModFecha() {
        return espUltModFecha;
    }

    public void setEspUltModFecha(LocalDateTime espUltModFecha) {
        this.espUltModFecha = espUltModFecha;
    }

    public String getEspUltModUsuario() {
        return espUltModUsuario;
    }

    public void setEspUltModUsuario(String espUltModUsuario) {
        this.espUltModUsuario = espUltModUsuario;
    }

    public Integer getEspVersion() {
        return espVersion;
    }

    public void setEspVersion(Integer espVersion) {
        this.espVersion = espVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (espPk != null ? espPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEspecialidad)) {
            return false;
        }
        SgEspecialidad other = (SgEspecialidad) object;
        if ((this.espPk == null && other.espPk != null) || (this.espPk != null && !this.espPk.equals(other.espPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEspecialidad[ espPk=" + espPk + " ]";
    }
    
}
