/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.annotations.AtributoUltimaOrigen;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.exceptions.BusinessException;
import java.lang.reflect.Field;

/**
 *
 * @author Sofis Solutions
 */
public class DatosAuditoria {

    public <T> T registrarDatosAuditoria(T objeto, String codigoUsuario, String origen) {
        try {
            Field fU = obtenerCampoAnotado(objeto, AtributoUltimoUsuario.class);
            if (fU != null) {
                fU.set(objeto, codigoUsuario);
            }

            Field fO = obtenerCampoAnotado(objeto, AtributoUltimaOrigen.class);
            if (fO != null) {
                fO.set(objeto, origen);
            }
        } catch (Exception ex) {
            BusinessException be = new BusinessException();
            throw be;
        }
        return objeto;
    }

    private static Field obtenerCampoAnotado(Object o, Class claseAnotacion) {
        Field fieldSeleccionado = null;
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true); // You might want to set modifier to public first.
            if (field.isAnnotationPresent(claseAnotacion)) {
                fieldSeleccionado = field;
                break;
            }


        }
        return fieldSeleccionado;
    }
}
