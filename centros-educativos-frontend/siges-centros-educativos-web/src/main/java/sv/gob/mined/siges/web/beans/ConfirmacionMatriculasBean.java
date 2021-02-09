/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
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
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgConfirmacionMatriculas;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConfirmacionMatriculas;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazySeccionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.ConfirmacionMatriculasRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ConfirmacionMatriculasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfirmacionMatriculasBean.class.getName());

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private AnioLectivoRestClient anioLectivoClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ConfirmacionMatriculasRestClient confMatClient;

    @Inject
    private MatriculaRestClient matriculaClient;

    @Inject
    @Param(name = "id")
    private Long cmaId;

    @Inject
    @Param(name = "id_transaccion")
    private String idTransaccionFirma;

    private LazySeccionDataModel seccionLazyModel;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private Long totalMatriculas;
    private Long totalHombres;
    private Long totalMujeres;
    private FiltroSeccion filtroSeccion = new FiltroSeccion();
    private FiltroAnioLectivo filtroAnio = new FiltroAnioLectivo();
    private FiltroConfirmacionMatriculas filtroConfMat = new FiltroConfirmacionMatriculas();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private SgConfirmacionMatriculas conf;

    public ConfirmacionMatriculasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            filtroSeccion.setIncluirCampos(new String[]{
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "secServicioEducativo.sduOpcion.opcNombre",
                "secServicioEducativo.sduProgramaEducativo.pedNombre",
                "secServicioEducativo.sduGrado.graNombre",
                "secNombre",
                "secJornadaLaboral.jlaNombre",
                "secPk",
                "secVersion"});
            filtroSeccion.setSecIncluirMatriculasCampos(new String[]{"matEstudiante.estPersona.perSexo.sexCodigo"});
            filtroSeccion.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicOrden",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modOrden",
                "secServicioEducativo.sduGrado.graOrden",
                "secNombre"});
            filtroSeccion.setAscending(new boolean[]{true, true, true, true, true});
            validarSedesAsignadas();
            if (cmaId != null) {
                cargarSedeAnio();
                soloLectura = Boolean.TRUE;
            }
            if (idTransaccionFirma != null) {
                this.confirmarFirma(idTransaccionFirma);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void onload() {
        //No borrar
        //Evento preRenderView utilizado para que el PostConstruct 
        //se ejecute antes que el render de los p:messages y nos permita mostrar mensajes de confirmarFirma en pantalla
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONFIRMACION_MATRICULAS)
                && !sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONFIRMACIONES_MATRICULAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarSedeAnio() {
        try {
            if (cmaId != null) {
                FiltroConfirmacionMatriculas filtro = new FiltroConfirmacionMatriculas();
                filtro.setCmaPk(cmaId);
                filtro.setIncluirCampos(new String[]{
                    "cmaSede.sedPk",
                    "cmaSede.sedCodigo",
                    "cmaSede.sedNombre",
                    "cmaSede.sedTipo",
                    "cmaSede.sedVersion",
                    "cmaAnioLectivo.alePk",
                    "cmaAnioLectivo.aleAnio",
                    "cmaAnioLectivo.aleVersion",
                    "cmaUsuarioConfirmacion",
                    "cmaFechaConfirmacion",
                    "cmaArhivoFirmado.achPk",
                    "cmaArhivoFirmado.achNombre",
                    "cmaArhivoFirmado.achExt",
                    "cmaArhivoFirmado.achContentType",
                    "cmaFirmada",
                    "cmaFirmaTransactionId",
                    "cmaVersion"

                });
                List<SgConfirmacionMatriculas> confs = confMatClient.buscar(filtro);

                if (!confs.isEmpty()) {
                    conf = confs.get(0);
                    sedeSeleccionada = conf.getCmaSede();
                    comboAnioLectivo.setSelectedT(conf.getCmaAnioLectivo());
                    cargarSecciones();
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarConfirmacion() {
        try {
            if (sedeSeleccionada != null && this.comboAnioLectivo.getSelectedT() != null) {
                FiltroConfirmacionMatriculas filtro = new FiltroConfirmacionMatriculas();
                filtro.setSedeFk(sedeSeleccionada.getSedPk());
                filtro.setAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                filtro.setIncluirCampos(new String[]{
                    "cmaSede.sedPk",
                    "cmaSede.sedCodigo",
                    "cmaSede.sedNombre",
                    "cmaSede.sedTipo",
                    "cmaSede.sedVersion",
                    "cmaAnioLectivo.alePk",
                    "cmaAnioLectivo.aleAnio",
                    "cmaAnioLectivo.aleVersion",
                    "cmaUsuarioConfirmacion",
                    "cmaFechaConfirmacion",
                    "cmaArhivoFirmado.achPk",
                    "cmaArhivoFirmado.achNombre",
                    "cmaArhivoFirmado.achExt",
                    "cmaArhivoFirmado.achContentType",
                    "cmaFirmada",
                    "cmaFirmaTransactionId",
                    "cmaVersion"

                });
                List<SgConfirmacionMatriculas> confs = confMatClient.buscar(filtro);

                if (!confs.isEmpty()) {
                    conf = confs.get(0);
                } else {
                    conf = new SgConfirmacionMatriculas();
                }

                cargarSecciones();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {
        try {
            if (comboAnioLectivo == null) {
                FiltroAnioLectivo fc2 = new FiltroAnioLectivo();
                fc2.setOrderBy(new String[]{"aleAnio"});
                fc2.setAscending(new boolean[]{false});
                fc2.setAleCorriente(Boolean.TRUE);
                comboAnioLectivo = new SofisComboG(anioLectivoClient.buscar(fc2), "aleAnio");
                comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboAnioLectivo.setSelectedT((SgAnioLectivo) this.comboAnioLectivo.getAllTs().get(this.comboAnioLectivo.getAllTs().size() - 1));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarSedesAsignadas() {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
            cargarConfirmacion();
        }
    }

    public void cargarSecciones() {
        try {
            if (sedeSeleccionada != null && this.comboAnioLectivo.getSelectedT() != null) {
                filtroSeccion.setSecSedeFk(sedeSeleccionada.getSedPk());
                filtroSeccion.setSecAnioLectivoFk(this.comboAnioLectivo.getSelectedT().getAlePk());
                filtroConfMat.setSedeFk(sedeSeleccionada.getSedPk());
                filtroConfMat.setAnioLectivoFk(this.comboAnioLectivo.getSelectedT().getAlePk());

                totalResultados = seccionClient.buscarTotal(filtroSeccion);
                seccionLazyModel = new LazySeccionDataModel(seccionClient, filtroSeccion, totalResultados, paginado);
                totalEnSede();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void totalEnSede() {
        try {
            if (sedeSeleccionada != null && this.comboAnioLectivo.getSelectedT() != null) {
                FiltroMatricula fm = new FiltroMatricula();
                fm.setSecurityOperation(null);
                fm.setMatRetirada(Boolean.FALSE);
                fm.setIncluirCampos(new String[]{"matPk", "matEstudiante.estPersona.perSexo.sexCodigo", "matVersion"});
                fm.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                fm.setSecSedeFk(sedeSeleccionada.getSedPk());
                List<SgMatricula> listMat = matriculaClient.buscar(fm);
                totalMatriculas = Long.valueOf(listMat.size());

                totalHombres = listMat.isEmpty() ? 0 : listMat.stream().map(m -> m.getMatEstudiante().getEstPersona().getPerSexo())
                        .filter(s -> Constantes.CODIGO_SEXO_MASCULINO.equals(s.getSexCodigo()))
                        .count();
                totalMujeres = listMat.isEmpty() ? 0 : listMat.stream().map(m -> m.getMatEstudiante().getEstPersona().getPerSexo())
                        .filter(s -> Constantes.CODIGO_SEXO_FEMENINO.equals(s.getSexCodigo()))
                        .count();

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void confirmar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            if (conf == null) {
                conf = new SgConfirmacionMatriculas();
            }
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
            conf.setCmaTransactionReturnUrl(baseURL + "/pp/confirmacionMatriculas");
            conf.setCmaSede(sedeSeleccionada);
            conf.setCmaAnioLectivo(this.comboAnioLectivo.getSelectedT());
            conf = this.confMatClient.preconfirmar(conf);

            if (!StringUtils.isBlank(conf.getCmaTransactionSignatureUrl())) {
                PrimeFaces.current().executeScript("window.location.replace(\"" + conf.getCmaTransactionSignatureUrl() + "\");");
            } else {
                //Firma deshabilitada
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
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
            conf = this.confMatClient.confirmar(transactionId);
            sedeSeleccionada = conf.getCmaSede();
            comboAnioLectivo.setSelectedT(conf.getCmaAnioLectivo());
            cargarSecciones();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSecurityOperation(ConstantesOperaciones.CONFIRMAR_MATRICULAS);
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void unselectSede() {
        this.sedeSeleccionada = null;
    }

    public Long getTotalMatriculas() {
        return totalMatriculas;
    }

    public void setTotalMatriculas(Long totalMatriculas) {
        this.totalMatriculas = totalMatriculas;
    }

    public Long getTotalHombres() {
        return totalHombres;
    }

    public void setTotalHombres(Long totalHombres) {
        this.totalHombres = totalHombres;
    }

    public Long getTotalMujeres() {
        return totalMujeres;
    }

    public void setTotalMujeres(Long totalMujeres) {
        this.totalMujeres = totalMujeres;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public LazySeccionDataModel getSeccionLazyModel() {
        return seccionLazyModel;
    }

    public void setSeccionLazyModel(LazySeccionDataModel seccionLazyModel) {
        this.seccionLazyModel = seccionLazyModel;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgConfirmacionMatriculas getConf() {
        return conf;
    }

    public void setConf(SgConfirmacionMatriculas conf) {
        this.conf = conf;
    }

}
