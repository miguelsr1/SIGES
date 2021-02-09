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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyControlAsistenciaPersonalCabezalDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaPersonalCabezalRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ControlesAsistenciaPersonalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ControlesAsistenciaPersonalBean.class.getName());

    @Inject
    private ControlAsistenciaPersonalCabezalRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SeccionRestClient restSeccion;
    
    @Inject
    private SedeRestClient restSede;

    private FiltroControlAsistenciaPersonalCabezal filtro = new FiltroControlAsistenciaPersonalCabezal();
    private LazyControlAsistenciaPersonalCabezalDataModel controlAsistenciaPersonalCabezalLazyModel;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private List<RevHistorico> historialControlAsistenciaPersonalCabezal = new ArrayList();
    private SgControlAsistenciaPersonalCabezal entidadEnEdicion = new SgControlAsistenciaPersonalCabezal();
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgSede sedeSeleccionada;

    public ControlesAsistenciaPersonalBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void forceInit() {
        //Utilizado para forzar init de ControlesAsistenciaPersonalBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONTROL_ASISTENCIA_PERSONAL)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setFirst(new Long(0));

            filtro.setCpcSede(sedeSeleccionada!=null?sedeSeleccionada.getSedPk():null);
            filtro.setCpcDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setCpcMunicipio(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setIncluirCampos(new String[]{"cpcPk" ,"cpcPersonalPresente","cpcPersonalEnLista", 
            "cpcPersonalAusentesJustificados", "cpcPersonalAusentesSinJustificar",
            "cpcSede.sedDireccion.dirDepartamento.depNombre",
            "cpcSede.sedDireccion.dirMunicipio.munNombre",
            "cpcSede.sedTipo",
            "cpcSede.sedNombre",
            "cpcSede.sedCodigo",
            "cpcFecha",
            "cpcUltModUsuario", "cpcVersion"});
            
            totalResultados = restClient.buscarTotal(filtro);
            controlAsistenciaPersonalCabezalLazyModel = new LazyControlAsistenciaPersonalCabezalDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> deptos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(deptos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

          
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamento() {
        try {

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboDepartamento.getSelectedT() != null) {

                FiltroMunicipio fc = new FiltroMunicipio();
                fc.setOrderBy(new String[]{"munNombre"});
                fc.setAscending(new boolean[]{true});
                fc.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fc.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                
                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fc);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
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

    private void limpiarCombos() {

    }


    public void limpiar() {
        filtro = new FiltroControlAsistenciaPersonalCabezal();
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        sedeSeleccionada = null;
        buscar();
    }

    public String historial(Long id) {
        try {
            historialControlAsistenciaPersonalCabezal = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
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

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public ControlAsistenciaPersonalCabezalRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ControlAsistenciaPersonalCabezalRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroControlAsistenciaPersonalCabezal getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroControlAsistenciaPersonalCabezal filtro) {
        this.filtro = filtro;
    }

    public LazyControlAsistenciaPersonalCabezalDataModel getControlAsistenciaPersonalCabezalLazyModel() {
        return controlAsistenciaPersonalCabezalLazyModel;
    }

    public void setControlAsistenciaPersonalCabezalLazyModel(LazyControlAsistenciaPersonalCabezalDataModel controlAsistenciaPersonalCabezalLazyModel) {
        this.controlAsistenciaPersonalCabezalLazyModel = controlAsistenciaPersonalCabezalLazyModel;
    }

    public SgControlAsistenciaPersonalCabezal getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgControlAsistenciaPersonalCabezal entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialControlAsistenciaPersonalCabezal() {
        return historialControlAsistenciaPersonalCabezal;
    }

    public void setHistorialControlAsistenciaPersonalCabezal(List<RevHistorico> historialControlAsistenciaPersonalCabezal) {
        this.historialControlAsistenciaPersonalCabezal = historialControlAsistenciaPersonalCabezal;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SeccionRestClient getRestSeccion() {
        return restSeccion;
    }

    public void setRestSeccion(SeccionRestClient restSeccion) {
        this.restSeccion = restSeccion;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
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

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }


}
