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
import sv.gob.mined.siges.filtros.FiltroRelProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.persistencia.entidades.SgRelProyectoFinanciamientoSistemaIntegrado;

public class RelProyectoFinanciamientoSistemaIntegradoDAO extends HibernateJpaDAOImp<SgRelProyectoFinanciamientoSistemaIntegrado, Integer> implements Serializable {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(RelProyectoFinanciamientoSistemaIntegradoDAO.class.getName());

    public RelProyectoFinanciamientoSistemaIntegradoDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroRelProyectoFinanciamientoSistemaIntegrado filtro) {

        List<CriteriaTO> criterios = new ArrayList();
      
        
        if (filtro.getSistemaIntegradoPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rpsSistemaIntegrado.sinPk", filtro.getSistemaIntegradoPk());
            criterios.add(criterio);
        }

        
        return criterios;
    }

    public List<SgRelProyectoFinanciamientoSistemaIntegrado> obtenerPorFiltro(FiltroRelProyectoFinanciamientoSistemaIntegrado filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelProyectoFinanciamientoSistemaIntegrado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null,filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroRelProyectoFinanciamientoSistemaIntegrado filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelProyectoFinanciamientoSistemaIntegrado.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
