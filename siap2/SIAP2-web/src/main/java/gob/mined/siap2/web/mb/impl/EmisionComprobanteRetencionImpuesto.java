/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
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
@Named(value = "emisionComprobanteRetencionImpuesto")
public class EmisionComprobanteRetencionImpuesto implements Serializable {

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

    String nombreProveedor;
    String nitProveedor;
    String idProveedor;
    String nombreRepresentante;
    String apellidosRepresentante;

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombreProveedor = null;
        nitProveedor = null;
        idProveedor = null;
        nombreRepresentante = null;
        apellidosRepresentante = null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(nombreProveedor)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreComercial", nombreProveedor);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nitProveedor)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nitOferente", nitProveedor);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(idProveedor) && TextUtils.isInteger(idProveedor)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "idOferente", Integer.valueOf(idProveedor));
                criterios.add(criterio);
            } else if (!TextUtils.isEmpty(idProveedor) && !TextUtils.isInteger(idProveedor)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_ID_OFERENTE_INVALIDO);
                throw b;
            }

            if (!TextUtils.isEmpty(nombreRepresentante)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreRepresentante", nombreRepresentante);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(apellidosRepresentante)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "apellidosRepresentante", apellidosRepresentante);
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

            String[] propiedades = {"id", "idOferente", "nitOferente", "nombreRepresentante", "apellidosRepresentante", "razonSocial", "personaJuridica", "nombreComercial", "pais", "activo"};

            String className = Proveedor.class.getName();
            String[] orderBy = {"nombreComercial"};
            boolean[] asc = {false};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
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

    public ReporteDelegate getReporteDelegate() {
        return reporteDelegate;
    }

    public void setReporteDelegate(ReporteDelegate reporteDelegate) {
        this.reporteDelegate = reporteDelegate;
    }

    public ContratoDelegate getContratoDelegate() {
        return contratoDelegate;
    }

    public void setContratoDelegate(ContratoDelegate contratoDelegate) {
        this.contratoDelegate = contratoDelegate;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getApellidosRepresentante() {
        return apellidosRepresentante;
    }

    public void setApellidosRepresentante(String apellidosRepresentante) {
        this.apellidosRepresentante = apellidosRepresentante;
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

    // </editor-fold>
}
