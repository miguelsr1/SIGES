/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.persistencia.entidades.SgSede;

/**
 * Filtro del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroLiquidacion implements Serializable {

    private Long liqPk;
    private Long liqComponentePk;
    private Long liqSubComponenteFk;
    private Long liqSedePk;
    private Long departamentoPk;
    private Integer anioFiscal;
    private EnumEstadoLiquidacion liqEstado;
    private Boolean habilitado;
    private SgSede pesSedeFk;
    private Long anioLectivoPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroLiquidacion() {
    }

    public Long getLiqPk() {
        return liqPk;
    }

    public void setLiqPk(Long liqPk) {
        this.liqPk = liqPk;
    }

    public Long getLiqComponentePk() {
        return liqComponentePk;
    }

    public void setLiqComponentePk(Long liqComponentePk) {
        this.liqComponentePk = liqComponentePk;
    }

    public Long getLiqSubComponenteFk() {
        return liqSubComponenteFk;
    }

    public void setLiqSubComponenteFk(Long liqSubComponenteFk) {
        this.liqSubComponenteFk = liqSubComponenteFk;
    }

    public Long getLiqSedePk() {
        return liqSedePk;
    }

    public void setLiqSedePk(Long liqSedePk) {
        this.liqSedePk = liqSedePk;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public EnumEstadoLiquidacion getLiqEstado() {
        return liqEstado;
    }

    public void setLiqEstado(EnumEstadoLiquidacion liqEstado) {
        this.liqEstado = liqEstado;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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

    public SgSede getPesSedeFk() {
        return pesSedeFk;
    }

    public void setPesSedeFk(SgSede pesSedeFk) {
        this.pesSedeFk = pesSedeFk;
    }

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    public Long getAnioLectivoPk() {
        return anioLectivoPk;
    }

    public void setAnioLectivoPk(Long anioLectivoPk) {
        this.anioLectivoPk = anioLectivoPk;
    }

}
