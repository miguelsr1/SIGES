/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudianteImpresion;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoReimpresion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudianteImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTitulo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteImpresionRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ReimpresionTituloBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReimpresionTituloBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private TituloRestClient tituloRestClient;

    @Inject
    private SolicitudImpresionRestClient solicitudImpresionRestClient;
    
    @Inject
    private EstudianteImpresionRestClient estudianteImpresionRestClient;

    private Long nieBuscar;
    private List<SgTitulo> titEstudiantes;
    private SgEstudiante estudianteEnEdicion;
    private SofisComboG<SgMotivoReimpresion> comboMotivoReimpresion;
    private List<SgSolicitudImpresion> solicitudImpresionEstudiante;
    private SgSolicitudImpresion entidadEnEdicion;
    private Integer paginado = 10;
    private Long totalResultados;

    public ReimpresionTituloBean() {
    }

    @PostConstruct
    public void init() {
        try {

            validarAcceso();
            cargarCombos();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REIMPRESION_TITULO) || !sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_REIMPRESION_TITULO)) {
            JSFUtils.redirectToIndex();
        }

    }

    public void buscarNIE() {

        try {
            estudianteEnEdicion = null;
            solicitudImpresionEstudiante = null;
            entidadEnEdicion = null;
            solicitudImpresionEstudiante = new ArrayList();
            if (nieBuscar != null) {
                FiltroTitulo ft = new FiltroTitulo();
                ft.setTitNie(nieBuscar);
                ft.setTitEstadoSolicitudImp(EnumEstadoSolicitudImpresion.IMPRESA);
                ft.setIncluirCampos(new String[]{
                    "titEstudianteFk.estPk",
                    "titEstudianteFk.estVersion",
                    "titNombreEstudiante",
                    "titNombreEstudiantePartida",
                    "titNombreCertificado",
                    "titFechaValidez",
                    "titFechaEmision",
                    "titSelloFirmaDirectorFk.sfiPk",
                    "titSelloFirmaDirectorFk.sfiVersion",
                    "titSelloFirmaTitularMinistroFk.sftPk",
                    "titSelloFirmaTitularMinistroFk.sftVersion",
                    "titSelloFirmaTitularAutenticaFk.sftPk",
                    "titSelloFirmaTitularAutenticaFk.sftVersion",
                    "titNombreDirector",
                    "titNombreMinistro",
                    "titAnio",
                    "titSedeFk.sedPk",
                    "titSedeFk.sedVersion",
                     "titSedeFk.sedTipo",
                    "titSedeCodigo",
                    "titSedeNombre",
                    "titServicioEducativoFk.sduPk",
                    "titServicioEducativoFk.sduVersion",
                    "titSolicitudImpresionFk.simPk",
                    "titSolicitudImpresionFk.simVersion",
                    "titSolicitudImpresionFk.simSeccion.secPk",
                    "titSolicitudImpresionFk.simSeccion.secVersion",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedNombre",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedTelefono",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedTipo",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                    "titSolicitudImpresionFk.simSeccion.secServicioEducativo.sduGrado.graNombre",
                    "titSolicitudImpresionFk.simSeccion.secNombre",
                    "titSolicitudImpresionFk.simDefinicionTitulo.dtiNombre",
                    "titSolicitudImpresionFk.simDefinicionTitulo.dtiVersion",
                    "titSolicitudImpresionFk.simDefinicionTitulo.dtiPk",
                    "titSolicitudImpresionFk.simMotivoReimpresion.mriNombre",
                    "titSolicitudImpresionFk.simMotivoReimpresion.mriPk",
                    "titSolicitudImpresionFk.simMotivoReimpresion.mriVersion",
                    "titUsuarioEnviaImprimir",
                    "titHash",
                    "titVersion",
                    "titAnulada",
                    "titMotivoReimpresion.mriNombre",
                    "titMotivoReimpresion.mriPk",
                    "titMotivoReimpresion.mriVersion"
                });
                titEstudiantes = tituloRestClient.buscar(ft);

                if (titEstudiantes.isEmpty()) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.NO_EXISTE_TITULO_PARA_ESTUDIANTE), "");
                } else {

                    solicitudImpresionEstudiante = new ArrayList();
                    estudianteEnEdicion = titEstudiantes.get(0).getTitEstudianteFk();
                    
                    FiltroEstudianteImpresion fei=new FiltroEstudianteImpresion();
                    List<EnumEstadoSolicitudImpresion> listEstado=new ArrayList();
                    listEstado.add(EnumEstadoSolicitudImpresion.ENVIADA);
                    listEstado.add(EnumEstadoSolicitudImpresion.CONFIRMADA);
                    listEstado.add(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
                    listEstado.add(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                    fei.setEimEstadosSolicitud(listEstado);
                    fei.setEimNoAnulada(Boolean.TRUE);
                    fei.setSimTipoSolicitudImpresion(EnumTipoSolicitudImpresion.REI);
                    fei.setEimNie(nieBuscar);
                    fei.setIncluirCampos(new String[]{"eimSolicitudImpresion.simSeccion.secPk"});
                    List<SgEstudianteImpresion> estudianteImpresionList = estudianteImpresionRestClient.buscar(fei);

                   
                    if (!estudianteImpresionList.isEmpty()) {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_2, "El estudiante seleccionado tiene solicitudes de reimpresión de título generadas.", "");
                    }

                    for (SgTitulo tit : titEstudiantes) {
                        List<SgEstudianteImpresion> solImpExiste = estudianteImpresionList.stream().filter(c -> c.getEimSolicitudImpresion().getSimSeccion().getSecPk().equals(tit.getTitSolicitudImpresionFk().getSimSeccion().getSecPk())).collect(Collectors.toList());
                        if (!solImpExiste.isEmpty()) {
                            // solicitudImpresionEstudiante.add(solImpExiste.get(0));
                        } else {
                            SgSolicitudImpresion solImprNuevo = new SgSolicitudImpresion();
                            solImprNuevo.setSimEstado(EnumEstadoSolicitudImpresion.ENVIADA);
                            solImprNuevo.setSimFechaSolicitud(LocalDate.now());
                            solImprNuevo.setSimSeccion(tit.getTitSolicitudImpresionFk().getSimSeccion());
                            solImprNuevo.setSimTipoImpresion(EnumTipoSolicitudImpresion.REI);
                            solImprNuevo.setSimSelloDirector(tit.getTitSelloFirmaDirectorFk());
                            solImprNuevo.setSimDefinicionTitulo(tit.getTitSolicitudImpresionFk().getSimDefinicionTitulo());
                            solImprNuevo.setSimUsuario(sessionBean.getEntidadUsuario());
                            solImprNuevo.setSimTituloAnulado(tit.getTitAnulada()!=null?tit.getTitAnulada():Boolean.FALSE);
                            solImprNuevo.setSimMotivoReimpresion(tit.getTitMotivoReimpresion());
                            solicitudImpresionEstudiante.add(solImprNuevo);
                        }
                    }
                  

                }

            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_VACIO), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setOrderBy(new String[]{"mriNombre", "mriVersion"});
            comboMotivoReimpresion = new SofisComboG(catalogoRestClient.buscarMotivoReimpresion(fc), "mriNombre");
            comboMotivoReimpresion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void elementoGuardar(SgSolicitudImpresion elem) {
        entidadEnEdicion = elem;
        comboMotivoReimpresion.setSelected(-1);
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getSimPk() == null) {
                entidadEnEdicion.setSimMotivoReimpresion(comboMotivoReimpresion.getSelectedT());
                SgEstudianteImpresion estImp = new SgEstudianteImpresion();
                estImp.setEimAnulada(Boolean.FALSE);
                estImp.setEimEstudiante(estudianteEnEdicion);
                List<SgEstudianteImpresion> estImpesion = new ArrayList();
                estImpesion.add(estImp);
                entidadEnEdicion.setSimEstudianteImpresion(estImpesion);
                entidadEnEdicion = solicitudImpresionRestClient.guardar(entidadEnEdicion);
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                buscarNIE();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public String colorFila(SgSolicitudImpresion simTit){
        if(simTit.getSimTituloAnulado()!=null && simTit.getSimTituloAnulado()){
            return "anulado";
        }
        return null;
    }

    public Long getNieBuscar() {
        return nieBuscar;
    }

    public void setNieBuscar(Long nieBuscar) {
        this.nieBuscar = nieBuscar;
    }

    public List<SgTitulo> getTitEstudiantes() {
        return titEstudiantes;
    }

    public void setTitEstudiantes(List<SgTitulo> titEstudiantes) {
        this.titEstudiantes = titEstudiantes;
    }

    public SgEstudiante getEstudianteEnEdicion() {
        return estudianteEnEdicion;
    }

    public void setEstudianteEnEdicion(SgEstudiante estudianteEnEdicion) {
        this.estudianteEnEdicion = estudianteEnEdicion;
    }

    public SofisComboG<SgMotivoReimpresion> getComboMotivoReimpresion() {
        return comboMotivoReimpresion;
    }

    public void setComboMotivoReimpresion(SofisComboG<SgMotivoReimpresion> comboMotivoReimpresion) {
        this.comboMotivoReimpresion = comboMotivoReimpresion;
    }

    public List<SgSolicitudImpresion> getSolicitudImpresionEstudiante() {
        return solicitudImpresionEstudiante;
    }

    public void setSolicitudImpresionEstudiante(List<SgSolicitudImpresion> solicitudImpresionEstudiante) {
        this.solicitudImpresionEstudiante = solicitudImpresionEstudiante;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public SgSolicitudImpresion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudImpresion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

}
