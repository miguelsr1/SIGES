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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "mrdId", scope = SgAfMotivoRechazoDescargo.class)
public class SgAfMotivoRechazoDescargo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long mrdId;
    private String mrdMotivoRechazo;
    private LocalDateTime mrdUltModFecha;
    private String mrdUltModUsuario;
    private Integer mrdVersion;
    private SgAfDescargo mrdDescargoFk;

    public SgAfMotivoRechazoDescargo() {
    }

    public Long getMrdId() {
        return mrdId;
    }

    public void setMrdId(Long mrdId) {
        this.mrdId = mrdId;
    }

    public String getMrdMotivoRechazo() {
        return mrdMotivoRechazo;
    }

    public void setMrdMotivoRechazo(String mrdMotivoRechazo) {
        this.mrdMotivoRechazo = mrdMotivoRechazo;
    }

    public LocalDateTime getMrdUltModFecha() {
        return mrdUltModFecha;
    }

    public void setMrdUltModFecha(LocalDateTime mrdUltModFecha) {
        this.mrdUltModFecha = mrdUltModFecha;
    }

    public String getMrdUltModUsuario() {
        return mrdUltModUsuario;
    }

    public void setMrdUltModUsuario(String mrdUltModUsuario) {
        this.mrdUltModUsuario = mrdUltModUsuario;
    }

    public Integer getMrdVersion() {
        return mrdVersion;
    }

    public void setMrdVersion(Integer mrdVersion) {
        this.mrdVersion = mrdVersion;
    }

    public SgAfDescargo getMrdDescargoFk() {
        return mrdDescargoFk;
    }

    public void setMrdDescargoFk(SgAfDescargo mrdDescargoFk) {
        this.mrdDescargoFk = mrdDescargoFk;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrdId != null ? mrdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfMotivoRechazoDescargo)) {
            return false;
        }
        SgAfMotivoRechazoDescargo other = (SgAfMotivoRechazoDescargo) object;
        if ((this.mrdId == null && other.mrdId != null) || (this.mrdId != null && !this.mrdId.equals(other.mrdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfMotivoRechazoDescargo[ mrdId=" + mrdId + " ]";
    }
    
}
