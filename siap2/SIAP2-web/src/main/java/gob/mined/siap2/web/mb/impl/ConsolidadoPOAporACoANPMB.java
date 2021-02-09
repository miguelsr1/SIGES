/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UtilsMB;
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
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions (www.sofis-solutions.com)
 */
@ViewScoped
@Named(value = "consolidadoPOAporACoANPMB")
public class ConsolidadoPOAporACoANPMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    UtilsMB utilsMB;

    private LazyDataModel lazyModel;

    private String nombre;
    private String planificacionEstrategicaId;
    private String lineaEstrategicaId;

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombre = null;
        planificacionEstrategicaId = null;
        lineaEstrategicaId = null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<>();
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", nombre);
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(planificacionEstrategicaId)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                        "planificacionEstrategica.id", Integer.valueOf(planificacionEstrategicaId));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(lineaEstrategicaId)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                        "lineasEstrategicas.id", Integer.valueOf(lineaEstrategicaId));
                criterios.add(criterio);
            }

            MatchCriteriaTO criterioEstado = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poas.estado", EstadoPOA.CONSOLIDANDO_POA);
            criterios.add(criterioEstado);            
            MatchCriteriaTO criterioLineaBase = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "poas.lineaTrabajo", 1);
            criterios.add(criterioLineaBase);
            
            CriteriaTO condicion;
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

            String[] propiedades = {"id", "nombre", "tipo"};

            String className = ConMontoPorAnio.class.getName();
            String[] orderBy = {"nombre"};
            boolean[] asc = {true};

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


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlanificacionEstrategicaId() {
        return planificacionEstrategicaId;
    }

    public void setPlanificacionEstrategicaId(String planificacionEstrategicaId) {
        this.planificacionEstrategicaId = planificacionEstrategicaId;
    }

    public String getLineaEstrategicaId() {
        return lineaEstrategicaId;
    }

    public void setLineaEstrategicaId(String lineaEstrategicaId) {
        this.lineaEstrategicaId = lineaEstrategicaId;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }
}
