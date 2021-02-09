/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioIndicador;
import gob.mined.siap2.entities.data.impl.Categoria;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.TechoAccionCentral;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.AND_TO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaIndicadorDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "programaIndicadorCE")
public class ProgramaIndicadorCrearEditar  extends SelectOneUTBean implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    UtilsMB utilsMB;
    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    ProgramaIndicadorDelegate programaIndicadorDelegate;

    private boolean update = false;
    private ProgramaIndicador objeto;
    private TechoAccionCentral tempMontoUnidadTecnica;
    private PlanificacionEstrategica planificacionEstrategica;

    private String tipoPrograma = "P";
    private String idPrograma;
    private String idIndicador;
    private String idTipoIndicador;

    private static Comparator<AnioIndicador> ordenadorAnios = new Comparator<AnioIndicador>() {
        @Override
        public int compare(AnioIndicador anio1, AnioIndicador anio2) {
            return anio1.getAnio().compareTo(anio2.getAnio());
        }
    };

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = versionDelegate.getProgramaIndicador(Integer.valueOf(id));
            if (objeto.getPrograma() instanceof ProgramaInstitucional) {
                tipoPrograma = "I";
            } else {
                ProgramaPresupuestario p = (ProgramaPresupuestario) objeto.getPrograma();
                if (p.getProgramaPresupuestario() == null) {
                    tipoPrograma = "P";
                } else {
                    tipoPrograma = "SP";
                }
            }
            idPrograma = String.valueOf(objeto.getPrograma().getId());
            idIndicador = String.valueOf(objeto.getIndicador().getId());
            idTipoIndicador = String.valueOf(objeto.getIndicador().getTipo().getId());

            reloadAnios();
        } else {
            objeto = new ProgramaIndicador();
            objeto.setAnioIndicadors(new LinkedList());
        }

        unidadTecnicaSelecionada = objeto.getUtResponsable();
    }

    /**
     * Recarga de forma ordenada la lista de años de un programa indicador
     */
    public void reloadAnios() {
        Collections.sort(objeto.getAnioIndicadors(), ordenadorAnios);
        if (!TextUtils.isEmpty(idPrograma)) {
            Programa p = (Programa) emd.getEntityById(Programa.class.getName(), Integer.valueOf(idPrograma));
            planificacionEstrategica = p.getPlanificacionEstrategica();

            if (objeto.getAnioIndicadors().isEmpty()) {
                Integer iter = DatesUtils.getYearOfDate(planificacionEstrategica.getDesde());
                Integer fin = DatesUtils.getYearOfDate(planificacionEstrategica.getHasta());

                while (iter <= fin) {
                    AnioIndicador anio = new AnioIndicador();
                    anio.setAnio(iter);
                    anio.setProgramaIndicador(objeto);
                    objeto.getAnioIndicadors().add(anio);
                    iter = iter + 1;
                }

            } else {
                Integer iter = DatesUtils.getYearOfDate(planificacionEstrategica.getDesde());
                Integer fin = getActualStartDate();
                while (iter < fin) {
                    AnioIndicador anio = new AnioIndicador();
                    anio.setAnio(iter);
                    anio.setProgramaIndicador(objeto);
                    objeto.getAnioIndicadors().add(anio);
                    iter = iter + 1;
                }

                iter = getActualEndDate() + 1;
                fin = DatesUtils.getYearOfDate(planificacionEstrategica.getHasta());
                while (iter <= fin) {
                    AnioIndicador anio = new AnioIndicador();
                    anio.setAnio(iter);
                    anio.setProgramaIndicador(objeto);
                    objeto.getAnioIndicadors().add(anio);
                    iter = iter + 1;
                }

            }
            Collections.sort(objeto.getAnioIndicadors(), ordenadorAnios);
        }
    }

    /**
     * Borra la lista de años de un programa indicador y luego la recarga
     */
    public void changePlanificacion() {
        objeto.getAnioIndicadors().clear();
        reloadAnios();
    }

    
     /**
      * Guarda el programa indicador en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            objeto.setUtResponsable(unidadTecnicaSelecionada);
            programaIndicadorDelegate.crearOActualizarProgramaIndicador(Integer.valueOf(idPrograma), Integer.valueOf(idIndicador), objeto);
            return "consultaProgramaIndicador.xhtml?faces-redirect=true";
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
     * Dirige el sitio a la página de consulta de programas indicadores
     * @return 
     */
    public String cerrar() {
        return "consultaProgramaIndicador.xhtml?faces-redirect=true";
    }

    /**
     * Devuelve un Map que contiene: programas (institucionales o presupuestarios) y sus respectivos id
     * @return 
     */
    public Map<String, String> getProgramas() {
        Map<String, String> lineas = new LinkedHashMap<>();

        //vigente
        MatchCriteriaTO criterioVigente = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoComun.VIGENTE);
            //que no sean subprogramas
        MatchCriteriaTO noSubprograma = null;
        if (tipoPrograma.equals("I")) {
            noSubprograma = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "programaInstitucional", 1);
        } else if (tipoPrograma.equals("P")) {
            noSubprograma = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "programaPresupuestario", 1);
        } else if (tipoPrograma.equals("SP")) {
            noSubprograma = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "programaPresupuestario", 1);
        }           
        AND_TO criterio = CriteriaTOUtils.createANDTO(criterioVigente, noSubprograma);

        String className;
        if (tipoPrograma.equals("I")) {
            className = ProgramaInstitucional.class.getName();
        } else {
            className = ProgramaPresupuestario.class.getName();
        }

        String[] orderBy = {"nombre"};
        boolean[] asc = {true};
        String[] propiedades = {"id", "nombre"};
        List<EntityReference<Integer>> ll = emd.getEntitiesReferenceByCriteria(className, criterio, null, null, propiedades, orderBy, asc);
        for (EntityReference l : ll) {
            lineas.put((String) l.getPropertyMap().get("nombre"), String.valueOf(l.getPropertyMap().get("id")));
        }

        return lineas;
    }

    /**
     * Devuelve un Map que contiene: indicadores y sus respectivos id
     * @return 
     */
    public Map getIndicadores() {
        Map map = new LinkedHashMap();
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            CriteriaTO estado = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoComun.VIGENTE);
            criterios.add(estado);

            if (!TextUtils.isEmpty(idTipoIndicador)) {
                MatchCriteriaTO mismoTipo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo.id", Integer.parseInt(idTipoIndicador));
                criterios.add(mismoTipo);
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            }

            String[] orderBy = {"nombre"};
            boolean[] ascending = {true};
            String className = Indicador.class.getName();

            String[] propiedades = {"id", "nombre"};
            List<EntityReference<Integer>> ll = emd.getEntitiesReferenceByCriteria(className, condicion, null, null, propiedades, orderBy, ascending);
            for (EntityReference l : ll) {
                map.put((String) l.getPropertyMap().get("nombre"), String.valueOf(l.getPropertyMap().get("id")));
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return map;
    }

    /**
     * Devuelve el nombre de la unidad de medida de un indicador
     * @return 
     */
    public String getUnidadMedida() {
        if (!TextUtils.isEmpty(idIndicador)) {
            Indicador i = (Indicador) emd.getEntityById(Indicador.class.getName(), Integer.valueOf(idIndicador));
            if (i.getUnidadDeMedida() != null) {
                return i.getUnidadDeMedida().getNombre();
            }
        }
        return " ";
    }

    /**
     * Devuelve la lista de tipos de seguimiento
     * @return 
     */
    public TipoSeguimiento[] getTipoSeguimiento() {
        return TipoSeguimiento.values();
    }

    /**
     * Devuelve el año de inicio de un programa indicador
     * @return 
     */
    private Integer getActualStartDate() {
        if (objeto.getAnioIndicadors().isEmpty()) {
            return null;
        }
        return objeto.getAnioIndicadors().get(0).getAnio();
    }

    /**
     * Devuelve el año de fin de un programa indicador
     * @return 
     */
    private Integer getActualEndDate() {
        if (objeto.getAnioIndicadors().isEmpty()) {
            return null;
        }
        return objeto.getAnioIndicadors().get(objeto.getAnioIndicadors().size() - 1).getAnio();
    }

    /**
     * Devuelve un Map con todas las categorías existentes y sus respectivos id
     * @return 
     */
    public Map getTipoIndicador() {
        Map categorias = new LinkedHashMap();
        List<Categoria> ll = emd.getEntities(Categoria.class.getName(), "nombre");
        for (Categoria p : ll) {
            categorias.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return categorias;
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

    public String getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(String idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getTipoPrograma() {
        return tipoPrograma;
    }

    public void setTipoPrograma(String tipoPrograma) {
        this.tipoPrograma = tipoPrograma;
    }

    public String getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(String idPrograma) {
        this.idPrograma = idPrograma;
    }

    public TechoAccionCentral getTempMontoUnidadTecnica() {
        return tempMontoUnidadTecnica;
    }

    public void setTempMontoUnidadTecnica(TechoAccionCentral tempMontoUnidadTecnica) {
        this.tempMontoUnidadTecnica = tempMontoUnidadTecnica;
    }

    public ProgramaIndicador getObjeto() {
        return objeto;
    }

    public PlanificacionEstrategica getPlanificacionEstrategica() {
        return planificacionEstrategica;
    }

    public void setPlanificacionEstrategica(PlanificacionEstrategica planificacionEstrategica) {
        this.planificacionEstrategica = planificacionEstrategica;
    }

    public void setObjeto(ProgramaIndicador objeto) {
        this.objeto = objeto;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getIdTipoIndicador() {
        return idTipoIndicador;
    }

    public void setIdTipoIndicador(String idTipoIndicador) {
        this.idTipoIndicador = idTipoIndicador;
    }

    public static Comparator<AnioIndicador> getOrdenadorAnios() {
        return ordenadorAnios;
    }

    public static void setOrdenadorAnios(Comparator<AnioIndicador> ordenadorAnios) {
        ProgramaIndicadorCrearEditar.ordenadorAnios = ordenadorAnios;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
