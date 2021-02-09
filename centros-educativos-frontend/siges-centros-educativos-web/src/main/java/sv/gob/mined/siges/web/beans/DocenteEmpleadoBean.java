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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgDatoEmpleado;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgRelPersonalEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAfp;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaEmpleado;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelEmpleado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDatoEmpleado;
import sv.gob.mined.siges.web.enumerados.EnumTipoDatoEmpleadoGuardar;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelPersonalEspecialidad;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DatoEmpleadoRestClient;
import sv.gob.mined.siges.web.restclient.RelPersonalEspecialidadRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DocenteEmpleadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocenteEmpleadoBean.class.getName());

    @Inject
    private DatoEmpleadoRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private RelPersonalEspecialidadRestClient restRelPersonalEspecialidad;
    

    private SgDatoEmpleado entidadEnEdicion = new SgDatoEmpleado();
    private SgPersonalSedeEducativa personalSede;
    private FiltroRelPersonalEspecialidad filtro = new FiltroRelPersonalEspecialidad();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private SofisComboG<EnumEstadoDatoEmpleado> comboEstadoDatoEmpleado;
    private SofisComboG<SgNivelEmpleado> comboNivel;
    private SofisComboG<SgCategoriaEmpleado> comboCategoria;
    private SofisComboG<SgAfp> comboAfp;
    private Boolean soloLectura = Boolean.TRUE;
    private SgEspecialidad especialidadSeleccionada;
    private List<Long> excluirEsp = new ArrayList();
    private List<SgRelPersonalEspecialidad> listaRelEspecialidades;
    private SgRelPersonalEspecialidad entidadEnEdicionEspecialidad = new SgRelPersonalEspecialidad();

    public DocenteEmpleadoBean() {
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
                comboEstadoDatoEmpleado.setSelectedT(entidadEnEdicion.getDemEstado());
                comboNivel.setSelectedT(entidadEnEdicion.getDemNivelFk());
                comboCategoria.setSelectedT(entidadEnEdicion.getDemCategoriaFk());
                comboAfp.setSelectedT(entidadEnEdicion.getDemAfp());
            }else{
                entidadEnEdicion = new SgDatoEmpleado();
                entidadEnEdicion.setDemPersonalSede(personalSede);
            }
            if(entidadEnEdicion.getDemEmpleadoMineducyt()==null){
                entidadEnEdicion.setDemEmpleadoMineducyt(Boolean.FALSE);
            }
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            filtro.setPersonal(personalSede.getPsePk());
            filtro.setOrderBy(new String[]{"rpeEspecialidad.espNombre"});
            filtro.setAscending(new boolean[]{false});
            filtro.setIncluirCampos(new String[]{"rpeEspecialidad.espPk", 
                "rpeEspecialidad.espCodigo", "rpeEspecialidad.espNombre", 
                "rpeEspecialidad.espVersion", "rpeFechaGraduacion", "rpeCum"});
            listaRelEspecialidades = restRelPersonalEspecialidad.buscar(filtro);
            excluirEsp = new ArrayList();
            for (SgRelPersonalEspecialidad rel : listaRelEspecialidades) {
                excluirEsp.add(rel.getRpeEspecialidad().getEspPk());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void agregarEspecialidad(){
        entidadEnEdicionEspecialidad = new SgRelPersonalEspecialidad();
        especialidadSeleccionada = null;
        JSFUtils.limpiarMensajesError();
    }


    public void cargarCombos() {
        try {
            List<EnumEstadoDatoEmpleado> estados = new ArrayList(Arrays.asList(EnumEstadoDatoEmpleado.values()));
            comboEstadoDatoEmpleado = new SofisComboG(estados, "text");
            comboEstadoDatoEmpleado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstadoDatoEmpleado.ordenar();

            FiltroCodiguera filtroC = new FiltroCodiguera();
            filtroC.setHabilitado(Boolean.TRUE);

            filtroC.setOrderBy(new String[]{"nemNombre"});
            filtroC.setIncluirCampos(new String[]{"nemNombre", "nemVersion"});
            List<SgNivelEmpleado> listaNivel = restCatalogo.buscarNivelEmpleado(filtroC);
            comboNivel = new SofisComboG(listaNivel, "nemNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"cemNombre"});
            filtroC.setIncluirCampos(new String[]{"cemNombre", "cemVersion"});
            List<SgCategoriaEmpleado> listaCategoria = restCatalogo.buscarCategoriaEmpleado(filtroC);
            comboCategoria = new SofisComboG(listaCategoria, "cemNombre");
            comboCategoria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"afpNombre"});
            filtroC.setIncluirCampos(new String[]{"afpNombre", "afpVersion"});
            List<SgAfp> afps = restCatalogo.buscarAfp(filtroC);
            comboAfp = new SofisComboG(afps, "afpNombre");
            comboAfp.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombos() {
        comboNivel.setSelected(-1);
        comboCategoria.setSelected(-1);
    }

    public String limpiar() {
        filtro = new FiltroRelPersonalEspecialidad();
        buscar();
        return null;
    }

    public void agregar() {
        limpiarCombos();
        cargarCombos();
        entidadEnEdicion = new SgDatoEmpleado();
    }

    public void actualizar(SgDatoEmpleado var) {
        limpiarCombos();
        entidadEnEdicion = (SgDatoEmpleado) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void guardar() {
        try {
            //Guardar datos de empleado
            entidadEnEdicion.setDemAfp(comboAfp.getSelectedT());
            entidadEnEdicion.setDemTipoDatoGuardar(EnumTipoDatoEmpleadoGuardar.DATOS_GENERALES);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void modificarEscalafon() {
        try{
            entidadEnEdicion.setDemCategoriaFk(comboCategoria.getSelectedT());
            entidadEnEdicion.setDemNivelFk(comboNivel.getSelectedT());
            entidadEnEdicion.setDemEstado(comboEstadoDatoEmpleado.getSelectedT());
            entidadEnEdicion.setDemTipoDatoGuardar(EnumTipoDatoEmpleadoGuardar.ESCALAFON);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('escalafonDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().ajax().update("form:escalafonDetalle"); 
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgEscalafon", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgEscalafon", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgEspecialidad> completeEspecialidad(String query) {
        try {
            FiltroEspecialidad fil = new FiltroEspecialidad();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"espNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setEspExcluirPK(excluirEsp);
            return restCatalogo.buscarEspecialidad(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void guardarEspecialidad() {
        try {
            entidadEnEdicionEspecialidad.setRpeEspecialidad(especialidadSeleccionada);
            entidadEnEdicionEspecialidad.setRpePersonal(personalSede);

            entidadEnEdicionEspecialidad = restRelPersonalEspecialidad.guardar(entidadEnEdicionEspecialidad);
            excluirEsp.add(especialidadSeleccionada.getEspPk());
            especialidadSeleccionada = null;

            if (listaRelEspecialidades.contains(entidadEnEdicionEspecialidad)) {
                listaRelEspecialidades.set(listaRelEspecialidades.indexOf(entidadEnEdicionEspecialidad), entidadEnEdicionEspecialidad);
            } else {
                listaRelEspecialidades.add(entidadEnEdicionEspecialidad);
            }
            PrimeFaces.current().executeScript("PF('especialidadesPersonalDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgEspecialidades", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgEspecialidades", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarEspecialidad(SgRelPersonalEspecialidad var) {
        limpiarCombos();
        entidadEnEdicionEspecialidad = var;
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminarEspecialidad() {
        try {
            SgRelPersonalEspecialidad esp = entidadEnEdicionEspecialidad;
            restRelPersonalEspecialidad.eliminar(esp.getRpePk());
            excluirEsp.remove(esp.getRpeEspecialidad().getEspPk());
            listaRelEspecialidades.remove(esp);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public FiltroRelPersonalEspecialidad getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRelPersonalEspecialidad filtro) {
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

    public SofisComboG<EnumEstadoDatoEmpleado> getComboEstadoDatoEmpleado() {
        return comboEstadoDatoEmpleado;
    }

    public void setComboEstadoDatoEmpleado(SofisComboG<EnumEstadoDatoEmpleado> comboEstadoDatoEmpleado) {
        this.comboEstadoDatoEmpleado = comboEstadoDatoEmpleado;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SofisComboG<SgNivelEmpleado> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivelEmpleado> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgCategoriaEmpleado> getComboCategoria() {
        return comboCategoria;
    }

    public void setComboCategoria(SofisComboG<SgCategoriaEmpleado> comboCategoria) {
        this.comboCategoria = comboCategoria;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgAfp> getComboAfp() {
        return comboAfp;
    }

    public void setComboAfp(SofisComboG<SgAfp> comboAfp) {
        this.comboAfp = comboAfp;
    }

    public SgEspecialidad getEspecialidadSeleccionada() {
        return especialidadSeleccionada;
    }

    public void setEspecialidadSeleccionada(SgEspecialidad especialidadSeleccionada) {
        this.especialidadSeleccionada = especialidadSeleccionada;
    }

    public SgPersonalSedeEducativa getPersonalSede() {
        return personalSede;
    }

    public void setPersonalSede(SgPersonalSedeEducativa personalSede) {
        this.personalSede = personalSede;
    }

    public List<SgRelPersonalEspecialidad> getListaRelEspecialidades() {
        return listaRelEspecialidades;
    }

    public void setListaRelEspecialidades(List<SgRelPersonalEspecialidad> listaRelEspecialidades) {
        this.listaRelEspecialidades = listaRelEspecialidades;
    }

    public SgRelPersonalEspecialidad getEntidadEnEdicionEspecialidad() {
        return entidadEnEdicionEspecialidad;
    }

    public void setEntidadEnEdicionEspecialidad(SgRelPersonalEspecialidad entidadEnEdicionEspecialidad) {
        this.entidadEnEdicionEspecialidad = entidadEnEdicionEspecialidad;
    }
    //</editor-fold>

 
}
