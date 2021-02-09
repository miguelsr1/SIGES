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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargo;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class DescargosDAO extends HibernateJpaDAOImp<SgAfDescargo, Long> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public DescargosDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroBienesDepreciables filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (StringUtils.isNotBlank(filtro.getCodigoDescargo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "desCodigoDescargo", filtro.getCodigoDescargo().trim());
            criterios.add(criterio);
        }
        

        if (filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "desUnidadAdministrativaFk.uadPk", null);
                    criterios.add(criterioNotNullAD);

                    if (filtro.getUnidadActivoFijoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "desUnidadAdministrativaFk.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                        criterios.add(criterio);
                    }
                    if (filtro.getUnidadAdministrativaId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "desUnidadAdministrativaFk.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "desSedeFk.sedPk", null);
                    criterios.add(criterioNotNullCE);

                    if (filtro.getDepartamentoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "desSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                        criterios.add(criterio);
                    }
                    if (filtro.getMunicipioId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "desSedeFk.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
                        criterios.add(criterio);
                    }
                    if (filtro.getUnidadAdministrativaId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "desSedeFk.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }

        if (filtro.getEstadoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "desEstadoFk.ebiPk", filtro.getEstadoId());
            criterios.add(criterio);
        }
        if(StringUtils.isNotBlank(filtro.getEstadoCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "desEstadoFk.ebiCodigo", filtro.getEstadoCodigo().trim());
            criterios.add(criterio);
        }

        if (filtro.getActivos() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "desActivo", filtro.getActivos());
            criterios.add(criterio);
        }

        if (StringUtils.isNotBlank(filtro.getCodigoInventario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "sgAfDescargosDetalle.ddeBienesDepreciablesFk.bdeCodigoInventario", filtro.getCodigoInventario().trim());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getFechaSolicitudDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "desFechaSolicitud", filtro.getFechaSolicitudDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaSolicitudHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "desFechaSolicitud", filtro.getFechaSolicitudHasta());
            criterios.add(criterio);
        }

        if (filtro.getFechaDescargoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "desFechaDescargo", filtro.getFechaDescargoDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaDescargoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "desFechaDescargo", filtro.getFechaDescargoHasta());
            criterios.add(criterio);
        }
        
        if (filtro.getValorAdquisicionDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "sgAfDescargosDetalle.ddeBienesDepreciablesFk.bdeValorAdquisicion", filtro.getValorAdquisicionDesde());
            criterios.add(criterio);
        }
        
        if (filtro.getValorAdquisicionHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "sgAfDescargosDetalle.ddeBienesDepreciablesFk.bdeValorAdquisicion", filtro.getValorAdquisicionHasta());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgAfDescargo> obtenerPorFiltro(FiltroBienesDepreciables filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfDescargo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
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
            return this.countByCriteria(SgAfDescargo.class, criterio, filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
