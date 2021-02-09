/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumTipoBeneficio;

public class FiltroProyectoInstitucional implements Serializable {
    
    private String codigo;
    private String nombre;
    private Boolean habilitado;
    private EnumTipoBeneficio tipoBeneficio;
    private Long programaInstitucional;
    private Boolean inicializarBeneficios;
    private Boolean inicializarAsociaciones;
    private Integer anio;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroProyectoInstitucional() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public EnumTipoBeneficio getTipoBeneficio() {
        return tipoBeneficio;
    }

    public void setTipoBeneficio(EnumTipoBeneficio tipoBeneficio) {
        this.tipoBeneficio = tipoBeneficio;
    }

    public Long getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(Long programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
    }

    public Boolean getInicializarBeneficios() {
        return inicializarBeneficios;
    }

    public void setInicializarBeneficios(Boolean inicializarBeneficios) {
        this.inicializarBeneficios = inicializarBeneficios;
    }

    public Boolean getInicializarAsociaciones() {
        return inicializarAsociaciones;
    }

    public void setInicializarAsociaciones(Boolean inicializarAsociaciones) {
        this.inicializarAsociaciones = inicializarAsociaciones;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    

    
    
    
//<editor-fold defaultstate="collapsed" desc="GET & SET estandar">

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
//</editor-fold>
    
}
