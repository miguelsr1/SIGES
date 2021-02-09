/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioInfraestructura;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rssPk", scope = SgRelSedeServicioInfra.class)
public class SgRelSedeServicioInfra implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rssPk;

    private SgSede rssSede;

    private SgServicioInfraestructura rssServicioInfra;

    private Boolean rssTieneServicio;

    private LocalDateTime rssUltModFecha;

    private String rssUltModUsuario;

    private Integer rssVersion;

    public SgRelSedeServicioInfra() {
    }

    public SgRelSedeServicioInfra(Long rssPk) {
        this.rssPk = rssPk;
    }

    public Long getRssPk() {
        return rssPk;
    }

    public void setRssPk(Long rssPk) {
        this.rssPk = rssPk;
    }

    public SgSede getRssSede() {
        return rssSede;
    }

    public void setRssSede(SgSede rssSede) {
        this.rssSede = rssSede;
    }

    public SgServicioInfraestructura getRssServicioInfra() {
        return rssServicioInfra;
    }

    public void setRssServicioInfra(SgServicioInfraestructura rssServicioInfra) {
        this.rssServicioInfra = rssServicioInfra;
    }

    public Boolean getRssTieneServicio() {
        return rssTieneServicio;
    }

    public void setRssTieneServicio(Boolean rssTieneServicio) {
        this.rssTieneServicio = rssTieneServicio;
    }

    public LocalDateTime getRssUltModFecha() {
        return rssUltModFecha;
    }

    public void setRssUltModFecha(LocalDateTime rssUltModFecha) {
        this.rssUltModFecha = rssUltModFecha;
    }

    public String getRssUltModUsuario() {
        return rssUltModUsuario;
    }

    public void setRssUltModUsuario(String rssUltModUsuario) {
        this.rssUltModUsuario = rssUltModUsuario;
    }

    public Integer getRssVersion() {
        return rssVersion;
    }

    public void setRssVersion(Integer rssVersion) {
        this.rssVersion = rssVersion;
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
        final SgRelSedeServicioInfra other = (SgRelSedeServicioInfra) obj;
        if (!Objects.equals(this.rssPk, other.rssPk)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rssPk != null ? rssPk.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgRelSedeServicioInfra[ rssPk=" + rssPk + " ]";
    }

}
