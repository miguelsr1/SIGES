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
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOpcion;
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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSiPromotor;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumTipoFiltro;
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
    private Integer paginado = 100;
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
    private SofisComboG<SgSiPromotor> comboPromotor;
    private List<EnumTipoFiltro> tipoFiltroList;
    private MapModel sedeMapModel;
    private Marker marker;
    private Boolean prueba;
    private Boolean renderizarBuscarPorCercania = Boolean.FALSE;
    private EnumTipoFiltro tipoFiltroSelected = EnumTipoFiltro.SEDE;
    private String textoBannerSedes = null;

    public SedesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
            cargarMaps();
            buscarConfiguracion();
            panelAvanzado = false;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void buscarConfiguracion() {
        renderizarBuscarPorCercania = Boolean.FALSE;
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.BOTON_BUSCAR_POR_CERCANIA_PORTAL);
            List<SgConfiguracion> conf = catalogoClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                if (conf.get(0).getConValor().equals("1")) {
                    renderizarBuscarPorCercania = Boolean.TRUE;
                }
            }
            
            fc.setCodigo(Constantes.TEXTO_PORTAL_SEDES_EDUCATIVAS);
            conf = catalogoClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                textoBannerSedes = conf.get(0).getConValor();
            }
            
        } catch (Exception ex) {

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
            filtro.setSedHabilitadaOLegalizada(Boolean.TRUE);
            filtro.setSedSiPromotor(comboPromotor != null ? comboPromotor.getSelectedT() != null ? comboPromotor.getSelectedT().getProPk() : null : null);
            filtro.setIncluirCampos(new String[]{
                "sedNombre",
                "sedCodigo",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirMunicipio.munNombre",
                "sedDireccion.dirCaserioTexto",
                "sedDireccion.dirDireccion",
                "sedDireccion.dirLatitud",
                "sedDireccion.dirLongitud",
                "sedTelefono",
                "sedTipo",
                "sedSedeAdscritaDe.sedPk",
                "sedSedeAdscritaDe.sedTipo"});
            totalResultados = restClient.buscarTotal(filtro);
            sedesLazyModel = new LazySedesDataModel(restClient, filtro, totalResultados, paginado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String estiloSede(SgSede sede) {
        switch (sede.getSedTipo()) {
            case CED_OFI:
                return "tituloitemSedeCEO";
            case CED_PRI:
                return "tituloitemSedeCEP";
            case UBI_EDUC:
                return "tituloitemSedeEducame";

        }
        return "tituloitemSedeCEO";
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

            List<TipoSede> tipoSede = new ArrayList(Arrays.asList(TipoSede.values()));
            comboTiposSede = new SofisComboG(tipoSede, "text");
            comboTiposSede.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTiposSede.ordenar();
            comboTiposSede.setSelectedT(TipoSede.CED_OFI);

            tipoFiltroList = new ArrayList(Arrays.asList(EnumTipoFiltro.values()));

            fc.setOrderBy(new String[]{"proNombre"});
            fc.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            List<SgSiPromotor> listPromotor = catalogoClient.buscarPromotor(fc);
            comboPromotor = new SofisComboG<>(listPromotor, "proNombre");
            comboPromotor.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(SedesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seleccionarTipoFiltro() {
        filtro = new FiltroSedes();
        cargarCombos();
        if (tipoFiltroSelected.equals(EnumTipoFiltro.SI)) {
            comboTiposSede.setSelected(-1);
            filtro.setSedTieneSistemaIntegrado(Boolean.TRUE);
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
                filtroNivel.setOrderBy(new String[]{"nivOrden"});
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

    public void cargarMaps() {
        if (sedesLazyModel != null) {
            /*for(SgSede sede: sedesLazyModel.getWrappedData()){                
                if(sede.getSedDireccion().getDirLatitud()!= null && sede.getSedDireccion().getDirLongitud() != null){
                    LatLng latLng = new LatLng(sede.getSedDireccion().getDirLatitud(), sede.getSedDireccion().getDirLongitud());
                    Marker punto = new Marker(latLng, sede.getSedCodigo()+"-"+sede.getSedCodigoNombre());
                    punto.setDraggable(true);
                    sedeMapModel.addOverlay(punto);
                }                
            }
             */
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

    private void limpiarCombos() {
        cargarCombos();
        cargarCombosAvanzado();
        limpiarCombosAvanzados();
    }

    public String limpiar() {
        filtro = new FiltroSedes();
        limpiarCombos();
        sedesLazyModel = null;
        return null;
    }

    public void seleccionarDepartamento() {
        try {
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
                fc.setOrderBy(new String[]{"cicOrden"});
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
                fc.setOrderBy(new String[]{"modOrden"});
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
                    fGrado.setOrderBy(new String[]{"graOrden"});
                    fGrado.setAscending(new boolean[]{true});
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
                fGrado.setOrderBy(new String[]{"graOrden"});
                fGrado.setAscending(new boolean[]{true});
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

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));
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

    public MapModel getSedeMapModel() {
        return sedeMapModel;
    }

    public void setSedeMapModel(MapModel sedeMapModel) {
        this.sedeMapModel = sedeMapModel;
    }

    public Boolean getPrueba() {
        return prueba;
    }

    public void setPrueba(Boolean prueba) {
        this.prueba = prueba;
    }

    public Boolean getRenderizarBuscarPorCercania() {
        return renderizarBuscarPorCercania;
    }

    public void setRenderizarBuscarPorCercania(Boolean renderizarBuscarPorCercania) {
        this.renderizarBuscarPorCercania = renderizarBuscarPorCercania;
    }

    public List<EnumTipoFiltro> getTipoFiltroList() {
        return tipoFiltroList;
    }

    public void setTipoFiltroList(List<EnumTipoFiltro> tipoFiltroList) {
        this.tipoFiltroList = tipoFiltroList;
    }

    public EnumTipoFiltro getTipoFiltroSelected() {
        return tipoFiltroSelected;
    }

    public void setTipoFiltroSelected(EnumTipoFiltro tipoFiltroSelected) {
        this.tipoFiltroSelected = tipoFiltroSelected;
    }

    public SofisComboG<SgSiPromotor> getComboPromotor() {
        return comboPromotor;
    }

    public void setComboPromotor(SofisComboG<SgSiPromotor> comboPromotor) {
        this.comboPromotor = comboPromotor;
    }

    public String getTextoBannerSedes() {
        return textoBannerSedes;
    }

    public void setTextoBannerSedes(String textoBannerSedes) {
        this.textoBannerSedes = textoBannerSedes;
    }

    
}
