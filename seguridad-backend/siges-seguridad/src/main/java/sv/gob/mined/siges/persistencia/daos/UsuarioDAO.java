/**
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
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroUsuario;
import sv.gob.mined.siges.persistencia.entidades.SgUsuario;

public class UsuarioDAO extends HibernateJpaDAOImp<SgUsuario, Integer> implements Serializable {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());

    public UsuarioDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroUsuario filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "usuCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "usuCodigo", filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "usuNombre", filtro.getNombre());
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "usuHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getCategoria() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "pusPersonaUsuarioRol.purRol.rolRolOperacion.ropOperacion.opeCategoriaOperacion.copPk", filtro.getCategoria());
            criterios.add(criterio);
        }

        if (filtro.getOperacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "pusPersonaUsuarioRol.purRol.rolRolOperacion.ropOperacion.opePk", filtro.getOperacion());
            criterios.add(criterio);
        }

        if (filtro.getRol() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "pusPersonaUsuarioRol.purRol.rolPk", filtro.getRol());
            criterios.add(criterio);
        }
        
        if (filtro.getAmbito() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "pusPersonaUsuarioRol.purContexto.conAmbito", filtro.getAmbito());
            criterios.add(criterio);
        }
        
        if (filtro.getContexto() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "pusPersonaUsuarioRol.purContexto.contextoId", filtro.getContexto());
            criterios.add(criterio);
        }
        

        
        return criterios;
    }

    public List<SgUsuario> obtenerPorFiltro(FiltroUsuario filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgUsuario.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroUsuario filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgUsuario.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}