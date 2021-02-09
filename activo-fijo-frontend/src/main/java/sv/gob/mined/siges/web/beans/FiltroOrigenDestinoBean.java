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
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class FiltroOrigenDestinoBean  implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FiltroOrigenDestinoBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;
     
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private ApplicationBean applicationBean;
    
    private SgAfUnidadesAdministrativas unidadOrigenSeleccionada;
    private SgSede sedeOrigenSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFOrigenSeleccionada;
    private TipoUnidad tipoUnidadOrigenSeleccionada;
    
    private SgAfUnidadesAdministrativas unidadDestinoSeleccionada;
    private SgSede sedeDestinoSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFDestinoSeleccionada;
    private TipoUnidad tipoUnidadDestinoSeleccionada;
    
    private FiltroCodiguera filtro;
    private SofisComboG<SgSede> comboSedesOrigen;
    private SofisComboG comboUnidadAdministrativaOrigen;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoOrigen;
    private SofisComboG<TipoUnidad> comboTiposUnidadOrigen;
    private SofisComboG<SgMunicipio> comboMunicipiosOrigen;
    
    private SofisComboG<SgSede> comboSedesDestino;
    private SofisComboG comboUnidadAdministrativaDestino;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoDestino;
    private SofisComboG<TipoUnidad> comboTiposUnidadDestino;
    private SofisComboG<SgMunicipio> comboMunicipiosDestino;
    
    private Boolean esUnidadAdministrativaOrigen;
    private Boolean esUnidadAdministrativaDestino;
    private Boolean soloLectura = Boolean.FALSE;
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF = new ArrayList();;
    private FiltroBienesDepreciables filtroBienesDepreciables;
    private Boolean cargarDefault = Boolean.FALSE;
    
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativasOrigen = new ArrayList();
    private List<SgSede> sedesOrigen = new ArrayList();
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativasDestino = new ArrayList();
    private List<SgSede> sedesDestino = new ArrayList();
    public FiltroOrigenDestinoBean() {
    }
    
    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            filtroBienesDepreciables = (FiltroBienesDepreciables) request.getAttribute("filtroBienesDepreciables");
            if (filtroBienesDepreciables == null) {
                filtroBienesDepreciables = new FiltroBienesDepreciables();
            }
            
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
            
             List<TipoUnidad> tipoUnidad = new ArrayList();
            if(sessionBean.getUnidadADDefecto() != null) {
                tipoUnidad.add(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            } else if(sessionBean.getSedeDefecto() != null) {
                tipoUnidad.add(TipoUnidad.CENTRO_ESCOLAR);
                comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            } else {
                tipoUnidad = new ArrayList(Arrays.asList(TipoUnidad.values()));
                comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
                comboTiposUnidadOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            
            comboTiposUnidadOrigen.ordenar();
            
            List<TipoUnidad> tipoUnidadDestino = new ArrayList(Arrays.asList(TipoUnidad.values()));
            comboTiposUnidadDestino = new SofisComboG(tipoUnidadDestino, "text");
            comboTiposUnidadDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTiposUnidadDestino.ordenar();
            
            
            FiltroUnidadesActivoFijo fua = new FiltroUnidadesActivoFijo();
            fua.setHabilitado(Boolean.TRUE);
            fua.setOrderBy(new String[]{"uafCodigo"});
            fua.setAscending(new boolean[]{true});
            fua.setIncluirCampos(new String[]{"uafCodigo","uafNombre", "uafDepartamento","uafVersion"});
            listaUnidadesAF = unidadesActivoFijoRestClient.buscar(fua);

            comboUnidadesActivoFijoOrigen = new SofisComboG(listaUnidadesAF, "codigoNombre");
            comboUnidadesActivoFijoOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            comboUnidadesActivoFijoDestino= new SofisComboG();
            comboUnidadesActivoFijoDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if(cargarDefault != null && cargarDefault) {
                cargarUnidadesSinPermiso();
            }
            
            comboMunicipiosOrigen = new SofisComboG();
            comboMunicipiosOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboMunicipiosDestino = new SofisComboG();
            comboMunicipiosDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboUnidadAdministrativaOrigen = new SofisComboG();
            comboUnidadAdministrativaOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboUnidadAdministrativaDestino = new SofisComboG();
            comboUnidadAdministrativaDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboSedesOrigen = new SofisComboG();
            comboSedesOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboSedesDestino= new SofisComboG();
            comboSedesDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarUnidadesSinPermiso() {
        try {
            FiltroUnidadesActivoFijo fua = new FiltroUnidadesActivoFijo();
            fua.setHabilitado(Boolean.TRUE);
            fua.setOrderBy(new String[]{"uafCodigo"});
            fua.setAscending(new boolean[]{true});
            fua.setIncluirCampos(new String[]{"uafCodigo","uafNombre", "uafDepartamento","uafVersion"});

            List<SgAfUnidadesActivoFijo> unidadesAF = unidadesActivoFijoRestClient.buscarSinPermisos(fua);

            comboUnidadesActivoFijoDestino= new SofisComboG(unidadesAF, "codigoNombre");
            comboUnidadesActivoFijoDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }    
    }
    public void cargarEntidad() {
        try {
            sedeOrigenSeleccionada = null;
            unidadOrigenSeleccionada = null;
            cargarSedePorDefecto();
            cargarUADPorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
   
    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            this.esUnidadAdministrativaOrigen = Boolean.FALSE;
            
            sedeOrigenSeleccionada = sessionBean.getSedeDefecto();

            tipoUnidadOrigenSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadOrigenSeleccionada);
            
            comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
            
            if(sedeOrigenSeleccionada!= null) {
                if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                    for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                        if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeOrigenSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            unidadAFOrigenSeleccionada = unidadesAF;
                        }
                    }
                } 

            }

            comboUnidadesActivoFijoOrigen.setSelectedT(unidadAFOrigenSeleccionada);
                    
            seleccionarUnidadActivoFijo(0);
        }
    }
    public void cargarUADPorDefecto() throws Exception {
        if (sessionBean.getUnidadADDefecto() != null) {
            this.esUnidadAdministrativaOrigen = Boolean.TRUE;
            unidadOrigenSeleccionada = sessionBean.getUnidadADDefecto();
            tipoUnidadOrigenSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadOrigenSeleccionada);
            
            comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
            
            if(unidadOrigenSeleccionada != null) {
                unidadAFOrigenSeleccionada = unidadOrigenSeleccionada.getUadUnidadActivoFijoFk();
            } 

            comboUnidadesActivoFijoOrigen.setSelectedT(unidadAFOrigenSeleccionada);
            
            seleccionarUnidadActivoFijo(0);
        }
    }
    
    public void seleccionarTipoUnidadAF(Integer opcion) {
        if(opcion != null) {
            if(Constantes.OPCION_CERO.equals(opcion)) {
                this.esUnidadAdministrativaOrigen = null;
                tipoUnidadOrigenSeleccionada = comboTiposUnidadOrigen.getSelectedT();
                 if (tipoUnidadOrigenSeleccionada != null) {
                    switch (tipoUnidadOrigenSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            this.esUnidadAdministrativaOrigen = Boolean.TRUE;
                            break;
                        case CENTRO_ESCOLAR:
                            this.esUnidadAdministrativaOrigen = Boolean.FALSE;
                            break;
                        default:
                            break;
                    }
                }
            } else if(Constantes.OPCION_UNO.equals(opcion)) {
                this.esUnidadAdministrativaDestino = null;
                tipoUnidadDestinoSeleccionada = comboTiposUnidadDestino.getSelectedT();
                 if (tipoUnidadDestinoSeleccionada != null) {
                    switch (tipoUnidadDestinoSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            this.esUnidadAdministrativaDestino = Boolean.TRUE;
                            break;
                        case CENTRO_ESCOLAR:
                            this.esUnidadAdministrativaDestino = Boolean.FALSE;
                            break;
                        default:
                            break;
                    }
                }
            }
            seleccionarUnidadActivoFijo(opcion);
        }
    }
    
    public void seleccionarUnidadActivoFijo(Integer opcion) {
        try {
            if(opcion != null) {
                if(Constantes.OPCION_CERO.equals(opcion)) {
                    comboMunicipiosOrigen = new SofisComboG();
                    comboMunicipiosOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    
                    unidadesAdministrativasOrigen = new ArrayList();
                    sedesOrigen = new ArrayList();
                   
                    unidadAFOrigenSeleccionada = comboUnidadesActivoFijoOrigen.getSelectedT();
                    
                    if(tipoUnidadOrigenSeleccionada == null) {
                        tipoUnidadOrigenSeleccionada = comboTiposUnidadOrigen.getSelectedT();
                        if (tipoUnidadOrigenSeleccionada != null) {
                            switch (tipoUnidadOrigenSeleccionada) {
                                case UNIDAD_ADMINISTRATIVA:
                                    this.esUnidadAdministrativaOrigen = Boolean.TRUE;
                                    break;
                                case CENTRO_ESCOLAR:
                                    this.esUnidadAdministrativaOrigen = Boolean.FALSE;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
              
                    if(unidadAFOrigenSeleccionada != null && this.esUnidadAdministrativaOrigen != null) {
                        if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadOrigenSeleccionada)) {
                            FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                            fuad.setOrderBy(new String[]{"uadCodigo"});
                            fuad.setAscending(new boolean[]{true});
                            fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                            fuad.setUnidadActivoFijoId(unidadAFOrigenSeleccionada != null ? unidadAFOrigenSeleccionada.getUafPk() : null);
                           
                            unidadesAdministrativasOrigen = unidadesAdministrativasRestClient.buscar(fuad);
                            
                        } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadOrigenSeleccionada)) {
                            if(unidadAFOrigenSeleccionada.getUafPk()!= null 
                                        && unidadAFOrigenSeleccionada.getUafDepartamento() != null
                                        && !unidadAFOrigenSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                FiltroMunicipio fm = new FiltroMunicipio();
                                fm.setMunHabilitado(Boolean.TRUE);
                                fm.setMunDepartamentoFk(unidadAFOrigenSeleccionada != null ? unidadAFOrigenSeleccionada.getUafDepartamento().getDepPk() : null);
                                fm.setAscending(new boolean[]{true});
                                fm.setOrderBy(new String[]{"munNombre"});
                                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});

                                List<SgMunicipio> municipios = catalogosRestClient.buscarMunicipio(fm);
                                comboMunicipiosOrigen = new SofisComboG(municipios, "munNombre");
                                comboMunicipiosOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                                if(sedeOrigenSeleccionada != null && sedeOrigenSeleccionada.getSedDireccion().getDirMunicipio() != null) {
                                    comboMunicipiosOrigen.setSelectedT(sedeOrigenSeleccionada.getSedDireccion().getDirMunicipio()); 
                                }
                                
                                seleccionarMunicipio(0);
                            }
                       }
                   } 
                } else if(Constantes.OPCION_UNO.equals(opcion)) {
                    comboMunicipiosDestino = new SofisComboG();
                    comboMunicipiosDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    
                    unidadesAdministrativasDestino = new ArrayList();
                    sedesDestino = new ArrayList();
                    
                    unidadAFDestinoSeleccionada = comboUnidadesActivoFijoDestino.getSelectedT();

                    if(tipoUnidadDestinoSeleccionada == null) {
                        tipoUnidadDestinoSeleccionada = comboTiposUnidadDestino.getSelectedT();
                        if (tipoUnidadDestinoSeleccionada != null) {
                            switch (tipoUnidadDestinoSeleccionada) {
                                case UNIDAD_ADMINISTRATIVA:
                                    this.esUnidadAdministrativaDestino = Boolean.TRUE;
                                    break;
                                case CENTRO_ESCOLAR:
                                    this.esUnidadAdministrativaDestino = Boolean.FALSE;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    
                    if(unidadAFDestinoSeleccionada != null && this.esUnidadAdministrativaDestino != null) {
                        if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadDestinoSeleccionada)) {
                            FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                            fuad.setOrderBy(new String[]{"uadCodigo"});
                            fuad.setAscending(new boolean[]{true});
                            fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                            fuad.setUnidadActivoFijoId(unidadAFDestinoSeleccionada != null ? unidadAFDestinoSeleccionada.getUafPk() : null);
                            unidadesAdministrativasDestino = unidadesAdministrativasRestClient.buscarSinPermisos(fuad);
                           
                        } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadDestinoSeleccionada)) {
                            if(unidadAFDestinoSeleccionada.getUafPk()!= null 
                                        && unidadAFDestinoSeleccionada.getUafDepartamento() != null
                                        && !unidadAFDestinoSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                FiltroMunicipio fm = new FiltroMunicipio();
                                fm.setMunHabilitado(Boolean.TRUE);
                                fm.setMunDepartamentoFk(unidadAFDestinoSeleccionada.getUafDepartamento().getDepPk());
                                fm.setAscending(new boolean[]{true});
                                fm.setOrderBy(new String[]{"munNombre"});
                                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});

                                List<SgMunicipio> municipios = catalogosRestClient.buscarMunicipio(fm);
                                comboMunicipiosDestino = new SofisComboG(municipios, "munNombre");
                                comboMunicipiosDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                                
                                if(sedeDestinoSeleccionada != null && sedeDestinoSeleccionada.getSedDireccion().getDirMunicipio() != null) {
                                    comboMunicipiosDestino.setSelectedT(sedeDestinoSeleccionada.getSedDireccion().getDirMunicipio()); 
                                }
                                
                                seleccionarMunicipio(1);
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
    
    public void seleccionarMunicipio(Integer opcion) {
        try {
            if(opcion != null) {
                FiltroSedes fil = new FiltroSedes();
                fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                fil.setOrderBy(new String[]{"sedCodigo"});
                fil.setAscending(new boolean[]{true});
                fil.setIncluirCampos(new String[]{"sedCodigo","sedTipo", "sedNombre", "sedVersion"});   
                
                if(Constantes.OPCION_CERO.equals(opcion)) {
                    if(comboUnidadesActivoFijoOrigen != null && comboUnidadesActivoFijoOrigen.getSelectedT() != null && comboUnidadesActivoFijoOrigen.getSelectedT() != null
                            && comboUnidadesActivoFijoOrigen.getSelectedT().getUafDepartamento() != null) {  
                        fil.setSedMunicipioId(comboMunicipiosOrigen != null && comboMunicipiosOrigen.getSelectedT() != null? comboMunicipiosOrigen.getSelectedT().getMunPk() : null);
                        fil.setSedDepartamentoId(comboUnidadesActivoFijoOrigen.getSelectedT().getUafDepartamento().getDepPk());
                        fil.setSedTipos(applicationBean.getTiposSedes());
                        fil.setPriSubvencionada(Boolean.TRUE);
                        sedesOrigen= sedeRestClient.buscar(fil);
                        
                   } else {
                        sedesOrigen = new ArrayList();
                   }
                } else if(Constantes.OPCION_UNO.equals(opcion)) {
                    if(comboUnidadesActivoFijoDestino != null && comboUnidadesActivoFijoDestino.getSelectedT() != null && comboUnidadesActivoFijoDestino.getSelectedT() != null
                            && comboUnidadesActivoFijoDestino.getSelectedT().getUafDepartamento() != null) {  
                        fil.setSedMunicipioId(comboMunicipiosDestino != null && comboMunicipiosDestino.getSelectedT() != null? comboMunicipiosDestino.getSelectedT().getMunPk() : null);
                        fil.setSedDepartamentoId(comboUnidadesActivoFijoDestino.getSelectedT().getUafDepartamento().getDepPk());
                        fil.setSedTipos(applicationBean.getTiposSedes());
                        fil.setPriSubvencionada(Boolean.TRUE);
                        fil.setSecurityOperation(null);
                        sedesDestino= sedeRestClient.buscar(fil);
                        
                    } else {
                        sedesDestino = new ArrayList();
                    }
                }
            } else {
                sedesOrigen = new ArrayList();
                sedesDestino = new ArrayList();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    public void limpiar() {
        filtroBienesDepreciables = new FiltroBienesDepreciables();
        sedeOrigenSeleccionada = null;
        sedeDestinoSeleccionada = null;
        unidadOrigenSeleccionada = null;
        unidadDestinoSeleccionada = null;
        tipoUnidadOrigenSeleccionada = null;
        tipoUnidadDestinoSeleccionada = null;
        
        comboMunicipiosOrigen.setSelected(-1);
        comboMunicipiosDestino.setSelected(-1);
        
        comboTiposUnidadOrigen.setSelected(-1);
        comboTiposUnidadDestino.setSelected(-1);
        
        comboUnidadesActivoFijoOrigen.setSelected(-1);
        comboUnidadesActivoFijoDestino.setSelected(-1);
        
        comboUnidadAdministrativaOrigen.setSelected(-1);
        comboUnidadAdministrativaDestino.setSelected(-1);
        
        cargarEntidad();
    }
    
    public SgAfUnidadesAdministrativas getUnidadOrigenSeleccionada() {
        return unidadOrigenSeleccionada;
    }

    public void setUnidadOrigenSeleccionada(SgAfUnidadesAdministrativas unidadOrigenSeleccionada) {
        this.unidadOrigenSeleccionada = unidadOrigenSeleccionada;
    }

    public SgSede getSedeOrigenSeleccionada() {
        return sedeOrigenSeleccionada;
    }

    public void setSedeOrigenSeleccionada(SgSede sedeOrigenSeleccionada) {
        this.sedeOrigenSeleccionada = sedeOrigenSeleccionada;
    }

    public SgAfUnidadesActivoFijo getUnidadAFOrigenSeleccionada() {
        return unidadAFOrigenSeleccionada;
    }

    public void setUnidadAFOrigenSeleccionada(SgAfUnidadesActivoFijo unidadAFOrigenSeleccionada) {
        this.unidadAFOrigenSeleccionada = unidadAFOrigenSeleccionada;
    }

    public TipoUnidad getTipoUnidadOrigenSeleccionada() {
        return tipoUnidadOrigenSeleccionada;
    }

    public void setTipoUnidadOrigenSeleccionada(TipoUnidad tipoUnidadOrigenSeleccionada) {
        this.tipoUnidadOrigenSeleccionada = tipoUnidadOrigenSeleccionada;
    }

    public SgAfUnidadesAdministrativas getUnidadDestinoSeleccionada() {
        return unidadDestinoSeleccionada;
    }

    public void setUnidadDestinoSeleccionada(SgAfUnidadesAdministrativas unidadDestinoSeleccionada) {
        this.unidadDestinoSeleccionada = unidadDestinoSeleccionada;
    }

    public SgSede getSedeDestinoSeleccionada() {
        return sedeDestinoSeleccionada;
    }

    public void setSedeDestinoSeleccionada(SgSede sedeDestinoSeleccionada) {
        this.sedeDestinoSeleccionada = sedeDestinoSeleccionada;
    }

    public SgAfUnidadesActivoFijo getUnidadAFDestinoSeleccionada() {
        return unidadAFDestinoSeleccionada;
    }

    public void setUnidadAFDestinoSeleccionada(SgAfUnidadesActivoFijo unidadAFDestinoSeleccionada) {
        this.unidadAFDestinoSeleccionada = unidadAFDestinoSeleccionada;
    }

    public TipoUnidad getTipoUnidadDestinoSeleccionada() {
        return tipoUnidadDestinoSeleccionada;
    }

    public void setTipoUnidadDestinoSeleccionada(TipoUnidad tipoUnidadDestinoSeleccionada) {
        this.tipoUnidadDestinoSeleccionada = tipoUnidadDestinoSeleccionada;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgSede> getComboSedesOrigen() {
        return comboSedesOrigen;
    }

    public void setComboSedesOrigen(SofisComboG<SgSede> comboSedesOrigen) {
        this.comboSedesOrigen = comboSedesOrigen;
    }

    public SofisComboG getComboUnidadAdministrativaOrigen() {
        return comboUnidadAdministrativaOrigen;
    }

    public void setComboUnidadAdministrativaOrigen(SofisComboG comboUnidadAdministrativaOrigen) {
        this.comboUnidadAdministrativaOrigen = comboUnidadAdministrativaOrigen;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijoOrigen() {
        return comboUnidadesActivoFijoOrigen;
    }

    public void setComboUnidadesActivoFijoOrigen(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoOrigen) {
        this.comboUnidadesActivoFijoOrigen = comboUnidadesActivoFijoOrigen;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidadOrigen() {
        return comboTiposUnidadOrigen;
    }

    public void setComboTiposUnidadOrigen(SofisComboG<TipoUnidad> comboTiposUnidadOrigen) {
        this.comboTiposUnidadOrigen = comboTiposUnidadOrigen;
    }

    public SofisComboG<SgMunicipio> getComboMunicipiosOrigen() {
        return comboMunicipiosOrigen;
    }

    public void setComboMunicipiosOrigen(SofisComboG<SgMunicipio> comboMunicipiosOrigen) {
        this.comboMunicipiosOrigen = comboMunicipiosOrigen;
    }

    public SofisComboG<SgSede> getComboSedesDestino() {
        return comboSedesDestino;
    }

    public void setComboSedesDestino(SofisComboG<SgSede> comboSedesDestino) {
        this.comboSedesDestino = comboSedesDestino;
    }

    public SofisComboG getComboUnidadAdministrativaDestino() {
        return comboUnidadAdministrativaDestino;
    }

    public void setComboUnidadAdministrativaDestino(SofisComboG comboUnidadAdministrativaDestino) {
        this.comboUnidadAdministrativaDestino = comboUnidadAdministrativaDestino;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijoDestino() {
        return comboUnidadesActivoFijoDestino;
    }

    public void setComboUnidadesActivoFijoDestino(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoDestino) {
        this.comboUnidadesActivoFijoDestino = comboUnidadesActivoFijoDestino;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidadDestino() {
        return comboTiposUnidadDestino;
    }

    public void setComboTiposUnidadDestino(SofisComboG<TipoUnidad> comboTiposUnidadDestino) {
        this.comboTiposUnidadDestino = comboTiposUnidadDestino;
    }

    public SofisComboG<SgMunicipio> getComboMunicipiosDestino() {
        return comboMunicipiosDestino;
    }

    public void setComboMunicipiosDestino(SofisComboG<SgMunicipio> comboMunicipiosDestino) {
        this.comboMunicipiosDestino = comboMunicipiosDestino;
    }

    public Boolean getEsUnidadAdministrativaOrigen() {
        return esUnidadAdministrativaOrigen;
    }

    public void setEsUnidadAdministrativaOrigen(Boolean esUnidadAdministrativaOrigen) {
        this.esUnidadAdministrativaOrigen = esUnidadAdministrativaOrigen;
    }

    public Boolean getEsUnidadAdministrativaDestino() {
        return esUnidadAdministrativaDestino;
    }

    public void setEsUnidadAdministrativaDestino(Boolean esUnidadAdministrativaDestino) {
        this.esUnidadAdministrativaDestino = esUnidadAdministrativaDestino;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public FiltroBienesDepreciables getFiltroBienesDepreciables() {
        return filtroBienesDepreciables;
    }

    public void setFiltroBienesDepreciables(FiltroBienesDepreciables filtroBienesDepreciables) {
        this.filtroBienesDepreciables = filtroBienesDepreciables;
    }

    public List<SgAfUnidadesActivoFijo> getListaUnidadesAF() {
        return listaUnidadesAF;
    }

    public void setListaUnidadesAF(List<SgAfUnidadesActivoFijo> listaUnidadesAF) {
        this.listaUnidadesAF = listaUnidadesAF;
    }

    public Boolean getCargarDefault() {
        return cargarDefault;
    }

    public void setCargarDefault(Boolean cargarDefault) {
        this.cargarDefault = cargarDefault;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<SgAfUnidadesAdministrativas> getUnidadesAdministrativasOrigen() {
        return unidadesAdministrativasOrigen;
    }

    public void setUnidadesAdministrativasOrigen(List<SgAfUnidadesAdministrativas> unidadesAdministrativasOrigen) {
        this.unidadesAdministrativasOrigen = unidadesAdministrativasOrigen;
    }

    public List<SgSede> getSedesOrigen() {
        return sedesOrigen;
    }

    public void setSedesOrigen(List<SgSede> sedesOrigen) {
        this.sedesOrigen = sedesOrigen;
    }

    public List<SgAfUnidadesAdministrativas> getUnidadesAdministrativasDestino() {
        return unidadesAdministrativasDestino;
    }

    public void setUnidadesAdministrativasDestino(List<SgAfUnidadesAdministrativas> unidadesAdministrativasDestino) {
        this.unidadesAdministrativasDestino = unidadesAdministrativasDestino;
    }

    public List<SgSede> getSedesDestino() {
        return sedesDestino;
    }

    public void setSedesDestino(List<SgSede> sedesDestino) {
        this.sedesDestino = sedesDestino;
    }
    
    
}
