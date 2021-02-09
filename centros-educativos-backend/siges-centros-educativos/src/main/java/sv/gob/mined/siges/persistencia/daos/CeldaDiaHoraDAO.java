/**
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
import sv.gob.mined.siges.filtros.FiltroCeldaDiaHora;
import sv.gob.mined.siges.persistencia.entidades.SgCeldaDiaHora;

public class CeldaDiaHoraDAO extends HibernateJpaDAOImp<SgCeldaDiaHora, Integer> implements Serializable {

    private EntityManager em;

    public CeldaDiaHoraDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroCeldaDiaHora filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCdhHorarioEscolar() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdhHorarioEscolar.hesPk", filtro.getCdhHorarioEscolar());
            criterios.add(criterio);
        }
        if(filtro.getCdhComponentePlanGrado()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdhComponentePlanGrado.cpgPk", filtro.getCdhComponentePlanGrado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgCeldaDiaHora> obtenerPorFiltro(FiltroCeldaDiaHora filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            if (criterios.size() > 1) {
                CriteriaTO criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
                return this.findEntityByCriteria(SgCeldaDiaHora.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
            } else if (criterios.size() == 1) {
                CriteriaTO criterio = criterios.get(0);
                return this.findEntityByCriteria(SgCeldaDiaHora.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
            } else {
                return this.findEntityByCriteria(SgCeldaDiaHora.class, null, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
            }
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCeldaDiaHora filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCeldaDiaHora.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
