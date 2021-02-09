/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroArchivoCalificaciones;
import sv.gob.mined.siges.negocio.validaciones.ArchivoCalificacionesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.ArchivoCalificacionesDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificaciones;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.enumerados.EnumEstadoImportado;

@Stateless
@Traced
public class ArchivoCalificacionesBean {
     private static final Logger LOGGER = Logger.getLogger(ArchivoCalificacionesBean .class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ArchivoBean archivoBean;

    @Inject
    private CalificacionBean calificacionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgArchivoCalificaciones
     * @throws GeneralException
     */
    public SgArchivoCalificaciones obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgArchivoCalificaciones> codDAO = new CodigueraDAO<>(em, SgArchivoCalificaciones.class);
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
                CodigueraDAO<SgArchivoCalificaciones> codDAO = new CodigueraDAO<>(em, SgArchivoCalificaciones.class);
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
     * @param entity SgArchivoCalificaciones
     * @return SgArchivoCalificaciones
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgArchivoCalificaciones guardar(SgArchivoCalificaciones entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ArchivoCalificacionesValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgArchivoCalificaciones> codDAO = new CodigueraDAO(em, SgArchivoCalificaciones.class);
                    SgArchivoCalificaciones arch = codDAO.guardar(entity, null);
                    return arch;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    public SgArchivoCalificaciones guardarConError(SgArchivoCalificaciones entity,String stackTrace) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (entity.getAccPk()!= null) {
                    String query = "update SgArchivoCalificaciones set accEstado='PROCESADO_ERROR', accDescripcion=:desc where accPk= :hrePk";
                    Query qda = em.createQuery(query).setParameter("desc", stackTrace).setParameter("hrePk", entity.getAccPk());
                    qda.executeUpdate();
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroArchivoCalificaciones filtro) throws GeneralException {
        try {
            ArchivoCalificacionesDAO codDAO = new ArchivoCalificacionesDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalendario>
     * @throws GeneralException
     */
    public List<SgArchivoCalificaciones> obtenerPorFiltro(FiltroArchivoCalificaciones filtro) throws GeneralException {
        try {
            ArchivoCalificacionesDAO codDAO = new ArchivoCalificacionesDAO(em);
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
                SgArchivoCalificaciones entity = em.find(SgArchivoCalificaciones.class, id);
                CodigueraDAO<SgArchivoCalificaciones> codDAO = new CodigueraDAO<>(em, SgArchivoCalificaciones.class);
                codDAO.eliminar(entity, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param diasAntiguedad
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarArchivosProcesados(Long diasAntiguedad) throws GeneralException {
        try {
            if(diasAntiguedad==null){
                diasAntiguedad=3L;
            }
            LocalDateTime fechaAntiguedad=LocalDateTime.now();
            fechaAntiguedad=fechaAntiguedad.minusDays(diasAntiguedad);
            
            FiltroArchivoCalificaciones fac= new FiltroArchivoCalificaciones();
            fac.setAccArchivoBorrado(Boolean.FALSE);
            fac.setAccEstado(EnumEstadoImportado.PROCESADO_EXITO);
            fac.setFechaHasta(fechaAntiguedad);
            List<SgArchivoCalificaciones> archivosBorrar=obtenerPorFiltro(fac);            
   
   
            for(SgArchivoCalificaciones archCal:archivosBorrar){
                
                archivoBean.eliminar(archCal.getAccArchivo());
                archCal.setAccArchivo(null);
                archCal.setAccArchivoBorrado(Boolean.TRUE);
                this.guardar(archCal);
                
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }
}
