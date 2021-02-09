/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyOrganizacionCurricularDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class OrganizacionesCurricularBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OrganizacionesCurricularBean.class.getName());

    @Inject
    private OrganizacionCurricularRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    private SgOrganizacionCurricular entidadEnEdicion = new SgOrganizacionCurricular();
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private LazyOrganizacionCurricularDataModel organizacionCurricularLazyModel;
    private List<RevHistorico> historialOrganizacionCurricular = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;

    public OrganizacionesCurricularBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ORGANIZACION_CURRICULAR)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            organizacionCurricularLazyModel = new LazyOrganizacionCurricularDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {

    }

    public String limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
        return null;
    }

    public void actualizar(SgOrganizacionCurricular var) {
        entidadEnEdicion = (SgOrganizacionCurricular) SerializationUtils.clone(var);
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getOcuPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String historial(Long id) {
        try {
            historialOrganizacionCurricular = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
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

    public LazyOrganizacionCurricularDataModel getOrganizacionCurricularLazyModel() {
        return organizacionCurricularLazyModel;
    }

    public void setOrganizacionCurricularLazyModel(LazyOrganizacionCurricularDataModel organizacionCurricularLazyModel) {
        this.organizacionCurricularLazyModel = organizacionCurricularLazyModel;
    }

    public OrganizacionCurricularRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(OrganizacionCurricularRestClient restClient) {
        this.restClient = restClient;
    }

    public SgOrganizacionCurricular getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgOrganizacionCurricular entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialOrganizacionCurricular() {
        return historialOrganizacionCurricular;
    }

}
