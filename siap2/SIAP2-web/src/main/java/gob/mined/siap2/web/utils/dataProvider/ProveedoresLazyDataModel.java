/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils.dataProvider;

import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;

/**
 *
 * @author Sofis Solutions
 */
public class ProveedoresLazyDataModel extends GeneralLazyDataModel {

    public ProveedoresLazyDataModel(IDataProvider provider) {
        super(provider);
    }

    @Override
    public Object getRowKey(Object object) {
        if (object != null) {
            if (object instanceof Proveedor) {
                return ((Proveedor) object).getId();
            }
        }

        return null;
    }

    @Override
    public Object getRowData(String rowKey) {
        EntityManagementDelegate emd = new EntityManagementDelegate();
        return emd.getEntityById(Proveedor.class.getName(), Integer.valueOf(rowKey));
    }

}
