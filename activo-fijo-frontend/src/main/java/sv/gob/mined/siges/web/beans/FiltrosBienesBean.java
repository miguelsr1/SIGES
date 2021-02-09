/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfClasificacionBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumEstados;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEmpleados;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EmpleadosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.OperacionesGenerales;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class FiltrosBienesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FiltrosBienesBean.class.getName());
    
    private Boolean renderActivoFijo = Boolean.FALSE;
    private Boolean renderActivo = Boolean.FALSE;
    private Boolean renderUnidadActivoFijo = Boolean.FALSE;
    private Boolean renderTipoBien= Boolean.FALSE;
    private Boolean renderCategoria = Boolean.FALSE;
    private Boolean renderProyecto= Boolean.FALSE;
    private Boolean renderAsignadoA= Boolean.FALSE;
    private Boolean renderNumSerie = Boolean.FALSE;
    private Boolean renderSeccion = Boolean.FALSE;
    private Boolean renderValorAdquisicion = Boolean.FALSE;
    private Boolean renderFechaAdquisicion = Boolean.FALSE;
    private Boolean renderTipoUnidad = Boolean.FALSE;
    private Boolean renderUnidadAdministrativa = Boolean.FALSE;
    private Boolean renderCalidad = Boolean.FALSE;
    private Boolean renderFuente = Boolean.FALSE;
    private Boolean renderEstado = Boolean.FALSE;
    private Boolean renderMarca = Boolean.FALSE;
    private Boolean renderCodInventario = Boolean.FALSE;
    private Boolean renderModelo = Boolean.FALSE;
    private Boolean renderFechaCreacion = Boolean.FALSE;
    private Boolean renderFechaSolicitud = Boolean.FALSE;
    private Boolean renderFechaDescargo = Boolean.FALSE;
    private Boolean renderNumeroSolicitud = Boolean.FALSE;
    private Boolean renderEstadoSolicitud = Boolean.FALSE;
    private Boolean renderEstadoDescargo = Boolean.FALSE;
    private Boolean renderNumeroActa = Boolean.FALSE;
    private Boolean renderMotor = Boolean.FALSE;
    private Boolean renderPlaca = Boolean.FALSE;
    private Boolean renderChasis = Boolean.FALSE;
    private Boolean renderFechaModificacion = Boolean.FALSE;
    private Boolean renderMunicipio = Boolean.FALSE;
    private Boolean renderFormaAdquisicion = Boolean.FALSE;
    private Boolean mostrarComboEmpleados = Boolean.FALSE;
    private Boolean renderValorAdquisicionDesde = Boolean.FALSE;
    private Boolean renderValorAdquisicionHasta = Boolean.FALSE;
    private Boolean cargarUADefecto = Boolean.FALSE;
    private Boolean validarEmpleado = Boolean.FALSE;
    private String elementosActualizar;
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private EmpleadosRestClient empleadosRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private ApplicationBean applicationBean;
    
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo;
    private SofisComboG<TipoUnidad> comboTiposUnidad;
    private SofisComboG<SgMunicipio> comboMunicipios;
    private SofisComboG<SgAfEstadosBienes> comboEstadosBienes;
    private SofisComboG<SgAfClasificacionBienes> comboClasificacionBienes;
    private SofisComboG<SgAfCategoriaBienes> comboCategoriaBienes;
    private SofisComboG<SgAfEstadosDescargo> comboDescargos;
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF = new ArrayList();
    private TipoUnidad tipoUnidadSeleccionada;
    private String tipoUnidad;
    private FiltroCodiguera filtro;
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    private Integer paginado = 10;
    private Boolean esUnidadAdministrativa;
    private SofisComboG<SgAfProyectos> comboProyectos;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento;
    
    private SgAfUnidadesAdministrativas unidadSeleccionada;
    private SgSede sedeSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFSeleccionada;
    private SgAfEmpleados empleadoSeleccionado;
    //private List<SgAfEmpleados> empleados = new ArrayList();
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativas = new ArrayList();
    private List<SgSede> sedes = new ArrayList();
    private String tipoPantalla="D";
    
    public FiltrosBienesBean() {
    }
    
    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.cargarUADefecto = (Boolean) request.getAttribute("cargarUADefecto");
            
            cargarCombos();
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }
    
    public void cargarCombos() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            
            List<TipoUnidad> tipoUnidadLista = new ArrayList();
            if(sessionBean.getUnidadADDefecto() != null) {
                tipoUnidadLista.add(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                comboTiposUnidad = new SofisComboG(tipoUnidadLista, "text");
                tipoUnidad ="ua";
                comboTiposUnidad.setSelectedT(TipoUnidad.UNIDAD_ADMINISTRATIVA);           
                filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            } else if(sessionBean.getSedeDefecto() != null) {
                tipoUnidadLista.add(TipoUnidad.CENTRO_ESCOLAR);
                comboTiposUnidad = new SofisComboG(tipoUnidadLista, "text");
                tipoUnidad ="ce";
                comboTiposUnidad.setSelectedT(TipoUnidad.CENTRO_ESCOLAR);           
                filtroBienes.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);

                tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            } else {
                tipoUnidadLista = new ArrayList(Arrays.asList(TipoUnidad.values()));
                comboTiposUnidad = new SofisComboG(tipoUnidadLista, "text");
                comboTiposUnidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                
                //LOGGER.info("this.cargarUADefect: " + this.cargarUADefecto);
                if(this.cargarUADefecto != null && this.cargarUADefecto) {
                    comboTiposUnidad.setSelectedT(TipoUnidad.UNIDAD_ADMINISTRATIVA);           
                    filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);

                    tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
                    tipoUnidad ="ua";
                }
                
            }
            comboTiposUnidad.ordenar();

            
            
            FiltroUnidadesActivoFijo fua = new FiltroUnidadesActivoFijo();
            fua.setHabilitado(Boolean.TRUE);
            fua.setOrderBy(new String[]{"uafCodigo"});
            fua.setAscending(new boolean[]{true});
            fua.setIncluirCampos(new String[]{"uafCodigo","uafNombre", "uafDepartamento","uafVersion"});
            listaUnidadesAF = unidadesActivoFijoRestClient.buscar(fua);

            comboUnidadesActivoFijo = new SofisComboG(listaUnidadesAF, "codigoNombre");
            comboUnidadesActivoFijo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            filtro.setOrderBy(new String[]{"ebiNombre"});
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo","ebiAplicaPara", "ebiVersion"});
            filtro.setAscending(new boolean[]{true});
            List<SgAfEstadosBienes> estadosBienes = catalogosRestClient.buscarEstadosBienes(filtro);
            
            List<SgAfEstadosBienes> estadosMostrar = new ArrayList();    
            
            if(estadosBienes != null && !estadosBienes.isEmpty()) {
                for(SgAfEstadosBienes estado : estadosBienes) {        
                    if(OperacionesGenerales.existeEnArray(estado.getEbiAplicaPara(), EnumEstados.DESCARGO.getText()) && tipoPantalla.equals("D")) {
                        estadosMostrar.add(estado);
                    }
                    
                } 
            }
            
            comboEstadosBienes = new SofisComboG(estadosMostrar, "ebiNombre");
            comboEstadosBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
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
            
            FiltroFuenteFinanciamientoAF fuentes = new FiltroFuenteFinanciamientoAF();
            fuentes.setHabilitado(Boolean.TRUE);
            fuentes.setOrderBy(new String[]{"ffiNombre"});
            fuentes.setIncluirCampos(new String[]{"ffiNombre","ffiAplicaPara", "ffiVersion"});
            List<SgAfFuenteFinanciamiento> listaFuentes = catalogosRestClient.buscarFuenteFinanciamiento(fuentes);        
            
            comboFuenteFinanciamiento = new SofisComboG(listaFuentes, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroProyecto fpro = new FiltroProyecto();
            fpro.setHabilitado(Boolean.TRUE);
            fpro.setOrderBy(new String[]{"proNombre"});
            fpro.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            List<SgAfProyectos> proyectos = catalogosRestClient.buscarProyecto(fpro);
            comboProyectos = new SofisComboG(proyectos, "proNombre");
            comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
            //actualizarFiltro();
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
            this.tipoUnidad ="ce";
            this.esUnidadAdministrativa = Boolean.FALSE;
            sedeSeleccionada = sessionBean.getSedeDefecto();
            
            tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            
            filtroBienes.setTipoUnidad(tipoUnidadSeleccionada);
            filtroBienes.setUnidadAdministrativaId(sedeSeleccionada.getSedPk());
            
            
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadSeleccionada);
            
            comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
            
            if(sedeSeleccionada!= null) {
                if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                    for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                        if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            unidadAFSeleccionada = unidadesAF;
                            comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
                        }
                    }
                } 

            }
                    
            seleccionarUnidadActivoFijo();
        }
    }
    public void cargarUADPorDefecto() throws Exception {
        if (sessionBean.getUnidadADDefecto() != null) {
            this.esUnidadAdministrativa = Boolean.TRUE;
            this.tipoUnidad ="ua";
            unidadSeleccionada = sessionBean.getUnidadADDefecto();
            tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            
            filtroBienes.setTipoUnidad(tipoUnidadSeleccionada);
            filtroBienes.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadSeleccionada);
            
            comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
            
            if(unidadSeleccionada != null) {
                unidadAFSeleccionada = unidadSeleccionada.getUadUnidadActivoFijoFk();
                comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
            } 
            
            seleccionarUnidadActivoFijo();
        }
    }
    
    public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        if(comboTiposUnidad != null) {
            comboTiposUnidad.setSelected(-1);
        }
        if(comboCategoriaBienes != null) {
            comboCategoriaBienes.setSelected(-1);
        }
        if(comboClasificacionBienes != null) {
            comboClasificacionBienes.setSelected(-1);
        }
        if(comboDescargos != null) {
            comboDescargos.setSelected(-1);
        }
        if(comboEstadosBienes != null) {
            comboEstadosBienes.setSelected(-1);
        }
        if(comboFuenteFinanciamiento != null) {
            comboFuenteFinanciamiento.setSelected(-1);
        }
        if(comboMunicipios != null) {
            comboMunicipios.setSelected(-1);
        }
        if(comboProyectos != null) {
            comboProyectos.setSelected(-1);
        }
        if(comboUnidadesActivoFijo != null) {
            comboUnidadesActivoFijo.setSelected(-1);
        }
      
        unidadAFSeleccionada = null;
        
        sedes = new ArrayList();
        unidadesAdministrativas = new ArrayList();
        //empleados = new ArrayList();
        tipoUnidadSeleccionada= null;
        tipoUnidad="";
        cargarEntidad();
    }
    public void seleccionarTipoUnidadAF() {
        esUnidadAdministrativa = null;
        tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
        this.tipoUnidad ="";
        if (tipoUnidadSeleccionada != null) {
            switch (tipoUnidadSeleccionada) {
                case UNIDAD_ADMINISTRATIVA:
                    esUnidadAdministrativa = Boolean.TRUE;
                    this.tipoUnidad ="ua";
                    break;
                case CENTRO_ESCOLAR:
                    esUnidadAdministrativa = Boolean.FALSE;
                    this.tipoUnidad ="ce";
                    break;
                default:
                    break;
            }
        }
        filtroBienes.setTipoUnidad(tipoUnidadSeleccionada);
        seleccionarUnidadActivoFijo();
    }
    
    public void seleccionarUnidadActivoFijo() {
        try {
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if(comboUnidadesActivoFijo.getSelectedT() != null) {
                unidadAFSeleccionada = comboUnidadesActivoFijo.getSelectedT();
            } else {
                unidadAFSeleccionada = null;
            }
            unidadesAdministrativas = new ArrayList();
            sedes = new ArrayList();
            
            if(tipoUnidadSeleccionada == null) {
                this.tipoUnidad ="";
                tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
                if (tipoUnidadSeleccionada != null) {
                    switch (tipoUnidadSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            esUnidadAdministrativa = Boolean.TRUE;
                            this.tipoUnidad ="ua";
                            break;
                        case CENTRO_ESCOLAR:
                            esUnidadAdministrativa = Boolean.FALSE;
                            this.tipoUnidad ="ce";
                            break;
                        default:
                            break;
                    }
                }
                filtroBienes.setTipoUnidad(tipoUnidadSeleccionada);
            }

           if(unidadAFSeleccionada != null && esUnidadAdministrativa != null) {
               filtroBienes.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
               if(tipoUnidadSeleccionada != null) {
                   if(esUnidadAdministrativa) {
                        FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                        fuad.setOrderBy(new String[]{"uadCodigo"});
                        fuad.setAscending(new boolean[]{true});
                        fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                        fuad.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                        unidadesAdministrativas = unidadesAdministrativasRestClient.buscar(fuad);

                        seleccionarUnidadAdministrativa();
                    } else {
                        if(unidadAFSeleccionada.getUafPk()!= null 
                                    && unidadAFSeleccionada.getUafDepartamento() != null
                                    && !unidadAFSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            FiltroMunicipio fm = new FiltroMunicipio();
                            fm.setMunHabilitado(Boolean.TRUE);
                            fm.setMunDepartamentoFk(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                            fm.setAscending(new boolean[]{true});
                            fm.setOrderBy(new String[]{"munNombre"});
                            fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});

                            List<SgMunicipio> municipios = catalogosRestClient.buscarMunicipio(fm);
                            comboMunicipios = new SofisComboG(municipios, "munNombre");
                            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                            seleccionarMunicipio();
                        }
                    } 
               }
           } else {
                filtroBienes.setUnidadActivoFijoId(null);
                comboMunicipios = new SofisComboG();
                comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
           }
           actualizarFiltro();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    public void seleccionarMunicipio() {
        try {
                FiltroSedes fil = new FiltroSedes();
                fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                fil.setSedTipos(applicationBean.getTiposSedes());
                fil.setPriSubvencionada(Boolean.TRUE);
                fil.setSedDepartamentoId(comboUnidadesActivoFijo != null && comboUnidadesActivoFijo.getSelectedT() != null ? comboUnidadesActivoFijo.getSelectedT().getUafDepartamento().getDepPk(): null);
                fil.setSedMunicipioId(comboMunicipios != null && comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
                fil.setOrderBy(new String[]{"sedCodigo"});
                fil.setAscending(new boolean[]{true});
                fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedVersion"});
                sedes = sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    public void seleccionarUnidadAdministrativa(){
        try {
            //empleados = new ArrayList();
            if(esUnidadAdministrativa != null && esUnidadAdministrativa && unidadSeleccionada != null) {
                filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                filtroBienes.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());
                
                FiltroEmpleados femp = new FiltroEmpleados();
                femp.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());
                femp.setOrderBy(new String[]{"empPersonaFk.perPrimerNombre"});
                femp.setAscending(new boolean[]{true});
                femp.setIncluirCampos(new String[]{"empPk","empVersion","empPersonaFk.perPk","empPersonaFk.perVersion",
                                                "empPersonaFk.perPrimerNombre","empPersonaFk.perSegundoNombre","empPersonaFk.perTercerNombre",
                                                "empPersonaFk.perPrimerApellido","empPersonaFk.perSegundoApellido","empPersonaFk.perTercerApellido"});

               // empleados = catalogosRestClient.buscarEmpleados(femp);
            } else if(sedeSeleccionada != null) {
                filtroBienes.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                filtroBienes.setUnidadAdministrativaId(sedeSeleccionada.getSedPk());
            }
            actualizarFiltro();
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
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }  
    }
    public List<SgAfEmpleados> completeEmpleado(String query) {
        try {
            if(unidadSeleccionada != null) {
                FiltroEmpleados femp = new FiltroEmpleados();
                femp.setMaxResults(10L);
                femp.setPerDUINombreCompleto(query);
                femp.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());
                femp.setAscending(new boolean[]{true});
                femp.setOrderBy(new String[]{ "empPersonaFk.perNombreBusqueda","empPersonaFk.perDui"});

                femp.setIncluirCampos(new String[]{"empPk","empVersion","empPersonaFk.perPk","empPersonaFk.perVersion",
                                                "empPersonaFk.perPrimerNombre","empPersonaFk.perSegundoNombre","empPersonaFk.perTercerNombre",
                                                "empPersonaFk.perPrimerApellido","empPersonaFk.perSegundoApellido","empPersonaFk.perTercerApellido","empPersonaFk.perDui"});



                return catalogosRestClient.buscarEmpleados(femp);
            }
            
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    private void actualizarFiltro() {
        String expr = "#{controllerParam[actionParam](filtrosBienesBean.filtroBienes)}";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
    }
    
    public Boolean getRenderActivoFijo() {
        return renderActivoFijo;
    }

    public void setRenderActivoFijo(Boolean renderActivoFijo) {
        this.renderActivoFijo = renderActivoFijo;
    }

    public Boolean getRenderUnidadActivoFijo() {
        return renderUnidadActivoFijo;
    }

    public void setRenderUnidadActivoFijo(Boolean renderUnidadActivoFijo) {
        this.renderUnidadActivoFijo = renderUnidadActivoFijo;
    }

    public Boolean getRenderTipoBien() {
        return renderTipoBien;
    }

    public void setRenderTipoBien(Boolean renderTipoBien) {
        this.renderTipoBien = renderTipoBien;
    }

    public Boolean getRenderCategoria() {
        return renderCategoria;
    }

    public void setRenderCategoria(Boolean renderCategoria) {
        this.renderCategoria = renderCategoria;
    }

    public Boolean getRenderProyecto() {
        return renderProyecto;
    }

    public void setRenderProyecto(Boolean renderProyecto) {
        this.renderProyecto = renderProyecto;
    }

    public Boolean getRenderAsignadoA() {
        return renderAsignadoA;
    }

    public void setRenderAsignadoA(Boolean renderAsignadoA) {
        this.renderAsignadoA = renderAsignadoA;
    }

    public Boolean getRenderNumSerie() {
        return renderNumSerie;
    }

    public void setRenderNumSerie(Boolean renderNumSerie) {
        this.renderNumSerie = renderNumSerie;
    }

    public Boolean getRenderSeccion() {
        return renderSeccion;
    }

    public void setRenderSeccion(Boolean renderSeccion) {
        this.renderSeccion = renderSeccion;
    }

    public Boolean getRenderValorAdquisicion() {
        return renderValorAdquisicion;
    }

    public void setRenderValorAdquisicion(Boolean renderValorAdquisicion) {
        this.renderValorAdquisicion = renderValorAdquisicion;
    }

    public Boolean getRenderFechaAdquisicion() {
        return renderFechaAdquisicion;
    }

    public void setRenderFechaAdquisicion(Boolean renderFechaAdquisicion) {
        this.renderFechaAdquisicion = renderFechaAdquisicion;
    }

    public Boolean getRenderTipoUnidad() {
        return renderTipoUnidad;
    }

    public void setRenderTipoUnidad(Boolean renderTipoUnidad) {
        this.renderTipoUnidad = renderTipoUnidad;
    }

    public Boolean getRenderUnidadAdministrativa() {
        return renderUnidadAdministrativa;
    }

    public void setRenderUnidadAdministrativa(Boolean renderUnidadAdministrativa) {
        this.renderUnidadAdministrativa = renderUnidadAdministrativa;
    }

    public Boolean getRenderCalidad() {
        return renderCalidad;
    }

    public void setRenderCalidad(Boolean renderCalidad) {
        this.renderCalidad = renderCalidad;
    }

    public Boolean getRenderFuente() {
        return renderFuente;
    }

    public void setRenderFuente(Boolean renderFuente) {
        this.renderFuente = renderFuente;
    }

    public Boolean getRenderEstado() {
        return renderEstado;
    }

    public void setRenderEstado(Boolean renderEstado) {
        this.renderEstado = renderEstado;
    }

    public Boolean getRenderMarca() {
        return renderMarca;
    }

    public void setRenderMarca(Boolean renderMarca) {
        this.renderMarca = renderMarca;
    }

    public Boolean getRenderCodInventario() {
        return renderCodInventario;
    }

    public void setRenderCodInventario(Boolean renderCodInventario) {
        this.renderCodInventario = renderCodInventario;
    }

    public Boolean getRenderModelo() {
        return renderModelo;
    }

    public void setRenderModelo(Boolean renderModelo) {
        this.renderModelo = renderModelo;
    }

    public Boolean getRenderFechaCreacion() {
        return renderFechaCreacion;
    }

    public void setRenderFechaCreacion(Boolean renderFechaCreacion) {
        this.renderFechaCreacion = renderFechaCreacion;
    }

    public Boolean getRenderActivo() {
        return renderActivo;
    }

    public void setRenderActivo(Boolean renderActivo) {
        this.renderActivo = renderActivo;
    }

    public Boolean getRenderFechaSolicitud() {
        return renderFechaSolicitud;
    }

    public void setRenderFechaSolicitud(Boolean renderFechaSolicitud) {
        this.renderFechaSolicitud = renderFechaSolicitud;
    }

    public Boolean getRenderNumeroSolicitud() {
        return renderNumeroSolicitud;
    }

    public void setRenderNumeroSolicitud(Boolean renderNumeroSolicitud) {
        this.renderNumeroSolicitud = renderNumeroSolicitud;
    }

    public Boolean getRenderEstadoSolicitud() {
        return renderEstadoSolicitud;
    }

    public void setRenderEstadoSolicitud(Boolean renderEstadoSolicitud) {
        this.renderEstadoSolicitud = renderEstadoSolicitud;
    }

    public Boolean getRenderMotor() {
        return renderMotor;
    }

    public void setRenderMotor(Boolean renderMotor) {
        this.renderMotor = renderMotor;
    }

    public Boolean getRenderPlaca() {
        return renderPlaca;
    }

    public void setRenderPlaca(Boolean renderPlaca) {
        this.renderPlaca = renderPlaca;
    }

    public Boolean getRenderChasis() {
        return renderChasis;
    }

    public void setRenderChasis(Boolean renderChasis) {
        this.renderChasis = renderChasis;
    } 

    public Boolean getRenderEstadoDescargo() {
        return renderEstadoDescargo;
    }

    public void setRenderEstadoDescargo(Boolean renderEstadoDescargo) {
        this.renderEstadoDescargo = renderEstadoDescargo;
    }

    public Boolean getRenderNumeroActa() {
        return renderNumeroActa;
    }

    public void setRenderNumeroActa(Boolean renderNumeroActa) {
        this.renderNumeroActa = renderNumeroActa;
    }

    public Boolean getRenderFechaModificacion() {
        return renderFechaModificacion;
    }

    public void setRenderFechaModificacion(Boolean renderFechaModificacion) {
        this.renderFechaModificacion = renderFechaModificacion;
    }

    public Boolean getRenderMunicipio() {
        return renderMunicipio;
    }

    public void setRenderMunicipio(Boolean renderMunicipio) {
        this.renderMunicipio = renderMunicipio;
    }

    public Boolean getRenderFormaAdquisicion() {
        return renderFormaAdquisicion;
    }

    public void setRenderFormaAdquisicion(Boolean renderFormaAdquisicion) {
        this.renderFormaAdquisicion = renderFormaAdquisicion;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijo() {
        return comboUnidadesActivoFijo;
    }

    public void setComboUnidadesActivoFijo(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo) {
        this.comboUnidadesActivoFijo = comboUnidadesActivoFijo;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public SofisComboG<SgAfEstadosBienes> getComboEstadosBienes() {
        return comboEstadosBienes;
    }

    public void setComboEstadosBienes(SofisComboG<SgAfEstadosBienes> comboEstadosBienes) {
        this.comboEstadosBienes = comboEstadosBienes;
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

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Boolean getEsUnidadAdministrativa() {
        return esUnidadAdministrativa;
    }

    public void setEsUnidadAdministrativa(Boolean esUnidadAdministrativa) {
        this.esUnidadAdministrativa = esUnidadAdministrativa;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
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

    public String getTipoPantalla() {
        return tipoPantalla;
    }

    public void setTipoPantalla(String tipoPantalla) {
        this.tipoPantalla = tipoPantalla;
    }

    public UnidadesActivoFijoRestClient getUnidadesActivoFijoRestClient() {
        return unidadesActivoFijoRestClient;
    }

    public void setUnidadesActivoFijoRestClient(UnidadesActivoFijoRestClient unidadesActivoFijoRestClient) {
        this.unidadesActivoFijoRestClient = unidadesActivoFijoRestClient;
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

    public SofisComboG<SgAfEstadosDescargo> getComboDescargos() {
        return comboDescargos;
    }

    public void setComboDescargos(SofisComboG<SgAfEstadosDescargo> comboDescargos) {
        this.comboDescargos = comboDescargos;
    }

    public Boolean getRenderFechaDescargo() {
        return renderFechaDescargo;
    }

    public void setRenderFechaDescargo(Boolean renderFechaDescargo) {
        this.renderFechaDescargo = renderFechaDescargo;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public UnidadesAdministrativasRestClient getUnidadesAdministrativasRestClient() {
        return unidadesAdministrativasRestClient;
    }

    public void setUnidadesAdministrativasRestClient(UnidadesAdministrativasRestClient unidadesAdministrativasRestClient) {
        this.unidadesAdministrativasRestClient = unidadesAdministrativasRestClient;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public CatalogosRestClient getCatalogosRestClient() {
        return catalogosRestClient;
    }

    public void setCatalogosRestClient(CatalogosRestClient catalogosRestClient) {
        this.catalogosRestClient = catalogosRestClient;
    }

    public ApplicationBean getApplicationBean() {
        return applicationBean;
    }

    public void setApplicationBean(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
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

    public Boolean getMostrarComboEmpleados() {
        return mostrarComboEmpleados;
    }

    public void setMostrarComboEmpleados(Boolean mostrarComboEmpleados) {
        this.mostrarComboEmpleados = mostrarComboEmpleados;
    }

    public SgAfEmpleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(SgAfEmpleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
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

    public Boolean getRenderValorAdquisicionDesde() {
        return renderValorAdquisicionDesde;
    }

    public void setRenderValorAdquisicionDesde(Boolean renderValorAdquisicionDesde) {
        this.renderValorAdquisicionDesde = renderValorAdquisicionDesde;
    }

    public Boolean getRenderValorAdquisicionHasta() {
        return renderValorAdquisicionHasta;
    }

    public void setRenderValorAdquisicionHasta(Boolean renderValorAdquisicionHasta) {
        this.renderValorAdquisicionHasta = renderValorAdquisicionHasta;
    }

    public List<SgAfUnidadesActivoFijo> getListaUnidadesAF() {
        return listaUnidadesAF;
    }

    public void setListaUnidadesAF(List<SgAfUnidadesActivoFijo> listaUnidadesAF) {
        this.listaUnidadesAF = listaUnidadesAF;
    }

    public String getElementosActualizar() {
        return elementosActualizar;
    }

    public void setElementosActualizar(String elementosActualizar) {
        this.elementosActualizar = elementosActualizar;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public Boolean getCargarUADefecto() {
        return cargarUADefecto;
    }

    public void setCargarUADefecto(Boolean cargarUADefecto) {
        this.cargarUADefecto = cargarUADefecto;
    }
}
