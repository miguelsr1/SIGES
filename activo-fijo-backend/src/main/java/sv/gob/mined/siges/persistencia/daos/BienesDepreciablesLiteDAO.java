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
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciableLite;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class BienesDepreciablesLiteDAO extends HibernateJpaDAOImp<SgAfBienDepreciableLite, Long> implements Serializable {
    
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public BienesDepreciablesLiteDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroBienesDepreciables filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    if(filtro.getUnidadActivoFijoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    } else {
                       MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "bdeUnidadesAdministrativas.uadPk", 1);
                        criterios.add(criterioNotNullAD); 
                    }
                    break;
                case CENTRO_ESCOLAR:
                    if(filtro.getDepartamentoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getMunicipioId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "bdeSede.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    } else {
                        MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "bdeSede.sedPk", 1);
                        criterios.add(criterioNotNullCE);

                    }
                    break;
                default:
                    break;
            }
        } else {
            if(filtro.getUnidadActivoFijoId()!= null) {
                List<CriteriaTO> unidadesPorAF = new ArrayList();
                MatchCriteriaTO criterioUnidades = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "bdeUnidadesAdministrativas.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                unidadesPorAF.add(criterioUnidades);
                
                if(filtro.getDepartamentoId() != null) {
                    MatchCriteriaTO criterioSedes = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.EQUALS, "bdeSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                    unidadesPorAF.add(criterioSedes);
                }
                
                CriteriaTO[] arrCriterioOR = unidadesPorAF.toArray(new CriteriaTO[unidadesPorAF.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        
        if(filtro.getCompletado() != null) {
            if(!filtro.getCompletado()) {
                List<CriteriaTO> completados = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeCompletado", filtro.getCompletado());
                completados.add(criterio);

                MatchCriteriaTO criterioOtraCategoria = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NULL, "bdeCompletado", 1);
                completados.add(criterioOtraCategoria);
                
                CriteriaTO[] arrCriterioOR = completados.toArray(new CriteriaTO[completados.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeCompletado", filtro.getCompletado());
                criterios.add(criterio);
            }
        }
        
        if(filtro.getTipoBienId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bdeTipoBien.tbiPk", filtro.getTipoBienId());
            criterios.add(criterio);
        }

        if(filtro.getEstadoId() != null) {
            List<CriteriaTO> estadosCriteria = new ArrayList();
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadosBienes.ebiPk", filtro.getEstadoId());
            estadosCriteria.add(criterio);
            
            
            if(filtro.getValidarEstadoSolicitudVacio() != null && filtro.getValidarEstadoSolicitudVacio()) {
                if(filtro.getTrasladado() != null && filtro.getTrasladado() && StringUtils.isNotBlank(filtro.getEstadoCodigo())) {
                    List<CriteriaTO> estadosCriteriaOr = new ArrayList();
                    List<CriteriaTO> estadosCriteriAnd = new ArrayList();
                    estadosCriteriAnd.add(criterio);
                    
                    MatchCriteriaTO criterioSolicitudVacio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeEstadosSolicitud", 1);
                    estadosCriteriaOr.add(criterioSolicitudVacio);

                    MatchCriteriaTO criterioCodigoEstadoSolicitud = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadosSolicitud.ebiCodigo", filtro.getEstadoCodigo().trim());
                    estadosCriteriaOr.add(criterioCodigoEstadoSolicitud);
                    
                    CriteriaTO[] arrCriterioOR = estadosCriteriaOr.toArray(new CriteriaTO[estadosCriteriaOr.size()]);
                    CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                    estadosCriteriAnd.add(criterioOR);
                    
                    CriteriaTO[] arrCriterioAND = estadosCriteriAnd.toArray(new CriteriaTO[estadosCriteriAnd.size()]);
                    CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];
                    criterios.add(criterioAND);
                } else {
                    MatchCriteriaTO criterioSolicitudVacio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeEstadosSolicitud", 1);
                    estadosCriteria.add(criterioSolicitudVacio);
                    //Si se requiere busqueda por estado solicitud vacio, entonces se hace un AND
                    CriteriaTO[] arrCriterioAND = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
                    CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];
                    criterios.add(criterioAND);
                }  
            } else if(filtro.getValidarEstadoSolicitud() != null && filtro.getValidarEstadoSolicitud()) {
                MatchCriteriaTO criterioSolicitud = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bdeEstadosSolicitud.ebiPk", filtro.getEstadoId());
                estadosCriteria.add(criterioSolicitud);
                //Si se requiere búsqueda por estado de solicitud, entonces se hace un OR
                CriteriaTO[] arrCriterioOR = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            } else {
                criterios.add(criterio);  
            }
        } else {
            if(filtro.getValidarEstadoSolicitudVacio() != null && filtro.getValidarEstadoSolicitudVacio()) {
                List<CriteriaTO> estadosCriteriaOr = new ArrayList();
                
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "bdeEstadosSolicitud", 1);
                estadosCriteriaOr.add(criterio);
                
                if(filtro.getEstadoCodigo() != null) {
                    MatchCriteriaTO criterioCodigoEstadoSolicitud = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "bdeEstadosSolicitud.ebiCodigo", filtro.getEstadoCodigo().trim());
                    estadosCriteriaOr.add(criterioCodigoEstadoSolicitud);
                }
                CriteriaTO[] arrCriterioOR = estadosCriteriaOr.toArray(new CriteriaTO[estadosCriteriaOr.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (!StringUtils.isBlank(filtro.getCodigoInventario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "bdeCodigoInventario", SofisStringUtils.normalizarParaBusqueda(filtro.getCodigoInventario().trim()));
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgAfBienDepreciableLite> obtenerPorFiltro(FiltroBienesDepreciables filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfBienDepreciableLite.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
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
            return this.countByCriteria(SgAfBienDepreciableLite.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
