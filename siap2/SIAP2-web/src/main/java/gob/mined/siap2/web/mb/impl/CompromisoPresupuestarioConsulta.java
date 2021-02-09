/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioModificativa;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioProceso;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.enums.EstadoCompromiso;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "compromisoPresupuestarioConsulta")
public class CompromisoPresupuestarioConsulta implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    UtilsMB utilsMB;
    @Inject
    UsuarioInfo unsiInfo;
    @Inject
    ProcesoAdquisicionDelegate procesoAdquisicionDelegate;
    @Inject
    ContratoDelegate contratoDelegate;

    @Inject
    TextMB textMB;

    @Inject
    ReporteDelegate reporteDelegate;

    private LazyDataModel<CompromisoPresupuestario> lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    Map<String, String> criteriosDisponibles = new HashMap();

    private String criterioSelecionado;
    private String numeroProcesoAdq;
    private String idAnioFiscal;
    private EstadoCompromiso estado;
    private String idMetodoAdq;
    private PasosProcesoAdquisicion estadoProceso;
    private CompromisoPresupuestario compromisoEnEdicion;
    private String idProgramaPres;
    private String idSubProgramaPres;
    private String idProyecto;
    private List<POInsumos> listaPoInsumosADescomprometer;

    private static final String TODOS_COMPROMISOS = "TODOS_COMPROMISOS";
    private static final String COMPROMISOS_ASIGNADOS_AL_USUARIO = "COMPROMISOS_ASIGNADOS_AL_USUARIO";

    @PostConstruct
    public void init() {
        initFilter();
        filterTable();
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        numeroProcesoAdq = null;
        idAnioFiscal = null;
        estado = null;
        idMetodoAdq = null;
        estadoProceso = null;
        compromisoEnEdicion = null;
        idProgramaPres = null;
        idSubProgramaPres = null;
        idProyecto = null;

        criteriosDisponibles = new HashMap();
        criterioSelecionado = COMPROMISOS_ASIGNADOS_AL_USUARIO;
        criteriosDisponibles.put(textMB.obtenerTexto("labels." + COMPROMISOS_ASIGNADOS_AL_USUARIO), COMPROMISOS_ASIGNADOS_AL_USUARIO);
        if (permisosMB.tieneOperacion(ConstantesEstandares.Operaciones.VER_TODOS_COMPROMISOS)) {
            criterioSelecionado = TODOS_COMPROMISOS;
            criteriosDisponibles.put(textMB.obtenerTexto("labels." + TODOS_COMPROMISOS), TODOS_COMPROMISOS);
        }

    }

    //VER_TODOS_COMPROMISOS
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (criterioSelecionado.equals(COMPROMISOS_ASIGNADOS_AL_USUARIO)
                    || !permisosMB.tieneOperacion(ConstantesEstandares.Operaciones.VER_TODOS_COMPROMISOS)) {

                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.responsable.usuCod", unsiInfo.getUserCod());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(numeroProcesoAdq)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.secuenciaNumero", NumberUtils.getIntegerONull(numeroProcesoAdq));
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(idAnioFiscal)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.anio.id", Integer.valueOf(idAnioFiscal));
                criterios.add(criterio);
            }
            if (estado != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", estado);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(idMetodoAdq)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.metodoAdquisicion.id", Integer.valueOf(idMetodoAdq));
                criterios.add(criterio);
            }

            if (estadoProceso != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "procesoAdquisicion.estado", estadoProceso);
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String className = CompromisoPresupuestario.class.getName();
            String[] orderBy = {"id"};
            boolean[] asc = {false};

            IDataProvider dataProvider = new EntityDataProvider(className, condicion, orderBy, asc);
            LazyDataModel tmp = new GeneralLazyDataModel(dataProvider);
            lazyModel = tmp;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    /**
     * Carga un compromiso presupuestario
     *
     * @param id
     */
    public void cargarCompromido(Integer id) {
        compromisoEnEdicion = (CompromisoPresupuestario) emd.getEntityById(CompromisoPresupuestario.class.getName(), id);

    }

    /**
     * Emite un compromiso presupuestario
     */
    public void emitirCompromidoPresupuestario() {
        try {
            compromisoEnEdicion = procesoAdquisicionDelegate.emitirCompromisoPresupuestario(compromisoEnEdicion.getId());
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }

    }

    /**
     * Genera y descarga el reporte de un compromiso presupuestario
     */
    public void descargarReporteCompromisoPresupuestario() {
        try {

            byte[] bytespdf = reporteDelegate.generarCompromisoPresupuestario(compromisoEnEdicion.getId());
            String nombreArchivo = "CompromisoPresupuestario_" + compromisoEnEdicion.getId() + ".pdf";
            ArchivoUtils.downloadPdfFromBytes(bytespdf, nombreArchivo);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Genera la reserva presupuestaria
     */
    public void generarReservaDeFondos() {
        try {
            compromisoEnEdicion = contratoDelegate.generarReservaDeFondos(compromisoEnEdicion.getId());

            String texto = textMB.obtenerTexto("labels.ReservaDeFondosGeneradaCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Abre el modal con los insumos asociados al proceso o la modificativa del
     * compromiso presupuestario seleccionado
     *
     * @param idCompromisoPresupuestario
     */
    public void verPoInsumosDelCompromiso(Integer idCompromisoPresupuestario) {
        listaPoInsumosADescomprometer = new LinkedList<>();
        listaPoInsumosADescomprometer = contratoDelegate.getPoInsumosCompromisoPresupuestario(idCompromisoPresupuestario);
    }

    /**
     * Valida el monto a descomprometer de cada insumo y actualiza el monto
     * comprometido
     */
    public void confirmarDescomprometerInsumos() {
        try {
            contratoDelegate.confirmarDescomprometerInsumos(listaPoInsumosADescomprometer);
            RequestContext.getCurrentInstance().execute("$('#verCompromisoPresupuestario').modal('hide');");       
        
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

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public String getIdMetodoAdq() {
        return idMetodoAdq;
    }

    public void setIdMetodoAdq(String idMetodoAdq) {
        this.idMetodoAdq = idMetodoAdq;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public EstadoCompromiso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompromiso estado) {
        this.estado = estado;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel<CompromisoPresupuestario> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<CompromisoPresupuestario> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public PermisosMB getPermisosMB() {
        return permisosMB;
    }

    public CompromisoPresupuestario getCompromisoEnEdicion() {
        return compromisoEnEdicion;
    }

    public void setCompromisoEnEdicion(CompromisoPresupuestario compromisoEnEdicion) {
        this.compromisoEnEdicion = compromisoEnEdicion;
    }

    public CompromisoPresupuestarioProceso getCompromisoEnEdicionProceso() {
        return (CompromisoPresupuestarioProceso) compromisoEnEdicion;
    }

    public void setCompromisoEnEdicionProceso(CompromisoPresupuestarioProceso compromisoEnEdicion) {
        this.compromisoEnEdicion = compromisoEnEdicion;
    }

    public CompromisoPresupuestarioModificativa getCompromisoEnEdicionModificativa() {
        return (CompromisoPresupuestarioModificativa) compromisoEnEdicion;
    }

    public void setCompromisoEnEdicionModificativa(CompromisoPresupuestarioProceso compromisoEnEdicion) {
        this.compromisoEnEdicion = compromisoEnEdicion;
    }

    public void setPermisosMB(PermisosMB permisosMB) {
        this.permisosMB = permisosMB;
    }

    public static Logger getLogger() {
        return logger;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public String getNumeroProcesoAdq() {
        return numeroProcesoAdq;
    }

    public PasosProcesoAdquisicion getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(PasosProcesoAdquisicion estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public void setNumeroProcesoAdq(String numeroProcesoAdq) {
        this.numeroProcesoAdq = numeroProcesoAdq;
    }

    public Map<String, String> getCriteriosDisponibles() {
        return criteriosDisponibles;
    }

    public void setCriteriosDisponibles(Map<String, String> criteriosDisponibles) {
        this.criteriosDisponibles = criteriosDisponibles;
    }

    public String getCriterioSelecionado() {
        return criterioSelecionado;
    }

    public void setCriterioSelecionado(String criterioSelecionado) {
        this.criterioSelecionado = criterioSelecionado;
    }

    public String getIdProgramaPres() {
        return idProgramaPres;
    }

    public void setIdProgramaPres(String idProgramaPres) {
        this.idProgramaPres = idProgramaPres;
    }

    public String getIdSubProgramaPres() {
        return idSubProgramaPres;
    }

    public void setIdSubProgramaPres(String idSubProgramaPres) {
        this.idSubProgramaPres = idSubProgramaPres;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public List<POInsumos> getListaPoInsumosADescomprometer() {
        return listaPoInsumosADescomprometer;
    }

    public void setListaPoInsumosADescomprometer(List<POInsumos> listaPoInsumosADescomprometer) {
        this.listaPoInsumosADescomprometer = listaPoInsumosADescomprometer;
    }

    // </editor-fold>
}
