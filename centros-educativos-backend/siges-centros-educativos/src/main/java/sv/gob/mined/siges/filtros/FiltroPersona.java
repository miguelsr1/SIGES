/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;

public class FiltroPersona extends FiltroSeccion implements Serializable {

    private Long perPk;
    private List<Long> perPks;
    private String perNombreCompleto;
    private Boolean perTieneDUI;
    private String perDUINombreCompleto;
    private String perPrimerNombre;
    private String perSegundoNombre;
    private String perTercerNombre;
    private String perPrimerApellido;
    private String perSegundoApellido;
    private String perTercerApellido;
    private LocalDate perFechaNacimiento;
    private Long nie;
    private Long cun;
    private String dui;
    private String nip;
    private String nit;
    private String inpep;
    private String isss;
    private String nup;
    private Long nroPartida;
    private String folioPartida;
    private String libroPartida;
    private List<SgIdentificacion> otrasIden;
    private Long perSexoPk;
    private Long perDepartamentoPk;
    private Long perMunicipioPk;
    private LocalDate perFechaNacimientoDesde;
    private LocalDate perFechaNacimientoHasta;
    private List<Long> ignorarPersonasPk;
    private Boolean perTieneIdentificacion;
    private Boolean perPresentaPartida;
    private List<Long> nies;
    private Long perZonaPk;
    
    private Boolean perTieneDiscapacidad;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroPersona() {
        super();
        seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerNombreCompleto() {
        return perNombreCompleto;
    }

    public void setPerNombreCompleto(String perNombreCompleto) {
        this.perNombreCompleto = perNombreCompleto;
    }

    public LocalDate getPerFechaNacimiento() {
        return perFechaNacimiento;
    }

    public void setPerFechaNacimiento(LocalDate perFechaNacimiento) {
        this.perFechaNacimiento = perFechaNacimiento;
    }

    public List<Long> getNies() {
        return nies;
    }

    public void setNies(List<Long> nies) {
        this.nies = nies;
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

    public Long getNie() {
        return nie;
    }

    public void setNie(Long nie) {
        this.nie = nie;
    }

    public Long getCun() {
        return cun;
    }

    public void setCun(Long cun) {
        this.cun = cun;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public Long getNroPartida() {
        return nroPartida;
    }

    public void setNroPartida(Long nroPartida) {
        this.nroPartida = nroPartida;
    }

    public String getFolioPartida() {
        return folioPartida;
    }

    public void setFolioPartida(String folioPartida) {
        this.folioPartida = folioPartida;
    }

    public String getLibroPartida() {
        return libroPartida;
    }

    public void setLibroPartida(String libroPartida) {
        this.libroPartida = libroPartida;
    }

    public Long getPerSexoPk() {
        return perSexoPk;
    }

    public void setPerSexoPk(Long perSexoPk) {
        this.perSexoPk = perSexoPk;
    }

    public Long getPerDepartamentoPk() {
        return perDepartamentoPk;
    }

    public void setPerDepartamentoPk(Long perDepartamentoPk) {
        this.perDepartamentoPk = perDepartamentoPk;
    }

    public Long getPerMunicipioPk() {
        return perMunicipioPk;
    }

    public void setPerMunicipioPk(Long perMunicipioPk) {
        this.perMunicipioPk = perMunicipioPk;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public LocalDate getPerFechaNacimientoDesde() {
        return perFechaNacimientoDesde;
    }

    public void setPerFechaNacimientoDesde(LocalDate perFechaNacimientoDesde) {
        this.perFechaNacimientoDesde = perFechaNacimientoDesde;
    }

    public LocalDate getPerFechaNacimientoHasta() {
        return perFechaNacimientoHasta;
    }

    public void setPerFechaNacimientoHasta(LocalDate perFechaNacimientoHasta) {
        this.perFechaNacimientoHasta = perFechaNacimientoHasta;
    }

    public List<SgIdentificacion> getOtrasIden() {
        return otrasIden;
    }

    public void setOtrasIden(List<SgIdentificacion> otrasIden) {
        this.otrasIden = otrasIden;
    }

    public List<Long> getIgnorarPersonasPk() {
        return ignorarPersonasPk;
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

    public void setIgnorarPersonasPk(List<Long> ignorarPersonasPk) {
        this.ignorarPersonasPk = ignorarPersonasPk;
    }

    public String getPerTercerNombre() {
        return perTercerNombre;
    }

    public void setPerTercerNombre(String perTercerNombre) {
        this.perTercerNombre = perTercerNombre;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public String getInpep() {
        return inpep;
    }

    public void setInpep(String inpep) {
        this.inpep = inpep;
    }

    public String getIsss() {
        return isss;
    }

    public void setIsss(String isss) {
        this.isss = isss;
    }

    public String getNup() {
        return nup;
    }

    public void setNup(String nup) {
        this.nup = nup;
    }


    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public List<Long> getPerPks() {
        return perPks;
    }

    public void setPerPks(List<Long> perPks) {
        this.perPks = perPks;
    }
    
    @Override
    public String toString() {
        return "FiltroPersona{" + "perPk=" + perPk + ", perNombreCompleto=" + perNombreCompleto + ", perFechaNacimiento=" + perFechaNacimiento + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + '}';
    }

    public Boolean getPerTieneIdentificacion() {
        return perTieneIdentificacion;
    }

    public void setPerTieneIdentificacion(Boolean perTieneIdentificacion) {
        this.perTieneIdentificacion = perTieneIdentificacion;
    }

    public Boolean getPerPresentaPartida() {
        return perPresentaPartida;
    }

    public void setPerPresentaPartida(Boolean perPresentaPartida) {
        this.perPresentaPartida = perPresentaPartida;
    }

    public Boolean getPerTieneDiscapacidad() {
        return perTieneDiscapacidad;
    }

    public void setPerTieneDiscapacidad(Boolean perTieneDiscapacidad) {
        this.perTieneDiscapacidad = perTieneDiscapacidad;
    }

    public Long getPerZonaPk() {
        return perZonaPk;
    }

    public void setPerZonaPk(Long perZonaPk) {
        this.perZonaPk = perZonaPk;
    }

    public Boolean getPerTieneDUI() {
        return perTieneDUI;
    }

    public void setPerTieneDUI(Boolean perTieneDUI) {
        this.perTieneDUI = perTieneDUI;
    }

    public String getPerDUINombreCompleto() {
        return perDUINombreCompleto;
    }

    public void setPerDUINombreCompleto(String perDUINombreCompleto) {
        this.perDUINombreCompleto = perDUINombreCompleto;
    }

   

}
