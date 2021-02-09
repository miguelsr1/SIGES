package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FiltroDatoContratacion implements Serializable {

    private Long dcoPk;
    private Long dcoCentroEducativo;
    private Long dcoCargo;
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

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Boolean incluirJornadasLaborales;

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

    public Boolean getIncluirJornadasLaborales() {
        return incluirJornadasLaborales;
    }

    public void setIncluirJornadasLaborales(Boolean incluirJornadasLaborales) {
        this.incluirJornadasLaborales = incluirJornadasLaborales;
    }

    public List<Long> getDcoPersonalesPk() {
        return dcoPersonalesPk;
    }

    public void setDcoPersonalesPk(List<Long> dcoPersonalesPk) {
        this.dcoPersonalesPk = dcoPersonalesPk;
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

    public Boolean getContratosActivos() {
        return contratosActivos;
    }

    public void setContratosActivos(Boolean contratosActivos) {
        this.contratosActivos = contratosActivos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.dcoPk);
        hash = 37 * hash + Objects.hashCode(this.dcoCentroEducativo);
        hash = 37 * hash + Objects.hashCode(this.dcoCargo);
        hash = 37 * hash + Objects.hashCode(this.dcoJornada);
        hash = 37 * hash + Objects.hashCode(this.dcoTipoContrato);
        hash = 37 * hash + Objects.hashCode(this.dcoPersonalPk);
        hash = 37 * hash + Objects.hashCode(this.dcoPersonalesPk);
        hash = 37 * hash + Objects.hashCode(this.dcoDesde);
        hash = 37 * hash + Objects.hashCode(this.dcoHasta);
        hash = 37 * hash + Objects.hashCode(this.dcoDatoEmpleado);
        hash = 37 * hash + Objects.hashCode(this.dcoDatoContratacion);
        hash = 37 * hash + Objects.hashCode(this.dcoPersonaPk);
        hash = 37 * hash + Objects.hashCode(this.dcoFecha);
        hash = 37 * hash + Objects.hashCode(this.contratosActivos);
        hash = 37 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 37 * hash + Objects.hashCode(this.first);
        hash = 37 * hash + Objects.hashCode(this.maxResults);
        hash = 37 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 37 * hash + Arrays.hashCode(this.ascending);
        hash = 37 * hash + Objects.hashCode(this.incluirJornadasLaborales);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroDatoContratacion other = (FiltroDatoContratacion) obj;
        if (!Objects.equals(this.dcoPk, other.dcoPk)) {
            return false;
        }
        if (!Objects.equals(this.dcoCentroEducativo, other.dcoCentroEducativo)) {
            return false;
        }
        if (!Objects.equals(this.dcoCargo, other.dcoCargo)) {
            return false;
        }
        if (!Objects.equals(this.dcoJornada, other.dcoJornada)) {
            return false;
        }
        if (!Objects.equals(this.dcoTipoContrato, other.dcoTipoContrato)) {
            return false;
        }
        if (!Objects.equals(this.dcoPersonalPk, other.dcoPersonalPk)) {
            return false;
        }
        if (!Objects.equals(this.dcoPersonalesPk, other.dcoPersonalesPk)) {
            return false;
        }
        if (!Objects.equals(this.dcoDesde, other.dcoDesde)) {
            return false;
        }
        if (!Objects.equals(this.dcoHasta, other.dcoHasta)) {
            return false;
        }
        if (!Objects.equals(this.dcoDatoEmpleado, other.dcoDatoEmpleado)) {
            return false;
        }
        if (!Objects.equals(this.dcoDatoContratacion, other.dcoDatoContratacion)) {
            return false;
        }
        if (!Objects.equals(this.dcoPersonaPk, other.dcoPersonaPk)) {
            return false;
        }
        if (!Objects.equals(this.dcoFecha, other.dcoFecha)) {
            return false;
        }
        if (!Objects.equals(this.contratosActivos, other.contratosActivos)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Objects.equals(this.incluirJornadasLaborales, other.incluirJornadasLaborales)) {
            return false;
        }
        return true;
    }
    
    

    
    
    
    
   

}
