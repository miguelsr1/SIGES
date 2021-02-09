/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;



/**
 * Filtro del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroLiquidacionOtroIngreso implements Serializable {

    private Long loiPk;
    private Long loiSedePk;
    private Integer anioFiscal;
    private Long departamentoPk;
    private EnumEstadoLiquidacion estado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroLiquidacionOtroIngreso() {
    }

    public Long getLoiPk() {
        return loiPk;
    }

    public void setLoiPk(Long loiPk) {
        this.loiPk = loiPk;
    }

    public Long getLoiSedePk() {
        return loiSedePk;
    }

    public void setLoiSedePk(Long loiSedePk) {
        this.loiSedePk = loiSedePk;
    }

    public EnumEstadoLiquidacion getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoLiquidacion estado) {
        this.estado = estado;
    }


    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
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

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    

}
