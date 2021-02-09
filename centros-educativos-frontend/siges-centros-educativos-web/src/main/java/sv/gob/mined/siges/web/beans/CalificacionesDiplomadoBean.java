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
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomado;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgModuloDiplomado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumTiposCalificacionDiplomado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionDiplomadoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.ModuloDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionesDiplomadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionesDiplomadoBean.class.getName());

    @Inject
    private CalificacionDiplomadoRestClient restClient;
    
    @Inject
    private SedeRestClient restSede;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private AnioLectivoRestClient anioLectRestClient;
    
    @Inject
    private DiplomadoRestClient diplomadoRestClient;
    
    @Inject
    private ModuloDiplomadoRestClient moduloDiplomadoRestClient;

    private FiltroCalificacionDiplomado filtro = new FiltroCalificacionDiplomado();
    private SgCalificacionDiplomado entidadEnEdicion = new SgCalificacionDiplomado();
    private List<RevHistorico> historialCalificacionDiplomado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionDiplomadoDataModel calificacionDiplomadoLazyModel;
    private SgSede sedeSeleccionadaFiltro;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgAnioLectivo> anioLectivoCombo;
    private SofisComboG<SgDiplomado> comboDiplomado;
    private SofisComboG<SgModuloDiplomado> comboModuloDiplomado;

    public CalificacionesDiplomadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setCadTipoCalificacion(EnumTiposCalificacionDiplomado.ORD);
            filtro.setCalDepartamentoFk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setCalAnioLectivoFk(anioLectivoCombo.getSelectedT() != null ? anioLectivoCombo.getSelectedT().getAlePk(): null);
            filtro.setCalSedeFk(sedeSeleccionadaFiltro!=null ? sedeSeleccionadaFiltro.getSedPk():null);
            filtro.setCalDiplomadoFk(comboDiplomado.getSelectedT()!=null?comboDiplomado.getSelectedT().getDipPk():null);
            filtro.setCalModuloDiplomadoFk(comboModuloDiplomado.getSelectedT()!=null?comboModuloDiplomado.getSelectedT().getMdiPk():null);
            filtro.setIncluirCampos(new String[]{
                "cadSedeFk.sedPk",
                "cadSedeFk.sedCodigo",
                "cadSedeFk.sedNombre",
                "cadSedeFk.sedTipo",
                "cadAnioLectivoFk.aleAnio",
                "cadModuloDiplomado.mdiDiplomado.dipNombre",
                "cadModuloDiplomado.mdiNombre",
                "cadNumeroPeriodo",
                "cadUltModUsuario",
                "cadUltModFecha",
                "cadVersion"
            });
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            calificacionDiplomadoLazyModel = new LazyCalificacionDiplomadoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            anioLectivoCombo = new SofisComboG(listAnio, "aleAnio");
            anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroDiplomado filtroD = new FiltroDiplomado();
            filtroD.setHabilitado(Boolean.TRUE);
            filtroD.setIncluirCampos(new String[]{"dipNombre", "dipVersion"});
            List<SgDiplomado> listaDiplomados = diplomadoRestClient.buscar(filtroD);
            comboDiplomado = new SofisComboG(listaDiplomados, "dipNombre");
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboModuloDiplomado= new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         } catch (Exception ex) {
            Logger.getLogger(PlazasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void seleccionarDiplomado(){
        try{
            comboModuloDiplomado= new SofisComboG();
            comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if(comboDiplomado.getSelectedT()!=null){
                FiltroModuloDiplomado fmd=new FiltroModuloDiplomado();
                fmd.setDiplomadoFk(comboDiplomado.getSelectedT().getDipPk());
                fmd.setIncluirCampos(new String[]{"mdiNombre","mdiVersion"});
                List<SgModuloDiplomado> list=moduloDiplomadoRestClient.buscar(fmd);
                
                comboModuloDiplomado= new SofisComboG(list,"mdiNombre");
                comboModuloDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            Logger.getLogger(PlazasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        sedeSeleccionadaFiltro = null;
        comboDepartamentos.setSelected(-1);
        anioLectivoCombo.setSelected(-1);
        comboDiplomado.setSelected(-1);
        comboModuloDiplomado.setSelected(-1);
        filtro = new FiltroCalificacionDiplomado();
        buscar();
    }


    public void actualizar(SgCalificacionDiplomado var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgCalificacionDiplomado) SerializationUtils.clone(var);
    }


    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCadPk());
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

    public void historial(Long id) {
        try {
            historialCalificacionDiplomado = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
//            if (securityOperation != null) {
//                fil.setSecurityOperation(securityOperation);
//            }

            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
            return restSede.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public FiltroCalificacionDiplomado getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalificacionDiplomado filtro) {
        this.filtro = filtro;
    }

    public SgCalificacionDiplomado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacionDiplomado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCalificacionDiplomado() {
        return historialCalificacionDiplomado;
    }

    public void setHistorialCalificacionDiplomado(List<RevHistorico> historialCalificacionDiplomado) {
        this.historialCalificacionDiplomado = historialCalificacionDiplomado;
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

    public LazyCalificacionDiplomadoDataModel getCalificacionDiplomadoLazyModel() {
        return calificacionDiplomadoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionDiplomadoDataModel calificacionDiplomadoLazyModel) {
        this.calificacionDiplomadoLazyModel = calificacionDiplomadoLazyModel;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> anioLectivoCombo) {
        this.anioLectivoCombo = anioLectivoCombo;
    }

    public SofisComboG<SgDiplomado> getComboDiplomado() {
        return comboDiplomado;
    }

    public void setComboDiplomado(SofisComboG<SgDiplomado> comboDiplomado) {
        this.comboDiplomado = comboDiplomado;
    }

    public SofisComboG<SgModuloDiplomado> getComboModuloDiplomado() {
        return comboModuloDiplomado;
    }

    public void setComboModuloDiplomado(SofisComboG<SgModuloDiplomado> comboModuloDiplomado) {
        this.comboModuloDiplomado = comboModuloDiplomado;
    }

}
