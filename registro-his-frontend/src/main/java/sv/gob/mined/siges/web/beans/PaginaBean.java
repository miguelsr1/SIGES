/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgRhPagina;
import sv.gob.mined.siges.web.dto.centroseducativos.SgNivel;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPagina;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PaginaRestClient;

import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class PaginaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PaginaBean.class.getName());

    @Inject
    private PaginaRestClient restClient;
    
    @Inject
    private NivelRestClient nivelClient;    
    
    @Inject
    @Param(name = "id")
    private Long pagId;
        
    @Inject
    @Param(name = "edit")
    private Boolean editar ;
    
    
    private FiltroPagina filtro = new FiltroPagina();
    private SgRhPagina entidadEnEdicion = new SgRhPagina();
    private SofisComboG<SgNivel> comboNivel;
    private String stNivel;
    private SgArchivo archivo;

    public PaginaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if(pagId!=null){              
                this.actualizar(restClient.obtenerPorId(pagId));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscar() {
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
            Logger.getLogger(PaginaBean.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroPagina();
        comboNivel.setSelected(-1);
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgRhPagina();
    }

    public void actualizar(SgRhPagina var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgRhPagina) SerializationUtils.clone(var);         
        if(entidadEnEdicion.getPagNivel()!= null && comboNivel != null){
            comboNivel.setSelectedT(entidadEnEdicion.getPagNivel());
        }           
    }
    
    public void guardar() {
        try {
            entidadEnEdicion.setPagNivel(comboNivel.getSelectedT());
            entidadEnEdicion=restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public Long getPagId() {
        return pagId;
    }

    public void setPagId(Long pagId) {
        this.pagId = pagId;
    }
    
    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public String getStNivel() {
        return stNivel;
    }

    public void setStNivel(String stNivel) {
        this.stNivel = stNivel;
    }
    
    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public SgArchivo getArchivo() {
        return archivo;
    }

    public void setArchivo(SgArchivo archivo) {
        this.archivo = archivo;
    }
}
