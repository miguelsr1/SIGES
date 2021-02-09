/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgInfObraExterior;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rixPk", scope = SgAuxiliarObraExterior.class)
public class SgAuxiliarObraExterior implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long rixPk;
    
       
    private List<SgRelInmuebleItemObraExterior> rixItemObraExterior;
    
    private List<SgRelInmuebleItemObraExterior> rixItemObraExteriorSeleccionado;
    
    private SgInfObraExterior rixObra;
    
    
    public SgAuxiliarObraExterior() {
    }

    public Long getRixPk() {
        return rixPk;
    }

    public void setRixPk(Long rixPk) {
        this.rixPk = rixPk;
    }

    public List<SgRelInmuebleItemObraExterior> getRixItemObraExterior() {
        return rixItemObraExterior;
    }

    public void setRixItemObraExterior(List<SgRelInmuebleItemObraExterior> rixItemObraExterior) {
        this.rixItemObraExterior = rixItemObraExterior;
    }

    public List<SgRelInmuebleItemObraExterior> getRixItemObraExteriorSeleccionado() {
        return rixItemObraExteriorSeleccionado;
    }

    public void setRixItemObraExteriorSeleccionado(List<SgRelInmuebleItemObraExterior> rixItemObraExteriorSeleccionado) {
        this.rixItemObraExteriorSeleccionado = rixItemObraExteriorSeleccionado;
    }

    public SgInfObraExterior getRixObra() {
        return rixObra;
    }

    public void setRixObra(SgInfObraExterior rixObra) {
        this.rixObra = rixObra;
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
        final SgAuxiliarObraExterior other = (SgAuxiliarObraExterior) obj;
        if (!Objects.equals(this.rixPk, other.rixPk)) {
            return false;
        }
        if (!Objects.equals(this.rixObra, other.rixObra)) {
            return false;
        }
        return true;
    }
	


    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleItemObraExterior[ rixPk=" + rixPk + " ]";
    }
    
}
