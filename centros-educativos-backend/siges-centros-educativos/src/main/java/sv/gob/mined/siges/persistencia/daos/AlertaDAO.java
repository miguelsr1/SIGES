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
import sv.gob.mined.siges.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.enumerados.EnumVariableAlerta;
import sv.gob.mined.siges.filtros.FiltroAlerta;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;

import sv.gob.mined.siges.persistencia.entidades.SgAlerta;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author usuario
 */
public class AlertaDAO extends HibernateJpaDAOImp<SgAlerta, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public AlertaDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroAlerta filtro) {
        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAleVariables() != null && !filtro.getAleVariables().isEmpty()) {
            List<CriteriaTO> variablesCriteria = new ArrayList();
            for (EnumVariableAlerta alerta : filtro.getAleVariables()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleVariable", alerta);
                variablesCriteria.add(criterio);
            }

            if (!variablesCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = variablesCriteria.toArray(new CriteriaTO[variablesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getAleRiesgos() != null && !filtro.getAleRiesgos().isEmpty()) {
            List<CriteriaTO> riesgosCriteria = new ArrayList();
            for (EnumRiesgoAlerta alerta : filtro.getAleRiesgos()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleRiesgo", alerta);
                riesgosCriteria.add(criterio);
            }

            if (!riesgosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = riesgosCriteria.toArray(new CriteriaTO[riesgosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getEstPersona() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perPk", filtro.getEstPersona());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "aleEstudiante.estPersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }

        if (filtro.getEstTrabaja() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estTrabaja", filtro.getEstTrabaja());
            criterios.add(criterio);
        }
        if (filtro.getEstEmbarazo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estEmbarazo", filtro.getEstEmbarazo());
            criterios.add(criterio);
        }

        if (filtro.getEstSinNie() != null) {
            if (filtro.getEstSinNie()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "aleEstudiante.estPersona.perNie", 1);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "aleEstudiante.estPersona.perNie", 1);
                criterios.add(criterio);
            }
        }
        if (filtro.getPerSexoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perSexo.sexPk", filtro.getPerSexoPk());
            criterios.add(criterio);
        }
        if (filtro.getPerDepartamentoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perDireccion.dirDepartamento.depPk", filtro.getPerDepartamentoPk());
            criterios.add(criterio);
        }
        if (filtro.getPerMunicipioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perDireccion.dirMunicipio.munPk", filtro.getPerMunicipioPk());
            criterios.add(criterio);
        }
        if (filtro.getPerFechaNacimientoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "aleEstudiante.estPersona.perFechaNacimiento", filtro.getPerFechaNacimientoDesde());
            criterios.add(criterio);
        }
        if (filtro.getPerFechaNacimientoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "aleEstudiante.estPersona.perFechaNacimiento", filtro.getPerFechaNacimientoHasta());
            criterios.add(criterio);
        }

        List<CriteriaTO> indentificacionesOR = new ArrayList();
        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perNie", filtro.getNie());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perDui", filtro.getDui());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getNip() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perNip", filtro.getNip());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getCun() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perCun", filtro.getCun());
            indentificacionesOR.add(criterio);
        }

        if (!indentificacionesOR.isEmpty()) {
            CriteriaTO[] arrCriterioOR = indentificacionesOR.toArray(new CriteriaTO[indentificacionesOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getSecSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
            criterios.add(criterio);
        }

        if (filtro.getSecNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
            criterios.add(criterio);
        }
        if (filtro.getSecCicloFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
            criterios.add(criterio);
        }

        if (filtro.getSecModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
            criterios.add(criterio);
        }

        if (filtro.getSecSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
            criterios.add(criterio);
        }

        if (filtro.getSecGradoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secPk", filtro.getSecPk());
            criterios.add(criterio);
        }

        if (filtro.getEstEstadoMatricula() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matEstado", filtro.getEstEstadoMatricula());
            criterios.add(criterio);
        }

        if (filtro.getSecAnioLectivoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
            criterios.add(criterio);
        }

        if (filtro.getPerTieneIdentificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perTieneIdentificacion", filtro.getPerTieneIdentificacion());
            criterios.add(criterio);
        }

        if (filtro.getPerPresentaPartida() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perPartidaNacimientoPresenta", filtro.getPerPresentaPartida());
            criterios.add(criterio);
        }

        if (filtro.getEstDepartamentoMatricula() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getEstDepartamentoMatricula());
            criterios.add(criterio);
        }

        if (filtro.getEstMunicipioMatricula() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munPk", filtro.getEstMunicipioMatricula());
            criterios.add(criterio);
        }

        if (filtro.getEstComponentePlanEstudioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graCompPlanGrado.cpgComponentePlanEstudio.cpePk", filtro.getEstComponentePlanEstudioPk());
            criterios.add(criterio);
        }
        if (filtro.getEstPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPk", filtro.getEstPk());
            criterios.add(criterio);
        }

        if (filtro.getEstudiantesPk() != null && !filtro.getEstudiantesPk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long estPk : filtro.getEstudiantesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPk", estPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getEstudiantesNie() != null && !filtro.getEstudiantesNie().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long estPk : filtro.getEstudiantesNie()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aleEstudiante.estPersona.perNie", estPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        return criterios;

    }

    public List<SgAlerta> obtenerPorFiltro(FiltroAlerta filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAlerta.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAlerta filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAlerta.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
