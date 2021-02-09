/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.caux.AsistenciaSeccion;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumMes;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaCabezalRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ReporteAsistenciaSedeBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReporteAsistenciaSedeBean.class.getName());

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private ControlAsistenciaCabezalRestClient cabezalClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CalendarioRestClient calendarioRestClient;

    @Inject
    @Param(name = "sed")
    private Long sedPk;

    // mapeo los elementos seleccionados en los combos del componente para generar el reporte
    @Inject
    private FiltrosSeccionBean filtrosSeccionBean;

    private SgSeccion entidadEnEdicion;
    private List<AsistenciaSeccion> asistenciasSecciones = new ArrayList<>();
    private List<Integer> dias = new ArrayList<>();
    private FiltroControlAsistenciaCabezal filCabezal = new FiltroControlAsistenciaCabezal();
    private SofisComboG<EnumMes> comboMeses;

    public ReporteAsistenciaSedeBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            // sólo una vez
            for (int i = 1; i <= 31; i++) {
                dias.add(i);
            }
            inicializarFiltrosSeccion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        cargarMes();
    }

    private void cargarMes() {
        comboMeses = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
        comboMeses.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        EnumMes mes = EnumMes.ENERO; // default
        int mesActual = LocalDate.now().getMonthValue();
        // busco mes actual
        for (EnumMes m : EnumMes.values()) {
            if (m.getNumero() == mesActual) {
                mes = m;
                break;
            }
        }
        comboMeses.setSelectedT(mes);
    }

    public void inicializarFiltrosSeccion() {
        this.filtrosSeccionBean.setFiltro(filCabezal);
        this.filtrosSeccionBean.cargarCombos();
        this.filtrosSeccionBean.seleccionarUltimoAnio();
        if (sedPk != null) {
            try {
                FiltroSedes fs = new FiltroSedes();
                fs.setSedPk(sedPk);
                fs.setIncluirCampos(new String[]{"sedVersion", "sedCodigo", "sedNombre", "sedTipo"});
                List<SgSede> sedes = sedeClient.buscar(fs);
                if (!sedes.isEmpty()) {
                    this.filtrosSeccionBean.cargarSedeSeleccionada(sedes.get(0));
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_SEDES_ASISTENCIAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String getTituloPagina() {
        return Etiquetas.getValue("reporteAsistenciasSedes");
    }

    public void generar() {
        try {

            // No se realizan validaciones ya que no se muestra botón generar hasta seleccionar desde vista los campos obligatorios.
            // Array asociado a lista mostrada en pantalla
            asistenciasSecciones = new ArrayList<>();

            // Para el filtro así obtengo el rango de fechas con YearMonth
            Integer mes = comboMeses.getSelectedT().getNumero();
            Integer anio = filCabezal.getSecAnioLectivoAnio();
            
            LocalDate fechaDesde=YearMonth.of(anio, mes).atDay(1);
            LocalDate fechaHasta=YearMonth.of(anio, mes).atEndOfMonth();
            
            if(filCabezal.getSecSedeFk()!=null && filtrosSeccionBean.getEsInternacional()){
                FiltroCalendario fc=new FiltroCalendario();
                fc.setAnioLectivo(anio);
                fc.setTipoCalendarioPk(filtrosSeccionBean.getSedeSeleccionada().getSedTipoCalendario().getTcePk());
                fc.setIncluirCampos(new String[]{"calTipoCalendario.tceCodigo","calFechaInicio","calFechaFin"});
                List<SgCalendario> cal=calendarioRestClient.buscar(fc);
                
                if(YearMonth.from(fechaDesde).compareTo(YearMonth.from(cal.get(0).getCalFechaInicio()))<0){
                    
                    fechaDesde=YearMonth.of(anio +1, mes).atDay(1);
                    fechaHasta=YearMonth.of(anio+1, mes).atEndOfMonth();
                }
            }

            // obtengo cabezales asociados a los filtros
            filCabezal.setCacDesde(fechaDesde);
            filCabezal.setCacHasta(fechaHasta);
            filCabezal.setIncluirCampos(new String[]{"cacSeccion.secPk", "cacFecha"});
            filCabezal.setOrderBy(new String[]{"cacSeccion.secServicioEducativo.sduOpcion.opcModalidad.modCiclo.cicNivel.nivOrden"});
            filCabezal.setAscending(new boolean[]{true});
            List<SgControlAsistenciaCabezal> cabezales = cabezalClient.buscar(filCabezal);

            //obtengo secciones asociadas a los filtros
            FiltroSeccion filtroSeccion = filCabezal;
            filtroSeccion.setIncluirCampos(new String[]{"secPk", "secNombre", "secJornadaLaboral.jlaNombre", "secServicioEducativo.sduGrado.graNombre"});
            filtroSeccion.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                "secServicioEducativo.sduGrado.graOrden",
                "secCodigo"});
            filtroSeccion.setAscending(new boolean[]{true, true, true});
            filtroSeccion.setSecEstado(EnumSeccionEstado.ABIERTA);
            List<SgSeccion> secciones = seccionClient.buscar(filtroSeccion);

            LinkedHashMap<SgSeccion, List<SgControlAsistenciaCabezal>> seccionCabezales = new LinkedHashMap<>();

            for (SgSeccion sec : secciones) {
                List<SgControlAsistenciaCabezal> cabezalesSeccionAux = new ArrayList<>();
                for (SgControlAsistenciaCabezal cab : cabezales) {
                    if (cab.getCacSeccion().getSecPk().equals(sec.getSecPk())) {
                        cabezalesSeccionAux.add(cab);
                    }
                }
                seccionCabezales.put(sec, cabezalesSeccionAux);
            }

            // cargo las asistencias de cada sede para los días del mes.
            for (SgSeccion sec : seccionCabezales.keySet()) {
                AsistenciaSeccion asistenciaSeccion = new AsistenciaSeccion();
                // cabezales desde día 1 hasta el último

                List<SgControlAsistenciaCabezal> cabezalesSec = seccionCabezales.get(sec);

                String[] asistencia = new String[31];
                Arrays.fill(asistencia, "N");
                // vacío días fuera del mes
                for (int i = (YearMonth.of(anio, mes).lengthOfMonth()); i < 31; i++) {
                    asistencia[i] = "";
                }
                // cada cabezal contiene la fecha de cuando se pasó la lista para cierto mes, obtengo el día y guardo como pasada la lista.
                for (SgControlAsistenciaCabezal cab : cabezalesSec) {
                    int dia = cab.getCacFecha().getDayOfMonth();
                    asistencia[dia - 1] = "+";
                }
                asistenciaSeccion.setAsistencias(Arrays.asList(asistencia));
                asistenciaSeccion.setSeccion(sec);
                asistenciasSecciones.add(asistenciaSeccion);
            }
            if (secciones.size() == 0) {
                LOGGER.log(Level.INFO, "no hay secciones");
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_NO_HAY_SECCIONES), "");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String estiloAsistenciaSeccion(String asistencia) {
        switch (asistencia) {
            case "+":
                return "glyphicon glyphicon-ok asistencia";
            case "N":
                return "glyphicon glyphicon-remove asistencia";
            default:
                return "";
        }
    }

    public void limpiar() {
        filtrosSeccionBean.limpiarCombos();
        this.filtrosSeccionBean.seleccionarUltimoAnio();
        cargarMes();
        asistenciasSecciones = new ArrayList<>();
    }

    public SgSeccion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSeccion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<AsistenciaSeccion> getAsistenciasSecciones() {
        return asistenciasSecciones;
    }

    public void setAsistenciasSecciones(List<AsistenciaSeccion> asistenciasSecciones) {
        this.asistenciasSecciones = asistenciasSecciones;
    }

    public List<Integer> getDias() {
        return dias;
    }

    public void setDias(List<Integer> dias) {
        this.dias = dias;
    }

    public SofisComboG<EnumMes> getComboMeses() {
        return comboMeses;
    }

    public void setComboMeses(SofisComboG<EnumMes> comboMeses) {
        this.comboMeses = comboMeses;
    }

    public FiltroControlAsistenciaCabezal getFilCabezal() {
        return filCabezal;
    }

    public void setFilCabezal(FiltroControlAsistenciaCabezal filCabezal) {
        this.filCabezal = filCabezal;
    }
}
