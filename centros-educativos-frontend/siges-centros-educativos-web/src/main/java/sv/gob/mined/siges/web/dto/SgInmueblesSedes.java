/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoInmueble;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgPropietariosTerreno;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tisPk", scope = SgInmueblesSedes.class)
public class SgInmueblesSedes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tisPk;
    
    private String tisObservaciones;
    
    private String tisCodigo;
    
    private Boolean tisTerrenoPrincipal;
    
    private Boolean tisActivoFijo;
    
    private Boolean tisPropietario;
            
    private SgSedeLite tisSedeFk;

    private SgDireccion tisDireccion;
    
    private String tisUltModUsuario;
    
    private BigDecimal tisAreaTotal;
    
    private BigDecimal tisAreaDisponible;
    
    private LocalDateTime tisUltModFecha;
    
    private Integer tisVersion;

    private List<SgInmueblesVulnerabilidades> ivuInmueblesSede;
    
     private List<SgRelInmuebleEspacioComun> tisRelInmuebleEspacioComun;    
 
    private List<SgRelInmuebleServicioBasico> tisServicioBasico;
    
    private SgEstadoInmueble tisEstadoInmueble;
    
    private SgPropietariosTerreno tisPropietariosTerreno;

    private String tisDetallePropietario;
    
    private SgFuenteFinanciamiento tisFuenteFinanciamiento;    
 
    private BigDecimal tisValorAdquisicion;

    private LocalDate tisFechaAdquisicion;
    
    private SgProcesoLegalizacion tisProcesoLegalizacion;
    
    private Long tisNumeroCorrelativo;
    
    public SgInmueblesSedes() {
        ivuInmueblesSede = new ArrayList();
        tisDireccion = new SgDireccion();
        tisPropietario=Boolean.FALSE;
        tisTerrenoPrincipal=Boolean.FALSE;
    }

    public Long getTisPk() {
        return tisPk;
    }

    public void setTisPk(Long tisPk) {
        this.tisPk = tisPk;
    }

    public String getTisObservaciones() {
        return tisObservaciones;
    }

    public void setTisObservaciones(String tisObservaciones) {
        this.tisObservaciones = tisObservaciones;
    }

    public Boolean getTisTerrenoPrincipal() {
        return tisTerrenoPrincipal;
    }

    public void setTisTerrenoPrincipal(Boolean tisTerrenoPrincipal) {
        this.tisTerrenoPrincipal = tisTerrenoPrincipal;
    }

    public Boolean getTisPropietario() {
        return tisPropietario;
    }

    public void setTisPropietario(Boolean tisPropietario) {
        this.tisPropietario = tisPropietario;
    }

    public SgSedeLite getTisSedeFk() {
        return tisSedeFk;
    }

    public void setTisSedeFk(SgSedeLite tisSedeFk) {
        this.tisSedeFk = tisSedeFk;
    }

    public String getTisUltModUsuario() {
        return tisUltModUsuario;
    }

    public void setTisUltModUsuario(String tisUltModUsuario) {
        this.tisUltModUsuario = tisUltModUsuario;
    }

    public BigDecimal getTisAreaTotal() {
        return tisAreaTotal;
    }

    public void setTisAreaTotal(BigDecimal tisAreaTotal) {
        this.tisAreaTotal = tisAreaTotal;
    }

    public BigDecimal getTisAreaDisponible() {
        return tisAreaDisponible;
    }

    public void setTisAreaDisponible(BigDecimal tisAreaDisponible) {
        this.tisAreaDisponible = tisAreaDisponible;
    }

    public LocalDateTime getTisUltModFecha() {
        return tisUltModFecha;
    }

    public void setTisUltModFecha(LocalDateTime tisUltModFecha) {
        this.tisUltModFecha = tisUltModFecha;
    }

    public Integer getTisVersion() {
        return tisVersion;
    }

    public void setTisVersion(Integer tisVersion) {
        this.tisVersion = tisVersion;
    }

    public List<SgInmueblesVulnerabilidades> getIvuInmueblesSede() {
        return ivuInmueblesSede;
    }

    public void setIvuInmueblesSede(List<SgInmueblesVulnerabilidades> ivuInmueblesSede) {
        this.ivuInmueblesSede = ivuInmueblesSede;
    }

    public String getTisCodigo() {
        return tisCodigo;
    }

    public void setTisCodigo(String tisCodigo) {
        this.tisCodigo = tisCodigo;
    }

    public SgDireccion getTisDireccion() {
        return tisDireccion;
    }

    public void setTisDireccion(SgDireccion tisDireccion) {
        this.tisDireccion = tisDireccion;
    }

    public SgEstadoInmueble getTisEstadoInmueble() {
        return tisEstadoInmueble;
    }

    public void setTisEstadoInmueble(SgEstadoInmueble tisEstadoInmueble) {
        this.tisEstadoInmueble = tisEstadoInmueble;
    }

    public List<SgRelInmuebleEspacioComun> getTisRelInmuebleEspacioComun() {
        return tisRelInmuebleEspacioComun;
    }

    public void setTisRelInmuebleEspacioComun(List<SgRelInmuebleEspacioComun> tisRelInmuebleEspacioComun) {
        this.tisRelInmuebleEspacioComun = tisRelInmuebleEspacioComun;
    }



    public List<SgRelInmuebleServicioBasico> getTisServicioBasico() {
        return tisServicioBasico;
    }

    public void setTisServicioBasico(List<SgRelInmuebleServicioBasico> tisServicioBasico) {
        this.tisServicioBasico = tisServicioBasico;
    }

    public SgPropietariosTerreno getTisPropietariosTerreno() {
        return tisPropietariosTerreno;
    }

    public void setTisPropietariosTerreno(SgPropietariosTerreno tisPropietariosTerreno) {
        this.tisPropietariosTerreno = tisPropietariosTerreno;
    }

    public String getTisDetallePropietario() {
        return tisDetallePropietario;
    }

    public void setTisDetallePropietario(String tisDetallePropietario) {
        this.tisDetallePropietario = tisDetallePropietario;
    }

    public SgFuenteFinanciamiento getTisFuenteFinanciamiento() {
        return tisFuenteFinanciamiento;
    }

    public void setTisFuenteFinanciamiento(SgFuenteFinanciamiento tisFuenteFinanciamiento) {
        this.tisFuenteFinanciamiento = tisFuenteFinanciamiento;
    }

    public BigDecimal getTisValorAdquisicion() {
        return tisValorAdquisicion;
    }

    public void setTisValorAdquisicion(BigDecimal tisValorAdquisicion) {
        this.tisValorAdquisicion = tisValorAdquisicion;
    }

   

    public LocalDate getTisFechaAdquisicion() {
        return tisFechaAdquisicion;
    }

    public void setTisFechaAdquisicion(LocalDate tisFechaAdquisicion) {
        this.tisFechaAdquisicion = tisFechaAdquisicion;
    }

    public SgProcesoLegalizacion getTisProcesoLegalizacion() {
        return tisProcesoLegalizacion;
    }

    public void setTisProcesoLegalizacion(SgProcesoLegalizacion tisProcesoLegalizacion) {
        this.tisProcesoLegalizacion = tisProcesoLegalizacion;
    }

    public Long getTisNumeroCorrelativo() {
        return tisNumeroCorrelativo;
    }

    public void setTisNumeroCorrelativo(Long tisNumeroCorrelativo) {
        this.tisNumeroCorrelativo = tisNumeroCorrelativo;
    }

    public Boolean getTisActivoFijo() {
        return tisActivoFijo;
    }

    public void setTisActivoFijo(Boolean tisActivoFijo) {
        this.tisActivoFijo = tisActivoFijo;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.tisPk);
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
        final SgInmueblesSedes other = (SgInmueblesSedes) obj;
        if (!Objects.equals(this.tisPk, other.tisPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes[ tisPk=" + tisPk + " ]";
    }

  
    
}
