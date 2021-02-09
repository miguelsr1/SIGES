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
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDocenteDocumento;
import sv.gob.mined.siges.negocio.validaciones.DocenteDocumentoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DocenteDocumentoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDocenteDocumento;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class DocenteDocumentoBean {
    
    private static final Logger LOGGER = Logger.getLogger(DocenteDocumentoBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDocenteDocumento
     * @throws GeneralException
     */
    public SgDocenteDocumento obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDocenteDocumento> dao = new CodigueraDAO<>(em, SgDocenteDocumento.class);
                SgDocenteDocumento ret = dao.obtenerPorId(id, null);
                return ret;
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
                CodigueraDAO<SgDocenteDocumento> codDAO = new CodigueraDAO<>(em, SgDocenteDocumento.class);
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
     * @param entity SgDocenteDocumento
     * @return SgDocenteDocumento
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDocenteDocumento guardar(SgDocenteDocumento entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion              
                if (DocenteDocumentoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgDocenteDocumento> codDAO = new CodigueraDAO<>(em, SgDocenteDocumento.class);
                    
                    if (entity.getDdoPk() != null){
                        SgDocenteDocumento comp = codDAO.obtenerPorId(entity.getDdoPk(), null);
                        if (!DocenteDocumentoValidacionNegocio.compararParaGrabar(entity, comp)){
                            entity.setDdoValidado(Boolean.FALSE);
                            entity.setDdoUltValidacionFecha(null);
                            entity.setDdoUltValidacionUsuario(null);
                        }
                    }
                    
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
     * Valida la capacitacion indicada
     *
     * @param Long capPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDocenteDocumento validarRealizado(Long dodPk) throws GeneralException {
        try {
            if (dodPk != null) {
                CodigueraDAO<SgDocenteDocumento> codDAO = new CodigueraDAO<>(em, SgDocenteDocumento.class);          
                SgDocenteDocumento cap = codDAO.obtenerPorId(dodPk, null);
                cap.setDdoValidado(Boolean.TRUE);
                cap.setDdoUltValidacionFecha(LocalDateTime.now());
                cap.setDdoUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
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
    public SgDocenteDocumento invalidarRealizado(Long dodPk) throws GeneralException {
        try {
            if (dodPk != null) {
                CodigueraDAO<SgDocenteDocumento> codDAO = new CodigueraDAO<>(em, SgDocenteDocumento.class);          
                SgDocenteDocumento cap = codDAO.obtenerPorId(dodPk, null);
                cap.setDdoValidado(Boolean.FALSE);
                cap.setDdoUltValidacionFecha(LocalDateTime.now());
                cap.setDdoUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
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
    public Long obtenerTotalPorFiltro(FiltroDocenteDocumento filtro) throws GeneralException {
        try {
            DocenteDocumentoDAO DAO = new DocenteDocumentoDAO(em);
            return DAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgDocenteDocumento
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgDocenteDocumento> obtenerPorFiltro(FiltroDocenteDocumento filtro) throws GeneralException {
        try {
            DocenteDocumentoDAO DAO = new DocenteDocumentoDAO(em);
            return DAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgDocenteDocumento> codDAO = new CodigueraDAO<>(em, SgDocenteDocumento.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve SgDocenteDocumento en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return T
     */
    public SgDocenteDocumento obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgDocenteDocumento> respuesta = reader.createQuery().forEntitiesAtRevision(SgDocenteDocumento.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgDocenteDocumento ret = respuesta.get(0);
                //TODO
                //InitializeObjectUtils.inicializarDocenteDocumentoHist(ret);
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

}
