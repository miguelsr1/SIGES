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
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "mrtId", scope = SgAfMotivoRechazoTraslado.class)
public class SgAfMotivoRechazoTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long mrtId;
    private String mrtMotivoRechazo;
    private LocalDateTime mrtUltModFecha;
    private String mrtUltModUsuario;
    private Integer mrtVersion;
    private SgAfTraslado mrtTrasladoFk;

    public SgAfMotivoRechazoTraslado() {
    }

    public Long getMrtId() {
        return mrtId;
    }

    public void setMrtId(Long mrtId) {
        this.mrtId = mrtId;
    }

    public String getMrtMotivoRechazo() {
        return mrtMotivoRechazo;
    }

    public void setMrtMotivoRechazo(String mrtMotivoRechazo) {
        this.mrtMotivoRechazo = mrtMotivoRechazo;
    }

    public LocalDateTime getMrtUltModFecha() {
        return mrtUltModFecha;
    }

    public void setMrtUltModFecha(LocalDateTime mrtUltModFecha) {
        this.mrtUltModFecha = mrtUltModFecha;
    }

    public String getMrtUltModUsuario() {
        return mrtUltModUsuario;
    }

    public void setMrtUltModUsuario(String mrtUltModUsuario) {
        this.mrtUltModUsuario = mrtUltModUsuario;
    }

    public Integer getMrtVersion() {
        return mrtVersion;
    }

    public void setMrtVersion(Integer mrtVersion) {
        this.mrtVersion = mrtVersion;
    }

    public SgAfTraslado getMrtTrasladoFk() {
        return mrtTrasladoFk;
    }

    public void setMrtTrasladoFk(SgAfTraslado mrtTrasladoFk) {
        this.mrtTrasladoFk = mrtTrasladoFk;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrtId != null ? mrtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfMotivoRechazoTraslado)) {
            return false;
        }
        SgAfMotivoRechazoTraslado other = (SgAfMotivoRechazoTraslado) object;
        if ((this.mrtId == null && other.mrtId != null) || (this.mrtId != null && !this.mrtId.equals(other.mrtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfMotivoRechazoTraslado[ mrtId=" + mrtId + " ]";
    }
    
}
