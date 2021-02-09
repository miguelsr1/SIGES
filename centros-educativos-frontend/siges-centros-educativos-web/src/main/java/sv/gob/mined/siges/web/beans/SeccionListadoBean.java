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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySeccionDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyServicioEducativoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PlanesEstudioRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class SeccionListadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SeccionListadoBean.class.getName());

    @Inject
    private SeccionRestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private ServicioEducativoRestClient restServicioEducativo;

    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    @Inject
    private PlanesEstudioRestClient restPlanEstudio;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    private SgSeccion entidadEnEdicion;
    private FiltroServicioEducativo filtroServicioEducativo = new FiltroServicioEducativo();
    private LazySeccionDataModel seccionLazyModel;
    private LazyServicioEducativoDataModel servicioEducativoLazyModel;
    private List<RevHistorico> historialSeccion = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 5;
    private Long totalResultados;
    private Long totalSecciones;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoInsert;
    private SofisComboG<EnumSeccionEstado> comboEstado;
    private SofisComboG<SgPlanEstudio> comboPlanEstudio;
    private SofisComboG<SgJornadaLaboral> comboJornadaLaboral;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgServicioEducativo servicioEducativoSeleccionado;
    private Boolean buscarSedeAdscripta = Boolean.FALSE;
    private SgSede sedeEnEdicion;
    private Boolean soloLectura;
    private SgAnioLectivo anioFiltroPorDefecto = null;
    


    public SeccionListadoBean() {
    }

    @PostConstruct
    public void init() {
        try {

            validarAcceso();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sedeEnEdicion = (SgSede) request.getAttribute("sedeEducativa");

            if (sedeEnEdicion == null) {
                //Seccion utilizado como página
                cargarCombosBusquedaCompleta();
                this.filtrosSeccion.setFiltro(this.filtroServicioEducativo);
                this.filtrosSeccion.setRenderSede(Boolean.TRUE);
                this.filtrosSeccion.setRenderProgramaOpcion(Boolean.TRUE);
                this.filtrosSeccion.setRenderSeccion(Boolean.FALSE);
                this.filtrosSeccion.cargarCombos();
                this.filtrosSeccion.seleccionarUltimoAnio();
            } else {
                //Utilizado como componente dentro de sede
                this.filtrosSeccion.setFiltro(this.filtroServicioEducativo);
                this.filtrosSeccion.setRenderSede(Boolean.FALSE);
                this.filtrosSeccion.setRenderProgramaOpcion(Boolean.TRUE);
                this.filtrosSeccion.setRenderSeccion(Boolean.FALSE);
                this.filtrosSeccion.cargarCombos();
                this.filtrosSeccion.seleccionarUltimoAnio();
                this.filtrosSeccion.cargarSedeSeleccionada(sedeEnEdicion);
                filtrosSeccion.getFiltro().setSecSedeFk(sedeEnEdicion.getSedPk());
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void forceInit() {
        //Utilizado para forzar init de SeccionBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SECCION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void actualizarSedeConSedeAdscripta() {
        filtroServicioEducativo.setSecSedeFk(sedeEnEdicion.getSedPk());
        filtroServicioEducativo.setSduSedeAdscripta(buscarSedeAdscripta ? sedeEnEdicion.getSedSedeAdscritaDe().getSedPk() : null);
        buscar();

    }

    
    public String buscar() {
        try {
           
            filtrosSeccion.getFiltro().setDepartamento(comboDepartamento != null && comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtrosSeccion.getFiltro().setMunicipio(comboMunicipio != null && comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);

          
            totalResultados = restClient.buscarTotal(filtrosSeccion.getFiltro());
            filtrosSeccion.getFiltro().setIncluirCampos(new String[]{
                "secAnioLectivo.alePk",
                "secAnioLectivo.aleAnio",
                "secAnioLectivo.aleVersion",
                "secNombre",
                "secServicioEducativo.sduPk",
                "secServicioEducativo.sduVersion",
                "secJornadaLaboral.jlaNombre",
                "secVersion"});
            seccionLazyModel = new LazySeccionDataModel(restClient, filtrosSeccion.getFiltro(), totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombosBusquedaCompleta() {

        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{false});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamento = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(listaDepartamento, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarCombos() {

        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            List<SgJornadaLaboral> listaJornadaLaboral = restCatalogo.buscarJornadasLaborales(fc);
            comboJornadaLaboral = new SofisComboG(listaJornadaLaboral, "jlaNombre");
            comboJornadaLaboral.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboPlanEstudio = new SofisComboG();
            comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAscending(new boolean[]{false});
            fal.setOrderBy(new String[]{"aleAnio"});
            List<SgAnioLectivo> anioLectivo = restAnioLectivo.buscar(fal);
            comboAnioLectivoInsert = new SofisComboG(anioLectivo, "aleAnio");
            comboAnioLectivoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumSeccionEstado> estados = new ArrayList(Arrays.asList(EnumSeccionEstado.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        if (comboEstado != null) {
            comboEstado.setSelected(-1);
        }
        if (comboAnioLectivoInsert != null) {
            comboAnioLectivoInsert.setSelected(-1);
        }
        if (comboJornadaLaboral != null) {
            comboJornadaLaboral.setSelected(-1);
        }

    }

    private void limpiarCombosBusqueda() {
        if (comboDepartamento != null) {
            comboDepartamento.setSelected(-1);
        }
        if (comboMunicipio != null) {
            comboMunicipio.setSelected(-1);
        }

    }

    public String limpiar() {
        //filtroServicioEducativo = new FiltroServicioEducativo();
        this.filtrosSeccion.limpiarCombos();
        limpiarCombosBusqueda();
        limpiarCombos();     
        return null;
    }

    public void agregar(SgServicioEducativo servicioEducativo) {
        try {
            limpiarCombos();
            if (servicioEducativo != null) {

                if (comboAnioLectivoInsert == null) {
                    cargarCombos();
                }

                FiltroPlanEstudio fpe = new FiltroPlanEstudio();
                fpe.setServicioEducativoFk(servicioEducativo.getSduPk());
                List<SgPlanEstudio> listPlanEst = restPlanEstudio.buscar(fpe);
                comboPlanEstudio = new SofisComboG(listPlanEst, "pesNombre");
                comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (listPlanEst.size() == 1) {
                    comboPlanEstudio.setSelectedT(listPlanEst.get(0));
                }

                entidadEnEdicion = new SgSeccion();
                entidadEnEdicion.setSecServicioEducativo(servicioEducativo);
                servicioEducativoSeleccionado = servicioEducativo;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgServicioEducativo servicio, SgSeccion var) {
        try {
            limpiarCombos();
            if (comboAnioLectivoInsert == null) {
                cargarCombos();
            }
            entidadEnEdicion = this.restClient.obtenerPorId(var.getSecPk());
            servicioEducativoSeleccionado = servicio;

            FiltroPlanEstudio fpe = new FiltroPlanEstudio();
            fpe.setServicioEducativoFk(servicio.getSduPk());
            List<SgPlanEstudio> listPlanEst = restPlanEstudio.buscar(fpe);
            comboPlanEstudio = new SofisComboG(listPlanEst, "pesNombre");
            comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboPlanEstudio.setSelectedT(entidadEnEdicion.getSecPlanEstudio());
            comboJornadaLaboral.setSelectedT(entidadEnEdicion.getSecJornadaLaboral());
            comboAnioLectivoInsert.setSelectedT(entidadEnEdicion.getSecAnioLectivo());
            comboEstado.setSelectedT(entidadEnEdicion.getSecEstado());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setSecJornadaLaboral(comboJornadaLaboral.getSelectedT() != null ? comboJornadaLaboral.getSelectedT() : null);
            entidadEnEdicion.setSecPlanEstudio(comboPlanEstudio.getSelectedT() != null ? comboPlanEstudio.getSelectedT() : null);
            entidadEnEdicion.setSecEstado(this.comboEstado.getSelectedT() != null ? comboEstado.getSelectedT() : null);
            entidadEnEdicion.setSecServicioEducativo(servicioEducativoSeleccionado != null ? servicioEducativoSeleccionado : null);
            entidadEnEdicion.setSecAnioLectivo(this.comboAnioLectivoInsert.getSelectedT() != null ? comboAnioLectivoInsert.getSelectedT() : null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
       
            servicioEducativoSeleccionado = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSecPk());
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
            historialSeccion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio fCod = new FiltroMunicipio();
                fCod.setOrderBy(new String[]{"munNombre"});
                fCod.setAscending(new boolean[]{true});
                fCod.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fCod.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fCod);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
        public void actualizarSoloSeccion(SgSeccion var) {
        try {
            limpiarCombos();
            if (comboAnioLectivoInsert == null) {
                cargarCombos();
            }
            entidadEnEdicion = this.restClient.obtenerPorId(var.getSecPk());
            servicioEducativoSeleccionado = restServicioEducativo.obtenerPorId(var.getSecServicioEducativo().getSduPk());

            FiltroPlanEstudio fpe = new FiltroPlanEstudio();
            fpe.setServicioEducativoFk(servicioEducativoSeleccionado.getSduPk());
            List<SgPlanEstudio> listPlanEst = restPlanEstudio.buscar(fpe);
            comboPlanEstudio = new SofisComboG(listPlanEst, "pesNombre");
            comboPlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboPlanEstudio.setSelectedT(entidadEnEdicion.getSecPlanEstudio());
            comboJornadaLaboral.setSelectedT(entidadEnEdicion.getSecJornadaLaboral());
            comboAnioLectivoInsert.setSelectedT(entidadEnEdicion.getSecAnioLectivo());
            comboEstado.setSelectedT(entidadEnEdicion.getSecEstado());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SeccionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SeccionRestClient restClient) {
        this.restClient = restClient;
    }

    public LazySeccionDataModel getSeccionLazyModel() {
        return seccionLazyModel;
    }

    public void setSeccionLazyModel(LazySeccionDataModel seccionLazyModel) {
        this.seccionLazyModel = seccionLazyModel;
    }

    public List<RevHistorico> getHistorialSeccion() {
        return historialSeccion;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public SgSeccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSeccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public AnioLectivoRestClient getRestAnioLectivo() {
        return restAnioLectivo;
    }

    public void setRestAnioLectivo(AnioLectivoRestClient restAnioLectivo) {
        this.restAnioLectivo = restAnioLectivo;
    }

    public ServicioEducativoRestClient getRestServicioEducativo() {
        return restServicioEducativo;
    }

    public void setRestServicioEducativo(ServicioEducativoRestClient restServicioEducativo) {
        this.restServicioEducativo = restServicioEducativo;
    }

    public FiltroServicioEducativo getFiltroServicioEducativo() {
        return filtroServicioEducativo;
    }

    public void setFiltroServicioEducativo(FiltroServicioEducativo filtroServicioEducativo) {
        this.filtroServicioEducativo = filtroServicioEducativo;
    }

    public LazyServicioEducativoDataModel getServicioEducativoLazyModel() {
        return servicioEducativoLazyModel;
    }

    public void setServicioEducativoLazyModel(LazyServicioEducativoDataModel servicioEducativoLazyModel) {
        this.servicioEducativoLazyModel = servicioEducativoLazyModel;
    }

    public SofisComboG<EnumSeccionEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumSeccionEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SgServicioEducativo getServicioEducativoSeleccionado() {
        return servicioEducativoSeleccionado;
    }

    public void setServicioEducativoSeleccionado(SgServicioEducativo servicioEducativoSeleccionado) {
        this.servicioEducativoSeleccionado = servicioEducativoSeleccionado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoInsert() {
        return comboAnioLectivoInsert;
    }

    public void setComboAnioLectivoInsert(SofisComboG<SgAnioLectivo> comboAnioLectivoInsert) {
        this.comboAnioLectivoInsert = comboAnioLectivoInsert;
    }

    public SofisComboG<SgPlanEstudio> getComboPlanEstudio() {
        return comboPlanEstudio;
    }

    public void setComboPlanEstudio(SofisComboG<SgPlanEstudio> comboPlanEstudio) {
        this.comboPlanEstudio = comboPlanEstudio;
    }

    public SofisComboG<SgJornadaLaboral> getComboJornadaLaboral() {
        return comboJornadaLaboral;
    }

    public void setComboJornadaLaboral(SofisComboG<SgJornadaLaboral> comboJornadaLaboral) {
        this.comboJornadaLaboral = comboJornadaLaboral;
    }

    public PlanesEstudioRestClient getRestPlanEstudio() {
        return restPlanEstudio;
    }

    public void setRestPlanEstudio(PlanesEstudioRestClient restPlanEstudio) {
        this.restPlanEstudio = restPlanEstudio;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public NivelRestClient getRestNivel() {
        return restNivel;
    }

    public void setRestNivel(NivelRestClient restNivel) {
        this.restNivel = restNivel;
    }

    public Boolean getBuscarSedeAdscripta() {
        return buscarSedeAdscripta;
    }

    public void setBuscarSedeAdscripta(Boolean buscarSedeAdscripta) {
        this.buscarSedeAdscripta = buscarSedeAdscripta;
    }

    public SgSede getSedeEnEdicion() {
        return sedeEnEdicion;
    }

    public void setSedeEnEdicion(SgSede sedeEnEdicion) {
        this.sedeEnEdicion = sedeEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgAnioLectivo getAnioFiltroPorDefecto() {
        return anioFiltroPorDefecto;
    }

    public void setAnioFiltroPorDefecto(SgAnioLectivo anioFiltroPorDefecto) {
        this.anioFiltroPorDefecto = anioFiltroPorDefecto;
    }

    public Long getTotalSecciones() {
        return totalSecciones;
    }

    public void setTotalSecciones(Long totalSecciones) {
        this.totalSecciones = totalSecciones;
    }
    
    

}
