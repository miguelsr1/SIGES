/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;


import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ReprogramacionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
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
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "consultaReprogramacionAsignacionNoProgramable")
public class ConsultaReprogramacionAsignacionNoProgramable implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    UtilsMB utilsMB;
    @Inject
    TextMB textMB;
    @Inject
    ReprogramacionDelegate reprogramacionDelegate;
    @Inject
    UsuarioInfo userInfo;


    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private EstadoReprogramacion estado;
    private String unidadTecnicaId;

    @PostConstruct
    public void init() {
        initFilter();
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        estado = null;
        unidadTecnicaId= null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (estado != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", estado);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(unidadTecnicaId)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rep_detalle_lista.poa.unidadTecnica.id", Integer.valueOf(unidadTecnicaId));
                criterios.add(criterio);
            }

            //tipo de reprogramacion
            MatchCriteriaTO criterioTipo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipoReprogramacion", TipoReprogramacion.ASIGNACION_NO_PROGRAMABLE);
            criterios.add(criterioTipo);
            //reprogramaciones a las que tiene acceso
            if (!permisosMB.tieneOperacion(ConstantesEstandares.Operaciones.REPROGRAMAR_TODAS_UT)){                
                if (userInfo.getUnidadTecnicas().isEmpty()) {
                    CriteriaTO condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "id", 1);
                    criterios.add(condicion);
                } else {
                    ArrayList<CriteriaTO> listaUT = new ArrayList(userInfo.getUnidadTecnicas().size());
                    for (UnidadTecnica ut : userInfo.getUnidadTecnicas()) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rep_detalle_lista.poa.unidadTecnica.id", ut.getId());
                        listaUT.add(criterio);
                    }
                    if (listaUT.size() == 1) {
                        criterios.add(listaUT.get(0));
                    } else {
                        CriteriaTO condicion = CriteriaTOUtils.createORTO(listaUT.toArray(new CriteriaTO[0]));
                        criterios.add(condicion);
                    }
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
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "estado", "tipoReprogramacion", "ultMod", "ultUsuario"};

            String className = Reprogramacion.class.getName();
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

    public EstadoReprogramacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoReprogramacion estado) {
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

    public String getUnidadTecnicaId() {
        return unidadTecnicaId;
    }

    public void setUnidadTecnicaId(String unidadTecnicaId) {
        this.unidadTecnicaId = unidadTecnicaId;
    }
    

    // </editor-fold>
}
