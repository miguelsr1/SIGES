/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.centros_educativos.SgDetallePlanEscolar;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.DetallePlanEscolarRestClient;
import sv.gob.mined.siges.web.restclient.FuenteFinanciamientoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ActividadesSistemaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ActividadesSistemaBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private DetallePlanEscolarRestClient detallePlanEscolarRestClient;

    @Inject
    private FuenteFinanciamientoRestClient fuenteFinanciamientoRestClient;
    
    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    private FiltroDetallePlanEscolar filtro = new FiltroDetallePlanEscolar();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SgSistemaIntegrado sistemaIntegrado;
    private List<RevHistorico> historialEntidad = new ArrayList();
    private List<SgDetallePlanEscolar> listaDetallesPlanEscolar;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;

    public ActividadesSistemaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sistemaIntegrado = (SgSistemaIntegrado) request.getAttribute("sistemaIntegrado");
            if (sistemaIntegrado != null) {
                cargarCombos();
                buscar();
            } else {
                throw new Exception("No sistema integrado found");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {
        try {
            // combo fuente financiamiento
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"ffiNombre"});
            fc.setIncluirCampos(new String[]{"ffiNombre", "ffiVersion"});
            List<SgFuenteFinanciamiento> listaFuenteFinanciamiento;
            listaFuenteFinanciamiento = fuenteFinanciamientoRestClient.buscar(fc);
            comboFuenteFinanciamiento = new SofisComboG(listaFuenteFinanciamiento, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroAnioLectivo fc2 = new FiltroAnioLectivo();
            fc2.setOrderBy(new String[]{"aleAnio"});
            fc2.setAscending(new boolean[]{false});            
            comboAnioLectivo = new SofisComboG(restAnioLectivo.buscar(fc2), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivo.setSelectedT((SgAnioLectivo) this.comboAnioLectivo.getAllTs().get(this.comboAnioLectivo.getAllTs().size() - 1));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            // cargar actividades asociadas a las sedes con plan escolar
            filtro = new FiltroDetallePlanEscolar();
            filtro.setPlanEscolarHabilitado(Boolean.TRUE);
            filtro.setSistemaPk(this.sistemaIntegrado.getSinPk());
            filtro.setAnioFk(comboAnioLectivo.getSelectedT()!=null?comboAnioLectivo.getSelectedT().getAlePk():null);
            filtro.setIncluirCampos(new String[]{
                "dpePlanEscolarAnual",
                "dpeActividad",
                "dpeResponsables",
                "dpeRecursos",
                "dpeRecursos.rcsNombre",
                "dpeCostoEstimado",
                "dpeFuenteFinanciamiento",
                "dpeFuenteFinanciamiento.ffiNombre",
                "dpeFechaInicio",
                "dpeFechaFin",
                "dpePlanEscolarAnual.peaAnioLectivo.aleAnio",
                "dpePlanEscolarAnual.peaPk",
                "dpePlanEscolarAnual.peaSede.sedCodigo",
                "dpePlanEscolarAnual.peaSede.sedNombre",
                "dpePlanEscolarAnual.peaSede.sedTelefono",
                "dpePlanEscolarAnual.peaSede.sedDireccion.dirDepartamento.depNombre",
                "dpePlanEscolarAnual.peaSede.sedDireccion.dirMunicipio.munNombre",
                "dpeVersion"
            });
            filtro.setFuenteFinanciamientoPk(this.comboFuenteFinanciamiento.getSelectedT() != null ? this.comboFuenteFinanciamiento.getSelectedT().getFfiPk() : null);
            filtro.setSedePk(this.sedeSeleccionada != null ? this.sedeSeleccionada.getSedPk() : null);
            // fin filtros            
            listaDetallesPlanEscolar = detallePlanEscolarRestClient.buscar(filtro);
            totalResultados = Long.valueOf(listaDetallesPlanEscolar.size());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void limpiar() {
        comboAnioLectivo.setSelected(-1);
        filtro = new FiltroDetallePlanEscolar();
        this.sedeSeleccionada = null;
        this.comboFuenteFinanciamiento.setSelected(-1);
        this.listaDetallesPlanEscolar = null;
        this.totalResultados = 0L;
        buscar();
    }

    public List<SgSede> completeSede(String query) {
        return Optional.ofNullable(listaDetallesPlanEscolar
                .stream()
                .map(a -> a.getDpePlanEscolarAnual().getPeaSede())
                .distinct()
                .filter(s -> s.getSedCodigo().contains(query) || s.getSedNombreBusqueda().contains(query))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
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

    public List<SgDetallePlanEscolar> getListaDetallesPlanEscolar() {
        return listaDetallesPlanEscolar;
    }

    public void setListaDetallesPlanEscolar(List<SgDetallePlanEscolar> listaDetallesPlanEscolar) {
        this.listaDetallesPlanEscolar = listaDetallesPlanEscolar;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }
    
}
