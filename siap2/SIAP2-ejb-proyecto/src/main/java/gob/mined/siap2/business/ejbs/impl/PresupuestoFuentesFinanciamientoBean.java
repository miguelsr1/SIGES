package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase incluye los métodos para la gestión de SsGesPresEs
 *
 * @author Sofis Solutions
 */
@Stateless(name = "presupuestoFuentesFinanciamientoBean")
@LocalBean
public class PresupuestoFuentesFinanciamientoBean {

    private static final Logger logger = Logger.getLogger(PresupuestoFuentesFinanciamientoBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    /**
     * Este método crea o actualiza una Relacion entre Gestion de presuspuesto
     * escolar y anio Fiscal.
     *
     * @param anioFiscal
     */
    public void crearActualizar(RelacionPresAnioFinanciamiento anioFiscal) {
        try {
            BusinessException be = new BusinessException();

            if (!be.getErrores().isEmpty()) {
                throw be;
            }

            if (anioFiscal.getId() == null) {
                generalDAO.getEntityManager().persist(anioFiscal);
            } else {
                generalDAO.getEntityManager().merge(anioFiscal);
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public RelacionPresAnioFinanciamiento getById(Integer id) {
        return generalDAO.getEntityManager().find(RelacionPresAnioFinanciamiento.class, id);
    }

    /**
     * Método que se encarga de eliminar una relacion entre Gestion de
     * presuspuesto escolar y anio Fiscal.
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminarRelacion(Integer id) {
        try {
            RelacionPresAnioFinanciamiento reg = (RelacionPresAnioFinanciamiento) generalDAO.find(RelacionPresAnioFinanciamiento.class, id);
            if (reg != null) {
                generalDAO.delete(reg);
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

    public List<RelacionPresAnioFinanciamiento> getComponentesPresupuestoEscolarByFiltro(FiltroRelPresAnioFinanciamiento filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }

            if (filtro.getRelAnioPresupuesto() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relAnioPresupuesto.id", filtro.getRelAnioPresupuesto());
                criterios.add(criterio);
            }

            if (filtro.getIdSubcomponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relAnioPresupuesto.componentePresupuestoEscolar.id", filtro.getIdSubcomponente());
                criterios.add(criterio);
            }

            if (filtro.getIdAnioFiscal()!= null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relAnioPresupuesto.anioFiscal.id", filtro.getIdAnioFiscal());
                criterios.add(criterio);
            }

            if (filtro.getIdLineaPresupuestaria() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relAnioPresupuesto.subCuenta.id", filtro.getIdLineaPresupuestaria());
                criterios.add(criterio);
            }

            if (filtro.getIdFuenteFinanciamiento() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteFinanciamiento.id", filtro.getIdFuenteFinanciamiento());
                criterios.add(criterio);
            }

            if (filtro.getIdFuenteRecursos() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteRecursos.id", filtro.getIdFuenteRecursos());
                criterios.add(criterio);
            }
            
            if (filtro.getTipoPresupuestoAnio() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relAnioPresupuesto.tipo", filtro.getTipoPresupuestoAnio());
                criterios.add(criterio);
            }
            
            if (!TextUtils.isEmpty(filtro.getNombreComplemento())) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "relAnioPresupuesto.nombreComplemento", filtro.getNombreComplemento());
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(RelacionPresAnioFinanciamiento.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

}
