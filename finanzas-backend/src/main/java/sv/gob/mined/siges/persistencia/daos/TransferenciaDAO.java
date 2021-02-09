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
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroTransferencia;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferencia;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.ReflectionUtils;

/**
 * Clase que gestiona las solicitudes de transferencia
 *
 * @author jgiron
 */
public class TransferenciaDAO extends HibernateJpaDAOImp<SsTransferencia, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @param segBean
     * @throws Exception
     */
    public TransferenciaDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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
     * Determina si existe un objeto según su id y un contexto de seguridad
     *
     * @param id
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Boolean objetoExistePorId(Integer id, String securityOperation) throws DAOGeneralException {
        try {
            String campoId = ReflectionUtils.obtenerCampoAnotado(SsTransferencia.class, Id.class).getName();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoId, id);
            Long count = this.countByCriteria(SsTransferencia.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
            if (count > 0L) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
     /**
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroTransferencia filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if (filtro.getTraId()!= null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "traId", filtro.getTraId());
            criterios.add(criterio);
        }
        
       
        return criterios;
    }

    /**
     * Devuelve una lista de solicitudes de transferencia que satisfacen un
     * filtro en un determinado contexto de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<SsTransferencia> obtenerPorFiltro(FiltroTransferencia filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SsTransferencia.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elemetnos que satisfacen un determinado filtro en
     * un contexto de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferencia filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SsTransferencia.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
   

}
