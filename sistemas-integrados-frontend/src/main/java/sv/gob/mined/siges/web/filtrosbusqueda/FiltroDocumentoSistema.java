/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumCategoriaDocumento;

public class FiltroDocumentoSistema implements Serializable {
    
    private String nombre;
    private String descripcion;
    private Long tipoDocumento;
    private Long sistemaIntegrado;
    private EnumCategoriaDocumento categoriaDocumento;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroDocumentoSistema() {
    }
     
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Long tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getSistemaIntegrado() {
        return sistemaIntegrado;
    }

    public void setSistemaIntegrado(Long sistemaIntegrado) {
        this.sistemaIntegrado = sistemaIntegrado;
    }

    public EnumCategoriaDocumento getCategoriaDocumento() {
        return categoriaDocumento;
    }

    public void setCategoriaDocumento(EnumCategoriaDocumento categoriaDocumento) {
        this.categoriaDocumento = categoriaDocumento;
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
