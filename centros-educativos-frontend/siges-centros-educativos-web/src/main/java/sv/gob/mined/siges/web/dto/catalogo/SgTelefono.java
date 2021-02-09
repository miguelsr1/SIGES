/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "telPk", scope = SgTelefono.class)
public class SgTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long telPk;
    
    private String telTelefono;
    
    private SgAsociacion telAsociaciones;
    
    private SgTipoTelefono telTipoTelefono;
    
    private LocalDateTime telUltModFecha;
    
    private String telUltModUsuario;
    
    private Integer telVersion;

    public SgTelefono() {
    }

    public SgTelefono(Long telPk) {
        this.telPk = telPk;
    }

    public Long getTelPk() {
        return telPk;
    }

    public void setTelPk(Long telPk) {
        this.telPk = telPk;
    }

    public String getTelTelefono() {
        return telTelefono;
    }

    public void setTelTelefono(String telTelefono) {
        this.telTelefono = telTelefono;
    }

    public LocalDateTime getTelUltModFecha() {
        return telUltModFecha;
    }

    public void setTelUltModFecha(LocalDateTime telUltModFecha) {
        this.telUltModFecha = telUltModFecha;
    }

    public String getTelUltModUsuario() {
        return telUltModUsuario;
    }

    public void setTelUltModUsuario(String telUltModUsuario) {
        this.telUltModUsuario = telUltModUsuario;
    }

    public Integer getTelVersion() {
        return telVersion;
    }

    public void setTelVersion(Integer telVersion) {
        this.telVersion = telVersion;
    }

    public SgAsociacion getTelAsociaciones() {
        return telAsociaciones;
    }

    public void setTelAsociaciones(SgAsociacion telAsociaciones) {
        this.telAsociaciones = telAsociaciones;
    }

    public SgTipoTelefono getTelTipoTelefono() {
        return telTipoTelefono;
    }

    public void setTelTipoTelefono(SgTipoTelefono telTipoTelefono) {
        this.telTipoTelefono = telTipoTelefono;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telPk != null ? telPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTelefono)) {
            return false;
        }
        SgTelefono other = (SgTelefono) object;
        if ((this.telPk == null && other.telPk != null) || (this.telPk != null && !this.telPk.equals(other.telPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTelefono[ telPk=" + telPk + " ]";
    }
    
}
