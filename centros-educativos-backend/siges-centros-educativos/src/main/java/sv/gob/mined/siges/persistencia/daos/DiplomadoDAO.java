/*
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
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroDiplomado;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class DiplomadoDAO extends HibernateJpaDAOImp<SgDiplomado, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;
    private EntityManager em;

    public DiplomadoDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.em = em;
        this.segDAO = segBean;
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroDiplomado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "dipCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "dipCodigo", filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "dipNombre", SofisStringUtils.normalizarParaBusqueda(filtro.getNombre()));
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dipHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getAnioLectivoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dipDiplomadosEstudiante.depAnioLectivo.alePk", filtro.getAnioLectivoPk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "dipDiplomadosEstudiante.depSedeFk.sedPk", filtro.getSedePk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        return criterios;
    }
    
    public List<SgDiplomado> obtenerDiplomadosAutorizados(Long sedPk) {

        String query = "Select d.* from centros_educativos.sg_diplomados d where d.dip_habilitado AND EXISTS "
                + "(select 1 from centros_educativos.sg_rel_sede_diplomado rsd where rsd.rsd_diplomado_fk = d.dip_pk AND rsd.rsd_habilitado AND rsd.rsd_sede_fk = :sedePk)";

        return em.createNativeQuery(query, SgDiplomado.class)
                .setParameter("sedePk", sedPk)
                .getResultList();

    }

    public List<SgDiplomado> obtenerDiplomadosParaAutorizacion(Long sedPk) {

        String query = "Select d.* from centros_educativos.sg_diplomados d where d.dip_habilitado AND NOT EXISTS "
                + "(select 1 from centros_educativos.sg_rel_sede_diplomado rsd where rsd.rsd_diplomado_fk = d.dip_pk AND rsd.rsd_habilitado AND rsd.rsd_sede_fk = :sedePk)";

        return em.createNativeQuery(query, SgDiplomado.class)
                .setParameter("sedePk", sedPk)
                .getResultList();
    }

    public List<SgDiplomado> obtenerPorFiltro(FiltroDiplomado filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            Boolean distinc = filtro.getSeNecesitaDistinct() != null ? filtro.getSeNecesitaDistinct() : Boolean.FALSE;

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgDiplomado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinc, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDiplomado filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDiplomado.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
