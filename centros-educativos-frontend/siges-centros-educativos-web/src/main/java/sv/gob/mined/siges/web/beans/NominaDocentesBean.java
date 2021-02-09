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
import org.primefaces.PrimeFaces;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.lazymodels.LazyNominaDocenteDataModel;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class NominaDocentesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(NominaDocentesBean.class.getName());

    @Inject
    private PersonalSedeEducativaRestClient restClient;

    @Inject
    private DatoContratacionRestClient datoContraClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    private FiltroPersonalSedeEducativa filtro = new FiltroPersonalSedeEducativa();
    private LazyNominaDocenteDataModel lazyDataModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgSede sedeSeleccionado;
    private Boolean panelAvanzado;
    private String txtFiltroAvanzado = "Búsqueda Avanzada +";
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private List<SgEspecialidad> especialidadesSeleccionadas;
    
    private Boolean incluirAdscritas = Boolean.FALSE;

    public NominaDocentesBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_NOMINA_DOCENTE)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {

            if (sedeSeleccionado == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SEDE_VACIO), "");
                return null;
            }

            List<TipoPersonalSedeEducativa> tipos = new ArrayList<>();
            tipos.add(TipoPersonalSedeEducativa.DOC);
            tipos.add(TipoPersonalSedeEducativa.DOF);
            tipos.add(TipoPersonalSedeEducativa.ALF);
            filtro.setIncluirCampos(new String[]{"psePersona.perNie", "psePersona.perNip", "psePersona.perDui",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre",
                "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perFechaNacimiento"});
            filtro.setPerDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setPerMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setPerCentroEducativo(sedeSeleccionado.getSedPk());
            filtro.setPerTiposEmpleado(tipos);
            filtro.setPersonalActivo(Boolean.TRUE);
            filtro.setBuscarEnSedeAdscrita(incluirAdscritas);
            filtro.setEspecialidades(especialidadesSeleccionadas != null ? (especialidadesSeleccionadas.stream().map(e -> e.getEspPk()).collect(Collectors.toList())) : null);
            totalResultados = restClient.buscarTotal(filtro);
            lazyDataModel = new LazyNominaDocenteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
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

            if (sessionBean.getSedeDefecto() != null) {
                this.sedeSeleccionado = sessionBean.getSedeDefecto();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            sedeSeleccionado = null;
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{true});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
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

    private void limpiarCombos() {

    }

    public String limpiar() {
        filtro = new FiltroPersonalSedeEducativa();
        filtroNombreCompleto = new FiltroNombreCompleto();
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        sedeSeleccionado = null;
        lazyDataModel = null;
        especialidadesSeleccionadas = null;
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

    public List<SgEspecialidad> completeEspecialidad(String query) {
        try {
            FiltroEspecialidad fil = new FiltroEspecialidad();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"espNombre"});
            fil.setAscending(new boolean[]{false});
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

    public FiltroPersonalSedeEducativa getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPersonalSedeEducativa filtro) {
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

    public LazyNominaDocenteDataModel getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyNominaDocenteDataModel lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public PersonalSedeEducativaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(PersonalSedeEducativaRestClient restClient) {
        this.restClient = restClient;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
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

    public SgSede getSedeSeleccionado() {
        return sedeSeleccionado;
    }

    public void setSedeSeleccionado(SgSede sedeSeleccionado) {
        this.sedeSeleccionado = sedeSeleccionado;
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

    public DatoContratacionRestClient getDatoContraClient() {
        return datoContraClient;
    }

    public void setDatoContraClient(DatoContratacionRestClient datoContraClient) {
        this.datoContraClient = datoContraClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public List<SgEspecialidad> getEspecialidadesSeleccionadas() {
        return especialidadesSeleccionadas;
    }

    public void setEspecialidadesSeleccionadas(List<SgEspecialidad> especialidadesSeleccionadas) {
        this.especialidadesSeleccionadas = especialidadesSeleccionadas;
    }

    public Boolean getIncluirAdscritas() {
        return incluirAdscritas;
    }

    public void setIncluirAdscritas(Boolean incluirAdscritas) {
        this.incluirAdscritas = incluirAdscritas;
    }
    
    

}
