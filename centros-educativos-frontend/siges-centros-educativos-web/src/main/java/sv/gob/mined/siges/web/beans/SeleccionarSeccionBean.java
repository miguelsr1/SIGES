/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgRelOpcionProgEd;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumMes;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOpcionProgEd;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.RelOpcionProgEdRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SeleccionarSeccionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SeleccionarSeccionBean.class.getName());

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
    private SeccionRestClient restSeccion;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    @Inject
    private OpcionRestClient restOpcion;

    @Inject
    private RelOpcionProgEdRestClient restProg;

    @Inject
    private SessionBean sessionBean;

    private SgSeccion entidadEnEdicion;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private List<SgRelModEdModAten> relModEdModAtenSelected;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion;
    private SofisComboG<SgGrado> comboGrado;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private SofisComboG<SgSeccion> comboSeccion;
    private SofisComboG<SgOpcion> comboOpcion;
    private SofisComboG<SgProgramaEducativo> comboProgramaEducativo;
    private SofisComboG<EnumMes> comboMeses;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean retornarSoloSeccionesAbiertas = Boolean.TRUE;
    private Boolean callingPostConstruct;

    private String[] incluirCamposSeccion;

    @PostConstruct
    public void init() {
        try {
            callingPostConstruct = Boolean.TRUE;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgSeccion) request.getAttribute("seccion");
            retornarSoloSeccionesAbiertas = (Boolean) request.getAttribute("retornarSoloSeccionesAbiertas");
            incluirCamposSeccion = (String[]) request.getAttribute("incluirCamposSeccion");          
            //securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = (entidadEnEdicion != null);
            cargarCombos();
            callingPostConstruct = Boolean.FALSE;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"minNombre"});

            sedeSeleccionada = null;
            comboNivel = new SofisComboG();
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboMeses = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
            comboMeses.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            cargarSedePorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
            seleccionarSede();
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
//            if (securityOperation != null) {
//                fil.setSecurityOperation(securityOperation);
//            }
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
            return restSede.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
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
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();
            actualizarSeccion();
            if (sedeSeleccionada != null) {
                FiltroNivel fNivel = new FiltroNivel();
                fNivel.setSedPk(sedeSeleccionada.getSedPk());
                fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
                fNivel.setNivHabilitado(Boolean.TRUE);
                fNivel.setOrderBy(new String[]{"nivOrden"});
                fNivel.setAscending(new boolean[]{true});
                List<SgNivel> niveles = restNivel.buscarConCache(fNivel);
                comboNivel = new SofisComboG(niveles, "nivNombre");
                comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (niveles.size() == 1) {
                    comboNivel.setSelectedT(niveles.get(0));
                    seleccionarNivel();
                }
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
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();
            actualizarSeccion();
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
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();
            actualizarSeccion();
            if (comboCiclo.getSelectedT() != null) {
                FiltroModalidad fModalidad = new FiltroModalidad();
                fModalidad.setCicPk(comboCiclo.getSelectedT().getCicPk());
                fModalidad.setAscending(new boolean[]{true});
                fModalidad.setOrderBy(new String[]{"modNombre"});
                fModalidad.setModHabilitado(Boolean.TRUE);
                fModalidad.setIncluirCampos(new String[]{"modNombre", "modVersion"});
                List<SgModalidad> modalidad = restModalidad.buscarConCache(fModalidad);
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
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();
            actualizarSeccion();
            if (comboModalidad.getSelectedT() != null) {
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
                    seleccionarOpcion();
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
                for (SgRelModEdModAten rel : relModEdModAtenSelected) {
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

    public void seleccionarOpcion() {
        try {
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion.setSelected(-1);
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            actualizarSeccion();
            if (comboOpcion.getSelectedT() != null) {
                //Buscar los programas impartidas en esta opcion
                FiltroRelOpcionProgEd fprog = new FiltroRelOpcionProgEd();
                fprog.setRoeOpcionPk(comboOpcion.getSelectedT().getOpcPk());
                List<SgRelOpcionProgEd> relProgramas = restProg.buscarConCache(fprog);
                List<SgProgramaEducativo> programas = new ArrayList<>();
                relProgramas.forEach((p) -> {
                    if (BooleanUtils.isTrue(p.getRoeProgramaEducativo().getPedHabilitado())) {
                        programas.add(p.getRoeProgramaEducativo());
                    }
                });
                comboProgramaEducativo = new SofisComboG(new ArrayList(programas), "pedNombre");
                comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (programas.size() == 1) {
                    comboProgramaEducativo.setSelectedT(programas.get(0));
                }

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarProgramaEducativo() {

        actualizarSeccion();
        comboGrado = new SofisComboG();
        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidadAtencion.setSelected(-1);
        comboProgramaEducativo.setSelectedT(comboProgramaEducativo.getSelectedT());
    }

    public void seleccionarModalidadAtencion() {
        try {
            comboSubModalidadAtencion = null;
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            actualizarSeccion();
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
                    fGrado.setOrderBy(new String[]{"graOrden"});
                    fGrado.setAscending(new boolean[]{true});
                    if (BooleanUtils.isTrue(retornarSoloSeccionesAbiertas)) {
                        fGrado.setGraDefinicionTitulo(Boolean.TRUE);
                    }
                    List<SgGrado> grado = restGrado.buscarConCache(fGrado);
                    comboGrado = new SofisComboG(grado, "graNombre");
                    comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (grado.size() == 1) {
                        comboGrado.setSelectedT(grado.get(0));
                        seleccionarGrado();
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
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            actualizarSeccion();
            if (comboSubModalidadAtencion.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(comboSubModalidadAtencion.getSelectedT().getSmoPk());
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                fGrado.setHabilitado(Boolean.TRUE);
                List<SgGrado> grado = restGrado.buscarConCache(fGrado);
                comboGrado = new SofisComboG(grado, "graNombre");
                comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (grado.size() == 1) {
                    comboGrado.setSelectedT(grado.get(0));
                    seleccionarGrado();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void seleccionarGrado() {
        try {
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            actualizarSeccion();
            if (comboGrado.getSelectedT() != null) {
                FiltroAnioLectivo fal = new FiltroAnioLectivo();
                fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                List<SgAnioLectivo> listAnio = restAnioLectivo.buscarConCache(fal);
                listAnio.sort(Comparator.comparing(SgAnioLectivo::getAleAnio).reversed());
                comboAnioLectivo = new SofisComboG(listAnio, "aleAnio");
                comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (listAnio.size() == 1) {
                    comboAnioLectivo.setSelectedT(listAnio.get(0));
                    seleccionarAnioLectivo();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnioLectivo() {
        try {
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            actualizarSeccion();
            if (comboGrado.getSelectedT() != null && comboAnioLectivo.getSelectedT() != null) {
                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setSecGradoFk(comboGrado.getSelectedT().getGraPk());
                fSeccion.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                fSeccion.setSecSedeFk(this.sedeSeleccionada.getSedPk());
                fSeccion.setSecOpcionFk(comboOpcion.getSelectedT() != null ? comboOpcion.getSelectedT().getOpcPk() : null);
                fSeccion.setSecProgramaEducativoFk(comboProgramaEducativo.getSelectedT() != null ? comboProgramaEducativo.getSelectedT().getPedPk() : null);  
                if (BooleanUtils.isNotFalse(this.retornarSoloSeccionesAbiertas)) { //True or null
                    fSeccion.setSecEstado(EnumSeccionEstado.ABIERTA);
                }
                if (this.incluirCamposSeccion != null) {
                    fSeccion.setIncluirCampos(this.incluirCamposSeccion);
                }
                List<SgSeccion> seccion = restSeccion.buscar(fSeccion);
                comboSeccion = new SofisComboG(new ArrayList(seccion), "nombreSeccionTipoPeriodo");
                comboSeccion.ordenar();
                comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (seccion.size() == 1) {
                    comboSeccion.setSelectedT(seccion.get(0));
                    actualizarSeccion();
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombos() {
        sedeSeleccionada = null;
        seleccionarSede();
    }

    private void actualizarSeccion() {
        try {
            if (BooleanUtils.isTrue(this.callingPostConstruct)) {
                return;
            }
            String expr = "#{controllerParam[actionParam](seleccionarSeccionBean.comboSeccion.selectedT)}";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void setearCombosDadaSeccion(SgSeccion sec) {
        try {
            sedeSeleccionada = sec.getSecServicioEducativo().getSduSede();
            seleccionarSede();
            comboNivel.setSelectedT(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel());
            seleccionarNivel();
            comboCiclo.setSelectedT(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo());
            seleccionarCiclo();
            comboModalidad.setSelectedT(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa());
            seleccionarModalidad();
            if (sec.getSecServicioEducativo().getSduOpcion() != null){
                comboOpcion.setSelectedT(sec.getSecServicioEducativo().getSduOpcion());
                seleccionarOpcion();
                comboProgramaEducativo.setSelectedT(sec.getSecServicioEducativo().getSduProgramaEducativo());
            }
            comboModalidadAtencion.setSelectedT(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion());
            seleccionarModalidadAtencion();
            if (sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null){
                comboSubModalidadAtencion.setSelectedT(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion());
                seleccionarSubModalidadAtencion();
            }
            comboGrado.setSelectedT(sec.getSecServicioEducativo().getSduGrado());
            seleccionarGrado();
            comboAnioLectivo.setSelectedT(sec.getSecAnioLectivo());
            seleccionarAnioLectivo();
            comboSeccion.setSelectedT(sec);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
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

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SofisComboG<SgSeccion> getComboSeccion() {
        return comboSeccion;
    }

    public void setComboSeccion(SofisComboG<SgSeccion> comboSeccion) {
        this.comboSeccion = comboSeccion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSeccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSeccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getRetornarSoloSeccionesAbiertas() {
        return retornarSoloSeccionesAbiertas;
    }

    public void setRetornarSoloSeccionesAbiertas(Boolean retornarSoloSeccionesAbiertas) {
        this.retornarSoloSeccionesAbiertas = retornarSoloSeccionesAbiertas;
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

    public SofisComboG<EnumMes> getComboMeses() {
        return comboMeses;
    }

    public void setComboMeses(SofisComboG<EnumMes> comboMeses) {
        this.comboMeses = comboMeses;
    }

    public String[] getIncluirCamposSeccion() {
        return incluirCamposSeccion;
    }

    public void setIncluirCamposSeccion(String[] incluirCamposSeccion) {
        this.incluirCamposSeccion = incluirCamposSeccion;
    }
    
    
    
}
