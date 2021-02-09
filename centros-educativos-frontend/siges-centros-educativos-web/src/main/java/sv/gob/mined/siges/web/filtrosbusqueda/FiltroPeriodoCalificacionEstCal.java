package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;

/**
 *
 * @author fabricio
 */
public class FiltroPeriodoCalificacionEstCal implements Serializable {

    private Long seccionPk;
    private Long anioLectivoPk;
    private Long componentePlanEstudioPk;
    private Long periodoCalificacionPk;
    
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.seccionPk);
        hash = 53 * hash + Objects.hashCode(this.anioLectivoPk);
        hash = 53 * hash + Objects.hashCode(this.componentePlanEstudioPk);
        hash = 53 * hash + Objects.hashCode(this.periodoCalificacionPk);
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
        final FiltroPeriodoCalificacionEstCal other = (FiltroPeriodoCalificacionEstCal) obj;
        if (!Objects.equals(this.seccionPk, other.seccionPk)) {
            return false;
        }
        if (!Objects.equals(this.anioLectivoPk, other.anioLectivoPk)) {
            return false;
        }
        if (!Objects.equals(this.componentePlanEstudioPk, other.componentePlanEstudioPk)) {
            return false;
        }
        if (!Objects.equals(this.periodoCalificacionPk, other.periodoCalificacionPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltroPeriodoCalificacionEstCal{" + "seccionPk=" + seccionPk + ", anioLectivoPk=" + anioLectivoPk + ", componentePlanEstudioPk=" + componentePlanEstudioPk + ", periodoCalificacionPk=" + periodoCalificacionPk + '}';
    }
    
    
    
    

}
