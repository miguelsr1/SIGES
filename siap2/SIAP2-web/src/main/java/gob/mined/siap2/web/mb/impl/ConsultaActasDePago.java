/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "consultaActasDePago")
public class ConsultaActasDePago implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    PlanificacionEstrategicaDelegate planificacion;
    @Inject
    ReporteDelegate reporteDelegate;
    @Inject
    ContratoDelegate contratoDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nroActa;
    private String nroContrato;
    private Date fechaGeneracionDesde;
    private Date fechaGeneracionHasta;
    private String nit;
    private TipoActaContrato tipoActa;

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nroActa = null;
        nroContrato = null;
        fechaGeneracionDesde = null;
        fechaGeneracionHasta = null;
        tipoActa = null;
        nit = null;

    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<>();
            if (!TextUtils.isEmpty(nroActa)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nroActa", NumberUtils.getIntegerONull(nroActa));
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nroContrato)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "contratoOC.nroContrato", NumberUtils.getIntegerONull(nroContrato));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(nit)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS,
                        "contratoOC.procesoAdquisicionProveedor.proveedor.razonSocial", nit);
                criterios.add(criterio);
            }
            if (tipoActa != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo", tipoActa);
                criterios.add(criterio);
            }

            if (fechaGeneracionDesde != null) {
                
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "fechaGeneracion", fechaGeneracionDesde);
                criterios.add(criterio);
            }
            
            if (fechaGeneracionHasta != null) {
                
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "fechaGeneracion", fechaGeneracionHasta);
                criterios.add(criterio);
            }

            MatchCriteriaTO criterioActa = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoActa.APROBADA);
            criterios.add(criterioActa);

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

            String[] propiedades = {"id", "numeroComprobanteRecepcionPago.id", "nroActa", "numeroSolicitudPago", "tipo", "estado",
                "contratoOC.nroContrato", "contratoOC.procesoAdquisicionProveedor.proveedor.razonSocial", "fechaGeneracion", "montoRecibido"};

            String className = ActaContrato.class.getName();
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
     * Devuelve los tipos de actas existentes
     * @return 
     */
    public TipoActaContrato[] getTipoActas() {
        return TipoActaContrato.values();
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public PlanificacionEstrategicaDelegate getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(PlanificacionEstrategicaDelegate planificacion) {
        this.planificacion = planificacion;
    }

    public String getNroActa() {
        return nroActa;
    }

    public void setNroActa(String nroActa) {
        this.nroActa = nroActa;
    }

    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
    }

    public Date getFechaGeneracionDesde() {
        return fechaGeneracionDesde;
    }

    public void setFechaGeneracionDesde(Date fechaGeneracionDesde) {
        this.fechaGeneracionDesde = fechaGeneracionDesde;
    }

    public Date getFechaGeneracionHasta() {
        return fechaGeneracionHasta;
    }

    public void setFechaGeneracionHasta(Date fechaGeneracionHasta) {
        this.fechaGeneracionHasta = fechaGeneracionHasta;
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

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public TipoActaContrato getTipoActa() {
        return tipoActa;
    }

    public void setTipoActa(TipoActaContrato tipoActa) {
        this.tipoActa = tipoActa;
    }

    // </editor-fold>
}
