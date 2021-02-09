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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Expression;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgFormula;
import sv.gob.mined.siges.web.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFormula;
import sv.gob.mined.siges.web.lazymodels.LazyFormulaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.FormulaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import org.mariuszgromada.math.mxparser.Function;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class FormulaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FormulaBean.class.getName());

    @Inject
    private FormulaRestClient restClient;

    private FiltroFormula filtro = new FiltroFormula();
    private SgFormula entidadEnEdicion = new SgFormula();
    private List<SgFormula> historialFormula = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyFormulaDataModel formulaLazyModel;
    private SofisComboG<EnumTipoFormula> comboTipoFormula;
    private SofisComboG<EnumTipoFormula> comboTipoFormulaFiltro;

    public FormulaBean() {
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
            filtro.setTipoFormula(comboTipoFormulaFiltro.getSelectedT());
            totalResultados = restClient.buscarTotal(filtro);
            formulaLazyModel = new LazyFormulaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<EnumTipoFormula> estados = new ArrayList(Arrays.asList(EnumTipoFormula.values()));
        comboTipoFormula = new SofisComboG(estados, "text");
        comboTipoFormula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboTipoFormulaFiltro = new SofisComboG(estados, "text");
        comboTipoFormulaFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {
        comboTipoFormula.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroFormula();
        comboTipoFormulaFiltro.setSelected(-1);
        buscar();
    }

    public List<SgFormula> completeFormula(String query) {
        try {
            FiltroFormula fil = new FiltroFormula();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setDescartarPk(entidadEnEdicion.getFomPk());
            //fil.setTipoFormula(comboTipoFormula.getSelectedT());
            fil.setDescartarSubFormulasPk(entidadEnEdicion.getFomPk());
            //fil.setTieneSubFormula(Boolean.FALSE);
            fil.setOrderBy(new String[]{"fomNombre"});
            fil.setIncluirSubformulas(Boolean.TRUE);
            fil.setAscending(new boolean[]{false});
            return this.entidadEnEdicion.getFomSubFormula() != null
                    ? restClient.buscar(fil).stream()
                            .filter(i -> !this.entidadEnEdicion.getFomSubFormula().contains(i))
                            .collect(Collectors.toList())
                    : restClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void checkSyntaxFormula() {
        if (!StringUtils.isBlank(entidadEnEdicion.getFomTextoLargo())) {
            Function f = new Function(entidadEnEdicion.getFomTextoLargo());
            String parteDefinicion;

            String[] formulaSeparada = entidadEnEdicion.getFomTextoLargo().split("=", 2);
            parteDefinicion = formulaSeparada[0];
            parteDefinicion = parteDefinicion.trim();

            Expression expresion = new Expression(parteDefinicion, f);

            if (entidadEnEdicion.getFomSubFormula() != null) {
                for (SgFormula subform : entidadEnEdicion.getFomSubFormula()) {
                   // Function subf = new Function(subform.getFomTextoLargo());
                    f.addDefinitions(cargarSubformula(subform));
                }
            }

            if (f.checkSyntax()) {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP, "Fórmula correcta", "");
                return;
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Fórmula incorrecta: " + f.getErrorMessage(), "");
                return;
            }
        }
        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP, "Fórmula correcta", "");
    }
    
    private Function cargarSubformula(SgFormula formula){
        Function f = new Function(formula.getFomTextoLargo());
        if(formula.getFomSubFormula()!=null && formula.getFomTienSubformula()){
            for(SgFormula fr:formula.getFomSubFormula()){
                f.addDefinitions(cargarSubformula(fr));
            }
        }
        return f;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgFormula();
        entidadEnEdicion.setFomSubFormula(new ArrayList());
    }

    public void actualizar(SgFormula var) {
        try {
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            FiltroFormula ff=new FiltroFormula();
            ff.setIncluirSubformulas(Boolean.TRUE);
            ff.setFormulaPk(var.getFomPk());
            List<SgFormula> listForm=restClient.buscar(ff);
            entidadEnEdicion = listForm.get(0);
            comboTipoFormula.setSelectedT(entidadEnEdicion.getFomTipoFormula());
            if (entidadEnEdicion.getFomSubFormula() == null) {
                entidadEnEdicion.setFomSubFormula(new ArrayList());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionTipoFormula(){
        entidadEnEdicion.setFomSubFormula(new ArrayList());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setFomTienSubformula(Boolean.FALSE);
            if(entidadEnEdicion.getFomSubFormula()!=null && !entidadEnEdicion.getFomSubFormula().isEmpty()){
                entidadEnEdicion.setFomTienSubformula(Boolean.TRUE);
            }
            entidadEnEdicion.setFomTipoFormula(comboTipoFormula.getSelectedT());
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
            restClient.eliminar(entidadEnEdicion.getFomPk());
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
            historialFormula = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroFormula getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroFormula filtro) {
        this.filtro = filtro;
    }

    public SgFormula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgFormula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgFormula> getHistorialFormula() {
        return historialFormula;
    }

    public void setHistorialFormula(List<SgFormula> historialFormula) {
        this.historialFormula = historialFormula;
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

    public LazyFormulaDataModel getFormulaLazyModel() {
        return formulaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyFormulaDataModel formulaLazyModel) {
        this.formulaLazyModel = formulaLazyModel;
    }

    public SofisComboG<EnumTipoFormula> getComboTipoFormula() {
        return comboTipoFormula;
    }

    public void setComboTipoFormula(SofisComboG<EnumTipoFormula> comboTipoFormula) {
        this.comboTipoFormula = comboTipoFormula;
    }

    public FormulaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(FormulaRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<EnumTipoFormula> getComboTipoFormulaFiltro() {
        return comboTipoFormulaFiltro;
    }

    public void setComboTipoFormulaFiltro(SofisComboG<EnumTipoFormula> comboTipoFormulaFiltro) {
        this.comboTipoFormulaFiltro = comboTipoFormulaFiltro;
    }

}
