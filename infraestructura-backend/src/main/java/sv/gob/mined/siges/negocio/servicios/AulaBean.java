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
import sv.gob.mined.siges.filtros.FiltroAula;
import sv.gob.mined.siges.negocio.validaciones.AulaValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.RelAulaEspacioValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.RelAulaServicioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AulaDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAula;
import sv.gob.mined.siges.persistencia.entidades.SgAulaLiteEspacio;
import sv.gob.mined.siges.persistencia.entidades.SgAulaLiteServicio;
import sv.gob.mined.siges.persistencia.entidades.SgRelAulaEspacio;
import sv.gob.mined.siges.persistencia.entidades.SgRelAulaServicio;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class AulaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAula
     * @throws GeneralException
     */
    public SgAula obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAula> codDAO = new CodigueraDAO<>(em, SgAula.class);
                SgAula aul=codDAO.obtenerPorId(id);
                aul.getAulTipologiaConstruccion().size();
                return aul;
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
                CodigueraDAO<SgAula> codDAO = new CodigueraDAO<>(em, SgAula.class);
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
     * @param aul SgAula
     * @return SgAula
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAula guardar(SgAula aul) throws GeneralException {
        try {
            if (aul != null) {
                if (AulaValidacionNegocio.validar(aul)) {
                    CodigueraDAO<SgAula> codDAO = new CodigueraDAO<>(em, SgAula.class);

                    return codDAO.guardar(aul);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return aul;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAula filtro) throws GeneralException {
        try {
            AulaDAO codDAO = new AulaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgAula>
     * @throws GeneralException
     */
    public List<SgAula> obtenerPorFiltro(FiltroAula filtro) throws GeneralException {
        try {
            AulaDAO codDAO = new AulaDAO(em);
            return codDAO.obtenerPorFiltro(filtro,  filtro.getSecurityOperation());
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
                CodigueraDAO<SgAula> codDAO = new CodigueraDAO<>(em, SgAula.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAula
     * @return SgAula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAulaLiteEspacio guardarLiteEspacio(SgAulaLiteEspacio entity) throws GeneralException {
        try {
            if (entity != null) {                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
          
                for (SgRelAulaEspacio ree : entity.getAulRelAulaEspacio()) {
                     RelAulaEspacioValidacionNegocio.validar(ree);
                  
                }
             
                    CodigueraDAO<SgAulaLiteEspacio> codDAO = new CodigueraDAO<>(em, SgAulaLiteEspacio.class);
                    return codDAO.guardar(entity);
               
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAula
     * @return SgAula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAulaLiteServicio guardarLiteServicio(SgAulaLiteServicio entity) throws GeneralException {
        try {
            if (entity != null) {                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                Boolean guardar = Boolean.TRUE;
                for (SgRelAulaServicio ree : entity.getAulRelAulaServicio()) {
                    guardar = RelAulaServicioValidacionNegocio.validar(ree);
                }  
                    CodigueraDAO<SgAulaLiteServicio> codDAO = new CodigueraDAO<>(em, SgAulaLiteServicio.class);
                    return codDAO.guardar(entity);
               
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

}
