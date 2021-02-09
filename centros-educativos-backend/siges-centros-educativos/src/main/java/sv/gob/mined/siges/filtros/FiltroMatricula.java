/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgRangoEdad;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.EnumPromocionGradoMatricula;

public class FiltroMatricula extends FiltroSeccion implements Serializable {
    
    private Long matEstudiantePk;
    private List<Long> matSeccionesPk;
    private List<Long> matEstudiantesPk;
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
    private Boolean gradosRequierenValidacionAcademica;
    private Boolean incluirResponsableOContactoEmergencia;
    private EnumPromocionGradoMatricula matPromocionGrado;
    private List<Long> matListPk;
    private Boolean matAnulada;
    private Long matNie;
    private Boolean matExcluirNieNull;
    private Long matPk;
    private Boolean matCerradaCierreAnio;
    private Boolean matCerradaCierreAnioOAbierta;
    
    private Long matMunicipioFk;
    private Long matDepartamentoFk;
    private Long matSexoFk;
    private Boolean soloModalidadesFlexibles;
    private Long matEstadoCivil;
    private Long matSubmodalidad;
    
    private Boolean matUltima;
    
    private LocalDate matFechaDesdeSup;
    private LocalDate matFechaHastaSup;
    
    private List<SgRangoEdad> matRangosEdad;
        
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroMatricula() {         
        super();
        securityOperation = ConstantesOperaciones.BUSCAR_MATRICULA;
    }

    public Boolean getMatExcluirNieNull() {
        return matExcluirNieNull;
    }

    public void setMatExcluirNieNull(Boolean matExcluirNieNull) {
        this.matExcluirNieNull = matExcluirNieNull;
    }

    public Boolean getMatCerradaCierreAnio() {
        return matCerradaCierreAnio;
    }

    public void setMatCerradaCierreAnio(Boolean matCerradaCierreAnio) {
        this.matCerradaCierreAnio = matCerradaCierreAnio;
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

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
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

    public Boolean getGradosRequierenValidacionAcademica() {
        return gradosRequierenValidacionAcademica;
    }

    public void setGradosRequierenValidacionAcademica(Boolean gradosRequierenValidacionAcademica) {
        this.gradosRequierenValidacionAcademica = gradosRequierenValidacionAcademica;
    }

    public Boolean getIncluirResponsableOContactoEmergencia() {
        return incluirResponsableOContactoEmergencia;
    }

    public void setIncluirResponsableOContactoEmergencia(Boolean incluirResponsableOContactoEmergencia) {
        this.incluirResponsableOContactoEmergencia = incluirResponsableOContactoEmergencia;
    }

    public EnumPromocionGradoMatricula getMatPromocionGrado() {
        return matPromocionGrado;
    }

    public void setMatPromocionGrado(EnumPromocionGradoMatricula matPromocionGrado) {
        this.matPromocionGrado = matPromocionGrado;
    }

    public List<Long> getMatListPk() {
        return matListPk;
    }

    public void setMatListPk(List<Long> matListPk) {
        this.matListPk = matListPk;
    }

    public Boolean getMatUltima() {
        return matUltima;
    }

    public void setMatUltima(Boolean matUltima) {
        this.matUltima = matUltima;
    }

    public List<Long> getMatEstudiantesPk() {
        return matEstudiantesPk;
    }

    public void setMatEstudiantesPk(List<Long> matEstudiantesPk) {
        this.matEstudiantesPk = matEstudiantesPk;
    }

    public Boolean getMatAnulada() {
        return matAnulada;
    }

    public void setMatAnulada(Boolean matAnulada) {
        this.matAnulada = matAnulada;
    }

    public Long getMatNie() {
        return matNie;
    }

    public void setMatNie(Long matNie) {
        this.matNie = matNie;
    }

    public Boolean getMatCerradaCierreAnioOAbierta() {
        return matCerradaCierreAnioOAbierta;
    }

    public void setMatCerradaCierreAnioOAbierta(Boolean matCerradaCierreAnioOAbierta) {
        this.matCerradaCierreAnioOAbierta = matCerradaCierreAnioOAbierta;
    }

    public Long getMatDepartamentoFk() {
        return matDepartamentoFk;
    }

    public void setMatDepartamentoFk(Long matDepartamentoFk) {
        this.matDepartamentoFk = matDepartamentoFk;
    }

    public Long getMatSexoFk() {
        return matSexoFk;
    }

    public void setMatSexoFk(Long matSexoFk) {
        this.matSexoFk = matSexoFk;
    }

    public Boolean getSoloModalidadesFlexibles() {
        return soloModalidadesFlexibles;
    }

    public void setSoloModalidadesFlexibles(Boolean soloModalidadesFlexibles) {
        this.soloModalidadesFlexibles = soloModalidadesFlexibles;
    }

    public Long getMatEstadoCivil() {
        return matEstadoCivil;
    }

    public void setMatEstadoCivil(Long matEstadoCivil) {
        this.matEstadoCivil = matEstadoCivil;
    }

    public Long getMatSubmodalidad() {
        return matSubmodalidad;
    }

    public void setMatSubmodalidad(Long matSubmodalidad) {
        this.matSubmodalidad = matSubmodalidad;
    }

    public List<SgRangoEdad> getMatRangosEdad() {
        return matRangosEdad;
    }

    public void setMatRangosEdad(List<SgRangoEdad> matRangosEdad) {
        this.matRangosEdad = matRangosEdad;
    }

    public Long getMatMunicipioFk() {
        return matMunicipioFk;
    }

    public void setMatMunicipioFk(Long matMunicipioFk) {
        this.matMunicipioFk = matMunicipioFk;
    }

    
}
