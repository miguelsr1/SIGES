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
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgArchivoBitMatricula;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoImportado;
import sv.gob.mined.siges.web.enumerados.EnumMes;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ImportarBitMatriculaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ImportarBitMatriculaBean.class.getName());

    @Inject
    private MatriculaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    private SgArchivoBitMatricula entidadEnEdicion;
    private SgSede sedeSeleccionada;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";
    private SofisComboG<EnumMes> comboMesIngreso;
    private SofisComboG<EnumMes> comboMesEgreso;

    public ImportarBitMatriculaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            entidadEnEdicion = new SgArchivoBitMatricula();

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.TAMANIO_ARCHIVO_IMPORT);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tamanioImportArchivo = conf.get(0).getConValor();
            }
            fc.setCodigo(Constantes.TIPO_ARCHIVO_IMPORT);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tipoImportArchivo = conf.get(0).getConValor();
            }

            if (sessionBean.getSedeDefecto() != null) {
                sedeSeleccionada = sessionBean.getSedeDefecto();
            }

            cargarCombos();
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        comboMesIngreso = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
        comboMesIngreso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboMesEgreso = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
        comboMesEgreso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void validarAcceso() {
        //Control de seguridad
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_IMPORT_BIT_MATRICULA)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.MENU_IMPORT_BIT_MATRICULA);
            JSFUtils.redirectToIndex();
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
            return restSede.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
        sedeSeleccionada = null;
        comboMesIngreso.setSelected(-1);
        comboMesEgreso.setSelected(-1);
    }

    public void limpiar() {

    }

    public void agregar() {
        limpiarCombos();
    }

    public void eliminarArchivo() {
        entidadEnEdicion.setAbmArchivo(null);
    }

    public void seleccionarArchivo(FileUploadEvent event) {
        try {
            SgArchivo arc = new SgArchivo();
            handleArchivoBean.subirArchivoTmp(event.getFile(), arc);
            entidadEnEdicion.setAbmArchivo(arc);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarArchivo() {
        try {
            entidadEnEdicion.setAbmMesIngreso(comboMesIngreso.getSelectedT() != null ? comboMesIngreso.getSelectedT().getNumero() : null);
            entidadEnEdicion.setAbmMesEgreso(comboMesEgreso.getSelectedT() != null ? comboMesEgreso.getSelectedT().getNumero() : null);
            entidadEnEdicion.setAbmSede(sedeSeleccionada);
            entidadEnEdicion.setAbmEstado(EnumEstadoImportado.PROCESAMIENTO_DIRECTO);

            restClient.importar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

            entidadEnEdicion = new SgArchivoBitMatricula();
            limpiarCombos();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajesError(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgArchivoBitMatricula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgArchivoBitMatricula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
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

    public SofisComboG<EnumMes> getComboMesIngreso() {
        return comboMesIngreso;
    }

    public void setComboMesIngreso(SofisComboG<EnumMes> comboMesIngreso) {
        this.comboMesIngreso = comboMesIngreso;
    }

    public SofisComboG<EnumMes> getComboMesEgreso() {
        return comboMesEgreso;
    }

    public void setComboMesEgreso(SofisComboG<EnumMes> comboMesEgreso) {
        this.comboMesEgreso = comboMesEgreso;
    }

}
