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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ialPk", scope = SgRelInmuebleAlmacenamientoAgua.class)
public class SgRelInmuebleAlmacenamientoAgua implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ialPk;

    private Integer ialBueno;

    private Integer ialMalo;

    private Integer ialRegular;

    private String ialDescripcion;

    private LocalDateTime ialUltModFecha;

    private String ialUltModUsuario;

    private Integer ialVersion;

    private SgInmueblesSedes ialInmuebleSede;
    
    private SgInfTratamientoAgua ialAlmacenamientoAgua;
    
    public SgRelInmuebleAlmacenamientoAgua() {
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(ialBueno!=null?ialBueno:0);
        total=total+(ialRegular!=null?ialRegular:0);
        total=total+(ialMalo!=null?ialMalo:0);
        return total;
    }

    public Long getIalPk() {
        return ialPk;
    }

    public void setIalPk(Long ialPk) {
        this.ialPk = ialPk;
    }

    public Integer getIalBueno() {
        return ialBueno;
    }

    public void setIalBueno(Integer ialBueno) {
        this.ialBueno = ialBueno;
    }

    public Integer getIalMalo() {
        return ialMalo;
    }

    public void setIalMalo(Integer ialMalo) {
        this.ialMalo = ialMalo;
    }

    public Integer getIalRegular() {
        return ialRegular;
    }

    public void setIalRegular(Integer ialRegular) {
        this.ialRegular = ialRegular;
    }

    public String getIalDescripcion() {
        return ialDescripcion;
    }

    public void setIalDescripcion(String ialDescripcion) {
        this.ialDescripcion = ialDescripcion;
    }

  
    public LocalDateTime getIalUltModFecha() {
        return ialUltModFecha;
    }

    public void setIalUltModFecha(LocalDateTime ialUltModFecha) {
        this.ialUltModFecha = ialUltModFecha;
    }

    public String getIalUltModUsuario() {
        return ialUltModUsuario;
    }

    public void setIalUltModUsuario(String ialUltModUsuario) {
        this.ialUltModUsuario = ialUltModUsuario;
    }

    public Integer getIalVersion() {
        return ialVersion;
    }

    public void setIalVersion(Integer ialVersion) {
        this.ialVersion = ialVersion;
    }
    
    public SgInmueblesSedes getIalInmuebleSede() {
        return ialInmuebleSede;
    }

    public void setRisInmuebleSede(SgInmueblesSedes ialInmuebleSede) {
        this.ialInmuebleSede = ialInmuebleSede;
    }

    public SgInfTratamientoAgua getIalAlmacenamientoAgua() {
        return ialAlmacenamientoAgua;
    }

    public void setIalAlmacenamientoAgua(SgInfTratamientoAgua ialAlmacenamientoAgua) {
        this.ialAlmacenamientoAgua = ialAlmacenamientoAgua;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ialPk != null ? ialPk.hashCode() : 0);
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
        final SgRelInmuebleAlmacenamientoAgua other = (SgRelInmuebleAlmacenamientoAgua) obj;
        if (!Objects.equals(this.ialPk, other.ialPk)) {
            return false;
        }
        if (!Objects.equals(this.ialInmuebleSede, other.ialInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.ialAlmacenamientoAgua, other.ialAlmacenamientoAgua)) {
            return false;
        }
        return true;
    }
	
    


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleAlmacenamientoAgua[ ialPk=" + ialPk + " ]";
    }
    
}
