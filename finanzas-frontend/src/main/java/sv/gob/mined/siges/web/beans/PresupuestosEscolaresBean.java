/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuario;
import sv.gob.mined.siges.web.lazymodels.LazyPresupuestosEscolaresDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.AutorizacionEdicionPresupuestoRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean corespondietne a la gestión de los presupuestos escolares (conjunto de
 * presupuesto).
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PresupuestosEscolaresBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PresupuestosEscolaresBean.class.getName());

    @Inject
    private PresupuestoEscolarRestCliente restClient;
    @Inject
    private AutorizacionEdicionPresupuestoRestClient autorizacionRestClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private UsuarioRestClient userRestClient;
    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;

    @Inject
    @Param(name = "id")
    private Long PresupuestoEscolarId;

    private SgPresupuestoEscolar entidadEnEdicion = new SgPresupuestoEscolar();
    private SgAutorizacionEdicionPresupuesto autorizacionEnEdicion = new SgAutorizacionEdicionPresupuesto();
    private FiltroPresupuestoEscolar filtro = new FiltroPresupuestoEscolar();
    private Long totalResultados;
    private LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel;
    private Integer paginado = 10;
    private List<RevHistorico> historialPresupuestoEscolar = new ArrayList();
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private SofisComboG<EnumPresupuestoEscolarEstado> comboEstado;
    private SofisComboG<EnumPresupuestoEscolarEstado> comboCambiarEstado;
    private Boolean soloLectura = Boolean.FALSE;
    private Integer selectedTab;
    private SgSede sedeSeleccionadaFiltro;
    private SgSede sedeSeleccionada;
    private List<SgPresupuestoEscolar> selectedPea;
    private SgMovimientos movimientoEnEdicion;
    private List<SgUsuario> usuariosSeleccionadas = new ArrayList();
    private Boolean autorizacion = Boolean.FALSE;
    Date date = new Date();
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    /**
     * Constructor Prespuestos Escolares.
     */
    public PresupuestosEscolaresBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
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

        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PRESUPUESTOS_ESCOLARES)) {

            sessionBean.getOperaciones();

            JSFUtils.redirectToIndex();
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            FiltroAnioFiscal filtroAnioFiscal = new FiltroAnioFiscal();
            filtroAnioFiscal.setIncluirCampos(new String[]{"aniPk", "aniAnio", "aniVersion"});
            List<SgAnioFiscal> listAnio = anioFiscalRestClient.buscar(filtroAnioFiscal);
            anioFiscalCombo = new SofisComboG(listAnio, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumPresupuestoEscolarEstado> listEstados = new ArrayList(Arrays.asList(EnumPresupuestoEscolarEstado.values()));
            comboEstado = new SofisComboG(listEstados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboCambiarEstado = new SofisComboG(listEstados, "text");
            comboCambiarEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el oncomplete de sedes en presupuestos escolares.
     */
    public List<SgSede> completeSedeFiltro(String query) {
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

    /**
     * Inializa para guardar, Presupuesto Escolar
     */
    public void agregar() {
        entidadEnEdicion = new SgPresupuestoEscolar();

    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));

//            List<SgSede> sedes = sessionBean.buscarSedes(null);
//            List<Long> sedesIds = new ArrayList<>();
//            if(sedes!=null && !sedes.isEmpty()){
//                sedesIds = sedes.stream().map(s -> s.getSedPk()).distinct().collect(Collectors.toList());
//            }
            if (sedeSeleccionadaFiltro != null) {
                filtro.setIdSede(sedeSeleccionadaFiltro.getSedPk());
            }
//            else if (sedesIds != null && !sedesIds.isEmpty()) {
//                if(!sessionBean.getUser().getName().equals("casuser")){
//                    filtro.setSedesIds(sedesIds);
//                }
//            }

            if (anioFiscalCombo.getSelectedT() != null) {
                filtro.setAnioFiscal(anioFiscalCombo.getSelectedT().getAniAnio());
            } else {
                filtro.setAnioFiscal(localDate.getYear());
            }

            if (comboEstado.getSelectedT() != null) {
                filtro.setPesEstado(comboEstado.getSelectedT());
            }

            filtro.setIncluirCampos(new String[]{"pesPk",
                "pesNombre",
                "pesNombreBusqueda",
                "pesDescripcion",
                "pesCodigo",
                "pesEstado",
                "pesHabilitado",
                "pesObservacion",
                "pesUltmodFecha",
                "pesUltmodUsuario",
                "pesAnioFiscalFk.aniPk",
                "pesAnioFiscalFk.aniAnio",
                "pesAnioFiscalFk.aniNombre",
                "pesAnioFiscalFk.aniVersion",
                "pesSedeFk.sedPk",
                "pesSedeFk.sedCodigo",
                "pesSedeFk.sedNombre",
                "pesSedeFk.sedTipo",
                "pesSedeFk.sedNombreBusqueda",
                "pesSedeFk.sedVersion",
                "pesVersion"

            });
            filtro.setOrderBy(new String[]{"pesPk"});
            filtro.setAscending(new boolean[]{false});

            totalResultados = restClient.buscarTotal(filtro);
            reglasPresupuestoEscolarDataModel = new LazyPresupuestosEscolaresDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de presupuestos escolares.
     */
    public void actualizar(SgPresupuestoEscolar req) {
        try {
            entidadEnEdicion = restClient.obtenerPorId(req.getPesPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el título de la operación a realizar en presupuestos escolares.
     */
    public String getTituloPagina() {
        if (this.PresupuestoEscolarId == null) {
            return Etiquetas.getValue("nuevoPresupuestoEscolar");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verPresupuestoEscolar");
        } else {
            return Etiquetas.getValue("edicionPresupuestoEscolar");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialPresupuestoEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Inicializa Fltro presupuesto escolar vácio.
     */
    public void limpiar() {
        filtro = new FiltroPresupuestoEscolar();
        anioFiscalCombo.setSelected(-1);
        comboEstado.setSelected(-1);
        sedeSeleccionadaFiltro = null;
        buscar();
    }

    /**
     * Elimina un registro de presupusto escolar.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPesPk());
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

    /**
     * Crea o actualiza un registro de presupuestos escolares.
     */
    public void guardar() {
        try {
            restClient.guardar(entidadEnEdicion);
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

    /**
     * Cambia el estado del presupuesto.
     */
    public void cambiarEstado() {
        try {
            if (comboCambiarEstado.getSelectedT() != null) {
                if (selectedPea != null && !selectedPea.isEmpty()) {
                    for (SgPresupuestoEscolar pea : selectedPea) {
                        pea.setPesEstado(comboCambiarEstado.getSelectedT());
                        if (pea.getPesEstado().equals(EnumPresupuestoEscolarEstado.FORMULADO) && comboCambiarEstado.getSelectedT().equals(EnumPresupuestoEscolarEstado.EN_PROCESO)) {
                            restClient.guardar(pea);
                            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                        } else if (pea.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO) && comboCambiarEstado.getSelectedT().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
                            restClient.guardar(pea);
                            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                        } else if (pea.getPesEstado().equals(EnumPresupuestoEscolarEstado.AJUSTADO) && comboCambiarEstado.getSelectedT().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
                            restClient.guardar(pea);
                            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                        }

                    }
                    selectedPea = new ArrayList();
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    /**
     * Carga el combo de Usuarios.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgUsuario>
     */
    public List<SgUsuario> completeUsuarios(String query) {
        try {
            usuariosSeleccionadas = new ArrayList();
            FiltroUsuario user = new FiltroUsuario();
            user.setNombre(query);
            user.setHabilitado(Boolean.TRUE);
            user.setMaxResults(11L);
            user.setOrderBy(new String[]{"usuNombre"});
            user.setAscending(new boolean[]{false});
            return userRestClient.buscar(user);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Autoriza presupuesto escolar para su edcion.
     */
    public void autorizar(SgPresupuestoEscolar req) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (req);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Autoriza presupuesto escolar para su edcion.
     */
    public boolean validarAutorizacion(SgPresupuestoEscolar pres) {
        try {
            entidadEnEdicion = (pres);
            if (entidadEnEdicion != null && entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
                FiltroAutorizacionEdicionPresupuesto aut = new FiltroAutorizacionEdicionPresupuesto();
                aut.setAutPresupuestoFk(entidadEnEdicion.getPesPk());
                aut.setAutUsuarioValidadoFk(sessionBean.getEntidadUsuario().getUsuPk());
                aut.setOrderBy(new String[]{"autPk"});
                aut.setIncluirCampos(new String[]{
                    "autPk",
                    "autPresupuestoFk.pesPk",
                    "autPresupuestoFk.pesVersion",
                    "autUsuarioValidadoFk.usuPk",
                    "autUsuarioValidadoFk.usuVersion",
                    "autVersion"
                });
                aut.setAscending(new boolean[]{false});
                List<SgAutorizacionEdicionPresupuesto> usuarios = autorizacionRestClient.buscar(aut);

                if (!usuarios.isEmpty()) {
                    return true;
                }

            } else {
                return false;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return false;
    }

    /**
     * Crea o actualiza un registro de presupuestos escolares.
     */
    public void guardarAutorizacion() {
        try {
            List<SgAutorizacionEdicionPresupuesto> usuarios = new ArrayList();
            if (usuariosSeleccionadas != null && !usuariosSeleccionadas.isEmpty()) {
                usuariosSeleccionadas.forEach(r -> {
                    SgAutorizacionEdicionPresupuesto user = new SgAutorizacionEdicionPresupuesto();
                    user.setAutPresupuestoFk(entidadEnEdicion);
                    user.setAutUsuarioValidadoFk(r);
                    usuarios.add(user);
                });
            }
            if (usuarios != null && !usuarios.isEmpty()) {
                for (SgAutorizacionEdicionPresupuesto aut : usuarios) {
                    autorizacionRestClient.guardar(aut);
                }
            }
            entidadEnEdicion.setPesEdicion(Boolean.TRUE);
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

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public void setEntidadEnEdicion(SgPresupuestoEscolar entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgPresupuestoEscolar getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroPresupuestoEscolar getFiltro() {
        return filtro;
    }

    public SgAutorizacionEdicionPresupuesto getAutorizacionEnEdicion() {
        return autorizacionEnEdicion;
    }

    public void setAutorizacionEnEdicion(SgAutorizacionEdicionPresupuesto autorizacionEnEdicion) {
        this.autorizacionEnEdicion = autorizacionEnEdicion;
    }

    public Boolean getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(Boolean autorizacion) {
        this.autorizacion = autorizacion;
    }

    public void setFiltro(FiltroPresupuestoEscolar filtro) {
        this.filtro = filtro;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyPresupuestosEscolaresDataModel getReglasPresupuestoEscolarDataModel() {
        return reglasPresupuestoEscolarDataModel;
    }

    public void setReglasPresupuestoEscolarDataModel(LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel) {
        this.reglasPresupuestoEscolarDataModel = reglasPresupuestoEscolarDataModel;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public List<RevHistorico> getHistorialPresupuestoEscolar() {
        return historialPresupuestoEscolar;
    }

    public void setHistorialPresupuestoEscolar(List<RevHistorico> historialPresupuestoEscolar) {
        this.historialPresupuestoEscolar = historialPresupuestoEscolar;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Long getPresupuestoEscolarId() {
        return PresupuestoEscolarId;
    }

    public void setPresupuestoEscolarId(Long PresupuestoEscolarId) {
        this.PresupuestoEscolarId = PresupuestoEscolarId;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SgAnioFiscal> getAnioFiscalCombo() {
        return anioFiscalCombo;
    }

    public void setAnioFiscalCombo(SofisComboG<SgAnioFiscal> anioFiscalCombo) {
        this.anioFiscalCombo = anioFiscalCombo;
    }

    public SofisComboG<EnumPresupuestoEscolarEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumPresupuestoEscolarEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public List<SgPresupuestoEscolar> getSelectedPea() {
        return selectedPea;
    }

    public void setSelectedPea(List<SgPresupuestoEscolar> selectedPea) {
        this.selectedPea = selectedPea;
    }

    public SgMovimientos getMovimientoEnEdicion() {
        return movimientoEnEdicion;
    }

    public void setMovimientoEnEdicion(SgMovimientos movimientoEnEdicion) {
        this.movimientoEnEdicion = movimientoEnEdicion;
    }

    public SofisComboG<EnumPresupuestoEscolarEstado> getComboCambiarEstado() {
        return comboCambiarEstado;
    }

    public void setComboCambiarEstado(SofisComboG<EnumPresupuestoEscolarEstado> comboCambiarEstado) {
        this.comboCambiarEstado = comboCambiarEstado;
    }

////
    public List<SgUsuario> getUsuariosSeleccionadas() {
        return usuariosSeleccionadas;
    }

    public void setUsuariosSeleccionadas(List<SgUsuario> usuariosSeleccionadas) {
        this.usuariosSeleccionadas = usuariosSeleccionadas;
    }

    // </editor-fold>
}
