/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tisPk", scope = SgInmueblesSedes.class)
public class SgInmueblesSedes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tisPk;

    private String tisObservaciones;

    private String tisCodigo;

    private Boolean tisTerrenoPrincipal;

    private Boolean tisPropietario;

    private SgSede tisSedeFk;

    private SgDireccion tisDireccion;

    private String tisUltModUsuario;

    private BigDecimal tisAreaTotal;

    private BigDecimal tisAreaDisponible;

    private LocalDateTime tisUltModFecha;

    private Integer tisVersion;

    private String tisDetallePropietario;

    private BigDecimal tisValorAdquisicion;

    private LocalDate tisFechaAdquisicion;

    private Long tisNumeroCorrelativo;

    private Boolean tisActivoFijo;

    private Boolean tisTieneTransformador;
    
    private Integer tisCapacidadTransformador;

    
    private Boolean tisPoseeAreaAcopio;
    
    private String tisCondicionesAcceso;
    
    
    private Boolean tisPerteneceSede;
  


    public SgInmueblesSedes() {
     
        tisDireccion = new SgDireccion();
        tisPropietario = Boolean.FALSE;
        tisTerrenoPrincipal = Boolean.FALSE;
        tisActivoFijo = Boolean.FALSE;
        tisPerteneceSede=Boolean.TRUE;
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

    public SgSede getTisSedeFk() {
        return tisSedeFk;
    }

    public void setTisSedeFk(SgSede tisSedeFk) {
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


    public String getTisDetallePropietario() {
        return tisDetallePropietario;
    }

    public void setTisDetallePropietario(String tisDetallePropietario) {
        this.tisDetallePropietario = tisDetallePropietario;
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

    public Boolean getTisTieneTransformador() {
        return tisTieneTransformador;
    }

    public void setTisTieneTransformador(Boolean tisTieneTransformador) {
        this.tisTieneTransformador = tisTieneTransformador;
    }

    public Integer getTisCapacidadTransformador() {
        return tisCapacidadTransformador;
    }

    public void setTisCapacidadTransformador(Integer tisCapacidadTransformador) {
        this.tisCapacidadTransformador = tisCapacidadTransformador;
    }

    public Boolean getTisPoseeAreaAcopio() {
        return tisPoseeAreaAcopio;
    }

    public void setTisPoseeAreaAcopio(Boolean tisPoseeAreaAcopio) {
        this.tisPoseeAreaAcopio = tisPoseeAreaAcopio;
    }

    public String getTisCondicionesAcceso() {
        return tisCondicionesAcceso;
    }

    public void setTisCondicionesAcceso(String tisCondicionesAcceso) {
        this.tisCondicionesAcceso = tisCondicionesAcceso;
    }

    public Boolean getTisPerteneceSede() {
        return tisPerteneceSede;
    }

    public void setTisPerteneceSede(Boolean tisPerteneceSede) {
        this.tisPerteneceSede = tisPerteneceSede;
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
