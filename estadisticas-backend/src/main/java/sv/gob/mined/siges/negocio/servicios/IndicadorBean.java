/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroIndicadores;
import sv.gob.mined.siges.negocio.validaciones.IndicadorValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.IndicadoresDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstIndicador;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class IndicadorBean {

    @PersistenceContext(name = Constantes.MAIN_DATASOURCE)
    private EntityManager em;


    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgIndicador
     * @throws GeneralException
     */
    public SgEstIndicador obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstIndicador> codDAO = new CodigueraDAO<>(em, SgEstIndicador.class);
                return codDAO.obtenerPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstIndicador> codDAO = new CodigueraDAO<>(em, SgEstIndicador.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param ind SgIndicador
     * @return SgIndicador
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstIndicador guardar(SgEstIndicador ind) throws GeneralException {
        try {
            if (ind != null) {
                if (IndicadorValidacionNegocio.validar(ind)) {
                    CodigueraDAO<SgEstIndicador> codDAO = new CodigueraDAO<>(em, SgEstIndicador.class);
                    return codDAO.guardar(ind);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ind;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroIndicadores filtro) throws GeneralException {
        try {
            IndicadoresDAO codDAO = new IndicadoresDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgIndicador>
     * @throws GeneralException
     */
    public List<SgEstIndicador> obtenerPorFiltro(FiltroIndicadores filtro) throws GeneralException {
        try {
            IndicadoresDAO codDAO = new IndicadoresDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstIndicador> codDAO = new CodigueraDAO<>(em, SgEstIndicador.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
