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
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgConsultaGuardada;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.enumerados.EnumConsultaGuardada;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConsultaGuardada;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.restclient.ConsultaGuardadaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ObjectMapperContextResolver;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudiantesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudiantesBean.class.getName());

    @Inject
    private EstudianteRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private ConsultaGuardadaRestClient restConsultaGuardada;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    private SedeRestClient restSede;

    @Inject
    @Param(name = "sedbpk")
    private Long sedePk;

    private FiltroEstudiante filtro = new FiltroEstudiante();
    private LazyEstudianteDataModel estudianteLazyModel;
    private List<RevHistorico> historial = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgSexo> comboSexos;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SofisComboG<SgConsultaGuardada> comboConsultaGuardada;
    private SgMatricula matriculaAnular;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado;
    private Boolean buscarMatAbierta = Boolean.TRUE;
    private Boolean buscarSoloRetirados = Boolean.FALSE;

    private SgConsultaGuardada consultaGuardada = new SgConsultaGuardada();
    private SofisComboG<SgDepartamento> comboDepartamentoMatricula;
    private SofisComboG<SgMunicipio> comboMunicipioMatricula;
    private SgEstudiante entidadEnEdicion;

    private SofisComboG<EnumAmbito> comboAmbito;

    public EstudiantesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
            matriculaAnular = new SgMatricula();
            filtro.setHabilitado(Boolean.TRUE);
            validarAcceso();
            cargarCombos();

            if (sessionBean.getAmbitosSeleccionablesBusqueda() != null && sessionBean.getAmbitosSeleccionablesBusqueda().size() > 1) {
                comboAmbito = new SofisComboG(sessionBean.getAmbitosSeleccionablesBusqueda(), "text");
            }

            // si hay param de sede, cargo sede en bsqueda avanzada.
            if (this.sedePk != null) {
                FiltroSedes filtro = new FiltroSedes();
                filtro.setSedPk(sedePk);
                filtro.setIncluirCampos(new String[]{"sedPk", "sedTipo", "sedVersion", "sedCodigo", "sedNombre"});
                filtro.setMaxResults(1L);
                List<SgSede> listaSedes = restSede.buscar(filtro);
                if (listaSedes.size() > 0) {
                    SgSede sede = listaSedes.get(0);
                    verPanelAvanzado(); // inicializa pnl
                    this.filtrosSeccion.cargarSedeSeleccionada(sede);
                    buscar();
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTUDIANTES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void seleccionarMatricula(Long id) {
        try {
            matriculaAnular = restMatricula.obtenerPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void anularRetiro() {
        try {
            restMatricula.anularRetiro(matriculaAnular);
            buscar();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"estPersona.perNie",
                "estPersona.perPk", "estPersona.perDireccion.dirPk",
                "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido", "estPersona.perFechaNacimiento",
                "estPersona.perSexo.sexNombre", "estHabilitado", "estPersona.perHabilitado",
                "estUltimaMatricula.matMotivoRetiro.mreDefinitivo", "estUltimaMatricula.matPk",
                "estUltimaMatricula.matEstado", "estUltimaMatricula.matMotivoRetiro.mreCambioSecc",
                "estUltimaMatricula.matMotivoRetiro.mreTraslado", "estUltimaMatricula.matSeccion.secPk"});

            if (comboAmbito != null) {
                filtro.setAmbito(comboAmbito.getSelectedT());
            }

            filtro.setEstDepartamentoMatricula(comboDepartamentoMatricula != null ? comboDepartamentoMatricula.getSelectedT() != null ? comboDepartamentoMatricula.getSelectedT().getDepPk() : null : null);
            filtro.setEstMunicipioMatricula(comboMunicipioMatricula != null ? comboMunicipioMatricula.getSelectedT() != null ? comboMunicipioMatricula.getSelectedT().getMunPk() : null : null);
            filtro.setPerSexoPk(comboSexos.getSelectedT() != null ? comboSexos.getSelectedT().getSexPk() : null);
            filtro.setPerDepartamentoPk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setPerMunicipioPk(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setEstEstadoMatricula(buscarMatAbierta ? EnumMatriculaEstado.ABIERTO : null);
            filtro.setHabilitado(!buscarSoloRetirados);
            totalResultados = restClient.buscarTotal(filtro);

            estudianteLazyModel = new LazyEstudianteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDepartamentoMatricula = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentoMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"sexNombre"});
            fc.setIncluirCampos(new String[]{"sexNombre", "sexVersion"});
            List<SgSexo> sexos = restCatalogo.buscarSexo(fc);
            comboSexos = new SofisComboG(sexos, "sexNombre");
            comboSexos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipioMatricula = new SofisComboG();
            comboMunicipioMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboConsultaGuardada = new SofisComboG();
            comboConsultaGuardada.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void departamentoElegido() {
        try {
            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio fm = new FiltroMunicipio();
                fm.setOrderBy(new String[]{"munNombre"});
                fm.setAscending(new boolean[]{true});
                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fm.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());

                List<SgMunicipio> listMunicipio = restCatalogo.buscarMunicipio(fm);
                comboMunicipio = new SofisComboG(listMunicipio, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboMunicipio.ordenar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void departamentoElegidoMatricula() {
        try {
            if (comboDepartamentoMatricula.getSelectedT() != null) {
                FiltroMunicipio fm = new FiltroMunicipio();
                fm.setOrderBy(new String[]{"munNombre"});
                fm.setAscending(new boolean[]{true});
                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fm.setMunDepartamentoFk(comboDepartamentoMatricula.getSelectedT().getDepPk());

                List<SgMunicipio> listMunicipio = restCatalogo.buscarMunicipio(fm);
                comboMunicipioMatricula = new SofisComboG(listMunicipio, "munNombre");
                comboMunicipioMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboMunicipioMatricula.ordenar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboSexos.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        comboDepartamentoMatricula.setSelected(-1);
        comboMunicipio = new SofisComboG();
        comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboMunicipioMatricula = new SofisComboG();
        comboMunicipioMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (this.filtrosSeccion.getFiltro() != null) {
            this.filtrosSeccion.limpiarCombos();
        }
    }

    public void agregarConsultaGuardada() {
        if (comboConsultaGuardada.isEmpty()) {
            cargarConsultaGuardada();
        }
        comboConsultaGuardada.setSelected(-1);
        consultaGuardada = new SgConsultaGuardada();
    }

    public void buscarSoloMatAbiertaSelected() {
        this.buscarSoloRetirados = Boolean.FALSE;
    }

    public void buscarSoloRetiradosSelected() {
        this.buscarMatAbierta = Boolean.FALSE;
    }

    public void cargarConsultaGuardada() {
        try {
            FiltroConsultaGuardada fcg = new FiltroConsultaGuardada();
            fcg.setCgrFiltro(EnumConsultaGuardada.ESTUDIANTE);
            fcg.setUsuCodigo(sessionBean.getEntidadUsuario().getUsuCodigo());
            fcg.setAscending(new boolean[]{true});
            fcg.setOrderBy(new String[]{"cgrNombre"});
            fcg.setIncluirCampos(new String[]{"cgrNombre"});
            List<SgConsultaGuardada> listConsulta = restConsultaGuardada.buscar(fcg);
            comboConsultaGuardada = new SofisComboG(listConsulta, "cgrNombre");
            comboConsultaGuardada.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String actualizarConsulta() {
        if (comboConsultaGuardada.getSelectedT() != null) {
            try {

                SgConsultaGuardada cons = restConsultaGuardada.obtenerPorId(comboConsultaGuardada.getSelectedT().getCgrPk());

                ObjectMapperContextResolver contextResolver = new ObjectMapperContextResolver();
                filtro = contextResolver.getDefaultMapper().readValue(cons.getCgrConsulta(), FiltroEstudiante.class);
                filtrosSeccion.setFiltro(this.filtro);
                filtrosSeccion.cargarCombosDeFiltro();
                if (filtro.getPerSexoPk() != null) {
                    comboSexos.setSelected(filtro.getPerSexoPk().hashCode());
                }
                if (filtro.getPerDepartamentoPk() != null) {
                    comboDepartamentos.setSelected(filtro.getPerDepartamentoPk().hashCode());
                }
                departamentoElegido();
                if (filtro.getPerMunicipioPk() != null) {
                    comboMunicipio.setSelected(filtro.getPerMunicipioPk().hashCode());
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        }
        PrimeFaces.current().executeScript("PF('consultaGuardada').hide()");
        return null;
    }

    public void eliminarConsultaGuardada() {
        try {
            if (comboConsultaGuardada.getSelectedT() != null) {
                restConsultaGuardada.eliminar(comboConsultaGuardada.getSelectedT().getCgrPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                cargarConsultaGuardada();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String guardarConsulta() {
        try {
            ObjectMapperContextResolver contextResolver = new ObjectMapperContextResolver();

            consultaGuardada.setCgrConsulta(contextResolver.getDefaultMapper().writeValueAsString(filtro));
            consultaGuardada.setCgrUsuario(sessionBean.getEntidadUsuario().getUsuCodigo());
            consultaGuardada.setCgrFiltro(EnumConsultaGuardada.ESTUDIANTE);
            restConsultaGuardada.guardar(consultaGuardada);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            cargarConsultaGuardada();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;

    }

    public String limpiar() {
        limpiarCombos();
        filtro = new FiltroEstudiante();
        buscarMatAbierta = Boolean.FALSE;
        filtro.setHabilitado(Boolean.TRUE);
        filtroNombreCompleto = new FiltroNombreCompleto();
        estudianteLazyModel = null;
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
        inicializarFiltrosSeccion();
    }

    public void inicializarFiltrosSeccion() {
        if (this.filtrosSeccion.getFiltro() == null) {
            this.filtrosSeccion.setFiltro(this.filtro);
            this.filtrosSeccion.cargarCombos();
        }
    }

    public String cargarHistorial(Long id) {
        try {
            historial = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void filtroNombreCompletoSeleccionar(FiltroNombreCompleto filtroNombre) {
        if (filtroNombre != null) {
            filtro.setPerPrimerNombre(filtroNombre.getPerPrimerNombre());
            filtro.setPerSegundoNombre(filtroNombre.getPerSegundoNombre());
            filtro.setPerTercerNombre(filtroNombre.getPerTercerNombre());
            filtro.setPerPrimerApellido(filtroNombre.getPerPrimerApellido());
            filtro.setPerSegundoApellido(filtroNombre.getPerSegundoApellido());
            filtro.setPerTercerApellido(filtroNombre.getPerTercerApellido());
            if (!StringUtils.isBlank(filtroNombre.getNombreCompleto())) {
                filtro.setPerNombreCompleto(null);
            }
        }
        PrimeFaces.current().ajax().update("form:pnlSearch");
    }

    public void actualizar(SgEstudiante est) {
        entidadEnEdicion = est;
    }

    public void prepararParaEliminar(SgEstudiante est) {
        try {            
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = est;
            if (entidadEnEdicion.getEstPk() != null) {
                FiltroMatricula fm=new FiltroMatricula();
                fm.setMatEstudiantePk(entidadEnEdicion.getEstPk());
                fm.setMatEstado(EnumMatriculaEstado.ABIERTO);
                fm.setIncluirCampos(new String[]{"matVersion"});
                List<SgMatricula> matList=restMatricula.buscar(fm);
                
                if(matList!=null && !matList.isEmpty()){
                    JSFUtils.agregarWarn("eliminarMsg", "ADVERTENCIA: El estudiante que desea eliminar tiene matrícula abierta, ¿desea continuar?.", "");
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEstPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            PrimeFaces.current().executeScript("PF('confirmDialogEliminarEst').hide()");
            entidadEnEdicion = null;
            
        } catch (BusinessException be) {
            JSFUtils.limpiarMensajesError();
            JSFUtils.agregarMensajes("pupupMsg2", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstudiante filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
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

    public LazyEstudianteDataModel getEstudianteLazyModel() {
        return estudianteLazyModel;
    }

    public void setEstudianteLazyModel(LazyEstudianteDataModel estudianteLazyModel) {
        this.estudianteLazyModel = estudianteLazyModel;
    }

    public SofisComboG<SgSexo> getComboSexos() {
        return comboSexos;
    }

    public void setComboSexos(SofisComboG<SgSexo> comboSexos) {
        this.comboSexos = comboSexos;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SgMatricula getMatriculaAnular() {
        return matriculaAnular;
    }

    public void setMatriculaAnular(SgMatricula matriculaAnular) {
        this.matriculaAnular = matriculaAnular;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
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

    public Boolean getBuscarMatAbierta() {
        return buscarMatAbierta;
    }

    public void setBuscarMatAbierta(Boolean buscarMatAbierta) {
        this.buscarMatAbierta = buscarMatAbierta;
    }

    public SofisComboG<SgConsultaGuardada> getComboConsultaGuardada() {
        return comboConsultaGuardada;
    }

    public void setComboConsultaGuardada(SofisComboG<SgConsultaGuardada> comboConsultaGuardada) {
        this.comboConsultaGuardada = comboConsultaGuardada;
    }

    public SgConsultaGuardada getConsultaGuardada() {
        return consultaGuardada;
    }

    public void setConsultaGuardada(SgConsultaGuardada consultaGuardada) {
        this.consultaGuardada = consultaGuardada;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoMatricula() {
        return comboDepartamentoMatricula;
    }

    public void setComboDepartamentoMatricula(SofisComboG<SgDepartamento> comboDepartamentoMatricula) {
        this.comboDepartamentoMatricula = comboDepartamentoMatricula;
    }

    public SofisComboG<SgMunicipio> getComboMunicipioMatricula() {
        return comboMunicipioMatricula;
    }

    public void setComboMunicipioMatricula(SofisComboG<SgMunicipio> comboMunicipioMatricula) {
        this.comboMunicipioMatricula = comboMunicipioMatricula;
    }

    public Boolean getBuscarSoloRetirados() {
        return buscarSoloRetirados;
    }

    public void setBuscarSoloRetirados(Boolean buscarSoloRetirados) {
        this.buscarSoloRetirados = buscarSoloRetirados;
    }

    public SofisComboG<EnumAmbito> getComboAmbito() {
        return comboAmbito;
    }

    public void setComboAmbito(SofisComboG<EnumAmbito> comboAmbito) {
        this.comboAmbito = comboAmbito;
    }

}
