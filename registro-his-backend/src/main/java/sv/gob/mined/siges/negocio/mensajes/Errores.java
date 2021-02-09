/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.mensajes;

public class Errores {

    public static final String ERROR_GENERAL = "ERROR_GENERAL";
    public static final String ERROR_OPTIMISTIC_LOCK = "ERROR_OPTIMISTIC_LOCK";

    public static final String ERROR_DATO_VACIO = "ERROR_DATO_VACIO";
    public static final String ERROR_CODIGO_VACIO = "ERROR_CODIGO_VACIO";
    public static final String ERROR_NOMBRE_VACIO = "ERROR_NOMBRE_VACIO";
    public static final String ERROR_DESCRIPCION_VACIO = "ERROR_DESCRIPCION_VACIO";
    public static final String ERROR_VALOR_VACIO = "ERROR_VALOR_VACIO";

    public static final String ERROR_LARGO_VALOR_25 = "ERROR_LARGO_VALOR_25";
    public static final String ERROR_LARGO_DESCRIPCION_255 = "ERROR_LARGO_DESCRIPCION_255";
    public static final String ERROR_LARGO_DESCRIPCION_500 = "ERROR_LARGO_DESCRIPCION_500";
    public static final String ERROR_LARGO_NOMBRE_255 = "ERROR_LARGO_NOMBRE_255";
    public static final String ERROR_LARGO_NOMBRE_20 = "ERROR_LARGO_NOMBRE_20";
    public static final String ERROR_LARGO_VALOR_255 = "ERROR_LARGO_VALOR_255";
    public static final String ERROR_LARGO_VALOR_500 = "ERROR_LARGO_VALOR_500";
    public static final String ERROR_LARGO_CODIGO_45 = "ERROR_LARGO_CODIGO_45";
    public static final String ERROR_LARGO_CODIGO_4 = "ERROR_LARGO_CODIGO_4";
//    public static final String ERROR_LARGO_CODIGO_5 = "ERROR_LARGO_CODIGO_5";
    public static final String ERROR_LARGO_CODIGO_6 = "ERROR_LARGO_CODIGO_6";
    public static final String ERROR_LARGO_CODIGO_8 = "ERROR_LARGO_CODIGO_8";
    public static final String ERROR_LARGO_CODIGO_10 = "ERROR_LARGO_CODIGO_10";
    public static final String ERROR_LARGO_CODIGO_20 = "ERROR_LARGO_CODIGO_20";
    public static final String ERROR_LARGO_CODIGO_50 = "ERROR_LARGO_CODIGO_50";

    public static final String ERROR_CODIGO_O_NOMBRE_DUPLICADOS = "ERROR_CODIGO_O_NOMBRE_DUPLICADOS";
    public static final String ERROR_CODIGO_DUPLICADO = "ERROR_CODIGO_DUPLICADO";
    public static final String ERROR_NOMBRE_DUPLICADO = "ERROR_NOMBRE_DUPLICADO";

    public static final String ERROR_ENTIDAD_REFERENCIADA = "ERROR_ENTIDAD_REFERENCIADA";

    public static final String ERROR_SEGURIDAD = "ERROR_SEGURIDAD";
    public static final String ERROR_CONSTRAINT_EXCEPTION = "ERROR_CONSTRAINT_EXCEPTION";
    public static final String ERROR_CODIGO_INVENTARIO_DUPLICADO = "ERROR_CODIGO_INVENTARIO_DUPLICADO";
    public static final String ERROR_CODIGO_DESCARGO_DUPLICADO = "ERROR_CODIGO_DESCARGO_DUPLICADO";

    public static final String ERROR_DEPARTAMENTO_VACIO = "ERROR_DEPARTAMENTO_VACIO";
    public static final String ERROR_MUNICIPIO_VACIO = "ERROR_MUNICIPIO_VACIO";

