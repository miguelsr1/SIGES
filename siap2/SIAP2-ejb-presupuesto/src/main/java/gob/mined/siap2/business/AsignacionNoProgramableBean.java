/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramacionFinancieraAsignacionNoProgramable;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AsignacionNoProgramableDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase incluye los métodos de la lógica de asignación no programable.
 *
 * @author sofis
 */
@Stateless(name = "AsignacionNoProgramableBean")
@LocalBean
public class AsignacionNoProgramableBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private AsignacionNoProgramableDAO asignacionNoProgramableDAO;

    @Inject
    private GeneralBean peleBean;

    /**
     * Crea o actualiza una asiganación no programable
     *
     * @param idPlanificacion
     * @param asignacion
     */
    public void crearOActualizarAsignacionNoProgramable(Integer idPlanificacion, AsignacionNoProgramable asignacion) {
        try {
            peleBean.chequearIgualNombre(AsignacionNoProgramable.class, asignacion.getId(), asignacion.getNombre());
            PlanificacionEstrategica p = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, idPlanificacion);
            asignacion.setPlanificacionEstrategica(p);
            generalDAO.update(asignacion);
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
     * elimina una asignación no programable
     *
     * @param idAsignacion
     */
    public void eleiminarAsignacionNoProgramable(Integer idAsignacion) {
        try {
            AsignacionNoProgramable a = (AsignacionNoProgramable) generalDAO.find(AsignacionNoProgramable.class, idAsignacion);

            if (!a.getActividadesNP().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"subactividades", "la asignación"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }

            List poas = generalDAO.findByOneProperty(POAConActividades.class, "conMontoPorAnio.id", a.getId());
            if (!poas.isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"POA", "la asignación"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }

            List<ProgramacionFinancieraAsignacionNoProgramable> programacion = generalDAO.findByOneProperty(ProgramacionFinancieraAsignacionNoProgramable.class, "asignacionNoProgramable.id", a.getId());
            for (ProgramacionFinancieraAsignacionNoProgramable p : programacion) {
                if (p.getMonto() != null && p.getMonto().compareTo(BigDecimal.ZERO) != 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {"monto en la programación financiera", "la asignación"};
                    b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                    throw b;
                }
            }

            for (ProgramacionFinancieraAsignacionNoProgramable p : programacion) {
                generalDAO.delete(p);
            }
            generalDAO.delete(a);

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }

    /**
     * Este método devuelve la lista de asignaciones no programables de una
     * planificación estratégica.
     *
     * @param idPlanificacion
     * @return
     */
    public List<AsignacionNoProgramable> getAsignacionesEnPlanificacion(Integer idPlanificacion) {
        try {
            List<AsignacionNoProgramable> l = asignacionNoProgramableDAO.getAsignaiconesEnplanificaion(idPlanificacion);
            for (AsignacionNoProgramable a : l) {
                a.getTechosPlanificacion().isEmpty();
            }
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método guarda los techos de asignación no programable.
     *
     * @param l
     */
    public void guardarTechosAsignaiconesNoProgramables(List<AsignacionNoProgramable> l) {
        try {
            for (AsignacionNoProgramable a : l) {
                generalDAO.update(a);
            }
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
