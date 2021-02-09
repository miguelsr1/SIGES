/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.HashMap;
import java.util.ResourceBundle;

public class Mensajes {

    private static HashMap<String, String> MENSAJES = new HashMap();
    private static ResourceBundle bundle = ResourceBundle.getBundle("sv.gob.mined.siges.web.presentacion.mensajes.mensajes");

    public static String obtenerMensaje(String key) {
        if (bundle.containsKey(key)) {
            return bundle.getString(key);
        } else {
            return "?" + key + "?";
        }
    }

    public static boolean containsKey(String key) {
        return bundle.containsKey(key);
    }

    public static final String GUARDADO_CORRECTO = "GUARDADO_CORRECTO";
    public static final String ELIMINADO_CORRECTO = "ELIMINADO_CORRECTO";
    public static final String ENVIADO_CORRECTO = "ENVIADO_CORRECTO";
    public static final String ERROR_GENERAL = "ERROR_GENERAL";
    public static final String USUARIO_INCORRECTO = "USUARIO_INCORRECTO";
    public static final String CAMBIO_CONTRASENIA_CORRECTO = "CAMBIO_CONTRASENIA_CORRECTO";
    public static final String ERROR_CONTRASENIA_ACTUAL_VACIA = "ERROR_CONTRASENIA_ACTUAL_VACIA";
    public static final String ERROR_CONTRASENIA_NUEVA_VACIA = "ERROR_CONTRASENIA_NUEVA_VACIA";
    public static final String ERROR_CONFIRMACION_CONTRASENIA_VACIA = "ERROR_CONFIRMACION_CONTRASENIA_VACIA";
    public static final String ERROR_CONTRASENIAS_NO_COINCIDEN = "ERROR_CONTRASENIAS_NO_COINCIDEN";
    public static final String DATOS_INCORRECTOS = "DATOS_INCORRECTOS";
    public static final String ERROR_LARGO_MINIMO_8 = "ERROR_LARGO_MINIMO_8";
    public static final String ERROR_CANTIDAD_MINIMA_DIGITOS_1 = "ERROR_CANTIDAD_MINIMA_DIGITOS_1";
    public static final String ERROR_CANTIDAD_MINIMA_LETRAS_MAYUSCULAS_1 = "ERROR_CANTIDAD_MINIMA_LETRAS_MAYUSCULAS_1";
    public static final String ERROR_CONTRASENIA_INCLUYE_USUARIO = "ERROR_CONTRASENIA_INCLUYE_USUARIO";
    public static final String ERROR_SEGURIDAD = "ERROR_SEGURIDAD";

    

}
