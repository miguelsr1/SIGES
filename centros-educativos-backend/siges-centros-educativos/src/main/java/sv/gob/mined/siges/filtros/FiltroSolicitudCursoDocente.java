/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudCurso;

public class FiltroSolicitudCursoDocente implements Serializable {
    
    private Long scdCurso;
    private EnumEstadoSolicitudCurso scdEstado;
    private String scdDui;
    private List<EnumEstadoSolicitudCurso> scdEstados;
    private Long scdPersonal;
    
    private String[] incluirCampos;
    private String securityOperation;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    public FiltroSolicitudCursoDocente() {
    }


    public Long getScdCurso() {
        return scdCurso;
    }

    public void setScdCurso(Long scdCurso) {
        this.scdCurso = scdCurso;
    }

    public EnumEstadoSolicitudCurso getScdEstado() {
        return scdEstado;
    }

    public void setScdEstado(EnumEstadoSolicitudCurso scdEstado) {
        this.scdEstado = scdEstado;
    }

    public String getScdDui() {
        return scdDui;
    }

    public void setScdDui(String scdDui) {
        this.scdDui = scdDui;
    }

    public List<EnumEstadoSolicitudCurso> getScdEstados() {
        return scdEstados;
    }

    public void setScdEstados(List<EnumEstadoSolicitudCurso> scdEstados) {
        this.scdEstados = scdEstados;
    }

    public Long getScdPersonal() {
        return scdPersonal;
    }

    public void setScdPersonal(Long scdPersonal) {
        this.scdPersonal = scdPersonal;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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


    
    //</editor-fold>
}
