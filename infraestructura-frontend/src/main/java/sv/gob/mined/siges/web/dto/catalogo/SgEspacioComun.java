/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ecoPk", scope = SgEspacioComun.class)
public class SgEspacioComun implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ecoPk;

    private String ecoCodigo;

    private String ecoNombre;

    private Boolean ecoHabilitado;   
    
    private Boolean ecoPermiteIngresarDescripcion;

    private Boolean ecoAplicaTerreno;
    
    private Boolean ecoAplicaEdificio;    
   
    private Boolean ecoAplicaAula;

    private String ecoDescripcion;

    private LocalDateTime ecoUltModFecha;

    private String ecoUltModUsuario;

    private Integer ecoVersion;
    
    private Integer ecoOrden;
    
    
    public SgEspacioComun() {
        this.ecoHabilitado = Boolean.TRUE;
    }

    public Long getEcoPk() {
        return ecoPk;
    }

    public void setEcoPk(Long ecoPk) {
        this.ecoPk = ecoPk;
    }

    public String getEcoCodigo() {
        return ecoCodigo;
    }

    public void setEcoCodigo(String ecoCodigo) {
        this.ecoCodigo = ecoCodigo;
    }

    public String getEcoNombre() {
        return ecoNombre;
    }

    public void setEcoNombre(String ecoNombre) {
        this.ecoNombre = ecoNombre;
    }

    public Boolean getEcoPermiteIngresarDescripcion() {
        return ecoPermiteIngresarDescripcion;
    }

    public void setEcoPermiteIngresarDescripcion(Boolean ecoPermiteIngresarDescripcion) {
        this.ecoPermiteIngresarDescripcion = ecoPermiteIngresarDescripcion;
    }

    public LocalDateTime getEcoUltModFecha() {
        return ecoUltModFecha;
    }

    public void setEcoUltModFecha(LocalDateTime ecoUltModFecha) {
        this.ecoUltModFecha = ecoUltModFecha;
    }

    public String getEcoUltModUsuario() {
        return ecoUltModUsuario;
    }

    public void setEcoUltModUsuario(String ecoUltModUsuario) {
        this.ecoUltModUsuario = ecoUltModUsuario;
    }

    public Integer getEcoVersion() {
        return ecoVersion;
    }

    public void setEcoVersion(Integer ecoVersion) {
        this.ecoVersion = ecoVersion;
    }

    
     public Boolean getEcoHabilitado() {
        return ecoHabilitado;
    }

    public void setEcoHabilitado(Boolean ecoHabilitado) {
        this.ecoHabilitado = ecoHabilitado;
    }

    public Boolean getEcoAplicaTerreno() {
        return ecoAplicaTerreno;
    }

    public void setEcoAplicaTerreno(Boolean ecoAplicaTerreno) {
        this.ecoAplicaTerreno = ecoAplicaTerreno;
    }

    public Boolean getEcoAplicaEdificio() {
        return ecoAplicaEdificio;
    }

    public void setEcoAplicaEdificio(Boolean ecoAplicaEdificio) {
        this.ecoAplicaEdificio = ecoAplicaEdificio;
    }

    public Boolean getEcoAplicaAula() {
        return ecoAplicaAula;
    }

    public void setEcoAplicaAula(Boolean ecoAplicaAula) {
        this.ecoAplicaAula = ecoAplicaAula;
    }

    public String getEcoDescripcion() {
        return ecoDescripcion;
    }

    public void setEcoDescripcion(String ecoDescripcion) {
        this.ecoDescripcion = ecoDescripcion;
    }

    public Integer getEcoOrden() {
        return ecoOrden;
    }

    public void setEcoOrden(Integer ecoOrden) {
        this.ecoOrden = ecoOrden;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecoPk != null ? ecoPk.hashCode() : 0);
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
        final SgEspacioComun other = (SgEspacioComun) obj;
        if (!Objects.equals(this.ecoPk, other.ecoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEspacioComun[ ecoPk=" + ecoPk + " ]";
    }
    
   
    
}
