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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyOrganismoAdministracionEscolarDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EvaluacionesOrganismosAdministracionEscolarBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EvaluacionesOrganismosAdministracionEscolarBean.class.getName());

    @Inject
    private OrganismoAdministracionEscolarRestClient restClient;
    
    @Inject
    private CatalogosRestClient catalogoClient;    
    
    @Inject
    private SedeRestClient restSede;  

    @Inject
    private SessionBean sessionBean;  

    private FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
    private SgOrganismoAdministracionEscolar entidadEnEdicion = new SgOrganismoAdministracionEscolar();
    private List<RevHistorico> historialOrganismoAdministracionEscolar = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyOrganismoAdministracionEscolarDataModel organismoAdministracionEscolarLazyModel;
    private SofisComboG<TipoSede> comboTiposSede = new SofisComboG<>();
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgMunicipio> comboMunicipios = new SofisComboG<>();
    private SgSede sedeSeleccionada;
    private SofisComboG<EnumEstadoOrganismoAdministrativo> comboEstado = new SofisComboG<>();
    private SofisComboG<SgTipoOrganismoAdministrativo> comboTipoOAE = new SofisComboG<>();

    public EvaluacionesOrganismosAdministracionEscolarBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setOaeTipoSede(comboTiposSede.getSelectedT());
            filtro.setOaeEstado(comboEstado.getSelectedT());
            filtro.setOaeDepartamento(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setOaeMunicipio(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            filtro.setOaeSedeFk(sedeSeleccionada!=null?sedeSeleccionada.getSedPk():null);
            filtro.setOaeSinEstado(EnumEstadoOrganismoAdministrativo.ELABORACION);
            if(comboTipoOAE.getSelectedT() != null){
                filtro.setOaeTipoOAE(comboTipoOAE.getSelectedT().getToaPk());
            }else{
                filtro.setOaeTipoOAE(null);
            }
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            organismoAdministracionEscolarLazyModel = new LazyOrganismoAdministracionEscolarDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            List<TipoSede> tipoSede = new ArrayList(Arrays.asList(TipoSede.values()));
            comboTiposSede = new SofisComboG(tipoSede, "text");
            comboTiposSede.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTiposSede.ordenar();
            
            List<EnumEstadoOrganismoAdministrativo> tipoEnum= new ArrayList(Arrays.asList(EnumEstadoOrganismoAdministrativo.values()));
            tipoEnum.remove(EnumEstadoOrganismoAdministrativo.ELABORACION);
            comboEstado=new SofisComboG(tipoEnum, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstado.ordenar();
            
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
            
            FiltroCodiguera filtroTipo = new FiltroCodiguera();
            filtroTipo.setIncluirCampos(new String[]{"toaNombre", "toaVersion"});
            filtroTipo.setHabilitado(Boolean.TRUE);
            comboTipoOAE = new SofisComboG<>(catalogoClient.buscarTipoOrganismoAdministrativo(filtroTipo),"toaNombre");
            comboTipoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        
    }

    public void limpiar() {
        filtro = new FiltroOrganismoAdministrativoEscolar();
        comboTiposSede.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        comboMunicipios = new SofisComboG<>();
        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboEstado.setSelected(-1);
        sedeSeleccionada = null;
        comboTipoOAE = new SofisComboG<>();
        cargarCombos();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgOrganismoAdministracionEscolar();
    }

    public void actualizar(SgOrganismoAdministracionEscolar var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgOrganismoAdministracionEscolar) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getOaePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void deseleccionarSede(AjaxBehaviorEvent event) {
        this.sedeSeleccionada = null;
        filtro.setOaeSedeFk(null);
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
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialOrganismoAdministracionEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public FiltroOrganismoAdministrativoEscolar getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroOrganismoAdministrativoEscolar filtro) {
        this.filtro = filtro;
    }

    public SgOrganismoAdministracionEscolar getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgOrganismoAdministracionEscolar entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialOrganismoAdministracionEscolar() {
        return historialOrganismoAdministracionEscolar;
    }

    public void setHistorialOrganismoAdministracionEscolar(List<RevHistorico> historialOrganismoAdministracionEscolar) {
        this.historialOrganismoAdministracionEscolar = historialOrganismoAdministracionEscolar;
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

    public LazyOrganismoAdministracionEscolarDataModel getOrganismoAdministracionEscolarLazyModel() {
        return organismoAdministracionEscolarLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyOrganismoAdministracionEscolarDataModel organismoAdministracionEscolarLazyModel) {
        this.organismoAdministracionEscolarLazyModel = organismoAdministracionEscolarLazyModel;
    }

    public SofisComboG<TipoSede> getComboTiposSede() {
        return comboTiposSede;
    }

    public void setComboTiposSede(SofisComboG<TipoSede> comboTiposSede) {
        this.comboTiposSede = comboTiposSede;
    }

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

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<EnumEstadoOrganismoAdministrativo> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoOrganismoAdministrativo> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgTipoOrganismoAdministrativo> getComboTipoOAE() {
        return comboTipoOAE;
    }

    public void setComboTipoOAE(SofisComboG<SgTipoOrganismoAdministrativo> comboTipoOAE) {
        this.comboTipoOAE = comboTipoOAE;
    }

}
