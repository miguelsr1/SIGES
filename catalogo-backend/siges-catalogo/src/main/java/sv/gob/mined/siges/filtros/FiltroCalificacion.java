/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroCalificacion implements Serializable {
    

    private Long ecaCalPk;
    
    private String calCodigo;
    private String calCodigoExacto;

    private String calValor;

    private Integer calOrden;

    private Boolean calHabilitado;

    private String calEscala;
    private String calEscalaExacto;
    private Long calEscalaCalificacionPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroCalificacion() {
    }

    public String getCalCodigo() {
        return calCodigo;
    }

    public void setCalCodigo(String calCodigo) {
        this.calCodigo = calCodigo;
    }

    public String getCalCodigoExacto() {
        return calCodigoExacto;
    }

    public void setCalCodigoExacto(String calCodigoExacto) {
        this.calCodigoExacto = calCodigoExacto;
    }

    public String getCalValor() {
        return calValor;
    }

    public void setCalValor(String calValor) {
        this.calValor = calValor;
    }

    public Integer getCalOrden() {
        return calOrden;
    }

    public void setCalOrden(Integer calOrden) {
        this.calOrden = calOrden;
    }

    public Boolean getCalHabilitado() {
        return calHabilitado;
    }

    public void setCalHabilitado(Boolean calHabilitado) {
        this.calHabilitado = calHabilitado;
    }

    public String getCalEscala() {
        return calEscala;
    }

    public void setCalEscala(String calEscala) {
        this.calEscala = calEscala;
    }

    public String getCalEscalaExacto() {
        return calEscalaExacto;
    }

    public void setCalEscalaExacto(String calEscalaExacto) {
        this.calEscalaExacto = calEscalaExacto;
    }

    public Long getEcaCalPk() {
        return ecaCalPk;
    }

    public void setEcaCalPk(Long ecaCalPk) {
        this.ecaCalPk = ecaCalPk;
    }

    public Long getCalEscalaCalificacionPk() {
        return calEscalaCalificacionPk;
    }

    public void setCalEscalaCalificacionPk(Long calEscalaCalificacionPk) {
        this.calEscalaCalificacionPk = calEscalaCalificacionPk;
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
    
    

    
}
