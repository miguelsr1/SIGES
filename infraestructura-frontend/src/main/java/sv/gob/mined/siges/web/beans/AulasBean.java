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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyAulaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
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
public class AulasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AulasBean.class.getName());

    @Inject
    private AulaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;

    private FiltroAula filtro = new FiltroAula();
    private SgAula entidadEnEdicion = new SgAula();
    private List<RevHistorico> historialAula = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyAulaDataModel aulaLazyModel;

    private SgSede sedeSeleccionada;
    private Boolean esSedePorDefecto;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();
    private SgAfUnidadesAdministrativas unidadAdministrativaSeleccionada;

    public AulasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (sessionBean.getSedeDefecto() != null) {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_AULA)&& !sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_AULAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setDepartamentoId(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipioId(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setSedeId(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            totalResultados = restClient.buscarTotal(filtro);
            aulaLazyModel = new LazyAulaDataModel(restClient, filtro, totalResultados, paginado);
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

    public void seleccionarSede() {
        try {
            if (sedeSeleccionada != null) {
                filtro.setSedeId(sedeSeleccionada.getSedPk());
            } else {
                filtro.setSedeId(null);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

      public void limpiar() {
        filtro = new FiltroAula();
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        unidadAdministrativaSeleccionada=null;
        sedeSeleccionada = null;
        if(sessionBean.getSedeDefecto() != null) {
            filtro.setSedeId(sessionBean.getSedeDefecto().getSedPk());
            buscar();
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
  
    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getAulPk());
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
            historialAula = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgAula var) {
        try{
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getAulPk()); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgAula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialAula() {
        return historialAula;
    }

    public void setHistorialAula(List<RevHistorico> historialAula) {
        this.historialAula = historialAula;
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

    public LazyAulaDataModel getAulaLazyModel() {
        return aulaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyAulaDataModel aulaLazyModel) {
        this.aulaLazyModel = aulaLazyModel;
    }

    public FiltroAula getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAula filtro) {
        this.filtro = filtro;
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
