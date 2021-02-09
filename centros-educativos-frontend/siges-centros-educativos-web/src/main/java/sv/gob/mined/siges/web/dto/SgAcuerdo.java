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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "acuPk", scope = SgAcuerdo.class)
public class SgAcuerdo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long acuPk;

    private String acuNombreAcuerdo;
    
    
    private String acuDescripcion;
    
   
    private EnumEstadoAcuerdo acuEstado;
    
   
    private LocalDate acuFecha;    
    
    private Boolean acuAplicaSistemasIntegrados;

    private LocalDateTime acuUltModFecha;

    private String acuUltModUsuario;

    private Integer acuVersion;
    
    private SgPropuestaPedagogica acuPropuestaPedagogica;
    
    public SgAcuerdo() {
        acuAplicaSistemasIntegrados=Boolean.FALSE;
    }

    public Long getAcuPk() {
        return acuPk;
    }

    public void setAcuPk(Long acuPk) {
        this.acuPk = acuPk;
    }

    public String getAcuNombreAcuerdo() {
        return acuNombreAcuerdo;
    }

    public void setAcuNombreAcuerdo(String acuNombreAcuerdo) {
        this.acuNombreAcuerdo = acuNombreAcuerdo;
    }

    public String getAcuDescripcion() {
        return acuDescripcion;
    }

    public void setAcuDescripcion(String acuDescripcion) {
        this.acuDescripcion = acuDescripcion;
    }

    public EnumEstadoAcuerdo getAcuEstado() {
        return acuEstado;
    }

    public void setAcuEstado(EnumEstadoAcuerdo acuEstado) {
        this.acuEstado = acuEstado;
    }

    public LocalDate getAcuFecha() {
        return acuFecha;
    }

    public void setAcuFecha(LocalDate acuFecha) {
        this.acuFecha = acuFecha;
    }

    public Boolean getAcuAplicaSistemasIntegrados() {
        return acuAplicaSistemasIntegrados;
    }

    public void setAcuAplicaSistemasIntegrados(Boolean acuAplicaSistemasIntegrados) {
        this.acuAplicaSistemasIntegrados = acuAplicaSistemasIntegrados;
    }

    public LocalDateTime getAcuUltModFecha() {
        return acuUltModFecha;
    }

    public void setAcuUltModFecha(LocalDateTime acuUltModFecha) {
        this.acuUltModFecha = acuUltModFecha;
    }

    public String getAcuUltModUsuario() {
        return acuUltModUsuario;
    }

    public void setAcuUltModUsuario(String acuUltModUsuario) {
        this.acuUltModUsuario = acuUltModUsuario;
    }

    public Integer getAcuVersion() {
        return acuVersion;
    }

    public void setAcuVersion(Integer acuVersion) {
        this.acuVersion = acuVersion;
    }

    public SgPropuestaPedagogica getAcuPropuestaPedagogica() {
        return acuPropuestaPedagogica;
    }

    public void setAcuPropuestaPedagogica(SgPropuestaPedagogica acuPropuestaPedagogica) {
        this.acuPropuestaPedagogica = acuPropuestaPedagogica;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acuPk != null ? acuPk.hashCode() : 0);
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
        final SgAcuerdo other = (SgAcuerdo) obj;
        if (!Objects.equals(this.acuPk, other.acuPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAcuerdo[ acuPk=" + acuPk + " ]";
    }
    
}
