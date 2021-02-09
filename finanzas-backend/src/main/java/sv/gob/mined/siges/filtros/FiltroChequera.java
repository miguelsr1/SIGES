/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancarias;

/**
 * Filtro de Chequeras
 *
 * @author Sofis Solutions
 */
public class FiltroChequera implements Serializable {

    private FilterConfig filterConfig = null;

    private Long chePk;

    private SgCuentasBancarias cheCuentaBancariaFk;

    private LocalDate cheFechaChequera;

    private String cheSerie;

    private Integer cheNumeroInicial;

    private Integer cheNumeroFinal;

    private LocalDateTime cheUltMod;

    private String cheUltUsuario;

    private Integer cheVersion;

    private Long sedePk;

    private Long cuentaBancaria;

    private List<Long> sedesIds;

    private String cheSerieComplete;

    private String titular;

    private Long sede;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String securityOperation;
    private Boolean seNecesitaDistinct;

    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroChequera() {

        super();
        this.first = 0L;
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_CHERQUERA;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getChePk() {
        return chePk;
    }

    public void setChePk(Long chePk) {
        this.chePk = chePk;
    }

    public SgCuentasBancarias getCheCuentaBancariaFk() {
        return cheCuentaBancariaFk;
    }

    public void setCheCuentaBancariaFk(SgCuentasBancarias cheCuentaBancariaFk) {
        this.cheCuentaBancariaFk = cheCuentaBancariaFk;
    }

    public LocalDate getCheFechaChequera() {
        return cheFechaChequera;
    }

    public void setCheFechaChequera(LocalDate cheFechaChequera) {
        this.cheFechaChequera = cheFechaChequera;
    }

    public LocalDateTime getCheUltMod() {
        return cheUltMod;
    }

    public void setCheUltMod(LocalDateTime cheUltMod) {
        this.cheUltMod = cheUltMod;
    }

    public String getCheUltUsuario() {
        return cheUltUsuario;
    }

    public void setCheUltUsuario(String cheUltUsuario) {
        this.cheUltUsuario = cheUltUsuario;
    }

    public Integer getCheVersion() {
        return cheVersion;
    }

    public void setCheVersion(Integer cheVersion) {
        this.cheVersion = cheVersion;
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Integer getCheNumeroInicial() {
        return cheNumeroInicial;
    }

    public void setCheNumeroInicial(Integer cheNumeroInicial) {
        this.cheNumeroInicial = cheNumeroInicial;
    }

    public Integer getCheNumeroFinal() {
        return cheNumeroFinal;
    }

    public void setCheNumeroFinal(Integer cheNumeroFinal) {
        this.cheNumeroFinal = cheNumeroFinal;
    }

    public String getCheSerie() {
        return cheSerie;
    }

    public void setCheSerie(String cheSerie) {
        this.cheSerie = cheSerie;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(Long cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
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

    public String getCheSerieComplete() {
        return cheSerieComplete;
    }

    public void setCheSerieComplete(String cheSerieComplete) {
        this.cheSerieComplete = cheSerieComplete;
    }

    public Long getSede() {
        return sede;
    }

    public void setSede(Long sede) {
        this.sede = sede;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    // </editor-fold>
}
