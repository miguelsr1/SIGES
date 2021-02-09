/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.enumerados.EnumModoPago;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;

/**
 * Filtro de los documnetos de recibos
 *
 * @author Sofis Solutions
 */
public class FiltroPago implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;

    private Long pgsPk;
    private Long pgsFactura;
    private EnumModoPago pgsModoPago;
    private SgCuentasBancarias pgsCuentaFk;
    private Integer pgsNumeroCheque;
    private LocalDateTime pgsFechaPago;
    private LocalDate pgsFechaDesdeS;
    private LocalDate pgsFechaHastaS;
    private LocalDateTime pgsFechaDesde;
    private LocalDateTime pgsFechaHasta;
    private Integer origen;
    private Long componente;
    private Long subcomponente;
    private Long unidadPresupuestaria;
    private Long lineaPresupuestaria;
    private Integer pgsVersion;
    private SgMovimientoCuentaBancaria pgsMovimientoCBFk;
    private EnumMovimientosOrigen movimientoOrigenFac;
    private List<Long> sedesIds;
    private Long pgsChequeraFk;

    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    private String securityOperation;
    private Boolean seNecesitaDistinct;

    public FiltroPago() {

        super();
        this.first = 0L;
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_PAGOS;
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

    public Long getPgsFactura() {
        return pgsFactura;
    }

    public void setPgsFactura(Long pgsFactura) {
        this.pgsFactura = pgsFactura;
    }

    public EnumModoPago getPgsModoPago() {
        return pgsModoPago;
    }

    public void setPgsModoPago(EnumModoPago pgsModoPago) {
        this.pgsModoPago = pgsModoPago;
    }

    public SgCuentasBancarias getPgsCuentaFk() {
        return pgsCuentaFk;
    }

    public void setPgsCuentaFk(SgCuentasBancarias pgsCuentaFk) {
        this.pgsCuentaFk = pgsCuentaFk;
    }

    public Integer getPgsNumeroCheque() {
        return pgsNumeroCheque;
    }

    public void setPgsNumeroCheque(Integer pgsNumeroCheque) {
        this.pgsNumeroCheque = pgsNumeroCheque;
    }

    public LocalDateTime getPgsFechaPago() {
        return pgsFechaPago;
    }

    public void setPgsFechaPago(LocalDateTime pgsFechaPago) {
        this.pgsFechaPago = pgsFechaPago;
    }

    public LocalDate getPgsFechaDesdeS() {
        return pgsFechaDesdeS;
    }

    public void setPgsFechaDesdeS(LocalDate pgsFechaDesdeS) {
        this.pgsFechaDesdeS = pgsFechaDesdeS;
    }

    public LocalDate getPgsFechaHastaS() {
        return pgsFechaHastaS;
    }

    public void setPgsFechaHastaS(LocalDate pgsFechaHastaS) {
        this.pgsFechaHastaS = pgsFechaHastaS;
    }

    public LocalDateTime getPgsFechaDesde() {
        return pgsFechaDesde;
    }

    public void setPgsFechaDesde(LocalDateTime pgsFechaDesde) {
        this.pgsFechaDesde = pgsFechaDesde;
    }

    public LocalDateTime getPgsFechaHasta() {
        return pgsFechaHasta;
    }

    public void setPgsFechaHasta(LocalDateTime pgsFechaHasta) {
        this.pgsFechaHasta = pgsFechaHasta;
    }

    public EnumMovimientosOrigen getMovimientoOrigenFac() {
        return movimientoOrigenFac;
    }

    public void setMovimientoOrigenFac(EnumMovimientosOrigen movimientoOrigenFac) {
        this.movimientoOrigenFac = movimientoOrigenFac;
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

    public Integer getOrigen() {
        return origen;
    }

    public void setOrigen(Integer origen) {
        this.origen = origen;
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

    public Long getPgsPk() {
        return pgsPk;
    }

    public void setPgsPk(Long pgsPk) {
        this.pgsPk = pgsPk;
    }

    public Integer getPgsVersion() {
        return pgsVersion;
    }

    public void setPgsVersion(Integer pgsVersion) {
        this.pgsVersion = pgsVersion;
    }

    public SgMovimientoCuentaBancaria getPgsMovimientoCBFk() {
        return pgsMovimientoCBFk;
    }

    public void setPgsMovimientoCBFk(SgMovimientoCuentaBancaria pgsMovimientoCBFk) {
        this.pgsMovimientoCBFk = pgsMovimientoCBFk;
    }

    public List<Long> getSedesIds() {
        return sedesIds;
    }

    public void setSedesIds(List<Long> sedesIds) {
        this.sedesIds = sedesIds;
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

    public Long getPgsChequeraFk() {
        return pgsChequeraFk;
    }

    public void setPgsChequeraFk(Long pgsChequeraFk) {
        this.pgsChequeraFk = pgsChequeraFk;
    }

}
