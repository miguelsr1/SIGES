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
import sv.gob.mined.siges.filtros.FiltroRelSedeRiesgoAfectacion;
import sv.gob.mined.siges.persistencia.entidades.SgRelSedeRiesgoAfectacion;

public class SedeRiesgoAfectacionDAO extends HibernateJpaDAOImp<SgRelSedeRiesgoAfectacion, Integer> implements Serializable {

    private EntityManager em;

    public SedeRiesgoAfectacionDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroRelSedeRiesgoAfectacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getRarPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rarPk", filtro.getRarPk());
            criterios.add(criterio);
        }
        if (filtro.getRarTipoRiesgo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rarTipoRiesgo.triPk", filtro.getRarTipoRiesgo());
            criterios.add(criterio);
        }
        if (filtro.getRarGradoAfectacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rarGradoAfectacion.gafPk", filtro.getRarGradoAfectacion());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgRelSedeRiesgoAfectacion> obtenerPorFiltro(FiltroRelSedeRiesgoAfectacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelSedeRiesgoAfectacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroRelSedeRiesgoAfectacion filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgRelSedeRiesgoAfectacion.class);
    }

    public Long cantidadTotal(FiltroRelSedeRiesgoAfectacion filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelSedeRiesgoAfectacion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
