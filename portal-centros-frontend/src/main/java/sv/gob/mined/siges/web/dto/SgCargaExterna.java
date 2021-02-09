/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoNumericoValorEstadistica;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carPk", scope = SgCargaExterna.class)
public class SgCargaExterna implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long carPk;

    private Integer carAnio;

    private SgEstNombreExtraccion carNombre;
    
    private SgEstIndicador carIndicador;
    
    private EnumDesagregacion carDesagregacion; //opcional
    
    private EnumTipoNumericoValorEstadistica carTipoNumerico;
    
    private String carDescripcion;
       
    private LocalDateTime carUltmodFecha;

    private String carUltmodUsuario;

    private Integer carVersion;
    
    private String carExcelTmpPath;
    
 
    public Long getCarPk() {
        return carPk;
    }

    public void setCarPk(Long carPk) {
        this.carPk = carPk;
    }

    public Integer getCarAnio() {
        return carAnio;
    }

    public void setCarAnio(Integer carAnio) {
        this.carAnio = carAnio;
    }

    public LocalDateTime getCarUltmodFecha() {
        return carUltmodFecha;
    }

    public void setCarUltmodFecha(LocalDateTime carUltmodFecha) {
        this.carUltmodFecha = carUltmodFecha;
    }

    public String getCarUltmodUsuario() {
        return carUltmodUsuario;
    }

    public void setCarUltmodUsuario(String carUltmodUsuario) {
        this.carUltmodUsuario = carUltmodUsuario;
    }

    public Integer getCarVersion() {
        return carVersion;
    }

    public void setCarVersion(Integer carVersion) {
        this.carVersion = carVersion;
    }


    public SgEstNombreExtraccion getCarNombre() {
        return carNombre;
    }

    public void setCarNombre(SgEstNombreExtraccion carNombre) {
        this.carNombre = carNombre;
    }


    public String getCarDescripcion() {
        return carDescripcion;
    }

    public void setCarDescripcion(String carDescripcion) {
        this.carDescripcion = carDescripcion;
    }

    public SgEstIndicador getCarIndicador() {
        return carIndicador;
    }

    public void setCarIndicador(SgEstIndicador carIndicador) {
        this.carIndicador = carIndicador;
    }

    public EnumDesagregacion getCarDesagregacion() {
        return carDesagregacion;
    }

    public void setCarDesagregacion(EnumDesagregacion carDesagregacion) {
        this.carDesagregacion = carDesagregacion;
    }

    public String getCarExcelTmpPath() {
        return carExcelTmpPath;
    }

    public void setCarExcelTmpPath(String carExcelTmpPath) {
        this.carExcelTmpPath = carExcelTmpPath;
    }

    public EnumTipoNumericoValorEstadistica getCarTipoNumerico() {
        return carTipoNumerico;
    }

    public void setCarTipoNumerico(EnumTipoNumericoValorEstadistica carTipoNumerico) {
        this.carTipoNumerico = carTipoNumerico;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.carPk);
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
        final SgCargaExterna other = (SgCargaExterna) obj;
        if (!Objects.equals(this.carPk, other.carPk)) {
            return false;
        }
        return true;
    }

}
