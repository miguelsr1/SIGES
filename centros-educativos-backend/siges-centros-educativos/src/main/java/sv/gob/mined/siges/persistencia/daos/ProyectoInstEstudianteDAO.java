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
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroProyectoInstEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgProyectoInstEstudiante;

public class ProyectoInstEstudianteDAO extends HibernateJpaDAOImp<SgProyectoInstEstudiante, Integer> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProyectoInstEstudianteDAO.class.getName());
    private EntityManager em;

    public ProyectoInstEstudianteDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroProyectoInstEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getProyecto() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pieProyectoInstitucional.proPk", filtro.getProyecto());
            criterios.add(criterio);
        }

        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pieEstudiante.estPersona.perNie", filtro.getNie());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pieEstudiante.estPersona.perPrimerNombreBusqueda", filtro.getPrimerNombre().toLowerCase().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pieEstudiante.estPersona.perSegundoNombreBusqueda", filtro.getSegundoNombre().toLowerCase().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pieEstudiante.estPersona.perTercerNombreBusqueda", filtro.getTercerNombre().toLowerCase().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pieEstudiante.estPersona.perPrimerApellidoBusqueda", filtro.getPrimerApellido().toLowerCase().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pieEstudiante.estPersona.perSegundoApellidoBusqueda", filtro.getSegundoApellido().toLowerCase().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "pieEstudiante.estPersona.perTercerApellidoBusqueda", filtro.getTercerApellido().toLowerCase().trim());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgProyectoInstEstudiante> obtenerPorFiltro(FiltroProyectoInstEstudiante filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgProyectoInstEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroProyectoInstEstudiante filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgProyectoInstEstudiante.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
