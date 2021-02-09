/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.LogAuditoria;
import gob.mined.siap2.exceptions.GeneralException;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author
 */
@Stateless
@LocalBean
/**
 * Esta clase incluye los métodos para registrar los datos en el log de auditoría
 * 
 */
public class LogAuditoriaBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    NumeradoresBean numBean;
    @PersistenceContext(unitName = "generico-PU")
    EntityManager em;

    @Resource
    SessionContext ctx;

    /**
     * Este método registra una entrada en el log de auditoría.
     * 
     * @param id de la operación a loguear
     * @param operacion es la operación que realiza el usuario.
     * @param usuario usuario que realiza la operación
     * @param descripcion descripción de la operación.
     * @param idEntidad entidad involucrada.
     * @return
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    
    public Long loguear(Long id, String operacion, String usuario, String descripcion, Object idEntidad) throws GeneralException {
        if (id == null) {
            id = numBean.obtenerNumeroOperacion();
        }
         
        if (usuario == null) {
            usuario = ctx.getCallerPrincipal().getName();
        }
        logger.log(Level.INFO, "id={0},operacion={1},usuario={2},descripcion={3},idEntidad={4}",
                new Object[]{id, operacion, usuario, descripcion, idEntidad});
        LogAuditoria log = new LogAuditoria();
        log.setLogDescripcion(descripcion);
        log.setLogEntidadId(BigInteger.valueOf(id));
        log.setLogFecha(new Date());
        log.setLogOperacion(operacion);
        log.setLogTrn(BigInteger.valueOf(id));
        log.setLogUsuario(usuario);
        em.persist(log);
        return id;
    }
}
