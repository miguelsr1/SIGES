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
import sv.gob.mined.siges.web.dto.SgRhEtiqueta;
import sv.gob.mined.siges.web.dto.SgRhPagina;
import sv.gob.mined.siges.web.dto.centroseducativos.SgNivel;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPagina;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EtiquetaRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PaginaRestClient;

import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EtiquetaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EtiquetaBean.class.getName());

    @Inject
    private EtiquetaRestClient restClient;
    
    @Inject
    private NivelRestClient nivelClient;    
    
    @Inject
    private PaginaRestClient paginaClient;        
    
    @Inject
    @Param(name = "id")
    private Long etiId;
    
    @Inject
    @Param(name = "edit")
    private Boolean editar;
       
    private SgRhEtiqueta entidadEnEdicion = new SgRhEtiqueta();
    private String formatoSeleccionado = "csv";    
    private SofisComboG<SgNivel> comboNivel;
    private String stNivel;
    private SgArchivo archivo = null;

    public EtiquetaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if(etiId!=null){              
                this.actualizar(restClient.obtenerPorId(etiId));
            }else{
                agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
            comboNivel.setDisabled(false);
        } catch (Exception ex) {
            Logger.getLogger(EtiquetaBean.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    public void limpiar() {
        comboNivel.setSelected(-1);
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgRhEtiqueta();
        entidadEnEdicion.setEtiPagina(new SgRhPagina());
    }

    public void actualizar(SgRhEtiqueta var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgRhEtiqueta) SerializationUtils.clone(var);        
        if (entidadEnEdicion.getEtiPagina() != null) {
            if (entidadEnEdicion.getEtiPagina().getPagNivel() != null ) {
                comboNivel.setSelectedT(entidadEnEdicion.getEtiPagina().getPagNivel());
            }
        }else{
            entidadEnEdicion.setEtiPagina(new SgRhPagina());      
        }
    }
    
    
    private SgRhPagina buscarPagina(){
        FiltroPagina filtro = new FiltroPagina();
        SgRhPagina pagina = null;
        if(entidadEnEdicion.getEtiPagina()!=null){
            filtro.setPagNivelPk(comboNivel.getSelectedT().getNivPk());            
            filtro.setPagAnio(entidadEnEdicion.getEtiPagina().getPagAnio());
            filtro.setPagPagina(entidadEnEdicion.getEtiPagina().getPagPagina());           
            filtro.setPagLibro(entidadEnEdicion.getEtiPagina().getPagLibro());
            filtro.setMaxResults(1L);
            filtro.setIncluirCampos(new String[]{"pagPk"});
            
            try {
                List<SgRhPagina> paginas = paginaClient.buscar(filtro);
                if(paginas != null && paginas.size()>=1){
                    pagina = paginas.get(0);
                }
            } catch (Exception ex) {
                Logger.getLogger(EtiquetaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pagina;
    }
    
    public void guardar() {
        try {   
            entidadEnEdicion.getEtiPagina().setPagNivel(comboNivel.getSelectedT());
            SgRhPagina pagina=null;
            if(comboNivel.getSelectedT()!=null &&
                  entidadEnEdicion.getEtiPagina().getPagAnio()!=null &&
                   entidadEnEdicion.getEtiPagina().getPagPagina()!=null &&
                    entidadEnEdicion.getEtiPagina().getPagLibro()!=null){
               pagina= buscarPagina();
               
             
            }           
            
            entidadEnEdicion=restClient.guardar(entidadEnEdicion);
            
            if(pagina==null){
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, "Se creó nueva página para el Nivel, Año, Libro y Página indicados", "");
            }
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
            restClient.eliminar(entidadEnEdicion.getEtiPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public SgRhEtiqueta getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgRhEtiqueta entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }
    
    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Long getEtiId() {
        return etiId;
    }

    public void setEtiId(Long etiId) {
        this.etiId = etiId;
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
