/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroExtraccion;
import sv.gob.mined.siges.persistencia.entidades.SgExtraccion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author usuario
 */
public class ExtraccionDAO extends HibernateJpaDAOImp<SgExtraccion, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public ExtraccionDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
        jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroExtraccion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "extAnio", filtro.getAnio());
            criterios.add(criterio);
        }
        if (filtro.getDatasetPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "extDataset.datPk", filtro.getDatasetPk());
            criterios.add(criterio);
        }
        if (filtro.getNombrePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "extNombre.nexPk", filtro.getNombrePk());
            criterios.add(criterio);
        }
        if (filtro.getEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "extEstado", filtro.getEstado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgExtraccion> obtenerPorFiltro(FiltroExtraccion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgExtraccion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroExtraccion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgExtraccion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public void procesarExtraccionesPendientes() throws DAOGeneralException {
        try {
            Query q = em.createNativeQuery("DO $$ "
                    + " BEGIN "
                    + " PERFORM estadisticas.procesar_extracciones_pendientes(); "
                    + " END; "
                    + " $$ LANGUAGE plpgsql;");
            q.executeUpdate();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
