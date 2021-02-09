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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyEdificioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
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
public class EdificiosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EdificiosBean.class.getName());

    @Inject
    private EdificioRestClient restClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;

    private FiltroEdificio filtro = new FiltroEdificio();
    private SgEdificio entidadEnEdicion = new SgEdificio();
    private List<RevHistorico> historialEdificio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEdificioDataModel edificioLazyModel;
    private SgSede sedeSeleccionada;
    private Boolean esSedePorDefecto;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();
    private SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada;
    

    public EdificiosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if(sessionBean.getSedeDefecto() != null) {
                esSedePorDefecto = Boolean.TRUE;
                sedeSeleccionada = sessionBean.getSedeDefecto();
                filtro.setSedeId(sedeSeleccionada.getSedPk());
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_EDIFICIO)&& !sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_EDIFICIOS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setDepartamentoId(comboDepartamento.getSelectedT()!=null?comboDepartamento.getSelectedT().getDepPk():null);
            filtro.setMunicipioId(comboMunicipio.getSelectedT()!=null?comboMunicipio.getSelectedT().getMunPk():null);
            filtro.setSedeId(sedeSeleccionada!=null?sedeSeleccionada.getSedPk():null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            edificioLazyModel = new LazyEdificioDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
    
    public void seleccionarUnidadAdministrativa() {
        try {
            if(unidadAdministrativaSeleccionada != null) {
                filtro.setUnidadAdministrativa(unidadAdministrativaSeleccionada.getUadPk());
            } else {
                filtro.setUnidadAdministrativa(null);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizar(SgEdificio var) {
        try{
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getEdiPk()); 
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
    
    public void seleccionarSede() {
        try {
            if(sedeSeleccionada != null) {
                filtro.setSedeId(sedeSeleccionada.getSedPk());
            } else {
                filtro.setSedeId(null);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiar() {
        filtro = new FiltroEdificio();
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        sedeSeleccionada = null;
        unidadAdministrativaSeleccionada=null;
        if(sessionBean.getSedeDefecto() != null) {
            filtro.setSedeId(sessionBean.getSedeDefecto().getSedPk());
            buscar();
        }
    }




    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEdiPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialEdificio = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroEdificio getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEdificio filtro) {
        this.filtro = filtro;
    }

    public SgEdificio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEdificio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialEdificio() {
        return historialEdificio;
    }

    public void setHistorialEdificio(List<RevHistorico> historialEdificio) {
        this.historialEdificio = historialEdificio;
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

    public LazyEdificioDataModel getEdificioLazyModel() {
        return edificioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEdificioDataModel edificioLazyModel) {
        this.edificioLazyModel = edificioLazyModel;
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

    public SgAfUnidadesAdministrativas getUnidadAdministrativaSeleccionada() {
        return unidadAdministrativaSeleccionada;
    }

    public void setUnidadAdministrativaSeleccionada(SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada) {
        this.unidadAdministrativaSeleccionada = unidadAdministrativaSeleccionada;
    }

}
