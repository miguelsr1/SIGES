/*
 *  SIGES

 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.constantes;

/**
 * Clase con las constantes de las operaciones de Seguridad.
 *
 * @author jgiron
 */
public class ConstantesOperaciones {

    public static final String AUTENTICADO = "AUTH";

    //OPERACIONES DE BUSQUEDA
    public static final String BUSCAR_PRESUPUESTOS_ESCOLARES = "FB1";
    public static final String BUSCAR_MOVIMIENTO_PRESUPUESTO = "FB2";
    public static final String BUSCAR_DIRECCION_DEPARTAMENTAL = "FB3";
    public static final String BUSCAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL = "FB4";
    public static final String BUSCAR_DOCUMENTOS = "FB5";
    public static final String BUSCAR_CUENTAS_BANCARIAS = "FB6";
    public static final String BUSCAR_MOVIMIENTO_CUENTA_BANCARIA = "FB7";
    public static final String BUSCAR_RUBRO = "FB8";
    public static final String BUSCAR_TRANSFERENCIAS_COMPONENTES = "FB9";
    public static final String BUSCAR_TRANSFERENCIAS_A_CED = "FB10";
    public static final String BUSCAR_AREA_INVERSION = "FB11";
    public static final String BUSCAR_RECIBOS = "FB12";
    public static final String BUSCAR_BANCOS = "FB14";
    public static final String BUSCAR_SOLICITUD_DE_TRANSFERENCIA = "FB15";
    public static final String BUSCAR_CUENTASBANCARIASCC = "FB16";
    public static final String BUSCAR_MOVIMIENTOSCAJACHICA = "FB17";
    public static final String BUSCAR_ANIO_FISCAL = "FB18";
    public static final String BUSCAR_INSUMO = "FB19";
    public static final String BUSCAR_METODO_ADQ = "FB20";
    public static final String BUSCAR_PLAN_DE_COMPRAS = "FB21";
    public static final String BUSCAR_REQUERIMIENTO_FONDO_A_CED = "FB22";
    public static final String BUSCAR_FACTURAS = "FB24";
    public static final String BUSCAR_PAGOS = "FB25";
    public static final String BUSCAR_FUENTE_FIN = "FB26";
    public static final String BUSCAR_FUENTE_REC = "FB27";
    public static final String BUSCAR_TRANSFERENCIAS_GLOBAL_DEP = "FB28";
    public static final String BUSCAR_CONCILIACION_BANCARIA = "FB29";
    public static final String BUSCAR_DETALLE_DESEMBOLSO = "FB30";
    public static final String BUSCAR_DESEMBOLSO = "FB31";
    public static final String BUSCAR_DESEMBOLSO_CED = "FB32";
    public static final String BUSCAR_AUTORIZACION_EDICION_PRESUPUESTO = "FB34";
    public static final String BUSCAR_LIQUIDACION = "FB33";

    public static final String BUSCAR_MOVIMIENTOS_LIQUIDACION = "FB35";
    public static final String BUSCAR_LIQUIDACION_OTRO_INGRESO = "FB36";
    public static final String BUSCAR_MOVIMIENTOS_LIQUIDACION_OTROS = "FB37";
    public static final String BUSCAR_EVALUACION_LIQUIDACION = "FB38";
    public static final String BUSCAR_CHERQUERA = "FB39";
    public static final String BUSCAR_EVALUACION_ITEM_LIQUIDACION = "FB40";
    public static final String BUSCAR_EVALUACION_LIQUIDACION_OTRO = "FB41";
    public static final String BUSCAR_EVALUACION_ITEM_LIQUIDACION_OTRO = "FB42";
    public static final String BUSCAR_PROVEEDOR = "FB43";

    //CRUD PRESUPUESTOS
    public static final String CREAR_PRESUPUESTOS_ESCOLARES = "F1";
    public static final String ACTUALIZAR_PRESUPUESTOS_ESCOLARES = "F2";
    public static final String ELIMINAR_PRESUPUESTOS_ESCOLARES = "F3";

    //CRUD MOVIMIENTOS
    public static final String CREAR_MOVIMIENTO_PRESUPUESTO = "F4";
    public static final String ACTUALIZAR_MOVIMIENTO_PRESUPUESTO = "F5";
    public static final String ELIMINAR_MOVIMIENTO_PRESUPUESTO = "F6";

    //CRUD DESEMBOLSOS
    public static final String CREAR_DESEMBOLSO = "F26";
    public static final String ACTUALIZAR_DESEMBOLSO = "F27";
    public static final String ELIMINAR_DESEMBOLSO = "F28";

    public static final String CREAR_DIRECCION_DEPARTAMENTAL = "F7";
    public static final String ACTUALIZAR_DIRECCION_DEPARTAMENTAL = "F8";
    public static final String ELIMINAR_DIRECCION_DEPARTAMENTAL = "F9";

    //CRUD TRANSFERENCIAS
    public static final String CREAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL = "F10";
    public static final String ACTUALIZAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL = "F11";
    public static final String ELIMINAR_TRANSFERENCIA_DIRECCION_DEPARTAMENTAL = "F12";

