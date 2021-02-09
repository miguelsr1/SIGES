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
import sv.gob.mined.siges.negocio.validaciones.NombreExtraccionesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstNombreExtraccion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class NombreExtraccionesBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNombreExtracciones
     * @throws GeneralException
     */
    public SgEstNombreExtraccion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstNombreExtraccion> codDAO = new CodigueraDAO<>(em, SgEstNombreExtraccion.class);
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
                CodigueraDAO<SgEstNombreExtraccion> codDAO = new CodigueraDAO<>(em, SgEstNombreExtraccion.class);
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
     * @param nex SgNombreExtracciones
     * @return SgNombreExtracciones
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstNombreExtraccion guardar(SgEstNombreExtraccion nex) throws GeneralException {
        try {
            if (nex != null) {
                if (NombreExtraccionesValidacionNegocio.validar(nex)) {
                    CodigueraDAO<SgEstNombreExtraccion> codDAO = new CodigueraDAO<>(em, SgEstNombreExtraccion.class);
                    boolean guardar = true;
                    if (nex.getNexPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(nex.getClass(), nex.getNexPk() , nex.getNexVersion());
                        SgEstNombreExtraccion valorAnterior = (SgEstNombreExtraccion) valorAnt;
                        guardar = !NombreExtraccionesValidacionNegocio.compararParaGrabar(valorAnterior, nex);
                    }
                    if (guardar) {
                        return codDAO.guardar(nex);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return nex;
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
            CodigueraDAO<SgEstNombreExtraccion> codDAO = new CodigueraDAO<>(em, SgEstNombreExtraccion.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgNombreExtracciones>
     * @throws GeneralException
     */
    public List<SgEstNombreExtraccion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEstNombreExtraccion> codDAO = new CodigueraDAO<>(em, SgEstNombreExtraccion.class);
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
                CodigueraDAO<SgEstNombreExtraccion> codDAO = new CodigueraDAO<>(em, SgEstNombreExtraccion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
