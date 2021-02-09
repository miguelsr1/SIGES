/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgProyectoInstEstudiante;
import sv.gob.mined.siges.web.dto.SgProyectoInstitucionalSede;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgAsociacion;
import sv.gob.mined.siges.web.dto.catalogo.SgBeneficio;
import sv.gob.mined.siges.web.dto.catalogo.SgProyectoInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoBeneficio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucional;
import sv.gob.mined.siges.web.lazymodels.LazyProyectoInstEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.ProyectoInstEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.ProyectoInstitucionalRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;


@Named
@ViewScoped
public class ProyectoInstitucionalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProyectoInstitucionalBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ProyectoInstitucionalRestClient restProyecto;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private EstudianteRestClient restEstudiante;

    @Inject
    private ProyectoInstEstudianteRestClient restProyectoInstEstudiante;

    @Inject
    @Param(name = "idProIns")
    private Long proyectoId;
    
    @Inject
    @Param(name = "idSed")
    private Long sedId;
    
    @Inject
    private FiltrosSeccionBean filtrosSeccion;
    
    private SgSede sedeSeleccionada;
    private SgProyectoInstitucional proyectoSeleccionado;
    private SgBeneficio beneficioSeleccionado;
    private SgAsociacion asociacionSeleccionado;
    private SgProyectoInstitucionalSede proyectoSedeEnEdicion = new SgProyectoInstitucionalSede();
    private SofisComboG<EnumTipoBeneficio> comboTipoBeneficio;
    private SofisComboG<SgProyectoInstitucional> comboProyecto;
    private SofisComboG<SgBeneficio> comboBeneficio;
    private SofisComboG<SgAsociacion> comboAsociacion;
    private FiltroEstudiante filtroEstudiantes = new FiltroEstudiante();
    private List<SgEstudiante> estudiantesLista;
    private List<SgEstudiante> estudiantesSeleccionados;
    private LocalDate fechaOtorgado;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private List<SgProyectoInstEstudiante> listaOriginal;
    private FiltroProyectoInstEstudiante filtroProyectoEstudiantes = new FiltroProyectoInstEstudiante();
    private LazyProyectoInstEstudianteDataModel estudiantesLazy;
    private Integer paginado = 10;
    private Long totalEstudiantes;


    public ProyectoInstitucionalBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            
            if (proyectoId != null && proyectoId > 0) {
                this.actualizar(restProyecto.obtenerPorId(proyectoId));
            }else if (sedId != null && sedId > 0) {
                sedeSeleccionada = restSede.obtenerPorIdLazy(sedId);
            }else{
                JSFUtils.redirectToIndex();
            }
            
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_PROYECTO_INSTITUCIONAL_SEDE)) {
            JSFUtils.redirectToIndex();
        }

    }

    public void cargarProyecto() {
        try {
            if(comboTipoBeneficio.getSelectedT()!=null && comboProyecto.getSelectedT()!=null && comboBeneficio.getSelectedT()!=null){
                proyectoSeleccionado = comboProyecto.getSelectedT();
                beneficioSeleccionado = comboBeneficio.getSelectedT();
                asociacionSeleccionado = comboAsociacion.getSelectedT();
            }else{
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_DATOS_OBLIGATORIOS_VACIO), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarTipoBeneficio(){
        try{
                        
            comboProyecto = new SofisComboG();
            comboProyecto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboBeneficio = new SofisComboG();
            comboBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboAsociacion = new SofisComboG();
            comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if(comboTipoBeneficio.getSelectedT()!=null){
                FiltroProyectoInstitucional fpi = new FiltroProyectoInstitucional();
                fpi.setOrderBy(new String[]{"pinNombre"});
                fpi.setAscending(new boolean[]{true});
                fpi.setTipoBeneficio(comboTipoBeneficio.getSelectedT());
                fpi.setInicializarBeneficios(Boolean.TRUE);
                fpi.setInicializarAsociaciones(Boolean.TRUE);
                comboProyecto = new SofisComboG(restCatalogo.buscarProyecto(fpi), "pinNombre");
                comboProyecto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void seleccionarProyecto(){
        try{
            
            comboBeneficio = new SofisComboG();
            comboBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboAsociacion = new SofisComboG();
            comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if(comboProyecto.getSelectedT()!=null){
                List<SgBeneficio> listaBeneficios = new ArrayList<>(comboProyecto.getSelectedT().getPinBeneficios()).stream()
                        .filter(ben -> ben.getBnfTipoBeneficio().equals(comboTipoBeneficio.getSelectedT()))
                        .collect(Collectors.toList());
                
                comboBeneficio = new SofisComboG(listaBeneficios, "bnfNombre");
                comboBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                
                List<SgAsociacion> listaAsociacion = comboProyecto.getSelectedT().getPinAsociaciones();
                comboAsociacion = new SofisComboG(listaAsociacion, "asoNombre");
                comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscar() {
        try {
            filtroProyectoEstudiantes.setProyecto(proyectoSedeEnEdicion.getProPk());
            filtroProyectoEstudiantes.setIncluirCampos(new String[]{"piePk", "pieEstudiante.estPk", "pieEstudiante.estPersona.perNie",
                "pieEstudiante.estPersona.perPrimerNombre", "pieEstudiante.estPersona.perSegundoNombre", "pieEstudiante.estPersona.perTercerNombre",
                "pieEstudiante.estPersona.perPrimerApellido", "pieEstudiante.estPersona.perSegundoApellido", "pieEstudiante.estPersona.perTercerApellido", 
                "pieFechaOtorgado", "pieObservaciones"});
            
            totalEstudiantes = restProyectoInstEstudiante.buscarTotal(filtroProyectoEstudiantes);
            estudiantesLazy = new LazyProyectoInstEstudianteDataModel(restProyectoInstEstudiante, filtroProyectoEstudiantes, totalEstudiantes, paginado);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        
    }
    
    public void filtroNombreCompletoSeleccionar(FiltroNombreCompleto filtroNombre) {
        if (filtroNombre != null) {
            filtroProyectoEstudiantes.setPrimerNombre(filtroNombre.getPerPrimerNombre());
            filtroProyectoEstudiantes.setSegundoNombre(filtroNombre.getPerSegundoNombre());
            filtroProyectoEstudiantes.setTercerNombre(filtroNombre.getPerTercerNombre());
            filtroProyectoEstudiantes.setPrimerApellido(filtroNombre.getPerPrimerApellido());
            filtroProyectoEstudiantes.setSegundoApellido(filtroNombre.getPerSegundoApellido());
            filtroProyectoEstudiantes.setTercerApellido(filtroNombre.getPerTercerApellido());
        }
        PrimeFaces.current().ajax().update("form:pnlBusquedaEstudiante");
    }
    
    public String buscarEstudiantes() {
        try {
            
            if(filtroEstudiantes.getSecGradoFk()!=null){
                filtroEstudiantes.setIncluirCampos(new String[]{"estPk", "estPersona.perNie",
                    "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                    "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido", 
                    "estPersona.perFechaNacimiento", "estVersion", 
                    "estPersona.perSexo.sexNombre", "estHabilitado", "estPersona.perHabilitado"});

                filtroEstudiantes.setEstEstadoMatricula(EnumMatriculaEstado.ABIERTO);
                
                List<SgEstudiante> lista = restEstudiante.buscar(filtroEstudiantes);
                estudiantesLista = new ArrayList();
                
                for (SgEstudiante est : lista) {
                    if (!superaCantidadMaxima(listaOriginal, est, beneficioSeleccionado.getBnfCantidadAnual())) {
                        estudiantesLista.add(est);
                    }
                }
            }else{
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_FILTRO_GRADO_VACIO), "");
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void confirmarEstudiantes(){
        try{
            if(fechaOtorgado!=null){
                SgProyectoInstEstudiante nuevo;
                SgProyectoInstEstudiante[] listaGuardar = new SgProyectoInstEstudiante[estudiantesSeleccionados.size()];

                if (estudiantesSeleccionados != null) {
                    Integer contador = 0;
                    for (SgEstudiante est : estudiantesSeleccionados) {
                        nuevo = new SgProyectoInstEstudiante();
                        nuevo.setPieEstudiante(est);
                        nuevo.setPieProyectoInstitucional(proyectoSedeEnEdicion);
                        nuevo.setPieFechaOtorgado(fechaOtorgado);
                        listaOriginal.add(nuevo);
                        listaGuardar[contador] = nuevo;
                        contador++;
                    }
                }
                restProyectoInstEstudiante.guardarEstudiantes(listaGuardar);
                estudiantesSeleccionados = new ArrayList();
                buscar();
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            }else{
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_FECHA_OTORGADO_VACIO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    private  boolean superaCantidadMaxima(List<SgProyectoInstEstudiante> col, SgEstudiante op, Integer maximo){
        List<SgProyectoInstEstudiante> lista = new ArrayList<>(col).stream()
                        .filter(est -> est.getPieEstudiante().getEstPk().equals(op.getEstPk()))
                        .collect(Collectors.toList());
        if(lista!=null && lista.size()<maximo){
            return false;
        }
        
        return true;
    }

    public void cargarCombos() {
        try {
               
            List<EnumTipoBeneficio> tipos = new ArrayList(Arrays.asList(EnumTipoBeneficio.values()));
            comboTipoBeneficio = new SofisComboG(tipos, "text");
            comboTipoBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboProyecto = new SofisComboG();
            comboProyecto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboBeneficio = new SofisComboG();
            comboBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboAsociacion = new SofisComboG();
            comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            
            proyectoSedeEnEdicion.setProProyectoInstitucional(proyectoSeleccionado);
            proyectoSedeEnEdicion.setProBeneficio(beneficioSeleccionado);
            proyectoSedeEnEdicion.setProAsociacion(asociacionSeleccionado);
            proyectoSedeEnEdicion.setProSede(sedeSeleccionada);
            proyectoSedeEnEdicion = restProyecto.guardar(proyectoSedeEnEdicion);
            actualizar(proyectoSedeEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void nuevaBusqueda(){
        this.estudiantesSeleccionados = new ArrayList();
        this.fechaOtorgado = null;
        this.filtrosSeccion.limpiarCombos();
    }
    
    public void agregarEstudiante(){
        if (this.filtrosSeccion.getFiltro() == null) {
            this.filtrosSeccion.setFiltro(this.filtroEstudiantes);
            this.filtrosSeccion.setRenderSede(Boolean.FALSE);
            this.filtrosSeccion.setRenderProgramaOpcion(Boolean.TRUE);
            this.filtrosSeccion.setRenderSeccion(Boolean.TRUE);
            this.filtrosSeccion.setDisableAnioLectio(Boolean.TRUE);
            this.filtrosSeccion.cargarCombos();
            this.filtrosSeccion.seleccionarAnioLectivo(proyectoSeleccionado.getPinAnio());
            this.filtrosSeccion.cargarSedeSeleccionada(sedeSeleccionada);
        }
    }
    
    public void eliminarListaEstudiantes(){
        estudiantesLista = new ArrayList();
    }
    
    public void eliminarEstudiante(SgProyectoInstEstudiante var) {
        try {
            
            if(var.getPiePk()!=null){
                restProyectoInstEstudiante.eliminar(var.getPiePk());
            }
            listaOriginal.remove(var);
            buscar();
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    private void limpiarCombos() {
    }

    public void limpiar() {
        filtroNombreCompleto = new FiltroNombreCompleto();
        filtroProyectoEstudiantes = new FiltroProyectoInstEstudiante();
        limpiarCombos();
        buscar();
    }

    public void actualizar(SgProyectoInstitucionalSede var) {
        limpiarCombos();
        proyectoSedeEnEdicion = (SgProyectoInstitucionalSede) SerializationUtils.clone(var);
        
        proyectoSeleccionado = proyectoSedeEnEdicion.getProProyectoInstitucional();
        beneficioSeleccionado = proyectoSedeEnEdicion.getProBeneficio();
        asociacionSeleccionado = proyectoSedeEnEdicion.getProAsociacion();
        sedeSeleccionada = proyectoSedeEnEdicion.getProSede();
        
        if(beneficioSeleccionado.getIsTipoEstudiante()){
            agregarEstudiante();
            
            try{
                filtroProyectoEstudiantes = new FiltroProyectoInstEstudiante();
                filtroProyectoEstudiantes.setProyecto(proyectoSedeEnEdicion.getProPk());
                filtroProyectoEstudiantes.setIncluirCampos(new String[]{"piePk", "pieEstudiante.estPk", "pieEstudiante.estPersona.perNie",
                    "pieEstudiante.estPersona.perPrimerNombre", "pieEstudiante.estPersona.perSegundoNombre", "pieEstudiante.estPersona.perTercerNombre",
                    "pieEstudiante.estPersona.perPrimerApellido", "pieEstudiante.estPersona.perSegundoApellido", "pieEstudiante.estPersona.perTercerApellido", 
                    "pieFechaOtorgado", "pieObservaciones"});

                listaOriginal = new ArrayList<>(restProyectoInstEstudiante.buscar(filtroProyectoEstudiantes));

                if(listaOriginal == null || listaOriginal.isEmpty()){
                    proyectoSedeEnEdicion.setProyectoInstEstudiante(new ArrayList());
                }

                buscar();
            } catch (BusinessException be) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        }
        
    }
    
    public void actualizarEstudiante(){
        try{
            SgProyectoInstEstudiante[] listaGuardar = estudiantesLazy.getWrappedData().stream().toArray(SgProyectoInstEstudiante[]::new);
            restProyectoInstEstudiante.guardarEstudiantes(listaGuardar);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

      
    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public SgProyectoInstitucional getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(SgProyectoInstitucional proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public SofisComboG<EnumTipoBeneficio> getComboTipoBeneficio() {
        return comboTipoBeneficio;
    }

    public void setComboTipoBeneficio(SofisComboG<EnumTipoBeneficio> comboTipoBeneficio) {
        this.comboTipoBeneficio = comboTipoBeneficio;
    }

    public SofisComboG<SgProyectoInstitucional> getComboProyecto() {
        return comboProyecto;
    }

    public void setComboProyecto(SofisComboG<SgProyectoInstitucional> comboProyecto) {
        this.comboProyecto = comboProyecto;
    }

    public SofisComboG<SgBeneficio> getComboBeneficio() {
        return comboBeneficio;
    }

    public void setComboBeneficio(SofisComboG<SgBeneficio> comboBeneficio) {
        this.comboBeneficio = comboBeneficio;
    }

    public SofisComboG<SgAsociacion> getComboAsociacion() {
        return comboAsociacion;
    }

    public void setComboAsociacion(SofisComboG<SgAsociacion> comboAsociacion) {
        this.comboAsociacion = comboAsociacion;
    }

    public SgBeneficio getBeneficioSeleccionado() {
        return beneficioSeleccionado;
    }

    public void setBeneficioSeleccionado(SgBeneficio beneficioSeleccionado) {
        this.beneficioSeleccionado = beneficioSeleccionado;
    }

    public SgAsociacion getAsociacionSeleccionado() {
        return asociacionSeleccionado;
    }

    public void setAsociacionSeleccionado(SgAsociacion asociacionSeleccionado) {
        this.asociacionSeleccionado = asociacionSeleccionado;
    }

    public SgProyectoInstitucionalSede getProyectoSedeEnEdicion() {
        return proyectoSedeEnEdicion;
    }

    public void setProyectoSedeEnEdicion(SgProyectoInstitucionalSede proyectoSedeEnEdicion) {
        this.proyectoSedeEnEdicion = proyectoSedeEnEdicion;
    }

    public List<SgEstudiante> getEstudiantesLista() {
        return estudiantesLista;
    }

    public void setEstudiantesLista(List<SgEstudiante> estudiantesLista) {
        this.estudiantesLista = estudiantesLista;
    }

    public List<SgEstudiante> getEstudiantesSeleccionados() {
        return estudiantesSeleccionados;
    }

    public void setEstudiantesSeleccionados(List<SgEstudiante> estudiantesSeleccionados) {
        this.estudiantesSeleccionados = estudiantesSeleccionados;
    }

    public LocalDate getFechaOtorgado() {
        return fechaOtorgado;
    }

    public void setFechaOtorgado(LocalDate fechaOtorgado) {
        this.fechaOtorgado = fechaOtorgado;
    }

    public FiltroEstudiante getFiltroEstudiantes() {
        return filtroEstudiantes;
    }

    public void setFiltroEstudiantes(FiltroEstudiante filtroEstudiantes) {
        this.filtroEstudiantes = filtroEstudiantes;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public List<SgProyectoInstEstudiante> getListaOriginal() {
        return listaOriginal;
    }

    public void setListaOriginal(List<SgProyectoInstEstudiante> listaOriginal) {
        this.listaOriginal = listaOriginal;
    }

    public FiltroProyectoInstEstudiante getFiltroProyectoEstudiantes() {
        return filtroProyectoEstudiantes;
    }

    public void setFiltroProyectoEstudiantes(FiltroProyectoInstEstudiante filtroProyectoEstudiantes) {
        this.filtroProyectoEstudiantes = filtroProyectoEstudiantes;
    }

    public LazyProyectoInstEstudianteDataModel getEstudiantesLazy() {
        return estudiantesLazy;
    }

    public void setEstudiantesLazy(LazyProyectoInstEstudianteDataModel estudiantesLazy) {
        this.estudiantesLazy = estudiantesLazy;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public void setTotalEstudiantes(Long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    
    

}
