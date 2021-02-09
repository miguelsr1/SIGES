/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.lazymodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgConsultaCalificacionesPendientesSedesEnNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionesPendientesSedeEnNivel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
public class LazyConsultaCalificacionesPendientesSedeEnNivelDataModel extends LazyDataModel<SgConsultaCalificacionesPendientesSedesEnNivel> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyConsultaCalificacionesPendientesSedeEnNivelDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private CalificacionRestClient calificacionClient;
    private FiltroCalificacionesPendientesSedeEnNivel filtro;

    private List<SgConsultaCalificacionesPendientesSedesEnNivel> estDesagregada;
    private List<SgConsultaCalificacionesPendientesSedesEnNivel> estSinDesagregacion;
    private HashMap<Object, HashMap<String, Object>> desagregacionesPendientes;
    private HashMap<Object, HashMap<String, Object>> desagregacionesPosibles;
    private HashMap<Object, HashMap<String, Object>> desagregacionesRealizadas;

    public LazyConsultaCalificacionesPendientesSedeEnNivelDataModel(CalificacionRestClient calificacionClient, FiltroCalificacionesPendientesSedeEnNivel filtro, Long rowCount, Integer pageSize) {
        this.calificacionClient = calificacionClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }

    @Override
    public SgConsultaCalificacionesPendientesSedesEnNivel getRowData(String rowKey) {
        try {
            return null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return null;
        }
    }

    @Override
    public Long getRowKey(SgConsultaCalificacionesPendientesSedesEnNivel o) {
        return null;
    }

    @Override
    public List<SgConsultaCalificacionesPendientesSedesEnNivel> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }
            estDesagregada = calificacionClient.obtenerCalificacionesPendientesPorSedeEnNivel(filtro);
            convertirListaACrossTable();
            return estSinDesagregacion;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public Object obtenerPendientesDesagregacion(Object labelPrincipal, String labelDesagregacion) {
        if (this.desagregacionesPendientes.containsKey(labelPrincipal) && this.desagregacionesPendientes.get(labelPrincipal).containsKey(labelDesagregacion)) {
            return this.desagregacionesPendientes.get(labelPrincipal).get(labelDesagregacion);
        }
        return 0L;
    }
    
    public Object obtenerRealizadasDesagregacion(Object labelPrincipal, String labelDesagregacion) {
        if (this.desagregacionesRealizadas.containsKey(labelPrincipal) && this.desagregacionesRealizadas.get(labelPrincipal).containsKey(labelDesagregacion)) {
            return this.desagregacionesRealizadas.get(labelPrincipal).get(labelDesagregacion);
        }
        return 0L;
    }
    
    public Object obtenerPosiblesDesagregacion(Object labelPrincipal, String labelDesagregacion) {
        if (this.desagregacionesPosibles.containsKey(labelPrincipal) && this.desagregacionesPosibles.get(labelPrincipal).containsKey(labelDesagregacion)) {
            return this.desagregacionesPosibles.get(labelPrincipal).get(labelDesagregacion);
        }
        return 0L;
    }

    public void convertirListaACrossTable() {
        desagregacionesPendientes = new HashMap<>();
        desagregacionesPosibles = new HashMap<>();
        desagregacionesRealizadas = new HashMap<>();
        estSinDesagregacion = new ArrayList<>();
        for (SgConsultaCalificacionesPendientesSedesEnNivel e : estDesagregada) {
            if (!desagregacionesPendientes.containsKey(e.getSedeCodigo())) {
                estSinDesagregacion.add(e);
            }
            if (!StringUtils.isBlank(e.getDesagregacion())) {
                desagregacionesPendientes.computeIfAbsent(e.getSedeCodigo(), s -> new HashMap<>()).put(e.getDesagregacion(), e.getPendientes());
                if (e.getPosibles() != null){
                    desagregacionesPosibles.computeIfAbsent(e.getSedeCodigo(), s -> new HashMap<>()).put(e.getDesagregacion(), e.getPosibles());
                }
                if (e.getRealizadas() != null){
                    desagregacionesRealizadas.computeIfAbsent(e.getSedeCodigo(), s -> new HashMap<>()).put(e.getDesagregacion(), e.getRealizadas());
                }
            }
        }

    }

    public void setFiltro(FiltroCalificacionesPendientesSedeEnNivel filtro) {
        this.filtro = filtro;
    }


    public List<SgConsultaCalificacionesPendientesSedesEnNivel> getEstDesagregada() {
        return estDesagregada;
    }

    public List<SgConsultaCalificacionesPendientesSedesEnNivel> getEstSinDesagregacion() {
        return estSinDesagregacion;
    }

      
    
    
    

}
