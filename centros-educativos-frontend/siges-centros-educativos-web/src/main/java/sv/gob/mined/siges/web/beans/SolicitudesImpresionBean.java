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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudImpresion;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudImpresionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudesImpresionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudesImpresionBean.class.getName());

    @Inject
    private SolicitudImpresionRestClient restClient;
    
    @Inject
    private SedeRestClient restSede;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    @Param(name = "estado")
    private EnumEstadoSolicitudImpresion estadoImp;

    private FiltroSolicitudImpresion filtro = new FiltroSolicitudImpresion();
    private SgSolicitudImpresion entidadEnEdicion = new SgSolicitudImpresion();
    private List<RevHistorico> historialSolicitudImpresion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySolicitudImpresionDataModel solicitudImpresionLazyModel;
    private SofisComboG<EnumEstadoSolicitudImpresion> comboEstado;
    private SgSede sedSeleccionado;

    public SolicitudesImpresionBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUDES_IMPRESIONES)) {
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
            "simSeccion.secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",
            "simSeccion.secServicioEducativo.sduOpcion.opcNombre",
            "simSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
            "simSeccion.secNombre",
            "simEstado",
            "simTipoImpresion",
            "simUsuario.usuNombre",
            "simFechaSolicitud",
            "simDefinicionTitulo.dtiNombreCertificado",
            "simDefinicionTitulo.dtiNombre"});
            filtro.setSimEstado(comboEstado.getSelectedT()!=null?comboEstado.getSelectedT():null);
           
            filtro.setSimSede(sedSeleccionado!=null?sedSeleccionado.getSedPk():null);
            List<EnumTipoSolicitudImpresion> listTipo=new ArrayList();
            listTipo.add(EnumTipoSolicitudImpresion.REI);
            listTipo.add(EnumTipoSolicitudImpresion.IMP);
            filtro.setSimTiposSolicitudImpresion(listTipo);
         //   filtro.setSimTipoSolicitudImpresion(EnumTipoSolicitudImpresion.IMP);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            solicitudImpresionLazyModel = new LazySolicitudImpresionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public String colorRow(SgSolicitudImpresion est) {

        if (est != null) {
            switch(est.getSimEstado()){
                case IMPRESA: return "aprobadoSolicitud";
                case CONFIRMADA: return "confirmado";
                case CON_EXCEPCIONES: return "con-excepciones";
                case RECHAZADA: return "reprobado";
                case ANULADA: return "reprobado";
                default:
                                
            }
            
        }
        return null;
    }

    public void cargarCombos() {
        List<EnumEstadoSolicitudImpresion> estados = new ArrayList(Arrays.asList(EnumEstadoSolicitudImpresion.values()));
        estados.remove(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
        comboEstado = new SofisComboG(estados, "text");
        comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboEstado.setSelectedT(EnumEstadoSolicitudImpresion.ENVIADA);
        if(estadoImp!=null){
            comboEstado.setSelectedT(estadoImp);
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
    
  public List<SgSede> completeSede(String query) {
        try {

            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedNombre","sedCodigo", "sedTipo", "sedVersion"});

            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
        comboEstado.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroSolicitudImpresion();
        sedSeleccionado=null;
        limpiarCombos();
        solicitudImpresionLazyModel = null;
        totalResultados = null;
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

}
