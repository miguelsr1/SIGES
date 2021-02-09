/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsOperacion;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Sofis Solutions
 */
public class SsOperacionPaginator extends LazyDataModel<SsOperacion> implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private static String className = SsOperacion.class.getName();

    private CriteriaTO condicion;
    private String[] orderBy = null;
    private boolean[] ascending = null;

    @Override
    public List<SsOperacion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        this.setRowCount(getCountData());
        return getBufferedData(first, pageSize);
    }


    
    public SsOperacionPaginator() {

    }

    public SsOperacionPaginator(CriteriaTO condicion) {
        this.condicion = condicion;
        this.orderBy = null;
        this.ascending = null;

    }

    public SsOperacionPaginator(CriteriaTO condicion, String[] orderBy, boolean[] asc) {
        this.condicion = condicion;
        this.orderBy = orderBy;
        this.ascending = asc;

    }

    public List getBufferedData(Integer startRow,
            Integer offset) {
        try {
            EntityManagementDelegate emd = new EntityManagementDelegate();
                
            List resultado = emd.getEntitiesByCriteria(className, condicion, startRow, startRow + offset, orderBy, ascending);
            return resultado;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return new LinkedList();
        }
    }

    public Integer getCountData() {
        try {

            EntityManagementDelegate emd = new EntityManagementDelegate();
            Long resultado = emd.getCountsByCriteria(className, null, null, condicion);
                        
            return new Integer(resultado.toString());            
        } catch (Exception ex) {
            return 500;
        }
    }

}
