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
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAula;
import sv.gob.mined.siges.web.lazymodels.LazyAulaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EdificioAulasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EdificioAulasBean.class.getName());

    @Inject
    private AulaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroAula filtro = new FiltroAula();
    private SgEdificio entidadEnEdicion = new SgEdificio();
    private List<RevHistorico> historialAula = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados=0L;
    private LazyAulaDataModel aulaLazyModel;
    private Boolean soloLectura = Boolean.FALSE;

    private SgSede sedeSeleccionada;
    private Boolean esSedePorDefecto;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();

    public EdificioAulasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgEdificio) request.getAttribute("edificio");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
             buscar();
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
            if(entidadEnEdicion.getEdiPk()!=null){
                filtro.setEdificioPk(entidadEnEdicion.getEdiPk());
                totalResultados = restClient.buscarTotal(filtro);
                aulaLazyModel = new LazyAulaDataModel(restClient, filtro, totalResultados, paginado);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }





      public void limpiar() {
        filtro = new FiltroAula();
       
    }
  
  

    public void historial(Long id) {
        try {
            historialAula = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgEdificio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEdificio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }



    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
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

}
