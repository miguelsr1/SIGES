/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.lazymodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SistemaIntegradoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
public class LazySistemaIntegradoDataModel extends LazyDataModel<SgSistemaIntegrado> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazySistemaIntegradoDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private SistemaIntegradoRestClient restClient;
    private FiltroSistemaIntegrado filtro;

    public LazySistemaIntegradoDataModel(SistemaIntegradoRestClient restClient, FiltroSistemaIntegrado filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }

    @Override
    public SgSistemaIntegrado getRowData(String rowKey) {
        try {
            return null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return null;
        }
    }

    @Override
    public Object getRowKey(SgSistemaIntegrado c) {
        return c.getSinPk();
    }

    @Override
    public List<SgSistemaIntegrado> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }
            return restClient.buscar(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public void setFiltro(FiltroSistemaIntegrado filtro) {
        this.filtro = filtro;
    }

}
