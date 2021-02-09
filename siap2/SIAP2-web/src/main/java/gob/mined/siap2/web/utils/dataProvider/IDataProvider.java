/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

/*
 * 
 * 
 */

import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public interface IDataProvider  {

    /**
     * Esta operación devuelve los datos a mostrar
     * @param startRow
     * @param offset
     * @return
     */
    List getBufferedData(Integer startRow, Integer offset);

    /**
     * Retorna la cantidad de datos que debe desplegar
     * @return
     */
    Integer getCountData();
    
    
    void setOrderBy (String[] orderBy, boolean[] asc);
    
}