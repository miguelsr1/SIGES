/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPlanCompras;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsProveedor;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumTipoDocumentoPago;
import sv.gob.mined.siges.web.enumerados.EnumTipoItemFactura;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroFactura implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;

    private Long facPk;
    private String facNumero;
    private SgSede facSedeFk;
    private SsProveedor facProveedorFk;
    private LocalDate facFechaFactura;
    private BigDecimal facSubTotal;
    private BigDecimal facDeducciones;
    private EnumTipoItemFactura facTipoItem;
    private SgPlanCompras facItemPlanCompra;
    private SgMovimientos facItemMovimiento;
    private EnumFacturaEstado facEstado;
    private BigDecimal facTotal;
    private LocalDateTime facUltModFecha;
    private String facUltModUsuario;
    private Integer facVersion;
    private Long sedePk;
    private Long componente;
    private Long subcomponente;
    private Long unidadPresupuestaria;
    private Long lineaPresupuestaria;
    private Long facProveedorId;
    private Long facAnioFiscalId;
    private EnumTipoDocumentoPago facTipoDocumento;
    private LocalDate facFechaFacturaDesde;
    private LocalDate facFechaFacturaHasta;
    private LocalDate facFechaNotaCredito;
    private String facNotaCredito;
    private Integer anioFiscal;
    private EnumMovimientosOrigen movimientoOrigen;
    private String facNumeroComplete;
    private List<Long> sedesIds;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private String securityOperation;
    private Boolean seNecesitaDistinct;

    @Inject
    private SessionBean sessionBean;

    public FiltroFactura() {

        super();
        this.first = 0L;
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_FACTURAS;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public Long getFacPk() {
        return facPk;
    }

    public void setFacPk(Long facPk) {
        this.facPk = facPk;
    }

    public String getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(String facNumero) {
        this.facNumero = facNumero;
    }

    public LocalDate getFacFechaFactura() {
        return facFechaFactura;
    }

    public void setFacFechaFactura(LocalDate facFechaFactura) {
        this.facFechaFactura = facFechaFactura;
    }

    public BigDecimal getFacSubTotal() {
        return facSubTotal;
    }

    public void setFacSubTotal(BigDecimal facSubTotal) {
        this.facSubTotal = facSubTotal;
    }

    public BigDecimal getFacDeducciones() {
        return facDeducciones;
    }

    public void setFacDeducciones(BigDecimal facDeducciones) {
        this.facDeducciones = facDeducciones;
    }

    public EnumTipoItemFactura getFacTipoItem() {
        return facTipoItem;
    }

    public void setFacTipoItem(EnumTipoItemFactura facTipoItem) {
        this.facTipoItem = facTipoItem;
    }

    public SgPlanCompras getFacItemPlanCompra() {
        return facItemPlanCompra;
    }

    public void setFacItemPlanCompra(SgPlanCompras facItemPlanCompra) {
        this.facItemPlanCompra = facItemPlanCompra;
    }

    public SgMovimientos getFacItemMovimiento() {
        return facItemMovimiento;
    }

    public void setFacItemMovimiento(SgMovimientos facItemMovimiento) {
        this.facItemMovimiento = facItemMovimiento;
    }

    public EnumFacturaEstado getFacEstado() {
        return facEstado;
    }

    public void setFacEstado(EnumFacturaEstado facEstado) {
        this.facEstado = facEstado;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public LocalDateTime getFacUltModFecha() {
        return facUltModFecha;
    }

    public void setFacUltModFecha(LocalDateTime facUltModFecha) {
        this.facUltModFecha = facUltModFecha;
    }

    public String getFacUltModUsuario() {
        return facUltModUsuario;
    }

    public void setFacUltModUsuario(String facUltModUsuario) {
        this.facUltModUsuario = facUltModUsuario;
    }

    public Integer getFacVersion() {
        return facVersion;
    }

    public void setFacVersion(Integer facVersion) {
        this.facVersion = facVersion;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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

    public SgSede getFacSedeFk() {
        return facSedeFk;
    }

    public void setFacSedeFk(SgSede facSedeFk) {
        this.facSedeFk = facSedeFk;
    }

    public Long getComponente() {
        return componente;
    }

    public void setComponente(Long componente) {
        this.componente = componente;
    }

    public Long getSubcomponente() {
        return subcomponente;
    }

    public void setSubcomponente(Long subcomponente) {
        this.subcomponente = subcomponente;
    }

    public Long getUnidadPresupuestaria() {
        return unidadPresupuestaria;
    }

    public void setUnidadPresupuestaria(Long unidadPresupuestaria) {
        this.unidadPresupuestaria = unidadPresupuestaria;
    }

    public Long getLineaPresupuestaria() {
        return lineaPresupuestaria;
    }

    public void setLineaPresupuestaria(Long lineaPresupuestaria) {
        this.lineaPresupuestaria = lineaPresupuestaria;
    }

    public LocalDate getFacFechaFacturaDesde() {
        return facFechaFacturaDesde;
    }

    public void setFacFechaFacturaDesde(LocalDate facFechaFacturaDesde) {
        this.facFechaFacturaDesde = facFechaFacturaDesde;
    }

    public LocalDate getFacFechaFacturaHasta() {
        return facFechaFacturaHasta;
    }

    public void setFacFechaFacturaHasta(LocalDate facFechaFacturaHasta) {
        this.facFechaFacturaHasta = facFechaFacturaHasta;
    }

    public SsProveedor getFacProveedorFk() {
        return facProveedorFk;
    }

    public void setFacProveedorFk(SsProveedor facProveedorFk) {
        this.facProveedorFk = facProveedorFk;
    }

    public Long getFacAnioFiscalId() {
        return facAnioFiscalId;
    }

    public void setFacAnioFiscalId(Long facAnioFiscalId) {
        this.facAnioFiscalId = facAnioFiscalId;
    }

    public EnumTipoDocumentoPago getFacTipoDocumento() {
        return facTipoDocumento;
    }

    public void setFacTipoDocumento(EnumTipoDocumentoPago facTipoDocumento) {
        this.facTipoDocumento = facTipoDocumento;
    }

    public LocalDate getFacFechaNotaCredito() {
        return facFechaNotaCredito;
    }

    public void setFacFechaNotaCredito(LocalDate facFechaNotaCredito) {
        this.facFechaNotaCredito = facFechaNotaCredito;
    }

    public String getFacNotaCredito() {
        return facNotaCredito;
    }

    public void setFacNotaCredito(String facNotaCredito) {
        this.facNotaCredito = facNotaCredito;
    }

    public Long getFacProveedorId() {
        return facProveedorId;
    }

    public void setFacProveedorId(Long facProveedorId) {
        this.facProveedorId = facProveedorId;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public EnumMovimientosOrigen getMovimientoOrigen() {
        return movimientoOrigen;
    }

    public void setMovimientoOrigen(EnumMovimientosOrigen movimientoOrigen) {
        this.movimientoOrigen = movimientoOrigen;
    }

    public List<Long> getSedesIds() {
        return sedesIds;
    }

    public void setSedesIds(List<Long> sedesIds) {
        this.sedesIds = sedesIds;
    }

    public String getFacNumeroComplete() {
        return facNumeroComplete;
    }

    public void setFacNumeroComplete(String facNumeroComplete) {
        this.facNumeroComplete = facNumeroComplete;
    }
    
    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    // </editor-fold>

    
}
