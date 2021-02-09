package uy.com.sofis.pfea.utilidades;

import com.sofis.persistence.dao.reference.EntityReference;
import com.sofis.sofisform.to.CriteriaTO;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;
import java.util.logging.Logger;
import uy.com.sofis.pfea.sb.EntityManagementSB;

public class EntityReferenceDataProvider implements IDataProvider, Serializable {

    private static final Logger logger = Logger.getLogger(EntityReferenceDataProvider.class.getName());

    private String[] propiedades;
    private String className;
    private CriteriaTO condicion;
    private String[] orderBy = null;
    private boolean[] ascending = null;

    private EntityManagementSB entityManagementSB;

    public EntityReferenceDataProvider(EntityManagementSB entityManagementSB) {
        this.entityManagementSB = entityManagementSB;
    }

    public EntityReferenceDataProvider(EntityManagementSB entityManagementSB, String[] propiedades, String className, CriteriaTO condicion) {
        this.entityManagementSB = entityManagementSB;
        this.propiedades = propiedades;
        this.className = className;
        this.condicion = condicion;
        this.orderBy = null;
        this.ascending = null;

    }

    public EntityReferenceDataProvider(EntityManagementSB entityManagementSB, String[] propiedades, String className,
            CriteriaTO condicion, String[] orderBy, boolean[] asc) {
        this.entityManagementSB = entityManagementSB;
        this.propiedades = propiedades;
        this.className = className;
        this.condicion = condicion;
        this.orderBy = orderBy;
        this.ascending = asc;

    }

    public List<EntityReference<Integer>> getBufferedData(Integer startRow,
            Integer offset) {
        try {
            List<EntityReference<Integer>> resultado = entityManagementSB.getEntitiesReferenceByCriteria(
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
            Long resultado = entityManagementSB.getCountsByCriteria(className, null, null, condicion);
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
