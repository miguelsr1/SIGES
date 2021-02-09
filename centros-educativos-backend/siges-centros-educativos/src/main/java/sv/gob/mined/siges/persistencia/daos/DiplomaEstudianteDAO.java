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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroDiplomaEstudiante;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomaEstudiante;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
public class DiplomaEstudianteDAO extends HibernateJpaDAOImp<SgDiplomaEstudiante, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;
    private EntityManager em;

    public DiplomaEstudianteDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroDiplomaEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getEstudianteFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dieEstudianteFk.estPk", filtro.getEstudianteFk());
            criterios.add(criterio);
        }

        if (filtro.getDiplomaFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dieDiplomaFk.dilPk", filtro.getDiplomaFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgDiplomaEstudiante> obtenerPorFiltro(FiltroDiplomaEstudiante filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgDiplomaEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDiplomaEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDiplomaEstudiante.class, criterio, false,null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
