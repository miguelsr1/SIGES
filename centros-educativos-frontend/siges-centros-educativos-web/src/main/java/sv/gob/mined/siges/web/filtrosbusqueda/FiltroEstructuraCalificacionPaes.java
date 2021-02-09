package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoArchivoCalificacionPAES;

public class FiltroEstructuraCalificacionPaes implements Serializable {

    private EnumEstadoArchivoCalificacionPAES estadoPaes;
    private Long estArchivoCalificacionPAES;
    private String Nie;
    private String codigoExterno;
    private Integer anio;
    private Long archivoPaesPk;
    private String perPrimerNombre;
    private String perSegundoNombre;
    private String perPrimerApellido;
    private String perSegundoApellido;
    private String perNombreCompleto;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroEstructuraCalificacionPaes() {
    }

    public EnumEstadoArchivoCalificacionPAES getEstadoPaes() {
        return estadoPaes;
    }

    public void setEstadoPaes(EnumEstadoArchivoCalificacionPAES estadoPaes) {
        this.estadoPaes = estadoPaes;
    }

    public Long getEstArchivoCalificacionPAES() {
        return estArchivoCalificacionPAES;
    }

    public void setEstArchivoCalificacionPAES(Long estArchivoCalificacionPAES) {
        this.estArchivoCalificacionPAES = estArchivoCalificacionPAES;
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

    public String getNie() {
        return Nie;
    }

    public void setNie(String Nie) {
        this.Nie = Nie;
    }

    public String getCodigoExterno() {
        return codigoExterno;
    }

    public void setCodigoExterno(String codigoExterno) {
        this.codigoExterno = codigoExterno;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getArchivoPaesPk() {
        return archivoPaesPk;
    }

    public void setArchivoPaesPk(Long archivoPaesPk) {
        this.archivoPaesPk = archivoPaesPk;
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

    public String getPerNombreCompleto() {
        return perNombreCompleto;
    }

    public void setPerNombreCompleto(String perNombreCompleto) {
        this.perNombreCompleto = perNombreCompleto;
    }



    
}
