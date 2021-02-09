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
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroIncorporacion;
import sv.gob.mined.siges.persistencia.entidades.SgIncorporacion;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class IncorporacionDAO extends HibernateJpaDAOImp<SgIncorporacion, Integer> implements Serializable {

    private EntityManager em;

    public IncorporacionDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroIncorporacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getIncPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "incPk", filtro.getIncPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "incNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getNombreCompleto()));
            criterios.add(criterio);
        }
        if (filtro.getFechaNacimiento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "incFechaNacimiento", filtro.getFechaNacimiento());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH_ILIKE, "incPrimerNombre", SofisStringUtils.normalizarParaBusqueda(filtro.getPrimerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH_ILIKE, "incSegundoNombre", SofisStringUtils.normalizarParaBusqueda(filtro.getSegundoNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH_ILIKE, "incTercerNombre", SofisStringUtils.normalizarParaBusqueda(filtro.getTercerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH_ILIKE, "incPrimerApellido", SofisStringUtils.normalizarParaBusqueda(filtro.getPrimerApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH_ILIKE, "incSegundoApellido", SofisStringUtils.normalizarParaBusqueda(filtro.getSegundoApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH_ILIKE, "incTercerApellido", SofisStringUtils.normalizarParaBusqueda(filtro.getTercerApellido()));
            criterios.add(criterio);
        }


        //IDENTIFICACIONES
        List<CriteriaTO> indentificacionesOR = new ArrayList();

        if (!StringUtils.isBlank(filtro.getDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "incDui", filtro.getDui());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCarneResidente())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "incCarneResidente", filtro.getCarneResidente());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPasaporte())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "incPasaporte", filtro.getPasaporte());
            indentificacionesOR.add(criterio);
        }

        if (!indentificacionesOR.isEmpty()) {
            CriteriaTO[] arrCriterioOR = indentificacionesOR.toArray(new CriteriaTO[indentificacionesOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getFechaNacimientoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "incFechaNacimiento", filtro.getFechaNacimientoDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaNacimientoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "incFechaNacimiento", filtro.getFechaNacimientoHasta());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgIncorporacion> obtenerPorFiltro(FiltroIncorporacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgIncorporacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroIncorporacion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgIncorporacion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
