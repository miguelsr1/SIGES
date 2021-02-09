/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.auditoria;

import java.time.LocalDateTime;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AuditoriaBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void guardarConsumo(String codigoUsuario, String ip, String clase, String operacion, Long entidadId, String comentario) throws Exception{
        SgRegistroAuditoria aud = new SgRegistroAuditoria();  
        aud.setAudOperacion(operacion);
        aud.setAudClase(clase);
        aud.setAudCodigoUsuario(codigoUsuario);
        aud.setAudComentario(comentario);
        aud.setAudEntidadId(entidadId);
        aud.setAudIp(ip);
        aud.setAudResultadoOperacion(ResultadoOperacion.OK);
        aud.setAudFecha(LocalDateTime.now());
        aud.setAudHashMD5(aud.calcularMD5());
        em.persist(aud);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void guardarExcepcion(String codigoUsuario, String ip, String clase, String operacion, Long entidadId, String excepcion) throws Exception {
        SgRegistroAuditoria aud = new SgRegistroAuditoria();  
        if (excepcion != null){
            aud.setAudExcepcion(excepcion.substring(0, Math.min(excepcion.length(), 999)));
        }
        aud.setAudOperacion(operacion);
        aud.setAudClase(clase);
        aud.setAudCodigoUsuario(codigoUsuario);
        aud.setAudEntidadId(entidadId);
        aud.setAudIp(ip);
        aud.setAudResultadoOperacion(ResultadoOperacion.ERROR);
        aud.setAudFecha(LocalDateTime.now());
        aud.setAudHashMD5(aud.calcularMD5());
        em.persist(aud);
    }
}
