/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "demPk", scope = SgDetalleMatricula.class)
public class SgDetalleMatricula implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long demPk;

    private Integer demCostoMatricula;
   
    private Integer demCantidadMensualidad;
    
    private Integer demCostoMensualidad;

    private LocalDateTime demUltModFecha;

    private String demUltModUsuario;

    private Integer demVersion;
    
    private SgSede demSede;

    private SgNivel demNivel;
 
    private SgAnioLectivo demAnioLectivo;
    
    
    public SgDetalleMatricula() {
     
    }

    public Long getDemPk() {
        return demPk;
    }

    public void setDemPk(Long demPk) {
        this.demPk = demPk;
    }

    public Integer getDemCostoMatricula() {
        return demCostoMatricula;
    }

    public void setDemCostoMatricula(Integer demCostoMatricula) {
        this.demCostoMatricula = demCostoMatricula;
    }

    public Integer getDemCantidadMensualidad() {
        return demCantidadMensualidad;
    }

    public void setDemCantidadMensualidad(Integer demCantidadMensualidad) {
        this.demCantidadMensualidad = demCantidadMensualidad;
    }


    public Integer getDemCostoMensualidad() {
        return demCostoMensualidad;
    }

    public void setDemCostoMensualidad(Integer demCostoMensualidad) {
        this.demCostoMensualidad = demCostoMensualidad;
    }

    public SgSede getDemSede() {
        return demSede;
    }

    public void setDemSede(SgSede demSede) {
        this.demSede = demSede;
    }

    public SgNivel getDemNivel() {
        return demNivel;
    }

    public void setDemNivel(SgNivel demNivel) {
        this.demNivel = demNivel;
    }

   

    public SgAnioLectivo getDemAnioLectivo() {
        return demAnioLectivo;
    }

    public void setDemAnioLectivo(SgAnioLectivo demAnioLectivo) {
        this.demAnioLectivo = demAnioLectivo;
    }

   

    public LocalDateTime getDemUltModFecha() {
        return demUltModFecha;
    }

    public void setDemUltModFecha(LocalDateTime demUltModFecha) {
        this.demUltModFecha = demUltModFecha;
    }

    public String getDemUltModUsuario() {
        return demUltModUsuario;
    }

    public void setDemUltModUsuario(String demUltModUsuario) {
        this.demUltModUsuario = demUltModUsuario;
    }

    public Integer getDemVersion() {
        return demVersion;
    }

    public void setDemVersion(Integer demVersion) {
        this.demVersion = demVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (demPk != null ? demPk.hashCode() : 0);
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
        final SgDetalleMatricula other = (SgDetalleMatricula) obj;
        if (!Objects.equals(this.demPk, other.demPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgDetalleMatricula[ demPk=" + demPk + " ]";
    }
    
}
