package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroReposicionTitulo implements Serializable {

    private String estudiante;
    private String sede;
    private EnumEstadoSolicitudImpresion simEstado;
    private EnumTipoSolicitudImpresion simTipoImpresion;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Boolean seNecesitaDistinct;

    public FiltroReposicionTitulo() {
        seNecesitaDistinct = Boolean.FALSE;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public EnumEstadoSolicitudImpresion getSimEstado() {
        return simEstado;
    }

    public void setSimEstado(EnumEstadoSolicitudImpresion simEstado) {
        this.simEstado = simEstado;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public EnumTipoSolicitudImpresion getSimTipoImpresion() {
        return simTipoImpresion;
    }

    public void setSimTipoImpresion(EnumTipoSolicitudImpresion simTipoImpresion) {
        this.simTipoImpresion = simTipoImpresion;
    }

}
