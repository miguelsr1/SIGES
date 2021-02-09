/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.lazymodels.LazyGradoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgRelGradoPrecedencia;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPrecedencia;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroDefinicionTitulo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPrecedenciaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class GradoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GradoBean.class.getName());

    @Inject
    @Param(name = "modId")
    private Long modId;

    @Inject
    private GradoRestClient restClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private ModalidadRestClient modalidadClient;

    @Inject
    private NivelRestClient nivelClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private CicloRestClient restCiclo;

    @Inject
    private OpcionRestClient restOpcion;

    @Inject
    private RelGradoPrecedenciaRestClient relGradoPrecedenciaClient;

    private FiltroGrado filtro = new FiltroGrado();
    private SgGrado entidadEnEdicion = new SgGrado();
    private SgModalidad modalidadEnEdicion = new SgModalidad();
    private List<RevHistorico> historialGrado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyGradoDataModel gradoLazyModel;
    private List<SgRelModEdModAten> listaRelacion;
    private SgRelModEdModAten relacionEnEdicion;
    private List<Object> listaModalidadAtencion = new ArrayList();
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgOpcion> comboOpcion;
    private SofisComboG<SgProgramaEducativo> comboProgramaEducativo;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion;
    private List<SgRelModEdModAten> relModEdModAtenSelected;
    private SofisComboG<SgGrado> comboGrado;
    private SgGrado gradoSeleccionado;

    private SgGrado gradoSeleccionadoPrecedencia;
    private List<SgRelGradoPrecedencia> gradoSeleccionadoPrecedencias = new ArrayList<>();
    private SgRelGradoPrecedencia relacionPrecedenciaEdicion;

    public GradoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            if (modId == null || modId <= 0) {
                redirectToIndex();
            }
            cargarDatos();
            cargarCombos();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_GRADO)) {
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
            totalResultados = restClient.buscarTotal(filtro);
            gradoLazyModel = new LazyGradoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {

            FiltroNivel fn = new FiltroNivel();
            fn.setOrganizacionCurricularPk(modalidadEnEdicion.getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
            List<SgNivel> niveles = nivelClient.buscar(fn);

            comboNivel = new SofisComboG(niveles, "nivNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            //cargarSedePorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarDatos() {
        try {
            limpiarCombos();
            modalidadEnEdicion = modalidadClient.obtenerPorId(modId);
            FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
            filtroRel.setReaModalidadEducativa(modId);
            filtroRel.setInicializarGrados(Boolean.TRUE);
            listaRelacion = relModRestClient.buscar(filtroRel);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroGrado();
        buscar();
    }

    public List<SgDefinicionTitulo> completeDefinicionTitulo(String query) {
        try {
            FiltroDefinicionTitulo fil = new FiltroDefinicionTitulo();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"dtiNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getGraDefinicionTitulo() != null
                    ? restCatalogo.buscarDefinicionTitulo(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getGraDefinicionTitulo().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarDefinicionTitulo(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void agregar(SgRelModEdModAten rel) {
        relacionEnEdicion = rel;
        entidadEnEdicion = new SgGrado();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgRelModEdModAten rel, SgGrado var) {
        try {
            entidadEnEdicion = restClient.obtenerPorId(var.getGraPk());
            relacionEnEdicion = rel;
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setGraRelacionModalidad(relacionEnEdicion); 
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            
            if (relacionEnEdicion.getReaGrado() == null) {
                relacionEnEdicion.setReaGrado(new ArrayList());
            }
            
            if (relacionEnEdicion.getReaGrado().contains(entidadEnEdicion)) {
                relacionEnEdicion.getReaGrado().remove(entidadEnEdicion);
            }
            
            relacionEnEdicion.getReaGrado().add(entidadEnEdicion);

            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarPrecedencias(SgGrado var) {
        try {
            relacionPrecedenciaEdicion = null;
            gradoSeleccionadoPrecedencia = var;
            FiltroRelGradoPrecedencia fgp = new FiltroRelGradoPrecedencia();
            fgp.setRgpGradoDestino(var.getGraPk());
            fgp.setIncluirCampos(new String[]{"rgpVersion",
                "rgpGradoOrigenFk.graPk", "rgpGradoOrigenFk.graVersion", "rgpGradoOrigenFk.graCodigo", "rgpGradoOrigenFk.graNombre",
                "rgpGradoOrigenFk.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "rgpGradoOrigenFk.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "rgpGradoOrigenFk.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "rgpGradoOrigenFk.graRelacionModalidad.reaModalidadAtencion.matNombre",
                "rgpGradoOrigenFk.graRelacionModalidad.reaSubModalidadAtencion.smoNombre"});
            gradoSeleccionadoPrecedencias = relGradoPrecedenciaClient.buscar(fgp);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void agregarNuevaRelacionPrecedencia() {
         try {
            relacionPrecedenciaEdicion = new SgRelGradoPrecedencia();
            gradoSeleccionadoPrecedencia = restClient.obtenerPorId(gradoSeleccionadoPrecedencia.getGraPk());
            comboNivel.setSelectedT(gradoSeleccionadoPrecedencia.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel());
            seleccionarNivel();
            comboCiclo.setSelectedT(gradoSeleccionadoPrecedencia.getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo());
            seleccionarCiclo();
            comboModalidad.setSelectedT(gradoSeleccionadoPrecedencia.getGraRelacionModalidad().getReaModalidadEducativa());
            seleccionarModalidad();
            comboModalidadAtencion.setSelectedT(gradoSeleccionadoPrecedencia.getGraRelacionModalidad().getReaModalidadAtencion());
            seleccionarModalidadAtencion();
            if (gradoSeleccionadoPrecedencia.getGraRelacionModalidad().getReaSubModalidadAtencion() != null) {
                comboSubModalidadAtencion.setSelectedT(gradoSeleccionadoPrecedencia.getGraRelacionModalidad().getReaSubModalidadAtencion());
                seleccionarSubModalidadAtencion();
            }
            if (gradoSeleccionadoPrecedencia.getGraGradoSiguiente() != null) {
                comboGrado.setSelectedT(gradoSeleccionadoPrecedencia.getGraGradoSiguiente());
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarRelacionPrecedencia(SgRelGradoPrecedencia rgp) {
        try {
            relGradoPrecedenciaClient.eliminar(rgp.getRgpPk());
            gradoSeleccionadoPrecedencias.remove(rgp);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarRelacionPrecedencia() {
        try {
            relacionPrecedenciaEdicion.setRgpGradoDestinoFk(this.gradoSeleccionadoPrecedencia);
            relacionPrecedenciaEdicion.setRgpGradoOrigenFk(comboGrado.getSelectedT());
            relGradoPrecedenciaClient.guardar(relacionPrecedenciaEdicion);
            cargarPrecedencias(gradoSeleccionadoPrecedencia);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getGraPk());
            relacionEnEdicion.getReaGrado().remove(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialGrado = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarNivel() {
        try {
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            relModEdModAtenSelected = new ArrayList<>();
            if (comboNivel.getSelectedT() != null) {
                FiltroCiclo fCiclo = new FiltroCiclo();
                fCiclo.setAscending(new boolean[]{true});
                fCiclo.setOrderBy(new String[]{"cicNombre"});
                fCiclo.setIncluirCampos(new String[]{"cicNombre", "cicVersion"});
                fCiclo.setCicHabilitado(Boolean.TRUE);
                fCiclo.setNivPk(comboNivel.getSelectedT().getNivPk());
                List<SgCiclo> ciclos = restCiclo.buscarConCache(fCiclo);

                comboCiclo = new SofisComboG(ciclos, "cicNombre");
                comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (ciclos.size() == 1) {
                    comboCiclo.setSelectedT(ciclos.get(0));
                    seleccionarCiclo();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarCiclo() {
        try {
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            relModEdModAtenSelected = new ArrayList<>();
            if (comboCiclo.getSelectedT() != null) {
                FiltroModalidad fModalidad = new FiltroModalidad();
                fModalidad.setCicPk(comboCiclo.getSelectedT().getCicPk());
                fModalidad.setAscending(new boolean[]{true});
                fModalidad.setOrderBy(new String[]{"modNombre"});
                fModalidad.setModHabilitado(Boolean.TRUE);
                fModalidad.setIncluirCampos(new String[]{"modNombre", "modVersion"});

                List<SgModalidad> modalidad = modalidadClient.buscarConCache(fModalidad);

                comboModalidad = new SofisComboG(new ArrayList(modalidad), "modNombre");
                comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (modalidad.size() == 1) {
                    comboModalidad.setSelectedT(modalidad.get(0));
                    seleccionarModalidad();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModalidad() {
        try {
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            relModEdModAtenSelected = new ArrayList<>();

            if (comboModalidad.getSelectedT() != null) {
                //Buscar las opciones impartidas en esta modalidad
                FiltroOpciones fopcion = new FiltroOpciones();
                fopcion.setOpcModalidadPk(comboModalidad.getSelectedT().getModPk());
                fopcion.setIncluirCampos(new String[]{"opcNombre", "opcVersion"});
                fopcion.setHabilitado(Boolean.TRUE);
                fopcion.setAscending(new boolean[]{true});
                fopcion.setOrderBy(new String[]{"opcNombre"});
                List<SgOpcion> opciones = restOpcion.buscarConCache(fopcion);

                comboOpcion = new SofisComboG(new ArrayList(opciones), "opcNombre");
                comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (opciones.size() == 1) {
                    comboOpcion.setSelectedT(opciones.get(0));
                }

                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(comboModalidad.getSelectedT().getModPk());
                filtroRel.setIncluirCampos(new String[]{"reaModalidadEducativa.modPk",
                    "reaModalidadEducativa.modNombre",
                    "reaModalidadEducativa.modHabilitado",
                    "reaModalidadEducativa.modVersion",
                    "reaModalidadAtencion.matPk",
                    "reaModalidadAtencion.matNombre",
                    "reaModalidadAtencion.matHabilitado",
                    "reaModalidadAtencion.matVersion",
                    "reaSubModalidadAtencion.smoPk",
                    "reaSubModalidadAtencion.smoNombre",
                    "reaSubModalidadAtencion.smoHabilitado",
                    "reaSubModalidadAtencion.smoVersion"});
                List<SgModalidadAtencion> listModAt = new ArrayList();
                relModEdModAtenSelected = relModRestClient.buscarConCache(filtroRel);
                for (SgRelModEdModAten rel : relModRestClient.buscar(filtroRel)) {
                    if (!listModAt.contains(rel.getReaModalidadAtencion()) && BooleanUtils.isTrue(rel.getReaModalidadAtencion().getMatHabilitado())) {
                        listModAt.add(rel.getReaModalidadAtencion());
                    }
                }

                comboModalidadAtencion = new SofisComboG(listModAt, "matNombre");
                comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (listModAt.size() == 1) {
                    comboModalidadAtencion.setSelectedT(listModAt.get(0));
                    seleccionarModalidadAtencion();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModalidadAtencion() {
        try {
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;

            if (comboModalidadAtencion.getSelectedT() != null) {

                //Verificar si esta modalidad de atención tiene submodalidades
                List<SgSubModalidadAtencion> listaSubMod = new ArrayList<>();
                SgModalidadAtencion modAtencionSelect = comboModalidadAtencion.getSelectedT();

                for (SgRelModEdModAten relAux : relModEdModAtenSelected) {
                    if (relAux.getReaSubModalidadAtencion() != null && modAtencionSelect.equals(relAux.getReaModalidadAtencion()) && BooleanUtils.isTrue(relAux.getReaSubModalidadAtencion().getSmoHabilitado())) {
                        listaSubMod.add(relAux.getReaSubModalidadAtencion());
                    }
                }
                if (!listaSubMod.isEmpty()) {
                    comboSubModalidadAtencion = new SofisComboG(listaSubMod, "smoNombre");
                    comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (listaSubMod.size() == 1) {
                        comboSubModalidadAtencion.setSelectedT(listaSubMod.get(0));
                        seleccionarSubModalidadAtencion();
                    }

                } else {
                    FiltroGrado fGrado = new FiltroGrado();
                    fGrado.setModAtencionPk(comboModalidadAtencion.getSelectedT().getMatPk());
                    fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                    fGrado.setHabilitado(Boolean.TRUE);
                    fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                    List<SgGrado> grado = restClient.buscarConCache(fGrado);

                    if (!grado.isEmpty()) {
                        comboGrado = new SofisComboG(grado, "graNombre");
                        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }

                    if (grado.size() == 1) {
                        comboGrado.setSelectedT(grado.get(0));
                    }
                }

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSubModalidadAtencion() {
        try {
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboSubModalidadAtencion.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(comboSubModalidadAtencion.getSelectedT().getSmoPk());
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                fGrado.setHabilitado(Boolean.TRUE);
                List<SgGrado> grado = restClient.buscarConCache(fGrado);
                comboGrado = new SofisComboG(grado, "graNombre");
                comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (grado.size() == 1) {
                    comboGrado.setSelectedT(grado.get(0));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public FiltroGrado getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroGrado filtro) {
        this.filtro = filtro;
    }

    public SgGrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgGrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialGrado() {
        return historialGrado;
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

    public LazyGradoDataModel getGradoLazyModel() {
        return gradoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyGradoDataModel gradoLazyModel) {
        this.gradoLazyModel = gradoLazyModel;
    }

    public SgModalidad getModalidadEnEdicion() {
        return modalidadEnEdicion;
    }

    public void setModalidadEnEdicion(SgModalidad modalidadEnEdicion) {
        this.modalidadEnEdicion = modalidadEnEdicion;
    }

    public List<SgRelModEdModAten> getListaRelacion() {
        return listaRelacion;
    }

    public void setListaRelacion(List<SgRelModEdModAten> listaRelacion) {
        this.listaRelacion = listaRelacion;
    }

    public SgRelModEdModAten getRelacionEnEdicion() {
        return relacionEnEdicion;
    }

    public void setRelacionEnEdicion(SgRelModEdModAten relacionEnEdicion) {
        this.relacionEnEdicion = relacionEnEdicion;
    }

    public List<Object> getListaModalidadAtencion() {
        return listaModalidadAtencion;
    }

    public void setListaModalidadAtencion(List<Object> listaModalidadAtencion) {
        this.listaModalidadAtencion = listaModalidadAtencion;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgCiclo> getComboCiclo() {
        return comboCiclo;
    }

    public void setComboCiclo(SofisComboG<SgCiclo> comboCiclo) {
        this.comboCiclo = comboCiclo;
    }

    public SofisComboG<SgModalidad> getComboModalidad() {
        return comboModalidad;
    }

    public void setComboModalidad(SofisComboG<SgModalidad> comboModalidad) {
        this.comboModalidad = comboModalidad;
    }

    public SofisComboG<SgOpcion> getComboOpcion() {
        return comboOpcion;
    }

    public void setComboOpcion(SofisComboG<SgOpcion> comboOpcion) {
        this.comboOpcion = comboOpcion;
    }

    public SofisComboG<SgProgramaEducativo> getComboProgramaEducativo() {
        return comboProgramaEducativo;
    }

    public void setComboProgramaEducativo(SofisComboG<SgProgramaEducativo> comboProgramaEducativo) {
        this.comboProgramaEducativo = comboProgramaEducativo;
    }

    public SofisComboG<SgModalidadAtencion> getComboModalidadAtencion() {
        return comboModalidadAtencion;
    }

    public void setComboModalidadAtencion(SofisComboG<SgModalidadAtencion> comboModalidadAtencion) {
        this.comboModalidadAtencion = comboModalidadAtencion;
    }

    public SofisComboG<SgSubModalidadAtencion> getComboSubModalidadAtencion() {
        return comboSubModalidadAtencion;
    }

    public void setComboSubModalidadAtencion(SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion) {
        this.comboSubModalidadAtencion = comboSubModalidadAtencion;
    }

    public SofisComboG<SgGrado> getComboGrado() {
        return comboGrado;
    }

    public void setComboGrado(SofisComboG<SgGrado> comboGrado) {
        this.comboGrado = comboGrado;
    }

    public List<SgRelModEdModAten> getRelModEdModAtenSelected() {
        return relModEdModAtenSelected;
    }

    public void setRelModEdModAtenSelected(List<SgRelModEdModAten> relModEdModAtenSelected) {
        this.relModEdModAtenSelected = relModEdModAtenSelected;
    }

    public SgGrado getGradoSeleccionado() {
        return gradoSeleccionado;
    }

    public void setGradoSeleccionado(SgGrado gradoSeleccionado) {
        this.gradoSeleccionado = gradoSeleccionado;
    }

    public SgGrado getGradoSeleccionadoPrecedencia() {
        return gradoSeleccionadoPrecedencia;
    }

    public void setGradoSeleccionadoPrecedencia(SgGrado gradoSeleccionadoPrecedencia) {
        this.gradoSeleccionadoPrecedencia = gradoSeleccionadoPrecedencia;
    }

    public List<SgRelGradoPrecedencia> getGradoSeleccionadoPrecedencias() {
        return gradoSeleccionadoPrecedencias;
    }

    public void setGradoSeleccionadoPrecedencias(List<SgRelGradoPrecedencia> gradoSeleccionadoPrecedencias) {
        this.gradoSeleccionadoPrecedencias = gradoSeleccionadoPrecedencias;
    }

    public SgRelGradoPrecedencia getRelacionPrecedenciaEdicion() {
        return relacionPrecedenciaEdicion;
    }

    public void setRelacionPrecedenciaEdicion(SgRelGradoPrecedencia relacionPrecedenciaEdicion) {
        this.relacionPrecedenciaEdicion = relacionPrecedenciaEdicion;
    }
    
    

}
