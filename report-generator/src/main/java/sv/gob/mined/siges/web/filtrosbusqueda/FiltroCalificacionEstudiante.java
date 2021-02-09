/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
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
    private Boolean valorAsignado;
    private Long anioLectivoPk;
    private Boolean caeEsPaes;
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

    public Long getSeccionPk() {
        return seccionPk;
    }

    public void setSeccionPk(Long seccionPk) {
        this.seccionPk = seccionPk;
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

    public Long getExcluirCabezalPk() {
        return excluirCabezalPk;
    }

    public void setExcluirCabezalPk(Long excluirCabezalPk) {
        this.excluirCabezalPk = excluirCabezalPk;
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

    public Boolean getValorAsignado() {
        return valorAsignado;
    }

    public void setValorAsignado(Boolean valorAsignado) {
        this.valorAsignado = valorAsignado;
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }

    public Long getSeccionActualEstudiantesPk() {
        return seccionActualEstudiantesPk;
    }

    public void setSeccionActualEstudiantesPk(Long seccionActualEstudiantesPk) {
        this.seccionActualEstudiantesPk = seccionActualEstudiantesPk;
    }

    public Boolean getCaeEsPaes() {
        return caeEsPaes;
    }

    public void setCaeEsPaes(Boolean caeEsPaes) {
        this.caeEsPaes = caeEsPaes;
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
    
    
    
}
