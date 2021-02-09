/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.math.BigDecimal;
import java.util.List;

/**
 * Esta clase corresponde a un tipo de datos que incluyen datos para un grafico de torta.
 * @note Datos para un grafico de torta.
 * @author Sofis Solutions
 */
public class DataProyectoGraficoTorta {
    String name;
    List<DataProyectoGraficoTorta> children;
    BigDecimal size;

    /**
     * Nombre de la fuente
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Nombre de la fuente
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Datos asociados a la fuente
     * @return 
     */
    public List<DataProyectoGraficoTorta> getChildren() {
        return children;
    }

    /**
     * Datos asociados a la fuente
     * @param children 
     */
    public void setChildren(List<DataProyectoGraficoTorta> children) {
        this.children = children;
    }

    /**
     * Tamanño 
     * @return 
     */
    public BigDecimal getSize() {
        return size;
    }

    /**
     * Tamaño
     * @param size 
     */
    public void setSize(BigDecimal size) {
        this.size = size;
    }
    
    
    
}
