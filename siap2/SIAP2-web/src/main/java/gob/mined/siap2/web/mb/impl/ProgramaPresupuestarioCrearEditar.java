/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.utils.AnioFiscalUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestarioTechoAnio;
import gob.mined.siap2.entities.data.impl.Supuesto;
import gob.mined.siap2.entities.data.impl.TechoProgramaPresupueastarioAnio;
import gob.mined.siap2.entities.data.impl.TechoProgramaPresupueastarioFuente;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "programaPresupuestarioCE")
public class ProgramaPresupuestarioCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    UtilsMB utilsMB;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    GeneralDelegate peled;
    @Inject
    ProgramaDelegate progDelegate;
    @Inject
    VersionesDelegate versionDelegate;

    private boolean update = false;
    private ProgramaPresupuestario objeto;
    private Supuesto supuesto;
    private TechoProgramaPresupueastarioFuente techo;
    protected DualListModel<LineaEstrategica> lineasEstrategicas;
    private Stack<ProgramaPresupuestario> stack = new Stack();

    private String idPlanificacion;
    private String idClasificadorFuncional;
    //línea del subprograma
    private String idLinea;
    private String idFuenteFinanciamiento;
    private String idFuenteRecurso;

    private List<Integer> aniosPlanificacion;
    private PlanificacionEstrategica planificacion;
    private String idToEliminar;
    private String tempProgInstId;
    private List<BigDecimal> totalesSuma;
    private List<BigDecimal> totalesAsignado;
    private List<BigDecimal> totalesDiferencia;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = versionDelegate.getProgramaPresupuestario(Integer.valueOf(id));
            if (objeto.getPlanificacionEstrategica() != null) {
                idPlanificacion = String.valueOf(objeto.getPlanificacionEstrategica().getId());
            }
            if (objeto.getClasificadorFuncional() != null) {
                idClasificadorFuncional = String.valueOf(objeto.getClasificadorFuncional().getId());
            }
            loadLineasDisponibles(true);
            loadTechosPrograma();
            //en caso de ser un subprograma carga la línea
            if (objeto.getProgramaPresupuestario() != null) {
                LineaEstrategica l = getLineaEstrategicaDeSubprograma(objeto);
                if (l != null) {
                    idLinea = String.valueOf(l.getId());
                }
            }
        } else {
            objeto = initPrograma();
            loadLineasDisponibles(false);
            loadTechosPrograma();
        }
    }

    /**
     * Guarda el objeto en edición
     *
     * @return
     */
    public String guardar() {
        try {
            //se carga el clasificador Funcioal
            if (TextUtils.isEmpty(idClasificadorFuncional)) {
                objeto.setClasificadorFuncional(null);
            } else {
                ClasificadorFuncional c = (ClasificadorFuncional) emd.getEntityById(ClasificadorFuncional.class.getName(), Integer.valueOf(idClasificadorFuncional));
                objeto.setClasificadorFuncional(c);
            }

            if (lineasEstrategicas.getTarget() == null || lineasEstrategicas.getTarget().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_AL_MENOS_UNA_LINEA_ESTRATEGICA);
                throw b;
            }

            //se remueven todos los años vacíos;
            for (TechoProgramaPresupueastarioFuente techoFuente : objeto.getTechos()) {
                Iterator<TechoProgramaPresupueastarioAnio> iterTechoAnio = techoFuente.getTechos().iterator();
                while (iterTechoAnio.hasNext()) {
                    TechoProgramaPresupueastarioAnio techo = iterTechoAnio.next();
                    if (techo.getAnioFiscal().getId() == null) {
                        iterTechoAnio.remove();
                    }
                }
            }

            if (objeto.getProgramaPresupuestario() == null) {
                //setea las lineas estratégicas en caso de no ser subprograma (si no las mantiene)
                objeto.setLineasEstrategicas(new HashSet(lineasEstrategicas.getTarget()));
            } else {
                //en caso de ser un subprograma setea la línea que eligió
                objeto.setLineasEstrategicas(new LinkedHashSet<LineaEstrategica>());
                if (!TextUtils.isEmpty(idLinea)) {
                    LineaEstrategica linea = (LineaEstrategica) emd.getEntityById(LineaEstrategica.class.getName(), Integer.valueOf(idLinea));
                    objeto.getLineasEstrategicas().add(linea);
                }
            }
            if (stack.isEmpty()) {
                progDelegate.crearActualizarProgramaPresupuestario(objeto, Integer.valueOf(idPlanificacion));
                return "consultaProgramaPresupuestario.xhtml?faces-redirect=true";
            } else {
                ProgramaPresupuestario padre = stack.pop();
                if (objeto.getProgramaPresupuestario() == null) {
                    objeto.setProgramaPresupuestario(padre);
                    padre.getProgramasPresupuestarios().add(objeto);
                }
                objeto = padre;
                loadTechosPrograma();
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        completarAnios();
        return null;
    }

    /**
     * Dirige el sitio a la consulta de programas presupuestarios
     *
     * @return
     */
    public String cerrar() {
        if (stack.isEmpty()) {
            return "consultaProgramaPresupuestario.xhtml?faces-redirect=true";
        } else {
            objeto = stack.pop();
        }
        return null;
    }

    /**
     * Carga una planificación estratégica
     */
    public void loadTechosPrograma() {
        if (!TextUtils.isEmpty(idPlanificacion)) {
            planificacion = (PlanificacionEstrategica) emd.getEntityById(PlanificacionEstrategica.class.getName(), Integer.valueOf(idPlanificacion));
        } else {
            planificacion = null;
        }
        completarAnios();
    }

    /**
     * Carga los años de una planificación estratégica y añade los techos del
     * programa presupuestario
     */
    public void completarAnios() {
        aniosPlanificacion = new LinkedList();
        if (planificacion != null) {
            //se cargan los anios de la planificación en una lista           
            Integer desde = DatesUtils.getYearOfDate(planificacion.getDesde());
            Integer hasta = DatesUtils.getYearOfDate(planificacion.getHasta());
            for (int anio = desde; anio <= hasta; anio++) {
                //busca el anio fiscal
                AnioFiscal anioFiscal = null;
                List<AnioFiscal> aniosFiscales = emd.findByOneProperty(AnioFiscal.class.getName(), "anio", anio);
                if (!aniosFiscales.isEmpty()) {
                    anioFiscal = aniosFiscales.get(0);
                } else {
                    anioFiscal = AnioFiscalUtils.crearAnioFiscal(anio);
                }
                aniosPlanificacion.add(anio);

                //se añade el techo para la direccion si no existe
                for (TechoProgramaPresupueastarioFuente techo : objeto.getTechos()) {
                    agrreglarTechoPara(techo, anioFiscal, anio - desde);
                }
            }
            recalcularTotales();
        }
    }

    /**
     * agrega un techo a la fuente para el año en la posición pasada por
     * parámetro
     *
     * @param direccion
     * @param anio
     * @param pos
     */
    private void agrreglarTechoPara(TechoProgramaPresupueastarioFuente techo, AnioFiscal anioFiscal, int pos) {
        int iter = 0;
        boolean encontro = false;
        List<TechoProgramaPresupueastarioAnio> montos = techo.getTechos();

        while (iter < montos.size()) {
            if (montos.get(iter).getAnioFiscal().getAnio().equals(anioFiscal.getAnio())) {
                encontro = true;
                break;
            }
            iter++;
        }
        //si lo encontro en otra posicion lo cambia de lugar
        if (encontro && iter != pos) {
            TechoProgramaPresupueastarioAnio techoAnio = montos.get(iter);
            montos.remove(iter);
            montos.add(pos, techoAnio);
        }

        if (!encontro) {
            TechoProgramaPresupueastarioAnio techoAnio = new TechoProgramaPresupueastarioAnio();
            techoAnio.setTechoProgramaPresupueastarioFuente(techo);
            techoAnio.setAnioFiscal(anioFiscal);
            techoAnio.setMonto(BigDecimal.ZERO);
            montos.add(pos, techoAnio);
        }
    }

    /**
     * Carga las líneas disponibles y techos de programa
     */
    public void cambioEnPlanificacion() {
        loadLineasDisponibles(false);
        loadTechosPrograma();
    }

    /**
     * Carga las líneas estratégicas disponibles
     *
     * @param conservarOriginales
     */
    public void loadLineasDisponibles(boolean conservarOriginales) {
        List<LineaEstrategica> disponibles = new LinkedList<>();
        if (!TextUtils.isEmpty(idPlanificacion)) {
            disponibles = versionDelegate.getLineasEstrategicasVigetnes(Integer.valueOf(idPlanificacion));
        }
        List<LineaEstrategica> enUso = new LinkedList<>();
        if (conservarOriginales) {
            ProgramaPresupuestario programa = objeto;
            if (objeto.getProgramaPresupuestario() != null) {
                programa = objeto.getProgramaPresupuestario();
            }
            for (LineaEstrategica l : programa.getLineasEstrategicas()) {
                enUso.add(l);
                if (disponibles.contains(l)) {
                    disponibles.remove(l);
                }
            }

        }
        lineasEstrategicas = new DualListModel(disponibles, enUso);
    }

    /**
     * Carga un subprograma
     *
     * @param index
     */
    public void loadSubprograma(String index) {
        ProgramaPresupuestario p;
        if (TextUtils.isEmpty(index)) {
            p = initPrograma();
        } else {
            p = objeto.getProgramasPresupuestarios().get(Integer.valueOf(index).intValue());
        }
        idLinea = null;
        //si es un subprograma y tiene línea se carga
        if (p.getProgramaPresupuestario() != null && !p.getLineasEstrategicas().isEmpty()) {
            idLinea = String.valueOf(getLineaEstrategicaDeSubprograma(p).getId());
        }
        stack.push(objeto);
        objeto = p;
        loadTechosPrograma();
        if (objeto.getClasificadorFuncional() != null) {
            idClasificadorFuncional = String.valueOf(objeto.getClasificadorFuncional().getId());
        } else {
            idClasificadorFuncional = null;
        }
    }

    /**
     * Devuelve la línea estratégica asociada a un subprograma
     *
     * @param subprograma
     * @return
     */
    public LineaEstrategica getLineaEstrategicaDeSubprograma(ProgramaPresupuestario subprograma) {
        if (subprograma.getLineasEstrategicas().isEmpty()) {
            return null;
        }
        Iterator iter = subprograma.getLineasEstrategicas().iterator();
        return (LineaEstrategica) iter.next();
    }

    /**
     * Devuelve un Map con las líneas estratégicas y sus respectivos id
     *
     * @return
     */
    public Map<String, String> getLineasParaSubprograma() {
        Map<String, String> map = new LinkedHashMap<>();
        List<LineaEstrategica> l = lineasEstrategicas.getTarget();
        for (LineaEstrategica iter : l) {
            map.put(iter.getNombre(), String.valueOf(iter.getId()));
        }
        return map;
    }

    /**
     * carga o recarga la fuente de financiamiento
     */
    public void reloadTecho() {
        if (techo == null) {
            idFuenteFinanciamiento = null;
            idFuenteRecurso = null;
            techo = new TechoProgramaPresupueastarioFuente();
            techo.setTechos(new LinkedList());
        } else {
            idFuenteFinanciamiento = String.valueOf(techo.getFuenteFinanciamiento().getId());
            idFuenteRecurso = String.valueOf(techo.getFuenteRecursos().getId());
        }
    }

    /**
     * Agrega un techo a un programa presupuestario
     */
    public void addTecho() {
        try {
            FuenteFinanciamiento ff = (FuenteFinanciamiento) emd.getEntityById(FuenteFinanciamiento.class.getName(), Integer.valueOf(idFuenteFinanciamiento));
            FuenteRecursos fr = (FuenteRecursos) emd.getEntityById(FuenteRecursos.class.getName(), Integer.valueOf(idFuenteRecurso));

            for (TechoProgramaPresupueastarioFuente t : objeto.getTechos()) {
                if (!t.equals(techo) && t.getFuenteFinanciamiento().equals(ff) && t.getFuenteRecursos().equals(fr)) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_YA_EXISTE_UN_TECHO_PARA_DICHA_FUENTE_FINANCIAMIENTO_FUENTE_RECURSO);
                    throw b;
                }
            }

            techo.setFuenteFinanciamiento(ff);
            techo.setFuenteRecursos(fr);
            if (techo.getProgramaPresupuestario() == null) {
                techo.setProgramaPresupuestario(objeto);
                objeto.getTechos().add(techo);
            }
            loadTechosPrograma();
            RequestContext.getCurrentInstance().execute("$('#anadirFuente').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Remueve un techo de un programa presupuestario
     *
     * @param toDelete
     */
    public void eliminarFuente(TechoProgramaPresupueastarioFuente toDelete) {
        try {
            boolean remove = objeto.getTechos().remove(toDelete);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Crea o carga un supuesto desde la lista de supuestos del programa
     * presupuestario en edición
     *
     * @param index
     */
    public void loadSupuesto(String index) {
        if (TextUtils.isEmpty(index)) {
            supuesto = new Supuesto();
        } else {
            supuesto = objeto.getSupuestos().get(Integer.valueOf(index).intValue());
        }
    }

    /**
     * Agrega un supuesto a la lista de supuestos del programa presupuestario en
     * edición
     */
    public void saveSupuesto() {
        try {
            if (supuesto.getPrograma() == null) {
                objeto.getSupuestos().add(supuesto);
                supuesto.setPrograma(objeto);
            }
            RequestContext.getCurrentInstance().execute("$('#anadirSupuesto').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un supuesto
     *
     * @param toRemove
     */
    public void eliminarSupuesto(Supuesto toRemove) {
        try {
            objeto.getSupuestos().remove(toRemove);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un subprograma institucional si no tiene programas
     * presupuestarios o supuestos asociados
     *
     * @param toRemove
     */
    public void eliminarSubprograma(ProgramaPresupuestario toRemove) {
        try {
            if (!progDelegate.eliminableProgramaPresupuestario(toRemove.getId())) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
                throw b;
            }
            if (toRemove.getProgramasPresupuestarios().size() > 0 || toRemove.getSupuestos().size() > 0 || toRemove.getProgramasInstitucionales().size() > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
                throw b;
            }
            objeto.getProgramasPresupuestarios().remove(toRemove);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Agrega un programa institucional a un programa presupuestario
     */
    public void addProgramaInstitucional() {
        try {
            ProgramaInstitucional pi = (ProgramaInstitucional) emd.getEntityById(ProgramaInstitucional.class.getName(), Integer.valueOf(tempProgInstId));
            objeto.getProgramasInstitucionales().add(pi);
            tempProgInstId = null;
            RequestContext.getCurrentInstance().execute("$('#anadirProgramaInstitucional').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Remueve un programa institucional de un programa presupuestario
     *
     * @param pi
     */
    public void eliminarProgramaInstitucional(ProgramaInstitucional pi) {
        objeto.getProgramasInstitucionales().remove(pi);
        RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
    }

    /**
     * Devuelve una lista de programas institucionales
     *
     * @return
     */
    public Map<String, String> getProgramasInstitucionales() {
        Map<String, String> m = new LinkedHashMap();
        List<CriteriaTO> criterios = new LinkedList<CriteriaTO>();

        //que no sean subprogramas
        MatchCriteriaTO noSubprograma = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "programaInstitucional", 0);
        criterios.add(noSubprograma);

        //estado vigente
        MatchCriteriaTO vigente = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoComun.VIGENTE);
        criterios.add(vigente);

        if (!TextUtils.isEmpty(idPlanificacion)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "planificacionEstrategica.id", Integer.parseInt(idPlanificacion));
            criterios.add(criterio);
        }

        CriteriaTO condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
        String[] orderBy = {"nombre"};
        boolean[] asc = {true};
        List<ProgramaInstitucional> todos = emd.findEntityByCriteria(ProgramaInstitucional.class, condicion, orderBy, asc, null, null);
        for (ProgramaInstitucional p : todos) {
            if (!objeto.getProgramasInstitucionales().contains(p)) {
                m.put(p.getNombre(), String.valueOf(p.getId()));
            }
        }
        return m;
    }

    /**
     * Verifica si es un sub programa o no
     *
     * @return
     */
    public boolean isSubprograma() {
        if (objeto.getProgramaPresupuestario() != null) {
            return true;
        }
        if (!stack.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Devuelve una lista de planificaciones estratégicas vigentes
     *
     * @return
     */
    public Map<String, String> getPlanificaciones() {
        return utilsMB.getPlanificacionesEstrategicasVigetnes();
    }

    /**
     * Devuelve la lista de líneas estratégicas vigentes
     *
     * @return
     */
    public Map<String, String> getLineas() {
        if (!TextUtils.isEmpty(idPlanificacion)) {
            return utilsMB.getLineasEstrategicasVigetnes((idPlanificacion));
        } else {
            return new HashMap();
        }
    }

    /**
     * Devuelve el nombre de una planificación estratégica
     *
     * @return
     */
    public String getNombrePlanificacion() {
        if (!TextUtils.isEmpty(idPlanificacion)) {
            PlanificacionEstrategica p = (PlanificacionEstrategica) emd.getEntityById(PlanificacionEstrategica.class.getName(), Integer.valueOf(idPlanificacion));
            return p.getNombre();
        } else {
            return null;
        }
    }

    /**
     * Crea e inicializa los valores de un programa institucional
     *
     * @return
     */
    private ProgramaPresupuestario initPrograma() {
        ProgramaPresupuestario p = new ProgramaPresupuestario();
        p.setProgramasInstitucionales(new LinkedList());
        p.setSupuestos(new LinkedList());
        p.setProgramasPresupuestarios(new LinkedList());
        p.setTechos(new LinkedList());
        p.setTechosPresupuestarios(new LinkedList());
        p.setEstado(EstadoComun.VIGENTE);
        return p;
    }

    /**
     * Devuelve el nombre del botón que sirve para guardar o aceptar según sea
     * el caso
     *
     * @return
     */
    public String getLabelSaveButton() {
        if (stack.empty()) {
            return "labels.Guardar";
        } else {
            return "labels.Aceptar";
        }
    }

    /**
     * Devuelve el nombre del botón que sirve para cancelar o ir atrás según sea
     * el caso
     *
     * @return
     */
    public String getLabelCancelButton() {
        if (stack.empty()) {
            return "labels.Cancelar";
        } else {
            return "labels.Atras";
        }
    }

    /**
     * Devuelve una lista de nombres
     *
     * @return
     */
    public List<String> getNombrePadres() {
        List l = new LinkedList();

        ProgramaPresupuestario inicio;
        if (stack.isEmpty()) {
            inicio = objeto;
        } else {
            inicio = stack.get(0);
        }
        inicio = inicio.getProgramaPresupuestario();
        while (inicio != null) {
            l.add(0, getNombrePrograma(inicio.getNombre()));
            inicio = inicio.getProgramaPresupuestario();
        }

        Iterator iter = stack.iterator();
        while (iter.hasNext()) {
            ProgramaPresupuestario p = (ProgramaPresupuestario) iter.next();
            l.add(getNombrePrograma(p.getNombre()));
        }

        if (TextUtils.isEmpty(objeto.getNombre())) {
            l.add(l.size(), "Actual");
        } else {
            l.add(l.size(), objeto.getNombre());
        }

        return l;
    }

    /**
     * Devuelve el nombre de un programa presupuestario
     *
     * @param nombre
     * @return
     */
    private String getNombrePrograma(String nombre) {
        if (TextUtils.isEmpty(nombre)) {
            return ("Programa Sin Nombre");
        }
        return nombre;

    }

    /**
     * Devuelve una clase CSS según la cantidad de programas institucionales
     *
     * @return
     */
    public String getSubClassLevel() {
        return getCssLvl(stack.size());
    }

    public String getCssLvl(Integer lvl) {
        if (lvl < 5) {
            return "nivel-interno-" + lvl;
        } else {
            return "nivel-interno-5";
        }

    }

    /**
     * Devuelve una clase CSS
     *
     * @return
     */
    public String getVolverLavel() {
        String volver = "Volver";
        if (!stack.isEmpty()) {
            volver = volver + " a " + stack.get(stack.size() - 1).getNombre();
        }
        return volver;
    }

    /**
     * Calcula la suma de los montos totales y asignados de los techos para los
     * años de una planificación
     */
    public void recalcularTotales() {
        totalesSuma = new LinkedList();
        totalesAsignado = new LinkedList();
        totalesDiferencia = new LinkedList();

        if (planificacion != null) {
            Integer desde = DatesUtils.getYearOfDate(planificacion.getDesde());
            Integer hasta = DatesUtils.getYearOfDate(planificacion.getHasta());
            for (int anio = desde; anio <= hasta; anio++) {
                int indexAnio = anio - desde;

                //se suman los totales
                BigDecimal suma = getSumaTotal(indexAnio);
                BigDecimal asignado = getAsignadoAnio(anio);
                BigDecimal diferencia = asignado.subtract(suma);

                totalesSuma.add(indexAnio, suma);
                totalesAsignado.add(indexAnio, asignado);
                totalesDiferencia.add(indexAnio, diferencia);
            }
        }
    }

    /**
     * Calcula la suma de los montos totales de un determinado año
     */
    private BigDecimal getSumaTotal(int indexAnio) {
        BigDecimal suma = BigDecimal.ZERO;
        for (TechoProgramaPresupueastarioFuente techoFuente : objeto.getTechos()) {
            TechoProgramaPresupueastarioAnio techo = (TechoProgramaPresupueastarioAnio) techoFuente.getTechos().get(indexAnio);
            if (techo.getMonto() != null) {
                suma = suma.add(techo.getMonto());
            }
        }
        return suma;
    }

    /**
     * Calcula la suma de los montos asignados de un determinado año
     */
    private BigDecimal getAsignadoAnio(Integer anio) {
        for (ProgramaPresupuestarioTechoAnio techo : objeto.getTechosPresupuestarios()) {
            if (techo.getAnioFiscal().getAnio().equals(anio)) {
                return techo.getTecho();
            }
        }
        return BigDecimal.ZERO;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Supuesto getSupuesto() {
        return supuesto;
    }

    public String getIdToEliminar() {
        return idToEliminar;
    }

    public void setIdToEliminar(String idToEliminar) {
        this.idToEliminar = idToEliminar;
    }

    public void setSupuesto(Supuesto supuesto) {
        this.supuesto = supuesto;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public DualListModel<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public List<BigDecimal> getTotalesSuma() {
        return totalesSuma;
    }

    public void setTotalesSuma(List<BigDecimal> totalesSuma) {
        this.totalesSuma = totalesSuma;
    }

    public List<BigDecimal> getTotalesAsignado() {
        return totalesAsignado;
    }

    public void setTotalesAsignado(List<BigDecimal> totalesAsignado) {
        this.totalesAsignado = totalesAsignado;
    }

    public List<BigDecimal> getTotalesDiferencia() {
        return totalesDiferencia;
    }

    public void setTotalesDiferencia(List<BigDecimal> totalesDiferencia) {
        this.totalesDiferencia = totalesDiferencia;
    }

    public List<Integer> getAniosPlanificacion() {
        return aniosPlanificacion;
    }

    public void setAniosPlanificacion(List<Integer> aniosPlanificacion) {
        this.aniosPlanificacion = aniosPlanificacion;
    }

    public PlanificacionEstrategica getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(PlanificacionEstrategica planificacion) {
        this.planificacion = planificacion;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(String idLinea) {
        this.idLinea = idLinea;
    }

    public TechoProgramaPresupueastarioFuente getTecho() {
        return techo;
    }

    public void setTecho(TechoProgramaPresupueastarioFuente techo) {
        this.techo = techo;
    }

    public void setLineasEstrategicas(DualListModel<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public String getTempProgInstId() {
        return tempProgInstId;
    }

    public void setTempProgInstId(String tempProgInstId) {
        this.tempProgInstId = tempProgInstId;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public GeneralDelegate getPeled() {
        return peled;
    }

    public void setPeled(GeneralDelegate peled) {
        this.peled = peled;
    }

    public ProgramaPresupuestario getObjeto() {
        return objeto;
    }

    public String getIdClasificadorFuncional() {
        return idClasificadorFuncional;
    }

    public void setIdClasificadorFuncional(String idClasificadorFuncional) {
        this.idClasificadorFuncional = idClasificadorFuncional;
    }

    public void setObjeto(ProgramaPresupuestario objeto) {
        this.objeto = objeto;
    }

    public Stack<ProgramaPresupuestario> getStack() {
        return stack;
    }

    public void setStack(Stack<ProgramaPresupuestario> stack) {
        this.stack = stack;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getIdFuenteFinanciamiento() {
        return idFuenteFinanciamiento;
    }

    public void setIdFuenteFinanciamiento(String idFuenteFinanciamiento) {
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
    }

    public String getIdFuenteRecurso() {
        return idFuenteRecurso;
    }

    public void setIdFuenteRecurso(String idFuenteRecurso) {
        this.idFuenteRecurso = idFuenteRecurso;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
