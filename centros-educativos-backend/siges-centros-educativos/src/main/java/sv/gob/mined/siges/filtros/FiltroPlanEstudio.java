/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroPlanEstudio implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private Long gradoPk;
    private Boolean vigente;
    private Long servicioEducativoFk;
    private Long pesPk;
    private Long nivel;
    private Long ciclo;
    private Long modalidadEducativa;
    private Long modalidadAtencion;
    private Long opcion;
    private Long programaEducativo;
    private Long subModalidadAtencion;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    
    public FiltroPlanEstudio() {
        this.seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getGradoPk() {
        return gradoPk;
    }

    public void setGradoPk(Long gradoPk) {
        this.gradoPk = gradoPk;
    }

    public Long getServicioEducativoFk() {
        return servicioEducativoFk;
    }

    public void setServicioEducativoFk(Long servicioEducativoFk) {
        this.servicioEducativoFk = servicioEducativoFk;
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public Long getCiclo() {
        return ciclo;
    }

    public void setCiclo(Long ciclo) {
        this.ciclo = ciclo;
    }

    public Long getModalidadEducativa() {
        return modalidadEducativa;
    }

    public void setModalidadEducativa(Long modalidadEducativa) {
        this.modalidadEducativa = modalidadEducativa;
    }

    public Long getModalidadAtencion() {
        return modalidadAtencion;
    }

    public void setModalidadAtencion(Long modalidadAtencion) {
        this.modalidadAtencion = modalidadAtencion;
    }

    public Long getOpcion() {
        return opcion;
    }

    public void setOpcion(Long opcion) {
        this.opcion = opcion;
    }

    public Long getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(Long programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public Long getSubModalidadAtencion() {
        return subModalidadAtencion;
    }

    public void setSubModalidadAtencion(Long subModalidadAtencion) {
        this.subModalidadAtencion = subModalidadAtencion;
    }
    
}
