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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazySedesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoCalendarioRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.catalogo.SgImplementadora;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class SedesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SedesBean.class.getName());

    @Inject
    private SedeRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private TipoCalendarioRestClient tipoCalendarioRestClient;

    @Inject
    private ServicioEducativoRestClient servicioEducativoClient;

    @Inject
    private NivelRestClient nivelClient;

    @Inject
    private CicloRestClient cicloClient;

    @Inject
    private ModalidadRestClient modalidadClient;

    @Inject
    private OpcionRestClient opcionClient;

    @Inject
    private GradoRestClient gradoClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;

    private FiltroSedes filtro = new FiltroSedes();
    private LazySedesDataModel sedesLazyModel;
    private List<RevHistorico> historialSede = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgMunicipio> comboMunicipios = new SofisComboG<>();
    private SofisComboG<TipoSede> comboTiposSede = new SofisComboG<>();
    private Boolean cerrarFiltro = Boolean.FALSE;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
    private SofisComboG<SgZona> comboZonas;
    private SofisComboG<SgTipoCalendario> tipoCalendarioFiltroCombo;
    private SofisComboG<SgTipoOrganismoAdministrativo> tipoOrganismoAdministrativoCombo;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgCiclo> comboCiclo;
    private SofisComboG<SgModalidad> comboModalidad;
    private List<SgRelModEdModAten> relModEdModAtenSelected;
    private SofisComboG<SgOpcion> comboOpcion;
    private SofisComboG<SgGrado> comboGrado;
    private SofisComboG<SgProgramaEducativo> comboProgramaEducativo;
    private SofisComboG<SgModalidadAtencion> comboModalidadAtencion;
    private SofisComboG<SgSubModalidadAtencion> comboSubModalidad;
    private SofisComboG<SgImplementadora> comboImplementadora;
    private FiltroNombreCompleto filtroNombreCompletoAlfabetizador = new FiltroNombreCompleto();
    private FiltroNombreCompleto filtroNombreCompletoPromotor = new FiltroNombreCompleto();

    public SedesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            filtro.setIncluirAdscritas(Boolean.FALSE);
            buscar();
            panelAvanzado = false;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SEDES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
        }
        cargarCombosAvanzado();
    }

    public String buscar() {
        try {
            filtro.setSedDepartamentoId(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setSedMunicipioId(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            filtro.setSedTipo(comboTiposSede.getSelectedT());
            filtro.setIncluirSiTieneAdscritas(Boolean.TRUE);
            filtro.setSedZonaId((comboZonas != null) ? comboZonas.getSelectedT() != null ? comboZonas.getSelectedT().getZonPk() : null : null);
            filtro.setSedTipoCalendario((tipoCalendarioFiltroCombo != null) ? tipoCalendarioFiltroCombo.getSelectedT() != null ? tipoCalendarioFiltroCombo.getSelectedT().getTcePk() : null : null);
            filtro.setCedTipoOrganismoAdministrativoPk((tipoOrganismoAdministrativoCombo != null) ? tipoOrganismoAdministrativoCombo.getSelectedT() != null ? tipoOrganismoAdministrativoCombo.getSelectedT().getToaPk() : null : null);
            filtro.setSedNivel((comboNivel != null) ? comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null : null);
            filtro.setSedCiclo((comboCiclo != null) ? comboCiclo.getSelectedT() != null ? comboCiclo.getSelectedT().getCicPk() : null : null);
            filtro.setSedModalidadEducativa((comboModalidad != null) ? comboModalidad.getSelectedT() != null ? comboModalidad.getSelectedT().getModPk() : null : null);
            filtro.setSedOpcion((comboOpcion != null) ? comboOpcion.getSelectedT() != null ? comboOpcion.getSelectedT().getOpcPk() : null : null);
            filtro.setSedGrado((comboGrado != null) ? comboGrado.getSelectedT() != null ? comboGrado.getSelectedT().getGraPk() : null : null);
            filtro.setSedProgramaEducativo((comboProgramaEducativo != null) ? comboProgramaEducativo.getSelectedT() != null ? comboProgramaEducativo.getSelectedT().getPedPk() : null : null);
            filtro.setSedModalidadAtencion((comboModalidadAtencion != null) ? comboModalidadAtencion.getSelectedT() != null ? comboModalidadAtencion.getSelectedT().getMatPk() : null : null);
            filtro.setSedSubModalidad((comboSubModalidad != null) ? comboSubModalidad.getSelectedT() != null ? comboSubModalidad.getSelectedT().getSmoPk() : null : null);
            filtro.setSedImplementadora(comboImplementadora.getSelectedT() != null ? comboImplementadora.getSelectedT().getImpPk() : null);
            filtro.setUtilizarFiltrosEnSedePadre(filtro.getIncluirAdscritas()); //Si incluimos adscriptas en dataSecurity, buscamos también en los datos del padre.

            filtro.setIncluirCampos(new String[]{
                "sedNombre",
                "sedCodigo",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirMunicipio.munNombre",
                "sedTelefono",
                "sedTipo",
                "sedSedeAdscritaDe.sedCodigo",
                "sedSedeAdscritaDe.sedNombre",
                "sedSedeAdscritaDe.sedTipo"});

            totalResultados = restClient.buscarTotal(filtro);
            sedesLazyModel = new LazySedesDataModel(restClient, filtro, totalResultados, paginado);
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
            List<SgDepartamento> listaDepartamentos = catalogoClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipios = new SofisComboG<>();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"impOrden"});
            fc.setIncluirCampos(new String[]{"impNombre", "impVersion"});
            List<SgImplementadora> implementadoras = catalogoClient.buscarImplementadora(fc);
            comboImplementadora = new SofisComboG(implementadoras, "impNombre");
            comboImplementadora.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<TipoSede> tipoSede = new ArrayList(Arrays.asList(TipoSede.values()));
            comboTiposSede = new SofisComboG(tipoSede, "text");
            comboTiposSede.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTiposSede.ordenar();
        } catch (Exception ex) {
            Logger.getLogger(SedesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarCombosAvanzado() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});

            if (comboZonas == null) {
                fc.setOrderBy(new String[]{"zonNombre"});
                fc.setIncluirCampos(new String[]{"zonPk", "zonNombre", "zonVersion"});
                List<SgZona> listaZonas = catalogoClient.buscarZona(fc);
                comboZonas = new SofisComboG<>(listaZonas, "zonNombre");
                comboZonas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

            if (tipoCalendarioFiltroCombo == null) {
                fc.setOrderBy(new String[]{"tceNombre"});
                fc.setIncluirCampos(new String[]{"tcePk", "tceNombre", "tceVersion"});
                List<SgTipoCalendario> listCalendario = tipoCalendarioRestClient.buscar(fc);
                tipoCalendarioFiltroCombo = new SofisComboG(listCalendario, "tceNombre");
                tipoCalendarioFiltroCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

            if (tipoOrganismoAdministrativoCombo == null) {
                fc.setOrderBy(new String[]{"toaNombre"});
                fc.setIncluirCampos(new String[]{"toaPk", "toaNombre", "toaVersion"});
                List<SgTipoOrganismoAdministrativo> listaTipoOrg = catalogoClient.buscarTipoOrganismoAdministrativo(fc);
                tipoOrganismoAdministrativoCombo = new SofisComboG<>(listaTipoOrg, "toaNombre");
                tipoOrganismoAdministrativoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

            if (comboNivel == null) {
                FiltroNivel filtroNivel = new FiltroNivel();
                filtroNivel.setOrderBy(new String[]{"nivNombre"});
                filtroNivel.setAscending(new boolean[]{true});
                filtroNivel.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
                List<SgNivel> listaNivel = nivelClient.buscar(filtroNivel);
                comboNivel = new SofisComboG(listaNivel, "nivelOrganizacion");
                comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                comboCiclo = new SofisComboG<>();
                comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                comboModalidad = new SofisComboG<>();
                comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                comboGrado = new SofisComboG<>();
                comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                comboOpcion = new SofisComboG<>();
                comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

            if (comboProgramaEducativo == null) {
                fc.setOrderBy(new String[]{"pedNombre"});
                fc.setIncluirCampos(new String[]{"pedPk", "pedNombre", "pedVersion"});
                List<SgProgramaEducativo> listaPE = catalogoClient.buscarProgramasEducativos(fc);
                comboProgramaEducativo = new SofisComboG(listaPE, "pedNombre");
                comboProgramaEducativo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

            comboModalidadAtencion = new SofisComboG<>();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboSubModalidad = new SofisComboG<>();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void limpiarCombosAvanzados() {
        comboZonas.setSelected(-1);
        tipoCalendarioFiltroCombo.setSelected(-1);
        tipoOrganismoAdministrativoCombo.setSelected(-1);
        comboNivel.setSelected(-1);
        comboProgramaEducativo.setSelected(-1);
        comboModalidadAtencion.setSelected(-1);
        comboSubModalidad.setSelected(-1);
        comboCiclo = new SofisComboG<>();
        comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboModalidad = new SofisComboG<>();
        comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboGrado = new SofisComboG<>();
        comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboOpcion = new SofisComboG<>();
        comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboSubModalidad = new SofisComboG<>();
        comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void filtroNombreAlfabetizadorCompletoSeleccionar(FiltroNombreCompleto filtroNombreAlfabetizador) {
        if (filtroNombreAlfabetizador != null) {
            filtro.setSedAlfAlfabetizadorNombreCompleto(filtroNombreAlfabetizador);
            if (!StringUtils.isBlank(filtroNombreAlfabetizador.getNombreCompleto())) {
                filtro.setSedAlfAlfabetizadorNombre(null);
                
                filtro.setSedAlfPromotorNombre(null);
                filtro.setSedAlfPromotorNombreCompleto(null);
                filtroNombreCompletoPromotor = new FiltroNombreCompleto();
            }
        }

        PrimeFaces.current().ajax().update("form:pnlSearch");
    }
    
    public void limpiarFiltroPromotor(){
        filtro.setSedAlfPromotorNombre(null);
        filtro.setSedAlfPromotorNombreCompleto(null);
        filtroNombreCompletoPromotor = new FiltroNombreCompleto();
    }
    public void limpiarFiltroAlfabetizador(){
        filtro.setSedAlfAlfabetizadorNombre(null);
        filtro.setSedAlfAlfabetizadorNombreCompleto(null);
        filtroNombreCompletoAlfabetizador = new FiltroNombreCompleto();
    }

    public void filtroNombrePromotorCompletoSeleccionar(FiltroNombreCompleto filtroNombrePromotor) {
        if (filtroNombrePromotor != null) {
            filtro.setSedAlfPromotorNombreCompleto(filtroNombrePromotor);
            if (!StringUtils.isBlank(filtroNombrePromotor.getNombreCompleto())) {
                filtro.setSedAlfPromotorNombre(null);

                filtro.setSedAlfAlfabetizadorNombre(null);
                filtro.setSedAlfAlfabetizadorNombreCompleto(null);
                filtroNombreCompletoAlfabetizador = new FiltroNombreCompleto();
            }
        }

        PrimeFaces.current().ajax().update("form:pnlSearch");
    }

    private void limpiarCombos() {
        cargarCombos();
        cargarCombosAvanzado();
        limpiarCombosAvanzados();
    }

    public String limpiar() {
        filtro = new FiltroSedes();
        filtro.setIncluirAdscritas(Boolean.FALSE);
        limpiarCombos();
        sedesLazyModel = null;
        return null;
    }

    public String historial(Long id) {
        try {
            historialSede = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarDepartamento() {
        try {
            comboImplementadora.setSelected(-1);
            if (TipoSede.CIR_ALF.equals(comboTiposSede.getSelectedT())) {
                filtro.setIncluirAdscritas(Boolean.TRUE);
            }

            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());
                filtro.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                List<SgMunicipio> municipios = catalogoClient.buscarMunicipio(filtro);
                comboMunicipios = new SofisComboG(municipios, "munNombre");
                comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarTipo() {
        comboImplementadora.setSelected(-1);
        filtroNombreCompletoAlfabetizador = new FiltroNombreCompleto();
        filtroNombreCompletoPromotor = new FiltroNombreCompleto();
        filtro.setSedAlfAlfabetizadorNombre(null);
        filtro.setSedAlfPromotorNombre(null);
        filtro.setSedAlfAlfabetizadorNombreCompleto(null);
        filtro.setSedAlfPromotorNombreCompleto(null);
    }

    public void seleccionarNivel() {
        try {
            comboCiclo = new SofisComboG();
            comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            if (comboNivel.getSelectedT() != null) {
                FiltroCiclo fc = new FiltroCiclo();
                fc.setOrderBy(new String[]{"cicNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setNivPk(comboNivel.getSelectedT().getNivPk());
                fc.setIncluirCampos(new String[]{"cicPk", "cicNombre", "cicVersion"});

                List<SgCiclo> ciclos = cicloClient.buscar(fc);
                comboCiclo = new SofisComboG(ciclos, "cicNombre");
                comboCiclo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarCiclo() {
        try {
            comboModalidad = new SofisComboG();
            comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            if (comboCiclo.getSelectedT() != null) {
                FiltroModalidad fc = new FiltroModalidad();
                fc.setOrderBy(new String[]{"modNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setCicPk(comboCiclo.getSelectedT().getCicPk());
                fc.setIncluirCampos(new String[]{"modPk", "modNombre", "modVersion"});

                List<SgModalidad> modalidad = modalidadClient.buscar(fc);
                comboModalidad = new SofisComboG(modalidad, "modNombre");
                comboModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (modalidad.size() == 1) {
                    comboModalidad.setSelectedT(modalidad.get(0));
                    seleccionarModalidad();
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModalidad() {
        try {
            comboModalidadAtencion = new SofisComboG();
            comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboOpcion = new SofisComboG();
            comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            relModEdModAtenSelected = new ArrayList<>();

            if (comboModalidad.getSelectedT() != null) {

                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaModalidadEducativa(comboModalidad.getSelectedT().getModPk());
                List<SgModalidadAtencion> listaMA = new ArrayList();
                relModEdModAtenSelected = relModRestClient.buscar(filtroRel);
                for (SgRelModEdModAten rel : relModEdModAtenSelected) {
                    if (!listaMA.contains(rel.getReaModalidadAtencion())) {
                        listaMA.add(rel.getReaModalidadAtencion());
                    }
                }

                comboModalidadAtencion = new SofisComboG(listaMA, "matNombre");
                comboModalidadAtencion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                FiltroOpciones fOpc = new FiltroOpciones();
                fOpc.setOrderBy(new String[]{"opcNombre"});
                fOpc.setAscending(new boolean[]{true});
                fOpc.setOpcModalidadPk(comboModalidad.getSelectedT().getModPk());
                fOpc.setIncluirCampos(new String[]{"opcPk", "opcNombre", "opcVersion"});
                List<SgOpcion> opcion = opcionClient.buscar(fOpc);
                comboOpcion = new SofisComboG(opcion, "opcNombre");
                comboOpcion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                if (listaMA.size() == 1) {
                    comboModalidadAtencion.setSelectedT(listaMA.get(0));
                    seleccionarModalidadAtencion();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModalidadAtencion() {
        try {
            comboSubModalidad = new SofisComboG();
            comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboModalidadAtencion.getSelectedT() != null) {

                //Verificar si esta modalidad de atención tiene submodalidades
                List<SgSubModalidadAtencion> listaSubMod = new ArrayList<>();
                SgModalidadAtencion modAtencionSelect = comboModalidadAtencion.getSelectedT();

                for (SgRelModEdModAten relAux : relModEdModAtenSelected) {
                    if (relAux.getReaSubModalidadAtencion() != null && modAtencionSelect.equals(relAux.getReaModalidadAtencion()) && BooleanUtils.isTrue(relAux.getReaSubModalidadAtencion().getSmoHabilitado())) {
                        listaSubMod.add(relAux.getReaSubModalidadAtencion());
                    }
                }
                if (!listaSubMod.isEmpty()) {
                    comboSubModalidad = new SofisComboG(listaSubMod, "smoNombre");
                    comboSubModalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (listaSubMod.size() == 1) {
                        comboSubModalidad.setSelectedT(listaSubMod.get(0));
                        seleccionarSubModalidadAtencion();
                    }

                } else {
                    FiltroGrado fGrado = new FiltroGrado();
                    fGrado.setModAtencionPk(comboModalidadAtencion.getSelectedT().getMatPk());
                    fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                    fGrado.setHabilitado(Boolean.TRUE);
                    fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                    List<SgGrado> grado = gradoClient.buscar(fGrado);
                    comboGrado = new SofisComboG(grado, "graNombre");
                    comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    if (grado.size() == 1) {
                        comboGrado.setSelectedT(grado.get(0));
                    }
                }

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSubModalidadAtencion() {
        try {
            comboGrado = new SofisComboG();
            comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboSubModalidad.getSelectedT() != null) {
                FiltroGrado fGrado = new FiltroGrado();
                fGrado.setSubModAtencionPk(comboSubModalidad.getSelectedT().getSmoPk());
                fGrado.setModPk(comboModalidad.getSelectedT().getModPk());
                fGrado.setIncluirCampos(new String[]{"graNombre", "graVersion"});
                List<SgGrado> grado = gradoClient.buscar(fGrado);
                comboGrado = new SofisComboG(grado, "graNombre");
                comboGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (grado.size() == 1) {
                    comboGrado.setSelectedT(grado.get(0));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public Boolean verEditar(SgSede sed) {
        if (sed.getSedTipo() != null) {
            switch (sed.getSedTipo()) {
                case CED_OFI:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CENTRO_EDUCATIVO_OFICIAL)) {
                        return true;
                    }
                    break;
                case CED_PRI:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CENTRO_EDUCATIVO_OFICIAL)) {
                        return true;
                    }
                    break;
                case CIR_ALF:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_ALFABETIZACION)) {
                        return true;
                    }
                case CIR_INF:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_INFANCIA)) {
                        return true;
                    }
                    break;
                case UBI_EDUC:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_EDUCAME)) {
                        return true;
                    }
                default:
                    break;
            }
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="getter and setter">
    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public FiltroSedes getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSedes filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorialSede() {
        return historialSede;
    }

    public void setHistorialSede(List<RevHistorico> historialSede) {
        this.historialSede = historialSede;
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

    public LazySedesDataModel getSedesLazyModel() {
        return sedesLazyModel;
    }

    public void setSedesLazyModel(LazySedesDataModel sedesLazyModel) {
        this.sedesLazyModel = sedesLazyModel;
    }

    public SofisComboG<SgZona> getComboZonas() {
        return comboZonas;
    }

    public void setComboZonas(SofisComboG<SgZona> comboZonas) {
        this.comboZonas = comboZonas;
    }

    public CatalogosRestClient getCatalogoClient() {
        return catalogoClient;
    }

    public void setCatalogoClient(CatalogosRestClient catalogoClient) {
        this.catalogoClient = catalogoClient;
    }

    public SofisComboG<TipoSede> getComboTiposSede() {
        return comboTiposSede;
    }

    public void setComboTiposSede(SofisComboG<TipoSede> comboTiposSede) {
        this.comboTiposSede = comboTiposSede;
    }

    public Boolean getCerrarFiltro() {
        return cerrarFiltro;
    }

    public void setCerrarFiltro(Boolean cerrarFiltro) {
        this.cerrarFiltro = cerrarFiltro;
    }

    public SedeRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SedeRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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

    public ServicioEducativoRestClient getServicioEducativoClient() {
        return servicioEducativoClient;
    }

    public void setServicioEducativoClient(ServicioEducativoRestClient servicioEducativoClient) {
        this.servicioEducativoClient = servicioEducativoClient;
    }

    public SofisComboG<SgTipoCalendario> getTipoCalendarioFiltroCombo() {
        return tipoCalendarioFiltroCombo;
    }

    public void setTipoCalendarioFiltroCombo(SofisComboG<SgTipoCalendario> tipoCalendarioFiltroCombo) {
        this.tipoCalendarioFiltroCombo = tipoCalendarioFiltroCombo;
    }

    public SofisComboG<SgTipoOrganismoAdministrativo> getTipoOrganismoAdministrativoCombo() {
        return tipoOrganismoAdministrativoCombo;
    }

    public void setTipoOrganismoAdministrativoCombo(SofisComboG<SgTipoOrganismoAdministrativo> tipoOrganismoAdministrativoCombo) {
        this.tipoOrganismoAdministrativoCombo = tipoOrganismoAdministrativoCombo;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgCiclo> getComboCiclo() {
        return comboCiclo;
    }

    public void setComboCiclo(SofisComboG<SgCiclo> comboCiclo) {
        this.comboCiclo = comboCiclo;
    }

    public SofisComboG<SgModalidad> getComboModalidad() {
        return comboModalidad;
    }

    public void setComboModalidad(SofisComboG<SgModalidad> comboModalidad) {
        this.comboModalidad = comboModalidad;
    }

    public SofisComboG<SgOpcion> getComboOpcion() {
        return comboOpcion;
    }

    public void setComboOpcion(SofisComboG<SgOpcion> comboOpcion) {
        this.comboOpcion = comboOpcion;
    }

    public SofisComboG<SgGrado> getComboGrado() {
        return comboGrado;
    }

    public void setComboGrado(SofisComboG<SgGrado> comboGrado) {
        this.comboGrado = comboGrado;
    }

    public SofisComboG<SgProgramaEducativo> getComboProgramaEducativo() {
        return comboProgramaEducativo;
    }

    public void setComboProgramaEducativo(SofisComboG<SgProgramaEducativo> comboProgramaEducativo) {
        this.comboProgramaEducativo = comboProgramaEducativo;
    }

    public SofisComboG<SgModalidadAtencion> getComboModalidadAtencion() {
        return comboModalidadAtencion;
    }

    public void setComboModalidadAtencion(SofisComboG<SgModalidadAtencion> comboModalidadAtencion) {
        this.comboModalidadAtencion = comboModalidadAtencion;
    }

    public SofisComboG<SgSubModalidadAtencion> getComboSubModalidad() {
        return comboSubModalidad;
    }

    public void setComboSubModalidad(SofisComboG<SgSubModalidadAtencion> comboSubModalidad) {
        this.comboSubModalidad = comboSubModalidad;
    }

    public SofisComboG<SgImplementadora> getComboImplementadora() {
        return comboImplementadora;
    }

    public void setComboImplementadora(SofisComboG<SgImplementadora> comboImplementadora) {
        this.comboImplementadora = comboImplementadora;
    }

    public FiltroNombreCompleto getFiltroNombreCompletoAlfabetizador() {
        return filtroNombreCompletoAlfabetizador;
    }

    public void setFiltroNombreCompletoAlfabetizador(FiltroNombreCompleto filtroNombreCompletoAlfabetizador) {
        this.filtroNombreCompletoAlfabetizador = filtroNombreCompletoAlfabetizador;
    }

    public FiltroNombreCompleto getFiltroNombreCompletoPromotor() {
        return filtroNombreCompletoPromotor;
    }

    public void setFiltroNombreCompletoPromotor(FiltroNombreCompleto filtroNombreCompletoPromotor) {
        this.filtroNombreCompletoPromotor = filtroNombreCompletoPromotor;
    }

}
