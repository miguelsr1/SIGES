/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.sofisform.to.OR_TO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
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
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza la consulta de los POA de proyecto.
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "poaProyectosConsulta")
public class POAParaProyectosConsulta implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    UsuarioInfo usuarioInfo;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    VersionesDelegate versionDelegate;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    PlanificacionEstrategicaDelegate planificacionDelegate;
    @Inject
    ProyectoDelegate progDelegate;
    @Inject
    PermisosMB permisosMB;
    @Inject
    UtilsMB utilsMB;


    private LazyDataModel lazyModel;
    private String nombre;
    private TipoProyecto tipoProyecto;

    private String idPlanificacion;
    private String idLinea;
    private String idProgramaPresupuestario;
    private String idProgramaInstitucional;
    private String unidadTecnicaId;
    private String tmpIdFuenteFinanciamiento;
    private String tmpIdFuenteRecurso;
    private List<UnidadTecnica> usuarioUnidadTecnicas;
    private String tipoRelacion;

    private static final String PROPIETARIO = "PROPIETARIO";
    private static final String COLABORADOR = "COLABORADOR";

    /**
     * Creates a new instance of ProgramaInstitucionalMB
     */
    public POAParaProyectosConsulta() {
    }

    @PostConstruct
    public void init() {
        usuarioUnidadTecnicas = usuarioInfo.getUnidadTecnicas();
        initFilter();
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombre = null;
        tipoProyecto = null;
        idPlanificacion = null;
        idLinea = null;
        idProgramaPresupuestario = null;
        idProgramaInstitucional = null;
        unidadTecnicaId = null;
        tmpIdFuenteFinanciamiento = null;
        tmpIdFuenteRecurso = null;
        tipoRelacion = PROPIETARIO;
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
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

            if (tipoProyecto != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipoProyecto", tipoProyecto);
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(idProgramaPresupuestario)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaPresupuestario.id", Integer.valueOf(idProgramaPresupuestario));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(idProgramaInstitucional)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaInstitucional.id", Integer.valueOf(idProgramaInstitucional));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(unidadTecnicaId)) {
                MatchCriteriaTO criterioComponente = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "proyectoComponentes.unidadTecnica.id", Integer.valueOf(unidadTecnicaId));
                MatchCriteriaTO criterioMacroActividades = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "proyectoMacroactividad.unidadTecnica.id", Integer.valueOf(unidadTecnicaId));
                OR_TO or = CriteriaTOUtils.createORTO(criterioComponente, criterioMacroActividades);
                criterios.add(or);
            }

            if (!TextUtils.isEmpty(tmpIdFuenteFinanciamiento)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuentesProyectos.fuenteFinanciamiento.id", Integer.valueOf(tmpIdFuenteFinanciamiento));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(tmpIdFuenteRecurso)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuentesProyectos.fuenteRecursos.id", Integer.valueOf(tmpIdFuenteRecurso));
                criterios.add(criterio);
            }

            //si no tien UT NO TRAE NADA
            if (usuarioUnidadTecnicas.isEmpty()) {
                //añade condicion que no se cumpla
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "id", 1);
                criterios.add(criterio);
                
            //si  es de unidad tecnica trae los proyectos de esa UT   
            } else if (tipoRelacion.equals(PROPIETARIO)) {
                List<CriteriaTO> ortos = new LinkedList();
                for (UnidadTecnica ut : usuarioUnidadTecnicas) {
                    MatchCriteriaTO tineUT = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pOAProyectos.lineas.producto.unidadTecnica.id", ut.getId());
                    ortos.add(tineUT);
                }
                CriteriaTO condicion =null;
                if (ortos.size() == 1){
                    condicion =ortos.get(0);
                }else{
                    condicion = CriteriaTOUtils.createORTO(ortos.toArray(new CriteriaTO[ortos.size()]));                    
                }
                criterios.add(condicion);

            //sino quiere los proyectos de los que es colaborador    
            } else {
                List<CriteriaTO> ortos = new LinkedList();
                for (UnidadTecnica ut : usuarioUnidadTecnicas) {
                    MatchCriteriaTO tineUT = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pOAProyectos.lineas.colaboradoras.id", ut.getId());
                    ortos.add(tineUT);
                }
                CriteriaTO condicion =null;
                if (ortos.size() == 1){
                    condicion =ortos.get(0);
                }else{
                    condicion = CriteriaTOUtils.createORTO(ortos.toArray(new CriteriaTO[ortos.size()]));                    
                }
                criterios.add(condicion);

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

            String[] propiedades = {"id", "nombre", "tipoProyecto", "programaPresupuestario.nombre", "programaInstitucional.nombre", "inicio", "fin", "codigoSIIP", "tipoEstructuraComponente", "tipoEstructuraMacroactividad"};

            String className = Proyecto.class.getName();
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

  

   
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public String getUnidadTecnicaId() {
        return unidadTecnicaId;
    }

    public String getTmpIdFuenteFinanciamiento() {
        return tmpIdFuenteFinanciamiento;
    }

    public void setTmpIdFuenteFinanciamiento(String tmpIdFuenteFinanciamiento) {
        this.tmpIdFuenteFinanciamiento = tmpIdFuenteFinanciamiento;
    }

    public String getTmpIdFuenteRecurso() {
        return tmpIdFuenteRecurso;
    }

    public void setTmpIdFuenteRecurso(String tmpIdFuenteRecurso) {
        this.tmpIdFuenteRecurso = tmpIdFuenteRecurso;
    }

    public void setUnidadTecnicaId(String unidadTecnicaId) {
        this.unidadTecnicaId = unidadTecnicaId;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(String idLinea) {
        this.idLinea = idLinea;
    }

    public String getIdProgramaPresupuestario() {
        return idProgramaPresupuestario;
    }

    public void setIdProgramaPresupuestario(String idProgramaPresupuestario) {
        this.idProgramaPresupuestario = idProgramaPresupuestario;
    }

    public String getIdProgramaInstitucional() {
        return idProgramaInstitucional;
    }

    public void setIdProgramaInstitucional(String idProgramaInstitucional) {
        this.idProgramaInstitucional = idProgramaInstitucional;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public List<UnidadTecnica> getUsuarioUnidadTecnicas() {
        return usuarioUnidadTecnicas;
    }

    public void setUsuarioUnidadTecnicas(List<UnidadTecnica> usuarioUnidadTecnicas) {
        this.usuarioUnidadTecnicas = usuarioUnidadTecnicas;
    }

    public String getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    // </editor-fold>
}
