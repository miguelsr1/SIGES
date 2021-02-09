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
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SolicitudPlazaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.caux.SgMatrizComparativa;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgAplicacionPlaza;
import sv.gob.mined.siges.web.dto.SgEspecialidadesPersonalAlAplicar;
import sv.gob.mined.siges.web.dto.SgSolicitudPlaza;
import sv.gob.mined.siges.web.dto.catalogo.SgCalidadIngresoAplicantes;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivosSeleccionPLaza;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAplicacionPlaza;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AplicacionPlazaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class MatrizComparativaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MatrizComparativaBean.class.getName());

    @Inject
    private SolicitudPlazaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AplicacionPlazaRestClient restAplicaciones;
    
    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    @Param(name = "id")
    private Long plazaId;

    private SgSolicitudPlaza entidadEnEdicion;
    private SgSolicitudPlaza plazaSeleccionada;
    private FiltroAplicacionPlaza filtro = new FiltroAplicacionPlaza();
    private Integer paginado = 10;
    private Long totalResultados;
    private List<SgMatrizComparativa> listaMatriz = new ArrayList<>();
    private List<SgAplicacionPlaza> listAplicacionPlaza;
    private SgAplicacionPlaza aplicacionEnEdicion;
    private Boolean existeDocenteSeleccionado=Boolean.FALSE;
    private Boolean editarPublicacion;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private List<SgEspecialidad> especialidades;
    private List<SgDiscapacidad> discapacidades;
    private List<SgMotivosSeleccionPLaza> motivosSeleccion;
    private SofisComboG<SgCalidadIngresoAplicantes> comboCalidadAplicante;
    
    private String mensajeWarningDocentes = null;
    
    public MatrizComparativaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            buscarConfiguracion();
            cargarCombos();

            if (plazaId != null && plazaId > 0) {
                entidadEnEdicion = restClient.obtenerPorId(plazaId);
            }
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void buscarConfiguracion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.MENSAJE_MATRIZ_COMPARATIVA);
            List<SgConfiguracion> conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                mensajeWarningDocentes = conf.get(0).getConValor();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_MATRIZ_COMPARATIVA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            listaMatriz=new ArrayList();
            if (entidadEnEdicion != null) {

                filtro.setAplPlaza(entidadEnEdicion.getSplPk());
                filtro.setIncluirCampos(new String[]{
                    "aplPersonal.psePk",
                    "aplPersonal.pseVersion",
                    "aplPersonal.psePersona.perPk",
                    "aplPersonal.psePersona.perVersion",
                    "aplPersonal.psePersona.perPrimerNombre",
                    "aplPersonal.psePersona.perSegundoNombre",
                    "aplPersonal.psePersona.perTercerNombre",
                    "aplPersonal.psePersona.perPrimerApellido",
                    "aplPersonal.psePersona.perSegundoApellido",
                    "aplPersonal.psePersona.perTercerApellido",
                    "aplPersonal.psePersona.perDui",
                    "aplPersonal.psePersona.perNip",
                    "aplPersonal.psePersona.perDireccion.dirDepartamento.depNombre",
                    "aplPersonal.psePersona.perDireccion.dirMunicipio.munNombre",
                    "aplCalidadAplicantes.ciaNombre",
                    "aplCalidadAplicantes.ciaCodigo",
                    "aplCalidadAplicantes.ciaVersion",
                    "aplCalidadAplicantes.ciaPk",
                    "aplFechaAplico",
                    "aplPlaza.splPk",
                    "aplPlaza.splVersion",
                    "aplCodigoUsuario",
                    "aplEstado",
                    "aplObservacion",
                    "aplFechaAplico",
                    "aplSeleccionadoEnMatriz",
                    "aplRevPersonalCuandoAplica",
                    "aplObservacion",
                    "aplVersion"});
                filtro.setDepartamento(comboDepartamento.getSelectedT()!=null?comboDepartamento.getSelectedT().getDepPk():null);
                filtro.setMunicipio(comboMunicipio.getSelectedT()!=null?comboMunicipio.getSelectedT().getMunPk():null);
                
                if(especialidades!=null && !especialidades.isEmpty()){
                    filtro.setEspecialidades(new ArrayList(especialidades.stream().map(c->c.getEspPk()).collect(Collectors.toList())));
                }else{
                    filtro.setEspecialidades(null);
                }
                
                if(discapacidades !=null && !discapacidades.isEmpty()){
                    filtro.setDiscapacidades(new ArrayList(discapacidades.stream().map(c->c.getDisPk()).collect(Collectors.toList())));
                }else{
                    filtro.setDiscapacidades(null);
                }
                filtro.setInicializarDiscapacidades(Boolean.TRUE);
                filtro.setInicializarSedesPersonal(Boolean.TRUE);
                filtro.setInicializarEspecialidades(Boolean.TRUE);
                filtro.setInicializarMotivosSeleccion(Boolean.TRUE);
                filtro.setOrderBy(new String[]{"aplCalidadAplicantes.ciaNombre","aplSeleccionadoEnMatriz","aplPersonal.psePk"});
                filtro.setAscending(new boolean[]{false,false,true});
                
                if(comboCalidadAplicante.getSelectedT() != null){
                    filtro.setCalidadAplicante(comboCalidadAplicante.getSelectedT().getCiaPk());
                }
                
                totalResultados = restAplicaciones.buscarTotal(filtro);
                listAplicacionPlaza = restAplicaciones.buscar(filtro);
                SgAplicacionPlaza aplPlazaPrimerElemento=null;
                for (SgAplicacionPlaza app : listAplicacionPlaza) {             
                    if(BooleanUtils.isTrue(app.getAplSeleccionadoEnMatriz())){
                        aplPlazaPrimerElemento=app;
                    }else{
                        app.setAplMotivosSeleccionPLaza(null);
                    }
                    if (app.getAplEspecialidadesAlAplicar() != null && !app.getAplEspecialidadesAlAplicar().isEmpty()) {
                        
                        Collections.sort(app.getAplEspecialidadesAlAplicar(),(o1,o2)->(o1.getEpaFechaGraduacion()==null && o2.getEpaFechaGraduacion()==null)?1:
                        (o1.getEpaFechaGraduacion()==null && o2.getEpaFechaGraduacion()!=null)?1:
                        (o1.getEpaFechaGraduacion()!=null && o2.getEpaFechaGraduacion()==null)?-1:
                                o1.getEpaFechaGraduacion().compareTo(o2.getEpaFechaGraduacion()));
                       
                        app.setAplMenorFechaGraduacion(app.getAplEspecialidadesAlAplicar().get(0).getEpaFechaGraduacion());
                        Boolean agregadoDocente = Boolean.FALSE;
                        
                        for (SgEspecialidadesPersonalAlAplicar per : app.getAplEspecialidadesAlAplicar()) {
                            SgMatrizComparativa m = new SgMatrizComparativa();
                            m.setAplicacion(app);
                            m.setEspecialidades(per);
                            if (BooleanUtils.isTrue(app.getAplSeleccionadoEnMatriz())) {
                                m.setSeleccionadoDocente(Boolean.TRUE);
                                existeDocenteSeleccionado=Boolean.TRUE;
                            } else {
                                m.setSeleccionadoDocente(Boolean.FALSE);
                            }
                            if (!agregadoDocente) {
                                m.setMostrarBotonDocente(Boolean.TRUE);
                                agregadoDocente = Boolean.TRUE;
                            }
                            
                            if(app.getPlazaSeleccionado() != null){
                                m.setSeleccionadoOtraPlaza(Boolean.TRUE);
                            }else{
                                m.setSeleccionadoOtraPlaza(Boolean.FALSE);
                            }
                            
                            listaMatriz.add(m);
                        }
                    } else {
                        SgMatrizComparativa m = new SgMatrizComparativa();
                        m.setAplicacion(app);
                        m.setMostrarBotonDocente(Boolean.TRUE);
                        if (BooleanUtils.isTrue(app.getAplSeleccionadoEnMatriz())) {
                            existeDocenteSeleccionado=Boolean.TRUE;
                            m.setSeleccionadoDocente(Boolean.TRUE);
                        } else {
                            m.setSeleccionadoDocente(Boolean.FALSE);
                        }
                        
                        if(app.getPlazaSeleccionado() != null){
                            m.setSeleccionadoOtraPlaza(Boolean.TRUE);
                        }else{
                            m.setSeleccionadoOtraPlaza(Boolean.FALSE);
                        }
                        
                        listaMatriz.add(m);
                    }
                }
                
                Collections.sort(listaMatriz,(o1,o2)->(o1.getAplicacion().getAplMenorFechaGraduacion()==null && o2.getAplicacion().getAplMenorFechaGraduacion()==null)?1:
                        (o1.getAplicacion().getAplMenorFechaGraduacion()==null && o2.getAplicacion().getAplMenorFechaGraduacion()!=null)?1:
                        (o1.getAplicacion().getAplMenorFechaGraduacion()!=null && o2.getAplicacion().getAplMenorFechaGraduacion()==null)?-1:
                                o2.getAplicacion().getAplMenorFechaGraduacion().compareTo(o1.getAplicacion().getAplMenorFechaGraduacion()));
               if(aplPlazaPrimerElemento!=null){
                   List<SgMatrizComparativa> listPrimeros=new ArrayList();
                   for(SgMatrizComparativa mat: listaMatriz){
                       if(mat.getAplicacion().equals(aplPlazaPrimerElemento)){
                           listPrimeros.add(mat);
                       }
                   }
                   if(!listPrimeros.isEmpty()){
                       listaMatriz.removeAll(listPrimeros);
                       listPrimeros.addAll(listaMatriz);
                       listaMatriz=listPrimeros;
                   }
               }
            }
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
        }
    }
    
    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio fCod = new FiltroMunicipio();
                fCod.setOrderBy(new String[]{"munNombre"});
                fCod.setAscending(new boolean[]{true});
                fCod.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fCod.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());          
                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fCod);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fCod = new FiltroCodiguera();
            fCod.setOrderBy(new String[]{"depNombre"});
            fCod.setAscending(new boolean[]{true});
            fCod.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fCod);
            comboDepartamento = new SofisComboG(new ArrayList(departamentos), "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroCodiguera fMot = new FiltroCodiguera();
            fMot.setHabilitado(Boolean.TRUE);
            fMot.setOrderBy(new String[]{"mspOrden"});
            fMot.setAscending(new boolean[]{true});
            motivosSeleccion = restCatalogo.buscarMotivosSeleccion(fMot);
            
            FiltroCodiguera fCalidad = new FiltroCodiguera();
            fCalidad.setOrderBy(new String[]{"ciaNombre"});
            fCalidad.setAscending(new boolean[]{false});
            fCalidad.setIncluirCampos(new String[]{"ciaNombre", "ciaVersion", "ciaPk"});
            List<SgCalidadIngresoAplicantes> calidadAplicantes = restCatalogo.buscarIngresoAplicantePlaza(fCalidad);
            comboCalidadAplicante = new SofisComboG(new ArrayList(calidadAplicantes), "ciaNombre");
            comboCalidadAplicante.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));            
            
            
        } catch (Exception ex) {
            Logger.getLogger(MatrizComparativaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<SgEspecialidad> completeEspecialidad(String query) {
        try {
            FiltroEspecialidad fesp = new FiltroEspecialidad();
            fesp.setHabilitado(Boolean.TRUE);
            fesp.setNombre(query);
            fesp.setAscending(new boolean[]{true});
            fesp.setOrderBy(new String[]{"espNombre"});
            fesp.setIncluirCampos(new String[]{"espNombre", "espVersion"});
            fesp.setMaxResults(11L);
            return especialidades!= null
                    ? restCatalogo.buscarEspecialidad(fesp).stream()
                            .filter(i -> !this.especialidades.contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarEspecialidad(fesp);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgDiscapacidad> completeDiscapacidad(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"disNombre"});
            fil.setAscending(new boolean[]{false});
            return discapacidades != null
                    ? restCatalogo.buscarDiscapacidad(fil).stream()
                            .filter(i -> !this.discapacidades.contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarDiscapacidad(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    private void limpiarCombos() {
        cargarCombos();
    }

    public String limpiar() {
        filtro = new FiltroAplicacionPlaza();
        especialidades = new ArrayList<>();
        discapacidades = new ArrayList<>();
        motivosSeleccion = new ArrayList<>();
        limpiarCombos();
        buscar();
        return null;
    }

    public void seleccionarDocente(Long psePk) {
        for (SgMatrizComparativa m : listaMatriz) {
            if (m.getAplicacion().getAplPersonal().getPsePk().equals(psePk)) {
                m.setSeleccionadoDocente(Boolean.TRUE);
            } else {
                m.setSeleccionadoDocente(Boolean.FALSE);
            }
        }
    }

    public void actualizarAplicacion(SgAplicacionPlaza aplicacion, Boolean editar){
        aplicacionEnEdicion= (SgAplicacionPlaza)SerializationUtils.clone(aplicacion);
        this.editarPublicacion=editar;
    }
    
    public void obtenerPlazaSeleccionado(SgAplicacionPlaza aplicacion){
        plazaSeleccionada = aplicacion.getPlazaSeleccionado().getAplPlaza();
    }
    
    public void guardarAplicacionConDocenteSeleccionado(){
        try {
            aplicacionEnEdicion.setAplSeleccionadoEnMatriz(Boolean.TRUE);
            restAplicaciones.guardarSeleccionadoMatriz(aplicacionEnEdicion);
            existeDocenteSeleccionado=Boolean.TRUE;
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('confirmSeleccionar').hide()");
            PrimeFaces.current().ajax().update("form");
            aplicacionEnEdicion=null;
         } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void publicarSolicitud(){
        try {
            
            entidadEnEdicion=restClient.publicarSolicitud(entidadEnEdicion);            
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('confirmDialog').hide()");          
         } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public SgSolicitudPlaza getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudPlaza entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMatrizComparativa> getListaMatriz() {
        return listaMatriz;
    }

    public void setListaMatriz(List<SgMatrizComparativa> listaMatriz) {
        this.listaMatriz = listaMatriz;
    }

    public FiltroAplicacionPlaza getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAplicacionPlaza filtro) {
        this.filtro = filtro;
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

    //</editor-fold>
    public List<SgAplicacionPlaza> getListAplicacionPlaza() {
        return listAplicacionPlaza;
    }

    public void setListAplicacionPlaza(List<SgAplicacionPlaza> listAplicacionPlaza) {
        this.listAplicacionPlaza = listAplicacionPlaza;
    }

    public SgAplicacionPlaza getAplicacionEnEdicion() {
        return aplicacionEnEdicion;
    }

    public void setAplicacionEnEdicion(SgAplicacionPlaza aplicacionEnEdicion) {
        this.aplicacionEnEdicion = aplicacionEnEdicion;
    }

    public Boolean getExisteDocenteSeleccionado() {
        return existeDocenteSeleccionado;
    }

    public void setExisteDocenteSeleccionado(Boolean existeDocenteSeleccionado) {
        this.existeDocenteSeleccionado = existeDocenteSeleccionado;
    }

    public Boolean getEditarPublicacion() {
        return editarPublicacion;
    }

    public void setEditarPublicacion(Boolean editarPublicacion) {
        this.editarPublicacion = editarPublicacion;
    }

    public Boolean getPanelAvanzado() {
        return panelAvanzado;
    }

    public void setPanelAvanzado(Boolean panelAvanzado) {
        this.panelAvanzado = panelAvanzado;
    }

    public String getTxtFiltroAvanzado() {
        return txtFiltroAvanzado;
    }

    public void setTxtFiltroAvanzado(String txtFiltroAvanzado) {
        this.txtFiltroAvanzado = txtFiltroAvanzado;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public List<SgEspecialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<SgEspecialidad> especialidades) {
        this.especialidades = especialidades;
    }

    public List<SgDiscapacidad> getDiscapacidades() {
        return discapacidades;
    }

    public void setDiscapacidades(List<SgDiscapacidad> discapacidades) {
        this.discapacidades = discapacidades;
    }

    public List<SgMotivosSeleccionPLaza> getMotivosSeleccion() {
        return motivosSeleccion;
    }

    public void setMotivosSeleccion(List<SgMotivosSeleccionPLaza> motivosSeleccion) {
        this.motivosSeleccion = motivosSeleccion;
    }

    public SgSolicitudPlaza getPlazaSeleccionada() {
        return plazaSeleccionada;
    }

    public void setPlazaSeleccionada(SgSolicitudPlaza plazaSeleccionada) {
        this.plazaSeleccionada = plazaSeleccionada;
    }

    public String getMensajeWarningDocentes() {
        return mensajeWarningDocentes;
    }

    public void setMensajeWarningDocentes(String mensajeWarningDocentes) {
        this.mensajeWarningDocentes = mensajeWarningDocentes;
    }

    public SofisComboG<SgCalidadIngresoAplicantes> getComboCalidadAplicante() {
        return comboCalidadAplicante;
    }

    public void setComboCalidadAplicante(SofisComboG<SgCalidadIngresoAplicantes> comboCalidadAplicante) {
        this.comboCalidadAplicante = comboCalidadAplicante;
    }
  
}
