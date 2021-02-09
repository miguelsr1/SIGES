/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRecibos;
import sv.gob.mined.siges.negocio.validaciones.RecibosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RecibosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgRecibos;

/**
 * Servicio que gestiona los recibos para las trasnferencias
 *
 * @author Sofis Solutions
 */
@Stateless
public class RecibosBean {

    private static final Logger LOGGER = Logger.getLogger(RecibosBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRecibos
     * @throws GeneralException
     */
    public SgRecibos obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRecibos> codDAO = new CodigueraDAO<>(em, SgRecibos.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_RECIBOS);
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
                CodigueraDAO<SgRecibos> codDAO = new CodigueraDAO<>(em, SgRecibos.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_RECIBOS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param rec SgRecibos
     * @return SgRecibos
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRecibos guardar(SgRecibos rec) throws GeneralException {
        try {
            if (rec != null) {
                if (RecibosValidacionNegocio.validar(rec)) {
                    CodigueraDAO<SgRecibos> codDAO = new CodigueraDAO<>(em, SgRecibos.class);
                    CodigueraDAO<SgArchivo> achDAO = new CodigueraDAO<>(em, SgArchivo.class);
                    achDAO.guardar(rec.getRecArchivo(), ConstantesOperaciones.CREAR_RECIBOS);
                    return codDAO.guardar(rec, ConstantesOperaciones.CREAR_RECIBOS);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rec;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRecibos filtro) throws GeneralException {
        try {
            RecibosDAO codDAO = new RecibosDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRecibos>
     * @throws GeneralException
     */
    public List<SgRecibos> obtenerPorFiltro(FiltroRecibos filtro) throws GeneralException {
        try {
            RecibosDAO codDAO = new RecibosDAO(em);
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
                CodigueraDAO<SgRecibos> codDAO = new CodigueraDAO<>(em, SgRecibos.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_RECIBOS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
