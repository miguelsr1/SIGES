/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.listener;

import gob.mined.siap2.annotations.AtributoNormalizable;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimaOrigen;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.utils.generalutils.ReflectionUtils;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofis
 */
public class DatosAuditoriaEntity {

    private static final Logger LOGGER = Logger.getLogger(DatosAuditoriaEntity.class.getName());

    public <T> T registrarDatosAuditoria(T o, String codigoUsuario, String origen) throws Exception {
        try {

            for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(AtributoUltimoUsuario.class)) {
                    try {
                        field.set(o, codigoUsuario);
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
                if (field.isAnnotationPresent(AtributoUltimaModificacion.class)) {
                    try {
                        field.set(o, new Date());
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }

                if (field.isAnnotationPresent(AtributoUltimaOrigen.class)) {
                    try {
                        field.set(o, origen);
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }

                if (field.isAnnotationPresent(AtributoNormalizable.class)) {
                    try {
                        String s = (String) field.get(o);
                        if (s != null) {
                            field.set(o, StringsUtils.normalizarString(s));
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
        return o;
    }
}
