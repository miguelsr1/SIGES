/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.reprogramacion;

import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.POAUtils;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.validaciones.ValidacionReprogramacionDetalle;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActividadAsignacionNP;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.OrigenDistribuccionMontoInsumo;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataPOAProyectoView;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralDelegate;
import gob.mined.siap2.web.delegates.impl.ReprogramacionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.ReprogramacionesUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.ws.to.ReprogramacionACFinanciera;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la
 * página que maneja la reprogramación. Este MB tiene las funcionalidades
 * genéricas, que son sobreescritas por los MB según el tipo de reprogramación.
 *
 * @author Sofis Solutions
 */
public class CrearEditarReprogramacionBase implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;

    @Inject
    UsuarioInfo userInfo;

    @Inject
    PermisosMB permisosMB;
    @Inject
    POAProyectoDelegate pOAProyectoDelegate;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    private ProyectoDelegate pyDelegate;
    @Inject
    private GeneralDelegate generalDelegate;
    @Inject
    private ReprogramacionDelegate reprogramacionDelegate;

    private SofisComboG<Proyecto> comboProyecto = new SofisComboG<>();
    private SofisComboG<GeneralPOA> comboPOA = new SofisComboG<>();
    private SofisComboG<GeneralPOA> comboPOAVisualizar = new SofisComboG<>();

    private SofisComboG<POLinea> comboLineasPOA = new SofisComboG<>();
    private SofisComboG<POActividadBase> comboActividades = new SofisComboG<>();
    private SofisComboG<POInsumos> comboInsumos = new SofisComboG<>();
    private SofisComboG<PACGrupo> comboGrupo = new SofisComboG<>();

    private POInsumos insumoEnEdicion = null;
    private List<POFlujoCajaMenusal> flujoMensual = new ArrayList();
    private ReprogramacionDetalle reprogEnEdicion;
    private Reprogramacion reprog;
    private Integer tabSeleccionado = 0;

    private List<ReprogramacionACFinanciera> listaRFin = new ArrayList();
    private Boolean modoProcesarPAC = Boolean.FALSE;
    private final Integer CANTIDAD_MESES = 3;
    private Boolean requiereAPROBACION = Boolean.TRUE;

    private Integer idActividadNP;
    private Integer tmpIdTramo;

    private List<DataPOAProyectoView> listaDatosPOA = new ArrayList();

    protected Integer cantidadMontosFuentesDePoInsumo;

    private BigDecimal totalMesesInsumo;

    public void doForward(ActionEvent evt) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String redirect = "IR_A_INICIO";// define the navigation rule that must be used in order to redirect the user to the adequate page...
        NavigationHandler myNav = facesContext.getApplication().getNavigationHandler();
        myNav.handleNavigation(facesContext, null, redirect);
    }

    /**
     * Este método inicializa la reprogramación
     *
     * @param tipoReprogramacion
     */
    public void init(TipoReprogramacion tipoReprogramacion) {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            reprog = reprogramacionDelegate.getReprogramacion(Integer.valueOf(id));
            if (reprog.getTipoReprogramacion() != tipoReprogramacion) {
                reprog = null;
            }
        } else {
            reprog = new Reprogramacion();
            reprog.setTipoReprogramacion(tipoReprogramacion);
            reprog.setEstado(EstadoReprogramacion.FORMULACION);
            reprog.setRep_detalle_lista(new LinkedList<ReprogramacionDetalle>());
        }

        actualizarFinanciera();
        cargarCombos();
    }

    public String getVolver() {
        return null;
    }

    /**
     * Este es el método a sobreescribir según el tipo de reprogramación
     *
     * @param anio
     * @return
     */
    protected List<GeneralPOA> obtenerpoas(AnioFiscal anio, boolean filtrarUT, List<UnidadTecnica> utAfiltrar) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Carga el combo de los POA del usuario que tienen PAC asociado
     *
     */
    private void cargarCombos() {
        try {
            boolean filtrarUT = !permisosMB.tieneOperacion(ConstantesEstandares.Operaciones.REPROGRAMAR_TODAS_UT);
            List<UnidadTecnica> utDisponibles = new LinkedList<>();
            if (filtrarUT) {
                utDisponibles = userInfo.getUTDeUsuarioConOperacion(ConstantesEstandares.Operaciones.REPROGRAMAR_REPROGRAMAR_UT);
            }

            List<GeneralPOA> resultado = new ArrayList();
            for (AnioFiscal anio : generalDelegate.obtenerAnioFiscalEnEjecucion()) {
                List<GeneralPOA> listaPOA = obtenerpoas(anio, filtrarUT, utDisponibles);
                for (GeneralPOA p : listaPOA) {
                    resultado.add(p);
                }
            }

            comboPOA = new SofisComboG<>(resultado, "nombre");
            comboPOA.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
            comboPOA.setSelected(null);

            comboPOAVisualizar = new SofisComboG<>(resultado, "nombre");
            comboPOAVisualizar.setSelected(null);
            comboPOAVisualizar.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
            comboActividades = new SofisComboG<>();
            comboActividades.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
            comboInsumos = new SofisComboG<>();
            comboInsumos.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Este es el método que carga los detalles a editar de una reprogramación
     */
    public void cargarDetalleToEditar() {

        if (reprogEnEdicion == null) {
            reprogEnEdicion = new ReprogramacionDetalle();
            reprogEnEdicion.setNuevaActividad(false);
            reprogEnEdicion.setNuevoInsumo(false);
            reprogEnEdicion.setInsumoNuevoNoUaci(false);
            reprogEnEdicion.setInsumoNuevoMontosFuentes(new LinkedList());

            limpiarComboActividades();
            limpiarComboInsumos();
        }

        if (reprogEnEdicion.getPoa() != null) {
            comboPOA.setSelectedT(reprogEnEdicion.getPoa());
            cambioPOA(false);
        } else {
            comboPOA.setSelected(null);
        }

        if (reprogEnEdicion.getPoaLinea() != null) {
            comboLineasPOA.setSelectedT(reprogEnEdicion.getPoaLinea());
            cambioLinea(false);
        } else {
            limpiarComboLinea();
        }

        if (reprogEnEdicion.getPoaActividad() != null) {
            comboActividades.setSelected(reprogEnEdicion.getPoaActividad().hashCode());
            cambioActividad();
        } else {
            limpiarComboActividades();
        }

        if (reprogEnEdicion.getPoaInsumo() != null) {
            comboInsumos.setSelectedT(reprogEnEdicion.getPoaInsumo());
        } else {
            limpiarComboInsumos();
        }

        if (reprogEnEdicion.getActividadNuevaAsigNP() != null) {
            idActividadNP = reprogEnEdicion.getActividadNuevaAsigNP().getId();
        }

        if (reprogEnEdicion.getInsumoNuevoTramo() != null) {
            tmpIdTramo = reprogEnEdicion.getInsumoNuevoTramo().getId();
        }

        if (reprogEnEdicion.getGrupoPAC() != null) {
            comboGrupo.setSelectedT(reprogEnEdicion.getGrupoPAC());
        } else {
            comboGrupo.setSelectedT(null);
        }
        actualizarTotalMesesInsumo();
        calcularTotalProgramacionFinancieraMontosFuentes();

        cantidadMontosFuentesDePoInsumo = 0;
        if (reprogEnEdicion.getInsumoNuevoMontosFuentes() != null) {
            cantidadMontosFuentesDePoInsumo = reprogEnEdicion.getInsumoNuevoMontosFuentes().size();
        }
    }

    /**
     * Elimina una línea de reprogramación
     *
     * @param uuid
     */
    public void eliminarDetalle() {
        reprog.getRep_detalle_lista().remove(reprogEnEdicion);
        actualizarFinanciera();
        tabSeleccionado = 0;
    }

    /**
     * Edita los datos de una línea de PAC.
     *
     * @param uuid
     */
    public void editarLineaPAC() {
        cargarDetalleToEditar();
        tabSeleccionado = 1;
    }

    /**
     * Actualización de los datos de una línea de reprogramación
     *
     */
    public void cambioPOA(boolean recalcularFuentes) {
        List<POLinea> lineas = new LinkedList();

        comboActividades = new SofisComboG<>();
        comboActividades.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
        if (comboPOA.getSelectedT() != null) {
            GeneralPOA poa = comboPOA.getSelectedT();
            if (poa instanceof POAProyecto) {
                lineas = ((POAProyecto) poa).getLineas();

                comboLineasPOA = new SofisComboG<>(lineas, "nombre");
                comboLineasPOA.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
                limpiarComboActividades();
            } else {
                List<POActividadBase> l = ((POAConActividades) poa).getActividades();
                comboActividades = new SofisComboG<>(l, "nombre");
                comboActividades.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
            }

        }

        limpiarComboInsumos();
        cargarGruposPAC();
        if (recalcularFuentes) {
            recalcularCambioPOA();
        }
    }

    /**
     * Este método carga los grupos de un PAC
     */
    public void cargarGruposPAC() {
        Collection<PACGrupo> listaGrupos = new LinkedList();
        GeneralPOA poa = comboPOA.getSelectedT();
        if (poa != null) {
            listaGrupos = reprogramacionDelegate.obtenerGruposPorPOAId(poa.getId());
        }
        comboGrupo = new SofisComboG<>(new ArrayList(listaGrupos), "nombre");
        comboGrupo.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));

    }

    /**
     * Este método se utiliza para el cambio de la linea.
     *
     * @param recalcularFuentes
     */
    public void cambioLinea(boolean recalcularFuentes) {
        List<POActividadBase> actividades = new LinkedList();
        POLinea linea = comboLineasPOA.getSelectedT();
        if (linea != null) {
            actividades = linea.getActividades();
        }
        comboActividades = new SofisComboG<>(actividades, "nombre");
        comboActividades.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
        limpiarComboInsumos();

        if (recalcularFuentes) {
            recalcularCambioPOA();
        }
    }

    /**
     * Este método se utiliza para recalcular el cambio del POA
     */
    private void recalcularCambioPOA() {
        GeneralPOA nuevoPOA = comboPOA.getSelectedT();
        POLinea nuevalinea = comboLineasPOA.getSelectedT();

        boolean recalcuarFuentes = true;

        if (reprogEnEdicion.getPoa() != null && reprogEnEdicion.getPoa().equals(nuevoPOA)
                && (reprogEnEdicion.getPoaLinea() == null && nuevalinea == null) || (reprogEnEdicion.getPoaLinea() != null && reprogEnEdicion.getPoaLinea().equals(nuevalinea))) {
            recalcuarFuentes = false;
        }
        reprogEnEdicion.setPoa(nuevoPOA);
        reprogEnEdicion.setPoaLinea(nuevalinea);

        if (recalcuarFuentes) {

            reprogEnEdicion.setInsumoNuevoMontosFuentes(new LinkedList<POMontoFuenteInsumo>());
            if (reprogEnEdicion.getPoa() != null) {
                if (reprogEnEdicion.getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                    //se crea la fuente para goes
                    OrigenDistribuccionMontoInsumo distribuccionMontoInsumo = new OrigenDistribuccionMontoInsumo();
                    distribuccionMontoInsumo.setFuenteRecursos(generalDelegate.getGOES());

                    POMontoFuenteInsumo montoFuente = new POMontoFuenteInsumo();
                    montoFuente.setFuente(distribuccionMontoInsumo);

                    reprogEnEdicion.getInsumoNuevoMontosFuentes().add(montoFuente);

                } else if (reprogEnEdicion.getPoaLinea() != null) {
                    for (ProyectoEstPorcentajeFuente f : reprogEnEdicion.getPoaLinea().getProducto().getProyectoEstructura().getMontosFuentes()) {
                        if (!ProyectoUtils.aporteEnEstructuraVacio(f, false)) {
                            if (ProyectoUtils.getInsumoMontoFuente(f.getFuente(), reprogEnEdicion.getInsumoNuevoMontosFuentes()) == null) {
                                POMontoFuenteInsumo monto = new POMontoFuenteInsumo();
                                monto.setFuente(f.getFuente());
                                reprogEnEdicion.getInsumoNuevoMontosFuentes().add(monto);
                            }
                        }
                    }
                }

            }
        }
        crearRelacionesMontoFuenteInsumoConFlujoDeCajMensual();
        cantidadMontosFuentesDePoInsumo = 0;
        if (reprogEnEdicion.getInsumoNuevoMontosFuentes() != null) {
            cantidadMontosFuentesDePoInsumo = reprogEnEdicion.getInsumoNuevoMontosFuentes().size();
        }
    }

    /**
     * Actualización de los datos de una línea a partir del cambio de actividad.
     */
    public void cambioActividad() {

        List<POInsumos> insumos = new LinkedList();
        POActividadBase actividad = comboActividades.getSelectedT();
        if (actividad != null) {
            insumos = actividad.getInsumos();
        }
        comboInsumos = new SofisComboG<>(insumos, "nombre");
        comboInsumos.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
        comboInsumos.setSelected(null);
    }

    /**
     * Este método limpia deja en vacía el combo de las lineas
     */
    public void limpiarComboLinea() {
        comboLineasPOA = new SofisComboG<>();
        comboLineasPOA.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
    }

    /**
     * Este método limpia deja en vacía el combo de las actividades
     */
    public void limpiarComboActividades() {
        comboActividades = new SofisComboG<>();
        comboActividades.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
    }

    /**
     * Este método limpia deja en vacía el combo de los insumos
     */
    public void limpiarComboInsumos() {
        comboInsumos = new SofisComboG<>();
        comboInsumos.addEmptyItem(textMB.obtenerTexto("labels.seleccioneUno"));
        totalMesesInsumo = BigDecimal.ZERO;
    }

    /**
     * Este método retorna las actividades de asignación no programable.
     *
     * @return
     */
    public Map<String, String> getActividadAsignacionNP() {
        Map<String, String> m = new LinkedHashMap<>();
        GeneralPOA poa = comboPOA.getSelectedT();
        if (poa != null && poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
            ConMontoPorAnio objeto = ((POAConActividades) poa).getConMontoPorAnio();
            if (objeto.getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
                AsignacionNoProgramable a = (AsignacionNoProgramable) objeto;
                for (ActividadAsignacionNP iter : a.getActividadesNP()) {
                    m.put(iter.getNombre(), iter.getId().toString());
                }
            }
        }
        return m;
    }

    /**
     * Este método es llamado cuando se cambia el insumo desde el combo de
     * insumos
     */
    public void cambioInsumo() {
        insumoEnEdicion = comboInsumos.getSelectedT();
        if (insumoEnEdicion != null) {
            reprogEnEdicion.setInsumoNuevoFechaContratacion(insumoEnEdicion.getFechaContratacion());
            reprogEnEdicion.setInsumoNuevoNoUaci(insumoEnEdicion.getNoUACI());
            reprogEnEdicion.setInsumoNuevoObservaciones(insumoEnEdicion.getObservacion());

            reprogEnEdicion.setInsumoNuevoTramo(insumoEnEdicion.getTramo());
            if (reprogEnEdicion.getInsumoNuevoTramo() != null) {
                tmpIdTramo = reprogEnEdicion.getInsumoNuevoTramo().getId();
            } else {
                tmpIdTramo = null;
            }

            reprogEnEdicion.setGrupoPAC(insumoEnEdicion.getPacGrupo());
            comboGrupo.setSelectedT(reprogEnEdicion.getGrupoPAC());

            reprogEnEdicion.setInsumoNuevoCantidad(insumoEnEdicion.getCantidad());
            reprogEnEdicion.setInsumoNuevoUnitario(insumoEnEdicion.getMontoUnit());
            reprogEnEdicion.setInsumoNuevoTotal(insumoEnEdicion.getMontoTotal());

            for (POMontoFuenteInsumo nuevaFuente : reprogEnEdicion.getInsumoNuevoMontosFuentes()) {
                POMontoFuenteInsumo fuenteInsumo = POAUtils.getFuenteEnInsumo(insumoEnEdicion, nuevaFuente.getFuente());
                if (fuenteInsumo != null) {
                    nuevaFuente.setCertificado(fuenteInsumo.getCertificado());
                    nuevaFuente.setMonto(fuenteInsumo.getMonto());
                    nuevaFuente.setCertificadoDisponibilidadPresupuestariaAprobada(fuenteInsumo.getCertificadoDisponibilidadPresupuestariaAprobada());
                }
            }

            //SE SETEA EL FLUJO DE CAJA DEL OTRO INSUMO
            List<AnioFiscal> aniosFiscales = (List<AnioFiscal>) generalDelegate.obtenerAnioFiscalEnEjecucion();
            if (aniosFiscales != null && !aniosFiscales.isEmpty()) {
                Integer anioEjecucion = aniosFiscales.get(0).getAnio();
                for (FlujoCajaAnio fca : insumoEnEdicion.getFlujosDeCajaAnio()) {
                    if (fca.getAnio().equals(anioEjecucion)) {
                        for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                            if (fcm.getMes() != null && fcm.getMes().equals(1)) {
                                reprogEnEdicion.setRdeMes1(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(2)) {
                                reprogEnEdicion.setRdeMes2(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(3)) {
                                reprogEnEdicion.setRdeMes3(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(4)) {
                                reprogEnEdicion.setRdeMes4(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(5)) {
                                reprogEnEdicion.setRdeMes5(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(6)) {
                                reprogEnEdicion.setRdeMes6(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(7)) {
                                reprogEnEdicion.setRdeMes7(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(8)) {
                                reprogEnEdicion.setRdeMes8(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(9)) {
                                reprogEnEdicion.setRdeMes9(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(10)) {
                                reprogEnEdicion.setRdeMes10(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(11)) {
                                reprogEnEdicion.setRdeMes11(fcm.getMonto());
                            }
                            if (fcm.getMes() != null && fcm.getMes().equals(12)) {
                                reprogEnEdicion.setRdeMes12(fcm.getMonto());
                            }
                        }
                    }
                }
            }
            actualizarTotalMesesInsumo();
            resetearMontosFuentesFCM();
            calcularTotalProgramacionFinancieraMontosFuentes();
        }

    }

    /**
     * Le setea a la reprogramación en edición que es un nuevo insumo
     */
    public void cambioCrearActividad() {

        if (reprogEnEdicion.getNuevaActividad()) {
            reprogEnEdicion.setNuevoInsumo(true);
        }
    }

    /**
     * Este método retorna si el insumo requiere tramos.
     *
     * @return
     */
    public boolean isnumoRequiereTramos() {
        if (reprog.getTipoReprogramacion() != TipoReprogramacion.PROYECTO) {
            return false;
        }
        POLinea linea = comboLineasPOA.getSelectedT();
        if (linea == null) {
            return false;
        }
        CategoriaConvenio cat = ProyectoUtils.getCategoriaConvenio(linea.getProducto().getProyectoEstructura(), false);
        if (cat == null || cat.getTipo() != TipoAporteProyecto.POR_TRAMOS) {
            return false;
        }

        return true;
    }

    /**
     * Este método retorna cuales son los tramos disponibles
     *
     * @return
     */
    public Map getTramosDisponibles() {
        Map<String, String> res = new LinkedHashMap();
        if (reprog.getTipoReprogramacion() != TipoReprogramacion.PROYECTO) {
            return res;
        }

        POAProyecto poa = (POAProyecto) comboPOA.getSelectedT();
        if (poa == null) {
            return res;
        }

        POLinea linea = comboLineasPOA.getSelectedT();
        if (linea == null) {
            return res;
        }

        CategoriaConvenio cat = ProyectoUtils.getCategoriaConvenio(linea.getProducto().getProyectoEstructura(), false);
        if (cat == null || cat.getTipo() != TipoAporteProyecto.POR_TRAMOS) {
            return res;
        }

        List<ProyectoAporteTramoTramo> tramos = pOAProyectoDelegate.getTramos(poa.getProyecto().getId(), cat);
        for (ProyectoAporteTramoTramo tramo : tramos) {
            String monto = UtilsMB.nomberToString(tramo.getMontoHasta());
            res.put(monto, tramo.getId().toString());
        }

        return res;
    }

    /**
     * Este método es llamado cuando se cambia el tramo
     *
     * @param idTramo
     */
    public void cambiarTramo(String idTramo) {
        if (TextUtils.isEmpty(idTramo)) {
            reprogEnEdicion.setInsumoNuevoTramo(null);
        } else {
            ProyectoAporteTramoTramo tramo = pOAProyectoDelegate.getProyectoAporteTramoTramo(Integer.valueOf(idTramo));
            reprogEnEdicion.setInsumoNuevoTramo(tramo);
        }
    }

    /**
     * Este método es llamado cuando se cambia el POA
     */
    //actualiza la línea del poa
    public void cambioComboPOAVisualizar() {
        listaDatosPOA = new ArrayList();
        GeneralPOA poa = comboPOAVisualizar.getSelectedT();
        if (poa != null) {
            if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
                POAProyecto poaProyecto = (POAProyecto) poa;
                for (POLinea lin : poaProyecto.getLineas()) {
                    for (POActividadBase act : lin.getActividades()) {
                        for (POInsumos in : act.getInsumos()) {
                            DataPOAProyectoView dp = new DataPOAProyectoView();
                            dp.setProducto(lin.getProducto().getProducto().getNombre());
                            dp.setActividad(act.getNombre());
                            dp.setInsumoCodigo(in.getInsumo().getCodigo());
                            dp.setInsumoId(in.getId());
                            dp.setInsumoNombre(in.getInsumo().getNombre());
                            dp.setMonto(in.getMontoTotal());
                            if (in.getNoUACI() != null) {
                                if (in.getNoUACI()) {
                                    dp.setUaci("NO");
                                } else {
                                    dp.setUaci("SI");
                                }
                            }
                            dp.setMontoCertificado(in.getMontoTotalCertificado());
                            if (in.getProcesoInsumo() != null) {
                                dp.setMontoAdjudicado(in.getProcesoInsumo().getMontoTotalAdjudicado());
                            }
                            dp.setMontoComprometido(in.getMontoComprometido());
                            if (in.getMontoPep() != null) {
                                if (in.getMontoTotalCertificado() != null) {
                                    dp.setMontoDisponibleCertificado(in.getMontoPep().subtract(in.getMontoTotalCertificado()));
                                } else {
                                    dp.setMontoDisponibleCertificado(in.getMontoPep());
                                }
                            }
                            if (in.getMontoPep() != null) {
                                if (in.getMontoComprometido() != null) {
                                    dp.setMontoDisponibleComprometido(in.getMontoPep().subtract(in.getMontoComprometido()));
                                } else {
                                    dp.setMontoDisponibleComprometido(in.getMontoPep());
                                }
                            }
                            listaDatosPOA.add(dp);
                        }
                    }
                }

            } else if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                POAConActividades poaConActividades = (POAConActividades) poa;
                listaDatosPOA = ReprogramacionesUtils.actualizarListaPOA(poaConActividades);
            }
        }
    }

    public void agregarNuevoInsumo() {

    }

    public void cambioUACI() {

    }

    /**
     * Almacenamiento de una reprogramación. Al guardar una reprogramación se
     * realiza la validación de los datos
     */
    public void guardarReprog() {
        try {
            reprog = reprogramacionDelegate.guardarReprogramacion(reprog);

            tabSeleccionado = 0;

            jSFUtils.agregarInfo(textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente"));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Envía una reprogramación para consideración. Si requiere aprobación a la
     * aprobación del PAC, cambia el estado.
     */
    public void enviarReprog() {
        try {
            reprog = reprogramacionDelegate.enviarReprogramacion(reprog);

            jSFUtils.agregarInfo(textMB.obtenerTexto("labels.LaReprogramacionSeEnvioCorrectamente"));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Aprueba una reprogramación que requiere PAC
     */
    public void aprobarEnPAC() {
        try {
            reprog = reprogramacionDelegate.aprobarEnPac(reprog);
            tabSeleccionado = 0;

            jSFUtils.agregarInfo(textMB.obtenerTexto("labels.SeAproboCorrectamente"));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Rechaza una reprogramación que requiere PAC
     */
    public void rechazarPAC() {
        try {
            reprog = reprogramacionDelegate.rechazarEnPac(reprog);

            jSFUtils.agregarInfo(textMB.obtenerTexto("labels.SeEnvioAlaUTParaSuReformulacion"));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Actualización de los datos financieros
     */
    public void actualizarFinanciera() {
        listaRFin = new ArrayList();

        for (ReprogramacionDetalle det : reprog.getRep_detalle_lista()) {
            ReprogramacionACFinanciera rep = new ReprogramacionACFinanciera(det);
            listaRFin.add(rep);

        }
    }

    /**
     * Este método aplica una reprogramación
     */
    public void aplicarReprogramacion() {
        try {
            reprog = reprogramacionDelegate.aplicarReprogramacion(reprog.getId());
            jSFUtils.agregarInfo("Se aplico correctamente la reformulación");
            tabSeleccionado = 0;
        } catch (BusinessException be) {
            for (String s : be.getErrores()) {
                jSFUtils.agregarError(s);
            }
        } catch (Exception ex) {
            jSFUtils.agregarError("ERROR_GENERAL");
        }
    }

    /**
     * Guarda una de las líneas de reprogramación.
     */
    public void guardarLineaReprog() {
        try {
            reprogEnEdicion.setPoa(comboPOA.getSelectedT());
            reprogEnEdicion.setPoaLinea(comboLineasPOA.getSelectedT());

            if (reprogEnEdicion.getNuevaActividad()) {
                reprogEnEdicion.setPoaActividad(null);
                if (idActividadNP != null) {
                    ActividadAsignacionNP actividad = (ActividadAsignacionNP) emd.getEntityById(ActividadAsignacionNP.class.getName(), idActividadNP);
                    reprogEnEdicion.setActividadNuevaAsigNP(actividad);
                }
            } else {
                reprogEnEdicion.setPoaActividad(comboActividades.getSelectedT());
                reprogEnEdicion.setActividadNuevaAsigNP(null);
            }

            if (reprogEnEdicion.getNuevoInsumo()) {
                reprogEnEdicion.setPoaInsumo(null);
            } else {
                reprogEnEdicion.setInsumoNuevo(null);
                reprogEnEdicion.setPoaInsumo(comboInsumos.getSelectedT());
            }

            if (reprog.getEstado() == EstadoReprogramacion.PENDIENTE_PAC) {
                reprogEnEdicion.setGrupoPAC(comboGrupo.getSelectedT());
            }

            if (tmpIdTramo != null) {
                ProyectoAporteTramoTramo tramo = (ProyectoAporteTramoTramo) emd.getEntityById(ProyectoAporteTramoTramo.class.getName(), tmpIdTramo);
                reprogEnEdicion.setInsumoNuevoTramo(tramo);
            }

            //Si el insumo modificado es No Uaci y tiene una sola fuente, se copian los montos
            if (reprogEnEdicion.getInsumoNuevoMontosFuentes().size() == 1) {
                for (POMontoFuenteInsumo montoFuente : reprogEnEdicion.getInsumoNuevoMontosFuentes()) {
                    if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                        for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                            copiarMontoMesReprogAMontoFuenteFCM(montoFuenteFCM, reprogEnEdicion);
                        }
                    }
                }
            }

            ValidacionReprogramacionDetalle.validar(reprogEnEdicion, reprog.getEstado());

            if (reprogEnEdicion.getReprogramacionId() == null) {
                reprogEnEdicion.setReprogramacionId(reprog);
                reprog.getRep_detalle_lista().add(reprogEnEdicion);
            }

            actualizarFinanciera();
            RequestContext.getCurrentInstance().execute("$('#editarDetalleModal').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Copia el monto de un mes de la programación financiera del insumo en el
     * pago de la fuente en ese mes
     *
     * @param montoFuenteFCM
     * @param repDetalle
     */
    private void copiarMontoMesReprogAMontoFuenteFCM(POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM, ReprogramacionDetalle repDetalle) {
        Integer mes = montoFuenteFCM.getFlujoCajaMensual().getMes();
        switch (mes) {
            case 1:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes1());
                break;
            case 2:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes2());
                break;
            case 3:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes3());
                break;
            case 4:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes4());
                break;
            case 5:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes5());
                break;
            case 6:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes6());
                break;
            case 7:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes7());
                break;
            case 8:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes8());
                break;
            case 9:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes9());
                break;
            case 10:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes10());
                break;
            case 11:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes11());
                break;
            case 12:
                montoFuenteFCM.setMonto(repDetalle.getRdeMes12());
                break;
        }
    }

    /**
     * este método retorna el nombre del pac del detalle en edición
     *
     * @return
     */
    public String getNombrePAC() {
        if (reprogEnEdicion != null && reprogEnEdicion.getPoa() != null) {
            GeneralPOA poa = reprogEnEdicion.getPoa();
            PAC pac = reprogramacionDelegate.obtenerPACPorPOAId(poa.getId());
            if (pac != null) {
                return pac.getNombre();
            }
        }
        return null;
    }

    /**
     * Este método recalcula el monto total del insumo, en base al unitario y la
     * cantidad.
     */
    public void recalcualrMontoTotalReprogEnEdicion() {
        try {
            reprogEnEdicion.setInsumoNuevoTotal(reprogEnEdicion.getInsumoNuevoUnitario().multiply(new BigDecimal(reprogEnEdicion.getInsumoNuevoCantidad())));
        } catch (Exception e) {
        }
    }

    private void crearRelacionesMontoFuenteInsumoConFlujoDeCajMensual() {
        if (reprogEnEdicion != null && reprogEnEdicion.getPoa() != null) {
            Integer anio = reprogEnEdicion.getPoa().getAnioFiscal().getAnio();
            FlujoCajaAnio fca = FlujoCajaMensualUtils.generarFCM(anio);
            for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                fcm.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                for (POMontoFuenteInsumo montoFuenteInsumo : reprogEnEdicion.getInsumoNuevoMontosFuentes()) {
                    if (montoFuenteInsumo.getMontosFuentesInsumosFCM() == null) {
                        montoFuenteInsumo.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                    }
                    POMontoFuenteInsumoFlujoCajaMensual montoFuenteInsumoFCM = new POMontoFuenteInsumoFlujoCajaMensual();
                    montoFuenteInsumoFCM.setFlujoCajaMensual(fcm);
                    montoFuenteInsumoFCM.setMontoFuenteInsumo(montoFuenteInsumo);
                    fcm.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                    montoFuenteInsumo.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                }
            }
        }
    }

    public List<POMontoFuenteInsumoFlujoCajaMensual> obtenerFuentesFCMPorMes(Integer numeroMes) {
        List<POMontoFuenteInsumoFlujoCajaMensual> listaFuentesFCM = new LinkedList<>();
        if (reprogEnEdicion != null && reprogEnEdicion.getInsumoNuevoMontosFuentes() != null) {
            for (POMontoFuenteInsumo montoFuenteInsumo : reprogEnEdicion.getInsumoNuevoMontosFuentes()) {
                if (montoFuenteInsumo.getMontosFuentesInsumosFCM() != null) {
                    for (POMontoFuenteInsumoFlujoCajaMensual fuenteFCM : montoFuenteInsumo.getMontosFuentesInsumosFCM()) {
                        if (fuenteFCM.getFlujoCajaMensual().getMes().equals(numeroMes)) {
                            listaFuentesFCM.add(fuenteFCM);
                        }
                    }
                }
            }
            Collections.sort(listaFuentesFCM, new Comparator<POMontoFuenteInsumoFlujoCajaMensual>() {
                @Override
                public int compare(POMontoFuenteInsumoFlujoCajaMensual o1, POMontoFuenteInsumoFlujoCajaMensual o2) {
                    return o1.getMontoFuenteInsumo().getFuente().getFuenteRecursos().getCodigo().compareTo(o2.getMontoFuenteInsumo().getFuente().getFuenteRecursos().getCodigo());
                }
            });
        }
        return listaFuentesFCM;
    }

    /**
     * Este método retorna si la reprogramación esta en edición
     *
     * @return
     */
    public Boolean estaEnFormulacion() {
        return EstadoReprogramacion.FORMULACION.equals(reprog.getEstado());
    }

    /**
     * Ordena los montos fuentes por código de fuente de recurso
     *
     * @param listaMontosFuentes
     * @return
     */
    public List<POMontoFuenteInsumo> ordenarMontosFuentes(List<POMontoFuenteInsumo> listaMontosFuentes) {
        if (listaMontosFuentes != null) {
            Collections.sort(listaMontosFuentes, new Comparator<POMontoFuenteInsumo>() {
                @Override
                public int compare(POMontoFuenteInsumo o1, POMontoFuenteInsumo o2) {
                    return o1.getFuente().getFuenteRecursos().getCodigo().compareTo(o2.getFuente().getFuenteRecursos().getCodigo());
                }
            });
        }
        return listaMontosFuentes;
    }

    /**
     * Calcula la suma de montos ingresados en cada uno de los meses de la
     * programación financiera del insumo
     *
     * @return
     */
    public void actualizarTotalMesesInsumo() {
        totalMesesInsumo = BigDecimal.ZERO;
        if (reprogEnEdicion != null) {
            if (reprogEnEdicion.getRdeMes1() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes1());
            }
            if (reprogEnEdicion.getRdeMes2() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes2());
            }
            if (reprogEnEdicion.getRdeMes3() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes3());
            }
            if (reprogEnEdicion.getRdeMes4() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes4());
            }
            if (reprogEnEdicion.getRdeMes5() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes5());
            }
            if (reprogEnEdicion.getRdeMes6() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes6());
            }
            if (reprogEnEdicion.getRdeMes7() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes7());
            }
            if (reprogEnEdicion.getRdeMes8() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes8());
            }
            if (reprogEnEdicion.getRdeMes9() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes9());
            }
            if (reprogEnEdicion.getRdeMes10() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes10());
            }
            if (reprogEnEdicion.getRdeMes11() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes11());
            }
            if (reprogEnEdicion.getRdeMes12() != null) {
                totalMesesInsumo = totalMesesInsumo.add(reprogEnEdicion.getRdeMes12());
            }
        }
    }

    /**
     * Calcula el total de los montos de las fuentes del insumo en los 12 meses
     */
    public void calcularTotalProgramacionFinancieraMontosFuentes() {
        if (reprogEnEdicion != null && reprogEnEdicion.getInsumoNuevoMontosFuentes() != null) {
            for (POMontoFuenteInsumo montoFuente : reprogEnEdicion.getInsumoNuevoMontosFuentes()) {
                montoFuente.setTotalProgramacionFinanciera(BigDecimal.ZERO);
                if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                    for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                        if (montoFuenteFCM.getMonto() != null) {
                            montoFuente.setTotalProgramacionFinanciera(montoFuente.getTotalProgramacionFinanciera().add(montoFuenteFCM.getMonto()));
                        }
                    }
                }
            }
        }
    }

    private void resetearMontosFuentesFCM() {
        if (reprogEnEdicion != null && reprogEnEdicion.getInsumoNuevoMontosFuentes() != null) {
            for (POMontoFuenteInsumo montoFuente : reprogEnEdicion.getInsumoNuevoMontosFuentes()) {
                montoFuente.setTotalProgramacionFinanciera(BigDecimal.ZERO);
                if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                    for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                        montoFuenteFCM.setMonto(BigDecimal.ZERO);
                    }
                }
            }
        }
    }

    public List<POMontoFuenteInsumo> obtenerListaOrdenadaMontosFuentesReprogEnEdicion() {
        List<POMontoFuenteInsumo> listaMontosFuentes = new LinkedList<>();
        if (reprogEnEdicion != null && reprogEnEdicion.getInsumoNuevoMontosFuentes() != null) {
            listaMontosFuentes = reprogEnEdicion.getInsumoNuevoMontosFuentes();
            if (listaMontosFuentes != null) {
                Collections.sort(listaMontosFuentes, new Comparator<POMontoFuenteInsumo>() {
                    @Override
                    public int compare(POMontoFuenteInsumo o1, POMontoFuenteInsumo o2) {
                        return o1.getFuente().getFuenteRecursos().getCodigo().compareTo(o2.getFuente().getFuenteRecursos().getCodigo());
                    }
                });
            }
        }
        return listaMontosFuentes;
    }

    public BigDecimal calcularDifTotalPACPorMes(Integer mes) {
        BigDecimal totalMes = BigDecimal.ZERO;
        if (listaRFin != null) {
            for (ReprogramacionACFinanciera repACFin : listaRFin) {
                switch (mes) {
                    case 1:
                        if (repACFin.getPacmes1() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes1());
                        }
                        break;
                    case 2:
                        if (repACFin.getPacmes2() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes2());
                        }
                        break;
                    case 3:
                        if (repACFin.getPacmes3() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes3());
                        }
                        break;
                    case 4:
                        if (repACFin.getPacmes4() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes4());
                        }
                        break;
                    case 5:
                        if (repACFin.getPacmes5() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes5());
                        }
                        break;
                    case 6:
                        if (repACFin.getPacmes6() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes6());
                        }
                        break;
                    case 7:
                        if (repACFin.getPacmes7() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes7());
                        }
                        break;
                    case 8:
                        if (repACFin.getPacmes8() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes8());
                        }
                        break;
                    case 9:
                        if (repACFin.getPacmes9() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes9());
                        }
                        break;
                    case 10:
                        if (repACFin.getPacmes10() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes10());
                        }
                        break;
                    case 11:
                        if (repACFin.getPacmes11() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes11());
                        }
                        break;
                    case 12:
                        if (repACFin.getPacmes12() != null) {
                            totalMes = totalMes.add(repACFin.getPacmes12());
                        }
                        break;
                    case 13:
                        if (repACFin.getTotalPAC() != null) {
                            totalMes = totalMes.add(repACFin.getTotalPAC());
                        }
                        break;
                }
            }
        }
        return totalMes;
    }

    /**
     * PEP menos monto certificado
     * @param fin
     * @return 
     */
    public BigDecimal calcularMontoComprometidoInsumo(ReprogramacionACFinanciera fin) {
        BigDecimal montoComprometido = BigDecimal.ZERO;
        if (fin.getInsumo() != null) {
            if (fin.getInsumo().getMontoPep() != null) {
                montoComprometido = fin.getInsumo().getMontoPep();
                if(fin.getInsumo().getMontoTotalCertificado()!=null){
                    montoComprometido = montoComprometido.subtract(fin.getInsumo().getMontoTotalCertificado());
                }
            }
        }
        return montoComprometido;
    }
    
    /**
     * PEP menos monto comprometido
     * @param fin
     * @return 
     */
    public BigDecimal calcularMontoDisponibleInsumo(ReprogramacionACFinanciera fin) {
        BigDecimal montoComprometido = BigDecimal.ZERO;
        if (fin.getInsumo() != null) {
            if (fin.getInsumo().getMontoPep() != null) {
                montoComprometido = fin.getInsumo().getMontoPep();
                if(fin.getInsumo().getMontoComprometido()!=null){
                    montoComprometido = montoComprometido.subtract(fin.getInsumo().getMontoComprometido());
                }
            }
        }
        return montoComprometido;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public SofisComboG<Proyecto> getComboProyecto() {
        return comboProyecto;
    }

    public void setComboProyecto(SofisComboG<Proyecto> comboProyecto) {
        this.comboProyecto = comboProyecto;
    }

    public SofisComboG<POActividadBase> getComboActividades() {
        return comboActividades;
    }

    public void setComboActividades(SofisComboG<POActividadBase> comboActividades) {
        this.comboActividades = comboActividades;
    }

    public SofisComboG<POInsumos> getComboInsumos() {
        return comboInsumos;
    }

    public void setComboInsumos(SofisComboG<POInsumos> comboInsumos) {
        this.comboInsumos = comboInsumos;
    }

    public POInsumos getInsumoEnEdicion() {
        return insumoEnEdicion;
    }

    public void setInsumoEnEdicion(POInsumos insumoEnEdicion) {
        this.insumoEnEdicion = insumoEnEdicion;
    }

    public List<POFlujoCajaMenusal> getFlujoMensual() {
        return flujoMensual;
    }

    public void setFlujoMensual(List<POFlujoCajaMenusal> flujoMensual) {
        this.flujoMensual = flujoMensual;
    }

    public ReprogramacionDetalle getReprogEnEdicion() {
        return reprogEnEdicion;
    }

    public void setReprogEnEdicion(ReprogramacionDetalle reprogEnEdicion) {
        this.reprogEnEdicion = reprogEnEdicion;
    }

    public Reprogramacion getReprog() {
        return reprog;
    }

    public void setReprog(Reprogramacion reprog) {
        this.reprog = reprog;
    }

    public SofisComboG<POLinea> getComboLineasPOA() {
        return comboLineasPOA;
    }

    public void setComboLineasPOA(SofisComboG<POLinea> comboLineasPOA) {
        this.comboLineasPOA = comboLineasPOA;
    }

    public Integer getTabSeleccionado() {
        return tabSeleccionado;
    }

    public void setTabSeleccionado(Integer tabSeleccionado) {
        this.tabSeleccionado = tabSeleccionado;
    }

    public List<ReprogramacionACFinanciera> getListaRFin() {
        return listaRFin;
    }

    public void setListaRFin(List<ReprogramacionACFinanciera> listaRFin) {
        this.listaRFin = listaRFin;
    }

    public Boolean getModoProcesarPAC() {
        modoProcesarPAC = EstadoReprogramacion.PENDIENTE_PAC.equals(reprog.getEstado());
        //Controlar permisos
        return modoProcesarPAC;
    }

    public void setModoProcesarPAC(Boolean modoProcesarPAC) {
        this.modoProcesarPAC = modoProcesarPAC;
    }

    public Boolean getRequiereAPROBACION() {
        return requiereAPROBACION;
    }

    public void setRequiereAPROBACION(Boolean requiereAPROBACION) {
        this.requiereAPROBACION = requiereAPROBACION;
    }

    public SofisComboG<PACGrupo> getComboGrupo() {
        return comboGrupo;
    }

    public void setComboGrupo(SofisComboG<PACGrupo> comboGrupo) {
        this.comboGrupo = comboGrupo;
    }

    public SofisComboG<GeneralPOA> getComboPOA() {
        return comboPOA;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public SofisComboG<GeneralPOA> getComboPOAVisualizar() {
        return comboPOAVisualizar;
    }

    public void setComboPOAVisualizar(SofisComboG<GeneralPOA> comboPOAVisualizar) {
        this.comboPOAVisualizar = comboPOAVisualizar;
    }

    public POAProyectoDelegate getpOAProyectoDelegate() {
        return pOAProyectoDelegate;
    }

    public void setpOAProyectoDelegate(POAProyectoDelegate pOAProyectoDelegate) {
        this.pOAProyectoDelegate = pOAProyectoDelegate;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public ProyectoDelegate getPyDelegate() {
        return pyDelegate;
    }

    public void setPyDelegate(ProyectoDelegate pyDelegate) {
        this.pyDelegate = pyDelegate;
    }

    public GeneralDelegate getGeneralDelegate() {
        return generalDelegate;
    }

    public void setGeneralDelegate(GeneralDelegate generalDelegate) {
        this.generalDelegate = generalDelegate;
    }

    public ReprogramacionDelegate getReprogramacionDelegate() {
        return reprogramacionDelegate;
    }

    public void setReprogramacionDelegate(ReprogramacionDelegate reprogramacionDelegate) {
        this.reprogramacionDelegate = reprogramacionDelegate;
    }

    public Integer getTmpIdTramo() {
        return tmpIdTramo;
    }

    public void setTmpIdTramo(Integer tmpIdTramo) {
        this.tmpIdTramo = tmpIdTramo;
    }

    public void setComboPOA(SofisComboG<GeneralPOA> comboPOA) {
        this.comboPOA = comboPOA;
    }

    public Integer getIdActividadNP() {
        return idActividadNP;
    }

    public void setIdActividadNP(Integer idActividadNP) {
        this.idActividadNP = idActividadNP;
    }

    public List<DataPOAProyectoView> getListaDatosPOA() {
        return listaDatosPOA;
    }

    public void setListaDatosPOA(List<DataPOAProyectoView> listaDatosPOA) {
        this.listaDatosPOA = listaDatosPOA;
    }

    public Integer getCantidadMontosFuentesDePoInsumo() {
        return cantidadMontosFuentesDePoInsumo;
    }

    public void setCantidadMontosFuentesDePoInsumo(Integer cantidadMontosFuentesDePoInsumo) {
        this.cantidadMontosFuentesDePoInsumo = cantidadMontosFuentesDePoInsumo;
    }

    public BigDecimal getTotalMesesInsumo() {
        return totalMesesInsumo;
    }

    public void setTotalMesesInsumo(BigDecimal totalMesesInsumo) {
        this.totalMesesInsumo = totalMesesInsumo;
    }

    // </editor-fold>
}
