package com.sofis.persistence.listener;

import com.sofis.persistence.anotaciones.AtributoUltimaModificacion;
import com.sofis.persistence.anotaciones.AtributoUltimoUsuario;
import java.lang.reflect.Field;
import java.util.Date;

/**
 *
 * @author sofis
 */
public class DatosAuditoriaEntity {
        public <T> T registrarDatosAuditoria(T objeto, String codigoUsuario, String origen) throws Exception{
        try {
            
            Field fU = obtenerCampoAnotado(objeto, AtributoUltimoUsuario.class);
            if (fU != null) {
                fU.set(objeto, codigoUsuario);
            }

            Field fUM = obtenerCampoAnotado(objeto, AtributoUltimaModificacion.class);
            if (fUM != null) {
                fUM.set(objeto, new Date());
            }

        } catch (Exception ex) {
            throw ex;
        }
        return objeto;
    }

    private static Field obtenerCampoAnotado(Object o, Class claseAnotacion) {
        Field fieldSeleccionado = null;
        if (o.getClass().getSuperclass() != null) {
            for (Field field : o.getClass().getSuperclass().getDeclaredFields()) {
                field.setAccessible(true); 
                if (field.isAnnotationPresent(claseAnotacion)) {
                    fieldSeleccionado = field;
                    break;
                }
            }
        }
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true); 
            if (field.isAnnotationPresent(claseAnotacion)) {
                fieldSeleccionado = field;
                break;
            }
        }
        return fieldSeleccionado;
    }
}
