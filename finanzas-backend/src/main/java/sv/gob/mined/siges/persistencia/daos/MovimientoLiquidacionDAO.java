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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente al presupuesto escolar
 *
 * @author jgiron
 */
public class MovimientoLiquidacionDAO extends HibernateJpaDAOImp<SgMovimientoLiquidacion, Long> implements Serializable {
    
    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @param segBean
     * @throws Exception
     */
    public MovimientoLiquidacionDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroMovimientoLiquidacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        
        if (filtro.getMlqLiquidacionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mlqLiquidacionPk.liqPk", filtro.getMlqLiquidacionPk());
            criterios.add(criterio);
        }
        
        if(filtro.getMovimientoTipo()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mlqTipoMovimiento", filtro.getMovimientoTipo());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    /**
     * Devuelve los elementos que satisfacen un filtro dado en un contexto de
     * seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<SgMovimientoLiquidacion> obtenerPorFiltro(FiltroMovimientoLiquidacion filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMovimientoLiquidacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
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
    public Long obtenerTotalPorFiltro(FiltroMovimientoLiquidacion filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMovimientoLiquidacion.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
     

}
