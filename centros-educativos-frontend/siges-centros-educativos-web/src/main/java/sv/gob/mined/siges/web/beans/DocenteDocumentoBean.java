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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgDocenteDocumento;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoDocumentoDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDocenteDocumento;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DocenteDocumentoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DocenteDocumentoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocenteDocumentoBean.class.getName());

    @Inject
    private DocenteDocumentoRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    private SgDocenteDocumento entidadEnEdicion = new SgDocenteDocumento();
    private SgPersonalSedeEducativa personalSede;
    private FiltroDocenteDocumento filtro = new FiltroDocenteDocumento();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private Boolean soloLectura = Boolean.TRUE;
    private List<SgDocenteDocumento> listaDocumentos = new ArrayList();
    private SofisComboG<SgTipoDocumentoDocente> comboTipoDocumento;

    public DocenteDocumentoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_DATO_EMPLEADO)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.BUSCAR_DATO_EMPLEADO);
            JSFUtils.redirectToIndex();
        }
    }

    public void personalSedeEducativa(SgPersonalSedeEducativa var) {
        try {
            personalSede = var;
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            filtro.setDdoPersonal(personalSede.getPsePk());
            filtro.setOrderBy(new String[]{"ddoUltModFecha"});
            filtro.setAscending(new boolean[]{false});
            filtro.setIncluirCampos(new String[]{"ddoTipoDocumento.tddNombre", "ddoDescripcion", "ddoValidado",
                "ddoArchivo.achPk", "ddoArchivo.achNombre", "ddoArchivo.achContentType", "ddoArchivo.achExt", "ddoUltModFecha", "ddoVersion"});
            listaDocumentos = restClient.buscar(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {

            FiltroCodiguera fec = new FiltroCodiguera();
            fec.setHabilitado(Boolean.TRUE);
            fec.setAscending(new boolean[]{true});
            fec.setOrderBy(new String[]{"tddNombre"});
            List<SgTipoDocumentoDocente> listatipos = restCatalogo.buscarTipoDocumentoDocente(fec);
            comboTipoDocumento = new SofisComboG(listatipos, "tddNombre");
            comboTipoDocumento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoDocumento.setSelected(-1);
    }

    public String limpiar() {
        limpiarCombos();
        filtro = new FiltroDocenteDocumento();
        buscar();
        return null;
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgDocenteDocumento();
    }

    public void actualizar(SgDocenteDocumento var) {
        limpiarCombos();
        entidadEnEdicion = (SgDocenteDocumento) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void eliminarDocumento() {
        try {
            restClient.eliminar(entidadEnEdicion.getDdoPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean getSoloLecturaDocumento() {
        return this.soloLectura || (entidadEnEdicion.getDdoPk() != null && !sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DOCENTE_DOCUMENTO));
    }

    public void editarDocumento(SgDocenteDocumento var) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getDdoPk());
            comboTipoDocumento.setSelectedT(entidadEnEdicion.getDdoTipoDocumento());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarDocumento(SgDocenteDocumento doc) {
        try {
            if (BooleanUtils.isFalse(doc.getDdoValidado())) {
                restClient.invalidarRealizado(doc.getDdoPk());
            } else {
                restClient.validarRealizado(doc.getDdoPk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setDdoPersonal(personalSede);
            entidadEnEdicion.setDdoTipoDocumento(comboTipoDocumento.getSelectedT());
            restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('documentoDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgDocumento", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgDocumento", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void handlePartidaFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getDdoArchivo() == null) {
            entidadEnEdicion.setDdoArchivo(new SgArchivo());
        }
        handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getDdoArchivo());
    }

    public void eliminarArchivo() {
        this.entidadEnEdicion.setDdoArchivo(null);
    }

    public SgDocenteDocumento getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDocenteDocumento entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroDocenteDocumento getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDocenteDocumento filtro) {
        this.filtro = filtro;
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

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgPersonalSedeEducativa getPersonalSede() {
        return personalSede;
    }

    public void setPersonalSede(SgPersonalSedeEducativa personalSede) {
        this.personalSede = personalSede;
    }

    public List<SgDocenteDocumento> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<SgDocenteDocumento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public SofisComboG<SgTipoDocumentoDocente> getComboTipoDocumento() {
        return comboTipoDocumento;
    }

    public void setComboTipoDocumento(SofisComboG<SgTipoDocumentoDocente> comboTipoDocumento) {
        this.comboTipoDocumento = comboTipoDocumento;
    }

}
