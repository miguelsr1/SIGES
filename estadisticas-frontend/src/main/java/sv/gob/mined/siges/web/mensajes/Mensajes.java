/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.Locale;
import java.util.ResourceBundle;

public class Mensajes {

    private static final String MENSAJES = "sv.gob.mined.siges.web.mensajes.MensajesMultipleResourceBundle";
    private static ResourceBundle bundle = ResourceBundle.getBundle(MENSAJES, new Locale("es"));

    public static String obtenerMensaje(String key) {
        return bundle.getString(key);
    }

    public static final String GUARDADO_CORRECTO = "GUARDADO_CORRECTO";
    public static final String ELIMINADO_CORRECTO = "ELIMINADO_CORRECTO";
    public static final String ENVIADO_CORRECTO = "ENVIADO_CORRECTO";
    public static final String ERROR_GENERAL = "ERROR_GENERAL";
    public static final String USUARIO_INCORRECTO = "USUARIO_INCORRECTO";
    public static final String IDENTIFICACION_NO_INGRESADA = "IDENTIFICACION_NO_INGRESADA";
    public static final String PERSONA_NO_ENCONTRADA = "PERSONA_NO_ENCONTRADA";
    public static final String MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION = "MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION";
    public static final String NIE_GENERADO = "NIE_GENERADO";
    public static final String DATOS_INCORRECTOS = "DATOS_INCORRECTOS";
    public static final String ERROR_SECCION_VACIO = "ERROR_SECCION_VACIO";
    public static final String ERROR_SECCION_SIN_PLAN_ESTUDIO = "ERROR_SECCION_SIN_PLAN_ESTUDIO";
    public static final String ERROR_NO_SE_ENCONTRARON_ESTUDIANTES = "ERROR_NO_SE_ENCONTRARON_ESTUDIANTES";
    public static final String ERROR_SEGURIDAD = "ERROR_SEGURIDAD";
    public static final String ERROR_DUI_INCORRECTO = "ERROR_DUI_INCORRECTO";
    public static final String ERROR_ESTUDIANTE_SIN_SECCION = "ERROR_ESTUDIANTE_SIN_SECCION";
    public static final String ERROR_DATOS_ESCALAFON_VACIO = "ERROR_DATOS_ESCALAFON_VACIO";
    public static final String ERROR_FILAS_HORARIO = "ERROR_FILAS_HORARIO";
    public static final String ERROR_NIE_VACIO = "ERROR_NIE_VACIO";
    
    public static final String ERROR_PERSONA_RETIRADO = "ERROR_PERSONA_RETIRADO";
    public static final String ERROR_MOTIVO_TRASLADO_VACIO = "ERROR_MOTIVO_TRASLADO_VACIO";
    public static final String ERROR_MATRICULA_ACTUAL_VACIO = "ERROR_MATRICULA_ACTUAL_VACIO";
    public static final String ERROR_COMPONENTE_PLAN_ESTUDIO_SIN_CALCULO_NOTA_INSTITUCIONAL = "ERROR_COMPONENTE_PLAN_ESTUDIO_SIN_CALCULO_NOTA_INSTITUCIONAL";
    public static final String ERROR_COMPONENTE_PLAN_ESTUDIO_SIN_FUNCION_REDONDEO = "ERROR_COMPONENTE_PLAN_ESTUDIO_SIN_FUNCION_REDONDEO";
    public static final String ERROR_NO_HAY_ANIO_LECTIVO_HABILITADO = "ERROR_NO_HAY_ANIO_LECTIVO_HABILITADO";
    public static final String ERROR_NO_HAY_SECCIONES_PARA_LOS_DATOS_SELECCIONADOS = "ERROR_NO_HAY_SECCIONES_PARA_LOS_DATOS_SELECCIONADOS";
    public static final String ERROR_IDENTIFICACIONES_OBLIGATORIA_VACIA = "ERROR_IDENTIFICACIONES_OBLIGATORIA_VACIA";
    public static final String ERROR_SERVICIO_EDUCATIVO_NO_ENCONTRADO = "ERROR_SERVICIO_EDUCATIVO_NO_ENCONTRADO";
    public static final String ERROR_MATRICULA_ABIERTA_ESTUDIANTE = "ERROR_MATRICULA_ABIERTA_ESTUDIANTE";
    
    public static final String ERROR_ANIO_VACIO = "ERROR_ANIO_VACIO";
    public static final String ERROR_INDICADOR_VACIO = "ERROR_INDICADOR_VACIO";
    public static final String ERROR_NOMBRE_EXTRACCION_VACIO = "ERROR_NOMBRE_EXTRACCION_VACIO";
    public static final String ERROR_NO_EXISTE_CARGA_EXTERNA = "ERROR_NO_EXISTE_CARGA_EXTERNA";

    
            
        
}
