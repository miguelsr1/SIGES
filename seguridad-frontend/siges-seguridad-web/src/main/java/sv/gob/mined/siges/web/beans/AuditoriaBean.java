/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.enumerados.ResultadoOperacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRegistroAuditoria;
import sv.gob.mined.siges.web.lazymodels.LazyRegistroAuditoriaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.RegistroAuditoriaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AuditoriaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AuditoriaBean.class.getName());

    @Inject
    private RegistroAuditoriaRestClient restClient;

    private FiltroRegistroAuditoria filtro = new FiltroRegistroAuditoria();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyRegistroAuditoriaDataModel categoriaOperacionLazyModel;
    private SofisComboG<ResultadoOperacion> comboResultadoOperacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public AuditoriaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            if (fechaDesde != null) {
                filtro.setFechaDesde(fechaDesde.atStartOfDay());
            }
            if (fechaHasta != null) {
                filtro.setFechaHasta(fechaHasta.atTime(23, 59, 59));
            }
            filtro.setResultadoOperacion(comboResultadoOperacion!=null?comboResultadoOperacion.getSelectedT():null);
            totalResultados = restClient.buscarTotal(filtro);
            categoriaOperacionLazyModel = new LazyRegistroAuditoriaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<ResultadoOperacion> estados = new ArrayList(Arrays.asList(ResultadoOperacion.values()));
        comboResultadoOperacion = new SofisComboG(estados, "text");
        comboResultadoOperacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroRegistroAuditoria();
        comboResultadoOperacion.setSelected(-1);
        fechaDesde = null;
        fechaHasta = null;
        buscar();
    }

    public RegistroAuditoriaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(RegistroAuditoriaRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroRegistroAuditoria getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRegistroAuditoria filtro) {
        this.filtro = filtro;
    }

    public LazyRegistroAuditoriaDataModel getCategoriaOperacionLazyModel() {
        return categoriaOperacionLazyModel;
    }

    public void setCategoriaOperacionLazyModel(LazyRegistroAuditoriaDataModel categoriaOperacionLazyModel) {
        this.categoriaOperacionLazyModel = categoriaOperacionLazyModel;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyRegistroAuditoriaDataModel getRegistroAuditoriaLazyModel() {
        return categoriaOperacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyRegistroAuditoriaDataModel categoriaOperacionLazyModel) {
        this.categoriaOperacionLazyModel = categoriaOperacionLazyModel;
    }

    public SofisComboG<ResultadoOperacion> getComboResultadoOperacion() {
        return comboResultadoOperacion;
    }

    public void setComboResultadoOperacion(SofisComboG<ResultadoOperacion> comboResultadoOperacion) {
        this.comboResultadoOperacion = comboResultadoOperacion;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

}
