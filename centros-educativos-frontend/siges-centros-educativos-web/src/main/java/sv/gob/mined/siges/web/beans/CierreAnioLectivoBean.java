package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCierreAnioLectivoSede;
import sv.gob.mined.siges.web.dto.SgResumenCierreSedeRequest;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgPreguntaCierreAnio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CierreAnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author bruno
 */
@Named
@ViewScoped
public class CierreAnioLectivoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CierreAnioLectivoBean.class.getName());

    @Inject
    private AnioLectivoRestClient anioLectivoClient;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private CierreAnioLectivoRestClient cierreAnioLectivoRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    @Param(name = "id")
    private Long cierreId;

    @Inject
    @Param(name = "rev")
    private Long aleRev;

    @Inject
    @Param(name = "id_transaccion")
    private String idTransaccionFirma;

    private SgCierreAnioLectivoSede entidadEnEdicion;
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private SgConfiguracion mensajeCierre;

    private List<SgPreguntaCierreAnio> preguntasCierre;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (cierreId != null && cierreId > 0) {
                if (aleRev != null && aleRev > 0) {
                    //this.actualizar(cierreAnioLectivoRestClient.obtenerEnRevision(aleId, aleRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    cargarResumenCierreAnio();
                    soloLectura = Boolean.TRUE;
                }
            } else if (idTransaccionFirma != null) {
                this.confirmarFirma(idTransaccionFirma);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setIncluirCampos(new String[]{"pcaPregunta", "pcaNombre", "pcaVersion"});
            fc.setOrderBy(new String[]{"pcaPk"});
            preguntasCierre = catalogoClient.buscarPreguntasCierreAnio(fc);

            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAscending(new boolean[]{false});
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAleCorriente(Boolean.TRUE);
            List<SgAnioLectivo> listAnio = anioLectivoClient.buscar(fal);
            comboAnioLectivo = new SofisComboG(listAnio, "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (mensajeCierre == null) {
                mensajeCierre = catalogoClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_MENSAJE_CIERRE_ANIO);
            }

            cargarSedePorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CIERRE_ANIO_LECTIVO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void seleccionarSede() {
        comboAnioLectivo.setSelected(-1);
        entidadEnEdicion = null;
    }

    public void seleccionarAnioLectivo() {
        try {
            if (this.comboAnioLectivo.getSelectedT() != null) {

                cargarResumenCierreAnio();

                if (entidadEnEdicion.getCalPk() == null) {
                    //Cargar preguntas
                    entidadEnEdicion.actualizarPreguntas(preguntasCierre);
                }

            } else {
                this.entidadEnEdicion = null;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarResumenCierreAnio() {
        try {
            //Cargar resumen secciones
            SgResumenCierreSedeRequest req = new SgResumenCierreSedeRequest();
            if (this.comboAnioLectivo != null && this.comboAnioLectivo.getSelectedT() != null) {
                req.setAnioLectivoPk(this.comboAnioLectivo.getSelectedT().getAlePk());
            }
            if (this.getSedeSeleccionada() != null) {
                req.setSedePk(this.sedeSeleccionada.getSedPk());
            }
            req.setCierreAnioPk(cierreId);
            entidadEnEdicion = cierreAnioLectivoRestClient.obtenerResumenCierreSedeAnio(req);
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        if (this.entidadEnEdicion == null || this.entidadEnEdicion.getCalPk() == null) {
            return Etiquetas.getValue("cerrarAnioLectivo");
        } else {
            return Etiquetas.getValue("visualizarCierreAnioLectivo");
        }
    }

    public void confirmar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
            entidadEnEdicion.setCalTransactionReturnUrl(baseURL + "/pp/" + ConstantesPaginas.CIERRE_ANIO_LECTIVO);
            SgCierreAnioLectivoSede conf = this.cierreAnioLectivoRestClient.preconfirmar(entidadEnEdicion);

            if (!StringUtils.isBlank(conf.getCalTransactionSignatureUrl())) {
                PrimeFaces.current().executeScript("window.location.replace(\"" + conf.getCalTransactionSignatureUrl() + "\");");
            } else {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ANIO_LECTIVO_CERRADO_CORRECTO), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void confirmarFirma(String transactionId) {
        try {
            SgCierreAnioLectivoSede cierre = this.cierreAnioLectivoRestClient.confirmar(transactionId);
            cierreId = cierre.getCalPk();
            cargarResumenCierreAnio();
            soloLectura = Boolean.TRUE;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    /**
     * Cierra el año lectivo de una Sede
     */
    public void cerrarAnioLectivo() {
        try {
            entidadEnEdicion.setCalSede(sedeSeleccionada);
            entidadEnEdicion.setCalAnioLectivo(this.comboAnioLectivo.getSelectedT());
            if (entidadEnEdicion.getCalPk() == null) {
                SgCierreAnioLectivoSede guardado = cierreAnioLectivoRestClient.guardar(entidadEnEdicion);
                entidadEnEdicion.setCalPk(guardado.getCalPk()); //Deshabilitar edit
            }
            confirmar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgCierreAnioLectivoSede getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCierreAnioLectivoSede entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SgConfiguracion getMensajeCierre() {
        return mensajeCierre;
    }

    public void setMensajeCierre(SgConfiguracion mensajeCierre) {
        this.mensajeCierre = mensajeCierre;
    }

}
