/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.centros_educativos.TipoEscalaCalificacion;

public class FiltroEscalaCalificacion implements Serializable {
    

    private String ecaCodigo;
    private String ecaCodigoExacto;

    private Boolean ecaHabilitado;

    private String ecaNombre;
    private String ecaNombreExacto;

    private TipoEscalaCalificacion ecaTipoEscala;

    private String ecaValorMinimo;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroEscalaCalificacion() {
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }

    public String getEcaCodigoExacto() {
        return ecaCodigoExacto;
    }

    public void setEcaCodigoExacto(String ecaCodigoExacto) {
        this.ecaCodigoExacto = ecaCodigoExacto;
    }

    public Boolean getEcaHabilitado() {
        return ecaHabilitado;
    }

    public void setEcaHabilitado(Boolean ecaHabilitado) {
        this.ecaHabilitado = ecaHabilitado;
    }

    public String getEcaNombre() {
        return ecaNombre;
    }

    public void setEcaNombre(String ecaNombre) {
        this.ecaNombre = ecaNombre;
    }

    public String getEcaNombreExacto() {
        return ecaNombreExacto;
    }

    public void setEcaNombreExacto(String ecaNombreExacto) {
        this.ecaNombreExacto = ecaNombreExacto;
    }

    public TipoEscalaCalificacion getEcaTipoEscala() {
        return ecaTipoEscala;
    }

    public void setEcaTipoEscala(TipoEscalaCalificacion ecaTipoEscala) {
        this.ecaTipoEscala = ecaTipoEscala;
    }

    public String getEcaValorMinimo() {
        return ecaValorMinimo;
    }

    public void setEcaValorMinimo(String ecaValorMinimo) {
        this.ecaValorMinimo = ecaValorMinimo;
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
