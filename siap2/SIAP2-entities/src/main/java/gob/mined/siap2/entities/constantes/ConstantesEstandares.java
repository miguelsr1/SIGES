/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.constantes;

/**
 * Esta clase incluye constantes del sistema.
 *
 * @author Sofis Solutions
 */
public class ConstantesEstandares {

    public static final String SEPARADOR = "<BR/>";

    public static final String CODIGO_IDIOMA_ESPANIOL = "ES";
    public static final String LOGGER = "com.sofis";

    public static final String CON_CORREO = "CON_CORREO";

    public static final String OPERADOR_YA_EXISTE = "Operador existente";
    public static final String OPERADOR_NO_EXISTE = "Operador no existe";

    public static final String PUERTA_NUEVA_SIN_NOMBRE = "Puerta??";

    public static final String USUARIO_SISTEMA = "MINED";

    public static final int LOCK_CONTROL_DE_INGRESO = 1;
    public static final int LOCK_MODIFICACION_CUPOS_PUERTA = 2;
    public static final int LOCK_CUPOS_PUERTA = 3;
    public static final int LOCK_CALCULO_REPORTE = 4;
    public static final int LOCK_CODIGUERAS = 5;
    public static final int LOCK_SECUENCIA_PROCESO_ADQUISICION = 6;
    public static final int LOCK_SECUENCIA_CONTRATO_ORDEN_COMPRA = 7;
    public static final int LOCK_GENERAR_BLOQUEOS_DE_PAC = 8;
    public static final int LOCK_GENERAR_BLOQUEOS_PEP = 9;
    public static final int LOCK_SECUENCIA_PAGO_CONTRATO = 10;
    public static final int LOCK_SECUENCIA_SOLICITUD_PAGO_ACTA = 11;
    public static final int LOCK_GENERACION_QUEDAN = 12;
    public static final int LOCK_SECUENCIA_MODIFICATIVA_CONTRATO = 13;

    public static final String CODIGO_FUENTE_RECURSO_GOES = "GOES";

    public static final String OPERACION_CREAR_EDITAR_CONTRATO_OC = "CREAR_EDITAR_CONTRATO_OC";
    public static final String OPERACION_FIRMAR_CONTRATO_OC = "FIRMAR_CONTRATO_OC";

    public static final String ORIGEN = "SIAP2";
    public static final String TIPO_UNICO_OPERACION = "TIPO_UNICO";

    public static final String CODIGO_ESTADO_CALIDAD_BUENO = "1";
    public static final String CODIGO_ESTADO_EXISTENTE = "EXIST";
    public static final String CODIGO_FORMA_ADQUISICION_GOES = "3";
    
    public static final Boolean DEFAULT_PWD_CAMBIO_PASSWORD_DESPUES_OLVIDO = Boolean.FALSE;
    public static final Boolean DEFAULT_PWD_CAMBIO_PASSWORD_PRIMERA_VEZ = Boolean.FALSE;
    public static final Boolean DEFAULT_PWD_PERMITE_USU_COD_EN_PASSWORD = Boolean.TRUE;
    public static final Integer DEFAULT_PWD_CANTIDAD_INTENTOS_FALLIDOS = 0;
    public static final Integer DEFAULT_PWD_CANTIDAD_MIN_CARACTERES_ESPECIALES = 0;
    public static final Integer DEFAULT_PWD_CANTIDAD_MIN_MAYUSCULAS = 1;
    public static final Integer DEFAULT_PWD_CANTIDAD_MIN_MINUSCULAS = 0;
    public static final Integer DEFAULT_PWD_CANTIDAD_MIN_DIGITOS = 1;
    public static final Integer DEFAULT_PWD_DIAS_CADUCIDAD = 0;
    public static final Integer DEFAULT_PWD_LARGO_MININO = 8;

    public static final int CANTIDAD_NIVELES_OEG = 10;

    public static abstract class COLORES_PROCESO {

        public static final String COLOR_VERDE = "VERDE";
        public static final String COLOR_AMARILLO = "AMARILLO";
        public static final String COLOR_ROJO = "ROJO";
    }

    public static abstract class Operaciones {

