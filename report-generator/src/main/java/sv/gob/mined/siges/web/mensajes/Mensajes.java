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
    public static final String ERROR_SEGURIDAD = "ERROR_SEGURIDAD";
    
    public static final String SECCION_NO_ENCONTRADA = "SECCION_NO_ENCONTRADA";
    public static final String TRASLADO_NO_ENCONTRADO = "SECCION_NO_ENCONTRADA";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String SEDE_NO_ENCONTRADA = "SEDE_NO_ENCONTRADA";
    public static final String SECCION_SIN_CALIFICACIONES = "SECCION_SIN_CALIFICACIONES";
    public static final String SECCION_SIN_ESTUDIANTES = "SECCION_SIN_ESTUDIANTES";
    
    public static final String EMPLEADO_SIN_BIENES = "EMPLEADO_SIN_BIENES";
    public static final String BUSQUEDA_SIN_RESULTADOS = "BUSQUEDA_SIN_RESULTADOS";
    public static final String UNIDAD_ACTIVO_FIJO_VACIA = "UNIDAD_ACTIVO_FIJO_VACIA";
    public static final String SEDE_VACIA = "SEDE_VACIA";
    public static final String UNIDAD_ADMINISTRATIVA_VACIA = "UNIDAD_ADMINISTRATIVA_VACIA";
    public static final String SECCION_SIN_ESTUDIANTES_PROMOVIDOS = "SECCION_SIN_ESTUDIANTES_PROMOVIDOS";
    public static final String SECCION_NO_CERRADA = "SECCION_NO_CERRADA";
    public static final String SOLICITUD_NO_ENCONTRADA = "SOLICITUD_NO_ENCONTRADA";
    public static final String TRANSFERENCIA_NO_ENCONTRADA = "TRANSFERENCIA_NO_ENCONTRADA";
    public static final String ESTUDIANTE_NO_ENCONTRADO = "ESTUDIANTE_NO_ENCONTRADO";
    public static final String LIQUIDACION_NO_ENCONTRADA = "LIQUIDACION_NO_ENCONTRADA";
    public static final String OAE_NO_ENCONTRADA = "OAE_NO_ENCONTRADA";
    
    public static final String RESPONSABLE_AF_NO_CONFIGURADO = "RESPONSABLE_AF_NO_CONFIGURADO";
    public static final String CARGO_RESPONSABLE_AF_NO_CONFIGURADO = "CARGO_RESPONSABLE_AF_NO_CONFIGURADO";
    //Módulo de Simple
    public static final String NOMBRE_APROBADO_VACIO = "NOMBRE_APROBADO_VACIO";
    public static final String DEPARTAMENTO_VACIO = "DEPARTAMENTO_VACIO";
    public static final String MUNICIPIO_VACIO = "MUNICIPIO_VACIO";
    public static final String CANTON_VACIO = "CANTON_VACIO";
    public static final String DIRECCION_VACIO = "DIRECCION_VACIO";
    
}
