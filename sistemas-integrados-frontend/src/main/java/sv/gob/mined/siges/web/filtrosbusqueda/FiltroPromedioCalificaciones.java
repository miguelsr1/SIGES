/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroPromedioCalificaciones implements Serializable {

    private Long sistemaPk;
    private Long sedePk;
    private Long cicloPk;
    private Long gradoPk;
    private Long sedModalidadEducativaPk;
    private Long sedOpcionPk;
    private Long sedProgramaEducativoPk;
    private Long sedModalidadAtencionPk;
    private Long sedSubModalidadPk;
    private Long nivelPk;
    private Long secPk;
    private Long anioLectivoPk;
    private Integer anioLectivoAnio;

    public FiltroPromedioCalificaciones() {
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }

    public Integer getAnioLectivoAnio() {
        return anioLectivoAnio;
    }

    public void setAnioLectivoAnio(Integer anioLectivoAnio) {
        this.anioLectivoAnio = anioLectivoAnio;
    }

    public Long getSecPk() {
        return secPk;
    }

    public void setSecPk(Long secPk) {
        this.secPk = secPk;
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

}
