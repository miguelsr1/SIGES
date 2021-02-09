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
import sv.gob.mined.siges.filtros.FiltroRelModEdModAten;
import sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten;

public class RelModEdModAtenDAO extends HibernateJpaDAOImp<SgRelModEdModAten, Integer> implements Serializable {

    private EntityManager em;

    public RelModEdModAtenDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroRelModEdModAten filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getReaModalidadEducativa() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "reaModalidadEducativa.modPk", filtro.getReaModalidadEducativa());
            criterios.add(criterio);
        }
        if (filtro.getReaModalidadAtencion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "reaModalidadAtencion.matPk", filtro.getReaModalidadAtencion());
            criterios.add(criterio);
        }
        if (filtro.getReaSubModalidad() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "reaSubModalidadAtencion.smoPk", filtro.getReaSubModalidad());
            criterios.add(criterio);
        }
        if (filtro.getReaNivel() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getReaNivel());
            criterios.add(criterio);
        }
        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "reaGrado.graServicioEducativo.sduSede.sedPk", filtro.getSedePk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        return criterios;
    }

    public List<SgRelModEdModAten> obtenerPorFiltro(FiltroRelModEdModAten filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelModEdModAten.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroRelModEdModAten filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelModEdModAten.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
