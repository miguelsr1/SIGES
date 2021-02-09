/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.TipoModuloValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoModulo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoModuloBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoModulo
     * @throws GeneralException
     */
    public SgTipoModulo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoModulo> codDAO = new CodigueraDAO<>(em, SgTipoModulo.class);
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
                CodigueraDAO<SgTipoModulo> codDAO = new CodigueraDAO<>(em, SgTipoModulo.class);
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
     * @param tmo SgTipoModulo
     * @return SgTipoModulo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoModulo guardar(SgTipoModulo tmo) throws GeneralException {
        try {
            if (tmo != null) {
                if (TipoModuloValidacionNegocio.validar(tmo)) {
                    CodigueraDAO<SgTipoModulo> codDAO = new CodigueraDAO<>(em, SgTipoModulo.class);
                    boolean guardar = true;
                    if (tmo.getTmoPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tmo.getClass(), tmo.getTmoPk() , tmo.getTmoVersion());
                        SgTipoModulo valorAnterior = (SgTipoModulo) valorAnt;
                        guardar = !TipoModuloValidacionNegocio.compararParaGrabar(valorAnterior, tmo);
                    }
                    if (guardar) {
                        return codDAO.guardar(tmo);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tmo;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoModulo> codDAO = new CodigueraDAO<>(em, SgTipoModulo.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTipoModulo>
     * @throws GeneralException
     */
    public List<SgTipoModulo> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoModulo> codDAO = new CodigueraDAO<>(em, SgTipoModulo.class);
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
                CodigueraDAO<SgTipoModulo> codDAO = new CodigueraDAO<>(em, SgTipoModulo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
