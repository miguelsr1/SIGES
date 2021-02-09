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
import sv.gob.mined.siges.filtros.FiltroMejora;
import sv.gob.mined.siges.persistencia.entidades.SgMejoras;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class MejoraDAO extends HibernateJpaDAOImp<SgMejoras, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public MejoraDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroMejora filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getEdificio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mejEdificio.ediPk", filtro.getEdificio());
            criterios.add(criterio);
        }
        if (filtro.getTipoMejora()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mejTipoMejora.tmePk", filtro.getTipoMejora());
            criterios.add(criterio);
        }
        if (filtro.getFecha()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mejFecha", filtro.getFecha());
            criterios.add(criterio);
        }
        if (filtro.getFuenteFinanciamiento()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mejFuenteFinanciamiento.ffiPk", filtro.getFuenteFinanciamiento());
            criterios.add(criterio);
        }
        if (filtro.getInmueble()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mejInmueble.tisPk", filtro.getInmueble());
            criterios.add(criterio);
        }
       
        return criterios;
    }

    public List<SgMejoras> obtenerPorFiltro(FiltroMejora filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMejoras.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroMejora filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMejoras.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
