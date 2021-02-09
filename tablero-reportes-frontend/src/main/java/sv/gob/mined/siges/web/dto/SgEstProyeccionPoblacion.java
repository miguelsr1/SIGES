/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "proPk", scope = SgEstProyeccionPoblacion.class)
public class SgEstProyeccionPoblacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long proPk;

    private Integer proEdad;
    
    private Integer proAnio;
    
    private String proSexoNom;
    
    private Integer proPoblacion;

    public Long getProPk() {
        return proPk;
    }

    public void setProPk(Long proPk) {
        this.proPk = proPk;
    }

    public Integer getProEdad() {
        return proEdad;
    }

    public void setProEdad(Integer proEdad) {
        this.proEdad = proEdad;
    }

    public Integer getProAnio() {
        return proAnio;
    }

    public void setProAnio(Integer proAnio) {
        this.proAnio = proAnio;
    }

    public String getProSexoNom() {
        return proSexoNom;
    }

    public void setProSexoNom(String proSexoNom) {
        this.proSexoNom = proSexoNom;
    }

    public Integer getProPoblacion() {
        return proPoblacion;
    }

    public void setProPoblacion(Integer proPoblacion) {
        this.proPoblacion = proPoblacion;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.proPk);
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
        final SgEstProyeccionPoblacion other = (SgEstProyeccionPoblacion) obj;
        if (!Objects.equals(this.proPk, other.proPk)) {
            return false;
        }
        return true;
    }

}
