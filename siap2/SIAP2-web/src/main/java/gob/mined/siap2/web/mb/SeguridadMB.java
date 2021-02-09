/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author sofis
 */
@Named(value = "seguridadMB")
@SessionScoped
public class SeguridadMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    PermisosMB menuMB;

    private HashMap<String, String> permisosPaginas = new HashMap<>();

    @PostConstruct
    public void init() {
        permisosPaginas.put("inicio", "INICIO");
        permisosPaginas.put("consultaTexto", "CONSULTA_TEXTO");
        permisosPaginas.put("consultaOperacion", "CONSULTA_OPERACION");
        permisosPaginas.put("consultaConfiguracion", "CONSULTA_CONFIGURACION");
        permisosPaginas.put("consultaLogAuditoria", "CONSULTA_LOG_AUDITORIA");
        permisosPaginas.put("consultaPlantillasCorreo", "CONSULTA_PLANTILLAS_CORREO");
        permisosPaginas.put("consultaUsuarios", "CONSULTA_USUARIOS");
        permisosPaginas.put("consultaRoles", "CONSULTA_ROLES");

        permisosPaginas.put("consultaPlanificacionEstrategica", "CONSULTA_PLANIFICACION_ESTRATEGICA");
        permisosPaginas.put("crearEditarPlanificacionEstrategica", "CREAR_EDITAR_PLANIFICACION_ESTRATEGICA");
        permisosPaginas.put("verPlanificacionEstrategica", "VER_PLANIFICACION_ESTRATEGICA");

        permisosPaginas.put("consultaLineaEstrategica", "CONSULTA_LINEA_ESTRATEGICA");
        permisosPaginas.put("crearEditarLineaEstrategica", "CREAR_EDITAR_LINEA_ESTRATEGICA");
        permisosPaginas.put("verLineaEstrategica", "VER_LINEA_ESTRATEGICA");

        permisosPaginas.put("consultaProgramaInstitucional", "CONSULTA_PROGRAMA_INSTITUCIONAL");
        permisosPaginas.put("crearEditarProgramaInstitucional", "CREAR_EDITAR_PROGRAMA_INSTITUCIONAL");
        permisosPaginas.put("verProgramaInstitucional", "VER_PROGRAMA_INSTITUCIONAL");

        permisosPaginas.put("consultaAsignacionesNoProgramables", "CONSULTA_ASIGNACIONES_NO_PROGRAMABLES");
        permisosPaginas.put("crearEditarAsignacionNoProgramable", "CREAR_EDITAR_ASIGNACION_NP");
        permisosPaginas.put("verAsignacionNoProgramable", "VER_ASIGNACION_NP");

        permisosPaginas.put("consultaAccionCentral", "CONSULTA_ACCION_CENTRAL");
        permisosPaginas.put("crearEditarAccionCentral", "CREAR_EDITAR_ACCION_CENTRAL");
        permisosPaginas.put("verAccionCentral", "VER_ACCION_CENTRAL");

        permisosPaginas.put("consultaProgramaPresupuestario", "CONSULTA_PROGRAMA_PRESUPUESTARIO");
        permisosPaginas.put("crearEditarProgramaPresupuestario", "CREAR_EDITAR_PROGRAMA_PRESUPUESTARIO");
        permisosPaginas.put("verProgramaPresupuestario", "VER_PROGRAMA_PRESUPUESTARIO");

        permisosPaginas.put("consultaCatalogoDeIndicadores", "CONSULTA_CATALOGO_DE_INDICADORES");
        permisosPaginas.put("crearEditarCatalogoDeIndicadores", "CREAR_EDITAR_CATALOGO_DE_INDICADORES");
        permisosPaginas.put("verCatalogoDeIndicadores", "VER_CATALOGO_DE_INDICADORES");

        permisosPaginas.put("consultaProgramaIndicador", "CONSULTA_PROGRAMA_INDICADOR");
        permisosPaginas.put("crearEditarProgramaIndicador", "CREAR_EDITAR_PROGRAMA_INDICADOR");
        permisosPaginas.put("verProgramaIndicador", "VER_PROGRAMA_INDICADOR");

        permisosPaginas.put("consultaProyecto", "CONSULTA_PROYECTO");
        permisosPaginas.put("crearEditarProyecto", "CREAR_EDITAR_PROYECTO");
        permisosPaginas.put("verProyecto", "VER_PROYECTO");

        permisosPaginas.put("consultaTechoPresupuestarioGOES", "CONSULTA_TECHO_PRESUPUESTARIO_GOES");
        permisosPaginas.put("crearEditarTechoPresupuestarioGOES", "CREAR_EDITAR_TECHO_PRESUPUESTARIO_GOES");
        permisosPaginas.put("verTechoPresupuestarioGOES", "VER_TECHO_PRESUPUESTARIO_GOES");

        permisosPaginas.put("consultaCatalogoDeInsumos", "CONSULTA_CATALOGO_DE_INSUMOS");
        permisosPaginas.put("crearEditarCatalogoDeInsumos", "CREAR_EDITAR_CATALOGO_DE_INSUMOS");
        permisosPaginas.put("verCatalogoDeInsumos", "VER_CATALOGO_DE_INSUMOS");
        permisosPaginas.put("consultaObjetosEspecificosGasto", "CONSULTAR_CATALOGO_OBJETOS_ESPECFICOS_GASTO");

        
        permisosPaginas.put("consultaPOGParaProyectos", "CONSULTA_POG_PARA_PROYECTOS");
        permisosPaginas.put("crearEditarPOGParaProyectos", "CREAR_EDITAR_POG_PARA_PROYECTOS");
        permisosPaginas.put("verPOGParaProyectos", "VER_POG_PARA_PROYECTOS");

        permisosPaginas.put("consultaCatalogoDeProductos", "CONSULTA_CATALOGO_DE_PRODUCTOS");
        permisosPaginas.put("crearEditarCatalogoDeProductos", "CREAR_EDITAR_CATALOGO_DE_PRODUCTOS");
        permisosPaginas.put("verCatalogoDeProductos", "VER_CATALOGO_DE_PRODUCTOS");

        permisosPaginas.put("consultaFormulacionPorAnioFiscal", "CONSULTA_FORMULACION_POR_ANIO_FISCAL");
        permisosPaginas.put("crearEditarFormulacionPorAnioFiscal", "CREAR_EDITAR_FORMULACION_POR_ANIO_FISCAL");
        permisosPaginas.put("verFormulacionPorAnioFiscal", "VER_FORMULACION_POR_ANIO_FISCAL");

        permisosPaginas.put("cambioContrasenia", "CAMBIAR_MI_CONTRASENIA");

        permisosPaginas.put("consultaPOAAccionCentral", "CONSULTA_POA_ACCION_CENTRAL");
        permisosPaginas.put("crearEditarPOAAccionCentral", "CREAR_EDITAR_POA_ACCION_CENTRAL");
        permisosPaginas.put("verPOAAccionCentral", "VER_POA_ACCION_CENTRAL");
        permisosPaginas.put("validarPOAAccionCentral", "VALIDAR_POA_ACCION_CENTRAL");

        permisosPaginas.put("consultaPOAAsignacionesNoProgramables", "CONSULTA_POA_ASIGNACIONES_NO_PROGRAMABLES");
        permisosPaginas.put("crearEditarPOAAsignacionesNoProgramables", "CREAR_EDITAR_POA_ASIGNACIONES_NO_PROGRAMABLES");
        permisosPaginas.put("verPOAAsignacionesNoProgramables", "VER_POA_ASIGNACIONES_NO_PROGRAMABLES");
        permisosPaginas.put("validarPOAAsignacionNoProgramable", "VALIDAR_POA_ASIGNACION_NO_PROGRAMABLE");

        permisosPaginas.put("consultaPOAProyecto", "CONSULTA_POA_PROYECTO");
        permisosPaginas.put("crearEditarPOAProyecto", "CREAR_EDITAR_POA_PROYECTO");
        permisosPaginas.put("verPOAProyecto", "VER_POA_PROYECTO");
        permisosPaginas.put("validarPOAProyecto", "VALIDAR_POA_PROYECTO");
        permisosPaginas.put("asociarLineasPOAProyecto", "ASOCIAR_LINEAS_POA_PROYECTO");
        permisosPaginas.put("colaborarPOAProyecto", "COLABORAR_POA_PROYECTO");

        permisosPaginas.put("consultaNotificaciones", "CONSULTA_NOTIFICACIONES");

        permisosPaginas.put("consultaPOAporACoANP", "CONSOLIDADO_POA_AC_ANP");
        permisosPaginas.put("consolidadoPOAporACoANP", "CONSOLIDADO_POA_AC_ANP");
        permisosPaginas.put("consultaPOAporProyecto", "CONSOLIDADO_POA_PROYECTO");
        permisosPaginas.put("consolidadoPOAporProyecto", "CONSOLIDADO_POA_PROYECTO");
        //VER_LINEA_TRABAJO_ADMINISTRACION

        permisosPaginas.put("consultaPlantillaDeInsumos", "CONSULTA_PLANTILLA_DE_INSUMOS");
        permisosPaginas.put("crearEditarPlantillaDeInsumos", "CREAR_EDITAR_PLANTILLA_DE_INSUMOS");
        permisosPaginas.put("verPlantillaDeInsumos", "VER_PLANTILLA_DE_INSUMOS");

        permisosPaginas.put("consultaPAC", "CONSULTA_PAC");
        permisosPaginas.put("crearEditarPAC", "CREAR_EDITAR_PAC");
        permisosPaginas.put("verPAC", "VER_PAC");
        permisosPaginas.put("crearEditarPACGrupo", "CREAR_EDITAR_PAC_GRUPO");
        permisosPaginas.put("reporteInsumosPAC", "REPORTE_INSUMOS_PAC");

        permisosPaginas.put("consultaMetodosAdquisicion", "CONSULTA_METODOS_DE_ADQUISICION");
        permisosPaginas.put("crearEditarMetodosAdquisicion", "CREAR_EDITAR_METODOS_DE_ADQUISICION");
        permisosPaginas.put("verMetodosAdquisicion", "VER_METODOS_DE_ADQUISICION");

        permisosPaginas.put("crearEditarProcesoAdquisicion", "CREAR_EDITAR_PROCESO_ADQUISICION");
        permisosPaginas.put("consultaProcesoAdquisicion", "CONSULTA_PROCESO_ADQUISICION");
        permisosPaginas.put("consultaTiemposProcesoAdquisicion", "CONSULTA_TIEMPOS_PROCESO_ADQUISICION");
        permisosPaginas.put("verProcesoAdquisicion", "VER_PROCESO_ADQUISICION");

        permisosPaginas.put("consultaContratoOC", "CONSULTA_CONTRATO_OC");
        permisosPaginas.put("crearEditarContratoOC", "CREAR_EDITAR_CONTRATO_OC");
        permisosPaginas.put("verContratoOC", "VER_CONTRATO_OC");

        permisosPaginas.put("crearEditarFlujoCajaMensual", "CREAR_EDITAR_FLUJO_CAJA_MENSUAL");

        permisosPaginas.put("consultaCodigueraCategoria", "CODIGUERA_CATEGORIA");
        permisosPaginas.put("consultaCodigueraCategoriaConvenio", "CODIGUERA_CATEGORIA_CONVENIO");
        permisosPaginas.put("consultaConvenio", "CODIGUERA_CONVENIO");
        permisosPaginas.put("crearEditarConvenio", "CREAR_EDITAR_CONVENIO");
        permisosPaginas.put("verConvenio", "VER_CONVENIO");

        permisosPaginas.put("cargaTechosProgramaPresupuestario", "CARGA_TECHOS_PROGRAMAS_PRESUPUESTARIOS");
        permisosPaginas.put("verTechosProgramaPresupuestario", "VER_TECHOS_PROGRAMAS_PRESUPUESTARIOS");

        permisosPaginas.put("cargaTechosAccionesCentrales", "CARGA_TECHOS_ACCIONES_CENTRALES");
        permisosPaginas.put("verTechosAccionesCentrales", "VER_TECHOS_ACCIONES_CENTRALES");

        permisosPaginas.put("cargaTechosAsignacionNoProgramables", "CARGA_TECHOS_ASIGNACIONES_NO_PROGRAMABLES");
        permisosPaginas.put("verTechosAsignacionNoProgramables", "VER_TECHOS_ASIGNACIONES_NO_PROGRAMABLES");

        //Unidades tecnicas
        permisosPaginas.put("consultaUnidadTecnica", "CONSULTA_UNIDAD_TECNICA");
        permisosPaginas.put("crearUnidadTecnica", "CREAR_UNIDAD_TECNICA");
        permisosPaginas.put("verUnidadTecnica", "VER_UNIDAD_TECNICA");
        
        //Tipos de credito
        permisosPaginas.put("consultaTipoCredito", "TIPOS_DE_CREDITO");
        
        //Areas de inversion
        permisosPaginas.put("consultaAreasInversion", "AREAS_DE_INVERSION");
        permisosPaginas.put("crearEditarAreasInversion", "CREAR_AREAS_DE_INVERSION");
        permisosPaginas.put("crearEditarSubAreasInversion", "CREAR_SUB_AREAS_DE_INVERSION");
        permisosPaginas.put("agregarInsumoSubAreasInversionDesdeArchivo", "AGREGAR_INSUMOS_SUBAREAS_DE_INVERSION");
        //Cuentas
        permisosPaginas.put("consultaCuentas", "CUENTAS");
        permisosPaginas.put("crearEditarCuentas", "CREAR_EDITAR_CUENTAS");
        permisosPaginas.put("crearEditarSubCuentas", "CREAR_EDITAR_SUB_CUENTAS");

        //Unidades Operativas
        //POA
        permisosPaginas.put("consultaPOA", "CONSULTA_PLAN_OPERATIVO_ANUAL");
        permisosPaginas.put("crearEditarPlanOperativoAnual", "CREAR_EDITAR_PLAN_OPERATIVO_ANUAL");
        permisosPaginas.put("verPlanOperativoAnual", "VER_PLAN_OPERATIVO_ANUAL");
        permisosPaginas.put("validarPlanOperativoAnual", "EVALUAR_PLAN_OPERATIVO_ANUAL");
        //Seguimiento POA
        permisosPaginas.put("consultaSeguimientoPOA", "CONSULTA_SEGUIMIENTO_PLAN_OPERATIVO_ANUAL");
        permisosPaginas.put("seguimientoPOA", "CONSULTA_SEGUIMIENTO_PLAN_OPERATIVO_ANUAL");
        permisosPaginas.put("consultaProgramacionTrimestral", "PROGRAMACION_TRIMESTRAL_PLAN_OPERATIVO_ANUAL");
        permisosPaginas.put("verMetasDeIndicadoresPOA", "VER_METAS_DE_INDICADORES_POA");
        permisosPaginas.put("verValoresDeIndicadoresPOA", "VER_VALORES_DE_INDICADORES_POA");
        permisosPaginas.put("consultaRiesgosPOA", "VER_MATRIZ_DE_RIESGOS_POA");
        permisosPaginas.put("verSeguimientoDeIndicadoresPOA", "VER_SEGUIMIENTO_INDICADORES_POA");
        
        //EMITIR_REPORTE_PEP
        permisosPaginas.put("consultaFuenteFinanciamiento", "CODIGUERA_FUENTE_FINANCIAMIENTO");
        permisosPaginas.put("consultaFuenteRecursos", "CODIGUERA_FUENTE_RECURSO");
        permisosPaginas.put("consultaIndicadorFormaMedicion", "CODIGUERA_INDICADOR_MEDICION");
        permisosPaginas.put("consultaMacroActividad", "CODIGUERA_MACRO_ACTIVIDAD");
        permisosPaginas.put("consultaNormativa", "CODIGUERA_NORMATIVA");
        permisosPaginas.put("consultaActividadPOProyecto", "CODIGUERA_ACTIVIDAD_PO_PROYECTO");

        permisosPaginas.put("cargaProgramacionFinancieraAccionCentral", "CARGA_PROGRAMACION_FINANCIERA_ACCION_CENTRAL");
        permisosPaginas.put("cargaProgramacionFinancieraAsignacionNoProgramable", "CARGA_PROGRAMACION_FINANCIERA_ASIGNACION_NO_PROGRAMABLE");
        permisosPaginas.put("verProgramacionFinancieraAccionCentral", "VER_PROGRAMACION_FINANCIERA_ACCION_CENTRAL");
        permisosPaginas.put("verProgramacionFinancieraAsignacionNoProgramable", "VER_PROGRAMACION_FINANCIERA_ASIGNACION_NO_PROGRAMABLE");

        permisosPaginas.put("politicaContrasenia", "POLITICA_CONTRASENIA");

        permisosPaginas.put("consultaTipoDocumento", "CODIGUERA_TIPO_DOCUEMNTO");
        permisosPaginas.put("consultaTextosAyuda", "CODIGUERA_TEXTOS_AYUDA");

        permisosPaginas.put("cargaTechosProyecto", "CARGA_TECHOS_PROYECTOS");
        permisosPaginas.put("verTechosProyecto", "VER_TECHOS_PROYECTOS");

        permisosPaginas.put("consultaCodigueraUnidadDeMedida", "CODIGUERA_UNIDAD_MEDIDA");

        permisosPaginas.put("verPOGParaProyectos", "VER_POG_PROYECTO");

        permisosPaginas.put("consultaPeriodoDeSeguimientoDeIndicadores", "CONSULTA_PERIODO_SEGUIMIENTO_INDICADORES");
        permisosPaginas.put("crearEditarPeriodoDeSeguimientoDeIndicadores", "CREAR_EDITAR_PERIODO_SEGUIMIENTO_INDICADORES");
        permisosPaginas.put("verPeriodoDeSeguimientoDeIndicadores", "VER_PERIODO_SEGUIMIENTO_INDICADORES");

        permisosPaginas.put("cargaValoresDeIndicadores", "CARGA_VALORES_INDICADORES");
        permisosPaginas.put("cargaMetasDeIndicadores", "CARGA_METAS_INDICADORES");

        permisosPaginas.put("validarCertificadoDisponibilidadPresupuestaria", "VALIDAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA");

        permisosPaginas.put("descertificarMontoFuenteInsumo", "DESCERTIFICAR_MONTO_FUENTE_INSUMO");

        permisosPaginas.put("verMetasDeIndicadores", "VER_METAS_DE_INDICADORES");
        permisosPaginas.put("verValoresDeIndicadores", "VER_VALORES_DE_INDICADORES");
        permisosPaginas.put("consultaRiesgos", "VER_METAS_DE_INDICADORES");

        permisosPaginas.put("verConsolidadoPOAporProyecto", "VER_CONSOLIDADO_PORA_POR_PROYECTO");
        permisosPaginas.put("verConsolidadoPOAporACoANP", "VER_CONSOLIDADO_PORA_ACCION_CENTRAL_ASIG_NP");

        permisosPaginas.put("consolidarPOGParaProyectos", "CONSOLIDAR_POG_PROYECTO");

        permisosPaginas.put("consultaImpuesto", "GESTIONAR_IMPUESTOS");

        permisosPaginas.put("validarImpuestosContrato", "VALIDAR_IMPUESTO_CONTRATO");

        permisosPaginas.put("consultaActasPago", "CONSULTA_ACTAS_DE_PAGO");
        permisosPaginas.put("verActaDePago", "VER_ACTAS_DE_PAGO");

        permisosPaginas.put("emisionComprobanteRetencionImpuesto", "EMITIR_COMPROBANTE_RETENCION_IMPUESTO");

        permisosPaginas.put("crearEditarComprobanteRetencionImpuestos", "EMITIR_COMPROBANTE_RETENCION_IMPUESTO_GENERAR");

        permisosPaginas.put("retencionesDeImpuestosPorProyecto", "RETENCIONES_DE_IMPUESTOS_POR_PROYECTO");

        permisosPaginas.put("retencionDeImpuestosPorProveedor", "RETENCIONES_DE_IMPUESTOS_POR_PROVEEDOR");

        permisosPaginas.put("constanciaAnualDeRetencionPorProveedor", "CONSTANCIA_ANUAL_RETENCION_IMPUESTO_POR_PROVEEDOR");

        permisosPaginas.put("consultaPlaLineasMetasIndicadores", "CONSULTA_PLA_LINEAS_METAS_INDICADORES");
        permisosPaginas.put("consultaLineasMetasIndicadores", "CONSULTA_LINEAS_METAS_INDICADORES");

        permisosPaginas.put("consultaEstadoInsumos", "CONSULTA_ESTADO_INSUMOS");

        permisosPaginas.put("consultaPoliza", "CONSULTA_POLIZAS");
        permisosPaginas.put("crearEditarPoliza", "CREAR_EDITAR_POLIZA");

        permisosPaginas.put("verReportePOAProyectoAdministrativo", "VER_REPORTE_POA_PROYECTO_ADMINISTRATIVO");

        permisosPaginas.put("reportePOIProyectosAdministrativos", "VER_REPORTE_POI_PROYECTO_ADMINISTRATIVO");
        permisosPaginas.put("reportePOIProyectosAdministrativosPrgInstitucional", "VER_REPORTE_POI_PROYECTO_ADMINISTRATIVO_PRG_INSTITUCIONAL");
        permisosPaginas.put("reporteSeguimientoProyectoAministrativo", "VER_REPORTE_SEGUIMIENTO_PROYECTO_ADMINISTRATIVO");
        permisosPaginas.put("consultaProveedores", "CONSULTA_PROVEEDORES");
        permisosPaginas.put("verProveedor", "VER_PROVEEDOR");

        permisosPaginas.put("consultaCompromisoPresupuestario", "CONSULTA_COMPROMISO_PRESUPUESTARIO");
        permisosPaginas.put("validarCompromisoPresupuestario", "VALIDAR_COMPROMISO_PRESUPUESTARIO");

        permisosPaginas.put("reprogramacionAccionCentral", "REPROGRAMACION_ACCION_CENTRAL_CE");
        permisosPaginas.put("consultaReprogramacionAccionCentral", "REPROGRAMACION_ACCION_CENTRAL_CONSULTA");
        permisosPaginas.put("consultaReprogramacionAsignacionNoProgramable", "REPROGRAMACION_ASIGNACION_NO_PROGRAMABLE_CONSULTA");
        permisosPaginas.put("reprogramacionAsignacionNoProgramable", "REPROGRAMACION_ASIGNACION_NO_PROGRAMABLE_CE");
        permisosPaginas.put("consultaReprogramacionProyecto", "REPROGRAMACION_PROYECTOS_CONSULTA");
        permisosPaginas.put("reprogramacionProyecto", "REPROGRAMACION_PROYECTO_CE");
        permisosPaginas.put("resumenClasificadorFuncionalGasto", "RESUMEN_CLASIFICADOR_FUNCIONAL");
        permisosPaginas.put("cierreTesoreria", "CIERRE_TESORERIA");
        permisosPaginas.put("cierrePlanficacion", "CIERRE_PLANIFICACION");
        permisosPaginas.put("cierrePresupuesto", "CIERRE_PRESUPUESTO");
        permisosPaginas.put("cierreInversion", "CIERRE_INVERSION");

        permisosPaginas.put("importarInfCompromiso", "IMPORTAR_INF_COMPROMISO");
        permisosPaginas.put("verInformacionCompromiso", "VER_INFORMACION_COMPROMISO");
        permisosPaginas.put("consultaDatosSafi", "CONSULTA_DATOS_SAFI");
        permisosPaginas.put("verSafi", "VER_SAFI");

        permisosPaginas.put("resumenClasificadorFuncionalGasto", "RESUMEN_PRESUPUESTARIO");

        permisosPaginas.put("consultaCertificadoDisponibilidadPresupuestaria", "CONSULTA_CERTIFICADO_DIPONIBILIDAD_PRESUPUESTARIA");
        permisosPaginas.put("enviarCertificadoDispPorProceso", "ENVIAR_CERTIFICADO_PRES_POR_PROCESO");
        permisosPaginas.put("validarCertificadoDispPorProceso", "VALIDAR_CERTIFICADO_PRES_POR_PROCESO");
        permisosPaginas.put("seguimientoCronogramaInsumos", "SEGUIMIENTO_CRONOGRAMA_INSUMOS");

        permisosPaginas.put("consultaCertificadosDisponibilidadPresupuestariaEmitidos", "CONSULTA_VALIDAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA");
        permisosPaginas.put("verValidarCertificadoDisponibilidadPresupuestaria", "VER_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA");        
        
        permisosPaginas.put("reenviarCertificadoDisponibilidadPresupuestaria", "ENVIAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA");
        
        permisosPaginas.put("consultaInsumosNoUACI", "CONSULTA_INSUMOS_NO_UACI");
        
        permisosPaginas.put("generacionReportePEP", "EMITIR_REPORTE_PEP");
        
        permisosPaginas.put("consultaPresupuesto", "CONSULTA_PRESUPUESTO");
        
        permisosPaginas.put("cronogramaDeRecursos", "CRONOGRAMA_RECURSOS");

        permisosPaginas.put("gestionPresupuestoEscolar", "COMPONENTE_GESTION_PRESUPUESTO_ESCOLAR");
        permisosPaginas.put("crearEditarGestionPresupuestoEscolar", "CREAR_EDITAR_GESTION_PRESUPUESTO_ESCOLAR");
        permisosPaginas.put("verSedesTechosPresupuesto", "CREAR_EDITAR_GESTION_PRESUPUESTO_ESCOLAR");
        permisosPaginas.put("consultaCategoriaPresupuestoEcolar", "CATEGORIA_PRESUPUESTO_ESCOLAR");
        permisosPaginas.put("consultaRubro", "RUBROS");
        permisosPaginas.put("consultaTiposTransferencia", "CONSULTA_TIPOS_TRANSFERENCIA");
        permisosPaginas.put("consultaSubRubro", "SUB_RUBROS");
        permisosPaginas.put("consultaTopePresupuestal", "TOPE_PRESUPUESTAL");
        permisosPaginas.put("consultaDetalleTopePresupuestal", "TOPE_PRESUPUESTAL");
        permisosPaginas.put("crearEditarTopePresupuestal", "CREAR_EDITAR_TOPE_PRESUPUESTAL");
        permisosPaginas.put("crearTopePresupuestalArchivo", "CREAR_EDITAR_TOPE_PRESUPUESTAL");
        permisosPaginas.put("consultaTotalPresupuestos", "CONSULTA_TOTAL_PRESUPUESTOS");
        permisosPaginas.put("consultaTotalTransferencias", "CONSULTA_TOTAL_TRANSFERENCIAS");
        permisosPaginas.put("consultaTransferencias", "CONSULTA_TRANSFERENCIAS");
        permisosPaginas.put("crearTransferenciasArchivo", "CREAR_EDITAR_TRANSFERENCIA");
        permisosPaginas.put("consultaTransferenciaComponente", "CONSULTA_TRANSFERENCIA_COMPONENTE");
        permisosPaginas.put("consultaDesembolsos", "CONSULTA_DESEMBOLSO");
        permisosPaginas.put("consultaTransferenciaComponentePrevisualizacion", "CONSULTA_TRANSFERENCIA_COMPONENTE_PREVISUALIZACION");
    }

    /**
     * Este método retorna el permiso que se tiene que tener para acceder a una pagina
     * 
     * @param pagina
     * @return 
     */
    private String getPermiso(String pagina) {
        return permisosPaginas.get(pagina);
    }

    /**
     * Este método retorna si el usuario tiene acceso autorizado a la página que esta intentando acceder.
     * 
     * @return 
     */
    public boolean getAccesoAutorizado() {

        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String path = ctx.getRequestServletPath();
        path = path.substring(0, path.lastIndexOf('.'));
        path = path.substring(path.lastIndexOf('/') + 1);

        //MenuMB menuMB = (MenuMB)JSFUtils.getBean("menuMB");
        Boolean tienePermiso = menuMB.getPermisos().get(getPermiso(path));
        return tienePermiso != null && tienePermiso;
    }
}