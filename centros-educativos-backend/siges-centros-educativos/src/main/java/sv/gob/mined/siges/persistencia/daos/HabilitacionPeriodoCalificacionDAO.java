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
import sv.gob.mined.siges.filtros.FiltroHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class HabilitacionPeriodoCalificacionDAO extends HibernateJpaDAOImp<SgHabilitacionPeriodoCalificacion, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private EntityManager em;
    private JsonWebToken jwt;

    public HabilitacionPeriodoCalificacionDAO(EntityManager em, SeguridadBean seg) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
        this.segDAO=seg;
    }

    private List<CriteriaTO> generarCriteria(FiltroHabilitacionPeriodoCalificacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getHpcDepartamentoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getHpcDepartamentoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getHpcSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedPk", filtro.getHpcSedeFk());
            criterios.add(criterio);
        }
         if (filtro.getHpcNie()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcEstudianteFk.estPersona.perNie", filtro.getHpcNie());
            criterios.add(criterio);
        }
         
         if (filtro.getHpcEstado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcEstado", filtro.getHpcEstado());
            criterios.add(criterio);
        }
      

       

        return criterios;
    }

    public List<SgHabilitacionPeriodoCalificacion> obtenerPorFiltro(FiltroHabilitacionPeriodoCalificacion filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgHabilitacionPeriodoCalificacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,  segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()),filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroHabilitacionPeriodoCalificacion filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgHabilitacionPeriodoCalificacion.class, criterio, false,  segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public SgHabilitacionPeriodoCalificacion obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(SgHabilitacionPeriodoCalificacion.class, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
