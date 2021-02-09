/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgDatoSaludEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgFichaSalud;
import sv.gob.mined.siges.web.dto.SgHabitosAlimentacion;
import sv.gob.mined.siges.web.dto.SgHabitosPersonales;
import sv.gob.mined.siges.web.dto.SgMedidasExamenFisico;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgEnfermedadNoTransmisible;
import sv.gob.mined.siges.web.dto.catalogo.SgTiemposComidaDia;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoApoyo;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDatoSaludEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFichaSalud;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabitosAlimentacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabitosPersonales;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMedidasExamenFisico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTiposApoyo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DatosSaludEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.FichaSaludRestClient;
import sv.gob.mined.siges.web.restclient.HabitosAlimentacionRestClient;
import sv.gob.mined.siges.web.restclient.HabitosPersonalesRestClient;
import sv.gob.mined.siges.web.restclient.MedidasExamenFisicoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DatosSaludEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DatosSaludEstudianteBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DatosSaludEstudianteRestClient datosSaludEstudianteRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;

    @Inject
    private FichaSaludRestClient fichaSaludRestClient;

    @Inject
    private MedidasExamenFisicoRestClient medidasExamenFisicoRestClient;

    @Inject
    private HabitosPersonalesRestClient habitosPersonalesRestClient;

    @Inject
    private HabitosAlimentacionRestClient habitosAlimentacionRestClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    private SgEstudiante estudiante;

    private Integer paginado = 10;
    private Boolean soloLectura = Boolean.FALSE;

    private SofisComboG<SgAnioLectivo> comboAnioLectivos;
    private SofisComboG<SgAnioLectivo> comboAnioLectivosInsert;
    private SofisComboG<SgTipoApoyo> comboTipoApoyo;
    private List<SgDatoSaludEstudiante> datosSalud;
    private SgAnioLectivo anioSeleccionado;
    private SgAnioLectivo anioSeleccionadoInsert;
    private SgTipoApoyo tipoApoyoSeleccionado;
    private SgDatoSaludEstudiante datoSaludEstudiante;
    private SgFichaSalud fichaSaludEnEdicion;
    private List<SgMedidasExamenFisico> medidasExFiscoList;
    private SgMedidasExamenFisico medidasExFiscoEnEdicion;
    private List<SgHabitosAlimentacion> habitoAlimentacionPersonalList;
    private SgHabitosAlimentacion habitoAlimentacionPersonalEnEdicion;
    private List<SgHabitosPersonales> habitosPersonalesList;
    private SgHabitosPersonales habitoPersonalEnEdicion;
    private String msjConfiguracionFichaSalud;

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            agregar();
            buscarConfiguracion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void buscarConfiguracion(){
        try{
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.FICHA_SALUD_MSJ_CONF);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if(!conf.isEmpty()){
                msjConfiguracionFichaSalud=conf.get(0).getConValor();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        datoSaludEstudiante = new SgDatoSaludEstudiante();
        agregarFichaSalud();
        agregarHabitosAlimentacionPersonal();
        agregarHabitosPersonal();
        agregarMedidasExamenFisico();
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);

            FiltroTiposApoyo fta = new FiltroTiposApoyo();
            fta.setHabilitado(Boolean.TRUE);
            fta.setAplicaEstudiante(Boolean.TRUE);
            fta.setOrderBy(new String[]{"tapNombre"});
            fta.setIncluirCampos(new String[]{"tapNombre", "tapVersion"});
            List<SgTipoApoyo> lTipoApoyo = catalogoRestClient.buscarTipoApoyo(fta);
            comboTipoApoyo = new SofisComboG(lTipoApoyo, "tapNombre");
            comboTipoApoyo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroAnioLectivo fanio = new FiltroAnioLectivo();
            fanio.setAleSedeFk(estudiante != null ? estudiante.getEstUltimaSedePk() : null);
            fanio.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            fanio.setAscending(new boolean[]{false});
            fanio.setOrderBy(new String[]{"aleAnio"});
            List<SgAnioLectivo> anios = anioLectivoRestClient.buscar(fanio);

            comboAnioLectivos = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            for (SgAnioLectivo anio : anios) {
                if (anio.getAleEstado().equals(EnumAnioLectivoEstado.ABIERTO)) {
                    comboAnioLectivos.setSelectedT(anio);
                    break;
                }
            }

            comboAnioLectivosInsert = new SofisComboG(new ArrayList(anios), "aleAnio");
            comboAnioLectivosInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //Operaciones ficha de salud
    public void agregarFichaSalud() {
        JSFUtils.limpiarMensajesError();
        fichaSaludEnEdicion = new SgFichaSalud();
    }

    public void actualizarFichaSalud(SgFichaSalud entity) {
        JSFUtils.limpiarMensajesError();
        fichaSaludEnEdicion = SerializationUtils.clone(entity);
    }

    public void guardarFichaSalud() {
        try {
            fichaSaludEnEdicion.setFsaEstudiante(estudiante);
            fichaSaludEnEdicion = fichaSaludRestClient.guardar(fichaSaludEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAlergicoMedicamento() {
        if (fichaSaludEnEdicion != null) {
            if (BooleanUtils.isFalse(fichaSaludEnEdicion.getFsaEsAlergicoMedicamento())) {
                fichaSaludEnEdicion.setFsaAlergicoMedicamentos(null);
            }
        }
    }

    public void seleccionarAlergicoAlimento() {
        if (fichaSaludEnEdicion != null) {
            if (BooleanUtils.isFalse(fichaSaludEnEdicion.getFsaEsAlergicoAlimento())) {
                fichaSaludEnEdicion.setFsaAlergicoAlimentos(null);
            }
        }
    }

    //Operaciones de Medidas antropométricas y examen físico
    public void agregarMedidasExamenFisico() {
        JSFUtils.limpiarMensajesError();
        medidasExFiscoEnEdicion = new SgMedidasExamenFisico();
    }

    public void actualizarMedidasExamenFisico(SgMedidasExamenFisico entity) {
        try {
            JSFUtils.limpiarMensajesError();
            medidasExFiscoEnEdicion = medidasExamenFisicoRestClient.obtenerPorId(entity.getMefPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgMedidasExamen", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarMedidasExamenFisico() {
        try {
            
            medidasExFiscoEnEdicion.setMefEstudianteFk(estudiante);
            
            
            medidasExFiscoEnEdicion = medidasExamenFisicoRestClient.guardar(medidasExFiscoEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarMedidasExamenFisico();
            PrimeFaces.current().executeScript("PF('itemDialogMedidasExamen').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgMedidasExamen", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarMedidasExamenFisico() {
        try {
            if (medidasExFiscoEnEdicion != null) {
                medidasExamenFisicoRestClient.eliminar(medidasExFiscoEnEdicion.getMefPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                buscarMedidasExamenFisico();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarMedidasExamenFisico() {
        try {
            medidasExFiscoList = new ArrayList();
            if (estudiante != null) {
                FiltroMedidasExamenFisico fmef = new FiltroMedidasExamenFisico();
                fmef.setFichaEstudianteFk(estudiante.getEstPk());
                fmef.setIncluirCampos(new String[]{"mefEdad", "mefPeso", "mefTalla", "mefImc", "mefCbd", "mefEstudianteFk.estPk", "mefEstudianteFk.estVersion", "mefVersion"});
                medidasExFiscoList = medidasExamenFisicoRestClient.buscar(fmef);

            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //Metodos de Habitos de alimentacion
    public void agregarHabitosAlimentacionPersonal() {
        JSFUtils.limpiarMensajesError();
        habitoAlimentacionPersonalEnEdicion = new SgHabitosAlimentacion();
        comboAnioLectivosInsert.setSelected(-1);
    }

    public void actualizarHabitosAlimentacionPersonal(SgHabitosAlimentacion entity) {
        try {
            JSFUtils.limpiarMensajesError();
            habitoAlimentacionPersonalEnEdicion = habitosAlimentacionRestClient.obtenerPorId(entity.getHalPk());
            comboAnioLectivosInsert.setSelectedT(entity.getHalAnioLectivoFk());

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarHabitosAlimentacionPersonal() {
        try {
            habitoAlimentacionPersonalEnEdicion.setHalEstudianteFk(estudiante);
            
            habitoAlimentacionPersonalEnEdicion = habitosAlimentacionRestClient.guardar(habitoAlimentacionPersonalEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarHabitosAlimentacion();
            PrimeFaces.current().executeScript("PF('itemDialogHabitosAlimentacion').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgHabitoAlimentacion", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarAnioHabitoAlimentacion(){
        habitoAlimentacionPersonalEnEdicion.setHalAnioLectivoFk(comboAnioLectivosInsert.getSelectedT());
    }

    public void eliminarHabitosAlimentacionPersonal() {
        try {
            if (habitoAlimentacionPersonalEnEdicion != null) {
                habitosAlimentacionRestClient.eliminar(habitoAlimentacionPersonalEnEdicion.getHalPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                buscarHabitosAlimentacion();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarHabitosAlimentacion() {
        try {
            habitoAlimentacionPersonalList = new ArrayList();
            if (fichaSaludEnEdicion != null) {
                FiltroHabitosAlimentacion fmef = new FiltroHabitosAlimentacion();
                fmef.setFichaEstudianteFk(estudiante.getEstPk());
                fmef.setIncluirTiempoComida(Boolean.TRUE);
                fmef.setAnioLectivoPk(comboAnioLectivos.getSelectedT() != null ? comboAnioLectivos.getSelectedT().getAlePk() : null);
                fmef.setIncluirCampos(new String[]{"halPk", "halConsumeFrutasVerduras", "halFrecuenciaConsumoFrutas", "halConsumeAgua", "halCantidadVasos",
                    "halAnioLectivoFk.alePk", "halAnioLectivoFk.aleAnio", "halAnioLectivoFk.aleVersion", "halEstudianteFk.estPk", "halEstudianteFk.estVersion",
                    "halVersion"});

                habitoAlimentacionPersonalList = habitosAlimentacionRestClient.buscar(fmef);

            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgTiemposComidaDia> completeTiempoComida(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"tcdNombre"});
            fil.setAscending(new boolean[]{false});
            return this.habitoAlimentacionPersonalEnEdicion.getHalTiempoComidaDia() != null
                    ? restCatalogo.buscarTiempoComidaDia(fil).stream()
                            .filter(i -> !this.habitoAlimentacionPersonalEnEdicion.getHalTiempoComidaDia().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarTiempoComidaDia(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionConsumeFrutas() {
        if (habitoAlimentacionPersonalEnEdicion != null) {
            if (BooleanUtils.isFalse(habitoAlimentacionPersonalEnEdicion.getHalConsumeFrutasVerduras())) {
                habitoAlimentacionPersonalEnEdicion.setHalFrecuenciaConsumoFrutas(null);
            }
        }
    }

    public void seleccionTomaAgua() {
        if (habitoAlimentacionPersonalEnEdicion != null) {
            if (BooleanUtils.isFalse(habitoAlimentacionPersonalEnEdicion.getHalConsumeAgua())) {
                habitoAlimentacionPersonalEnEdicion.setHalCantidadVasos(null);
            }
        }
    }

    //METODOS DE HABITOS PERSONALES
    public void agregarHabitosPersonal() {
        JSFUtils.limpiarMensajesError();
        habitoPersonalEnEdicion = new SgHabitosPersonales();
        comboAnioLectivosInsert.setSelected(-1);
    }

    public void actualizarHabitosPersonal(SgHabitosPersonales entity) {
        try {
            JSFUtils.limpiarMensajesError();
            habitoPersonalEnEdicion = habitosPersonalesRestClient.obtenerPorId(entity.getHapPk());
            comboAnioLectivosInsert.setSelectedT(entity.getHapAnioLectivoFk());

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarHabitoPersonal() {
        try {
            habitoPersonalEnEdicion.setHapEstudianteFk(estudiante);
            habitoPersonalEnEdicion.setHapAnioLectivoFk(comboAnioLectivosInsert.getSelectedT());
            habitoPersonalEnEdicion = habitosPersonalesRestClient.guardar(habitoPersonalEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarHabitosPersonal();
            PrimeFaces.current().executeScript("PF('itemDialogHabitosPersonales').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgHabitoPersonal", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarHabitosPersonal() {
        try {
            if (habitoPersonalEnEdicion != null) {
                habitosPersonalesRestClient.eliminar(habitoPersonalEnEdicion.getHapPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                buscarHabitosPersonal();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarHabitosPersonal() {
        try {
            habitosPersonalesList = new ArrayList();
            if (fichaSaludEnEdicion != null) {
                FiltroHabitosPersonales fmef = new FiltroHabitosPersonales();
                fmef.setFichaEstudianteFk(estudiante.getEstPk());
                fmef.setIncluirCampos(new String[]{"hapPk", "hapTiempoFrentePantalla", "hapTiempoCompartidoConPadres", "hapTiempoTareasEscuela", "hapTiempoRecreacion",
                    "hapTiempoActividadFisica", "hapTipoActividadFisica", "hapTiempoDormir", "hapObservaciones", "hapAnioLectivoFk.alePk", "hapAnioLectivoFk.aleAnio",
                    "hapAnioLectivoFk.aleVersion", "hapEstudianteFk.estPk", "hapEstudianteFk.estVersion"});
                fmef.setAnioLectivoPk(comboAnioLectivos.getSelectedT() != null ? comboAnioLectivos.getSelectedT().getAlePk() : null);
                habitosPersonalesList = habitosPersonalesRestClient.buscar(fmef);

            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarDatoSaludEstudiante() {
        JSFUtils.limpiarMensajesError();
        datoSaludEstudiante = new SgDatoSaludEstudiante();
        this.tipoApoyoSeleccionado = null;
        this.anioSeleccionadoInsert = null;
        comboAnioLectivosInsert.setSelected(-1);
        comboTipoApoyo.setSelected(-1);
    }

    public void editarDatoSaludEstudiante(SgDatoSaludEstudiante datoSalud) {
        try {
            datoSaludEstudiante = datosSaludEstudianteRestClient.obtenerPorId(datoSalud.getDsePk());
            this.comboAnioLectivosInsert.setSelectedT(datoSaludEstudiante.getDseAnioLectivo());
            this.comboTipoApoyo.setSelectedT(datoSaludEstudiante.getDseTipoApoyo());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarDatoSaludEstudiante() {
        try {
            datosSaludEstudianteRestClient.eliminar(datoSaludEstudiante.getDsePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscarDatosSaludEstudiante();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarDatoSaludEstudiante() {
        try {
            datoSaludEstudiante.setDseAnioLectivo(anioSeleccionadoInsert);
            datoSaludEstudiante.setDseTipoApoyo(tipoApoyoSeleccionado);
            datoSaludEstudiante.setDseEstudiante(estudiante != null ? estudiante : null);
            datoSaludEstudiante = datosSaludEstudianteRestClient.guardar(datoSaludEstudiante);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarDatosSaludEstudiante();
            PrimeFaces.current().executeScript("PF('itemDialogDatosSaludEstudiante').hide()");
            anioSeleccionadoInsert = null;
            tipoApoyoSeleccionado = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgDatoSalud", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgDatoSalud", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarDatosSaludEstudiante() {
        try {
            FiltroDatoSaludEstudiante fDatoSalud = new FiltroDatoSaludEstudiante();
            fDatoSalud.setDseAnio(comboAnioLectivos.getSelectedT() != null ? comboAnioLectivos.getSelectedT().getAlePk() : null);
            fDatoSalud.setDseEstudianteId(estudiante.getEstPk());
            fDatoSalud.setIncluirCampos(new String[]{
                "dseAnioLectivo.alePk",
                "dseAnioLectivo.aleAnio",
                "dseTipoApoyo.tapNombre",
                "dseDescripcion",
                "dseEstudiante.estPk",
                "dseEstudiante.estVersion",
                "dseVersion"});

            datosSalud = datosSaludEstudianteRestClient.buscar(fDatoSalud);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void inicializarFicha() {
        try {
            cargarCombos();
            FiltroFichaSalud ffs = new FiltroFichaSalud();
            ffs.setFichaEstudianteFk(estudiante.getEstPk());
            ffs.setIncluirCampos(new String[]{"fsaLactancia",
                "fsaPartoPrematuro",
                "fsaEsAlergicoMedicamento",
                "fsaAlergicoMedicamentos",
                "fsaEsAlergicoAlimento",
                "fsaAlergicoAlimentos",
                "fsaMedicamentoPrescriptor",
                "fsaEnfermedadesPadece",
                "fsaMedicamentoPermanente",
                "fsaSituacionesDesencadenantes",
                "fsaManifestacionesFisicasPsicologicas",
                "fsaTiemposComida",
                "fsaConsumeFrutasVerduras",
                "fsaFrutasVerdurasDia",
                "fsaConsumeAgua",
                "fsaAguaVasos",
                "fsaTiempoTv",
                "fsaTiempoTareas",
                "fsaTiempoDormir",
                "fsaTiempoFamilia",
                "fsaTiempoRecreacion",
                "fsaTiempoEjercicio",
                "fsaTipoActividadFisica",
                "fsaComentarios",
                "fsaVersion",
                "fsaEstudiante.estPk",
                "fsaEstudiante.estVersion"});
            ffs.setIncluirENT(Boolean.TRUE);
            List<SgFichaSalud> fichas = fichaSaludRestClient.buscar(ffs);
            buscarDatosSaludEstudiante();
            buscarMedidasExamenFisico();
            buscarHabitosPersonal();
            buscarHabitosAlimentacion();
            if (!fichas.isEmpty()) {
                actualizarFichaSalud(fichas.get(0));
                
            } else {
                agregar();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgEnfermedadNoTransmisible> completeENT(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"entNombre"});
            fil.setAscending(new boolean[]{false});
            return this.fichaSaludEnEdicion.getFsaEnfermedadNoTransmitible() != null
                    ? restCatalogo.buscarEnfermedadNoTransmisible(fil).stream()
                            .filter(i -> !this.fichaSaludEnEdicion.getFsaEnfermedadNoTransmitible().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarEnfermedadNoTransmisible(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void buscarPorAnioLectivo() {
        buscarDatosSaludEstudiante();
        buscarHabitosPersonal();
        buscarHabitosAlimentacion();
    }

    public void seleccionarAnioLectivoInsert() {
        this.anioSeleccionadoInsert = comboAnioLectivosInsert != null ? comboAnioLectivosInsert.getSelectedT() : null;
    }

    public void comboAnioLectivoSelected() {
        this.anioSeleccionado = comboAnioLectivos != null ? comboAnioLectivos.getSelectedT() : null;
    }

    public void comboAnioLectivoInsertSelected() {
        this.anioSeleccionadoInsert = comboAnioLectivosInsert != null ? comboAnioLectivosInsert.getSelectedT() : null;
    }

    public void seleccionarTipoApoyo() {
        this.tipoApoyoSeleccionado = comboTipoApoyo != null ? comboTipoApoyo.getSelectedT() : null;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SgEstudiante getEntidadEnEdicion() {
        return estudiante;
    }

    public void setEntidadEnEdicion(SgEstudiante entidadEnEdicion) {
        this.estudiante = entidadEnEdicion;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivos() {
        return comboAnioLectivos;
    }

    public void setComboAnioLectivos(SofisComboG<SgAnioLectivo> comboAnioLectivos) {
        this.comboAnioLectivos = comboAnioLectivos;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivosInsert() {
        return comboAnioLectivosInsert;
    }

    public void setComboAnioLectivosInsert(SofisComboG<SgAnioLectivo> comboAnioLectivosInsert) {
        this.comboAnioLectivosInsert = comboAnioLectivosInsert;
    }

    public SofisComboG<SgTipoApoyo> getComboTipoApoyo() {
        return comboTipoApoyo;
    }

    public void setComboTipoApoyo(SofisComboG<SgTipoApoyo> comboTipoApoyo) {
        this.comboTipoApoyo = comboTipoApoyo;
    }

    public List<SgDatoSaludEstudiante> getDatosSalud() {
        return datosSalud;
    }

    public void setDatosSalud(List<SgDatoSaludEstudiante> datosSalud) {
        this.datosSalud = datosSalud;
    }

    public SgAnioLectivo getAnioSeleccionado() {
        return anioSeleccionado;
    }

    public void setAnioSeleccionado(SgAnioLectivo anioSeleccionado) {
        this.anioSeleccionado = anioSeleccionado;
    }

    public SgAnioLectivo getAnioSeleccionadoInsert() {
        return anioSeleccionadoInsert;
    }

    public void setAnioSeleccionadoInsert(SgAnioLectivo anioSeleccionadoInsert) {
        this.anioSeleccionadoInsert = anioSeleccionadoInsert;
    }

    public SgTipoApoyo getTipoApoyoSeleccionado() {
        return tipoApoyoSeleccionado;
    }

    public void setTipoApoyoSeleccionado(SgTipoApoyo tipoApoyoSeleccionado) {
        this.tipoApoyoSeleccionado = tipoApoyoSeleccionado;
    }

    public SgDatoSaludEstudiante getDatoSaludEstudiante() {
        return datoSaludEstudiante;
    }

    public void setDatoSaludEstudiante(SgDatoSaludEstudiante datoSaludEstudiante) {
        this.datoSaludEstudiante = datoSaludEstudiante;
    }

    public SgFichaSalud getFichaSaludEnEdicion() {
        return fichaSaludEnEdicion;
    }

    public void setFichaSaludEnEdicion(SgFichaSalud fichaSaludEnEdicion) {
        this.fichaSaludEnEdicion = fichaSaludEnEdicion;
    }

    public SgMedidasExamenFisico getMedidasExFiscoEnEdicion() {
        return medidasExFiscoEnEdicion;
    }

    public void setMedidasExFiscoEnEdicion(SgMedidasExamenFisico medidasExFiscoEnEdicion) {
        this.medidasExFiscoEnEdicion = medidasExFiscoEnEdicion;
    }

    public List<SgHabitosAlimentacion> getHabitoAlimentacionPersonalList() {
        return habitoAlimentacionPersonalList;
    }

    public void setHabitoAlimentacionPersonalList(List<SgHabitosAlimentacion> habitoAlimentacionPersonalList) {
        this.habitoAlimentacionPersonalList = habitoAlimentacionPersonalList;
    }

    public SgHabitosAlimentacion getHabitoAlimentacionPersonalEnEdicion() {
        return habitoAlimentacionPersonalEnEdicion;
    }

    public void setHabitoAlimentacionPersonalEnEdicion(SgHabitosAlimentacion habitoAlimentacionPersonalEnEdicion) {
        this.habitoAlimentacionPersonalEnEdicion = habitoAlimentacionPersonalEnEdicion;
    }

    public List<SgHabitosPersonales> getHabitosPersonalesList() {
        return habitosPersonalesList;
    }

    public void setHabitosPersonalesList(List<SgHabitosPersonales> habitosPersonalesList) {
        this.habitosPersonalesList = habitosPersonalesList;
    }

    public SgHabitosPersonales getHabitoPersonalEnEdicion() {
        return habitoPersonalEnEdicion;
    }

    public void setHabitoPersonalEnEdicion(SgHabitosPersonales habitoPersonalEnEdicion) {
        this.habitoPersonalEnEdicion = habitoPersonalEnEdicion;
    }

    public List<SgMedidasExamenFisico> getMedidasExFiscoList() {
        return medidasExFiscoList;
    }

    public void setMedidasExFiscoList(List<SgMedidasExamenFisico> medidasExFiscoList) {
        this.medidasExFiscoList = medidasExFiscoList;
    }

    public String getMsjConfiguracionFichaSalud() {
        return msjConfiguracionFichaSalud;
    }

    public void setMsjConfiguracionFichaSalud(String msjConfiguracionFichaSalud) {
        this.msjConfiguracionFichaSalud = msjConfiguracionFichaSalud;
    }

}
