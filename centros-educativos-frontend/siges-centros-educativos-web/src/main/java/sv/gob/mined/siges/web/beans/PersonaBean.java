/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgNacionalidad;
import sv.gob.mined.siges.web.dto.SgTelefono;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgEtnia;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTelefono;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.TelefonoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.IdentificacionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoFallecimiento;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPersona;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.SgIdentificacion;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgUnirPersona;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgReferenciasApoyo;
import sv.gob.mined.siges.web.dto.catalogo.SgTerapia;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTrastornoAprendizaje;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.restclient.AllegadoRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@ViewScoped
public class PersonaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PersonaBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long perId;

    @Inject
    @Param(name = "rev")
    private Boolean perRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private PersonaRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private TelefonoRestClient restTelefonoClient;

    @Inject
    private IdentificacionRestClient restIdentificacionClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private PersonalSedeEducativaRestClient restPersonal;

    @Inject
    private AllegadoRestClient restAllegado;

    @Inject
    private MatriculaComponenteBean matriculaComponenteBean;

    @Inject
    private SelloFirmaRestClient selloFirmaRestClient;
    
    @Inject
    private IdentificacionComponenteBean identificacionBean;

    private SgPersona entidadEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<SgSexo> comboSexos;
    private SofisComboG<SgEtnia> comboEtnia;

    private SofisComboG<SgEstadoCivil> comboEstadoCivil;
    private SofisComboG<SgTipoTelefono> comboTiposTelefonos;
    private SofisComboG<SgDepartamento> comboDepartamentoNacimiento;
    private SofisComboG<SgMunicipio> comboMunicipioNacimiento;
    private SofisComboG<SgDepartamento> comboDepartamentoPartida;
    private SofisComboG<SgMunicipio> comboMunicipioPartida;
    private SofisComboG<SgNacionalidad> comboNacionalidad;
    private SgTelefono telefonoEnEdicion = new SgTelefono();
    private Boolean renderIdentPersonalSede;
    private Boolean renderPartidaNacimiento;
    private Boolean tienePartida = Boolean.FALSE;
    private Boolean continuarExistePersona = Boolean.FALSE;
    private SofisComboG<EnumEstadoPersona> comboEstadoPersona;
    private SofisComboG<SgMotivoFallecimiento> comboMotivoFallecimiento;
    private String tabActiveId;
    private SgPersonalSedeEducativa personal = new SgPersonalSedeEducativa();
    private List<SgAllegado> listaAllegados = new ArrayList<>();
    private Boolean personaSePuedeEliminar = Boolean.FALSE;
    private Boolean puedeEditarFechaNacimiento = Boolean.FALSE;
    private Boolean verTrastornosAprendizaje = Boolean.FALSE;
    private Boolean consumoServicioPartidaNacimientoRNPNHabilitado = Boolean.FALSE;
    private Boolean consumoServicioDUIRNPNHabilitado = Boolean.FALSE;
    private Boolean consumoServicioCUNRNPNHabilitado = Boolean.FALSE;
    private FiltroPersona filtroPersonaUnir;
    private SgUnirPersona personaUnir;
    private SgPersona entidadEnEdicionUnir;
    
    private List<SgIdentificacion> listOtrasIdentificaciones;
    private SofisComboG<SgPais> comboPais;
    private SofisComboG<SgTipoIdentificacion> comboTipoIdentificacion;
    private String ideNumeroDocumento;
    private Boolean tieneOtraIdent;

    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();

    public PersonaBean() {
    }

    @PostConstruct
    public void init() {
        try {

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgPersona) request.getAttribute("persona");
            Boolean soloLecturaComponente = (Boolean) request.getAttribute("soloLecturaPersona");
            puedeEditarFechaNacimiento = (Boolean) request.getAttribute("puedeEditarFechaNacimiento");

            if (soloLecturaComponente != null) {
                soloLectura = soloLecturaComponente;
            }

            soloLectura = editable != null ? !editable : soloLectura;
            if (!soloLectura) {
                cargarCombos();
            }

            //Persona utilizado como página
            if (entidadEnEdicion == null) {      
                validarAcceso();
                if (perId == null || perId <= 0) {
                    JSFUtils.redirectToIndex();
                    //this.agregar();
                } else {
                    SgPersona per=this.restClient.obtenerPorId(perId);
                    this.actualizar(per);
                }
            }
            filtroPersonaUnir=new FiltroPersona();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_PERSONA)) {
            JSFUtils.redirectToIndex();
        }
    }

    @PreDestroy
    public void preDestroy() {
        if (entidadEnEdicion != null && entidadEnEdicion.getPerFoto() != null && !StringUtils.isBlank(entidadEnEdicion.getPerFoto().getAchTmpPath())) {
            Path tmpFile = Paths.get(entidadEnEdicion.getPerFoto().getAchTmpPath());
            if (tmpFile != null && Files.exists(tmpFile)) {
                try {
                    Files.delete(tmpFile);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
    }

    public void cargarCombos() {

        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"sexNombre"});
            fc.setIncluirCampos(new String[]{"sexNombre", "sexVersion"});
            List<SgSexo> sexos = restCatalogo.buscarSexo(fc);
            comboSexos = new SofisComboG(new ArrayList(sexos), "sexNombre");
            comboSexos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"etnNombre"});
            fc.setIncluirCampos(new String[]{"etnNombre", "etnVersion"});
            List<SgEtnia> etinas = restCatalogo.buscarEtnia(fc);
            comboEtnia = new SofisComboG(new ArrayList(etinas), "etnNombre");
            comboEtnia.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"eciNombre"});
            fc.setIncluirCampos(new String[]{"eciNombre", "eciVersion"});
            List<SgEstadoCivil> estadosCivil = restCatalogo.buscarEstadoCivil(fc);
            comboEstadoCivil = new SofisComboG(new ArrayList(estadosCivil), "eciNombre");
            comboEstadoCivil.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"ttoNombre"});
            fc.setIncluirCampos(new String[]{"ttoNombre", "ttoVersion"});
            List<SgTipoTelefono> tiposTelefonos = restCatalogo.buscarTipoTelefono(fc);
            comboTiposTelefonos = new SofisComboG(new ArrayList(tiposTelefonos), "ttoNombre");
            comboTiposTelefonos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> depto = restCatalogo.buscarDepartamento(fc);
            comboDepartamentoNacimiento = new SofisComboG(depto, "depNombre");
            comboDepartamentoNacimiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDepartamentoPartida = new SofisComboG(depto, "depNombre");
            comboDepartamentoPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"nacCodigo", "nacNombre", "nacVersion"});
            fc.setOrderBy(new String[]{"nacNombre"});
            List<SgNacionalidad> listNac = restCatalogo.buscarNacionalidad(fc);
            comboNacionalidad = new SofisComboG(new ArrayList(listNac), "nacNombre");
            comboNacionalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipioNacimiento = new SofisComboG();
            comboMunicipioNacimiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipioPartida = new SofisComboG();
            comboMunicipioPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoPersona> estadoPersona = new ArrayList(Arrays.asList(EnumEstadoPersona.values()));
            comboEstadoPersona = new SofisComboG(estadoPersona, "text");
            comboEstadoPersona.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstadoPersona.ordenar();

            fc.setIncluirCampos(new String[]{"mfaCodigo", "mfaNombre", "mfaVersion"});
            fc.setOrderBy(new String[]{"mfaNombre"});
            List<SgMotivoFallecimiento> listMotFall = restCatalogo.buscarMotivoFallecimiento(fc);
            comboMotivoFallecimiento = new SofisComboG(new ArrayList(listMotFall), "mfaNombre");
            comboMotivoFallecimiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            SgConfiguracion ccun = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_CUN_RNPN_HABILITADO);
            SgConfiguracion cdui = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_DUI_RNPN_HABILITADO);
            SgConfiguracion cpnac = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_PARTIDA_NAC_RNPN_HABILITADO);

            if (ccun != null && ccun.getConValor() != null) {
                consumoServicioCUNRNPNHabilitado = Boolean.parseBoolean(ccun.getConValor());
            }

            if (cdui != null && cdui.getConValor() != null) {
                consumoServicioDUIRNPNHabilitado = Boolean.parseBoolean(cdui.getConValor());
            }

            if (cpnac != null && cpnac.getConValor() != null) {
                consumoServicioPartidaNacimientoRNPNHabilitado = Boolean.parseBoolean(cpnac.getConValor());
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgPersona();

    }

    public void handleFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getPerFoto() == null) {
            entidadEnEdicion.setPerFoto(new SgArchivo());
        }
        handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getPerFoto());
    }

    public void handlePartidaFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getPerPartidaNacimientoArchivo() == null) {
            entidadEnEdicion.setPerPartidaNacimientoArchivo(new SgArchivo());
        }
        handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getPerPartidaNacimientoArchivo());
    }

    public void limpiarCombos() {
        comboSexos.setSelected(-1);
        comboEtnia.setSelected(-1);
        comboEstadoCivil.setSelected(-1);
        comboDepartamentoNacimiento.setSelected(-1);
        comboMunicipioNacimiento.setSelected(-1);
        comboNacionalidad.setSelected(-1);
        comboEstadoPersona.setSelected(-1);
    }

    public void tienePartidaSelected() {
        if (BooleanUtils.isFalse(tienePartida)) {
            this.entidadEnEdicion.setPerPartidaNacimiento(null);
            this.entidadEnEdicion.setPerPartidaNacimientoFolio(null);
            this.entidadEnEdicion.setPerPartidaNacimientoLibro(null);
            this.entidadEnEdicion.setPerPartidaNacimientoComplemento(null);
            this.entidadEnEdicion.setPerPartidaNacimientoAnio(null);
            this.entidadEnEdicion.setPerPartidaDepartamento(null);
            this.entidadEnEdicion.setPerPartidaMunicipio(null);
            this.comboDepartamentoPartida.setSelected(-1);
            this.comboMunicipioPartida.setSelected(-1);
            this.entidadEnEdicion.setPerPartidaNacimientoPresenta(Boolean.FALSE);
        }
    }

    public void tieneDiscapacidadSelected() {
        this.entidadEnEdicion.setPerTieneDiagnostico(null);
    }

    public void actualizar(SgPersona per) {
        try {
            if (per != null) {
                if (!per.equals(this.entidadEnEdicion)) {
                    tienePartida = Boolean.FALSE;
                }
                if (!soloLectura) {
                    if (comboSexos == null) {
                        cargarCombos();
                    }
                    limpiarCombos();
                    comboSexos.setSelectedT(per.getPerSexo());
                    comboEtnia.setSelectedT(per.getPerEtnia());
                    comboEstadoCivil.setSelectedT(per.getPerEstadoCivil());
                    comboNacionalidad.setSelectedT(per.getPerNacionalidad());
                    comboEstadoPersona.setSelectedT(per.getPerEstado());
                    comboMotivoFallecimiento.setSelectedT(per.getPerMotivoFallecimiento());
                    comboEstadoPersona.setSelectedT(per.getPerEstado());

                    if (per.getPerDepartamentoNacimento() != null) {
                        comboDepartamentoNacimiento.setSelectedT(per.getPerDepartamentoNacimento());
                        seleccionarDepartamento();
                    }
                    if (per.getPerMunicipioNacimiento() != null) {
                        comboMunicipioNacimiento.setSelectedT(per.getPerMunicipioNacimiento());
                    }

                    if (per.getPerPartidaDepartamento() != null) {
                        comboDepartamentoPartida.setSelectedT(per.getPerPartidaDepartamento());
                        seleccionarDepartamentoPartida();
                    }

                    if (per.getPerPartidaMunicipio() != null) {
                        comboMunicipioPartida.setSelectedT(per.getPerPartidaMunicipio());
                    }

                }
            }
            if (per.getPerPartidaNacimiento() != null || BooleanUtils.isTrue(per.getPerPartidaNacimientoPresenta())) {
                tienePartida = Boolean.TRUE;
            }
            entidadEnEdicion = per;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgDiscapacidad> completeDiscapacidad(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"disNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getPerDiscapacidades() != null
                    ? restCatalogo.buscarDiscapacidad(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getPerDiscapacidades().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarDiscapacidad(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgTerapia> completeTerapia(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"terNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getPerTerapias() != null
                    ? restCatalogo.buscarTerapias(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getPerTerapias().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarTerapias(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgReferenciasApoyo> completeReferenciaApoyo(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"reaNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getPerReferenciasApoyo() != null
                    ? restCatalogo.buscarReferenciasApoyo(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getPerReferenciasApoyo().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarReferenciasApoyo(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgTrastornoAprendizaje> completeTrastornoAprendizaje(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setIncluirCampos(new String[]{"traPk", "traNombre"});
            fil.setOrderBy(new String[]{"traNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getPerTrastornosAprendizaje() != null
                    ? restCatalogo.buscarTrastornoAprendizaje(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getPerTrastornosAprendizaje().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarTrastornoAprendizaje(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipioNacimiento = new SofisComboG();
            comboMunicipioNacimiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            entidadEnEdicion.setPerDepartamentoNacimento(comboDepartamentoNacimiento.getSelectedT());
            if (comboDepartamentoNacimiento.getSelectedT() != null) {
                FiltroMunicipio filtroM = new FiltroMunicipio();
                filtroM.setOrderBy(new String[]{"munNombre"});
                filtroM.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                filtroM.setAscending(new boolean[]{true});
                filtroM.setMunDepartamentoFk(comboDepartamentoNacimiento.getSelectedT().getDepPk());
                if (!munCache.containsKey(comboDepartamentoNacimiento.getSelectedT())) {
                    munCache.put(this.comboDepartamentoNacimiento.getSelectedT(), restCatalogo.buscarMunicipio(filtroM));
                }
                comboMunicipioNacimiento = new SofisComboG(munCache.get(this.comboDepartamentoNacimiento.getSelectedT()), "munNombre");
                comboMunicipioNacimiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamentoPartida() {
        try {
            comboMunicipioPartida = new SofisComboG();
            comboMunicipioPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            entidadEnEdicion.setPerPartidaDepartamento(comboDepartamentoPartida.getSelectedT());
            if (comboDepartamentoPartida.getSelectedT() != null) {
                FiltroMunicipio filtroM = new FiltroMunicipio();
                filtroM.setOrderBy(new String[]{"munNombre"});
                filtroM.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                filtroM.setAscending(new boolean[]{true});
                filtroM.setMunDepartamentoFk(comboDepartamentoPartida.getSelectedT().getDepPk());
                if (!munCache.containsKey(comboDepartamentoPartida.getSelectedT())) {
                    munCache.put(this.comboDepartamentoPartida.getSelectedT(), restCatalogo.buscarMunicipio(filtroM));
                }
                comboMunicipioPartida = new SofisComboG(munCache.get(this.comboDepartamentoPartida.getSelectedT()), "munNombre");
                comboMunicipioPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarMunicipio() {
        entidadEnEdicion.setPerMunicipioNacimiento(comboMunicipioNacimiento.getSelectedT());
    }

    public void seleccionarMunicipioPartida() {
        entidadEnEdicion.setPerPartidaMunicipio(comboMunicipioPartida.getSelectedT());
    }

    public void comboEstadoCivilSelected() {
        entidadEnEdicion.setPerEstadoCivil(comboEstadoCivil.getSelectedT());
    }

    public void comboSexoSelected() {
        entidadEnEdicion.setPerSexo(comboSexos.getSelectedT());
    }

    public void comboEtniaSelected() {
        entidadEnEdicion.setPerEtnia(comboEtnia.getSelectedT());
    }

    public void comboNacionalidadSelected() {
        entidadEnEdicion.setPerNacionalidad(comboNacionalidad.getSelectedT());
    }

    public void comboEstadoPersonaSelected() {
        entidadEnEdicion.setPerEstado(comboEstadoPersona.getSelectedT());
        if (entidadEnEdicion.getPerEstado() == null || !entidadEnEdicion.getPerEstado().equals(EnumEstadoPersona.FALLECIDO)) {
            entidadEnEdicion.setPerFechaFallecimiento(null);
            entidadEnEdicion.setPerMotivoFallecimiento(null);
            comboMotivoFallecimiento.setSelected(-1);
        }
    }

    public void comboMotivoFallecimientoSelected() {
        entidadEnEdicion.setPerMotivoFallecimiento(comboMotivoFallecimiento.getSelectedT());
    }

    public void guardar() {
        try {

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarTelefonoAPersona() {
        try {
            telefonoEnEdicion.setTelTipoTelefono(comboTiposTelefonos.getSelectedT());
            ViewIdUtils.generateViewId(telefonoEnEdicion, entidadEnEdicion.getPerTelefonos());
            restTelefonoClient.validar(telefonoEnEdicion);
            if (entidadEnEdicion.getPerTelefonos() == null) {
                entidadEnEdicion.setPerTelefonos(new ArrayList<>());
            }
            if (entidadEnEdicion.getPerTelefonos().contains(telefonoEnEdicion)) {
                entidadEnEdicion.getPerTelefonos().set(entidadEnEdicion.getPerTelefonos().indexOf(telefonoEnEdicion), telefonoEnEdicion);
            } else {
                entidadEnEdicion.getPerTelefonos().add(telefonoEnEdicion);
            }
            PrimeFaces.current().executeScript("PF('telefonoDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean getPuedeValidarPartidaNacimiento() {
        if (this.soloLectura) {
            return Boolean.FALSE;
        }
        if (BooleanUtils.isNotTrue(this.consumoServicioPartidaNacimientoRNPNHabilitado)) {
            return Boolean.FALSE;
        }
        if (BooleanUtils.isTrue(this.entidadEnEdicion.getPerPartidaNacPendienteValidacionRNPN())
                || BooleanUtils.isTrue(this.entidadEnEdicion.getPerDuiPendienteValidacionRNPN())
                || BooleanUtils.isTrue(this.entidadEnEdicion.getPerCunPendienteValidacionRNPN())) {
            return Boolean.FALSE;
        }
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VALIDAR_PARTIDA_NAC_PERSONA_RNPN)) {
            return Boolean.FALSE;
        }
        if (StringUtils.isNotBlank(this.entidadEnEdicion.getPerDui()) || BooleanUtils.isTrue(this.entidadEnEdicion.getPerDuiValidadoRNPN())) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerCun() != null || BooleanUtils.isTrue(this.entidadEnEdicion.getPerCunValidadoRNPN())) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPartidaNacimiento() == null
                || this.entidadEnEdicion.getPerPartidaNacimientoAnio() == null
                || StringUtils.isBlank(this.entidadEnEdicion.getPerPartidaNacimientoLibro())
                || StringUtils.isBlank(this.entidadEnEdicion.getPerPartidaNacimientoFolio())) {
            return Boolean.FALSE;
        }
        return BooleanUtils.isNotTrue(this.entidadEnEdicion.getPerPartidaNacValidadaRNPN());
    }

    public void validarDUI() {
        try {
            if (!Objects.equals(this.entidadEnEdicion.getPerDUIGuardado(), this.entidadEnEdicion.getPerDui())) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_DEBE_GUARDAR_PERSONA_ANTES_DE_CONSULTAR_RNPN), "");
                return;
            }
            restClient.validarPersonaRNPNPorDUI(this.entidadEnEdicion.getPerPk());
            actualizarPersona();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            this.entidadEnEdicion.setPerDuiPendienteValidacionRNPN(Boolean.TRUE);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarCUN() {
        try {
            if (!Objects.equals(this.entidadEnEdicion.getPerCUNGuardado(), this.entidadEnEdicion.getPerCun())) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_DEBE_GUARDAR_PERSONA_ANTES_DE_CONSULTAR_RNPN), "");
                return;
            }
            restClient.validarPersonaRNPNPorCUN(this.entidadEnEdicion.getPerPk());
            actualizarPersona();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            this.entidadEnEdicion.setPerCunPendienteValidacionRNPN(Boolean.TRUE);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarPartidaNacimiento() {
        try {
            if (!Objects.equals(this.entidadEnEdicion.getPerPartidaNacimientoGuardado(), this.entidadEnEdicion.getPerPartidaNacimiento())
                    || !Objects.equals(this.entidadEnEdicion.getPerPartidaNacimientoAnioGuardado(), this.entidadEnEdicion.getPerPartidaNacimientoAnio())
                    || !Objects.equals(this.entidadEnEdicion.getPerPartidaNacimientoFolioGuardado(), this.entidadEnEdicion.getPerPartidaNacimientoFolio())
                    || !Objects.equals(this.entidadEnEdicion.getPerPartidaNacimientoLibroGuardado(), this.entidadEnEdicion.getPerPartidaNacimientoLibro())
                    || !Objects.equals(this.entidadEnEdicion.getPerPartidaNacimientoComplementoGuardado(), this.entidadEnEdicion.getPerPartidaNacimientoComplemento())
                    || !Objects.equals(this.entidadEnEdicion.getPerPartidaDepartamentoGuardado(), this.entidadEnEdicion.getPerPartidaDepartamento())
                    || !Objects.equals(this.entidadEnEdicion.getPerPartidaMunicipioGuardado(), this.entidadEnEdicion.getPerPartidaMunicipio())) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_DEBE_GUARDAR_PERSONA_ANTES_DE_CONSULTAR_RNPN), "");
                return;
            }
            restClient.validarPersonaRNPNPorPartidaNacimiento(this.entidadEnEdicion.getPerPk());
            actualizarPersona();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            this.entidadEnEdicion.setPerPartidaNacPendienteValidacionRNPN(Boolean.TRUE);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarPersona() {
        try {
            if (entidadEnEdicion.getPerIngresoAlgunaIdentificacion()) {

                entidadEnEdicion.setPerSeEncontroIdentificacion(Boolean.FALSE);
                entidadEnEdicion.setPerSeBuscoEnBd(Boolean.TRUE);

                List<SgPersona> listaPersonas = this.restClient.obtenerPersonasPorIdentificacion(this.entidadEnEdicion);

                if (StringUtils.isNotBlank(this.entidadEnEdicion.getPerDui())
                        && BooleanUtils.isTrue(consumoServicioDUIRNPNHabilitado)
                        && listaPersonas.isEmpty()) {
                    try {
                        SgPersona p = this.restClient.obtenerPersonaDesdeRNPNPorDUI(this.entidadEnEdicion.getPerDui());
                        listaPersonas.add(p);
                    } catch (BusinessException be) {
                        JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
                        return;
                    } catch (Exception ex) {
                        //Servicio deshabilitado, o error de consumo 
                    }
                }

                if (this.entidadEnEdicion.getPerCun() != null
                        && BooleanUtils.isTrue(consumoServicioCUNRNPNHabilitado)
                        && listaPersonas.isEmpty()) {
                    try {
                        SgPersona p = this.restClient.obtenerPersonaDesdeRNPNPorCUN(this.entidadEnEdicion.getPerCun());
                        listaPersonas.add(p);
                    } catch (BusinessException be) {
                        JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
                        return;
                    } catch (Exception ex) {
                        //Servicio deshabilitado, o error de consumo 
                    }
                }

                if (listaPersonas.isEmpty()) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.PERSONA_NO_ENCONTRADA), "");
                    if (this.entidadEnEdicion.getPerNie() != null || this.entidadEnEdicion.getPerNip() != null) {
                        entidadEnEdicion.setPerSeBuscoEnBd(Boolean.FALSE); // En estos casos no se deja avanzar
                    }

                    if (BooleanUtils.isTrue(this.renderIdentPersonalSede)) {
                        if (BooleanUtils.isTrue(this.continuarExistePersona)) {
                            entidadEnEdicion.setPerSePermiteModificarIdentificacion(Boolean.FALSE);
                            entidadEnEdicion.setPerSeBuscoEnBd(Boolean.FALSE);
                        } else {
                            entidadEnEdicion.setPerSePermiteModificarIdentificacion(Boolean.TRUE);
                        }
                    }
                }

                if (listaPersonas.size() > 1) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION), "");
                }

                if (listaPersonas.size() == 1) {
                    entidadEnEdicion = listaPersonas.get(0);
                    entidadEnEdicion.setPerSeEncontroIdentificacion(Boolean.TRUE);
                    entidadEnEdicion.setPerSeBuscoEnBd(Boolean.TRUE);
                    actualizarPersona();
                }
            } else {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.IDENTIFICACION_NO_INGRESADA), "");
            }

        } catch (BusinessException be) {
            entidadEnEdicion.setPerSeEncontroIdentificacion(Boolean.FALSE);
            entidadEnEdicion.setPerSeBuscoEnBd(Boolean.FALSE);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            entidadEnEdicion.setPerSeEncontroIdentificacion(Boolean.FALSE);
            entidadEnEdicion.setPerSeBuscoEnBd(Boolean.FALSE);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void actualizarPersona() {
        String expr = "#{controllerParam[actionParam](personaBean.entidadEnEdicion)}";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
    }

    public void eliminarFoto() {
        this.entidadEnEdicion.setPerFoto(null);
    }

    public void eliminarPartidaArchivo() {
        this.entidadEnEdicion.setPerPartidaNacimientoArchivo(null);
    }

    public void agregarTelefono() {
        telefonoEnEdicion = new SgTelefono();
        comboTiposTelefonos.setSelected(-1);
    }

    public void editarTelefono(SgTelefono telefono) {
        telefonoEnEdicion = (SgTelefono) SerializationUtils.clone(telefono);
        comboTiposTelefonos.setSelectedT(telefonoEnEdicion.getTelTipoTelefono());
    }

    public void eliminarTelefono(SgTelefono telefono) {
        this.entidadEnEdicion.getPerTelefonos().remove(telefono);
    }

    public void changeTab(TabChangeEvent event) {
        try {
            this.tabActiveId = event.getTab().getId();
            if (tabActiveId.equals("registros")) {
                if (matriculaComponenteBean.getEstEdicion() == null) {
                    matriculaComponenteBean.actualizar(entidadEnEdicion.getPerEstudiante());
                }
                if (personal.getPsePk() == null) {
                    FiltroPersonalSedeEducativa fPersonal = new FiltroPersonalSedeEducativa();
                    fPersonal.setPerPk(entidadEnEdicion.getPerPk());
                    fPersonal.setIncluirCampos(new String[]{"psePk", "pseVersion",
                        "pseDatoEmpleado.demPk", "pseDatoEmpleado.demVersion", "pseDatoEmpleado.demEstado", "pseDatoEmpleado.demCodigoEmpleado",
                        "pseDatoEmpleado.demNivelFk.nemNombre", "pseDatoEmpleado.demCategoriaFk.cemNombre"});
                    fPersonal.setIncluirSedes(Boolean.TRUE);
                    List<SgPersonalSedeEducativa> lpersonal = restPersonal.buscar(fPersonal);
                    if (!lpersonal.isEmpty()) {
                        personal = lpersonal.get(0);
                    }
                }
            }
            if (listaAllegados.isEmpty()) {
                FiltroAllegado fA = new FiltroAllegado();
                fA.setAllPersonaPk(entidadEnEdicion.getPerPk());
                fA.setIncluirCampos(new String[]{"allPk", "allVersion",
                    "allEsFamiliar", "allTipoParentesco.tpaPk", "allTipoParentesco.tpaCodigo",
                    "allTipoParentesco.tpaNombre", "allTipoParentesco.tpaVersion", "allContactoEmergencia",
                    "allPersonaReferenciada.perPrimerNombre", "allPersonaReferenciada.perSegundoNombre", "allPersonaReferenciada.perTercerNombre",
                    "allPersonaReferenciada.perPrimerApellido", "allPersonaReferenciada.perSegundoApellido", "allPersonaReferenciada.perTercerApellido",
                    "allPersonaReferenciada.perNie"});
                listaAllegados = restAllegado.buscar(fA);
            }
            if (tabActiveId.equals("registros")) {
                personaSePuedeEliminar();
            }
            PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
        } catch (BusinessException be) {
            entidadEnEdicion.setPerSeEncontroIdentificacion(Boolean.FALSE);
            entidadEnEdicion.setPerSeBuscoEnBd(Boolean.FALSE);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            entidadEnEdicion.setPerSeEncontroIdentificacion(Boolean.FALSE);
            entidadEnEdicion.setPerSeBuscoEnBd(Boolean.FALSE);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void personaSePuedeEliminar() {
        personaSePuedeEliminar = Boolean.TRUE;
        if (entidadEnEdicion.getPerEstudiante() != null) {
            personaSePuedeEliminar = Boolean.FALSE;
            return;
        }

        if (personal.getPsePk() != null) {
            personaSePuedeEliminar = Boolean.FALSE;
            return;
        }

        if (!listaAllegados.isEmpty()) {
            personaSePuedeEliminar = Boolean.FALSE;
            return;
        }
    }

    public void eliminar() {
        try {
            FiltroSelloFirma fsf = new FiltroSelloFirma();
            fsf.setSfiPersonaPk(entidadEnEdicion.getPerPk());
            fsf.setIncluirCampos(new String[]{"sfiPk"});
            List<SgSelloFirma> selloFirma = selloFirmaRestClient.buscar(fsf);
            if (!selloFirma.isEmpty()) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_CON_SELLO_FIRMA), "");
                return;
            }

            restClient.eliminar(entidadEnEdicion.getPerPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.PERSONAS);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean puedeModificarFechaNacimiento() {
        if (soloLectura) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        if (BooleanUtils.isTrue(entidadEnEdicion.getPerDuiValidadoRNPN())
                || BooleanUtils.isTrue(entidadEnEdicion.getPerCunValidadoRNPN())
                || BooleanUtils.isTrue(entidadEnEdicion.getPerPartidaNacValidadaRNPN())) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() == null) {
            return Boolean.TRUE;
        }
        if (entidadEnEdicion.getPerPk() != null && BooleanUtils.isTrue(puedeEditarFechaNacimiento)) {
            LocalDate fecha = LocalDate.of(0001, 01, 01);
            return (entidadEnEdicion.getPerFechaNacimiento() == null || entidadEnEdicion.getPerFechaNacimiento().equals(fecha));
        }
        return Boolean.FALSE;
    }

    public Boolean getPuedeEditarNombresYApellidos() {
        if (soloLectura) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        if (BooleanUtils.isTrue(entidadEnEdicion.getPerDuiValidadoRNPN())
                || BooleanUtils.isTrue(entidadEnEdicion.getPerCunValidadoRNPN())
                || BooleanUtils.isTrue(entidadEnEdicion.getPerPartidaNacValidadaRNPN())) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() == null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getPuedeEditarRestoDeDatosPersona() {
        if (this.soloLectura) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() == null) {
            return Boolean.TRUE;
        }
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        return Boolean.TRUE;
    }

    public Boolean getPuedeEditarDatosSensiblesPersona() {
        if (this.soloLectura) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() == null) {
            return Boolean.TRUE;
        }
        if (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        if (BooleanUtils.isTrue(this.entidadEnEdicion.getPerSeEncontroIdentificacion())) {
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    public Boolean getPuedeEditarPartidaNacimiento() {
        if (this.soloLectura) {
            return Boolean.FALSE;
        }
        if (this.entidadEnEdicion.getPerPk() != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        return BooleanUtils.isNotTrue(this.entidadEnEdicion.getPerPartidaNacValidadaRNPN());
    }
    
    
    public void cargarCombosOtraIdentificacion() {
        try {           
                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setAscending(new boolean[]{true});
                fc.setOrderBy(new String[]{"paiNombre"});
                fc.setIncluirCampos(new String[]{"paiNombre", "paiVersion", "paiCodigo"});
                List<SgPais> paises = restCatalogo.buscarPais(fc);
                comboPais = new SofisComboG(new ArrayList(paises), "paiNombre");
                comboPais.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                fc.setOrderBy(new String[]{"tinNombre"});
                fc.setIncluirCampos(new String[]{"tinCodigo", "tinNombre", "tinVersion"});
                fc.setHabilitado(Boolean.TRUE);
                List<SgTipoIdentificacion> tiposIdentificaciones = restCatalogo.buscarTipoIdentificacion(fc);
                tiposIdentificaciones=tiposIdentificaciones.stream().filter(c->c.getTinCodigo().equals("1") || c.getTinCodigo().equals("5")).collect(Collectors.toList());
                comboTipoIdentificacion = new SofisComboG(new ArrayList(tiposIdentificaciones), "tinNombre");
                comboTipoIdentificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));    

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    
    public void seleccionarOtrasIdentificaciones(){
        listOtrasIdentificaciones=new ArrayList();
        comboPais.setSelected(-1);
        comboTipoIdentificacion.setSelected(-1);
        ideNumeroDocumento=null;
    }
    
    public void agregarOtraIdentificacionEnTabla(){
        SgIdentificacion ident=new SgIdentificacion();
        if(comboPais.getSelectedT()==null ||
                comboTipoIdentificacion.getSelectedT()==null ||
                   (StringUtils.isBlank(ideNumeroDocumento))){
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, "Debe seleccionar todos los campos.", "");
            return;
        }
        
        if(comboPais.getSelectedT()!=null){
            ident.setIdePaisEmisor(comboPais.getSelectedT());
        }
        if(comboTipoIdentificacion.getSelectedT()!=null){
            ident.setIdeTipoDocumento(comboTipoIdentificacion.getSelectedT());
        }
        ident.setIdeNumeroDocumento(ideNumeroDocumento);
        
        listOtrasIdentificaciones.add(ident);
        
        comboPais.setSelected(-1);
        comboTipoIdentificacion.setSelected(-1);
        ideNumeroDocumento=null;
    }
    
    public void eliminarOtraIdentificacion(SgIdentificacion elem){
        listOtrasIdentificaciones.remove(elem);
    }

    public void prepararUnirPersona() {
        tieneOtraIdent=Boolean.FALSE;
        cargarCombosOtraIdentificacion();
        listOtrasIdentificaciones=new ArrayList();
        this.personaUnir = new SgUnirPersona();
        personaUnir.setPersonaApk(entidadEnEdicion.getPerPk());
        filtroPersonaUnir = new FiltroPersona();
        entidadEnEdicionUnir = null;
    }

    //DUI, NIE, CUN, NIP, Carné de residente, Pasaporte.
    public void buscarPersonaUnir() {
        try {
            entidadEnEdicionUnir = null;
            if (StringUtils.isBlank(filtroPersonaUnir.getDui())
                    && filtroPersonaUnir.getNie() == null
                    && filtroPersonaUnir.getCun() == null
                    && StringUtils.isBlank(filtroPersonaUnir.getNip())
                    && listOtrasIdentificaciones.isEmpty()) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Debe completar al menos uno de los filtros.", "");
                return;
            } else {
                filtroPersonaUnir.setIncluirCampos(new String[]{"perPrimerNombre", "perSegundoNombre",
                    "perPrimerApellido", "perSegundoApellido", "perVersion","perDui","perCun","perNie","perNip"});
                filtroPersonaUnir.setOtrasIden(listOtrasIdentificaciones.isEmpty()?null:listOtrasIdentificaciones);
                List<SgPersona> perList = restClient.buscar(filtroPersonaUnir);
                if (perList != null && !perList.isEmpty()) {
                    entidadEnEdicionUnir = perList.get(0);
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "No existe persona para las identificaciones seleccionadas.", "");
                    return;
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void unirPersonas() {
        try {
            if (entidadEnEdicionUnir != null && entidadEnEdicionUnir.getPerPk() != null) {
                personaUnir.setPersonaBpk(entidadEnEdicionUnir.getPerPk());
                restClient.unirPersona(personaUnir);       
              //  this.actualizar(this.restClient.obtenerPorId(perId));
              //  identificacionBean.actualizar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "La persona quedo unida de forma correcta.", "");
                PrimeFaces.current().executeScript("PF('itemDialogPersonaUnir').hide()");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.redirectToPage(ConstantesPaginas.PERSONA + "?id=" + entidadEnEdicion.getPerPk()+"&edit=false"); 
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgPersona getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPersona entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgSexo> getComboSexos() {
        return comboSexos;
    }

    public void setComboSexos(SofisComboG<SgSexo> comboSexos) {
        this.comboSexos = comboSexos;
    }

    public SofisComboG<SgEtnia> getComboEtnia() {
        return comboEtnia;
    }

    public void setComboEtnia(SofisComboG<SgEtnia> comboEtnia) {
        this.comboEtnia = comboEtnia;
    }

    public SofisComboG<SgEstadoCivil> getComboEstadoCivil() {
        return comboEstadoCivil;
    }

    public void setComboEstadoCivil(SofisComboG<SgEstadoCivil> comboEstadoCivil) {
        this.comboEstadoCivil = comboEstadoCivil;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgTelefono getTelefonoEnEdicion() {
        return telefonoEnEdicion;
    }

    public void setTelefonoEnEdicion(SgTelefono telefonoEnEdicion) {
        this.telefonoEnEdicion = telefonoEnEdicion;
    }

    public SofisComboG<SgTipoTelefono> getComboTiposTelefonos() {
        return comboTiposTelefonos;
    }

    public void setComboTiposTelefonos(SofisComboG<SgTipoTelefono> comboTiposTelefonos) {
        this.comboTiposTelefonos = comboTiposTelefonos;
    }

    public Long getPerId() {
        return perId;
    }

    public void setPerId(Long perId) {
        this.perId = perId;
    }

    public Boolean getPerRev() {
        return perRev;
    }

    public void setPerRev(Boolean perRev) {
        this.perRev = perRev;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public PersonaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(PersonaRestClient restClient) {
        this.restClient = restClient;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public TelefonoRestClient getRestTelefonoClient() {
        return restTelefonoClient;
    }

    public void setRestTelefonoClient(TelefonoRestClient restTelefonoClient) {
        this.restTelefonoClient = restTelefonoClient;
    }

    public IdentificacionRestClient getRestIdentificacionClient() {
        return restIdentificacionClient;
    }

    public void setRestIdentificacionClient(IdentificacionRestClient restIdentificacionClient) {
        this.restIdentificacionClient = restIdentificacionClient;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoNacimiento() {
        return comboDepartamentoNacimiento;
    }

    public void setComboDepartamentoNacimiento(SofisComboG<SgDepartamento> comboDepartamentoNacimiento) {
        this.comboDepartamentoNacimiento = comboDepartamentoNacimiento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipioNacimiento() {
        return comboMunicipioNacimiento;
    }

    public void setComboMunicipioNacimiento(SofisComboG<SgMunicipio> comboMunicipioNacimiento) {
        this.comboMunicipioNacimiento = comboMunicipioNacimiento;
    }

    public SofisComboG<SgNacionalidad> getComboNacionalidad() {
        return comboNacionalidad;
    }

    public void setComboNacionalidad(SofisComboG<SgNacionalidad> comboNacionalidad) {
        this.comboNacionalidad = comboNacionalidad;
    }

    public Boolean getRenderIdentPersonalSede() {
        return renderIdentPersonalSede;
    }

    public void setRenderIdentPersonalSede(Boolean renderIdentPersonalSede) {
        this.renderIdentPersonalSede = renderIdentPersonalSede;
    }

    public Boolean getRenderPartidaNacimiento() {
        return renderPartidaNacimiento;
    }

    public void setRenderPartidaNacimiento(Boolean renderPartidaNacimiento) {
        if (renderPartidaNacimiento == null) {
            this.renderPartidaNacimiento = Boolean.FALSE;
        } else {
            this.renderPartidaNacimiento = renderPartidaNacimiento;
        }
    }

    public Boolean getTienePartida() {
        return tienePartida;
    }

    public void setTienePartida(Boolean tienePartida) {
        this.tienePartida = tienePartida;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoPartida() {
        return comboDepartamentoPartida;
    }

    public void setComboDepartamentoPartida(SofisComboG<SgDepartamento> comboDepartamentoPartida) {
        this.comboDepartamentoPartida = comboDepartamentoPartida;
    }

    public SofisComboG<SgMunicipio> getComboMunicipioPartida() {
        return comboMunicipioPartida;
    }

    public void setComboMunicipioPartida(SofisComboG<SgMunicipio> comboMunicipioPartida) {
        this.comboMunicipioPartida = comboMunicipioPartida;
    }

    public Boolean getContinuarExistePersona() {
        return continuarExistePersona;
    }

    public void setContinuarExistePersona(Boolean continuarExistePersona) {
        this.continuarExistePersona = continuarExistePersona;
    }

    public SofisComboG<EnumEstadoPersona> getComboEstadoPersona() {
        return comboEstadoPersona;
    }

    public void setComboEstadoPersona(SofisComboG<EnumEstadoPersona> comboEstadoPersona) {
        this.comboEstadoPersona = comboEstadoPersona;
    }

    public SofisComboG<SgMotivoFallecimiento> getComboMotivoFallecimiento() {
        return comboMotivoFallecimiento;
    }

    public void setComboMotivoFallecimiento(SofisComboG<SgMotivoFallecimiento> comboMotivoFallecimiento) {
        this.comboMotivoFallecimiento = comboMotivoFallecimiento;
    }

    public SgPersonalSedeEducativa getPersonal() {
        return personal;
    }

    public void setPersonal(SgPersonalSedeEducativa personal) {
        this.personal = personal;
    }

    public List<SgAllegado> getListaAllegados() {
        return listaAllegados;
    }

    public void setListaAllegados(List<SgAllegado> listaAllegados) {
        this.listaAllegados = listaAllegados;
    }

    public Boolean getPersonaSePuedeEliminar() {
        return personaSePuedeEliminar;
    }

    public void setPersonaSePuedeEliminar(Boolean personaSePuedeEliminar) {
        this.personaSePuedeEliminar = personaSePuedeEliminar;
    }

    public Boolean getPuedeEditarFechaNacimiento() {
        return puedeEditarFechaNacimiento;
    }

    public void setPuedeEditarFechaNacimiento(Boolean puedeEditarFechaNacimiento) {
        this.puedeEditarFechaNacimiento = puedeEditarFechaNacimiento;
    }

    public Boolean getVerTrastornosAprendizaje() {
        return verTrastornosAprendizaje;
    }

    public void setVerTrastornosAprendizaje(Boolean verTrastornosAprendizaje) {
        this.verTrastornosAprendizaje = verTrastornosAprendizaje;
    }

    public FiltroPersona getFiltroPersonaUnir() {
        return filtroPersonaUnir;
    }

    public void setFiltroPersonaUnir(FiltroPersona filtroPersonaUnir) {
        this.filtroPersonaUnir = filtroPersonaUnir;
    }

    public SgUnirPersona getPersonaUnir() {
        return personaUnir;
    }

    public void setPersonaUnir(SgUnirPersona personaUnir) {
        this.personaUnir = personaUnir;
    }

    public SgPersona getEntidadEnEdicionUnir() {
        return entidadEnEdicionUnir;
    }

    public void setEntidadEnEdicionUnir(SgPersona entidadEnEdicionUnir) {
        this.entidadEnEdicionUnir = entidadEnEdicionUnir;
    }

    public List<SgIdentificacion> getListOtrasIdentificaciones() {
        return listOtrasIdentificaciones;
    }

    public void setListOtrasIdentificaciones(List<SgIdentificacion> listOtrasIdentificaciones) {
        this.listOtrasIdentificaciones = listOtrasIdentificaciones;
    }

    public SofisComboG<SgPais> getComboPais() {
        return comboPais;
    }

    public void setComboPais(SofisComboG<SgPais> comboPais) {
        this.comboPais = comboPais;
    }

    public SofisComboG<SgTipoIdentificacion> getComboTipoIdentificacion() {
        return comboTipoIdentificacion;
    }

    public void setComboTipoIdentificacion(SofisComboG<SgTipoIdentificacion> comboTipoIdentificacion) {
        this.comboTipoIdentificacion = comboTipoIdentificacion;
    }

    public String getIdeNumeroDocumento() {
        return ideNumeroDocumento;
    }

    public void setIdeNumeroDocumento(String ideNumeroDocumento) {
        this.ideNumeroDocumento = ideNumeroDocumento;
    }

    public Boolean getTieneOtraIdent() {
        return tieneOtraIdent;
    }

    public void setTieneOtraIdent(Boolean tieneOtraIdent) {
        this.tieneOtraIdent = tieneOtraIdent;
    }

}
