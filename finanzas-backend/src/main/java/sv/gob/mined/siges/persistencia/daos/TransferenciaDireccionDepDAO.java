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
import sv.gob.mined.siges.filtros.FiltroTransferenciaDireccionDep;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaDireccionDep;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a las transfererncias a las DDE
 *
 * @author jgiron
 */
public class TransferenciaDireccionDepDAO extends HibernateJpaDAOImp<SgTransferenciaDireccionDep, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     * @param em
     * @param segBean
     * @throws Exception
     */
    public TransferenciaDireccionDepDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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
    private List<CriteriaTO> generarCriteria(FiltroTransferenciaDireccionDep filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if (filtro.getTddPk()!= null && filtro.getTddPk()>0) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddPk", filtro.getTddPk());
            criterios.add(criterio);
        }
        
        if (filtro.getTddTransferenciaFk()!= null && filtro.getTddTransferenciaFk()>0) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddTransferenciaFk.tcId", filtro.getTddTransferenciaFk());
            criterios.add(criterio);
        }
        
        if (filtro.getTransferenciaId()!= null && filtro.getTransferenciaId()>0) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddTransferenciaFk.tcTransferencia.traId", filtro.getTransferenciaId());
            criterios.add(criterio);
        }
        
        if (filtro.getTddEstado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddEstado", filtro.getTddEstado());
            criterios.add(criterio);
        }

        if (filtro.getDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddDireccionDepFk.dedDepartamentoFk", filtro.getDepartamento());
            criterios.add(criterio);
        }

        if (filtro.getComponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddTransferenciaFk.componente.cpeId", filtro.getComponente());
            criterios.add(criterio);
        }

        if (filtro.getSubComponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tddTransferenciaFk.subComponente.gesId", filtro.getSubComponente());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve una lista de transferencias a dirección departamental que satisfacen un filtro dado
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<SgTransferenciaDireccionDep> obtenerPorFiltro(FiltroTransferenciaDireccionDep filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgTransferenciaDireccionDep.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(),distinct,ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un filtro dado.
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaDireccionDep filtro, String securityOperation) throws DAOGeneralException {
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
            return this.countByCriteria(SgTransferenciaDireccionDep.class, criterio,distinct,ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
