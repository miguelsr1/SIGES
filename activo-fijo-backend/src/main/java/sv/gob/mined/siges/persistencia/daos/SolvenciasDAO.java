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
import sv.gob.mined.siges.filtros.FiltroSolvencias;
import sv.gob.mined.siges.persistencia.entidades.SgAfSolvencias;

public class SolvenciasDAO extends HibernateJpaDAOImp<SgAfSolvencias, Long> implements Serializable {

    public SolvenciasDAO(EntityManager em) {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroSolvencias filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "solUnidadAdministrativaFk.uadPk", 1);
                    criterios.add(criterioNotNullAD);

                    if(filtro.getUnidadActivoFijoId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "solUnidadAdministrativaFk.uadUnidadActivoFijoFk.uafPk", filtro.getUnidadActivoFijoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "solUnidadAdministrativaFk.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                   /** if(filtro.getActualizado()!= null) {
                        MatchCriteriaTO criterioMayorIgual = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.GREATEREQUAL, "solUnidadAdministrativaFk.uadFechaInventario", 
                                LocalDate.of(filtro.getAnio().intValue(), Constantes.MES_ENERO.intValue(), Constantes.MES_PRIMER_DIA.intValue()));
                        criterios.add(criterioMayorIgual);
                        
                        MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.LESSEQUAL, "solUnidadAdministrativaFk.uadFechaInventario", 
                                LocalDate.of(filtro.getAnio().intValue(), Constantes.MES_DICIEMBRE.intValue(), Constantes.MES_DICIEMBRE_ULTIMO_DIA.intValue()));
                        criterios.add(criterio1);
                    }**/
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "solSedeFk.sedPk", 1);
                    criterios.add(criterioNotNullCE);

                    if(filtro.getDepartamentoId() != null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "solSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
                        criterios.add(criterio);
                    }
                    if(filtro.getMunicipioId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "solSedeFk.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
                        criterios.add(criterio);
                    }
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "solSedeFk.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }
        
        if(filtro.getAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "solAnio", filtro.getAnio());
                        criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgAfSolvencias> obtenerPorFiltro(FiltroSolvencias filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfSolvencias.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroSolvencias filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfSolvencias.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
