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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoTitular;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoTitular;
import sv.gob.mined.siges.web.lazymodels.LazySelloFirmaTitularDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaTitularRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SellosFirmasAutenticaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SellosFirmasAutenticaBean.class.getName());

    @Inject
    private SelloFirmaTitularRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SessionBean sessionBean;


    private FiltroSelloFirmaTitular filtro = new FiltroSelloFirmaTitular();
    private SgSelloFirmaTitular entidadEnEdicion = new SgSelloFirmaTitular();
    private List<RevHistorico> historialSelloFirma = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySelloFirmaTitularDataModel selloFirmaTitularLazyModel;
    private SofisComboG<SgCargoTitular> comboCargoTitular;
    private SgCargoTitular cargoAutentica;

    public SellosFirmasAutenticaBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SELLOS_FIRMAS_TITULAR)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setEsTitular(Boolean.FALSE);
            filtro.setSftCargoTitular(comboCargoTitular.getSelectedT()!=null?comboCargoTitular.getSelectedT().getCtiPk():null);
            filtro.setIncluirCampos(new String[]{
                "sftPrimerNombre",
                "sftPrimerApellido",
                "sftCargoTitular.ctiNombre",
                "sftHabilitado",
                "sftUltModUsuario",
                "sftUltModFecha",
                "sftVersion",
            });          
            totalResultados = restClient.buscarTotal(filtro);
            selloFirmaTitularLazyModel = new LazySelloFirmaTitularDataModel(restClient, filtro, totalResultados, paginado);
          
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCargoTitular fc = new FiltroCargoTitular();
            fc.setEsTitular(Boolean.FALSE);
            List<SgCargoTitular> listTipoRep = catalogoRestClient.buscarCargoTitular(fc);
            comboCargoTitular = new SofisComboG(listTipoRep, "ctiNombre");
            comboCargoTitular.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboCargoTitular.setSelected(-1);
    }

    public void limpiar() {
        limpiarCombos();
        filtro = new FiltroSelloFirmaTitular();
        totalResultados = null;
        selloFirmaTitularLazyModel = null;
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSelloFirmaTitular();
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
            restClient.eliminar(entidadEnEdicion.getSftPk());
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
            historialSelloFirma = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public FiltroSelloFirmaTitular getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSelloFirmaTitular filtro) {
        this.filtro = filtro;
    }

    public SgSelloFirmaTitular getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSelloFirmaTitular entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialSelloFirma() {
        return historialSelloFirma;
    }

    public void setHistorialSelloFirma(List<RevHistorico> historialSelloFirma) {
        this.historialSelloFirma = historialSelloFirma;
    }

    public LazySelloFirmaTitularDataModel getSelloFirmaTitularLazyModel() {
        return selloFirmaTitularLazyModel;
    }

    public void setSelloFirmaTitularLazyModel(LazySelloFirmaTitularDataModel selloFirmaTitularLazyModel) {
        this.selloFirmaTitularLazyModel = selloFirmaTitularLazyModel;
    }

    public SofisComboG<SgCargoTitular> getComboCargoTitular() {
        return comboCargoTitular;
    }

    public void setComboCargoTitular(SofisComboG<SgCargoTitular> comboCargoTitular) {
        this.comboCargoTitular = comboCargoTitular;
    }

    public SgCargoTitular getCargoAutentica() {
        return cargoAutentica;
    }

    public void setCargoAutentica(SgCargoTitular cargoAutentica) {
        this.cargoAutentica = cargoAutentica;
    }



}
