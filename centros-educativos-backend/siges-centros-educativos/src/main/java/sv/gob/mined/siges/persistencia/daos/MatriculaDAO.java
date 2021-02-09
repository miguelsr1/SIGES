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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class MatriculaDAO extends HibernateJpaDAOImp<SgMatricula, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;
    private static final Logger LOGGER = Logger.getLogger(MatriculaDAO.class.getName());

    public MatriculaDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroMatricula filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getMatRetirada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matRetirada", filtro.getMatRetirada());
            criterios.add(criterio);
        }

        if (filtro.getMatEstudiantePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPk", filtro.getMatEstudiantePk());
            criterios.add(criterio);
        }

        if (filtro.getEstHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estHabilitado", filtro.getEstHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getMatValidadaAcademicamente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matValidacionAcademica", filtro.getMatValidadaAcademicamente());
            criterios.add(criterio);
        }

        if (filtro.getSecPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secPk", filtro.getSecPk());
            criterios.add(criterio);
        }
        if (filtro.getSecSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getMatAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secAnioLectivo.aleAnio", filtro.getMatAnio());
            criterios.add(criterio);
        }
        if (filtro.getMatEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstado", filtro.getMatEstado());
            criterios.add(criterio);
        }

        if (filtro.getMatPromocionGrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matPromocionGrado", filtro.getMatPromocionGrado());
            criterios.add(criterio);
        }

        if (filtro.getSecGradoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecGradoSiguienteFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graSiguientes.rgpGradoDestinoFk.graPk", filtro.getSecGradoSiguienteFk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSecNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
            criterios.add(criterio);
        }
        if (filtro.getSecCicloFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
            criterios.add(criterio);
        }
        if (filtro.getSecSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
            criterios.add(criterio);
        }

        if (filtro.getSecAnioLectivoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecAnioLectivoAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secAnioLectivo.aleAnio", filtro.getSecAnioLectivoAnio());
            criterios.add(criterio);
        }

        if (filtro.getSecOpcionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduOpcion.opcPk", filtro.getSecOpcionFk());
            criterios.add(criterio);
        }
        if (filtro.getSecProgramaEducativoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduProgramaEducativo.pedPk", filtro.getSecProgramaEducativoFk());
            criterios.add(criterio);
        }

        //Por defecto excluir anuladas a menos que se indique lo contrario
        if (filtro.getMatAnulada() == null) {
            if (BooleanUtils.isNotTrue(filtro.getIncluirAnuladas())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matAnulada", false);
                criterios.add(criterio);
            }
        } else {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matAnulada", filtro.getMatAnulada());
            criterios.add(criterio);

        }

        if (filtro.getMatFechaEntreIngresoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "matFechaIngreso", filtro.getMatFechaEntreIngresoHasta());
            criterios.add(criterio);

            List<CriteriaTO> fechaHastaCriteria = new ArrayList();
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "matFechaHasta", filtro.getMatFechaEntreIngresoHasta());
            fechaHastaCriteria.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "matFechaHasta", 1);
            fechaHastaCriteria.add(criterio);
            CriteriaTO[] arrCriterioOR = fechaHastaCriteria.toArray(new CriteriaTO[fechaHastaCriteria.size()]);
            CriteriaTO criterioOR = CriteriaTOUtils.createORTO(arrCriterioOR);

            criterios.add(criterioOR);
        }

        // [Inicio,Fin] superposición [Desde,Hasta] --> [x1,x2] superposición [y1,y2]
        // x1 <= y2 && y1 <= x2
        if (filtro.getMatFechaDesdeSup() != null) {
            List<CriteriaTO> fechaHastaCriteria = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "matFechaHasta", filtro.getMatFechaDesdeSup());
            fechaHastaCriteria.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "matFechaHasta", 1);
            fechaHastaCriteria.add(criterio);
            CriteriaTO[] arrCriterioOR = fechaHastaCriteria.toArray(new CriteriaTO[fechaHastaCriteria.size()]);
            CriteriaTO criterioOR = CriteriaTOUtils.createORTO(arrCriterioOR);
            criterios.add(criterioOR);

        }

        if (filtro.getMatFechaHastaSup() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "matFechaIngreso", filtro.getMatFechaHastaSup());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoUsuarioCreador())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matCreacionUsuario", filtro.getCodigoUsuarioCreador());
            criterios.add(criterio);
        }

        if (filtro.getMatSeccionesPk() != null && !filtro.getMatSeccionesPk().isEmpty()) {
            List<CriteriaTO> seccionesCriteria = new ArrayList();
            for (Long secPk : filtro.getMatSeccionesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secPk", secPk);
                seccionesCriteria.add(criterio);
            }

            if (!seccionesCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = seccionesCriteria.toArray(new CriteriaTO[seccionesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getMatListPk() != null && !filtro.getMatListPk().isEmpty()) {
            List<CriteriaTO> seccionesCriteria = new ArrayList();
            for (Long secPk : filtro.getMatListPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matPk", secPk);
                seccionesCriteria.add(criterio);
            }

            if (!seccionesCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = seccionesCriteria.toArray(new CriteriaTO[seccionesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }

        }

        if (filtro.getMatEstudiantesPk() != null && !filtro.getMatEstudiantesPk().isEmpty()) {
            List<CriteriaTO> estudiantesCriteria = new ArrayList();
            for (Long estPk : filtro.getMatEstudiantesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPk", estPk);
                estudiantesCriteria.add(criterio);
            }

            if (!estudiantesCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = estudiantesCriteria.toArray(new CriteriaTO[estudiantesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPersona.perNie", filtro.getNie());
            criterios.add(criterio);
        }

        if (filtro.getMatExcluirPk() != null && !filtro.getMatExcluirPk().isEmpty()) {
            List<CriteriaTO> matCriteria = new ArrayList();
            for (Long matPk : filtro.getMatExcluirPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "matPk", matPk);
                matCriteria.add(criterio);
            }

            if (!matCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = matCriteria.toArray(new CriteriaTO[matCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (BooleanUtils.isTrue(filtro.getGradosRequierenValidacionAcademica())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduGrado.graRelGradoPlanEstudio.rgpRequiereValidacionAcademica", filtro.getGradosRequierenValidacionAcademica());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getMatNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPersona.perNie", filtro.getGradosRequierenValidacionAcademica());
            criterios.add(criterio);
        }
        if (filtro.getMatExcluirNieNull() != null && BooleanUtils.isTrue(filtro.getMatExcluirNieNull())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "matEstudiante.estPersona.perNie", 1);
            criterios.add(criterio);
        }
        
        if (filtro.getMatPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matPk", filtro.getMatPk());
            criterios.add(criterio);
        }
        if(filtro.getMatCerradaCierreAnio()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matCerradoPorCierreAnio", filtro.getMatCerradaCierreAnio());
            criterios.add(criterio);
        }
        
        if(filtro.getMatCerradaCierreAnioOAbierta()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matCerradoPorCierreAnio", filtro.getMatCerradaCierreAnioOAbierta());
           
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "matEstado", EnumMatriculaEstado.ABIERTO);

            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
        if(filtro.getMatDepartamentoFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getMatDepartamentoFk());
            criterios.add(criterio);
        }
        if(filtro.getMatSexoFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPersona.perSexo.sexPk", filtro.getMatSexoFk());
            criterios.add(criterio);
        }
        if(filtro.getSoloModalidadesFlexibles()!=null && BooleanUtils.isTrue(filtro.getSoloModalidadesFlexibles())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
            criterios.add(criterio);
        }
        if(filtro.getMatEstadoCivil()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "matEstudiante.estPersona.perEstadoCivil.eciPk", filtro.getMatEstadoCivil());
            criterios.add(criterio);
        }   
        if(filtro.getMatSubmodalidad()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getMatSubmodalidad());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgMatricula> obtenerPorFiltro(FiltroMatricula filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMatricula.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(),
                    securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroMatricula filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMatricula.class, criterio, filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
