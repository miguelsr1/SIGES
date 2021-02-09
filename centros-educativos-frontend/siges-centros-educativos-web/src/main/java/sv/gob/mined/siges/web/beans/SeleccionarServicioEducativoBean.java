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
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.RelOpcionProgEdRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SeleccionarServicioEducativoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SeleccionarServicioEducativoBean.class.getName());

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
    private ServicioEducativoRestClient restServicioEducativo;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private OpcionRestClient restOpcion;

    @Inject
    private RelOpcionProgEdRestClient restProg;

    private SgServicioEducativo entidadEnEdicion;
    private SgServicioEducativo entidadEnEdicionCopia;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgOpcion> comboOpcion;
    private SofisComboG<SgProgramaEducativo> comboProgramaEducativo;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion;
    private List<SgRelModEdModAten> relModEdModAtenSelected;
    private SofisComboG<SgGrado> comboGrado;
    private String securityOperation;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean ejecutarAction = Boolean.TRUE;
    private Boolean verGrado = Boolean.TRUE;
    private Boolean soloSede = Boolean.FALSE;
    private Boolean verCiclo = Boolean.TRUE;
    private Boolean verProgramaEducativo = Boolean.TRUE;
    private Boolean soloLecturaCombos = Boolean.FALSE;
    private Boolean verDeptoMun = Boolean.FALSE;
    private String idError=null;

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgServicioEducativo) request.getAttribute("servicioEducativo");
            entidadEnEdicionCopia=(SgServicioEducativo) request.getAttribute("servicioEducativo");
            soloLectura = (entidadEnEdicion != null);
            securityOperation = (String) request.getAttribute("securityOperation");
             idError= (String) request.getAttribute("idError");
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {
        try {

            sedeSeleccionada = null;
            comboNivel = new SofisComboG();
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
    
    public void cargarServicioEducativo(){
        if(entidadEnEdicionCopia!=null){
                sedeSeleccionada=entidadEnEdicionCopia.getSduSede();
                seleccionarSede();
                comboNivel.setSelectedT(entidadEnEdicionCopia.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel());
                seleccionarNivel();
                comboCiclo.setSelectedT(entidadEnEdicionCopia.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo());
                this.seleccionarCiclo();
                comboModalidad.setSelectedT(entidadEnEdicionCopia.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa());
                this.seleccionarModalidad();
                comboModalidadAtencion.setSelectedT(entidadEnEdicionCopia.getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion());
                this.seleccionarModalidadAtencion();
                if(entidadEnEdicionCopia.getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion()!=null){
                    comboSubModalidadAtencion.setSelectedT(entidadEnEdicionCopia.getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion());
                    this.seleccionarSubModalidadAtencion();
                }
                comboGrado.setSelectedT(entidadEnEdicionCopia.getSduGrado());
                this.seleccionarGrado();
                
            }
    }
    
    

    public void cargarSedePorDefecto() {
        if (sessionBean.getSedeDefecto() != null){
            sedeSeleccionada = sessionBean.getSedeDefecto();
            seleccionarSede();
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            if (securityOperation != null) {
                fil.setSecurityOperation(securityOperation);
            }
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
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
            entidadEnEdicion = null;
            actualizarServicioEducativo();
            if (sedeSeleccionada != null && !soloSede) {
                FiltroNivel fNivel = new FiltroNivel();
                fNivel.setSedPk(sedeSeleccionada.getSedPk());
                fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
                fNivel.setAscending(new boolean[]{true});
                fNivel.setNivHabilitado(Boolean.TRUE);
                fNivel.setOrderBy(new String[]{"nivNombre"});
                List<SgNivel> niveles = restNivel.buscarConCache(fNivel);
                
                
                comboNivel = new SofisComboG(niveles, "nivNombre");
                comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                
                if (niveles.size() == 1) {
                    comboNivel.setSelectedT(niveles.get(0));
                    seleccionarNivel();
                }
            }
            controllParamSede();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    private void controllParamSede() {
            String expr = "#{controllerParam2[actionParam3]()}";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
       
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

            entidadEnEdicion = null;
            relModEdModAtenSelected = new ArrayList<>();
            actualizarServicioEducativo();
            if (comboNivel.getSelectedT() != null) {
                FiltroCiclo fCiclo = new FiltroCiclo();
                fCiclo.setAscending(new boolean[]{true});
                fCiclo.setOrderBy(new String[]{"cicNombre"});
                fCiclo.setIncluirCampos(new String[]{"cicNombre", "cicVersion"});
                fCiclo.setCicHabilitado(Boolean.TRUE);
                fCiclo.setNivPk(comboNivel.getSelectedT().getNivPk());
                fCiclo.setSedePk(sedeSeleccionada.getSedPk());
                List<SgCiclo> ciclos = restCiclo.buscarConCache(fCiclo);
                
                
                comboCiclo = new SofisComboG(ciclos, "cicNombre");
                comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                
                if (ciclos.size() == 1 || verCiclo.equals(Boolean.FALSE)) {
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
            entidadEnEdicion = null;
            actualizarServicioEducativo();
            if (comboCiclo.getSelectedT() != null) {
                FiltroModalidad fModalidad = new FiltroModalidad();
                fModalidad.setCicPk(verCiclo.equals(Boolean.FALSE)?null: comboCiclo.getSelectedT().getCicPk());
                fModalidad.setAscending(new boolean[]{true});
                fModalidad.setOrderBy(new String[]{"modNombre"});
                fModalidad.setModHabilitado(Boolean.TRUE);
                fModalidad.setSedePk(sedeSeleccionada.getSedPk());
                fModalidad.setNivel(comboNivel.getSelectedT().getNivPk());
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
            entidadEnEdicion = null;
            actualizarServicioEducativo();
            if (comboModalidad.getSelectedT() != null) {
                //Buscar las opciones impartidas en esta modalidad
                FiltroServicioEducativo fse=new FiltroServicioEducativo();
                fse.setSecSedeFk(sedeSeleccionada.getSedPk());
                fse.setSecModalidadEducativaPk(comboModalidad.getSelectedT().getModPk());
                fse.setIncluirCampos(new String[]{"sduOpcion.opcPk","sduOpcion.opcNombre", "sduOpcion.opcVersion"});
                List<SgServicioEducativo> servEdu=restServicioEducativo.buscar(fse);
                List<SgOpcion> opciones =new ArrayList();                
                for(SgServicioEducativo serv:servEdu){
                    if(serv.getSduOpcion()!=null && !opciones.contains(serv.getSduOpcion())){
                        opciones.add(serv.getSduOpcion());
                    }
                }
              
              /*  FiltroOpciones fopcion = new FiltroOpciones();
                fopcion.setOpcModalidadPk(comboModalidad.getSelectedT().getModPk());
                fopcion.setIncluirCampos(new String[]{"opcNombre", "opcVersion"});
                fopcion.setHabilitado(Boolean.TRUE);
                fopcion.setAscending(new boolean[]{true});
                fopcion.setOrderBy(new String[]{"opcNombre"});
                List<SgOpcion> opciones = restOpcion.buscarConCache(fopcion);*/
                
                
                
                comboOpcion = new SofisComboG(new ArrayList(opciones), "opcNombre");
                comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (opciones.size() == 1) {
                    comboOpcion.setSelectedT(opciones.get(0));
                    seleccionarOpcion();
                }

                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(comboModalidad.getSelectedT().getModPk());
                filtroRel.setSedePk(sedeSeleccionada.getSedPk());
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

    public void seleccionarOpcion() {
        try {
            comboProgramaEducativo = new SofisComboG();
            comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion.setSelected(-1);
            comboSubModalidadAtencion = null;

            entidadEnEdicion = null;
            actualizarServicioEducativo();
            if (comboOpcion.getSelectedT() != null) {
                //Buscar los programas impartidas en esta opcion
                FiltroServicioEducativo fse=new FiltroServicioEducativo();
                fse.setSecSedeFk(sedeSeleccionada.getSedPk());
                fse.setSecModalidadEducativaPk(comboModalidad.getSelectedT().getModPk());
                fse.setSecOpcionFk(comboOpcion.getSelectedT().getOpcPk());
                fse.setIncluirCampos(new String[]{"sduProgramaEducativo.pedPk","sduProgramaEducativo.pedNombre", "sduProgramaEducativo.pedVersion"});
                List<SgServicioEducativo> servEdu=restServicioEducativo.buscar(fse);
                 List<SgProgramaEducativo> programas =new ArrayList();                
                for(SgServicioEducativo serv:servEdu){
                    if(serv.getSduProgramaEducativo()!=null && !programas.contains(serv.getSduProgramaEducativo())){
                        programas.add(serv.getSduProgramaEducativo());
                    }
                }
               /* 
                FiltroRelOpcionProgEd fprog = new FiltroRelOpcionProgEd();
                fprog.setRoeOpcionPk(comboOpcion.getSelectedT().getOpcPk());
                List<SgRelOpcionProgEd> relProgramas = restProg.buscarConCache(fprog);
                List<SgProgramaEducativo> programas = new ArrayList<>();
                for(SgRelOpcionProgEd p : relProgramas){
                    if (BooleanUtils.isTrue(p.getRoeProgramaEducativo().getPedHabilitado())){
                        programas.add(p.getRoeProgramaEducativo());
                    }
                }*/
                
                
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

        entidadEnEdicion = null;
        actualizarServicioEducativo();
        comboGrado = new SofisComboG();
        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidadAtencion.setSelected(-1);
        comboProgramaEducativo.setSelectedT(comboProgramaEducativo.getSelectedT());
        comboSubModalidadAtencion = null;
    }

    public void seleccionarModalidadAtencion() {
        try {
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidadAtencion = null;

            entidadEnEdicion = null;
            actualizarServicioEducativo();
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
                    List<SgGrado> grado = restGrado.buscarConCache(fGrado);


                    if(!grado.isEmpty()){
                        comboGrado = new SofisComboG(grado, "graNombre");
                        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }


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
            actualizarServicioEducativo();
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
            entidadEnEdicion = null;
            if (comboGrado.getSelectedT() != null) {
                FiltroServicioEducativo fservicio = new FiltroServicioEducativo();
                fservicio.setSecSedeFk(sedeSeleccionada.getSedPk());
                fservicio.setSecGradoFk(comboGrado.getSelectedT().getGraPk());
                fservicio.setSecOpcionFk(comboOpcion.getSelectedT() != null ? comboOpcion.getSelectedT().getOpcPk() : null);
                fservicio.setSecProgramaEducativoFk(comboProgramaEducativo.getSelectedT() != null ? comboProgramaEducativo.getSelectedT().getPedPk() : null);
                fservicio.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
                fservicio.setSecSubModalidadAtencionFk(comboSubModalidadAtencion!=null?comboSubModalidadAtencion.getSelectedT()!=null?comboSubModalidadAtencion.getSelectedT().getSmoPk():null:null);

                List<SgServicioEducativo> servicios = restServicioEducativo.buscar(fservicio);
                if (servicios.size() > 1) {             
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SERVICIOS_MULTIPLES), "");                 
                } else if (servicios.size() == 1) {
                    entidadEnEdicion = servicios.get(0);
                } else {                    
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SERVICIO_EDUCATIVO_NO_ENCONTRADO), "");
                                      
                }
            }
            actualizarServicioEducativo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void actualizarServicioEducativo() {
        if(ejecutarAction){
            String expr = "#{controllerParam2[actionParam2](seleccionarServicioEducativoBean.entidadEnEdicion)}";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
        }
    }
    
    public void cargarServicioEducativo(SgServicioEducativo serv){
        entidadEnEdicion = serv;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public NivelRestClient getRestNivel() {
        return restNivel;
    }

    public void setRestNivel(NivelRestClient restNivel) {
        this.restNivel = restNivel;
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

    public ServicioEducativoRestClient getRestServicioEducativo() {
        return restServicioEducativo;
    }

    public void setRestServicioEducativo(ServicioEducativoRestClient restServicioEducativo) {
        this.restServicioEducativo = restServicioEducativo;
    }

    public RelModEdModAtenRestClient getRelModRestClient() {
        return relModRestClient;
    }

    public void setRelModRestClient(RelModEdModAtenRestClient relModRestClient) {
        this.relModRestClient = relModRestClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SgServicioEducativo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgServicioEducativo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public List<SgRelModEdModAten> getRelModEdModAtenSelected() {
        return relModEdModAtenSelected;
    }

    public void setRelModEdModAtenSelected(List<SgRelModEdModAten> relModEdModAtenSelected) {
        this.relModEdModAtenSelected = relModEdModAtenSelected;
    }

    public SofisComboG<SgGrado> getComboGrado() {
        return comboGrado;
    }

    public void setComboGrado(SofisComboG<SgGrado> comboGrado) {
        this.comboGrado = comboGrado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public OpcionRestClient getRestOpcion() {
        return restOpcion;
    }

    public void setRestOpcion(OpcionRestClient restOpcion) {
        this.restOpcion = restOpcion;
    }

    public RelOpcionProgEdRestClient getRestProg() {
        return restProg;
    }

    public void setRestProg(RelOpcionProgEdRestClient restProg) {
        this.restProg = restProg;
    }

    public Boolean getEjecutarAction() {
        return ejecutarAction;
    }

    public void setEjecutarAction(Boolean ejecutarAction) {
        this.ejecutarAction = ejecutarAction;
    }

    public Boolean getVerGrado() {
        return verGrado;
    }

    public void setVerGrado(Boolean verGrado) {
        this.verGrado = verGrado;
    }

    public Boolean getSoloSede() {
        return soloSede;
    }

    public void setSoloSede(Boolean soloSede) {
        this.soloSede = soloSede;
    }

    public Boolean getVerCiclo() {
        return verCiclo;
    }

    public void setVerCiclo(Boolean verCiclo) {
        this.verCiclo = verCiclo;
    }

    public Boolean getVerProgramaEducativo() {
        return verProgramaEducativo;
    }

    public void setVerProgramaEducativo(Boolean verProgramaEducativo) {
        this.verProgramaEducativo = verProgramaEducativo;
    }

    public Boolean getSoloLecturaCombos() {
        return soloLecturaCombos;
    }

    public void setSoloLecturaCombos(Boolean soloLecturaCombos) {
        this.soloLecturaCombos = soloLecturaCombos;
    }

    public SofisComboG<SgSubModalidadAtencion> getComboSubModalidadAtencion() {
        return comboSubModalidadAtencion;
    }

    public void setComboSubModalidadAtencion(SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion) {
        this.comboSubModalidadAtencion = comboSubModalidadAtencion;
    }

    public Boolean getVerDeptoMun() {
        return verDeptoMun;
    }

    public void setVerDeptoMun(Boolean verDeptoMun) {
        this.verDeptoMun = verDeptoMun;
    }

    public SgServicioEducativo getEntidadEnEdicionCopia() {
        return entidadEnEdicionCopia;
    }

    public void setEntidadEnEdicionCopia(SgServicioEducativo entidadEnEdicionCopia) {
        this.entidadEnEdicionCopia = entidadEnEdicionCopia;
    }

    public String getIdError() {
        return idError;
    }

    public void setIdError(String idError) {
        this.idError = idError;
    }

}
