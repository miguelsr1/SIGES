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
import sv.gob.mined.siges.filtros.FiltroAcuerdo;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdo;

public class AcuerdoDAO extends HibernateJpaDAOImp<SgAcuerdo, Integer> implements Serializable {

    private EntityManager em;

    public AcuerdoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroAcuerdo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAcuPropuestaPedagogica() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acuPropuestaPedagogica.ppePk", filtro.getAcuPropuestaPedagogica());
            criterios.add(criterio);
        }

        // si filtro acuerdos por sistemas intregados sí o sí debo filtrar por los que aplican
        if (filtro.getSistemaIntegradoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acuPropuestaPedagogica.ppeSede.sedSistemas.sinPk.sinPk", filtro.getSistemaIntegradoPk());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acuAplicaSistemasIntegrados", Boolean.TRUE);
            criterios.add(criterio);
            criterios.add(criterio1);
        }

        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acuPropuestaPedagogica.ppeSede.sedPk", filtro.getSedePk());
            criterios.add(criterio);
        }

        if (filtro.getEstadoAcuerdo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acuEstado", filtro.getEstadoAcuerdo());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgAcuerdo> obtenerPorFiltro(FiltroAcuerdo filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAcuerdo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroAcuerdo filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgAcuerdo.class);
    }

    public Long cantidadTotal(FiltroAcuerdo filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAcuerdo.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
