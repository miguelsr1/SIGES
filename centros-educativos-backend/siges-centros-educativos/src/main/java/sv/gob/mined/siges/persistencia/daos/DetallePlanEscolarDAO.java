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
import sv.gob.mined.siges.filtros.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar;

public class DetallePlanEscolarDAO extends HibernateJpaDAOImp<SgDetallePlanEscolar, Integer> implements Serializable {

    private SeguridadDAO segDAO;

    public DetallePlanEscolarDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroDetallePlanEscolar filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPlanEscolarHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpeAplicaSistemasIntegrados", filtro.getPlanEscolarHabilitado());
            criterios.add(criterio);
//            VER SI REQUIERE FECHA ENTRE RANGO INICIO FIN HOY
//            MatchCriteriaTO criterioFechaHasta = CriteriaTOUtils.createMatchCriteriaTO(
//                    MatchCriteriaTO.types.LESSEQUAL, "dpeFechaFin", LocalDate.now());
//            criterios.add(criterioFechaHasta);
//
//            MatchCriteriaTO criterioFechaDesde = CriteriaTOUtils.createMatchCriteriaTO(
//                    MatchCriteriaTO.types.GREATEREQUAL, "dpeFechaInicio", LocalDate.now());
//            criterios.add(criterioFechaDesde);
        }

        if (filtro.getSistemaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpePlanEscolarAnual.peaSede.sedSistemas.sinPk.sinPk", filtro.getSistemaPk());
            criterios.add(criterio);
        }

        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpePlanEscolarAnual.peaSede.sedPk", filtro.getSedePk());
            criterios.add(criterio);
        }

        if (filtro.getFuenteFinanciamientoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpeFuenteFinanciamiento.ffiPk", filtro.getSistemaPk());
            criterios.add(criterio);
        }
        if(filtro.getAnioFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpePlanEscolarAnual.peaAnioLectivo.alePk", filtro.getAnioFk());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgDetallePlanEscolar> obtenerPorFiltro(FiltroDetallePlanEscolar filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

//            List<OperationSecurity> ops = null;
//            if (securityOperation != null) {
//                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
//            }
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgDetallePlanEscolar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDetallePlanEscolar filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

//            List<OperationSecurity> ops = null;
//            if (securityOperation != null) {
//                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
//            }
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDetallePlanEscolar.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
