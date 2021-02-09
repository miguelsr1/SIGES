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
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgRelOpcionProgEd;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOpcionProgEd;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.RelOpcionProgEdRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SeleccionarGradoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SeleccionarGradoBean.class.getName());
    
    @Inject
    private GradoRestClient restGrado;

    @Inject
    private NivelRestClient nivelRestClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private AnioLectivoRestClient restAnioLectivo;
    
    @Inject
    private OpcionRestClient restOpcion;

    @Inject
    private RelOpcionProgEdRestClient restProg;
    
    @Inject
    private ModalidadRestClient restModalidad;

    @Inject
    private CicloRestClient restCiclo;
    
    @Inject
    private SessionBean sessionBean;

    @Inject
    private OrganizacionCurricularRestClient organizacionCurricularRestClient;
    
    
    private SgGrado entidadEnEdicion;
    private SgOrganizacionCurricular organizacionCurricularSeleccionada;
    
    private SofisComboG<SgOrganizacionCurricular> comboOrganizacionCurricular;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private List<SgRelModEdModAten> relModEdModAtenSelected;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion;
    private SofisComboG<SgGrado> comboGrado;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean retornarSoloSeccionesAbiertas = Boolean.TRUE;
    private Boolean callingPostConstruct;
    private SofisComboG<SgOpcion> comboOpcion;
    private SofisComboG<SgProgramaEducativo> comboProgramaEducativo;

    @PostConstruct
    public void init() {
        try {
            callingPostConstruct = Boolean.TRUE;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgGrado) request.getAttribute("grado");
            retornarSoloSeccionesAbiertas = (Boolean) request.getAttribute("retornarSoloSeccionesAbiertas");
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
            fc.setOrderBy(new String[]{"ocuNombre"});
            fc.setAscending(new boolean[]{true});
            fc.setIncluirCampos(new String[]{"ocuNombre", "ocuVersion"});

            organizacionCurricularSeleccionada = null;
            
            List<SgOrganizacionCurricular> lista = organizacionCurricularRestClient.buscar(fc);      
            
            comboOrganizacionCurricular = new SofisComboG(lista, "ocuNombre");
            comboOrganizacionCurricular.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
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
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarOrganizacionCurricular() {
        try {
            organizacionCurricularSeleccionada = comboOrganizacionCurricular.getSelectedT();
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
            actualizarGrado();
            if (organizacionCurricularSeleccionada != null) {
                FiltroNivel fNivel = new FiltroNivel();
                fNivel.setOrganizacionCurricularPk(organizacionCurricularSeleccionada.getOcuPk());
                fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
                fNivel.setAscending(new boolean[]{true});
                fNivel.setNivHabilitado(Boolean.TRUE);
                fNivel.setOrderBy(new String[]{"nivOrden"});
                List<SgNivel> niveles = nivelRestClient.buscarConCache(fNivel);
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
            actualizarGrado();
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
            actualizarGrado();
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
            actualizarGrado();
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
                    seleccionarOpcion();
                }
                
                
                
                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(comboModalidad.getSelectedT().getModPk());
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

            entidadEnEdicion = null;
            actualizarGrado();
            if (comboOpcion.getSelectedT() != null) {
                //Buscar los programas impartidas en esta opcion
                FiltroRelOpcionProgEd fprog = new FiltroRelOpcionProgEd();
                fprog.setRoeOpcionPk(comboOpcion.getSelectedT().getOpcPk());
                fprog.setOrderBy(new String[]{"roeProgramaEducativo.pedNombre"});
                fprog.setAscending(new boolean[]{true});
                List<SgRelOpcionProgEd> relProgramas = restProg.buscarConCache(fprog);
                List<SgProgramaEducativo> programas = new ArrayList<>();
                relProgramas.forEach((p) -> {
                    if (BooleanUtils.isTrue(p.getRoeProgramaEducativo().getPedHabilitado())){
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
        entidadEnEdicion = null;
        actualizarGrado();
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
            entidadEnEdicion = null;
            actualizarGrado();
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
                    fGrado.setOrderBy(new String[]{"graOrden"});
                    fGrado.setAscending(new boolean[]{true});
                    fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                    if (BooleanUtils.isTrue(retornarSoloSeccionesAbiertas)) {
                        fGrado.setGraDefinicionTitulo(Boolean.TRUE);
                    }
                    List<SgGrado> grado = restGrado.buscarConCache(fGrado);
                    comboGrado = new SofisComboG(grado, "graNombre");
                    comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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
            entidadEnEdicion = null;
            actualizarGrado();
            if (comboSubModalidadAtencion.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(comboSubModalidadAtencion.getSelectedT().getSmoPk());
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                fGrado.setHabilitado(Boolean.TRUE);
                fGrado.setOrderBy(new String[]{"graOrden"});
                fGrado.setAscending(new boolean[]{true});
                List<SgGrado> grado = restGrado.buscar(fGrado);
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
    
    public void limpiarCombos(){
        comboOrganizacionCurricular.setSelected(-1);
        organizacionCurricularSeleccionada = null;
        seleccionarOrganizacionCurricular();
    }
    
    public void actualizarGrado() {
        try {
            if (BooleanUtils.isTrue(this.callingPostConstruct)) {
                return;
            }
            String expr = "#{controllerParam[actionParam](seleccionarGradoBean.comboGrado.selectedT)}";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public GradoRestClient getRestGrado() {
        return restGrado;
    }

    public void setRestGrado(GradoRestClient restGrado) {
        this.restGrado = restGrado;
    }

    public AnioLectivoRestClient getRestAnioLectivo() {
        return restAnioLectivo;
    }

    public void setRestAnioLectivo(AnioLectivoRestClient restAnioLectivo) {
        this.restAnioLectivo = restAnioLectivo;
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

    public ModalidadRestClient getRestModalidad() {
        return restModalidad;
    }

    public void setRestModalidad(ModalidadRestClient restModalidad) {
        this.restModalidad = restModalidad;
    }

    public CicloRestClient getRestCiclo() {
        return restCiclo;
    }

    public void setRestCiclo(CicloRestClient restCiclo) {
        this.restCiclo = restCiclo;
    }

    public OrganizacionCurricularRestClient getOrganizacionCurricularRestClient() {
        return organizacionCurricularRestClient;
    }

    public void setOrganizacionCurricularRestClient(OrganizacionCurricularRestClient organizacionCurricularRestClient) {
        this.organizacionCurricularRestClient = organizacionCurricularRestClient;
    }

    public SgGrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgGrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgOrganizacionCurricular getOrganizacionCurricularSeleccionada() {
        return organizacionCurricularSeleccionada;
    }

    public void setOrganizacionCurricularSeleccionada(SgOrganizacionCurricular organizacionCurricularSeleccionada) {
        this.organizacionCurricularSeleccionada = organizacionCurricularSeleccionada;
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

    public List<SgRelModEdModAten> getRelModEdModAtenSelected() {
        return relModEdModAtenSelected;
    }

    public void setRelModEdModAtenSelected(List<SgRelModEdModAten> relModEdModAtenSelected) {
        this.relModEdModAtenSelected = relModEdModAtenSelected;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getRetornarSoloSeccionesAbiertas() {
        return retornarSoloSeccionesAbiertas;
    }

    public void setRetornarSoloSeccionesAbiertas(Boolean retornarSoloSeccionesAbiertas) {
        this.retornarSoloSeccionesAbiertas = retornarSoloSeccionesAbiertas;
    }

    public Boolean getCallingPostConstruct() {
        return callingPostConstruct;
    }

    public void setCallingPostConstruct(Boolean callingPostConstruct) {
        this.callingPostConstruct = callingPostConstruct;
    } 

    public SofisComboG<SgOrganizacionCurricular> getComboOrganizacionCurricular() {
        return comboOrganizacionCurricular;
    }

    public void setComboOrganizacionCurricular(SofisComboG<SgOrganizacionCurricular> comboOrganizacionCurricular) {
        this.comboOrganizacionCurricular = comboOrganizacionCurricular;
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
    
}
