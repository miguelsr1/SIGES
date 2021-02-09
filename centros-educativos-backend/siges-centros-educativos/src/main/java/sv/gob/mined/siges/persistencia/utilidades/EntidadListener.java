/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class EntidadListener {

    private static final Logger LOGGER = Logger.getLogger(EntidadListener.class.getName());
    
    private JsonWebToken callerPrincipal;

    @PrePersist
    @PreUpdate
    public void preSave(Object o) throws Exception {
        
        callerPrincipal = Lookup.obtenerJWT();

        for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(AtributoUltimoUsuario.class)) {
                try {
                    field.set(o, callerPrincipal != null ? callerPrincipal.getName() : null);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
            if (field.isAnnotationPresent(AtributoUltimaModificacion.class)) {
                try {
                    field.set(o, LocalDateTime.now());
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
            if (field.isAnnotationPresent(AtributoNormalizable.class)) {
                try {
                    String s = (String) field.get(o);
                    if (s != null) {
                        field.set(o, SofisStringUtils.normalizarString(s));
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
