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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class CalificacionesHistoricasEstudianteDAO extends HibernateJpaDAOImp<SgCalificacionesHistoricasEstudiante, Integer> implements Serializable {

    private JsonWebToken jwt;
    
    private SeguridadBean segDAO;

    public CalificacionesHistoricasEstudianteDAO(EntityManager em,SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroCalificacionesHistoricasEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
                criterios.add(criterio);
            }

            if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
                criterios.add(criterio);
            }

            if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
                criterios.add(criterio);
            }

            if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
                criterios.add(criterio);
            }

            if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
                criterios.add(criterio);
            }

            if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
                criterios.add(criterio);
            }

            if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "chePersonaFk.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
                criterios.add(criterio);
            }

            if (filtro.getEstNie() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteNie", filtro.getEstNie());
                criterios.add(criterio);
            }
            if (filtro.getEstPk()!= null) {
                SgEstudiante est=new SgEstudiante();
                est.setEstPk(filtro.getEstPk());
                est.setEstVersion(0);
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk",est);
                criterios.add(criterio);
            }
            
            if (filtro.getSedeSIGES()!= null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheSedeFk.sedPk", filtro.getSedeSIGES());
                criterios.add(criterio);
            }
            
            if (filtro.getSedeExternaCodigo()!= null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheSedeExternaCodigo", filtro.getSedeExternaCodigo());
                criterios.add(criterio);
            }
            if (filtro.getSedeExternaNombre()!= null) {
                String nombre=SofisStringUtils.normalizarParaBusqueda( filtro.getSedeExternaNombre());
                nombre = nombre.replaceAll("[^a-zA-Z0-9 ]", "");
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cheSedeExternaNombreBusqueda",nombre);
                criterios.add(criterio);
                 criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "cheSedeFk",1);
                criterios.add(criterio);
            }
            
            if (filtro.getComponente()!= null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cheComponenteBusqueda",SofisStringUtils.normalizarParaBusqueda(filtro.getComponente()));
                criterios.add(criterio);
            }
            if(filtro.getChePk()!=null){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "chePk",filtro.getChePk());
                criterios.add(criterio);
            }
            if (filtro.getAnio()!= null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheAnioMatricula", filtro.getAnio());
                criterios.add(criterio);
            }
        return criterios;
    }

    public List<SgCalificacionesHistoricasEstudiante> obtenerPorFiltro(FiltroCalificacionesHistoricasEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            
            Boolean distinct = Boolean.FALSE;
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = Boolean.FALSE;
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
            return this.findEntityByCriteria(SgCalificacionesHistoricasEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    
    public Long obtenerTotalPorFiltro(FiltroCalificacionesHistoricasEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            Boolean distinct  = Boolean.FALSE;
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct= Boolean.FALSE;
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
            return this.countByCriteria(SgCalificacionesHistoricasEstudiante.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }


}
