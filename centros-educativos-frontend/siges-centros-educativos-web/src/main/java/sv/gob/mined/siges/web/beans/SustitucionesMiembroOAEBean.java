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
import sv.gob.mined.siges.web.dto.SgSustitucionMiembroOAE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSustitucionMiembroOAE;
import sv.gob.mined.siges.web.lazymodels.LazySustitucionMiembroOAEDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SustitucionMiembroOAERestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSustitucionMiembroOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SustitucionesMiembroOAEBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SustitucionesMiembroOAEBean.class.getName());

    @Inject
    private SustitucionMiembroOAERestClient restClient;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private SedeRestClient restSede;
    
    @Inject
    private CatalogosRestClient catalogoClient;

    private FiltroSustitucionMiembroOAE filtro = new FiltroSustitucionMiembroOAE();
    private SgSustitucionMiembroOAE entidadEnEdicion = new SgSustitucionMiembroOAE();
    private List<RevHistorico> historialSustitucionMiembroOAE = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySustitucionMiembroOAEDataModel sustitucionMiembroOAELazyModel;
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SgSede sedeSeleccionada;
    private SofisComboG<EnumEstadoSustitucionMiembroOAE> comboEstado = new SofisComboG<>();
    private SofisComboG<SgTipoOrganismoAdministrativo> comboTipoOAE = new SofisComboG<>();

    public SustitucionesMiembroOAEBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SUSTITUCION_MIEMBRO_OAE)) {
            JSFUtils.redirectToIndex();
        }
    }


    public void buscar() {
        try {
            filtro.setSmoSede(sedeSeleccionada!=null?sedeSeleccionada.getSedPk():null);
            filtro.setSmoEstado(comboEstado.getSelectedT());
            filtro.setSmoDepartamento(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setSmoTipoOrganismoAdministrativo(comboTipoOAE.getSelectedT()!=null?comboTipoOAE.getSelectedT().getToaPk():null);
            filtro.setIncluirCampos(new String[]{
                "smoOaeFk.oaeSede.sedCodigo",
                "smoOaeFk.oaeSede.sedTipo",
                "smoOaeFk.oaeSede.sedNombre",
                "smoOaeFk.oaeSede.sedDireccion.dirDepartamento.depNombre",
                "smoOaeFk.oaeSede.sedDireccion.dirMunicipio.munNombre",
                "smoOaeFk.oaeActaIntegracion",
                "smoOaeFk.oaeFechaActaIntegracion",
                "smoOaeFk.oaeNumeroAcuerdo",
                "smoOaeFk.oaeFechaAcuerdo",
                "smoOaeFk.oaeFechaVencimiento",
                "smoOaeFk.oaeFechaCierre",
                "smoOaeFk.oaeEstado",
                "smoOaeFk.oaeActaIntegracion",
                "smoOaeFk.oaePk",
                "smoFecha",
                "smoEstado"});
            filtro.setIncluirCantidadMiembros(Boolean.TRUE);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            sustitucionMiembroOAELazyModel = new LazySustitucionMiembroOAEDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            
            

            List<EnumEstadoSustitucionMiembroOAE> tipoEnum = new ArrayList(Arrays.asList(EnumEstadoSustitucionMiembroOAE.values()));
            comboEstado = new SofisComboG(tipoEnum, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstado.ordenar();

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

  
            
            FiltroCodiguera filtroTipo = new FiltroCodiguera();
            filtroTipo.setIncluirCampos(new String[]{"toaNombre", "toaVersion"});
            filtroTipo.setHabilitado(Boolean.TRUE);
            comboTipoOAE = new SofisComboG<>(catalogoClient.buscarTipoOrganismoAdministrativo(filtroTipo),"toaNombre");
            comboTipoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        sedeSeleccionada=null;
        comboEstado.setSelected(-1);
        comboTipoOAE.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        filtro = new FiltroSustitucionMiembroOAE();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgSustitucionMiembroOAE();
    }

    public void actualizar(SgSustitucionMiembroOAE var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgSustitucionMiembroOAE) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getSmoPk());
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
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void historial(Long id) {
        try {
            historialSustitucionMiembroOAE = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroSustitucionMiembroOAE getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSustitucionMiembroOAE filtro) {
        this.filtro = filtro;
    }

    public SgSustitucionMiembroOAE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSustitucionMiembroOAE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialSustitucionMiembroOAE() {
        return historialSustitucionMiembroOAE;
    }

    public void setHistorialSustitucionMiembroOAE(List<RevHistorico> historialSustitucionMiembroOAE) {
        this.historialSustitucionMiembroOAE = historialSustitucionMiembroOAE;
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

    public LazySustitucionMiembroOAEDataModel getSustitucionMiembroOAELazyModel() {
        return sustitucionMiembroOAELazyModel;
    }

    public void setTiposCalendarioLazyModel(LazySustitucionMiembroOAEDataModel sustitucionMiembroOAELazyModel) {
        this.sustitucionMiembroOAELazyModel = sustitucionMiembroOAELazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<EnumEstadoSustitucionMiembroOAE> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSustitucionMiembroOAE> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgTipoOrganismoAdministrativo> getComboTipoOAE() {
        return comboTipoOAE;
    }

    public void setComboTipoOAE(SofisComboG<SgTipoOrganismoAdministrativo> comboTipoOAE) {
        this.comboTipoOAE = comboTipoOAE;
    }

}
