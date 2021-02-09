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
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import sv.gob.mined.siges.filtros.FiltroFactorRiesgoSede;
import sv.gob.mined.siges.persistencia.entidades.SgFactorRiesgoSede;

public class FactorRiesgoSedeDAO extends HibernateJpaDAOImp<SgFactorRiesgoSede, Integer> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SgFactorRiesgoSede.class.getName());
    private EntityManager em;

    public FactorRiesgoSedeDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroFactorRiesgoSede filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getFriSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "friSede.sedPk", filtro.getFriSede());
            criterios.add(criterio);
        }
        if (filtro.getFriRiesgoAmbiental() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "friTipoRiesgo.triRiesgoAmbiental", filtro.getFriRiesgoAmbiental());
            criterios.add(criterio);
        }
        if (filtro.getFriRiesgoSocial() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "friTipoRiesgo.triRiesgoSocial", filtro.getFriRiesgoSocial());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgFactorRiesgoSede> obtenerPorFiltro(FiltroFactorRiesgoSede filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgFactorRiesgoSede.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroFactorRiesgoSede filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgFactorRiesgoSede.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
