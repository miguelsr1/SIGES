/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudCurso;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroSolicitudCursoDocente;
import sv.gob.mined.siges.negocio.validaciones.SolicitudCursoDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SolicitudCursoDocenteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudCursoDocente;

@Stateless
@Traced
public class SolicitudCursoDocenteBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSolicitudCursoDocente
     * @throws GeneralException
     */
    public SgSolicitudCursoDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolicitudCursoDocente> codDAO = new CodigueraDAO<>(em, SgSolicitudCursoDocente.class);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolicitudCursoDocente> codDAO = new CodigueraDAO<>(em, SgSolicitudCursoDocente.class);
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
     * @param entity SgSolicitudCursoDocente
     * @return SgSolicitudCursoDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudCursoDocente guardar(SgSolicitudCursoDocente entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudCursoDocenteValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgSolicitudCursoDocente> codDAO = new CodigueraDAO<>(em, SgSolicitudCursoDocente.class);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroSolicitudCursoDocente filtro) throws GeneralException {
        try {
            SolicitudCursoDocenteDAO codDAO = new SolicitudCursoDocenteDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgSolicitudCursoDocente
     * @throws GeneralException
     */
    public List<SgSolicitudCursoDocente> obtenerPorFiltro(FiltroSolicitudCursoDocente filtro) throws GeneralException {
        try {
            SolicitudCursoDocenteDAO codDAO = new SolicitudCursoDocenteDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            
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
                CodigueraDAO<SgSolicitudCursoDocente> codDAO = new CodigueraDAO<>(em, SgSolicitudCursoDocente.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Aprueba el objeto indicado
     *
     * @param entity SgSolicitudCursoDocente
     * @return SgSolicitudCursoDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudCursoDocente aprobar(SgSolicitudCursoDocente entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudCursoDocenteValidacionNegocio.validar(entity)) {
                    entity.setScdEstado(EnumEstadoSolicitudCurso.APROBADA);
                    CodigueraDAO<SgSolicitudCursoDocente> codDAO = new CodigueraDAO<>(em, SgSolicitudCursoDocente.class);
                    return codDAO.guardar(entity, ConstantesOperaciones.APROBAR_SOLICITUD_CURSO_DOCENTE);
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
     * Rechaza el objeto indicado
     *
     * @param entity SgSolicitudCursoDocente
     * @return SgSolicitudCursoDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudCursoDocente rechazar(SgSolicitudCursoDocente entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudCursoDocenteValidacionNegocio.validar(entity)) {
                    entity.setScdEstado(EnumEstadoSolicitudCurso.RECHAZADA);
                    CodigueraDAO<SgSolicitudCursoDocente> codDAO = new CodigueraDAO<>(em, SgSolicitudCursoDocente.class);
                    return codDAO.guardar(entity, ConstantesOperaciones.RECHAZAR_SOLICITUD_CURSO_DOCENTE);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

}
