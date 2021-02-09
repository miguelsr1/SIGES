/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoMejora;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mejPk", scope = SgMejoras.class)
public class SgMejoras implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mejPk;

    private LocalDate mejFecha;
 
    private String mejDescripcion;    

    private BigDecimal mejCosto;
    
    private LocalDateTime mejUltModFecha;

    private String mejUltModUsuario;

    private Integer mejVersion;
    
    private SgInmueblesSedes mejInmueble;        

    private SgEdificio mejEdificio;    

    private SgTipoMejora mejTipoMejora;
    
     private SgFuenteFinanciamiento mejFuenteFinanciamiento;
    
    public SgMejoras() {

    }

    public Long getMejPk() {
        return mejPk;
    }

    public void setMejPk(Long mejPk) {
        this.mejPk = mejPk;
    }

   

    public LocalDateTime getMejUltModFecha() {
        return mejUltModFecha;
    }

    public void setMejUltModFecha(LocalDateTime mejUltModFecha) {
        this.mejUltModFecha = mejUltModFecha;
    }

    public String getMejUltModUsuario() {
        return mejUltModUsuario;
    }

    public void setMejUltModUsuario(String mejUltModUsuario) {
        this.mejUltModUsuario = mejUltModUsuario;
    }

    public Integer getMejVersion() {
        return mejVersion;
    }

    public void setMejVersion(Integer mejVersion) {
        this.mejVersion = mejVersion;
    }

    public LocalDate getMejFecha() {
        return mejFecha;
    }

    public void setMejFecha(LocalDate mejFecha) {
        this.mejFecha = mejFecha;
    }

    public String getMejDescripcion() {
        return mejDescripcion;
    }

    public void setMejDescripcion(String mejDescripcion) {
        this.mejDescripcion = mejDescripcion;
    }

    public BigDecimal getMejCosto() {
        return mejCosto;
    }

    public void setMejCosto(BigDecimal mejCosto) {
        this.mejCosto = mejCosto;
    }

    public SgInmueblesSedes getMejInmueble() {
        return mejInmueble;
    }

    public void setMejInmueble(SgInmueblesSedes mejInmueble) {
        this.mejInmueble = mejInmueble;
    }

    public SgEdificio getMejEdificio() {
        return mejEdificio;
    }

    public void setMejEdificio(SgEdificio mejEdificio) {
        this.mejEdificio = mejEdificio;
    }

    public SgTipoMejora getMejTipoMejora() {
        return mejTipoMejora;
    }

    public void setMejTipoMejora(SgTipoMejora mejTipoMejora) {
        this.mejTipoMejora = mejTipoMejora;
    }

    public SgFuenteFinanciamiento getMejFuenteFinanciamiento() {
        return mejFuenteFinanciamiento;
    }

    public void setMejFuenteFinanciamiento(SgFuenteFinanciamiento mejFuenteFinanciamiento) {
        this.mejFuenteFinanciamiento = mejFuenteFinanciamiento;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mejPk != null ? mejPk.hashCode() : 0);
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
        final SgMejoras other = (SgMejoras) obj;
        if (!Objects.equals(this.mejPk, other.mejPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMejoras[ mejPk=" + mejPk + " ]";
    }
    
}
