/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;

public class FiltroCalificacionEstudiante implements Serializable {

    private Long cabezalPk;
    private Long excluirCabezalPk;
    private Long seccionPk;    
    private Long seccionActualEstudiantesPk; //Secc actual de los estudiantes
    private Long estudiantePk;
    private Long caeEstudiantePk;
    private Long caeRangoFechaPk;
    private List<Long> caeEstudiantesPk;
    private Long caeComponentePlanEstudio;
    private EnumTiposPeriodosCalificaciones caeTipoPeriodoCalificacion;
    private List<EnumTiposPeriodosCalificaciones> caeTiposPeriodoCalificacion;
    private EnumCalendarioEscolar caeTipoCalendarioEscolar;
    private Integer caeNumero;
    private Long anioLectivoPk;
    private Boolean caeEsPaes;
    private Long caeNie;
    private Long caeGradoFk;
    private EnumTipoPeriodoSeccion caeTipoPeriodo;
    private Integer caeNumeroPeriodo;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroCalificacionEstudiante() {
    }

    public Long getCabezalPk() {
        return cabezalPk;
    }

    public void setCabezalPk(Long cabezalPk) {
        this.cabezalPk = cabezalPk;
    }

    public Long getSeccionPk() {
        return seccionPk;
    }

    public void setSeccionPk(Long seccionPk) {
        this.seccionPk = seccionPk;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getCaeEstudiantePk() {
        return caeEstudiantePk;
    }

    public void setCaeEstudiantePk(Long caeEstudiantePk) {
        this.caeEstudiantePk = caeEstudiantePk;
    }

    public Long getCaeRangoFechaPk() {
        return caeRangoFechaPk;
    }

    public void setCaeRangoFechaPk(Long caeRangoFechaPk) {
        this.caeRangoFechaPk = caeRangoFechaPk;
    }

    public EnumTiposPeriodosCalificaciones getCaeTipoPeriodoCalificacion() {
        return caeTipoPeriodoCalificacion;
    }

    public void setCaeTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones caeTipoPeriodoCalificacion) {
        this.caeTipoPeriodoCalificacion = caeTipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getCaeTipoCalendarioEscolar() {
        return caeTipoCalendarioEscolar;
    }

    public void setCaeTipoCalendarioEscolar(EnumCalendarioEscolar caeTipoCalendarioEscolar) {
        this.caeTipoCalendarioEscolar = caeTipoCalendarioEscolar;
    }

    public Integer getCaeNumero() {
        return caeNumero;
    }

    public void setCaeNumero(Integer caeNumero) {
        this.caeNumero = caeNumero;
    }

    public List<Long> getCaeEstudiantesPk() {
        return caeEstudiantesPk;
    }

    public void setCaeEstudiantesPk(List<Long> caeEstudiantesPk) {
        this.caeEstudiantesPk = caeEstudiantesPk;
    }

    public Long getCaeComponentePlanEstudio() {
        return caeComponentePlanEstudio;
    }

    public void setCaeComponentePlanEstudio(Long caeComponentePlanEstudio) {
        this.caeComponentePlanEstudio = caeComponentePlanEstudio;
    }

    public Long getExcluirCabezalPk() {
        return excluirCabezalPk;
    }

    public void setExcluirCabezalPk(Long excluirCabezalPk) {
        this.excluirCabezalPk = excluirCabezalPk;
    }
    public Long getEstudiantePk() {
        return estudiantePk;
    }

    public void setEstudiantePk(Long estudiantePk) {
        this.estudiantePk = estudiantePk;
    }

    public List<EnumTiposPeriodosCalificaciones> getCaeTiposPeriodoCalificacion() {
        return caeTiposPeriodoCalificacion;
    }

    public void setCaeTiposPeriodoCalificacion(List<EnumTiposPeriodosCalificaciones> caeTiposPeriodoCalificacion) {
        this.caeTiposPeriodoCalificacion = caeTiposPeriodoCalificacion;
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }

    public Boolean getCaeEsPaes() {
        return caeEsPaes;
    }

    public void setCaeEsPaes(Boolean caeEsPaes) {
        this.caeEsPaes = caeEsPaes;
    }

    public Long getSeccionActualEstudiantesPk() {
        return seccionActualEstudiantesPk;
    }

    public void setSeccionActualEstudiantesPk(Long seccionActualEstudiantesPk) {
        this.seccionActualEstudiantesPk = seccionActualEstudiantesPk;
    }

    public Long getCaeNie() {
        return caeNie;
    }

    public void setCaeNie(Long caeNie) {
        this.caeNie = caeNie;
    }

    public Long getCaeGradoFk() {
        return caeGradoFk;
    }

    public void setCaeGradoFk(Long caeGradoFk) {
        this.caeGradoFk = caeGradoFk;
    }

    public EnumTipoPeriodoSeccion getCaeTipoPeriodo() {
        return caeTipoPeriodo;
    }

    public void setCaeTipoPeriodo(EnumTipoPeriodoSeccion caeTipoPeriodo) {
        this.caeTipoPeriodo = caeTipoPeriodo;
    }

    public Integer getCaeNumeroPeriodo() {
        return caeNumeroPeriodo;
    }

    public void setCaeNumeroPeriodo(Integer caeNumeroPeriodo) {
        this.caeNumeroPeriodo = caeNumeroPeriodo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.cabezalPk);
        hash = 79 * hash + Objects.hashCode(this.excluirCabezalPk);
        hash = 79 * hash + Objects.hashCode(this.seccionPk);
        hash = 79 * hash + Objects.hashCode(this.seccionActualEstudiantesPk);
        hash = 79 * hash + Objects.hashCode(this.estudiantePk);
        hash = 79 * hash + Objects.hashCode(this.caeEstudiantePk);
        hash = 79 * hash + Objects.hashCode(this.caeRangoFechaPk);
        hash = 79 * hash + Objects.hashCode(this.caeEstudiantesPk);
        hash = 79 * hash + Objects.hashCode(this.caeComponentePlanEstudio);
        hash = 79 * hash + Objects.hashCode(this.caeTipoPeriodoCalificacion);
        hash = 79 * hash + Objects.hashCode(this.caeTiposPeriodoCalificacion);
        hash = 79 * hash + Objects.hashCode(this.caeTipoCalendarioEscolar);
        hash = 79 * hash + Objects.hashCode(this.caeNumero);
        hash = 79 * hash + Objects.hashCode(this.anioLectivoPk);
        hash = 79 * hash + Objects.hashCode(this.caeEsPaes);
        hash = 79 * hash + Objects.hashCode(this.caeNie);
        hash = 79 * hash + Objects.hashCode(this.first);
        hash = 79 * hash + Objects.hashCode(this.maxResults);
        hash = 79 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 79 * hash + Arrays.hashCode(this.ascending);
        hash = 79 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroCalificacionEstudiante other = (FiltroCalificacionEstudiante) obj;
        if (!Objects.equals(this.cabezalPk, other.cabezalPk)) {
            return false;
        }
        if (!Objects.equals(this.excluirCabezalPk, other.excluirCabezalPk)) {
            return false;
        }
        if (!Objects.equals(this.seccionPk, other.seccionPk)) {
            return false;
        }
        if (!Objects.equals(this.seccionActualEstudiantesPk, other.seccionActualEstudiantesPk)) {
            return false;
        }
        if (!Objects.equals(this.estudiantePk, other.estudiantePk)) {
            return false;
        }
        if (!Objects.equals(this.caeEstudiantePk, other.caeEstudiantePk)) {
            return false;
        }
        if (!Objects.equals(this.caeRangoFechaPk, other.caeRangoFechaPk)) {
            return false;
        }
        if (!Objects.equals(this.caeEstudiantesPk, other.caeEstudiantesPk)) {
            return false;
        }
        if (!Objects.equals(this.caeComponentePlanEstudio, other.caeComponentePlanEstudio)) {
            return false;
        }
        if (this.caeTipoPeriodoCalificacion != other.caeTipoPeriodoCalificacion) {
            return false;
        }
        if (!Objects.equals(this.caeTiposPeriodoCalificacion, other.caeTiposPeriodoCalificacion)) {
            return false;
        }
        if (this.caeTipoCalendarioEscolar != other.caeTipoCalendarioEscolar) {
            return false;
        }
        if (!Objects.equals(this.caeNumero, other.caeNumero)) {
            return false;
        }
        if (!Objects.equals(this.anioLectivoPk, other.anioLectivoPk)) {
            return false;
        }
        if (!Objects.equals(this.caeEsPaes, other.caeEsPaes)) {
            return false;
        }
        if (!Objects.equals(this.caeNie, other.caeNie)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltroCalificacionEstudiante{" + "cabezalPk=" + cabezalPk + ", excluirCabezalPk=" + excluirCabezalPk + ", seccionPk=" + seccionPk + ", seccionActualEstudiantesPk=" + seccionActualEstudiantesPk + ", estudiantePk=" + estudiantePk + ", caeEstudiantePk=" + caeEstudiantePk + ", caeRangoFechaPk=" + caeRangoFechaPk + ", caeEstudiantesPk=" + caeEstudiantesPk + ", caeComponentePlanEstudio=" + caeComponentePlanEstudio + ", caeTipoPeriodoCalificacion=" + caeTipoPeriodoCalificacion + ", caeTiposPeriodoCalificacion=" + caeTiposPeriodoCalificacion + ", caeTipoCalendarioEscolar=" + caeTipoCalendarioEscolar + ", caeNumero=" + caeNumero + ", anioLectivoPk=" + anioLectivoPk + ", caeEsPaes=" + caeEsPaes + ", caeNie=" + caeNie + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", incluirCampos=" + incluirCampos + '}';
    }

    

}
