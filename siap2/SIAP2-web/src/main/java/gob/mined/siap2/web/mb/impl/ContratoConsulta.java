/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.dataProvider.ContratoDataProvider;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.EstadosInsumosDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "contratoConsulta")
public class ContratoConsulta implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    AccionCentralDelegate accionCentralD;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    UsuarioInfo userInfo; 
    @Inject
    ContratoDelegate contratoDelegate;

    private LazyDataModel<ContratoOC> lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nroContratoOC;
    private TipoContrato tipoContratoOC;
    private String nombreProceso;
    private Date fechaDesde;
    private Date fechaHasta;
    private String proveedor;    
    private EstadoContrato estadoContratoOC;
    private String nroInsumo;   
    private String idToEliminar;
    
    @PostConstruct
    public void init() {
        filterTable();
    }
    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nroContratoOC = null;
        tipoContratoOC = null;
        nombreProceso = null;
        fechaDesde = null;
        fechaHasta = null;
        proveedor = null;
        estadoContratoOC = null;
        nroInsumo = null;
    }
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            
            Integer mNroContratoOC = NumberUtils.getIntegerONull(nroContratoOC);
            TipoContrato mTipoContratoOC = tipoContratoOC;
            String mNombreProceso = nombreProceso;
            Date mFechaDesde = fechaDesde;
            Date mFechaHasta = fechaHasta;
            String mProveedor = proveedor;   
            EstadoContrato mEstadoContratoOC = estadoContratoOC;
            Integer mNroInsumo = NumberUtils.getIntegerONull(nroInsumo);
            EstadoContrato mEstadoContrato = EstadoContrato.EN_CREACION_DENTRO_PROCESO;
            String mUsuarioCodigo = userInfo.getUserCod();
            
            String[] orderBy = {"id"};
            boolean[] asc = {false};

            IDataProvider dataProviderG = new ContratoDataProvider(contratoDelegate, mNroContratoOC, mTipoContratoOC, mNombreProceso, mFechaDesde, mFechaHasta, mProveedor, mEstadoContratoOC, mNroInsumo, mEstadoContrato, mUsuarioCodigo, orderBy, asc);
            LazyDataModel var = new GeneralLazyDataModel(dataProviderG);

            lazyModel = var;

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    /**
     * Elimina una acción central
     */
    public void eliminar() {
        try {
            accionCentralD.eleiminarAccionCentral(Integer.valueOf(idToEliminar));
            RequestContext.getCurrentInstance().execute("$('#confirmModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Deja un contrato en estado en ejecución
     * @param idContratoOC 
     */
    public void abrirContratoOC(Integer idContratoOC) {
        try {
            contratoDelegate.abrirContratoOC(idContratoOC);            
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("mainPanel");
        }
    }
    
    /**
     * Comprueba si el usuario logueado es el administrador de determinado contrato
     * @param contrato
     * @return 
     */
    public Boolean usuarioEsAdministradorDelContrato(ContratoOC contrato){
        return contratoDelegate.usuarioEsAdministradorDelContrato(contrato, userInfo.getUserCod());
    }

    //geters
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel<ContratoOC> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<ContratoOC> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getIdToEliminar() {
        return idToEliminar;
    }

    public void setIdToEliminar(String idToEliminar) {
        this.idToEliminar = idToEliminar;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public static Logger getLogger() {
        return logger;
    }

    public TipoContrato getTipoContratoOC() {
        return tipoContratoOC;
    }

    public void setTipoContratoOC(TipoContrato tipoContratoOC) {
        this.tipoContratoOC = tipoContratoOC;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public String getNroContratoOC() {
        return nroContratoOC;
    }

    public void setNroContratoOC(String nroContratoOC) {
        this.nroContratoOC = nroContratoOC;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public EstadoContrato getEstadoContratoOC() {
        return estadoContratoOC;
    }

    public void setEstadoContratoOC(EstadoContrato estadoContratoOC) {
        this.estadoContratoOC = estadoContratoOC;
    }    
    
    public String getNroInsumo() {
        return nroInsumo;
    }

    public void setNroInsumo(String nroInsumo) {
        this.nroInsumo = nroInsumo;
    }    

    // </editor-fold>
}
