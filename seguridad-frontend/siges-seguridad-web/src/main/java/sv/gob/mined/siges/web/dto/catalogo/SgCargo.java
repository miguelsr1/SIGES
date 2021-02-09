/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.dto.centros_educativos.SgDatoContratacion;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carPk", scope = SgCargo.class)
public class SgCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long carPk;
    
    private String carCodigo;
    
    private Boolean carHabilitado;
    
    private String carNombre;
    
    private String carNombreBusqueda;
    
    private LocalDateTime carUltModFecha;
    
    private String carUltModUsuario;
    
    private Integer carVersion;
    
    private List<SgDatoContratacion> carDatoContratacion;

    public SgCargo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.carNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.carNombre);
    }

    public SgCargo(Long carPk) {
        this.carPk = carPk;
    }

    public Long getCarPk() {
        return carPk;
    }

    public void setCarPk(Long carPk) {
        this.carPk = carPk;
    }

    public String getCarCodigo() {
        return carCodigo;
    }

    public void setCarCodigo(String carCodigo) {
        this.carCodigo = carCodigo;
    }

    public Boolean getCarHabilitado() {
        return carHabilitado;
    }

    public void setCarHabilitado(Boolean carHabilitado) {
        this.carHabilitado = carHabilitado;
    }

    public String getCarNombre() {
        return carNombre;
    }

    public void setCarNombre(String carNombre) {
        this.carNombre = carNombre;
    }

    public String getCarNombreBusqueda() {
        return carNombreBusqueda;
    }

    public void setCarNombreBusqueda(String carNombreBusqueda) {
        this.carNombreBusqueda = carNombreBusqueda;
    }

    public LocalDateTime getCarUltModFecha() {
        return carUltModFecha;
    }

    public void setCarUltModFecha(LocalDateTime carUltModFecha) {
        this.carUltModFecha = carUltModFecha;
    }

    public String getCarUltModUsuario() {
        return carUltModUsuario;
    }

    public void setCarUltModUsuario(String carUltModUsuario) {
        this.carUltModUsuario = carUltModUsuario;
    }

    public Integer getCarVersion() {
        return carVersion;
    }

    public void setCarVersion(Integer carVersion) {
        this.carVersion = carVersion;
    }

    public List<SgDatoContratacion> getCarDatoContratacion() {
        return carDatoContratacion;
    }

    public void setCarDatoContratacion(List<SgDatoContratacion> carDatoContratacion) {
        this.carDatoContratacion = carDatoContratacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carPk != null ? carPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCargo)) {
            return false;
        }
        SgCargo other = (SgCargo) object;
        if ((this.carPk == null && other.carPk != null) || (this.carPk != null && !this.carPk.equals(other.carPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCargo[ carPk=" + carPk + " ]";
    }
    
}
