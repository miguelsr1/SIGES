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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "raePk", scope = SgRelAulaEspacio.class)
public class SgRelAulaEspacio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long raePk;

    private SgAula raeAula;    

    private SgEspacioComun raeEspacioComun;

    private LocalDateTime raeUltModFecha;

    private String raeUltModUsuario;

    private Integer raeVersion;
    
    private Integer raeBueno;

    private Integer raeRegular;

    private Integer raeMalo;

    private String raeDescripcion;
    
    
    public SgRelAulaEspacio() {
    }
    
    @JsonIgnore
    public Integer getTotal(){
        Integer total=0;
        total=total+(raeBueno!=null?raeBueno:0);
        total=total+(raeRegular!=null?raeRegular:0);
        total=total+(raeMalo!=null?raeMalo:0);
        return total;
    }

    public Long getRaePk() {
        return raePk;
    }

    public void setRaePk(Long raePk) {
        this.raePk = raePk;
    }

   
    public LocalDateTime getRaeUltModFecha() {
        return raeUltModFecha;
    }

    public void setRaeUltModFecha(LocalDateTime raeUltModFecha) {
        this.raeUltModFecha = raeUltModFecha;
    }

    public String getRaeUltModUsuario() {
        return raeUltModUsuario;
    }

    public void setRaeUltModUsuario(String raeUltModUsuario) {
        this.raeUltModUsuario = raeUltModUsuario;
    }

    public Integer getRaeVersion() {
        return raeVersion;
    }

    public void setRaeVersion(Integer raeVersion) {
        this.raeVersion = raeVersion;
    }

    public SgAula getRaeAula() {
        return raeAula;
    }

    public void setRaeAula(SgAula raeAula) {
        this.raeAula = raeAula;
    }

    public SgEspacioComun getRaeEspacioComun() {
        return raeEspacioComun;
    }

    public void setRaeEspacioComun(SgEspacioComun raeEspacioComun) {
        this.raeEspacioComun = raeEspacioComun;
    }


    public Integer getRaeBueno() {
        return raeBueno;
    }

    public void setRaeBueno(Integer raeBueno) {
        this.raeBueno = raeBueno;
    }

    public Integer getRaeRegular() {
        return raeRegular;
    }

    public void setRaeRegular(Integer raeRegular) {
        this.raeRegular = raeRegular;
    }

    public Integer getRaeMalo() {
        return raeMalo;
    }

    public void setRaeMalo(Integer raeMalo) {
        this.raeMalo = raeMalo;
    }

    public String getRaeDescripcion() {
        return raeDescripcion;
    }

    public void setRaeDescripcion(String raeDescripcion) {
        this.raeDescripcion = raeDescripcion;
    }

 


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (raePk != null ? raePk.hashCode() : 0);
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
        final SgRelAulaEspacio other = (SgRelAulaEspacio) obj;
        if (!Objects.equals(this.raePk, other.raePk)) {
            return false;
        }
        if (!Objects.equals(this.raeAula, other.raeAula)) {
            return false;
        }
        if (!Objects.equals(this.raeEspacioComun, other.raeEspacioComun)) {
            return false;
        }
        return true;
    }

 
	


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelAulaEspacio[ raePk=" + raePk + " ]";
    }
    
}
