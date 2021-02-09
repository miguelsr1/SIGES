/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.tipos;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroMA implements Serializable {
    private Integer anio;

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
   
}
