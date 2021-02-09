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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudPlaza;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudPlazaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudPlazaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolicitudPlaza;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudPlaza;
import sv.gob.mined.siges.web.enumerados.EnumTipoPlaza;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class SolicitudesPlazaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudesPlazaBean.class.getName());

    @Inject
    private SolicitudPlazaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private NivelRestClient nivelClient;
    @Inject
    private SedeRestClient restSede;
    

    private SgSolicitudPlaza entidadEnEdicion;
    private FiltroSolicitudPlaza filtro = new FiltroSolicitudPlaza();
    private LazySolicitudPlazaDataModel plazaLazyModel;
    private List<RevHistorico> historialPlaza = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<EnumTipoPlaza> comboTipoPlaza;
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgMunicipio> comboMunicipios = new SofisComboG<>();
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgJornadaLaboral> comboJornada;
    private SofisComboG<EnumEstadoSolicitudPlaza> comboEstado;
    private SgSede sedeSeleccionadaFiltro;
    

    public SolicitudesPlazaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLAZA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public boolean renderPlazaDesierta(SgSolicitudPlaza sol){
        if(sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MARCAR_PLAZA_DESIERTA)){
            try {
                if(!sol.getSplEstado().equals(EnumEstadoSolicitudPlaza.DESIERTA) && !sol.getSplEstado().equals(EnumEstadoSolicitudPlaza.PUBLICADA)){
                    return true;
                }
            } catch (Exception ex) {
                Logger.getLogger(PlazasBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public void actualizar(SgSolicitudPlaza var) {
        try {
//            List<SgSolicitudPlaza> solicitudes = var.getPlaSolicitudPlaza();
            entidadEnEdicion = restClient.obtenerPorId(var.getSplPk());
//            entidadEnEdicion.setPlaSolicitudPlaza(solicitudes);
//            sedeSeleccionada = var.getPlaSedeFk();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void marcarPLazaDesierta() {
        try {
            actualizar(entidadEnEdicion);
            entidadEnEdicion.setSplEstado(EnumEstadoSolicitudPlaza.DESIERTA);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }    
    
    public String tooltipDesierta(SgSolicitudPlaza sol){
        if(sol.getSplEstado().equals(EnumEstadoSolicitudPlaza.DESIERTA)){
            return Etiquetas.getValue("plazaDesierta");
        }
        return null;
    }
    
    public String buscar() {
        try {
            filtro.setTipo(comboTipoPlaza.getSelectedT());
            filtro.setDepartamento(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            filtro.setNivel((comboNivel != null) ? comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null : null);
            filtro.setJornada((comboJornada != null) ? comboJornada.getSelectedT() != null ? comboJornada.getSelectedT().getJlaPk() : null : null);
            filtro.setEstado(comboEstado.getSelectedT());
            filtro.setSede(sedeSeleccionadaFiltro!=null?sedeSeleccionadaFiltro.getSedPk():null);
            filtro.setEsPosiblePublicar(Boolean.TRUE);
            filtro.setIncluirCampos(new String[]{
                "splSedeSolicitante.sedPk",
                "splSedeSolicitante.sedCodigo",
                "splSedeSolicitante.sedNombre",
                "splSedeSolicitante.sedTipo",
                "splSedeSolicitante.sedDireccion.dirDepartamento.depNombre",
                "splSedeSolicitante.sedDireccion.dirMunicipio.munNombre",
                "splNivel.nivNombre",
                "splModalidadEducativa.modNombre",
                "splOpcion.opcNombre",
                "splTipoPlaza",
                "splEstado",
                "splComponentePlanEstudio.cpeNombre",
                "splComponentePlanEstudio.cpeTipo"});
            totalResultados = restClient.buscarTotal(filtro);
            plazaLazyModel = new LazySolicitudPlazaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipios = new SofisComboG<>();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumTipoPlaza> tipoPlaza = new ArrayList(Arrays.asList(EnumTipoPlaza.values()));
            comboTipoPlaza = new SofisComboG(tipoPlaza, "text");
            comboTipoPlaza.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTipoPlaza.ordenar();
            
            fc.setOrderBy(new String[]{"jlaNombre"});
            fc.setIncluirCampos(new String[]{"jlaNombre", "jlaVersion"});
            List<SgJornadaLaboral> listaJornada = catalogoClient.buscarJornadasLaborales(fc);
            comboJornada = new SofisComboG<>(listaJornada, "jlaNombre");
            comboJornada.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            FiltroNivel filtroNivel = new FiltroNivel();
            filtroNivel.setOrderBy(new String[]{"nivNombre"});
            filtroNivel.setAscending(new boolean[]{true});
            filtroNivel.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
            List<SgNivel> listaNivel = nivelClient.buscar(filtroNivel);
            comboNivel = new SofisComboG(listaNivel, "nivelOrganizacion");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            List<EnumEstadoSolicitudPlaza> estados= new ArrayList(Arrays.asList(EnumEstadoSolicitudPlaza.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstado.ordenar();
        } catch (Exception ex) {
            Logger.getLogger(SolicitudesPlazaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void seleccionarParaConfirmar(Long id){
        try {
            entidadEnEdicion = restClient.obtenerPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void aprobarPlaza(){
        try {
            entidadEnEdicion.setSplEstado(EnumEstadoSolicitudPlaza.APROBADA);
            restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.APROBADO_CORRECTO), "");
            entidadEnEdicion = null;
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    private void limpiarCombosAvanzados() {
        comboTipoPlaza.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        comboMunicipios.setSelected(-1);
        comboNivel.setSelected(-1);
        comboJornada.setSelected(-1);
    }

    private void limpiarCombos() {
        cargarCombos();
        limpiarCombosAvanzados();
    }

    public String limpiar() {
        sedeSeleccionadaFiltro=null;
        filtro = new FiltroSolicitudPlaza();
        limpiarCombos();
        buscar();
        return null;
    }

    public String historial(Long id) {
        try {
            historialPlaza = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());
                filtro.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                List<SgMunicipio> municipios = catalogoClient.buscarMunicipio(filtro);
                comboMunicipios = new SofisComboG(municipios, "munNombre");
                comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
//            if (securityOperation != null) {
//                fil.setSecurityOperation(securityOperation);
//            }

            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
            return restSede.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
     public void seleccionarParaPublicar(SgSolicitudPlaza entity){
        try {
            entidadEnEdicion= (SgSolicitudPlaza)SerializationUtils.clone(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void publicarSolicitud(){
        try {
            
            entidadEnEdicion=restClient.publicarSolicitud(entidadEnEdicion);            
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('confirmDialog').hide()");          
         } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }



    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public SgSolicitudPlaza getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudPlaza entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroSolicitudPlaza getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSolicitudPlaza filtro) {
        this.filtro = filtro;
    }

    public LazySolicitudPlazaDataModel getPlazaLazyModel() {
        return plazaLazyModel;
    }

    public void setPlazaLazyModel(LazySolicitudPlazaDataModel plazaLazyModel) {
        this.plazaLazyModel = plazaLazyModel;
    }

    public List<RevHistorico> getHistorialPlaza() {
        return historialPlaza;
    }

    public void setHistorialPlaza(List<RevHistorico> historialPlaza) {
        this.historialPlaza = historialPlaza;
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

    public SofisComboG<EnumTipoPlaza> getComboTipoPlaza() {
        return comboTipoPlaza;
    }

    public void setComboTipoPlaza(SofisComboG<EnumTipoPlaza> comboTipoPlaza) {
        this.comboTipoPlaza = comboTipoPlaza;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgJornadaLaboral> getComboJornada() {
        return comboJornada;
    }

    public void setComboJornada(SofisComboG<SgJornadaLaboral> comboJornada) {
        this.comboJornada = comboJornada;
    }
    
    public SofisComboG<EnumEstadoSolicitudPlaza> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSolicitudPlaza> comboEstado) {
        this.comboEstado = comboEstado;
    }
    
    
    //</editor-fold>

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    
    

}
