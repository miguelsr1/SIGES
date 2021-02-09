/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDateTime;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.ResultadoOperacion;
import sv.gob.mined.siges.persistencia.entidades.SgRegistroAuditoriaConsumoRNPN;

@Stateless
@Traced
public class RNPNAuditBean {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void guardarConsumo(String personaDocumento, String operacion, String cuerpoEnviado, String cuerpoRecibido, String codigoUsuario, String mensaje, String endpoint) throws Exception {
        SgRegistroAuditoriaConsumoRNPN aud = new SgRegistroAuditoriaConsumoRNPN();
        if (mensaje != null) {
            aud.setAudMensaje(mensaje.substring(0, Math.min(mensaje.length(), 999)));
        }
        aud.setAudPersonaDocumento(personaDocumento);
        aud.setAudOperacion(operacion);
        aud.setAudCuerpoEnviado(cuerpoEnviado);
        aud.setAudCuerpoRecibido(cuerpoRecibido);
        aud.setAudCodigoUsuario(codigoUsuario);
        aud.setAudResultadoOperacion(ResultadoOperacion.OK);
        aud.setAudFecha(LocalDateTime.now());
        aud.setAudHashMD5(aud.calcularMD5());
        aud.setAudEndpoint(endpoint);
        em.persist(aud);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void guardarExcepcion(String personaDocumento, String operacion, String cuerpoEnviado, String cuerpoRecibido, String codigoUsuario, String mensaje, String endpoint) throws Exception {
        SgRegistroAuditoriaConsumoRNPN aud = new SgRegistroAuditoriaConsumoRNPN();
        if (mensaje != null) {
            aud.setAudMensaje(mensaje.substring(0, Math.min(mensaje.length(), 999)));
        }
        aud.setAudPersonaDocumento(personaDocumento);
        aud.setAudOperacion(operacion);
        aud.setAudCuerpoEnviado(cuerpoEnviado);
        aud.setAudCuerpoRecibido(cuerpoRecibido);
        aud.setAudCodigoUsuario(codigoUsuario);
        aud.setAudResultadoOperacion(ResultadoOperacion.ERROR);
        aud.setAudFecha(LocalDateTime.now());
        aud.setAudHashMD5(aud.calcularMD5());
        aud.setAudEndpoint(endpoint);
        em.persist(aud);
    }

}
