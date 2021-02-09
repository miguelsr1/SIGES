/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.AllegadoRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgEncuestaEstudiante;
import sv.gob.mined.siges.web.dto.SgMenorEncuestaEstudiante;
import sv.gob.mined.siges.web.dto.SgNacionalidad;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgElementoHogar;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.web.dto.catalogo.SgMaterialPiso;
import sv.gob.mined.siges.web.dto.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncuestaEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.web.restclient.EncuestaEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EncuestaEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EncuestaEstudianteBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long encId;

    @Inject
    @Param(name = "estId")
    private Long estId;

    @Inject
    @Param(name = "rev")
    private Long estRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private EncuestaEstudianteRestClient restClient;

    @Inject
    private EstudianteRestClient estudianteClient;

    @Inject
    private PersonaRestClient personaClient;

    @Inject
    private AllegadoRestClient allegadoClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private SessionBean sessionBean;

    private Boolean soloLectura = Boolean.FALSE;
    private SgEncuestaEstudiante entidadEnEdicion;
    private SgEstudiante estudianteEnEdicion;
    private Long estudianteNie;
    private SgAllegado referente;
    private SgMenorEncuestaEstudiante menorEdicion;
    
    private String mensajeEncuestaEstudiante;


    //Encuesta
    private SofisComboG<SgFuenteAbastecimientoAgua> comboFuentesAbastecimientoAgua;
    private SofisComboG<SgMaterialPiso> comboMaterialesPiso;
    private SofisComboG<SgCompaniaTelecomunicacion> comboCompaniasTelecomunicacion;
    private SofisComboG<SgTipoServicioSanitario> comboTipoServicioSanitario;
    private SofisComboG<SgZona> comboZona;

    //Menor
    private SofisComboG<SgMedioTransporte> comboMediosTransporte;
    private SofisComboG<SgSexo> comboSexos;
    private SofisComboG<SgNacionalidad> comboNacionalidad;
    private SofisComboG<SgEstadoCivil> comboEstadoCivil;
    private SofisComboG<SgTipoParentesco> comboTipoParentesco;

    public EncuestaEstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (encId != null && encId > 0) {
                if (estRev != null && estRev > 0) {
                    //this.actualizar(restClient.obtenerEnRevision(estId, estRev));
                    //this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(encId));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else if (estId != null && estId > 0) {
                buscarEstudiante();
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
        try {
            if (soloLectura) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_ENCUESTA_ESTUDIANTE)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (encId == null) {
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ENCUESTA_ESTUDIANTE)) {
                        JSFUtils.redirectToIndex();
                    }
                } else {
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ENCUESTA_ESTUDIANTE)) {
                        JSFUtils.redirectToIndex();
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void buscarEstudiante() {
        try {
            limpiarCombos();
            entidadEnEdicion = new SgEncuestaEstudiante();
            estudianteEnEdicion =null;
            if (estudianteNie != null || estId != null) {

                FiltroEstudiante fest = new FiltroEstudiante();
                fest.setNie(estudianteNie);
                fest.setEstPk(estId);
                fest.setMaxResults(1L);
                fest.setIncluirCampos(new String[]{
                    "estVersion", "estPersona.perPk", "estPersona.perNie", "estPersona.perSexo.sexNombre", "estPersona.perFechaNacimiento",
                    "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                    "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido",
                    "estUltModFecha"
                });
                List<SgEstudiante> listEst = estudianteClient.buscar(fest);
                if (!listEst.isEmpty()) {
                    estudianteEnEdicion = listEst.get(0);
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRO_ESTUDIANTE), "");
                    return;
                }

                FiltroEncuestaEstudiante fenc = new FiltroEncuestaEstudiante();
                fenc.setNie(estudianteNie);
                fenc.setEstudiantePk(estId);
                fenc.setMaxResults(1L);
                fenc.setInicializarElementosHogar(Boolean.TRUE);
                fenc.setInicializarMenores(Boolean.TRUE);
                fenc.setOrderBy(new String[]{"encFechaIngreso","encSintonizaCanal10","encSintonizaFranjaEducativa"});
                fenc.setAscending(new boolean[]{false});
                List<SgEncuestaEstudiante> listEnc = restClient.buscar(fenc);
                if (!listEnc.isEmpty()) {
                    entidadEnEdicion = listEnc.get(0);  //Estudiante de encuesta es lazy          
                }

                entidadEnEdicion.setEncEstudiante(estudianteEnEdicion);
                setearCombos();
                cargarReferente();

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarMenor() {
        try {
            this.comboMediosTransporte.setSelected(-1);
            if (menorEdicion != null) {

                if (menorEdicion.getMenNie() == null) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_VACIO), "");
                    return;
                }

                FiltroEstudiante fest = new FiltroEstudiante();
                fest.setNie(menorEdicion.getMenNie());
                fest.setMaxResults(1L);
                fest.setIncluirCampos(new String[]{
                    "estVersion", "estPersona.perPk", "estPersona.perNie", "estPersona.perFechaNacimiento",
                    "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                    "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido",
                    "estPersona.perNacionalidad.nacPk", "estPersona.perNacionalidad.nacNombre", "estPersona.perNacionalidad.nacVersion",
                    "estPersona.perEstadoCivil.eciPk", "estPersona.perEstadoCivil.eciNombre", "estPersona.perEstadoCivil.eciVersion",
                    "estPersona.perSexo.sexPk", "estPersona.perSexo.sexNombre", "estPersona.perSexo.sexVersion",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipo",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedNombre",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedVersion",
                    "estMedioTransporte.mtrPk", "estMedioTransporte.mtrNombre", "estMedioTransporte.mtrVersion",
                    "estUltModFecha"
                });
                List<SgEstudiante> listEst = estudianteClient.buscar(fest);
                if (!listEst.isEmpty()) {
                    SgEstudiante est = listEst.get(0);
                    SgPersona per = est.getEstPersona();
                    menorEdicion.setMenValidadoSIGES(Boolean.TRUE);
                    menorEdicion.setMenMatriculadoSiges(Boolean.TRUE);
                    menorEdicion.setMenPersonaFk(per.getPerPk());
                    menorEdicion.setMenEstudianteFk(est.getEstPk());

                    menorEdicion.setMenFechaNacimiento(per.getPerFechaNacimiento());
                    menorEdicion.setMenPrimerNombre(per.getPerPrimerNombre());
                    menorEdicion.setMenSegundoNombre(per.getPerSegundoNombre());
                    menorEdicion.setMenPrimerApellido(per.getPerPrimerApellido());
                    menorEdicion.setMenSegundoApellido(per.getPerSegundoApellido());
                    menorEdicion.setMenSexo(per.getPerSexo());
                    menorEdicion.setMenNacionalidad(per.getPerNacionalidad());
                    menorEdicion.setMenEstadoCivil(per.getPerEstadoCivil());

                    if (est.getEstUltimaMatricula() != null) {
                        menorEdicion.setMenSede(est.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduSede());
                    } else {
                        menorEdicion.setMenSede(null);
                    }
                    this.comboMediosTransporte.setSelectedT(est.getEstMedioTransporte());
                } else {
                    
                    // Verificamos si existe como persona
                    FiltroPersona fper = new FiltroPersona();
                    fper.setIncluirCampos(new String[]{
                        "perVersion", "perPk", "perNie", "perFechaNacimiento",
                        "perPrimerNombre", "perSegundoNombre", "perTercerNombre",
                        "perPrimerApellido", "perSegundoApellido", "perTercerApellido",
                        "perNacionalidad.nacPk", "perNacionalidad.nacNombre", "perNacionalidad.nacVersion",
                        "perEstadoCivil.eciPk", "perEstadoCivil.eciNombre", "perEstadoCivil.eciVersion",
                        "perSexo.sexPk", "perSexo.sexNombre", "perSexo.sexVersion"
                    });
                    fper.setNie(menorEdicion.getMenNie());
                    fest.setMaxResults(1L);
                    List<SgPersona> pers = personaClient.buscar(fper);

                    if (!pers.isEmpty()) {
                        SgPersona per = pers.get(0);
                        menorEdicion.setMenPersonaFk(per.getPerPk());
                        menorEdicion.setMenValidadoSIGES(Boolean.TRUE);
                        menorEdicion.setMenMatriculadoSiges(Boolean.FALSE);

                        menorEdicion.setMenFechaNacimiento(per.getPerFechaNacimiento());
                        menorEdicion.setMenPrimerNombre(per.getPerPrimerNombre());
                        menorEdicion.setMenSegundoNombre(per.getPerSegundoNombre());
                        menorEdicion.setMenPrimerApellido(per.getPerPrimerApellido());
                        menorEdicion.setMenSegundoApellido(per.getPerSegundoApellido());
                        menorEdicion.setMenSexo(per.getPerSexo());
                        menorEdicion.setMenNacionalidad(per.getPerNacionalidad());
                        menorEdicion.setMenEstadoCivil(per.getPerEstadoCivil());

                    } else {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRO_PERSONA), "");
                        return;
                    }

                }

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarReferente() {
        try {
            referente = null;
            if (entidadEnEdicion != null && estudianteEnEdicion.getEstPersona().getPerPk() != null) {
                FiltroAllegado ff = new FiltroAllegado();
                ff.setAllEsReferente(Boolean.TRUE);
                ff.setIncluirCampos(new String[]{"allTipoParentesco.tpaNombre",
                    "allPersona.perPrimerNombre",
                    "allPersona.perSegundoNombre",
                    "allPersona.perPk",
                    "allPersona.perPrimerApellido",
                    "allPersona.perSegundoApellido",
                    "allPersona.perUsuarioPk",
                    "allPersona.perDui",
                    "allPersona.perNie",
                    "allPersona.perCun",
                    "allPersona.perNip",
                    "allPersona.perNit",
                    "allReferente",
                    "allViveConAllegado",
                    "allVersion"});
                ff.setAllPersonaReferenciadaPk(estudianteEnEdicion.getEstPersona().getPerPk());
                ff.setMaxResults(1L);
                List<SgAllegado> listAllegados = allegadoClient.buscar(ff);
                if (!listAllegados.isEmpty()) {
                    referente = listAllegados.get(0);
                    referente.setAllPersona(personaClient.obtenerPorId(referente.getAllPersona().getPerPk())); //Cargar todas las identificaciones               
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            FiltroTipoServicioSanitario fss = new FiltroTipoServicioSanitario();
            fss.setHabilitado(Boolean.TRUE);
            fss.setTssAplicaEstudiante(Boolean.TRUE);
            comboTipoServicioSanitario = new SofisComboG(catalogosClient.buscarTipoServicioSanitario(fss), "tssNombre");
            comboTipoServicioSanitario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"faaNombre"});
            fc.setIncluirCampos(new String[]{"faaNombre", "faaVersion"});
            comboFuentesAbastecimientoAgua = new SofisComboG(catalogosClient.buscarFuentesAbastecimientoAgua(fc), "faaNombre");
            comboFuentesAbastecimientoAgua.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"mapNombre"});
            fc.setIncluirCampos(new String[]{"mapNombre", "mapVersion"});
            comboMaterialesPiso = new SofisComboG(catalogosClient.buscarMaterialesPiso(fc), "mapNombre");
            comboMaterialesPiso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"cteNombre"});
            fc.setIncluirCampos(new String[]{"cteNombre", "cteVersion"});
            comboCompaniasTelecomunicacion = new SofisComboG(catalogosClient.buscarCompaniasTelecomunicacion(fc), "cteNombre");
            comboCompaniasTelecomunicacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"zonNombre", "zonVersion"});
            fc.setOrderBy(new String[]{"zonNombre"});
            List<SgZona> zonas = catalogosClient.buscarZona(fc);
            comboZona = new SofisComboG(zonas, "zonNombre");
            comboZona.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"mtrNombre"});
            fc.setIncluirCampos(new String[]{"mtrNombre", "mtrVersion"});
            List<SgMedioTransporte> mediosTransporte = catalogosClient.buscarMediosTransporte(fc);
            comboMediosTransporte = new SofisComboG(mediosTransporte, "mtrNombre");
            comboMediosTransporte.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"tpaNombre"});
            fc.setIncluirCampos(new String[]{"tpaNombre", "tpaVersion"});
            List<SgTipoParentesco> list = catalogosClient.buscarTipoParentesco(fc);
            comboTipoParentesco = new SofisComboG(list, "tpaNombre");
            comboTipoParentesco.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"nacCodigo", "nacNombre", "nacVersion"});
            fc.setOrderBy(new String[]{"nacNombre"});
            List<SgNacionalidad> listNac = catalogosClient.buscarNacionalidad(fc);
            comboNacionalidad = new SofisComboG(new ArrayList(listNac), "nacNombre");
            comboNacionalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"sexNombre"});
            fc.setIncluirCampos(new String[]{"sexNombre", "sexVersion"});
            List<SgSexo> sexos = catalogosClient.buscarSexo(fc);
            comboSexos = new SofisComboG(new ArrayList(sexos), "sexNombre");
            comboSexos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"eciNombre"});
            fc.setIncluirCampos(new String[]{"eciNombre", "eciVersion"});
            List<SgEstadoCivil> estadosCivil = catalogosClient.buscarEstadoCivil(fc);
            comboEstadoCivil = new SofisComboG(new ArrayList(estadosCivil), "eciNombre");
            comboEstadoCivil.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
             SgConfiguracion c = catalogosClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_MENSAJE_ENCUESTA_ESTUDIANTE);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                mensajeEncuestaEstudiante = c.getConValor();
            }

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
            return sedeClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgElementoHogar> completeElementosHogar(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"ehoNombre"});
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getEncElementosHogar() != null
                    ? catalogosClient.buscarElementosHogar(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getEncElementosHogar().contains(i))
                            .collect(Collectors.toList())
                    : catalogosClient.buscarElementosHogar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void actualizar(SgEncuestaEstudiante est) {
        try {
            if (est == null) {
                JSFUtils.redirectToIndex();
            } else {
                entidadEnEdicion = est;
                if (entidadEnEdicion.getEncEstudianteFk() != null) {
                    FiltroEstudiante fest = new FiltroEstudiante();
                    fest.setEstPk(entidadEnEdicion.getEncEstudianteFk());
                    fest.setMaxResults(1L);
                    fest.setIncluirCampos(new String[]{
                        "estVersion", "estPersona.perPk", "estPersona.perNie", "estPersona.perSexo.sexNombre", "estPersona.perFechaNacimiento",
                        "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                        "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido",
                        "estUltModFecha"
                    });
                    List<SgEstudiante> listEst = estudianteClient.buscar(fest);
                    if (!listEst.isEmpty()) {
                        estudianteEnEdicion = listEst.get(0);
                        entidadEnEdicion.setEncEstudiante(estudianteEnEdicion);
                        setearCombos();
                        cargarReferente();
                    } else {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRO_ESTUDIANTE), "");
                        return;
                    }
                }
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void setearCombos() {
        this.comboCompaniasTelecomunicacion.setSelectedT(this.entidadEnEdicion.getEncCompaniaInternetResidencial());
        this.comboFuentesAbastecimientoAgua.setSelectedT(this.entidadEnEdicion.getEncFuenteAbastecimientoAguaResidencial());
        this.comboMaterialesPiso.setSelectedT(this.entidadEnEdicion.getEncMaterialPisoResidencial());
        this.comboTipoServicioSanitario.setSelectedT(this.entidadEnEdicion.getEncTipoServicioSanitarioResidencial());
        this.comboZona.setSelectedT(this.entidadEnEdicion.getEncZonaResidencia());
    }

    public void limpiarCombos() {
        this.comboCompaniasTelecomunicacion.setSelected(-1);
        this.comboFuentesAbastecimientoAgua.setSelected(-1);
        this.comboMaterialesPiso.setSelected(-1);
        this.comboTipoServicioSanitario.setSelected(-1);
        this.comboZona.setSelected(-1);
    }

    public void agregarMenorAEncuesta() {
        try {
            menorEdicion.setMenMedioTransporte(comboMediosTransporte.getSelectedT());
            restClient.validar(menorEdicion);
            if (entidadEnEdicion.getEncMenores() == null) {
                entidadEnEdicion.setEncMenores(new ArrayList<>());
            }
            if (entidadEnEdicion.getEncMenores().contains(menorEdicion)) {
                entidadEnEdicion.getEncMenores().set(entidadEnEdicion.getEncMenores().indexOf(menorEdicion), menorEdicion);
            } else {
                entidadEnEdicion.getEncMenores().add(menorEdicion);
            }
            PrimeFaces.current().executeScript("PF('menorDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarMenor(SgMenorEncuestaEstudiante menor) {
        this.entidadEnEdicion.getEncMenores().remove(menor);
    }

    public void agregarMenor() {
        menorEdicion = new SgMenorEncuestaEstudiante();
        menorEdicion.setMenViewPk((new Random()).nextLong());
        limpiarCombosMenor();
    }

    public void limpiarCombosMenor() {
        comboMediosTransporte.setSelected(-1);
        comboSexos.setSelected(-1);
        comboNacionalidad.setSelected(-1);
        comboEstadoCivil.setSelected(-1);
        comboTipoParentesco.setSelected(-1);
    }

    public void editarMenor(SgMenorEncuestaEstudiante menor) {
        limpiarCombosMenor();
        menorEdicion = (SgMenorEncuestaEstudiante) SerializationUtils.clone(menor);
        comboMediosTransporte.setSelectedT(menor.getMenMedioTransporte());
        comboSexos.setSelectedT(menor.getMenSexo());
        comboNacionalidad.setSelectedT(menor.getMenNacionalidad());
        comboEstadoCivil.setSelectedT(menor.getMenEstadoCivil());
        comboTipoParentesco.setSelectedT(menor.getMenTipoParentesco());
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            if (encId == null && estId == null) {
                entidadEnEdicion = null;
                estudianteEnEdicion = null;
                referente = null;
                estudianteNie = null;
                limpiarCombos();
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void comboTipoServicioSanitarioSelected() {
        entidadEnEdicion.setEncTipoServicioSanitarioResidencial(comboTipoServicioSanitario.getSelectedT());
        entidadEnEdicion.setEncTipoServicioSanitarioResidencialOtro(null);
    }

    public void comboFuentesAbastecimientoAguaSelected() {
        entidadEnEdicion.setEncFuenteAbastecimientoAguaResidencial(comboFuentesAbastecimientoAgua.getSelectedT());
        entidadEnEdicion.setEncFuenteAbastecimientoAguaResidencialOtra(null);
    }

    public void comboMaterialesPisoSelected() {
        entidadEnEdicion.setEncMaterialPisoResidencial(comboMaterialesPiso.getSelectedT());
        entidadEnEdicion.setEncMaterialPisoResidencialOtro(null);
    }

    public void comboCompaniasTelecomunicacionSelected() {
        entidadEnEdicion.setEncCompaniaInternetResidencial(comboCompaniasTelecomunicacion.getSelectedT());
    }

    public void comboZonaSelected() {
        entidadEnEdicion.setEncZonaResidencia(comboZona.getSelectedT());
    }

    public void viveConPersonasMenoresSelected() {
        if (BooleanUtils.isFalse(entidadEnEdicion.getEncViveConPersonasMenores())) {
            entidadEnEdicion.setEncMenores(new ArrayList<>());
        }
    }
    
    public void esFamiliarMenorSelected(){
        if (BooleanUtils.isFalse(menorEdicion.getMenEsFamiliar())) {
            comboTipoParentesco.setSelected(-1);
        }
    }

    public void comboEstadoCivilMenorSelected() {
        menorEdicion.setMenEstadoCivil(comboEstadoCivil.getSelectedT());
    }

    public void comboSexoMenorSelected() {
        menorEdicion.setMenSexo(comboSexos.getSelectedT());
    }

    public void comboNacionalidadMenorSelected() {
        menorEdicion.setMenNacionalidad(comboNacionalidad.getSelectedT());
    }

    public void comboParentescoMenorSelected() {
        menorEdicion.setMenTipoParentesco(comboTipoParentesco.getSelectedT());
    }

    public Boolean getRenderIngresarOtraFuenteAbastecimientoDeAgua() {
        return entidadEnEdicion.getEncFuenteAbastecimientoAguaResidencial() != null && entidadEnEdicion.getEncFuenteAbastecimientoAguaResidencial().getFaaPk().equals(Constantes.PK_FUENTE_ABASTECIMIENTO_AGUA_OTRA);
    }

    public Boolean getRenderIngresarOtroMaterialDePiso() {
        return entidadEnEdicion.getEncMaterialPisoResidencial() != null && entidadEnEdicion.getEncMaterialPisoResidencial().getMapPk().equals(Constantes.PK_MATERIAL_PISO_OTRO);
    }

    public Boolean getRenderIngresarOtroServicioSanitario() {
        return entidadEnEdicion.getEncTipoServicioSanitarioResidencial() != null && entidadEnEdicion.getEncTipoServicioSanitarioResidencial().getTssPk().equals(Constantes.PK_SERVICIO_SANITARIO_OTRO);
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getEditable() {
        return editable;
    }

    public Long getEstRev() {
        return estRev;
    }

    public SgEncuestaEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEncuestaEstudiante entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgEstudiante getEstudianteEnEdicion() {
        return estudianteEnEdicion;
    }

    public void setEstudianteEnEdicion(SgEstudiante estudianteEnEdicion) {
        this.estudianteEnEdicion = estudianteEnEdicion;
    }

    public SofisComboG<SgFuenteAbastecimientoAgua> getComboFuentesAbastecimientoAgua() {
        return comboFuentesAbastecimientoAgua;
    }

    public void setComboFuentesAbastecimientoAgua(SofisComboG<SgFuenteAbastecimientoAgua> comboFuentesAbastecimientoAgua) {
        this.comboFuentesAbastecimientoAgua = comboFuentesAbastecimientoAgua;
    }

    public SofisComboG<SgMaterialPiso> getComboMaterialesPiso() {
        return comboMaterialesPiso;
    }

    public void setComboMaterialesPiso(SofisComboG<SgMaterialPiso> comboMaterialesPiso) {
        this.comboMaterialesPiso = comboMaterialesPiso;
    }

    public SofisComboG<SgCompaniaTelecomunicacion> getComboCompaniasTelecomunicacion() {
        return comboCompaniasTelecomunicacion;
    }

    public void setComboCompaniasTelecomunicacion(SofisComboG<SgCompaniaTelecomunicacion> comboCompaniasTelecomunicacion) {
        this.comboCompaniasTelecomunicacion = comboCompaniasTelecomunicacion;
    }

    public SofisComboG<SgTipoServicioSanitario> getComboTipoServicioSanitario() {
        return comboTipoServicioSanitario;
    }

    public void setComboTipoServicioSanitario(SofisComboG<SgTipoServicioSanitario> comboTipoServicioSanitario) {
        this.comboTipoServicioSanitario = comboTipoServicioSanitario;
    }

    public SofisComboG<SgZona> getComboZona() {
        return comboZona;
    }

    public void setComboZona(SofisComboG<SgZona> comboZona) {
        this.comboZona = comboZona;
    }

    public SgAllegado getReferente() {
        return referente;
    }

    public void setReferente(SgAllegado referente) {
        this.referente = referente;
    }

    public Long getEstudianteNie() {
        return estudianteNie;
    }

    public void setEstudianteNie(Long estudianteNie) {
        this.estudianteNie = estudianteNie;
    }

    public SofisComboG<SgMedioTransporte> getComboMediosTransporte() {
        return comboMediosTransporte;
    }

    public void setComboMediosTransporte(SofisComboG<SgMedioTransporte> comboMediosTransporte) {
        this.comboMediosTransporte = comboMediosTransporte;
    }

    public SgMenorEncuestaEstudiante getMenorEdicion() {
        return menorEdicion;
    }

    public void setMenorEdicion(SgMenorEncuestaEstudiante menorEdicion) {
        this.menorEdicion = menorEdicion;
    }

    public Long getEstId() {
        return estId;
    }

    public SofisComboG<SgSexo> getComboSexos() {
        return comboSexos;
    }

    public void setComboSexos(SofisComboG<SgSexo> comboSexos) {
        this.comboSexos = comboSexos;
    }

    public SofisComboG<SgNacionalidad> getComboNacionalidad() {
        return comboNacionalidad;
    }

    public void setComboNacionalidad(SofisComboG<SgNacionalidad> comboNacionalidad) {
        this.comboNacionalidad = comboNacionalidad;
    }

    public SofisComboG<SgEstadoCivil> getComboEstadoCivil() {
        return comboEstadoCivil;
    }

    public void setComboEstadoCivil(SofisComboG<SgEstadoCivil> comboEstadoCivil) {
        this.comboEstadoCivil = comboEstadoCivil;
    }

    public SofisComboG<SgTipoParentesco> getComboTipoParentesco() {
        return comboTipoParentesco;
    }

    public void setComboTipoParentesco(SofisComboG<SgTipoParentesco> comboTipoParentesco) {
        this.comboTipoParentesco = comboTipoParentesco;
    }

    public String getMensajeEncuestaEstudiante() {
        return mensajeEncuestaEstudiante;
    }

    public void setMensajeEncuestaEstudiante(String mensajeEncuestaEstudiante) {
        this.mensajeEncuestaEstudiante = mensajeEncuestaEstudiante;
    }
    
    

}
