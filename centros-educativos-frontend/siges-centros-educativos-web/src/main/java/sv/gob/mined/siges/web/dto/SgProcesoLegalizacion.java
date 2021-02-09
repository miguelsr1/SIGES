/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.dto.catalogo.SgInfMinisterioOtorgante;
import sv.gob.mined.siges.web.dto.catalogo.SgInfNaturalezaContrato;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "plePk", scope = SgProcesoLegalizacion.class)
public class SgProcesoLegalizacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long plePk;


    private Integer pleNumeroPresentacion;


    private LocalDate pleFechaPresentacion;
    
    private String plePresentante;


    private String pleObservacionesPresentacion;
    
  
    private String pleCodigoAnterior;
    

    private String pleNotario;
    

    private String pleOtorgante;
    
   
    private String pleNumeroEscritura;
    
    private Integer plePlazoAnio;    
  
    private String pleLibroProtocolo;
    
  
    private LocalDate pleFechaContrato;
    

    private Integer pleNumeroAntecedente;
    
  
    private String pleLibro;
    
  
    private String pleMatricula;
    
  
    private Integer pleNumeroInscripcion;
    

    private LocalDate pleFechaInscripcion;
    
    private String pleAsiento;    
   
    private String pleNumeroMatricula;
    
   
    private LocalDate pleFechaMatricula;
    
    
    private String pleObservacionesInscripciones;
    
    
    private LocalDate pleFechaAsignacion;
    

    private String pleColaboradorAsignado;
    

    private Integer pleNumeroExpediente;
    
  
    private String pleObservacionesEstado;
    

    private Integer pleNumeroAcuerdo;
    

    private LocalDate pleFechaAcuerdo;  
        

    private LocalDateTime pleUltModFecha;

    private String pleUltModUsuario;


    private Integer pleVersion;
    

    private SgEstadoProcesoLegalizacion pleEstado;
    

    private SgInfNaturalezaContrato pleNaturalezaContrato;
    

    private SgEstadoProcesoLegalizacion pleEstadoProceso;
    

    private SgInfMinisterioOtorgante pleMinisterioOtorgante; 
    
    
    public SgProcesoLegalizacion() {
       
    }

    public Long getPlePk() {
        return plePk;
    }

    public void setPlePk(Long plePk) {
        this.plePk = plePk;
    }

    public Integer getPleNumeroPresentacion() {
        return pleNumeroPresentacion;
    }

    public void setPleNumeroPresentacion(Integer pleNumeroPresentacion) {
        this.pleNumeroPresentacion = pleNumeroPresentacion;
    }

    public LocalDate getPleFechaPresentacion() {
        return pleFechaPresentacion;
    }

    public void setPleFechaPresentacion(LocalDate pleFechaPresentacion) {
        this.pleFechaPresentacion = pleFechaPresentacion;
    }

    public String getPlePresentante() {
        return plePresentante;
    }

    public void setPlePresentante(String plePresentante) {
        this.plePresentante = plePresentante;
    }

    public String getPleObservacionesPresentacion() {
        return pleObservacionesPresentacion;
    }

    public void setPleObservacionesPresentacion(String pleObservacionesPresentacion) {
        this.pleObservacionesPresentacion = pleObservacionesPresentacion;
    }

    public String getPleCodigoAnterior() {
        return pleCodigoAnterior;
    }

    public void setPleCodigoAnterior(String pleCodigoAnterior) {
        this.pleCodigoAnterior = pleCodigoAnterior;
    }

    public String getPleNotario() {
        return pleNotario;
    }

    public void setPleNotario(String pleNotario) {
        this.pleNotario = pleNotario;
    }

    public String getPleOtorgante() {
        return pleOtorgante;
    }

    public void setPleOtorgante(String pleOtorgante) {
        this.pleOtorgante = pleOtorgante;
    }

    public String getPleNumeroEscritura() {
        return pleNumeroEscritura;
    }

    public void setPleNumeroEscritura(String pleNumeroEscritura) {
        this.pleNumeroEscritura = pleNumeroEscritura;
    }

    public Integer getPlePlazoAnio() {
        return plePlazoAnio;
    }

    public void setPlePlazoAnio(Integer plePlazoAnio) {
        this.plePlazoAnio = plePlazoAnio;
    }

    public String getPleLibroProtocolo() {
        return pleLibroProtocolo;
    }

    public void setPleLibroProtocolo(String pleLibroProtocolo) {
        this.pleLibroProtocolo = pleLibroProtocolo;
    }

    public LocalDate getPleFechaContrato() {
        return pleFechaContrato;
    }

    public void setPleFechaContrato(LocalDate pleFechaContrato) {
        this.pleFechaContrato = pleFechaContrato;
    }

    public Integer getPleNumeroAntecedente() {
        return pleNumeroAntecedente;
    }

    public void setPleNumeroAntecedente(Integer pleNumeroAntecedente) {
        this.pleNumeroAntecedente = pleNumeroAntecedente;
    }

    public String getPleLibro() {
        return pleLibro;
    }

    public void setPleLibro(String pleLibro) {
        this.pleLibro = pleLibro;
    }

    public String getPleMatricula() {
        return pleMatricula;
    }

    public void setPleMatricula(String pleMatricula) {
        this.pleMatricula = pleMatricula;
    }

    public Integer getPleNumeroInscripcion() {
        return pleNumeroInscripcion;
    }

    public void setPleNumeroInscripcion(Integer pleNumeroInscripcion) {
        this.pleNumeroInscripcion = pleNumeroInscripcion;
    }

    public LocalDate getPleFechaInscripcion() {
        return pleFechaInscripcion;
    }

    public void setPleFechaInscripcion(LocalDate pleFechaInscripcion) {
        this.pleFechaInscripcion = pleFechaInscripcion;
    }

    public String getPleAsiento() {
        return pleAsiento;
    }

    public void setPleAsiento(String pleAsiento) {
        this.pleAsiento = pleAsiento;
    }   
    

    public String getPleNumeroMatricula() {
        return pleNumeroMatricula;
    }

    public void setPleNumeroMatricula(String pleNumeroMatricula) {
        this.pleNumeroMatricula = pleNumeroMatricula;
    }

    public LocalDate getPleFechaMatricula() {
        return pleFechaMatricula;
    }

    public void setPleFechaMatricula(LocalDate pleFechaMatricula) {
        this.pleFechaMatricula = pleFechaMatricula;
    }

    public String getPleObservacionesInscripciones() {
        return pleObservacionesInscripciones;
    }

    public void setPleObservacionesInscripciones(String pleObservacionesInscripciones) {
        this.pleObservacionesInscripciones = pleObservacionesInscripciones;
    }

    public LocalDate getPleFechaAsignacion() {
        return pleFechaAsignacion;
    }

    public void setPleFechaAsignacion(LocalDate pleFechaAsignacion) {
        this.pleFechaAsignacion = pleFechaAsignacion;
    }

    public String getPleColaboradorAsignado() {
        return pleColaboradorAsignado;
    }

    public void setPleColaboradorAsignado(String pleColaboradorAsignado) {
        this.pleColaboradorAsignado = pleColaboradorAsignado;
    }

    public Integer getPleNumeroExpediente() {
        return pleNumeroExpediente;
    }

    public void setPleNumeroExpediente(Integer pleNumeroExpediente) {
        this.pleNumeroExpediente = pleNumeroExpediente;
    }

    public String getPleObservacionesEstado() {
        return pleObservacionesEstado;
    }

    public void setPleObservacionesEstado(String pleObservacionesEstado) {
        this.pleObservacionesEstado = pleObservacionesEstado;
    }

    public Integer getPleNumeroAcuerdo() {
        return pleNumeroAcuerdo;
    }

    public void setPleNumeroAcuerdo(Integer pleNumeroAcuerdo) {
        this.pleNumeroAcuerdo = pleNumeroAcuerdo;
    }

    public LocalDate getPleFechaAcuerdo() {
        return pleFechaAcuerdo;
    }

    public void setPleFechaAcuerdo(LocalDate pleFechaAcuerdo) {
        this.pleFechaAcuerdo = pleFechaAcuerdo;
    }

    public LocalDateTime getPleUltModFecha() {
        return pleUltModFecha;
    }

    public void setPleUltModFecha(LocalDateTime pleUltModFecha) {
        this.pleUltModFecha = pleUltModFecha;
    }

    public String getPleUltModUsuario() {
        return pleUltModUsuario;
    }

    public void setPleUltModUsuario(String pleUltModUsuario) {
        this.pleUltModUsuario = pleUltModUsuario;
    }

    public Integer getPleVersion() {
        return pleVersion;
    }

    public void setPleVersion(Integer pleVersion) {
        this.pleVersion = pleVersion;
    }

    public SgEstadoProcesoLegalizacion getPleEstado() {
        return pleEstado;
    }

    public void setPleEstado(SgEstadoProcesoLegalizacion pleEstado) {
        this.pleEstado = pleEstado;
    }

    public SgInfNaturalezaContrato getPleNaturalezaContrato() {
        return pleNaturalezaContrato;
    }

    public void setPleNaturalezaContrato(SgInfNaturalezaContrato pleNaturalezaContrato) {
        this.pleNaturalezaContrato = pleNaturalezaContrato;
    }

    public SgEstadoProcesoLegalizacion getPleEstadoProceso() {
        return pleEstadoProceso;
    }

    public void setPleEstadoProceso(SgEstadoProcesoLegalizacion pleEstadoProceso) {
        this.pleEstadoProceso = pleEstadoProceso;
    }

    public SgInfMinisterioOtorgante getPleMinisterioOtorgante() {
        return pleMinisterioOtorgante;
    }

    public void setPleMinisterioOtorgante(SgInfMinisterioOtorgante pleMinisterioOtorgante) {
        this.pleMinisterioOtorgante = pleMinisterioOtorgante;
    }

 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plePk != null ? plePk.hashCode() : 0);
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
        final SgProcesoLegalizacion other = (SgProcesoLegalizacion) obj;
        if (!Objects.equals(this.plePk, other.plePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEdificio[ ediPk=" + plePk + " ]";
    }
    
}
