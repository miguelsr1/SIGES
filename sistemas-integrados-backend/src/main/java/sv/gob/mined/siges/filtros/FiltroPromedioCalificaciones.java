/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroPromedioCalificaciones implements Serializable {

    private Long sistemaPk;
    private Long orgCurricularPk;
    private Long sedePk;
    private Long cicloPk;
    private Long gradoPk;
    private Long nivelPk;
    private Long sedModalidadEducativaPk;
    private Long sedOpcionPk;
    private Long sedProgramaEducativoPk;
    private Long sedModalidadAtencionPk;
    private Long sedSubModalidadPk;

    public FiltroPromedioCalificaciones() {
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
    
    public Long getSistemaPk() {
        return sistemaPk;
    }

    public void setSistemaPk(Long sistemaPk) {
        this.sistemaPk = sistemaPk;
    }

    public Long getNivelPk() {
        return nivelPk;
    }

    public void setNivelPk(Long nivelPk) {
        this.nivelPk = nivelPk;
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

    public Long getOrgCurricularPk() {
        return orgCurricularPk;
    }

    public void setOrgCurricularPk(Long orgCurricularPk) {
        this.orgCurricularPk = orgCurricularPk;
    }
    
    

}
