/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros.centros_educativos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;

public class FiltroMatricula extends FiltroSeccion implements Serializable {
    
    private Long matEstudiantePk;
    private List<Long> matSeccionesPk;
    private EnumMatriculaEstado matEstado;
    private Integer matAnio;
    private Boolean estHabilitado;
    private Boolean matValidadaAcademicamente;
    private Long nie;
    private String codigoUsuarioCreador;
    private Boolean matRetirada;
    private String securityOperation; 
    private List<Long> matExcluirPk;
    private Boolean incluirAnuladas;
    private LocalDate matFechaEntreIngresoHasta;
    
    private LocalDate matFechaDesdeSup;
    private LocalDate matFechaHastaSup;
        
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroMatricula() {         
        super();
       
    }

    public Long getMatEstudiantePk() {
        return matEstudiantePk;
    }

    public void setMatEstudiantePk(Long matEstudiantePk) {
        this.matEstudiantePk = matEstudiantePk;
    }

    public List<Long> getMatSeccionesPk() {
        return matSeccionesPk;
    }

    public void setMatSeccionesPk(List<Long> matSeccionesPk) {
        this.matSeccionesPk = matSeccionesPk;
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

    public EnumMatriculaEstado getMatEstado() {
        return matEstado;
    }

    public void setMatEstado(EnumMatriculaEstado matEstado) {
        this.matEstado = matEstado;
    }

    public Integer getMatAnio() {
        return matAnio;
    }

    public void setMatAnio(Integer matAnio) {
        this.matAnio = matAnio;
    }

    public Boolean getEstHabilitado() {
        return estHabilitado;
    }

    public void setEstHabilitado(Boolean estHabilitado) {
        this.estHabilitado = estHabilitado;
    }

    public Boolean getMatValidadaAcademicamente() {
        return matValidadaAcademicamente;
    }

    public void setMatValidadaAcademicamente(Boolean matValidadaAcademicamente) {
        this.matValidadaAcademicamente = matValidadaAcademicamente;
    }

    public Long getNie() {
        return nie;
    }

    public void setNie(Long nie) {
        this.nie = nie;
    }

    public String getCodigoUsuarioCreador() {
        return codigoUsuarioCreador;
    }

    public void setCodigoUsuarioCreador(String codigoUsuarioCreador) {
        this.codigoUsuarioCreador = codigoUsuarioCreador;
    }

    public Boolean getMatRetirada() {
        return matRetirada;
    }

    public void setMatRetirada(Boolean matRetirada) {
        this.matRetirada = matRetirada;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public List<Long> getMatExcluirPk() {
        return matExcluirPk;
    }

    public void setMatExcluirPk(List<Long> matExcluirPk) {
        this.matExcluirPk = matExcluirPk;
    }

    public Boolean getIncluirAnuladas() {
        return incluirAnuladas;
    }

    public void setIncluirAnuladas(Boolean incluirAnuladas) {
        this.incluirAnuladas = incluirAnuladas;
    }

    public LocalDate getMatFechaDesdeSup() {
        return matFechaDesdeSup;
    }

    public void setMatFechaDesdeSup(LocalDate matFechaDesdeSup) {
        this.matFechaDesdeSup = matFechaDesdeSup;
    }

    public LocalDate getMatFechaHastaSup() {
        return matFechaHastaSup;
    }

    public void setMatFechaHastaSup(LocalDate matFechaHastaSup) {
        this.matFechaHastaSup = matFechaHastaSup;
    }

    public LocalDate getMatFechaEntreIngresoHasta() {
        return matFechaEntreIngresoHasta;
    }

    public void setMatFechaEntreIngresoHasta(LocalDate matFechaEntreIngresoHasta) {
        this.matFechaEntreIngresoHasta = matFechaEntreIngresoHasta;
    }

    
   
}
