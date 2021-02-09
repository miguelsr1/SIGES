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
import sv.gob.mined.siges.web.dto.DetalleDescargo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDescargosDetalle;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
public class LazyDetalleDescargoDataModel extends LazyDataModel<DetalleDescargo> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyDetalleDescargoDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private DescargosRestClient restClient;
    private FiltroDescargosDetalle filtro;
    
    public LazyDetalleDescargoDataModel(DescargosRestClient restClient, FiltroDescargosDetalle filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }

    @Override
    public DetalleDescargo getRowData(String rowKey) {
        try {
            return restClient.obtenerDetallePorIdDTO(Long.valueOf(rowKey), Boolean.FALSE);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return null;
        }
    }

    @Override
    public Object getRowKey(DetalleDescargo c) {
        return c.getId();
    }

    @Override
    public List<DetalleDescargo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }  
            return restClient.buscarDetalleDTO(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public void setFiltro(FiltroDescargosDetalle filtro) {
        this.filtro = filtro;
    }
}
