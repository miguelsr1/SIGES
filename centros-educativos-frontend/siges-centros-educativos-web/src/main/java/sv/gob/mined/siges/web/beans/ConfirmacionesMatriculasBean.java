/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgConfirmacionMatriculas;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConfirmacionMatriculas;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyConfirmacionMatriculasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ConfirmacionMatriculasRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ConfirmacionesMatriculasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfirmacionesMatriculasBean.class.getName());

    @Inject
    private ConfirmacionMatriculasRestClient restClient;

    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;
    
    @Inject
    private CatalogosRestClient catalogoClient;

    private FiltroConfirmacionMatriculas filtro = new FiltroConfirmacionMatriculas();
    private SgConfirmacionMatriculas entidadEnEdicion = new SgConfirmacionMatriculas();
    private Integer paginado = 10;
    private Long totalResultados;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private LazyConfirmacionMatriculasDataModel confirmacionMatriculasLazyModel;

    public ConfirmacionesMatriculasBean() {
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
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONFIRMACIONES_MATRICULAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setAnioLectivoFk(this.comboAnioLectivo.getSelectedT() != null ? this.comboAnioLectivo.getSelectedT().getAlePk() : null);
            filtro.setSedeFk(this.sedeSeleccionada != null ? this.sedeSeleccionada.getSedPk() : null);
            filtro.setDepartamentoFk(this.comboDepartamento.getSelectedT() != null ? this.comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setMunicipioFk(this.comboMunicipio.getSelectedT() != null ? this.comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setConfirmada(Boolean.TRUE);
            filtro.setIncluirCampos(new String[]{
                "cmaAnioLectivo.aleAnio",
                "cmaAnioLectivo.alePk",
                "cmaSede.sedPk",
                "cmaSede.sedTipo",
                "cmaSede.sedCodigo",
                "cmaSede.sedNombre",
                "cmaUsuarioConfirmacion",
                "cmaFechaConfirmacion",
                "cmaVersion",
                "cmaFirmada"
            });
            
            totalResultados = restClient.buscarTotal(filtro);
            confirmacionMatriculasLazyModel = new LazyConfirmacionMatriculasDataModel(restClient, filtro, totalResultados, paginado);
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
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroAnioLectivo fc2 = new FiltroAnioLectivo();
            fc2.setOrderBy(new String[]{"aleAnio"});
            fc2.setAscending(new boolean[]{false});
            comboAnioLectivo = new SofisComboG(restAnioLectivo.buscar(fc2), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo.setSelectedT((SgAnioLectivo) this.comboAnioLectivo.getAllTs().get(this.comboAnioLectivo.getAllTs().size() - 1));
            
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> deptos = catalogoClient.buscarDepartamento(fc);
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
                List<SgMunicipio> municipios = catalogoClient.buscarMunicipio(fc);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void limpiar() {
        sedeSeleccionada = null;
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
        comboAnioLectivo.setSelected(-1);
        filtro = new FiltroConfirmacionMatriculas();
        buscar();
    }
    

    public void actualizar(SgConfirmacionMatriculas var) {
        entidadEnEdicion = (SgConfirmacionMatriculas) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }
    
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCmaPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public FiltroConfirmacionMatriculas getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroConfirmacionMatriculas filtro) {
        this.filtro = filtro;
    }

    public SgConfirmacionMatriculas getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgConfirmacionMatriculas entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public LazyConfirmacionMatriculasDataModel getConfirmacionMatriculasLazyModel() {
        return confirmacionMatriculasLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyConfirmacionMatriculasDataModel confirmacionMatriculasLazyModel) {
        this.confirmacionMatriculasLazyModel = confirmacionMatriculasLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
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
    
    

}
