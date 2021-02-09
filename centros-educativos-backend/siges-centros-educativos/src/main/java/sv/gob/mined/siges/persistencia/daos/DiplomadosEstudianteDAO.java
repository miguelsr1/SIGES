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
import sv.gob.mined.siges.filtros.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomadosEstudiante;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class DiplomadosEstudianteDAO extends HibernateJpaDAOImp<SgDiplomadosEstudiante, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;
    private EntityManager em;

    public DiplomadosEstudianteDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroDiplomadosEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getDiplomadoEstudianteId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depPk", filtro.getDiplomadoEstudianteId());
            criterios.add(criterio);
        }

        if (filtro.getAnioLectivoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depAnioLectivo.alePk", filtro.getAnioLectivoId());
            criterios.add(criterio);
        }

        if (filtro.getDiplomadoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depDiplomado.dipPk", filtro.getDiplomadoId());
            criterios.add(criterio);
        }

        if (filtro.getEstudianteId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depEstudiante.estPk", filtro.getEstudianteId());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "depEstudiante.estPersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }

        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depEstudiante.estPersona.perNie", filtro.getNie());
            criterios.add(criterio);
        }
        if (filtro.getDepartamentoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoPk());
            criterios.add(criterio);
        }
        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depSedeFk.sedPk", filtro.getSedePk());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgDiplomadosEstudiante> obtenerPorFiltro(FiltroDiplomadosEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            Boolean distinct = filtro.getSeNecesitaDistinct() != null ? filtro.getSeNecesitaDistinct() : Boolean.FALSE;
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct() != null ? filtro.getSeNecesitaDistinct() : Boolean.FALSE;
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit())
                                || SeguridadAmbitos.SISTEMA_INTEGRADO.name().equals(op.getAmbit())
                                || SeguridadAmbitos.MODALIDADES_FLEXIBLES.name().equals(op.getAmbit())
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
            return this.findEntityByCriteria(SgDiplomadosEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDiplomadosEstudiante filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDiplomadosEstudiante.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
