/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "afpPk", scope = SgAfp.class) 
public class SgAfp implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long afpPk;
    
    private String afpCodigo;
    
    private String afpNombre;
    
    private String afpNombreBusqueda;
    
    private Boolean afpHabilitado;
    
    private LocalDateTime afpUltModFecha;
    
    private String afpUltModUsuario;
    
    private Integer afpVersion;

    public SgAfp() {
    }

    public SgAfp(Long afpPk) {
        this.afpPk = afpPk;
    }

    public Long getAfpPk() {
        return afpPk;
    }

    public void setAfpPk(Long afpPk) {
        this.afpPk = afpPk;
    }

    public String getAfpCodigo() {
        return afpCodigo;
    }

    public void setAfpCodigo(String afpCodigo) {
        this.afpCodigo = afpCodigo;
    }

    public String getAfpNombre() {
        return afpNombre;
    }

    public void setAfpNombre(String afpNombre) {
        this.afpNombre = afpNombre;
    }

    public String getAfpNombreBusqueda() {
        return afpNombreBusqueda;
    }

    public void setAfpNombreBusqueda(String afpNombreBusqueda) {
        this.afpNombreBusqueda = afpNombreBusqueda;
    }

    public Boolean getAfpHabilitado() {
        return afpHabilitado;
    }

    public void setAfpHabilitado(Boolean afpHabilitado) {
        this.afpHabilitado = afpHabilitado;
    }

    public LocalDateTime getAfpUltModFecha() {
        return afpUltModFecha;
    }

    public void setAfpUltModFecha(LocalDateTime afpUltModFecha) {
        this.afpUltModFecha = afpUltModFecha;
    }

    public String getAfpUltModUsuario() {
        return afpUltModUsuario;
    }

    public void setAfpUltModUsuario(String afpUltModUsuario) {
        this.afpUltModUsuario = afpUltModUsuario;
    }

    public Integer getAfpVersion() {
        return afpVersion;
    }

    public void setAfpVersion(Integer afpVersion) {
        this.afpVersion = afpVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (afpPk != null ? afpPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfp)) {
            return false;
        }
        SgAfp other = (SgAfp) object;
        if ((this.afpPk == null && other.afpPk != null) || (this.afpPk != null && !this.afpPk.equals(other.afpPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfp[ afpPk=" + afpPk + " ]";
    }
    
}
