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
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.ImpuestoDelegate;
import gob.mined.siap2.web.delegates.impl.LineaEstrategicaDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "emitirComprobateRetencionImpuestoGen")
public class EmitirComprobateRetencionImpuestoGenerar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;

    @Inject
    LineaEstrategicaDelegate lineaEstrategicaD;

    @Inject
    ImpuestoDelegate impuestoDelegate;

    @Inject
    ContratoDelegate contratoDelegate;

    private boolean update = false;
    private Proveedor objeto;
    private Date fechaDesde;
    private Date fechaHasta;
    private Integer idImpuesto;

    private LazyDataModel lazyModel;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (Proveedor) emd.getEntityById(Proveedor.class.getName(), Integer.valueOf(id));
        }
        filterPagos();
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterPagos() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (objeto != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "contratoOC.procesoAdquisicionProveedor.proveedor.id", objeto.getId());
                criterios.add(criterio);
            }

            if (fechaDesde != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "fechaGeneracion", fechaDesde);
                criterios.add(criterio);
            }

            if (fechaHasta != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "fechaGeneracion", fechaHasta);
                criterios.add(criterio);
            }

            //LAS ACTAS APROBADAS
            MatchCriteriaTO criterioActa = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoActa.APROBADA);
            criterios.add(criterioActa);

            //LAS ACTAS QUE TIENEN QUEDAN
            MatchCriteriaTO quedanEmitido = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "quedanEmitido", Boolean.TRUE);
            criterios.add(quedanEmitido);

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

            String[] propiedades = {"id", "numeroComprobanteRecepcionPago.id", "nroActa", "numeroSolicitudPago", "tipo", "estado", "contratoOC.nroContrato", "contratoOC.procesoAdquisicionProveedor.proveedor.razonSocial", "fechaGeneracion", "montoRecibido"};

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
     * Redirige el sitio a la página de emisión de comprobante de retención de impuestos
     * @return 
     */
    public String cerrar() {
        return "emisionComprobanteRetencionImpuesto.xhtml?faces-redirect=true";
    }

    /**
     * Devuelve los estados de líneas
     * @return 
     */
    public EstadoComun[] getEstadoLineas() {
        return EstadoComun.values();
    }

    /**
     * Devuelve el monto retenido para un impuesto especifico
     * @param idActa
     * @param idImpuesto
     * @return 
     */
    public BigDecimal obtenerMontoRetenidoParaImpuesto(Integer idActa, Integer idImpuesto) {
        try {
            if (idImpuesto == null) {
                return null;
            }
            return impuestoDelegate.obtenerMontoRetenidoParaImpuesto(idActa, idImpuesto);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    /**
     * Devuelve el monto total de la retención de un impuesto
     * @return 
     */
    public BigDecimal calcularMontoTotal() {
        BigDecimal montoTotal = contratoDelegate.getMontoTotalParaComprobanteRetencionImpuestosPorProv(objeto.getId(), idImpuesto, fechaDesde, fechaHasta);

        return montoTotal;
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public LineaEstrategicaDelegate getLineaEstrategicaD() {
        return lineaEstrategicaD;
    }

    public void setLineaEstrategicaD(LineaEstrategicaDelegate lineaEstrategicaD) {
        this.lineaEstrategicaD = lineaEstrategicaD;
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public Proveedor getObjeto() {
        return objeto;
    }

    public void setObjeto(Proveedor objeto) {
        this.objeto = objeto;
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

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
