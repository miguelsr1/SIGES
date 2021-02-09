/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAcuerdo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgAcuerdo;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAcuerdo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAcuerdoSistema;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AcuerdoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class AcuerdoSistemaBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(AcuerdoSistemaBean.class.getName());
    
    @Inject
    private AcuerdoRestClient restClient;
    
    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private SessionBean sessionBean;
    
    private FiltroAcuerdo filtro = new FiltroAcuerdo();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SgSistemaIntegrado sistemaIntegrado;
    private SofisComboG<EnumEstadoAcuerdo> comboEstadoAcuerdo;
    private Boolean soloLectura;
    private Boolean verDatos;
    private List<RevHistorico> historialEntidad = new ArrayList();
    private SgSede sedeSeleccionada;
    private List<SgAcuerdo> listaAcuerdos;
    
    public AcuerdoSistemaBean() {
    }
    
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sistemaIntegrado = (SgSistemaIntegrado) request.getAttribute("sistemaIntegrado");
            if (sistemaIntegrado != null) {
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String buscar() {
        try {
            filtro.setSistemaIntegradoPk(sistemaIntegrado.getSinPk()); // se filtra en base al S.I. Siempre
            filtro.setSedePk(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            filtro.setEstadoAcuerdo(comboEstadoAcuerdo != null ? comboEstadoAcuerdo.getSelectedT() : null);
            filtro.setIncluirCampos(new String[]{
                "acuNombreAcuerdo",
                "acuEstado",
                "acuDescripcion",
                "acuVersion",
                "acuFecha",
                "acuPropuestaPedagogica",
                "acuPropuestaPedagogica.ppePk",
                "acuPropuestaPedagogica.ppeSede",
                "acuPropuestaPedagogica.ppeSede.sedCodigo",
                "acuPropuestaPedagogica.ppeSede.sedNombre",
                "acuPropuestaPedagogica.ppeSede.sedTelefono",
                "acuPropuestaPedagogica.ppeSede.sedDireccion.dirDepartamento.depNombre",
                "acuPropuestaPedagogica.ppeSede.sedDireccion.dirMunicipio.munNombre"
            });
            
            listaAcuerdos = restClient.buscar(filtro);
            totalResultados = Long.valueOf(listaAcuerdos.size());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void cargarCombos() {
        try {
            List<EnumEstadoAcuerdoSistema> estados = new ArrayList(Arrays.asList(EnumEstadoAcuerdo.values()));
            comboEstadoAcuerdo = new SofisComboG(estados, "text");
            comboEstadoAcuerdo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstadoAcuerdo.ordenar();
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        return Optional.ofNullable(listaAcuerdos
                .stream()
                .map(a -> a.getAcuPropuestaPedagogica().getPpeSede())
                .distinct()
                .filter(s -> s.getSedCodigo().contains(query) || s.getSedNombreBusqueda().contains(query))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }
    
    private void limpiarCombos() {
        comboEstadoAcuerdo.setSelected(-1);
    }
    
    public String limpiar() {
        filtro = new FiltroAcuerdo();
        limpiarCombos();
        sedeSeleccionada = null;
        listaAcuerdos = null;
        totalResultados = null;
        buscar();
        return null;
    }
    
    public FiltroAcuerdo getFiltro() {
        return filtro;
    }
    
    public void setFiltro(FiltroAcuerdo filtro) {
        this.filtro = filtro;
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
    
    public SgSistemaIntegrado getSistemaIntegrado() {
        return sistemaIntegrado;
    }
    
    public void setSistemaIntegrado(SgSistemaIntegrado sistemaIntegrado) {
        this.sistemaIntegrado = sistemaIntegrado;
    }
    
    public Boolean getSoloLectura() {
        return soloLectura;
    }
    
    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }
    
    public Boolean getVerDatos() {
        return verDatos;
    }
    
    public void setVerDatos(Boolean verDatos) {
        this.verDatos = verDatos;
    }
    
    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }
    
    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }
    
    public List<SgAcuerdo> getListaAcuerdos() {
        return listaAcuerdos;
    }
    
    public void setListaAcuerdos(List<SgAcuerdo> listaAcuerdos) {
        this.listaAcuerdos = listaAcuerdos;
    }
    
    public SofisComboG<EnumEstadoAcuerdo> getComboEstadoAcuerdo() {
        return comboEstadoAcuerdo;
    }
    
    public void setComboEstadoAcuerdo(SofisComboG<EnumEstadoAcuerdo> comboEstadoAcuerdo) {
        this.comboEstadoAcuerdo = comboEstadoAcuerdo;
    }
    
}
