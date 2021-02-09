/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "resPk", scope = SgRelEdificioServicio.class)
public class SgRelEdificioServicio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long resPk;

    private SgEdificio resEdificio;
    
    private SgServicioBasico resServicio;

    private LocalDateTime resUltModFecha;

    private String resUltModUsuario;

    private Integer resVersion;
    
    
    public SgRelEdificioServicio() {
    }

    public Long getResPk() {
        return resPk;
    }

    public void setResPk(Long resPk) {
        this.resPk = resPk;
    }


    public LocalDateTime getResUltModFecha() {
        return resUltModFecha;
    }

    public void setResUltModFecha(LocalDateTime resUltModFecha) {
        this.resUltModFecha = resUltModFecha;
    }

    public String getResUltModUsuario() {
        return resUltModUsuario;
    }

    public void setResUltModUsuario(String resUltModUsuario) {
        this.resUltModUsuario = resUltModUsuario;
    }

    public Integer getResVersion() {
        return resVersion;
    }

    public void setResVersion(Integer resVersion) {
        this.resVersion = resVersion;
    }

    public SgEdificio getResEdificio() {
        return resEdificio;
    }

    public void setResEdificio(SgEdificio resEdificio) {
        this.resEdificio = resEdificio;
    }

    public SgServicioBasico getResServicio() {
        return resServicio;
    }

    public void setResServicio(SgServicioBasico resServicio) {
        this.resServicio = resServicio;
    }

   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resPk != null ? resPk.hashCode() : 0);
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
        final SgRelEdificioServicio other = (SgRelEdificioServicio) obj;
        if (!Objects.equals(this.resPk, other.resPk)) {
            return false;
        }
        if (!Objects.equals(this.resEdificio, other.resEdificio)) {
            return false;
        }
        if (!Objects.equals(this.resServicio, other.resServicio)) {
            return false;
        }
        return true;
    }
	



    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelEdificioServicio[ resPk=" + resPk + " ]";
    }
    
}
