package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroChequera implements Serializable {

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

    private Long sede;

    private Long cuentaBancaria;

    private List<Long> sedesIds;

    private String cheSerieComplete;

    private String titular;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    private String securityOperation;
    private Boolean seNecesitaDistinct;

    public FiltroChequera() {
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    public String getCheSerieComplete() {
        return cheSerieComplete;
    }

    public void setCheSerieComplete(String cheSerieComplete) {
        this.cheSerieComplete = cheSerieComplete;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
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
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.chePk);
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
        final FiltroChequera other = (FiltroChequera) obj;
        if (!Objects.equals(this.chePk, other.chePk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
