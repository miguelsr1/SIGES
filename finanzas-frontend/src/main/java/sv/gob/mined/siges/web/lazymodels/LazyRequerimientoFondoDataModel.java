/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.lazymodels;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
public class LazyRequerimientoFondoDataModel extends LazyDataModel<SgRequerimientoFondo> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyRequerimientoFondoDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private RequerimientoFondoRestClient restClient;
    private FiltroRequerimientosFondo filtro;
    private Double porcentaje;

    public LazyRequerimientoFondoDataModel(RequerimientoFondoRestClient restClient, FiltroRequerimientosFondo filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }
    
    public LazyRequerimientoFondoDataModel(RequerimientoFondoRestClient restClient, FiltroRequerimientosFondo filtro, Long rowCount, Integer pageSize, Double porcentaje) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
        this.porcentaje=porcentaje;
    }

    @Override
    public SgRequerimientoFondo getRowData(String rowKey) {
        try {
            return null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return null;
        }
    }

    @Override
    public Object getRowKey(SgRequerimientoFondo c) {
        return c.getStrPk();
    }

    @Override
    public List<SgRequerimientoFondo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }
            List<SgRequerimientoFondo> list = restClient.buscar(filtro);
            
            if(porcentaje!=null){
                porcentaje = porcentaje / 100;
                list.stream().filter(r -> r.getStrImporteTotal()!=null).forEach(r->{
                    LOGGER.log(Level.SEVERE, "Monto " + r.getStrImporteTotal().multiply(new BigDecimal(porcentaje)));
                    r.setMontoDesembolso(r.getStrImporteTotal().multiply(new BigDecimal(porcentaje)));
                });
            }
            
            return list;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public void setFiltro(FiltroRequerimientosFondo filtro) {
        this.filtro = filtro;
    }

}
