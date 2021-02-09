/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroServicioBasico implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    private Boolean aplicaInmueble;
    private Boolean aplicaEdificio;
    private Boolean aplicaAula;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroServicioBasico() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Boolean getAplicaInmueble() {
        return aplicaInmueble;
    }

    public void setAplicaInmueble(Boolean aplicaInmueble) {
        this.aplicaInmueble = aplicaInmueble;
    }

    public Boolean getAplicaEdificio() {
        return aplicaEdificio;
    }

    public void setAplicaEdificio(Boolean aplicaEdificio) {
        this.aplicaEdificio = aplicaEdificio;
    }

    public Boolean getAplicaAula() {
        return aplicaAula;
    }

    public void setAplicaAula(Boolean aplicaAula) {
        this.aplicaAula = aplicaAula;
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