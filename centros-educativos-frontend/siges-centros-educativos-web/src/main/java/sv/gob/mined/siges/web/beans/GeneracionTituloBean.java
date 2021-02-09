/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEstudianteImpresion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudImpresion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaTitularRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class GeneracionTituloBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GeneracionTituloBean.class.getName());

    @Inject
    private SolicitudImpresionRestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SelloFirmaTitularRestClient selloFirmaTitularRestClient;

    @Inject
    private SelloFirmaRestClient selloFirmaRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private DatoContratacionRestClient restDatoContratacion;

    @Inject
    @Param(name = "id")
    private Long solicitudImpresionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private FiltroSolicitudImpresion filtro = new FiltroSolicitudImpresion();
    private SgSolicitudImpresion entidadEnEdicion = new SgSolicitudImpresion();
    private List<RevHistorico> historialSolicitudImpresion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;

    private SofisComboG<SgMotivoAnulacionTitulo>[] comboMotivoAnulacionTitulo;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<SgSelloFirmaTitular> comboSelloFirmaTitular;
    private List<SgTitulo> tituloList;
    private LocalDate fechaValidez;

    public GeneracionTituloBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();

            cargarCombos();
            if (solicitudImpresionId != null && solicitudImpresionId > 0) {
                actualizar(solicitudImpresionId);
                soloLectura = editable != null ? !editable : soloLectura;
                if (!entidadEnEdicion.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA)) {
                    soloLectura = Boolean.TRUE;
                }
                if (entidadEnEdicion.getSimDefinicionTitulo() != null) {
                    comboSelloFirmaTitular.setSelectedT(entidadEnEdicion.getSimSelloAutentica());
                }

                Collections.sort(entidadEnEdicion.getSimEstudianteImpresion(), (o1, o2) -> o1.getEimEstudiante().getEstPersona().getPerNombreCompletoOrder().compareTo(o2.getEimEstudiante().getEstPersona().getPerNombreCompletoOrder()));
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "No existe solicitud de impresión", "");
                soloLectura = Boolean.TRUE;
            }
            cargarCombosMotivoAnulacion();
            cargarConfiguracion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_GENERACION_TITULO)) {
            JSFUtils.redirectToIndex();
        }
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
                "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk",
                "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
                "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedVersion",
                "simSeccion.secServicioEducativo.sduSede.sedPk",
                "simSeccion.secServicioEducativo.sduSede.sedVersion",
                "simSeccion.secServicioEducativo.sduSede.sedTipo",
                "simSeccion.secServicioEducativo.sduSede.sedNombre",
                "simSeccion.secServicioEducativo.sduPk",
                "simSeccion.secServicioEducativo.sduVersion",
                "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "simSeccion.secNombre",
                "simUsuario.usuPk",
                "simUsuario.usuNombre",
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
                "simReposicionEstudianteSiges",
                "simResolucionFk.achPk",
                "simResolucionFk.achNombre",
                "simResolucionFk.achExt",
                "simResolucionFk.achTmpPath",
                "simResolucionFk.achVersion",
                "simImpresora",
                "simFechaValidez"});
            List<SgSolicitudImpresion> solImp = restClient.buscar(fsi);
            entidadEnEdicion = solImp.get(0);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarConfiguracion() {
        try {
            if (entidadEnEdicion.getSimFechaValidez() == null) {

                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setCodigo(Constantes.TITULO_FECHA_VALIDEZ_DESDE);
                List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
                if (!conf.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
                    entidadEnEdicion.setSimFechaValidez(LocalDate.parse(conf.get(0).getConValor(), formatter));
                }
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
        try {
            FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
            fsft.setSftHabilitado(Boolean.TRUE);
            fsft.setSftCodigoCargo(Constantes.CARGO_AUTENTICA);
            List<SgSelloFirmaTitular> listSelloFirmaTitular = selloFirmaTitularRestClient.buscar(fsft);
            comboSelloFirmaTitular = new SofisComboG(listSelloFirmaTitular, "sftNombreCompleto");
            comboSelloFirmaTitular.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombosMotivoAnulacion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setOrderBy(new String[]{"matNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgMotivoAnulacionTitulo> motAnulacion = restCatalogo.buscarMotivoAnulacionTitulo(fc);
            if (entidadEnEdicion != null && entidadEnEdicion.getSimEstudianteImpresion()!=null && !entidadEnEdicion.getSimEstudianteImpresion().isEmpty() && entidadEnEdicion.getSimEstudianteImpresion().size() > 0) {
                comboMotivoAnulacionTitulo = new SofisComboG[entidadEnEdicion.getSimEstudianteImpresion().size()];

                for (Integer i = 0; i < entidadEnEdicion.getSimEstudianteImpresion().size(); i++) {
                    comboMotivoAnulacionTitulo[i] = new SofisComboG(motAnulacion, "matNombre");
                    comboMotivoAnulacionTitulo[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                }

                for (SgEstudianteImpresion estImp : entidadEnEdicion.getSimEstudianteImpresion()) {
                    if (estImp.getEimMotivoAnulacion() != null) {
                        comboMotivoAnulacionTitulo[entidadEnEdicion.getSimEstudianteImpresion().indexOf(estImp)].setSelectedT(estImp.getEimMotivoAnulacion());
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnulada(Integer rowIndex, SgEstudianteImpresion anulada) {
        if (BooleanUtils.isTrue(anulada.getEimAnulada())) {
            comboMotivoAnulacionTitulo[rowIndex].setSelected(-1);
            anulada.setEimObservacionAnulada(null);
        }

    }

    public List<SgSede> completeSede(String query) {
        try {

            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedNombre", "sedCodigo", "sedTipo", "sedVersion"});

            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public Integer cantidadEstudiantes(SgSolicitudImpresion solImp) {
        return solImp.getSimEstudianteImpresion() != null ? solImp.getSimEstudianteImpresion().size() : 0;

    }

    private void limpiarCombos() {
        comboSelloFirmaTitular.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroSolicitudImpresion();
        limpiarCombos();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSolicitudImpresion();
    }

    public void actualizar(SgSolicitudImpresion var) {
        limpiarCombos();
        entidadEnEdicion = (SgSolicitudImpresion) SerializationUtils.clone(var);

    }

    public void confirmar() {
        try {

            for (SgEstudianteImpresion estImp : entidadEnEdicion.getSimEstudianteImpresion()) {
                if (BooleanUtils.isTrue(estImp.getEimAnulada())) {
                    estImp.setEimMotivoAnulacion(comboMotivoAnulacionTitulo[entidadEnEdicion.getSimEstudianteImpresion().indexOf(estImp)].getSelectedT());
                }

            }
            entidadEnEdicion.setSimSelloAutentica(comboSelloFirmaTitular.getSelectedT());

            entidadEnEdicion.setSimFechaEnviadoImprimir(LocalDate.now());
            Boolean respuesta = buscarDirectorMinistro();
            if (!respuesta) {
                return;
            }
            entidadEnEdicion = restClient.confirmarSolicitud(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            soloLectura = Boolean.TRUE;

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        if(entidadEnEdicion.getSimFechaEntregadoDepartamental()==null){
            entidadEnEdicion.setSimTituloEntregadoDepartamental(Boolean.FALSE);
        }
        if(entidadEnEdicion.getSimFechaEntregadoCentroEducativo()==null){
            entidadEnEdicion.setSimTituloEntregadoCentroEducativo(Boolean.FALSE);
        }
    }

    public Boolean buscarDirectorMinistro() throws Exception {

        if (entidadEnEdicion.getSimSeccion() != null) {
            FiltroSelloFirma fsf = new FiltroSelloFirma();
            fsf.setSfiHabilitado(Boolean.TRUE);
            if (entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe() != null) {
                List<Long> sedeList = new ArrayList();
                sedeList.add(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedPk());
                sedeList.add(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                fsf.setSfiSedes(sedeList);
            } else {
                fsf.setSedPk(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedPk());
            }

            fsf.setSfiTipoRepresentanteCodigo(Constantes.TIPO_REPRESENTANTE_DIRECTOR);
            fsf.setIncluirCampos(new String[]{"sfiPk",
                "sfiVersion",
                "sfiSede.sedPk",
                "sfiSede.sedTipo",
                "sfiPersona.perPk",
                "sfiPersona.perVersion"});
            List<SgSelloFirma> listSelloFirma = selloFirmaRestClient.buscar(fsf);
            if (!listSelloFirma.isEmpty()) {
                SgSelloFirma selloFirma = listSelloFirma.get(0);
                if(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe()!=null){
                    List<SgSelloFirma> listSedePadre=listSelloFirma.stream().filter(c->c.getSfiSede().getSedPk().equals(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedPk())).collect(Collectors.toList());
                    if(!listSedePadre.isEmpty()){
                        selloFirma=listSedePadre.get(0);
                    }
                }
                entidadEnEdicion.setSimSelloDirector(selloFirma);

            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SELLO_FIRMA_DIRECTOR_VACIO), "");
                return Boolean.FALSE;
            }

        }
        FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
        fsft.setSftHabilitado(Boolean.TRUE);
        fsft.setSftCodigoCargo(Constantes.CARGO_MINISTRO);
        List<SgSelloFirmaTitular> listSelloFirmaTitular = selloFirmaTitularRestClient.buscar(fsft);
        if (listSelloFirmaTitular.size() > 0) {
            entidadEnEdicion.setSimSelloMinistro(listSelloFirmaTitular.get(0));//TODO:SE QUEDA CON EL PRIMERO, HAY QUE HACER QUE SE QUEDE CON EL MINISTRO AGREGANDO BOOLEANO EN CATALOGO 
        }

        return Boolean.TRUE;
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

    public SofisComboG<SgSelloFirmaTitular> getComboSelloFirmaTitular() {
        return comboSelloFirmaTitular;
    }

    public void setComboSelloFirmaTitular(SofisComboG<SgSelloFirmaTitular> comboSelloFirmaTitular) {
        this.comboSelloFirmaTitular = comboSelloFirmaTitular;
    }

    public SofisComboG<SgMotivoAnulacionTitulo>[] getComboMotivoAnulacionTitulo() {
        return comboMotivoAnulacionTitulo;
    }

    public void setComboMotivoAnulacionTitulo(SofisComboG<SgMotivoAnulacionTitulo>[] comboMotivoAnulacionTitulo) {
        this.comboMotivoAnulacionTitulo = comboMotivoAnulacionTitulo;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgTitulo> getTituloList() {
        return tituloList;
    }

    public void setTituloList(List<SgTitulo> tituloList) {
        this.tituloList = tituloList;
    }

}