        public static final String CARGA_VALORES_INDICADORES = "CARGA_VALORES_INDICADORES";
        public static final String CARGA_METAS_INDICADORES = "CARGA_METAS_INDICADORES";
        public static final String OPERACION_PARA_VALIDAR_POA_ACCION_CENTRAL = "VALIDAR_POA_ACCION_CENTRAL";
        public static final String OPERACION_PARA_VALIDAR_POA_ASIGNACION_NO_PROGRAMABLE = "VALIDAR_POA_ASIGNACION_NO_PROGRAMABLE";
        public static final String OPERACION_PARA_VALIDAR_POA_PROYECTO = "VALIDAR_POA_PROYECTO";
        public static final String OPERACION_PARA_CONSOLIDAR_POA_PROYECTO = "CONSOLIDADO_POA_PROYECTO";
        public static final String OPERACION_CREAR_EDITAR_PROCESO_ADQUISICION = "CREAR_EDITAR_PROCESO_ADQUISICION";
        public static final String VALIDAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA = "VALIDAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA";
        public static final String VALIDAR_IMPUESTO_CONTRATO = "VALIDAR_IMPUESTO_CONTRATO";
        public static final String FIRMAR_COSNTANCIA_ANUAL_DE_RETENCION_POR_PROVEEDOR = "FIRMAR_COSNTANCIA_ANUAL_DE_RETENCION_POR_PROVEEDOR";
        public static final String VALIDAR_CERTIFICADO_PRES_POR_PROCESO = "VALIDAR_CERTIFICADO_PRES_POR_PROCESO";
        public static final String ENVIAR_CERTIFICADO_PRES_POR_PROCESO = "ENVIAR_CERTIFICADO_PRES_POR_PROCESO";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_ADJUDICACION = "CAMBIAR_ESTADO_CON_PROCESO_EN_ADJUDICACION";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_INICIALIZACION = "CAMBIAR_ESTADO_CON_PROCESO_EN_INICIALIZACION";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_RECEPCION_TDR_ET_CERT_DISP = "CAMBIAR_ESTADO_CON_PROCESO_EN_RECEPCION_TDR_ET_CERT_DISP";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_REVISION_JEFE_UACI = "CAMBIAR_ESTADO_CON_PROCESO_EN_REVISION_JEFE_UACI";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_REVISION_TECNICO_UACI = "CAMBIAR_ESTADO_CON_PROCESO_EN_REVISION_TECNICO_UACI";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_INVITACION = "CAMBIAR_ESTADO_CON_PROCESO_EN_INVITACION";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_RECEPCION_OFERTAS = "CAMBIAR_ESTADO_CON_PROCESO_EN_RECEPCION_OFERTAS";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_EVALUACION = "CAMBIAR_ESTADO_CON_PROCESO_EN_EVALUACION";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_COMPROMISO_PRESUPUESTARIO = "CAMBIAR_ESTADO_CON_PROCESO_EN_COMPROMISO_PRESUPUESTARIO";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_CONTRATO_ORDEN_DE_COMPRA = "CAMBIAR_ESTADO_CON_PROCESO_EN_CONTRATO_ORDEN_DE_COMPRA";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_ORDEN_INICIO = "CAMBIAR_ESTADO_CON_PROCESO_EN_ORDEN_INICIO";
        public static final String CAMBIAR_ESTADO_CON_PROCESO_EN_CERRADO = "CAMBIAR_ESTADO_CON_PROCESO_EN_CERRADO";
        public static final String VALIDAR_COMPROMISO_PRESUPUESTARIO = "VALIDAR_COMPROMISO_PRESUPUESTARIO";
        public static final String VALIDAR_POA_PROYECTO_ADMINISTRATIVO = "VALIDAR_POA_PROYECTO_ADMINISTRATIVO";
        public static final String EVALUAR_PLAN_OPERATIVO_ANUAL = "EVALUAR_PLAN_OPERATIVO_ANUAL";
        public static final String ENVIAR_PLAN_OPERATIVO_ANUAL = "ENVIAR_PLAN_OPERATIVO_ANUAL";
        public static final String VALIDAR_POA_PROYECTO_INV_NO_INV = "VALIDAR_POA_PROYECTO_INV_NO_INV";
        public static final String REPROGRAMAR_TODAS_UT = "REPROGRAMAR_TODAS_UT";
        public static final String REPROGRAMAR_REPROGRAMAR_UT = "REPROGRAMAR_REPROGRAMAR_UT";
        public static final String ENVIAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA = "ENVIAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA";
        public static final String VER_TODOS_INSUMOS_NO_UACI = "VER_TODOS_INSUMOS_NO_UACI";
        public static final String RECIBIR_NOTIFICACIONES_PRESUPUESTO = "RECIBIR_NOTIFICACIONES_PRESUPUESTO";        
        public static final String VER_TODOS_COMPROMISOS = "VER_TODOS_COMPROMISOS";
        public static final String VER_TODOS_LOS_CONTRATOS = "VER_TODOS_LOS_CONTRATOS";
        public static final String REGISTRAR_POA_APOYO = "REGISTRAR_POA_APOYO";
        public static final String GESTION_POA_UNIDADES_APOYO = "GESTION_POA_UNIDADES_APOYO";
        public static final String CERRAR_POA_PROYECTO_UNIDAD_RESPONSABLE = "CERRAR_POA_PROYECTO_UNIDAD_RESPONSABLE";
        public static final String EDITAR_SUBCOMPONENTE = "EDITAR_SUBCOMPONENTE";
        public static final String OPERACION_INEXISTENTE = "-1";

    }

    public static abstract class Secuencias {

        public static final String SEC_NUM_CER_DISP_PRESUP = "SEC_NUM_CER_DISP_PRESUP";
        public static final String SEC_NUM_POLIZA_CONCENTRACION="SEC_NUM_POLIZA_CONCENTRACION";
    }

    public static abstract class FuenteRecursosCodigo {

        public static final String GOES = "GOES";
    }
}
