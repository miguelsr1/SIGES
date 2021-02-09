/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.web.delegates.impl.POADelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class POADataProvider implements IDataProvider, Serializable {
    private POADelegate pOADelegate;
    private FiltroPOA filtro;
    private String[] orderBy = null;
    private boolean[] ascending = null;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    public POADataProvider(POADelegate delegate,FiltroPOA filtro, String[] orderBy,  boolean[] ascending) {
        this.pOADelegate = delegate;
        this.filtro = filtro;
        this.orderBy= orderBy;
        this.ascending= ascending;
    }
    
    
    public List<Object> getBufferedData(Integer startRow,Integer offset) {
        try {
            filtro.setFirst(new Long(startRow));
            filtro.setMaxResults(new Long(offset));
            filtro.setOrderBy(orderBy);
            filtro.setAscending(ascending);
            List l = pOADelegate.obtenerPorFiltro(filtro);
            return l;
            
        } catch (Exception ex) {            
             return new LinkedList();
        }
    }
    
    @Override
    public Integer getCountData() {
        try {
            Long resultado = pOADelegate.obtenerTotalPorFiltro(filtro);
            return resultado.intValue();
        } catch (Exception ex) {
            
            return 0;
        }
    }
    

    @Override
    public void setOrderBy(String[] orderBy, boolean[] asc) {
        this.orderBy = orderBy;
        this.ascending = asc;
    }
    
}
