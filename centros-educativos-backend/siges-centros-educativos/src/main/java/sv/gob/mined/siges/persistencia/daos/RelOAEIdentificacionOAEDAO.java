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
import sv.gob.mined.siges.filtros.FiltroRelOAEIdentificacionOAE;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
public class RelOAEIdentificacionOAEDAO extends HibernateJpaDAOImp<SgRelOAEIdentificacionOAE, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;
    private EntityManager em;

    public RelOAEIdentificacionOAEDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroRelOAEIdentificacionOAE filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getRioIdentificacionOAEfk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rioIdentificacionOAEfk.ioaPk", filtro.getRioIdentificacionOAEfk());
            criterios.add(criterio);
        }

        if (filtro.getRioOrganismoAdministracionEscolarFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rioOrganismoAdministracionEscolarFk.oaePk", filtro.getRioOrganismoAdministracionEscolarFk());
            criterios.add(criterio);
        }
        
        if (filtro.getObligatorio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rioIdentificacionOAEfk.ioaEsObligatorio", filtro.getObligatorio());
            criterios.add(criterio);
        }
        if (filtro.getIdentificacionHabilitada()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rioIdentificacionOAEfk.ioaHabilitado", filtro.getIdentificacionHabilitada());
            criterios.add(criterio);
        }

        
        return criterios;
    }

    public List<SgRelOAEIdentificacionOAE> obtenerPorFiltro(FiltroRelOAEIdentificacionOAE filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelOAEIdentificacionOAE.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), Boolean.FALSE, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroRelOAEIdentificacionOAE filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelOAEIdentificacionOAE.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
