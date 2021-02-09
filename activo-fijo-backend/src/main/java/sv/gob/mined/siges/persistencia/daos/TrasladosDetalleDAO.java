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
import sv.gob.mined.siges.filtros.FiltroTrasladoDetalle;
import sv.gob.mined.siges.persistencia.entidades.SgAfTrasladosDetalle;

public class TrasladosDetalleDAO extends HibernateJpaDAOImp<SgAfTrasladosDetalle, Long> implements Serializable {

    public TrasladosDetalleDAO(EntityManager em) {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroTrasladoDetalle filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if(filtro.getTrasladoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "tdeTrasladoFk.traPk", filtro.getTrasladoId());
                    criterios.add(criterio);
        }

        if(filtro.getBienId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "tdeBienesDepreciablesFk.bdePk", filtro.getBienId());
                    criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgAfTrasladosDetalle> obtenerPorFiltro(FiltroTrasladoDetalle filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = this.generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgAfTrasladosDetalle.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroTrasladoDetalle filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfTrasladosDetalle.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
