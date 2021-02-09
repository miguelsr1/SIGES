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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgPerfilRoles;
import sv.gob.mined.siges.web.dto.SgPerfilesUsuariosIngresadosCe;
import sv.gob.mined.siges.web.dto.SgRol;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRol;
import sv.gob.mined.siges.web.lazymodels.LazyPerfilesUsuariosIngresadosCeDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PerfilesUsuariosIngresadosCeRestClient;
import sv.gob.mined.siges.web.restclient.RolRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PerfilesUsuariosIngresadosCeBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PerfilesUsuariosIngresadosCeBean.class.getName());

    @Inject
    private PerfilesUsuariosIngresadosCeRestClient restClient;
    
    @Inject
    private RolRestClient rolRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgPerfilesUsuariosIngresadosCe entidadEnEdicion = new SgPerfilesUsuariosIngresadosCe();
    private List<SgPerfilesUsuariosIngresadosCe> historialPerfilesUsuariosIngresadosCe = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPerfilesUsuariosIngresadosCeDataModel perfilesUsuariosIngresadosCeLazyModel;
    
    private List<SgPerfilRoles> rolesPerfiles = new ArrayList();
    private List<SgPerfilRoles> rolesPerfilesSeleccionados = new ArrayList();
    private List<SgPerfilRoles> rolesPerfilesAntesDeCambio = new ArrayList();
    private String tabActiveId;
    
    private FiltroRol filtroRol = new FiltroRol();

    public PerfilesUsuariosIngresadosCeBean() {
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
            perfilesUsuariosIngresadosCeLazyModel = new LazyPerfilesUsuariosIngresadosCeDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }
    
    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        if (tabActiveId.equals("roles")) {
            cargarRoles();
        }
    }
    
    public void cargarRoles(){
        try {
            List<SgRol> roles = new ArrayList();
            List<SgRol> listAuxiliar = new ArrayList();

            filtroRol = new FiltroRol();
            filtroRol.setHabilitado(Boolean.TRUE);
            roles = rolRestClient.buscar(filtroRol);

            rolesPerfiles = new ArrayList();
            rolesPerfilesSeleccionados = new ArrayList();
            if(entidadEnEdicion.getPuiPerfilRoles() != null){
                for (SgPerfilRoles lsRol : entidadEnEdicion.getPuiPerfilRoles()) {
                    if (roles.contains(lsRol.getUirRol())) {
                        rolesPerfilesSeleccionados.add(lsRol);
                        rolesPerfiles.add(lsRol);
                    }
                }
            }
            listAuxiliar = (rolesPerfilesSeleccionados.stream().map(m -> m.getUirRol()).collect(Collectors.toList()));
            for (SgRol rol : roles) {
                SgPerfilRoles rop = new SgPerfilRoles();
                if (!listAuxiliar.contains(rol)) {
                    rop.setUirRol(rol);
                    rolesPerfiles.add(rop);
                }
            }
            Collections.sort(rolesPerfiles, (o1, o2) -> o1.getUirRol().getRolNombre().compareTo(o2.getUirRol().getRolNombre()));

            rolesPerfilesAntesDeCambio = rolesPerfilesSeleccionados;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgPerfilesUsuariosIngresadosCe();
        cargarRoles();
    }

    public void actualizar(SgPerfilesUsuariosIngresadosCe var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgPerfilesUsuariosIngresadosCe) SerializationUtils.clone(var);
        cargarRoles();
    }

    public void guardar() {
        try {
            entidadEnEdicion.getPuiPerfilRoles().addAll(rolesPerfilesSeleccionados);
            for (SgPerfilRoles rop : rolesPerfilesAntesDeCambio) {
                Boolean existe = Boolean.TRUE;
                for (SgPerfilRoles rac : rolesPerfilesSeleccionados) {
                    if (rop.getUirRol().equals(rac.getUirRol())) {
                        existe = Boolean.FALSE;
                        break;
                    }
                }
                if (existe) {
                    entidadEnEdicion.getPuiPerfilRoles().remove(rop);
                }
            }
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
            restClient.eliminar(entidadEnEdicion.getPuiPk());
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
            historialPerfilesUsuariosIngresadosCe = restClient.obtenerHistorialPorId(id);
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

    public SgPerfilesUsuariosIngresadosCe getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPerfilesUsuariosIngresadosCe entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgPerfilesUsuariosIngresadosCe> getHistorialPerfilesUsuariosIngresadosCe() {
        return historialPerfilesUsuariosIngresadosCe;
    }

    public void setHistorialPerfilesUsuariosIngresadosCe(List<SgPerfilesUsuariosIngresadosCe> historialPerfilesUsuariosIngresadosCe) {
        this.historialPerfilesUsuariosIngresadosCe = historialPerfilesUsuariosIngresadosCe;
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

    public LazyPerfilesUsuariosIngresadosCeDataModel getPerfilesUsuariosIngresadosCeLazyModel() {
        return perfilesUsuariosIngresadosCeLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyPerfilesUsuariosIngresadosCeDataModel perfilesUsuariosIngresadosCeLazyModel) {
        this.perfilesUsuariosIngresadosCeLazyModel = perfilesUsuariosIngresadosCeLazyModel;
    }

    public List<SgPerfilRoles> getRolesPerfiles() {
        return rolesPerfiles;
    }

    public void setRolesPerfiles(List<SgPerfilRoles> rolesPerfiles) {
        this.rolesPerfiles = rolesPerfiles;
    }

    public List<SgPerfilRoles> getRolesPerfilesSeleccionados() {
        return rolesPerfilesSeleccionados;
    }

    public void setRolesPerfilesSeleccionados(List<SgPerfilRoles> rolesPerfilesSeleccionados) {
        this.rolesPerfilesSeleccionados = rolesPerfilesSeleccionados;
    }

    public List<SgPerfilRoles> getRolesPerfilesAntesDeCambio() {
        return rolesPerfilesAntesDeCambio;
    }

    public void setRolesPerfilesAntesDeCambio(List<SgPerfilRoles> rolesPerfilesAntesDeCambio) {
        this.rolesPerfilesAntesDeCambio = rolesPerfilesAntesDeCambio;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

}
