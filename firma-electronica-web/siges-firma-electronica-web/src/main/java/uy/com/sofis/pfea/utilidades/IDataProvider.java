package uy.com.sofis.pfea.utilidades;

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