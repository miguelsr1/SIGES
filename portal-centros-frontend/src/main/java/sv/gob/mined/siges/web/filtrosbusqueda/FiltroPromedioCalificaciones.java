/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;

public class FiltroPromedioCalificaciones implements Serializable {

    private Long sistemaPk;
    private Long orgCurricularPk;
    private Long sedePk;
    private Long cicloPk;
    private Long gradoPk;
    private Long sedModalidadEducativaPk;
    private Long sedOpcionPk;
    private Long sedProgramaEducativoPk;
    private Long sedModalidadAtencionPk;
    private Long sedSubModalidadPk;
    private Long nivelPk;

    public FiltroPromedioCalificaciones() {
    }

    public Long getNivelPk() {
        return nivelPk;
    }

    public void setNivelPk(Long nivelPk) {
        this.nivelPk = nivelPk;
    }

    public Long getSedModalidadEducativaPk() {
        return sedModalidadEducativaPk;
    }

    public void setSedModalidadEducativaPk(Long sedModalidadEducativaPk) {
        this.sedModalidadEducativaPk = sedModalidadEducativaPk;
    }

    public Long getSedOpcionPk() {
        return sedOpcionPk;
    }

    public void setSedOpcionPk(Long sedOpcionPk) {
        this.sedOpcionPk = sedOpcionPk;
    }

    public Long getSedProgramaEducativoPk() {
        return sedProgramaEducativoPk;
    }

    public void setSedProgramaEducativoPk(Long sedProgramaEducativoPk) {
        this.sedProgramaEducativoPk = sedProgramaEducativoPk;
    }

    public Long getSedModalidadAtencionPk() {
        return sedModalidadAtencionPk;
    }

    public void setSedModalidadAtencionPk(Long sedModalidadAtencionPk) {
        this.sedModalidadAtencionPk = sedModalidadAtencionPk;
    }

    public Long getSedSubModalidadPk() {
        return sedSubModalidadPk;
    }

    public void setSedSubModalidadPk(Long sedSubModalidadPk) {
        this.sedSubModalidadPk = sedSubModalidadPk;
    }

    
    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getCicloPk() {
        return cicloPk;
    }

    public void setCicloPk(Long cicloPk) {
        this.cicloPk = cicloPk;
    }

    public Long getGradoPk() {
        return gradoPk;
    }

    public void setGradoPk(Long gradoPk) {
        this.gradoPk = gradoPk;
    }

    public Long getSistemaPk() {
        return sistemaPk;
    }

    public void setSistemaPk(Long sistemaPk) {
        this.sistemaPk = sistemaPk;
    }

    public Long getOrgCurricularPk() {
        return orgCurricularPk;
    }

    public void setOrgCurricularPk(Long orgCurricularPk) {
        this.orgCurricularPk = orgCurricularPk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.sistemaPk);
        hash = 97 * hash + Objects.hashCode(this.orgCurricularPk);
        hash = 97 * hash + Objects.hashCode(this.sedePk);
        hash = 97 * hash + Objects.hashCode(this.cicloPk);
        hash = 97 * hash + Objects.hashCode(this.gradoPk);
        hash = 97 * hash + Objects.hashCode(this.sedModalidadEducativaPk);
        hash = 97 * hash + Objects.hashCode(this.sedOpcionPk);
        hash = 97 * hash + Objects.hashCode(this.sedProgramaEducativoPk);
        hash = 97 * hash + Objects.hashCode(this.sedModalidadAtencionPk);
        hash = 97 * hash + Objects.hashCode(this.sedSubModalidadPk);
        hash = 97 * hash + Objects.hashCode(this.nivelPk);
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
        final FiltroPromedioCalificaciones other = (FiltroPromedioCalificaciones) obj;
        if (!Objects.equals(this.sistemaPk, other.sistemaPk)) {
            return false;
        }
        if (!Objects.equals(this.orgCurricularPk, other.orgCurricularPk)) {
            return false;
        }
        if (!Objects.equals(this.sedePk, other.sedePk)) {
            return false;
        }
        if (!Objects.equals(this.cicloPk, other.cicloPk)) {
            return false;
        }
        if (!Objects.equals(this.gradoPk, other.gradoPk)) {
            return false;
        }
        if (!Objects.equals(this.sedModalidadEducativaPk, other.sedModalidadEducativaPk)) {
            return false;
        }
        if (!Objects.equals(this.sedOpcionPk, other.sedOpcionPk)) {
            return false;
        }
        if (!Objects.equals(this.sedProgramaEducativoPk, other.sedProgramaEducativoPk)) {
            return false;
        }
        if (!Objects.equals(this.sedModalidadAtencionPk, other.sedModalidadAtencionPk)) {
            return false;
        }
        if (!Objects.equals(this.sedSubModalidadPk, other.sedSubModalidadPk)) {
            return false;
        }
        if (!Objects.equals(this.nivelPk, other.nivelPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltroPromedioCalificaciones{" + "sistemaPk=" + sistemaPk + ", orgCurricularPk=" + orgCurricularPk + ", sedePk=" + sedePk + ", cicloPk=" + cicloPk + ", gradoPk=" + gradoPk + ", sedModalidadEducativaPk=" + sedModalidadEducativaPk + ", sedOpcionPk=" + sedOpcionPk + ", sedProgramaEducativoPk=" + sedProgramaEducativoPk + ", sedModalidadAtencionPk=" + sedModalidadAtencionPk + ", sedSubModalidadPk=" + sedSubModalidadPk + ", nivelPk=" + nivelPk + '}';
    }
    
    

    
}
