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
import sv.gob.mined.siges.filtros.FiltroNivel;
import sv.gob.mined.siges.persistencia.entidades.SgNivel;

public class NivelDAO extends HibernateJpaDAOImp<SgNivel, Integer> implements Serializable {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(NivelDAO.class.getName());

    public NivelDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroNivel filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getSedPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nivCiclos.cicModalidades.modRelModalidadAtencion.reaGrado.graServicioEducativo.sduSede.sedPk", filtro.getSedPk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getOrganizacionCurricularPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nivOrganizacionCurricular.ocuPk", filtro.getOrganizacionCurricularPk());
            criterios.add(criterio);
        }
        if (filtro.getNivHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nivHabilitado", filtro.getNivHabilitado());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgNivel> obtenerPorFiltro(FiltroNivel filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgNivel.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroNivel filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgNivel.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
