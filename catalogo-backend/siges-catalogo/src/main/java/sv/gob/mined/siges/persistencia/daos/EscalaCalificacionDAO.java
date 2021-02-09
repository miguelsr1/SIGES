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
import sv.gob.mined.siges.filtros.FiltroEscalaCalificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.SgEscalaCalificacion;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class EscalaCalificacionDAO extends HibernateJpaDAOImp<SgEscalaCalificacion, Long> implements Serializable {

    private EntityManager em;

    public EscalaCalificacionDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroEscalaCalificacion filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgEscalaCalificacion.class, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(SgEscalaCalificacion.class, AtributoNombre.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgEscalaCalificacion.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();
        //Código
        if (!StringUtils.isBlank(filtro.getEcaCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getEcaCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getEcaCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getEcaCodigoExacto().trim());
            criterios.add(criterio);
        }

        //Nombre
        if (!StringUtils.isBlank(filtro.getEcaNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getEcaNombre().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getEcaNombreExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getEcaNombreExacto().trim()));
            criterios.add(criterio);
        }
        //Tipo Escala
        if (filtro.getEcaTipoEscala() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "ecaTipoEscala", filtro.getEcaTipoEscala());
            criterios.add(criterio);
        }
        //Valor minimo
        if (!StringUtils.isBlank(filtro.getEcaValorMinimo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "ecaValorMinimo.calValor", filtro.getEcaValorMinimo().trim());
            criterios.add(criterio);
        }
        //Habilitado
        if (filtro.getEcaHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getEcaHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgEscalaCalificacion> obtenerPorFiltro(FiltroEscalaCalificacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgEscalaCalificacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEscalaCalificacion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEscalaCalificacion.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
