/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos.catalogo;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import sv.gob.mined.siges.filtros.catalogo.FiltroCargoRol;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargoRoles;


public class CargoRolDAO extends HibernateJpaDAOImp<SgCargoRoles, Long> implements Serializable {

    private EntityManager em;

    public CargoRolDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    private List<CriteriaTO> generarCriteria(FiltroCargoRol filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCarCargo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "carCargo.carPk", filtro.getCarCargo());
            criterios.add(criterio);
        }
        

        return criterios;
    }

    public List<SgCargoRoles> obtenerPorFiltro(FiltroCargoRol filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCargoRoles.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCargoRol filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCargoRoles.class, criterio, false,  null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
