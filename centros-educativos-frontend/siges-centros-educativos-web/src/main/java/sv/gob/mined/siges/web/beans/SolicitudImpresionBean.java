/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudianteImpresion;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudImpresion;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudImpresionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudImpresionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudImpresionBean.class.getName());

    @Inject
    private SolicitudImpresionRestClient restClient;

    @Inject
    private EstudianteRestClient restEstudiante;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    private GradoRestClient restGrado;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstRestClient;

    @Inject
    private CalificacionEstudianteRestClient calificacionEstudianteRestClient;

    @Inject
    @Param(name = "id")
    private Long SolImpId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private FiltroSolicitudImpresion filtro = new FiltroSolicitudImpresion();
    private SgSolicitudImpresion entidadEnEdicion = new SgSolicitudImpresion();
    private List<RevHistorico> historialSolicitudImpresion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySolicitudImpresionDataModel solicitudImpresionLazyModel;
    private Boolean soloLectura = Boolean.FALSE;
    private List<SgEstudiante> estudianteList;
    private Boolean[] checkList;
    private String mensajeDeGuardar;
    private Boolean filtroGradoConTitulo = Boolean.TRUE;
    private SofisComboG<SgDefinicionTitulo> definicionTituloCombo;
    private SgSeccion seccionEnEdicion;
    private SgConfiguracion mensajeSolicitudImpresion;
    private String[] incluirCamposSeccion = new String[]{
        "secServicioEducativo.sduGrado.graPk",
        "secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
        "secServicioEducativo.sduSede.sedPk",
        "secServicioEducativo.sduSede.sedTipo",
        "secServicioEducativo.sduOpcion.opcNombre",
        "secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk",
        "secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
        "secServicioEducativo.sduSede.sedSedeAdscritaDe.sedVersion",
        "secServicioEducativo.sduProgramaEducativo.pedNombre",
        "secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",
        "secPlanEstudio.pesPk",
        "secPlanEstudio.pesNombre",
        "secAnioLectivo.alePk",
        "secEstado",
        "secNombre",
        "secCodigo",
        "secVersion"};

    public SolicitudImpresionBean() {
    }
    
    

    @PostConstruct
    public void init() {
        try {
            cargarCombos();

            if (SolImpId != null && SolImpId > 0) {
                actualizar(SolImpId);
                soloLectura = editable != null ? !editable : soloLectura;
                seccionEnEdicion = entidadEnEdicion.getSimSeccion();
                if (entidadEnEdicion.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA)) {
                    soloLectura = Boolean.TRUE;
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION)) {
                    JSFUtils.redirectToIndex();
                }
                agregar();
            }
            validarAcceso();
            cargarConfiguracion();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {

    }

    public String getTituloPagina() {
        if (this.SolImpId == null) {
            return Etiquetas.getValue("nuevoSolicitudImpresion");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verSolicitudImpresion");
        } else {
            return Etiquetas.getValue("edicionSolicitudImpresion");
        }
    }

    public void cargarConfiguracion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.MENSAJE_SOLICITUD_IMPRESION);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                mensajeSolicitudImpresion = conf.get(0);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public String obtenerNombreTitulo() {
        SgSolicitudImpresion sim = entidadEnEdicion;
        String nombreCerificado = "";
        if (sim.getSimPk() != null && sim.getSimDefinicionTitulo() != null) {
            nombreCerificado = sim.getSimDefinicionTitulo().getDtiNombreCertificado();
            if (sim.getSimDefinicionTitulo() != null && sim.getSimSeccion() != null) {
                if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{OPCION}")) {

                    nombreCerificado = nombreCerificado.replace("{OPCION}", sim.getSimSeccion().getSecServicioEducativo().getSduOpcion() != null ? "" + sim.getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcNombre() : "");
                }

                if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{PROGRAMA}")) {
                    nombreCerificado = nombreCerificado.replace("{PROGRAMA}", sim.getSimSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? "" + sim.getSimSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedNombre() : "");
                }

                if (sim.getSimDefinicionTitulo().getDtiNombreCertificado().contains("{SECTOR}")) {
                    nombreCerificado = nombreCerificado.replace("{SECTOR}", sim.getSimSeccion().getSecServicioEducativo().getSduOpcion() != null && sim.getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico() != null ? "" + sim.getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico().getSecNombre() : "");
                }
            }
        }
        return nombreCerificado;
    }

    public void cargarCombos() {
        definicionTituloCombo = new SofisComboG();
        definicionTituloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroSolicitudImpresion();
        // buscar();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSolicitudImpresion();
    }

    public void actualizar(Long simPḱ) {
        try {
            FiltroSolicitudImpresion fsi = new FiltroSolicitudImpresion();
            fsi.setSimPk(simPḱ);
            fsi.setInicializarEstudianteImp(Boolean.TRUE);
            fsi.setIncluirCampos(new String[]{"simFechaSolicitud",   
                "simFechaEnviadoImprimir",
                "simEstado",
                "simTipoImpresion",
                "simSeccion.secPk",
                "simSeccion.secVersion",
                "simSeccion.secServicioEducativo.sduOpcion.opcNombre",
                "simSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                "simSeccion.secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",                
                "simSeccion.secServicioEducativo.sduGrado.graPk",
                "simSeccion.secServicioEducativo.sduGrado.graNombre",
                "simSeccion.secAnioLectivo.aleAnio",
                "simSeccion.secPlanEstudio.pesPk",
                "simSeccion.secServicioEducativo.sduSede.sedCodigo",
                "simSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "simSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "simSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "simSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                "simSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "simSeccion.secServicioEducativo.sduSede.sedPk",
                "simSeccion.secServicioEducativo.sduSede.sedVersion",
                "simSeccion.secServicioEducativo.sduSede.sedTipo",
                "simSeccion.secServicioEducativo.sduSede.sedNombre",
                "simSeccion.secServicioEducativo.sduPk",
                "simSeccion.secServicioEducativo.sduVersion",
                "simSeccion.secNombre",
                "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk",
                "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
                "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedVersion",
                "simUsuario.usuPk",
                "simUsuario.usuVersion",
                "simInicioNumeroCorrelativo",
                "simVersion",
                "simDefinicionTitulo.dtiPk",
                "simDefinicionTitulo.dtiNombre",
                "simDefinicionTitulo.dtiVersion",
                "simDefinicionTitulo.dtiNombreCertificado",
                "simSelloDirector.sfiPk",
                "simSelloDirector.sfiVersion",
                "simSelloDirector.sfiPersona.perPrimerNombre",
                "simSelloDirector.sfiPersona.perSegundoNombre",
                "simSelloDirector.sfiPersona.perTercerNombre",
                "simSelloDirector.sfiPersona.perPrimerApellido",
                "simSelloDirector.sfiPersona.perSegundoApellido",
                "simSelloDirector.sfiPersona.perTercerApellido",
                "simSelloMinistro.sftPk",
                "simSelloMinistro.sftPrimerNombre",
                "simSelloMinistro.sftSegundoNombre",
                "simSelloMinistro.sftPrimerApellido",
                "simSelloMinistro.sftSegundoApellido",
                "simSelloMinistro.sftVersion",
                "simSelloAutentica.sftPk",
                "simSelloAutentica.sftVersion",
                "simSelloAutentica.sftPrimerNombre",
                "simSelloAutentica.sftSegundoNombre",
                "simSelloAutentica.sftPrimerApellido",
                "simSelloAutentica.sftSegundoApellido",
                "simMotivoReimpresion.mriPk",
                "simMotivoReimpresion.mriVersion",
                "simReposicionTitulo.retPk",
                "simReposicionTitulo.retVersion",
                "simReposicionTitulo.retNombreEstudiantePartida",
                "simReposicionTitulo.retNombreEstudiantePartidaBusqueda",
                "simReposicionTitulo.retAnio",
                "simReposicionTitulo.retSedeNombre",
                "simReposicionTitulo.retSedeNombreBusqueda",
                "simReposicionTitulo.retEstadoReposicion",
                "simReposicionTitulo.retUsuarioEnviaImprimir",
                "simNumeroResolucion",
                "simReposicionTitulo.retSede.sedPk",
                "simReposicionTitulo.retSede.sedVersion",
                "simReposicionTitulo.retSede.sedTipo",
                "simReposicionTitulo.retSolicitudImpresion.simPk",
                "simReposicionTitulo.retSolicitudImpresion.simVersion",
                "simReposicionTitulo.retAnulada",
                "simReposicionTitulo.retMotivoAnulacion.matPk",
                "simReposicionTitulo.retMotivoAnulacion.matVersion",
                "simReposicionTitulo.retObservacionAnulada",
                "simTituloEntregadoDepartamental",
                "simFechaEntregadoDepartamental",
                "simTituloEntregadoCentroEducativo",
                "simFechaEntregadoCentroEducativo",
                "simImpresora",
                "simFechaValidez"});
            List<SgSolicitudImpresion> solImp=restClient.buscar(fsi);
            limpiarCombos();
            entidadEnEdicion = solImp.get(0);
            cargarDefinicionTitulo(entidadEnEdicion.getSimSeccion());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void enviar() {
        try {

            entidadEnEdicion.setSimFechaSolicitud(LocalDate.now());
            entidadEnEdicion.setSimUsuario(sessionBean.getEntidadUsuario());
            entidadEnEdicion.setSimEstado(EnumEstadoSolicitudImpresion.ENVIADA);
            entidadEnEdicion.setSimDefinicionTitulo(definicionTituloCombo.getSelectedT());
            List<SgEstudianteImpresion> est = new ArrayList();
            for (int i = 0; i < estudianteList.size(); i++) {
                if (checkList[i]) {
                    SgEstudianteImpresion estImp = new SgEstudianteImpresion();
                    estImp.setEimEstudiante(estudianteList.get(i));
                    estImp.setEimAnulada(Boolean.FALSE);
                    est.add(estImp);

                }
            }
            entidadEnEdicion.setSimEstudianteImpresion(est);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            soloLectura = Boolean.TRUE;
            // cargarSolicitudImpresion();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_IMPRESIONES);

            //     buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (BooleanUtils.isFalse(entidadEnEdicion.getSimTituloEntregadoCentroEducativo())) {
                entidadEnEdicion.setSimFechaEntregadoCentroEducativo(null);
            }
            if (BooleanUtils.isFalse(entidadEnEdicion.getSimTituloEntregadoDepartamental())) {
                entidadEnEdicion.setSimFechaEntregadoDepartamental(null);
            }

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            // cargarSolicitudImpresion();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarDefinicionTitulo(SgSeccion sec) {
        try {

            seccionEnEdicion = sec;

            definicionTituloCombo = new SofisComboG();
            definicionTituloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (sec != null) {
                seccionEnEdicion = sec;

                FiltroRelGradoPlanEstudio fgpe = new FiltroRelGradoPlanEstudio();
                fgpe.setRgpGrado(sec.getSecServicioEducativo().getSduGrado().getGraPk());
                fgpe.setRgpPlanEstudio(sec.getSecPlanEstudio().getPesPk());
                List<SgDefinicionTitulo> defTitList = new ArrayList(relGradoPlanEstRestClient.buscarDefinicionTitulo(fgpe));
                
                defTitList=adaptarNombreEnCertificado(defTitList);

                definicionTituloCombo = new SofisComboG(defTitList, "dtiNombreCertificado");
                definicionTituloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (SolImpId != null && SolImpId > 0) {
                    if (!defTitList.contains(entidadEnEdicion.getSimDefinicionTitulo())) {
                        defTitList.add(entidadEnEdicion.getSimDefinicionTitulo());
                        defTitList=adaptarNombreEnCertificado(defTitList);
                        
                        definicionTituloCombo = new SofisComboG(defTitList, "dtiNombreCertificado");
                        definicionTituloCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }
                    definicionTituloCombo.setSelectedT(entidadEnEdicion.getSimDefinicionTitulo());
                    cargarSolicitudImpresion();
                } else {
                    if (defTitList.size() == 1) {
                        definicionTituloCombo.setSelectedT(defTitList.get(0));
                        cargarSolicitudImpresion();
                    }
                }

            } else {
                entidadEnEdicion = new SgSolicitudImpresion();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgDefinicionTitulo> adaptarNombreEnCertificado(List<SgDefinicionTitulo> defTitList){
        for(SgDefinicionTitulo defTit:defTitList){
            String nombreCertificado = defTit.getDtiNombreCertificado();
            if (seccionEnEdicion != null) {
                if (nombreCertificado.contains("{OPCION}")) {

                    nombreCertificado = nombreCertificado.replace("{OPCION}", seccionEnEdicion.getSecServicioEducativo().getSduOpcion() != null ? "" + seccionEnEdicion.getSecServicioEducativo().getSduOpcion().getOpcNombre() : "");
                }

                if (nombreCertificado.contains("{PROGRAMA}")) {
                    nombreCertificado = nombreCertificado.replace("{PROGRAMA}", seccionEnEdicion.getSecServicioEducativo().getSduProgramaEducativo() != null ? "" + seccionEnEdicion.getSecServicioEducativo().getSduProgramaEducativo().getPedNombre() : "");
                }

                if (nombreCertificado.contains("{SECTOR}")) {
                    nombreCertificado = nombreCertificado.replace("{SECTOR}", seccionEnEdicion.getSecServicioEducativo().getSduOpcion() != null && seccionEnEdicion.getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico() != null ? "" + seccionEnEdicion.getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico().getSecNombre() : "");
                }
                defTit.setDtiNombreCertificado(nombreCertificado);
            }
        }
        return defTitList;
    }

    public void mensajeGuardar() {
        mensajeDeGuardar = "¿Desea enviar?";
        if (Arrays.asList(checkList).contains(false)) {
            mensajeDeGuardar = "Hay estudiantes sin seleccionar, ¿desea enviar?";
        }

    }

    public void cargarSolicitudImpresion() {
        try {
            //Se cargan los estudiantes que cumplen:
            //-Tiene calificación de Grado Promovido
            //-La matricula tiene validación académica en true
            //-No tienen otra solicitud de impresión generada (salvo que esté rechazada o anulada)
            //-Cumple con la fórmula de título de la entidad Definición título
            estudianteList = new ArrayList();

            if (definicionTituloCombo.getSelectedT() != null) {
                if (seccionEnEdicion != null) {
                    estudianteList = new ArrayList();
                    if (SolImpId == null) {
                        agregar();
                        SgDefinicionTitulo defTit = catalogoRestClient.obtenerPorIdDefinicionTitulo(definicionTituloCombo.getSelectedT().getDtiPk());

                        FiltroSolicitudImpresion fsi = new FiltroSolicitudImpresion();
                        fsi.setSimSeccion(seccionEnEdicion.getSecPk());
                        fsi.setSimDefinicionTitulo(defTit.getDtiPk());
                        fsi.setFormula(defTit.getDtiFormula() != null ? defTit.getDtiFormula().getFomTextoLargo() : null);
                        estudianteList = restClient.buscarEstudiantesHabilitados(fsi);

                        entidadEnEdicion.setSimSeccion(seccionEnEdicion);
                        entidadEnEdicion.setSimDefinicionTitulo(definicionTituloCombo.getSelectedT());
                        checkList = new Boolean[estudianteList.size()];
                        Arrays.fill(checkList, Boolean.TRUE);

                    } else {
                        estudianteList = entidadEnEdicion.getSimEstudianteImpresion().stream().map(c -> c.getEimEstudiante()).collect(Collectors.toList());
                        Collections.sort(estudianteList, (o1, o2) -> o1.getEstPersona().getPerNombreCompletoOrder().compareTo(o2.getEstPersona().getPerNombreCompletoOrder()));
                        checkList = new Boolean[estudianteList.size()];
                        Arrays.fill(checkList, Boolean.TRUE);
                    }
                }
            } else {
                entidadEnEdicion = new SgSolicitudImpresion();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscarAnuladaEstudiante(Long estPk) {
        if (entidadEnEdicion.getSimPk() != null) {
            List<SgEstudianteImpresion> estImp = entidadEnEdicion.getSimEstudianteImpresion().stream().filter(c -> c.getEimEstudiante().getEstPk().equals(estPk)).collect(Collectors.toList());
            if (!estImp.isEmpty()) {
                if (BooleanUtils.isTrue(estImp.get(0).getEimAnulada())) {
                    return "Si";
                }
            }
        }
        return "No";
    }

    public String buscarAnuladaObservacion(Long estPk) {
        if (entidadEnEdicion.getSimPk() != null) {
            List<SgEstudianteImpresion> estImp = entidadEnEdicion.getSimEstudianteImpresion().stream().filter(c -> c.getEimEstudiante().getEstPk().equals(estPk)).collect(Collectors.toList());
            if (!estImp.isEmpty()) {
                return estImp.get(0).getEimObservacionAnulada();
            }
        }
        return null;
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSimPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialSolicitudImpresion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroSolicitudImpresion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSolicitudImpresion filtro) {
        this.filtro = filtro;
    }

    public SgSolicitudImpresion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudImpresion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialSolicitudImpresion() {
        return historialSolicitudImpresion;
    }

    public void setHistorialSolicitudImpresion(List<RevHistorico> historialSolicitudImpresion) {
        this.historialSolicitudImpresion = historialSolicitudImpresion;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazySolicitudImpresionDataModel getSolicitudImpresionLazyModel() {
        return solicitudImpresionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazySolicitudImpresionDataModel solicitudImpresionLazyModel) {
        this.solicitudImpresionLazyModel = solicitudImpresionLazyModel;
    }

    public SolicitudImpresionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SolicitudImpresionRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Long getSolImpId() {
        return SolImpId;
    }

    public void setSolImpId(Long SolImpId) {
        this.SolImpId = SolImpId;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgEstudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<SgEstudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    public Boolean[] getCheckList() {
        return checkList;
    }

    public void setCheckList(Boolean[] checkList) {
        this.checkList = checkList;
    }

    public String getMensajeDeGuardar() {
        return mensajeDeGuardar;
    }

    public void setMensajeDeGuardar(String mensajeDeGuardar) {
        this.mensajeDeGuardar = mensajeDeGuardar;
    }

    public Boolean getFiltroGradoConTitulo() {
        return filtroGradoConTitulo;
    }

    public void setFiltroGradoConTitulo(Boolean filtroGradoConTitulo) {
        this.filtroGradoConTitulo = filtroGradoConTitulo;
    }

    public SofisComboG<SgDefinicionTitulo> getDefinicionTituloCombo() {
        return definicionTituloCombo;
    }

    public void setDefinicionTituloCombo(SofisComboG<SgDefinicionTitulo> definicionTituloCombo) {
        this.definicionTituloCombo = definicionTituloCombo;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public SgConfiguracion getMensajeSolicitudImpresion() {
        return mensajeSolicitudImpresion;
    }

    public void setMensajeSolicitudImpresion(SgConfiguracion mensajeSolicitudImpresion) {
        this.mensajeSolicitudImpresion = mensajeSolicitudImpresion;
    }

    public String[] getIncluirCamposSeccion() {
        return incluirCamposSeccion;
    }

    public void setIncluirCamposSeccion(String[] incluirCamposSeccion) {
        this.incluirCamposSeccion = incluirCamposSeccion;
    }

}
