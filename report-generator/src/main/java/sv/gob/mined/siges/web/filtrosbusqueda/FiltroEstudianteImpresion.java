package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;

/**
 *
 * @author usuario
 */
public class FiltroEstudianteImpresion implements Serializable {
    
    private Long eimSolicitudImpresionPk;
    private Long eimPk;
    private List<Long> eimEstudiante;
    private Long eimSeccion;
    private Long eimDefinicionTitulo;
    private Boolean eimAnulada;
    private Boolean eimNoAnulada;
    private List<EnumEstadoSolicitudImpresion> eimEstadosSolicitud;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    public FiltroEstudianteImpresion() {
    }

    public Long getEimSolicitudImpresionPk() {
        return eimSolicitudImpresionPk;
    }

    public void setEimSolicitudImpresionPk(Long eimSolicitudImpresionPk) {
        this.eimSolicitudImpresionPk = eimSolicitudImpresionPk;
    }

    public List<Long> getEimEstudiante() {
        return eimEstudiante;
    }

    public void setEimEstudiante(List<Long> eimEstudiante) {
        this.eimEstudiante = eimEstudiante;
    }

    public Long getEimSeccion() {
        return eimSeccion;
    }

    public void setEimSeccion(Long eimSeccion) {
        this.eimSeccion = eimSeccion;
    }

    public Long getEimDefinicionTitulo() {
        return eimDefinicionTitulo;
    }

    public void setEimDefinicionTitulo(Long eimDefinicionTitulo) {
        this.eimDefinicionTitulo = eimDefinicionTitulo;
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

    public Boolean getEimAnulada() {
        return eimAnulada;
    }

    public void setEimAnulada(Boolean eimAnulada) {
        this.eimAnulada = eimAnulada;
    }

    public Boolean getEimNoAnulada() {
        return eimNoAnulada;
    }

    public void setEimNoAnulada(Boolean eimNoAnulada) {
        this.eimNoAnulada = eimNoAnulada;
    }

    public List<EnumEstadoSolicitudImpresion> getEimEstadosSolicitud() {
        return eimEstadosSolicitud;
    }

    public void setEimEstadosSolicitud(List<EnumEstadoSolicitudImpresion> eimEstadosSolicitud) {
        this.eimEstadosSolicitud = eimEstadosSolicitud;
    }

    public Long getEimPk() {
        return eimPk;
    }

    public void setEimPk(Long eimPk) {
        this.eimPk = eimPk;
    }

    
}
