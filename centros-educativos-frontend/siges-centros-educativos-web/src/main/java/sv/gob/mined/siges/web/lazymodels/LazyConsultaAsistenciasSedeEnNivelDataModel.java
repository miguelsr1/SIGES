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
import sv.gob.mined.siges.web.dto.SgConsultaAsistenciasSedesEnNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciasSedeEnNivel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaCabezalRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
public class LazyConsultaAsistenciasSedeEnNivelDataModel extends LazyDataModel<SgConsultaAsistenciasSedesEnNivel> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyConsultaAsistenciasSedeEnNivelDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private ControlAsistenciaCabezalRestClient controlAsistenciaClient;
    private FiltroAsistenciasSedeEnNivel filtro;

    private List<SgConsultaAsistenciasSedesEnNivel> estDesagregada;
    private List<SgConsultaAsistenciasSedesEnNivel> estSinDesagregacion;
    private HashMap<Object, HashMap<String, Object>> desagregacionesAsistencias;
    private HashMap<Object, HashMap<String, Object>> desagregacionesInasistencias;

    public LazyConsultaAsistenciasSedeEnNivelDataModel(ControlAsistenciaCabezalRestClient controlAsistenciaClient, FiltroAsistenciasSedeEnNivel filtro, Long rowCount, Integer pageSize) {
        this.controlAsistenciaClient = controlAsistenciaClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }

    @Override
    public SgConsultaAsistenciasSedesEnNivel getRowData(String rowKey) {
            return null;
    }

    @Override
    public Long getRowKey(SgConsultaAsistenciasSedesEnNivel o) {
        return null;
    }

    @Override
    public List<SgConsultaAsistenciasSedesEnNivel> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }
            estDesagregada = controlAsistenciaClient.obtenerAsistenciasPorSedeEnNivel(filtro);
            convertirListaACrossTable();
            return estSinDesagregacion;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public Object obtenerCantidadAsistencias(Object labelPrincipal, String labelDesagregacion) {
        if (this.desagregacionesAsistencias.containsKey(labelPrincipal) && this.desagregacionesAsistencias.get(labelPrincipal).containsKey(labelDesagregacion)) {
            return this.desagregacionesAsistencias.get(labelPrincipal).get(labelDesagregacion);
        }
        return 0L;
    }
    
    public Object obtenerCantidadInasistencias(Object labelPrincipal, String labelDesagregacion) {
        if (this.desagregacionesInasistencias.containsKey(labelPrincipal) && this.desagregacionesInasistencias.get(labelPrincipal).containsKey(labelDesagregacion)) {
            return this.desagregacionesInasistencias.get(labelPrincipal).get(labelDesagregacion);
        }
        return 0L;
    }

    public void convertirListaACrossTable() {
        desagregacionesAsistencias = new HashMap<>();
        desagregacionesInasistencias = new HashMap<>();
        estSinDesagregacion = new ArrayList<>();
        for (SgConsultaAsistenciasSedesEnNivel e : estDesagregada) {
            if (!desagregacionesAsistencias.containsKey(e.getSedeCodigo())) {
                estSinDesagregacion.add(e);
            }
            if (!StringUtils.isBlank(e.getDesagregacion())) {
                desagregacionesAsistencias.computeIfAbsent(e.getSedeCodigo(), s -> new HashMap<>()).put(e.getDesagregacion(), e.getAsistencias());
                desagregacionesInasistencias.computeIfAbsent(e.getSedeCodigo(), s -> new HashMap<>()).put(e.getDesagregacion(), e.getInasistencias());
            }
        }

    }

    public void setFiltro(FiltroAsistenciasSedeEnNivel filtro) {
        this.filtro = filtro;
    }


    public List<SgConsultaAsistenciasSedesEnNivel> getEstDesagregada() {
        return estDesagregada;
    }

    public List<SgConsultaAsistenciasSedesEnNivel> getEstSinDesagregacion() {
        return estSinDesagregacion;
    }

      
    
    
    

}
