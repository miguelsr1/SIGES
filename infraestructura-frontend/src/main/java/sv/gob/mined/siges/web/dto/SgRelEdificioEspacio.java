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
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reePk", scope = SgRelEdificioEspacio.class)
public class SgRelEdificioEspacio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long reePk;

    private SgEdificio reeEdificio;    
    
    private SgEspacioComun reeEspacioComun;

    private LocalDateTime reeUltModFecha;

    private String reeUltModUsuario;

    private Integer reeVersion;
    
    private Integer reeBueno;    

    private Integer reeRegular;

    private Integer reeMalo;

    private String reeDescripcion;
    
    
    public SgRelEdificioEspacio() {
     
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(reeBueno!=null?reeBueno:0);
        total=total+(reeRegular!=null?reeRegular:0);
        total=total+(reeMalo!=null?reeMalo:0);
        return total;
    }

    public Long getReePk() {
        return reePk;
    }

    public void setReePk(Long reePk) {
        this.reePk = reePk;
    }

    public SgEdificio getReeEdificio() {
        return reeEdificio;
    }

    public void setReeEdificio(SgEdificio reeEdificio) {
        this.reeEdificio = reeEdificio;
    }

    public SgEspacioComun getReeEspacioComun() {
        return reeEspacioComun;
    }

    public void setReeEspacioComun(SgEspacioComun reeEspacioComun) {
        this.reeEspacioComun = reeEspacioComun;
    }


    public LocalDateTime getReeUltModFecha() {
        return reeUltModFecha;
    }

    public void setReeUltModFecha(LocalDateTime reeUltModFecha) {
        this.reeUltModFecha = reeUltModFecha;
    }

    public String getReeUltModUsuario() {
        return reeUltModUsuario;
    }

    public void setReeUltModUsuario(String reeUltModUsuario) {
        this.reeUltModUsuario = reeUltModUsuario;
    }

    public Integer getReeVersion() {
        return reeVersion;
    }

    public void setReeVersion(Integer reeVersion) {
        this.reeVersion = reeVersion;
    }

    public Integer getReeBueno() {
        return reeBueno;
    }

    public void setReeBueno(Integer reeBueno) {
        this.reeBueno = reeBueno;
    }

    public Integer getReeRegular() {
        return reeRegular;
    }

    public void setReeRegular(Integer reeRegular) {
        this.reeRegular = reeRegular;
    }

    public Integer getReeMalo() {
        return reeMalo;
    }

    public void setReeMalo(Integer reeMalo) {
        this.reeMalo = reeMalo;
    }

    public String getReeDescripcion() {
        return reeDescripcion;
    }

    public void setReeDescripcion(String reeDescripcion) {
        this.reeDescripcion = reeDescripcion;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reePk != null ? reePk.hashCode() : 0);
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
        final SgRelEdificioEspacio other = (SgRelEdificioEspacio) obj;
        if (!Objects.equals(this.reePk, other.reePk)) {
            return false;
        }
        if (!Objects.equals(this.reeEdificio, other.reeEdificio)) {
            return false;
        }
        if (!Objects.equals(this.reeEspacioComun, other.reeEspacioComun)) {
            return false;
        }
        return true;
    }
	



    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelEdificioEspacio[ reePk=" + reePk + " ]";
    }
    
}
