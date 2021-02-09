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
import static sv.gob.mined.siges.enumerados.TipoUnidad.CENTRO_ESCOLAR;
import static sv.gob.mined.siges.enumerados.TipoUnidad.UNIDAD_ADMINISTRATIVA;
import sv.gob.mined.siges.filtros.FiltroNotificacionCumplimiento;
import sv.gob.mined.siges.persistencia.entidades.SgAfNotificacionCumplimiento;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
public class NotificacionCumplimientoDAO extends HibernateJpaDAOImp<SgAfNotificacionCumplimiento, Long> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public NotificacionCumplimientoDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroNotificacionCumplimiento filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "ncuUnidadAdministrativaFk.uadPk", 1);
                    criterios.add(criterioNotNullAD);
                    
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ncuUnidadAdministrativaFk.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "ncuSedeFk.sedPk", 1);
                    criterios.add(criterioNotNullCE);
                    
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ncuSedeFk.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }
        
       if(filtro.getLeida() != null) {
           MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ncuLeida", filtro.getLeida());
                        criterios.add(criterio);
       }
        

        return criterios;
    }

    public List<SgAfNotificacionCumplimiento> obtenerPorFiltro(FiltroNotificacionCumplimiento filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = this.generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgAfNotificacionCumplimiento.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroNotificacionCumplimiento filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfNotificacionCumplimiento.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
