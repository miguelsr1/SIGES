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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoRepresentante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTipoRepresentante;
import sv.gob.mined.siges.web.lazymodels.LazySelloFirmaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SellosFirmasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SellosFirmasBean.class.getName());

    @Inject
    private SelloFirmaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private PersonaRestClient personaRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroSelloFirma filtro = new FiltroSelloFirma();
    private SgSelloFirma entidadEnEdicion = new SgSelloFirma();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySelloFirmaDataModel selloFirmaLazyModel;
    private SofisComboG<SgTipoRepresentante> comboTipoRepresentante;
    private List<SgSede> listSede;
    private SgSede sedSeleccionado;
    private List<RevHistorico> historialSellosFirmas = new ArrayList();

    public SellosFirmasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SELLOS_FIRMAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setSfiTipoRepresentantePk(comboTipoRepresentante.getSelectedT() != null ? comboTipoRepresentante.getSelectedT().getTrePk() : null);
            filtro.setSedPk(sedSeleccionado != null ? sedSeleccionado.getSedPk() : null);
            filtro.setIncluirCampos(new String[]{
                "sfiPersona.perPrimerNombre",
                "sfiPersona.perSegundoNombre",
                "sfiPersona.perPrimerApellido",
                "sfiPersona.perSegundoApellido",
                "sfiTipoRepresentante.treNombre",
                "sfiPersona.perDui",
                "sfiPersona.perNip",
                "sfiHabilitado",
                "sfiUltModUsuario",
                "sfiUltModFecha",
                "sfiVersion",
                "sfiSede.sedCodigo",
                "sfiSede.sedPk",
                "sfiSede.sedTipo",
                "sfiSede.sedVersion",
                "sfiSede.sedNombre",
                "sfiSede.sedTelefono",
                "sfiSede.sedDireccion.dirDepartamento.depNombre",
                "sfiSede.sedDireccion.dirMunicipio.munNombre",
            }); 
            totalResultados = restClient.buscarTotal(filtro);
            selloFirmaLazyModel = new LazySelloFirmaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroTipoRepresentante fc = new FiltroTipoRepresentante();
            List<SgTipoRepresentante> listTipoRep = catalogoRestClient.buscarTipoRepresentante(fc);
            comboTipoRepresentante = new SofisComboG(listTipoRep, "treNombre");
            comboTipoRepresentante.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoRepresentante.setSelected(-1);
    }

    public void limpiar() {
        limpiarCombos();
        filtro = new FiltroSelloFirma();
        sedSeleccionado = null;
        selloFirmaLazyModel = null;
        totalResultados = null;
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSelloFirma();
    }

    public void actualizar(SgSelloFirma var) {
        limpiarCombos();
        entidadEnEdicion = (SgSelloFirma) SerializationUtils.clone(var);
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
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

    public void seleccionarSede() {
        try {
            entidadEnEdicion = new SgSelloFirma();
            entidadEnEdicion.setSfiSede(sedSeleccionado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgPersona> completePersona(String query) {
        try {
            FiltroPersona fil = new FiltroPersona();
            fil.setDui(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return personaRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
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
            restClient.eliminar(entidadEnEdicion.getSfiPk());
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
            historialSellosFirmas = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroSelloFirma getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSelloFirma filtro) {
        this.filtro = filtro;
    }

    public SgSelloFirma getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSelloFirma entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public LazySelloFirmaDataModel getSelloFirmaLazyModel() {
        return selloFirmaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazySelloFirmaDataModel selloFirmaLazyModel) {
        this.selloFirmaLazyModel = selloFirmaLazyModel;
    }

    public SofisComboG<SgTipoRepresentante> getComboTipoRepresentante() {
        return comboTipoRepresentante;
    }

    public void setComboTipoRepresentante(SofisComboG<SgTipoRepresentante> comboTipoRepresentante) {
        this.comboTipoRepresentante = comboTipoRepresentante;
    }

    public List<SgSede> getListSede() {
        return listSede;
    }

    public void setListSede(List<SgSede> listSede) {
        this.listSede = listSede;
    }

    public SgSede getSedSeleccionado() {
        return sedSeleccionado;
    }

    public void setSedSeleccionado(SgSede sedSeleccionado) {
        this.sedSeleccionado = sedSeleccionado;
    }

    public List<RevHistorico> getHistorialSellosFirmas() {
        return historialSellosFirmas;
    }

    public void setHistorialSellosFirmas(List<RevHistorico> historialSellosFirmas) {
        this.historialSellosFirmas = historialSellosFirmas;
    }

}
