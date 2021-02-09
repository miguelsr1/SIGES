/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;


@Named
public class EntityReferenceDataProvider implements IDataProvider, Serializable {

    private String[] propiedades;
    private String className;
    private CriteriaTO condicion;
    private String[] orderBy = null;
    private boolean[] ascending = null;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    

    public EntityReferenceDataProvider() {
       
    }

    public EntityReferenceDataProvider(String[] propiedades, String className, CriteriaTO condicion) {
        this.propiedades = propiedades;
        this.className = className;
        this.condicion = condicion;
        this.orderBy = null;
        this.ascending = null;
        
    }

    public EntityReferenceDataProvider(String[] propiedades, String className,
            CriteriaTO condicion, String[] orderBy, boolean[] asc) {
        this.propiedades = propiedades;
        this.className = className;
        this.condicion = condicion;
        this.orderBy = orderBy;
        this.ascending = asc;
        
    }

    public List<EntityReference<Integer>> getBufferedData(Integer startRow,
            Integer offset) {
        try {            
            EntityManagementDelegate emd = new EntityManagementDelegate();
             List<EntityReference<Integer>> resultado = emd.getEntitiesReferenceByCriteria(
                    className, condicion, startRow, startRow + offset, propiedades,
                    orderBy, ascending);
             return resultado;
        } catch (Exception ex) {
            
             return new LinkedList<EntityReference<Integer>>();
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
