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
    public static final String ANIO_LECTIVO_CERRADO_CORRECTO = "ANIO_LECTIVO_CERRADO_CORRECTO";
    public static final String USUARIO_CREADO_CORRECTO = "USUARIO_CREADO_CORRECTO";
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

    public static final String ERROR_SERVICIO_EDUCATIVO_VACIO = "ERROR_SERVICIO_EDUCATIVO_VACIO";
    public static final String ERROR_SERVICIOS_MULTIPLES = "ERROR_SERVICIOS_MULTIPLES";
    public static final String ERROR_CAMBIO_SECCION_VACIO = "ERROR_CAMBIO_SECCION_VACIO";
    public static final String ERROR_SEDE_IGUAL_TRASLADO = "ERROR_SEDE_IGUAL_TRASLADO";
    public static final String ERROR_SOLICITUD_TRASLADO_ACTIVA = "ERROR_SOLICITUD_TRASLADO_ACTIVA";
    public static final String ERROR_ACCION_VACIO = "ERROR_ACCION_VACIO";
    
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
    public static final String APROBADO_CORRECTO = "APROBADO_CORRECTO";
    public static final String ERROR_DATOS_OBLIGATORIOS_VACIO = "ERROR_DATOS_OBLIGATORIOS_VACIO";
    public static final String ERROR_FILTRO_GRADO_VACIO = "ERROR_FILTRO_GRADO_VACIO";
    public static final String ERROR_FECHA_OTORGADO_VACIO = "ERROR_FECHA_OTORGADO_VACIO";
    public static final String ERROR_SEDE_VACIO = "ERROR_SEDE_VACIO";
    public static final String ERROR_CREACION_USUARIO_ALLEGADO = "ERROR_CREACION_USUARIO_ALLEGADO";
    public static final String ERROR_VALIDAR_ACTIVIADADES = "ERROR_VALIDAR_ACTIVIADADES";
    public static final String ERROR_COMPONENTE_PLAN_ESTUDIO_FORMULA_APROBACION = "ERROR_COMPONENTE_PLAN_ESTUDIO_FORMULA_APROBACION";
    public static final String ERROR_GRADO_SIN_FORMULA = "ERROR_GRADO_SIN_FORMULA";
    public static final String ERROR_ESCALA_CALIFICACION_ASOCIADA_NO_TIENE_VALOR_MINIMO = "ERROR_ESCALA_CALIFICACION_ASOCIADA_NO_TIENE_VALOR_MINIMO";
    public static final String WARNING_ESTUDIANTE_SOLO_LECTURA_PERTENECE_OTRA_INSTITUCION = "WARNING_ESTUDIANTE_SOLO_LECTURA_PERTENECE_OTRA_INSTITUCION";
    public static final String ERROR_NIE_ESTUDIANTE_NO_EXISTE = "ERROR_NIE_ESTUDIANTE_NO_EXISTE";
    public static final String ERROR_SECCION_IMPORT_VACIO = "ERROR_SECCION_IMPORT_VACIO";
    public static final String ERROR_FECHA_IMPORT_VACIO = "ERROR_FECHA_IMPORT_VACIO";
    public static final String ERROR_NIE_IMPORT_VACIO = "ERROR_NIE_IMPORT_VACIO";
    public static final String ERROR_NO_SE_ENCONTRO_ESTUDIANTE = "ERROR_NO_SE_ENCONTRO_ESTUDIANTE";
    public static final String ERROR_NO_SE_ENCONTRO_PERSONAL = "ERROR_NO_SE_ENCONTRO_PERSONAL";
    public static final String ERROR_NO_SE_ENCONTRO_PERSONA = "ERROR_NO_SE_ENCONTRO_PERSONA";
    public static final String CAMBIO_ESTADO_APROBADO = "CAMBIO_ESTADO_APROBADO";
    public static final String CAMBIO_ESTADO_RECHAZADO = "CAMBIO_ESTADO_RECHAZADO";
    public static final String WARNING_RESPONSABLE_ES_PERSONAL_DE_SEDE = "WARNING_RESPONSABLE_ES_PERSONAL_DE_SEDE";
    public static final String ERROR_SECCION_DESTINO_VACIO = "ERROR_SECCION_DESTINO_VACIO";
    public static final String ERROR_LISTA_ESTUDIANTES_VACIO = "ERROR_LISTA_ESTUDIANTES_VACIO";
    public static final String ERROR_USUARIO_ALLEGADO_EXISTE = "ERROR_USUARIO_ALLEGADO_EXISTE";
    public static final String ERROR_MATRICULA_VACIO = "ERROR_MATRICULA_VACIO";
    public static final String ERROR_NO_EXISTEN_COMPONENTES_PLAN_GRADO = "ERROR_NO_EXISTEN_COMPONENTES_PLAN_GRADO";
    public static final String ERROR_CALIFICACIONES_REALIZADAS = "ERROR_CALIFICACIONES_REALIZADAS";
    public static final String ERROR_ASISTENCIAS_REALIZADAS = "ERROR_ASISTENCIAS_REALIZADAS";
    public static final String ERROR_FECHA_INGRESO_VACIO = "ERROR_FECHA_INGRESO_VACIO";
    public static final String ERROR_ESTUDIANTE_VACIO = "ERROR_ESTUDIANTE_VACIO";
    public static final String WARNING_NUEVOS_ESTUDIANTES_EN_SECCION_SIN_CONTROL_ASISTENCIA = "WARNING_NUEVOS_ESTUDIANTES_EN_SECCION_SIN_CONTROL_ASISTENCIA";
    public static final String ERROR_FECHA_VACIO = "ERROR_FECHA_VACIO";
    public static final String ERROR_CODIGO_COMPONENTE_PLAN_ESTUDIO_IMPORT_VACIO = "ERROR_CODIGO_COMPONENTE_PLAN_ESTUDIO_IMPORT_VACIO";
    public static final String ERROR_RANGO_FECHA_IMPORT_VACIO = "ERROR_RANGO_FECHA_IMPORT_VACIO";
    public static final String ERROR_CALIFICACION_IMPORT_VACIO = "ERROR_CALIFICACION_IMPORT_VACIO";
    public static final String ERROR_NIE_NO_NUMERICO = "ERROR_NIE_NO_NUMERICO";
    public static final String ERROR_FECHA_FORMATO_INCORRECTO = "ERROR_FECHA_FORMATO_INCORRECTO";
    public static final String ERROR_ARCHIVO_VACIO = "ERROR_ARCHIVO_VACIO";
    public static final String VALIDACION_ARCHIVO = "VALIDACION_ARCHIVO";
    public static final String ARCHIVO_EXITOSAMENTE_COLA = "ARCHIVO_EXITOSAMENTE_COLA";
    public static final String ERROR_EDAD_MAXIMA_MAYOR_100 = "ERROR_EDAD_MAXIMA_MAYOR_100";
    public static final String ERROR_PERIODO_MATRICULA_CERRADO = "ERROR_PERIODO_MATRICULA_CERRADO";
    public static final String ERROR_IDENTIFICACION_DUPLICADA = "ERROR_IDENTIFICACION_DUPLICADA";
    public static final String ERROR_IDENTIFICACION_DUPLICADA_OAE = "ERROR_IDENTIFICACION_DUPLICADA_OAE";

    public static final String ERROR_MATRICULA_ANULADA = "ERROR_MATRICULA_ANULADA";

    public static final String ERROR_DIMENSIONES_IMAGENES = "ERROR_DIMENSIONES_IMAGENES";
    public static final String ERROR_CONFIGURACION_ARCHIVO_VACIO = "ERROR_CONFIGURACION_ARCHIVO_VACIO";
    public static final String WARNING_CONTROL_ASISTENCIA_ANIO_LECTIVO_CERRADO = "WARNING_CONTROL_ASISTENCIA_ANIO_LECTIVO_CERRADO";
    public static final String WARNING_CALIFICACION_RANGO_FECHA_DESHABILITADO = "WARNING_CALIFICACION_RANGO_FECHA_DESHABILITADO";
    public static final String ERROR_TRASLADO_DIFERENTE_GRADO = "ERROR_TRASLADO_DIFERENTE_GRADO";
    public static final String ERROR_SUB_MODALIDAD_VACIO = "ERROR_SUB_MODALIDAD_VACIO";
    public static final String ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_VACIA = "ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_VACIA";
    public static final String ERROR_NO_SE_ENCONTRARON_EQUIVALENCIAS = "ERROR_NO_SE_ENCONTRARON_EQUIVALENCIAS";
    public static final String ERROR_NUMERO_PERIODO_MENOR_RANGO_FECHA = "ERROR_NUMERO_PERIODO_MENOR_RANGO_FECHA";
    public static final String ERROR_RETIRO_TRASLADO_EN_PROCESO = "ERROR_RETIRO_TRASLADO_EN_PROCESO";
    public static final String ERROR_NO_PUEDE_CAMBIAR_FECHA = "ERROR_NO_PUEDE_CAMBIAR_FECHA";
    public static final String WARNING_NO_HAY_SECCIONES = "WARNING_NO_HAY_SECCIONES";
    public static final String ERROR_VALIDAR_MATRICULA = "ERROR_VALIDAR_MATRICULA";
    public static final String ERROR_SECCIONES_NO_EQUIVALENTES = "ERROR_SECCIONES_NO_EQUIVALENTES";
    public static final String ERROR_ANIO_SECCIONES_DIFIERE = "ERROR_ANIO_SECCIONES_DIFIERE";
    public static final String ARCHIVOS_PENDIENTES_PROCESADOS = "ARCHIVOS_PENDIENTES_PROCESADOS";
    public static final String ERROR_ESTUDIANTE_PROMOVIDO = "ERROR_ESTUDIANTE_PROMOVIDO";
    public static final String ANIO_CERRADO_EXITO = "ANIO_CERRADO_EXITO";       
    public static final String WARNING_SECCION_CERRADO = "WARNING_SECCION_CERRADO";
    public static final String EXISTE_ESTUDIANTE_SIN_PERIODOS = "EXISTE_ESTUDIANTE_SIN_PERIODOS";
    public static final String NOTA_INSTITUCIONAL_GENERADA_CORRECTAMENTE = "NOTA_INSTITUCIONAL_GENERADA_CORRECTAMENTE";
    public static final String NOTA_APROBACION_GENERADA_CORRECTAMENTE = "NOTA_APROBACION_GENERADA_CORRECTAMENTE";
    public static final String WARNING_GUARDAR_CAMBIOS = "WARNING_GUARDAR_CAMBIOS";    
    public static final String ERROR_PERSONAL_VACIO = "ERROR_PERSONAL_VACIO";    
    public static final String ERROR_EXISTE_CONTROL_ASISTENCIA_PARA_FECHA = "ERROR_EXISTE_CONTROL_ASISTENCIA_PARA_FECHA";  
    public static final String ERROR_EXISTE_CONTROL_ASISTENCIA_PARA_DIA = "ERROR_EXISTE_CONTROL_ASISTENCIA_PARA_DIA";   
    public static final String ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_ALFABETIZADOR_VACIA = "ERROR_IDENTIFICACIONES_OBLIGATORIA_NIP_ALFABETIZADOR_VACIA";   
    public static final String NO_EXISTE_TITULO_PARA_ESTUDIANTE = "NO_EXISTE_TITULO_PARA_ESTUDIANTE";  
    public static final String MATRICULAS_CERRADAS_PROMOCION = "MATRICULAS_CERRADAS_PROMOCION";  

    public static final String ERROR_EN_FORMULA = "ERROR_EN_FORMULA";  
    public static final String PROMOCION_EXITOSAMENTE_COLA = "PROMOCION_EXITOSAMENTE_COLA";
    public static final String ERROR_SELLO_FIRMA_DIRECTOR_VACIO = "ERROR_SELLO_FIRMA_DIRECTOR_VACIO";
    public static final String ERROR_DIRECTOR_NO_TIENE_CONTRATO_VIGENTE = "ERROR_DIRECTOR_NO_TIENE_CONTRATO_VIGENTE";
    public static final String ERROR_SERVICIO_EDUCATIVO_NO_HABILITADO = "ERROR_SERVICIO_EDUCATIVO_NO_HABILITADO";
    public static final String ERROR_MOTIVO_REIMPRESION_VACIO = "ERROR_MOTIVO_REIMPRESION_VACIO";
    public static final String ERROR_GRADO_REQUIERE_NIE = "ERROR_GRADO_REQUIERE_NIE";
    public static final String ERROR_CODIGO_VACIO = "ERROR_CODIGO_VACIO";

    public static final String CAMBIO_ESTADO_AMPLIAR_DATOS = "CAMBIO_ESTADO_AMPLIAR_DATOS";
    public static final String REPOSICION_GENERADA_CORRECTAMENTE = "REPOSICION_GENERADA_CORRECTAMENTE";
    public static final String ERROR_MATRICULA_NO_CUMPLE_EQUIVALENCIA = "ERROR_MATRICULA_NO_CUMPLE_EQUIVALENCIA";
    public static final String ERROR_PERSONA_CON_SELLO_FIRMA = "ERROR_PERSONA_CON_SELLO_FIRMA";
    public static final String ERROR_ESTUDIANTE_NO_TIENE_MATRICULA_ABIERTA = "ERROR_ESTUDIANTE_NO_TIENE_MATRICULA_ABIERTA";
    public static final String RECHAZADO_CORRECTO = "RECHAZADO_CORRECTO";
    public static final String ERROR_NIVEL_VACIO = "ERROR_NIVEL_VACIO";
    public static final String ERROR_SUBMODALIDAD_VACIO = "ERROR_SUBMODALIDAD_VACIO";
    public static final String ERROR_NIVEL_NO_SELECCIONADO = "ERROR_NIVEL_NO_SELECCIONADO";
    public static final String ERROR_NO_SE_ENCONTRO_PERSONAL_CON_CONTRATO_ACTIVO_EN_SEDE = "ERROR_NO_SE_ENCONTRO_PERSONAL_CON_CONTRATO_ACTIVO_EN_SEDE";
    public static final String ERROR_CARGO_DE_PERSONAL_NO_TIENE_TIPOS_DE_REPRESENTANTE_HABILITADOS = "ERROR_CARGO_DE_PERSONAL_NO_TIENE_TIPOS_DE_REPRESENTANTE_HABILITADOS";
    public static final String ERROR_DUI_VACIO = "ERROR_DUI_VACIO";
    public static final String ERROR_NO_EXISTE_PERSONA_DUI_RNPN = "ERROR_NO_EXISTE_PERSONA_DUI_RNPN";
    public static final String ERROR_NO_EXISTE_PERSONA_CUN_RNPN = "ERROR_NO_EXISTE_PERSONA_CUN_RNPN";
    public static final String ERROR_NO_EXISTE_PERSONA_PARTIDA_NACIMIENTO_RNPN = "ERROR_NO_EXISTE_PERSONA_PARTIDA_NACIMIENTO_RNPN";
    public static final String ERROR_DEBE_GUARDAR_PERSONA_ANTES_DE_CONSULTAR_RNPN = "ERROR_DEBE_GUARDAR_PERSONA_ANTES_DE_CONSULTAR_RNPN";
    public static final String ERROR_MENOR_YA_ASOCIADO_CON_ESTUDIANTE = "ERROR_MENOR_YA_ASOCIADO_CON_ESTUDIANTE";
    public static final String CERRADO_SEDE_CORRECTO = "CERRADO_SEDE_CORRECTO";
}
