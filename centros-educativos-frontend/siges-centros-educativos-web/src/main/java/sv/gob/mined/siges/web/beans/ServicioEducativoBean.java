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
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyServicioEducativoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelSedeDiplomado;
import sv.gob.mined.siges.web.lazymodels.LazyRelSedeDiplomadoDataModel;
import sv.gob.mined.siges.web.restclient.RelSedeDiplomadoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ServicioEducativoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ServicioEducativoBean.class.getName());

    @Inject
    private ServicioEducativoRestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private CicloRestClient restCiclo;

    @Inject
    private ModalidadRestClient restModalidad;

    @Inject
    private GradoRestClient restGrado;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private RelSedeDiplomadoRestClient relSedeDiplomadoRestClient;

    private SgServicioEducativo entidadEnEdicion = new SgServicioEducativo();
    private FiltroServicioEducativo filtro = new FiltroServicioEducativo();
    private LazyServicioEducativoDataModel servicioEducativoLazyModel;
    private LazyRelSedeDiplomadoDataModel relSedeDiplomadoLazyModel;
    private List<RevHistorico> historialServicioEducativo = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<EnumServicioEducativoEstado> comboEstadoServicioEducativo;
    private SofisComboG<EnumServicioEducativoEstado> comboEstadoServicioEducativoEdit;
    private SofisComboG<SgNivel> comboNiveles;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgGrado> comboGrado;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private Long sedPkSeleccionado;
    private String sedeNombre;
    private Boolean soloLectura;
    private SgSede sedePadre;
    private List<SgServicioEducativo> listServicioEducativoSedePadre;
    private List<SgServicioEducativo> listServicioEducativoSedePadreSelected;
    private Integer paginadoDiplomado = 10;
    private Long totalResultadosDiplomado;

    public ServicioEducativoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sedePadre = (SgSede) request.getAttribute("sedeEducativa");
            if (sedePadre == null) {
                //ServicioEducativo utilizado como página
            } else {
                //ServicioEducativo como componente dentro de sede
                filtro.setSecSedeFk(sedePadre.getSedPk());
                buscar();
                buscarDiplomados();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SERVICIO_EDUCATIVO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setSduEstado(this.comboEstadoServicioEducativo.getSelectedT() != null ? comboEstadoServicioEducativo.getSelectedT() : null);
            filtro.setSecNivelFk(this.comboNiveles.getSelectedT() != null ? comboNiveles.getSelectedT().getNivPk() : null);
            filtro.setDepartamento(this.comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(this.comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            totalResultados = restClient.buscarTotal(filtro);

            if (this.sedePadre != null) {
                filtro.setIncluirCampos(new String[]{
                    "sduFechaHabilitado",
                    "sduFechaSolicitada",
                    "sduVersion",
                    "sduEstado",
                    "sduGrado.graNombre",
                    "sduGrado.graPk",
                    "sduGrado.graVersion",
                    "sduOpcion.opcNombre",
                    "sduProgramaEducativo.pedNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "sduNumeroTramite",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre"});
            } else {
                filtro.setIncluirCampos(new String[]{
                    "sduSede.sedCodigo",
                    "sduSede.sedDireccion.dirDepartamento.depNombre",
                    "sduSede.sedDireccion.dirMunicipio.munNombre",
                    "sduSede.sedTelefono",
                    "sduFechaHabilitado",
                    "sduFechaSolicitada",
                    "sduVersion",
                    "sduEstado",
                    "sduSede.sedTipo",
                    "sduSede.sedNombre",
                    "sduSede.sedVersion",
                    "sduSede.sedPk",
                    "sduGrado.graNombre",
                    "sduGrado.graPk",
                    "sduGrado.graVersion",
                    "sduOpcion.opcNombre",
                    "sduNumeroTramite",
                    "sduProgramaEducativo.pedNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre"});
            }

            servicioEducativoLazyModel = new LazyServicioEducativoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void buscarDiplomados() {
        try {
            if (sedePadre!=null) {
                FiltroRelSedeDiplomado frsd=new FiltroRelSedeDiplomado();
                frsd.setRsdSedePk(sedePadre.getSedPk());
                frsd.setIncluirCampos(new String[]{"rsdDiplomadoFk.dipCodigo",
                "rsdDiplomadoFk.dipNombre",
                "rsdHabilitado",
                "rsdNumeroTramite"
                });
                totalResultadosDiplomado = relSedeDiplomadoRestClient.buscarTotal(frsd);
                relSedeDiplomadoLazyModel=new LazyRelSedeDiplomadoDataModel(relSedeDiplomadoRestClient,frsd,totalResultadosDiplomado, paginadoDiplomado);
                
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {

        try {
            FiltroNivel fc = new FiltroNivel();

            List<SgNivel> niveles = restNivel.buscar(fc);
            comboNiveles = new SofisComboG(niveles, "nivelOrganizacion");
            comboNiveles.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboNiveles.ordenar();

            List<EnumServicioEducativoEstado> estados = new ArrayList(Arrays.asList(EnumServicioEducativoEstado.values()));
            comboEstadoServicioEducativo = new SofisComboG(estados, "text");
            comboEstadoServicioEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboEstadoServicioEducativoEdit = new SofisComboG(estados, "text");
            comboEstadoServicioEducativoEdit.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fcod = new FiltroCodiguera();
            fcod.setHabilitado(Boolean.TRUE);
            fcod.setAscending(new boolean[]{true});
            fcod.setOrderBy(new String[]{"depNombre"});
            fcod.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fcod);
            comboDepartamento = new SofisComboG(new ArrayList(departamentos), "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboEstadoServicioEducativo.setSelected(-1);
        comboNiveles.setSelected(-1);
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        filtro = new FiltroServicioEducativo();
    }

    public String limpiar() {
        filtro = new FiltroServicioEducativo();
        limpiarCombos();
        servicioEducativoLazyModel = null;
        return null;
    }

    public void actualizar(Long sduPk) {
        try {
            entidadEnEdicion = restClient.obtenerPorId(sduPk);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void entidadEliminar(SgServicioEducativo ser) {
        try {
            entidadEnEdicion = ser;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSduPk());
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
            historialServicioEducativo = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgSede> completeSede(String query) {
        try {
            sedPkSeleccionado = null;
            FiltroSedes fil = new FiltroSedes();
            fil.setSedNombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarSede() {
        try {
            comboNivel = new SofisComboG();
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (sedPkSeleccionado != null) {
                FiltroNivel fNivel = new FiltroNivel();
                fNivel.setSedPk(sedPkSeleccionado);
                fNivel.setAscending(new boolean[]{true});
                fNivel.setOrderBy(new String[]{"nivNombre"});
                fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
                List<SgNivel> nivel = restNivel.buscar(fNivel);
                comboNivel = new SofisComboG(new ArrayList(nivel), "nivNombre");
                comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarNivel() {
        try {
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboNivel.getSelectedT() != null) {
                FiltroCiclo fCiclo = new FiltroCiclo();
                fCiclo.setAscending(new boolean[]{true});
                fCiclo.setOrderBy(new String[]{"cicNombre"});
                fCiclo.setIncluirCampos(new String[]{"cicNombre", "cicVersion"});
                fCiclo.setNivPk(comboNivel.getSelectedT().getNivPk());
                List<SgCiclo> nivel = restCiclo.buscar(fCiclo);
                comboCiclo = new SofisComboG(new ArrayList(nivel), "cicNombre");
                comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
            if (comboCiclo.getSelectedT() != null) {
                FiltroModalidad fModalidad = new FiltroModalidad();
                fModalidad.setAscending(new boolean[]{true});
                fModalidad.setOrderBy(new String[]{"modNombre"});
                fModalidad.setIncluirCampos(new String[]{"modNombre", "modVersion"});
                fModalidad.setCicPk(comboCiclo.getSelectedT().getCicPk());
                List<SgModalidad> modalidad = restModalidad.buscar(fModalidad);
                comboModalidad = new SofisComboG(new ArrayList(modalidad), "modNombre");
                comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModalidad() {
        try {
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboModalidad.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                List<SgGrado> grado = restGrado.buscar(fGrado);
                comboGrado = new SofisComboG(new ArrayList(grado), "graNombre");
                comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio fCod = new FiltroMunicipio();
                fCod.setOrderBy(new String[]{"munNombre"});
                fCod.setAscending(new boolean[]{true});
                fCod.setMunHabilitado(Boolean.TRUE);
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

    public void copiarServiciosSedePadre() {
        try {
            restClient.copiarServicios(sedePadre.getSedPk(), sedePadre.getSedSedeAdscritaDe().getSedPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarServicioSedePadre() {
        try {
            listServicioEducativoSedePadreSelected = new ArrayList();
            listServicioEducativoSedePadre = restClient.buscarServicioPadre(sedePadre.getSedPk(), sedePadre.getSedSedeAdscritaDe().getSedPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarCopiaServicioSedePadre() {
        try {
            if (listServicioEducativoSedePadreSelected.size() > 0) {
                for (SgServicioEducativo ser : listServicioEducativoSedePadreSelected) {
                    ser.setSduSede(sedePadre);
                }
            }
            restClient.copiarServiciosSelectos(listServicioEducativoSedePadreSelected);
            buscar();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public ServicioEducativoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ServicioEducativoRestClient restClient) {
        this.restClient = restClient;
    }

    public NivelRestClient getRestNivel() {
        return restNivel;
    }

    public void setRestNivel(NivelRestClient restNivel) {
        this.restNivel = restNivel;
    }

    public FiltroServicioEducativo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroServicioEducativo filtro) {
        this.filtro = filtro;
    }

    public LazyServicioEducativoDataModel getServicioEducativoLazyModel() {
        return servicioEducativoLazyModel;
    }

    public void setServicioEducativoLazyModel(LazyServicioEducativoDataModel servicioEducativoLazyModel) {
        this.servicioEducativoLazyModel = servicioEducativoLazyModel;
    }

    public List<RevHistorico> getHistorialServicioEducativo() {
        return historialServicioEducativo;
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

    public SgServicioEducativo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgServicioEducativo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<EnumServicioEducativoEstado> getComboEstadoServicioEducativo() {
        return comboEstadoServicioEducativo;
    }

    public void setComboEstadoServicioEducativo(SofisComboG<EnumServicioEducativoEstado> comboEstadoServicioEducativo) {
        this.comboEstadoServicioEducativo = comboEstadoServicioEducativo;
    }

    public SofisComboG<SgNivel> getComboNiveles() {
        return comboNiveles;
    }

    public void setComboNiveles(SofisComboG<SgNivel> comboNiveles) {
        this.comboNiveles = comboNiveles;
    }

    public SofisComboG<EnumServicioEducativoEstado> getComboEstadoServicioEducativoEdit() {
        return comboEstadoServicioEducativoEdit;
    }

    public void setComboEstadoServicioEducativoEdit(SofisComboG<EnumServicioEducativoEstado> comboEstadoServicioEducativoEdit) {
        this.comboEstadoServicioEducativoEdit = comboEstadoServicioEducativoEdit;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public Long getSedPkSeleccionado() {
        return sedPkSeleccionado;
    }

    public void setSedPkSeleccionado(Long sedPkSeleccionado) {
        this.sedPkSeleccionado = sedPkSeleccionado;
    }

    public String getSedeNombre() {
        return sedeNombre;
    }

    public void setSedeNombre(String sedeNombre) {
        this.sedeNombre = sedeNombre;
    }

    public CicloRestClient getRestCiclo() {
        return restCiclo;
    }

    public void setRestCiclo(CicloRestClient restCiclo) {
        this.restCiclo = restCiclo;
    }

    public ModalidadRestClient getRestModalidad() {
        return restModalidad;
    }

    public void setRestModalidad(ModalidadRestClient restModalidad) {
        this.restModalidad = restModalidad;
    }

    public GradoRestClient getRestGrado() {
        return restGrado;
    }

    public void setRestGrado(GradoRestClient restGrado) {
        this.restGrado = restGrado;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
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

    public SofisComboG<SgGrado> getComboGrado() {
        return comboGrado;
    }

    public void setComboGrado(SofisComboG<SgGrado> comboGrado) {
        this.comboGrado = comboGrado;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSede getSedePadre() {
        return sedePadre;
    }

    public void setSedePadre(SgSede sedePadre) {
        this.sedePadre = sedePadre;
    }

    public List<SgServicioEducativo> getListServicioEducativoSedePadre() {
        return listServicioEducativoSedePadre;
    }

    public void setListServicioEducativoSedePadre(List<SgServicioEducativo> listServicioEducativoSedePadre) {
        this.listServicioEducativoSedePadre = listServicioEducativoSedePadre;
    }

    public List<SgServicioEducativo> getListServicioEducativoSedePadreSelected() {
        return listServicioEducativoSedePadreSelected;
    }

    public void setListServicioEducativoSedePadreSelected(List<SgServicioEducativo> listServicioEducativoSedePadreSelected) {
        this.listServicioEducativoSedePadreSelected = listServicioEducativoSedePadreSelected;
    }

    public LazyRelSedeDiplomadoDataModel getRelSedeDiplomadoLazyModel() {
        return relSedeDiplomadoLazyModel;
    }

    public void setRelSedeDiplomadoLazyModel(LazyRelSedeDiplomadoDataModel relSedeDiplomadoLazyModel) {
        this.relSedeDiplomadoLazyModel = relSedeDiplomadoLazyModel;
    }

    public Integer getPaginadoDiplomado() {
        return paginadoDiplomado;
    }

    public void setPaginadoDiplomado(Integer paginadoDiplomado) {
        this.paginadoDiplomado = paginadoDiplomado;
    }

    public Long getTotalResultadosDiplomado() {
        return totalResultadosDiplomado;
    }

    public void setTotalResultadosDiplomado(Long totalResultadosDiplomado) {
        this.totalResultadosDiplomado = totalResultadosDiplomado;
    }

}
