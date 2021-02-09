package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAplicacionPlaza;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroAplicacionPlaza implements Serializable {
    
    private EnumEstadoAplicacionPlaza aplEstado;
    private Long aplPlaza;
    private Long aplPersonal;
    private String aplCodigoUsuario;
    private LocalDateTime aplFechaAplico;
    private LocalDateTime aplFechaAplicoInicio;
    private LocalDateTime aplFechaAplicoFin;
    private Boolean inicializarSedesPersonal;
    private Boolean inicializarEspecialidades;
    private Boolean inicializarDiscapacidades;
    private Boolean inicializarMotivosSeleccion;
    private String dui;
    private String nip;
    private Double epaCum;
    private LocalDate epaFechaGraduacionDesde;  
    private LocalDate epaFechaGraduacionHasta;  
    private Long departamento;
    private Long municipio;
    private List<Long> especialidades;
    private List<Long> discapacidades;
    private Long calidadAplicante;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    
    public FiltroAplicacionPlaza(){
        
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public Boolean getInicializarSedesPersonal() {
        return inicializarSedesPersonal;
    }

    public void setInicializarSedesPersonal(Boolean inicializarSedesPersonal) {
        this.inicializarSedesPersonal = inicializarSedesPersonal;
    }

    public Boolean getInicializarEspecialidades() {
        return inicializarEspecialidades;
    }

    public void setInicializarEspecialidades(Boolean inicializarEspecialidades) {
        this.inicializarEspecialidades = inicializarEspecialidades;
    }

    public EnumEstadoAplicacionPlaza getAplEstado() {
        return aplEstado;
    }

    public void setAplEstado(EnumEstadoAplicacionPlaza aplEstado) {
        this.aplEstado = aplEstado;
    }

    public Long getAplPlaza() {
        return aplPlaza;
    }

    public void setAplPlaza(Long aplPlaza) {
        this.aplPlaza = aplPlaza;
    }

    public Long getAplPersonal() {
        return aplPersonal;
    }

    public void setAplPersonal(Long aplPersonal) {
        this.aplPersonal = aplPersonal;
    }

    public String getAplCodigoUsuario() {
        return aplCodigoUsuario;
    }

    public void setAplCodigoUsuario(String aplCodigoUsuario) {
        this.aplCodigoUsuario = aplCodigoUsuario;
    }

    public LocalDateTime getAplFechaAplico() {
        return aplFechaAplico;
    }

    public void setAplFechaAplico(LocalDateTime aplFechaAplico) {
        this.aplFechaAplico = aplFechaAplico;
    }

    public LocalDateTime getAplFechaAplicoInicio() {
        return aplFechaAplicoInicio;
    }

    public void setAplFechaAplicoInicio(LocalDateTime aplFechaAplicoInicio) {
        this.aplFechaAplicoInicio = aplFechaAplicoInicio;
    }

    public LocalDateTime getAplFechaAplicoFin() {
        return aplFechaAplicoFin;
    }

    public void setAplFechaAplicoFin(LocalDateTime aplFechaAplicoFin) {
        this.aplFechaAplicoFin = aplFechaAplicoFin;
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

    public LocalDate getEpaFechaGraduacionDesde() {
        return epaFechaGraduacionDesde;
    }

    public void setEpaFechaGraduacionDesde(LocalDate epaFechaGraduacionDesde) {
        this.epaFechaGraduacionDesde = epaFechaGraduacionDesde;
    }

    public LocalDate getEpaFechaGraduacionHasta() {
        return epaFechaGraduacionHasta;
    }

    public void setEpaFechaGraduacionHasta(LocalDate epaFechaGraduacionHasta) {
        this.epaFechaGraduacionHasta = epaFechaGraduacionHasta;
    }
       
    
    
    //</editor-fold>

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Double getEpaCum() {
        return epaCum;
    }

    public void setEpaCum(Double epaCum) {
        this.epaCum = epaCum;
    }

  

    public Long getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Long departamento) {
        this.departamento = departamento;
    }

    public Long getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Long municipio) {
        this.municipio = municipio;
    }

    public List<Long> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Long> especialidades) {
        this.especialidades = especialidades;
    }

    public List<Long> getDiscapacidades() {
        return discapacidades;
    }

    public void setDiscapacidades(List<Long> discapacidades) {
        this.discapacidades = discapacidades;
    }

    public Boolean getInicializarDiscapacidades() {
        return inicializarDiscapacidades;
    }

    public void setInicializarDiscapacidades(Boolean inicializarDiscapacidades) {
        this.inicializarDiscapacidades = inicializarDiscapacidades;
    }

    public Boolean getInicializarMotivosSeleccion() {
        return inicializarMotivosSeleccion;
    }

    public void setInicializarMotivosSeleccion(Boolean inicializarMotivosSeleccion) {
        this.inicializarMotivosSeleccion = inicializarMotivosSeleccion;
    }

    public Long getCalidadAplicante() {
        return calidadAplicante;
    }

    public void setCalidadAplicante(Long calidadAplicante) {
        this.calidadAplicante = calidadAplicante;
    }

}