    public static final String ERROR_LIBRO_VACIO = "ERROR_LIBRO_VACIO";
    public static final String ERROR_PAGINA_VACIO = "ERROR_PAGINA_VACIO";
    public static final String ERROR_NOMBRE_SEDE_VACIO = "ERROR_NOMBRE_SEDE_VACIO";
    public static final String ERROR_ANIO_VACIO = "ERROR_ANIO_VACIO";
    public static final String ERROR_ANIO_RANGO = "ERROR_ANIO_RANGO";
    public static final String ERROR_NIVEL_VACIO = "ERROR_NIVEL_VACIO";
    public static final String ERROR_NOMBRE_LIBRO_VACIO = "ERROR_NOMBRE_LIBRO_VACIO";
    public static final String ERROR_NOMBRE_LIBRO_MAXIMO = "ERROR_NOMBRE_LIBRO_MAXIMO";
    public static final String ERROR_NUMERO_PAGINA_VACIO = "ERROR_NUMERO_PAGINA_VACIO";
    public static final String ERROR_IDENTIFICACIONES_VACIO = "ERROR_IDENTIFICACIONES_VACIO";
    public static final String ERROR_PRIMER_NOMBRE_VACIO = "ERROR_PRIMER_NOMBRE_VACIO";
    public static final String ERROR_PRIMER_APELLIDO_VACIO = "ERROR_PRIMER_APELLIDO_VACIO";
    public static final String ERROR_PRIMER_NOMBRE_100 = "ERROR_PRIMER_NOMBRE_100";
    public static final String ERROR_SEGUNDO_NOMBRE_100 = "ERROR_SEGUNDO_NOMBRE_100";
    public static final String ERROR_TERCER_NOMBRE_100 = "ERROR_TERCER_NOMBRE_100";
    public static final String ERROR_PRIMER_APELLIDO_100 = "ERROR_PRIMER_APELLIDO_100";
    public static final String ERROR_SEGUNDO_APELLIDO_100 = "ERROR_SEGUNDO_APELLIDO_100";
    public static final String ERROR_TERCER_APELLIDO_100 = "ERROR_TERCER_APELLIDO_100";
    public static final String ERROR_PAIS_VACIO = "ERROR_PAIS_VACIO";
    public static final String ERROR_PAIS_EMISOR_VACIO = "ERROR_PAIS_EMISOR_VACIO";
    public static final String ERROR_FECHA_NACIMIENTO_VACIO = "ERROR_FECHA_NACIMIENTO_VACIO";
    public static final String ERROR_FECHA_NACIMIENTO_MAYOR_ACTUAL = "ERROR_FECHA_NACIMIENTO_MAYOR_ACTUAL";
    public static final String ERROR_NACIONALIDAD_VACIA = "ERROR_NACIONALIDAD_VACIA";
    public static final String ERROR_SEXO_VACIO = "ERROR_SEXO_VACIO";
    public static final String ERROR_ESTADO_CIVIL_VACIO = "ERROR_ESTADO_CIVIL_VACIO";
    public static final String ERROR_LARGO_NOMBRE_SEDE_500 = "ERROR_LARGO_NOMBRE_SEDE_500";
    public static final String ERROR_LARGO_GRADO_ESTUDIO_250 = "ERROR_LARGO_GRADO_ESTUDIO_250";
    public static final String ERROR_GRADO_ESTUDIO_VACIO = "ERROR_GRADO_ESTUDIO_VACIO";
    public static final String ERROR_ANIO_ESTUDIO_VACIO = "ERROR_ANIO_ESTUDIO_VACIO";
    public static final String ERROR_LARGO_CIUDAD_250 = "ERROR_LARGO_CIUDAD_250";
    public static final String ERROR_CIUDAD_VACIA = "ERROR_CIUDAD_VACIA";
    public static final String ERROR_NUMERO_TRAMITE_VACIO = "ERROR_NUMERO_TRAMITE_VACIO";
    public static final String ERROR_NUMERO_RESOLUCION_VACIO = "ERROR_NUMERO_RESOLUCION_VACIO";

    public static final String ERROR_LARGO_NUMERO_TRAMITE_250 = "ERROR_LARGO_NUMERO_TRAMITE_250";
    public static final String ERROR_LARGO_NUMERO_RESOLUCION_250 = "ERROR_LARGO_NUMERO_RESOLUCION_250";
    public static final String ERROR_IDENTIFICACION_DUPLICADA = "ERROR_IDENTIFICACION_DUPLICADA";
    public static final String ERROR_FECHA_APROBACION_VACIA = "ERROR_FECHA_APROBACION_VACIA";
    public static final String ERROR_FECHA_APROBACION_MAYOR_ACTUAL = "ERROR_FECHA_APROBACION_MAYOR_ACTUAL";
    public static final String ERROR_NUMERO_TRAMITE_DUPLICADO = "ERROR_NUMERO_TRAMITE_DUPLICADO";
    public static final String ERROR_NUMERO_RESOLUCION_DUPLICADO = "ERROR_NUMERO_RESOLUCION_DUPLICADO";
    public static final String ERROR_DUI_INCORRECTO = "ERROR_DUI_INCORRECTO";
    public static final String ERROR_ANIO_ESTUDIO_MAYOR_ACTUAL = "ERROR_ANIO_ESTUDIO_MAYOR_ACTUAL";

}
