/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carPk", scope = SgLeySalario.class)
public class SgCargoRoles implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long carPk;

    private SgCargo carCargo;

    private SgRol carRol;

    private LocalDateTime carUltModFecha;  

    private String carUltModUsuario;
        
    private Integer carVersion;
    
    public Long getCarPk() {
        return carPk;
    }

    public void setCarPk(Long carPk) {
        this.carPk = carPk;
    }

    public SgCargo getCarCargo() {
        return carCargo;
    }

    public void setCarCargo(SgCargo carCargo) {
        this.carCargo = carCargo;
    }

    public SgRol getCarRol() {
        return carRol;
    }

    public void setCarRol(SgRol carRol) {
        this.carRol = carRol;
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
    
    
}
