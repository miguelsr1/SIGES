
package sv.gob.mined.siges.filtros;

import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;

/**
 *
 * @author fabricio
 */
public class FiltroPeriodoCalificacionEstCal {
    
    private Long seccionPk;
    private Long anioLectivoPk;
    private Long componentePlanEstudioPk;
    private Long periodoCalificacionPk;
    private Long gradoPk;
    
    //Los siguientes dos filtros sirven para las calificaciones semestrales
    private EnumTipoPeriodoSeccion tipoPeriodo;
    private Integer numeroPeriodo;


    public Long getSeccionPk() {
        return seccionPk;
    }

    public void setSeccionPk(Long seccionPk) {
        this.seccionPk = seccionPk;
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }

    public Long getComponentePlanEstudioPk() {
        return componentePlanEstudioPk;
    }

    public void setComponentePlanEstudioPk(Long componentePlanEstudioPk) {
        this.componentePlanEstudioPk = componentePlanEstudioPk;
    }

    public Long getPeriodoCalificacionPk() {
        return periodoCalificacionPk;
    }

    public void setPeriodoCalificacionPk(Long periodoCalificacionPk) {
        this.periodoCalificacionPk = periodoCalificacionPk;
    }

    public Long getGradoPk() {
        return gradoPk;
    }

    public void setGradoPk(Long gradoPk) {
        this.gradoPk = gradoPk;
    }

    public EnumTipoPeriodoSeccion getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(EnumTipoPeriodoSeccion tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public Integer getNumeroPeriodo() {
        return numeroPeriodo;
    }

    public void setNumeroPeriodo(Integer numeroPeriodo) {
        this.numeroPeriodo = numeroPeriodo;
    }
    
}
