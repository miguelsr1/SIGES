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
import javax.persistence.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroMovimientos;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientos;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a los movimientos del presupuesto escolar
 *
 * @author jgiron
 */
public class MovimientosDAO extends HibernateJpaDAOImp<SgMovimientos, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public MovimientosDAO(EntityManager em) throws Exception {
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
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroMovimientos filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getMovActividadPk() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movActividadPk.dpePk", filtro.getMovActividadPk());
            criterios.add(criterio);
        }

        if (filtro.getMovPresupuestoFk() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movPresupuestoFk.pesPk", filtro.getMovPresupuestoFk());
            criterios.add(criterio);
        }

        if (filtro.getMovPk() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movPk", filtro.getMovPk());
            criterios.add(criterio);
        }

        if (filtro.getMovFuenteRecursos() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movFuenteRecursos", filtro.getMovFuenteRecursos());
            criterios.add(criterio);
        }

        if (filtro.getMovFuenteIngresos() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movFuenteIngresos.movPk", filtro.getMovFuenteIngresos());
            criterios.add(criterio);
        }
        if (filtro.getMovTipo() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movTipo", filtro.getMovTipo());
            criterios.add(criterio);
        }

        if (filtro.getMovOrigen() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movOrigen", filtro.getMovOrigen());
            criterios.add(criterio);
        }

        if (filtro.getMovActividadAplicaPlanCompra() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "movSubAreaInversionPk.adiAplicaPlanCompras", filtro.getMovActividadAplicaPlanCompra());
            criterios.add(criterio);
        }
        if (filtro.getMovNumMoviento() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "movNumMoviento", filtro.getMovNumMoviento());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve la lista de movimientos que cumplen el filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgMovimientos> obtenerPorFiltro(FiltroMovimientos filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMovimientos.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen el filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientos filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMovimientos.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve los movimientos segùn un filtro y un área de inversión
     *
     * @param filtro
     * @param subarea
     * @return
     * @throws DAOGeneralException
     */
    public List<SgMovimientos> buscarSubAreaPrincipal(FiltroMovimientos filtro, Long subarea) throws DAOGeneralException {
        try {

            Query subAreasInversion = em.createNativeQuery("select * from siap2.ss_areas_inversion sai  where ai_area_padre = " + subarea, SgMovimientos.class);
            return subAreasInversion.getResultList();

        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Busca las descripciones de los movimientos que satisfacen un filtro y
     * están en una área de inversión
     *
     * @param filtro
     * @param subarea
     * @return
     * @throws DAOGeneralException
     */
    public List<SgMovimientos> buscarDescripcion(FiltroMovimientos filtro, Long subarea) throws DAOGeneralException {
        try {

            Query subAreasInversion = em.createNativeQuery("" + subarea, SgMovimientos.class);
            return subAreasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
