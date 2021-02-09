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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroReporteDirector;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgReporteDirector;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class ReporteDirectorDAO extends HibernateJpaDAOImp<SgReporteDirector, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public ReporteDirectorDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroReporteDirector filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getRidSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiSede.sedPk", filtro.getRidSede());
            criterios.add(criterio);
        }
        if (filtro.getRidSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiSede.sedPk", filtro.getRidSede());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSedNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "rdiSede.sedNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSedCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "rdiSede.sedCodigo", filtro.getSedCodigo().trim());
            criterios.add(criterio);
        }
        if (filtro.getRdiDatosEstudiante() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiDatosEstudiante", filtro.getRdiDatosEstudiante());
            criterios.add(criterio);
        }
        if (filtro.getRdiDatosPersonal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiDatosPersonal", filtro.getRdiDatosPersonal());
            criterios.add(criterio);
        }
        if (filtro.getRdiDatosSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiDatosSede", filtro.getRdiDatosSede());
            criterios.add(criterio);
        }
        if (filtro.getSedDepartamentoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiSede.sedDireccion.dirDepartamento.depPk", filtro.getSedDepartamentoFk());
            criterios.add(criterio);
        }
        if (filtro.getSedMunicipioFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiSede.sedDireccion.dirMunicipio.munPk", filtro.getSedMunicipioFk());
            criterios.add(criterio);
        }

        if (filtro.getRdiDatosGeneral() != null) {
            List<CriteriaTO> datosAND = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiDatosEstudiante", filtro.getRdiDatosGeneral());
            datosAND.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiDatosPersonal", filtro.getRdiDatosGeneral());
            datosAND.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rdiDatosSede", filtro.getRdiDatosGeneral());
            datosAND.add(criterio);
            CriteriaTO[] arrCriterioAND = datosAND.toArray(new CriteriaTO[datosAND.size()]);
            CriteriaTO criterioAND = CriteriaTOUtils.createORTO(arrCriterioAND);
            criterios.add(criterioAND);
        }

        return criterios;
    }

    public List<SgReporteDirector> obtenerPorFiltro(FiltroReporteDirector filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgReporteDirector.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroReporteDirector filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgReporteDirector.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
