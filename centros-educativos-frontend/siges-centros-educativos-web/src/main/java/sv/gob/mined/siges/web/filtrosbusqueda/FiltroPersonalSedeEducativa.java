/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumModeloContrato;
import sv.gob.mined.siges.web.enumerados.TipoPersonalSedeEducativa;

public class FiltroPersonalSedeEducativa implements Serializable {

    private Long perPk;
    private String perPrimerNombre;
    private String perSegundoNombre;
    private String perTercerNombre;
    private String perPrimerApellido;
    private String perSegundoApellido;
    private String perTercerApellido;
    private String perNombreCompleto;
    private LocalDate perFechaNacimiento;
    private Long perDepartamento;
    private Long perMunicipio;
    private Long perCentroEducativo;
    private String perNip;
    private String perDui;
    private String perCodigoEmpleado;
    private TipoPersonalSedeEducativa perTipoEmpleado;
    private Long perNie;
    private Boolean personalActivo;
    private Boolean docenteOActividadDocente;
    private List<EnumModeloContrato> modeloContrato;
    private Boolean tieneContratos;
    private List<Long> especialidades;
    private List<TipoPersonalSedeEducativa> perTiposEmpleado;
    
    private Long estadoDatoContratacion;
    
    private Boolean buscarEnSedeAdscrita;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Boolean incluirSedes;

    public FiltroPersonalSedeEducativa() {
    //    this.first = 0L;
        this.incluirSedes = Boolean.FALSE;
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

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerPrimerNombre() {
        return perPrimerNombre;
    }

    public void setPerPrimerNombre(String perPrimerNombre) {
        this.perPrimerNombre = perPrimerNombre;
    }

    public String getPerSegundoNombre() {
        return perSegundoNombre;
    }

    public void setPerSegundoNombre(String perSegundoNombre) {
        this.perSegundoNombre = perSegundoNombre;
    }

    public String getPerTercerNombre() {
        return perTercerNombre;
    }

    public void setPerTercerNombre(String perTercerNombre) {
        this.perTercerNombre = perTercerNombre;
    }

    public String getPerPrimerApellido() {
        return perPrimerApellido;
    }

    public void setPerPrimerApellido(String perPrimerApellido) {
        this.perPrimerApellido = perPrimerApellido;
    }

    public String getPerSegundoApellido() {
        return perSegundoApellido;
    }

    public void setPerSegundoApellido(String perSegundoApellido) {
        this.perSegundoApellido = perSegundoApellido;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public LocalDate getPerFechaNacimiento() {
        return perFechaNacimiento;
    }

    public void setPerFechaNacimiento(LocalDate perFechaNacimiento) {
        this.perFechaNacimiento = perFechaNacimiento;
    }

    public Long getPerDepartamento() {
        return perDepartamento;
    }

    public void setPerDepartamento(Long perDepartamento) {
        this.perDepartamento = perDepartamento;
    }

    public Long getPerMunicipio() {
        return perMunicipio;
    }

    public void setPerMunicipio(Long perMunicipio) {
        this.perMunicipio = perMunicipio;
    }

    public Long getPerCentroEducativo() {
        return perCentroEducativo;
    }

    public void setPerCentroEducativo(Long perCentroEducativo) {
        this.perCentroEducativo = perCentroEducativo;
    }

    public String getPerNip() {
        return perNip;
    }

    public void setPerNip(String perNip) {
        this.perNip = perNip;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        this.perDui = perDui;
    }

    public String getPerCodigoEmpleado() {
        return perCodigoEmpleado;
    }

    public void setPerCodigoEmpleado(String perCodigoEmpleado) {
        this.perCodigoEmpleado = perCodigoEmpleado;
    }

    public TipoPersonalSedeEducativa getPerTipoEmpleado() {
        return perTipoEmpleado;
    }

    public void setPerTipoEmpleado(TipoPersonalSedeEducativa perTipoEmpleado) {
        this.perTipoEmpleado = perTipoEmpleado;
    }

    public Long getPerNie() {
        return perNie;
    }

    public void setPerNie(Long perNie) {
        this.perNie = perNie;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getIncluirSedes() {
        return incluirSedes;
    }

    public void setIncluirSedes(Boolean incluirSedes) {
        this.incluirSedes = incluirSedes;
    }

    public String getPerNombreCompleto() {
        return perNombreCompleto;
    }

    public void setPerNombreCompleto(String perNombreCompleto) {
        this.perNombreCompleto = perNombreCompleto;
    }

    public Boolean getPersonalActivo() {
        return personalActivo;
    }

    public void setPersonalActivo(Boolean personalActivo) {
        this.personalActivo = personalActivo;
    }

    public Boolean getDocenteOActividadDocente() {
        return docenteOActividadDocente;
    }

    public void setDocenteOActividadDocente(Boolean docenteOActividadDocente) {
        this.docenteOActividadDocente = docenteOActividadDocente;
    }

    public List<EnumModeloContrato> getModeloContrato() {
        return modeloContrato;
    }

    public void setModeloContrato(List<EnumModeloContrato> modeloContrato) {
        this.modeloContrato = modeloContrato;
    }

    public Boolean getTieneContratos() {
        return tieneContratos;
    }

    public void setTieneContratos(Boolean tieneContratos) {
        this.tieneContratos = tieneContratos;
    }

    public List<Long> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Long> especialidades) {
        this.especialidades = especialidades;
    }

    public List<TipoPersonalSedeEducativa> getPerTiposEmpleado() {
        return perTiposEmpleado;
    }

    public void setPerTiposEmpleado(List<TipoPersonalSedeEducativa> perTiposEmpleado) {
        this.perTiposEmpleado = perTiposEmpleado;
    }

    public Boolean getBuscarEnSedeAdscrita() {
        return buscarEnSedeAdscrita;
    }

    public void setBuscarEnSedeAdscrita(Boolean buscarEnSedeAdscrita) {
        this.buscarEnSedeAdscrita = buscarEnSedeAdscrita;
    }

    public Long getEstadoDatoContratacion() {
        return estadoDatoContratacion;
    }

    public void setEstadoDatoContratacion(Long estadoDatoContratacion) {
        this.estadoDatoContratacion = estadoDatoContratacion;
    }
    
    
    
    
    
}
