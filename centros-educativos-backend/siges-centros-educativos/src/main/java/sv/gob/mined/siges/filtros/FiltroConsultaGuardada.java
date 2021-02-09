/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumConsultaGuardada;


public class FiltroConsultaGuardada implements Serializable {
    
    private String usuCodigo;
    
    private EnumConsultaGuardada cgrFiltro;
    
    private String cgrNombre;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroConsultaGuardada() {
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

    public String getUsuCodigo() {
        return usuCodigo;
    }

    public void setUsuCodigo(String usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public EnumConsultaGuardada getCgrFiltro() {
        return cgrFiltro;
    }

    public void setCgrFiltro(EnumConsultaGuardada cgrFiltro) {
        this.cgrFiltro = cgrFiltro;
    }

    public String getCgrNombre() {
        return cgrNombre;
    }

    public void setCgrNombre(String cgrNombre) {
        this.cgrNombre = cgrNombre;
    }

   

    
}
