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
import sv.gob.mined.siges.filtros.FiltroUsuarioRol;
import sv.gob.mined.siges.persistencia.entidades.SgUsuarioRol;

public class UsuarioRolDAO extends HibernateJpaDAOImp<SgUsuarioRol, Integer> implements Serializable {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(UsuarioRolDAO.class.getName());

    public UsuarioRolDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroUsuarioRol filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if (!StringUtils.isBlank(filtro.getCodigo())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purUsuario.usuPk", filtro.getUsuario());
            criterios.add(criterio);
        }

        if (filtro.getUsuario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purUsuario.usuPk", filtro.getUsuario());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getUsuarioCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purUsuario.usuCodigo", filtro.getUsuarioCodigo());
            criterios.add(criterio);
        }
        
        if (filtro.getRol() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purRol.rolPk", filtro.getRol());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getRolCodigo())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purRol.rolCodigo", filtro.getRolCodigo());
            criterios.add(criterio);
        }
        if (filtro.getAmbito() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purContexto.conAmbito", filtro.getAmbito());
            criterios.add(criterio);
        }
        if (filtro.getContexto() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purContexto.contextoId", filtro.getContexto());
            criterios.add(criterio);
        }
        if(filtro.getOperacionCodigo()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purRol.rolRolOperacion.ropOperacion.opeCodigo", filtro.getOperacionCodigo());
            criterios.add(criterio);
        }
        if (filtro.getUsuarios() != null && !filtro.getUsuarios().isEmpty()){
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long usuPk : filtro.getUsuarios()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "purUsuario.usuPk", usuPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        return criterios;
    }

    public List<SgUsuarioRol> obtenerPorFiltro(FiltroUsuarioRol filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgUsuarioRol.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroUsuarioRol filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgUsuarioRol.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
