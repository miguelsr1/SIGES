/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.negocio.validaciones.DetallePlanEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DetallePlanEscolarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class DetallePlanEscolarBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDetallePlanEscolar
     * @throws GeneralException
     */
    public SgDetallePlanEscolar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDetallePlanEscolar> codDAO = new CodigueraDAO<>(em, SgDetallePlanEscolar.class);
                SgDetallePlanEscolar det = codDAO.obtenerPorId(id, null);
                if (det.getDpeFotos() != null){
                    det.getDpeFotos().size();
                }
                return det;
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
                CodigueraDAO<SgDetallePlanEscolar> codDAO = new CodigueraDAO<>(em, SgDetallePlanEscolar.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

     /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgDetallePlanEscolar> obtenerPorFiltro(FiltroDetallePlanEscolar filtro) throws GeneralException {
        try {
            DetallePlanEscolarDAO codDAO = new DetallePlanEscolarDAO(em);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param entity SgDetallePlanEscolar
     * @return SgDetallePlanEscolar
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDetallePlanEscolar guardar(SgDetallePlanEscolar entity) throws GeneralException {
        try {
            if (entity != null) {
                if (DetallePlanEscolarValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgDetallePlanEscolar> codDAO = new CodigueraDAO<>(em, SgDetallePlanEscolar.class);
                    return codDAO.guardar(entity, null);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDetallePlanEscolar> codDAO = new CodigueraDAO<>(em, SgDetallePlanEscolar.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
