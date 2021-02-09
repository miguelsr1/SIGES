/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.TreeNode;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgRelInmuebleArchivo;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleArchivo;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleArchivoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.validacion.SedesViewValidator;
import sv.gob.mined.siges.web.dto.SgConsultaCalifiacionesAreasBasicas;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgModuloDiplomado;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgPropuestaPedagogica;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPromedioCalificaciones;
import sv.gob.mined.siges.web.restclient.SistemaIntegradoRestClient;
import sv.gob.mined.siges.web.utilidades.ObjectMapperContextResolver;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPropuestaPedagogica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.ModuloDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.PropuestaPedagogicaRestClient;

@Named
@ViewScoped
public class SedeBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SedeBean.class.getName());

    @Inject
    private SedeRestClient restClient;

    @Inject
    private RelInmuebleArchivoRestClient relInmuebleArchivoRestClient;

    @Inject
    private DiplomadoRestClient diplomadoClient;

    @Inject
    private ModuloDiplomadoRestClient moduloDiplomadoClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private SistemaIntegradoRestClient sistemaIntegradoClient;

    @Inject
    private TreeGradoFilter treeGradoFilter;

    @Inject
    private PropuestaPedagogicaRestClient propuestaPedagogicaRestClient;

    @Inject
    @Param(name = "sede")
    private Long sedeId;

    @Inject
    private SedeRestClient restSede;

    private Boolean soloLectura = Boolean.FALSE;
    private String tabActiveId;
    private SgSede entidadEnEdicion;
    private SedesViewValidator sedesViewValidator;
    private String ofertaJornadas = null;
    private List<SgJornadaLaboral> jornadasLaborales;
    private List<String> imagenes = new LinkedList<>();
    private List<SgArchivo> imagenesInfra;
    private String urlQGIS;
    private String promedioInstitucion;
    private String promedioNacional;

    private Boolean renderizarBuscarPorCercania = Boolean.FALSE;
    private List<SgConsultaCalifiacionesAreasBasicas> calificacionesInstitucion;
    private List<SgConsultaCalifiacionesAreasBasicas> calificacionesNacional;
    private List<SgDiplomado> diplomadosAutorizados;
    private HashMap<Long, List<SgModuloDiplomado>> modulosDiplomado = new HashMap<>();
    private SgDiplomado diplomadoEnEdicion = null;

    private FiltroServicioEducativo filtro_servicio = new FiltroServicioEducativo();
    private FiltroPromedioCalificaciones filtro = new FiltroPromedioCalificaciones();

    private SgPropuestaPedagogica propuestaPedagogica;
    
    private TreeNode filtrosRoot;
    private String mensajeInfoPromedioCalificaciones;

    private String nombreNodoSeleccionado;

    public SedeBean() {
    }

    @PostConstruct
    public void init() {
        try {
            imagenes.add("1435055437917.jpg");
            imagenes.add("ra2018-1197x440.jpg");
            if (sedeId != null && sedeId > 0) {
                this.actualizar(restClient.obtenerPorId(sedeId));
                soloLectura = true;
            }
            buscarConfiguracion();
            filtro_servicio.setSecSedeFk(this.sedeId);
            cargarCombos();
            treeGradoFilter.setSedePk(sedeId);
            treeGradoFilter.cargarOrganizacionesCurriculares();
            buscarPropuestaPedagogica();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void buscarPropuestaPedagogica() {
        try {
            propuestaPedagogica=new SgPropuestaPedagogica();
            if (sedeId != null) {
                FiltroPropuestaPedagogica fpp = new FiltroPropuestaPedagogica();
                fpp.setPpeSede(sedeId);
                fpp.setIncluirCampos(new String[]{"ppeNombre","ppeArchivo.achPk","ppeArchivo.achNombre",
                    "ppeArchivo.achExt",
                    "ppeArchivo.achDescripcion",
                    "ppeArchivo.achContentType",
                    "ppeArchivo.achTmpPath",
                    "ppeArchivo.achVersion"});
                fpp.setOrderBy(new String[]{"ppePk"});
                fpp.setAscending(new boolean[]{false});
                List<SgPropuestaPedagogica> list=propuestaPedagogicaRestClient.buscar(fpp);
                
                if(list!=null && !list.isEmpty()){
                    propuestaPedagogica=list.get(0);
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        filtro.setSedePk(null);
    }

    public void limpiar() {
        limpiarCombos();
        filtro = new FiltroPromedioCalificaciones();
    }

    public void cargarCalificacionesPorArea() {
        try {
            filtro = new FiltroPromedioCalificaciones();

            TreeNode selected = treeGradoFilter.getSelectedNode();

            nombreNodoSeleccionado = treeGradoFilter.getNodeFullLabel(selected);

            if (selected != null) {
                if (selected.getType().equals("org")) {
                    SgOrganizacionCurricular org = (SgOrganizacionCurricular) selected.getData();
                    filtro.setOrgCurricularPk(org.getOcuPk());
                }
                if (selected.getType().equals("nivel")) {
                    SgNivel niv = (SgNivel) selected.getData();
                    filtro.setNivelPk(niv.getNivPk());
                }
                if (selected.getType().equals("ciclo")) {
                    SgCiclo cic = (SgCiclo) selected.getData();
                    filtro.setCicloPk(cic.getCicPk());
                }
                if (selected.getType().equals("modalidad")) {
                    SgModalidad mod = (SgModalidad) selected.getData();
                    filtro.setSedModalidadEducativaPk(mod.getModPk());
                }
                if (selected.getType().equals("modalidad_atencion")) {
                    SgModalidadAtencion modAten = (SgModalidadAtencion) selected.getData();
                    filtro.setSedModalidadAtencionPk(modAten.getMatPk());

                    SgModalidad mod = (SgModalidad) selected.getParent().getData();
                    filtro.setSedModalidadEducativaPk(mod.getModPk());
                }
                if (selected.getType().equals("submodalidad_atencion")) {
                    SgSubModalidadAtencion submodAten = (SgSubModalidadAtencion) selected.getData();
                    filtro.setSedSubModalidadPk(submodAten.getSmoPk());

                    SgModalidad mod = (SgModalidad) selected.getParent().getParent().getData();
                    filtro.setSedModalidadEducativaPk(mod.getModPk());
                }
                if (selected.getType().equals("grado")) {
                    SgGrado grado = (SgGrado) selected.getData();
                    filtro.setGradoPk(grado.getGraPk());
                }
            }

            this.calificacionesNacional = sistemaIntegradoClient.obtenerPromedio(filtro);
            ObjectMapperContextResolver contextResolver = new ObjectMapperContextResolver();
            this.promedioNacional = contextResolver.getDefaultMapper().writeValueAsString(this.calificacionesNacional);
            filtro.setSedePk(this.sedeId);
            this.calificacionesInstitucion = sistemaIntegradoClient.obtenerPromedio(filtro);
            this.promedioInstitucion = contextResolver.getDefaultMapper().writeValueAsString(this.calificacionesInstitucion);

            //LOGGER.log(Level.INFO, "promedioNacional " + this.promedioNacional);
            PrimeFaces.current().executeScript("PF('treeSelectBlocker').hide()");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarConfiguracion() {
        renderizarBuscarPorCercania = Boolean.FALSE;
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigoExacto(Constantes.BOTON_BUSCAR_POR_CERCANIA_PORTAL);
            List<SgConfiguracion> conf = catalogoClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                if (conf.get(0).getConValor().equals("1") || conf.get(0).getConValor().equals("true")) {
                    renderizarBuscarPorCercania = Boolean.TRUE;
                }
            }

            fc.setCodigoExacto(Constantes.MENSAJE_PROMEDIO_CALIFICACIONES_SEDE_PORTAL);
            conf = catalogoClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                mensajeInfoPromedioCalificaciones = conf.get(0).getConValor();
            }

        } catch (Exception ex) {

        }
    }

    public void actualizarURLQgis() {
        if (entidadEnEdicion != null && entidadEnEdicion.getSedDireccion() != null && entidadEnEdicion.getSedDireccion().getDirLatitud() != null && entidadEnEdicion.getSedDireccion().getDirLongitud() != null) {
            urlQGIS = applicationBean.getQgisUrl() + "#8/" + entidadEnEdicion.getSedDireccion().getDirLatitud() + "/" + entidadEnEdicion.getSedDireccion().getDirLongitud();
        }
    }

    public void actualizar(SgSede sede) {
        try {
            entidadEnEdicion = sede;
            if (entidadEnEdicion.getSedDireccion() == null) {
                entidadEnEdicion.setSedDireccion(new SgDireccion());
            }
            sedesViewValidator = new SedesViewValidator(entidadEnEdicion);
            FiltroRelInmuebleArchivo fri = new FiltroRelInmuebleArchivo();
            fri.setSedeFk(entidadEnEdicion.getSedPk());
            fri.setPublicable(Boolean.TRUE);
            fri.setIncluirCampos(new String[]{"riaArchivo.achPk", "riaArchivo.achNombre", "riaArchivo.achContentType", "riaArchivo.achTmpPath", "riaArchivo.achExt"});
            List<SgRelInmuebleArchivo> listArchivos = relInmuebleArchivoRestClient.buscar(fri);
            imagenesInfra = new ArrayList(listArchivos.stream().map(c -> c.getRiaArchivo()).collect(Collectors.toList()));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        try {
            //Cargar diplomados
            diplomadosAutorizados = diplomadoClient.buscarDiplomadosAutorizadosSede(entidadEnEdicion.getSedPk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarModulosDiplomado(SgDiplomado diplomado) {
        try {
            if (diplomado != null) {
                diplomadoEnEdicion = diplomado;

                if (!modulosDiplomado.containsKey(diplomado.getDipPk())) {
                    FiltroModuloDiplomado filtroModDip = new FiltroModuloDiplomado();
                    filtroModDip.setDiplomadoFk(diplomado.getDipPk());
                    filtroModDip.setIncluirCampos(new String[]{"mdiNombre", "mdiDescripcion"});
                    modulosDiplomado.put(diplomado.getDipPk(), moduloDiplomadoClient.buscar(filtroModDip));
                }
            }

            diplomadosAutorizados = diplomadoClient.buscarDiplomadosAutorizadosSede(entidadEnEdicion.getSedPk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public String getTituloPagina() {
        return entidadEnEdicion.getSedNombre();
    }

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public String estiloSede() {
        switch (entidadEnEdicion.getSedTipo()) {
            case CED_OFI:
                return "tituloitemSedeCEO";
            case CED_PRI:
                return "tituloitemSedeCEP";
            case UBI_EDUC:
                return "tituloitemSedeEducame";

        }
        return "tituloitemSedeCEO";
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

    public SgSede getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSede entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SedesViewValidator getSedesViewValidator() {
        return sedesViewValidator;
    }

    public void setSedesViewValidator(SedesViewValidator sedesViewValidator) {
        this.sedesViewValidator = sedesViewValidator;
    }

    public List<SgJornadaLaboral> getJornadasLaborales() {
        return jornadasLaborales;
    }

    public void setJornadasLaborales(List<SgJornadaLaboral> jornadasLaborales) {
        this.jornadasLaborales = jornadasLaborales;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public SedeRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SedeRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public List<String> getGaleria() {
        return imagenes;
    }

    public String getOfertaJornadas() {
        if (ofertaJornadas == null) {
            ofertaJornadas = "";
            StringBuilder sb = new StringBuilder();
            if (entidadEnEdicion.getSedJornadasLaborales() != null) {
                for (SgJornadaLaboral row : entidadEnEdicion.getSedJornadasLaborales()) {
                    sb.append(row.getJlaNombre()).append(", ");
                }
                ofertaJornadas = sb.substring(0, sb.length() - 2);
            }
        }
        return ofertaJornadas;
    }

    public void setOfertaJornadas(String ofertaJornadas) {
        this.ofertaJornadas = ofertaJornadas;
    }

    public List<SgArchivo> getImagenesInfra() {
        return imagenesInfra;
    }

    public void setImagenesInfra(List<SgArchivo> imagenesInfra) {
        this.imagenesInfra = imagenesInfra;
    }

    public String getUrlQGIS() {
        return urlQGIS;
    }

    public void setUrlQGIS(String urlQGIS) {
        this.urlQGIS = urlQGIS;
    }

    public Boolean getRenderizarBuscarPorCercania() {
        return renderizarBuscarPorCercania;
    }

    public void setRenderizarBuscarPorCercania(Boolean renderizarBuscarPorCercania) {
        this.renderizarBuscarPorCercania = renderizarBuscarPorCercania;
    }

    public String getPromedioInstitucion() {
        return promedioInstitucion;
    }

    public void setPromedioInstitucion(String promedioInstitucion) {
        this.promedioInstitucion = promedioInstitucion;
    }

    public String getPromedioNacional() {
        return promedioNacional;
    }

    public void setPromedioNacional(String promedioNacional) {
        this.promedioNacional = promedioNacional;
    }

    public Double roundDouble(Double value) {
        if (value == null) {
            return Double.valueOf(0d);
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public TreeNode getFiltrosRoot() {
        return filtrosRoot;
    }

    public void setFiltrosRoot(TreeNode filtrosRoot) {
        this.filtrosRoot = filtrosRoot;
    }

    public String getMensajeInfoPromedioCalificaciones() {
        return mensajeInfoPromedioCalificaciones;
    }

    public String getNombreNodoSeleccionado() {
        return nombreNodoSeleccionado;
    }

    public List<SgDiplomado> getDiplomadosAutorizados() {
        return diplomadosAutorizados;
    }

    public void setDiplomadosAutorizados(List<SgDiplomado> diplomadosAutorizados) {
        this.diplomadosAutorizados = diplomadosAutorizados;
    }

    public HashMap<Long, List<SgModuloDiplomado>> getModulosDiplomado() {
        return modulosDiplomado;
    }

    public void setModulosDiplomado(HashMap<Long, List<SgModuloDiplomado>> modulosDiplomado) {
        this.modulosDiplomado = modulosDiplomado;
    }

    public SgDiplomado getDiplomadoEnEdicion() {
        return diplomadoEnEdicion;
    }

    public void setDiplomadoEnEdicion(SgDiplomado diplomadoEnEdicion) {
        this.diplomadoEnEdicion = diplomadoEnEdicion;
    }

    public SgPropuestaPedagogica getPropuestaPedagogica() {
        return propuestaPedagogica;
    }

    public void setPropuestaPedagogica(SgPropuestaPedagogica propuestaPedagogica) {
        this.propuestaPedagogica = propuestaPedagogica;
    }

}
