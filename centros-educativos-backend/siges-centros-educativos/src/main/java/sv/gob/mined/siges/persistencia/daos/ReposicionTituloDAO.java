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
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroReposicionTitulo;
import sv.gob.mined.siges.persistencia.entidades.SgReposicionTitulo;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class ReposicionTituloDAO extends HibernateJpaDAOImp<SgReposicionTitulo, Integer> implements Serializable {

    private SeguridadDAO segDAO;

    public ReposicionTituloDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroReposicionTitulo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getEstudiante())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "retNombreEstudiantePartidaBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getEstudiante()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSede())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "retSedeNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSede()));
            criterios.add(criterio);
        }
        if (filtro.getSimEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "retSolicitudImpresion.simEstado", filtro.getSimEstado());
            criterios.add(criterio);
        }
        if (filtro.getSimTipoImpresion()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "retSolicitudImpresion.simTipoImpresion", filtro.getSimTipoImpresion());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgReposicionTitulo> obtenerPorFiltro(FiltroReposicionTitulo filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgReposicionTitulo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null,filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroReposicionTitulo filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgReposicionTitulo.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
