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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgDatoEmpleado;
import sv.gob.mined.siges.web.dto.SgExperienciaLaboral;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoInstitucionPaga;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroExperienciaLaboral;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DatoEmpleadoRestClient;
import sv.gob.mined.siges.web.restclient.ExperienciaLaboralRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DocenteExperienciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocenteExperienciaBean.class.getName());

    @Inject
    private DatoEmpleadoRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private ExperienciaLaboralRestClient restExperienciaLaboral;

    @Inject
    private SessionBean sessionBean;

    private SgDatoEmpleado entidadEnEdicion = new SgDatoEmpleado();
    private SgPersonalSedeEducativa personalSede;
    private SofisComboG<SgTipoInstitucionPaga> comboTipoInstitucionPagaExp;
    private SgExperienciaLaboral entidadEnEdicionExperiencia = new SgExperienciaLaboral();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Boolean soloLectura = Boolean.TRUE;

    public DocenteExperienciaBean() {
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
            if (personalSede.getPseDatoEmpleado() != null) {
                entidadEnEdicion = personalSede.getPseDatoEmpleado();
            }
            entidadEnEdicion.setDemPersonalSede(personalSede);
            if (entidadEnEdicion.getDemPk() != null) {
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            FiltroExperienciaLaboral filtro = new FiltroExperienciaLaboral();
            filtro.setElaDatoEmpleadoPk(entidadEnEdicion.getDemPk());
            entidadEnEdicion.setDemExperienciaLaboral(restExperienciaLaboral.buscar(filtro));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera filtroC = new FiltroCodiguera();
            filtroC.setHabilitado(Boolean.TRUE);

            filtroC.setOrderBy(new String[]{"tipNombre"});
            filtroC.setIncluirCampos(new String[]{"tipNombre", "tipVersion"});
            List<SgTipoInstitucionPaga> listaTipoInstitucionPaga = restCatalogo.buscarTipoInstitucionPaga(filtroC);
            comboTipoInstitucionPagaExp = new SofisComboG(listaTipoInstitucionPaga, "tipNombre");
            comboTipoInstitucionPagaExp.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoInstitucionPagaExp.setSelected(-1);
    }

    public void agregar() {
        limpiarCombos();
        cargarCombos();
        entidadEnEdicion = new SgDatoEmpleado();
    }

    public void agregarExperiencia() {
        entidadEnEdicionExperiencia = new SgExperienciaLaboral();
        limpiarCombos();
    }

    public void actualizar(SgDatoEmpleado var) {
        limpiarCombos();
        entidadEnEdicion = (SgDatoEmpleado) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void actualizarExperiencia(SgExperienciaLaboral var) {
        limpiarCombos();
        entidadEnEdicionExperiencia = (SgExperienciaLaboral) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDemPk());
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

    public Boolean getSoloLecturaExperiencia() {
        return this.soloLectura || (entidadEnEdicionExperiencia.getElaPk() != null && !sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_EXPERIENCIA_LABORAL));
    }

    public void editarExperiencia(SgExperienciaLaboral var) {
        try {
            entidadEnEdicionExperiencia = restExperienciaLaboral.obtenerPorId(var.getElaPk());
            comboTipoInstitucionPagaExp.setSelectedT(entidadEnEdicionExperiencia.getElaTipoInstitucion());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarExperiencia(SgExperienciaLaboral exp) {
        try {
            if (BooleanUtils.isFalse(exp.getElaValidada())){
                restExperienciaLaboral.invalidarRealizado(exp.getElaPk());
            } else {
                restExperienciaLaboral.validarRealizado(exp.getElaPk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarExperiencia() {
        try {
            restExperienciaLaboral.eliminar(entidadEnEdicionExperiencia.getElaPk());
            if (this.entidadEnEdicion.getDemExperienciaLaboral().contains(entidadEnEdicionExperiencia)) {
                this.entidadEnEdicion.getDemExperienciaLaboral().remove(entidadEnEdicionExperiencia);
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarExperienciaDatosEmpleados() {
        try {
            entidadEnEdicionExperiencia.setElaTipoInstitucion(comboTipoInstitucionPagaExp.getSelectedT());
            entidadEnEdicionExperiencia.setElaDatoEmpleado(new SgDatoEmpleado(entidadEnEdicion.getDemPk(), entidadEnEdicion.getDemVersion()));

            if (entidadEnEdicion.getDemExperienciaLaboral() == null) {
                this.entidadEnEdicion.setDemExperienciaLaboral(new ArrayList<>());
            }

            entidadEnEdicionExperiencia = restExperienciaLaboral.guardar(entidadEnEdicionExperiencia);
            if (entidadEnEdicion.getDemExperienciaLaboral().contains(entidadEnEdicionExperiencia)) {
                entidadEnEdicion.getDemExperienciaLaboral().set(entidadEnEdicion.getDemExperienciaLaboral().indexOf(entidadEnEdicionExperiencia), entidadEnEdicionExperiencia);
            } else {
                entidadEnEdicion.getDemExperienciaLaboral().add(entidadEnEdicionExperiencia);

            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('experienciaDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgExperiencia", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public DatoEmpleadoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(DatoEmpleadoRestClient restClient) {
        this.restClient = restClient;
    }

    public SgDatoEmpleado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDatoEmpleado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SgExperienciaLaboral getEntidadEnEdicionExperiencia() {
        return entidadEnEdicionExperiencia;
    }

    public void setEntidadEnEdicionExperiencia(SgExperienciaLaboral entidadEnEdicionExperiencia) {
        this.entidadEnEdicionExperiencia = entidadEnEdicionExperiencia;
    }

    public SofisComboG<SgTipoInstitucionPaga> getComboTipoInstitucionPagaExp() {
        return comboTipoInstitucionPagaExp;
    }

    public void setComboTipoInstitucionPagaExp(SofisComboG<SgTipoInstitucionPaga> comboTipoInstitucionPagaExp) {
        this.comboTipoInstitucionPagaExp = comboTipoInstitucionPagaExp;
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

}
