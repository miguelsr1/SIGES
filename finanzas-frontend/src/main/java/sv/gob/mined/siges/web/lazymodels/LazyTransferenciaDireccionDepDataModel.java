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
import sv.gob.mined.siges.web.dto.SgTransferenciaDireccionDep;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaDireccionDep;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaDireccionDepRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
public class LazyTransferenciaDireccionDepDataModel extends LazyDataModel<SgTransferenciaDireccionDep> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyTransferenciaDireccionDepDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private TransferenciaDireccionDepRestClient restClient;
    private RequerimientoFondoRestClient solTraClient;
    private FiltroTransferenciaDireccionDep filtro;

    public LazyTransferenciaDireccionDepDataModel(TransferenciaDireccionDepRestClient restClient, FiltroTransferenciaDireccionDep filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }
    
    public LazyTransferenciaDireccionDepDataModel(RequerimientoFondoRestClient solTraClient,TransferenciaDireccionDepRestClient restClient, FiltroTransferenciaDireccionDep filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.solTraClient = solTraClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }

    @Override
    public SgTransferenciaDireccionDep getRowData(String rowKey) {
        try {
            return restClient.obtenerPorId(Long.valueOf(rowKey));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return null;
        }
    }

    @Override
    public Object getRowKey(SgTransferenciaDireccionDep c) {
        return c.getTddPk();
    }

    @Override
    public List<SgTransferenciaDireccionDep> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }
            List<SgTransferenciaDireccionDep> listado = restClient.buscar(filtro);
            
            listado.forEach(l->{
                l.setTddMontoRequerido(obtenerMonto(l.getTddPk()));
            });
            
            return listado;
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public void setFiltro(FiltroTransferenciaDireccionDep filtro) {
        this.filtro = filtro;
    }
    
    public BigDecimal obtenerMonto(Long tddId){
        BigDecimal montoRequerido= BigDecimal.ZERO;
        BigDecimal monto= BigDecimal.ZERO;
        try {
            FiltroRequerimientosFondo filtro = new FiltroRequerimientosFondo();
            //filtro.setTransferenciaDDFk(tddId);
            filtro.setOrderBy(new String[]{"strPk"});
            filtro.setAscending(new boolean[]{false});

            List<SgRequerimientoFondo> sols = solTraClient.buscar(filtro);
            
            for(SgRequerimientoFondo sol : sols){
                monto=sol.getReqsFondo().stream().map(r-> r.getRfcMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                montoRequerido=montoRequerido.add(monto);
            }
        
           return montoRequerido; 
           
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return montoRequerido;
    }

}
