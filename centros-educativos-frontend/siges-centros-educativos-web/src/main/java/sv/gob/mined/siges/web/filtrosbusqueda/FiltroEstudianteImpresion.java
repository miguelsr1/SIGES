/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;

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
    private List<Long> eimSolicitudesImpresion;
    private EnumTipoSolicitudImpresion simTipoSolicitudImpresion;
    private Long eimNie;
    private Long eimPlanEstudioFk;
    private Long eimGradoFk;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    public FiltroEstudianteImpresion() {
    }

    public List<Long> getEimSolicitudesImpresion() {
        return eimSolicitudesImpresion;
    }

    public void setEimSolicitudesImpresion(List<Long> eimSolicitudesImpresion) {
        this.eimSolicitudesImpresion = eimSolicitudesImpresion;
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

    public EnumTipoSolicitudImpresion getSimTipoSolicitudImpresion() {
        return simTipoSolicitudImpresion;
    }

    public void setSimTipoSolicitudImpresion(EnumTipoSolicitudImpresion simTipoSolicitudImpresion) {
        this.simTipoSolicitudImpresion = simTipoSolicitudImpresion;
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

    public Long getEimSolicitudImpresionPk() {
        return eimSolicitudImpresionPk;
    }

    public void setEimSolicitudImpresionPk(Long eimSolicitudImpresionPk) {
        this.eimSolicitudImpresionPk = eimSolicitudImpresionPk;
    }

    public Long getEimPk() {
        return eimPk;
    }

    public void setEimPk(Long eimPk) {
        this.eimPk = eimPk;
    }

    public Long getEimNie() {
        return eimNie;
    }

    public void setEimNie(Long eimNie) {
        this.eimNie = eimNie;
    }

    public Long getEimPlanEstudioFk() {
        return eimPlanEstudioFk;
    }

    public void setEimPlanEstudioFk(Long eimPlanEstudioFk) {
        this.eimPlanEstudioFk = eimPlanEstudioFk;
    }

    public Long getEimGradoFk() {
        return eimGradoFk;
    }

    public void setEimGradoFk(Long eimGradoFk) {
        this.eimGradoFk = eimGradoFk;
    }
    
    
}
