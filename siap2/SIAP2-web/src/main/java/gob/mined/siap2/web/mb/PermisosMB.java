/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.business.datatype.DataRolUsuario;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "permisosMB")
@SessionScoped
public class PermisosMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    private UsuarioDelegate usuDelegate;
    private HashMap<String, Boolean> permisos = new HashMap<>();
    private List<DataRolUsuario> roles;

    /**
     * Creates a new instance of MenuMB
     */
    public PermisosMB() {
    }

    @PostConstruct
    public void init() {
        menuAbierto = true;

        String usuario = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        permisos = new HashMap<>();
        roles = usuDelegate.obtenerOperacionesAgrupadasPorUT(usuario);

        for (DataRolUsuario rol : roles) {
            for (String s : rol.getCodigoOperaciones()) {
                permisos.put(s, Boolean.TRUE);
            }
        }

    }

    
    

    private boolean menuAbierto;

    /**
     * Cambia el estado del  menú, entre abierto o cerrado 
     */
    public void cambiarMenu() {
        menuAbierto = (!menuAbierto);
    }

    /**
     * Verifica si el usuario logueado tiene la operación pasada por parámetro
     * 
     * @param operacion
     * @return 
     */
    public boolean tieneOperacion(String operacion) {
        if (!permisos.containsKey(operacion) ) {
            return false;
        }
        return  permisos.get(operacion);
    }
    
    /**
     * Verifica si el usuario logueado tiene la operación en la UT pasada por parámetro
     * 
     * @param idUT
     * @param operacion
     * @return 
     */
    public boolean tieneOperacionEnUT(Integer idUT, String operacion) {
        for (DataRolUsuario rol: roles){
            if (Objects.equals(idUT, rol.getIdUnidadTecnica())){
                if (rol.getCodigoOperaciones().contains(operacion)){
                    return true;
                }
            } 
        }
        return false;
    }
    
    /**
     * Retorna una clase css, para usar en el menú dependiendo si se encuentra abierto o cerrado
     * 
     * @return 
     */
    public String getMenuCssClass() {
        if (menuAbierto) {
            return "";
        } else {
            return "toggled";
        }
    }

    public String fireAction(String codigo) {
        return codigo;
    }

    public UsuarioDelegate getUsuDelegate() {
        return usuDelegate;
    }

    public boolean isMenuAbierto() {
        return menuAbierto;
    }

    public void setMenuAbierto(boolean menuAbierto) {
        this.menuAbierto = menuAbierto;
    }

    public void setUsuDelegate(UsuarioDelegate usuDelegate) {
        this.usuDelegate = usuDelegate;
    }

    public HashMap<String, Boolean> getPermisos() {
        return permisos;
    }

    public void setPermisos(HashMap<String, Boolean> permisos) {
        this.permisos = permisos;
    }

    public String irAInicio() {
        return "IR_A_INICIO";
    }

    public String irATextos() {
        return "IR_A_TEXTOS";
    }

    public String irATextosAyuda() {
        return "IR_A_TEXTO_AYUDA";
    }

    public String irACambioContrasenia() {
        return "IR_A_CAMBIO_CONTRASENIA";
    }

    public String irAOperaciones() {
        return "IR_A_OPERACIONES";
    }

    public String irAConfiguracion() {
        return "IR_A_CONFIGURACION";
    }

    public String irAPlantillasCorreo() {
        return "IR_A_PLANTILLAS_CORREO";
    }

    public String irAUsuarios() {
        return "IR_A_USUARIOS";
    }

    public String irAPoliticaContrasenia() {
        return "IR_A_POLITICA_CONTRASENIA";
    }

    public String irALogAuditoria() {
        return "IR_A_LOG_AUDITORIA";
    }

    public String irARoles() {
        return "IR_A_ROLES";
    }

    public String irAPlanificacionEstrategica() {
        return "IR_A_PLANIFICACION_ESTRATEGICA";
    }

    public String irALineaEstrategica() {
        return "IR_A_LINEA_ESTRATEGICA";
    }

    public String irAProgramaInstitucional() {
        return "IR_A_PROGRAMA_INSTITUCIONAL";
    }

    public String irAAccionCentral() {
        return "IR_A_LINEA_ACCION_CENTRAL";
    }

    public String irAAsignacionesNoProgrmables() {
        return "IR_A_ASIGNACIONES_NO_PROGRAMABLES";
    }

    public String irAProgramaPresupuestario() {
        return "IR_A_PROGRAMA_PRESUPUESTARIO";
    }

    public String irACatalogoDeIndicadores() {
        return "IR_A_CATALOGO_DE_INDICADORES";
    }

    public String irAImpuestos() {
        return "IR_A_IMPUESTOS";
    }

    public String irAProgramaIndicador() {
        return "IR_A_PROGRAMA_INDICADOR";
    }

    public String irAProyectoAdministrativo() {
        FacesContext.getCurrentInstance()
            .getExternalContext()
            .getFlash()
            .put("restriccionXTipoProyecto", "1");
        return "consultaProyecto.xhtml?faces-redirect=true&restriccionXTipoProyecto=1";
    }

    public String irAReportePOIProyectosAdministrativos() {
        return "IR_A_REPORTE_POI_PROYECTOS_ADMINISTRATIVOS";
    }

    public String irAReportePOIProyectosAdministrativosPrgInstitucional() {
        return "IR_A_REPORTE_POI_PROYECTOS_ADMINISTRATIVOS_PRG_INSTITUCIONAL";
    }

    public String irAReporteSeguimientoProyectoAdministrativo() {
        return "IR_A_REPORTE_SEGUIMIENTO_PROYECTO_ADMINISTRATIVO";
    }

    public String irAProyectoInversion() {
        FacesContext.getCurrentInstance()
            .getExternalContext()
            .getFlash()
            .put("restriccionXTipoProyecto", "2");
        return "consultaProyecto.xhtml?faces-redirect=true&restriccionXTipoProyecto=2";
    }

    public String irATechosPresupuestarioGoes() {
        return "IR_A_TECHOS_PRESUPUESTARIOS_GOES";
    }

    public String irACatalogoDeInsumos() {
        return "IR_A_CATALOGO_DE_INSUMOS";
    }

    public String irAConsultaObjetosEspecificosGasto() {
        return "IR_A_OBJETOS_ESPECIFICOS_GASTO";
    }
    
    public String irProgramacionPOGProyectos() {
        return "IR_A_POG_PARA_PROYECTOS";
    }

    public String irCatalogoDeProductos() {
        return "IR_A_CATALOGO_PRODUCTOS";
    }

    public String irACodigueraUnidadDeMEdida() {
        return "IR_A_CODIGUERA_UNIDAD_DE_MEDIDA";
    }

    public String irAFormulacionPorAnioFiscal() {
        return "IR_A_FORMULACION_POR_ANIO_FISCAL";
    }

    public String irAPOAAccionCentral() {
        return "IR_A_POA_ACCION_CENTRAL";
    }

    public String irAPOAAsignacionesNoProgramables() {
        return "IR_A_POA_ASIGNACIONES_NO_PROGRAMABLES";
    }

    public String irAConsolidadoPOAporAcoANP() {
        return "IR_A_CONSULTA_POA_AC_ANP";
    }

    public String irAReprogramacionAccionCentral() {
        return "IR_A_REPROGRAMACION_ACCION_CENTRAL";
    }

    public String irAConsultaReprogramacionAccionCentral() {
        return "IR_A_CONSULTA_REPROGRAMACION_ACCION_CENTRAL";
    }

    public String irAConsultaReprogramacionAsignacionNoProgramable() {
        return "IR_A_CONSULTA_REPROGRAMACION_ASIGNACION_NO_PROGRAMABLE";
    }

    public String irAReprogramacionProyectos() {
        return "IR_A_CONSULTA_REPROGRAMACION_PROYECTOS";
    }

    public String irAConsolidadoPOAporProyecto() {
        return "IR_A_CONSULTA_POA_POR_PROYECTO";
    }

    public String irANotificaciones() {
        return "IR_A_NOTIFICACIONES";
    }

    public String irProgramacionPOAProyectos() {
        return "IR_A_CONSULTA_POA_PROYECTO";
    }

    public String irConsultaPlantillaDeInsumos() {
        return "IR_A_CONSULTA_PLANTILLA_DE_INSUMOS";
    }

    public String irConsultaPAC() {
        return "IR_A_CONSULTA_PAC";
    }

    public String irReporteInsumosPAC() {
        return "IR_A_REPORTE_INSUMOS_PAC";
    }

    public String irProcesoAdquisicion() {
        return "IR_A_PROCESO_ADQUISICION";
    }

    public String irTiemposProcesoAdquisicion() {
        return "IR_A_TIEMPOS_PROCESO_ADQUISICION";
    }

    public String irConsultaContratoOC() {
        return "IR_A_CONSULTA_CONTRATO_OC";
    }

    public String irAProveedores() {
        return "IR_A_PROVEEDORES";
    }

    public String salir() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        permisos = new HashMap<>();
        return "IR_A_FIN";
    }

    public String irMetodosAdquisicion() {
        return "IR_A_METODOS_ADQUISICION";
    }

    public String irAReportePAC() {
        return "IR_A_EMITIR_REPORTE_PAC";
    }

    public String irACodigueraCategoria() {
        return "IR_A_CODIGUERA_CATEGORIA";
    }

    public String irAPeriodoDeSeguimientoDeIndicadores() {
        return "IR_CONSULTA_PERIODO_DE_SEGUIMIENTO_INDICADORES";
    }

    public String irACodigueraCategoriaConvenio() {
        return "IR_A_CODIGUERA_CATEGORIA_CONVENIO";
    }

    public String irACodigueraConvenio() {
        return "IR_A_CONVENIO";
    }

    public String irAGestionUnidadTecnica() {
        return "IR_A_CODIGUERA_UNIDAD_TECNICA";
    }
    
    public String irAGestionPOA() {
        return "IR_A_POA";
    }
    public String irAConsultaSeguimientoPOA() {
        return "IR_A_SEGUIMIENTO_POA";
    }
    public String irAConsultaProgramacionTrimestralPOA() {
        return "IR_A_PROGRAMACION_TRIMESTRAL_POA";
    }
    public String irATipoCredito() {
        return "IR_A_TIPO_CREDITO";
    }

    public String irACodigueraFuenteFinanciamiento() {
        return "IR_A_CODIGUERA_FUENTE_FINANCIAMIENTO";
    }

    public String irACodigueraFuenteRecurso() {
        return "IR_A_CODIGUERA_FUENTE_RECURSO";
    }

    public String irACodigueraFormaMedicion() {
        return "IR_A_CODIGUERA_FORMA_MEDICION";
    }

    public String irACodigueraMacroActividad() {
        return "IR_A_CODIGUERA_MACRO_ACTIVIDAD";
    }

    public String irACodigueraNormativa() {
        return "IR_A_CODIGUERA_NORMATIVA";
    }

    public String irACargaOperacionFinancieraAC() {
        return "IR_A_PROGRAMACION_FINANCIERA_ACCION_CENTRAL";
    }

    public String irACargaOperacionFinancieraANP() {
        return "IR_A_PROGRAMACION_FINANCIERA_ASIGNACION_NO_PROGRAMABLE";
    }

    public String irActividadPOProyecto() {
        return "IR_A_CODIGUERA_ACTIVIDAD_PO_PROYECTO";
    }

    public String irATipoDocumento() {
        return "IR_A_TIPO_DOCUMENTO";
    }

    public String irAMetasDeIndicadores() {
        return "IR_METAS_DE_INDICADORES";
    }

    public String irAValoresDeIndicadores() {
        return "IR_VALORES_DE_INDICADORES";
    }

    public String irAValidarCertificadoDisponibilidadPresupuestaria() {
        return "IR_A_VALIDAR_CERTIFICADO_DE_DISPONIBILIDAD_PRESUPUESTARIA";
    }

    public String irAVerMetasDeIndicadores() {
        return "IR_A_VER_METAS_DE_INDICADORES";
    }

    public String irAVerValoresDeIndicadores() {
        return "IR_A_VER_VALORES_DE_INDICADORES";
    }

    public String irAVerMetasDeIndicadoresPOA() {
        return "IR_A_VER_METAS_DE_INDICADORES_POA";
    }

    public String irAVerValoresDeIndicadoresPOA() {
        return "IR_A_VER_VALORES_DE_INDICADORES_POA";
    }
    
    public String irAMatrizDeRiesgos() {
        return "IR_A_MATRIZ_DE_RIESGOS";
    }

    public String irAMatrizDeRiesgosPOA() {
        return "IR_A_MATRIZ_DE_RIESGOS_POA";
    }
    public String irVerSeguimientoIndicadoresPOA() {
        return "IR_A_VER_SEGUIMIENTO_INDICADORES_POA";
    }
    public String irActasDePago() {
        return "IR_A_ACTAS_PAGOS";
    }

    public String irAComprobanteRetencionImpuestos() {
        return "IR_A_EMISION_COMPROBANTE_RETENCION_IMPUESTOS";
    }

    public String irARetencionImpuestoPorProyecto() {
        return "IR_A_RETENCION_IMPUESTOS_POR_PROYECTO";
    }

    public String irARetencionImpuestoPorProveedor() {
        return "IR_A_RETENCION_IMPUESTOS_POR_PROVEEDOR";
    }

    public String irAPlaLineasMetasIndicadores() {
        return "IR_A_PLA_LINEAS_METAS_INDICADORES";
    }

    public String irALineasMetasIndicadores() {
        return "IR_A_LINEAS_METAS_INDICADORES";
    }

    public String irAResumenPresupuestario() {
        return "IR_A_RESUMEN_PRESUPUESTARIO";
    }

    public String irAConsultarEstadoInsumos() {
        return "IR_A_CONSULTAR_ESTADOS_INSUMOS";
    }
    
    public String irASeguimientoCronogramaInsumos() {
        return "IR_A_SEGUIMIENTO_CRONOGRAMA_INSUMOS";
    }

    public String irAPolizas() {
        return "IR_A_CONSULTAR_POLIZAS";
    }

    public String irACompromisoPresupuestario() {
        return "IR_A_COMPROMISO_PRESUPUESTARIO";
    }

    public String irACierreTesoreria() {
        return "IR_A_CIERRE_TESORERIA";
    }

    public String irACierrePlanificacion() {
        return "IR_A_CIERRE_PLANIFICACION";
    }

    public String irACierrePresupuesto() {
        return "IR_A_CIERRE_PRESUPUESTO";
    }

    public String irACierreInversion() {
        return "IR_A_CIERRE_INVERSION";
    }

    public String irAImportarInfCompromiso() {
        return "IR_A_IMPORTAR_INF_COMPROMISO";
    }

    public String irAConsultaDatosSafi() {
        return "IR_A_CONSULTA_DATOS_SAFI";
    }

    public String irACertificadoDeDisponibilidadPresupuestaria() {
        return "IR_A_CONSULTA_CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA";
    }
        
    public String irAValidarCertificadoDeDisponibilidadPresupuestaria() {
        return "IR_A_VALIDAR_CONSULTA_CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA";
    }
    
    public String irAConsultaInsumosNoUACI() {
        return "IR_A_CONSULTA_INSUMOS_NO_UACI";
    }
    
    public String irAGeneracionReportePEP(){        
        return "IR_A_GENERACION_REPORTE_PEP";
    }   
    
    public String irAConsultaPresupuesto() {
        return "IR_A_CONSULTA_PRESUPUESTO";
    }    
    
    public String irACronogramaRecursos() {
        return "IR_A_CRONOGRAMA_RECURSOS";
    }

    public String irAGestionPresupuestoEscolar() {
        return "IR_A_GESTION_PRESUPUESTO_ESCOLAR";
    }  

    public String irACreacionGestionPresupuestoEscolar() {
        return "IR_A_CREAR_EDITAR_GESTION_PRESUPUESTO_ESCOLAR";
    }
    
    public String irAAreasDeInversion(){
        return "IR_A_AREAS_DE_INVERSION";
    }
    
    public String irACreacionAreasDeInversion(){
        return "IR_A_CREACION_AREAS_DE_INVERSION";
    }
    
    public String irACreacionSubAreasDeInversion(){
        return "IR_A_CREACION_SUB_AREAS_DE_INVERSION";
    }
    
    public String irACategoriaPresupuestoEscolar(){
        return "IR_A_CATEGORIA_PRESUPUESTO_ESCOLAR";
    }
    
    public String irATiposTransferencia(){
        return "IR_A_TIPOS_TRANSFERENCIA";
    }
    
    public String irARubros(){
        return "IR_A_RUBROS";
    }
    
    public String irASubRubros(){
        return "IR_A_SUB_RUBROS";
    }
    
    public String irACuentas(){
        return "IR_A_CUENTAS";
    }
    
    public String irACreacionCuentas(){
        return "IR_A_CREACION_CUENTAS";
    }
    
    public String irACreacionSubCuentas(){
        return "IR_A_CREACION_SUB_CUENTAS";
    }
    
    public String irATopePresupuestal(){
        return "IR_A_TOPE_PRESUPUESTAL";
    }
    public String irADetalleTopePresupuestal(){
        return "IR_A_DETALLE_TOPE_PRESUPUESTAL";
    }
    
    
    public String irATotalPresupuestos(){
        return "IR_A_TOTAL_PRESUPUESTOS";
    }
    public String irATotalTransferencias(){
        return "IR_A_TOTAL_TRANSFERENCIAS";
    }
    
    public String irATransferencias(){
        return "IR_A_TRANSFERENCIAS";
    }
    
    public String irATransferenciaComponente(){
        return "IR_A_TRANSFERENCIA_COMPONENTE";
    }
    
    public String irATransferenciaComponentePrevisualizacion(){
        return "IR_A_TRANSFERENCIA_COMPONENTE_PREVISUALIZACION";
    }
    
    public String irADesembolso(){
        return "IR_A_DESEMBOLSO";
    }

}