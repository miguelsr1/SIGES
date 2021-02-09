/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgCalificacionCompPeriodo;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroCalificacion extends FiltroSeccion implements Serializable {
    
    private Long calPk;   
    private Long calComponentePlanEstudio;
    private LocalDate calFecha;
    private Long calRangoFecha;
    private EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion;  
    private List<EnumTiposPeriodosCalificaciones> calTiposPeriodoCalificacion;
    private EnumCalendarioEscolar calTipoCalendarioEscolar;    
    private Integer calNumero;
    private List<Long> calSecciones;
    private Long calPeriodoCalificacion;
    private EnumEstadoProcesamientoCalificacionPromocion calEstadoProcesamientoCal;
    private String endOfUsuario;
    private Boolean calTieneEstudiantePendiente;
    private List<SgCalificacionCompPeriodo> calificacionCompPeriodoList;
    private List<Long> calComponentePlanEstudioList;
    private EnumTipoPeriodoSeccion caeTipoPeriodo;
    private Integer caeNumeroPeriodo;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Boolean incluirCalificacionesEstudiantes;
    private Boolean inicializarLazyToOne;
    
    private String securityOperation;
   
    public FiltroCalificacion() {
        inicializarLazyToOne = Boolean.FALSE;
        securityOperation=ConstantesOperaciones.BUSCAR_CALIFICACIONES;
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public String getEndOfUsuario() {
        return endOfUsuario;
    }

    public void setEndOfUsuario(String endOfUsuario) {
        this.endOfUsuario = endOfUsuario;
    }
    

    public Long getCalComponentePlanEstudio() {
        return calComponentePlanEstudio;
    }

    public void setCalComponentePlanEstudio(Long calComponentePlanEstudio) {
        this.calComponentePlanEstudio = calComponentePlanEstudio;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public LocalDate getCalFecha() {
        return calFecha;
    }

    public void setCalFecha(LocalDate calFecha) {
        this.calFecha = calFecha;
    }

    public Long getCalRangoFecha() {
        return calRangoFecha;
    }

    public void setCalRangoFecha(Long calRangoFecha) {
        this.calRangoFecha = calRangoFecha;
    }

    public EnumTiposPeriodosCalificaciones getCalTipoPeriodoCalificacion() {
        return calTipoPeriodoCalificacion;
    }

    public void setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion) {
        this.calTipoPeriodoCalificacion = calTipoPeriodoCalificacion;
    }

    public EnumCalendarioEscolar getCalTipoCalendarioEscolar() {
        return calTipoCalendarioEscolar;
    }

    public void setCalTipoCalendarioEscolar(EnumCalendarioEscolar calTipoCalendarioEscolar) {
        this.calTipoCalendarioEscolar = calTipoCalendarioEscolar;
    }

    public Integer getCalNumero() {
        return calNumero;
    }

    public void setCalNumero(Integer calNumero) {
        this.calNumero = calNumero;
    }   

    public Long getCalPeriodoCalificacion() {
        return calPeriodoCalificacion;
    }

    public void setCalPeriodoCalificacion(Long calPeriodoCalificacion) {
        this.calPeriodoCalificacion = calPeriodoCalificacion;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
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

    public Boolean getIncluirCalificacionesEstudiantes() {
        return incluirCalificacionesEstudiantes;
    }

    public void setIncluirCalificacionesEstudiantes(Boolean incluirCalificacionesEstudiantes) {
        this.incluirCalificacionesEstudiantes = incluirCalificacionesEstudiantes;
    }

    public List<EnumTiposPeriodosCalificaciones> getCalTiposPeriodoCalificacion() {
        return calTiposPeriodoCalificacion;
    }

    public void setCalTiposPeriodoCalificacion(List<EnumTiposPeriodosCalificaciones> calTiposPeriodoCalificacion) {
        this.calTiposPeriodoCalificacion = calTiposPeriodoCalificacion;
    }

    public List<Long> getCalSecciones() {
        return calSecciones;
    }

    public void setCalSecciones(List<Long> calSecciones) {
        this.calSecciones = calSecciones;
    }

    public Boolean getInicializarLazyToOne() {
        return inicializarLazyToOne;
    }

    public void setInicializarLazyToOne(Boolean inicializarLazyToOne) {
        this.inicializarLazyToOne = inicializarLazyToOne;
    }

    public EnumEstadoProcesamientoCalificacionPromocion getCalEstadoProcesamientoCal() {
        return calEstadoProcesamientoCal;
    }

    public void setCalEstadoProcesamientoCal(EnumEstadoProcesamientoCalificacionPromocion calEstadoProcesamientoCal) {
        this.calEstadoProcesamientoCal = calEstadoProcesamientoCal;
    }

    public Boolean getCalTieneEstudiantePendiente() {
        return calTieneEstudiantePendiente;
    }

    public void setCalTieneEstudiantePendiente(Boolean calTieneEstudiantePendiente) {
        this.calTieneEstudiantePendiente = calTieneEstudiantePendiente;
    }

    public List<SgCalificacionCompPeriodo> getCalificacionCompPeriodoList() {
        return calificacionCompPeriodoList;
    }

    public void setCalificacionCompPeriodoList(List<SgCalificacionCompPeriodo> calificacionCompPeriodoList) {
        this.calificacionCompPeriodoList = calificacionCompPeriodoList;
    }

    public List<Long> getCalComponentePlanEstudioList() {
        return calComponentePlanEstudioList;
    }

    public void setCalComponentePlanEstudioList(List<Long> calComponentePlanEstudioList) {
        this.calComponentePlanEstudioList = calComponentePlanEstudioList;
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
