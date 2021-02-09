/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgConsultaCalifiacionesAreasBasicas;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.dto.centros_educativos.SgCiclo;
import sv.gob.mined.siges.web.dto.centros_educativos.SgGrado;
import sv.gob.mined.siges.web.dto.centros_educativos.SgModalidad;
import sv.gob.mined.siges.web.dto.centros_educativos.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.centros_educativos.SgNivel;
import sv.gob.mined.siges.web.dto.centros_educativos.SgOpcion;
import sv.gob.mined.siges.web.dto.centros_educativos.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.centros_educativos.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPromedioCalificaciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SistemaIntegradoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class IndicadoresBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(IndicadoresBean.class.getName());

    @Inject
    private SistemaIntegradoRestClient sistemaIntegradoRestClient;
    @Inject
    private NivelRestClient nivelClient;
    @Inject
    private CicloRestClient cicloClient;
    @Inject
    private GradoRestClient gradoClient;
    @Inject
    private ModalidadRestClient modalidadClient;
    @Inject
    private RelModEdModAtenRestClient relModRestClient;
    @Inject
    private OpcionRestClient opcionClient;
    @Inject
    private CatalogosRestClient catalogoClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private SeccionRestClient restSeccion;
    @Inject
    private SedeRestClient restSede;
    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgGrado> comboGrado;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private List<SgRelModEdModAten> relModEdModAtenSelected;
    private SofisComboG<SgOpcion> comboOpcion;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidad;
    private SofisComboG<SgProgramaEducativo> comboProgramaEducativo;
    private Boolean soloLectura = Boolean.FALSE;
    private SgSistemaIntegrado sistemaIntegrado;
    private List<SgConsultaCalifiacionesAreasBasicas> calificacionesPorArea;
    private FiltroPromedioCalificaciones filtro = new FiltroPromedioCalificaciones();
    private SgSede sedeSeleccionada;

    public IndicadoresBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sistemaIntegrado = (SgSistemaIntegrado) request.getAttribute("sistemaIntegrado");
            if (sistemaIntegrado != null) {
                cargarCombos();
                cargarCalificacionesPorArea();

            } else {
                throw new Exception("No sistema integrado found");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombos() {
        try {
            FiltroNivel filtroNivel = new FiltroNivel();
            filtroNivel.setOrderBy(new String[]{"nivNombre"});
            filtroNivel.setAscending(new boolean[]{true});
            filtroNivel.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
            List<SgNivel> listaNivel = nivelClient.buscar(filtroNivel);
            comboNivel = new SofisComboG(listaNivel, "nivNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if (comboProgramaEducativo == null) {
                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setAscending(new boolean[]{true});
                fc.setOrderBy(new String[]{"pedNombre"});
                fc.setIncluirCampos(new String[]{"pedPk", "pedNombre", "pedVersion"});
                List<SgProgramaEducativo> listaPE = catalogoClient.buscarProgramasEducativos(fc);
                comboProgramaEducativo = new SofisComboG(listaPE, "pedNombre");
                comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboNivel.setSelected(-1);
        comboProgramaEducativo.setSelected(-1);
        comboModalidadAtencion.setSelected(-1);
        comboSubModalidad.setSelected(-1);
        
        comboCiclo = new SofisComboG<>();
        comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboModalidad = new SofisComboG<>();
        comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboGrado = new SofisComboG<>();
        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboOpcion = new SofisComboG<>();
        comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboSubModalidad = new SofisComboG<>();
        comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        this.sedeSeleccionada = null;
        filtro.setSedePk(null);
    }

    public void limpiar() {
        limpiarCombos();
        filtro = new FiltroPromedioCalificaciones();
        calificacionesPorArea = null;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
    }

    public void cargarCalificacionesPorArea() {
        try {
            filtro.setSistemaPk(this.sistemaIntegrado.getSinPk());
            filtro.setSedePk(this.sedeSeleccionada != null ? this.sedeSeleccionada.getSedPk() : null);
            filtro.setCicloPk((comboCiclo != null) ? comboCiclo.getSelectedT() != null ? comboCiclo.getSelectedT().getCicPk() : null : null);
            filtro.setNivelPk((comboNivel != null) ? comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null : null);
            filtro.setGradoPk((comboGrado != null) ? comboGrado.getSelectedT() != null ? comboGrado.getSelectedT().getGraPk() : null : null);
            filtro.setSedModalidadEducativaPk((comboModalidad != null) ? comboModalidad.getSelectedT() != null ? comboModalidad.getSelectedT().getModPk() : null : null);
            filtro.setSedOpcionPk((comboOpcion != null) ? comboOpcion.getSelectedT() != null ? comboOpcion.getSelectedT().getOpcPk() : null : null);
            filtro.setGradoPk((comboGrado != null) ? comboGrado.getSelectedT() != null ? comboGrado.getSelectedT().getGraPk() : null : null);
            filtro.setSedProgramaEducativoPk((comboProgramaEducativo != null) ? comboProgramaEducativo.getSelectedT() != null ? comboProgramaEducativo.getSelectedT().getPedPk() : null : null);
            filtro.setSedModalidadAtencionPk((comboModalidadAtencion != null) ? comboModalidadAtencion.getSelectedT() != null ? comboModalidadAtencion.getSelectedT().getMatPk() : null : null);
            filtro.setSedSubModalidadPk((comboSubModalidad != null) ? comboSubModalidad.getSelectedT() != null ? comboSubModalidad.getSelectedT().getSmoPk() : null : null);
            calificacionesPorArea = sistemaIntegradoRestClient.obtenerAsistenciasPorSedeEnNivel(filtro);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void deseleccionarSede(AjaxBehaviorEvent event) {
        this.sedeSeleccionada = null;
        filtro.setSedePk(null);
    }

    public void seleccionarSede() {
        try {
            comboNivel.setSelected(0);
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            filtro.setSedePk(null);
            filtro.setNivelPk(null);
            filtro.setCicloPk(null);
            filtro.setSedModalidadAtencionPk(null);
            filtro.setSedSubModalidadPk(null);
            filtro.setSedModalidadEducativaPk(null);
            filtro.setGradoPk(null);
            filtro.setSecPk(null);
            filtro.setSedOpcionPk(null);

            if (this.sedeSeleccionada != null) {
                filtro.setSedePk(this.sedeSeleccionada.getSedPk());
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
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            if (comboNivel.getSelectedT() != null) {
                FiltroCiclo fc = new FiltroCiclo();
                fc.setOrderBy(new String[]{"cicNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setNivPk(comboNivel.getSelectedT().getNivPk());
                fc.setIncluirCampos(new String[]{"cicPk", "cicNombre", "cicVersion"});

                List<SgCiclo> ciclos = cicloClient.buscar(fc);
                comboCiclo = new SofisComboG(ciclos, "cicNombre");
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
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            if (comboCiclo.getSelectedT() != null) {
                FiltroModalidad fc = new FiltroModalidad();
                fc.setOrderBy(new String[]{"modNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setCicPk(comboCiclo.getSelectedT().getCicPk());
                fc.setIncluirCampos(new String[]{"modPk", "modNombre", "modVersion"});

                List<SgModalidad> modalidad = modalidadClient.buscar(fc);
                comboModalidad = new SofisComboG(modalidad, "modNombre");
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
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            if (comboModalidad.getSelectedT() != null) {

                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(comboModalidad.getSelectedT().getModPk());
                List<SgModalidadAtencion> listaMA = new ArrayList();
                relModEdModAtenSelected = relModRestClient.buscar(filtroRel);
                for (SgRelModEdModAten rel : relModEdModAtenSelected) {
                    if (!listaMA.contains(rel.getReaModalidadAtencion())) {
                        listaMA.add(rel.getReaModalidadAtencion());
                    }
                }
                comboModalidadAtencion = new SofisComboG(listaMA, "matNombre");
                comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                FiltroOpciones fOpc = new FiltroOpciones();
                fOpc.setOrderBy(new String[]{"opcNombre"});
                fOpc.setAscending(new boolean[]{true});
                fOpc.setOpcModalidadPk(comboModalidad.getSelectedT().getModPk());
                fOpc.setIncluirCampos(new String[]{"opcPk", "opcNombre", "opcVersion"});
                List<SgOpcion> opcion = opcionClient.buscar(fOpc);
                comboOpcion = new SofisComboG(opcion, "opcNombre");
                comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (listaMA.size() == 1) {
                    comboModalidadAtencion.setSelectedT(listaMA.get(0));
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
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
                    comboSubModalidad = new SofisComboG(listaSubMod, "smoNombre");
                    comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (listaSubMod.size() == 1) {
                        comboSubModalidad.setSelectedT(listaSubMod.get(0));
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
                    List<SgGrado> grado = gradoClient.buscar(fGrado);
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
            if (comboSubModalidad.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(comboSubModalidad.getSelectedT().getSmoPk());
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                fGrado.setOrderBy(new String[]{"graOrden"});
                fGrado.setAscending(new boolean[]{true});
                List<SgGrado> grado = gradoClient.buscar(fGrado);
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


    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedSistemaIntegradoPk(this.sistemaIntegrado.getSinPk());
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

    public Double roundDouble(Double value) {
        if (value == null) {
            return Double.valueOf(0d);
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

    public SistemaIntegradoRestClient getSistemaIntegradoRestClient() {
        return sistemaIntegradoRestClient;
    }

    public void setSistemaIntegradoRestClient(SistemaIntegradoRestClient sistemaIntegradoRestClient) {
        this.sistemaIntegradoRestClient = sistemaIntegradoRestClient;
    }

    public SgSistemaIntegrado getSistemaIntegrado() {
        return sistemaIntegrado;
    }

    public void setSistemaIntegrado(SgSistemaIntegrado sistemaIntegrado) {
        this.sistemaIntegrado = sistemaIntegrado;
    }

    public List<SgConsultaCalifiacionesAreasBasicas> getCalificacionesPorArea() {
        return calificacionesPorArea;
    }

    public void setCalificacionesPorArea(List<SgConsultaCalifiacionesAreasBasicas> calificacionesPorArea) {
        this.calificacionesPorArea = calificacionesPorArea;
    }

    public SofisComboG<SgModalidad> getComboModalidad() {
        return comboModalidad;
    }

    public void setComboModalidad(SofisComboG<SgModalidad> comboModalidad) {
        this.comboModalidad = comboModalidad;
    }

    public List<SgRelModEdModAten> getRelModEdModAtenSelected() {
        return relModEdModAtenSelected;
    }

    public void setRelModEdModAtenSelected(List<SgRelModEdModAten> relModEdModAtenSelected) {
        this.relModEdModAtenSelected = relModEdModAtenSelected;
    }

    public SofisComboG<SgOpcion> getComboOpcion() {
        return comboOpcion;
    }

    public void setComboOpcion(SofisComboG<SgOpcion> comboOpcion) {
        this.comboOpcion = comboOpcion;
    }

    public SofisComboG<SgSubModalidadAtencion> getComboSubModalidad() {
        return comboSubModalidad;
    }

    public void setComboSubModalidad(SofisComboG<SgSubModalidadAtencion> comboSubModalidad) {
        this.comboSubModalidad = comboSubModalidad;
    }

    public SofisComboG<SgModalidadAtencion> getComboModalidadAtencion() {
        return comboModalidadAtencion;
    }

    public void setComboModalidadAtencion(SofisComboG<SgModalidadAtencion> comboModalidadAtencion) {
        this.comboModalidadAtencion = comboModalidadAtencion;
    }

    public SofisComboG<SgProgramaEducativo> getComboProgramaEducativo() {
        return comboProgramaEducativo;
    }

    public void setComboProgramaEducativo(SofisComboG<SgProgramaEducativo> comboProgramaEducativo) {
        this.comboProgramaEducativo = comboProgramaEducativo;
    }

}
