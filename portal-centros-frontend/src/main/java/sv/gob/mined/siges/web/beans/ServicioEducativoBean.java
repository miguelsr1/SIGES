/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyServicioEducativoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ServicioEducativoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ServicioEducativoBean.class.getName());

    @Inject
    private ServicioEducativoRestClient restClient;

    @Inject
    private SeccionRestClient seccionRestClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private CicloRestClient restCiclo;

    @Inject
    private ModalidadRestClient restModalidad;

    @Inject
    private RelModEdModAtenRestClient restRelModEdModAten;

    @Inject
    private GradoRestClient restGrado;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    private SgServicioEducativo entidadEnEdicion = new SgServicioEducativo();
    private FiltroServicioEducativo filtro = new FiltroServicioEducativo();
    private LazyServicioEducativoDataModel servicioEducativoLazyModel;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long cantidadMatriculas;
    private Long totalResultados;
    private SofisComboG<EnumServicioEducativoEstado> comboEstadoServicioEducativo;
    private SofisComboG<EnumServicioEducativoEstado> comboEstadoServicioEducativoEdit;
    private SofisComboG<SgNivel> comboNiveles;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion;
    private SofisComboG<SgGrado> comboGrado;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private Long sedPkSeleccionado;
    private String sedeNombre;
    private Boolean soloLectura;
    private SgSede sedePadre;
    private List<SgServicioEducativo> servicioEducativoList;
    private List<SgRelModEdModAten> relModEdModAtenList;
    private String mensajeSede;
    
    private List<Integer> ultimosTresAnios;

    public ServicioEducativoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sedePadre = (SgSede) request.getAttribute("sedeEducativa");
            cargarCombos();
            buscarConfiguracion();
            if (sedePadre == null) {
                //ServicioEducativo utilizado como página
            } else {
                //ServicioEducativo como componente dentro de sede
                filtro.setSecSedeFk(sedePadre.getSedPk());
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void buscarConfiguracion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigoExacto(Constantes.MENSAJE_PANTALLA_SEDE_PORTAL);
            List<SgConfiguracion> conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                mensajeSede = conf.get(0).getConValor();
            }

        } catch (Exception ex) {

        }
    }

    public String buscar() {
        try {

            filtro.setSecNivelFk(this.comboNiveles.getSelectedT() != null ? comboNiveles.getSelectedT().getNivPk() : null);
            filtro.setSecModalidadAtencionPk(this.comboModalidadAtencion.getSelectedT() != null ? comboModalidadAtencion.getSelectedT().getMatPk() : null);
            filtro.setSecSubModalidadAtencionFk(this.comboSubModalidadAtencion.getSelectedT() != null ? comboSubModalidadAtencion.getSelectedT().getSmoPk() : null);

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
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre"});
            } else {
                filtro.setIncluirCampos(new String[]{
                    "sduSede.sedCodigo",
                    "sduSede.sedDireccion.dirDepartamento.depNombre",
                    "sduSede.sedDireccion.dirMunicipio.munNombre",
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
                    "sduProgramaEducativo.pedNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre"});
            }
            filtro.setOrderBy(new String[]{"sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicOrden",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modOrden", "sduGrado.graOrden"});
            filtro.setAscending(new boolean[]{true, true, true, true});
            servicioEducativoList = restClient.buscar(filtro);

            LocalDate fechaHoy = LocalDate.now();
            Integer year = fechaHoy.getYear();
            ultimosTresAnios = new ArrayList();
            ultimosTresAnios.add(year - 2);
            ultimosTresAnios.add(year - 1);
            ultimosTresAnios.add(year);

            FiltroSeccion filSec = new FiltroSeccion();
            filSec.setSecSedeFk(sedePadre.getSedPk());
            filSec.setSecServiciosEducativoFk(servicioEducativoList.stream().map(c -> c.getSduPk()).collect(Collectors.toList()));
            filSec.setSecurityOperation(null);
            filSec.setSecAnios(ultimosTresAnios);
            filSec.setIncluirCampos(new String[]{"secServicioEducativo.sduPk", "secAnioLectivo.aleAnio", "secCantidadEstudiantesNoRetirados"});
            List<SgSeccion> listSec = seccionRestClient.buscar(filSec);
            cantidadMatriculas = 0L;

            for (SgServicioEducativo ser : servicioEducativoList) {
                ser.setSduSumaMatriculasPorAnio(new HashMap<>());
                Integer suma = 0;
                IteracionAnios:
                for (Integer anio : ultimosTresAnios) {
                    List<SgSeccion> secciones = listSec.stream().filter(c -> c.getSecServicioEducativo().getSduPk().equals(ser.getSduPk()) && c.getSecAnioLectivo().getAleAnio().equals(anio)).collect(Collectors.toList());
                    suma = 0;
                    for (SgSeccion sec : secciones) {
                        suma += sec.getSecCantidadEstudiantesNoRetirados() != null ? sec.getSecCantidadEstudiantesNoRetirados().intValue() : 0;
                    }
                    ser.getSduSumaMatriculasPorAnio().put(anio, suma);
                    
                    if (anio.equals(year)){
                        cantidadMatriculas += suma;
                    }
                }   
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {

        try {
            FiltroNivel fc = new FiltroNivel();
            fc.setOrderBy(new String[]{"nivOrden"});
            fc.setAscending(new boolean[]{true});
            fc.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
            fc.setSedPk(sedePadre != null ? sedePadre.getSedPk() : null);

            List<SgNivel> niveles = restNivel.buscar(fc);
            comboNiveles = new SofisComboG(niveles, "nivNombre");
            comboNiveles.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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

            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboSubModalidadAtencion = new SofisComboG();
            comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

        comboNiveles.setSelected(-1);
        comboModalidadAtencion.setSelected(-1);
        comboSubModalidadAtencion.setSelected(-1);
    }

    public String limpiar() {
        limpiarCombos();
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

    public List<SgSede> completeSede(String query) {
        try {
            sedPkSeleccionado = null;
            FiltroSedes fil = new FiltroSedes();
            fil.setSedNombre(query);
            fil.setMaxResults(11L);
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

    public void seleccionarNivelparaModAten() {
        try {
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenList = new ArrayList();
            if (comboNiveles.getSelectedT() != null) {
                FiltroRelModEdModAten filtroRelModEdModAten = new FiltroRelModEdModAten();
                filtroRelModEdModAten.setReaNivel(comboNiveles.getSelectedT().getNivPk());
                relModEdModAtenList = restRelModEdModAten.buscar(filtroRelModEdModAten);
                List<SgModalidadAtencion> modalidadesAtencion = new ArrayList<>();
                for (SgRelModEdModAten elemento : relModEdModAtenList) {
                    if (!modalidadesAtencion.contains(elemento.getReaModalidadAtencion())) {
                        modalidadesAtencion.add(elemento.getReaModalidadAtencion());
                    }

                }
                comboModalidadAtencion = new SofisComboG(modalidadesAtencion, "matNombre");
                comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
            if (comboModalidadAtencion.getSelectedT() != null && comboNiveles.getSelectedT() != null) {
                List<SgSubModalidadAtencion> listaSubMod = new ArrayList();
                for (SgRelModEdModAten relAux : relModEdModAtenList) {
                    if (relAux.getReaSubModalidadAtencion() != null && comboModalidadAtencion.getSelectedT().equals(relAux.getReaModalidadAtencion()) && BooleanUtils.isTrue(relAux.getReaSubModalidadAtencion().getSmoHabilitado())) {
                        listaSubMod.add(relAux.getReaSubModalidadAtencion());
                    }
                }
                if (!listaSubMod.isEmpty()) {
                    comboSubModalidadAtencion = new SofisComboG(listaSubMod, "smoNombre");
                    comboSubModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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

    public SofisComboG<SgModalidadAtencion> getComboModalidadAtencion() {
        return comboModalidadAtencion;
    }

    public void setComboModalidadAtencion(SofisComboG<SgModalidadAtencion> comboModalidadAtencion) {
        this.comboModalidadAtencion = comboModalidadAtencion;
    }

    public List<SgServicioEducativo> getServicioEducativoList() {
        return servicioEducativoList;
    }

    public void setServicioEducativoList(List<SgServicioEducativo> servicioEducativoList) {
        this.servicioEducativoList = servicioEducativoList;
    }

    public SofisComboG<SgSubModalidadAtencion> getComboSubModalidadAtencion() {
        return comboSubModalidadAtencion;
    }

    public void setComboSubModalidadAtencion(SofisComboG<SgSubModalidadAtencion> comboSubModalidadAtencion) {
        this.comboSubModalidadAtencion = comboSubModalidadAtencion;
    }

    public List<SgRelModEdModAten> getRelModEdModAtenList() {
        return relModEdModAtenList;
    }

    public void setRelModEdModAtenList(List<SgRelModEdModAten> relModEdModAtenList) {
        this.relModEdModAtenList = relModEdModAtenList;
    }

    public String getMensajeSede() {
        return mensajeSede;
    }

    public void setMensajeSede(String mensajeSede) {
        this.mensajeSede = mensajeSede;
    }

    public Long getCantidadMatriculas() {
        return cantidadMatriculas;
    }

    public void setCantidadMatriculas(Long cantidadMatriculas) {
        this.cantidadMatriculas = cantidadMatriculas;
    }

    public List<Integer> getUltimosTresAnios() {
        return ultimosTresAnios;
    }

    public void setUltimosTresAnios(List<Integer> ultimosTresAnios) {
        this.ultimosTresAnios = ultimosTresAnios;
    }
    
}
