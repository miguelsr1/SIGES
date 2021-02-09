/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.dto.siap2.SsTopePresupuestal;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroMovimientosEdicion implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;
    private Long movPk;
    private String movCodigo;
    private Long movPresupuestoFk;
    private String movFuenteRecursos;
    private EnumMovimientosTipo movTipo;
    private Double movMonto;
    private BigDecimal movMontoAprobado;
    private LocalDate movUltmodFecha;
    private String movUltmodUsuario;
    private Integer movVersion;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Integer movNumMoviento;
    private String movOrigen;
    private Long movActividadPk;
    private Long movFuenteIngresos;
    private String[] incluirCampos;
    private Long movSubAreaInversionPk;
    private SsTopePresupuestal movTechoPresupuestal;
    private Long movFuenteIngresosOriginal;
    private Long movOriginalEditado;

    @Inject
    private SessionBean sessionBean;

    public FiltroMovimientosEdicion() {

        super();
        this.first = 0L;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMovPk() {
        return movPk;
    }

    public void setMovPk(Long movPk) {
        this.movPk = movPk;
    }

    public String getMovCodigo() {
        return movCodigo;
    }

    public void setMovCodigo(String movCodigo) {
        this.movCodigo = movCodigo;
    }

    public Long getMovPresupuestoFk() {
        return movPresupuestoFk;
    }

    public void setMovPresupuestoFk(Long movPresupuestoFk) {
        this.movPresupuestoFk = movPresupuestoFk;
    }

    public String getMovFuenteRecursos() {
        return movFuenteRecursos;
    }

    public void setMovFuenteRecursos(String movFuenteRecursos) {
        this.movFuenteRecursos = movFuenteRecursos;
    }

    public EnumMovimientosTipo getMovTipo() {
        return movTipo;
    }

    public void setMovTipo(EnumMovimientosTipo movTipo) {
        this.movTipo = movTipo;
    }

    public Double getMovMonto() {
        return movMonto;
    }

    public void setMovMonto(Double movMonto) {
        this.movMonto = movMonto;
    }

    public LocalDate getMovUltmodFecha() {
        return movUltmodFecha;
    }

    public void setMovUltmodFecha(LocalDate movUltmodFecha) {
        this.movUltmodFecha = movUltmodFecha;
    }

    public String getMovUltmodUsuario() {
        return movUltmodUsuario;
    }

    public void setMovUltmodUsuario(String movUltmodUsuario) {
        this.movUltmodUsuario = movUltmodUsuario;
    }

    public Integer getMovVersion() {
        return movVersion;
    }

    public void setMovVersion(Integer movVersion) {
        this.movVersion = movVersion;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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

    public Integer getMovNumMoviento() {
        return movNumMoviento;
    }

    public void setMovNumMoviento(Integer movNumMoviento) {
        this.movNumMoviento = movNumMoviento;
    }

    public String getMovOrigen() {
        return movOrigen;
    }

    public void setMovOrigen(String movOrigen) {
        this.movOrigen = movOrigen;
    }

    public Long getMovActividadPk() {
        return movActividadPk;
    }

    public void setMovActividadPk(Long movActividadPk) {
        this.movActividadPk = movActividadPk;
    }

    public Long getMovFuenteIngresos() {
        return movFuenteIngresos;
    }

    public void setMovFuenteIngresos(Long movFuenteIngresos) {
        this.movFuenteIngresos = movFuenteIngresos;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getMovSubAreaInversionPk() {
        return movSubAreaInversionPk;
    }

    public void setMovSubAreaInversionPk(Long movSubAreaInversionPk) {
        this.movSubAreaInversionPk = movSubAreaInversionPk;
    }

    public BigDecimal getMovMontoAprobado() {
        return movMontoAprobado;
    }

    public void setMovMontoAprobado(BigDecimal movMontoAprobado) {
        this.movMontoAprobado = movMontoAprobado;
    }

    public SsTopePresupuestal getMovTechoPresupuestal() {
        return movTechoPresupuestal;
    }

    public void setMovTechoPresupuestal(SsTopePresupuestal movTechoPresupuestal) {
        this.movTechoPresupuestal = movTechoPresupuestal;
    }

    public Long getMovFuenteIngresosOriginal() {
        return movFuenteIngresosOriginal;
    }

    public void setMovFuenteIngresosOriginal(Long movFuenteIngresosOriginal) {
        this.movFuenteIngresosOriginal = movFuenteIngresosOriginal;
    }

    public Long getMovOriginalEditado() {
        return movOriginalEditado;
    }

    public void setMovOriginalEditado(Long movOriginalEditado) {
        this.movOriginalEditado = movOriginalEditado;
    }

}
