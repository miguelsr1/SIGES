/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;

public class FiltroIncorporacion implements Serializable {

    private Long incPk;
    private String nombreCompleto;
    private String primerNombre;
    private String segundoNombre;
    private String tercerNombre;
    private String primerApellido;
    private String segundoApellido;
    private String tercerApellido;
    private LocalDate fechaNacimiento;
    private String pasaporte;
    private String carneResidente;
    private String dui;
    private Long sexoPk;
    private LocalDate fechaNacimientoDesde;
    private LocalDate fechaNacimientoHasta;
  
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroIncorporacion() {
        this.first = 0L;
    }

    public Long getIncPk() {
        return incPk;
    }

    public void setIncPk(Long incPk) {
        this.incPk = incPk;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getTercerNombre() {
        return tercerNombre;
    }

    public void setTercerNombre(String tercerNombre) {
        this.tercerNombre = tercerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getTercerApellido() {
        return tercerApellido;
    }

    public void setTercerApellido(String tercerApellido) {
        this.tercerApellido = tercerApellido;
    }

    public String getPasaporte() {
        return pasaporte;
    }

    public void setPasaporte(String pasaporte) {
        this.pasaporte = pasaporte;
    }

    public String getCarneResidente() {
        return carneResidente;
    }

    public void setCarneResidente(String carneResidente) {
        this.carneResidente = carneResidente;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public Long getSexoPk() {
        return sexoPk;
    }

    public void setSexoPk(Long sexoPk) {
        this.sexoPk = sexoPk;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaNacimientoDesde() {
        return fechaNacimientoDesde;
    }

    public void setFechaNacimientoDesde(LocalDate fechaNacimientoDesde) {
        this.fechaNacimientoDesde = fechaNacimientoDesde;
    }

    public LocalDate getFechaNacimientoHasta() {
        return fechaNacimientoHasta;
    }

    public void setFechaNacimientoHasta(LocalDate fechaNacimientoHasta) {
        this.fechaNacimientoHasta = fechaNacimientoHasta;
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

    

}
