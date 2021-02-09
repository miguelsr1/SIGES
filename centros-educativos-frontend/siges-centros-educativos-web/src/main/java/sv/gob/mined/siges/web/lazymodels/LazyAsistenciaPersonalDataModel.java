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
import sv.gob.mined.siges.web.dto.SgAsistenciaPersonal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciaPersonal;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsistenciaPersonalRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

public class LazyAsistenciaPersonalDataModel extends LazyDataModel<SgAsistenciaPersonal> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyAsistenciaPersonalDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private AsistenciaPersonalRestClient restClient;
    private FiltroAsistenciaPersonal filtro;

    public LazyAsistenciaPersonalDataModel(AsistenciaPersonalRestClient restClient, FiltroAsistenciaPersonal filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }

    @Override
    public SgAsistenciaPersonal getRowData(String rowKey) {
        try {
            return restClient.obtenerPorId(Long.valueOf(rowKey));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return null;
        }
    }

    @Override
    public Object getRowKey(SgAsistenciaPersonal c) {
        return c.getApePk();
    }

    @Override
    public List<SgAsistenciaPersonal> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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

    public void setFiltro(FiltroAsistenciaPersonal filtro) {
        this.filtro = filtro;
    }

}
