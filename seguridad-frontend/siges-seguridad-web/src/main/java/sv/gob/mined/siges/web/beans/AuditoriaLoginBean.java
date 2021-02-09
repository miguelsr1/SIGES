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
import sv.gob.mined.siges.web.enumerados.EnumTipoAutenticacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComAuditTrail;
import sv.gob.mined.siges.web.lazymodels.LazyComAuditTrailDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ComAuditTrailRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AuditoriaLoginBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AuditoriaLoginBean.class.getName());

    @Inject
    private ComAuditTrailRestClient restClient;

    private FiltroComAuditTrail filtro = new FiltroComAuditTrail();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyComAuditTrailDataModel categoriaOperacionLazyModel;
    private SofisComboG<EnumTipoAutenticacion> comboTipoAutenticacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public AuditoriaLoginBean() {
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
            filtro.setTipoAutenticacion(comboTipoAutenticacion.getSelectedT() != null ? comboTipoAutenticacion.getSelectedT().toString() : null);
            totalResultados = restClient.buscarTotal(filtro);
            categoriaOperacionLazyModel = new LazyComAuditTrailDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<EnumTipoAutenticacion> estados = new ArrayList(Arrays.asList(EnumTipoAutenticacion.values()));
        comboTipoAutenticacion = new SofisComboG(estados, "text");
        comboTipoAutenticacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroComAuditTrail();
        comboTipoAutenticacion.setSelected(-1);
        fechaDesde = null;
        fechaHasta = null;
        buscar();
    }

    public ComAuditTrailRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ComAuditTrailRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroComAuditTrail getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroComAuditTrail filtro) {
        this.filtro = filtro;
    }

    public LazyComAuditTrailDataModel getCategoriaOperacionLazyModel() {
        return categoriaOperacionLazyModel;
    }

    public void setCategoriaOperacionLazyModel(LazyComAuditTrailDataModel categoriaOperacionLazyModel) {
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

    public LazyComAuditTrailDataModel getComAuditTrailLazyModel() {
        return categoriaOperacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyComAuditTrailDataModel categoriaOperacionLazyModel) {
        this.categoriaOperacionLazyModel = categoriaOperacionLazyModel;
    }

    public SofisComboG<EnumTipoAutenticacion> getComboTipoAutenticacion() {
        return comboTipoAutenticacion;
    }

    public void setComboTipoAutenticacion(SofisComboG<EnumTipoAutenticacion> comboTipoAutenticacion) {
        this.comboTipoAutenticacion = comboTipoAutenticacion;
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
