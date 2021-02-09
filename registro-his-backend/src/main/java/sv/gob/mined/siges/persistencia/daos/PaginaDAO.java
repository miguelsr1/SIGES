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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroPagina;
import sv.gob.mined.siges.persistencia.entidades.SgRhPagina;

import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class PaginaDAO extends HibernateJpaDAOImp<SgRhPagina, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    private EntityManager em;

    public PaginaDAO(EntityManager em) throws Exception {
        super(em);
        this.em=em;
        this.segDAO = new SeguridadDAO(em);
        jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroPagina filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPagNombreLibro()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pagNombreLibro", filtro.getPagNombreLibro());
            criterios.add(criterio);
        }                        
        
        if (filtro.getPagLibro() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pagLibro", filtro.getPagLibro());
            criterios.add(criterio);
        }
        
        if (filtro.getPagPagina() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pagPagina", filtro.getPagPagina());
            criterios.add(criterio);
        }
        
        if (filtro.getPagNivelPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pagNivel.nivPk", filtro.getPagNivelPk());            
            criterios.add(criterio);           
        }  

        if (filtro.getPagAnio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pagAnio", filtro.getPagAnio());
            criterios.add(criterio);
        }
        if (filtro.getPagPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pagPk", filtro.getPagPk());
            criterios.add(criterio);
        }        
        return criterios;
    }

    public List<SgRhPagina> obtenerPorFiltro(FiltroPagina filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRhPagina.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(),false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroPagina filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRhPagina.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    

}
