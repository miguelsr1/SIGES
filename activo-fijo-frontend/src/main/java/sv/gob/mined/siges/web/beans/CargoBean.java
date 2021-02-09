/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfClasificacionBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFormaAdquisicion;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.centroseducativos.SgPersona;
import sv.gob.mined.siges.web.enumerados.EnumFuenteFinanciamiento;
import sv.gob.mined.siges.web.enumerados.EnumSeccionesCargoBienes;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessError;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoBienesRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.OperacionesGenerales;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class CargoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargoBean.class.getName());
    
    @Inject
    @Param(name = "id")
    private Long estId;

    @Inject
    @Param(name = "rev")
    private Long estRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    private ApplicationBean applicationBean;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private TipoBienesRestClient tipoBienesRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private PersonaRestClient personaRestClient;
    
    private SgAfBienDepreciable entidadEnEdicion = new SgAfBienDepreciable();
    
    private String urlReporte;

    private String codigoCorrelativoMaxVal = "999";
    private List<SgAfEmpleados> empleados = new ArrayList();
    private SofisComboG<SgAfEstadosCalidad> comboEstadosCalidad;
    private SofisComboG<SgAfFormaAdquisicion> comboFormaAdquisicion;
    private SofisComboG<SgAfProyectos> comboProyectos;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgMunicipio> comboMunicipios;

    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo;
    private SofisComboG<TipoUnidad> comboTiposUnidad;
    
    private TipoUnidad tipoUnidadSeleccionada;
    private SgAfTipoBienes tipoBienSeleccionado;
    private SgAfUnidadesAdministrativas unidadSeleccionada;
    private SgSede sedeSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFSeleccionada;
    private SgAfEmpleados empleadoSeleccionado;
    private SgPersona personaSeleccionada;
    private SgAfEmpleados empleadoSeleccionadoTemp;
    private SgAfFuenteFinanciamiento fuenteSeleccionada;
    
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativas = new ArrayList();
    private List<SgSede> sedes = new ArrayList();
    
    private SofisComboG<SgAfClasificacionBienes> comboClasificacionBienes;
    private SofisComboG<SgAfCategoriaBienes> comboCategoriaBienes;
    
    private SgAfTipoBienes tipoBienOriginal;
    private SgSede sedeOriginal;
    private SgAfUnidadesAdministrativas unidadOriginal;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean renderLote = Boolean.FALSE;
    private Boolean renderSeccionCategoria = Boolean.FALSE;
    private Boolean renderCantidadLote = Boolean.FALSE;
    private Boolean procesarLote = Boolean.FALSE;
    private Boolean editarCorrelativo = Boolean.FALSE;
    private Boolean editarTipoBien = Boolean.FALSE;
    private Boolean editarProyecto = Boolean.FALSE;
    private Boolean editarFuenteFinanciamiento = Boolean.FALSE;
    
    private Boolean editarEmpleado = Boolean.FALSE;
    private Boolean editarDatosAdquisicion = Boolean.FALSE;
    private FiltroCodiguera filtro;
    
    private Boolean renderMunicipio = Boolean.FALSE;
    private Boolean esUnidadAdministrativa = Boolean.FALSE;
    private Boolean inicializarUnidadAdministrativa = Boolean.FALSE;
    private Boolean renderSeccionBienesMuebles = Boolean.TRUE;
    private Boolean renderSeccionVehiculos = Boolean.FALSE;
    private Boolean renderFichaInmueble = Boolean.FALSE;
    
    private List<SgAfFormaAdquisicion> listaFormaAdquisicion;
    private List<SgAfProyectos> listaProyectos;
    private List<SgAfFuenteFinanciamiento> listaFuentes;
    private List<SgAfEstadosCalidad> listaEstadosCalidad;
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF = new ArrayList();
    private List<SgMunicipio> listaMunicipios;
    private SgAfEstadosBienes estadoExistente;
    private Boolean cargarDefecto= Boolean.FALSE;
    
    private String duiPersona;
    private String nombrePersona;
    List<SgAfEmpleados>  listaEmpleados = new ArrayList();
    
    private Boolean renderEmpleados = Boolean.FALSE;
    
    public CargoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    this.soloLectura = Boolean.TRUE;
                    this.actualizar(bienesRestClient.obtenerEnRevision(estId, estRev)); 
                } else {
                    soloLectura = editable != null ? !editable : soloLectura;
                    this.actualizar(bienesRestClient.obtenerPorId(estId, Boolean.TRUE)); 
                }
            } else {
                cargarEntidad();
                this.editarDatosAdquisicion = Boolean.TRUE;
                this.editarTipoBien = Boolean.TRUE;
                this.editarProyecto = Boolean.TRUE;
                this.editarFuenteFinanciamiento = Boolean.TRUE;
                this.renderLote = Boolean.TRUE;
                this.editarCorrelativo = Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
            
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CARGO_BIENES)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void actualizar(SgAfBienDepreciable est) {
        try {
            if (est == null) {
                JSFUtils.redirectToIndex();
            } else {
                 JSFUtils.limpiarMensajesError();
                entidadEnEdicion = est;
                if(entidadEnEdicion != null) {
                    this.cargarDefecto = Boolean.TRUE;
                    inicializarUnidadAdministrativa = Boolean.TRUE;
                    verificarActualizarDatosAdquisicion();
                    if(entidadEnEdicion.getBdeEstadosBienes() == null || estadoExistente == null 
                                  || !entidadEnEdicion.getBdeEstadosBienes().getEbiCodigo().equals(estadoExistente.getEbiCodigo())) {
                        this.soloLectura = Boolean.TRUE;
                    }
                    if((entidadEnEdicion.getBdeCompletado() != null && !entidadEnEdicion.getBdeCompletado()) && (entidadEnEdicion.getBdeEsLoteSiap() != null && entidadEnEdicion.getBdeEsLoteSiap())) {
                        this.renderLote = Boolean.TRUE;
                        entidadEnEdicion.setBdeEsLote(Boolean.TRUE);
                    }
                    
                    
                    tipoBienSeleccionado = entidadEnEdicion.getBdeTipoBien();
                    tipoBienOriginal = entidadEnEdicion.getBdeTipoBien();

                    comboEstadosCalidad.setSelectedT(entidadEnEdicion.getBdeEstadoCalidad());
                    comboFormaAdquisicion.setSelectedT(entidadEnEdicion.getBdeFormaAdquisicion());
                    comboFuenteFinanciamiento.setSelectedT(entidadEnEdicion.getBdeFuenteFinanciamiento());
                    
                    if(entidadEnEdicion.getBdeTipoBienCategoriaVinculada() != null && !entidadEnEdicion.getBdeTipoBienCategoriaVinculada()) {
                        comboClasificacionBienes.setSelectedT(entidadEnEdicion.getBdeCategoriaFk() != null ? entidadEnEdicion.getBdeCategoriaFk().getCabClasificacionBienes() : null);
                    } else {
                        entidadEnEdicion.setBdeCategoriaFk(null);
                    }
                    seleccionarClasificacion();
                    
                    sedeOriginal = entidadEnEdicion.getBdeSede();
                    unidadOriginal = entidadEnEdicion.getBdeUnidadesAdministrativas();
                    
                    sedeSeleccionada = entidadEnEdicion.getBdeSede();
                    unidadSeleccionada = entidadEnEdicion.getBdeUnidadesAdministrativas();
                    
                    fuenteSeleccionada = entidadEnEdicion.getBdeFuenteFinanciamiento();
                    if(fuenteSeleccionada != null && fuenteSeleccionada.getFfiRequiereProyecto() != null && fuenteSeleccionada.getFfiRequiereProyecto()) {
                        comboProyectos = new SofisComboG(listaProyectos, "proNombre");
                        comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        
                        comboProyectos.setSelectedT(entidadEnEdicion.getBdeProyectos());
                    } else {
                        entidadEnEdicion.setBdeProyectos(null);
                    }
                    if(unidadSeleccionada != null) {
                        esUnidadAdministrativa = Boolean.TRUE;
                        tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
                        unidadAFSeleccionada = entidadEnEdicion.getBdeUnidadesAdministrativas().getUadUnidadActivoFijoFk();
                        comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
                    } else if(sedeSeleccionada!= null) {
                        tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
                        comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
                        if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                            for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                                if(unidadesAF.getUafDepartamento().getDepPk().toString().equals(sedeSeleccionada.getSedDireccion().getDirDepartamento().getDepPk().toString())
                                        && !unidadesAF.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                    unidadAFSeleccionada = unidadesAF;
                                }
                            }
                        } 
                        
                    }
                    
                    comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
                    
                    seleccionarUnidadActivoFijo();
                    seleccionarUnidadAdministrativa();
                    empleadoSeleccionado = entidadEnEdicion.getBdeEmpleadoFk();
                    if(empleadoSeleccionado != null) {
                        entidadEnEdicion.setBdeAsignadoA("");
                        this.editarEmpleado = Boolean.TRUE;
                    } else {
                        this.editarEmpleado = Boolean.FALSE;
                    }

                    inicializarUnidadAdministrativa = Boolean.FALSE;
                    this.cargarDefecto = Boolean.FALSE;
                    if(tipoBienSeleccionado.getTbiCategoriaBienes() != null) {
                        entidadEnEdicion.setBdeVidaUtil(tipoBienSeleccionado.getTbiCategoriaBienes().getCabVidaUtil());
                        this.renderSeccionBienesMuebles = Boolean.FALSE;
                        this.renderSeccionVehiculos = Boolean.FALSE;
                        if(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList() != null) {
                            if(OperacionesGenerales.existeEnArray(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(),EnumSeccionesCargoBienes.SECCION_BIENES_MUEBLES)) {
                                this.renderSeccionBienesMuebles = Boolean.TRUE;
                            } else if(OperacionesGenerales.existeEnArray(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(),EnumSeccionesCargoBienes.SECCION_VEHICULOS)) {
                                this.renderSeccionVehiculos = Boolean.TRUE;
                            } else if(OperacionesGenerales.existeEnArray(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(),EnumSeccionesCargoBienes.SECCION_ID_EXTERNO)) {
                                this.renderFichaInmueble = Boolean.TRUE;
                                this.soloLectura = Boolean.TRUE;
                                this.urlReporte = applicationBean.getInfraestructuraFrontendUrl() + tipoBienSeleccionado.getTbiCategoriaBienes().getCabPathCargar() + "?id=" + entidadEnEdicion.getBdeInmuebleId() + "&edit=" + !soloLectura;
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void cargarEntidad() {
        try {
            sedeSeleccionada = null;
            unidadSeleccionada = null;
            cargarSedePorDefecto();
            cargarUADPorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            this.inicializarUnidadAdministrativa = Boolean.TRUE;
            this.esUnidadAdministrativa = Boolean.FALSE;
            this.cargarDefecto = Boolean.TRUE;
            sedeSeleccionada = sessionBean.getSedeDefecto();
            entidadEnEdicion.setBdeSede(sedeSeleccionada);

            tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            
            comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
           
            if(sedeSeleccionada!= null) {
                if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                    for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                        if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            unidadAFSeleccionada = unidadesAF;
                        }
                        
                        if(unidadesAF.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            listaUnidadesAF.remove(unidadesAF);//Quitamos de la lista la unidad central.
                        }
                    }
                } 

            }

            comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
                    
            seleccionarUnidadActivoFijo();
        }
    }
    public void cargarUADPorDefecto() throws Exception {
        if (sessionBean.getUnidadADDefecto() != null) {
            this.inicializarUnidadAdministrativa = Boolean.TRUE;
            this.esUnidadAdministrativa = Boolean.TRUE;
            this.cargarDefecto = Boolean.TRUE;
            this.editarEmpleado = Boolean.TRUE;
            
            unidadSeleccionada = sessionBean.getUnidadADDefecto();
            entidadEnEdicion.setBdeUnidadesAdministrativas(unidadSeleccionada);
            
            tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
     
            comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
            
            if(unidadSeleccionada != null) {
                unidadAFSeleccionada = unidadSeleccionada.getUadUnidadActivoFijoFk();
            } 

            comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
            
            seleccionarUnidadActivoFijo();
        }
    }
    
    public void verificarActualizarDatosAdquisicion() {
        this.editarDatosAdquisicion = Boolean.FALSE;
        this.editarFuenteFinanciamiento = Boolean.FALSE;
        this.editarProyecto = Boolean.FALSE;
        this.editarTipoBien = Boolean.FALSE;
        this.editarCorrelativo = Boolean.FALSE;
        if(entidadEnEdicion != null && entidadEnEdicion.getBdePk() != null) {
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_CORRELATIVO)) {
                this.editarCorrelativo = Boolean.TRUE;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_CORRELATIVO_VALOR_ADQUISICION_AF)) {
                if(entidadEnEdicion.getBdeValorAdquisicion().compareTo(applicationBean.getValorActivoFijoLimite()) < 0) {
                    this.editarCorrelativo = Boolean.TRUE;
                }
            }
            
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DATOS_ADQUISICION)) {
                this.editarDatosAdquisicion = Boolean.TRUE;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DATOS_ADQUISICION_MENOR_AF)) {
                if(entidadEnEdicion.getBdeValorAdquisicion().compareTo(applicationBean.getValorActivoFijoLimite()) < 0) {
                    this.editarDatosAdquisicion = Boolean.TRUE;
                }
            }
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.MODIFICAR_FUENTE_FINANCIAMIENTO)) {
                this.editarFuenteFinanciamiento= Boolean.TRUE;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.MODIFICAR_FUENTE_FINANCIAMIENTO_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                if(entidadEnEdicion.getBdeValorAdquisicion().compareTo(applicationBean.getValorActivoFijoLimite()) < 0) {
                    this.editarFuenteFinanciamiento = Boolean.TRUE;
                }
            }

            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.MODIFICAR_PROYECTO)) {
                this.editarProyecto = Boolean.TRUE;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.MODIFICAR_PROYECTO_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                if(entidadEnEdicion.getBdeValorAdquisicion().compareTo(applicationBean.getValorActivoFijoLimite()) < 0) {
                    this.editarProyecto = Boolean.TRUE;
                }
            }

            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.MODIFICAR_TIPO_BIEN)) {
                this.editarTipoBien = Boolean.TRUE;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.MODIFICAR_TIPO_BIEN_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                if(entidadEnEdicion.getBdeValorAdquisicion().compareTo(applicationBean.getValorActivoFijoLimite()) < 0) {
                    this.editarTipoBien = Boolean.TRUE;
                }
            }
        }
        
    }
    
    
    public void cargarCombos() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            if(sessionBean.getUnidadADDefecto() != null) {
                tipoUnidad.add(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            } else if(sessionBean.getSedeDefecto() != null) {
                tipoUnidad.add(TipoUnidad.CENTRO_ESCOLAR);
                comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            } else {
                tipoUnidad = new ArrayList(Arrays.asList(TipoUnidad.values()));
                comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
                comboTiposUnidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            comboTiposUnidad.ordenar();
            
            
            FiltroUnidadesActivoFijo fil = new FiltroUnidadesActivoFijo();
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"uafCodigo"});
            fil.setIncluirCampos(new String[]{"uafCodigo", "uafNombre", "uafVersion", "uafDepartamento.depPk"});
            fil.setAscending(new boolean[]{true});
            
            
            listaUnidadesAF = unidadesActivoFijoRestClient.buscar(fil);
           
            comboUnidadesActivoFijo = new SofisComboG(listaUnidadesAF, "codigoNombre");
            comboUnidadesActivoFijo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"ecaCodigo"});
            filtro.setIncluirCampos(new String[]{"ecaNombre","ecaCodigo", "ecaVersion"});
            listaEstadosCalidad = catalogosRestClient.buscarEstadosCalidad(filtro);
            comboEstadosCalidad = new SofisComboG(listaEstadosCalidad, "ecaNombre");
            comboEstadosCalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if(listaEstadosCalidad != null && !listaEstadosCalidad.isEmpty()) {
                for(SgAfEstadosCalidad calidad : listaEstadosCalidad) {
                    if(BigDecimal.ONE.toString().equals(calidad.getEcaCodigo())) {
                        comboEstadosCalidad.setSelectedT(calidad);
                    }
                }
            }
            
            FiltroFuenteFinanciamientoAF fuentes = new FiltroFuenteFinanciamientoAF();
            fuentes.setHabilitado(Boolean.TRUE);
            fuentes.setOrderBy(new String[]{"ffiNombre"});
            fuentes.setIncluirCampos(new String[]{"ffiNombre","ffiAplicaPara","ffiRequiereProyecto", "ffiVersion"});
            List<SgAfFuenteFinanciamiento> listaFuentes = catalogosRestClient.buscarFuenteFinanciamiento(fuentes);
            
            List<SgAfFuenteFinanciamiento> fuentesMostrar = new ArrayList();
            EnumFuenteFinanciamiento fuentePara = null;
            
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.FUENTE_MINED)) {
                fuentePara = EnumFuenteFinanciamiento.MINED;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.FUENTE_DEPARTAMENTAL)) {
                fuentePara = EnumFuenteFinanciamiento.DEPARTAMENTAL; 
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.FUENTE_CENTRO_EDUCATIVO)) {
                fuentePara = EnumFuenteFinanciamiento.CENTRO_EDUCATIVO;
            }
            
            if(listaFuentes != null && !listaFuentes.isEmpty()) {
                for(SgAfFuenteFinanciamiento fuente : listaFuentes) { 
                    if(OperacionesGenerales.existeEnArray(fuente.getFfiAplicaPara(), fuentePara.getText())) {
                        fuentesMostrar.add(fuente);
                    }
                } 
            }           
            
            comboFuenteFinanciamiento = new SofisComboG(fuentesMostrar, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroProyecto fpro = new FiltroProyecto();
            fpro.setHabilitado(Boolean.TRUE);
            fpro.setLiquidado(Boolean.FALSE);
            fpro.setAscending(new boolean[]{true});
            fpro.setOrderBy(new String[]{"proNombre"});
            fpro.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            listaProyectos = catalogosRestClient.buscarProyecto(fpro);
            
            
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"fadNombre"});
            filtro.setIncluirCampos(new String[]{"fadNombre", "fadVersion"});
            listaFormaAdquisicion = catalogosRestClient.buscarFormaAdquisicion(filtro);
            comboFormaAdquisicion = new SofisComboG(listaFormaAdquisicion, "fadNombre");
            comboFormaAdquisicion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo", "ebiVersion"});
            filtro.setOrderBy(new String[]{"ebiNombre"});
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setCodigo(Constantes.CODIGO_ESTADO_EXISTENTE);
            List<SgAfEstadosBienes> estadoBien = catalogosRestClient.buscarEstadosBienes(filtro);
            if(estadoBien != null && !estadoBien.isEmpty()) {
                estadoExistente = estadoBien.get(0);
            }
            
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setIncluirCampos(new String[]{"cbiCodigo","cbiNombre", "cbiPk"});
            filtro.setOrderBy(new String[]{"cbiNombre"});
            List<SgAfClasificacionBienes> clasificaciones = catalogosRestClient.buscarClasificacionBienes(filtro);
            comboClasificacionBienes = new SofisComboG(clasificaciones, "cbiNombre");
            comboClasificacionBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboCategoriaBienes = new SofisComboG();
            comboCategoriaBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarFuente() { 
        fuenteSeleccionada = comboFuenteFinanciamiento.getSelectedT();
        if(fuenteSeleccionada != null && fuenteSeleccionada.getFfiRequiereProyecto() != null && fuenteSeleccionada.getFfiRequiereProyecto()) {
            comboProyectos = new SofisComboG(listaProyectos, "proNombre");
            comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } else {
            entidadEnEdicion.setBdeProyectos(null);
            comboProyectos = new SofisComboG();
        }    
    }
    
    public List<SgAfTipoBienes> completeTipoBien(String query) {
        try {
            FiltroTipoBienes filtro = new FiltroTipoBienes();
            filtro.setHabilitado(Boolean.TRUE); 
            filtro.setCodigoONombre(query);
            filtro.setMaxResults(11L);
            filtro.setOrderBy(new String[]{"tbiNombre"});
            filtro.setAscending(new boolean[]{false});
            filtro.setIncluirCampos(new String[]{"tbiPk","tbiCodigo", "tbiNombre","tbiVersion"});
            return tipoBienesRestClient.buscar(filtro);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void completeCodigoInventario() {
        String codigoCorrelativo = StringUtils.EMPTY;
        Integer codigoSize = 0;
        FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
        codigoCorrelativoMaxVal = "";
        
        if(esUnidadAdministrativa != null) {
            if(esUnidadAdministrativa) {
                if(unidadSeleccionada != null) {
                    codigoSize = applicationBean.getCodigoCorrelativoUnidadAdministrativaSize();
                }
            } else if(sedeSeleccionada != null) {
                codigoSize = applicationBean.getCodigoCorrelativoCentroEducativoSize();
            }
        }
        Integer correlativo  = 0;
        codigoCorrelativoMaxVal = StringUtils.leftPad("", codigoSize, "9");
        if(entidadEnEdicion.getBdeCodigoCorrelativo() != null && StringUtils.isNotBlank(entidadEnEdicion.getBdeCodigoCorrelativo())) {
            correlativo =   Integer.parseInt(entidadEnEdicion.getBdeCodigoCorrelativo());
            if(correlativo.compareTo(0)==0) {
                List<BusinessError> errores = new ArrayList();
                errores.add(new BusinessError(Mensajes.ERROR_CORRELATIVO_CERO));
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                return;
            }
        } else {
            if(tipoBienSeleccionado != null && (unidadSeleccionada != null || sedeSeleccionada != null) ) {
                if(esUnidadAdministrativa) {
                    entidadEnEdicion.setBdeCodigoInventario(unidadSeleccionada.getUadCodigo() + "-" + tipoBienSeleccionado.getTbiCodigo() + "-" );
                } else {
                    entidadEnEdicion.setBdeCodigoInventario(sedeSeleccionada.getSedCodigo() + "-" +tipoBienSeleccionado.getTbiCodigo() + "-");
                } 
            }
            return;
        }
        
        if(correlativo != null) {
            if(correlativo.compareTo(Integer.valueOf(StringUtils.leftPad("", codigoSize, "9"))) > 0) {
                codigoCorrelativoMaxVal = StringUtils.leftPad("", correlativo.toString().length(), "9");
            }
        }
        codigoCorrelativo = StringUtils.leftPad(correlativo.toString(),codigoCorrelativoMaxVal.length(), "0");
        
        if(tipoBienSeleccionado != null && (unidadSeleccionada != null || sedeSeleccionada != null) && StringUtils.isNotBlank(entidadEnEdicion.getBdeCodigoCorrelativo())) {
            if(esUnidadAdministrativa) {
                entidadEnEdicion.setBdeCodigoInventario(unidadSeleccionada.getUadCodigo() + "-" + tipoBienSeleccionado.getTbiCodigo() + "-" + codigoCorrelativo);
            } else {
                entidadEnEdicion.setBdeCodigoInventario(sedeSeleccionada.getSedCodigo() + "-" +tipoBienSeleccionado.getTbiCodigo() + "-" + codigoCorrelativo);
            } 
        }
    }
    
    public void completeCodigoInventarioMaximoLote() {
        Integer cantLote = entidadEnEdicion.getBdeCantidadLote();
        String codigoCorrelativo = StringUtils.EMPTY;
        Integer codigoSize = 0;
        FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
        codigoCorrelativoMaxVal = "";
        
        if(esUnidadAdministrativa != null) {
            if(esUnidadAdministrativa) {
                if(unidadSeleccionada != null) {
                    codigoSize = applicationBean.getCodigoCorrelativoUnidadAdministrativaSize();
                }
            } else if(sedeSeleccionada != null) {
                codigoSize = applicationBean.getCodigoCorrelativoCentroEducativoSize();
            }
        }
        Integer correlativo  = 0;
        codigoCorrelativoMaxVal = StringUtils.leftPad("", codigoSize, "9");
        if(entidadEnEdicion.getBdeCodigoCorrelativo() != null && StringUtils.isNotBlank(entidadEnEdicion.getBdeCodigoCorrelativo())) {
            correlativo =   Integer.parseInt(entidadEnEdicion.getBdeCodigoCorrelativo());
            if(correlativo.compareTo(0)==0) {
                List<BusinessError> errores = new ArrayList();
                errores.add(new BusinessError(Mensajes.ERROR_CORRELATIVO_CERO));
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                return;
            }
        } else {
            if(tipoBienSeleccionado != null && (unidadSeleccionada != null || sedeSeleccionada != null) ) {
                if(esUnidadAdministrativa) {
                    entidadEnEdicion.setBdeCodigoInventario(unidadSeleccionada.getUadCodigo() + "-" + tipoBienSeleccionado.getTbiCodigo() + "-" );
                } else {
                    entidadEnEdicion.setBdeCodigoInventario(sedeSeleccionada.getSedCodigo() + "-" +tipoBienSeleccionado.getTbiCodigo() + "-");
                } 
            }
            return;
        }
        
        if(correlativo != null) {
            if(correlativo.compareTo(Integer.valueOf(StringUtils.leftPad("", codigoSize, "9"))) > 0) {
                codigoCorrelativoMaxVal = StringUtils.leftPad("", correlativo.toString().length(), "9");
            }
        }
        if(cantLote == null || cantLote.compareTo(0) == 0) {
            cantLote = 1;
        }
        correlativo = correlativo + (cantLote - 1);
        codigoCorrelativo = StringUtils.leftPad(correlativo.toString(),codigoCorrelativoMaxVal.length(), "0");
       // codigoCorrelativo = codigoCorrelativo + cantLote;
        if(tipoBienSeleccionado != null && (unidadSeleccionada != null || sedeSeleccionada != null) && StringUtils.isNotBlank(entidadEnEdicion.getBdeCodigoCorrelativo())) {
            if(esUnidadAdministrativa) {
                entidadEnEdicion.setBdeCodigoInventarioGenerar(unidadSeleccionada.getUadCodigo() + "-" + tipoBienSeleccionado.getTbiCodigo() + "-" + codigoCorrelativo);
            } else {
                entidadEnEdicion.setBdeCodigoInventarioGenerar(sedeSeleccionada.getSedCodigo() + "-" +tipoBienSeleccionado.getTbiCodigo() + "-" + codigoCorrelativo);
            } 
        }
        entidadEnEdicion.setBdeCodigoCorrelativoGenerar(codigoCorrelativo);
        entidadEnEdicion.setBdeNumCorrelativoGenerar(correlativo);
    }
    
    
    public void seleccionarTipoUnidadAF() {
        empleados = new ArrayList();
        esUnidadAdministrativa = null;
        //tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
        //unidadSeleccionada = null;
        //sedeSeleccionada = null;
        empleadoSeleccionado = null;
        if (comboTiposUnidad.getSelectedT() != null) {
            tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
            switch (comboTiposUnidad.getSelectedT()) {
                case UNIDAD_ADMINISTRATIVA:
                    esUnidadAdministrativa = Boolean.TRUE;
                    this.editarEmpleado = Boolean.TRUE;
                    break;
                case CENTRO_ESCOLAR:
                    esUnidadAdministrativa = Boolean.FALSE;
                    this.editarEmpleado = Boolean.FALSE;
                    break;
                default:
                    break;
            }
        }
        seleccionarUnidadActivoFijo();
    }
    
    public void seleccionarUnidadAdministrativa(){
        try {
            if(inicializarUnidadAdministrativa == null || !inicializarUnidadAdministrativa) {
                seleccionarTipoBien();
            }
            completeCodigoInventarioMaximoLote();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }  
    }
    
    public void seleccionarClasificacion(){
        try {
            FiltroCategoriaBienes fc = new FiltroCategoriaBienes();
            fc.setOrderBy(new String[]{"cabNombre"});
            fc.setIncluirCampos(new String[]{"cabNombre","cabCodigo", "cabVersion"});
            fc.setHabilitado(Boolean.TRUE);
            fc.setClasificacionId(comboClasificacionBienes != null && comboClasificacionBienes.getSelectedT() != null ? comboClasificacionBienes.getSelectedT().getCbiPk() : null);
            List<SgAfCategoriaBienes> categorias = catalogosRestClient.buscarCategoriaBienes(fc);
            comboCategoriaBienes = new SofisComboG(categorias, "cabNombre");
            comboCategoriaBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if(entidadEnEdicion != null && entidadEnEdicion.getBdeCategoriaFk() != null) {
                comboCategoriaBienes.setSelectedT(entidadEnEdicion.getBdeCategoriaFk());
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }  
    }
    
    public void tipoBienCategoriaVinculada() {
        if(entidadEnEdicion != null) {
            if(entidadEnEdicion.getBdeTipoBienCategoriaVinculada()) {
                entidadEnEdicion.setBdeCategoriaFk(null);
                if(comboClasificacionBienes != null) {
                    comboClasificacionBienes.setSelected(-1);
                }
                if(comboCategoriaBienes != null) {
                    comboCategoriaBienes.setSelected(-1);
                }
            }
        }
    }
    
    public void seleccionarUnidadActivoFijo() {
        try {
            if(comboUnidadesActivoFijo != null && comboUnidadesActivoFijo.getSelectedT() != null) {
                unidadAFSeleccionada = comboUnidadesActivoFijo.getSelectedT();
            } 
            
            unidadesAdministrativas = new ArrayList();
            sedes = new ArrayList();
            empleados = new ArrayList();
            if(cargarDefecto == null || !cargarDefecto) {
                unidadSeleccionada = null;
                sedeSeleccionada = null;
                empleadoSeleccionado = null;
            }
            
            if(inicializarUnidadAdministrativa == null || !inicializarUnidadAdministrativa) {
                //unidadSeleccionada = null;
                //sedeSeleccionada = null;
                //empleadoSeleccionado = null;
                entidadEnEdicion.setBdeAsignadoA("");
                entidadEnEdicion.setBdeEmpleadoFk(null);
            }
            
            if(tipoUnidadSeleccionada == null) {
                tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
                if (tipoUnidadSeleccionada != null) {
                    switch (tipoUnidadSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            esUnidadAdministrativa = Boolean.TRUE;
                            break;
                        case CENTRO_ESCOLAR:
                            esUnidadAdministrativa = Boolean.FALSE;
                            break;
                        default:
                            break;
                    }
                }
            }
            
            if(unidadAFSeleccionada != null && tipoUnidadSeleccionada != null) { 
                if(esUnidadAdministrativa != null && esUnidadAdministrativa) {
                    FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                    fuad.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                    fuad.setOrderBy(new String[]{"uadCodigo"});
                    fuad.setAscending(new boolean[]{true});
                    fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre","uadUnidadActivoFijoFk.uafDepartamento", "uadVersion"});
                    unidadesAdministrativas = unidadesAdministrativasRestClient.buscar(fuad);
                    
                } else {
                    if(!unidadAFSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                        FiltroSedes fil = new FiltroSedes();
                        fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                        fil.setSedDepartamentoId(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                        fil.setSedTipos(applicationBean.getTiposSedes());
                        fil.setPriSubvencionada(Boolean.TRUE);
                        fil.setOrderBy(new String[]{"sedCodigo"});
                        fil.setAscending(new boolean[]{true});
                        fil.setIncluirCampos(new String[]{"sedCodigo","sedTipo", "sedNombre", "sedDireccion.dirDepartamento","sedVersion"});
                        sedes = sedeRestClient.buscar(fil);
                    }
                }
            }
            if(inicializarUnidadAdministrativa == null || !inicializarUnidadAdministrativa) {
                seleccionarTipoBien();
            }
            completeCodigoInventarioMaximoLote();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }    
    
    public List<SgPersona> completePersona(String query) {
        try {
            FiltroPersona fper = new FiltroPersona();
            fper.setMaxResults(10L);
            fper.setAscending(new boolean[]{true});
            fper.setOrderBy(new String[]{ "perNombreBusqueda","perDui"});
            fper.setPerTieneDUI(Boolean.TRUE);
            fper.setPerDUINombreCompleto(query);
            fper.setIncluirCampos(new String[]{"perPk","perVersion","perPrimerNombre","perSegundoNombre","perTercerNombre","perPrimerApellido",
                                                "perSegundoApellido","perTercerApellido","perDui"});
            return personaRestClient.buscar(fper);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    
    public void buscarPersona() {
        try {
            listaEmpleados = new ArrayList();
            //entidadEnEdicion.setBdeEmpleadoFk(null);
            //empleadoSeleccionado = null;
            if(unidadSeleccionada != null && unidadSeleccionada.getUadPk() != null) {
                if((this.duiPersona != null && StringUtils.isNotBlank(this.duiPersona)) || (this.nombrePersona != null && StringUtils.isNotBlank(this.nombrePersona))) {
                    /**
                    FiltroEmpleados femp = new FiltroEmpleados();
                    femp.setMaxResults(20L);
                    femp.setDui(duiPersona);
                    femp.setPerNombreCompleto(nombrePersona);
                    femp.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());
                    femp.setAscending(new boolean[]{true});
                    femp.setOrderBy(new String[]{ "empPersonaFk.perNombreBusqueda","empPersonaFk.perDui"});
                    
                    femp.setIncluirCampos(new String[]{"empPk","empVersion","empPersonaFk.perPk","empPersonaFk.perVersion",
                                                    "empPersonaFk.perPrimerNombre","empPersonaFk.perSegundoNombre","empPersonaFk.perTercerNombre",
                                                    "empPersonaFk.perPrimerApellido","empPersonaFk.perSegundoApellido","empPersonaFk.perTercerApellido","empPersonaFk.perDui"});



                    //Primero buscamos en los empleados de la unidad, de lo contrario lo obtenemos de las personas
                    listaEmpleados = catalogosRestClient.buscarEmpleados(femp);
                  
                    if(listaEmpleados != null && !listaEmpleados.isEmpty()) {
                        if(listaEmpleados.size() == 1) {
                            empleadoSeleccionado = listaEmpleados.get(0);
                            entidadEnEdicion.setBdeEmpleadoFk(empleadoSeleccionado);
                            
                            if(empleadoSeleccionado == null ) {
                                List<BusinessError> errores = new ArrayList();
                                errores.add(new BusinessError(Mensajes.ERROR_PERSONA_NO_ENCONTRADA_DUI_ASOCIADO));
                                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                                return;
                            }
                        } else {
                            PrimeFaces.current().executeScript("PF('empleadosDialog').show()");
                        }
                    } else {**/
                        FiltroPersona fper = new FiltroPersona();
                        fper.setMaxResults(20L);
                        fper.setAscending(new boolean[]{true});
                        fper.setOrderBy(new String[]{ "perNombreBusqueda","perDui"});
                        fper.setDui(duiPersona);
                        fper.setPerTieneDUI(Boolean.TRUE);
                        fper.setPerNombreCompleto(nombrePersona);
                        fper.setIncluirCampos(new String[]{"perPk","perVersion","perPrimerNombre","perSegundoNombre","perTercerNombre","perPrimerApellido",
                                                            "perSegundoApellido","perTercerApellido","perDui"});
                        List<SgPersona> listaPersona = personaRestClient.buscar(fper);
                       // LOGGER.info("LISTA DE PERSONAS: " + listaPersona.size());
                        if(listaPersona != null && !listaPersona.isEmpty() && listaPersona.size() == 1) {
                            SgAfEmpleados empleado = new SgAfEmpleados();
                            SgPersona persona = listaPersona.get(0);
                            empleado.setEmpPersonaFk(persona);
                            empleado.setEmpUnidadAdministrativaFk(unidadSeleccionada);
                            empleadoSeleccionado = empleado;
                            entidadEnEdicion.setBdeEmpleadoFk(empleadoSeleccionado);
                            
                            if(empleadoSeleccionado == null ) {
                                List<BusinessError> errores = new ArrayList();
                                errores.add(new BusinessError(Mensajes.ERROR_PERSONA_NO_ENCONTRADA_DUI_ASOCIADO));
                                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                                return;
                            }
                        } else {
                            listaEmpleados = new ArrayList();
                            for(SgPersona per : listaPersona ) {
                                SgAfEmpleados empleado = new SgAfEmpleados();
                                empleado.setEmpPersonaFk(per);
                                empleado.setEmpUnidadAdministrativaFk(unidadSeleccionada);
                                
                                listaEmpleados.add(empleado);
                            }
                        
                            PrimeFaces.current().executeScript("PF('empleadosDialog').show()");
                        }
                    //}
                } else {
                    List<BusinessError> errores = new ArrayList();
                    errores.add(new BusinessError(Mensajes.ERROR_DUI_NOMBRE_PERSONA_VACIO));
                    JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                    return;
                }
            } else {
                List<BusinessError> errores = new ArrayList();
                errores.add(new BusinessError(Mensajes.ERROR_UNIDAD_ADMINISTRATIVA_VACIO));
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                return;
            }
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarEmpleadoUnidad() {
        empleadoSeleccionado = new SgAfEmpleados();
        empleadoSeleccionado.setEmpPersonaFk(personaSeleccionada);
        entidadEnEdicion.setBdeEmpleadoFk(empleadoSeleccionado);
    }
    
    public void seleccionarMunicipio() {
        try {
            if(comboMunicipios != null && comboMunicipios.getSelectedT() != null && comboMunicipios.getSelectedT().getMunPk() != null ) {
                FiltroSedes fil = new FiltroSedes();
                fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                fil.setSedMunicipioId(comboMunicipios.getSelectedT().getMunPk());
                fil.setSedTipos(applicationBean.getTiposSedes());
                fil.setPriSubvencionada(Boolean.TRUE);
                fil.setOrderBy(new String[]{"sedNombre"});
                fil.setAscending(new boolean[]{false});
                fil.setIncluirCampos(new String[]{"sedCodigo","sedTipo", "sedNombre", "sedDireccion.dirDepartamento","sedVersion"});
                sedes = sedeRestClient.buscar(fil);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }

    public void seleccionarTipoBien() {
        String codigoUnidad = StringUtils.EMPTY;
        String codigoTipoBien = StringUtils.EMPTY;
        String codigoCorrelativo = StringUtils.EMPTY;
        Integer codigoSize = 0;
        FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
        //filtro.setValidarEstadoSolicitud(Boolean.TRUE);
        Integer numeroCorrelativo = 0;
        Boolean buscar = Boolean.FALSE;
        codigoCorrelativoMaxVal = "";
        this.renderSeccionBienesMuebles = Boolean.FALSE;
        this.renderSeccionVehiculos = Boolean.FALSE;
        try {
            entidadEnEdicion.setBdeCodigoCorrelativo("");
            entidadEnEdicion.setBdeCodigoInventario("");
            entidadEnEdicion.setBdeDescripcion("");

            if(tipoBienSeleccionado != null && (sedeSeleccionada != null || unidadSeleccionada != null)) {      
                
                codigoTipoBien = tipoBienSeleccionado.getTbiCodigo();
                entidadEnEdicion.setBdeDescripcion(tipoBienSeleccionado.getTbiNombre());
  
                if(tipoBienSeleccionado.getTbiCategoriaBienes() != null) {
                    entidadEnEdicion.setBdeVidaUtil(tipoBienSeleccionado.getTbiCategoriaBienes().getCabVidaUtil());

                    if(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList() != null) {
                        if(OperacionesGenerales.existeEnArray(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(),EnumSeccionesCargoBienes.SECCION_BIENES_MUEBLES)) {
                            this.renderSeccionBienesMuebles = Boolean.TRUE;
                        } else if(OperacionesGenerales.existeEnArray(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(),EnumSeccionesCargoBienes.SECCION_VEHICULOS)) {
                            this.renderSeccionVehiculos = Boolean.TRUE;
                        } else if(OperacionesGenerales.existeEnArray(tipoBienSeleccionado.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(),EnumSeccionesCargoBienes.SECCION_ID_EXTERNO)) {
                            tipoBienSeleccionado = null;
                            entidadEnEdicion.setBdeDescripcion("");
                            List<BusinessError> errores = new ArrayList();
                            errores.add(new BusinessError(Mensajes.ERROR_NO_PUEDE_CREAR_BIEN_INMUEBLE));
                            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                            return;
                        }
                    }
                }
                
                if(entidadEnEdicion.getBdePk() == null || tipoBienOriginal == null || (tipoBienOriginal != null && !tipoBienOriginal.getTbiPk().equals(tipoBienSeleccionado.getTbiPk()))) {
                    buscar = Boolean.TRUE;
                }        
                
                if(esUnidadAdministrativa != null) {
                    if(esUnidadAdministrativa) {
                        if(unidadSeleccionada != null) {
                            codigoSize = applicationBean.getCodigoCorrelativoUnidadAdministrativaSize();
                            
                            codigoUnidad = unidadSeleccionada.getUadCodigo();
                            filtro.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());

                            
                            if(unidadOriginal != null) {
                                if(!unidadOriginal.getUadPk().equals(unidadSeleccionada.getUadPk())) {
                                    buscar = Boolean.TRUE;
                                }
                            } else {
                                buscar = Boolean.TRUE;
                            }
                        }
                    } else if(sedeSeleccionada != null) {
                        codigoSize = applicationBean.getCodigoCorrelativoCentroEducativoSize();
                        codigoUnidad = sedeSeleccionada.getSedCodigo();
                        filtro.setUnidadAdministrativaId(sedeSeleccionada.getSedPk());
                        if(sedeOriginal != null) {
                            if(!sedeOriginal.getSedPk().equals(sedeSeleccionada.getSedPk())) {
                                buscar = Boolean.TRUE;
                            } else {
                                buscar = Boolean.TRUE;
                            }
                        }
                    }
                }

                if(buscar) {
                    filtro.setTipoUnidad(tipoUnidadSeleccionada);
                    filtro.setTipoBienId(tipoBienSeleccionado.getTbiPk());
                    filtro.setEstadoCodigo(Constantes.CODIGO_ESTADO_TRASLADADO);
                    numeroCorrelativo = bienesRestClient.buscarUltimoCorrelativo(filtro);

                    if(numeroCorrelativo.compareTo(Integer.valueOf(StringUtils.leftPad("", codigoSize, "9"))) > 0) {
                        codigoCorrelativoMaxVal = StringUtils.leftPad("", numeroCorrelativo.toString().length(), "9");
                    } else {
                        codigoCorrelativoMaxVal = StringUtils.leftPad("", codigoSize, "9");
                    }
                    
                    codigoCorrelativo = StringUtils.leftPad(numeroCorrelativo.toString(), codigoCorrelativoMaxVal.length(), "0");
                } else {
                    codigoCorrelativoMaxVal = StringUtils.leftPad("", codigoSize, "9");
                    Integer correlativo = entidadEnEdicion.getBdeNumCorrelativo();
                    if(correlativo != null) {
                        if(correlativo.compareTo(Integer.valueOf(StringUtils.leftPad("", codigoSize, "9"))) > 0) {
                            codigoCorrelativoMaxVal = StringUtils.leftPad("", correlativo.toString().length(), "9");
                        }
                    }
                    codigoCorrelativo = StringUtils.leftPad(correlativo.toString(),codigoCorrelativoMaxVal.length(), "0");
                }

                entidadEnEdicion.setBdeCodigoCorrelativo(codigoCorrelativo);
                entidadEnEdicion.setBdeCodigoInventario(codigoUnidad + "-" + codigoTipoBien + "-" + codigoCorrelativo); 
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarEmpleado() {
        if(empleadoSeleccionado != null) {
            entidadEnEdicion.setBdeAsignadoA("");
        }
    }
    
    public void procesarLoteSelected() {
        if (!this.procesarLote) {
            entidadEnEdicion.setBdeCantidadLote(null);
        }
    }
    
    public void seleccionarOtraCategoria() {
        if (entidadEnEdicion.getBdeTipoBienCategoriaVinculada() != null && entidadEnEdicion.getBdeTipoBienCategoriaVinculada()) {
            this.renderSeccionCategoria = Boolean.FALSE;
        } else {
            this.renderSeccionCategoria = Boolean.TRUE;
        }
    }
    
    public void guardar() {
        try {
            entidadEnEdicion.setBdeFuenteFinanciamiento(fuenteSeleccionada);
            entidadEnEdicion.setBdeTipoBien(tipoBienSeleccionado);
            entidadEnEdicion.setBdeFormaAdquisicion(comboFormaAdquisicion.getSelectedT());
            entidadEnEdicion.setBdeProyectos(comboProyectos != null ? comboProyectos.getSelectedT() : null);
            entidadEnEdicion.setBdeEstadoCalidad(comboEstadosCalidad.getSelectedT());
            if(empleadoSeleccionado != null) {
                entidadEnEdicion.setBdeAsignadoA("");
                entidadEnEdicion.setBdeEmpleadoFk(empleadoSeleccionado);
            }
            
            if(entidadEnEdicion.getBdeTipoBienCategoriaVinculada() != null && !entidadEnEdicion.getBdeTipoBienCategoriaVinculada()) {
                entidadEnEdicion.setBdeCategoriaFk(comboCategoriaBienes.getSelectedT());
            }
            
            if(entidadEnEdicion.getBdePk()== null) {
                entidadEnEdicion.setBdeFechaCreacion(LocalDateTime.now());
                if(entidadEnEdicion.getBdeEsLote()) {
                    entidadEnEdicion.setBdeEstadoProcesoLote(0);
                }
            }

            if(entidadEnEdicion.getBdeEstadosBienes() == null) { // Solo si no está previamente seteado
                entidadEnEdicion.setBdeEstadosBienes(estadoExistente);
            }
            
            if(esUnidadAdministrativa) {
                entidadEnEdicion.setBdeUnidadesAdministrativas(unidadSeleccionada);
            } else {
                entidadEnEdicion.setBdeSede(sedeSeleccionada);
            }
            Boolean mostrarMensajeLote = Boolean.FALSE;
            
            if(entidadEnEdicion.getBdePk() == null && (entidadEnEdicion.getBdeEsLote() != null && entidadEnEdicion.getBdeEsLote())) {
                mostrarMensajeLote = Boolean.TRUE;
            } else if(entidadEnEdicion.getBdePk() != null && (entidadEnEdicion.getBdeEsLote() != null && entidadEnEdicion.getBdeEsLote()) 
                    && (entidadEnEdicion.getBdeEsLoteSiap() != null && entidadEnEdicion.getBdeEsLoteSiap()) && (entidadEnEdicion.getBdeCompletado() == null || !entidadEnEdicion.getBdeCompletado())) {
                mostrarMensajeLote = Boolean.TRUE;
            }
            entidadEnEdicion = bienesRestClient.guardar(entidadEnEdicion);
            empleadoSeleccionado = entidadEnEdicion.getBdeEmpleadoFk();
            //Nuevos valores original. Esto para tener control del correlativo.
            unidadOriginal  = entidadEnEdicion.getBdeUnidadesAdministrativas();
            sedeOriginal = entidadEnEdicion.getBdeSede();
            tipoBienOriginal = entidadEnEdicion.getBdeTipoBien();
            
            renderLote = Boolean.FALSE;
            verificarActualizarDatosAdquisicion();
            if(mostrarMensajeLote) {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO_EJECUCION_LOTE_POSTERIOR) + " " + entidadEnEdicion.getBdeCodigoInventario() +".", "");
            } else {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
            

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgAfEstadosCalidad> getComboEstadosCalidad() {
        return comboEstadosCalidad;
    }

    public void setComboEstadosCalidad(SofisComboG<SgAfEstadosCalidad> comboEstadosCalidad) {
        this.comboEstadosCalidad = comboEstadosCalidad;
    }

    public SofisComboG<SgAfFormaAdquisicion> getComboFormaAdquisicion() {
        return comboFormaAdquisicion;
    }

    public void setComboFormaAdquisicion(SofisComboG<SgAfFormaAdquisicion> comboFormaAdquisicion) {
        this.comboFormaAdquisicion = comboFormaAdquisicion;
    }

    public SofisComboG<SgAfProyectos> getComboProyectos() {
        return comboProyectos;
    }

    public void setComboProyectos(SofisComboG<SgAfProyectos> comboProyectos) {
        this.comboProyectos = comboProyectos;
    }

    public SofisComboG<SgAfFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public SgAfTipoBienes getTipoBienSeleccionado() {
        return tipoBienSeleccionado;
    }

    public void setTipoBienSeleccionado(SgAfTipoBienes tipoBienSeleccionado) {
        this.tipoBienSeleccionado = tipoBienSeleccionado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getRenderLote() {
        return renderLote;
    }

    public void setRenderLote(Boolean renderLote) {
        this.renderLote = renderLote;
    }

    public Boolean getRenderCantidadLote() {
        return renderCantidadLote;
    }

    public void setRenderCantidadLote(Boolean renderCantidadLote) {
        this.renderCantidadLote = renderCantidadLote;
    }

    public Boolean getProcesarLote() {
        return procesarLote;
    }

    public void setProcesarLote(Boolean procesarLote) {
        this.procesarLote = procesarLote;
    }

    public SgAfBienDepreciable getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfBienDepreciable entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijo() {
        return comboUnidadesActivoFijo;
    }

    public void setComboUnidadesActivoFijo(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo) {
        this.comboUnidadesActivoFijo = comboUnidadesActivoFijo;
    }


    public Boolean getRenderMunicipio() {
        if(sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_CAMPO_MUNICIPIO)) {
            if(esUnidadAdministrativa != null && esUnidadAdministrativa) {
               this.renderMunicipio = true;
            } else {
               this.renderMunicipio = false;
            }
        } else {
            this.renderMunicipio = false;
        }
        return renderMunicipio;
    }

    public void setRenderMunicipio(Boolean renderMunicipio) {
        this.renderMunicipio = renderMunicipio;
    }

    public List<SgAfFormaAdquisicion> getListaFormaAdquisicion() {
        return listaFormaAdquisicion;
    }

    public void setListaFormaAdquisicion(List<SgAfFormaAdquisicion> listaFormaAdquisicion) {
        this.listaFormaAdquisicion = listaFormaAdquisicion;
    }

    public List<SgAfProyectos> getListaProyectos() {
        return listaProyectos;
    }

    public void setListaProyectos(List<SgAfProyectos> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }

    public List<SgAfFuenteFinanciamiento> getListaFuentes() {
        return listaFuentes;
    }

    public void setListaFuentes(List<SgAfFuenteFinanciamiento> listaFuentes) {
        this.listaFuentes = listaFuentes;
    }

    public List<SgAfEstadosCalidad> getListaEstadosCalidad() {
        return listaEstadosCalidad;
    }

    public void setListaEstadosCalidad(List<SgAfEstadosCalidad> listaEstadosCalidad) {
        this.listaEstadosCalidad = listaEstadosCalidad;
    }

    public List<SgAfUnidadesActivoFijo> getListaUnidadesAF() {
        return listaUnidadesAF;
    }

    public void setListaUnidadesAF(List<SgAfUnidadesActivoFijo> listaUnidadesAF) {
        this.listaUnidadesAF = listaUnidadesAF;
    }

    public List<SgMunicipio> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SgMunicipio> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public Boolean getEditarCorrelativo() {
        return editarCorrelativo;
    }

    public void setEditarCorrelativo(Boolean editarCorrelativo) {
        this.editarCorrelativo = editarCorrelativo;
    }

    

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getEsUnidadAdministrativa() {
        return esUnidadAdministrativa;
    }

    public void setEsUnidadAdministrativa(Boolean esUnidadAdministrativa) {
        this.esUnidadAdministrativa = esUnidadAdministrativa;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public SgAfTipoBienes getTipoBienOriginal() {
        return tipoBienOriginal;
    }

    public void setTipoBienOriginal(SgAfTipoBienes tipoBienOriginal) {
        this.tipoBienOriginal = tipoBienOriginal;
    }

    public SgSede getSedeOriginal() {
        return sedeOriginal;
    }

    public void setSedeOriginal(SgSede sedeOriginal) {
        this.sedeOriginal = sedeOriginal;
    }

    public SgAfUnidadesAdministrativas getUnidadOriginal() {
        return unidadOriginal;
    }

    public void setUnidadOriginal(SgAfUnidadesAdministrativas unidadOriginal) {
        this.unidadOriginal = unidadOriginal;
    }

    public SgAfUnidadesAdministrativas getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(SgAfUnidadesAdministrativas unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgAfUnidadesActivoFijo getUnidadAFSeleccionada() {
        return unidadAFSeleccionada;
    }

    public void setUnidadAFSeleccionada(SgAfUnidadesActivoFijo unidadAFSeleccionada) {
        this.unidadAFSeleccionada = unidadAFSeleccionada;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidad() {
        return comboTiposUnidad;
    }

    public void setComboTiposUnidad(SofisComboG<TipoUnidad> comboTiposUnidad) {
        this.comboTiposUnidad = comboTiposUnidad;
    }

    public TipoUnidad getTipoUnidadSeleccionada() {
        return tipoUnidadSeleccionada;
    }

    public void setTipoUnidadSeleccionada(TipoUnidad tipoUnidadSeleccionada) {
        this.tipoUnidadSeleccionada = tipoUnidadSeleccionada;
    }

    public String getCodigoCorrelativoMaxVal() {
        return codigoCorrelativoMaxVal;
    }

    public void setCodigoCorrelativoMaxVal(String codigoCorrelativoMaxVal) {
        this.codigoCorrelativoMaxVal = codigoCorrelativoMaxVal;
    }

    public SgAfEmpleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(SgAfEmpleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public SgAfFuenteFinanciamiento getFuenteSeleccionada() {
        return fuenteSeleccionada;
    }

    public void setFuenteSeleccionada(SgAfFuenteFinanciamiento fuenteSeleccionada) {
        this.fuenteSeleccionada = fuenteSeleccionada;
    }

    public Boolean getRenderSeccionBienesMuebles() {
        return renderSeccionBienesMuebles;
    }

    public void setRenderSeccionBienesMuebles(Boolean renderSeccionBienesMuebles) {
        this.renderSeccionBienesMuebles = renderSeccionBienesMuebles;
    }

    public Boolean getRenderSeccionVehiculos() {
        return renderSeccionVehiculos;
    }

    public void setRenderSeccionVehiculos(Boolean renderSeccionVehiculos) {
        this.renderSeccionVehiculos = renderSeccionVehiculos;
    }

    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
    }

    public SofisComboG<SgAfClasificacionBienes> getComboClasificacionBienes() {
        return comboClasificacionBienes;
    }

    public void setComboClasificacionBienes(SofisComboG<SgAfClasificacionBienes> comboClasificacionBienes) {
        this.comboClasificacionBienes = comboClasificacionBienes;
    }

    public SofisComboG<SgAfCategoriaBienes> getComboCategoriaBienes() {
        return comboCategoriaBienes;
    }

    public void setComboCategoriaBienes(SofisComboG<SgAfCategoriaBienes> comboCategoriaBienes) {
        this.comboCategoriaBienes = comboCategoriaBienes;
    }

    public Boolean getRenderSeccionCategoria() {
        return renderSeccionCategoria;
    }

    public void setRenderSeccionCategoria(Boolean renderSeccionCategoria) {
        this.renderSeccionCategoria = renderSeccionCategoria;
    }

    public Boolean getEditarDatosAdquisicion() {
        return editarDatosAdquisicion;
    }

    public void setEditarDatosAdquisicion(Boolean editarDatosAdquisicion) {
        this.editarDatosAdquisicion = editarDatosAdquisicion;
    }

    public Boolean getEditarEmpleado() {
        return editarEmpleado;
    }

    public void setEditarEmpleado(Boolean editarEmpleado) {
        this.editarEmpleado = editarEmpleado;
    }

    public Boolean getRenderFichaInmueble() {
        return renderFichaInmueble;
    }

    public void setRenderFichaInmueble(Boolean renderFichaInmueble) {
        this.renderFichaInmueble = renderFichaInmueble;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }

    public List<SgAfEmpleados> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<SgAfEmpleados> empleados) {
        this.empleados = empleados;
    }

    public List<SgAfUnidadesAdministrativas> getUnidadesAdministrativas() {
        return unidadesAdministrativas;
    }

    public void setUnidadesAdministrativas(List<SgAfUnidadesAdministrativas> unidadesAdministrativas) {
        this.unidadesAdministrativas = unidadesAdministrativas;
    }

    public List<SgSede> getSedes() {
        return sedes;
    }

    public void setSedes(List<SgSede> sedes) {
        this.sedes = sedes;
    }

    public Boolean getCargarDefecto() {
        return cargarDefecto;
    }

    public void setCargarDefecto(Boolean cargarDefecto) {
        this.cargarDefecto = cargarDefecto;
    }

    public String getDuiPersona() {
        return duiPersona;
    }

    public void setDuiPersona(String duiPersona) {
        this.duiPersona = duiPersona;
    }

    public Boolean getEditarTipoBien() {
        return editarTipoBien;
    }

    public void setEditarTipoBien(Boolean editarTipoBien) {
        this.editarTipoBien = editarTipoBien;
    }

    public Boolean getEditarProyecto() {
        return editarProyecto;
    }

    public void setEditarProyecto(Boolean editarProyecto) {
        this.editarProyecto = editarProyecto;
    }

    public Boolean getEditarFuenteFinanciamiento() {
        return editarFuenteFinanciamiento;
    }

    public void setEditarFuenteFinanciamiento(Boolean editarFuenteFinanciamiento) {
        this.editarFuenteFinanciamiento = editarFuenteFinanciamiento;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public List<SgAfEmpleados> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<SgAfEmpleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public Boolean getRenderEmpleados() {
        return renderEmpleados;
    }

    public void setRenderEmpleados(Boolean renderEmpleados) {
        this.renderEmpleados = renderEmpleados;
    }

    public SgAfEmpleados getEmpleadoSeleccionadoTemp() {
        return empleadoSeleccionadoTemp;
    }

    public void setEmpleadoSeleccionadoTemp(SgAfEmpleados empleadoSeleccionadoTemp) {
        this.empleadoSeleccionadoTemp = empleadoSeleccionadoTemp;
    }

    public SgPersona getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(SgPersona personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }
}
