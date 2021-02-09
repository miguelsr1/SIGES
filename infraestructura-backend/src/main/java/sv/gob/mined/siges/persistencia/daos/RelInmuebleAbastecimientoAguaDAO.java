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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class RelInmuebleAbastecimientoAguaDAO extends HibernateJpaDAOImp<SgRelInmuebleAbastecimientoAgua, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public RelInmuebleAbastecimientoAguaDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroRelInmuebleAbastecimientoAgua filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
       
        if(filtro.getInmuebleFk()!= null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "iaaInmuebleSede.tisPk", filtro.getInmuebleFk());
            criterios.add(criterio);
        }
       
        return criterios;
    }

    public List<SgRelInmuebleAbastecimientoAgua> obtenerPorFiltro(FiltroRelInmuebleAbastecimientoAgua filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelInmuebleAbastecimientoAgua.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleAbastecimientoAgua filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelInmuebleAbastecimientoAgua.class, criterio,false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }   
    
}
