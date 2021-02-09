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
import sv.gob.mined.siges.filtros.FiltroDescargosDetalle;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargoDetalle;

public class DescargosDetalleDAO extends HibernateJpaDAOImp<SgAfDescargoDetalle, Long> implements Serializable {

    public DescargosDetalleDAO(EntityManager em) {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroDescargosDetalle filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "ddeDescargoFk.desUnidadAdministrativaFk.uadPk", 1);
                    criterios.add(criterioNotNullAD);

                    if(filtro.getUnidadActivoFijoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desUnidadAdministrativaFk.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desUnidadAdministrativaFk.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "ddeDescargoFk.desSedeFk.sedPk", 1);
                    criterios.add(criterioNotNullCE);

                    if(filtro.getDepartamentoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getMunicipioId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desSedeFk.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desSedeFk.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }
        if(filtro.getTipoDescargo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desTipoDescargoFk.edePk", filtro.getTipoDescargo());
            criterios.add(criterio);
        }
        
        if(filtro.getCategoriaId() != null) {
            List<CriteriaTO> categorias = new ArrayList();
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "ddeBienesDepreciablesFk.bdeTipoBien.tbiCategoriaBienes.cabPk", filtro.getCategoriaId());
            categorias.add(criterio);
            
            MatchCriteriaTO criterioOtraCategoria = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NULL, "ddeBienesDepreciablesFk.bdeCategoriaFk", 1);
            categorias.add(criterioOtraCategoria);
            
            CriteriaTO[] arrCriterioAND = categorias.toArray(new CriteriaTO[categorias.size()]);
            CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];

            categorias = new ArrayList();
            categorias.add(criterioAND);
            
            
            MatchCriteriaTO criterioOtraCategoriaNueva = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "ddeBienesDepreciablesFk.bdeCategoriaFk.cabPk", filtro.getCategoriaId());
            categorias.add(criterioOtraCategoriaNueva);
            
            //Si se requiere búsqueda por categoría original o modificada, entonces se hace un OR
            CriteriaTO[] arrCriterioOR = categorias.toArray(new CriteriaTO[categorias.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
            
        }
        
        if(filtro.getClasificacionId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "ddeBienesDepreciablesFk.bdeTipoBien.tbiCategoriaBienes.cabClasificacionBienes.cbiPk", filtro.getClasificacionId());
            criterios.add(criterio);
        }
        
        if(filtro.getActivos() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desActivo", filtro.getActivos());
            criterios.add(criterio);
        }
        
        if(filtro.getCodigoInventario() != null && StringUtils.isNotBlank(filtro.getCodigoInventario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "ddeBienesDepreciablesFk.bdeCodigoInventario", filtro.getCodigoInventario().trim());
            criterios.add(criterio);
        }
        
        if(filtro.getNumeroActa() != null && StringUtils.isNotBlank(filtro.getNumeroActa())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "desCodigoDescargo", filtro.getNumeroActa().trim());
            criterios.add(criterio);
        }
        
        
        if(filtro.getActivos() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desActivo", filtro.getActivos());
            criterios.add(criterio);
        }
        
        if(filtro.getDescargoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "ddeDescargoFk.desPk", filtro.getDescargoId());
                    criterios.add(criterio);
        }

        if(filtro.getBienId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "ddeBienesDepreciablesFk.bdePk", filtro.getBienId());
                    criterios.add(criterio);
        }
        
        if (filtro.getFechaDescargoDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "ddeDescargoFk.desFechaDescargo", filtro.getFechaDescargoDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaDescargoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "ddeDescargoFk.desFechaDescargo", filtro.getFechaDescargoHasta());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgAfDescargoDetalle> obtenerPorFiltro(FiltroDescargosDetalle filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfDescargoDetalle.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDescargosDetalle filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfDescargoDetalle.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
