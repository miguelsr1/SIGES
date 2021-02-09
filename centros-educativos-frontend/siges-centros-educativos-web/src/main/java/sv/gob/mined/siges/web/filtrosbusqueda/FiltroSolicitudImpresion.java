/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;


/**
 *
 * @author usuario
 */
public class FiltroSolicitudImpresion implements Serializable {
    
    private Long simSeccion;
    private Long simUsuario;
    private EnumEstadoSolicitudImpresion simEstado;
    private LocalDate simFechaSolicitud;
    private Long simSede;
    private Long simDepartamento;
    private Long simMunicipio;
    private LocalDate simFechaDesde;
    private LocalDate simFechaHasta;
    private Long simDefinicionTitulo;
    private Long simPk;
    private Boolean inicializarEstudianteImp;
    private Long estudiantesPk;
    private Boolean seNecesitaDistinct;
    private EnumTipoSolicitudImpresion simTipoSolicitudImpresion;
    private Boolean incluirTitulos;
    private List<EnumEstadoSolicitudImpresion> simEstados;
    private String formula;        
    private Boolean simTituloEntregadoDepartamental;    
    private Boolean simTituloEntregadoCentroEducativo;  
    private List<EnumTipoSolicitudImpresion> simTiposSolicitudImpresion;
    private Boolean simCantidadEstudiantes;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    

    
    private String[] incluirCampos;
    
    public FiltroSolicitudImpresion() {
        seNecesitaDistinct=Boolean.FALSE;
    }

    public Long getSimSeccion() {
        return simSeccion;
    }

    public void setSimSeccion(Long simSeccion) {
        this.simSeccion = simSeccion;
    }

    public Boolean getSimCantidadEstudiantes() {
        return simCantidadEstudiantes;
    }

    public void setSimCantidadEstudiantes(Boolean simCantidadEstudiantes) {
        this.simCantidadEstudiantes = simCantidadEstudiantes;
    }

    public Long getSimUsuario() {
        return simUsuario;
    }

    public void setSimUsuario(Long simUsuario) {
        this.simUsuario = simUsuario;
    }

     public EnumEstadoSolicitudImpresion getSimEstado() {
        return simEstado;
    }

    public void setSimEstado(EnumEstadoSolicitudImpresion simEstado) {
        this.simEstado = simEstado;
    }

    public LocalDate getSimFechaSolicitud() {
        return simFechaSolicitud;
    }

    public void setSimFechaSolicitud(LocalDate simFechaSolicitud) {
        this.simFechaSolicitud = simFechaSolicitud;
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

    public Long getSimSede() {
        return simSede;
    }

    public void setSimSede(Long simSede) {
        this.simSede = simSede;
    }

    public Long getSimDepartamento() {
        return simDepartamento;
    }

    public void setSimDepartamento(Long simDepartamento) {
        this.simDepartamento = simDepartamento;
    }

    public Long getSimMunicipio() {
        return simMunicipio;
    }

    public void setSimMunicipio(Long simMunicipio) {
        this.simMunicipio = simMunicipio;
    }

    public LocalDate getSimFechaDesde() {
        return simFechaDesde;
    }

    public void setSimFechaDesde(LocalDate simFechaDesde) {
        this.simFechaDesde = simFechaDesde;
    }

    public LocalDate getSimFechaHasta() {
        return simFechaHasta;
    }

    public void setSimFechaHasta(LocalDate simFechaHasta) {
        this.simFechaHasta = simFechaHasta;
    }

    public Long getSimDefinicionTitulo() {
        return simDefinicionTitulo;
    }

    public void setSimDefinicionTitulo(Long simDefinicionTitulo) {
        this.simDefinicionTitulo = simDefinicionTitulo;
    }

    public Long getSimPk() {
        return simPk;
    }

    public void setSimPk(Long simPk) {
        this.simPk = simPk;
    }

    public Boolean getInicializarEstudianteImp() {
        return inicializarEstudianteImp;
    }

    public void setInicializarEstudianteImp(Boolean inicializarEstudianteImp) {
        this.inicializarEstudianteImp = inicializarEstudianteImp;
    }

    public Long getEstudiantesPk() {
        return estudiantesPk;
    }

    public void setEstudiantesPk(Long estudiantesPk) {
        this.estudiantesPk = estudiantesPk;
    }

  
    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public EnumTipoSolicitudImpresion getSimTipoSolicitudImpresion() {
        return simTipoSolicitudImpresion;
    }

    public void setSimTipoSolicitudImpresion(EnumTipoSolicitudImpresion simTipoSolicitudImpresion) {
        this.simTipoSolicitudImpresion = simTipoSolicitudImpresion;
    }

    public Boolean getIncluirTitulos() {
        return incluirTitulos;
    }

    public void setIncluirTitulos(Boolean incluirTitulos) {
        this.incluirTitulos = incluirTitulos;
    }

    public List<EnumEstadoSolicitudImpresion> getSimEstados() {
        return simEstados;
    }

    public void setSimEstados(List<EnumEstadoSolicitudImpresion> simEstados) {
        this.simEstados = simEstados;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Boolean getSimTituloEntregadoDepartamental() {
        return simTituloEntregadoDepartamental;
    }

    public void setSimTituloEntregadoDepartamental(Boolean simTituloEntregadoDepartamental) {
        this.simTituloEntregadoDepartamental = simTituloEntregadoDepartamental;
    }

    public Boolean getSimTituloEntregadoCentroEducativo() {
        return simTituloEntregadoCentroEducativo;
    }

    public void setSimTituloEntregadoCentroEducativo(Boolean simTituloEntregadoCentroEducativo) {
        this.simTituloEntregadoCentroEducativo = simTituloEntregadoCentroEducativo;
    }

    public List<EnumTipoSolicitudImpresion> getSimTiposSolicitudImpresion() {
        return simTiposSolicitudImpresion;
    }

    public void setSimTiposSolicitudImpresion(List<EnumTipoSolicitudImpresion> simTiposSolicitudImpresion) {
        this.simTiposSolicitudImpresion = simTiposSolicitudImpresion;
    }
    
    
}
