/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.enums.EstadoPoliza;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.mb.impl.padq.ContratoOrdenCompra;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "polizaConsulta")
public class PolizaConsulta implements Serializable {

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
    UtilsMB utilsMB;

    @Inject
    ReporteDelegate reporteDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String codigoInsumo;
    private String idAnioFiscal;
    private EstadoPoliza estado;
    private String idFuenteRecursos;
    private String idCategoriaConvenio;
    private String idFuenteFinanciamiento;

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        codigoInsumo = null;
        idAnioFiscal = null;
        estado = null;
        idFuenteRecursos = null;
        idCategoriaConvenio = null;
        idFuenteFinanciamiento = null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(codigoInsumo)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuente.insumo.id", NumberUtils.getIntegerONull(codigoInsumo));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(idAnioFiscal)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuente.insumo.poa.anioFiscal.id", Integer.valueOf(idAnioFiscal));
                criterios.add(criterio);
            }

            if (estado != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", estado);
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(idFuenteRecursos)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuente.fuente.fuenteRecursos.id", Integer.valueOf(idFuenteRecursos));
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(idFuenteFinanciamiento)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuente.fuente.fuenteRecursos.fuenteFinanciamiento.id", Integer.valueOf(idFuenteFinanciamiento));
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(idCategoriaConvenio)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuente.fuente.categoriaConvenio.id", Integer.valueOf(idCategoriaConvenio));
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "estado", "monto", "fuente.insumo.poa.anioFiscal.anio", "fuente.insumo.poa.unidadTecnica.nombre", "fuente.insumo.id", "fuente.fuente.fuenteRecursos.nombre", "fuente.fuente.categoriaConvenio.nombre"};

            String className = PolizaDeConcentracion.class.getName();
            String[] orderBy = {"id"};
            boolean[] asc = {false};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);

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
     * Este método corresponde al evento de eliminación del objeto seleccionado.
     *
     * @param idToEliminar
     */
    public void eliminar(Integer idToEliminar) {
        try {
            accionCentralD.eleiminarAccionCentral(Integer.valueOf(idToEliminar));
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    
            /**
     * Genera y descarga el reporte de acta de anticipo
     *
     * @param actaId
     */
    public void generarPolizaConcentracion(Integer polizaId) {
        try {
            byte[] bytespdf = reporteDelegate.generarPolizaConcentracion(polizaId);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength(bytespdf.length);
            String fileName = "polizaConcentracion.pdf";

            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream output;
            try {
                output = ec.getResponseOutputStream();
                output.write(bytespdf);

            } catch (IOException ex) {
                Logger.getLogger(ContratoOrdenCompra.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            fc.responseComplete();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public PermisosMB getPermisosMB() {
        return permisosMB;
    }

    public void setPermisosMB(PermisosMB permisosMB) {
        this.permisosMB = permisosMB;
    }

    public String getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(String codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public EstadoPoliza getEstado() {
        return estado;
    }

    public String getIdFuenteFinanciamiento() {
        return idFuenteFinanciamiento;
    }

    public void setIdFuenteFinanciamiento(String idFuenteFinanciamiento) {
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
    }
    

    public void setEstado(EstadoPoliza estado) {
        this.estado = estado;
    }

    public static Logger getLogger() {
        return logger;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public String getIdFuenteRecursos() {
        return idFuenteRecursos;
    }

    public void setIdFuenteRecursos(String idFuenteRecursos) {
        this.idFuenteRecursos = idFuenteRecursos;
    }

    
    public String getIdCategoriaConvenio() {
        return idCategoriaConvenio;
    }

    public void setIdCategoriaConvenio(String idCategoriaConvenio) {
        this.idCategoriaConvenio = idCategoriaConvenio;
    }
    

    // </editor-fold>

}
