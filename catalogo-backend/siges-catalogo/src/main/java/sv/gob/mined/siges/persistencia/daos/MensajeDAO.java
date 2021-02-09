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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroMensaje;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.entidades.SgMensaje;
import sv.gob.mined.siges.utils.ReflectionUtils;

public class MensajeDAO extends HibernateJpaDAOImp<SgMensaje, Long> implements Serializable {

    private EntityManager em;

    public MensajeDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    public List<SgMensaje> obtenerPorFiltro(FiltroMensaje filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMensaje.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroMensaje filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));          
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMensaje.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    private List<CriteriaTO> generarCriteria(FiltroMensaje filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgMensaje.class, AtributoCodigo.class);
        String campoDescripcion = ReflectionUtils.obtenerNombreCampoAnotado(SgMensaje.class, AtributoDescripcion.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgMensaje.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getValor())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "msjValor", filtro.getValor().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDescripcion())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoDescripcion, filtro.getDescripcion().trim());
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }
    
    public Object[][] obtenerParaResourceBundle() throws DAOGeneralException {
        try {
            String query = "Select e.msj_codigo, e.msj_valor from " + Constantes.SCHEMA_CATALOGO + ".sg_mensajes e where e.msj_habilitado = true";
            List<Object[]> list = em.createNativeQuery(query).getResultList();
            Object[][] ret = list.toArray(new Object[][] {});
            return ret;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
