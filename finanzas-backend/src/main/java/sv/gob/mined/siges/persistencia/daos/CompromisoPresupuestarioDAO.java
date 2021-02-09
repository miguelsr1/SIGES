/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroCompromisoPresupuestario;
import sv.gob.mined.siges.persistencia.entidades.SgCompromisoPresupuestario;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a la entidad compromiso presupuestario
 *
 * @author jgiron
 */
public class CompromisoPresupuestarioDAO extends HibernateJpaDAOImp<SgCompromisoPresupuestario, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    
    /**
     * Constructor
     *
     * @param em
     * @throws Exception
     */
    public CompromisoPresupuestarioDAO(EntityManager em) throws Exception {
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
    private List<CriteriaTO> generarCriteria(FiltroCompromisoPresupuestario filtro) {
        List<CriteriaTO> criterios = new ArrayList();

        return criterios;
    }
    
    /**
     * Devuelve los elementos que satisfacen un filtro determinado.
     *
     * @param filtro
     * @return lista de las áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SgCompromisoPresupuestario> obtenerPorFiltro(FiltroCompromisoPresupuestario filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCompromisoPresupuestario.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
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
    public Long obtenerTotalPorFiltro(FiltroCompromisoPresupuestario filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCompromisoPresupuestario.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
