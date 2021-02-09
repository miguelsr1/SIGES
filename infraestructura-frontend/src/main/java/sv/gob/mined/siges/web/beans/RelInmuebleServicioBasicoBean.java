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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.web.dto.SgRelInmuebleAccesibilidad;
import sv.gob.mined.siges.web.dto.SgRelInmuebleAlmacenamientoAgua;
import sv.gob.mined.siges.web.dto.SgRelInmuebleManejoDesechos;
import sv.gob.mined.siges.web.dto.SgRelInmuebleServicioSanitario;
import sv.gob.mined.siges.web.dto.SgRelInmuebleTipoDrenaje;
import sv.gob.mined.siges.web.dto.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgInfAccesibilidad;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoAbastecimiento;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoManejoDesechos;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTratamientoAgua;
import sv.gob.mined.siges.web.enumerados.EnumEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleAccesibilidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleAlmacenamientoAgua;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleManejoDesechos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleServicioSanitario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleTipoDrenaje;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleAbastecimientoAguaRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleAccesibilidadRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleAlmacenamientoAguaRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleManejoDesechosRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleServicioBasicoRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleServicioSanitarioRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleTipoDrenajeRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleServicioBasicoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleServicioBasicoBean.class.getName());

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private InmueblesSedesRestClient restClient;

    @Inject
    private RelInmuebleServicioBasicoRestClient relInmueblesServicioBasicoRestClient;

    @Inject
    private RelInmuebleServicioSanitarioRestClient relInmueblesServicioSanitarioRestClient;

    @Inject
    private RelInmuebleAccesibilidadRestClient relInmuebleAccesibilidadRestClient;

    @Inject
    private RelInmuebleAbastecimientoAguaRestClient relInmuebleAbastecimientoAguaRestClient;

    @Inject
    private RelInmuebleAlmacenamientoAguaRestClient relInmuebleAlmacenamientoAguaRestClient;

    @Inject
    private RelInmuebleTipoDrenajeRestClient relInmuebleTipoDrenajeRestClient;

    @Inject
    private RelInmuebleManejoDesechosRestClient relInmuebleManejoDesechosRestClient;

    @Inject
    private InmuebleSedesBean inmuebleSedeBean;

    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();

    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;

    private List<SgRelInmuebleServicioSanitario> inmueblesServicioSanitario = new ArrayList();
    private List<SgRelInmuebleServicioSanitario> inmueblesServicioSanitarioSeleccionado = new ArrayList();

    private List<SgRelInmuebleAccesibilidad> inmueblesAccesibilidad = new ArrayList();
    private List<SgRelInmuebleAccesibilidad> inmueblesAccesibilidadSeleccionados = new ArrayList();

    private List<SgRelInmuebleAbastecimientoAgua> inmueblesAbastecimientoAgua = new ArrayList();
    private List<SgRelInmuebleAbastecimientoAgua> inmueblesAbastecimientoAguaSeleccionados = new ArrayList();

    private List<SgRelInmuebleAlmacenamientoAgua> inmueblesAlmacenamientoAgua = new ArrayList();
    private List<SgRelInmuebleAlmacenamientoAgua> inmueblesAlmacenamientoAguaSeleccionados = new ArrayList();

    private List<SgRelInmuebleTipoDrenaje> inmueblesTipoDrenaje = new ArrayList();
    private List<SgRelInmuebleTipoDrenaje> inmueblesTipoDrenajeSeleccionados = new ArrayList();

    private List<SgRelInmuebleManejoDesechos> inmueblesManejoDesechos = new ArrayList();
    private List<SgRelInmuebleManejoDesechos> inmueblesManejoDesechosSeleccionados = new ArrayList();

    private Integer paginado = 10;
    private Long totalResultados;
    private Boolean beanIniciado = Boolean.FALSE;
    private Boolean[] habilitarEntradaTextoServicioSanitario;

    private Boolean[] habilitarEntradaTextoAccesibilidad;
    private Boolean[] habilitarEntradaTextoAbastecimientoAgua;
    private Boolean[] habilitarEntradaTextoAlmacenamientoAgua;
    private Boolean[] habilitarEntradaTextoTipoDrenaje;
    private Boolean[] habilitarEntradaTextoManejoDesechos;
    private SofisComboG<EnumEstado> comboEstado;

    public RelInmuebleServicioBasicoBean() {

    }

    @PostConstruct
    public void init() {
        try {

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
            cargarCombos();
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {
        List<EnumEstado> estados = new ArrayList(Arrays.asList(EnumEstado.values()));
        comboEstado = new SofisComboG(estados, "text");
        comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (entidadEnEdicion.getTisCondicionesInstalacionesElec() != null) {
            comboEstado.setSelectedT(entidadEnEdicion.getTisCondicionesInstalacionesElec());
        }
    }

    public void cargarEntidad() {
        try {
            if (!beanIniciado) {
                cargarInmueblesServicioSanitario();
                cargarTratamientoAgua();
                cargarManejoDesechos();
                cargarAccesibilidad();
                beanIniciado = Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarTratamientoAgua() {
        try {

            List<SgInfTratamientoAgua> espacioComunList = new ArrayList();
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarTratamientoAgua(filtro);

            List<SgInfTratamientoAgua> listAbastecimientoAgua = espacioComunList.stream().filter(c -> BooleanUtils.isTrue(c.getTraAplicaAbastecimientoAgua())).collect(Collectors.toList());
            List<SgInfTratamientoAgua> listAlmacenamientoAgua = espacioComunList.stream().filter(c -> BooleanUtils.isTrue(c.getTraAplicaAlmacenamientoAgua())).collect(Collectors.toList());
            List<SgInfTratamientoAgua> listDrenaje = espacioComunList.stream().filter(c -> BooleanUtils.isTrue(c.getTraAplicaDrenaje())).collect(Collectors.toList());

            //CARGA ABASTECIMIENTO AGUA
            inmueblesAbastecimientoAgua = new ArrayList();
            inmueblesAbastecimientoAguaSeleccionados = new ArrayList();

            FiltroRelInmuebleAbastecimientoAgua fic = new FiltroRelInmuebleAbastecimientoAgua();
            fic.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesAbastecimientoAguaSeleccionados = relInmuebleAbastecimientoAguaRestClient.buscar(fic);

            if (!inmueblesAbastecimientoAguaSeleccionados.isEmpty()) {
                inmueblesAbastecimientoAgua.addAll(inmueblesAbastecimientoAguaSeleccionados);
                List<SgInfTratamientoAgua> espaciosSeleccionados = inmueblesAbastecimientoAgua.stream().map(c -> c.getIaaAbastecimientoAgua()).collect(Collectors.toList());
                for (SgInfTratamientoAgua esp : listAbastecimientoAgua) {
                    if (!espaciosSeleccionados.contains(esp)) {
                        SgRelInmuebleAbastecimientoAgua relEspCom = new SgRelInmuebleAbastecimientoAgua();
                        relEspCom.setIaaAbastecimientoAgua(esp);
                        inmueblesAbastecimientoAgua.add(relEspCom);
                    }
                }
            } else {
                for (SgInfTratamientoAgua esp : listAbastecimientoAgua) {
                    SgRelInmuebleAbastecimientoAgua relEspCom = new SgRelInmuebleAbastecimientoAgua();
                    relEspCom.setIaaAbastecimientoAgua(esp);
                    inmueblesAbastecimientoAgua.add(relEspCom);
                }
            }

            Collections.sort(inmueblesAbastecimientoAgua, (o1, o2) -> o1.getIaaAbastecimientoAgua().getTraNombre().compareTo(o2.getIaaAbastecimientoAgua().getTraNombre()));
            Collections.sort(inmueblesAbastecimientoAgua, (o1,o2)-> (o1.getIaaAbastecimientoAgua().getTraOrden() != null ? o1.getIaaAbastecimientoAgua().getTraOrden()  : 0) - (o2.getIaaAbastecimientoAgua().getTraOrden() != null ? o2.getIaaAbastecimientoAgua().getTraOrden()  : 0));
            
            habilitarEntradaTextoAbastecimientoAgua = new Boolean[inmueblesAbastecimientoAgua.size()];
            Arrays.fill(habilitarEntradaTextoAbastecimientoAgua, Boolean.FALSE);

            for (SgRelInmuebleAbastecimientoAgua relEsp : inmueblesAbastecimientoAguaSeleccionados) {
                habilitarEntradaTextoAbastecimientoAgua[inmueblesAbastecimientoAgua.indexOf(relEsp)] = Boolean.TRUE;
            }

            //CARGA ALMACENAMIENTO AGUA
            inmueblesAlmacenamientoAgua = new ArrayList();
            inmueblesAlmacenamientoAguaSeleccionados = new ArrayList();
            FiltroRelInmuebleAlmacenamientoAgua fac = new FiltroRelInmuebleAlmacenamientoAgua();
            fac.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesAlmacenamientoAguaSeleccionados = relInmuebleAlmacenamientoAguaRestClient.buscar(fac);

            if (!inmueblesAlmacenamientoAguaSeleccionados.isEmpty()) {
                inmueblesAlmacenamientoAgua.addAll(inmueblesAlmacenamientoAguaSeleccionados);
                List<SgInfTratamientoAgua> listAlm = inmueblesAlmacenamientoAgua.stream().map(c -> c.getIalAlmacenamientoAgua()).collect(Collectors.toList());
                for (SgInfTratamientoAgua esp : listAlmacenamientoAgua) {
                    if (!listAlm.contains(esp)) {
                        SgRelInmuebleAlmacenamientoAgua relEspCom = new SgRelInmuebleAlmacenamientoAgua();
                        relEspCom.setIalAlmacenamientoAgua(esp);
                        inmueblesAlmacenamientoAgua.add(relEspCom);
                    }
                }
            } else {
                for (SgInfTratamientoAgua esp : listAlmacenamientoAgua) {
                    SgRelInmuebleAlmacenamientoAgua relEspCom = new SgRelInmuebleAlmacenamientoAgua();
                    relEspCom.setIalAlmacenamientoAgua(esp);
                    inmueblesAlmacenamientoAgua.add(relEspCom);
                }
            }

            Collections.sort(inmueblesAlmacenamientoAgua, (o1, o2) -> o1.getIalAlmacenamientoAgua().getTraNombre().compareTo(o2.getIalAlmacenamientoAgua().getTraNombre()));
            Collections.sort(inmueblesAlmacenamientoAgua, (o1,o2)-> (o1.getIalAlmacenamientoAgua().getTraOrden() != null ? o1.getIalAlmacenamientoAgua().getTraOrden()  : 0) - (o2.getIalAlmacenamientoAgua().getTraOrden() != null ? o2.getIalAlmacenamientoAgua().getTraOrden()  : 0));
            
            habilitarEntradaTextoAlmacenamientoAgua = new Boolean[inmueblesAlmacenamientoAgua.size()];
            Arrays.fill(habilitarEntradaTextoAlmacenamientoAgua, Boolean.FALSE);

            for (SgRelInmuebleAlmacenamientoAgua relEsp : inmueblesAlmacenamientoAguaSeleccionados) {
                habilitarEntradaTextoAlmacenamientoAgua[inmueblesAlmacenamientoAgua.indexOf(relEsp)] = Boolean.TRUE;
            }

            //CARGA TIPO DRENAJE
            inmueblesTipoDrenaje = new ArrayList();
            inmueblesTipoDrenajeSeleccionados = new ArrayList();
            FiltroRelInmuebleTipoDrenaje ftd = new FiltroRelInmuebleTipoDrenaje();
            ftd.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesTipoDrenajeSeleccionados = relInmuebleTipoDrenajeRestClient.buscar(ftd);

            if (!inmueblesTipoDrenajeSeleccionados.isEmpty()) {
                inmueblesTipoDrenaje.addAll(inmueblesTipoDrenajeSeleccionados);
                List<SgInfTratamientoAgua> listAlm = inmueblesTipoDrenaje.stream().map(c -> c.getItdTipoDrenaje()).collect(Collectors.toList());
                for (SgInfTratamientoAgua esp : listDrenaje) {
                    if (!listAlm.contains(esp)) {
                        SgRelInmuebleTipoDrenaje relEspCom = new SgRelInmuebleTipoDrenaje();
                        relEspCom.setItdTipoDrenaje(esp);
                        inmueblesTipoDrenaje.add(relEspCom);
                    }
                }
            } else {
                for (SgInfTratamientoAgua esp : listDrenaje) {
                    SgRelInmuebleTipoDrenaje relEspCom = new SgRelInmuebleTipoDrenaje();
                    relEspCom.setItdTipoDrenaje(esp);
                    inmueblesTipoDrenaje.add(relEspCom);
                }
            }

            Collections.sort(inmueblesTipoDrenaje, (o1, o2) -> o1.getItdTipoDrenaje().getTraNombre().compareTo(o2.getItdTipoDrenaje().getTraNombre()));
            Collections.sort(inmueblesTipoDrenaje, (o1,o2)-> (o1.getItdTipoDrenaje().getTraOrden() != null ? o1.getItdTipoDrenaje().getTraOrden()  : 0) - (o2.getItdTipoDrenaje().getTraOrden() != null ? o2.getItdTipoDrenaje().getTraOrden()  : 0));
            

            habilitarEntradaTextoTipoDrenaje = new Boolean[inmueblesTipoDrenaje.size()];
            Arrays.fill(habilitarEntradaTextoTipoDrenaje, Boolean.FALSE);

            for (SgRelInmuebleTipoDrenaje relEsp : inmueblesTipoDrenajeSeleccionados) {
                habilitarEntradaTextoTipoDrenaje[inmueblesTipoDrenaje.indexOf(relEsp)] = Boolean.TRUE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarManejoDesechos() {
        try {

            inmueblesManejoDesechos = new ArrayList();
            inmueblesManejoDesechosSeleccionados = new ArrayList();
            List<SgInfTipoManejoDesechos> espacioComunList = new ArrayList();

            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarManejoDeschos(filtro);

            FiltroRelInmuebleManejoDesechos fic = new FiltroRelInmuebleManejoDesechos();
            fic.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesManejoDesechosSeleccionados = relInmuebleManejoDesechosRestClient.buscar(fic);

            if (!inmueblesManejoDesechosSeleccionados.isEmpty()) {
                inmueblesManejoDesechos.addAll(inmueblesManejoDesechosSeleccionados);
                List<SgInfTipoManejoDesechos> espaciosSeleccionados = inmueblesManejoDesechos.stream().map(c -> c.getImdManejoDesechos()).collect(Collectors.toList());
                for (SgInfTipoManejoDesechos esp : espacioComunList) {
                    if (!espaciosSeleccionados.contains(esp)) {
                        SgRelInmuebleManejoDesechos relEspCom = new SgRelInmuebleManejoDesechos();
                        relEspCom.setImdManejoDesechos(esp);
                        inmueblesManejoDesechos.add(relEspCom);
                    }
                }
            } else {
                for (SgInfTipoManejoDesechos esp : espacioComunList) {
                    SgRelInmuebleManejoDesechos relEspCom = new SgRelInmuebleManejoDesechos();
                    relEspCom.setImdManejoDesechos(esp);
                    inmueblesManejoDesechos.add(relEspCom);
                }
            }

            Collections.sort(inmueblesManejoDesechos, (o1, o2) -> o1.getImdManejoDesechos().getTmdNombre().compareTo(o2.getImdManejoDesechos().getTmdNombre()));
            Collections.sort(inmueblesManejoDesechos, (o1,o2)-> (o1.getImdManejoDesechos().getTmdOrden()!= null ? o1.getImdManejoDesechos().getTmdOrden()  : 0) - (o2.getImdManejoDesechos().getTmdOrden() != null ? o2.getImdManejoDesechos().getTmdOrden()  : 0));
            
            
            habilitarEntradaTextoManejoDesechos = new Boolean[inmueblesManejoDesechos.size()];
            Arrays.fill(habilitarEntradaTextoManejoDesechos, Boolean.FALSE);

            for (SgRelInmuebleManejoDesechos relEsp : inmueblesManejoDesechosSeleccionados) {
                habilitarEntradaTextoManejoDesechos[inmueblesManejoDesechos.indexOf(relEsp)] = Boolean.TRUE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarAccesibilidad() {
        try {

            inmueblesAccesibilidad = new ArrayList();
            inmueblesAccesibilidadSeleccionados = new ArrayList();
            List<SgInfAccesibilidad> espacioComunList = new ArrayList();

            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setHabilitado(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarAccesibilidad(filtro);

            FiltroRelInmuebleAccesibilidad fic = new FiltroRelInmuebleAccesibilidad();
            fic.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesAccesibilidadSeleccionados = relInmuebleAccesibilidadRestClient.buscar(fic);

            if (!inmueblesAccesibilidadSeleccionados.isEmpty()) {
                inmueblesAccesibilidad.addAll(inmueblesAccesibilidadSeleccionados);
                List<SgInfAccesibilidad> espaciosSeleccionados = inmueblesAccesibilidad.stream().map(c -> c.getIacAccesibilidad()).collect(Collectors.toList());
                for (SgInfAccesibilidad esp : espacioComunList) {
                    if (!espaciosSeleccionados.contains(esp)) {
                        SgRelInmuebleAccesibilidad relEspCom = new SgRelInmuebleAccesibilidad();
                        relEspCom.setIacAccesibilidad(esp);
                        inmueblesAccesibilidad.add(relEspCom);
                    }
                }
            } else {
                for (SgInfAccesibilidad esp : espacioComunList) {
                    SgRelInmuebleAccesibilidad relEspCom = new SgRelInmuebleAccesibilidad();
                    relEspCom.setIacAccesibilidad(esp);
                    inmueblesAccesibilidad.add(relEspCom);
                }
            }

            Collections.sort(inmueblesAccesibilidad, (o1, o2) -> o1.getIacAccesibilidad().getAccNombre().compareTo(o2.getIacAccesibilidad().getAccNombre()));
            Collections.sort(inmueblesAccesibilidad, (o1,o2)-> (o1.getIacAccesibilidad().getAccOrden()!= null ? o1.getIacAccesibilidad().getAccOrden()  : 0) - (o2.getIacAccesibilidad().getAccOrden() != null ? o2.getIacAccesibilidad().getAccOrden()  : 0));
            

            habilitarEntradaTextoAccesibilidad = new Boolean[inmueblesAccesibilidad.size()];
            Arrays.fill(habilitarEntradaTextoAccesibilidad, Boolean.FALSE);

            for (SgRelInmuebleAccesibilidad relEsp : inmueblesAccesibilidadSeleccionados) {
                habilitarEntradaTextoAccesibilidad[inmueblesAccesibilidad.indexOf(relEsp)] = Boolean.TRUE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public List<SgInfTipoAbastecimiento> completeTipoAbastecimiento(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"tabNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getTisTipoAbastecimiento() != null
                    ? catalogosRestClient.buscarTipoAbastecimiento(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getTisTipoAbastecimiento().contains(i))
                            .collect(Collectors.toList())
                    : catalogosRestClient.buscarTipoAbastecimiento(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarInmueblesServicioSanitario() {
        try {
            entidadEnEdicion = restClient.obtenerPorId(entidadEnEdicion.getTisPk());
            inmueblesServicioSanitario = new ArrayList();
            inmueblesServicioSanitarioSeleccionado = new ArrayList();
            List<SgTipoServicioSanitario> espacioComunList = new ArrayList();

            FiltroTipoServicioSanitario filtro = new FiltroTipoServicioSanitario();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setTssAplicaInmueble(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarTipoServicioSanitario(filtro);

            FiltroRelInmuebleServicioSanitario fic = new FiltroRelInmuebleServicioSanitario();
            fic.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesServicioSanitarioSeleccionado = relInmueblesServicioSanitarioRestClient.buscar(fic);

            if (!inmueblesServicioSanitarioSeleccionado.isEmpty()) {
                inmueblesServicioSanitario.addAll(inmueblesServicioSanitarioSeleccionado);
                List<SgTipoServicioSanitario> espaciosSeleccionados = inmueblesServicioSanitario.stream().map(c -> c.getRitServicioSanitario()).collect(Collectors.toList());
                for (SgTipoServicioSanitario esp : espacioComunList) {
                    if (!espaciosSeleccionados.contains(esp)) {
                        SgRelInmuebleServicioSanitario relEspCom = new SgRelInmuebleServicioSanitario();
                        relEspCom.setRitServicioSanitario(esp);
                        inmueblesServicioSanitario.add(relEspCom);
                    }
                }
            } else {
                for (SgTipoServicioSanitario esp : espacioComunList) {
                    SgRelInmuebleServicioSanitario relEspCom = new SgRelInmuebleServicioSanitario();
                    relEspCom.setRitServicioSanitario(esp);
                    inmueblesServicioSanitario.add(relEspCom);
                }
            }

            Collections.sort(inmueblesServicioSanitario, (o1, o2) -> o1.getRitServicioSanitario().getTssNombre().compareTo(o2.getRitServicioSanitario().getTssNombre()));
           
            habilitarEntradaTextoServicioSanitario = new Boolean[inmueblesServicioSanitario.size()];
            Arrays.fill(habilitarEntradaTextoServicioSanitario, Boolean.FALSE);

            for (SgRelInmuebleServicioSanitario relEsp : inmueblesServicioSanitarioSeleccionado) {
                habilitarEntradaTextoServicioSanitario[inmueblesServicioSanitario.indexOf(relEsp)] = Boolean.TRUE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void guardar() {
        try {
            entidadEnEdicion.setTisServicioSanitario(new ArrayList());
            entidadEnEdicion.getTisServicioSanitario().addAll(inmueblesServicioSanitarioSeleccionado);
            entidadEnEdicion.setTisAccesibilidad(new ArrayList());
            entidadEnEdicion.getTisAccesibilidad().addAll(inmueblesAccesibilidadSeleccionados);
            entidadEnEdicion.setTisAbastecimientoAgua(new ArrayList());
            entidadEnEdicion.getTisAbastecimientoAgua().addAll(inmueblesAbastecimientoAguaSeleccionados);
            entidadEnEdicion.setTisAlmacenamientoAgua(new ArrayList());
            entidadEnEdicion.getTisAlmacenamientoAgua().addAll(inmueblesAlmacenamientoAguaSeleccionados);
            entidadEnEdicion.setTisTipoDrenaje(new ArrayList());
            entidadEnEdicion.getTisTipoDrenaje().addAll(inmueblesTipoDrenajeSeleccionados);
            entidadEnEdicion.setTisInmuebleManejoDesechos(new ArrayList());
            entidadEnEdicion.getTisInmuebleManejoDesechos().addAll(inmueblesManejoDesechosSeleccionados);
            if (!BooleanUtils.isTrue(entidadEnEdicion.getTisTieneTransformador())) {
                entidadEnEdicion.setTisCapacidadTransformador(null);
            }
            if (comboEstado.getSelectedT() != null) {
                entidadEnEdicion.setTisCondicionesInstalacionesElec(comboEstado.getSelectedT());
            }

            SgInmueblesSedes sed = restClient.guardarListaServiciosBasicos(entidadEnEdicion);
            inmuebleSedeBean.getEntidadEnEdicion().setTisVersion(sed.getTisVersion());
            inmuebleSedeBean.getEntidadEnEdicion().setTisTipoAbastecimiento(sed.getTisTipoAbastecimiento());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            cargarInmueblesServicioSanitario();
            inmuebleSedeBean.actualizar(entidadEnEdicion.getTisPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void checkboxSelect(SelectEvent selectEvent) {

        if (selectEvent.getObject() instanceof SgRelInmuebleServicioSanitario) {
            SgRelInmuebleServicioSanitario relImpSelect = (SgRelInmuebleServicioSanitario) selectEvent.getObject();
            habilitarEntradaTextoServicioSanitario[inmueblesServicioSanitario.indexOf(relImpSelect)] = Boolean.TRUE;
            relImpSelect.setRitBueno(null);
            relImpSelect.setRitMalo(null);
            relImpSelect.setRitRegular(null);
            relImpSelect.setRitNinos(null);
            relImpSelect.setRitNinas(null);
            relImpSelect.setRitAdministrativos(null);
            relImpSelect.setRitMaestros(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleAccesibilidad) {
            SgRelInmuebleAccesibilidad relImpSelect = (SgRelInmuebleAccesibilidad) selectEvent.getObject();
            habilitarEntradaTextoAccesibilidad[inmueblesAccesibilidad.indexOf(relImpSelect)] = Boolean.TRUE;
            relImpSelect.setIacBueno(null);
            relImpSelect.setIacMalo(null);
            relImpSelect.setIacRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleAbastecimientoAgua) {
            SgRelInmuebleAbastecimientoAgua relImpSelect = (SgRelInmuebleAbastecimientoAgua) selectEvent.getObject();
            habilitarEntradaTextoAbastecimientoAgua[inmueblesAbastecimientoAgua.indexOf(relImpSelect)] = Boolean.TRUE;
            relImpSelect.setIaaBueno(null);
            relImpSelect.setIaaMalo(null);
            relImpSelect.setIaaRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleAlmacenamientoAgua) {
            SgRelInmuebleAlmacenamientoAgua relImpSelect = (SgRelInmuebleAlmacenamientoAgua) selectEvent.getObject();
            habilitarEntradaTextoAlmacenamientoAgua[inmueblesAlmacenamientoAgua.indexOf(relImpSelect)] = Boolean.TRUE;
            relImpSelect.setIalBueno(null);
            relImpSelect.setIalMalo(null);
            relImpSelect.setIalRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleTipoDrenaje) {
            SgRelInmuebleTipoDrenaje relImpSelect = (SgRelInmuebleTipoDrenaje) selectEvent.getObject();
            habilitarEntradaTextoTipoDrenaje[inmueblesTipoDrenaje.indexOf(relImpSelect)] = Boolean.TRUE;
            relImpSelect.setItdBueno(null);
            relImpSelect.setItdMalo(null);
            relImpSelect.setItdRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleManejoDesechos) {
            SgRelInmuebleManejoDesechos relImpSelect = (SgRelInmuebleManejoDesechos) selectEvent.getObject();
            habilitarEntradaTextoManejoDesechos[inmueblesManejoDesechos.indexOf(relImpSelect)] = Boolean.TRUE;
            relImpSelect.setImdBueno(null);
            relImpSelect.setImdMalo(null);
            relImpSelect.setImdRegular(null);
        }
    }

    public void checkboxUnselect(UnselectEvent selectEvent) {
        if (selectEvent.getObject() instanceof SgRelInmuebleServicioSanitario) {
            SgRelInmuebleServicioSanitario relImpSelect = (SgRelInmuebleServicioSanitario) selectEvent.getObject();
            habilitarEntradaTextoServicioSanitario[inmueblesServicioSanitario.indexOf(relImpSelect)] = Boolean.FALSE;
            relImpSelect.setRitBueno(null);
            relImpSelect.setRitMalo(null);
            relImpSelect.setRitRegular(null);
            relImpSelect.setRitNinos(null);
            relImpSelect.setRitNinas(null);
            relImpSelect.setRitAdministrativos(null);
            relImpSelect.setRitMaestros(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleAccesibilidad) {
            SgRelInmuebleAccesibilidad relImpSelect = (SgRelInmuebleAccesibilidad) selectEvent.getObject();
            habilitarEntradaTextoAccesibilidad[inmueblesAccesibilidad.indexOf(relImpSelect)] = Boolean.FALSE;
            relImpSelect.setIacBueno(null);
            relImpSelect.setIacMalo(null);
            relImpSelect.setIacRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleAbastecimientoAgua) {
            SgRelInmuebleAbastecimientoAgua relImpSelect = (SgRelInmuebleAbastecimientoAgua) selectEvent.getObject();
            habilitarEntradaTextoAbastecimientoAgua[inmueblesAbastecimientoAgua.indexOf(relImpSelect)] = Boolean.FALSE;
            relImpSelect.setIaaBueno(null);
            relImpSelect.setIaaMalo(null);
            relImpSelect.setIaaRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleAlmacenamientoAgua) {
            SgRelInmuebleAlmacenamientoAgua relImpSelect = (SgRelInmuebleAlmacenamientoAgua) selectEvent.getObject();
            habilitarEntradaTextoAlmacenamientoAgua[inmueblesAlmacenamientoAgua.indexOf(relImpSelect)] = Boolean.FALSE;
            relImpSelect.setIalBueno(null);
            relImpSelect.setIalMalo(null);
            relImpSelect.setIalRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleTipoDrenaje) {
            SgRelInmuebleTipoDrenaje relImpSelect = (SgRelInmuebleTipoDrenaje) selectEvent.getObject();
            habilitarEntradaTextoTipoDrenaje[inmueblesTipoDrenaje.indexOf(relImpSelect)] = Boolean.FALSE;
            relImpSelect.setItdBueno(null);
            relImpSelect.setItdMalo(null);
            relImpSelect.setItdRegular(null);
        }

        if (selectEvent.getObject() instanceof SgRelInmuebleManejoDesechos) {
            SgRelInmuebleManejoDesechos relImpSelect = (SgRelInmuebleManejoDesechos) selectEvent.getObject();
            habilitarEntradaTextoManejoDesechos[inmueblesManejoDesechos.indexOf(relImpSelect)] = Boolean.FALSE;
            relImpSelect.setImdBueno(null);
            relImpSelect.setImdMalo(null);
            relImpSelect.setImdRegular(null);
        }
    }

    public void toggleSelect(ToggleSelectEvent selectEvent) {
        String idComponent = selectEvent.getComponent().getId();

        switch (idComponent) {
            case "listaServicioSanitario":
                if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTextoServicioSanitario, Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTextoServicioSanitario, Boolean.FALSE);
                    inmueblesServicioSanitario.stream().forEach(c -> {
                        c.setRitBueno(null);
                        c.setRitMalo(null);
                        c.setRitRegular(null);
                        c.setRitNinos(null);
                        c.setRitNinas(null);
                        c.setRitAdministrativos(null);
                        c.setRitMaestros(null);
                    });
                }
            case "listaAccesibilidad":
                if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTextoAccesibilidad, Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTextoAccesibilidad, Boolean.FALSE);
                    inmueblesAccesibilidad.stream().forEach(c -> {
                        c.setIacBueno(null);
                        c.setIacMalo(null);
                        c.setIacRegular(null);
                    });
                }
            case "listaabastecimiento_agua":
                if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTextoAbastecimientoAgua, Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTextoAbastecimientoAgua, Boolean.FALSE);
                    inmueblesAbastecimientoAgua.stream().forEach(c -> {
                        c.setIaaBueno(null);
                        c.setIaaMalo(null);
                        c.setIaaRegular(null);
                    });
                }
            case "listaalmacenamiento_agua":
                if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTextoAlmacenamientoAgua, Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTextoAlmacenamientoAgua, Boolean.FALSE);
                    inmueblesAlmacenamientoAgua.stream().forEach(c -> {
                        c.setIalBueno(null);
                        c.setIalMalo(null);
                        c.setIalRegular(null);
                    });
                } 
            case "listaAguasNegras":
                if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTextoTipoDrenaje, Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTextoTipoDrenaje, Boolean.FALSE);
                    inmueblesTipoDrenaje.stream().forEach(c -> {
                        c.setItdBueno(null);
                        c.setItdMalo(null);
                        c.setItdRegular(null);
                    });
                }  
            case "desechosSolidos":
                if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTextoManejoDesechos, Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTextoManejoDesechos, Boolean.FALSE);
                    inmueblesManejoDesechos.stream().forEach(c -> {
                        c.setImdBueno(null);
                        c.setImdMalo(null);
                        c.setImdRegular(null);
                    });
                }     
                
            default:
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

    public Boolean getBeanIniciado() {
        return beanIniciado;
    }

    public void setBeanIniciado(Boolean beanIniciado) {
        this.beanIniciado = beanIniciado;
    }

    public List<SgRelInmuebleServicioSanitario> getInmueblesServicioSanitario() {
        return inmueblesServicioSanitario;
    }

    public void setInmueblesServicioSanitario(List<SgRelInmuebleServicioSanitario> inmueblesServicioSanitario) {
        this.inmueblesServicioSanitario = inmueblesServicioSanitario;
    }

    public List<SgRelInmuebleServicioSanitario> getInmueblesServicioSanitarioSeleccionado() {
        return inmueblesServicioSanitarioSeleccionado;
    }

    public void setInmueblesServicioSanitarioSeleccionado(List<SgRelInmuebleServicioSanitario> inmueblesServicioSanitarioSeleccionado) {
        this.inmueblesServicioSanitarioSeleccionado = inmueblesServicioSanitarioSeleccionado;
    }

    public Boolean[] getHabilitarEntradaTextoServicioSanitario() {
        return habilitarEntradaTextoServicioSanitario;
    }

    public void setHabilitarEntradaTextoServicioSanitario(Boolean[] habilitarEntradaTextoServicioSanitario) {
        this.habilitarEntradaTextoServicioSanitario = habilitarEntradaTextoServicioSanitario;
    }

    public InmuebleSedesBean getInmuebleSedeBean() {
        return inmuebleSedeBean;
    }

    public void setInmuebleSedeBean(InmuebleSedesBean inmuebleSedeBean) {
        this.inmuebleSedeBean = inmuebleSedeBean;
    }

    public List<SgRelInmuebleAccesibilidad> getInmueblesAccesibilidad() {
        return inmueblesAccesibilidad;
    }

    public void setInmueblesAccesibilidad(List<SgRelInmuebleAccesibilidad> inmueblesAccesibilidad) {
        this.inmueblesAccesibilidad = inmueblesAccesibilidad;
    }

    public List<SgRelInmuebleAccesibilidad> getInmueblesAccesibilidadSeleccionados() {
        return inmueblesAccesibilidadSeleccionados;
    }

    public void setInmueblesAccesibilidadSeleccionados(List<SgRelInmuebleAccesibilidad> inmueblesAccesibilidadSeleccionados) {
        this.inmueblesAccesibilidadSeleccionados = inmueblesAccesibilidadSeleccionados;
    }

    public List<SgRelInmuebleAbastecimientoAgua> getInmueblesAbastecimientoAgua() {
        return inmueblesAbastecimientoAgua;
    }

    public void setInmueblesAbastecimientoAgua(List<SgRelInmuebleAbastecimientoAgua> inmueblesAbastecimientoAgua) {
        this.inmueblesAbastecimientoAgua = inmueblesAbastecimientoAgua;
    }

    public List<SgRelInmuebleAbastecimientoAgua> getInmueblesAbastecimientoAguaSeleccionados() {
        return inmueblesAbastecimientoAguaSeleccionados;
    }

    public void setInmueblesAbastecimientoAguaSeleccionados(List<SgRelInmuebleAbastecimientoAgua> inmueblesAbastecimientoAguaSeleccionados) {
        this.inmueblesAbastecimientoAguaSeleccionados = inmueblesAbastecimientoAguaSeleccionados;
    }

    public List<SgRelInmuebleAlmacenamientoAgua> getInmueblesAlmacenamientoAgua() {
        return inmueblesAlmacenamientoAgua;
    }

    public void setInmueblesAlmacenamientoAgua(List<SgRelInmuebleAlmacenamientoAgua> inmueblesAlmacenamientoAgua) {
        this.inmueblesAlmacenamientoAgua = inmueblesAlmacenamientoAgua;
    }

    public List<SgRelInmuebleAlmacenamientoAgua> getInmueblesAlmacenamientoAguaSeleccionados() {
        return inmueblesAlmacenamientoAguaSeleccionados;
    }

    public void setInmueblesAlmacenamientoAguaSeleccionados(List<SgRelInmuebleAlmacenamientoAgua> inmueblesAlmacenamientoAguaSeleccionados) {
        this.inmueblesAlmacenamientoAguaSeleccionados = inmueblesAlmacenamientoAguaSeleccionados;
    }

    public List<SgRelInmuebleTipoDrenaje> getInmueblesTipoDrenaje() {
        return inmueblesTipoDrenaje;
    }

    public void setInmueblesTipoDrenaje(List<SgRelInmuebleTipoDrenaje> inmueblesTipoDrenaje) {
        this.inmueblesTipoDrenaje = inmueblesTipoDrenaje;
    }

    public List<SgRelInmuebleTipoDrenaje> getInmueblesTipoDrenajeSeleccionados() {
        return inmueblesTipoDrenajeSeleccionados;
    }

    public void setInmueblesTipoDrenajeSeleccionados(List<SgRelInmuebleTipoDrenaje> inmueblesTipoDrenajeSeleccionados) {
        this.inmueblesTipoDrenajeSeleccionados = inmueblesTipoDrenajeSeleccionados;
    }

    public List<SgRelInmuebleManejoDesechos> getInmueblesManejoDesechos() {
        return inmueblesManejoDesechos;
    }

    public void setInmueblesManejoDesechos(List<SgRelInmuebleManejoDesechos> inmueblesManejoDesechos) {
        this.inmueblesManejoDesechos = inmueblesManejoDesechos;
    }

    public List<SgRelInmuebleManejoDesechos> getInmueblesManejoDesechosSeleccionados() {
        return inmueblesManejoDesechosSeleccionados;
    }

    public void setInmueblesManejoDesechosSeleccionados(List<SgRelInmuebleManejoDesechos> inmueblesManejoDesechosSeleccionados) {
        this.inmueblesManejoDesechosSeleccionados = inmueblesManejoDesechosSeleccionados;
    }

    public Boolean[] getHabilitarEntradaTextoAccesibilidad() {
        return habilitarEntradaTextoAccesibilidad;
    }

    public void setHabilitarEntradaTextoAccesibilidad(Boolean[] habilitarEntradaTextoAccesibilidad) {
        this.habilitarEntradaTextoAccesibilidad = habilitarEntradaTextoAccesibilidad;
    }

    public Boolean[] getHabilitarEntradaTextoAbastecimientoAgua() {
        return habilitarEntradaTextoAbastecimientoAgua;
    }

    public void setHabilitarEntradaTextoAbastecimientoAgua(Boolean[] habilitarEntradaTextoAbastecimientoAgua) {
        this.habilitarEntradaTextoAbastecimientoAgua = habilitarEntradaTextoAbastecimientoAgua;
    }

    public Boolean[] getHabilitarEntradaTextoAlmacenamientoAgua() {
        return habilitarEntradaTextoAlmacenamientoAgua;
    }

    public void setHabilitarEntradaTextoAlmacenamientoAgua(Boolean[] habilitarEntradaTextoAlmacenamientoAgua) {
        this.habilitarEntradaTextoAlmacenamientoAgua = habilitarEntradaTextoAlmacenamientoAgua;
    }

    public Boolean[] getHabilitarEntradaTextoTipoDrenaje() {
        return habilitarEntradaTextoTipoDrenaje;
    }

    public void setHabilitarEntradaTextoTipoDrenaje(Boolean[] habilitarEntradaTextoTipoDrenaje) {
        this.habilitarEntradaTextoTipoDrenaje = habilitarEntradaTextoTipoDrenaje;
    }

    public Boolean[] getHabilitarEntradaTextoManejoDesechos() {
        return habilitarEntradaTextoManejoDesechos;
    }

    public void setHabilitarEntradaTextoManejoDesechos(Boolean[] habilitarEntradaTextoManejoDesechos) {
        this.habilitarEntradaTextoManejoDesechos = habilitarEntradaTextoManejoDesechos;
    }

    public SofisComboG<EnumEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

}
