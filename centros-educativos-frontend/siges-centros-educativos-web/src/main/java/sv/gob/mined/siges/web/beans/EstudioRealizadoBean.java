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
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCapacitacion;
import sv.gob.mined.siges.web.dto.SgEstudioRealizado;
import sv.gob.mined.siges.web.dto.SgEstudioSuperior;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAlcanceCapacitacion;
import sv.gob.mined.siges.web.dto.catalogo.SgCarrera;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgIdioma;
import sv.gob.mined.siges.web.dto.SgIdiomaRealizado;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelIdioma;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgSistemaGestionContenido;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCapacitacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoEstudio;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDatoEmpleado;
import sv.gob.mined.siges.web.enumerados.EnumNivelSGC;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CapacitacionRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudioRealizadoRestClient;
import sv.gob.mined.siges.web.restclient.EstudioSuperiorRestClient;
import sv.gob.mined.siges.web.restclient.IdiomaRealizadoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudioRealizadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudioRealizadoBean.class.getName());

    @Inject
    private EstudioRealizadoRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient restSedes;

    @Inject
    private CapacitacionRestClient restCapacitaciones;

    @Inject
    private EstudioSuperiorRestClient restEstudioSuperior;

    @Inject
    private IdiomaRealizadoRestClient restIdioma;

    @Inject
    private SessionBean sessionBean;

    private SgEstudioRealizado entidadEnEdicion;
    private SgIdiomaRealizado entidadEnEdicionIdiomas = new SgIdiomaRealizado();
    private SgEstudioSuperior entidadEnEdicionEstudioSuperior = new SgEstudioSuperior();
    private SgCapacitacion entidadEnEdicionCapacitacion = new SgCapacitacion();
    private String formatoSeleccionado = "csv";
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<EnumEstadoDatoEmpleado> comboNivel;
    private SofisComboG<SgSistemaGestionContenido> comboSGC;
    private SofisComboG<SgIdioma> comboIdioma;
    private SofisComboG<SgNivelIdioma> comboNivelIdioma;
    private SofisComboG<SgEscolaridad> comboEscolaridad;
    private SofisComboG<SgMaximoNivelEducativoAlcanzado> comboMaximoEducativoAlcanzado;
    private List<SelectItem> nivelSGC;
    private SofisComboG<SgTipoEstudio> comboTipoEstudio;
    private SofisComboG<SgPais> comboPais;
    private SofisComboG<SgCarrera> comboCarrera;
    private SofisComboG<SgEspecialidad> comboEspecialidad;
    private SgSede sedeSelecionada;
    private SgSede sedeSelecionadaES;
    private SofisComboG<SgModalidadEstudio> comboModalidadEstudio;
    private SofisComboG<SgModalidadEstudio> comboModalidadEstudioES;
    private SofisComboG<SgTipoCapacitacion> comboTipoCapacitacion;
    private SofisComboG<SgAlcanceCapacitacion> comboAlcance;
    private SgPersonalSedeEducativa personalSede;
    private Boolean selectPais = false;
    private Boolean soloLectura = Boolean.FALSE;

    public EstudioRealizadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void personalSedeEducativa(SgPersonalSedeEducativa var) {
        try {
            personalSede = var;
            if (personalSede.getPseEstudioRealizado() != null) {
                entidadEnEdicion = restClient.obtenerPorId(personalSede.getPseEstudioRealizado().getErePk());
                comboSGC.setSelectedT(entidadEnEdicion.getEreSistemaGestionContenido());
                comboEscolaridad.setSelectedT(entidadEnEdicion.getEreEscolaridad());
                comboMaximoEducativoAlcanzado.setSelectedT(entidadEnEdicion.getEreMaximoNivelEducativoAlcanzado());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public boolean mostrarNivelCSG(){
        if(comboSGC.getSelectedT() == null){
            return false;
        }
        return true;
    }
    
    public String buscar() {
        try {
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {

            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});

            filtro.setIncluirCampos(new String[]{"sgcCodigo","sgcNombre", "sgcVersion"});
            filtro.setOrderBy(new String[]{"sgcNombre"});
            comboSGC = new SofisComboG(restCatalogo.buscarSGC(filtro), "sgcNombre");
            comboSGC.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            nivelSGC = new ArrayList(Arrays.asList(EnumNivelSGC.values()));

            filtro.setOrderBy(new String[]{"idiNombre"});
            filtro.setIncluirCampos(new String[]{"idiNombre", "idiVersion"});
            comboIdioma = new SofisComboG(restCatalogo.buscarIdioma(filtro), "idiNombre");
            comboIdioma.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"nidNombre"});
            filtro.setIncluirCampos(new String[]{"nidNombre", "nidVersion"});
            comboNivelIdioma = new SofisComboG(restCatalogo.buscarNivelIdioma(filtro), "nidNombre");
            comboNivelIdioma.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"escNombre"});
            filtro.setIncluirCampos(new String[]{"escNombre", "escVersion"});
            comboEscolaridad = new SofisComboG(restCatalogo.buscarEscolaridad(filtro), "escNombre");
            comboEscolaridad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"tesNombre"});
            filtro.setIncluirCampos(new String[]{"tesNombre", "tesVersion"});
            comboTipoEstudio = new SofisComboG(restCatalogo.buscarTipoEstudio(filtro), "tesNombre");
            comboTipoEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"paiNombre"});
            filtro.setIncluirCampos(new String[]{"paiNombre", "paiCodigo", "paiVersion"});
            comboPais = new SofisComboG(restCatalogo.buscarPais(filtro), "paiNombre");
            comboPais.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"crrNombre"});
            filtro.setIncluirCampos(new String[]{"crrNombre", "crrVersion"});
            comboCarrera = new SofisComboG(restCatalogo.buscarCarrera(filtro), "crrNombre");
            comboCarrera.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroEspecialidad fesp = new FiltroEspecialidad();
            fesp.setHabilitado(Boolean.TRUE);
            fesp.setAscending(new boolean[]{true});
            fesp.setOrderBy(new String[]{"espNombre"});
            fesp.setIncluirCampos(new String[]{"espNombre", "espVersion"});
            comboEspecialidad = new SofisComboG(restCatalogo.buscarEspecialidad(fesp), "espNombre");
            comboEspecialidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"mesNombre"});
            filtro.setIncluirCampos(new String[]{"mesNombre", "mesVersion"});
            List<SgModalidadEstudio> listaModalidad = restCatalogo.buscarModalidadEstudio(filtro);
            comboModalidadEstudio = new SofisComboG(listaModalidad, "mesNombre");
            comboModalidadEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboModalidadEstudioES = new SofisComboG(listaModalidad, "mesNombre");
            comboModalidadEstudioES.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"tcaNombre"});
            filtro.setIncluirCampos(new String[]{"tcaNombre", "tcaVersion"});
            comboTipoCapacitacion = new SofisComboG(restCatalogo.buscarTipoCapacitacion(filtro), "tcaNombre");
            comboTipoCapacitacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"acaNombre"});
            filtro.setIncluirCampos(new String[]{"acaNombre", "acaVersion"});
            comboAlcance = new SofisComboG(restCatalogo.buscarAlcanceCapacitacion(filtro), "acaNombre");
            comboAlcance.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"mneNombre"});
            filtro.setIncluirCampos(new String[]{"mneNombre", "mneVersion"});
            comboMaximoEducativoAlcanzado = new SofisComboG(restCatalogo.buscarMaximoNivelEducativoAlcanzado(filtro), "mneNombre");
            comboMaximoEducativoAlcanzado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void seleccionarPais() {
        if (comboPais.getSelectedT() != null) {
            if (comboPais.getSelectedT().getPaiCodigo().equals(Constantes.CODIGO_PAIS_EL_SALVADOR)) {
                selectPais = true;
            } else {
                selectPais = false;
            }
        } else {
            selectPais = false;
        }
    }

    private void limpiarComboIdioma() {
        comboIdioma.setSelected(-1);
        comboNivelIdioma.setSelected(-1);
    }

    private void limpiarComboEstudioSuperior() {
        comboTipoEstudio.setSelected(-1);
        comboPais.setSelected(-1);
        comboCarrera.setSelected(-1);
        comboEspecialidad.setSelected(-1);
        sedeSelecionadaES = null;
        comboModalidadEstudioES.setSelected(-1);
    }

    private void limpiarComboCapacitacion() {
        comboTipoCapacitacion.setSelected(-1);
        comboAlcance.setSelected(-1);
        sedeSelecionada = null;
        comboModalidadEstudio.setSelected(-1);
    }

    public String limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
        return null;
    }

    public void agregar() {
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgEstudioRealizado var) {
        limpiarCombos();
        entidadEnEdicion = (SgEstudioRealizado) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void guardar() {
        try {
            entidadEnEdicion.setEreSistemaGestionContenido(comboSGC.getSelectedT());
            entidadEnEdicion.setEreEscolaridad(comboEscolaridad.getSelectedT());
            entidadEnEdicion.setEreMaximoNivelEducativoAlcanzado(comboMaximoEducativoAlcanzado.getSelectedT());
            entidadEnEdicion.setErePersonalSede(new SgPersonalSedeEducativa(personalSede.getPsePk(), personalSede.getPseVersion()));
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            LOGGER.info(be.getMessage());
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getErePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            sedeSelecionada = null;
            sedeSelecionadaES = null;
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            return restSedes.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void agregarIdioma() {
        entidadEnEdicionIdiomas = new SgIdiomaRealizado();
        limpiarComboIdioma();
    }

    public void editarIdioma(SgIdiomaRealizado var) {
        try {
            entidadEnEdicionIdiomas = restIdioma.obtenerPorId(var.getIrePk());
            comboIdioma.setSelectedT(entidadEnEdicionIdiomas.getIreIdioma());
            comboNivelIdioma.setSelectedT(entidadEnEdicionIdiomas.getIreNivelIdioma());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarIdioma(SgIdiomaRealizado ire) {
        try {
            if (BooleanUtils.isFalse(ire.getIreValidado())) {
                restIdioma.invalidarRealizado(ire.getIrePk());
            } else {
                restIdioma.validarRealizado(ire.getIrePk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarIdioma(SgIdiomaRealizado var) {
        limpiarCombos();
        entidadEnEdicionIdiomas = (SgIdiomaRealizado) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminarIdioma() {
        try {
            restIdioma.eliminar(entidadEnEdicionIdiomas.getIrePk());
            this.entidadEnEdicion.getEreIdiomas().remove(entidadEnEdicionIdiomas);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarIdiomaEstudioRealizado() {
        try {
            entidadEnEdicionIdiomas.setIreIdioma(comboIdioma.getSelectedT());
            entidadEnEdicionIdiomas.setIreNivelIdioma(comboNivelIdioma.getSelectedT());
            entidadEnEdicionIdiomas.setIreEstudiosRealizados(new SgEstudioRealizado(entidadEnEdicion.getErePk(), entidadEnEdicion.getEreVersion()));

            restIdioma.validar(entidadEnEdicionIdiomas);
            if (entidadEnEdicion.getEreIdiomas() == null) {
                entidadEnEdicion.setEreIdiomas(new ArrayList<>());
            }
            entidadEnEdicionIdiomas = restIdioma.guardar(entidadEnEdicionIdiomas);
            entidadEnEdicionIdiomas.setIreEstudiosRealizados(new SgEstudioRealizado(entidadEnEdicion.getErePk(), entidadEnEdicion.getEreVersion()));

            if (entidadEnEdicion.getEreIdiomas().contains(entidadEnEdicionIdiomas)) {
                entidadEnEdicion.getEreIdiomas().set(entidadEnEdicion.getEreIdiomas().indexOf(entidadEnEdicionIdiomas), entidadEnEdicionIdiomas);
            } else {
                entidadEnEdicion.getEreIdiomas().add(entidadEnEdicionIdiomas);
            }
            PrimeFaces.current().executeScript("PF('idiomaDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgIdioma", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarEstudioSuperior() {
        entidadEnEdicionEstudioSuperior = new SgEstudioSuperior();
        limpiarComboEstudioSuperior();
    }

    public void verEstudioSuperior(SgEstudioSuperior var) {
        try {
            entidadEnEdicionEstudioSuperior = restEstudioSuperior.obtenerPorId(var.getEsuPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void editarEstudioSuperior(SgEstudioSuperior var) {
        try {
            entidadEnEdicionEstudioSuperior = restEstudioSuperior.obtenerPorId(var.getEsuPk());
            comboTipoEstudio.setSelectedT(entidadEnEdicionEstudioSuperior.getEsuTipo());
            comboPais.setSelectedT(entidadEnEdicionEstudioSuperior.getEsuPais());
            comboCarrera.setSelectedT(entidadEnEdicionEstudioSuperior.getEsuCarrera());
            comboEspecialidad.setSelectedT(entidadEnEdicionEstudioSuperior.getEsuEspecialidad());
            sedeSelecionadaES = entidadEnEdicionEstudioSuperior.getEsuInstitucionEstudio();
            comboModalidadEstudioES.setSelectedT(entidadEnEdicionEstudioSuperior.getEsuModalidad());
            seleccionarPais();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarEstudioSuperior(SgEstudioSuperior est) {
        try {
            if (BooleanUtils.isFalse(est.getEsuValidado())) {
                restEstudioSuperior.invalidarRealizado(est.getEsuPk());
            } else {
                restEstudioSuperior.validarRealizado(est.getEsuPk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarEstudioSuperior(SgEstudioSuperior var) {
        limpiarCombos();
        entidadEnEdicionEstudioSuperior = (SgEstudioSuperior) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminarEstudioSuperior() {
        try {
            restEstudioSuperior.eliminar(entidadEnEdicionEstudioSuperior.getEsuPk());
            this.entidadEnEdicion.getEreEstudiosSuperiores().remove(entidadEnEdicionEstudioSuperior);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarEstudioSuperiorEstudioRealizado() {
        try {
            entidadEnEdicionEstudioSuperior.setEsuTipo(comboTipoEstudio.getSelectedT());
            entidadEnEdicionEstudioSuperior.setEsuPais(comboPais.getSelectedT());
            entidadEnEdicionEstudioSuperior.setEsuCarrera(comboCarrera.getSelectedT());
            entidadEnEdicionEstudioSuperior.setEsuEspecialidad(comboEspecialidad.getSelectedT());
            entidadEnEdicionEstudioSuperior.setEsuInstitucionEstudio(sedeSelecionadaES);
            entidadEnEdicionEstudioSuperior.setEsuModalidad(comboModalidadEstudioES.getSelectedT());
            entidadEnEdicionEstudioSuperior.setEsuEstudios(new SgEstudioRealizado(entidadEnEdicion.getErePk(), entidadEnEdicion.getEreVersion()));

            if (comboPais.getSelectedT() != null) {
                if (comboPais.getSelectedT().getPaiCodigo().equals(Constantes.CODIGO_PAIS_EL_SALVADOR)) {
                    entidadEnEdicionEstudioSuperior.setEsuCarreraTxt(null);
                    entidadEnEdicionEstudioSuperior.setEsuInstitucionEstudioTxt(null);
                } else {
                    entidadEnEdicionEstudioSuperior.setEsuCarrera(null);
                    entidadEnEdicionEstudioSuperior.setEsuInstitucionEstudio(null);
                }
            }

            if (entidadEnEdicion.getEreEstudiosSuperiores() == null) {
                this.entidadEnEdicion.setEreEstudiosSuperiores(new ArrayList<>());
            }
            entidadEnEdicionEstudioSuperior = restEstudioSuperior.guardar(entidadEnEdicionEstudioSuperior);
            entidadEnEdicionEstudioSuperior.setEsuEstudios(new SgEstudioRealizado(entidadEnEdicion.getErePk(), entidadEnEdicion.getEreVersion()));

            if (entidadEnEdicion.getEreEstudiosSuperiores().contains(entidadEnEdicionEstudioSuperior)) {
                entidadEnEdicion.getEreEstudiosSuperiores().set(entidadEnEdicion.getEreEstudiosSuperiores().indexOf(entidadEnEdicionEstudioSuperior), entidadEnEdicionEstudioSuperior);
            } else {
                entidadEnEdicion.getEreEstudiosSuperiores().add(entidadEnEdicionEstudioSuperior);
            }
            PrimeFaces.current().executeScript("PF('estudioSuperiorDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgEstudioSuperior", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgEstudioSuperior", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarCapaciacion() {
        entidadEnEdicionCapacitacion = new SgCapacitacion();
        entidadEnEdicionCapacitacion.setCapCursoAcreditado(false);
        limpiarComboCapacitacion();
    }

    public void verCapacitacion(SgCapacitacion var) {
        try {
            entidadEnEdicionCapacitacion = restCapacitaciones.obtenerPorId(var.getCapPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void editarCapacitacion(SgCapacitacion var) {
        try {
            entidadEnEdicionCapacitacion = restCapacitaciones.obtenerPorId(var.getCapPk());
            comboTipoCapacitacion.setSelectedT(entidadEnEdicionCapacitacion.getCapTipoCapacitacion());
            comboAlcance.setSelectedT(entidadEnEdicionCapacitacion.getCapAlcanceCapacitacion());
            comboModalidadEstudio.setSelectedT(entidadEnEdicionCapacitacion.getCapModalidad());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarCapacitacion(SgCapacitacion var) {
        limpiarCombos();
        entidadEnEdicionCapacitacion = (SgCapacitacion) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminarCapacitacion() {
        try {
            restCapacitaciones.eliminar(entidadEnEdicionCapacitacion.getCapPk());
            this.entidadEnEdicion.getEreCapacitaciones().remove(entidadEnEdicionCapacitacion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarCapacitacion(SgCapacitacion cap) {
        try {
            if (BooleanUtils.isFalse(cap.getCapValidado())) {
                restCapacitaciones.invalidarRealizado(cap.getCapPk());
            } else {
                restCapacitaciones.validarRealizado(cap.getCapPk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarCapacitacionEstudioRealizado() {
        try {
            entidadEnEdicionCapacitacion.setCapTipoCapacitacion(comboTipoCapacitacion.getSelectedT());
            entidadEnEdicionCapacitacion.setCapAlcanceCapacitacion(comboAlcance.getSelectedT());
            entidadEnEdicionCapacitacion.setCapModalidad(comboModalidadEstudio.getSelectedT());
            entidadEnEdicionCapacitacion.setCapEstudios(new SgEstudioRealizado(entidadEnEdicion.getErePk(), entidadEnEdicion.getEreVersion()));

            entidadEnEdicionCapacitacion = restCapacitaciones.guardar(entidadEnEdicionCapacitacion);
            entidadEnEdicionCapacitacion.setCapEstudios(new SgEstudioRealizado(entidadEnEdicion.getErePk(), entidadEnEdicion.getEreVersion()));

            if (entidadEnEdicion.getEreCapacitaciones() == null) {
                this.entidadEnEdicion.setEreCapacitaciones(new ArrayList<>());
            }
            if (entidadEnEdicion.getEreCapacitaciones().contains(entidadEnEdicionCapacitacion)) {
                entidadEnEdicion.getEreCapacitaciones().set(entidadEnEdicion.getEreCapacitaciones().indexOf(entidadEnEdicionCapacitacion), entidadEnEdicionCapacitacion);
            } else {
                entidadEnEdicion.getEreCapacitaciones().add(entidadEnEdicionCapacitacion);
            }
            PrimeFaces.current().executeScript("PF('capacitacionDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgCapacitacion", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgCapacitacion", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
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

    public SgEstudioRealizado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstudioRealizado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SgIdiomaRealizado getEntidadEnEdicionIdiomas() {
        return entidadEnEdicionIdiomas;
    }

    public void setEntidadEnEdicionIdiomas(SgIdiomaRealizado entidadEnEdicionIdiomas) {
        this.entidadEnEdicionIdiomas = entidadEnEdicionIdiomas;
    }

    public SofisComboG<EnumEstadoDatoEmpleado> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<EnumEstadoDatoEmpleado> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgSistemaGestionContenido> getComboSGC() {
        return comboSGC;
    }

    public void setComboSGC(SofisComboG<SgSistemaGestionContenido> comboSGC) {
        this.comboSGC = comboSGC;
    }

    public List<SelectItem> getNivelSGC() {
        return nivelSGC;
    }

    public void setNivelSGC(List<SelectItem> nivelSGC) {
        this.nivelSGC = nivelSGC;
    }

    public SofisComboG<SgIdioma> getComboIdioma() {
        return comboIdioma;
    }

    public void setComboIdioma(SofisComboG<SgIdioma> comboIdioma) {
        this.comboIdioma = comboIdioma;
    }

    public SofisComboG<SgNivelIdioma> getComboNivelIdioma() {
        return comboNivelIdioma;
    }

    public void setComboNivelIdioma(SofisComboG<SgNivelIdioma> comboNivelIdioma) {
        this.comboNivelIdioma = comboNivelIdioma;
    }

    public EstudioRealizadoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(EstudioRealizadoRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<SgEscolaridad> getComboEscolaridad() {
        return comboEscolaridad;
    }

    public void setComboEscolaridad(SofisComboG<SgEscolaridad> comboEscolaridad) {
        this.comboEscolaridad = comboEscolaridad;
    }

    public SgEstudioSuperior getEntidadEnEdicionEstudioSuperior() {
        return entidadEnEdicionEstudioSuperior;
    }

    public void setEntidadEnEdicionEstudioSuperior(SgEstudioSuperior entidadEnEdicionEstudioSuperior) {
        this.entidadEnEdicionEstudioSuperior = entidadEnEdicionEstudioSuperior;
    }

    public SofisComboG<SgTipoEstudio> getComboTipoEstudio() {
        return comboTipoEstudio;
    }

    public void setComboTipoEstudio(SofisComboG<SgTipoEstudio> comboTipoEstudio) {
        this.comboTipoEstudio = comboTipoEstudio;
    }

    public SofisComboG<SgPais> getComboPais() {
        return comboPais;
    }

    public void setComboPais(SofisComboG<SgPais> comboPais) {
        this.comboPais = comboPais;
    }

    public SofisComboG<SgCarrera> getComboCarrera() {
        return comboCarrera;
    }

    public void setComboCarrera(SofisComboG<SgCarrera> comboCarrera) {
        this.comboCarrera = comboCarrera;
    }

    public SofisComboG<SgEspecialidad> getComboEspecialidad() {
        return comboEspecialidad;
    }

    public void setComboEspecialidad(SofisComboG<SgEspecialidad> comboEspecialidad) {
        this.comboEspecialidad = comboEspecialidad;
    }

    public SgSede getSedeSelecionada() {
        return sedeSelecionada;
    }

    public void setSedeSelecionada(SgSede sedeSelecionada) {
        this.sedeSelecionada = sedeSelecionada;
    }

    public SofisComboG<SgModalidadEstudio> getComboModalidadEstudio() {
        return comboModalidadEstudio;
    }

    public void setComboModalidadEstudio(SofisComboG<SgModalidadEstudio> comboModalidadEstudio) {
        this.comboModalidadEstudio = comboModalidadEstudio;
    }

    public SedeRestClient getRestSedes() {
        return restSedes;
    }

    public void setRestSedes(SedeRestClient restSedes) {
        this.restSedes = restSedes;
    }

    public SgCapacitacion getEntidadEnEdicionCapacitacion() {
        return entidadEnEdicionCapacitacion;
    }

    public void setEntidadEnEdicionCapacitacion(SgCapacitacion entidadEnEdicionCapacitacion) {
        this.entidadEnEdicionCapacitacion = entidadEnEdicionCapacitacion;
    }

    public SofisComboG<SgTipoCapacitacion> getComboTipoCapacitacion() {
        return comboTipoCapacitacion;
    }

    public void setComboTipoCapacitacion(SofisComboG<SgTipoCapacitacion> comboTipoCapacitacion) {
        this.comboTipoCapacitacion = comboTipoCapacitacion;
    }

    public SofisComboG<SgAlcanceCapacitacion> getComboAlcance() {
        return comboAlcance;
    }

    public void setComboAlcance(SofisComboG<SgAlcanceCapacitacion> comboAlcance) {
        this.comboAlcance = comboAlcance;
    }

    public CapacitacionRestClient getRestCapacitaciones() {
        return restCapacitaciones;
    }

    public void setRestCapacitaciones(CapacitacionRestClient restCapacitaciones) {
        this.restCapacitaciones = restCapacitaciones;
    }

    public EstudioSuperiorRestClient getRestEstudioSuperior() {
        return restEstudioSuperior;
    }

    public void setRestEstudioSuperior(EstudioSuperiorRestClient restEstudioSuperior) {
        this.restEstudioSuperior = restEstudioSuperior;
    }

    public IdiomaRealizadoRestClient getRestIdioma() {
        return restIdioma;
    }

    public void setRestIdioma(IdiomaRealizadoRestClient restIdioma) {
        this.restIdioma = restIdioma;
    }

    public SgSede getSedeSelecionadaES() {
        return sedeSelecionadaES;
    }

    public void setSedeSelecionadaES(SgSede sedeSelecionadaES) {
        this.sedeSelecionadaES = sedeSelecionadaES;
    }

    public SofisComboG<SgModalidadEstudio> getComboModalidadEstudioES() {
        return comboModalidadEstudioES;
    }

    public void setComboModalidadEstudioES(SofisComboG<SgModalidadEstudio> comboModalidadEstudioES) {
        this.comboModalidadEstudioES = comboModalidadEstudioES;
    }

    public SgPersonalSedeEducativa getPersonalSede() {
        return personalSede;
    }

    public void setPersonalSede(SgPersonalSedeEducativa personalSede) {
        this.personalSede = personalSede;
    }

    public Boolean getSelectPais() {
        return selectPais;
    }

    public void setSelectPais(Boolean selectPais) {
        this.selectPais = selectPais;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgMaximoNivelEducativoAlcanzado> getComboMaximoEducativoAlcanzado() {
        return comboMaximoEducativoAlcanzado;
    }

    public void setComboMaximoEducativoAlcanzado(SofisComboG<SgMaximoNivelEducativoAlcanzado> comboMaximoEducativoAlcanzado) {
        this.comboMaximoEducativoAlcanzado = comboMaximoEducativoAlcanzado;
    }

}
