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
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroAllegado;

import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author usuario
 */
public class AllegadoDAO extends HibernateJpaDAOImp<SgAllegado, Integer> implements Serializable {

    private JsonWebToken jwt;

    public AllegadoDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroAllegado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAllPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allPk", filtro.getAllPk());
            criterios.add(criterio);
        }
        if (filtro.getAllPersonaReferenciadaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allPersonaReferenciada.perPk", filtro.getAllPersonaReferenciadaPk());
            criterios.add(criterio);
        }

        if (filtro.getAllPersonaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allPersona.perPk", filtro.getAllPersonaPk());
            criterios.add(criterio);
        }

        if (filtro.getAllEsReferente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allReferente", filtro.getAllEsReferente());
            criterios.add(criterio);
        }

        if (BooleanUtils.isTrue(filtro.getAllEsReferenteOContactoEmergencia())) {
            List<CriteriaTO> criteria = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allReferente", true);
            criteria.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allContactoEmergencia", true);
            criteria.add(criterio);

            CriteriaTO[] arrCriteria = criteria.toArray(new CriteriaTO[criteria.size()]);
            CriteriaTO criterioOR = CriteriaTOUtils.createORTO(arrCriteria);
            criterios.add(criterioOR);
        }

        if (filtro.getAllPersonasReferenciadasPk() != null && !filtro.getAllPersonasReferenciadasPk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long perPk : filtro.getAllPersonasReferenciadasPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "allPersonaReferenciada.perPk", perPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        return criterios;
    }

    public List<SgAllegado> obtenerPorFiltro(FiltroAllegado filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAllegado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAllegado filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAllegado.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
