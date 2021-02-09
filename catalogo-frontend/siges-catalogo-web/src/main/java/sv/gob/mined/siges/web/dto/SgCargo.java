/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private List<SgCargoRoles> carCargoRoles;
    
    private Boolean carAplicaAcuerdo;
    
    private Boolean carAplicaContrato;
    
    private Boolean carAplicaOtros;

    public SgCargo() {
        carCargoRoles = new ArrayList<>();
    }

    public SgCargo(Long carPk) {
        this.carPk = carPk;
    }

    public List<SgCargoRoles> getCarCargoRoles() {
        return carCargoRoles;
    }

    public void setCarCargoRoles(List<SgCargoRoles> carCargoRoles) {
        this.carCargoRoles = carCargoRoles;
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

    public Boolean getCarAplicaAcuerdo() {
        return carAplicaAcuerdo;
    }

    public void setCarAplicaAcuerdo(Boolean carAplicaAcuerdo) {
        this.carAplicaAcuerdo = carAplicaAcuerdo;
    }

    public Boolean getCarAplicaContrato() {
        return carAplicaContrato;
    }

    public void setCarAplicaContrato(Boolean carAplicaContrato) {
        this.carAplicaContrato = carAplicaContrato;
    }

    public Boolean getCarAplicaOtros() {
        return carAplicaOtros;
    }

    public void setCarAplicaOtros(Boolean carAplicaOtros) {
        this.carAplicaOtros = carAplicaOtros;
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
