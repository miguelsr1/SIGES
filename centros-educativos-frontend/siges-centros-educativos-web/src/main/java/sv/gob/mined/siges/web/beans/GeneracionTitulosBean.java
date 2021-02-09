/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudImpresionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
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
public class GeneracionTitulosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GeneracionTitulosBean.class.getName());

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

    private FiltroSolicitudImpresion filtro = new FiltroSolicitudImpresion();
    private SgSolicitudImpresion entidadEnEdicion = new SgSolicitudImpresion();
    private List<RevHistorico> historialSolicitudImpresion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySolicitudImpresionDataModel solicitudImpresionLazyModel;
    private SofisComboG<EnumEstadoSolicitudImpresion> comboEstado;
    private SgSede sedSeleccionado;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SofisComboG<SgSelloFirmaTitular> comboSelloFirmaTitular;
    private SofisComboG<EnumTipoSolicitudImpresion> comboTipo;

    public GeneracionTitulosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_GENERACION_TITULO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"simSeccion.secServicioEducativo.sduSede.sedCodigo",
                "simSeccion.secServicioEducativo.sduSede.sedNombre",
                "simSeccion.secServicioEducativo.sduSede.sedTelefono",
                "simSeccion.secServicioEducativo.sduSede.sedTipo",
                "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "simSeccion.secServicioEducativo.sduSede.sedNombre",
                "simSeccion.secServicioEducativo.sduOpcion.opcNombre",
                "simSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                "simSeccion.secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",
                "simSeccion.secNombre",
                "simSeccion.secVersion",
                "simUsuario.usuPk",
                "simUsuario.usuVersion",
                "simInicioNumeroCorrelativo",
                "simEstado",
                "simFechaEnviadoImprimir",
                "simTipoImpresion",
                "simUsuario.usuNombre",
                "simFechaSolicitud",
                "simDefinicionTitulo.dtiPk",
                "simDefinicionTitulo.dtiVersion",
                "simDefinicionTitulo.dtiNombre",
                "simReposicionTitulo.retSedeNombre",
                "simDefinicionTitulo.dtiNombreCertificado",
                "simReposicionEstudianteSiges",
                "simVersion"});
            filtro.setSimEstado(comboEstado.getSelectedT());
            filtro.setSimDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setSimMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setSimSede(sedSeleccionado != null ? sedSeleccionado.getSedPk() : null);
            filtro.setSimTipoSolicitudImpresion(comboTipo.getSelectedT());
            filtro.setFirst(new Long(0));
            filtro.setSimCantidadEstudiantes(Boolean.TRUE);

            totalResultados = restClient.buscarTotal(filtro);
            solicitudImpresionLazyModel = new LazySolicitudImpresionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String obtenerNombreTitulo(SgSolicitudImpresion sim) {
        String nombreCerificado = sim.getSimDefinicionTitulo().getDtiNombreCertificado();
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
        return nombreCerificado;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgDepartamento> deptos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(deptos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoSolicitudImpresion> estados = new ArrayList(Arrays.asList(EnumEstadoSolicitudImpresion.values()));
            estados.remove(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstado.setSelectedT(EnumEstadoSolicitudImpresion.ENVIADA);

            FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
            fsft.setSftHabilitado(Boolean.TRUE);
            List<SgSelloFirmaTitular> listSelloFirmaTitular = selloFirmaTitularRestClient.buscar(fsft);
            comboSelloFirmaTitular = new SofisComboG(listSelloFirmaTitular, "sftNombreCompleto");
            comboSelloFirmaTitular.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumTipoSolicitudImpresion> listTipo=new ArrayList(Arrays.asList(EnumTipoSolicitudImpresion.values()));
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SIN_IMPRIMIR_TITULO)) {
                listTipo.remove(EnumTipoSolicitudImpresion.SIM);
            }
            
            comboTipo = new SofisComboG(listTipo, "text");
            comboTipo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {

            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
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

    public String colorRow(SgSolicitudImpresion est) {

        if (est != null) {
            switch (est.getSimEstado()) {
                case IMPRESA:
                    return "aprobadoSolicitud";
                case CONFIRMADA:
                    return "confirmado";
                case CON_EXCEPCIONES:
                    return "con-excepciones";
                case RECHAZADA:
                    return "reprobado";
                case ANULADA:
                    return "reprobado";
                default:

            }

        }
        return null;
    }

    public Integer cantidadEstudiantes(SgSolicitudImpresion solImp) {
        if (solImp.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REP)) {
            return 1;
        } else {
            return solImp.getSimEstudianteImpresion() != null ? solImp.getSimEstudianteImpresion().size() : 0;
        }
    }

    private void limpiarCombos() {
        comboEstado.setSelected(-1);
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        comboSelloFirmaTitular.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroSolicitudImpresion();
        sedSeleccionado = null;
        limpiarCombos();
        buscar();
    }

    public void seleccionarDepartamento() {
        try {

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboDepartamento.getSelectedT() != null) {

                FiltroMunicipio fc = new FiltroMunicipio();
                fc.setOrderBy(new String[]{"munNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fc.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());

                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fc);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSolicitudImpresion();
    }

    public void actualizar(SgSolicitudImpresion var) {
        limpiarCombos();
        entidadEnEdicion = (SgSolicitudImpresion) SerializationUtils.clone(var);

    }

    /*
    public void guardar() {
        try {
            entidadEnEdicion.setSimSelloAutentica(comboSelloFirmaTitular.getSelectedT());          
                        
            entidadEnEdicion.setSimEstado(EnumEstadoSolicitudImpresion.EMISION);
            entidadEnEdicion.setSimFechaEnviadoImprimir(LocalDate.now());
            buscarDirectorMinistro();
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }*/
    public void rechazar() {
        try {

            entidadEnEdicion = restClient.rechazarSolicitud(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void anular() {
        try {

            entidadEnEdicion = restClient.anularSolicitud(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarDirectorMinistro() {
        try {
            if (entidadEnEdicion.getSimSeccion() != null) {
                FiltroSelloFirma fsf = new FiltroSelloFirma();
                fsf.setSfiHabilitado(Boolean.TRUE);
                fsf.setSedPk(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                fsf.setSfiTipoRepresentanteCodigo(Constantes.TIPO_REPRESENTANTE_DIRECTOR);
                List<SgSelloFirma> listSelloFirma = selloFirmaRestClient.buscar(fsf);
                if (listSelloFirma.size() > 0) {
                    SgSelloFirma selloFirma = listSelloFirma.get(0);
                    entidadEnEdicion.setSimSelloDirector(selloFirma);
                }

            }
            FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
            fsft.setSftHabilitado(Boolean.TRUE);
            List<SgSelloFirmaTitular> listSelloFirmaTitular = selloFirmaTitularRestClient.buscar(fsft);
            if (listSelloFirmaTitular.size() > 0) {
                entidadEnEdicion.setSimSelloMinistro(listSelloFirmaTitular.get(0));//TODO:SE QUEDA CON EL PRIMERO, HAY QUE HACER QUE SE QUEDE CON EL MINISTRO AGREGANDO BOOLEANO EN CATALOGO 
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSimPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
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

    public SofisComboG<EnumEstadoSolicitudImpresion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSolicitudImpresion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SgSede getSedSeleccionado() {
        return sedSeleccionado;
    }

    public void setSedSeleccionado(SgSede sedSeleccionado) {
        this.sedSeleccionado = sedSeleccionado;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SofisComboG<SgSelloFirmaTitular> getComboSelloFirmaTitular() {
        return comboSelloFirmaTitular;
    }

    public void setComboSelloFirmaTitular(SofisComboG<SgSelloFirmaTitular> comboSelloFirmaTitular) {
        this.comboSelloFirmaTitular = comboSelloFirmaTitular;
    }

    public SofisComboG<EnumTipoSolicitudImpresion> getComboTipo() {
        return comboTipo;
    }

    public void setComboTipo(SofisComboG<EnumTipoSolicitudImpresion> comboTipo) {
        this.comboTipo = comboTipo;
    }

}
