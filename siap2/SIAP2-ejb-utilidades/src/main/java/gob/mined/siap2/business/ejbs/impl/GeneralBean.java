/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.interfaces.BaseCodiguera;
import com.mined.siap2.utilidades.PersistenceHelper;
import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.AnioFiscalDAO;
import gob.mined.siap2.data.daos.CodigueraDAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAccionCentral;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.TechoProgramaPresupueastarioFuente;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.GeneralCodigueralDAO;
import gob.mined.siap2.persistence.dao.imp.InsumoDAO;
import gob.mined.siap2.persistence.dao.imp.LineaEstrategicaDAO;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.PlanificacionEstrategicaDAO;
import gob.mined.siap2.persistence.dao.imp.ProgramaDAO;
import gob.mined.siap2.persistence.dao.imp.ProgramacionFinancieraDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siges.entities.data.impl.SgTipoOrganismoAdministrativo;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 *
 * @author Sofis Solutions
 */
@Stateless(name = "GeneralBean")
@LocalBean
public class GeneralBean {

    @Inject
    @JPADAO
    private POADAO poaDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private GeneralCodigueralDAO generalCodigueralDAO;
    @Inject
    @JPADAO
    private ProgramaDAO programaDAO;
    @Inject
    @JPADAO
    PlanificacionEstrategicaDAO planificacionEstrategicaDAO;
    @Inject
    @JPADAO
    LineaEstrategicaDAO lineaEstrategicaDAO;
    @Inject
    @JPADAO
    ProgramacionFinancieraDAO programacionFinancieraDAO;
    @Inject
    @JPADAO
    CodigueraDAO codigueraDAO;
    @Inject
    @JPADAO
    private InsumoDAO insumoDAO;
    @Inject
    private DatosUsuario datosUsuarioBean;
    @Inject
    private UsuarioBean usuarioBean;

