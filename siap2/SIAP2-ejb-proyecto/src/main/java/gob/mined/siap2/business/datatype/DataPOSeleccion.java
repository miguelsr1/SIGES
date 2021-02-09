/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.POLinea;
import java.util.List;

/**
 * Esta clase corresponde a un tipo de dato para la seleccion de un PO
 * @author Sofis Solutions
 */
public class DataPOSeleccion {
    private Integer idPO;
    private Integer anio;
    private String unidadTecnica;
    private List<POLinea> lineas;

    public Integer getIdPO() {
        return idPO;
    }

    public void setIdPO(Integer idPO) {
        this.idPO = idPO;
    }

  
    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(String unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public List<POLinea> getLineas() {
        return lineas;
    }

    public void setLineas(List<POLinea> lineas) {
        this.lineas = lineas;
    }


    
    
    
    
}
