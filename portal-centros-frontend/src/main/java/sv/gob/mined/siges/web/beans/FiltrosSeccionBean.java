/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class FiltrosSeccionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FiltrosSeccionBean.class.getName());

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
    private AnioLectivoRestClient restAnioLectivo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private OpcionRestClient opcionClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

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
    private Boolean renderSeccion = Boolean.TRUE;
    private Boolean renderProgramaOpcion = Boolean.FALSE;
    private Boolean renderSede = Boolean.TRUE;
    private Boolean soloAniosLectivosAbiertos = Boolean.FALSE;
    private Boolean disableAnioLectio = Boolean.FALSE;
    private Boolean retornarSoloSeccionesAbiertas = Boolean.FALSE;

    private FiltroSeccion filtro;

    @PostConstruct
    public void init() {
        try {
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarSedeSeleccionada(SgSede sed) {
        try {
            if (sed != null) {
                sedeSeleccionada = sed;
                seleccionarSede();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombos() {
        try {
            if (comboProgramaEducativo == null && BooleanUtils.isTrue(this.renderProgramaOpcion)) {
                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setAscending(new boolean[]{true});
                fc.setOrderBy(new String[]{"pedNombre"});
                fc.setIncluirCampos(new String[]{"pedNombre", "pedVersion"});
                List<SgProgramaEducativo> listaPE = restCatalogo.buscarProgramasEducativos(fc);
                comboProgramaEducativo = new SofisComboG(listaPE, "pedNombre");
                comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

            if (comboNivel == null) {
                FiltroNivel fNivel = new FiltroNivel();
                fNivel.setOrderBy(new String[]{"nivOrden"});
                fNivel.setAscending(new boolean[]{true});
                fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
                List<SgNivel> niveles = restNivel.buscarConCache(fNivel);
                comboNivel = new SofisComboG(niveles, "nivNombre");
                comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (niveles.size() == 1) {
                    comboNivel.setSelectedT(niveles.get(0));
                    seleccionarNivel();
                }
            }
            if (comboAnioLectivo == null) {
                FiltroAnioLectivo fc2 = new FiltroAnioLectivo();
                fc2.setOrderBy(new String[]{"aleAnio"});
                fc2.setAscending(new boolean[]{false});
                if (BooleanUtils.isTrue(this.soloAniosLectivosAbiertos)) {
                    fc2.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
                }
                comboAnioLectivo = new SofisComboG(restAnioLectivo.buscarConCache(fc2), "aleAnio");
                comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            limpiarCombos();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombos() {
        comboNivel.setSelected(-1);
        if (!disableAnioLectio) {
            comboAnioLectivo.setSelected(-1);
            filtro.setSecAnioLectivoFk(null);
        }
        comboCiclo = new SofisComboG();
        comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidad = new SofisComboG();
        comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidadAtencion = new SofisComboG();
        comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboSubModalidadAtencion = new SofisComboG();
        comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboGrado = new SofisComboG();
        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboSeccion = new SofisComboG();
        comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboOpcion = new SofisComboG();
        comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (renderSede) {
            this.sedeSeleccionada = null;
            filtro.setSecSedeFk(null);
        }
        if (comboProgramaEducativo != null) {
            comboProgramaEducativo.setSelected(-1);
            filtro.setSecProgramaEducativoFk(null);
        }
        filtro.setSecNivelFk(null);
        filtro.setSecCicloFk(null);
        filtro.setSecModalidadAtencionPk(null);
        filtro.setSecModalidadEducativaPk(null);
        filtro.setSecGradoFk(null);
        filtro.setSecPk(null);
        filtro.setSecOpcionFk(null);
        filtro.setSecProgramaEducativoFk(null);

    }

    public void seleccionarUltimoAnio() {
        try {
            comboAnioLectivo.setSelectedT((SgAnioLectivo) this.comboAnioLectivo.getAllTs().get(this.comboAnioLectivo.getAllTs().size() - 1));
            filtro.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnioLectivo(Integer anio) {
        try {
            List<SgAnioLectivo> lista = comboAnioLectivo.getAllTs();
            for (SgAnioLectivo a : lista) {
                if (Objects.equals(a.getAleAnio(), anio)) {
                    comboAnioLectivo.setSelectedT(a);
                    break;
                }
            }
            filtro.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void deseleccionarSede(AjaxBehaviorEvent event) {
        this.sedeSeleccionada = null;
        filtro.setSecSedeFk(null);
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
            comboSubModalidadAtencion = new SofisComboG();
            comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            filtro.setSecSedeFk(null);
            filtro.setSecNivelFk(null);
            filtro.setSecCicloFk(null);
            filtro.setSecModalidadAtencionPk(null);
            filtro.setSecSubModalidadAtencionFk(null);
            filtro.setSecModalidadEducativaPk(null);
            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);
            filtro.setSecOpcionFk(null);

            if (this.sedeSeleccionada != null) {
                filtro.setSecSedeFk(this.sedeSeleccionada.getSedPk());
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
            comboSubModalidadAtencion = new SofisComboG();
            comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            filtro.setSecNivelFk(null);
            filtro.setSecCicloFk(null);
            filtro.setSecModalidadAtencionPk(null);
            filtro.setSecSubModalidadAtencionFk(null);
            filtro.setSecModalidadEducativaPk(null);
            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);
            filtro.setSecOpcionFk(null);

            if (comboNivel.getSelectedT() != null) {
                filtro.setSecNivelFk(comboNivel.getSelectedT().getNivPk());

                FiltroCiclo fCiclo = new FiltroCiclo();
                fCiclo.setNivPk(comboNivel.getSelectedT().getNivPk());
                fCiclo.setOrderBy(new String[]{"cicNombre"});
                fCiclo.setAscending(new boolean[]{true});
                fCiclo.setIncluirCampos(new String[]{"cicNombre", "cicVersion"});
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
            comboSubModalidadAtencion = new SofisComboG();
            comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            filtro.setSecCicloFk(null);
            filtro.setSecModalidadAtencionPk(null);
            filtro.setSecSubModalidadAtencionFk(null);
            filtro.setSecModalidadEducativaPk(null);
            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);
            filtro.setSecOpcionFk(null);

            if (comboCiclo.getSelectedT() != null) {
                filtro.setSecCicloFk(comboCiclo.getSelectedT().getCicPk());

                FiltroModalidad fModalidad = new FiltroModalidad();
                fModalidad.setCicPk(comboCiclo.getSelectedT().getCicPk());
                fModalidad.setOrderBy(new String[]{"modNombre"});
                fModalidad.setAscending(new boolean[]{true});
                fModalidad.setIncluirCampos(new String[]{"modNombre", "modVersion"});
                List<SgModalidad> modalidad = restModalidad.buscarConCache(fModalidad);
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
            comboSubModalidadAtencion = new SofisComboG();
            comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            filtro.setSecModalidadEducativaPk(null);
            filtro.setSecModalidadAtencionPk(null);
            filtro.setSecSubModalidadAtencionFk(null);
            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);
            filtro.setSecOpcionFk(null);

            if (comboModalidad.getSelectedT() != null) {
                filtro.setSecModalidadEducativaPk(comboModalidad.getSelectedT().getModPk());

                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(comboModalidad.getSelectedT().getModPk());
                List<SgModalidadAtencion> listaMA = new ArrayList();
                relModEdModAtenSelected = relModRestClient.buscarConCache(filtroRel);
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
                List<SgOpcion> opcion = opcionClient.buscarConCache(fOpc);
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
            comboSubModalidadAtencion = new SofisComboG();
            comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setSecModalidadAtencionPk(null);
            filtro.setSecSubModalidadAtencionFk(null);
            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);

            if (comboModalidadAtencion.getSelectedT() != null) {
                filtro.setSecModalidadAtencionPk(comboModalidadAtencion.getSelectedT().getMatPk());

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
            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);
            filtro.setSecSubModalidadAtencionFk(this.comboSubModalidadAtencion.getSelectedT() != null ? this.comboSubModalidadAtencion.getSelectedT().getSmoPk() : null);
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboSubModalidadAtencion.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(comboSubModalidadAtencion.getSelectedT().getSmoPk());
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
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
            comboSeccion = new SofisComboG();
            comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setSecGradoFk(null);
            filtro.setSecPk(null);

            if (comboGrado.getSelectedT() != null) {
                filtro.setSecGradoFk(comboGrado.getSelectedT().getGraPk());
                seleccionarAnioLectivo();
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
            filtro.setSecAnioLectivoFk(null);
            filtro.setSecPk(null);
            if (comboAnioLectivo.getSelectedT() != null) {
                filtro.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
            }
            if (comboGrado.getSelectedT() != null && comboAnioLectivo.getSelectedT() != null) {
                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setSecGradoFk(comboGrado.getSelectedT().getGraPk());
                if (retornarSoloSeccionesAbiertas) {
                    fSeccion.setSecEstado(EnumSeccionEstado.ABIERTA);
                }
                if (renderProgramaOpcion) {
                    fSeccion.setSecProgramaEducativoFk(comboProgramaEducativo != null ? comboProgramaEducativo.getSelectedT() != null ? comboProgramaEducativo.getSelectedT().getPedPk() : null : null);
                    fSeccion.setSecOpcionFk(comboOpcion != null ? comboOpcion.getSelectedT() != null ? comboOpcion.getSelectedT().getOpcPk() : null : null);
                }
                fSeccion.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                fSeccion.setSecSedeFk(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
                fSeccion.setOrderBy(new String[]{"secNombre"});
                fSeccion.setAscending(new boolean[]{true});
                fSeccion.setIncluirCampos(new String[]{"secNombre", "secVersion", "secJornadaLaboral.jlaNombre"});

                List<SgSeccion> seccion = restSeccion.buscar(fSeccion);
                comboSeccion = new SofisComboG(new ArrayList(seccion), "nombreSeccion");
                comboSeccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (seccion.size() == 1) {
                    comboSeccion.setSelectedT(seccion.get(0));
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombosDeFiltro() {
        try {
            FiltroSeccion fs = (FiltroSeccion) SerializationUtils.clone(filtro);

            cargarCombos();
            if (fs.getSecSedeFk() != null) {
                FiltroSedes fil = new FiltroSedes();
                fil.setSedPk(fs.getSecSedeFk());
                fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
                sedeSeleccionada = restSede.buscar(fil).get(0);
                seleccionarSede();
            }
            if (fs.getSecAnioLectivoFk() != null) {
                List<SgAnioLectivo> anios = comboAnioLectivo.getAllTs();
                SgAnioLectivo anio = anios.stream().filter(c -> fs.getSecAnioLectivoFk().equals(c.getAlePk())).collect(Collectors.toList()).get(0);
                comboAnioLectivo.setSelectedT(anio);
                seleccionarAnioLectivo();
            }
            if (fs.getSecNivelFk() != null) {
                List<SgNivel> niveles = comboNivel.getAllTs();
                SgNivel nivel = niveles.stream().filter(c -> fs.getSecNivelFk().equals(c.getNivPk())).collect(Collectors.toList()).get(0);
                comboNivel.setSelectedT(nivel);
                seleccionarNivel();
            }
            if (fs.getSecCicloFk() != null) {
                List<SgCiclo> ciclos = comboCiclo.getAllTs();
                SgCiclo ciclo = ciclos.stream().filter(c -> fs.getSecCicloFk().equals(c.getCicPk())).collect(Collectors.toList()).get(0);
                comboCiclo.setSelectedT(ciclo);
                seleccionarCiclo();
            }
            if (fs.getSecModalidadEducativaPk() != null) {
                List<SgModalidad> modalidades = comboModalidad.getAllTs();
                SgModalidad modalidad = modalidades.stream().filter(c -> fs.getSecModalidadEducativaPk().equals(c.getModPk())).collect(Collectors.toList()).get(0);
                comboModalidad.setSelectedT(modalidad);
                seleccionarModalidad();
            }
            if (fs.getSecOpcionFk() != null) {
                List<SgOpcion> opciones = comboOpcion.getAllTs();
                SgOpcion opcion = opciones.stream().filter(c -> fs.getSecOpcionFk().equals(c.getOpcPk())).collect(Collectors.toList()).get(0);
                comboOpcion.setSelectedT(opcion);
                seleccionarOpcion();
            }
            if (fs.getSecProgramaEducativoFk() != null) {
                List<SgProgramaEducativo> programasEd = comboProgramaEducativo.getAllTs();
                SgProgramaEducativo progEd = programasEd.stream().filter(c -> fs.getSecProgramaEducativoFk().equals(c.getPedPk())).collect(Collectors.toList()).get(0);
                comboProgramaEducativo.setSelectedT(progEd);
                seleccionarProgramaEducativo();
            }
            if (fs.getSecModalidadAtencionPk() != null) {
                List<SgModalidadAtencion> modalidadesAtencion = comboModalidadAtencion.getAllTs();
                SgModalidadAtencion modalidadAtencion = modalidadesAtencion.stream().filter(c -> fs.getSecModalidadAtencionPk().equals(c.getMatPk())).collect(Collectors.toList()).get(0);
                comboModalidadAtencion.setSelectedT(modalidadAtencion);
                seleccionarModalidadAtencion();
            }
            if (fs.getSecSubModalidadAtencionFk() != null) {
                List<SgSubModalidadAtencion> subModalidadesAtencion = comboSubModalidadAtencion.getAllTs();
                SgSubModalidadAtencion subModalidad = subModalidadesAtencion.stream().filter(c -> fs.getSecSubModalidadAtencionFk().equals(c.getSmoPk())).collect(Collectors.toList()).get(0);
                comboSubModalidadAtencion.setSelectedT(subModalidad);
                seleccionarSubModalidadAtencion();
            }
            if (fs.getSecGradoFk() != null) {
                List<SgGrado> grados = comboGrado.getAllTs();
                SgGrado grado = grados.stream().filter(c -> fs.getSecGradoFk().equals(c.getGraPk())).collect(Collectors.toList()).get(0);
                comboGrado.setSelectedT(grado);
                seleccionarGrado();
            }
            if (fs.getSecPk() != null) {
                List<SgSeccion> secciones = comboSeccion.getAllTs();
                SgSeccion seccion = secciones.stream().filter(c -> fs.getSecGradoFk().equals(c.getSecPk())).collect(Collectors.toList()).get(0);
                comboSeccion.setSelectedT(seccion);
                seleccionarSeccion();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void seleccionarOpcion() {
        filtro.setSecOpcionFk((this.comboOpcion.getSelectedT() != null && BooleanUtils.isTrue(renderProgramaOpcion)) ? this.comboOpcion.getSelectedT().getOpcPk() : null);
    }

    public void seleccionarProgramaEducativo() {
        filtro.setSecProgramaEducativoFk((this.comboProgramaEducativo.getSelectedT() != null && BooleanUtils.isTrue(renderProgramaOpcion)) ? this.comboProgramaEducativo.getSelectedT().getPedPk() : null);
    }

    public void seleccionarSeccion() {
        filtro.setSecPk((this.comboSeccion.getSelectedT() != null && BooleanUtils.isTrue(renderSeccion)) ? this.comboSeccion.getSelectedT().getSecPk() : null);
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
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

    public Boolean getRenderSeccion() {
        return renderSeccion;
    }

    public void setRenderSeccion(Boolean renderSeccion) {
        this.renderSeccion = renderSeccion;
    }

    public Boolean getRenderProgramaOpcion() {
        return renderProgramaOpcion;
    }

    public void setRenderProgramaOpcion(Boolean renderProgramaOpcion) {
        this.renderProgramaOpcion = renderProgramaOpcion;
    }

    public Boolean getRenderSede() {
        return renderSede;
    }

    public void setRenderSede(Boolean renderSede) {
        this.renderSede = renderSede;
    }

    public FiltroSeccion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSeccion filtro) {
        this.filtro = filtro;
    }

    public Boolean getSoloAniosLectivosAbiertos() {
        return soloAniosLectivosAbiertos;
    }

    public void setSoloAniosLectivosAbiertos(Boolean soloAniosLectivosAbiertos) {
        this.soloAniosLectivosAbiertos = soloAniosLectivosAbiertos;
    }

    public Boolean getDisableAnioLectio() {
        return disableAnioLectio;
    }

    public void setDisableAnioLectio(Boolean disableAnioLectio) {
        this.disableAnioLectio = disableAnioLectio;
    }

    public Boolean getRetornarSoloSeccionesAbiertas() {
        return retornarSoloSeccionesAbiertas;
    }

    public void setRetornarSoloSeccionesAbiertas(Boolean retornarSoloSeccionesAbiertas) {
        this.retornarSoloSeccionesAbiertas = retornarSoloSeccionesAbiertas;
    }

}
