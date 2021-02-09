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
import sv.gob.mined.siges.filtros.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ControlAsistenciaCabezalDAO extends HibernateJpaDAOImp<SgControlAsistenciaCabezal, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public ControlAsistenciaCabezalDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroControlAsistenciaCabezal filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCacPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacPk", filtro.getCacPk());
            criterios.add(criterio);
        }
        if (filtro.getCacFecha() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacFecha", filtro.getCacFecha());
            criterios.add(criterio);
        }
        if (filtro.getSecSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getSecNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
            criterios.add(criterio);
        }
        if (filtro.getSecCicloFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
            criterios.add(criterio);
        }

        if (filtro.getSecModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
            criterios.add(criterio);
        }

        if (filtro.getSecSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
            criterios.add(criterio);
        }

        if (filtro.getSecGradoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecAnioLectivoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secPk", filtro.getSecPk());
            criterios.add(criterio);
        }
        if (filtro.getDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }
        if (filtro.getMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }
        if (filtro.getCacDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "cacFecha", filtro.getCacDesde());
            criterios.add(criterio);
        }
        if (filtro.getCacHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "cacFecha", filtro.getCacHasta());
            criterios.add(criterio);
        }
        if (filtro.getSecTipoCalendario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cacSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk", filtro.getSecTipoCalendario());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgControlAsistenciaCabezal> obtenerPorFiltro(FiltroControlAsistenciaCabezal filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgControlAsistenciaCabezal.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroControlAsistenciaCabezal filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgControlAsistenciaCabezal.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
