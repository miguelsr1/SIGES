/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class EntityPACDataProvider implements IDataProvider, Serializable {

    private CriteriaTO condicion;
    private String[] orderBy = null;
    private boolean[] ascending = null;
    private PACDelegate delegate;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    

    public EntityPACDataProvider() {
       
    }

    public EntityPACDataProvider(String className, CriteriaTO condicion) {
        this.condicion = condicion;
        this.orderBy = null;
        this.ascending = null;
        
    }

    public EntityPACDataProvider( PACDelegate delegate, String className, CriteriaTO condicion, String[] orderBy, boolean[] asc) {
        this.condicion = condicion;
        this.orderBy = orderBy;
        this.ascending = asc;
        this.delegate= delegate;
        
    }

    public List<Object> getBufferedData(Integer startRowI,
            Integer offsetI) {
        try {
            Long startRow = Long.valueOf(startRowI);
            Long offset = Long.valueOf(offsetI);
            return delegate.filtrarPAC( condicion, startRow,  startRow + offset, orderBy, ascending);
        } catch (Exception ex) {            
             return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            EntityManagementDelegate emd = new EntityManagementDelegate();
             Long resultado = emd.getCountsByCriteria(PAC.class.getName(), null, null, condicion);
            return new Integer(resultado.toString());
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
