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
import sv.gob.mined.siges.filtros.FiltroGestionAnioLectivo;
import sv.gob.mined.siges.persistencia.entidades.SgGestionAnioLectivo;

public class GestionAnioLectivoDAO extends HibernateJpaDAOImp<SgGestionAnioLectivo, Integer> implements Serializable {

    private EntityManager em;

    public GestionAnioLectivoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroGestionAnioLectivo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getGalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "galPk", filtro.getGalPk());
            criterios.add(criterio);
        }
        if (filtro.getGalCodigo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "galCodigo", filtro.getGalCodigo());
            criterios.add(criterio);
        }
        if (filtro.getGalAnioActual() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "galAnioActual", filtro.getGalAnioActual());
            criterios.add(criterio);
        }
        if (filtro.getGalHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "galHabilitado", filtro.getGalHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgGestionAnioLectivo> obtenerPorFiltro(FiltroGestionAnioLectivo filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            if (criterios.size() > 1) {
                CriteriaTO criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
                return this.findEntityByCriteria(SgGestionAnioLectivo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
            } else if (criterios.size() == 1) {
                CriteriaTO criterio = criterios.get(0);
                return this.findEntityByCriteria(SgGestionAnioLectivo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
            } else {
                return this.findEntityByCriteria(SgGestionAnioLectivo.class, null, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
            }
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroGestionAnioLectivo filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgGestionAnioLectivo.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
