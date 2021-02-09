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
import sv.gob.mined.siges.filtros.FiltroAsistenciaPersonal;
import sv.gob.mined.siges.persistencia.entidades.SgAsistenciaPersonal;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class AsistenciaPersonalDAO extends HibernateJpaDAOImp<SgAsistenciaPersonal, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public AsistenciaPersonalDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroAsistenciaPersonal filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getApePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apePk", filtro.getApePk());
            criterios.add(criterio);
        }
        if (filtro.getApeInasistencia() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeInasistencia", filtro.getApeInasistencia());
            criterios.add(criterio);
        }
        if (filtro.getApePersonal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apePersonal.psePk", filtro.getApePersonal());
            criterios.add(criterio);
        }
        if (filtro.getApeMotivoInasistencia() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeMotivoInasistencia.mipPk", filtro.getApeMotivoInasistencia());
            criterios.add(criterio);
        }
        if (filtro.getApeCabezal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "apeControl.cpcPk", filtro.getApeCabezal());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgAsistenciaPersonal> obtenerPorFiltro(FiltroAsistenciaPersonal filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAsistenciaPersonal.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAsistenciaPersonal filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAsistenciaPersonal.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
