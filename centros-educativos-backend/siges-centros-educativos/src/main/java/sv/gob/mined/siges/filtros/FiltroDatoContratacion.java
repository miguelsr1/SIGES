package sv.gob.mined.siges.filtros;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class FiltroDatoContratacion implements Serializable {
    
    private Long dcoPk;
    private Long dcoCentroEducativo;
    private String dcoCentroEducativoCodigo;
    private Long dcoCargo;
    private String dcoCargoCodigo;
    private List<String> dcoCargoCodigos;
    private Long dcoJornada;
    private Long dcoTipoContrato;
    private Long dcoPersonalPk;
    private List<Long> dcoPersonalesPk;
    private LocalDate dcoDesde;
    private LocalDate dcoHasta;
    private Long dcoDatoEmpleado;
    private Long dcoDatoContratacion;  
    private Long dcoPersonaPk;  
    private LocalDate dcoFecha;
    private Boolean contratosActivos;  
    private Integer dcoAnio;
    
    private Boolean buscarEnSedeAdscrita;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Boolean incluirJornadasLaborales;
    
    private Long excluirDcoPk;

    public FiltroDatoContratacion() {
    }

    public Long getDcoPk() {
        return dcoPk;
    }

    public void setDcoPk(Long dcoPk) {
        this.dcoPk = dcoPk;
    }

    public Long getDcoCentroEducativo() {
        return dcoCentroEducativo;
    }

    public void setDcoCentroEducativo(Long dcoCentroEducativo) {
        this.dcoCentroEducativo = dcoCentroEducativo;
    }

    public String getDcoCargoCodigo() {
        return dcoCargoCodigo;
    }

    public void setDcoCargoCodigo(String dcoCargoCodigo) {
        this.dcoCargoCodigo = dcoCargoCodigo;
    }

    public Long getDcoCargo() {
        return dcoCargo;
    }

    public void setDcoCargo(Long dcoCargo) {
        this.dcoCargo = dcoCargo;
    }

    public Long getDcoJornada() {
        return dcoJornada;
    }

    public void setDcoJornada(Long dcoJornada) {
        this.dcoJornada = dcoJornada;
    }

    public Long getDcoTipoContrato() {
        return dcoTipoContrato;
    }

    public void setDcoTipoContrato(Long dcoTipoContrato) {
        this.dcoTipoContrato = dcoTipoContrato;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getDcoPersonalPk() {
        return dcoPersonalPk;
    }

    public void setDcoPersonalPk(Long dcoPersonalPk) {
        this.dcoPersonalPk = dcoPersonalPk;
    }

    public LocalDate getDcoDesde() {
        return dcoDesde;
    }

    public void setDcoDesde(LocalDate dcoDesde) {
        this.dcoDesde = dcoDesde;
    }

    public LocalDate getDcoHasta() {
        return dcoHasta;
    }

    public void setDcoHasta(LocalDate dcoHasta) {
        this.dcoHasta = dcoHasta;
    }

    public Long getDcoDatoEmpleado() {
        return dcoDatoEmpleado;
    }

    public void setDcoDatoEmpleado(Long dcoDatoEmpleado) {
        this.dcoDatoEmpleado = dcoDatoEmpleado;
    }

    public Long getDcoDatoContratacion() {
        return dcoDatoContratacion;
    }

    public void setDcoDatoContratacion(Long dcoDatoContratacion) {
        this.dcoDatoContratacion = dcoDatoContratacion;
    }

    public Long getExcluirDcoPk() {
        return excluirDcoPk;
    }

    public void setExcluirDcoPk(Long excluirDcoPk) {
        this.excluirDcoPk = excluirDcoPk;
    }

    public List<Long> getDcoPersonalesPk() {
        return dcoPersonalesPk;
    }

    public void setDcoPersonalesPk(List<Long> dcoPersonalesPk) {
        this.dcoPersonalesPk = dcoPersonalesPk;
    }

    public Boolean getIncluirJornadasLaborales() {
        return incluirJornadasLaborales;
    }

    public void setIncluirJornadasLaborales(Boolean incluirJornadasLaborales) {
        this.incluirJornadasLaborales = incluirJornadasLaborales;
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

    public Long getDcoPersonaPk() {
        return dcoPersonaPk;
    }

    public void setDcoPersonaPk(Long dcoPersonaPk) {
        this.dcoPersonaPk = dcoPersonaPk;
    }

    public LocalDate getDcoFecha() {
        return dcoFecha;
    }

    public void setDcoFecha(LocalDate dcoFecha) {
        this.dcoFecha = dcoFecha;
    }

    public Boolean getBuscarEnSedeAdscrita() {
        return buscarEnSedeAdscrita;
    }

    public void setBuscarEnSedeAdscrita(Boolean buscarEnSedeAdscrita) {
        this.buscarEnSedeAdscrita = buscarEnSedeAdscrita;
    }

    public String getDcoCentroEducativoCodigo() {
        return dcoCentroEducativoCodigo;
    }

    public void setDcoCentroEducativoCodigo(String dcoCentroEducativoCodigo) {
        this.dcoCentroEducativoCodigo = dcoCentroEducativoCodigo;
    }

    public List<String> getDcoCargoCodigos() {
        return dcoCargoCodigos;
    }

    public void setDcoCargoCodigos(List<String> dcoCargoCodigos) {
        this.dcoCargoCodigos = dcoCargoCodigos;
    }

    public Integer getDcoAnio() {
        return dcoAnio;
    }

    public void setDcoAnio(Integer dcoAnio) {
        this.dcoAnio = dcoAnio;
    }

    
    public Boolean getContratosActivos() {
        return contratosActivos;
    }

    public void setContratosActivos(Boolean contratosActivos) {
        this.contratosActivos = contratosActivos;
    }

  
}
