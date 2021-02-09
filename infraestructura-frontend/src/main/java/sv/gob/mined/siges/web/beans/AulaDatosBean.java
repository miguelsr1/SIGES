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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipologiaConstruccion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAula;
import sv.gob.mined.siges.web.enumerados.EnumTipoAula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AulaDatosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AulaDatosBean.class.getName());

    @Inject
    private AulaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private InmueblesSedesRestClient inmueblesSedesRestClient;
    
    @Inject
    private EdificioRestClient edificioRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AulaBean aulaBean;

    private SgAula entidadEnEdicion = new SgAula();
    private List<RevHistorico> historialAula = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private Boolean esSedePorDefecto;
    private SgEdificio edificioSeleccionado;
    private Integer nivelSeleccionado;
    private List<SelectItem> nivelCombo;

    private String securityOperation;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<EnumEstadoAula> comboEstadoAula;
    private SofisComboG<EnumTipoAula> comboTipoAula;

    public AulaDatosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgAula) request.getAttribute("aula");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombos() {
        
            nivelSeleccionado = 0;
            nivelCombo = new ArrayList();
            nivelCombo.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            
            List<EnumEstadoAula> estadosAula = new ArrayList(Arrays.asList(EnumEstadoAula.values()));
            comboEstadoAula = new SofisComboG(estadosAula, "text");
            comboEstadoAula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
      
            List<EnumTipoAula> tipoAula = new ArrayList(Arrays.asList(EnumTipoAula.values()));
            comboTipoAula = new SofisComboG(tipoAula, "text");
            comboTipoAula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }
    
    public List<SgInfTipologiaConstruccion> completeTipologiaConstruccion(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"ticNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getAulTipologiaConstruccion()!= null
                    ? catalogosRestClient.buscarTiplogiaConstruccion(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getAulTipologiaConstruccion().contains(i))
                            .collect(Collectors.toList())
                    : catalogosRestClient.buscarTiplogiaConstruccion(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarEntidad() {
        if (entidadEnEdicion.getAulEdificioFk()!= null) {
            edificioSeleccionado=entidadEnEdicion.getAulEdificioFk();
            seleccionarEdificio();
            nivelSeleccionado=entidadEnEdicion.getAulNivel();
            comboEstadoAula.setSelectedT(entidadEnEdicion.getAulEstado());
            comboTipoAula.setSelectedT(entidadEnEdicion.getAulTipo());
        }
    }

    public List<SgEdificio> completeEdificio(String query) {
        try {
            FiltroEdificio fil = new FiltroEdificio();
            fil.setCodigo(query);
            fil.setMaxResults(11L);
            fil.setIncluirCampos(new String[]{"ediPk", "ediCodigo","ediCantidadNiveles", "ediVersion"});
            return edificioRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    
    public void seleccionarEdificio(){
        nivelSeleccionado = 0;
        nivelCombo = new ArrayList();
        nivelCombo.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
        if(edificioSeleccionado!=null){
            if(edificioSeleccionado.getEdiCantidadNiveles()!=null){                
                Integer cantidad=edificioSeleccionado.getEdiCantidadNiveles();
                for (int i = 1; i <= cantidad; i++) {
                    nivelCombo.add(new SelectItem(i, ""+i));
                }
            }
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setAulEdificioFk(edificioSeleccionado);
            entidadEnEdicion.setAulNivel(nivelSeleccionado!=0?nivelSeleccionado:null);
            entidadEnEdicion.setAulEstado(comboEstadoAula.getSelectedT());
            entidadEnEdicion.setAulTipo(comboTipoAula.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            aulaBean.setEntidadEnEdicion(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgEdificio var) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getEdiPk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }



   
  
 

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
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



    public Boolean getEsSedePorDefecto() {
        return esSedePorDefecto;
    }

    public void setEsSedePorDefecto(Boolean esSedePorDefecto) {
        this.esSedePorDefecto = esSedePorDefecto;
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

    public Integer getNivelSeleccionado() {
        return nivelSeleccionado;
    }

    public void setNivelSeleccionado(Integer nivelSeleccionado) {
        this.nivelSeleccionado = nivelSeleccionado;
    }

    public List<SelectItem> getNivelCombo() {
        return nivelCombo;
    }

    public void setNivelCombo(List<SelectItem> nivelCombo) {
        this.nivelCombo = nivelCombo;
    }

    public SgEdificio getEdificioSeleccionado() {
        return edificioSeleccionado;
    }

    public void setEdificioSeleccionado(SgEdificio edificioSeleccionado) {
        this.edificioSeleccionado = edificioSeleccionado;
    }

    public SofisComboG<EnumEstadoAula> getComboEstadoAula() {
        return comboEstadoAula;
    }

    public void setComboEstadoAula(SofisComboG<EnumEstadoAula> comboEstadoAula) {
        this.comboEstadoAula = comboEstadoAula;
    }

    public SofisComboG<EnumTipoAula> getComboTipoAula() {
        return comboTipoAula;
    }

    public void setComboTipoAula(SofisComboG<EnumTipoAula> comboTipoAula) {
        this.comboTipoAula = comboTipoAula;
    }



}
