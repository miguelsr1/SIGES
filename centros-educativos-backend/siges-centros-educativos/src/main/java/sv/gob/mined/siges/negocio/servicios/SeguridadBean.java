/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.security.OperationSecurity;
import java.util.List;
import java.util.logging.Logger;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.seguridad.FiltroRol;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuario;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuarioRol;
import sv.gob.mined.siges.negocio.validaciones.UsuarioRolValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SeguridadDAO;
import sv.gob.mined.siges.persistencia.daos.UsuarioDAO;
import sv.gob.mined.siges.persistencia.daos.seguridad.RolDAO;
import sv.gob.mined.siges.persistencia.daos.seguridad.UsuarioRolDAO;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgRol;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioRol;

@Stateless
public class SeguridadBean {

    @PersistenceContext
    private EntityManager em;

   

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCasoViolacion
     * @return Lista de SgCasoViolacion
     * @throws GeneralException
     */
    public List<SgUsuario> obtenerUsuarios(FiltroUsuario filtro) throws GeneralException {
        try {
            UsuarioDAO codDAO = new UsuarioDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgUsuarioRol> obtenerUsuariosRol(FiltroUsuarioRol filtro) throws GeneralException {
        try {
            UsuarioRolDAO codDAO = new UsuarioRolDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroRol
     * @return Lista de SgRol
     * @throws GeneralException
     */
    public List<SgRol> obtenerRol(FiltroRol filtro) throws GeneralException {
        try {
            RolDAO codDAO = new RolDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgPersonaUsuariaRol
     * @return SgPersonaUsuariaRol
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuarioRol guardarUsuarioRol(SgUsuarioRol entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (UsuarioRolValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgUsuarioRol> codDAO = new CodigueraDAO<>(em, SgUsuarioRol.class);
                    SgUsuarioRol usuRol = codDAO.guardar(entity, null);
                    return usuRol;
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
    public void eliminarUsuarioRol(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgUsuarioRol> codDAO = new CodigueraDAO<>(em, SgUsuarioRol.class);
                SgUsuarioRol usuRol = codDAO.obtenerPorId(id, null);
                SgUsuario usu = usuRol.getPurUsuario();
                usu.getPusPersonaUsuarioRol().remove(usuRol);
                codDAO.eliminarPorId(id, null);

            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(SeguridadBean.class.getName());
    
    
    @CacheResult(cacheName = Constantes.OPERACIONES_SEGURIDAD_CACHE)
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario) {
        SeguridadDAO segDAO = new SeguridadDAO(em);
        return segDAO.obtenerOperacionesSeguridad(operacion, codigoUsuario);
    }


    @CacheResult(cacheName = Constantes.OPERACIONES_SEGURIDAD_AMBITO_CACHE)
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigoUsuario, EnumAmbito ambito) {
        SeguridadDAO segDAO = new SeguridadDAO(em);
        return segDAO.obtenerOperacionesSeguridad(operacion, codigoUsuario, ambito);
    }

}
