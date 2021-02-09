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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgPeriodoCalendario;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.lazymodels.LazyCalendarioEscolarDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalendarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalendarioEscolarBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalendarioEscolarBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long calendarioId;

    @Inject
    private CalendarioEscolarRestClient restClient;

    @Inject
    private CalendarioRestClient calendarioRestClient;

    @Inject
    private NivelRestClient nivelRestClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgPeriodoCalendario entidadEnEdicion = new SgPeriodoCalendario();
    private List<RevHistorico> historialCalendarioEscolar = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalendarioEscolarDataModel calendarioEscolarLazyModel;
    private SofisComboG<SgModalidadAtencion> modalidadAtencionCombo;
    private SofisComboG<SgSubModalidadAtencion> subModalidadAtencionCombo;
    private SofisComboG<SgNivel> nivelCombo;
    private SofisComboG<EnumCalendarioEscolar> tipoCalEscCombo;
    private List<SgPeriodoCalendario> listaCalendarioEsc;
    private List<EnumCalendarioEscolar> tipoCalEscList;
    private List<SgNivel> listNivel;
    private SgCalendario calendarioEnEdicion;

    public CalendarioEscolarBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CALENDARIOS_ESCOLARES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            FiltroPeriodoCalendario fpc = new FiltroPeriodoCalendario();
            //TODO: incluir campos?
            fpc.setCalPk(calendarioId);
            listaCalendarioEsc = restClient.buscar(fpc);
            calendarioEnEdicion = calendarioRestClient.obtenerPorId(calendarioId);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgPeriodoCalendario> buscarTipo(EnumCalendarioEscolar tipo, SgNivel niv) {
        return listaCalendarioEsc.stream()
                .filter(c -> c.getCesTipo().equals(tipo))
                .filter(c -> c.getCesNivel().equals(niv))
                .collect(Collectors.toList());
    }

    public void cargarComboModAt() {
        try {

            subModalidadAtencionCombo=null;
            modalidadAtencionCombo = new SofisComboG();
            modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if(nivelCombo.getSelectedT()!=null){
                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaNivel(nivelCombo.getSelectedT().getNivPk());
                List<SgRelModEdModAten> listrel = relModRestClient.buscar(filtroRel);

                List<SgModalidadAtencion> listModAt = new ArrayList();
                for (SgRelModEdModAten rel : listrel) {
                    if (!listModAt.contains(rel.getReaModalidadAtencion())) {
                        if (rel.getReaModalidadAtencion().getMatHabilitado()) {
                            listModAt.add(rel.getReaModalidadAtencion());
                        }
                    }
                }
                modalidadAtencionCombo = new SofisComboG(listModAt, "matNombre");
                modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {

            modalidadAtencionCombo = new SofisComboG();
            modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroNivel fc = new FiltroNivel();
            fc.setNivHabilitado(Boolean.TRUE);
            fc.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
            listNivel = nivelRestClient.buscar(fc);
            nivelCombo = new SofisComboG(listNivel, "nivNombre");
            nivelCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumCalendarioEscolar> escalas = new ArrayList(Arrays.asList(EnumCalendarioEscolar.values()));
            tipoCalEscCombo = new SofisComboG(escalas, "text");
            tipoCalEscCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            tipoCalEscList = new ArrayList();
            tipoCalEscList.add(EnumCalendarioEscolar.MAT);
            tipoCalEscList.add(EnumCalendarioEscolar.PREC);
            tipoCalEscList.add(EnumCalendarioEscolar.PRECPS);
            tipoCalEscList.add(EnumCalendarioEscolar.SREC);
            tipoCalEscList.add(EnumCalendarioEscolar.SRECPS);
            
            subModalidadAtencionCombo=null;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarSubModalidad(){
        try{
            subModalidadAtencionCombo=null;
            if(modalidadAtencionCombo.getSelectedT()!=null && nivelCombo.getSelectedT()!=null){
                
                FiltroRelModEdModAten filRel=new FiltroRelModEdModAten();
                filRel.setReaModalidadAtencion(modalidadAtencionCombo.getSelectedT().getMatPk());
                filRel.setReaNivel(nivelCombo.getSelectedT().getNivPk());
                List<SgRelModEdModAten> listRel=relModRestClient.buscar(filRel);
                List<SgSubModalidadAtencion> listSub=new ArrayList();
                for(SgRelModEdModAten rel:listRel){
                    if(rel.getReaSubModalidadAtencion()!=null){
                        if(! listSub.contains(rel.getReaSubModalidadAtencion())){
                            listSub.add(rel.getReaSubModalidadAtencion());
                        }
                    }
                }
                if(!listSub.isEmpty()){
                    subModalidadAtencionCombo=new SofisComboG(listSub,"smoNombre");
                    subModalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }    
    }

    private void limpiarCombos() {
        modalidadAtencionCombo.setSelected(-1);
        nivelCombo.setSelected(-1);
        tipoCalEscCombo.setSelected(-1);

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgPeriodoCalendario();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgPeriodoCalendario var) {
        try {
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getCesPk());
            nivelCombo.setSelectedT(entidadEnEdicion.getCesNivel());
            cargarComboModAt();
            modalidadAtencionCombo.setSelectedT(entidadEnEdicion.getCesModalidadAtencion());
            cargarSubModalidad();
            if(subModalidadAtencionCombo!=null){
                subModalidadAtencionCombo.setSelectedT( entidadEnEdicion.getCesSubModalidadAtencion());
            }

            tipoCalEscCombo.setSelectedT(entidadEnEdicion.getCesTipo());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            if (nivelCombo.getSelectedT() != null) {
                entidadEnEdicion.setCesNivel(nivelCombo.getSelectedT());
            }
            if (modalidadAtencionCombo.getSelectedT() != null) {
                entidadEnEdicion.setCesModalidadAtencion(modalidadAtencionCombo.getSelectedT());
            }
            if (tipoCalEscCombo.getSelectedT() != null) {
                entidadEnEdicion.setCesTipo(tipoCalEscCombo.getSelectedT());
            }
            entidadEnEdicion.setCesSubModalidadAtencion(null);
            if(subModalidadAtencionCombo!=null){
                if(subModalidadAtencionCombo.getSelectedT()!=null){
                    entidadEnEdicion.setCesSubModalidadAtencion(subModalidadAtencionCombo.getSelectedT());
                }
            }
            entidadEnEdicion.setCesCalendario(calendarioEnEdicion);

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCesPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialCalendarioEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgPeriodoCalendario getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPeriodoCalendario entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCalendarioEscolar() {
        return historialCalendarioEscolar;
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

    public LazyCalendarioEscolarDataModel getCalendarioEscolarLazyModel() {
        return calendarioEscolarLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalendarioEscolarDataModel calendarioEscolarLazyModel) {
        this.calendarioEscolarLazyModel = calendarioEscolarLazyModel;
    }

    public SofisComboG<SgModalidadAtencion> getModalidadAtencionCombo() {
        return modalidadAtencionCombo;
    }

    public void setModalidadAtencionCombo(SofisComboG<SgModalidadAtencion> modalidadAtencionCombo) {
        this.modalidadAtencionCombo = modalidadAtencionCombo;
    }

    public SofisComboG<SgNivel> getNivelCombo() {
        return nivelCombo;
    }

    public void setNivelCombo(SofisComboG<SgNivel> nivelCombo) {
        this.nivelCombo = nivelCombo;
    }

    public SofisComboG<EnumCalendarioEscolar> getTipoCalEscCombo() {
        return tipoCalEscCombo;
    }

    public void setTipoCalEscCombo(SofisComboG<EnumCalendarioEscolar> tipoCalEscCombo) {
        this.tipoCalEscCombo = tipoCalEscCombo;
    }

    public List<SgPeriodoCalendario> getListaCalendarioEsc() {
        return listaCalendarioEsc;
    }

    public void setListaCalendarioEsc(List<SgPeriodoCalendario> listaCalendarioEsc) {
        this.listaCalendarioEsc = listaCalendarioEsc;
    }

    public List<EnumCalendarioEscolar> getTipoCalEscList() {
        return tipoCalEscList;
    }

    public void setTipoCalEscList(List<EnumCalendarioEscolar> tipoCalEscList) {
        this.tipoCalEscList = tipoCalEscList;
    }

    public List<SgNivel> getListNivel() {
        return listNivel;
    }

    public void setListNivel(List<SgNivel> listNivel) {
        this.listNivel = listNivel;
    }

    public SgCalendario getCalendarioEnEdicion() {
        return calendarioEnEdicion;
    }

    public void setCalendarioEnEdicion(SgCalendario calendarioEnEdicion) {
        this.calendarioEnEdicion = calendarioEnEdicion;
    }

    public SofisComboG<SgSubModalidadAtencion> getSubModalidadAtencionCombo() {
        return subModalidadAtencionCombo;
    }

    public void setSubModalidadAtencionCombo(SofisComboG<SgSubModalidadAtencion> subModalidadAtencionCombo) {
        this.subModalidadAtencionCombo = subModalidadAtencionCombo;
    }

}
