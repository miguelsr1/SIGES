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
import sv.gob.mined.siges.filtros.FiltroAreaInversion;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgAreaInversion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a la entidad área de inversión
 *
 * @author jgiron
 */
public class AreaInversionDAO extends HibernateJpaDAOImp<SgAreaInversion, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    /**
     * Constructor
     *
     * @param em
     * @throws Exception
     */
    public AreaInversionDAO(EntityManager em) throws Exception {
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
    private List<CriteriaTO> generarCriteria(FiltroAreaInversion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAdiPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "adiPk", filtro.getAdiPk());
            criterios.add(criterio);
        }
        if (filtro.getAdiNombre() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.
                    createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "adiNombre", filtro.getAdiNombre());
            criterios.add(criterio);

        }
        if (filtro.getAdiPadrePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.
                    createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "adiPadrePk.adiPk", filtro.getAdiPadrePk());
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
    public List<SgAreaInversion> obtenerPorFiltro(FiltroAreaInversion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAreaInversion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
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
    public Long obtenerTotalPorFiltro(FiltroAreaInversion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAreaInversion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Busca las áreas de inversión que son áreas principales y cumplen un
     * determinado filtro
     *
     * @param filtro
     * @return lista de áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SgAreaInversion> buscarAreaPrincipal(FiltroAreaInversion filtro) throws DAOGeneralException {
        try {
            Query areasInversion = em.createNativeQuery("select * from siap2.ss_areas_inversion sai where ai_area_padre is null", SgAreaInversion.class);
            return areasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve las subáreas de inversión de un área dada y que satisfacen un
     * filtro
     *
     * @param filtro
     * @param subarea
     * @return lista de áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SgAreaInversion> buscarSubAreaPrincipal(FiltroAreaInversion filtro, Long subarea) throws DAOGeneralException {
        try {
            Query subAreasInversion = em.createNativeQuery("select * from siap2.ss_areas_inversion sai  where ai_area_padre = " + subarea, SgAreaInversion.class);
            return subAreasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve todas las subàreas de inversión según un determinado filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgAreaInversion> buscarSubAreaTransferencia(Long filtro) throws DAOGeneralException {
        try {
            Query areasInversion = em.createNativeQuery("select * from SIAP2.SS_AREAS_INVERSION SAI "
                    + "where AI_ID IN (select AI_ID from SIAP2.SS_REL_COMPONENTEPRESUPESCOLAR_AREAINV SRCA "
                    + "where GES_ID IN (select TP_COMPONENTE from SIAP2.SS_TOPE_PRESUPESTAL STP "
                    + "where TP_MOVIMIENTO = " + filtro + "))", SgAreaInversion.class);
            return areasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve todas las subàreas de inversión según un determinado filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgAreaInversion> buscarAreaTransferencia(Long filtro) throws DAOGeneralException {
        try {
            Query areasInversion = em.createNativeQuery("select * from SIAP2.SS_AREAS_INVERSION SAI where AI_ID in  "
                    + "(select ai_area_padre from SIAP2.SS_AREAS_INVERSION SAI where AI_ID IN "
                    + "(select AI_ID from SIAP2.SS_REL_COMPONENTEPRESUPESCOLAR_AREAINV SRCA where GES_ID IN "
                    + "(select TP_COMPONENTE from SIAP2.SS_TOPE_PRESUPESTAL STP where TP_MOVIMIENTO = " + filtro + ")))", SgAreaInversion.class);
            return areasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
