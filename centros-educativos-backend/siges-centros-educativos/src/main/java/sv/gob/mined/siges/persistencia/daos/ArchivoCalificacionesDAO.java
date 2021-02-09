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
import sv.gob.mined.siges.filtros.FiltroArchivoCalificaciones;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificaciones;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ArchivoCalificacionesDAO extends HibernateJpaDAOImp<SgArchivoCalificaciones, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public ArchivoCalificacionesDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroArchivoCalificaciones filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAccEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "accEstado", filtro.getAccEstado());
            criterios.add(criterio);
        }

        if (filtro.getAccArchivoBorrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "accArchivoBorrado", filtro.getAccArchivoBorrado());
            criterios.add(criterio);
        }

        if (filtro.getFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "accUltmodFecha", filtro.getFechaHasta());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgArchivoCalificaciones> obtenerPorFiltro(FiltroArchivoCalificaciones filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgArchivoCalificaciones.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroArchivoCalificaciones filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgArchivoCalificaciones.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
