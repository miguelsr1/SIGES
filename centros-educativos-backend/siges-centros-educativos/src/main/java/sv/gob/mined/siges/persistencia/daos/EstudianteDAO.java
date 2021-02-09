/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class EstudianteDAO extends HibernateJpaDAOImp<SgEstudiante, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    private EntityManager em;

    public EstudianteDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getEstPersona() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perPk", filtro.getEstPersona());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estPersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }
        
        if (filtro.getEstEncuestaRealizada() != null){
            if (filtro.getEstEncuestaRealizada()){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "estUltimaEncuesta", 1);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "estUltimaEncuesta", 1);
                criterios.add(criterio);
            }
        }

        if (filtro.getEstTrabaja() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estTrabaja", filtro.getEstTrabaja());
            criterios.add(criterio);
        }
        if (filtro.getEstEmbarazo() != null) {
            if(BooleanUtils.isTrue(Boolean.TRUE)){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estEmbarazo", filtro.getEstEmbarazo());
                criterios.add(criterio);
            }else{
                criterios.add(CriteriaTOUtils.createORTO(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estEmbarazo", filtro.getEstEmbarazo()),
                        CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "estEmbarazo", 1)) );
            }
            
        }

        if (filtro.getEstSinNie() != null) {
            if (filtro.getEstSinNie()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "estPersona.perNie", 1);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "estPersona.perNie", 1);
                criterios.add(criterio);
            }
        }
        if (filtro.getPerSexoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perSexo.sexPk", filtro.getPerSexoPk());
            criterios.add(criterio);
        }
        if (filtro.getPerDepartamentoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perDireccion.dirDepartamento.depPk", filtro.getPerDepartamentoPk());
            criterios.add(criterio);
        }
        if (filtro.getPerMunicipioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perDireccion.dirMunicipio.munPk", filtro.getPerMunicipioPk());
            criterios.add(criterio);
        }
        if (filtro.getPerZonaPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perDireccion.dirZona.zonPk", filtro.getPerZonaPk());
            criterios.add(criterio);
        }
        if (filtro.getPerFechaNacimientoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "estPersona.perFechaNacimiento", filtro.getPerFechaNacimientoDesde());
            criterios.add(criterio);
        }
        if (filtro.getPerFechaNacimientoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "estPersona.perFechaNacimiento", filtro.getPerFechaNacimientoHasta());
            criterios.add(criterio);
        }

        List<CriteriaTO> indentificacionesOR = new ArrayList();
        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perNie", filtro.getNie());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perDui", filtro.getDui());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getNip() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perNip", filtro.getNip());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getCun() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perCun", filtro.getCun());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getPerTieneIdentificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perTieneIdentificacion", filtro.getPerTieneIdentificacion());
            criterios.add(criterio);
        }

        if (filtro.getPerPresentaPartida() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perPartidaNacimientoPresenta", filtro.getPerPresentaPartida());
            criterios.add(criterio);
        }

        if (!indentificacionesOR.isEmpty()) {
            CriteriaTO[] arrCriterioOR = indentificacionesOR.toArray(new CriteriaTO[indentificacionesOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }
        
        if (filtro.getPerTieneDiscapacidad()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perTieneDiscapacidades", filtro.getPerTieneDiscapacidad());
            criterios.add(criterio);
        }

        if (BooleanUtils.isNotTrue(filtro.getBuscarSoloEnUltimaMatricula())) {

            if (filtro.getSecSedeFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getSecNivelFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }
            if (filtro.getSecCicloFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }
            if (filtro.getSecModalidadEducativaPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getSecModalidadAtencionPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getSecSubModalidadAtencionFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getSecGradoFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getSecPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secPk", filtro.getSecPk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getEstEstadoMatricula() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matEstado", filtro.getEstEstadoMatricula());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }
            if (filtro.getMatAnulada() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matAnulada", filtro.getMatAnulada());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getSecAnioLectivoFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getEstDepartamentoMatricula() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getEstDepartamentoMatricula());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getEstMunicipioMatricula() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munPk", filtro.getEstMunicipioMatricula());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

            if (filtro.getEstComponentePlanEstudioPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduGrado.graCompPlanGrado.cpgComponentePlanEstudio.cpePk", filtro.getEstComponentePlanEstudioPk());
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }

        } else {

            if (filtro.getSecSedeFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
                criterios.add(criterio);
            }

            if (filtro.getSecNivelFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
                criterios.add(criterio);
            }
            if (filtro.getSecCicloFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
                criterios.add(criterio);
            }
            if (filtro.getSecModalidadEducativaPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
                criterios.add(criterio);
            }

            if (filtro.getSecModalidadAtencionPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
                criterios.add(criterio);
            }

            if (filtro.getSecSubModalidadAtencionFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
                criterios.add(criterio);
            }

            if (filtro.getSecGradoFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
                criterios.add(criterio);
            }

            if (filtro.getSecPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secPk", filtro.getSecPk());
                criterios.add(criterio);
            }

            if (filtro.getEstEstadoMatricula() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matEstado", filtro.getEstEstadoMatricula());
                criterios.add(criterio);
            }
            if (filtro.getMatAnulada() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matAnulada", filtro.getMatAnulada());
                criterios.add(criterio);
            }

            if (filtro.getSecAnioLectivoFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
                criterios.add(criterio);
            }

            if (filtro.getEstDepartamentoMatricula() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getEstDepartamentoMatricula());
                criterios.add(criterio);
            }

            if (filtro.getEstMunicipioMatricula() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munPk", filtro.getEstMunicipioMatricula());
                criterios.add(criterio);
            }

            if (filtro.getEstComponentePlanEstudioPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graCompPlanGrado.cpgComponentePlanEstudio.cpePk", filtro.getEstComponentePlanEstudioPk());
                criterios.add(criterio);
            }

        }

        if (filtro.getEstPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPk", filtro.getEstPk());
            criterios.add(criterio);
        }

        if (filtro.getEstudiantesPk() != null && !filtro.getEstudiantesPk().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "estPk", filtro.getEstudiantesPk());
            criterios.add(criterio);
        }

        if (filtro.getEstudiantesNie() != null && !filtro.getEstudiantesNie().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "estPersona.perNie", filtro.getEstudiantesNie());
            criterios.add(criterio);
        }
        if (filtro.getEstadoCivilFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estPersona.perEstadoCivil.eciPk", filtro.getEstadoCivilFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgEstudiante> obtenerPorFiltro(FiltroEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            Boolean distinct = filtro.getSeNecesitaDistinct();
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                        if (SeguridadAmbitos.DEPARTAMENTAL.name().equals(op.getAmbit())
                                || SeguridadAmbitos.SISTEMA_INTEGRADO.name().equals(op.getAmbit())
                                || SeguridadAmbitos.SEDE.name().equals(op.getAmbit())
                                || SeguridadAmbitos.PARTICION_SEDE.name().equals(op.getAmbit())) {
                            distinct = Boolean.TRUE;
                        }
                    }
                }
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            Boolean distinct = filtro.getSeNecesitaDistinct();
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                        if (SeguridadAmbitos.DEPARTAMENTAL.name().equals(op.getAmbit())
                                || SeguridadAmbitos.SISTEMA_INTEGRADO.name().equals(op.getAmbit())
                                || SeguridadAmbitos.SEDE.name().equals(op.getAmbit())
                                || SeguridadAmbitos.PARTICION_SEDE.name().equals(op.getAmbit())) {
                            distinct = Boolean.TRUE;
                        }
                    }
                }
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEstudiante.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
