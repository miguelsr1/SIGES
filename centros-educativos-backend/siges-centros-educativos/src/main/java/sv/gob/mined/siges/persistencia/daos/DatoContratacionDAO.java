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
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;

public class DatoContratacionDAO extends HibernateJpaDAOImp<SgDatoContratacion, Integer> implements Serializable {

    private EntityManager em;

    public DatoContratacionDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroDatoContratacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getDcoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoPk", filtro.getDcoPk());
            criterios.add(criterio);
        }
        if (filtro.getExcluirDcoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "dcoPk", filtro.getExcluirDcoPk());
            criterios.add(criterio);
        }

        if (filtro.getDcoPersonalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoDatoEmpleado.demPersonalSede.psePk", filtro.getDcoPersonalPk());
            criterios.add(criterio);
        }

        if (filtro.getDcoDatoEmpleado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoDatoEmpleado.demPk", filtro.getDcoDatoEmpleado());
            criterios.add(criterio);
        }

        if (filtro.getDcoCargo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoCargo.carPk", filtro.getDcoCargo());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDcoCargoCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoCargo.carCodigo", filtro.getDcoCargoCodigo());
            criterios.add(criterio);
        }
        
        if (filtro.getDcoCargoCodigos() != null && !filtro.getDcoCargoCodigos().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "dcoCargo.carCodigo", filtro.getDcoCargoCodigos());
            criterios.add(criterio);
        }

        if (filtro.getDcoCentroEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoCentroEducativo.sedPk", filtro.getDcoCentroEducativo());
            if (BooleanUtils.isTrue(filtro.getBuscarEnSedeAdscrita())) {

                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoCentroEducativo.sedSedeAdscritaDe.sedPk", filtro.getDcoCentroEducativo());
                List<CriteriaTO> sedesCriteria = new ArrayList();
                sedesCriteria.add(criterio);
                sedesCriteria.add(criterio2);
                CriteriaTO[] arrCriterioOR = sedesCriteria.toArray(new CriteriaTO[sedesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);

            } else {
                criterios.add(criterio);
            }
        }

        if (!StringUtils.isBlank(filtro.getDcoCentroEducativoCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoCentroEducativo.sedCodigo", filtro.getDcoCentroEducativoCodigo());
            if (BooleanUtils.isTrue(filtro.getBuscarEnSedeAdscrita())) {

                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoCentroEducativo.sedSedeAdscritaDe.sedCodigo", filtro.getDcoCentroEducativoCodigo());
                List<CriteriaTO> sedesCriteria = new ArrayList();
                sedesCriteria.add(criterio);
                sedesCriteria.add(criterio2);
                CriteriaTO[] arrCriterioOR = sedesCriteria.toArray(new CriteriaTO[sedesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);

            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getDcoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "dcoDesde", filtro.getDcoHasta());
            criterios.add(criterio);
        }

        if (filtro.getDcoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "dcoHasta", filtro.getDcoDesde());
            criterios.add(criterio);
        }
        
        if (filtro.getDcoAnio() != null){  
            

//        caso 1)  Tengo desde y hasta seteados. Tengo q comparar la intersección entre 1/enero/año,31/dic/año contra desde, hasta del dato de contratación
//        OR
//        caso 2) Tengo desde seteado, y hasta null. Tengo q comparar que desde sea menor a 31/dic/año y hasta null
//        OR
//        caso 3) Tengo desde null, y hasta seteado. Tengo q comparar que desde sea null, y hasta sea mayor a 1/enero/año -> ESTE CASO NO SE DA NUNCA. LO IGNORO
//        OR
//        caso 4) desde y hasta null

            
            CriteriaTO criterioOR1 = CriteriaTOUtils.createORTO(
                         
                    CriteriaTOUtils.createANDTO(
                            CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "dcoHasta", LocalDate.of(filtro.getDcoAnio(), Month.JANUARY, 1)),
                            CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "dcoDesde", LocalDate.of(filtro.getDcoAnio(), Month.DECEMBER, 31)))
                    ,
                    CriteriaTOUtils.createANDTO(
                            CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoHasta", 1),
                            CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "dcoDesde", LocalDate.of(filtro.getDcoAnio(), Month.DECEMBER, 31)))   
                    ,
                    CriteriaTOUtils.createANDTO(
                            CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoHasta", 1),
                            CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoDesde", 1)));
            
            criterios.add(criterioOR1);

        }

        if (filtro.getDcoPersonalesPk() != null && !filtro.getDcoPersonalesPk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long perPk : filtro.getDcoPersonalesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoDatoEmpleado.demPersonalSede.psePk", perPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getDcoPersonaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dcoDatoEmpleado.demPersonalSede.psePersona.perPk", filtro.getDcoPersonaPk());
            criterios.add(criterio);
        }

        if (filtro.getDcoFecha() != null) {

            CriteriaTO criterioOR1 = CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "dcoDesde", filtro.getDcoFecha()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoDesde", 1)
            );
            criterios.add(criterioOR1);

            List<CriteriaTO> fechaHastaCriteria = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "dcoHasta", filtro.getDcoFecha());
            fechaHastaCriteria.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoHasta", 1);
            fechaHastaCriteria.add(criterio);
            CriteriaTO[] arrCriterioOR = fechaHastaCriteria.toArray(new CriteriaTO[fechaHastaCriteria.size()]);
            CriteriaTO criterioOR = CriteriaTOUtils.createORTO(arrCriterioOR);

            criterios.add(criterioOR);
        }

        if (BooleanUtils.isTrue(filtro.getContratosActivos())) {
            List<CriteriaTO> activos = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "dcoHasta", LocalDate.now());
            activos.add(criterio);

            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoHasta", 1);
            activos.add(criterio);

            if (!activos.isEmpty()) {
                CriteriaTO[] arrCriterioOR = activos.toArray(new CriteriaTO[activos.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }

            activos = new ArrayList();
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "dcoDesde", LocalDate.now());
            activos.add(criterio);

            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "dcoDesde", 1);
            activos.add(criterio);

            if (!activos.isEmpty()) {
                CriteriaTO[] arrCriterioOR = activos.toArray(new CriteriaTO[activos.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }

        }

        return criterios;
    }

    public List<SgDatoContratacion> obtenerPorFiltro(FiltroDatoContratacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgDatoContratacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDatoContratacion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDatoContratacion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
