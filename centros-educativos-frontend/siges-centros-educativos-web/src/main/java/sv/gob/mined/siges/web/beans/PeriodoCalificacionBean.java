/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.lazymodels.LazyPeriodoCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.RangoFechaRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PeriodoCalificacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PeriodoCalificacionBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long calendarioId;

    @Inject
    private PeriodoCalificacionRestClient restClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private RangoFechaRestClient rangoFechaRestClient;

    @Inject
    private CalendarioRestClient calendarioRestClient;

    @Inject
    private NivelRestClient nivelRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroPeriodoCalificacion filtro = new FiltroPeriodoCalificacion();
    private SgPeriodoCalificacion entidadEnEdicion = new SgPeriodoCalificacion();
    private List<SgPeriodoCalificacion> historialPeriodoCalificacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPeriodoCalificacionDataModel periodoCalificacionLazyModel;
    private List<SgPeriodoCalificacion> listPeriodoCalificacion;
    private List<SgModalidad> listModalidadEducativa;
    private SofisComboG<SgNivel> nivelCombo;
    private SofisComboG<SgCiclo> cicloCombo;
    private SofisComboG<SgModalidadAtencion> modalidadAtencionCombo;
    private SofisComboG<SgModalidad> modalidadEducativaCombo;
    private List<SgNivel> listNiveles;
    private SgRangoFecha rangoFechaEnEdicion = new SgRangoFecha();
    private SgCalendario calendarioEnEdicion;
    private SofisComboG<SgSubModalidadAtencion> subModalidadAtencionCombo;

    private HashMap<Long, Boolean> panelCollapsed = new HashMap<>();
    private Boolean esAnual = Boolean.TRUE;
    private List<SelectItem> periodos;
    private Integer periodoSeleccionado;

    public PeriodoCalificacionBean() {
    }

    public void actualizarCalendario(Long calId) {
        calendarioId = calId;
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            if (calendarioId != null && calendarioId > 0) {
                calendarioEnEdicion = calendarioRestClient.obtenerPorId(calendarioId);
            } else {
                redirectToIndex();
            }
            cargarCombos();
            buscar();
            for (SgModalidad mod : listModalidadEducativa) {
                panelCollapsed.put(mod.getModPk(), Boolean.TRUE);
            }
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_PERIODOS_CALIFICACION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setCalPk(calendarioId);
            filtro.setInicializarRangoFecha(Boolean.TRUE);
            filtro.setBusquedaTodoTipoPeriodo(Boolean.TRUE);
            listPeriodoCalificacion = restClient.buscar(filtro);
            listModalidadEducativa = new ArrayList();
            for (SgPeriodoCalificacion perCal : listPeriodoCalificacion) {
           
                if (!listModalidadEducativa.contains(perCal.getPcaModalidad())) {
                    listModalidadEducativa.add(perCal.getPcaModalidad());
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgPeriodoCalificacion> obtenerPeriodos(SgModalidad mod) {
        List<SgPeriodoCalificacion> list = new ArrayList();
        for (SgPeriodoCalificacion perCal : listPeriodoCalificacion) {
            if (mod.equals(perCal.getPcaModalidad())) {
                list.add(perCal);
            }
        }
        return list;
    }

    public void cargarCombos() {
        try {
            FiltroNivel fn = new FiltroNivel();
            fn.setNivHabilitado(Boolean.TRUE);
            fn.setInicializarCiclos(Boolean.TRUE);
            listNiveles = nivelRestClient.buscar(fn);
            nivelCombo = new SofisComboG(listNiveles, "nivNombre");
            nivelCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            cicloCombo = new SofisComboG();
            cicloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            modalidadEducativaCombo = new SofisComboG();
            modalidadEducativaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            modalidadAtencionCombo = new SofisComboG();
            modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            subModalidadAtencionCombo = null;

            periodos = new ArrayList<SelectItem>();
            SelectItem si = new SelectItem(null, Etiquetas.getValue("comboEmptyItem"));
            periodos.add(si);
            periodos.add(new SelectItem(1, "Primer período"));
            periodos.add(new SelectItem(2, "Segundo período"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarComboCiclo() {
        subModalidadAtencionCombo = null;
        cicloCombo = new SofisComboG();
        cicloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        modalidadEducativaCombo = new SofisComboG();
        modalidadEducativaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        modalidadAtencionCombo = new SofisComboG();
        modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (nivelCombo.getSelectedT() != null) {
            cicloCombo = new SofisComboG(nivelCombo.getSelectedT().getNivCiclos(), "cicNombre");
            cicloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }

    public void cargarComboModalidad() {
        subModalidadAtencionCombo = null;
        modalidadEducativaCombo = new SofisComboG();
        modalidadEducativaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        modalidadAtencionCombo = new SofisComboG();
        modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (cicloCombo.getSelectedT() != null) {
            modalidadEducativaCombo = new SofisComboG(cicloCombo.getSelectedT().getCicModalidades(), "modNombre");
            modalidadEducativaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }

    public void cargarComboModAt() {
        try {
            subModalidadAtencionCombo = null;
            FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
            if (modalidadEducativaCombo.getSelectedT() != null) {
                filtroRel.setReaModalidadEducativa(modalidadEducativaCombo.getSelectedT().getModPk());
                List<SgRelModEdModAten> listrel = relModRestClient.buscar(filtroRel);
                List<SgModalidadAtencion> listModAt = new ArrayList();
                for (SgRelModEdModAten rel : listrel) {
                    if (!listModAt.contains(rel.getReaModalidadAtencion())) {
                        listModAt.add(rel.getReaModalidadAtencion());
                    }
                }
                modalidadAtencionCombo = new SofisComboG(listModAt, "matNombre");
                modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                modalidadAtencionCombo = new SofisComboG();
                modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarSubModalidadAtencion() {
        try {
            subModalidadAtencionCombo = null;
            if (modalidadAtencionCombo.getSelectedT() != null && modalidadEducativaCombo.getSelectedT() != null) {

                //Verificar si esta modalidad de atención tiene submodalidades
                List<SgSubModalidadAtencion> listaSubMod = new ArrayList<>();
                SgModalidadAtencion modAtencionSelect = modalidadAtencionCombo.getSelectedT();
                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadAtencion(modAtencionSelect.getMatPk());
                List<SgRelModEdModAten> listrel = relModRestClient.buscar(filtroRel);

                for (SgRelModEdModAten relAux : listrel) {
                    if (relAux.getReaSubModalidadAtencion() != null && modalidadEducativaCombo.getSelectedT().equals(relAux.getReaModalidadEducativa()) && modAtencionSelect.equals(relAux.getReaModalidadAtencion()) && BooleanUtils.isTrue(relAux.getReaSubModalidadAtencion().getSmoHabilitado())) {
                        listaSubMod.add(relAux.getReaSubModalidadAtencion());
                    }
                }
                if (!listaSubMod.isEmpty()) {
                    subModalidadAtencionCombo = new SofisComboG(listaSubMod, "smoNombre");
                    subModalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (listaSubMod.size() == 1) {
                        subModalidadAtencionCombo.setSelectedT(listaSubMod.get(0));
                    }

                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        nivelCombo.setSelected(-1);
        modalidadAtencionCombo = new SofisComboG();
        modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        cicloCombo = new SofisComboG();
        cicloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        modalidadEducativaCombo = new SofisComboG();
        modalidadEducativaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void limpiar() {
        filtro = new FiltroPeriodoCalificacion();
        buscar();
    }
    
    public String nombreTipoPeriodo(SgPeriodoCalificacion periodoCal){
        String nombre="Anual";
        if(EnumTipoPeriodoSeccion.COHORTE.equals(periodoCal.getPcaTipoPeriodo())){
            switch(periodoCal.getPcaNumeroPeriodo()){
                case 1: nombre="Primer cohorte";
                        break;
                case 2: nombre="Segundo cohorte";
                        break;
            }
        }
        return nombre;    
    }

    public void agregar() {
        esAnual = Boolean.TRUE;
        periodoSeleccionado=null;
        limpiarCombos();
        entidadEnEdicion = new SgPeriodoCalificacion();
        JSFUtils.limpiarMensajesError();
    }

    public void agregarRangoFecha(SgPeriodoCalificacion perCal) {
        entidadEnEdicion = perCal;
        rangoFechaEnEdicion = new SgRangoFecha();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgPeriodoCalificacion var) {
        esAnual = Boolean.TRUE;
        limpiarCombos();
        entidadEnEdicion = (SgPeriodoCalificacion) SerializationUtils.clone(var);
        if (entidadEnEdicion.getPcaPk() != null) {
            nivelCombo.setSelectedT(entidadEnEdicion.getPcaModalidad().getModCiclo().getCicNivel());
            cargarComboCiclo();
            cicloCombo.setSelectedT(entidadEnEdicion.getPcaModalidad().getModCiclo());
            cargarComboModalidad();
            modalidadEducativaCombo.setSelectedT(entidadEnEdicion.getPcaModalidad());
            cargarComboModAt();
            modalidadAtencionCombo.setSelectedT(entidadEnEdicion.getPcaModalidadAtencion());
            cargarSubModalidadAtencion();
            if (subModalidadAtencionCombo != null) {
                subModalidadAtencionCombo.setSelectedT(entidadEnEdicion.getPcaSubModalidadAtencion());
            }
            
            esAnual = entidadEnEdicion.getPcaTipoPeriodo()==null?Boolean.TRUE: EnumTipoPeriodoSeccion.ANUAL.equals(entidadEnEdicion.getPcaTipoPeriodo());
            periodoSeleccionado = entidadEnEdicion.getPcaNumeroPeriodo();
        }
        JSFUtils.limpiarMensajesError();

    }

    public void actualizarRangoFecha(SgPeriodoCalificacion perCal, SgRangoFecha var) {
        entidadEnEdicion = perCal;
        rangoFechaEnEdicion = (SgRangoFecha) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();

    }

    public void guardar() {
        try {
            if (BooleanUtils.isTrue(esAnual)) {
                entidadEnEdicion.setPcaTipoPeriodo(EnumTipoPeriodoSeccion.ANUAL);
                entidadEnEdicion.setPcaNumeroPeriodo(null);
            } else {
                entidadEnEdicion.setPcaTipoPeriodo(EnumTipoPeriodoSeccion.COHORTE);
                entidadEnEdicion.setPcaNumeroPeriodo(this.periodoSeleccionado);
            }
            if (entidadEnEdicion.getPcaPk() != null) {
                if (entidadEnEdicion.getPcaRangoFecha() != null && entidadEnEdicion.getPcaNumero() != null) {
                    if (entidadEnEdicion.getPcaNumero() < entidadEnEdicion.getPcaRangoFecha().size()) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NUMERO_PERIODO_MENOR_RANGO_FECHA), "");
                        return;
                    }
                }
            }
            entidadEnEdicion.setPcaCalendario(calendarioEnEdicion);
            entidadEnEdicion.setPcaModalidad(modalidadEducativaCombo.getSelectedT());
            entidadEnEdicion.setPcaModalidadAtencion(modalidadAtencionCombo.getSelectedT());
            if (subModalidadAtencionCombo != null) {
                entidadEnEdicion.setPcaSubModalidadAtencion(subModalidadAtencionCombo.getSelectedT());
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarRangoFecha() {
        try {

            rangoFechaEnEdicion.setRfePeriodoCalificacion(entidadEnEdicion);
            rangoFechaEnEdicion = rangoFechaRestClient.guardar(rangoFechaEnEdicion);

            PrimeFaces.current().executeScript("PF('itemDialog_rangoFecha').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPcaPk());
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

    public void eliminarRangoFecha() {
        try {

            rangoFechaRestClient.eliminar(rangoFechaEnEdicion.getRfePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            rangoFechaEnEdicion = new SgRangoFecha();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialPeriodoCalificacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void togglePanel(Long id) {
        this.panelCollapsed.put(id, !panelCollapsed.get(id));
        LOGGER.log(Level.SEVERE, panelCollapsed.toString());
    }

    public SgPeriodoCalificacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPeriodoCalificacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgPeriodoCalificacion> getHistorialPeriodoCalificacion() {
        return historialPeriodoCalificacion;
    }

    public void setHistorialPeriodoCalificacion(List<SgPeriodoCalificacion> historialPeriodoCalificacion) {
        this.historialPeriodoCalificacion = historialPeriodoCalificacion;
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

    public LazyPeriodoCalificacionDataModel getPeriodoCalificacionLazyModel() {
        return periodoCalificacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyPeriodoCalificacionDataModel periodoCalificacionLazyModel) {
        this.periodoCalificacionLazyModel = periodoCalificacionLazyModel;
    }

    public FiltroPeriodoCalificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPeriodoCalificacion filtro) {
        this.filtro = filtro;
    }

    public List<SgPeriodoCalificacion> getListPeriodoCalificacion() {
        return listPeriodoCalificacion;
    }

    public void setListPeriodoCalificacion(List<SgPeriodoCalificacion> listPeriodoCalificacion) {
        this.listPeriodoCalificacion = listPeriodoCalificacion;
    }

    public List<SgModalidad> getListModalidadEducativa() {
        return listModalidadEducativa;
    }

    public void setListModalidadEducativa(List<SgModalidad> listModalidadEducativa) {
        this.listModalidadEducativa = listModalidadEducativa;
    }

    public SofisComboG<SgModalidadAtencion> getModalidadAtencionCombo() {
        return modalidadAtencionCombo;
    }

    public void setModalidadAtencionCombo(SofisComboG<SgModalidadAtencion> modalidadAtencionCombo) {
        this.modalidadAtencionCombo = modalidadAtencionCombo;
    }

    public SofisComboG<SgModalidad> getModalidadEducativaCombo() {
        return modalidadEducativaCombo;
    }

    public void setModalidadEducativaCombo(SofisComboG<SgModalidad> modalidadEducativaCombo) {
        this.modalidadEducativaCombo = modalidadEducativaCombo;
    }

    public SgRangoFecha getRangoFechaEnEdicion() {
        return rangoFechaEnEdicion;
    }

    public void setRangoFechaEnEdicion(SgRangoFecha rangoFechaEnEdicion) {
        this.rangoFechaEnEdicion = rangoFechaEnEdicion;
    }

    public SgCalendario getCalendarioEnEdicion() {
        return calendarioEnEdicion;
    }

    public void setCalendarioEnEdicion(SgCalendario calendarioEnEdicion) {
        this.calendarioEnEdicion = calendarioEnEdicion;
    }

    public SofisComboG<SgNivel> getNivelCombo() {
        return nivelCombo;
    }

    public void setNivelCombo(SofisComboG<SgNivel> nivelCombo) {
        this.nivelCombo = nivelCombo;
    }

    public SofisComboG<SgCiclo> getCicloCombo() {
        return cicloCombo;
    }

    public void setCicloCombo(SofisComboG<SgCiclo> cicloCombo) {
        this.cicloCombo = cicloCombo;
    }

    public List<SgNivel> getListNiveles() {
        return listNiveles;
    }

    public void setListNiveles(List<SgNivel> listNiveles) {
        this.listNiveles = listNiveles;
    }

    public HashMap<Long, Boolean> getPanelCollapsed() {
        return panelCollapsed;
    }

    public void setPanelCollapsed(HashMap<Long, Boolean> panelCollapsed) {
        this.panelCollapsed = panelCollapsed;
    }

    public SofisComboG<SgSubModalidadAtencion> getSubModalidadAtencionCombo() {
        return subModalidadAtencionCombo;
    }

    public void setSubModalidadAtencionCombo(SofisComboG<SgSubModalidadAtencion> subModalidadAtencionCombo) {
        this.subModalidadAtencionCombo = subModalidadAtencionCombo;
    }

    public Boolean getEsAnual() {
        return esAnual;
    }

    public void setEsAnual(Boolean esAnual) {
        this.esAnual = esAnual;
    }

    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public Integer getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(Integer periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

}
