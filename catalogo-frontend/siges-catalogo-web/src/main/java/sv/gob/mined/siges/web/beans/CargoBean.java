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
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCargo;
import sv.gob.mined.siges.web.dto.SgCargoRoles;
import sv.gob.mined.siges.web.dto.SgRol;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRol;
import sv.gob.mined.siges.web.lazymodels.LazyCargoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CargoRestClient;
import sv.gob.mined.siges.web.restclient.RolRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CargoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargoBean.class.getName());

    @Inject
    private CargoRestClient restClient;

    @Inject
    private RolRestClient rolRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgCargo entidadEnEdicion = new SgCargo();
    private List<SgCargo> historialCargo = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCargoDataModel cargoLazyModel;

    private String tabActiveId;
    private FiltroRol filtroRol = new FiltroRol();
    private List<SgCargoRoles> rolesCargos = new ArrayList();
    private List<SgCargoRoles> rolesCargosSeleccionados = new ArrayList();
    private List<SgCargoRoles> rolesCargosAntesDeCambio = new ArrayList();

    public CargoBean() {
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

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            cargoLazyModel = new LazyCargoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCargo();
        cargarRoles();
    }

    public void actualizar(SgCargo var) throws Exception {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = restClient.obtenerPorId(var.getCarPk());
        cargarRoles();

    }

    public void guardar() {
        try {
            entidadEnEdicion.getCarCargoRoles().addAll(rolesCargosSeleccionados);
            for (SgCargoRoles rop : rolesCargosAntesDeCambio) {
                Boolean existe = Boolean.TRUE;
                for (SgCargoRoles rac : rolesCargosSeleccionados) {
                    if (rop.getCarRol().equals(rac.getCarRol())) {
                        existe = Boolean.FALSE;
                        break;
                    }
                }
                if (existe) {
                    entidadEnEdicion.getCarCargoRoles().remove(rop);
                }
            }

            restClient.guardar(entidadEnEdicion);
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

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        if (tabActiveId.equals("roles")) {
            cargarRoles();
        }
    }

    public void cargarRoles() {
        try {
            List<SgRol> roles = new ArrayList();
            List<SgRol> listAuxiliar = new ArrayList();

            filtroRol = new FiltroRol();
            filtroRol.setHabilitado(Boolean.TRUE);
            roles = rolRestClient.buscar(filtroRol);

            rolesCargos = new ArrayList();
            rolesCargosSeleccionados = new ArrayList();

            for (SgCargoRoles lsRol : entidadEnEdicion.getCarCargoRoles()) {
                if (roles.contains(lsRol.getCarRol())) {
                    rolesCargosSeleccionados.add(lsRol);
                    rolesCargos.add(lsRol);
                }
            }

            listAuxiliar = (rolesCargosSeleccionados.stream().map(m -> m.getCarRol()).collect(Collectors.toList()));
            for (SgRol rol : roles) {
                SgCargoRoles rop = new SgCargoRoles();
                if (!listAuxiliar.contains(rol)) {
                    rop.setCarRol(rol);
                    rolesCargos.add(rop);
                }

            }
            Collections.sort(rolesCargos, (o1, o2) -> o1.getCarRol().getRolNombre().compareTo(o2.getCarRol().getRolNombre()));

            rolesCargosAntesDeCambio = rolesCargosSeleccionados;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCarPk());
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
            historialCargo = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCargo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCargo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgCargo> getHistorialCargo() {
        return historialCargo;
    }

    public void setHistorialCargo(List<SgCargo> historialCargo) {
        this.historialCargo = historialCargo;
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

    public LazyCargoDataModel getCargoLazyModel() {
        return cargoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCargoDataModel cargoLazyModel) {
        this.cargoLazyModel = cargoLazyModel;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public FiltroRol getFiltroRol() {
        return filtroRol;
    }

    public void setFiltroRol(FiltroRol filtroRol) {
        this.filtroRol = filtroRol;
    }

    public List<SgCargoRoles> getRolesCargos() {
        return rolesCargos;
    }

    public void setRolesCargos(List<SgCargoRoles> rolesCargos) {
        this.rolesCargos = rolesCargos;
    }

    public List<SgCargoRoles> getRolesCargosSeleccionados() {
        return rolesCargosSeleccionados;
    }

    public void setRolesCargosSeleccionados(List<SgCargoRoles> rolesCargosSeleccionados) {
        this.rolesCargosSeleccionados = rolesCargosSeleccionados;
    }

    public List<SgCargoRoles> getRolesCargosAntesDeCambio() {
        return rolesCargosAntesDeCambio;
    }

    public void setRolesCargosAntesDeCambio(List<SgCargoRoles> rolesCargosAntesDeCambio) {
        this.rolesCargosAntesDeCambio = rolesCargosAntesDeCambio;
    }

    


}
