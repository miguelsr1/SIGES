package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;

public class SgRegistroIncorporacion implements Serializable  {
    
    private String dui;
    private String numCarneResidente;

    private String numPasaporte;
    private Long paisPasaportePk;
    
    private String primerNombre;
    private String segundoNombre;
    private String tercerNombre;
    private String primerApellido;
    private String segundoApellido;
    private String tercerApellido;
    private Long sexoPk;
    private Long estadoFamiliarPk;
    private Long nacionalidadPk;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;
    
    private String nombreSede;
    private String ultimoGradoEstudio;
    private String ciudad;
    private Integer anioEstudio;
    private String numTramite;
    private String numResolucion;
      
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaAprobacion;
       

    public SgRegistroIncorporacion() {
    }
    

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNumCarneResidente() {
        return numCarneResidente;
    }

    public void setNumCarneResidente(String numCarneResidente) {
        this.numCarneResidente = numCarneResidente;
    }

    public String getNumPasaporte() {
        return numPasaporte;
    }

    public void setNumPasaporte(String numPasaporte) {
        this.numPasaporte = numPasaporte;
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

    public Long getSexoPk() {
        return sexoPk;
    }

    public void setSexoPk(Long sexoPk) {
        this.sexoPk = sexoPk;
    }

    public Long getEstadoFamiliarPk() {
        return estadoFamiliarPk;
    }

    public void setEstadoFamiliarPk(Long estadoFamiliarPk) {
        this.estadoFamiliarPk = estadoFamiliarPk;
    }

    public Long getNacionalidadPk() {
        return nacionalidadPk;
    }

    public void setNacionalidadPk(Long nacionalidadPk) {
        this.nacionalidadPk = nacionalidadPk;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public Long getPaisPasaportePk() {
        return paisPasaportePk;
    }

    public void setPaisPasaportePk(Long paisPasaportePk) {
        this.paisPasaportePk = paisPasaportePk;
    }

    public String getUltimoGradoEstudio() {
        return ultimoGradoEstudio;
    }

    public void setUltimoGradoEstudio(String ultimoGradoEstudio) {
        this.ultimoGradoEstudio = ultimoGradoEstudio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getAnioEstudio() {
        return anioEstudio;
    }

    public void setAnioEstudio(Integer anioEstudio) {
        this.anioEstudio = anioEstudio;
    }

    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getNumResolucion() {
        return numResolucion;
    }

    public void setNumResolucion(String numResolucion) {
        this.numResolucion = numResolucion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    

    
    
}
