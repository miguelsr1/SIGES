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
import sv.gob.mined.siges.filtros.FiltroEtiqueta;
import sv.gob.mined.siges.persistencia.entidades.SgRhEtiqueta;

import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class EtiquetaDAO extends HibernateJpaDAOImp<SgRhEtiqueta, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    private EntityManager em;

    public EtiquetaDAO(EntityManager em) throws Exception {
        super(em);
        this.em=em;
        this.segDAO = new SeguridadDAO(em);
        jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroEtiqueta filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getNivPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiPagina.pagNivel.nivPk", filtro.getNivPk());
            criterios.add(criterio);
        }
        if (filtro.getEtiAnio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiPagina.pagAnio", filtro.getEtiAnio());
            criterios.add(criterio);
        }                         
        if (filtro.getEtiLibro() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiPagina.pagLibro", filtro.getEtiLibro());
            criterios.add(criterio);
        }       
        if (filtro.getEtiPagina() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiPagina.pagPagina", filtro.getEtiPagina());
            criterios.add(criterio);
        }
        if (filtro.getPagPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiPagina.pagPk", filtro.getPagPk());
            criterios.add(criterio);
        }

        if (filtro.getEtiNombre()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "etiNombre", filtro.getEtiNombre());
            criterios.add(criterio);
        }
        
        if (filtro.getEtiApellido()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "etiApellido", filtro.getEtiApellido());
            criterios.add(criterio);
        }        

        if (filtro.getEtiDui() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiDui", filtro.getEtiDui());
            criterios.add(criterio);
        }
        
        if (filtro.getEtiNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "etiNie", filtro.getEtiNie());
            criterios.add(criterio);
        }
                
        if (filtro.getEtiNombreSede()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "etiNombreSede", filtro.getEtiNombreSede());            
            criterios.add(criterio);           
        }  
          
        return criterios;
    }

    public List<SgRhEtiqueta> obtenerPorFiltro(FiltroEtiqueta filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRhEtiqueta.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(),false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEtiqueta filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRhEtiqueta.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    

}