    @Inject
    @JPADAO
    private AnioFiscalDAO anioFiscalDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método chequea si existe otro elemento de la misma clase con el mismo nombre
     *
     * @param clase
     * @param id
     * @param nombre
     */
    public void chequearIgualNombre(Class clase, Integer id, String nombre) {
        try {
            /*cheuqea no exista igual nombre */
            CriteriaTO condicion = null;
            MatchCriteriaTO igualNombre = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nombre", nombre);
            if (id != null) {
                MatchCriteriaTO distintoId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "id", id);
                condicion = CriteriaTOUtils.createANDTO(igualNombre, distintoId);
            } else {
                condicion = igualNombre;
            }
            String[] propiedades = {"id"};
            List existeIgualNombre = generalDAO.findEntityReferenceByCriteria(clase, condicion, null, null, null, null, propiedades);
            if (!existeIgualNombre.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NOMBRE_REPETIDO);
                throw b;
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException ge = new BusinessException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método chequea si existe otro elemento de la misma clase con el mismo código
     *
     * @param clase
     * @param id
     * @param nombre
     */
    public void chequearIgualCodigo(Class clase, Integer id, String codigo) {
        try {
            /*cheuqea no exista igual nombre */
            CriteriaTO condicion = null;
            MatchCriteriaTO igualNombre = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", codigo);
            if (id != null) {
                MatchCriteriaTO distintoId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "id", id);
                condicion = CriteriaTOUtils.createANDTO(igualNombre, distintoId);
            } else {
                condicion = igualNombre;
            }
            String[] propiedades = {"id"};
            List existeIgualNombre = generalDAO.findEntityReferenceByCriteria(clase, condicion, null, null, null, null, propiedades);
            if (!existeIgualNombre.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_CODIGO_REPETIDO);
                throw b;
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException ge = new BusinessException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Retorna la fuente de recursos con código GOES
     *
     * @return
     */
    public FuenteRecursos getGOES() {
        try {
            List<FuenteRecursos> l = generalDAO.findByOneProperty(FuenteRecursos.class, "codigo", ConstantesEstandares.FuenteRecursosCodigo.GOES);
            return l.get(0);
        } catch (GeneralException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve las planificaciones estratégicas vigentes
     *
     * @return
     */
    public List<PlanificacionEstrategica> getPlanificacionesEstrategicasVigentes() {
        try {
            List<PlanificacionEstrategica> l = planificacionEstrategicaDAO.getPlanificacionesByEstado(EstadoComun.VIGENTE);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite guardar un objeto del catálogo
     *
     * @param codiguera
     * @return
     */
    /**public BaseCodiguera guardarCodigueraValidacionUnico(BaseCodiguera codiguera) throws DAOConstraintViolationException{
        try {
            chequearIgualNombre(codiguera.getClass(), codiguera.getId(), codiguera.getNombre());
            return (BaseCodiguera) generalDAO.update(codiguera);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        }  catch(DAOConstraintViolationException e){
            throw e;  
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }**/
/**
     * Este método permite guardar un objeto del catálogo
     *
     * @param codiguera
     * @return
     */
    public BaseCodiguera guardarCodiguera(BaseCodiguera codiguera)  {
        try {
            chequearIgualNombre(codiguera.getClass(), codiguera.getId(), codiguera.getNombre());
            return (BaseCodiguera) generalDAO.update(codiguera);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        }  catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            if (PersistenceHelper.isConstraintViolation(ex)) {
                ge.setCodigo(ConstantesErrores.ERROR_CODIGO_REPETIDO);
                ge.addError(ConstantesErrores.ERROR_CODIGO_REPETIDO);
            } else {
                logger.log(Level.SEVERE, null, ex); 
                ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
                ge.addError(ex.getMessage());
            }
            
            throw ge;
        }
    }
    /**
     * Este método devuelve todas las líneas estratégicas en una determinada
     * planificación.
     *
     * @param idPlanificacion
     * @return
     */
    public List<LineaEstrategica> getLineasEstrategicasVigetnes(Integer idPlanificacion) {
        try {
            List<LineaEstrategica> l;
            if (idPlanificacion != null) {
                l = lineaEstrategicaDAO.getLineas(idPlanificacion, EstadoComun.VIGENTE);
            } else {
                l = generalDAO.findByOneProperty(LineaEstrategica.class, "estado", EstadoComun.VIGENTE, "nombre");
            }
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una acción central por id
     *
     * @param id
     * @return
     */
    public AccionCentral getAccionCentral(Integer id) {
        try {
            AccionCentral a = (AccionCentral) generalDAO.find(AccionCentral.class, id);
            a.getLineasEstrategicas().isEmpty();
            a.getMontosUT().isEmpty();
            return a;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una asignación no programables según id
     *
     * @param id
     * @return
     */
    public AsignacionNoProgramable getAsignacionNoProgramable(Integer id) {
        try {
            AsignacionNoProgramable a = (AsignacionNoProgramable) generalDAO.find(AsignacionNoProgramable.class, id);
            a.getLineasEstrategicas().isEmpty();
            a.getTechosPlanificacion().isEmpty();
            a.getActividadesNP().isEmpty();
            return a;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método devuelve todos los clasificaciones funcionales del gasto
     *
     * @return
     */
    public List<ClasificadorFuncional> getClasificadoresFuncionalesAsignables() {
        try {
            List<ClasificadorFuncional> l = generalCodigueralDAO.getClasificadoresAsignables();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un programa institucional por id
     *
     * @param id
     * @return
     */
    public ProgramaInstitucional getProgramaInstitucional(Integer id) {
        try {
            ProgramaInstitucional p = (ProgramaInstitucional) generalDAO.find(ProgramaInstitucional.class, id);
            initProgramaInstitucional(p);
            return p;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método inicializa un programa institucional
     *
     * @param p
     */
    private void initProgramaInstitucional(ProgramaInstitucional p) {
        p.getSupuestos().isEmpty();
        p.getLineasEstrategicas().isEmpty();
        for (ProgramaInstitucional hijo : p.getProgramasInstitucionales()) {
            initProgramaInstitucional(hijo);
        }
    }

    /**
     * Este método devuelve un programa presupuestario por id.
     *
     * @param id
     * @return
     */
    public ProgramaPresupuestario getProgramaPresupuestario(Integer id) {
        try {
            ProgramaPresupuestario p = (ProgramaPresupuestario) generalDAO.find(ProgramaPresupuestario.class, id);

            initProgramaPresupuestario(p);
            return p;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método inicializa un programa presupuestario.
     *
     * @param p
     */
    private void initProgramaPresupuestario(ProgramaPresupuestario p) {
        p.getSupuestos().isEmpty();
        p.getProgramasInstitucionales().isEmpty();
        p.getLineasEstrategicas().isEmpty();
        for (TechoProgramaPresupueastarioFuente ta : p.getTechos()) {
            ta.getTechos().isEmpty();
        }
        for (ProgramaPresupuestario hijo : p.getProgramasPresupuestarios()) {
            initProgramaPresupuestario(hijo);
        }
    }

    /**
     * Este método devuelve una asociación programa-indicador inicializada
     *
     * @param id
     * @return
     */
    public ProgramaIndicador getProgramaIndicador(Integer id) {
        try {
            ProgramaIndicador a = (ProgramaIndicador) generalDAO.find(ProgramaIndicador.class, id);
            a.getAnioIndicadors().isEmpty();
            return a;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un proyecto por id.
     *
     * @param id
     * @return
     */
    public Proyecto getProyecto(Integer id) {
        try {
            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, id);
            return p;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una planificación financiera por id
     *
     * @param id
     * @return
     */
    public PlanificacionEstrategica getPlanificacionEstrategica(Integer id) {
        try {
            PlanificacionEstrategica p = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, id);
            p.getTechosAccionCentral().isEmpty();
            p.getLineasEstrategicas().isEmpty();
            return p;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una lista de programaciones financieras de acción
     * central para un rango de años.
     *
     * @param minAnio
     * @param maxAnio
     * @return
     */
    public List<ProgramacionFinancieraAccionCentral> getProgramacionFinancieraAccionCentral(Integer minAnio, Integer maxAnio) {
        try {
            List<ProgramacionFinancieraAccionCentral> l = programacionFinancieraDAO.getProgFinancieraAccionCentral(minAnio, maxAnio);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una lista de programaciones financieras de
     * asignaciones no programables.
     *
     * @param minAnio
     * @param maxAnio
     * @return
     */
    public List<ProgramacionFinancieraAsignacionNoProgramable> getProgramacionFinancieraAsigNP(Integer minAnio, Integer maxAnio) {
        try {
            List<ProgramacionFinancieraAsignacionNoProgramable> l = programacionFinancieraDAO.getProgFinancieraAsigNP(minAnio, maxAnio);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite guardar una programación financiera de acción central
     *
     * @param programacion
     */
    public void guardarProgramacionFinancieraAccionCentral(List<ProgramacionFinancieraAccionCentral> programacion) {
        try {
            int minAnio = Integer.MAX_VALUE;
            int maxAnio = Integer.MIN_VALUE;
            List<ProgramacionFinancieraAccionCentral> todosActuales = getProgramacionFinancieraAccionCentral(minAnio, maxAnio);
            for (ProgramacionFinancieraAccionCentral p : todosActuales) {
                boolean encontrado = false;
                for (ProgramacionFinancieraAccionCentral pa : programacion) {
                    if (p.getId().equals(pa.getId())) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    generalDAO.delete(p);
                }
            }
            for (ProgramacionFinancieraAccionCentral p : programacion) {
                generalDAO.update(p);
                if (p.getAnio() < minAnio) {
                    minAnio = p.getAnio();
                }
                if (p.getAnio() > maxAnio) {
                    maxAnio = p.getAnio();
                }
            }

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite guardar una programación financiera de asignación no
     * programable.
     *
     * @param programacion programación financiera a guardar.
     */
    public void guardarProgramacionFinancieraAsigNP(List<ProgramacionFinancieraAsignacionNoProgramable> programacion) {
        try {
            for (ProgramacionFinancieraAsignacionNoProgramable p : programacion) {
                generalDAO.update(p);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los programas institucionales vigentes.
     *
     * @return
     */
    public List<ProgramaInstitucional> getProgramasInstitucionalesVigentes() {
        try {
            List<ProgramaInstitucional> l = programaDAO.getProgramasInstitucioanalesActivos();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los programas presupuestarios vigentes.
     *
     * @return
     */
    public List<ProgramaPresupuestario> getProgramasPresupuestariosVigentes() {
        try {
            List<ProgramaPresupuestario> l = programaDAO.getProgramasPresupuestarios();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve los subprogramas vigentes de un programa dado
     *
     * @param idPadre
     * @return
     */
    public List<ProgramaPresupuestario> getSubProgramasPresupuestariosVigentes(Integer idPadre) {
        try {
            List<ProgramaPresupuestario> l = programaDAO.getSubProgramasPresupuestarios(idPadre);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una lista de objetos de una clase.
     *
     * @param clase
     * @return
     */
    public List getClasesGeneralCodiguera(Class clase) {
        try {
            List l = generalCodigueralDAO.getGeneralCodiguera(clase);
            l.isEmpty();
            return l;
        } catch(NoResultException nre){
            return null;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Retorna todas las fuentes de recursos asociadas a la fuente de
     * financiamiento
     *
     * @param idFuenteFinanciamiento
     * @return
     */
    public List<FuenteRecursos> getFuentesRecursos(Integer idFuenteFinanciamiento) {
        try {
            List<FuenteRecursos> l = codigueraDAO.getFuenteRecursosHabilitados(idFuenteFinanciamiento);
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve una lista de actividades de POA de proyecto cuyo
     * nombre incluye el query como subcadena.
     *
     * @param query es la subcadena a buscar en el nombre de la actividad.
     * @return
     */
    public List<ActividadPOProyecto> completeTextCodigueraActividadesPO(String query) {
        try {
            List l = generalCodigueralDAO.getActividadPOProyecto(query);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve los años fiscales de la planificación
     *
     * @return
     */
    public List<AnioFiscal> getAniosFiscalesPlanificacion() {
        try {
            List<AnioFiscal> l = generalCodigueralDAO.getAniosFiscalesPlanificacion();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método devuelve los años fiscales de la planificación
     *
     * @return
     */
    public List<AnioFiscal> getAniosFiscalesFormulacion() {
        try {
            List<AnioFiscal> l = generalCodigueralDAO.getAniosFiscalesFormulacion();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método permite obtener el año fiscales habilitados para
     * formulacion.
     *
     * @return
     */
    public AnioFiscal getUltimoAnioFiscalFormulacion() {
        return generalCodigueralDAO.getUltimoAnioFiscalFormulacion();
    }

    /**
     * Este método devuelve los años fiscales de la planificación
     *
     * @return
     */
    public List<SgTipoOrganismoAdministrativo> getTiposOrganismosAdministrativos() {
        try {
            List<SgTipoOrganismoAdministrativo> l = generalCodigueralDAO.getTiposOrganismosAdministrativos();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve las unidades técnicas de tipo dirección
     *
     * @return
     */
    public List<UnidadTecnica> getUnidadesTecnicasDireccion() {
        try {
            List<UnidadTecnica> l = generalCodigueralDAO.getUTDirecciones();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todos los proyectos que tienen el nombre como
     * subcadena
     *
     * @param nombre es el texto incluido en el nombre.
     * @return
     */
    public List<Proyecto> getProyectosConNombre(String nombre) {
        try {
            List<Proyecto> l = generalCodigueralDAO.getProyectos(nombre);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todos los insumos No UACI según ID
     *
     * @param id
     * @return
     */
    public List<POInsumos> getInsumosNOUACIporCodigo(Integer id) {
        try {
            List<POInsumos> l = generalCodigueralDAO.getInsumosNOUACIporCodigo(id);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los insumos no UACI de un POA
     * que tengan al menos una fuente certificada
     *
     * @param id
     * @return
     */
    public List<POInsumos> getInsumosNOUACICertificadosPorCodigo(Integer id) {
        try {
            List<POInsumos> l = generalCodigueralDAO.getInsumosNOUACICertificadosPorCodigo(id);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todos los indicadores de tipo producto habilitados
     *
     * @return
     */
    public List<Indicador> getProductosVigentes() {
        try {
            List<Indicador> l = codigueraDAO.getProductosHabilitados();
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todas las líneas estratégicas vigentes.
     *
     * @return
     */
    public List<LineaEstrategica> getLineasEstrategicasVigentes() {
        try {
            List<LineaEstrategica> l = lineaEstrategicaDAO.getLineas(EstadoComun.VIGENTE);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todos los POA de Trabajo vigentes en un año fiscal.
     *
     * @param idAnioFiscal
     * @return
     */
    public List<GeneralPOA> getPOAsTrabago(Integer idAnioFiscal) {
        try {
            List<GeneralPOA> l = poaDAO.getPOAsTrabajoEnAnioFiscal(idAnioFiscal);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método devuelve todos los insumos cuyo nombre incluye una subcadena.
     *
     * @param nombre es la subcadena a buscar en el nombre.
     * @return
     */
    public List<Insumo> getInsumos(String nombre) {
        try {
            List<Insumo> l;
            if (TextUtils.isEmpty(nombre)) {
                l = generalDAO.findAll(Insumo.class, "nombre");
            } else {
                l = insumoDAO.getInsumos(nombre);
            }
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve todos los años fiscales marcados como En Ejecución
     *
     * @return
     */
    public Collection<AnioFiscal> obtenerAnioFiscalEnEjecucion() {
        try {
            return anioFiscalDAO.obtenerAniosFiscalesEjecucion();
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            ge.setEx(ex);
            throw ge;

        }
    }

    /**
     * Método que se encarga de retornar un año fiscal o un error
     *
     * @param anio
     * @return
     */
    public AnioFiscal getAnioFiscalOErrorPorAnio(Integer anio) {
        try {
            List<AnioFiscal> consultaAniosFiscales = generalDAO.findByOneProperty(AnioFiscal.class, "anio", anio);

            if (consultaAniosFiscales.isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {String.valueOf(anio)};
                b.addError(ConstantesErrores.ERR_NO_SE_ENCUENTRA_CREADO_EL_ANIO_FISCAL_0, params);
                throw b;
            }

            return consultaAniosFiscales.get(0);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    
    /**
     * Método que se encarga de retornar un año fiscal o un error
     *
     * @param anio
     * @return
     */
    public AnioFiscal getAnioFiscalPorId(Integer id) {
        try {
            return (AnioFiscal) generalDAO.findById(AnioFiscal.class, id);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_NO_SE_ENCUENTRA_CREADO_EL_ANIO_FISCAL_0);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * retorna verdadero si el año fiscal esta finalizado
     * 
     * @param idAnioFiscal
     * @return 
     */
    public Boolean anioSeleccionadoEstaFinalizado(Integer idAnioFiscal) {
        try {
            Boolean estaFinalizado = false;
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);
            if (anioFiscal != null && anioFiscal.getHasta() != null) {
                if (anioFiscal.getHasta().compareTo(new Date()) < 0) {
                    estaFinalizado = true;
                }
            }
            return estaFinalizado;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
