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
import sv.gob.mined.siges.web.dto.catalogo.SgInfAccesibilidad;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iacPk", scope = SgRelInmuebleAccesibilidad.class)
public class SgRelInmuebleAccesibilidad implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long iacPk;
 
    private Integer iacBueno;

    private Integer iacMalo;

    private Integer iacRegular;

    private String iacDescripcion;

    private LocalDateTime iacUltModFecha;

    private String iacUltModUsuario;

    private Integer iacVersion;
 
    private SgInmueblesSedes iacInmuebleSede;
    
    private SgInfAccesibilidad iacAccesibilidad;
    
    
    public SgRelInmuebleAccesibilidad() {
     
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(iacBueno!=null?iacBueno:0);
        total=total+(iacRegular!=null?iacRegular:0);
        total=total+(iacMalo!=null?iacMalo:0);
        return total;
    }

    public Long getIacPk() {
        return iacPk;
    }

    public void setIacPk(Long iacPk) {
        this.iacPk = iacPk;
    }

   
    public LocalDateTime getIacUltModFecha() {
        return iacUltModFecha;
    }

    public void setIacUltModFecha(LocalDateTime iacUltModFecha) {
        this.iacUltModFecha = iacUltModFecha;
    }

    public String getIacUltModUsuario() {
        return iacUltModUsuario;
    }

    public void setIacUltModUsuario(String iacUltModUsuario) {
        this.iacUltModUsuario = iacUltModUsuario;
    }

    public Integer getIacVersion() {
        return iacVersion;
    }

    public void setIacVersion(Integer iacVersion) {
        this.iacVersion = iacVersion;
    }

    public Integer getIacBueno() {
        return iacBueno;
    }

    public void setIacBueno(Integer iacBueno) {
        this.iacBueno = iacBueno;
    }

    public Integer getIacMalo() {
        return iacMalo;
    }

    public void setIacMalo(Integer iacMalo) {
        this.iacMalo = iacMalo;
    }

    public Integer getIacRegular() {
        return iacRegular;
    }

    public void setIacRegular(Integer iacRegular) {
        this.iacRegular = iacRegular;
    }

    public String getIacDescripcion() {
        return iacDescripcion;
    }

    public void setIacDescripcion(String iacDescripcion) {
        this.iacDescripcion = iacDescripcion;
    }

    public SgInmueblesSedes getIacInmuebleSede() {
        return iacInmuebleSede;
    }

    public void setIacInmuebleSede(SgInmueblesSedes iacInmuebleSede) {
        this.iacInmuebleSede = iacInmuebleSede;
    }

  

    public SgInfAccesibilidad getIacAccesibilidad() {
        return iacAccesibilidad;
    }

    public void setIacAccesibilidad(SgInfAccesibilidad iacAccesibilidad) {
        this.iacAccesibilidad = iacAccesibilidad;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iacPk != null ? iacPk.hashCode() : 0);
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
        final SgRelInmuebleAccesibilidad other = (SgRelInmuebleAccesibilidad) obj;
        if (!Objects.equals(this.iacPk, other.iacPk)) {
            return false;
        }
        if (!Objects.equals(this.iacAccesibilidad, other.iacAccesibilidad)) {
            return false;
        }
        return true;
    }
	



    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleAccesibilidad[ iacPk=" + iacPk + " ]";
    }
    
}
