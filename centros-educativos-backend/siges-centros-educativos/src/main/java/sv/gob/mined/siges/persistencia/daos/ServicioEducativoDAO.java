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
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroServicioEducativo;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ServicioEducativoDAO extends HibernateJpaDAOImp<SgServicioEducativo, Integer> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ServicioEducativoDAO.class.getName());
    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public ServicioEducativoDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroServicioEducativo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getSduEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduEstado", filtro.getSduEstado());
            criterios.add(criterio);
        }

        if (filtro.getSduFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "sduFechaHabilitado", filtro.getSduFechaDesde());
            criterios.add(criterio);

        }

        if (filtro.getSduFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "sduFechaHabilitado", filtro.getSduFechaHasta());
            criterios.add(criterio);
        }

        if (filtro.getSecIntegrada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secIntegrada", filtro.getSecIntegrada());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        List<CriteriaTO> sedeOR = new ArrayList();

        if (filtro.getSecSedeFk() != null) {
            MatchCriteriaTO criterio;
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedPk", filtro.getSecSedeFk());
            if (filtro.getSduSedeAdscripta() == null) {
                criterios.add(criterio);
            } else {
                sedeOR.add(criterio);
            }
        }

        if (!StringUtils.isBlank(filtro.getCodigoSede())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sduSede.sedCodigo", filtro.getCodigoSede());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombreSede())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sduSede.sedNombreBusqueda", filtro.getNombreSede());
            criterios.add(criterio);
        }

        if (filtro.getDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }

        if (filtro.getMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }
        if (filtro.getSduSedeAdscripta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSede.sedPk", filtro.getSduSedeAdscripta());
            sedeOR.add(criterio);
            CriteriaTO[] arrCriterioOR = sedeOR.toArray(new CriteriaTO[sedeOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getSecNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
            criterios.add(criterio);
        }

        if (filtro.getSecCicloFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
            criterios.add(criterio);
        }

        if (filtro.getSecModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
            criterios.add(criterio);
        }

        if (filtro.getTieneOpcion() != null) {
            if (filtro.getTieneOpcion()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "sduOpcion", filtro.getTieneOpcion());
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "sduOpcion", filtro.getTieneOpcion());
                criterios.add(criterio);
            }

        }
        
        if (filtro.getTienePrograma() != null) {
            if (filtro.getTienePrograma()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "sduProgramaEducativo", 1);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "sduProgramaEducativo", 1);
                criterios.add(criterio);
            }

        }

        if (filtro.getSecOpcionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduOpcion.opcPk", filtro.getSecOpcionFk());
            criterios.add(criterio);
        }
        if (filtro.getSecGradoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graPk", filtro.getSecGradoFk());
            criterios.add(criterio);
        }
        if (filtro.getSecProgramaEducativoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduProgramaEducativo.pedPk", filtro.getSecProgramaEducativoFk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
            criterios.add(criterio);
        }
        if (filtro.getSecSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
            criterios.add(criterio);
        }
        if (filtro.getSduNumeroTramite() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduNumeroTramite", filtro.getSduNumeroTramite());
            criterios.add(criterio);
        }

        if (filtro.getSecSubModalidadesAtencionFk() != null && !filtro.getSecSubModalidadesAtencionFk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long secPk : filtro.getSecSubModalidadesAtencionFk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", secPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getSduExisteSeccion() != null && filtro.getSduExisteSeccion()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "sduSeccion.secPk", 1);
            criterios.add(criterio);
        }

        if (filtro.getEstadoSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secEstado", filtro.getEstadoSeccion());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getEstadoPromocion() != null) {
            if (filtro.getEstadoPromocion().equals(EnumEstadoProcesamientoCalificacionPromocion.PROCESADO)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secTodasPromocionesGradoRealizadas", Boolean.TRUE);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secTodasPromocionesGradoRealizadas", Boolean.FALSE);
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (BooleanUtils.isTrue(filtro.getSduExisteSeccion())) {
            if (filtro.getSecAnioLectivoFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
                criterios.add(criterio);
            }

            if (filtro.getSecIntegrada() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sduSeccion.secIntegrada", filtro.getSecIntegrada());
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        return criterios;
    }

    public List<SgServicioEducativo> obtenerPorFiltro(FiltroServicioEducativo filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) || SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())
                                || SeguridadAmbitos.IMPLEMENTADORA.name().equals(op.getAmbit())) {
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
            return this.findEntityByCriteria(SgServicioEducativo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroServicioEducativo filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) || SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())) {
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
            return this.countByCriteria(SgServicioEducativo.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
