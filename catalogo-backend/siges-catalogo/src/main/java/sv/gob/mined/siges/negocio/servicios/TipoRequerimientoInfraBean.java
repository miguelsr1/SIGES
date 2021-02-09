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
import sv.gob.mined.siges.negocio.validaciones.TipoRequerimientoInfraValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoRequerimientoInfra;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoRequerimientoInfraBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoRequerimientoInfra
     * @throws GeneralException
     */
    public SgTipoRequerimientoInfra obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoRequerimientoInfra> codDAO = new CodigueraDAO<>(em, SgTipoRequerimientoInfra.class);
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
                CodigueraDAO<SgTipoRequerimientoInfra> codDAO = new CodigueraDAO<>(em, SgTipoRequerimientoInfra.class);
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
     * @param tri SgTipoRequerimientoInfra
     * @return SgTipoRequerimientoInfra
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoRequerimientoInfra guardar(SgTipoRequerimientoInfra tri) throws GeneralException {
        try {
            if (tri != null) {
                if (TipoRequerimientoInfraValidacionNegocio.validar(tri)) {
                    CodigueraDAO<SgTipoRequerimientoInfra> codDAO = new CodigueraDAO<>(em, SgTipoRequerimientoInfra.class);
                    boolean guardar = true;
                    if (tri.getTriPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tri.getClass(), tri.getTriPk() , tri.getTriVersion());
                        SgTipoRequerimientoInfra valorAnterior = (SgTipoRequerimientoInfra) valorAnt;
                        guardar = !TipoRequerimientoInfraValidacionNegocio.compararParaGrabar(valorAnterior, tri);
                    }
                    if (guardar) {
                        return codDAO.guardar(tri);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tri;
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
            CodigueraDAO<SgTipoRequerimientoInfra> codDAO = new CodigueraDAO<>(em, SgTipoRequerimientoInfra.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTipoRequerimientoInfra>
     * @throws GeneralException
     */
    public List<SgTipoRequerimientoInfra> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoRequerimientoInfra> codDAO = new CodigueraDAO<>(em, SgTipoRequerimientoInfra.class);
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
                CodigueraDAO<SgTipoRequerimientoInfra> codDAO = new CodigueraDAO<>(em, SgTipoRequerimientoInfra.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