    //CRUD MOVIMIENTOS
    public static final String CREAR_DOCUMENTOS = "F13";
    public static final String ACTUALIZAR_DOCUMENTOS = "F14";
    public static final String ELIMINAR_DOCUMENTOS = "F15";
    //CRUD CUENTAS BANCARIAS
    public static final String CREAR_CUENTAS_BANCARIAS = "F16";
    public static final String ACTUALIZAR_CUENTAS_BANCARIAS = "F17";
    public static final String ELIMINAR_CUENTAS_BANCARIAS = "F18";

    //CRUD MOVIMIENTOS DE CUENTAS BANCARIAS
    public static final String CREAR_MOVIMIENTO_CUENTA_BANCARIA = "F19";
    public static final String ACTUALIZAR_MOVIMIENTO_CUENTA_BANCARIA = "F78";

    //CRUD TRANSFERENCIAS A CENTROS EDUCATIVOS
    public static final String CREAR_TRANSFERENCIAS_A_CED = "F20";
    public static final String ACTUALIZAR_TRANSFERENCIAS_A_CED = "F21";
    public static final String ELIMINAR_TRANSFERENCIAS_A_CED = "F22";

    //CRUD SOLICITUDES DE TRANSFERENCIAS
    public static final String CREAR_SOLICITUD_TRANSFERENCIA = "F23";
    public static final String ACTUALIZAR_SOLICITUD_TRANSFERENCIA = "F24";
    public static final String ELIMINAR_SOLICITUD_TRANSFERENCIA = "F25";
    public static final String BUSCAR_SOLICITUD_TRANSFERENCIA = "F26";

    //CRUD RECIBOS
    public static final String CREAR_RECIBOS = "F23";
    public static final String ACTUALIZAR_RECIBOS = "F24";
    public static final String ELIMINAR_RECIBOS = "F25";

    //CRUD DE SOLICITUDES DE TRANSFERENCIAS
    public static final String CREAR_SOLICITUD_DE_TRANSFERENCIA = "F33";
    public static final String ACTUALIZAR_SOLICITUD_DE_TRANSFERENCIA = "F34";
    public static final String ELIMINAR_SOLICITUD_DE_TRANSFERENCIA = "F35";

    //CRUD DE CUENTAS BANCARIAS DE CAJA CHICA PARA SEDES
    public static final String CREAR_CUENTASBANCARIASCC = "F36";
    public static final String ACTUALIZAR_CUENTASBANCARIASCC = "F37";
    public static final String ELIMINAR_CUENTASBANCARIASCC = "F38";

    //CRUD MOVIMIENTOS DE CAJA CHICA
    public static final String CREAR_MOVIMIENTOSCAJACHICA = "F39";

    //CRUD DE REQUERIMIENTOS DE FONDOS A CED
    public static final String CREAR_REQUERIMIENTO_FONDO_A_CED = "F43";
    public static final String ACTUALIZAR_REQUERIMIENTO_FONDO_A_CED = "F44";
    public static final String ELIMINAR_REQUERIMIENTO_FONDO_A_CED = "F45";

    //CRUD PLAN DE COMRAS
    public static final String CREAR_PLAN_DE_COMPRAS = "F40";
    public static final String ACTUALIZAR_PLAN_DE_COMPRAS = "F41";
    public static final String ELIMINAR_PLAN_DE_COMPRAS = "F42";

    //Control vista
    public static final String CAMBIAR_PRESUPUESTO_ESTADO_FORMULADO = "FP1";
    public static final String CAMBIAR_PRESUPUESTO_ESTADO_EN_AJUSTE = "FP2";
    public static final String CAMBIAR_PRESUPUESTO_ESTADO_APROBADO = "FP3";
    public static final String CAMBIAR_PRESUPUESTO_ESTADO_APROBADO_MODIFICADO = "FP4";
    public static final String AUTORIZAR_EDICION_PRESUPUESTO = "FP7";
    public static final String OBSERVAR_PLAN_COMPRAS = "FP8";
    public static final String APROBAR_PLAN_COMPRAS = "FP9";
    public static final String AJUSTE_PLAN_COMPRAS = "FP10";
    public static final String CAMBIAR_DATOS_PROVEEDOR = "FP13";
    public static final String EVALUAR_PRESUPUESTO = "FP14";
    public static final String OBSERVAR_PRESUPUESTO = "FP15";
    public static final String CERRAR_PRESUPUESTO = "FP16";

    //CRUD FACTURAS
    public static final String CREAR_FACTURAS = "F47";
    public static final String ACTUALIZAR_FACTURAS = "F48";
    public static final String ELIMINAR_FACTURAS = "F49";
    public static final String ANULAR_FACTURAS_RECIBOS = "F53";

    //Pagos
    public static final String CREAR_PAGOS = "F50";
    public static final String ACTUALIZAR_PAGOS = "F51";
    public static final String ELIMINAR_PAGOS = "F52";

