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
import sv.gob.mined.siges.filtros.FiltroHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class HabilitacionPeriodoMatriculaDAO extends HibernateJpaDAOImp<SgHabilitacionPeriodoMatricula, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private EntityManager em;
    private JsonWebToken jwt;

    public HabilitacionPeriodoMatriculaDAO(EntityManager em, SeguridadBean seg) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
        this.segDAO=seg;
    }

    private List<CriteriaTO> generarCriteria(FiltroHabilitacionPeriodoMatricula filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getHmpDepartamentoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getHmpDepartamentoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getHmpSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpSedeFk.sedPk", filtro.getHmpSedeFk());
            criterios.add(criterio);
        }
         if (filtro.getHmpNivel()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpRelHabilitacionMatriculaNivel.rhnNivelFk.nivPk", filtro.getHmpNivel());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
         
         if (filtro.getHmpEstado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpEstado", filtro.getHmpEstado());
            criterios.add(criterio);
        }
      
         if (filtro.getHmpFechaDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "hmpFechaHasta", filtro.getHmpFechaDesde());
            criterios.add(criterio);
        }

        if (filtro.getHmpFechaHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "hmpFechaDesde", filtro.getHmpFechaHasta());
            criterios.add(criterio);
        }
       

        return criterios;
    }

    public List<SgHabilitacionPeriodoMatricula> obtenerPorFiltro(FiltroHabilitacionPeriodoMatricula filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            Boolean distinct=filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;
            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
   
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgHabilitacionPeriodoMatricula.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct,  segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()),filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroHabilitacionPeriodoMatricula filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgHabilitacionPeriodoMatricula.class, criterio, false,  segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public SgHabilitacionPeriodoMatricula obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(SgHabilitacionPeriodoMatricula.class, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
