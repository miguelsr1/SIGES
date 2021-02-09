/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroEncuestaEstudiante implements Serializable {

    private Long encPk;
    private Long estudiantePk;
    private String perNombreCompleto;
    private String perPrimerNombre;
    private String perSegundoNombre;
    private String perTercerNombre;
    private String perPrimerApellido;
    private String perSegundoApellido;
    private String perTercerApellido;
    private Long nie;
    private Long departamentoPk;
    private Long municipioPk;
    private Long sedePk;
    
    private Boolean inicializarElementosHogar;
    private Boolean inicializarMenores;
  
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroEncuestaEstudiante() {
        super();
        seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getEncPk() {
        return encPk;
    }

    public void setEncPk(Long encPk) {
        this.encPk = encPk;
    }

    public Long getNie() {
        return nie;
    }

    public void setNie(Long nie) {
        this.nie = nie;
    }

    public Long getEstudiantePk() {
        return estudiantePk;
    }

    public void setEstudiantePk(Long estudiantePk) {
        this.estudiantePk = estudiantePk;
    }
    
    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    public Long getMunicipioPk() {
        return municipioPk;
    }

    public void setMunicipioPk(Long municipioPk) {
        this.municipioPk = municipioPk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String getPerNombreCompleto() {
        return perNombreCompleto;
    }

    public void setPerNombreCompleto(String perNombreCompleto) {
        this.perNombreCompleto = perNombreCompleto;
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

    public Boolean getInicializarElementosHogar() {
        return inicializarElementosHogar;
    }

    public void setInicializarElementosHogar(Boolean inicializarElementosHogar) {
        this.inicializarElementosHogar = inicializarElementosHogar;
    }

    public Boolean getInicializarMenores() {
        return inicializarMenores;
    }

    public void setInicializarMenores(Boolean inicializarMenores) {
        this.inicializarMenores = inicializarMenores;
    }
    
    

    
}
