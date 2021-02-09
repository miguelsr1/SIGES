/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.utils.generalutils.TextUtils;
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
public class GeneralLazyDataModel extends LazyDataModel<Object> {

    IDataProvider provider;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    public GeneralLazyDataModel(IDataProvider provider) {
        this.provider = provider;
        try {
        Integer dataSize = provider.getCountData();
        this.setRowCount(dataSize);
        } catch (Exception ex) {
            this.setRowCount(0);
        }
    }

    @Override
    public List<Object> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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

            List<Object> l;
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

}
