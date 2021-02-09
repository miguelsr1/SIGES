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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgReposicionSolicitud;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroDefinicionTitulo;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyTituloDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ReposicionTituloRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TituloBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TituloBean.class.getName());

    @Inject
    private TituloRestClient restClient;

    @Inject
    private ReposicionTituloRestClient reposicionTituloRestClient;
    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstRestClient;

    private FiltroTitulo filtro = new FiltroTitulo();
    private SgTitulo entidadEnEdicion = new SgTitulo();
    private List<RevHistorico> historialTitulo = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTituloDataModel tituloLazyModel;
    private SofisComboG<EnumEstadoSolicitudImpresion> estadoCombo;
    private SgReposicionSolicitud repSolicitud;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(zip|pdf)$/";
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SofisComboG<SgDefinicionTitulo> comboDefinicionTitulo;
    private Boolean renderComboTitulo;
    private Boolean renderExportar;

    public TituloBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarConfiguracion();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            renderExportar=Boolean.FALSE;
            filtro.setIncluirCampos(new String[]{
                "titEstudianteFk.estVersion","titEstudianteFk.estPk",
                "titEstudianteFk.estPersona.perVersion",
                "titSedeFk.sedVersion","titSedeFk.sedPk","titSedeFk.sedTipo",
                "titSolicitudImpresionFk.simPk","titSolicitudImpresionFk.simVersion",
                "titEstudianteFk.estPersona.perNie",
                "titNombreEstudiante",
                "titSolicitudImpresionFk.simEstado",
                "titNombreCertificado",
                "titFechaEmision",
                "titFechaValidez",
                "titAnulada",
                "titReposicion",
                "titNumeroRegistro",
                "titSolicitudImpresionFk.simEstado",
                "titSolicitudImpresionFk.simTipoImpresion",
                "titDuiEstudiante",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedTipo",
                "titSedeFk.sedDireccion.dirDepartamento.depNombre",
                "titSedeFk.sedDireccion.dirMunicipio.munNombre",
                "titSedeFk.sedTipo",
                "titSedeCodigo",
                "titSedeNombre",
                "titSeccionFk.secPk",
                "titSeccionFk.secServicioEducativo.sduOpcion.opcNombre",
                "titSeccionFk.secServicioEducativo.sduProgramaEducativo.pedNombre",
                "titSeccionFk.secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",
                "titSeccionFk.secVersion",
                "titSeccionFk.secServicioEducativo.sduGrado.graPk",
                "titSeccionFk.secPlanEstudio.pesPk",
                "titVersion",
                "titAnio"});
            filtro.setTitEstadoSolicitudImp(estadoCombo.getSelectedT());
            filtro.setDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            tituloLazyModel = new LazyTituloDataModel(restClient, filtro, totalResultados, paginado);
            
            if(comboDepartamento.getSelectedT()!=null){
                renderExportar=Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            List<EnumEstadoSolicitudImpresion> estados = new ArrayList();
            estados.add(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
            estados.add(EnumEstadoSolicitudImpresion.IMPRESA);
            estados.add(EnumEstadoSolicitudImpresion.ANULADA);
            estadoCombo = new SofisComboG(estados, "text");
            estadoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgDepartamento> deptos = catalogoRestClient.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(deptos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDefinicionTitulo = new SofisComboG();
            comboDefinicionTitulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarConfiguracion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.TAMANIO_ARCHIVO_REPOSICION_TITULO);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tamanioImportArchivo = conf.get(0).getConValor();
            }
            fc.setCodigo(Constantes.TIPO_ARCHIVO_REPOSICION_TITULO);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tipoImportArchivo = conf.get(0).getConValor();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        estadoCombo.setSelected(-1);
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        filtro = new FiltroTitulo();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTitulo();
    }

    public void actualizar(SgTitulo var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgTitulo) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
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
    }
    
    public void anular() {
        try {
            entidadEnEdicion.setTitAnulada(Boolean.TRUE);
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
    }

    public void nuevaReposicionTitulo(SgTitulo tit) {
        repSolicitud = new SgReposicionSolicitud();
        repSolicitud.setTituloPk(tit.getTitPk());
        repSolicitud.setUsuarioSolicitante(sessionBean.getEntidadUsuario());

        comboDefinicionTitulo = new SofisComboG();
        comboDefinicionTitulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        renderComboTitulo = Boolean.FALSE;

        //los titulos migrados no tienen solicitud asociada, en este caso se tiene que seleccionar la definicion titulo
        if (tit.getTitSeccionFk() != null) {
            renderComboTitulo = Boolean.TRUE;
            cargarDefinicionTitulo(tit.getTitSeccionFk());
        }
    }

    public void cargarDefinicionTitulo(SgSeccion sec) {
        try {

            if (sec != null) {
                List<SgDefinicionTitulo> defTitList = new ArrayList();

                if (sec.getSecServicioEducativo().getSduGrado() != null && sec.getSecPlanEstudio() != null) {
                    FiltroRelGradoPlanEstudio fgpe = new FiltroRelGradoPlanEstudio();
                    fgpe.setRgpGrado(sec.getSecServicioEducativo().getSduGrado().getGraPk());
                    fgpe.setRgpPlanEstudio(sec.getSecPlanEstudio().getPesPk());
                    defTitList = new ArrayList(relGradoPlanEstRestClient.buscarDefinicionTitulo(fgpe));
                } else {
                    FiltroDefinicionTitulo fil = new FiltroDefinicionTitulo();
                    fil.setDtiEsTipoReposicion(Boolean.FALSE);
                    fil.setIncluirCampos(new String[]{"dtiNombre","dtiNombreCertificado","dtiPk","dtiVersion"});
                    defTitList=catalogoRestClient.buscarDefinicionTitulo(fil);
                }

                defTitList = adaptarNombreEnCertificado(defTitList, sec);

                comboDefinicionTitulo = new SofisComboG(defTitList, "dtiNombreCertificado");
                comboDefinicionTitulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgDefinicionTitulo> adaptarNombreEnCertificado(List<SgDefinicionTitulo> defTitList, SgSeccion sec) {
        for (SgDefinicionTitulo defTit : defTitList) {
            String nombreCertificado = defTit.getDtiNombreCertificado();
            if (sec != null) {
                if (nombreCertificado.contains("{OPCION}")) {

                    nombreCertificado = nombreCertificado.replace("{OPCION}", sec.getSecServicioEducativo().getSduOpcion() != null ? "" + sec.getSecServicioEducativo().getSduOpcion().getOpcNombre() : "");
                }

                if (nombreCertificado.contains("{PROGRAMA}")) {
                    nombreCertificado = nombreCertificado.replace("{PROGRAMA}", sec.getSecServicioEducativo().getSduProgramaEducativo() != null ? "" + sec.getSecServicioEducativo().getSduProgramaEducativo().getPedNombre() : "");
                }

                if (nombreCertificado.contains("{SECTOR}")) {
                    nombreCertificado = nombreCertificado.replace("{SECTOR}", sec.getSecServicioEducativo().getSduOpcion() != null && sec.getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico() != null ? "" + sec.getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico().getSecNombre() : "");
                }
                defTit.setDtiNombreCertificado(nombreCertificado);
            }
        }
        return defTitList;
    }

    public void generarReposicionTitulo() {
        try {
            repSolicitud.setDefinicionTitulo(comboDefinicionTitulo.getSelectedT());
            reposicionTituloRestClient.reponerSolicitud(repSolicitud);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.REPOSICION_GENERADA_CORRECTAMENTE), "");
            buscar();
            repSolicitud = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void subirResolucion(FileUploadEvent event) {
        try {

            SgArchivo arc = new SgArchivo();
            handleArchivoBean.subirArchivoTmp(event.getFile(), arc);
            repSolicitud.setResolucion(arc);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTitPk());
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
            historialTitulo = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

                List<SgMunicipio> municipios = catalogoRestClient.buscarMunicipio(fc);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public FiltroTitulo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTitulo filtro) {
        this.filtro = filtro;
    }

    public SgTitulo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTitulo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialTitulo() {
        return historialTitulo;
    }

    public void setHistorialTitulo(List<RevHistorico> historialTitulo) {
        this.historialTitulo = historialTitulo;
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

    public LazyTituloDataModel getTituloLazyModel() {
        return tituloLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTituloDataModel tituloLazyModel) {
        this.tituloLazyModel = tituloLazyModel;
    }

    public SofisComboG<EnumEstadoSolicitudImpresion> getEstadoCombo() {
        return estadoCombo;
    }

    public void setEstadoCombo(SofisComboG<EnumEstadoSolicitudImpresion> estadoCombo) {
        this.estadoCombo = estadoCombo;
    }

    public SgReposicionSolicitud getRepSolicitud() {
        return repSolicitud;
    }

    public void setRepSolicitud(SgReposicionSolicitud repSolicitud) {
        this.repSolicitud = repSolicitud;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public SofisComboG<SgDefinicionTitulo> getComboDefinicionTitulo() {
        return comboDefinicionTitulo;
    }

    public void setComboDefinicionTitulo(SofisComboG<SgDefinicionTitulo> comboDefinicionTitulo) {
        this.comboDefinicionTitulo = comboDefinicionTitulo;
    }

    public Boolean getRenderComboTitulo() {
        return renderComboTitulo;
    }

    public void setRenderComboTitulo(Boolean renderComboTitulo) {
        this.renderComboTitulo = renderComboTitulo;
    }
    public Boolean getRenderExportar() {
        return renderExportar;
    }

    public void setRenderExportar(Boolean renderExportar) {
        this.renderExportar = renderExportar;
    }
}
