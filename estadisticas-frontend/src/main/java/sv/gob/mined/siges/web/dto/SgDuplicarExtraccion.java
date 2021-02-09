package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;

/**
 *
 * @author fabricio
 */
public class SgDuplicarExtraccion implements Serializable {
    
    private Long extPk;
    private Integer nuevoAnio;
    private String nuevaDescripcion;
    private SgEstNombreExtraccion nuevoNombre;

    public Long getExtPk() {
        return extPk;
    }

    public void setExtPk(Long extPk) {
        this.extPk = extPk;
    }

    public Integer getNuevoAnio() {
        return nuevoAnio;
    }

    public void setNuevoAnio(Integer nuevoAnio) {
        this.nuevoAnio = nuevoAnio;
    }

    public String getNuevaDescripcion() {
        return nuevaDescripcion;
    }

    public void setNuevaDescripcion(String nuevaDescripcion) {
        this.nuevaDescripcion = nuevaDescripcion;
    }

    public SgEstNombreExtraccion getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(SgEstNombreExtraccion nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }
    
    
}
