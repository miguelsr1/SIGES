/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgInmueblesVulnerabilidades;
import sv.gob.mined.siges.web.dto.SgRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.dto.SgVulnerabilidades;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgDireccion;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoAbastecimiento;
import sv.gob.mined.siges.web.dto.catalogo.SgPropietariosTerreno;
import sv.gob.mined.siges.web.enumerados.EnumTipoUnidadResponsable;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtros.FiltroRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleUnidadRespRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.restclient.VulnerabilidadesRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InmuebleBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InmuebleBean.class.getName());

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private VulnerabilidadesRestClient vulnerabilidadesRestClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private InmueblesSedesRestClient restClient;

    @Inject
    private InmuebleSedesBean inmuebleSedeBean;

    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;

    @Inject
    private RelInmuebleUnidadRespRestClient relInmuebleUnidadRespRestClient;

    @Inject
    private EdificioRestClient edificioRestClient;

    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();

    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean callingPostConstruct;
    private String securityOperation;

    private List<SgInmueblesVulnerabilidades> vulnerabilidadesInmuebles = new ArrayList();
    private List<SgInmueblesVulnerabilidades> vulnerabilidadesInmueblesSeleccionados = new ArrayList();
    private List<SgInmueblesVulnerabilidades> vulnerabilidadesInmueblesAntesDeCambio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgPropietariosTerreno> comboPropietario;
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada;
    private SgRelInmuebleUnidadResp relInmuebleUnidadRespEnEdicion;
    private List<SgRelInmuebleUnidadResp> relInmuebleUnidadRespList;
    private List<EnumTipoUnidadResponsable> tipoUnidadResponsable;
    private Boolean mostrarCartelEliminar;

    public InmuebleBean() {

    }

    @PostConstruct
    public void init() {
        try {
            cargarCombo();
            callingPostConstruct = Boolean.TRUE;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
            cargarEntidad();
            callingPostConstruct = Boolean.FALSE;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void buscarOtrasSedesUnidadAdmin() {
        try {
            relInmuebleUnidadRespList = new ArrayList();
            if (entidadEnEdicion.getTisPk() != null) {
                FiltroRelInmuebleUnidadResp fri = new FiltroRelInmuebleUnidadResp();
                fri.setInmuebleFk(entidadEnEdicion.getTisPk());
                relInmuebleUnidadRespList = relInmuebleUnidadRespRestClient.buscar(fri);
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombo() {
        try {
            tipoUnidadResponsable = new ArrayList(Arrays.asList(EnumTipoUnidadResponsable.values()));

            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"pdtNombre"});
            filtro.setAscending(new boolean[]{true});
            List<SgPropietariosTerreno> listProp = catalogosRestClient.buscarPropietariosTerreno(filtro);
            comboPropietario = new SofisComboG(listProp, "pdtNombre");
            comboPropietario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"ffiNombre"});
            filtro.setAscending(new boolean[]{true});
            List<SgFuenteFinanciamiento> listFuente = catalogosRestClient.buscarFuenteFinanciamiento(filtro);
            comboFuenteFinanciamiento = new SofisComboG(listFuente, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void comboPropietarioSelected() {
        if (entidadEnEdicion != null) {
            entidadEnEdicion.setTisPropietariosTerreno(comboPropietario.getSelectedT());
        }
    }

    public void propietarioSelected() {
        comboPropietario.setSelected(-1);
        entidadEnEdicion.setTisDetallePropietario(null);
    }

    public void cargarEntidad() {
        try {
            sedeSeleccionada = null;
            cargarSedePorDefecto();
            cargarVulnerabilidadesDeInmueble();
            actualizarCombos();
            buscarOtrasSedesUnidadAdmin();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarCombos() {
        if (entidadEnEdicion.getTisPropietariosTerreno() != null) {
            comboPropietario.setSelectedT(entidadEnEdicion.getTisPropietariosTerreno());
        }
        if (entidadEnEdicion.getTisFuenteFinanciamiento() != null) {
            comboFuenteFinanciamiento.setSelectedT(entidadEnEdicion.getTisFuenteFinanciamiento());
        }
    }

    public void cargarSedePorDefecto() throws Exception {
        if (BooleanUtils.isTrue(entidadEnEdicion.getTisPerteneceSede())) {
            if (sessionBean.getSedeDefecto() != null && entidadEnEdicion.getTisPk() == null) {
                sedeSeleccionada = sessionBean.getSedeDefecto();
                entidadEnEdicion.setTisSedeFk(sedeSeleccionada);
            } else {
                if (entidadEnEdicion != null && entidadEnEdicion.getTisPk() != null) {
                    if (sedeSeleccionada == null) {
                        sedeSeleccionada = entidadEnEdicion.getTisSedeFk();
                    }
                }
            }
        }
        if (BooleanUtils.isFalse(entidadEnEdicion.getTisPerteneceSede())) {
            unidadAdministrativaSeleccionada = entidadEnEdicion.getTisAfUnidadAdministrativa();
        }
    }

    public void updateAddressCoordinates() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lat = params.get("latitude");
        String lng = params.get("longitude");
        SgDireccion dir = entidadEnEdicion.getTisDireccion();
        if (lat != null) {
            dir.setDirLatitud(Double.parseDouble(lat));
        }
        if (lng != null) {
            dir.setDirLongitud(Double.parseDouble(lng));
        }
    }

    public void seleccionarSede() {
        try {
            entidadEnEdicion.setTisSedeFk(sedeSeleccionada);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarVulnerabilidadesDeInmueble() {
        try {
            List<SgVulnerabilidades> vulnerabilidades = new ArrayList();
            List<SgVulnerabilidades> listAuxiliar = new ArrayList();

            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"vulCodigo"});
            filtro.setIncluirCampos(new String[]{"vulNombre", "vulVersion"});

            vulnerabilidades = vulnerabilidadesRestClient.buscar(filtro);

            vulnerabilidadesInmuebles = new ArrayList();
            vulnerabilidadesInmueblesSeleccionados = new ArrayList();

            if (entidadEnEdicion != null) {
                for (SgInmueblesVulnerabilidades vunInmueble : entidadEnEdicion.getIvuInmueblesSede()) {
                    if (vulnerabilidades.contains(vunInmueble.getIvuVulnerabilidadesFk())) {
                        vulnerabilidadesInmueblesSeleccionados.add(vunInmueble);
                        vulnerabilidadesInmuebles.add(vunInmueble);
                    }
                }
            }
            listAuxiliar = vulnerabilidadesInmueblesSeleccionados.stream().map(m -> m.getIvuVulnerabilidadesFk()).collect(Collectors.toList());
            for (SgVulnerabilidades vul : vulnerabilidades) {
                SgInmueblesVulnerabilidades vulInmueble = new SgInmueblesVulnerabilidades();
                if (!listAuxiliar.contains(vul)) {
                    vulInmueble.setIvuVulnerabilidadesFk(vul);
                    vulnerabilidadesInmuebles.add(vulInmueble);
                }

            }

            Collections.sort(vulnerabilidadesInmuebles, (o1, o2) -> o1.getIvuVulnerabilidadesFk().getVulNombre().compareTo(o2.getIvuVulnerabilidadesFk().getVulNombre()));

            vulnerabilidadesInmueblesAntesDeCambio = vulnerabilidadesInmueblesSeleccionados;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            if (securityOperation != null) {
                fil.setSecurityOperation(securityOperation);
            }
            List<TipoSede> sedTipo = new ArrayList();
            sedTipo.add(TipoSede.CED_OFI);
            sedTipo.add(TipoSede.CED_PRI);
            fil.setSedPrivadaSubvencionada(Boolean.TRUE);
            fil.setSedTipos(sedTipo);
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
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

    public List<SgAfUnidadesAdministrativas> completeUnidadAdministrativa(String query) {
        try {
            FiltroUnidadesAdministrativas fil = new FiltroUnidadesAdministrativas();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setIncluirCampos(new String[]{"uadCodigo","uadNombre","uadVersion"});
            List<SgAfUnidadesAdministrativas> resultado = unidadesAdministrativasRestClient.buscar(fil);
            resultado = resultado.stream().sorted((a,b) -> Integer.compare(Integer.parseInt(a.getUadCodigo()),Integer.parseInt(b.getUadCodigo()))).collect(Collectors.toList());
            return resultado;
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getIvuInmueblesSede() != null) {
                entidadEnEdicion.setIvuInmueblesSede(new ArrayList());
                entidadEnEdicion.getIvuInmueblesSede().addAll(vulnerabilidadesInmueblesSeleccionados);

            }
            if (BooleanUtils.isTrue(entidadEnEdicion.getTisPerteneceSede())) {
                entidadEnEdicion.setTisAfUnidadAdministrativa(null);
            } else {
                if (BooleanUtils.isFalse(entidadEnEdicion.getTisPerteneceSede())) {
                    entidadEnEdicion.setTisSedeFk(null);
                    entidadEnEdicion.setTisAfUnidadAdministrativa(unidadAdministrativaSeleccionada);
                } else {
                    entidadEnEdicion.setTisSedeFk(null);
                    entidadEnEdicion.setTisAfUnidadAdministrativa(null);
                }
            }
            entidadEnEdicion.setTisFuenteFinanciamiento(comboFuenteFinanciamiento.getSelectedT());

            //Esta pestaña no tiene el dato tipoAbastecimiento, al mandar a guardar la entidad este se pierde, por eso se guarda antes de enviar
            List<SgInfTipoAbastecimiento> tipoAbs = entidadEnEdicion.getTisTipoAbastecimiento();
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            entidadEnEdicion.setTisTipoAbastecimiento(tipoAbs);
            inmuebleSedeBean.setEntidadEnEdicion(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            cargarVulnerabilidadesDeInmueble();
            buscarOtrasSedesUnidadAdmin();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarTipoOtrasSedesUa() {
        relInmuebleUnidadRespEnEdicion.setRiuAfUnidadAdministrativa(null);
        relInmuebleUnidadRespEnEdicion.setRiuSedeFk(null);
    }

    public void agregarOtraSedeUnidadAdministrativa() {
        relInmuebleUnidadRespEnEdicion = new SgRelInmuebleUnidadResp();
    }

    public void prepararEliminarOtraSedeUnidad(SgRelInmuebleUnidadResp relInmueble) {
        try {
            mostrarCartelEliminar=Boolean.FALSE;
            FiltroEdificio fe = new FiltroEdificio();
            fe.setRelInmuebleUnidadResFk(relInmueble.getRiuPk());
            fe.setIncluirCampos(new String[]{"ediPk"});
            fe.setMaxResults(1L);
            List<SgEdificio> edi = edificioRestClient.buscar(fe);

            if(edi.size()>0){
                mostrarCartelEliminar=Boolean.TRUE;
            }
            
            relInmuebleUnidadRespEnEdicion = (SgRelInmuebleUnidadResp) SerializationUtils.clone(relInmueble);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarOtraSedeUnidad() {
        try {
            relInmuebleUnidadRespRestClient.eliminar(relInmuebleUnidadRespEnEdicion.getRiuPk());
            relInmuebleUnidadRespEnEdicion = null;
            buscarOtrasSedesUnidadAdmin();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeOtrasSede(String query) {
        try {
            List<SgSede> listSede = relInmuebleUnidadRespList.stream().filter(c -> c.getRiuSedeFk() != null).map(c -> c.getRiuSedeFk()).collect(Collectors.toList());

            FiltroSedes fil = new FiltroSedes();
            if (securityOperation != null) {
                fil.setSecurityOperation(securityOperation);
            }
            List<TipoSede> sedTipo = new ArrayList();
            sedTipo.add(TipoSede.CED_OFI);
            sedTipo.add(TipoSede.CED_PRI);
            fil.setSedPrivadaSubvencionada(Boolean.TRUE);
            fil.setSedTipos(sedTipo);
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            SgSede sedePrincipal = entidadEnEdicion.getTisSedeFk();
            List<SgSede> listSedeBusqueda = restSede.buscar(fil);
            listSedeBusqueda.remove(sedePrincipal);
            return listSede != null ? listSedeBusqueda.stream().filter(i -> !listSede.contains(i)).collect(Collectors.toList())
                    : listSedeBusqueda;
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgAfUnidadesAdministrativas> completeOtrasUnidadAdministrativa(String query) {
        try {
            List<SgAfUnidadesAdministrativas> listUnidadAdmin = relInmuebleUnidadRespList.stream().filter(c -> c.getRiuAfUnidadAdministrativa() != null).map(c -> c.getRiuAfUnidadAdministrativa()).collect(Collectors.toList());

            FiltroUnidadesAdministrativas fil = new FiltroUnidadesAdministrativas();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setIncluirCampos(new String[]{"uadCodigo","uadNombre", "uadVersion"});
            List<SgAfUnidadesAdministrativas> resultado = unidadesAdministrativasRestClient.buscar(fil);
            resultado = resultado.stream().sorted((a,b) -> Integer.compare(Integer.parseInt(a.getUadCodigo()),Integer.parseInt(b.getUadCodigo()))).collect(Collectors.toList());
            List<SgAfUnidadesAdministrativas> resultado2 = unidadesAdministrativasRestClient.buscar(fil).stream().sorted((a,b) -> Integer.compare(Integer.parseInt(a.getUadCodigo()),Integer.parseInt(b.getUadCodigo()))).filter(c -> !listUnidadAdmin.contains(c)).collect(Collectors.toList());
            return listUnidadAdmin != null ? resultado2 :resultado;
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void guardarRelInmuebleUnidadResp() {
        try {
            relInmuebleUnidadRespEnEdicion.setRiuInmuebleFk(entidadEnEdicion);
            relInmuebleUnidadRespEnEdicion = relInmuebleUnidadRespRestClient.guardar(relInmuebleUnidadRespEnEdicion);

            PrimeFaces.current().executeScript("PF('dialogOtraSedUn').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarOtrasSedesUnidadAdmin();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgOtrSede", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgInmueblesSedes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInmueblesSedes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgInmueblesVulnerabilidades> getVulnerabilidadesInmuebles() {
        return vulnerabilidadesInmuebles;
    }

    public void setVulnerabilidadesInmuebles(List<SgInmueblesVulnerabilidades> vulnerabilidadesInmuebles) {
        this.vulnerabilidadesInmuebles = vulnerabilidadesInmuebles;
    }

    public List<SgInmueblesVulnerabilidades> getVulnerabilidadesInmueblesSeleccionados() {
        return vulnerabilidadesInmueblesSeleccionados;
    }

    public void setVulnerabilidadesInmueblesSeleccionados(List<SgInmueblesVulnerabilidades> vulnerabilidadesInmueblesSeleccionados) {
        this.vulnerabilidadesInmueblesSeleccionados = vulnerabilidadesInmueblesSeleccionados;
    }

    public List<SgInmueblesVulnerabilidades> getVulnerabilidadesInmueblesAntesDeCambio() {
        return vulnerabilidadesInmueblesAntesDeCambio;
    }

    public void setVulnerabilidadesInmueblesAntesDeCambio(List<SgInmueblesVulnerabilidades> vulnerabilidadesInmueblesAntesDeCambio) {
        this.vulnerabilidadesInmueblesAntesDeCambio = vulnerabilidadesInmueblesAntesDeCambio;
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

    public Boolean getCallingPostConstruct() {
        return callingPostConstruct;
    }

    public void setCallingPostConstruct(Boolean callingPostConstruct) {
        this.callingPostConstruct = callingPostConstruct;
    }

    public SofisComboG<SgPropietariosTerreno> getComboPropietario() {
        return comboPropietario;
    }

    public void setComboPropietario(SofisComboG<SgPropietariosTerreno> comboPropietario) {
        this.comboPropietario = comboPropietario;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SgAfUnidadesAdministrativas getUnidadAdministrativaSeleccionada() {
        return unidadAdministrativaSeleccionada;
    }

    public void setUnidadAdministrativaSeleccionada(SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada) {
        this.unidadAdministrativaSeleccionada = unidadAdministrativaSeleccionada;
    }

    public SgRelInmuebleUnidadResp getRelInmuebleUnidadRespEnEdicion() {
        return relInmuebleUnidadRespEnEdicion;
    }

    public void setRelInmuebleUnidadRespEnEdicion(SgRelInmuebleUnidadResp relInmuebleUnidadRespEnEdicion) {
        this.relInmuebleUnidadRespEnEdicion = relInmuebleUnidadRespEnEdicion;
    }

    public List<SgRelInmuebleUnidadResp> getRelInmuebleUnidadRespList() {
        return relInmuebleUnidadRespList;
    }

    public void setRelInmuebleUnidadRespList(List<SgRelInmuebleUnidadResp> relInmuebleUnidadRespList) {
        this.relInmuebleUnidadRespList = relInmuebleUnidadRespList;
    }

    public List<EnumTipoUnidadResponsable> getTipoUnidadResponsable() {
        return tipoUnidadResponsable;
    }

    public void setTipoUnidadResponsable(List<EnumTipoUnidadResponsable> tipoUnidadResponsable) {
        this.tipoUnidadResponsable = tipoUnidadResponsable;
    }

    public Boolean getMostrarCartelEliminar() {
        return mostrarCartelEliminar;
    }

    public void setMostrarCartelEliminar(Boolean mostrarCartelEliminar) {
        this.mostrarCartelEliminar = mostrarCartelEliminar;
    }

}
