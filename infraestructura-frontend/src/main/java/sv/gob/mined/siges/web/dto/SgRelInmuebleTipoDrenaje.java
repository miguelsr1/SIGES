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
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTratamientoAgua;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "itdPk", scope = SgRelInmuebleTipoDrenaje.class)
public class SgRelInmuebleTipoDrenaje implements Serializable {
    
    private static final long seritdVersionUID = 1L;
	
    private Long itdPk;

    private Integer itdBueno;

    private Integer itdMalo;

    private Integer itdRegular;

    private String itdDescripcion;

    private LocalDateTime itdUltModFecha;

    private String itdUltModUsuario;

    private Integer itdVersion;

    private SgInmueblesSedes itdInmuebleSede;
    
    private SgInfTratamientoAgua itdTipoDrenaje;
    
    
    public SgRelInmuebleTipoDrenaje() {
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(itdBueno!=null?itdBueno:0);
        total=total+(itdRegular!=null?itdRegular:0);
        total=total+(itdMalo!=null?itdMalo:0);
        return total;
    }

    public Long getItdPk() {
        return itdPk;
    }

    public void setItdPk(Long itdPk) {
        this.itdPk = itdPk;
    }

    public Integer getItdBueno() {
        return itdBueno;
    }

    public void setItdBueno(Integer itdBueno) {
        this.itdBueno = itdBueno;
    }

    public Integer getItdMalo() {
        return itdMalo;
    }

    public void setItdMalo(Integer itdMalo) {
        this.itdMalo = itdMalo;
    }

    public Integer getItdRegular() {
        return itdRegular;
    }

    public void setItdRegular(Integer itdRegular) {
        this.itdRegular = itdRegular;
    }

    public String getItdDescripcion() {
        return itdDescripcion;
    }

    public void setItdDescripcion(String itdDescripcion) {
        this.itdDescripcion = itdDescripcion;
    }

  
    public LocalDateTime getItdUltModFecha() {
        return itdUltModFecha;
    }

    public void setItdUltModFecha(LocalDateTime itdUltModFecha) {
        this.itdUltModFecha = itdUltModFecha;
    }

    public String getItdUltModUsuario() {
        return itdUltModUsuario;
    }

    public void setItdUltModUsuario(String itdUltModUsuario) {
        this.itdUltModUsuario = itdUltModUsuario;
    }

    public Integer getItdVersion() {
        return itdVersion;
    }

    public void setItdVersion(Integer itdVersion) {
        this.itdVersion = itdVersion;
    }
    
    public SgInmueblesSedes getItdInmuebleSede() {
        return itdInmuebleSede;
    }

    public void setRisInmuebleSede(SgInmueblesSedes itdInmuebleSede) {
        this.itdInmuebleSede = itdInmuebleSede;
    }

    public SgInfTratamientoAgua getItdTipoDrenaje() {
        return itdTipoDrenaje;
    }

    public void setItdTipoDrenaje(SgInfTratamientoAgua itdTipoDrenaje) {
        this.itdTipoDrenaje = itdTipoDrenaje;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itdPk != null ? itdPk.hashCode() : 0);
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
        final SgRelInmuebleTipoDrenaje other = (SgRelInmuebleTipoDrenaje) obj;
        if (!Objects.equals(this.itdPk, other.itdPk)) {
            return false;
        }
        if (!Objects.equals(this.itdInmuebleSede, other.itdInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.itdTipoDrenaje, other.itdTipoDrenaje)) {
            return false;
        }
        return true;
    }

   



    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleTipoDrenaje[ itdPk=" + itdPk + " ]";
    }
    
}
