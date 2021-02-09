/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.AccionCentralDelegate;
import gob.mined.siap2.web.delegates.impl.UACIDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
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
@Named(value = "metodoAdquisicionConsulta")
public class MetodoAdquisicionConsulta implements Serializable {

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
    UACIDelegate uaciDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nombre;
    private String planificacionEstrategicaId;
    private String lineaEstrategicaId;
    private boolean habilitado = true;
    private String tipoUACIoNoUACIoTodos;

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombre = null;
        habilitado = true;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", nombre);
                criterios.add(criterio);
            }

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", habilitado);
            criterios.add(criterio);

            if (!TextUtils.isEmpty(tipoUACIoNoUACIoTodos) && !tipoUACIoNoUACIoTodos.equals("Todos")) {
                if (tipoUACIoNoUACIoTodos.equals("UACI")) {
                    MatchCriteriaTO criterioTipo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "esUACI", Boolean.TRUE);
                    criterios.add(criterioTipo);
                } else {
                    List<CriteriaTO> criteriosNoUACI = new ArrayList<CriteriaTO>();    
                    
                    MatchCriteriaTO criterioTipo1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "esUACI", Boolean.FALSE);     
                    criteriosNoUACI.add(criterioTipo1);
                    MatchCriteriaTO criterioTipo2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "esUACI", 0);
                    criteriosNoUACI.add(criterioTipo2);
                    
                    CriteriaTO criterioTipoNoUACI = CriteriaTOUtils.createORTO(criteriosNoUACI.toArray(new CriteriaTO[0]));
                    criterios.add(criterioTipoNoUACI);
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

            String[] propiedades = {"id", "nombre", "habilitado", "esUACI"};

            String className = MetodoAdquisicion.class.getName();
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

    
    /**
     * Este método corresponde al evento de eliminación del objeto seleccionado.
     *
     * @param idToEliminar
     */
    public void eliminar(Integer idToEliminar) {
        try {
            Integer id = Integer.valueOf(idToEliminar);
            List<String> nombresPacs = uaciDelegate.obtenerNombresPacsAsociados(id);
            if (nombresPacs.isEmpty()) {
                uaciDelegate.eliminarMetodoAdquisicion(id);
                RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
            } else {
                String texto = "";
                int i = 0;
                for (String nombresPac : nombresPacs) {
                    if (i == 0) {
                        texto += nombresPac;
                    } else {
                        texto += ", " + nombresPac;
                    }

                    i++;
                }

                BusinessException b = new BusinessException();
                String[] values = {texto};
                b.addError(ConstantesErrores.ERR_ADQUISICION_DELETE_PACS_ASOCIADOS, values);
                throw b;

            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un método de adquisición
     * @param id 
     */
    public void eliminarMetodo(Integer id) {
        try {
            List<String> nombresPacs = uaciDelegate.obtenerNombresPacsAsociados(id);
            if (nombresPacs.isEmpty()) {
                uaciDelegate.eliminarMetodoAdquisicion(id);

                RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
            } else {
                String texto = "";
                int i = 0;
                for (String nombresPac : nombresPacs) {
                    if (i == 0) {
                        texto += nombresPac;
                    } else {
                        texto += ", " + nombresPac;
                    }

                    i++;
                }

                BusinessException b = new BusinessException();
                String[] values = {texto};
                b.addError(ConstantesErrores.ERR_ADQUISICION_DELETE_PACS_ASOCIADOS, values);
                throw b;

            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
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

    public String getNombre() {
        return nombre;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getTipoUACIoNoUACIoTodos() {
        return tipoUACIoNoUACIoTodos;
    }

    public void setTipoUACIoNoUACIoTodos(String tipoUACIoNoUACIoTodos) {
        this.tipoUACIoNoUACIoTodos = tipoUACIoNoUACIoTodos;
    }

    // </editor-fold>
}
