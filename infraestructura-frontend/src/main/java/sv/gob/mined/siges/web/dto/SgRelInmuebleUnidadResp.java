/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.enumerados.EnumTipoUnidadResponsable;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "riuPk", scope = SgRelInmuebleUnidadResp.class)
public class SgRelInmuebleUnidadResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long riuPk;

    private SgInmueblesSedes riuInmuebleFk;

    private EnumTipoUnidadResponsable riuTipoUnidadResp;

    private SgSede riuSedeFk;

    private SgAfUnidadesAdministrativas riuAfUnidadAdministrativa;

    private LocalDateTime riuUltModFecha;

    private String riuUltModUsuario;

    private Integer riuVersion;

    public SgRelInmuebleUnidadResp() {
        riuTipoUnidadResp=EnumTipoUnidadResponsable.SEDE;
    }

    public String getNombreElemento() {
        switch (this.riuTipoUnidadResp) {
            case SEDE:
                return (riuSedeFk != null ? riuSedeFk.getSedCodigoNombre() : null);
            case UNIDAD_ADMINISTRATIVA:
                return (riuAfUnidadAdministrativa != null ? riuAfUnidadAdministrativa.getUadNombre() : null);
            default:
        }
        return null;
    }

    public Long getRiuPk() {
        return riuPk;
    }

    public void setRiuPk(Long riuPk) {
        this.riuPk = riuPk;
    }

    public EnumTipoUnidadResponsable getRiuTipoUnidadResp() {
        return riuTipoUnidadResp;
    }

    public void setRiuTipoUnidadResp(EnumTipoUnidadResponsable riuTipoUnidadResp) {
        this.riuTipoUnidadResp = riuTipoUnidadResp;
    }

    public SgSede getRiuSedeFk() {
        return riuSedeFk;
    }

    public void setRiuSedeFk(SgSede riuSedeFk) {
        this.riuSedeFk = riuSedeFk;
    }

    public SgAfUnidadesAdministrativas getRiuAfUnidadAdministrativa() {
        return riuAfUnidadAdministrativa;
    }

    public void setRiuAfUnidadAdministrativa(SgAfUnidadesAdministrativas riuAfUnidadAdministrativa) {
        this.riuAfUnidadAdministrativa = riuAfUnidadAdministrativa;
    }

    public LocalDateTime getRiuUltModFecha() {
        return riuUltModFecha;
    }

    public void setRiuUltModFecha(LocalDateTime riuUltModFecha) {
        this.riuUltModFecha = riuUltModFecha;
    }

    public String getRiuUltModUsuario() {
        return riuUltModUsuario;
    }

    public void setRiuUltModUsuario(String riuUltModUsuario) {
        this.riuUltModUsuario = riuUltModUsuario;
    }

    public Integer getRiuVersion() {
        return riuVersion;
    }

    public void setRiuVersion(Integer riuVersion) {
        this.riuVersion = riuVersion;
    }

    public SgInmueblesSedes getRiuInmuebleFk() {
        return riuInmuebleFk;
    }

    public void setRiuInmuebleFk(SgInmueblesSedes riuInmuebleFk) {
        this.riuInmuebleFk = riuInmuebleFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riuPk != null ? riuPk.hashCode() : 0);
        return hash;
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
        final SgRelInmuebleUnidadResp other = (SgRelInmuebleUnidadResp) obj;
        if (!Objects.equals(this.riuPk, other.riuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleUnidadResp[ riuPk=" + riuPk + " ]";
    }

}