    //CRUD PROVEEDORES
    public static final String CREAR_PROVEEDOR = "F54";
    public static final String ACTUALIZAR_PROVEEDOR = "F55";
    public static final String ELIMINAR_PROVEEDOR = "F56";

    //CRUD CONCILIACIONE BANCARIAS
    public static final String CREAR_CONCILIACION_BANCARIA = "F60";
    public static final String ACTUALIZAR_CONCILIACION_BANCARIA = "F61";
    public static final String ELIMINAR_CONCILIACION_BANCARIA = "F62";

    //CRUD EDICION PRESUPUESTO APROBADO
    public static final String CREAR_EDICION_PRESPUESTO_ESCOLAR_APROBADO = "F63";
    public static final String ACTUALIZAR_EDICION_PRESPUESTO_ESCOLAR_APROBADO = "F64";
    public static final String ELIMINAR_EDICION_PRESPUESTO_ESCOLAR_APROBADO = "F65";

    //CRUD DE DETALLE DE DESEMBOLSOS
    public static final String CREAR_DETALLE_DESEMBOLSO = "F66";
    public static final String ACTUALIZAR_DETALLE_DESEMBOLSO = "F67";
    public static final String ELIMINAR_DETALLE_DESEMBOLSO = "F68";

    //CRUD DE DESEMBOLSO A CED
    public static final String CREAR_DESEMBOLSO_CED = "F69";
    public static final String ACTUALIZAR_DESEMBOLSO_CED = "F70";
    public static final String ELIMINAR_DESEMBOLSO_CED = "F71";

    //CRUD DE LIQUIDACION
    public static final String CREAR_LIQUIDACION = "F72";
    public static final String ACTUALIZAR_LIQUIDACION = "F73";
    public static final String ELIMINAR_LIQUIDACION = "F74";

    //OPERACIONES AUTORIZACIONES EDICION PRESUPUESTOS
    public static final String CREAR_AUTORIZACION_EDICION_PRESUPUESTO = "F75";
    public static final String ACTUALIZAR_AUTORIZACION_EDICION_PRESUPUESTO = "F76";
    public static final String ELIMINAR_AUTORIZACION_EDICION_PRESUPUESTO = "F77";

    //CRUD DE MOVIMIENTOS DE LIQUIDACION
    public static final String CREAR_MOVIMIENTOS_LIQUIDACION = "F79";
    public static final String ACTUALIZAR_MOVIMIENTOS_LIQUIDACION = "F80";
    public static final String ELIMINAR_MOVIMIENTOS_LIQUIDACION = "F81";

    //CRUD DE LIQUIDACION DE OTROS INGRESOS
    public static final String CREAR_LIQUIDACION_OTRO_INGRESO = "F82";
    public static final String ACTUALIZAR_LIQUIDACION_OTRO_INGRESO = "F83";
    public static final String ELIMINAR_LIQUIDACION_OTRO_INGRESO = "F84";

    //CRUD DE MOVIMIENTOS DE LIQUIDACION DE OTROS INGRESOS
    public static final String CREAR_MOVIMIENTOS_LIQUIDACION_OTROS = "F85";
    public static final String ACTUALIZAR_MOVIMIENTOS_LIQUIDACION_OTROS = "F86";
    public static final String ELIMINAR_MOVIMIENTOS_LIQUIDACION_OTROS = "F87";

    //CRUD DE EVALUACION DE LIQUIDACION
    public static final String CREAR_EVALUACION_LIQUIDACION = "F88";
    public static final String ACTUALIZAR_EVALUACION_LIQUIDACION = "F89";
    public static final String ELIMINAR_EVALUACION_LIQUIDACION = "F90";

    //CRUD CHEQUERAS
    public static final String CREAR_CHEQUERA = "F91";
    public static final String ACTUALIZAR_CHEQUERA = "F92";
    public static final String ELIMINAR_CHEQUERA = "F93";

    //CRUD EVALUACION ITEM LIQUIDACION
    public static final String CREAR_EVALUACION_ITEM_LIQUIDACION = "F94";
    public static final String ACTUALIZAR_EVALUACION_ITEM_LIQUIDACION = "F95";
    public static final String ELIMINAR_EVALUACION_ITEM_LIQUIDACION = "F96";

    //CRUD EVALUACION LIQUIDACION OTROS
    public static final String CREAR_EVALUACION_LIQUIDACION_OTRO = "F97";
    public static final String ACTUALIZAR_EVALUACION_LIQUIDACION_OTRO = "F98";
    public static final String ELIMINAR_EVALUACION_LIQUIDACION_OTRO = "F99";

    //CRUD EVALUACION ITEM LIQUIDACION OTROS
    public static final String CREAR_EVALUACION_ITEM_LIQUIDACION_OTRO = "F100";
    public static final String ACTUALIZAR_EVALUACION_ITEM_LIQUIDACION_OTRO = "F101";
    public static final String ELIMINAR_EVALUACION_ITEM_LIQUIDACION_OTRO = "F102";

}
