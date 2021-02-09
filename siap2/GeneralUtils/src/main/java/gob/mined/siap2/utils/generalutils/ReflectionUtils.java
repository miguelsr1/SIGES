/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siap2.utils.generalutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    public static Field obtenerCampoAnotado(Class o, Class claseAnotacion) {
        Field fieldSeleccionado = null;       
        for (Field field : ReflectionUtils.getAllFields(o)) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(claseAnotacion)) {
                fieldSeleccionado = field;
                break;
            }
        }
        return fieldSeleccionado;
    }

    public static String obtenerNombreCampoAnotado(Class o, Class claseAnotacion) {
        Field f = obtenerCampoAnotado(o, claseAnotacion);
        if (f != null) {
            return f.getName();
        }
        return null;
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
    
    public static Field getDeclaredField(Class<?> type, String name){
        List<Field> properties = getAllFields(type);
        for(Field field : properties){
            if(field.getName().equalsIgnoreCase(name)){
                return field;
            }
        }
        return null;
    }

}
