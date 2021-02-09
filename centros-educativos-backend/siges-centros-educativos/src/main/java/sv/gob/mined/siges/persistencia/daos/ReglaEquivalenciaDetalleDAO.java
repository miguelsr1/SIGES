/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import sv.gob.mined.siges.dto.SgGradoPlan;
import sv.gob.mined.siges.filtros.FiltroReglaEquivalenciaDetalle;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalenciaDetalle;

/**
 *
 * @author Sofis Solutions
 */
public class ReglaEquivalenciaDetalleDAO extends HibernateJpaDAOImp<SgReglaEquivalenciaDetalle, Integer> implements Serializable {



    public ReglaEquivalenciaDetalleDAO(EntityManager em) throws Exception {
        super(em);
    }
    


    private List<CriteriaTO> generarCriteria(FiltroReglaEquivalenciaDetalle filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getReglaNorativaDetalleId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redPk", filtro.getReglaNorativaDetalleId());
            criterios.add(criterio);
        }

        if (filtro.getReglaNormativaId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redReglaEquivalencia.reqPk", filtro.getReglaNormativaId());
            criterios.add(criterio);
        }

        if (filtro.getPlanEstudioId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redPlanEstudio.pesPk", filtro.getPlanEstudioId());
            criterios.add(criterio);
        }

        if (filtro.getGradoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redGrado.graPk", filtro.getGradoId());
            criterios.add(criterio);
        }

        if (filtro.getOperacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redOperacion", filtro.getOperacion());
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "redHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getReglaHabilitada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "redReglaEquivalencia.reqHabilitado", filtro.getReglaHabilitada());
            criterios.add(criterio);
        }

        if (filtro.getReglaPk() != null && !filtro.getReglaPk().isEmpty()) {
            List<CriteriaTO> estPkCriteria = new ArrayList();
            for (Long pk : filtro.getReglaPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "redReglaEquivalencia.reqPk", pk);
                estPkCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = estPkCriteria.toArray(new CriteriaTO[estPkCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getOpcion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "redOpcion.opcPk", filtro.getOpcion());
            criterios.add(criterio);
        }

        if (filtro.getProgramaEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "redProgramaEducativo.pedPk", filtro.getProgramaEducativo());
            criterios.add(criterio);
        }
        if (filtro.getGradoPlan()!= null && !filtro.getGradoPlan().isEmpty()) {
            
            List<CriteriaTO> variablesOR = new ArrayList();
            List<SgGradoPlan> gradP=new ArrayList(filtro.getGradoPlan());          
          
            
            for (SgGradoPlan gradoPlan : gradP) {
                List<CriteriaTO> variablesAND = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redGrado.graPk", gradoPlan.getGrado());
                variablesAND.add(criterio);
                
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redPlanEstudio.pesPk", gradoPlan.getPlanEstudio());
                variablesAND.add(criterio);
                
                if(gradoPlan.getOpcion()==null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "redOpcion", 1);
                    variablesAND.add(criterio);
                }else{
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redOpcion.opcPk", gradoPlan.getOpcion());
                    variablesAND.add(criterio);
                }
                
                if(gradoPlan.getProgramaEducativo()==null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "redProgramaEducativo", 1);
                    variablesAND.add(criterio);
                }else{
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "redProgramaEducativo.pedPk", gradoPlan.getProgramaEducativo());
                    variablesAND.add(criterio);
                }
                CriteriaTO[] arrCriterioAND = variablesAND.toArray(new CriteriaTO[variablesAND.size()]);
                CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];
                
                variablesOR.add(criterioAND);
            }

            if (!variablesOR.isEmpty()) {
                CriteriaTO[] arrCriterioOR = variablesOR.toArray(new CriteriaTO[variablesOR.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        return criterios;
    }

    public List<SgReglaEquivalenciaDetalle> obtenerPorFiltro(FiltroReglaEquivalenciaDetalle filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgReglaEquivalenciaDetalle.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroReglaEquivalenciaDetalle filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgReglaEquivalenciaDetalle.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
