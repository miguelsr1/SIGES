/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroAcuerdoSede;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class AcuerdoSedeDAO extends HibernateJpaDAOImp<SgAcuerdoSede, Integer> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AcuerdoSedeDAO.class.getName());

    private JsonWebToken jwt;
    private SeguridadBean segDAO;

    public AcuerdoSedeDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroAcuerdoSede filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        filtro.setSeNecesitaDistinct(Boolean.TRUE);

        if (filtro.getAseSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aseSede.sedPk", filtro.getAseSede());
            criterios.add(criterio);
        }

        if (filtro.getAseTipoAcuerdo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aseTipoAcuerdo.taoPk", filtro.getAseTipoAcuerdo());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getAseNumeroAcuerdo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "aseNumeroAcuerdo", filtro.getAseNumeroAcuerdo());
            criterios.add(criterio);
        }
        if (filtro.getAseNumeroResolucion()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aseNumeroResolucion", filtro.getAseNumeroResolucion());
            criterios.add(criterio);
        }
        if (filtro.getAseFechaRegistro() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aseFechaRegistro", filtro.getAseFechaRegistro());
            criterios.add(criterio);
        }

        if (filtro.getAseFechaRegistroInicio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "aseFechaRegistro", filtro.getAseFechaRegistroInicio());
            criterios.add(criterio);
        }

        if (filtro.getAseFechaRegistroFin() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "aseFechaRegistro", filtro.getAseFechaRegistroFin());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgAcuerdoSede> obtenerPorFiltro(FiltroAcuerdoSede filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            Boolean distinct = Boolean.TRUE;
            List<OperationSecurity> ops = null;
            if (securityOperation == null) {
                distinct = filtro.getSeNecesitaDistinct();
            } else {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                    }
                }
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }

            return this.findEntityByCriteria(SgAcuerdoSede.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAcuerdoSede filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAcuerdoSede.class, criterio, false, ops);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
