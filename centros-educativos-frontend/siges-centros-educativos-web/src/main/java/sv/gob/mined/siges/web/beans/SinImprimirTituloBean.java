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
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgReposicionTitulo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgTituloAnterior;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReposicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroDefinicionTitulo;
import sv.gob.mined.siges.web.lazymodels.LazyReposicionTituloDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ReposicionTituloRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SinImprimirTituloBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SinImprimirTituloBean.class.getName());

    @Inject
    private  SedeRestClient sedeRestClient;
    
    @Inject
    private ReposicionTituloRestClient restClient;
    
    @Inject
    private CatalogosRestClient catalogoRestClient;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private HandleArchivoBean handleArchivoBean;

    private FiltroReposicionTitulo filtro = new FiltroReposicionTitulo();
    private SgReposicionTitulo entidadEnEdicion = new SgReposicionTitulo();
    private List<SgReposicionTitulo> historialReposicionTitulo = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyReposicionTituloDataModel reposicionTituloLazyModel;
    private Boolean sedeSiges=Boolean.FALSE;
    private SofisComboG<SgDefinicionTitulo> definicionTituloCombo;
    private Boolean esEditable;
    private SofisComboG<EnumEstadoSolicitudImpresion> comboEstado;
    private SofisComboG<SgTituloAnterior> tituloAnteriorCombo;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(zip|pdf)$/";

    public SinImprimirTituloBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarConfiguracion();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setSimTipoImpresion(EnumTipoSolicitudImpresion.SIM);
            filtro.setSimEstado(comboEstado.getSelectedT()!=null?comboEstado.getSelectedT():null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            reposicionTituloLazyModel = new LazyReposicionTituloDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarConfiguracion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.TAMANIO_ARCHIVO_REPOSICION_TITULO);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tamanioImportArchivo = conf.get(0).getConValor();
            }
            fc.setCodigo(Constantes.TIPO_ARCHIVO_REPOSICION_TITULO);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tipoImportArchivo = conf.get(0).getConValor();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroDefinicionTitulo fcdt=new FiltroDefinicionTitulo();
            fcdt.setDtiEsTipoReposicion(Boolean.TRUE);
            fcdt.setOrderBy(new String[]{"dtiNombre"});
            fcdt.setAscending(new boolean[]{true});
            List<SgDefinicionTitulo> defTit=catalogoRestClient.buscarDefinicionTitulo(fcdt);
            definicionTituloCombo = new SofisComboG(defTit,"dtiNombre");
            definicionTituloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroCodiguera fc=new FiltroCodiguera();
            fc.setOrderBy(new String[]{"tanNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgTituloAnterior> titAnt=catalogoRestClient.buscarTituloAnterior(fc);
            tituloAnteriorCombo = new SofisComboG(titAnt,"tanNombre");
            tituloAnteriorCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
             List<EnumEstadoSolicitudImpresion> estados = new ArrayList(Arrays.asList(EnumEstadoSolicitudImpresion.values()));
             estados.remove(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstado.setSelectedT(EnumEstadoSolicitudImpresion.ENVIADA);
            
         } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        definicionTituloCombo.setSelected(-1);
        tituloAnteriorCombo.setSelected(-1);
        comboEstado.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroReposicionTitulo();
        comboEstado.setSelected(-1);
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgReposicionTitulo();
        entidadEnEdicion.setRetSolicitudImpresion(new SgSolicitudImpresion());
        entidadEnEdicion.getRetSolicitudImpresion().setSimTipoImpresion(EnumTipoSolicitudImpresion.SIM);
        entidadEnEdicion.getRetSolicitudImpresion().setSimResolucionFk(new SgArchivo());
        sedeSiges=Boolean.FALSE;
        esEditable=Boolean.TRUE;
    }

    public void actualizar(SgReposicionTitulo var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgReposicionTitulo) SerializationUtils.clone(var);
        if(entidadEnEdicion.getRetSede()!=null){
            sedeSiges=Boolean.TRUE;
        }
        definicionTituloCombo.setSelectedT(entidadEnEdicion.getRetSolicitudImpresion().getSimDefinicionTitulo());
        esEditable=Boolean.FALSE;
        
        if(entidadEnEdicion.getRetTituloAnterior2008()!=null){
            tituloAnteriorCombo.setSelectedT(entidadEnEdicion.getRetTituloAnterior2008());
        }
    }
    
    public void seleccionarEsAnterior2008(){
        entidadEnEdicion.setRetNombreTituloPosterior2008(null);
        tituloAnteriorCombo.setSelected(-1);
    }
    
    public void sedeSigesSeleccionada(){
        entidadEnEdicion.setRetSede(null);
        entidadEnEdicion.setRetSedeNombre(null);
    }

    public void guardar() {
        try {
            if(entidadEnEdicion.getRetSede()!=null){
                entidadEnEdicion.setRetSedeNombre(entidadEnEdicion.getRetSede().getSedCodigoNombre());
            }
            
            if(entidadEnEdicion.getRetPk()==null){                
                
                entidadEnEdicion.getRetSolicitudImpresion().setSimDefinicionTitulo(definicionTituloCombo.getSelectedT());
                entidadEnEdicion.getRetSolicitudImpresion().setSimUsuario(sessionBean.getEntidadUsuario());
            }
            entidadEnEdicion.setRetTituloAnterior2008(tituloAnteriorCombo.getSelectedT());
            entidadEnEdicion.getRetSolicitudImpresion().setSimEstudianteImpresion(new ArrayList());
            
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
            restClient.eliminar(entidadEnEdicion.getRetPk());
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
    
    public String colorRow(SgReposicionTitulo est) {

        if (est != null && est.getRetSolicitudImpresion()!=null) {
            switch(est.getRetSolicitudImpresion().getSimEstado()){
                case IMPRESA:
                    return "aprobadoSolicitud";
                case CONFIRMADA:
                    return "confirmado";
                case CON_EXCEPCIONES:
                    return "con-excepciones";
                case RECHAZADA:
                    return "reprobado";
                case ANULADA:
                    return "reprobado";
                default:
                                
            }
            
        }
        return null;
    }
    
    public void subirResolucion(FileUploadEvent event) {
        try {

            SgArchivo arc = new SgArchivo();
            handleArchivoBean.subirArchivoTmp(event.getFile(), arc);
            entidadEnEdicion.getRetSolicitudImpresion().setSimResolucionFk(arc);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
  
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

 
    public FiltroReposicionTitulo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroReposicionTitulo filtro) {
        this.filtro = filtro;
    }

    public SgReposicionTitulo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgReposicionTitulo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgReposicionTitulo> getHistorialReposicionTitulo() {
        return historialReposicionTitulo;
    }

    public void setHistorialReposicionTitulo(List<SgReposicionTitulo> historialReposicionTitulo) {
        this.historialReposicionTitulo = historialReposicionTitulo;
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

    public LazyReposicionTituloDataModel getReposicionTituloLazyModel() {
        return reposicionTituloLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyReposicionTituloDataModel reposicionTituloLazyModel) {
        this.reposicionTituloLazyModel = reposicionTituloLazyModel;
    }

    public Boolean getSedeSiges() {
        return sedeSiges;
    }

    public void setSedeSiges(Boolean sedeSiges) {
        this.sedeSiges = sedeSiges;
    }

    public SofisComboG<SgDefinicionTitulo> getDefinicionTituloCombo() {
        return definicionTituloCombo;
    }

    public void setDefinicionTituloCombo(SofisComboG<SgDefinicionTitulo> definicionTituloCombo) {
        this.definicionTituloCombo = definicionTituloCombo;
    }

    public Boolean getEsEditable() {
        return esEditable;
    }

    public void setEsEditable(Boolean esEditable) {
        this.esEditable = esEditable;
    }

    public SofisComboG<EnumEstadoSolicitudImpresion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSolicitudImpresion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgTituloAnterior> getTituloAnteriorCombo() {
        return tituloAnteriorCombo;
    }

    public void setTituloAnteriorCombo(SofisComboG<SgTituloAnterior> tituloAnteriorCombo) {
        this.tituloAnteriorCombo = tituloAnteriorCombo;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

}
