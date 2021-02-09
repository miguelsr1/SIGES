/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgRhPagina;
import sv.gob.mined.siges.web.dto.centroseducativos.SgNivel;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPagina;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.lazymodels.LazyPaginaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PaginaRestClient;

import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class PaginasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PaginasBean.class.getName());

    @Inject
    private PaginaRestClient restClient;
    
    @Inject
    private NivelRestClient nivelClient;    
        
    private FiltroPagina filtro = new FiltroPagina();
    private SgRhPagina entidadEnEdicion = new SgRhPagina();
    private List<RevHistorico> historialPagina = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPaginaDataModel paginaLazyModel;
    private SofisComboG<SgNivel> comboNivel;

    public PaginasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscar() {
        try {
            if(comboNivel.getSelectedT() != null){
                filtro.setPagNivelPk(comboNivel.getSelectedT().getNivPk());
            }
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            paginaLazyModel = new LazyPaginaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{            
            FiltroNivel filtroNivel = new FiltroNivel();
            filtroNivel.setOrderBy(new String[]{"nivNombre"});
            filtroNivel.setAscending(new boolean[]{true});
            filtroNivel.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
            List<SgNivel> listaNivel = nivelClient.buscar(filtroNivel);
            comboNivel = new SofisComboG(listaNivel, "nivNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));            
        } catch (Exception ex) {
            Logger.getLogger(PaginasBean.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    public void limpiar() {
        filtro = new FiltroPagina();
        comboNivel.setSelected(-1);
        buscar();
    }

    public void actualizar(SgRhPagina var) {
        JSFUtils.limpiarMensajesError();        
        entidadEnEdicion = (SgRhPagina) SerializationUtils.clone(var);            
    }    

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPagPk());
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
            historialPagina = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroPagina getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPagina filtro) {
        this.filtro = filtro;
    }

    public SgRhPagina getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgRhPagina entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialPagina() {
        return historialPagina;
    }

    public void setHistorialPagina(List<RevHistorico> historialPagina) {
        this.historialPagina = historialPagina;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public LazyPaginaDataModel getPaginaLazyModel() {
        return paginaLazyModel;
    }

    public void setPaginaLazyModel(LazyPaginaDataModel paginaLazyModel) {
        this.paginaLazyModel = paginaLazyModel;
    }
    
    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }
}
