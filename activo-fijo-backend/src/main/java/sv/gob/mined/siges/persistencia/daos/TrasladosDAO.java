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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.persistencia.entidades.SgAfTraslado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class TrasladosDAO extends HibernateJpaDAOImp<SgAfTraslado, Long> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public TrasladosDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroBienesDepreciables filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if(StringUtils.isNotBlank(filtro.getCodigoTraslado())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "traCodigoTraslado", filtro.getCodigoTraslado().trim());
                    criterios.add(criterio);
        }
        
        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "traUnidadAdmOrigenFk.uadPk", null);
                    criterios.add(criterioNotNullAD);

                    if(filtro.getUnidadActivoFijoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traUnidadAdmOrigenFk.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traUnidadAdmOrigenFk.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "traSedeOrigenFk.sedPk", null);
                    criterios.add(criterioNotNullCE);

                    if(filtro.getDepartamentoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traSedeOrigenFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getMunicipioId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traSedeOrigenFk.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traSedeOrigenFk.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }
        
        if(filtro.getTipoUnidadDestino() != null) {
            switch (filtro.getTipoUnidadDestino()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "traUnidadAdmDestinoFk.uadPk", null);
                    criterios.add(criterioNotNullAD);

                    if(filtro.getUnidadActivoFijoDestinoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traUnidadAdmDestinoFk.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoDestinoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaDestinoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traUnidadAdmDestinoFk.uadPk", filtro.getUnidadAdministrativaDestinoId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "traSedeDestinoFk.sedPk", null);
                    criterios.add(criterioNotNullCE);

                    if(filtro.getDepartamentoDestinoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traSedeDestinoFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoDestinoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getMunicipioDestinoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traSedeDestinoFk.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioDestinoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaDestinoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "traSedeDestinoFk.sedPk", filtro.getUnidadAdministrativaDestinoId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }
        
        if(filtro.getEstadoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "traEstadoFk.ebiPk", filtro.getEstadoId());
            criterios.add(criterio);
        }

        if(filtro.getEstadoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "traEstadoFk.ebiPk", filtro.getEstadoId());
            criterios.add(criterio);
        }
        if(StringUtils.isNotBlank(filtro.getEstadoCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "traEstadoFk.ebiCodigo", filtro.getEstadoCodigo().trim());
            criterios.add(criterio);
        }
        if(filtro.getAnio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "traAnio", filtro.getAnio().intValue());
            criterios.add(criterio);
        }
        
        if(filtro.getTipoTraslado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "traTipoTrasladoFk.etrPk", filtro.getTipoTraslado());
            criterios.add(criterio);
        }
        
        if(StringUtils.isNotBlank(filtro.getCodigoInventario()) ) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "sgAfTrasladosDetalle.tdeBienesDepreciablesFk.bdeCodigoInventario", filtro.getCodigoInventario().trim());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        
        if (filtro.getFechaSolicitudDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "traFechaSolicitud", filtro.getFechaSolicitudDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaSolicitudHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "traFechaSolicitud", filtro.getFechaSolicitudHasta());
            criterios.add(criterio);
        }
        
        if (filtro.getFechaTrasladoDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "traFechaTraslado", filtro.getFechaTrasladoDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaTrasladoHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "traFechaTraslado", filtro.getFechaTrasladoHasta());
            criterios.add(criterio);
        }

        if (filtro.getSolicitudVencida()!= null && filtro.getSolicitudVencida()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "traFechaRetono", LocalDate.now());
            criterios.add(criterio);
        }
        if (filtro.getValorAdquisicionDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "sgAfTrasladosDetalle.tdeBienesDepreciablesFk.bdeValorAdquisicion", filtro.getValorAdquisicionDesde());
            criterios.add(criterio);
        }
        
        if (filtro.getValorAdquisicionHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "sgAfTrasladosDetalle.tdeBienesDepreciablesFk.bdeValorAdquisicion", filtro.getValorAdquisicionHasta());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgAfTraslado> obtenerPorFiltro(FiltroBienesDepreciables filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = this.generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgAfTraslado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfTraslado.class, criterio, filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}