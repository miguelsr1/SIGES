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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;


/**
 * DAO correspondiente al componente
 *
 * @author jgiron
 */
public class CategoriaPresupuestoEscolarDAO extends HibernateJpaDAOImp<SsCategoriaPresupuestoEscolar, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    
    /**
     * Constructor
     *
     * @param em
     * @throws Exception
     */
    public CategoriaPresupuestoEscolarDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.em = em;
    }
    
    /**
     * Genera un criteria a partir del filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroCategoriaPresupuestoEscolar filtro) {
        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getCpeId() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "cpeId", filtro.getCpeId());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCpeNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cpeNombre", filtro.getCpeNombre());
            criterios.add(criterio);
        }

        return criterios;
    }
    
    /**
     * Devuelve las áreas de inversión que satisfacen un filtro determinado.
     *
     * @param filtro
     * @return lista de las áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SsCategoriaPresupuestoEscolar> obtenerPorFiltro(FiltroCategoriaPresupuestoEscolar filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SsCategoriaPresupuestoEscolar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    /**
     * Devuelve el total de elementos que satisfacen el filtro determinado.
     *
     * @param filtro
     * @return cantidad de elementos
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCategoriaPresupuestoEscolar filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SsCategoriaPresupuestoEscolar.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
