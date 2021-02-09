/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroCargoOAE implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String nombreMasculino;
    private Boolean habilitado;
    
    private Long tipoOrganismoAdministrativoPk;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroCargoOAE() {
    }
     

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Long getTipoOrganismoAdministrativoPk() {
        return tipoOrganismoAdministrativoPk;
    }

    public void setTipoOrganismoAdministrativoPk(Long tipoOrganismoAdministrativoPk) {
        this.tipoOrganismoAdministrativoPk = tipoOrganismoAdministrativoPk;
    }   

    public String getNombreMasculino() {
        return nombreMasculino;
    }

    public void setNombreMasculino(String nombreMasculino) {
        this.nombreMasculino = nombreMasculino;
    }

    
    
      
}
