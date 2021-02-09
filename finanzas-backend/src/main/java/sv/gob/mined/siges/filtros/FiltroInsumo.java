/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Filtro de los insumos
 *
 * @author jgiron
 */
public class FiltroInsumo implements Serializable {

    private Long insPk;

    private String insCodigo;

    private String insDescr;

    private String insNombre;

    private Long relInsumoArea;

    private Boolean ins_Ce;

    private LocalDateTime insUltmodFecha;

    private String insUltmodUsuario;

    private Integer insVersion;
    private Long relInsumoSubArea;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroInsumo() {
        this.seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getRelInsumoSubArea() {
        return relInsumoSubArea;
    }

    public void setRelInsumoSubArea(Long relInsumoSubArea) {
        this.relInsumoSubArea = relInsumoSubArea;
    }

    public Long getRelInsumoArea() {
        return relInsumoArea;
    }

    public void setRelInsumoArea(Long relInsumoArea) {
        this.relInsumoArea = relInsumoArea;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Long getInsPk() {
        return insPk;
    }

    public void setInsPk(Long insPk) {
        this.insPk = insPk;
    }

    public String getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(String insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsDescr() {
        return insDescr;
    }

    public void setInsDescr(String insDescr) {
        this.insDescr = insDescr;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public Boolean getIns_Ce() {
        return ins_Ce;
    }

    public void setIns_Ce(Boolean ins_Ce) {
        this.ins_Ce = ins_Ce;
    }

    public LocalDateTime getInsUltmodFecha() {
        return insUltmodFecha;
    }

    public void setInsUltmodFecha(LocalDateTime insUltmodFecha) {
        this.insUltmodFecha = insUltmodFecha;
    }

    public String getInsUltmodUsuario() {
        return insUltmodUsuario;
    }

    public void setInsUltmodUsuario(String insUltmodUsuario) {
        this.insUltmodUsuario = insUltmodUsuario;
    }

    public Integer getInsVersion() {
        return insVersion;
    }

    public void setInsVersion(Integer insVersion) {
        this.insVersion = insVersion;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

}
