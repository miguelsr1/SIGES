/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "riePk", scope = SgRelInmuebleEspacioComun.class)
public class SgRelInmuebleEspacioComun implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long riePk;
    

    private Integer rieCantidad;

    private String rieUltModUsuario;

    private LocalDateTime rieUltModFecha;

    private Integer rieVersion;    

    private SgInmueblesSedes rieInmuebleSede;    

    private SgEspacioComun rieEspacioComun;
    
    private Integer rieBueno;
   
    private Integer rieRegular;
    
    private Integer rieMalo;
    
    private String rieDescripcion;
    
    public SgRelInmuebleEspacioComun() {
        
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(rieBueno!=null?rieBueno:0);
        total=total+(rieRegular!=null?rieRegular:0);
        total=total+(rieMalo!=null?rieMalo:0);
        return total;
    }

    public Long getRiePk() {
        return riePk;
    }

    public void setRiePk(Long riePk) {
        this.riePk = riePk;
    }

    public Integer getRieCantidad() {
        return rieCantidad;
    }

    public void setRieCantidad(Integer rieCantidad) {
        this.rieCantidad = rieCantidad;
    }

    public String getRieUltModUsuario() {
        return rieUltModUsuario;
    }

    public void setRieUltModUsuario(String rieUltModUsuario) {
        this.rieUltModUsuario = rieUltModUsuario;
    }

    public LocalDateTime getRieUltModFecha() {
        return rieUltModFecha;
    }

    public void setRieUltModFecha(LocalDateTime rieUltModFecha) {
        this.rieUltModFecha = rieUltModFecha;
    }

    public Integer getRieVersion() {
        return rieVersion;
    }

    public void setRieVersion(Integer rieVersion) {
        this.rieVersion = rieVersion;
    }

    public SgInmueblesSedes getRieInmuebleSede() {
        return rieInmuebleSede;
    }

    public void setRieInmuebleSede(SgInmueblesSedes rieInmuebleSede) {
        this.rieInmuebleSede = rieInmuebleSede;
    }

    public SgEspacioComun getRieEspacioComun() {
        return rieEspacioComun;
    }

    public void setRieEspacioComun(SgEspacioComun rieEspacioComun) {
        this.rieEspacioComun = rieEspacioComun;
    }

    public Integer getRieBueno() {
        return rieBueno;
    }

    public void setRieBueno(Integer rieBueno) {
        this.rieBueno = rieBueno;
    }

    public Integer getRieRegular() {
        return rieRegular;
    }

    public void setRieRegular(Integer rieRegular) {
        this.rieRegular = rieRegular;
    }

    public Integer getRieMalo() {
        return rieMalo;
    }

    public void setRieMalo(Integer rieMalo) {
        this.rieMalo = rieMalo;
    }

    public String getRieDescripcion() {
        return rieDescripcion;
    }

    public void setRieDescripcion(String rieDescripcion) {
        this.rieDescripcion = rieDescripcion;
    }

  

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.riePk);
        hash = 59 * hash + Objects.hashCode(this.rieInmuebleSede);
        hash = 59 * hash + Objects.hashCode(this.rieEspacioComun);
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
        final SgRelInmuebleEspacioComun other = (SgRelInmuebleEspacioComun) obj;
        if (!Objects.equals(this.riePk, other.riePk)) {
            return false;
        }
        if (!Objects.equals(this.rieInmuebleSede, other.rieInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.rieEspacioComun, other.rieEspacioComun)) {
            return false;
        }
        return true;
    }

  
    
}
