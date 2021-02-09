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
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudImpresion;
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
public class GeneracionTituloReposicionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GeneracionTituloReposicionBean.class.getName());

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

    private SofisComboG<SgMotivoAnulacionTitulo> comboMotivoAnulacionTitulo;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<SgSelloFirmaTitular> comboSelloFirmaTitular;
    private List<SgTitulo> tituloList;

    public GeneracionTituloReposicionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();

            cargarCombos();
            if (solicitudImpresionId != null && solicitudImpresionId > 0) {
                entidadEnEdicion=restClient.obtenerPorId(solicitudImpresionId);              
                soloLectura = editable != null ? !editable : soloLectura;
                if (!entidadEnEdicion.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA)) {
                    soloLectura = Boolean.TRUE;
                }
                if (entidadEnEdicion.getSimDefinicionTitulo() != null) {
                    comboSelloFirmaTitular.setSelectedT(entidadEnEdicion.getSimSelloAutentica());
                } 
             

            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "No existe solicitud de impresión", "");
                soloLectura = Boolean.TRUE;
            }
            cargarCombosMotivoAnulacion();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_GENERACION_TITULO)) {
            JSFUtils.redirectToIndex();
        }
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
            comboMotivoAnulacionTitulo= new SofisComboG(motAnulacion, "matNombre");
            comboMotivoAnulacionTitulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    


    public void seleccionarAnulada() {
            comboMotivoAnulacionTitulo.setSelected(-1);

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

    public void guardar() {
        try {
            

            entidadEnEdicion.setSimSelloAutentica(comboSelloFirmaTitular.getSelectedT());
            entidadEnEdicion.getSimReposicionTitulo().setRetMotivoAnulacion(comboMotivoAnulacionTitulo.getSelectedT());
            entidadEnEdicion.setSimFechaEnviadoImprimir(LocalDate.now());
            if(BooleanUtils.isTrue(entidadEnEdicion.getSimReposicionTitulo().getRetAnulada())){
                entidadEnEdicion = restClient.rechazarSolicitud(entidadEnEdicion);
            }else{
                entidadEnEdicion = restClient.confirmarSolicitud(entidadEnEdicion);
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            soloLectura = Boolean.TRUE;

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void editarDatosEntrega() {
        try {

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void buscarDirectorMinistro() {
        try {
            if (entidadEnEdicion.getSimSeccion() != null) {
                FiltroSelloFirma fsf = new FiltroSelloFirma();
                fsf.setSfiHabilitado(Boolean.TRUE);
                fsf.setSfiTipoRepresentanteCodigo(Constantes.TIPO_REPRESENTANTE_DIRECTOR);
                fsf.setSedPk(entidadEnEdicion.getSimSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                List<SgSelloFirma> listSelloFirma = selloFirmaRestClient.buscar(fsf);
                if (listSelloFirma.size() > 0) {
                    SgSelloFirma selloFirma = listSelloFirma.get(0);
                    entidadEnEdicion.setSimSelloDirector(selloFirma);
                }

            }
            FiltroSelloFirmaTitular fsft = new FiltroSelloFirmaTitular();
            fsft.setSftHabilitado(Boolean.TRUE);
            fsft.setSftCodigoCargo(Constantes.CARGO_MINISTRO);
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

    public SofisComboG<SgMotivoAnulacionTitulo> getComboMotivoAnulacionTitulo() {
        return comboMotivoAnulacionTitulo;
    }

    public void setComboMotivoAnulacionTitulo(SofisComboG<SgMotivoAnulacionTitulo> comboMotivoAnulacionTitulo) {
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
