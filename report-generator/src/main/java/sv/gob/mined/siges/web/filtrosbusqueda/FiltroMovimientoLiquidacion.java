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
public class FiltroMovimientoLiquidacion implements Serializable {

    private Long mlqPk;
    private Long mlqLiquidacionPk;
    private Long mlqMovimientoPk;
    private EnumMovimientosTipo movimientoTipo;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroMovimientoLiquidacion() {
    }

    public Long getMlqPk() {
        return mlqPk;
    }

    public void setMlqPk(Long mlqPk) {
        this.mlqPk = mlqPk;
    }

    public Long getMlqLiquidacionPk() {
        return mlqLiquidacionPk;
    }

    public void setMlqLiquidacionPk(Long mlqLiquidacionPk) {
        this.mlqLiquidacionPk = mlqLiquidacionPk;
    }

    public Long getMlqMovimientoPk() {
        return mlqMovimientoPk;
    }

    public void setMlqMovimientoPk(Long mlqMovimientoPk) {
        this.mlqMovimientoPk = mlqMovimientoPk;
    }

    public EnumMovimientosTipo getMovimientoTipo() {
        return movimientoTipo;
    }

    public void setMovimientoTipo(EnumMovimientosTipo movimientoTipo) {
        this.movimientoTipo = movimientoTipo;
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


    

}
