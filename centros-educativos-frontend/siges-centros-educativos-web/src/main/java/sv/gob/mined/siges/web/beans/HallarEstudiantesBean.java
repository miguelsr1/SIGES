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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.lazymodels.LazyEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class HallarEstudiantesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HallarEstudiantesBean.class.getName());

    @Inject
    private EstudianteRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    private FiltroEstudiante filtro = new FiltroEstudiante();
    private LazyEstudianteDataModel estudianteLazyModel;
    private List<RevHistorico> historial = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    
    private Boolean buscarMatAbierta = Boolean.TRUE;
    


    public HallarEstudiantesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            filtro.setHabilitado(Boolean.TRUE);
            validarAcceso();
            cargarCombos();
           
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_HALLAR_ESTUDIANTES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setIncluirCampos(new String[]{
                "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedNombre",
                "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedCodigo",
                "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedVersion",
                "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipo",
                "estPersona.perNie",
                "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido", "estPersona.perFechaNacimiento",
                "estPersona.perSexo.sexNombre", "estHabilitado", "estPersona.perHabilitado",
                "estUltimaMatricula.matMotivoRetiro.mreDefinitivo", "estUltimaMatricula.matPk",
                "estUltimaMatricula.matEstado", "estUltimaMatricula.matMotivoRetiro.mreCambioSecc",
                "estUltimaMatricula.matMotivoRetiro.mreTraslado"});
            filtro.setEstEstadoMatricula(buscarMatAbierta ? EnumMatriculaEstado.ABIERTO : null);
            filtro.setSecurityOperation(null);
            totalResultados = restClient.buscarTotal(filtro);

            estudianteLazyModel = new LazyEstudianteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    private void limpiarCombos() {
        if (this.filtrosSeccion.getFiltro() != null) {
            this.filtrosSeccion.limpiarCombos();
        }
    }


    public String limpiar() {
        limpiarCombos();
        filtro = new FiltroEstudiante();
        buscarMatAbierta = Boolean.FALSE;
        filtro.setHabilitado(Boolean.TRUE);
        filtroNombreCompleto = new FiltroNombreCompleto();
        estudianteLazyModel = null;
        return null;
    }

    public void inicializarFiltrosSeccion() {
        if (this.filtrosSeccion.getFiltro() == null) {
            this.filtrosSeccion.setFiltro(this.filtro);
            this.filtrosSeccion.cargarCombos();
        }
    }

    public String cargarHistorial(Long id) {
        try {
            historial = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
    

    public FiltroEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstudiante filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
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

    public LazyEstudianteDataModel getEstudianteLazyModel() {
        return estudianteLazyModel;
    }

    public void setEstudianteLazyModel(LazyEstudianteDataModel estudianteLazyModel) {
        this.estudianteLazyModel = estudianteLazyModel;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public Boolean getBuscarMatAbierta() {
        return buscarMatAbierta;
    }

    public void setBuscarMatAbierta(Boolean buscarMatAbierta) {
        this.buscarMatAbierta = buscarMatAbierta;
    }
    
}
