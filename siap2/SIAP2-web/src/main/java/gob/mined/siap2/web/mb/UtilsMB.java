/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Idioma;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.Categoria;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ClasificadorFuncional;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo1Segmento;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo2Familia;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo3Clase;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo4Articulo;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.IndicadorFormaMedicion;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.MacroActividad;
import gob.mined.siap2.entities.data.impl.MetaIndicador;
import gob.mined.siap2.entities.data.impl.MetaIndicadorProducto;
import gob.mined.siap2.entities.data.impl.MetaIndicadorProyecto;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.TipoDocumento;
import gob.mined.siap2.entities.data.impl.UnidadDeMedida;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.entities.enums.EstadoCompromiso;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.entities.enums.EstadoPoliza;
import gob.mined.siap2.entities.enums.EstadoProcesoAdq;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.EstadoTransferencias;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.entities.enums.TipoEjecucion;
import gob.mined.siap2.entities.enums.TipoFactura;
import gob.mined.siap2.entities.enums.TipoImpuesto;
import gob.mined.siap2.entities.enums.TipoMedicion;
import gob.mined.siap2.entities.enums.TipoMetaIndicador;
import gob.mined.siap2.entities.enums.TipoMontoEstructura;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoPOAPAC;
import gob.mined.siap2.entities.enums.TipoPagoActa;
import gob.mined.siap2.entities.enums.TipoParipassu;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.ConvenioDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "utilsMB")
@ApplicationScoped
public class UtilsMB implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private final static String localeDinero = "es_SV";
    private final static String localeDineroTag = NumberUtils.localeDineroTag;
    private final static String patternDinero = NumberUtils.patternDinero;

    private final static String localeDate = "es";
    private final static String patternDate = "dd/MM/yyyy";

    private final static Integer inputNumberMinFractionDigits = 2;
    private final static Integer inputNumberMaxFractionDigits = 2;

    private final static int autoCompleteScrollHeight = 300;

    private final static int MINIMO_DIGITOS_AUTO_COMPLETE = 3;

    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    ConfiguracionDelegate configuracionDelegate;
    @Inject
    ConvenioDelegate convenioDelegate;
    @Inject
    ArchivoDelegate archivoDelegate;
    @Inject
    UsuarioDelegate usuarioDelegate;
    @Inject
    private ReporteDelegate reporteDelegate;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    private TextMB textMB;
    @Inject
    private InsumoDelegate insumoDelegate;

    /**
     * Retorna todos los valores del enumerado tipoTarea
     *
     * @return
     */
    public TipoTarea[] getTipoTareaMetodoAdq() {
        return TipoTarea.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadoComun
     *
     * @return
     */
    public EstadoComun[] getEstadoPrograma() {
        return EstadoComun.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadoComun
     *
     * @return
     */
    public EstadoComun[] getEstadoIndicadores() {
        return EstadoComun.values();
    }

    /**
     * Retorna todos los valores del enumerado TipoProyecto
     *
     * @return
     */
    public TipoProyecto[] getTiposProyecto() {
        return TipoProyecto.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadoProducto
     *
     * @return
     */
    public EstadoComun[] getEstadoProductos() {
        return EstadoComun.values();
    }

    /**
     * Retorna todos los valores del enumerado TipoSeguimiento
     *
     * @return
     */
    public TipoSeguimiento[] getTipoSeguimiento() {
        return TipoSeguimiento.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadosPAC
     *
     * @return
     */
    public EstadoPAC[] getEstadosPAC() {
        return EstadoPAC.values();
    }

    /**
     * Retorna todos los valores del enumerado PasosProcesoAdquisiqion
     *
     * @return
     */
    public PasosProcesoAdquisicion[] getEstadosProcesoAdq() {
        return PasosProcesoAdquisicion.values();
    }

    /**
     * Retorna todos los valores del enumerado TipoContrato
     *
     * @return
     */
    public TipoContrato[] getTiposContratoOC() {
        return TipoContrato.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadosContrato
     *
     * @return
     */
    public EstadoContrato[] getEstadosContrato() {
        return EstadoContrato.values();
    }

    /**
     * Retorna todos los valores del enumeradoTipoContrato
     *
     * @return
     */
    public TipoContrato[] getTiposContrato() {
        return TipoContrato.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadoComun
     *
     * @return
     */
    public EstadoComun[] getEstadoLineasEstrategicas() {
        return EstadoComun.values();
    }

    /**
     * Retorna todos los valores del enumerado TipoImpuestos
     *
     * @return
     */
    public TipoImpuesto[] getTipoImpuestos() {
        return TipoImpuesto.values();
    }

    /**
     * Retorna todos los valores del enumerado AportesProyecto
     *
     * @return
     */
    public TipoAporteProyecto[] getTiposAportesProyecto() {
        return TipoAporteProyecto.values();
    }

    /**
     * Retorna todos los valores del enumerado TipoMontoEstructuraProyecto
     *
     * @return
     */
    public TipoMontoEstructura[] getTipoMontoEstructuraProyecto() {
        return TipoMontoEstructura.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadoActividadPOA
     *
     * @return
     */
    public EstadoActividadPOA[] getEstadoActividadPOA() {
        return EstadoActividadPOA.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadoPolizas
     *
     * @return
     */
    public EstadoPoliza[] getEstadosPolizas() {
        return EstadoPoliza.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadosCompromisoPresupuestario
     *
     * @return
     */
    public EstadoCompromiso[] getEstadosCompromisoPresupuestario() {
        return EstadoCompromiso.values();
    }

    /**
     * Retorna todos los valores del enumerado TiposParipassu
     *
     * @return
     */
    public TipoParipassu[] getTiposParipassu() {
        return TipoParipassu.values();
    }

    /**
     * Retorna todos los valores del enumerado TiposFactura
     *
     * @return
     */
    public TipoFactura[] getTiposFacturas() {
        return TipoFactura.values();
    }

    /**
     * Retorna todos los valores del enumerado TiposPago
     *
     * @return
     */
    public TipoPagoActa[] getTiposPagoActa() {
        return TipoPagoActa.values();
    }

    /**
     * Retorna todos los valores del enumerado EstadosReprogramacion
     *
     * @return
     */
    public EstadoReprogramacion[] getEstadosReprogramacion() {
        return EstadoReprogramacion.values();
    }

    /**
     * Devuelve la lista de posibles estados de un certificado de disponibilidad
     * presupuestario
     *
     * @return
     */
    public EstadoCertificadoDispPresupuestaria[] getEstadoCertPres() {
        return EstadoCertificadoDispPresupuestaria.values();
    }
    
    /**
     * Devuelve todos los valores del enumerado de TipoEjecucion
     *
     * @return
     */
    public TipoEjecucion[] getTipoEjecucion(){
        return TipoEjecucion.values();
    }
    
    /**
     * Retorna la fecha actual
     *
     * @return
     */
    public Date getNewDate() {
        return new Date();
    }
    
    /**
     * Retorna todos los valores del enumerado EstadoTopePresupuestal
     *
     * @return
     */
    public EstadoTopePresupuestal[] getEstadosTopePresupuestal() {
        return EstadoTopePresupuestal.values();
    }
    
    /**
     * Retorna todos los valores del enumerado EstadoTransferencias
     *
     * @return
     */
    public EstadoTransferencias[] getEstadosTransferencias() {
        return EstadoTransferencias.values();
    }

    /**
     * Retorna las planificaciones vigentes
     *
     * @return
     */
    public Map<String, String> getPlanificacionesEstrategicasVigetnes() {
        Map<String, String> m = new LinkedHashMap();
        List<PlanificacionEstrategica> l = versionDelegate.getPlanificacionesEstrategicasVigentes();
        for (PlanificacionEstrategica p : l) {
            m.put(String.valueOf(p.getNombre()), String.valueOf(p.getId()));
        }
        return m;
    }

    /**
     * Retorna las planificaciones vigentes
     *
     * @return
     */
    public Map<String, String> getPlanificacionesEstrategicasVigetnesPorId() {
        Map<String, String> m = new LinkedHashMap();
        List<PlanificacionEstrategica> l = versionDelegate.getPlanificacionesEstrategicasVigentes();
        for (PlanificacionEstrategica p : l) {
            m.put(String.valueOf(p.getId()), String.valueOf(p.getNombre()));
        }
        return m;
    }

    /**
     * Retorna las lineas estratégicas vigentes
     *
     * @return
     */
    public Map<String, String> getLineasEstrategicasVigentes() {
        Map<String, String> m = new LinkedHashMap();
        List<LineaEstrategica> l = versionDelegate.getLineasEstrategicasVigentes();
        for (LineaEstrategica iter : l) {
            m.put(String.valueOf(iter.getNombre()), String.valueOf(iter.getId()));
        }
        return m;
    }

    /**
     * Retorna lineas estratégicas vigentes
     *
     * @param idPlanificacion
     * @return
     */
    public Map<String, String> getLineasEstrategicasVigetnes(String idPlanificacion) {
        Map<String, String> m = new LinkedHashMap();
        if (TextUtils.isEmpty(idPlanificacion)) {
            return m;
        }
        List<LineaEstrategica> l = versionDelegate.getLineasEstrategicasVigetnes(Integer.valueOf(idPlanificacion));
        for (LineaEstrategica iter : l) {
            m.put(String.valueOf(iter.getNombre()), String.valueOf(iter.getId()));
        }
        return m;
    }

    /**
     * Retorna los años fiscales
     *
     * @return
     */
    public Map<String, String> getAniosFiscales() {
        Map<String, String> m = new LinkedHashMap();
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        for (AnioFiscal a : l) {
            m.put(String.valueOf(a.getAnio()), String.valueOf(a.getId()));
        }

        return m;
    }

    /**
     * Retorna los años fiscales sin cerrar
     *
     * @return
     */
    public Map<String, String> getAniosFiscalesSinCerrar() {
        Map<String, String> m = new LinkedHashMap();
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        for (AnioFiscal a : l) {
            if (a.getCerrado() == null || !a.getCerrado()) {
                m.put(String.valueOf(a.getAnio()), String.valueOf(a.getId()));
            }
        }

        return m;
    }

    /**
     * Retorna los años fiscales
     *
     * @return
     */
    public Map<String, String> getAniosFiscalesPorId() {
        Map<String, String> m = new LinkedHashMap();
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        for (AnioFiscal a : l) {
            m.put(String.valueOf(a.getId()), String.valueOf(a.getAnio()));
        }

        return m;
    }

    /**
     * Retorna los años fiscales, pero haciendo referencia al año no al id
     *
     * @return
     */
    public Map<String, String> getAniosFiscalesSoloAnios() {
        Map<String, String> m = new LinkedHashMap();
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        for (AnioFiscal a : l) {
            m.put(String.valueOf(a.getAnio()), String.valueOf(a.getAnio()));
        }

        return m;
    }

    /**
     * Retorna los idiomas disponibles
     *
     * @return
     */
    public Map<String, String> getIdiomas() {
        Map<String, String> m = new LinkedHashMap();
        List<Idioma> l = emd.getEntities(Idioma.class.getName(), "idiCodigo");
        for (Idioma a : l) {
            m.put(String.valueOf(a.getIdiCodigo() + " " + a.getIdiDescripcion()), String.valueOf(a.getIdiId()));
        }

        return m;
    }

    /**
     * Retornas los años marcados como planificación
     *
     * @return
     */
    public Map<String, String> getAniosFiscalesPlanificacion() {
        Map<String, String> m = new LinkedHashMap();
        List<AnioFiscal> l = versionDelegate.getAniosFiscalesPlanificacion();
        for (AnioFiscal a : l) {
            m.put(String.valueOf(a.getAnio()), String.valueOf(a.getId()));
        }

        return m;
    }

    /**
     * Retorna los tipos de documento par aproyecto
     *
     * @return
     */
    public Map getTiposDocumentoProyecto() {
        Map map = new LinkedHashMap();
        List<TipoDocumento> ll = versionDelegate.getClasesGeneralCodiguera(TipoDocumento.class);
        for (TipoDocumento p : ll) {
            map.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return map;
    }

    /**
     * Retorna desde las codigueras las unidades de medida
     *
     * @return
     */
    public Map getUnidadesDeMedida() {
        Map map = new LinkedHashMap();
        List<UnidadDeMedida> ll = versionDelegate.getClasesGeneralCodiguera(UnidadDeMedida.class);
        for (UnidadDeMedida p : ll) {
            map.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return map;
    }

    /**
     * Retorna desde las codigueras los métodos de adquisición
     *
     * @return
     */
    public Map<String, String> getMetodosAdquisicion() {
        Map<String, String> m = new LinkedHashMap();
        List<MetodoAdquisicion> l = emd.findByOneProperty(MetodoAdquisicion.class.getName(), "habilitado", Boolean.TRUE, "nombre");
        for (MetodoAdquisicion a : l) {
            m.put(a.getNombre(), String.valueOf(a.getId()));
        }

        return m;
    }

    /**
     * Retorna desde las codigueras las categorías de los indicadores
     *
     * @return
     */
    public Map getCategoriasIndicadores() {
        Map categorias = new LinkedHashMap();
        List<Categoria> ll = versionDelegate.getClasesGeneralCodiguera(Categoria.class);
        for (Categoria p : ll) {
            categorias.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return categorias;
    }

    /**
     * Retorna desde las codigueras las formas de medición de los indicadores
     *
     * @return
     */
    public Map getFormasMedicionIndicadores() {
        Map formaMedicion = new LinkedHashMap();
        List<IndicadorFormaMedicion> ll = versionDelegate.getClasesGeneralCodiguera(IndicadorFormaMedicion.class);
        for (IndicadorFormaMedicion p : ll) {
            formaMedicion.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return formaMedicion;
    }

    /**
     * Retorna desde las codigueras los impuestos
     *
     * @return
     */
    public Map getImpuestos() {
        Map formaMedicion = new LinkedHashMap();
        List<Impuesto> ll = versionDelegate.getClasesGeneralCodiguera(Impuesto.class);
        for (Impuesto p : ll) {
            formaMedicion.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return formaMedicion;
    }

    /**
     * Retorna los programas presupuestarios
     *
     * @return
     */
    public Map<String, String> getProgramasPrespuestario() {
        Map<String, String> map = new LinkedHashMap();
        List<ProgramaPresupuestario> ll = versionDelegate.getProgramasPresupuestariosVigentes();
        for (ProgramaPresupuestario p : ll) {
            map.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return map;
    }

    /**
     * Retorna los subprogrmas presupuestarios
     *
     * @param idPrograma
     * @return
     */
    public Map<String, String> getSubProgramasPrespuestario(String idPrograma) {
        Map<String, String> map = new LinkedHashMap();
        if (!TextUtils.isEmpty(idPrograma)) {
            List<ProgramaPresupuestario> ll = versionDelegate.getSubProgramasPresupuestariosVigentes(Integer.valueOf(idPrograma));
            for (ProgramaPresupuestario p : ll) {
                map.put(p.getNombre(), String.valueOf(p.getId()));
            }

        }
        return map;
    }

    /**
     * Retorna los subprogramas presupuestarios
     *
     * @return
     */
    public Map<String, String> getSubProgramasPrespuestario() {
        Map<String, String> map = new LinkedHashMap();
        List<ProgramaPresupuestario> ll = versionDelegate.getSubProgramasPresupuestariosVigentes(null);
        for (ProgramaPresupuestario p : ll) {
            map.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return map;
    }

    /**
     * Retorna los programas institucionales
     *
     * @return
     */
    public Map<String, String> getProgramasInstitucionales() {
        Map<String, String> map = new LinkedHashMap();
        List<ProgramaInstitucional> ll = versionDelegate.getProgramasInstitucionalesVigentes();
        for (ProgramaInstitucional p : ll) {
            map.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return map;
    }

    /**
     * Retorna desde las codigueras las unidades técnicas
     *
     * @return
     */
    public Map getUnidadesTecnicas() {
        Map unidades = new LinkedHashMap();
        List<UnidadTecnica> ll = emd.getEntities(UnidadTecnica.class.getName(), "nombre");
        for (UnidadTecnica u : ll) {
            unidades.put(u.getNombre(), String.valueOf(u.getId()));
        }
        return unidades;
    }

    /**
     * Retorna desde las codigueras las unidades técnicas que son dirección
     *
     * @return
     */
    public Map getUnidadesTecnicasDireccion() {
        Map unidades = new LinkedHashMap();
        List<UnidadTecnica> ll = versionDelegate.getUnidadesTecnicasDireccion();
        for (UnidadTecnica u : ll) {
            unidades.put(u.getNombre(), String.valueOf(u.getId()));
        }
        return unidades;
    }

    /**
     * Retorna desde las codigueras las fuentes de financiamiento
     *
     * @return
     */
    public Map<String, String> getFuentesFinanciamiento() {
        Map<String, String> map = new LinkedHashMap();
        List<FuenteFinanciamiento> ll = versionDelegate.getClasesGeneralCodiguera(FuenteFinanciamiento.class);
        for (FuenteFinanciamiento l : ll) {
            map.put(l.getNombre(), String.valueOf(l.getId()));
        }
        return map;
    }

    /**
     * Retorna desde las codigueras las fuentes de recursos
     *
     * @param idFuenteFinanciamiento
     * @return
     */
    public Map<String, String> getFuentesRecurso(String idFuenteFinanciamiento) {
        Map<String, String> map = new LinkedHashMap();
        if (TextUtils.isEmpty(idFuenteFinanciamiento)) {
            return map;
        }
        List<FuenteRecursos> ll = versionDelegate.getFuentesRecursos(NumberUtils.getIntegerONull(idFuenteFinanciamiento));
        for (FuenteRecursos l : ll) {
            map.put(l.getNombre(), String.valueOf(l.getId()));
        }
        return map;
    }

    /**
     * Retorna desde las codigueras las actividades para un proyecto
     *
     * @return
     */
    public Map getMacroActividadesParaProyecto() {
        Map unidades = new LinkedHashMap();
        List<MacroActividad> ll = versionDelegate.getClasesGeneralCodiguera(MacroActividad.class);

        for (MacroActividad u : ll) {
            unidades.put(u.getNombre(), String.valueOf(u.getId()));
        }
        return unidades;
    }

    /**
     * Retorna los usuarios con operación
     *
     * @param codigoUsuario
     * @return
     */
    public Map getUsuariosConOperacion(String codigoUsuario) {
        Map res = new LinkedHashMap();
        List<SsUsuario> l = usuarioDelegate.getUsuariosConOperacion(codigoUsuario);
        for (SsUsuario u : l) {
            res.put(u.getUsuCod(), String.valueOf(u.getUsuId()));
        }
        return res;
    }

    /**
     * Retorna los proyectos
     *
     * @return
     */
    public Map<String, String> getProyectos() {
        Map<String, String> m = new LinkedHashMap();
        List<Proyecto> l = emd.getEntitiesUpper(Proyecto.class.getName(), "nombre");
        for (Proyecto iter : l) {
            String nombreP = iter.getNombre();
            m.put(nombreP, String.valueOf(iter.getId()));
        }

        return m;
    }

    /**
     * Retorna las asignaciones no programables
     *
     * @return
     */
    public Map<String, String> getAsignaiconesNP() {
        Map<String, String> m = new LinkedHashMap();
        List<AsignacionNoProgramable> l = emd.getEntitiesUpper(AsignacionNoProgramable.class.getName(), "nombre");
        for (AsignacionNoProgramable iter : l) {
            String nombreP = iter.getNombre();
            m.put(nombreP, String.valueOf(iter.getId()));
        }

        return m;
    }

    /**
     * Retorna las acciones programables
     *
     * @return
     */
    public Map<String, String> getAccionesCentrales() {
        Map<String, String> m = new LinkedHashMap();
        List<AccionCentral> l = emd.getEntitiesUpper(AccionCentral.class.getName(), "nombre");
        for (AccionCentral iter : l) {
            String nombreP = iter.getNombre();
            m.put(nombreP, String.valueOf(iter.getId()));
        }

        return m;
    }

    /**
     * Retorna los poas de un año fiscal
     *
     * @param idAnioFiscal
     * @return
     */
    public Map<String, String> getPOAS(String idAnioFiscal) {
        Map<String, String> m = new LinkedHashMap();

        if (!TextUtils.isEmpty(idAnioFiscal)) {
            List<GeneralPOA> l = versionDelegate.getPOAsTrabago(NumberUtils.getIntegerONull(idAnioFiscal));
            for (GeneralPOA iter : l) {
                String nombrePOA;
                if (iter.getTipo() == TipoPOA.POA_PROYECTO) {
                    nombrePOA = ((POAProyecto) iter).getProyecto().getNombre();
                } else {
                    nombrePOA = ((POAConActividades) iter).getConMontoPorAnio().getNombre();
                }

                nombrePOA = nombrePOA + " - " + iter.getUnidadTecnica().getNombre();
                m.put(nombrePOA, String.valueOf(iter.getId()));
            }
        }
        m = TextUtils.ordenarMapDeStringsPorClave(m);
        return m;
    }

    /**
     * Retorna los procesos de adquisición de un año fiscal
     *
     * @param idAnioFiscal
     * @return
     */
    public Map<String, String> getProcesosAdquisicion(String idAnioFiscal) {
        Map<String, String> m = new LinkedHashMap();
        List<ProcesoAdquisicion> l;
        if (TextUtils.isEmpty(idAnioFiscal)) {
            l = emd.getEntities(ProcesoAdquisicion.class.getName(), "nombre");
        } else {
            l = emd.findByOneProperty(ProcesoAdquisicion.class.getName(), "anio.id", NumberUtils.getIntegerONull(idAnioFiscal), "nombre");
        }
        for (ProcesoAdquisicion iter : l) {
            String nombreP = iter.getNombre();
            m.put(nombreP, String.valueOf(iter.getId()));
        }

        return m;
    }

    /**
     * Retorna los segmentos de la ONU
     *
     * @return
     */
    public Map<String, String> getSegmentosONU() {
        Map m = new LinkedHashMap();
        String[] orderBy = {"titulo"};
        boolean[] asc = {true};
        List<CodificacionInsumo1Segmento> l = emd.findEntityByCriteria(CodificacionInsumo1Segmento.class, null, orderBy, asc, null, null);
        for (CodificacionInsumo1Segmento c : l) {
            m.put(c.getCodigo() + " " + c.getTitulo(), String.valueOf(c.getId()));
        }
        return m;
    }

    /**
     * Retorna las familias de la ONU
     *
     * @param idSegemnto
     * @return
     */
    public Map<String, String> getFamiliasONU(String idSegemnto) {
        Map m = new LinkedHashMap();
        if (TextUtils.isEmpty(idSegemnto)) {
            return m;
        }
        if (!TextUtils.isEmpty(idSegemnto)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "padre.id", Integer.valueOf(idSegemnto));
            String[] orderBy = {"titulo"};
            boolean[] asc = {true};
            List<CodificacionInsumo2Familia> l = emd.findEntityByCriteria(CodificacionInsumo2Familia.class, criterio, orderBy, asc, null, null);
            for (CodificacionInsumo2Familia c : l) {
                m.put(c.getCodigo() + " " + c.getTitulo(), String.valueOf(c.getId()));
            }
        }
        return m;
    }

    /**
     * Retorna desde las codigueras las clases de la codificación ONU
     *
     * @param idFamilia
     * @return
     */
    public Map<String, String> getClaseONU(String idFamilia) {
        Map m = new LinkedHashMap();
        if (TextUtils.isEmpty(idFamilia)) {
            return m;
        }
        if (!TextUtils.isEmpty(idFamilia)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "padre.id", Integer.valueOf(idFamilia));
            String[] orderBy = {"titulo"};
            boolean[] asc = {true};
            List<CodificacionInsumo3Clase> l = emd.findEntityByCriteria(CodificacionInsumo3Clase.class, criterio, orderBy, asc, null, null);
            for (CodificacionInsumo3Clase c : l) {
                m.put(c.getCodigo() + " " + c.getTitulo(), String.valueOf(c.getId()));
            }
        }
        return m;
    }

    /**
     * Retorna desde las codigueras los artículos onu
     *
     * @param idClase
     * @return
     */
    public Map<String, String> getArticuloONU(String idClase) {
        Map m = new LinkedHashMap();
        if (TextUtils.isEmpty(idClase)) {
            return m;
        }
        if (!TextUtils.isEmpty(idClase)) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "padre.id", Integer.valueOf(idClase));
            String[] orderBy = {"titulo"};
            boolean[] asc = {true};
            List<CodificacionInsumo4Articulo> l = emd.findEntityByCriteria(CodificacionInsumo4Articulo.class, criterio, orderBy, asc, null, null);
            for (CodificacionInsumo4Articulo c : l) {
                m.put(c.getCodigo() + " " + c.getTitulo(), String.valueOf(c.getId()));
            }
        }
        return m;
    }

    /**
     * retorna si el año esta habilitado para la planificación
     *
     * @param anio
     * @return
     */
    public boolean habilitadaAnioTechoCategoriaPresupuestal(Integer anio) {
        List<AnioFiscal> aniosFiscales = emd.findByOneProperty(AnioFiscal.class.getName(), "anio", anio);
        if (aniosFiscales.isEmpty()) {
            return false;
        } else {
            return aniosFiscales.get(0).getHabilitadoPlanificacion();
        }
    }

    /**
     * Retorna desde las codigueras los productos
     *
     * @return
     */
    public Map<String, String> getProductos() {
        Map m = new LinkedHashMap();
        List<Indicador> l = versionDelegate.getProductosVigentes();
        for (Indicador c : l) {
            m.put(c.getNombre(), String.valueOf(c.getId()));
        }
        return m;
    }

    /**
     * Retorna el tipo de PAC para un POA
     *
     * @param poa
     * @return
     */
    public TipoPOAPAC getTipoPOA(GeneralPOA poa) {
        if (poa == null) {
            return null;
        }
        if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
            return TipoPOAPAC.POA_PROYECTO;
        }
        if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
            POAConActividades conActividades = (POAConActividades) poa;
            if (conActividades.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                return TipoPOAPAC.POA_ACCION_CENTRAL;
            } else {
                return TipoPOAPAC.POA_ASIGNACION_NO_PROGRAMABLE;
            }
        }
        return null;
    }

    /**
     * método que se llama cuando el usuario escribe en el autocomplete y filtra
     * los resultados
     *
     * @param query
     * @return
     */
    public List<Proyecto> completeTextProyecto(String query) {
        return versionDelegate.getProyectosConNombre(query);
    }

    /**
     * método que se llama cuando el usuario escribe en el autocomplete y filtra
     * los resultados
     *
     * @param query
     * @return
     */
    public List<POInsumos> completeTextInsumoNOUaciPorCodigo(String query) {
        Integer codigo = NumberUtils.getIntegerONull(query);
        if (codigo == null) {
            return new LinkedList<>();
        }
        return versionDelegate.getInsumosNOUACIporCodigo(codigo);
    }
    
    /**
     * método que se llama cuando el usuario escribe en el autocomplete y filtra
     * los resultados
     *
     * @param query
     * @return
     */
    public List<POInsumos> completeTextInsumoNOUaciCertificadosPorCodigo(String query) {
        Integer codigo = NumberUtils.getIntegerONull(query);
        if (codigo == null) {
            return new LinkedList<>();
        }
        return versionDelegate.getInsumosNOUACICertificadosPorCodigo(codigo);
    }

    /**
     * Patrón en par amostrar el dinero
     *
     * @return
     */
    public String getLocaleDinero() {
        return localeDinero;
    }

    /**
     * Patrón en par amostrar el dinero
     *
     * @return
     */
    public String getPatternDinero() {
        return patternDinero;
    }

    /**
     * retorna los clasificadores funcionales disponibles
     *
     * @return
     */
    public Map<String, String> getClasificadoresAsignables() {
        Map<String, String> map = new LinkedHashMap();
        List<ClasificadorFuncional> ll = versionDelegate.getClasificadoresFuncionalesAsignables();
        for (ClasificadorFuncional p : ll) {
            map.put(p.getCodigo() + " " + p.getNombre(), String.valueOf(p.getId()));
        }
        return map;
    }

    /**
     * Patrón para la fecha
     *
     * @return
     */
    public String getLocaleDate() {
        return localeDate;
    }

    /**
     * Patrón para la fecha
     *
     * @return
     */
    public String getPatternDate() {
        return patternDate;
    }

    /**
     * Patrón para input numérico
     *
     * @return
     */
    public Integer getInputNumberMinFractionDigits() {
        return inputNumberMinFractionDigits;
    }

    /**
     * Patrón para input numérico
     *
     * @return
     */
    public Integer getInputNumberMaxFractionDigits() {
        return inputNumberMaxFractionDigits;
    }

    /**
     * Largo de las sugerencias del autocomplete
     *
     * @return
     */
    public Integer getAutoCompleteScrollHeight() {
        return autoCompleteScrollHeight;
    }

    /**
     * Retorna desde las codigueras las categorías
     *
     * @param idConvenio
     * @return
     */
    public Map<String, String> getCategoriasConvenios(Integer idConvenio) {
        Map<String, String> map = new LinkedHashMap();
        if (idConvenio != null) {
            List<CategoriaConvenio> ll = convenioDelegate.getCategorias(idConvenio);
            for (CategoriaConvenio p : ll) {
                map.put(p.getCodigo() + " " + p.getNombre(), String.valueOf(p.getId()));
            }
        }
        return map;
    }

    /**
     * método que realiza el autocompletar para las codigueras de actividad de
     * proyecto
     *
     * @param query
     * @return
     */
    public List<ActividadPOProyecto> completeTextCodigueraActividadesPO(String query) {
        List<ActividadPOProyecto> l = versionDelegate.completeTextCodigueraActividadesPO(query);
        return l;
    }

    /**
     * Da formato a un número y lo devuelve como texto
     *
     * @param numero
     * @return
     */
    public static String nomberToString(BigDecimal numero) {
        return NumberUtils.nomberToString(numero);
    }

    /**
     * Da formato a un número y lo devuelve como texto
     *
     * @param numero
     * @return
     */
    public String formatNumber(BigDecimal numero) {
        if (numero == null) {
            return "";
        }
        return nomberToString(numero);
    }

    /**
     * Da formato a un número de pac y lo devuelve como texto
     *
     * @param number
     * @return
     */
    public String getNumeroParaPAC(Integer number) {
        String reducido = "00000" + number;
        return reducido.substring(reducido.length() - 5, reducido.length());
    }

    /**
     * Retorna el año fiscal actual
     *
     * @return
     */
    public AnioFiscal getAnioFiscalActual() {
        Integer year = DatesUtils.getYearOfDate(new Date());
        List<AnioFiscal> l = emd.findByOneProperty(AnioFiscal.class.getName(), "anio", year);
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    /**
     * Descarga un archivo de un DataFile
     *
     * @param file
     */
    public void downloadTempFile(DataFile file) {
        try {
            ArchivoUtils.downloadFile(file);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Descarga un archivo desde un Archivo
     *
     * @param file
     */
    public void downloadFile(Archivo file) {
        try {
            ArchivoUtils.downloadFile(file, archivoDelegate.getFile(file));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Retorna el indicador desde la meta
     *
     * @param meta
     * @return
     */
    public Indicador getIndicadorDesdeMeta(MetaIndicador meta) {
        if (meta == null) {
            return null;
        }
        if (meta.getTipo() == TipoMetaIndicador.META_PRODUCTO) {
            return ((MetaIndicadorProducto) meta).getProyectoEstProducto().getProducto();
        } else if (meta.getTipo() == TipoMetaIndicador.META_INDICADOR) {
            return ((MetaIndicadorProyecto) meta).getIndicador();
        }
        return null;
    }

    /**
     * Retorna el nombre del seguimiento según el tipo y la posición
     *
     * @param t
     * @param i
     * @return
     */
    public static String getTituloSeguimiento(TipoSeguimiento t, Integer i) {
        return TipoSeguimientoUtils.getListName(t).get(i);
    }

    /**
     * Retorna una lista ordenada de los nombres de seguimiento según el tipo
     *
     * @param t
     * @return
     */
    public List<String> getTitulosSeguimiento(TipoSeguimiento t) {
        return TipoSeguimientoUtils.getListName(t);
    }

    /**
     * Retorna la lista de meses
     *
     * @return
     */
    public List<String> getMeses() {
        return TipoSeguimientoUtils.getListName(TipoSeguimiento.MENSUAL);
    }

    /**
     * Retorna si un proceso esta desierto o sin efecto
     *
     * @param estado
     * @return
     */
    public Boolean procesoEstaDesiertoOSinEfecto(EstadoProcesoAdq estado) {
        return (estado == EstadoProcesoAdq.DESIERTO || estado == EstadoProcesoAdq.SIN_EFECTO);
    }

    /**
     * Retorna todos los valores del enumerado tipoTarea
     *
     * @return
     */
    public TipoMedicion[] getTiposMediciones() {
        return TipoMedicion.values();
    }
    /**
     * Retorna los tipos de actas para un contrato
     *
     * @param contrato
     * @return
     */
    public TipoActaContrato[] getTiposActasContrato(ContratoOC contrato) {
        boolean tieneAnticipo = (contrato.getPorcentajeAnticipo() != null && contrato.getPorcentajeAnticipo() > 0);
        boolean tieneDevolucion = (contrato.getPorcentajeDevolucion() != null && contrato.getPorcentajeDevolucion() > 0);
        Integer cantidadItems = 1;
        if (tieneAnticipo && tieneDevolucion) {
            cantidadItems = 3;
        } else if (tieneAnticipo || tieneDevolucion) {
            cantidadItems = 2;
        }

        TipoActaContrato[] tipos = new TipoActaContrato[cantidadItems];
        if (cantidadItems == 1) {
            tipos[0] = TipoActaContrato.RECEPCION;
        }
        if (cantidadItems == 2) {
            if (tieneAnticipo) {
                tipos[0] = TipoActaContrato.ANTICIPO;
                tipos[1] = TipoActaContrato.RECEPCION;
            } else {
                tipos[0] = TipoActaContrato.RECEPCION;
                tipos[1] = TipoActaContrato.DEVOLUCION;
            }
        }
        if (cantidadItems == 3) {
            tipos[0] = TipoActaContrato.ANTICIPO;
            tipos[1] = TipoActaContrato.RECEPCION;
            tipos[2] = TipoActaContrato.DEVOLUCION;
        }

        return tipos;
    }

    /**
     * Resta dos decimales
     *
     * @param n1
     * @param n2
     * @return
     */
    public BigDecimal restar(BigDecimal n1, BigDecimal n2) {
        if (n1 == null || n2 == null) {
            return null;
        }
        return n1.subtract(n2);
    }

    /**
     * método que se llama cuando el usuario escribe en el autocomplete y filtra
     * los resultados
     *
     * @param query
     * @return
     */
    public List<Insumo> completeInsumos(String query) {
        return versionDelegate.getInsumos(query);
    }

    /**
     * Genera el archivo de certificado de disponibilidad presupuestaria
     *
     * @param monto
     */
    public void generarCertificadoDisponibilidadPOInsumo(POMontoFuenteInsumo monto) {
        try {
            byte[] bytespdf = reporteDelegate.generarCertificadoDisponibilidadPOInsumo(monto.getId());
            ArchivoUtils.downloadPdfFromBytes(bytespdf, "CertificadoDeDisponibilidadPresupuestaria.pdf");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera el reporte de reprogramación
     *
     * @param reprogramacionId
     */
    public void generarReporteReprogramacion(Integer reprogramacionId) {
        try {
            byte[] bytespdf = reporteDelegate.generarReporteReprogramacion(reprogramacionId);
            ArchivoUtils.downloadPdfFromBytes(bytespdf, "Reprogramacion.pdf");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera el reporte de reprogramación
     *
     * @param reprogramacionId
     */
    public void generarReportePlanOperativoAnual(Integer poaId) {
        try {
            byte[] bytespdf = reporteDelegate.generarReportePlanOperativoAnual(poaId);
            ArchivoUtils.downloadPdfFromBytes(bytespdf, "PlanOperativoAnual.pdf");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Retorna un Map con el número y el nombre de cada mes
     *
     * @return
     */
    public Map<String, String> getMapMesesDelAnio() {
        Map<String, String> mapMeses = new LinkedHashMap();
        List<String> meses = new LinkedList<>();
        meses.add("ENERO");
        meses.add("FEBRERO");
        meses.add("MARZO");
        meses.add("ABRIL");
        meses.add("MAYO");
        meses.add("JUNIO");
        meses.add("JULIO");
        meses.add("AGOSTO");
        meses.add("SEPTIEMBRE");
        meses.add("OCTUBRE");
        meses.add("NOVIEMBRE");
        meses.add("DICIEMBRE");

        for (int i = 0; i < meses.size(); i++) {
            String idMes = (i + 1) + "";
            mapMeses.put(meses.get(i), idMes);
        }

        return mapMeses;
    }

    /**
     * Devuelve la categoría presupuestaria de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String getCategoriaPresupuestaria(Integer idPoInsumo) {
        return insumoDelegate.getCategoriaPresupuestariaDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del programa (si
     * es proyecto) o de AC o ANP de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoProgramaACoANPDePoInsumo(Integer idPoInsumo) {
        return insumoDelegate.getCodigoProgramaACoANPDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del subprograma
     * (si es proyecto) de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoSubprogramaDePoInsumo(Integer idPoInsumo) {
        return insumoDelegate.getCodigoSubprogramaDePoInsumo(idPoInsumo);
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del proyecto (si
     * no es AC o ANP) de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoProyectoDePoInsumo(Integer idPoInsumo) {
        return insumoDelegate.getCodigoProyectoDePoInsumo(idPoInsumo);
    }

    /**
     * Verifica si un insumo está asociado a un Proyecto
     *
     * @param poInsumo
     * @return
     */
    public Boolean poInsumoEsDeProyecto(POInsumos poInsumo) {
        return insumoDelegate.poInsumoEsDeProyecto(poInsumo);
    }

    /**
     * Verifica si un insumo está asociado a una Acción Central
     *
     * @param poInsumo
     * @return
     */
    public Boolean poInsumoEsDeAC(POInsumos poInsumo) {
        return insumoDelegate.poInsumoEsDeAC(poInsumo);
    }

    /**
     * Verifica si un insumo está asociado a una Asignación No Programable
     *
     * @param poInsumo
     * @return
     */
    public Boolean poInsumoEsDeANP(POInsumos poInsumo) {
        return insumoDelegate.poInsumoEsDeANP(poInsumo);
    }
    
    /**
     * Retorna los proyectos
     *
     * @return
     */
    public Map<String, String> getPACs() {
        Map<String, String> m = new LinkedHashMap();
        List<PAC> l = emd.getEntitiesUpper(PAC.class.getName(), "nombre");
        for (PAC iter : l) {
            String nombreP = iter.getNombre();
            m.put(nombreP, String.valueOf(iter.getId()));
        }

        return m;
    }
}
