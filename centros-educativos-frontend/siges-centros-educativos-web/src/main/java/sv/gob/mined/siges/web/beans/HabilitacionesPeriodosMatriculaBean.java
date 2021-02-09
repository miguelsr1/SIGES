/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyHabilitacionPeriodoMatriculaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.HabilitacionPeriodoMatriculaRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class HabilitacionesPeriodosMatriculaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HabilitacionesPeriodosMatriculaBean.class.getName());

    @Inject
    private HabilitacionPeriodoMatriculaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private NivelRestClient nivelClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroHabilitacionPeriodoMatricula filtro = new FiltroHabilitacionPeriodoMatricula();
    private SgHabilitacionPeriodoMatricula entidadEnEdicion = new SgHabilitacionPeriodoMatricula();
    private List<RevHistorico> historialHabilitacionPeriodoMatricula = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyHabilitacionPeriodoMatriculaDataModel habilitacionPeriodoMatriculaLazyModel;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<EnumEstadoHabilitacionPeriodoMatricula> comboEstado;
    private SgSede sedeActividadSeleccionadaBusqueda;
    private SofisComboG<SgNivel> comboNivel;

    public HabilitacionesPeriodosMatriculaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"hmpSedeFk.sedPk",
                "hmpSedeFk.sedTipo",
                "hmpSedeFk.sedCodigo",
                "hmpSedeFk.sedNombre",
                "hmpSedeFk.sedTelefono",
                "hmpSedeFk.sedDireccion.dirDepartamento.depNombre",
                "hmpSedeFk.sedDireccion.dirMunicipio.munNombre",
                "hmpFechaSolicitud",
                "hmpEstado",
                "hmpUltModUsuario",
                "hmpUltModFecha",
                "hmpObservacion",
                "hmpObservacionAprobacionRechazo",
                "hmpVersion",
                "hmpFechaDesde",
                "hmpFechaHasta"
            });
            filtro.setIncluyendoNivel(Boolean.TRUE);
            filtro.setHmpDepartamentoFk(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setHmpEstado(comboEstado.getSelectedT());
            filtro.setHmpSedeFk(sedeActividadSeleccionadaBusqueda != null ? sedeActividadSeleccionadaBusqueda.getSedPk() : null);
            filtro.setHmpNivel(comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            habilitacionPeriodoMatriculaLazyModel = new LazyHabilitacionPeriodoMatriculaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgNivel> obtenerNiveles(SgHabilitacionPeriodoMatricula habPer) {
        if (habPer.getHmpRelHabilitacionMatriculaNivel() != null) {
            return habPer.getHmpRelHabilitacionMatriculaNivel().stream().map(c->c.getRhnNivelFk()).distinct().collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoRestClient.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoHabilitacionPeriodoMatricula> estados = new ArrayList(Arrays.asList(EnumEstadoHabilitacionPeriodoMatricula.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroNivel filtroNivel = new FiltroNivel();
            filtroNivel.setNivHabilitado(Boolean.TRUE);
            filtroNivel.setIncluirCampos(new String[]{"nivNombre"});
            List<SgNivel> niveles = nivelClient.buscarConCache(filtroNivel);
            comboNivel = new SofisComboG<>(niveles, "nivNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
        comboDepartamento.setSelected(-1);
        comboEstado.setSelected(-1);
        comboNivel.setSelected(-1);
    }

    public void limpiar() {
        sedeActividadSeleccionadaBusqueda = null;
        filtro = new FiltroHabilitacionPeriodoMatricula();
        limpiarCombos();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgHabilitacionPeriodoMatricula();
    }

    public void actualizar(SgHabilitacionPeriodoMatricula var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgHabilitacionPeriodoMatricula) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getHmpPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialHabilitacionPeriodoMatricula = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroHabilitacionPeriodoMatricula getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroHabilitacionPeriodoMatricula filtro) {
        this.filtro = filtro;
    }

    public SgHabilitacionPeriodoMatricula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgHabilitacionPeriodoMatricula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialHabilitacionPeriodoMatricula() {
        return historialHabilitacionPeriodoMatricula;
    }

    public void setHistorialHabilitacionPeriodoMatricula(List<RevHistorico> historialHabilitacionPeriodoMatricula) {
        this.historialHabilitacionPeriodoMatricula = historialHabilitacionPeriodoMatricula;
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

    public LazyHabilitacionPeriodoMatriculaDataModel getHabilitacionPeriodoMatriculaLazyModel() {
        return habilitacionPeriodoMatriculaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyHabilitacionPeriodoMatriculaDataModel habilitacionPeriodoMatriculaLazyModel) {
        this.habilitacionPeriodoMatriculaLazyModel = habilitacionPeriodoMatriculaLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<EnumEstadoHabilitacionPeriodoMatricula> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoHabilitacionPeriodoMatricula> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SgSede getSedeActividadSeleccionadaBusqueda() {
        return sedeActividadSeleccionadaBusqueda;
    }

    public void setSedeActividadSeleccionadaBusqueda(SgSede sedeActividadSeleccionadaBusqueda) {
        this.sedeActividadSeleccionadaBusqueda = sedeActividadSeleccionadaBusqueda;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

}
