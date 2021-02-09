/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rgpPk", scope = SgRelGradoPrecedencia.class)
public class SgRelGradoPrecedencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rgpPk;
    
    private SgGrado rgpGradoDestinoFk;
    
    private SgGrado rgpGradoOrigenFk;
    
    private LocalDateTime rgpUltModFecha;

    private String rgpUltModUsuario;

    private Integer rgpVersion;
    
    public SgRelGradoPrecedencia() {
    }


    public Long getRgpPk() {
        return rgpPk;
    }

    public void setRgpPk(Long rgpPk) {
        this.rgpPk = rgpPk;
    }

    public SgGrado getRgpGradoDestinoFk() {
        return rgpGradoDestinoFk;
    }

    public void setRgpGradoDestinoFk(SgGrado rgpGradoDestinoFk) {
        this.rgpGradoDestinoFk = rgpGradoDestinoFk;
    }

    public SgGrado getRgpGradoOrigenFk() {
        return rgpGradoOrigenFk;
    }

    public void setRgpGradoOrigenFk(SgGrado rgpGradoOrigenFk) {
        this.rgpGradoOrigenFk = rgpGradoOrigenFk;
    }
    

    public LocalDateTime getRgpUltModFecha() {
        return rgpUltModFecha;
    }

    public void setRgpUltModFecha(LocalDateTime rgpUltModFecha) {
        this.rgpUltModFecha = rgpUltModFecha;
    }

    public String getRgpUltModUsuario() {
        return rgpUltModUsuario;
    }

    public void setRgpUltModUsuario(String rgpUltModUsuario) {
        this.rgpUltModUsuario = rgpUltModUsuario;
    }

    public Integer getRgpVersion() {
        return rgpVersion;
    }

    public void setRgpVersion(Integer rgpVersion) {
        this.rgpVersion = rgpVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.rgpPk);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgRelGradoPrecedencia other = (SgRelGradoPrecedencia) obj;
        if (!Objects.equals(this.rgpPk, other.rgpPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelGradoPlanEstudio{" + "rgpPk=" + rgpPk + ", rgpUltModFecha=" + rgpUltModFecha + ", rgpUltModUsuario=" + rgpUltModUsuario + ", rgpVersion=" + rgpVersion + '}';
    }
    
    

}
