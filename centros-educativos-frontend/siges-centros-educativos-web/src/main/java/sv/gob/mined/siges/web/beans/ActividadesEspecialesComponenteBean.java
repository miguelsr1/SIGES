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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgActividadEspecialSeccion;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgFormula;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumFuncionRedondeo;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroFormula;
import sv.gob.mined.siges.web.lazymodels.LazyComponentePlanGradoDataModel;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ActividadesEspecialesComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ActividadesEspecialesComponenteBean.class.getName());

    @Inject
    private ComponentePlanGradoRestClient restClient;

    @Inject
    private ComponentePlanEstudioRestClient cpeRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstRestClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private HorarioEscolarBean horarioEscolarBean;
    @Inject
    private CalificacionRestClient calificacionRestClient;

    private SgComponentePlanGrado entidadEnEdicion = new SgComponentePlanGrado();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyComponentePlanGradoDataModel componentePlanGradoLazyModel;
    private List<SgGrado> listaGrados;
    private SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion;
    private SgComponentePlanEstudio componentesPlanEstudioSeleccionado;
    private SofisComboG<SgProgramaEducativo> comboProgEducativo;
    private SofisComboG<TipoComponentePlanEstudio> comboTiposCompPlanEst;
    private SofisComboG<SgFormula> comboFormulas;
    private SgModalidad modalidadEnEdicion;
    private SofisComboG<EnumCalculoNotaInstitucional> comboCalculoNotaInstitucional;
    private SofisComboG<EnumFuncionRedondeo> comboFuncionRedondeo;
    private SgRelGradoPlanEstudio relGradoPlanEst;
    private Boolean soloLectura = Boolean.FALSE;
    private SgSeccion seccion;
    private TipoComponentePlanEstudio tipoActividadEspecial=TipoComponentePlanEstudio.AESS;
    private SgActividadEspecialSeccion actividadEspecial=new SgActividadEspecialSeccion();
    private List<SgComponentePlanGrado> listCpg=new ArrayList();

    public ActividadesEspecialesComponenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            soloLectura = request.getAttribute("soloLecturaActividadesEspeciales") != null ? (Boolean) request.getAttribute("soloLecturaActividadesEspeciales") : soloLectura;
            seccion = (SgSeccion) request.getAttribute("seccion");

            if (!soloLectura) {
                cargarCombos();
            }
             if(seccion!=null){
                buscar();
             }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALENDARIOS)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void forceInit() {
    }
    
    public void seleccionarSeccion(SgSeccion sec){
        seccion=sec;
        buscar();
    }

    public void buscar() {
        try {            
            FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();           
            fpg.setCpgSeccionExclusiva(seccion.getSecPk());
            fpg.setOrderBy(new String[]{"cpgComponentePlanEstudio.cpeNombre"});
            fpg.setAscending(new boolean[]{true});
            listCpg=restClient.buscar(fpg);
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroEscalaCalificacion fec = new FiltroEscalaCalificacion();
            fec.setEcaHabilitado(Boolean.TRUE);
            List<SgEscalaCalificacion> listaSectorEconomico = catalogoRestClient.buscarEscalaCalificacion(fec);
            comboEscalaCalificacion = new SofisComboG(listaSectorEconomico, "ecaNombre");
            comboEscalaCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEscalaCalificacion.ordenar();        

            comboCalculoNotaInstitucional = new SofisComboG();
            comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumFuncionRedondeo> funcionesRedondeo = new ArrayList(Arrays.asList(EnumFuncionRedondeo.values()));
            comboFuncionRedondeo = new SofisComboG(funcionesRedondeo, "text");
            comboFuncionRedondeo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroFormula fc = new FiltroFormula();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"fomNombre"});
            List<SgFormula> listaFormula = catalogoRestClient.buscarFormulas(fc);
            comboFormulas = new SofisComboG(listaFormula, "fomNombre");
            comboFormulas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eleccionEscalaCalificacion() {
        if (comboEscalaCalificacion.getSelectedT() != null) {
            List<EnumCalculoNotaInstitucional> list = new ArrayList();
            list.add(EnumCalculoNotaInstitucional.MAY);
            list.add(EnumCalculoNotaInstitucional.ULT);
            if (comboEscalaCalificacion.getSelectedT().getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                list.add(EnumCalculoNotaInstitucional.PROM);
                list.add(EnumCalculoNotaInstitucional.MED);
            }
            comboCalculoNotaInstitucional = new SofisComboG(list, "text");
            comboCalculoNotaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        }

    }

    public void cargarComponentePlanEstudio() {
        try {
            this.componentesPlanEstudioSeleccionado = null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboFormulas.setSelected(-1);
        this.comboEscalaCalificacion.setSelected(-1);
        comboCalculoNotaInstitucional.setSelected(-1);
        comboFuncionRedondeo.setSelected(-1);
    }

    public void limpiar() {
    }
    
    public void actualizarSeccion(SgSeccion sec){
        seccion=sec;
    }

    public void agregar() {
        limpiarCombos();
        actividadEspecial=new SgActividadEspecialSeccion();
        entidadEnEdicion = new SgComponentePlanGrado();
        entidadEnEdicion.setCpgGrado(seccion.getSecServicioEducativo().getSduGrado());
        entidadEnEdicion.setCpgPlanEstudio(seccion.getSecPlanEstudio());
        JSFUtils.limpiarMensajesError();

    }

    public void actualizar(SgComponentePlanGrado var) {
        limpiarCombos();
        entidadEnEdicion = (SgComponentePlanGrado) SerializationUtils.clone(var);
        if (entidadEnEdicion.getCpgEscalaCalificacion() != null) {
            comboEscalaCalificacion.setSelectedT(entidadEnEdicion.getCpgEscalaCalificacion());
        }
        actividadEspecial=(SgActividadEspecialSeccion)entidadEnEdicion.getCpgComponentePlanEstudio();
       
        eleccionEscalaCalificacion();
        if (entidadEnEdicion.getCpgCalculoNotaInstitucional() != null) {
            comboCalculoNotaInstitucional.setSelectedT(entidadEnEdicion.getCpgCalculoNotaInstitucional());
            comboFuncionRedondeo.setSelectedT(entidadEnEdicion.getCpgFuncionRedondeo());
        }
        if (entidadEnEdicion.getCpgFormula() != null) {
            comboFormulas.setSelectedT(entidadEnEdicion.getCpgFormula());
        }

      
        JSFUtils.limpiarMensajesError();
    }

    public void guardar() {
        try {
            if (StringUtils.isBlank(entidadEnEdicion.getCpgNombrePublicable())) {
                entidadEnEdicion.setCpgNombrePublicable(componentesPlanEstudioSeleccionado != null ? componentesPlanEstudioSeleccionado.getCpeNombre() : null);
            }
            actividadEspecial.setCpeTipo(tipoActividadEspecial);
            entidadEnEdicion.setCpgComponentePlanEstudio(actividadEspecial);
            entidadEnEdicion.setCpgEscalaCalificacion(comboEscalaCalificacion.getSelectedT());
            entidadEnEdicion.setCpgCalculoNotaInstitucional(comboCalculoNotaInstitucional.getSelectedT());
            entidadEnEdicion.setCpgFuncionRedondeo(EnumCalculoNotaInstitucional.PROM.equals(comboCalculoNotaInstitucional.getSelectedT()) ? comboFuncionRedondeo.getSelectedT() : null);
            entidadEnEdicion.setCpgFormula(comboFormulas.getSelectedT());
            entidadEnEdicion.setCpgExclusivoSeccion(seccion);
            entidadEnEdicion.setCpgNombrePublicable(actividadEspecial.getCpeNombre());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            PrimeFaces.current().executeScript("PF('itemDialogActividad').hide()");
            horarioEscolarBean.agregarHorarioEscolar(seccion);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void prepararParaEliminar(SgComponentePlanGrado var) {
        limpiarCombos();
        try {
            FiltroComponentePlanGrado fpg=new FiltroComponentePlanGrado();
            fpg.setCpgPk(var.getCpgPk());
            fpg.setCpgSeccionExclusiva(seccion.getSecPk());
            fpg.setIncluirCampos(new String[]{"cpgPk","cpgGrado.graPk",
                "cpgPlanEstudio.pesPk",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgExclusivoSeccion.secPk",
                "cpgExclusivoSeccion.secVersion","cpgVersion"});
            
            List<SgComponentePlanGrado> listCpg=restClient.buscar(fpg);
            if(!listCpg.isEmpty()){
                entidadEnEdicion =listCpg.get(0);
                FiltroCalificacion fc=new FiltroCalificacion();
                fc.setSecGradoFk(entidadEnEdicion.getCpgGrado().getGraPk());
                fc.setSecPlanEstudioFk(entidadEnEdicion.getCpgPlanEstudio().getPesPk());
                fc.setCalComponentePlanEstudio(entidadEnEdicion.getCpgComponentePlanEstudio().getCpePk());
                fc.setIncluirCampos(new String[]{"calPk"});
                fc.setMaxResults(1L);
                List<SgCalificacionCE> calificaciones = calificacionRestClient.buscar(fc);
                if(!calificaciones.isEmpty()){
                    JSFUtils.agregarWarn("eliminarMsg", "ADVERTENCIA: El componente que desea eliminar tiene calificaciones asociadas, de continuar las mismas serán eliminadas", "");
                }
            }else{
                entidadEnEdicion=null;
            }
            
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminarComponentePlanGrado(entidadEnEdicion);
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            horarioEscolarBean.agregarHorarioEscolar(seccion);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }



    public List<SgComponentePlanEstudio> completePlanEstudio(String query) {
        try {
            FiltroComponentePlanEstudio fil = new FiltroComponentePlanEstudio();
            fil.setCpeNombre(query);
            fil.setCpeHabilitado(Boolean.TRUE);
            fil.setCpeTipo(tipoActividadEspecial);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"cpeNombre"});
            fil.setAscending(new boolean[]{true});
            return cpeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void agregarFormula(SgGrado grado) {
        try {
            relGradoPlanEst = new SgRelGradoPlanEstudio();
            relGradoPlanEst.setRgpGradoFk(grado);
          //  relGradoPlanEst.setRgpPlanEstudioFk(seccionEnEdicion);
            comboFormulas.setSelected(-1);

            FiltroRelGradoPlanEstudio fgpe = new FiltroRelGradoPlanEstudio();
            fgpe.setRgpGrado(grado.getGraPk());
   //         fgpe.setRgpPlanEstudio(planEstEnEdicion.getPesPk());
            List<SgRelGradoPlanEstudio> relGradoPlan = relGradoPlanEstRestClient.buscar(fgpe);
            if (relGradoPlan.size() > 0) {
                relGradoPlanEst = relGradoPlan.get(0);
                comboFormulas.setSelectedT(relGradoPlanEst.getRgpFormulaFk() != null ? relGradoPlanEst.getRgpFormulaFk() : null);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void guardarFormula() {
        try {

            relGradoPlanEst.setRgpFormulaFk(comboFormulas.getSelectedT());
            relGradoPlanEstRestClient.guardar(relGradoPlanEst);
            PrimeFaces.current().executeScript("PF('itemDialogFormula').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgComponentePlanGrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgComponentePlanGrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public LazyComponentePlanGradoDataModel getComponentePlanGradoLazyModel() {
        return componentePlanGradoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyComponentePlanGradoDataModel componentePlanGradoLazyModel) {
        this.componentePlanGradoLazyModel = componentePlanGradoLazyModel;
    }
  

    public List<SgGrado> getListaGrados() {
        return listaGrados;
    }

    public void setListaGrados(List<SgGrado> listaGrados) {
        this.listaGrados = listaGrados;
    }

    public SofisComboG<SgEscalaCalificacion> getComboEscalaCalificacion() {
        return comboEscalaCalificacion;
    }

    public void setComboEscalaCalificacion(SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion) {
        this.comboEscalaCalificacion = comboEscalaCalificacion;
    }

    public SgComponentePlanEstudio getComponentesPlanEstudioSeleccionado() {
        return componentesPlanEstudioSeleccionado;
    }

    public void setComponentesPlanEstudioSeleccionado(SgComponentePlanEstudio componentesPlanEstudioSeleccionado) {
        this.componentesPlanEstudioSeleccionado = componentesPlanEstudioSeleccionado;
    }

    public SofisComboG<SgProgramaEducativo> getComboProgEducativo() {
        return comboProgEducativo;
    }

    public void setComboProgEducativo(SofisComboG<SgProgramaEducativo> comboProgEducativo) {
        this.comboProgEducativo = comboProgEducativo;
    }

    public SgModalidad getModalidadEnEdicion() {
        return modalidadEnEdicion;
    }

    public void setModalidadEnEdicion(SgModalidad modalidadEnEdicion) {
        this.modalidadEnEdicion = modalidadEnEdicion;
    }

    public SofisComboG<TipoComponentePlanEstudio> getComboTiposCompPlanEst() {
        return comboTiposCompPlanEst;
    }

    public void setComboTiposCompPlanEst(SofisComboG<TipoComponentePlanEstudio> comboTiposCompPlanEst) {
        this.comboTiposCompPlanEst = comboTiposCompPlanEst;
    }

    public SofisComboG<EnumFuncionRedondeo> getComboFuncionRedondeo() {
        return comboFuncionRedondeo;
    }

    public void setComboFuncionRedondeo(SofisComboG<EnumFuncionRedondeo> comboFuncionRedondeo) {
        this.comboFuncionRedondeo = comboFuncionRedondeo;
    }

    public SofisComboG<EnumCalculoNotaInstitucional> getComboCalculoNotaInstitucional() {
        return comboCalculoNotaInstitucional;
    }

    public void setComboCalculoNotaInstitucional(SofisComboG<EnumCalculoNotaInstitucional> comboCalculoNotaInstitucional) {
        this.comboCalculoNotaInstitucional = comboCalculoNotaInstitucional;
    }

    public SofisComboG<SgFormula> getComboFormulas() {
        return comboFormulas;
    }

    public void setComboFormulas(SofisComboG<SgFormula> comboFormulas) {
        this.comboFormulas = comboFormulas;
    }

    public SgRelGradoPlanEstudio getRelGradoPlanEst() {
        return relGradoPlanEst;
    }

    public void setRelGradoPlanEst(SgRelGradoPlanEstudio relGradoPlanEst) {
        this.relGradoPlanEst = relGradoPlanEst;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(SgSeccion seccion) {
        this.seccion = seccion;
    }

    
    public TipoComponentePlanEstudio getTipoActividadEspecial() {
        return tipoActividadEspecial;
    }

    public void setTipoActividadEspecial(TipoComponentePlanEstudio tipoActividadEspecial) {
        this.tipoActividadEspecial = tipoActividadEspecial;
    }

    public SgActividadEspecialSeccion getActividadEspecial() {
        return actividadEspecial;
    }

    public void setActividadEspecial(SgActividadEspecialSeccion actividadEspecial) {
        this.actividadEspecial = actividadEspecial;
    }

    public List<SgComponentePlanGrado> getListCpg() {
        return listCpg;
    }

    public void setListCpg(List<SgComponentePlanGrado> listCpg) {
        this.listCpg = listCpg;
    }

    
    
}
