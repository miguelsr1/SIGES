/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PeriodoSeguimientoIndicadores;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.sofisform.to.OR_TO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
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
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "periodoDeSeguimientoDeIndicadoresConsulta")
public class PeriodoDeSeguimientoDeIndicadoresConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private Date fechaDesde = null;
    private Date fechaHasta = null;
    private Boolean aplicaProyectosAdministrarivos = Boolean.TRUE;
    private Boolean aplicaProyectosDeInversion = Boolean.TRUE;

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        fechaDesde = null;
        fechaHasta = null;
        aplicaProyectosAdministrarivos = Boolean.TRUE;
        aplicaProyectosDeInversion = Boolean.TRUE;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (fechaDesde != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "desde", fechaDesde);
                criterios.add(criterio);
            }
            if (fechaHasta != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "hasta", fechaHasta);
                criterios.add(criterio);
            }

            if (BooleanUtils.isTrue(aplicaProyectosDeInversion)) {

                if (BooleanUtils.isTrue(aplicaProyectosAdministrarivos)) {
                    MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplicaProyectosDeInversion", aplicaProyectosDeInversion);
                    MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplicaProyectosAdministrarivos", aplicaProyectosAdministrarivos);
                    OR_TO orTO = new OR_TO();
                    orTO.setCriteria1(criterio1);
                    orTO.setCriteria2(criterio2);
                    criterios.add(orTO);
                } else {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplicaProyectosDeInversion", aplicaProyectosDeInversion);
                    criterios.add(criterio);
                }
            }
            if (BooleanUtils.isTrue(aplicaProyectosAdministrarivos)) {
                if (!BooleanUtils.isTrue(aplicaProyectosDeInversion)) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplicaProyectosAdministrarivos", aplicaProyectosAdministrarivos);
                    criterios.add(criterio);
                }
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

            String[] propiedades = {"id", "desde", "hasta", "aplicaProyectosDeInversion", "aplicaProyectosAdministrarivos"};

            String className = PeriodoSeguimientoIndicadores.class.getName();
            String[] orderBy = {"desde"};
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
            emd.delete(PeriodoSeguimientoIndicadores.class, idToEliminar);
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public Boolean getAplicaProyectosAdministrarivos() {
        return aplicaProyectosAdministrarivos;
    }

    public void setAplicaProyectosAdministrarivos(Boolean aplicaProyectosAdministrarivos) {
        this.aplicaProyectosAdministrarivos = aplicaProyectosAdministrarivos;
    }

    public Boolean getAplicaProyectosDeInversion() {
        return aplicaProyectosDeInversion;
    }

    public void setAplicaProyectosDeInversion(Boolean aplicaProyectosDeInversion) {
        this.aplicaProyectosDeInversion = aplicaProyectosDeInversion;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
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

    // </editor-fold>
}
