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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rasPk", scope = SgRelAulaServicio.class)
public class SgRelAulaServicio implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rasPk;

    private SgAula rasAula;    

    private SgServicioBasico rasServicio;

    private LocalDateTime rasUltModFecha;

    private String rasUltModUsuario;

    private Integer rasVersion;
    
    
    public SgRelAulaServicio() {
    
    }

    public Long getRasPk() {
        return rasPk;
    }

    public void setRasPk(Long rasPk) {
        this.rasPk = rasPk;
    }

    public SgAula getRasAula() {
        return rasAula;
    }

    public void setRasAula(SgAula rasAula) {
        this.rasAula = rasAula;
    }

    public SgServicioBasico getRasServicio() {
        return rasServicio;
    }

    public void setRasServicio(SgServicioBasico rasServicio) {
        this.rasServicio = rasServicio;
    }
 

    public LocalDateTime getRasUltModFecha() {
        return rasUltModFecha;
    }

    public void setRasUltModFecha(LocalDateTime rasUltModFecha) {
        this.rasUltModFecha = rasUltModFecha;
    }

    public String getRasUltModUsuario() {
        return rasUltModUsuario;
    }

    public void setRasUltModUsuario(String rasUltModUsuario) {
        this.rasUltModUsuario = rasUltModUsuario;
    }

    public Integer getRasVersion() {
        return rasVersion;
    }

    public void setRasVersion(Integer rasVersion) {
        this.rasVersion = rasVersion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rasPk != null ? rasPk.hashCode() : 0);
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
        final SgRelAulaServicio other = (SgRelAulaServicio) obj;
        if (!Objects.equals(this.rasPk, other.rasPk)) {
            return false;
        }
        if (!Objects.equals(this.rasAula, other.rasAula)) {
            return false;
        }
        if (!Objects.equals(this.rasServicio, other.rasServicio)) {
            return false;
        }
        return true;
    }
	
	


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelAulaServicio[ rasPk=" + rasPk + " ]";
    }
    
}
