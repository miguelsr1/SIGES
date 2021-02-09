/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.constantes;

import java.math.BigDecimal;


public class Constantes {
    
    public static final String CATEGORIA_BIENES_CACHE = "CATEGORIA_BIENES_CACHE";
    
    public static final String SCHEMA_CATALOGO = "catalogo";
    public static final String SCHEMA_CENTRO_EDUCATIVO = "centros_educativos";
    public static final String SCHEMA_SEGURIDAD = "seguridad";
    public static final String SCHEMA_ACTIVO_FIJO = "activo_fijo";
    public static final String SCHEMA_PUBLIC = "public";
   
    public static final String OPENTRACING_ERROR_TAG = "error";
    
    public static final String CODIGO_TIPO_DESCARGO_PERMUTA = "APER";
    public static final String CODIGO_ESTADO_CALIDAD_MALO = "2";
    public static final String CODIGO_ESTADO_TIPO_DESCARGO_INSERVIBLE = "01";
    public static final String CODIGO_ESTADO_DESCARGADO = "DESC";
    public static final String CODIGO_ESTADO_TRASLADADO = "TRASL";
    public static final String CODIGO_ESTADO_PROCESO_TRASLADO = "PROTRAS";
    public static final String CODIGO_ESTADO_SOLICITUD_RECHAZADA = "SOLRECH";
    //CODIGOS TIPOS TRASLADO
    public static final String CODIGO_TIPO_TRASLADO_REPARACION = "1";
    public static final String CODIGO_TIPO_TRASLADO_PRESTAMO = "2";
    public static final String CODIGO_TIPO_TRASLADO_DEFINITIVO = "3";
    
    public static final BigDecimal TEN = new BigDecimal(10);
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    
    public static final Integer DIAS_REFERENCIA_MES = 30;
    public static final Integer DIAS_REFERENCIA_MES_31_DIAS = 31;
    
    public static final Integer MESES_ANIO = 12;
    
    public static final Integer MES_FEBRERO= 2;
    public static final Integer DECIMALES_OPERACION = 4;
    public static final Integer DECIMALES_REDONDEO = 2;
    
    public static final Integer MES_ENERO = 1;
    public static final Integer MES_PRIMER_DIA = 1;
    public static final Integer MES_DICIEMBRE = 12;
    public static final Integer MES_DICIEMBRE_ULTIMO_DIA = 31;
}
