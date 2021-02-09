/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.POAProyectoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.dataProvider.EntityDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
@Named(value = "procesosCierre")
public class ProcesosCierre implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    UsuarioInfo usuarioInfo;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    private GeneralDelegate generalDelegate;
    @Inject
    private POAProyectoBean pOAProyectoBean;

    private LazyDataModel poasPendientesCerrarlazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private SofisComboG<AnioFiscal> comboAniosFiscales = new SofisComboG<>();

    @PostConstruct
    public void init() {
        actualizarCombos();
        filtrar();
    }

    /**
     * Actualiza el combo de años fiscales en ejecución
     */
    private void actualizarCombos() {
        Collection<AnioFiscal> anios = generalDelegate.obtenerAnioFiscalEnEjecucion();
        this.comboAniosFiscales = new SofisComboG<>(new ArrayList(anios), "nombre");
        this.comboAniosFiscales.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);

    }
    
    public void cleanObjects(){
        actualizarCombos();
        filtrar();
    }
    
    /**
     * Llama al método que filtra la tabla de POAs pendientes de cierre
     */
    public void filtrar() {
        filterTablePoasPEndientesACerrar();
    }

    /**
     * Regenera la tabla POAs pendientes de cierre según los filtros aplicados
     */
    public void filterTablePoasPEndientesACerrar() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (comboAniosFiscales.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", comboAniosFiscales.getSelectedT().getId());
                criterios.add(criterio);
            }

            MatchCriteriaTO criterioCierre = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cierreAnual", Boolean.FALSE);
            criterios.add(criterioCierre);

            MatchCriteriaTO lineaTrabajo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "lineaTrabajo", 1);
            criterios.add(lineaTrabajo);

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

            String className = GeneralPOA.class.getName();
            String[] orderBy = {"id"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityDataProvider(/*propiedades, */className, condicion, orderBy, asc);
            poasPendientesCerrarlazyModel = new GeneralLazyDataModel(dataProvider);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }


    /**
     * Acción correspondiente al cierre de Planificación
     *
     * @return
     */
    public String cierrePlanificacion() {
        return null;
    }

    /**
     * Acción correspondiente al cierre de Presupuesto.
     *
     */
    public void cierrePresupuesto() {
        try {
            if (comboAniosFiscales.getSelectedT() != null) {
                pOAProyectoBean.actualizarCierrePresupuesto(comboAniosFiscales.getSelectedT().getId());
                jSFUtils.agregarInfo(jSFUtils.getMensajeByKey("labels.AnioFiscalCerrado")); 
                filtrar();
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        }
    }
    
    public void verificarAnioFiscalSeleccionado(){
        if (comboAniosFiscales.getSelectedT() != null) {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
        }else{
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_ANIO_FISCAL_NO_SELECCIONADO);
        }
    }
    
    
    

    public SofisComboG<AnioFiscal> getComboAniosFiscales() {
        return comboAniosFiscales;
    }

    public void setComboAniosFiscales(SofisComboG<AnioFiscal> comboAniosFiscales) {
        this.comboAniosFiscales = comboAniosFiscales;
    }

    public LazyDataModel getPoasPendientesCerrarlazyModel() {
        return poasPendientesCerrarlazyModel;
    }

    public void setPoasPendientesCerrarlazyModel(LazyDataModel poasPendientesCerrarlazyModel) {
        this.poasPendientesCerrarlazyModel = poasPendientesCerrarlazyModel;
    }

}
