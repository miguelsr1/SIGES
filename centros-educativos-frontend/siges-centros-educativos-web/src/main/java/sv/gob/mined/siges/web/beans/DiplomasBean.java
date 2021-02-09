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
import sv.gob.mined.siges.web.dto.SgDiploma;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiploma;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyDiplomaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DiplomaRestClient;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DiplomasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DiplomasBean.class.getName());

    @Inject
    private DiplomaRestClient restClient;
    
    @Inject
    private SedeRestClient restSede;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;
    
    @Inject
    private DiplomadoRestClient diplomadoRestClient;

    private FiltroDiploma filtro = new FiltroDiploma();
    private SgDiploma entidadEnEdicion = new SgDiploma();
    private List<RevHistorico> historialDiploma = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDiplomaDataModel diplomaLazyModel;
    private SgSede sedeSeleccionadaFiltro;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private SofisComboG<SgDiplomado> comboDiplomado;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgMunicipio> comboMunicipio;

    public DiplomasBean() {
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
            filtro.setFirst(new Long(0));
            filtro.setSedePk(sedeSeleccionadaFiltro!=null ? sedeSeleccionadaFiltro.getSedPk():null);
            filtro.setDepartamentoPk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setAnioLectivoId(comboAnioLectivo != null && comboAnioLectivo.getSelectedT() != null ? comboAnioLectivo.getSelectedT().getAlePk() : null);
            filtro.setDiplomadoFk(comboDiplomado != null && comboDiplomado.getSelectedT() != null ? comboDiplomado.getSelectedT().getDipPk() : null);
            filtro.setMunicipioPk(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setIncluirCampos(new String[]{
                "dilSedeFk.sedPk",
                "dilSedeFk.sedCodigo",
                "dilSedeFk.sedNombre",
                "dilSedeFk.sedTipo",
                "dilAnioLectivoFk.aleAnio",
                "dilDiplomadoFk.dipNombre",
                "dilUltModUsuario",
                "dilUltModFecha",
                "dilVersion"
            });
            totalResultados = restClient.buscarTotal(filtro);
            diplomaLazyModel = new LazyDiplomaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroAnioLectivo filtroAnioLectivo = new FiltroAnioLectivo();
            filtroAnioLectivo.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            filtroAnioLectivo.setOrderBy(new String[]{"aleAnio"});
            filtroAnioLectivo.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAniosLectivos = anioLectivoRestClient.buscar(filtroAnioLectivo);
            comboAnioLectivo = new SofisComboG(listAniosLectivos, "aleDescripcion");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroDiplomado filtroD = new FiltroDiplomado();
            filtroD.setHabilitado(Boolean.TRUE);
            filtroD.setIncluirCampos(new String[]{"dipNombre", "dipVersion"});
            List<SgDiplomado> listaDiplomados = diplomadoRestClient.buscar(filtroD);
            comboDiplomado = new SofisComboG(listaDiplomados, "dipNombre");
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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

    private void limpiarCombos() {
        comboAnioLectivo.setSelected(-1);
        comboDiplomado.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        
        comboMunicipio = new SofisComboG();
        comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void limpiar() {
        limpiarCombos();
        sedeSeleccionadaFiltro = null;
        filtro = new FiltroDiploma();
        buscar();
    }


    public void actualizar(SgDiploma var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgDiploma) SerializationUtils.clone(var);
    }

 

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDilPk());
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
            historialDiploma = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
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

    public FiltroDiploma getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDiploma filtro) {
        this.filtro = filtro;
    }

    public SgDiploma getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDiploma entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialDiploma() {
        return historialDiploma;
    }

    public void setHistorialDiploma(List<RevHistorico> historialDiploma) {
        this.historialDiploma = historialDiploma;
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

    public LazyDiplomaDataModel getDiplomaLazyModel() {
        return diplomaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDiplomaDataModel diplomaLazyModel) {
        this.diplomaLazyModel = diplomaLazyModel;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SofisComboG<SgDiplomado> getComboDiplomado() {
        return comboDiplomado;
    }

    public void setComboDiplomado(SofisComboG<SgDiplomado> comboDiplomado) {
        this.comboDiplomado = comboDiplomado;
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

}
