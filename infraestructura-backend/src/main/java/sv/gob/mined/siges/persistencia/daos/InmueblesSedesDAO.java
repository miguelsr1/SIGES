/*
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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroInmueblesSedes;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class InmueblesSedesDAO extends HibernateJpaDAOImp<SgInmueblesSedes, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public InmueblesSedesDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroInmueblesSedes filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPropietario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisPropietario", filtro.getPropietario());
            criterios.add(criterio);
        }
        if (filtro.getActivoFijo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisActivoFijo", filtro.getActivoFijo());
            criterios.add(criterio);
        }
        if (filtro.getDepartamentoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
            criterios.add(criterio);
        }
        if (filtro.getMunicipioId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
            criterios.add(criterio);
        }
        if (filtro.getSedeId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisSedeFk.sedPk", filtro.getSedeId());
            criterios.add(criterio);
        }
        if (filtro.getUnidadAdministrativa() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisAfUnidadAdministrativa.uadPk", filtro.getUnidadAdministrativa());
            criterios.add(criterio);
        }
        if (filtro.getInmueblePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisPk", filtro.getInmueblePk());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "tisCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }
        if (filtro.getEstadoInmueble() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisEstadoInmueble.einPk", filtro.getEstadoInmueble());
            criterios.add(criterio);
        }
        if (filtro.getPerteneceA() != null) {
            MatchCriteriaTO criterio;
            switch (filtro.getPerteneceA()) {
                case SEDE:
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisPerteneceSede", Boolean.TRUE);
                    criterios.add(criterio);
                    break;
                case UNIDAD_ADM:
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisPerteneceSede", Boolean.FALSE);
                    criterios.add(criterio);
                    break;
                case OTROS:
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "tisPerteneceSede", 1);
                    criterios.add(criterio);
                    break;

            }

        }
        if (filtro.getSedeDeUnidadRespYOtrasSedes()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisSedeFk.sedPk", filtro.getSedeDeUnidadRespYOtrasSedes());
            
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisUnidadesResponsables.riuSedeFk.sedPk", filtro.getSedeDeUnidadRespYOtrasSedes());
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
            
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
        
        if (filtro.getUnidadAdministrativaDeUnidadRespYOtrasUnidades()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisAfUnidadAdministrativa.uadPk", filtro.getUnidadAdministrativaDeUnidadRespYOtrasUnidades());
            
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisUnidadesResponsables.riuAfUnidadAdministrativa.uadPk", filtro.getUnidadAdministrativaDeUnidadRespYOtrasUnidades());
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
            
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
        return criterios;
    }

    public List<SgInmueblesSedes> obtenerPorFiltro(FiltroInmueblesSedes filtro, String securityOperation) throws DAOGeneralException {
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
                        if (SeguridadAmbitos.DEPARTAMENTAL.name().equals(op.getAmbit()) || 
                                SeguridadAmbitos.SEDE.name().equals(op.getAmbit()) || 
                                SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) ||                                
                                SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())) {
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
            return this.findEntityByCriteria(SgInmueblesSedes.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroInmueblesSedes filtro, String securityOperation) throws DAOGeneralException {
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
            return this.countByCriteria(SgInmueblesSedes.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
