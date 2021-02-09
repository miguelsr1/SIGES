/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoInmueble;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumInmueblePertenece;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroInmueblesSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyInmueblesSedesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InmueblesSedesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InmueblesSedesBean.class.getName());

    @Inject
    private InmueblesSedesRestClient restClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    private FiltroInmueblesSedes filtro = new FiltroInmueblesSedes();
    private List<RevHistorico> historialInmueble = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyInmueblesSedesDataModel inmueblesLazyModel;
    private SgInmueblesSedes entidadEnEdicion;
    private SgSede sedeSeleccionada;
    private Boolean esSedePorDefecto;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();
    private SofisComboG<SgEstadoInmueble> comboEstadoInmueble;
    private SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada;
    private EnumInmueblePertenece[] enumeradoPertenece=EnumInmueblePertenece.values();
    
    public InmueblesSedesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if(sessionBean.getSedeDefecto() != null) {
                esSedePorDefecto = Boolean.TRUE;
                sedeSeleccionada = sessionBean.getSedeDefecto();
                filtro.setSedeDeUnidadRespYOtrasSedes(sedeSeleccionada.getSedPk());
                buscar();
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO) && !sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_INMUEBLES_O_TERRENOS) ) {
            JSFUtils.redirectToIndex();
        }
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }
    
    public void actualizar(SgInmueblesSedes var) {
        try{
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getTisPk()); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscar() {
        try {
            
            filtro.setFirst(new Long(0));
            filtro.setDepartamentoId(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipioId(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setEstadoInmueble(comboEstadoInmueble.getSelectedT() != null ? comboEstadoInmueble.getSelectedT().getEinPk(): null);
            totalResultados = restClient.buscarTotal(filtro);
            inmueblesLazyModel = new LazyInmueblesSedesDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarPerteneceA(){
        sedeSeleccionada=null;
        unidadAdministrativaSeleccionada=null;
    }
    
    public void cargarCombos() {
        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = catalogosRestClient.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));


            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"einNombre"});
            fc.setIncluirCampos(new String[]{"einNombre", "einVersion"});
            List<SgEstadoInmueble> estados=catalogosRestClient.buscarEstadoInmueble(fc);
            comboEstadoInmueble= new SofisComboG(estados, "einNombre");
            comboEstadoInmueble.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarDepartamento() {
        try {
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                if (!munCache.containsKey(comboDepartamento.getSelectedT())) {
                    munCache.put(this.comboDepartamento.getSelectedT(), catalogosRestClient.buscarMunicipio(filtro));
                }
                comboMunicipio = new SofisComboG(munCache.get(this.comboDepartamento.getSelectedT()), "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void seleccionarSede() {
        try {
            if(sedeSeleccionada != null) {
                filtro.setSedeDeUnidadRespYOtrasSedes(sedeSeleccionada.getSedPk());
            } else {
                filtro.setSedeDeUnidadRespYOtrasSedes(null);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarUnidadAdministrativa() {
        try {
            if(unidadAdministrativaSeleccionada != null) {
                filtro.setUnidadAdministrativaDeUnidadRespYOtrasUnidades(unidadAdministrativaSeleccionada.getUadPk());
            } else {
                filtro.setUnidadAdministrativaDeUnidadRespYOtrasUnidades(null);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiar() {
        filtro = new FiltroInmueblesSedes();
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        sedeSeleccionada = null;
        inmueblesLazyModel = null;
        unidadAdministrativaSeleccionada=null;
        if(sessionBean.getSedeDefecto() != null) {
            filtro.setSedeDeUnidadRespYOtrasSedes(sessionBean.getSedeDefecto().getSedPk());
            buscar();
        }
        comboEstadoInmueble.setSelected(-1);
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTisPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public void historial(Long id) {
        try {
            historialInmueble = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroInmueblesSedes getFiltro() {
        return filtro;
    }

    public List<RevHistorico> getHistorialInmueble() {
        return historialInmueble;
    }

    public void setHistorialInmueble(List<RevHistorico> historialInmueble) {
        this.historialInmueble = historialInmueble;
    }

    public SgAfUnidadesAdministrativas getUnidadAdministrativaSeleccionada() {
        return unidadAdministrativaSeleccionada;
    }

    public void setUnidadAdministrativaSeleccionada(SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada) {
        this.unidadAdministrativaSeleccionada = unidadAdministrativaSeleccionada;
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

    public LazyInmueblesSedesDataModel getInmueblesLazyModel() {
        return inmueblesLazyModel;
    }

    public void setInmueblesLazyModel(LazyInmueblesSedesDataModel inmueblesLazyModel) {
        this.inmueblesLazyModel = inmueblesLazyModel;
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

    public Boolean getEsSedePorDefecto() {
        return esSedePorDefecto;
    }

    public void setEsSedePorDefecto(Boolean esSedePorDefecto) {
        this.esSedePorDefecto = esSedePorDefecto;
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

    public HashMap<SgDepartamento, List<SgMunicipio>> getMunCache() {
        return munCache;
    }

    public void setMunCache(HashMap<SgDepartamento, List<SgMunicipio>> munCache) {
        this.munCache = munCache;
    }

    public SofisComboG<SgEstadoInmueble> getComboEstadoInmueble() {
        return comboEstadoInmueble;
    }

    public void setComboEstadoInmueble(SofisComboG<SgEstadoInmueble> comboEstadoInmueble) {
        this.comboEstadoInmueble = comboEstadoInmueble;
    }

    public EnumInmueblePertenece[] getEnumeradoPertenece() {
        return enumeradoPertenece;
    }

    public void setEnumeradoPertenece(EnumInmueblePertenece[] enumeradoPertenece) {
        this.enumeradoPertenece = enumeradoPertenece;
    }
    
}