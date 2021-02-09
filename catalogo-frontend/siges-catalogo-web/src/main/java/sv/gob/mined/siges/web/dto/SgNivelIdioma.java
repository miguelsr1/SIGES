/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nidPk", scope = SgNivelIdioma.class)
public class SgNivelIdioma implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long nidPk;
    
    private String nidCodigo;
    
    private Boolean nidHabilitado;
    
    private String nidNombre;
    
    private String nidNombreBusqueda;
    
    private LocalDateTime nidUltModFecha;
    
    private String nidUltModUsuario;
    
    private Integer nidVersion;

    public SgNivelIdioma() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nidNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nidNombre);
    }

    public SgNivelIdioma(Long nidPk) {
        this.nidPk = nidPk;
    }

    public Long getNidPk() {
        return nidPk;
    }

    public void setNidPk(Long nidPk) {
        this.nidPk = nidPk;
    }

    public String getNidCodigo() {
        return nidCodigo;
    }

    public void setNidCodigo(String nidCodigo) {
        this.nidCodigo = nidCodigo;
    }

    public Boolean getNidHabilitado() {
        return nidHabilitado;
    }

    public void setNidHabilitado(Boolean nidHabilitado) {
        this.nidHabilitado = nidHabilitado;
    }

    public String getNidNombre() {
        return nidNombre;
    }

    public void setNidNombre(String nidNombre) {
        this.nidNombre = nidNombre;
    }

    public String getNidNombreBusqueda() {
        return nidNombreBusqueda;
    }

    public void setNidNombreBusqueda(String nidNombreBusqueda) {
        this.nidNombreBusqueda = nidNombreBusqueda;
    }

    public LocalDateTime getNidUltModFecha() {
        return nidUltModFecha;
    }

    public void setNidUltModFecha(LocalDateTime nidUltModFecha) {
        this.nidUltModFecha = nidUltModFecha;
    }

    public String getNidUltModUsuario() {
        return nidUltModUsuario;
    }

    public void setNidUltModUsuario(String nidUltModUsuario) {
        this.nidUltModUsuario = nidUltModUsuario;
    }

    public Integer getNidVersion() {
        return nidVersion;
    }

    public void setNidVersion(Integer nidVersion) {
        this.nidVersion = nidVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nidPk != null ? nidPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelIdioma)) {
            return false;
        }
        SgNivelIdioma other = (SgNivelIdioma) object;
        if ((this.nidPk == null && other.nidPk != null) || (this.nidPk != null && !this.nidPk.equals(other.nidPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelIdioma[ nidPk=" + nidPk + " ]";
    }
    
}
