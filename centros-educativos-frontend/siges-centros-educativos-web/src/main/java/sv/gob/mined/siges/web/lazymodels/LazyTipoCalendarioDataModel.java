/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.lazymodels;

import java.io.Serializable;
import org.primefaces.model.LazyDataModel;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;

/**
 *
 * @author Sofis Solutions
 */
public class LazyTipoCalendarioDataModel extends LazyDataModel<SgTipoCalendario> implements Serializable {

//    private static final Logger LOGGER = Logger.getLogger(LazyTipoCalendarioDataModel.class.getName());
//    private static final long serialVersionUID = 1L;
//    private TipoCalendarioRestClient restClient;
//    private FiltroCodiguera filtro;
//
//    public LazyTipoCalendarioDataModel(TipoCalendarioRestClient restClient, FiltroCodiguera filtro, Long rowCount, Integer pageSize) {
//        this.restClient = restClient;
//        this.filtro = filtro;
//        this.setRowCount(rowCount.intValue());
//        this.setPageSize(pageSize);
//    }
//
//    @Override
//    public SgTipoCalendario getRowData(String rowKey) {
//        try {
//            return restClient.obtenerPorId(Long.valueOf(rowKey));
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//            return null;
//        }
//    }
//
//    @Override
//    public Object getRowKey(SgTipoCalendario c) {
//        return c.getTcaPk();
//    }
//
//    @Override
//    public List<SgTipoCalendario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//        try {
//            filtro.setFirst(new Long(first));
//            filtro.setMaxResults(new Long(pageSize));
//            if (sortField != null) {
//                filtro.setOrderBy(new String[]{sortField});
//                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
//            }
//            return restClient.buscar(filtro);
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//            return new ArrayList<>();
//        }
//    }
//
//    public void setFiltro(FiltroCodiguera filtro) {
//        this.filtro = filtro;
//    }
}
