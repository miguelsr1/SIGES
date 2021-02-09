package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


public class LazyLoadingTopePresupuestal extends LazyDataModel<TopePresupuestal>{
    
    IDataProvider provider;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    public LazyLoadingTopePresupuestal(IDataProvider provider) {
        this.provider = provider;
        try {
            Integer dataSize = provider.getCountData();
            this.setRowCount(dataSize);
        } catch (Exception ex) {
            this.setRowCount(0);
        }
    }

    @Override
    public List<TopePresupuestal> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            if (!TextUtils.isEmpty(sortField)) {
                String[] orderBy = {sortField};
                boolean asendente = true;
                if (sortOrder != null && sortOrder.compareTo(sortOrder.ASCENDING) != 0) {
                    asendente = false;
                }

                boolean[] asc = {asendente};
                provider.setOrderBy(orderBy, asc);
            }

            Integer dataSize = provider.getCountData();
            this.setRowCount(dataSize);

            List<TopePresupuestal> l;
            if (dataSize >= (first + pageSize)) {
                //tendria que entrar casi siempre aca
                l= provider.getBufferedData(first, pageSize);
            } else if (first < dataSize) {
                //caso en el que llega al final de la tabla y no completa la pagina
                l= provider.getBufferedData(first, (dataSize % pageSize));
            } else {
                //fix para cuando se filtra en una pagina mayor a la cantidad de resultados
                l= provider.getBufferedData(0, dataSize);
            }

            return l;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    
    @Override
    public Object getRowKey(TopePresupuestal tope) {
        return tope != null ? tope.getId() : null;
    }

    @Override
    public TopePresupuestal getRowData(String rowKey) {
        EntityManagementDelegate emd = new EntityManagementDelegate();
        try {
            return (TopePresupuestal) emd.getEntityById(TopePresupuestal.class.getName(), Integer.valueOf(rowKey));
        } catch (GeneralException | NumberFormatException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
}
