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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionDiplomadoEstudianteDAO extends HibernateJpaDAOImp<SgCalificacionDiplomadoEstudiante, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public CalificacionDiplomadoEstudianteDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.segDAO = segBean;
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroCalificacionDiplomadoEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCdeCalificacionDiplomadoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdeCalificacionDiplomadoFk.cadPk", filtro.getCdeCalificacionDiplomadoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getCdeEstudianteFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdeEstudianteFk.estPk", filtro.getCdeEstudianteFk());
            criterios.add(criterio);
        }
        
        if (filtro.getAnioLectivoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdeCalificacionDiplomadoFk.cadAnioLectivoFk.alePk", filtro.getAnioLectivoFk());
            criterios.add(criterio);
        }
       
        if (filtro.getModuloDiplomadoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdeCalificacionDiplomadoFk.cadModuloDiplomado.mdiPk", filtro.getModuloDiplomadoFk());
            criterios.add(criterio);
        }
        if (filtro.getSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdeCalificacionDiplomadoFk.cadSedeFk.sedPk", filtro.getSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getDiplomadoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdeCalificacionDiplomadoFk.cadModuloDiplomado.mdiDiplomado.dipPk", filtro.getDiplomadoFk());
            criterios.add(criterio);
        }
        
        if(filtro.getTipoCalificacionDiplomadoList()!=null && filtro.getTipoCalificacionDiplomadoList().size()>0){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cdeCalificacionDiplomadoFk.cadTipoCalificacion", filtro.getTipoCalificacionDiplomadoList());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgCalificacionDiplomadoEstudiante> obtenerPorFiltro(FiltroCalificacionDiplomadoEstudiante filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCalificacionDiplomadoEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,  securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCalificacionDiplomadoEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);


            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCalificacionDiplomadoEstudiante.class, criterio, false,  securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
