/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoConstruccion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ediPk", scope = SgEdificio.class)
public class SgEdificio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ediPk;

    private String ediCodigo;

    private String ediNombre;

    private String ediNombreBusqueda;

    private BigDecimal ediInversion;

    private Integer ediCantidadNiveles;

    private BigDecimal ediArea;

    private Double ediLatitud;

    private Double ediLongitud;

    private String ediObservaciones;

    private LocalDateTime ediUltModFecha;

    private String ediUltModUsuario;

    private Integer ediVersion;

    private SgInmueblesSedes ediInmuebleSedeFk;

    private SgTipoConstruccion ediTipoConstruccion;
    
    private List<SgRelEdificioEspacio> ediRelEdificioEspacio;
    
    private List<SgRelEdificioServicio> ediRelEdificioServicio;
    
    private List<SgRelEdificioServicioSanitario> ediRelEdificioServicioSanitario;
    
    private List<SgRelInmuebleUnidadResp> ediRelInmuebleUnidadResp;
    
    public SgEdificio() {
       
    }
    
    @JsonIgnore
    public String getRelInmuebleUnidadRespAsString(){
        if (this.ediRelInmuebleUnidadResp != null){
            return this.ediRelInmuebleUnidadResp.stream().map(d -> d.getNombreElemento()).collect(Collectors.joining("<br/> "));
        }
        return null;
    }


    public Long getEdiPk() {
        return ediPk;
    }

    public void setEdiPk(Long ediPk) {
        this.ediPk = ediPk;
    }

    public String getEdiCodigo() {
        return ediCodigo;
    }

    public void setEdiCodigo(String ediCodigo) {
        this.ediCodigo = ediCodigo;
    }

    public String getEdiNombre() {
        return ediNombre;
    }

    public void setEdiNombre(String ediNombre) {
        this.ediNombre = ediNombre;
    }

    public LocalDateTime getEdiUltModFecha() {
        return ediUltModFecha;
    }

    public void setEdiUltModFecha(LocalDateTime ediUltModFecha) {
        this.ediUltModFecha = ediUltModFecha;
    }

    public String getEdiUltModUsuario() {
        return ediUltModUsuario;
    }

    public void setEdiUltModUsuario(String ediUltModUsuario) {
        this.ediUltModUsuario = ediUltModUsuario;
    }

    public Integer getEdiVersion() {
        return ediVersion;
    }

    public void setEdiVersion(Integer ediVersion) {
        this.ediVersion = ediVersion;
    }

    public String getEdiNombreBusqueda() {
        return ediNombreBusqueda;
    }

    public void setEdiNombreBusqueda(String ediNombreBusqueda) {
        this.ediNombreBusqueda = ediNombreBusqueda;
    }

    public BigDecimal getEdiInversion() {
        return ediInversion;
    }

    public void setEdiInversion(BigDecimal ediInversion) {
        this.ediInversion = ediInversion;
    }

    public Integer getEdiCantidadNiveles() {
        return ediCantidadNiveles;
    }

    public void setEdiCantidadNiveles(Integer ediCantidadNiveles) {
        this.ediCantidadNiveles = ediCantidadNiveles;
    }

    public BigDecimal getEdiArea() {
        return ediArea;
    }

    public void setEdiArea(BigDecimal ediArea) {
        this.ediArea = ediArea;
    }

    public Double getEdiLatitud() {
        return ediLatitud;
    }

    public void setEdiLatitud(Double ediLatitud) {
        this.ediLatitud = ediLatitud;
    }

    public Double getEdiLongitud() {
        return ediLongitud;
    }

    public void setEdiLongitud(Double ediLongitud) {
        this.ediLongitud = ediLongitud;
    }

    public String getEdiObservaciones() {
        return ediObservaciones;
    }

    public void setEdiObservaciones(String ediObservaciones) {
        this.ediObservaciones = ediObservaciones;
    }

    public SgInmueblesSedes getEdiInmuebleSedeFk() {
        return ediInmuebleSedeFk;
    }

    public void setEdiInmuebleSedeFk(SgInmueblesSedes ediInmuebleSedeFk) {
        this.ediInmuebleSedeFk = ediInmuebleSedeFk;
    }

    public SgTipoConstruccion getEdiTipoConstruccion() {
        return ediTipoConstruccion;
    }

    public void setEdiTipoConstruccion(SgTipoConstruccion ediTipoConstruccion) {
        this.ediTipoConstruccion = ediTipoConstruccion;
    }

    public List<SgRelEdificioEspacio> getEdiRelEdificioEspacio() {
        return ediRelEdificioEspacio;
    }

    public void setEdiRelEdificioEspacio(List<SgRelEdificioEspacio> ediRelEdificioEspacio) {
        this.ediRelEdificioEspacio = ediRelEdificioEspacio;
    }

    public List<SgRelEdificioServicio> getEdiRelEdificioServicio() {
        return ediRelEdificioServicio;
    }

    public void setEdiRelEdificioServicio(List<SgRelEdificioServicio> ediRelEdificioServicio) {
        this.ediRelEdificioServicio = ediRelEdificioServicio;
    }

    public List<SgRelEdificioServicioSanitario> getEdiRelEdificioServicioSanitario() {
        return ediRelEdificioServicioSanitario;
    }

    public void setEdiRelEdificioServicioSanitario(List<SgRelEdificioServicioSanitario> ediRelEdificioServicioSanitario) {
        this.ediRelEdificioServicioSanitario = ediRelEdificioServicioSanitario;
    }

    public List<SgRelInmuebleUnidadResp> getEdiRelInmuebleUnidadResp() {
        return ediRelInmuebleUnidadResp;
    }

    public void setEdiRelInmuebleUnidadResp(List<SgRelInmuebleUnidadResp> ediRelInmuebleUnidadResp) {
        this.ediRelInmuebleUnidadResp = ediRelInmuebleUnidadResp;
    }

    
 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ediPk != null ? ediPk.hashCode() : 0);
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
        final SgEdificio other = (SgEdificio) obj;
        if (!Objects.equals(this.ediPk, other.ediPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEdificio[ ediPk=" + ediPk + " ]";
    }
    
}
