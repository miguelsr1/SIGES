/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;



/**
 * Filtro del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroMovimientoLiquidacionOtro implements Serializable {

    private Long mloPk;
    private Long mloLiquidacionOtroPk;
    private Long mloMovimientoPk;
    private EnumMovimientosTipo movimientoTipo;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroMovimientoLiquidacionOtro() {
    }

    public Long getMloPk() {
        return mloPk;
    }

    public void setMloPk(Long mloPk) {
        this.mloPk = mloPk;
    }

    public Long getMloLiquidacionOtroPk() {
        return mloLiquidacionOtroPk;
    }

    public void setMloLiquidacionOtroPk(Long mloLiquidacionOtroPk) {
        this.mloLiquidacionOtroPk = mloLiquidacionOtroPk;
    }

    public Long getMloMovimientoPk() {
        return mloMovimientoPk;
    }

    public void setMloMovimientoPk(Long mloMovimientoPk) {
        this.mloMovimientoPk = mloMovimientoPk;
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

    public EnumMovimientosTipo getMovimientoTipo() {
        return movimientoTipo;
    }

    public void setMovimientoTipo(EnumMovimientosTipo movimientoTipo) {
        this.movimientoTipo = movimientoTipo;
    }

    
    

}
