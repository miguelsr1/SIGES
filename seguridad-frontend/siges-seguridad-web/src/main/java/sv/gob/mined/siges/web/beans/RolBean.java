/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCategoriaOperacion;
import sv.gob.mined.siges.web.dto.SgRol;
import sv.gob.mined.siges.web.dto.SgOperacion;
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

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RolBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RolBean.class.getName());

    @Inject
    private RolRestClient restClient;

    @Inject
    private CategoriaOperacionRestClient restCategoriaOperacion;

    @Inject
    private OperacionRestClient restOperacion;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id")
    private Long idActualizar;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroRol filtroRol = new FiltroRol();
    private SgRol entidadEnEdicion = new SgRol();
    private List<RevHistorico> historialRol = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyRolDataModel rolLazyModel;
    private SofisComboG<SgCategoriaOperacion> comboCategorias;
    private Boolean verOperacionesAsignadas = Boolean.FALSE;

    private List<SgRolOperacion> rolOperaciones = new ArrayList();
    private List<SgRolOperacion> rolOperacioensSeleccionados = new ArrayList();
    private List<SgRolOperacion> rolOperacioensAntesDeCambio = new ArrayList();

    private String tituloPagina = Etiquetas.getValue("tituloNuevoRol");

    public RolBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (idActualizar != null && idActualizar > 0) {
                actualizar(idActualizar);
                tituloPagina = Etiquetas.getValue("tituloEditarRol");
            }

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

    public String operacionActiva(SgRolOperacion op) {
        if (rolOperacioensSeleccionados.contains(op)) {
            return "Sí";
        } else {
            return "No";
        }
    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public void limpiar() {
        comboCategorias.setSelected(-1);
        filtroRol = new FiltroRol();

    }

    public void cargarOperacionesDeCategoria() {
        try {
            List<SgOperacion> operaciones = new ArrayList();
            List<SgOperacion> listAuxiliar = new ArrayList();

            FiltroOperacion filtroOperacion = new FiltroOperacion();
            filtroOperacion.setRol(verOperacionesAsignadas ? entidadEnEdicion.getRolPk() : null);
            filtroOperacion.setCategoria(comboCategorias.getSelectedT() != null ? comboCategorias.getSelectedT().getCopPk() : null);
            operaciones = restOperacion.buscar(filtroOperacion);

            rolOperaciones = new ArrayList();
            rolOperacioensSeleccionados = new ArrayList();

            if (entidadEnEdicion.getRolRolOperacion() != null) {

                for (SgRolOperacion rolOp : entidadEnEdicion.getRolRolOperacion()) {
                    if (operaciones.contains(rolOp.getRopOperacion())) {
                        rolOperacioensSeleccionados.add(rolOp);
                        rolOperaciones.add(rolOp);
                    }
                }
                listAuxiliar = rolOperacioensSeleccionados.stream().map(m -> m.getRopOperacion()).collect(Collectors.toList());
            }

            for (SgOperacion op : operaciones) {
                SgRolOperacion rop = new SgRolOperacion();
                if (!listAuxiliar.contains(op)) {
                    rop.setRopOperacion(op);
                    rop.setRopCascada(Boolean.FALSE);
                    rolOperaciones.add(rop);
                }

            }
            Collections.sort(rolOperaciones, (o1, o2) -> o1.getRopOperacion().getOpeNombre().compareTo(o2.getRopOperacion().getOpeNombre()));

            rolOperacioensAntesDeCambio = rolOperacioensSeleccionados;

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
        try {
            entidadEnEdicion = restClient.obtenerPorId(var);
            cargarOperacionesDeCategoria();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getRolPk() != null) {

                if (entidadEnEdicion.getRolRolOperacion() == null) {
                    List<SgRolOperacion> list = new ArrayList<>();
                    entidadEnEdicion.setRolRolOperacion(list);
                }
                entidadEnEdicion.getRolRolOperacion().addAll(rolOperacioensSeleccionados);

                for (SgRolOperacion rop : rolOperacioensAntesDeCambio) {
                    Boolean existe = Boolean.TRUE;
                    for (SgRolOperacion rac : rolOperacioensSeleccionados) {
                        if (rop.getRopOperacion().equals(rac.getRopOperacion())) {
                            existe = Boolean.FALSE;
                            break;
                        }
                    }
                    if (existe) {
                        entidadEnEdicion.getRolRolOperacion().remove(rop);
                    }
                }
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            cargarOperacionesDeCategoria();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getRolPk());
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
            historialRol = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean esSuperUsuario() {
        return BooleanUtils.isNotTrue(sessionBean.getEntidadUsuario().getUsuSuperUsuario());

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

    public Long getIdActualizar() {
        return idActualizar;
    }

    public void setIdActualizar(Long idActualizar) {
        this.idActualizar = idActualizar;
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

    public Boolean getVerOperacionesAsignadas() {
        return verOperacionesAsignadas;
    }

    public void setVerOperacionesAsignadas(Boolean verOperacionesAsignadas) {
        this.verOperacionesAsignadas = verOperacionesAsignadas;
    }

    public List<SgRolOperacion> getRolOperaciones() {
        return rolOperaciones;
    }

    public void setRolOperaciones(List<SgRolOperacion> rolOperaciones) {
        this.rolOperaciones = rolOperaciones;
    }

    public List<SgRolOperacion> getRolOperacioensSeleccionados() {
        return rolOperacioensSeleccionados;
    }

    public void setRolOperacioensSeleccionados(List<SgRolOperacion> rolOperacioensSeleccionados) {
        this.rolOperacioensSeleccionados = rolOperacioensSeleccionados;
    }

}
