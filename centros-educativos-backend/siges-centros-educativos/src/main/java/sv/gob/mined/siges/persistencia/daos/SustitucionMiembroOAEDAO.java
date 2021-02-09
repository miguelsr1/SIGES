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
import sv.gob.mined.siges.filtros.FiltroSustitucionMiembroOAE;
import sv.gob.mined.siges.persistencia.entidades.SgSustitucionMiembroOAE;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class SustitucionMiembroOAEDAO extends HibernateJpaDAOImp<SgSustitucionMiembroOAE, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public SustitucionMiembroOAEDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = new SeguridadDAO(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroSustitucionMiembroOAE filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getSmoOaeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaePk", filtro.getSmoOaeFk());
            criterios.add(criterio);
        }

        if (filtro.getSmoEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoEstado", filtro.getSmoEstado());
            criterios.add(criterio);
        }

        if (filtro.getSmoDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaeSede.sedDireccion.dirDepartamento.depPk", filtro.getSmoDepartamento());
            criterios.add(criterio);
        }
        if (filtro.getSmoSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaeSede.sedPk", filtro.getSmoSede());
            criterios.add(criterio);
        }
        if (filtro.getSmoTipoOrganismoAdministrativo()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaeSede.cedTipoOrganismoAdministrativo.toaPk", filtro.getSmoTipoOrganismoAdministrativo());
            criterios.add(criterio);
        }
        if (filtro.getFechaDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "smoFecha", filtro.getFechaDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "smoFecha", filtro.getFechaHasta());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgSustitucionMiembroOAE> obtenerPorFiltro(FiltroSustitucionMiembroOAE filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgSustitucionMiembroOAE.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroSustitucionMiembroOAE filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSustitucionMiembroOAE.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
