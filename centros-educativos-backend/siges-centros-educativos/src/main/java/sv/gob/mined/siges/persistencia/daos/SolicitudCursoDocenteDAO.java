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
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudCurso;
import sv.gob.mined.siges.filtros.FiltroSolicitudCursoDocente;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudCursoDocente;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class SolicitudCursoDocenteDAO extends HibernateJpaDAOImp<SgSolicitudCursoDocente, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public SolicitudCursoDocenteDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSolicitudCursoDocente filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getScdCurso() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "scdCurso.cdsPk", filtro.getScdCurso());
            criterios.add(criterio);
        }

        if (filtro.getScdEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "scdEstado", filtro.getScdEstado());
            criterios.add(criterio);
        }

        if (filtro.getScdDui() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "scdPersonal.psePersona.perDui", filtro.getScdDui());
            criterios.add(criterio);
        }

        if (filtro.getScdPersonal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "scdPersonal.psePk", filtro.getScdPersonal());
            criterios.add(criterio);
        }

        if (filtro.getScdEstados() != null && !filtro.getScdEstados().isEmpty()) {
            List<CriteriaTO> datosAND = new ArrayList();
            for (EnumEstadoSolicitudCurso estado : filtro.getScdEstados()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "scdEstado", estado);
                datosAND.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = datosAND.toArray(new CriteriaTO[datosAND.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        return criterios;
    }

    public List<SgSolicitudCursoDocente> obtenerPorFiltro(FiltroSolicitudCursoDocente filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgSolicitudCursoDocente.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroSolicitudCursoDocente filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSolicitudCursoDocente.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
