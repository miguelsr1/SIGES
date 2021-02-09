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
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroTransferenciaGDep;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaGDep;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a las transferencias globales departamentales
 *
 * @author jgiron
 */
public class TransferenciaGDepDAO extends HibernateJpaDAOImp<SgTransferenciaGDep, Integer> implements Serializable {


    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    /**
     * *
     * Constructor de la clase
     *
     * @param em
     * @param segBean
     * @throws Exception
     */
    public TransferenciaGDepDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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
     * Genera una lista de criteria a partir un filtro.
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroTransferenciaGDep filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if (filtro.getTgdPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tgdPk", filtro.getTgdPk());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    /**
     * Devuelve una lista de transferencias a Centros educativos que satisfacen
     * un filtro en un contexto de seguridad dado.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<SgTransferenciaGDep> obtenerPorFiltro(FiltroTransferenciaGDep filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            
            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            List<OperationSecurity> ops = null;
            Boolean distinct = Boolean.FALSE;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }
            
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgTransferenciaGDep.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un filtro dado en un
     * contexto de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaGDep filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            List<OperationSecurity> ops = null;
            Boolean distinct = Boolean.FALSE;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }
            
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgTransferenciaGDep.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
