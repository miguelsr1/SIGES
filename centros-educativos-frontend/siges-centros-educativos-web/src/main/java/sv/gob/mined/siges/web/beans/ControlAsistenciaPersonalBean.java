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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAsistenciaPersonal;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciaPersonal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsistenciaPersonalRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaPersonalCabezalRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgDatoContratacion;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoInasistenciaPersonal;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDatoContratacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ControlAsistenciaPersonalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ControlAsistenciaPersonalBean.class.getName());

    @Inject
    private ControlAsistenciaPersonalCabezalRestClient restClient;

    @Inject
    private PersonalSedeEducativaRestClient restPersonal;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private AsistenciaPersonalRestClient asistenciaClient;

    @Inject
    private DatoContratacionRestClient restContratos;

    @Inject
    @Param(name = "sedeId")
    private Long sedeId;

    @Inject
    @Param(name = "id")
    private Long controlAsisId;

    @Inject
    @Param(name = "rev")
    private Long controlAsisRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private SessionBean sessionBean;

    private Boolean soloLectura = Boolean.FALSE;
    private SgControlAsistenciaPersonalCabezal entidadEnEdicion;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;

    private SgProgramaEducativo programaEd;
    private SofisComboG<SgMotivoInasistenciaPersonal>[] combosInasistencia;
    private List<SgMotivoInasistenciaPersonal> motivosInasistencia;
    private Boolean controlRealizado = Boolean.FALSE;
    private SgSede sedeSeleccionada;

    public ControlAsistenciaPersonalBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (controlAsisId != null && controlAsisId > 0) {
                if (controlAsisRev != null && controlAsisRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(controlAsisId, controlAsisRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(controlAsisId));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else {
                this.agregar();
                if (sedeId != null && sedeId > 0) {
                    cargarAsistenciaPersonal(restSede.obtenerPorId(sedeId));
                }
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getCpcPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public String getTituloPagina() {
        if (this.controlAsisId == null) {
            return Etiquetas.getValue("agregarControlAsistenciaPersonal");
        } else if (this.controlAsisRev != null) {
            return Etiquetas.getValue("historialControlAsistenciaPersonal");
            //return Etiquetas.getValue("historialControlAsistenciaPersonal") + " " + entidadEnEdicion.getCacSeccion().getSecServicioEducativo().getSduGrado().getGraNombre()+"-"+seccionSelecionado.getSecNombre() entidadEnEdicion.getCacFecha() + " (" + entidadEnEdicion.getSedUltModUsuario() + (entidadEnEdicion.getSedUltModFecha() != null ? (" " + this.appBean.getDateTimeFormater().format(entidadEnEdicion.getSedUltModFecha())) : "") + ")";
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verControlAsistenciaPersonal") + " " + entidadEnEdicion.getCpcSede().getSedNombre();
        } else {
            return Etiquetas.getValue("edicionControlAsistenciaPersonal") + " " + entidadEnEdicion.getCpcSede().getSedNombre();
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"mipNombre"});
            motivosInasistencia = restCatalogo.buscarMotivoInasistenciaPersonal(fc);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgControlAsistenciaPersonalCabezal();
        entidadEnEdicion.setCpcPersonalAusentesJustificados(0);
        entidadEnEdicion.setCpcPersonalAusentesSinJustificar(0);
    }

    public void dateSelect(SelectEvent event) {
        try {
            entidadEnEdicion.setCpcPk(null);
            this.cargarAsistenciaPersonal(this.entidadEnEdicion.getCpcSede());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarSede() {
        if(entidadEnEdicion==null){
            entidadEnEdicion = new SgControlAsistenciaPersonalCabezal();
        }
        entidadEnEdicion.setCpcSede(sedeSeleccionada);
    }

    public void cargarAsistenciaPersonal(SgSede sed) {
        try {

            this.entidadEnEdicion.setCpcSede(sed);

            if (entidadEnEdicion.getCpcSede() != null) {
                if (entidadEnEdicion.getCpcFecha() != null) {
                    
                    FiltroControlAsistenciaPersonalCabezal filtro = new FiltroControlAsistenciaPersonalCabezal();
                    filtro.setCpcSede(entidadEnEdicion.getCpcSede().getSedPk());
                    filtro.setCpcFecha(entidadEnEdicion.getCpcFecha());
                    filtro.setIncluirCampos(new String[]{"cpcVersion"});
                    
                    List<SgControlAsistenciaPersonalCabezal> listControl = restClient.buscar(filtro);

                    if (!listControl.isEmpty()) {
                        this.actualizar(restClient.obtenerPorId(listControl.get(0).getCpcPk()));
                    } else {
                        this.controlRealizado = Boolean.FALSE;
                        this.entidadEnEdicion.setCpcAsistenciaPersonal(new ArrayList<>());
                        this.entidadEnEdicion.setCpcPersonalAusentesJustificados(0);
                        this.entidadEnEdicion.setCpcPersonalAusentesSinJustificar(0);

                        
                        
                        List<SgDatoContratacion> contratos = buscarContratos();
                        combosInasistencia = new SofisComboG[contratos.size()];
                        
                        for(int i = 0; i<contratos.size() ; i++){
                            SgDatoContratacion contrato = contratos.get(i);
                            SgAsistenciaPersonal asis = new SgAsistenciaPersonal();
                            asis.setApePersonal(contrato.getDcoDatoEmpleado().getDemPersonalSede());
                            asis.setApeCargo(contrato.getDcoCargo());
                            this.entidadEnEdicion.getCpcAsistenciaPersonal().add(asis);
                            
                            
                            if(contrato.getDcoCargo()!=null){
                                
                                List<SgMotivoInasistenciaPersonal> agregar = new ArrayList<>();
                                for(SgMotivoInasistenciaPersonal mot : this.motivosInasistencia){
                                    if(mot.getMipCargos().contains(contrato.getDcoCargo())){
                                        agregar.add(mot);
                                    }
                                }
                                combosInasistencia[i] = new SofisComboG(agregar, "mipNombre");
                            }else{
                                combosInasistencia[i] = new SofisComboG();
                            }
                            combosInasistencia[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        }
                    }
                }
            } else {
                this.agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private List<SgDatoContratacion> buscarContratos() throws Exception {
        FiltroDatoContratacion filtroContratos = new FiltroDatoContratacion();
        filtroContratos.setDcoCentroEducativo(entidadEnEdicion.getCpcSede().getSedPk());
        filtroContratos.setIncluirCampos(new String[]{"dcoDatoEmpleado.demPersonalSede.psePersona.perNip",
            "dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerNombre", "dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoNombre", "dcoDatoEmpleado.demPersonalSede.psePersona.perNombreBusqueda",
            "dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerApellido", "dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoApellido", "dcoDatoEmpleado.demPersonalSede.psePersona.perFechaNacimiento",
            "dcoVersion", 
            "dcoDatoEmpleado.demPersonalSede.psePk", "dcoDatoEmpleado.demPersonalSede.pseVersion", 
            "dcoCargo.carNombre", "dcoCargo.carPk", "dcoCargo.carVersion", "dcoVersion"});
        filtroContratos.setOrderBy(new String[]{"dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerApellido"});
        filtroContratos.setAscending(new boolean[]{true});
        return restContratos.buscar(filtroContratos);
    }

    private List<SgPersonalSedeEducativa> buscarPersonal() throws Exception {
        FiltroPersonalSedeEducativa filtroPersonal = new FiltroPersonalSedeEducativa();
        filtroPersonal.setPerCentroEducativo(entidadEnEdicion.getCpcSede().getSedPk());
        filtroPersonal.setIncluirCampos(new String[]{"psePersona.perNip",
            "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perNombreBusqueda",
            "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perFechaNacimiento",
            "pseVersion"});
        filtroPersonal.setOrderBy(new String[]{"psePersona.perPrimerApellido"});
        filtroPersonal.setAscending(new boolean[]{true});
        return restPersonal.buscar(filtroPersonal);
    }

    private Long countPersonal() throws Exception {
        FiltroDatoContratacion filtroPersonal = new FiltroDatoContratacion();
        filtroPersonal.setDcoCentroEducativo(entidadEnEdicion.getCpcSede().getSedPk());
        return restContratos.buscarTotal(filtroPersonal);
    }

    public void inasistenciaSelected(SgAsistenciaPersonal asist, Integer rowIndex) {
        asist.setApeMotivoInasistencia(this.combosInasistencia[rowIndex].getSelectedT());
    }

    public void actualizar(SgControlAsistenciaPersonalCabezal cac) {
        try {
            if (cac == null) {
                this.agregar();
            } else {
                entidadEnEdicion = cac;
                controlRealizado = Boolean.TRUE;
                                
                FiltroAsistenciaPersonal fil = new FiltroAsistenciaPersonal();
                fil.setApeCabezal(this.entidadEnEdicion.getCpcPk());
                fil.setIncluirCampos(new String[]{
                    "apePersonal.psePk",
                    "apePersonal.psePersona.perNip",
                    "apePersonal.psePersona.perPrimerNombre", "apePersonal.psePersona.perSegundoNombre", "apePersonal.psePersona.perNombreBusqueda",
                    "apePersonal.psePersona.perPrimerApellido", "apePersonal.psePersona.perSegundoApellido", "apePersonal.psePersona.perFechaNacimiento",
                    "apePersonal.pseVersion",
                    "apeControl.cpcPk",
                    "apeControl.cpcVersion",
                    "apeObservacion",
                    "apeInasistencia",
                    "apeCargo.carPk",
                    "apeCargo.carNombre",
                    "apeCargo.carVersion",
                    "apeMotivoInasistencia.mipPk",
                    "apeMotivoInasistencia.mipNombre",
                    "apeMotivoInasistencia.mipCodigo",
                    "apeMotivoInasistencia.mipMotivoJustificado",
                    "apeMotivoInasistencia.mipVersion",
                    "apeVersion"});
                fil.setOrderBy(new String[]{"apePersonal.psePersona.perNombreBusqueda"});
                fil.setAscending(new boolean[]{true});
                entidadEnEdicion.setCpcAsistenciaPersonal(asistenciaClient.buscar(fil));
                
                Long cantPersonal = countPersonal();
                
                if (entidadEnEdicion.getCpcAsistenciaPersonal().size() < cantPersonal) {
                    List<SgDatoContratacion> listContratos = buscarContratos();
                    List<SgPersonalSedeEducativa> personalControlados = this.entidadEnEdicion.getCpcAsistenciaPersonal().stream().map(a -> a.getApePersonal()).collect(Collectors.toList());
                    for (int i = 0; i < listContratos.size(); i++) {
                        SgPersonalSedeEducativa per = listContratos.get(i).getDcoDatoEmpleado().getDemPersonalSede();
                        SgCargo contrato = listContratos.get(i).getDcoCargo();
                        if (!personalControlados.contains(per)) {
                            SgAsistenciaPersonal sgAsi = new SgAsistenciaPersonal();
                            sgAsi.setApePersonal(per);
                            sgAsi.setApeCargo(contrato);
                            this.entidadEnEdicion.getCpcAsistenciaPersonal().add(sgAsi);
                        }
                    }
                }
                                    
                combosInasistencia = new SofisComboG[entidadEnEdicion.getCpcAsistenciaPersonal().size()];
                for (int i = 0; i < entidadEnEdicion.getCpcAsistenciaPersonal().size(); i++) {
                    SgAsistenciaPersonal as = entidadEnEdicion.getCpcAsistenciaPersonal().get(i);
                    
                    
                    if(as.getApeCargo()!=null){

                        List<SgMotivoInasistenciaPersonal> agregar = new ArrayList<>();
                        for(SgMotivoInasistenciaPersonal mot : this.motivosInasistencia){
                            if(mot.getMipCargos().contains(as.getApeCargo())){
                                agregar.add(mot);
                            }
                        }
                        combosInasistencia[i] = new SofisComboG(agregar, "mipNombre");
                    }else{
                        combosInasistencia[i] = new SofisComboG();
                    }
                    combosInasistencia[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    
                    
                    
                    if(as.getApeCargo()!=null){
                        combosInasistencia[i].setSelectedT(as.getApeMotivoInasistencia());
                    }
                }

            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            cargarAsistenciaPersonal(entidadEnEdicion.getCpcSede());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public ControlAsistenciaPersonalCabezalRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ControlAsistenciaPersonalCabezalRestClient restClient) {
        this.restClient = restClient;
    }

    public PersonalSedeEducativaRestClient getRestPersonal() {
        return restPersonal;
    }

    public void setRestPersonal(PersonalSedeEducativaRestClient restPersonal) {
        this.restPersonal = restPersonal;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public AsistenciaPersonalRestClient getAsistenciaClient() {
        return asistenciaClient;
    }

    public void setAsistenciaClient(AsistenciaPersonalRestClient asistenciaClient) {
        this.asistenciaClient = asistenciaClient;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public Long getControlAsisId() {
        return controlAsisId;
    }

    public void setControlAsisId(Long controlAsisId) {
        this.controlAsisId = controlAsisId;
    }

    public Long getControlAsisRev() {
        return controlAsisRev;
    }

    public void setControlAsisRev(Long controlAsisRev) {
        this.controlAsisRev = controlAsisRev;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgControlAsistenciaPersonalCabezal getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgControlAsistenciaPersonalCabezal entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public SgProgramaEducativo getProgramaEd() {
        return programaEd;
    }

    public void setProgramaEd(SgProgramaEducativo programaEd) {
        this.programaEd = programaEd;
    }

    public SofisComboG<SgMotivoInasistenciaPersonal>[] getCombosInasistencia() {
        return combosInasistencia;
    }

    public void setCombosInasistencia(SofisComboG<SgMotivoInasistenciaPersonal>[] combosInasistencia) {
        this.combosInasistencia = combosInasistencia;
    }

    public List<SgMotivoInasistenciaPersonal> getMotivosInasistencia() {
        return motivosInasistencia;
    }

    public void setMotivosInasistencia(List<SgMotivoInasistenciaPersonal> motivosInasistencia) {
        this.motivosInasistencia = motivosInasistencia;
    }

    public Boolean getControlRealizado() {
        return controlRealizado;
    }

    public void setControlRealizado(Boolean controlRealizado) {
        this.controlRealizado = controlRealizado;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

}
