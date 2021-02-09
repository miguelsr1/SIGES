/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import java.io.Serializable;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "erroresMB")
@ApplicationScoped
public class ErroresMB  implements Serializable{

    private   static HashMap<String, String> MENSAJES_ERROR = new HashMap<String, String>();

    /**
     * Creates a new instance of ErroresMB
     */
    public ErroresMB() {
    }

    static {
        MENSAJES_ERROR.put("COD_VAC", "No se ha ingresado un código");
        MENSAJES_ERROR.put("DES_VAC", "No ha completado la descripción");
    }

    public static String obtenerDescripcion(String codigo) {
        return MENSAJES_ERROR.get(codigo);
    }
}
