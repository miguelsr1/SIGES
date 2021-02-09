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
import sv.gob.mined.siges.web.dto.catalogo.SgInfItemObraExterior;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rixPk", scope = SgRelInmuebleItemObraExterior.class)
public class SgRelInmuebleItemObraExterior implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rixPk;

    private Integer rixBueno;

    private Integer rixMalo;

    private Integer rixRegular;

    private String rixDescripcion;

    private LocalDateTime rixUltModFecha;

    private String rixUltModUsuario;

    private Integer rixVersion;

    private SgInmueblesSedes rixInmuebleSede;  
       
    private SgInfItemObraExterior rixItemObraExterior;
    
    
    public SgRelInmuebleItemObraExterior() {
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(rixBueno!=null?rixBueno:0);
        total=total+(rixRegular!=null?rixRegular:0);
        total=total+(rixMalo!=null?rixMalo:0);
        return total;
    }

    public Long getRixPk() {
        return rixPk;
    }

    public void setRixPk(Long rixPk) {
        this.rixPk = rixPk;
    }

    public Integer getRixBueno() {
        return rixBueno;
    }

    public void setRixBueno(Integer rixBueno) {
        this.rixBueno = rixBueno;
    }

    public Integer getRixMalo() {
        return rixMalo;
    }

    public void setRixMalo(Integer rixMalo) {
        this.rixMalo = rixMalo;
    }

    public Integer getRixRegular() {
        return rixRegular;
    }

    public void setRixRegular(Integer rixRegular) {
        this.rixRegular = rixRegular;
    }

    public String getRixDescripcion() {
        return rixDescripcion;
    }

    public void setRixDescripcion(String rixDescripcion) {
        this.rixDescripcion = rixDescripcion;
    }

  
    public LocalDateTime getRixUltModFecha() {
        return rixUltModFecha;
    }

    public void setRixUltModFecha(LocalDateTime rixUltModFecha) {
        this.rixUltModFecha = rixUltModFecha;
    }

    public String getRixUltModUsuario() {
        return rixUltModUsuario;
    }

    public void setRixUltModUsuario(String rixUltModUsuario) {
        this.rixUltModUsuario = rixUltModUsuario;
    }

    public Integer getRixVersion() {
        return rixVersion;
    }

    public void setRixVersion(Integer rixVersion) {
        this.rixVersion = rixVersion;
    }
    
    public SgInmueblesSedes getRixInmuebleSede() {
        return rixInmuebleSede;
    }

    public void setRixInmuebleSede(SgInmueblesSedes rixInmuebleSede) {
        this.rixInmuebleSede = rixInmuebleSede;
    }

    public SgInfItemObraExterior getRixItemObraExterior() {
        return rixItemObraExterior;
    }

    public void setRixItemObraExterior(SgInfItemObraExterior rixItemObraExterior) {
        this.rixItemObraExterior = rixItemObraExterior;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rixPk != null ? rixPk.hashCode() : 0);
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
        final SgRelInmuebleItemObraExterior other = (SgRelInmuebleItemObraExterior) obj;
        if (!Objects.equals(this.rixPk, other.rixPk)) {
            return false;
        }
        if (!Objects.equals(this.rixInmuebleSede, other.rixInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.rixItemObraExterior, other.rixItemObraExterior)) {
            return false;
        }
        return true;
    }
	



    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleItemObraExterior[ rixPk=" + rixPk + " ]";
    }
    
}
