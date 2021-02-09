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
import sv.gob.mined.siges.filtros.FiltroControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ControlAsistenciaPersonalCabezalDAO extends HibernateJpaDAOImp<SgControlAsistenciaPersonalCabezal, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public ControlAsistenciaPersonalCabezalDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroControlAsistenciaPersonalCabezal filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCpcPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcPk", filtro.getCpcPk());
            criterios.add(criterio);
        }
        if (filtro.getCpcFecha() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcFecha", filtro.getCpcFecha());
            criterios.add(criterio);
        }
        if (filtro.getCpcSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcSede.sedPk", filtro.getCpcSede());
            criterios.add(criterio);
        }
        if (filtro.getCpcDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcSede.sedDireccion.dirDepartamento.depPk", filtro.getCpcDepartamento());
            criterios.add(criterio);
        }
        if (filtro.getCpcMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcSede.sedDireccion.dirMunicipio.munPk", filtro.getCpcMunicipio());
            criterios.add(criterio);
        }
        if (filtro.getCpcDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "cpcFecha", filtro.getCpcDesde());
            criterios.add(criterio);
        }
        if (filtro.getCpcHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "cpcFecha", filtro.getCpcHasta());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgControlAsistenciaPersonalCabezal> obtenerPorFiltro(FiltroControlAsistenciaPersonalCabezal filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgControlAsistenciaPersonalCabezal.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroControlAsistenciaPersonalCabezal filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgControlAsistenciaPersonalCabezal.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
