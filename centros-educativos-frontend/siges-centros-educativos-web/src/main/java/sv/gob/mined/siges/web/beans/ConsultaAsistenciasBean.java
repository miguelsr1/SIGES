/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistenciasSedeEnNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyConsultaAsistenciasSedeEnNivelDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ControlAsistenciaCabezalRestClient;
import sv.gob.mined.siges.web.restclient.DepartamentoRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ConsultaAsistenciasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConsultaAsistenciasBean.class.getName());

    @Inject
    private ControlAsistenciaCabezalRestClient controlAsistenciaClient;

    @Inject
    private NivelRestClient nivelClient;

    @Inject
    private SedeRestClient sedesClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoClient;
    
    @Inject
    private CalendarioRestClient calendarioClient;
    @Inject
    private DepartamentoRestClient departamentoClient;
    

    private Integer paginado = 10;
    private FiltroAsistenciasSedeEnNivel filtro = new FiltroAsistenciasSedeEnNivel();

    private LazyConsultaAsistenciasSedeEnNivelDataModel consultaAsistenciasSedeEnNivelLazyModel;
    private Long totalResultados;
    private List<String> desagregacionesLabels;

    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgMunicipio> comboMunicipios = new SofisComboG<>();
    private SofisComboG<SgNivel> comboNiveles = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAniosCalendarioNacional = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAniosCalendarioInternacional = new SofisComboG<>();
    private SgSede sedeSeleccionada;
    private Boolean soloAsistencias;
    
    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            cargarSedePorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
           
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONSULTA_ASISTENCIAS_SEDES)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public Boolean getVerAsistencias(){
        return BooleanUtils.isNotFalse(soloAsistencias);
    }
    
    public Boolean getVerInasistencias(){
        return BooleanUtils.isNotTrue(soloAsistencias);
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = sessionBean.obtenerDepartamentosOperacion(ConstantesOperaciones.BUSCAR_ASISTENCIAS_POR_SEDE);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));           
           

            comboMunicipios = new SofisComboG<>();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
           
             if(!listaDepartamentos.isEmpty() && listaDepartamentos.size()==1){
                comboDepartamentos.setSelectedT(listaDepartamentos.get(0));
                seleccionarDepartamento();
                }
         
            
            FiltroCalendario fcal = new FiltroCalendario();
            fcal.setIncluirCampos(new String[]{"calTipoCalendario.tceCodigo", "calAnioLectivo.alePk", "calAnioLectivo.aleAnio"});
            fcal.setOrderBy(new String[]{"calAnioLectivo.aleAnio"});
            fcal.setAscending(new boolean[]{false});
            List<SgCalendario> calendarios = calendarioClient.buscar(fcal);
            List<SgAnioLectivo> aniosNacional = new ArrayList<>();
            List<SgAnioLectivo> aniosInternacional = new ArrayList<>();
            for (SgCalendario cal : calendarios){
                if (cal.getCalTipoCalendario().getTceCodigo().equals(Constantes.CALENDARIO_NACIONAL)){
                    aniosNacional.add(cal.getCalAnioLectivo());
                }
                if (cal.getCalTipoCalendario().getTceCodigo().equals(Constantes.CALENDARIO_INTERNACIONAL)){
                    aniosInternacional.add(cal.getCalAnioLectivo());
                }
            }
                        
            comboAniosCalendarioNacional = new SofisComboG<>(aniosNacional, "aleAnio");
            comboAniosCalendarioInternacional = new SofisComboG<>(aniosInternacional, "aleAnio");

            FiltroNivel filtroNivel = new FiltroNivel();
            filtroNivel.setNivHabilitado(Boolean.TRUE);
            filtroNivel.setIncluirCampos(new String[]{"nivNombre"});
            List<SgNivel> niveles = nivelClient.buscarConCache(filtroNivel);

            comboNiveles = new SofisComboG<>(niveles, "nivNombre");
            comboNiveles.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            desagregacionesLabels = new ArrayList<>(niveles.stream().map(n -> n.getNivNombre()).collect(Collectors.toList()));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
            return sedesClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }


    public void generar() {
        try {
            if (comboNiveles.getSelectedT() != null){
                List<String> niv = new ArrayList<>();
                niv.add(comboNiveles.getSelectedT().getNivNombre());
                desagregacionesLabels = new ArrayList<>(niv);
            } else {
                desagregacionesLabels = new ArrayList<>(((List<SgNivel>)comboNiveles.getAllTs()).stream().map(n -> n.getNivNombre()).collect(Collectors.toList()));
            }
            
            
            FiltroSedes filtroSedes = new FiltroSedes();
            filtroSedes.setSedDepartamentoId(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtroSedes.setSedMunicipioId(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            filtroSedes.setSedPk(this.sedeSeleccionada != null ? this.sedeSeleccionada.getSedPk() : null);
            filtroSedes.setSedNivel(comboNiveles.getSelectedT() != null ? comboNiveles.getSelectedT().getNivPk() : null);
            filtroSedes.setIncluirAdscritas(filtro.getIncluirAdscritas());
            totalResultados = sedesClient.buscarTotal(filtroSedes);
            
            
            filtro.setSedDepartamentoId(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setSedMunicipioId(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            filtro.setSedNivel(comboNiveles.getSelectedT() != null ? comboNiveles.getSelectedT().getNivPk() : null);
            filtro.setSedPk(this.sedeSeleccionada != null ? this.sedeSeleccionada.getSedPk() : null);
            filtro.setCalInternacionalAnioLectivoPk(comboAniosCalendarioInternacional.getSelectedT() != null ? comboAniosCalendarioInternacional.getSelectedT().getAlePk() : null);
            filtro.setCalNacionalAnioLectivoPk(comboAniosCalendarioNacional.getSelectedT() != null ? comboAniosCalendarioNacional.getSelectedT().getAlePk() : null);
            consultaAsistenciasSedeEnNivelLazyModel = new LazyConsultaAsistenciasSedeEnNivelDataModel(controlAsistenciaClient, filtro, totalResultados, paginado);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroAsistenciasSedeEnNivel();
        this.comboDepartamentos.setSelected(0);
        this.comboMunicipios.setSelected(0);
        this.sedeSeleccionada = null;
        this.comboNiveles.setSelected(0);
        this.consultaAsistenciasSedeEnNivelLazyModel = null;
    }

    public FiltroAsistenciasSedeEnNivel getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAsistenciasSedeEnNivel filtro) {
        this.filtro = filtro;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public LazyConsultaAsistenciasSedeEnNivelDataModel getConsultaAsistenciasSedeEnNivelLazyModel() {
        return consultaAsistenciasSedeEnNivelLazyModel;
    }

    public void setConsultaAsistenciasSedeEnNivelLazyModel(LazyConsultaAsistenciasSedeEnNivelDataModel consultaAsistenciasSedeEnNivelLazyModel) {
        this.consultaAsistenciasSedeEnNivelLazyModel = consultaAsistenciasSedeEnNivelLazyModel;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public List<String> getDesagregacionesLabels() {
        return desagregacionesLabels;
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

    public SofisComboG<SgNivel> getComboNiveles() {
        return comboNiveles;
    }

    public void setComboNiveles(SofisComboG<SgNivel> comboNiveles) {
        this.comboNiveles = comboNiveles;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getSoloAsistencias() {
        return soloAsistencias;
    }

    public void setSoloAsistencias(Boolean soloAsistencias) {
        this.soloAsistencias = soloAsistencias;
    }

    public SofisComboG<SgAnioLectivo> getComboAniosCalendarioNacional() {
        return comboAniosCalendarioNacional;
    }

    public void setComboAniosCalendarioNacional(SofisComboG<SgAnioLectivo> comboAniosCalendarioNacional) {
        this.comboAniosCalendarioNacional = comboAniosCalendarioNacional;
    }

    public SofisComboG<SgAnioLectivo> getComboAniosCalendarioInternacional() {
        return comboAniosCalendarioInternacional;
    }

    public void setComboAniosCalendarioInternacional(SofisComboG<SgAnioLectivo> comboAniosCalendarioInternacional) {
        this.comboAniosCalendarioInternacional = comboAniosCalendarioInternacional;
    }

       

}
