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
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;

import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgRelOpcionProgEd;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.lazymodels.LazyPlanesEstudioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.PlanesEstudioRestClient;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PlanesEstudioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PlanesEstudioBean.class.getName());

    @Inject
    private PlanesEstudioRestClient restClient;

    @Inject
    private OrganizacionCurricularRestClient orgCurrRest;

    @Inject
    private RelModEdModAtenRestClient relRest;

    @Inject
    private OpcionRestClient opcionRest;

    @Inject
    private SessionBean sessionBean;

    private FiltroPlanEstudio filtro = new FiltroPlanEstudio();
    private SgPlanEstudio entidadEnEdicion = new SgPlanEstudio();
    private List<RevHistorico> historialPlanesEstudio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPlanesEstudioDataModel planesEstudioLazyModel;
    private List<SgRelModEdModAten> listaRelacion;
    private SofisComboG<SgOrganizacionCurricular> comboOrgCurr;
    private SofisComboG<SgNivel> comboNiveles;
    private SofisComboG<SgCiclo> comboCiclos;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgOpcion> comboOpciones;
    private SofisComboG<SgProgramaEducativo> comboProgramas;
    private SofisComboG<SgModalidadAtencion> comboModalidadAten;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidad;
    private Boolean RenderizarOpcion = Boolean.FALSE;

    public PlanesEstudioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombosOrgCurr();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLANES_ESTUDIO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            planesEstudioLazyModel = new LazyPlanesEstudioDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombosOrgCurr() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            List<SgOrganizacionCurricular> listOrgCur = orgCurrRest.buscar(fc);
            comboOrgCurr = new SofisComboG(new ArrayList(listOrgCur), "ocuNombre");
            comboOrgCurr.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboNiveles = new SofisComboG();
            comboNiveles.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCiclos = new SofisComboG();
            comboCiclos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpciones = new SofisComboG();
            comboOpciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramas = new SofisComboG();
            comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAten = new SofisComboG();
            comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombosNivel() {
        try {
            comboNiveles = new SofisComboG();
            comboNiveles.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCiclos = new SofisComboG();
            comboCiclos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpciones = new SofisComboG();
            comboOpciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramas = new SofisComboG();
            comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAten = new SofisComboG();
            comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboOrgCurr.getSelectedT() != null) {
                SgOrganizacionCurricular orgCur = comboOrgCurr.getSelectedT();
                orgCur = orgCurrRest.obtenerPorId(orgCur.getOcuPk());
                comboNiveles = new SofisComboG(new ArrayList(orgCur.getOcuNiveles()), "nivNombre");
                comboNiveles.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (entidadEnEdicion.getPesPk() != null) {
                    comboNiveles.setSelectedT(entidadEnEdicion.getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel());
                } else {
                    if (orgCur.getOcuNiveles() != null) {
                        if (orgCur.getOcuNiveles().size() == 1) {
                            comboNiveles.setSelectedT(orgCur.getOcuNiveles().get(0));
                            cargarCombosCiclo();
                        }
                    }
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarCombosCiclo() {
        comboCiclos = new SofisComboG();
        comboCiclos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidad = new SofisComboG();
        comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboOpciones = new SofisComboG();
        comboOpciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboProgramas = new SofisComboG();
        comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidadAten = new SofisComboG();
        comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboSubModalidad = new SofisComboG();
        comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (comboNiveles.getSelectedT() != null) {
            SgNivel niv = comboNiveles.getSelectedT();
            List<SgCiclo> listCiclo = niv.getNivCiclos();
            comboCiclos = new SofisComboG(new ArrayList(listCiclo), "cicNombre");
            comboCiclos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getPesPk() != null) {
                comboCiclos.setSelectedT(entidadEnEdicion.getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo());
            } else {
                if (niv.getNivCiclos() != null) {
                    if (niv.getNivCiclos().size() == 1) {
                        comboCiclos.setSelectedT(niv.getNivCiclos().get(0));
                        cargarCombosModalidad();
                    }
                }
            }
        }

    }

    public void cargarCombosModalidad() {
        comboModalidad = new SofisComboG();
        comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboOpciones = new SofisComboG();
        comboOpciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboProgramas = new SofisComboG();
        comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboModalidadAten = new SofisComboG();
        comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboSubModalidad = new SofisComboG();
        comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (comboCiclos.getSelectedT() != null) {
            SgCiclo cic = comboCiclos.getSelectedT();

            List<SgModalidad> listModalidad = cic.getCicModalidades();
            comboModalidad = new SofisComboG(listModalidad, "modNombre");
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (entidadEnEdicion.getPesPk() != null) {
                comboModalidad.setSelectedT(entidadEnEdicion.getPesRelacionModalidad().getReaModalidadEducativa());
            } else {

                if (cic.getCicModalidades() != null) {
                    if (cic.getCicModalidades().size() == 1) {
                        comboModalidad.setSelectedT(cic.getCicModalidades().get(0));
                        if (BooleanUtils.isTrue(cic.getCicModalidades().get(0).getModAdmiteOpcion())) {
                            cargarCombosOpcion();
                        }
                        cargarCombosModalidadAtencion();
                    }
                }
            }
        }

    }

    public void cargarCombosOpcion() {
        try {
            if (comboModalidad.getSelectedT() != null) {
                this.RenderizarOpcion = Boolean.TRUE;
                FiltroOpciones fo = new FiltroOpciones();
                fo.setHabilitado(Boolean.TRUE);
                fo.setInicializarProgramaEducativo(Boolean.TRUE);
                fo.setOpcModalidadPk(comboModalidad.getSelectedT().getModPk());
                comboOpciones = new SofisComboG(opcionRest.buscar(fo), "opcNombre");
                comboOpciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboProgramas = new SofisComboG();
                comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (entidadEnEdicion.getPesPk() != null && entidadEnEdicion.getPesOpcion() != null) {
                    comboOpciones.setSelectedT(entidadEnEdicion.getPesOpcion());
                    cargarCombosProgramasEducativos();
                }
            } else {
                comboProgramas = new SofisComboG();
                comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarCombosProgramasEducativos() {
        comboProgramas = new SofisComboG();
        comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (comboOpciones.getSelectedT() != null) {
            SgOpcion opc = comboOpciones.getSelectedT();
            List<SgRelOpcionProgEd> listaRelProgramas = opc.getOpcRelacionOpcionProgramaEdu();
            List<SgProgramaEducativo> listaProg = new ArrayList();
            for (SgRelOpcionProgEd rel : listaRelProgramas) {
                listaProg.add(rel.getRoeProgramaEducativo());
            }
            comboProgramas = new SofisComboG(listaProg, "pedNombre");
            comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getPesPk() != null && entidadEnEdicion.getPesProgramaEducativo() != null) {
                comboProgramas.setSelectedT(entidadEnEdicion.getPesProgramaEducativo());
            }
        }

    }

    public void actualizacionCambioModalidadEducativa() {

        if (comboModalidad.getSelectedT() != null) {
            SgModalidad mod = comboModalidad.getSelectedT();
            if (mod.getModAdmiteOpcion() != null) {
                if (mod.getModAdmiteOpcion()) {
                    this.RenderizarOpcion = Boolean.TRUE;
                    cargarCombosOpcion();
                }
            }
            cargarCombosModalidadAtencion();

        } else {
            comboOpciones = new SofisComboG<>();
            comboOpciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboProgramas = new SofisComboG();
            comboProgramas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAten = new SofisComboG();
            comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }

    }

    public void cargarCombosModalidadAtencion() {
        try {
            if (comboModalidad.getSelectedT() != null) {
                SgModalidad mod = comboModalidad.getSelectedT();
                FiltroRelModEdModAten filtro = new FiltroRelModEdModAten();
                filtro.setReaModalidadEducativa(mod.getModPk());
                listaRelacion = relRest.buscar(filtro);
                List<SgModalidadAtencion> listaModAt = new ArrayList<>();

                for (SgRelModEdModAten relAux : listaRelacion) {
                    if (!listaModAt.contains(relAux.getReaModalidadAtencion())) {
                        listaModAt.add(relAux.getReaModalidadAtencion());
                    }
                }

                comboModalidadAten = new SofisComboG(listaModAt, "matNombre");
                comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboSubModalidad = new SofisComboG();
                comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (entidadEnEdicion.getPesPk() != null) {
                    comboModalidadAten.setSelectedT(entidadEnEdicion.getPesRelacionModalidad().getReaModalidadAtencion());
                } else {
                    if (listaModAt.size() == 1) {
                        comboModalidadAten.setSelectedT(listaModAt.get(0));
                        cargarCombosSubModalidad();
                    }

                }
            } else {
                comboModalidadAten = new SofisComboG();
                comboModalidadAten.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboSubModalidad = new SofisComboG();
                comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarCombosSubModalidad() {

        List<SgSubModalidadAtencion> listaSubMod = new ArrayList<>();
        SgModalidadAtencion modAtencionSelect = comboModalidadAten.getSelectedT();

        for (SgRelModEdModAten relAux : listaRelacion) {
            if (relAux.getReaSubModalidadAtencion() != null && modAtencionSelect.equals(relAux.getReaModalidadAtencion())) {
                listaSubMod.add(relAux.getReaSubModalidadAtencion());
            }
        }

        if (!listaSubMod.isEmpty()) {
            comboSubModalidad = new SofisComboG(new ArrayList(listaSubMod), "smoNombre");
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getPesPk() != null) {
                comboSubModalidad.setSelectedT(entidadEnEdicion.getPesRelacionModalidad().getReaSubModalidadAtencion());
            } else if (listaSubMod.size() == 1) {
                comboSubModalidad.setSelectedT(listaSubMod.get(0));
            }
        } else {
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }

    public void cargarCombosEdicion() {
        try {

            SgOrganizacionCurricular orgCur = entidadEnEdicion.getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular();
            orgCur = orgCurrRest.obtenerPorId(orgCur.getOcuPk());
            comboOrgCurr.setSelectedT(orgCur);
            cargarCombosNivel();
            cargarCombosCiclo();
            cargarCombosModalidad();
            cargarCombosOpcion();
            cargarCombosProgramasEducativos();
            cargarCombosModalidadAtencion();
            cargarCombosSubModalidad();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        listaRelacion = new ArrayList();
        comboOrgCurr.setSelected(0);
        comboNiveles = new SofisComboG();
        comboCiclos = new SofisComboG();
        comboModalidad = new SofisComboG();
        comboOpciones = new SofisComboG();
        comboProgramas = new SofisComboG();
        comboModalidadAten = new SofisComboG();
        comboSubModalidad = new SofisComboG();
    }

    public void limpiar() {
        filtro = new FiltroPlanEstudio();
        buscar();
    }

    public void agregar() {
        cargarCombosOrgCurr();
        entidadEnEdicion = new SgPlanEstudio();
    }

    public void actualizar(SgPlanEstudio var) {
        try {
            LOGGER.log(Level.INFO, "Plan de estudio: " + var.getPesPk());
            entidadEnEdicion = restClient.obtenerPorId(var.getPesPk());
            LOGGER.log(Level.INFO, "Plan de estudio: " + entidadEnEdicion);
            cargarCombosEdicion();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (comboModalidadAten.getSelectedT() != null) {
                for (SgRelModEdModAten relAux : listaRelacion) {

                    if (comboSubModalidad.getSelectedT() == null) {
                        if (relAux.getReaModalidadAtencion().getMatPk().equals(comboModalidadAten.getSelectedT().getMatPk())) {
                            entidadEnEdicion.setPesRelacionModalidad(relAux);
                            break;
                        }
                    } else {
                        if (relAux.getReaSubModalidadAtencion() != null) {
                            if (comboSubModalidad.getSelectedT().getSmoPk().equals(relAux.getReaSubModalidadAtencion().getSmoPk())) {
                                entidadEnEdicion.setPesRelacionModalidad(relAux);
                                break;
                            }
                        }
                    }
                }
            }

            entidadEnEdicion.setPesOpcion(comboOpciones.getSelectedT());
            entidadEnEdicion.setPesProgramaEducativo(comboProgramas.getSelectedT());

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            this.RenderizarOpcion = Boolean.FALSE;
            buscar();
            limpiarCombos();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

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

    public void historial(Long id) {
        try {
            historialPlanesEstudio = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroPlanEstudio getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPlanEstudio filtro) {
        this.filtro = filtro;
    }

    public SgPlanEstudio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPlanEstudio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialPlanesEstudio() {
        return historialPlanesEstudio;
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

    public LazyPlanesEstudioDataModel getPlanesEstudioLazyModel() {
        return planesEstudioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyPlanesEstudioDataModel planesEstudioLazyModel) {
        this.planesEstudioLazyModel = planesEstudioLazyModel;
    }

    public SofisComboG<SgNivel> getComboNiveles() {
        return comboNiveles;
    }

    public void setComboNiveles(SofisComboG<SgNivel> comboNiveles) {
        this.comboNiveles = comboNiveles;
    }

    public SofisComboG<SgCiclo> getComboCiclos() {
        return comboCiclos;
    }

    public void setComboCiclos(SofisComboG<SgCiclo> comboCiclos) {
        this.comboCiclos = comboCiclos;
    }

    public SofisComboG<SgModalidad> getComboModalidad() {
        return comboModalidad;
    }

    public void setComboModalidad(SofisComboG<SgModalidad> comboModalidad) {
        this.comboModalidad = comboModalidad;
    }

    public SofisComboG<SgModalidadAtencion> getComboModalidadAten() {
        return comboModalidadAten;
    }

    public void setComboModalidadAten(SofisComboG<SgModalidadAtencion> comboModalidadAten) {
        this.comboModalidadAten = comboModalidadAten;
    }

    public SofisComboG<SgOrganizacionCurricular> getComboOrgCurr() {
        return comboOrgCurr;
    }

    public void setComboOrgCurr(SofisComboG<SgOrganizacionCurricular> comboOrgCurr) {
        this.comboOrgCurr = comboOrgCurr;
    }

    public List<SgRelModEdModAten> getListaRelacion() {
        return listaRelacion;
    }

    public void setListaRelacion(List<SgRelModEdModAten> listaRelacion) {
        this.listaRelacion = listaRelacion;
    }

    public SofisComboG<SgSubModalidadAtencion> getComboSubModalidad() {
        return comboSubModalidad;
    }

    public void setComboSubModalidad(SofisComboG<SgSubModalidadAtencion> comboSubModalidad) {
        this.comboSubModalidad = comboSubModalidad;
    }

    public SofisComboG<SgOpcion> getComboOpciones() {
        return comboOpciones;
    }

    public void setComboOpciones(SofisComboG<SgOpcion> comboOpciones) {
        this.comboOpciones = comboOpciones;
    }

    public SofisComboG<SgProgramaEducativo> getComboProgramas() {
        return comboProgramas;
    }

    public void setComboProgramas(SofisComboG<SgProgramaEducativo> comboProgramas) {
        this.comboProgramas = comboProgramas;
    }

    public Boolean getRenderizarOpcion() {
        return RenderizarOpcion;
    }

    public void setRenderizarOpcion(Boolean RenderizarOpcion) {
        this.RenderizarOpcion = RenderizarOpcion;
    }

}
