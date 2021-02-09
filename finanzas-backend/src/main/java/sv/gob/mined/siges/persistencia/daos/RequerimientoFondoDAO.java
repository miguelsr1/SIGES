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
import sv.gob.mined.siges.filtros.FiltroRequerimientosFondo;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgRequerimientoFondo;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a requerimiento de fondo
 *
 * @author jgiron
 */
public class RequerimientoFondoDAO extends HibernateJpaDAOImp<SgRequerimientoFondo, Long> implements Serializable {

    private SeguridadBean segBean;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @param segBean
     * @throws Exception
     */
    public RequerimientoFondoDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.segBean = segBean;
    }

    /**
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroRequerimientosFondo filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if (filtro.getStrPk()!= null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strPk", filtro.getStrPk());
            criterios.add(criterio);
        }
        
        
        if (filtro.getTransferenciaGDep() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strTransferenciaGDepFk.tgdPk", filtro.getTransferenciaGDep());
            criterios.add(criterio);
        }
        
        
        if (filtro.getUnidadId()!= null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuId", filtro.getUnidadId());
            criterios.add(criterio);
        }
        
        if (filtro.getLineaId()!= null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuId", filtro.getLineaId());
            criterios.add(criterio);
        }
        
        if (filtro.getComponenteId() != null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeId", filtro.getComponenteId());
            criterios.add(criterio);
        }
        
        if (filtro.getSubComponenteId() != null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesId", filtro.getSubComponenteId());
            criterios.add(criterio);
        }
        
        if (filtro.getAnioFiscal()!= null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strTransferenciaGDepFk.tgdTransferenciaFk.traAnioFiscal.aniAnio", filtro.getAnioFiscal());
            criterios.add(criterio);
        }
        
        if (filtro.getEstado()!= null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "strEstado", filtro.getEstado());
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
    public List<SgRequerimientoFondo> obtenerPorFiltro(FiltroRequerimientosFondo filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }
            
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgRequerimientoFondo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,ops, filtro.getIncluirCampos());
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
    public Long obtenerTotalPorFiltro(FiltroRequerimientosFondo filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            List<OperationSecurity> ops = null;
            Boolean distinct = Boolean.FALSE;
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }
            
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRequerimientoFondo.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
