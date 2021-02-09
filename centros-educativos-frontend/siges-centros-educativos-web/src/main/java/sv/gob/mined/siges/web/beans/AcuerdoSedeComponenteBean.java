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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgAcuerdoSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAcuerdoSede;
import sv.gob.mined.siges.web.lazymodels.LazyAcuerdoSedeDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.AcuerdoSedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class AcuerdoSedeComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AcuerdoSedeComponenteBean.class.getName());

    @Inject
    private AcuerdoSedeRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    private SgAcuerdoSede entidadEnEdicion = new SgAcuerdoSede();
    private FiltroAcuerdoSede filtro = new FiltroAcuerdoSede();
    private LazyAcuerdoSedeDataModel acuerdoEducativoLazyModel;
    private List<RevHistorico> historialAcuerdoSede = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SgSede sedeEducativa;
    private SofisComboG<SgTipoAcuerdo> comboTipoAcuerdo;
    private SofisComboG<SgTipoAcuerdo> comboTipoAcuerdoB;
    private Boolean soloLectura;
    private Boolean verDatos;

    public AcuerdoSedeComponenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sedeEducativa = (SgSede) request.getAttribute("sedeEducativa");
            if(sedeEducativa!=null){
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SERVICIO_EDUCATIVO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setAseSede(sedeEducativa.getSedPk());
            filtro.setAseTipoAcuerdo(comboTipoAcuerdoB != null && comboTipoAcuerdoB.getSelectedT()!=null ? comboTipoAcuerdoB.getSelectedT().getTaoPk() : null);
            totalResultados = restClient.buscarTotal(filtro);

            filtro.setIncluirCampos(new String[]{
                "aseTipoAcuerdo.taoNombre",
                "aseNumeroAcuerdo",
                "aseNumeroResolucion",
                "aseSoloLectura",
                "aseNombreResponsable",
                "aseNumeroSolicitud",
                "aseVersion"});

            acuerdoEducativoLazyModel = new LazyAcuerdoSedeDataModel(restClient, filtro, totalResultados, paginado);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {

            FiltroCodiguera fcod = new FiltroCodiguera();
            fcod.setHabilitado(Boolean.TRUE);
            fcod.setAscending(new boolean[]{true});
            fcod.setOrderBy(new String[]{"taoNombre"});
            fcod.setIncluirCampos(new String[]{"taoNombre", "taoVersion"});
            List<SgTipoAcuerdo> tiposAcuerdos = restCatalogo.buscarTipoAcuerdo(fcod);
            comboTipoAcuerdo = new SofisComboG(new ArrayList(tiposAcuerdos), "taoNombre");
            comboTipoAcuerdo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboTipoAcuerdoB = new SofisComboG(new ArrayList(tiposAcuerdos), "taoNombre");
            comboTipoAcuerdoB.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void agregarAcuerdo() {
        verDatos = Boolean.FALSE;
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAcuerdoSede();
        entidadEnEdicion.setAseSede(sedeEducativa);
        comboTipoAcuerdo.setSelected(-1);
    }

    private void limpiarCombos() {
        comboTipoAcuerdoB.setSelected(-1);
        comboTipoAcuerdo.setSelected(-1);
    }

    public String limpiar() {
        filtro = new FiltroAcuerdoSede();
        limpiarCombos();
        acuerdoEducativoLazyModel = null;
        totalResultados = null;
        buscar();
        return null;
    }

    public void ver(Long sduPk) {
        try {
            verDatos = Boolean.TRUE;
            entidadEnEdicion = restClient.obtenerPorId(sduPk, Boolean.TRUE);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(Long sduPk) {
        try {
            verDatos = Boolean.FALSE;
            entidadEnEdicion = restClient.obtenerPorId(sduPk, Boolean.TRUE);
            verDatos = BooleanUtils.isTrue(entidadEnEdicion.getAseSoloLectura()); 
            comboTipoAcuerdo.setSelectedT(entidadEnEdicion.getAseTipoAcuerdo());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setAseTipoAcuerdo(comboTipoAcuerdo!=null?comboTipoAcuerdo.getSelectedT():null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('AcuerdoSedeDialog').hide()");
            if(acuerdoEducativoLazyModel!=null){
                buscar();
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgAcuerdoSede", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgAcuerdoSede", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getAsePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String historial(Long id) {
        try {
            historialAcuerdoSede = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public SgAcuerdoSede getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAcuerdoSede entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroAcuerdoSede getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAcuerdoSede filtro) {
        this.filtro = filtro;
    }

    public LazyAcuerdoSedeDataModel getAcuerdoEducativoLazyModel() {
        return acuerdoEducativoLazyModel;
    }

    public void setAcuerdoEducativoLazyModel(LazyAcuerdoSedeDataModel acuerdoEducativoLazyModel) {
        this.acuerdoEducativoLazyModel = acuerdoEducativoLazyModel;
    }

    public List<RevHistorico> getHistorialAcuerdoSede() {
        return historialAcuerdoSede;
    }

    public void setHistorialAcuerdoSede(List<RevHistorico> historialAcuerdoSede) {
        this.historialAcuerdoSede = historialAcuerdoSede;
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

    public SgSede getSedeEducativa() {
        return sedeEducativa;
    }

    public void setSedeEducativa(SgSede sedeEducativa) {
        this.sedeEducativa = sedeEducativa;
    }

    public SofisComboG<SgTipoAcuerdo> getComboTipoAcuerdo() {
        return comboTipoAcuerdo;
    }

    public void setComboTipoAcuerdo(SofisComboG<SgTipoAcuerdo> comboTipoAcuerdo) {
        this.comboTipoAcuerdo = comboTipoAcuerdo;
    }

    public SofisComboG<SgTipoAcuerdo> getComboTipoAcuerdoB() {
        return comboTipoAcuerdoB;
    }

    public void setComboTipoAcuerdoB(SofisComboG<SgTipoAcuerdo> comboTipoAcuerdoB) {
        this.comboTipoAcuerdoB = comboTipoAcuerdoB;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getVerDatos() {
        return verDatos;
    }

    public void setVerDatos(Boolean verDatos) {
        this.verDatos = verDatos;
    }

    
}
