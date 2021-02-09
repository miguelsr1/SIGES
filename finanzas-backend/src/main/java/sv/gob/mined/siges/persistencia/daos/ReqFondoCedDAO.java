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
import java.util.logging.Logger;
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroReqFondoCed;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgReqFondoCed;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a los requerimientos de fondos de centros educativos
 *
 * @author jgiron
 */
public class ReqFondoCedDAO extends HibernateJpaDAOImp<SgReqFondoCed, Integer> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReqFondoCedDAO.class.getName());

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    /**
     * Constructor de clase
     *
     * @param em
     * @param segBean
     * @throws Exception
     */
    public ReqFondoCedDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.segDAO = segBean;
    }

    /**
     * Genera una lista de criteria a partir de un filtro.
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroReqFondoCed filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getRfcStrFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfcSolTransferenciaFk.strPk", filtro.getRfcStrFk());
            criterios.add(criterio);
        }

        if (filtro.getRfcTacFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfcTransferenciaCedFk.tacPk", filtro.getRfcTacFk());
            criterios.add(criterio);
        }

        if (filtro.getListReqId() != null && !filtro.getListReqId().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rfcSolTransferenciaFk.strPk", filtro.getListReqId());
            criterios.add(criterio);
        }

        if (filtro.getListFuenteId() != null && !filtro.getListFuenteId().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.id", filtro.getListFuenteId());
            criterios.add(criterio);
        }

        if (filtro.getRequerimiento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfcSolTransferenciaFk", filtro.getRequerimiento());
            criterios.add(criterio);
        }

        if (filtro.getFuenteRecurso() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk", filtro.getFuenteRecurso());
            criterios.add(criterio);
        }

        if (filtro.getFuentePk() != null) {

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.id", filtro.getFuentePk());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve la lista de requerimientos de fondo que satisfacen un filtro
     * según un contexto de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<SgReqFondoCed> obtenerPorFiltro(FiltroReqFondoCed filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgReqFondoCed.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un criterio dado en un
     * contexto de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroReqFondoCed filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgReqFondoCed.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
