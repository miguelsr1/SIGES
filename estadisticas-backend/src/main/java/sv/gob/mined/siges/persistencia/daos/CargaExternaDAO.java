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
import sv.gob.mined.siges.filtros.FiltroCargaExterna;
import sv.gob.mined.siges.persistencia.entidades.SgCargaExterna;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author usuario
 */
public class CargaExternaDAO extends HibernateJpaDAOImp<SgCargaExterna, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public CargaExternaDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
        jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroCargaExterna filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "carAnio", filtro.getAnio());
            criterios.add(criterio);
        }
        if (filtro.getIndicadorPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "carIndicador.indPk", filtro.getIndicadorPk());
            criterios.add(criterio);
        }
        if (filtro.getNombrePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "carNombre.nexPk", filtro.getNombrePk());
            criterios.add(criterio);
        }

        if (filtro.getDesagregacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "carDesagregacion", filtro.getDesagregacion());
            criterios.add(criterio);
        }

        if (BooleanUtils.isTrue(filtro.getSinDesagregacion())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "carDesagregacion", 1);
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgCargaExterna> obtenerPorFiltro(FiltroCargaExterna filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCargaExterna.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroCargaExterna filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCargaExterna.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
