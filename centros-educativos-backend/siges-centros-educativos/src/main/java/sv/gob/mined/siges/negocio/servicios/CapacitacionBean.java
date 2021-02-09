/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.CapacitacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCapacitacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class CapacitacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCapacitacion
     * @throws GeneralException
     */
    public SgCapacitacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
                return codDAO.obtenerPorId(id, null);
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgCapacitacion
     * @return SgCapacitacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCapacitacion guardar(SgCapacitacion entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (CapacitacionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);

                    if (entity.getCapPk() != null) {
                        SgCapacitacion comp = codDAO.obtenerPorId(entity.getCapPk(), null);
                        if (!CapacitacionValidacionNegocio.compararParaGrabar(entity, comp)) {
                            entity.setCapValidado(Boolean.FALSE);
                            entity.setCapUltValidacionFecha(null);
                            entity.setCapUltValidacionUsuario(null);
                        }
                    }

                    return (SgCapacitacion) codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Valida la capacitacion indicada
     *
     * @param Long capPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCapacitacion validarRealizado(Long capPk) throws GeneralException {
        try {
            if (capPk != null) {
                CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
                SgCapacitacion cap = codDAO.obtenerPorId(capPk, null);
                cap.setCapValidado(Boolean.TRUE);
                cap.setCapUltValidacionFecha(LocalDateTime.now());
                cap.setCapUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
                return codDAO.guardar(cap, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Valida la capacitacion indicada
     *
     * @param Long capPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCapacitacion invalidarRealizado(Long capPk) throws GeneralException {
        try {
            if (capPk != null) {
                CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
                SgCapacitacion cap = codDAO.obtenerPorId(capPk, null);
                cap.setCapValidado(Boolean.FALSE);
                cap.setCapUltValidacionFecha(LocalDateTime.now());
                cap.setCapUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
                return codDAO.guardar(cap, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgCapacitacion
     * @throws GeneralException
     */
    public List<SgCapacitacion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
            return codDAO.obtenerPorFiltro(filtro, null);
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
                CodigueraDAO<SgCapacitacion> codDAO = new CodigueraDAO<>(em, SgCapacitacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
