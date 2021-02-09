/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgDatoContratacion;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoRepresentante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDatoContratacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTipoRepresentante;
import sv.gob.mined.siges.web.lazymodels.LazySelloFirmaDataModel;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SelloFirmaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SelloFirmaBean.class.getName());

    @Inject
    private SelloFirmaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private PersonaRestClient personaRestClient;

    @Inject
    private PersonalSedeEducativaRestClient personalClient;

    @Inject
    private DatoContratacionRestClient datoContratacionClient;

    @Inject
    @Param(name = "id")
    private Long selloFirmaId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private SessionBean sessionBean;

    private SgSelloFirma entidadEnEdicion = new SgSelloFirma();
    private List<SgSelloFirma> historialSelloFirma = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySelloFirmaDataModel selloFirmaLazyModel;
    private SofisComboG<SgTipoRepresentante> comboTipoRepresentante;
    private List<SgSede> listSede;
    private SgSede sedSeleccionado;
    private Boolean soloLectura = Boolean.FALSE;
    private String perDui;
    private SgPersona personaPersonal;
    private SgConfiguracion configuracionArchivoAlto;
    private SgConfiguracion configuracionArchivoAncho;
    private SgConfiguracion configuracionArchivoTipo;

    public SelloFirmaBean() {

    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (selloFirmaId != null && selloFirmaId > 0) {
                this.actualizar(restClient.obtenerPorId(selloFirmaId));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                this.agregar();
            }
            cargarConfiguracionArchivo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SELLO_FIRMA)) {
            JSFUtils.redirectToIndex();
        }

    }

    public String getTituloPagina() {
        if (this.selloFirmaId == null) {
            return Etiquetas.getValue("nuevoSelloFirma");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verSelloFirma");
        } else {
            return Etiquetas.getValue("edicionSelloFirma");
        }
    }

    public void cargarCombos() {

    }

    public void cargarConfiguracionArchivo() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();

            fc.setCodigoExacto(Constantes.ALTO_ARCHIVO_SELLO_FIRMA_IMPORT);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            configuracionArchivoAlto = !conf.isEmpty() ? conf.get(0) : null;

            fc.setCodigoExacto(Constantes.ANCHO_ARCHIVO_SELLO_FIRMA_IMPORT);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            configuracionArchivoAncho = !conf.isEmpty() ? conf.get(0) : null;

            fc.setCodigoExacto(Constantes.TIPO_ARCHIVO_SELLO_FIRMA_IMPORT);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            configuracionArchivoTipo = !conf.isEmpty() ? conf.get(0) : null;

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        if (comboTipoRepresentante != null){
            comboTipoRepresentante.setSelected(-1);
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSecurityOperation(ConstantesOperaciones.CREAR_SELLO_FIRMA);
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarSede() {
        try {

            entidadEnEdicion.setSfiSede(sedSeleccionado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarPersonaDui() {
        try {
            personaPersonal = null;
            if (perDui != null && !perDui.isEmpty() && sedSeleccionado != null) {

                FiltroPersonalSedeEducativa filp = new FiltroPersonalSedeEducativa();
                filp.setPerDui(perDui);
                filp.setPersonalActivo(Boolean.TRUE);
                filp.setPerCentroEducativo(sedSeleccionado.getSedPk());
                filp.setIncluirCampos(new String[]{"psePk", "psePersona.perDui", "psePersona.perNip", "psePersona.perUsuarioPk",
                    "psePersona.perPk", "psePersona.perPrimerNombre", "psePersona.perVersion",
                    "psePersona.perPrimerApellido", "psePersona.perSegundoNombre", "psePersona.perSegundoApellido"});

                List<SgPersonalSedeEducativa> pers = personalClient.buscar(filp);

                if (pers != null && pers.size() > 0) {
                    SgPersonalSedeEducativa personal = pers.get(0);
                    personaPersonal = personal.getPsePersona();

                    FiltroDatoContratacion filtro = new FiltroDatoContratacion();
                    filtro.setDcoPersonalPk(personal.getPsePk());
                    filtro.setContratosActivos(Boolean.TRUE);
                    filtro.setDcoCentroEducativo(sedSeleccionado.getSedPk());
                    filtro.setIncluirCampos(new String[]{"dcoCargo.carPk"});
                    List<SgDatoContratacion> datos = datoContratacionClient.buscar(filtro);
                   
                    List<Long> cargosHabilitados = datos.stream().map(d -> d.getDcoCargo().getCarPk()).collect(Collectors.toList());
                    List<SgTipoRepresentante> listTipoRep = null;

                    if (cargosHabilitados != null && !cargosHabilitados.isEmpty()) {
                        FiltroTipoRepresentante fc = new FiltroTipoRepresentante();
                        fc.setCargosPk(cargosHabilitados);
                        listTipoRep = catalogoRestClient.buscarTipoRepresentante(fc);
                        comboTipoRepresentante = new SofisComboG(listTipoRep, "treNombre");
                        comboTipoRepresentante.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    } else {
                        comboTipoRepresentante = new SofisComboG();
                        comboTipoRepresentante.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }

                    if (listTipoRep == null) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CARGO_DE_PERSONAL_NO_TIENE_TIPOS_DE_REPRESENTANTE_HABILITADOS), "");
                    }

                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRO_PERSONAL_CON_CONTRATO_ACTIVO_EN_SEDE), "");
                }
            }else{
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_DUI_VACIO), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void handleFileUploadFirma(FileUploadEvent event) {
        try {
            UploadedFile uploadedFile = event.getFile();
            byte[] bytes = IOUtils.toByteArray(uploadedFile.getInputstream());
            if (validateImageDimensions(bytes)) {
                if (entidadEnEdicion.getSfiFirmaSello() == null) {
                    entidadEnEdicion.setSfiFirmaSello(new SgArchivo());
                }
                FileUploadUtils.handleFileUpload(event.getFile(), entidadEnEdicion.getSfiFirmaSello(), tmpBasePath);
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE,
                        Mensajes.obtenerMensaje(Mensajes.ERROR_DIMENSIONES_IMAGENES) + " Alto:" + configuracionArchivoAlto.getConValor() + "px x Ancho:" + configuracionArchivoAncho.getConValor() + "px", "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public boolean validateImageDimensions(byte[] bytes) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            return bufferedImage.getHeight() == Integer.valueOf(configuracionArchivoAlto.getConValor()) && bufferedImage.getWidth() == Integer.valueOf(configuracionArchivoAncho.getConValor());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return false;
    }

    public void eliminarFotoFirma() {
        this.entidadEnEdicion.setSfiFirmaSello(null);
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgSelloFirma();
    }

    public void actualizar(SgSelloFirma selloFirma) {
        try {
            entidadEnEdicion = selloFirma;
            perDui = selloFirma.getSfiPersona() != null ? selloFirma.getSfiPersona().getPerDui() : null;
            personaPersonal = selloFirma.getSfiPersona() != null ? selloFirma.getSfiPersona() : null;
            sedSeleccionado = entidadEnEdicion.getSfiSede();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getSfiPk() == null){
                entidadEnEdicion.setSfiSede(sedSeleccionado);
                entidadEnEdicion.setSfiTipoRepresentante(comboTipoRepresentante.getSelectedT());
                entidadEnEdicion.setSfiPersona(personaPersonal);
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSfiPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public SgSelloFirma getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSelloFirma entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgSelloFirma> getHistorialSelloFirma() {
        return historialSelloFirma;
    }

    public void setHistorialSelloFirma(List<SgSelloFirma> historialSelloFirma) {
        this.historialSelloFirma = historialSelloFirma;
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

    public LazySelloFirmaDataModel getSelloFirmaLazyModel() {
        return selloFirmaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazySelloFirmaDataModel selloFirmaLazyModel) {
        this.selloFirmaLazyModel = selloFirmaLazyModel;
    }

    public SofisComboG<SgTipoRepresentante> getComboTipoRepresentante() {
        return comboTipoRepresentante;
    }

    public void setComboTipoRepresentante(SofisComboG<SgTipoRepresentante> comboTipoRepresentante) {
        this.comboTipoRepresentante = comboTipoRepresentante;
    }

    public List<SgSede> getListSede() {
        return listSede;
    }

    public void setListSede(List<SgSede> listSede) {
        this.listSede = listSede;
    }

    public SgSede getSedSeleccionado() {
        return sedSeleccionado;
    }

    public void setSedSeleccionado(SgSede sedSeleccionado) {
        this.sedSeleccionado = sedSeleccionado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        this.perDui = perDui;
    }

    public SgPersona getPersonaPersonal() {
        return personaPersonal;
    }

    public void setPersonaPersonal(SgPersona personaPersonal) {
        this.personaPersonal = personaPersonal;
    }

    //</editor-fold>
    public SgConfiguracion getConfiguracionArchivoAlto() {
        return configuracionArchivoAlto;
    }

    public void setConfiguracionArchivoAlto(SgConfiguracion configuracionArchivoAlto) {
        this.configuracionArchivoAlto = configuracionArchivoAlto;
    }

    public SgConfiguracion getConfiguracionArchivoAncho() {
        return configuracionArchivoAncho;
    }

    public void setConfiguracionArchivoAncho(SgConfiguracion configuracionArchivoAncho) {
        this.configuracionArchivoAncho = configuracionArchivoAncho;
    }

    public SgConfiguracion getConfiguracionArchivoTipo() {
        return configuracionArchivoTipo;
    }

    public void setConfiguracionArchivoTipo(SgConfiguracion configuracionArchivoTipo) {
        this.configuracionArchivoTipo = configuracionArchivoTipo;
    }

}
