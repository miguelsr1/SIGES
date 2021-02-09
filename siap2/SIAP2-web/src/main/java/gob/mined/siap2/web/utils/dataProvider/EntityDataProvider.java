/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class EntityDataProvider implements IDataProvider, Serializable {

    private String className;
    private CriteriaTO condicion;
    private String[] orderBy = null;
    private boolean[] ascending = null;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    

    public EntityDataProvider() {
       
    }

    public EntityDataProvider(String className, CriteriaTO condicion) {
        this.className = className;
        this.condicion = condicion;
        this.orderBy = null;
        this.ascending = null;
        
    }

    public EntityDataProvider(String className,
            CriteriaTO condicion, String[] orderBy, boolean[] asc) {
        this.className = className;
        this.condicion = condicion;
        this.orderBy = orderBy;
        this.ascending = asc;
        
    }

    public List<Object> getBufferedData(Integer startRow,
            Integer offset) {
        try {
            EntityManagementDelegate emd = new EntityManagementDelegate();
            return emd.getEntitiesByCriteria(className, condicion, startRow,  startRow + offset, orderBy, ascending);
        } catch (Exception ex) {            
             return new LinkedList();
        }
    }

    @Override
    public Integer getCountData() {
        try {
            EntityManagementDelegate emd = new EntityManagementDelegate();
             Long resultado = emd.getCountsByCriteria(className, null, null, condicion);
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
