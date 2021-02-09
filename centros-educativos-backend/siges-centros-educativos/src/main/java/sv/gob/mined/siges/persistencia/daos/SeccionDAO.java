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
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class SeccionDAO extends HibernateJpaDAOImp<SgSeccion, Integer> implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(SeccionDAO.class.getName());
    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public SeccionDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSeccion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getSecPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secPk", filtro.getSecPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getSecCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secCodigo", filtro.getSecCodigo());
            criterios.add(criterio);
        }
        if (filtro.getSecSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getSecGradoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
            criterios.add(criterio);
        }
        if (filtro.getSecAnioLectivoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecAnioLectivoAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secAnioLectivo.aleAnio", filtro.getSecAnioLectivoAnio());
            criterios.add(criterio);
        }

        if (filtro.getSecIntegrada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secIntegrada", filtro.getSecIntegrada());
            criterios.add(criterio);
        }

        if (filtro.getSecServicioEducativoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduPk", filtro.getSecServicioEducativoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecServiciosEducativoFk() != null && !filtro.getSecServiciosEducativoFk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long sduPk : filtro.getSecServiciosEducativoFk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduPk", sduPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (!StringUtils.isBlank(filtro.getCodigoSede())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "secServicioEducativo.sduSede.sedCodigo", filtro.getCodigoSede().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombreSede())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "secServicioEducativo.sduSede.sedNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getNombreSede()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getSecNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "secNombre", filtro.getSecNombre().trim());
            criterios.add(criterio);
        }

        if (filtro.getDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }

        if (filtro.getMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }

        if (filtro.getSecCicloFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
            criterios.add(criterio);
        }

        if (filtro.getSecNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
            criterios.add(criterio);
        }

        if (filtro.getSecModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
            criterios.add(criterio);
        }
        if (filtro.getSecSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
            criterios.add(criterio);
        }

        if (filtro.getSecOpcionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduOpcion.opcPk", filtro.getSecOpcionFk());
            criterios.add(criterio);
        }
        if (filtro.getSecProgramaEducativoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduProgramaEducativo.pedPk", filtro.getSecProgramaEducativoFk());
            criterios.add(criterio);
        }

        if (filtro.getSecEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secEstado", filtro.getSecEstado());
            criterios.add(criterio);
        }

        if (filtro.getSecPlanEstudioFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secPlanEstudio.pesPk", filtro.getSecPlanEstudioFk());
            criterios.add(criterio);
        }

        if (filtro.getSeccionesPk() != null && !filtro.getSeccionesPk().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "secPk", filtro.getSeccionesPk());
            criterios.add(criterio);
        }

        if (filtro.getSecAnios() != null && !filtro.getSecAnios().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Integer anio : filtro.getSecAnios()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secAnioLectivo.aleAnio", anio);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getSecSubModalidadesAtencionFk() != null && !filtro.getSecSubModalidadesAtencionFk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long secPk : filtro.getSecSubModalidadesAtencionFk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", secPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        
        if (filtro.getEstadoSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secEstado", filtro.getEstadoSeccion());
            criterios.add(criterio);
        }
        
        if(filtro.getEstadoPromocion() != null){
            if(filtro.getEstadoPromocion().equals(EnumEstadoProcesamientoCalificacionPromocion.PROCESADO)){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secTodasPromocionesGradoRealizadas", Boolean.TRUE);
                criterios.add(criterio); 
            }else{
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secTodasPromocionesGradoRealizadas", Boolean.FALSE);
                criterios.add(criterio);
            }
        }
        
        return criterios;
    }

    public List<SgSeccion> obtenerPorFiltro(FiltroSeccion filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())) {
                            filtro.setSeNecesitaDistinct(Boolean.TRUE);
                            break;
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
            return this.findEntityByCriteria(SgSeccion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroSeccion filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())) {
                            filtro.setSeNecesitaDistinct(Boolean.TRUE);
                            break;
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
            return this.countByCriteria(SgSeccion.class, criterio, filtro.getSeNecesitaDistinct(), ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
