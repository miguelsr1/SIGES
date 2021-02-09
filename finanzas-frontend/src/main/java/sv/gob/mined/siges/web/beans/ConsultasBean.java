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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.chartistjsf.model.chart.PieChartModel;
import org.omnifaces.cdi.Param;
import org.primefaces.event.ItemSelectEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.ResultadoAcumuladorDTO;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.enumerados.EnumAcumulador;
import sv.gob.mined.siges.web.filtros.FiltroAcumulador;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.ConsultasAcumuladoresRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped

/**
 * Bean para la gestión de consultas acumulativas.
 *
 * @author Sofis Solutions
 */
public class ConsultasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConsultasBean.class.getName());

    @Inject
    private ConsultasAcumuladoresRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    @Param(name = "tipoAcumulador")
    private Long tipoAcumulador;

    private List<ResultadoAcumuladorDTO> listaResultado = new ArrayList<>();
    private Integer paginado = 1000;
    private Long totalResultados = 0L;
    private FiltroAcumulador filtro = new FiltroAcumulador();
    private String acumuladorSeleccionado = "1";
    private String criterioSeleccionado = "1";
    private String criterioDetalle = "1";
    private SofisComboG<SgAnioLectivo> anioLectivoCombo = new SofisComboG<>();
    private PieChartModel pieChartModel;

    public ConsultasBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            pieChartModel = new PieChartModel();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de filtro
     */
    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            anioLectivoCombo = new SofisComboG(listAnio, "aleAnio");
            anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos que satisfacen el filtro
     */
    public void buscar() {
        try {
            filtro = new FiltroAcumulador();
            listaResultado = new ArrayList();
            switch (criterioSeleccionado) {
                case "1":
                    filtro.setAcum1(EnumAcumulador.AREA_INVERSION);
                    switch (criterioDetalle) {
                        case "1":
                            filtro.setAcum2(EnumAcumulador.SUB_AREA_INVERSION);
                            break;
                        case "2":
                            filtro.setAcum2(null);
                            break;

                    }
                    break;
                case "2":
                    filtro.setAcum1(EnumAcumulador.RUBRO);
                    break;
                default:
                    break;
            }
            switch (acumuladorSeleccionado) {
                case "1":
                    filtro.setFormulado(Boolean.TRUE);
                    break;
                case "2":
                    filtro.setAprobado(Boolean.TRUE);
                    break;
            }

            listaResultado = restClient.buscar(filtro);
            totalResultados = new Long(listaResultado.size());
            dibujarGrafico();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Renderiza el gráfico en la vista
     */
    public void dibujarGrafico() {
        pieChartModel = new PieChartModel();
        listaResultado.forEach(
                x -> {
                    LOGGER.log(Level.SEVERE, "Acum1=" + x.getAcum1() + "VAlor" + x.getValor() + " valor aprobado" + x.getValorAprobado());
                    if (x.getAcum2() != null && x.getValor() != null) {
                        pieChartModel.addLabel(x.getAcum2());
                    } else {
                        if (x.getAcum1() != null && x.getValor() != null) {
                            pieChartModel.addLabel(x.getAcum1());
                        }
                    }
                }
        );
        listaResultado.forEach(
                x -> {
                    LOGGER.log(Level.SEVERE, "Acum1=" + x.getAcum1() + "VAlor" + x.getValor() + " valor aprobado" + x.getValorAprobado());
                    if (x.getAcum2() != null && x.getValor() != null) {
                        pieChartModel.set(x.getValor());
                    } else {
                        if (x.getAcum1() != null && x.getValor() != null) {
                            pieChartModel.set(x.getValor());
                        }
                    }
                }
        );
        pieChartModel.setShowTooltip(true);

    }

    public void pieItemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected", "Item Value: "
                + pieChartModel.getData().get(event.getItemIndex()));
        FacesContext.getCurrentInstance().addMessage(event.getComponent().getClientId(), msg);
    }

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public List<ResultadoAcumuladorDTO> getListaResultado() {
        return listaResultado;
    }

    public void setListaResultado(List<ResultadoAcumuladorDTO> listaResultado) {
        this.listaResultado = listaResultado;
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

    public FiltroAcumulador getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAcumulador filtro) {
        this.filtro = filtro;
    }

    public String getCriterioSeleccionado() {
        return criterioSeleccionado;
    }

    public void setCriterioSeleccionado(String criterioSeleccionado) {
        this.criterioSeleccionado = criterioSeleccionado;
    }

    public String getAcumuladorSeleccionado() {
        return acumuladorSeleccionado;
    }

    public void setAcumuladorSeleccionado(String acumuladorSeleccionado) {
        this.acumuladorSeleccionado = acumuladorSeleccionado;
    }

    public String getCriterioDetalle() {
        return criterioDetalle;
    }

    public void setCriterioDetalle(String criterioDetalle) {
        this.criterioDetalle = criterioDetalle;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> anioLectivoCombo) {
        this.anioLectivoCombo = anioLectivoCombo;
    }

    public PieChartModel getPieChartModel() {
        return pieChartModel;
    }

    public void setPieChartModel(PieChartModel pieChartModel) {
        this.pieChartModel = pieChartModel;
    }
    // </editor-fold>
}
