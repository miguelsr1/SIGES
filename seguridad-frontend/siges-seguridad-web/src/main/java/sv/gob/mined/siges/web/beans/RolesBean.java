/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCategoriaOperacion;
import sv.gob.mined.siges.web.dto.SgOperacion;
import sv.gob.mined.siges.web.dto.SgRol;
import sv.gob.mined.siges.web.dto.SgRolOperacion;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOperacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRol;
import sv.gob.mined.siges.web.lazymodels.LazyRolDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CategoriaOperacionRestClient;
import sv.gob.mined.siges.web.restclient.OperacionRestClient;
import sv.gob.mined.siges.web.restclient.RolRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

/**
 *
 * @author usuario
 */
@Named
@ViewScoped
public class RolesBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(RolBean.class.getName());

    @Inject
    private RolRestClient restClient;

    @Inject
    private CategoriaOperacionRestClient restCategoriaOperacion;

    @Inject
    private OperacionRestClient restOperacion;

    @Inject
    private SessionBean sessionBean;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroRol filtroRol = new FiltroRol();
    private SgRol entidadEnEdicion = new SgRol();
    private List<RevHistorico> historialRol = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyRolDataModel rolLazyModel;
    private SofisComboG<SgCategoriaOperacion> comboCategorias;

    private List<SgRolOperacion> operaciones = new ArrayList();
    private List<SgRolOperacion> operacionesSeleccionadas = new ArrayList();
    private List<SgRolOperacion> operacionesDeseleccionadas = new ArrayList();
    private String tituloPagina = Etiquetas.getValue("tituloNuevoRol");
    private SgOperacion operacionSeleccionado;

    public RolesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();           
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombos() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"copNombre"});
            List<SgCategoriaOperacion> listaCategorias = restCategoriaOperacion.buscar(filtro);
            comboCategorias = new SofisComboG<>(listaCategorias, "copNombre");
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public void limpiar() {
        comboCategorias.setSelected(-1);
        operacionSeleccionado = null;
        filtroRol = new FiltroRol();
        buscar();
        operaciones.clear();
    }

    public void cargarOperacionesDeCategoria() {
        try {
            operaciones.clear();
            List<SgRolOperacion> dumy = new ArrayList();
            if (entidadEnEdicion.getRolRolOperacion() != null) {
                dumy.addAll(entidadEnEdicion.getRolRolOperacion());
            }

            if (comboCategorias.getSelectedT() != null) {
                SgCategoriaOperacion catSelected = restCategoriaOperacion.obtenerPorId(comboCategorias.getSelectedT().getCopPk());
                for (SgOperacion ope : catSelected.getCopOperacion()) {
                    SgRolOperacion rolOperacion = new SgRolOperacion();
                    rolOperacion.setRopOperacion(ope);
                    ViewIdUtils.generateViewId(rolOperacion, dumy);
                    if (entidadEnEdicion.getRolRolOperacion() == null || !(existInList(entidadEnEdicion.getRolRolOperacion(), rolOperacion))) {
                        operaciones.add(rolOperacion);
                        dumy.add(rolOperacion);
                    }
                }

            }
            dumy.clear();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void buscar() {
        try {
            filtroRol.setOperacion(operacionSeleccionado != null ? operacionSeleccionado.getOpePk() : null);
            
            
            filtroRol.setFirst(new Long(0));
            filtroRol.setOrderBy(new String[]{"rolNombre"});
            filtroRol.setAscending(new boolean[]{true});
            totalResultados = restClient.buscarTotal(filtroRol);
            rolLazyModel = new LazyRolDataModel(restClient, filtroRol, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgRol();

    }

    public void actualizar(Long var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        try {
            entidadEnEdicion = restClient.obtenerPorId(var);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    private boolean existInList(List<SgRolOperacion> col, SgRolOperacion op) {
        for (SgRolOperacion o : col) {
            if (o.getRopOperacion().getOpePk().equals(op.getRopOperacion().getOpePk())) {
                return true;
            }
        }
        return false;
    }

    public void seleccionarOperaciones() {
        if (entidadEnEdicion.getRolRolOperacion() == null) {
            entidadEnEdicion.setRolRolOperacion(new ArrayList());
        }
        if (operacionesSeleccionadas != null) {
            for (SgRolOperacion rolOper : operacionesSeleccionadas) {
                if (!(existInList(entidadEnEdicion.getRolRolOperacion(), rolOper))) {
                    //rolOper.setRopRol(entidadEnEdicion);
                    rolOper.setRopHabilitado(Boolean.TRUE);
                    entidadEnEdicion.getRolRolOperacion().add(rolOper);
                }
                operaciones.remove(rolOper);

            }
            operacionesSeleccionadas.clear();
            cargarOperacionesDeCategoria();
        }

    }

    public void deseleccionarOperaciones() {
        if (operacionesDeseleccionadas != null) {
            for (SgRolOperacion ope : operacionesDeseleccionadas) {
                entidadEnEdicion.getRolRolOperacion().remove(ope);
            }
            operacionesDeseleccionadas.clear();
        }
    }

  

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getRolPk());
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
            historialRol = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgOperacion> completeOperacion(String query) {
        try {

            FiltroOperacion fop = new FiltroOperacion();
            fop.setNombre(query);
            fop.setHabilitado(Boolean.TRUE);
            fop.setOrderBy(new String[]{"opeNombre"});
            fop.setAscending(new boolean[]{true});
            return restOperacion.buscar(fop);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgRol getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgRol entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialRol() {
        return historialRol;
    }

    public void setHistorialRol(List<RevHistorico> historialRol) {
        this.historialRol = historialRol;
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

    public LazyRolDataModel getRolLazyModel() {
        return rolLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyRolDataModel rolLazyModel) {
        this.rolLazyModel = rolLazyModel;
    }

    public SofisComboG<SgCategoriaOperacion> getComboCategorias() {
        return comboCategorias;
    }

    public void setComboCategorias(SofisComboG<SgCategoriaOperacion> comboCategorias) {
        this.comboCategorias = comboCategorias;
    }

    public List<SgRolOperacion> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<SgRolOperacion> operaciones) {
        this.operaciones = operaciones;
    }

    public List<SgRolOperacion> getOperacionesSeleccionadas() {
        return operacionesSeleccionadas;
    }

    public void setOperacionesSeleccionadas(List<SgRolOperacion> operacionesSeleccionadas) {
        this.operacionesSeleccionadas = operacionesSeleccionadas;
    }

    public List<SgRolOperacion> getOperacionesDeseleccionadas() {
        return operacionesDeseleccionadas;
    }

    public void setOperacionesDeseleccionadas(List<SgRolOperacion> operacionesDeseleccionadas) {
        this.operacionesDeseleccionadas = operacionesDeseleccionadas;
    }

    public RolRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(RolRestClient restClient) {
        this.restClient = restClient;
    }

    public CategoriaOperacionRestClient getRestCategoriaOperacion() {
        return restCategoriaOperacion;
    }

    public void setRestCategoriaOperacion(CategoriaOperacionRestClient restCategoriaOperacion) {
        this.restCategoriaOperacion = restCategoriaOperacion;
    }

    public OperacionRestClient getRestOperacion() {
        return restOperacion;
    }

    public void setRestOperacion(OperacionRestClient restOperacion) {
        this.restOperacion = restOperacion;
    }

    public SgOperacion getOperacionSeleccionado() {
        return operacionSeleccionado;
    }

    public void setOperacionSeleccionado(SgOperacion operacionSeleccionado) {
        this.operacionSeleccionado = operacionSeleccionado;
    }


    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public FiltroRol getFiltroRol() {
        return filtroRol;
    }

    public void setFiltroRol(FiltroRol filtroRol) {
        this.filtroRol = filtroRol;
    }
}
