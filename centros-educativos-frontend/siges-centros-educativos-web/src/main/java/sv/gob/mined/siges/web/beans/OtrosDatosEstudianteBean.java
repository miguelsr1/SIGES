/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.catalogo.SgDependenciaEconomica;
import sv.gob.mined.siges.web.dto.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.web.dto.catalogo.SgOcupacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoSangre;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgDatosResidencialesPersona;
import sv.gob.mined.siges.web.dto.catalogo.SgCompaniaTelecomunicacion;
import sv.gob.mined.siges.web.dto.catalogo.SgElementoHogar;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteAbastecimientoAgua;
import sv.gob.mined.siges.web.dto.catalogo.SgMaterialPiso;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class OtrosDatosEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OtrosDatosEstudianteBean.class.getName());

    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private SessionBean sessionBean;

    private SgEstudiante entidadEnEdicion;
    private SgDatosResidencialesPersona datosResidenciales;
    private SofisComboG<SgMedioTransporte> comboMedioTransporte;
    private SofisComboG<SgOcupacion> comboOcupacion;
    private SofisComboG<SgDependenciaEconomica> comboDependenciaEconomica;
    private SofisComboG<SgTipoTrabajo> comboTipoTrabajo;
    private SofisComboG<SgTipoSangre> comboTipoSangre;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<SgTipoServicioSanitario> comboTipoServicioSanitario;
    
    private SofisComboG<SgFuenteAbastecimientoAgua> comboFuentesAbastecimientoAgua;
    private SofisComboG<SgMaterialPiso> comboMaterialesPiso;
    private SofisComboG<SgCompaniaTelecomunicacion> comboCompaniasTelecomunicacion;

    public OtrosDatosEstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            soloLectura = request.getAttribute("soloLecturaOtrosDatosEstudiante") != null ? (Boolean) request.getAttribute("soloLecturaOtrosDatosEstudiante") : soloLectura;
            entidadEnEdicion = (SgEstudiante) request.getAttribute("estudiante");

            if (!soloLectura) {
                cargarCombos();
            }
            
            if (entidadEnEdicion != null) {
                actualizar(entidadEnEdicion);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {

        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"mtrNombre"});
            fc.setIncluirCampos(new String[]{"mtrNombre", "mtrVersion"});
            List<SgMedioTransporte> mediosTransporte = restCatalogo.buscarMediosTransporte(fc);
            comboMedioTransporte = new SofisComboG(mediosTransporte, "mtrNombre");
            comboMedioTransporte.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"ocuNombre"});
            fc.setIncluirCampos(new String[]{"ocuNombre", "ocuVersion"});
            comboOcupacion = new SofisComboG(restCatalogo.buscarOcupacion(fc), "ocuNombre");
            comboOcupacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"decNombre"});
            fc.setIncluirCampos(new String[]{"decNombre", "decVersion"});
            comboDependenciaEconomica = new SofisComboG(restCatalogo.buscarDependenciaEconomica(fc), "decNombre");
            comboDependenciaEconomica.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"ttrNombre"});
            fc.setIncluirCampos(new String[]{"ttrNombre", "ttrVersion"});
            comboTipoTrabajo = new SofisComboG(restCatalogo.buscarTipoTrabajo(fc), "ttrNombre");
            comboTipoTrabajo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"tsaNombre"});
            fc.setIncluirCampos(new String[]{"tsaNombre", "tsaVersion"});
            comboTipoSangre = new SofisComboG(restCatalogo.buscarTipoSangre(fc), "tsaNombre");
            comboTipoSangre.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroTipoServicioSanitario fss= new FiltroTipoServicioSanitario();
            fss.setHabilitado(Boolean.TRUE);
            fss.setTssAplicaEstudiante(Boolean.TRUE);
            comboTipoServicioSanitario = new SofisComboG(restCatalogo.buscarTipoServicioSanitario(fss), "tssNombre");
            comboTipoServicioSanitario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            fc.setOrderBy(new String[]{"faaNombre"});
            fc.setIncluirCampos(new String[]{"faaNombre", "faaVersion"});
            comboFuentesAbastecimientoAgua = new SofisComboG(restCatalogo.buscarFuentesAbastecimientoAgua(fc), "faaNombre");
            comboFuentesAbastecimientoAgua.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            fc.setOrderBy(new String[]{"mapNombre"});
            fc.setIncluirCampos(new String[]{"mapNombre", "mapVersion"});
            comboMaterialesPiso = new SofisComboG(restCatalogo.buscarMaterialesPiso(fc), "mapNombre");
            comboMaterialesPiso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            fc.setOrderBy(new String[]{"cteNombre"});
            fc.setIncluirCampos(new String[]{"cteNombre", "cteVersion"});
            comboCompaniasTelecomunicacion = new SofisComboG(restCatalogo.buscarCompaniasTelecomunicacion(fc), "cteNombre");
            comboCompaniasTelecomunicacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgElementoHogar> completeElementosHogar(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"ehoNombre"});
            fil.setAscending(new boolean[]{false});
            return this.datosResidenciales.getPerElementosHogar() != null
                    ? restCatalogo.buscarElementosHogar(fil).stream()
                            .filter(i -> !this.datosResidenciales.getPerElementosHogar().contains(i))
                            .collect(Collectors.toList())
                    : restCatalogo.buscarElementosHogar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void limpiarCombos(){
        comboMedioTransporte.setSelected(0);
        comboOcupacion.setSelected(0);
        comboDependenciaEconomica.setSelected(0);
        comboTipoTrabajo.setSelected(0);
        comboTipoSangre.setSelected(0);
        comboTipoServicioSanitario.setSelected(0);
    }

    public void actualizar(SgEstudiante est) {
        try {
            if (est != null) {
                entidadEnEdicion = est;
                datosResidenciales = est.getEstPersona().getPerDatosResidenciales();
                if (!soloLectura) {
                    limpiarCombos();
                    
                    comboMedioTransporte.setSelectedT(entidadEnEdicion.getEstMedioTransporte());
                    comboOcupacion.setSelectedT(entidadEnEdicion.getEstPersona().getPerOcupacion());
                    comboDependenciaEconomica.setSelectedT(entidadEnEdicion.getEstDependenciaEconomica());
                    comboTipoTrabajo.setSelectedT(entidadEnEdicion.getEstPersona().getPerTipoTrabajo());
                    comboTipoSangre.setSelectedT(entidadEnEdicion.getEstPersona().getPerTipoSangre());
                    comboTipoServicioSanitario.setSelectedT(datosResidenciales.getPerTipoServicioSanitarioResidencial());
                    comboFuentesAbastecimientoAgua.setSelectedT(datosResidenciales.getPerFuenteAbastecimientoAguaResidencial());
                    comboMaterialesPiso.setSelectedT(datosResidenciales.getPerMaterialPisoResidencial());
                    comboCompaniasTelecomunicacion.setSelectedT(datosResidenciales.getPerCompaniaInternetResidencial());
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void comboMedioTransporteSelected() {
        entidadEnEdicion.setEstMedioTransporte(comboMedioTransporte.getSelectedT());
    }

    public void comboOcupacionSelected() {
        entidadEnEdicion.getEstPersona().setPerOcupacion(comboOcupacion.getSelectedT());
    }

    public void comboDependenciaEconomicaSelected() {
        entidadEnEdicion.setEstDependenciaEconomica(comboDependenciaEconomica.getSelectedT());
    }

    public void comboTipoTrabajoSelected() {
        entidadEnEdicion.getEstPersona().setPerTipoTrabajo(comboTipoTrabajo.getSelectedT());
    }

    public void comboTipoSangreSelected() {
        entidadEnEdicion.getEstPersona().setPerTipoSangre(comboTipoSangre.getSelectedT());
    }

    public void comboTipoServicioSanitarioSelected() {
        datosResidenciales.setPerTipoServicioSanitarioResidencial(comboTipoServicioSanitario.getSelectedT());
        datosResidenciales.setPerTipoServicioSanitarioResidencialOtro(null);
    }
    
    public void comboFuentesAbastecimientoAguaSelected(){
        datosResidenciales.setPerFuenteAbastecimientoAguaResidencial(comboFuentesAbastecimientoAgua.getSelectedT());
        datosResidenciales.setPerFuenteAbastecimientoAguaResidencialOtra(null);
    }
    
    public void comboMaterialesPisoSelected(){
        datosResidenciales.setPerMaterialPisoResidencial(comboMaterialesPiso.getSelectedT());
        datosResidenciales.setPerMaterialPisoResidencialOtro(null);
    }
    
    public void comboCompaniasTelecomunicacionSelected(){
        datosResidenciales.setPerCompaniaInternetResidencial(comboCompaniasTelecomunicacion.getSelectedT());
    }
    
    public Boolean getRenderIngresarOtraFuenteAbastecimientoDeAgua(){
        return datosResidenciales.getPerFuenteAbastecimientoAguaResidencial() != null && datosResidenciales.getPerFuenteAbastecimientoAguaResidencial().getFaaPk().equals(Constantes.PK_FUENTE_ABASTECIMIENTO_AGUA_OTRA);
    }
    
    public Boolean getRenderIngresarOtroMaterialDePiso(){
        return datosResidenciales.getPerMaterialPisoResidencial()!= null && datosResidenciales.getPerMaterialPisoResidencial().getMapPk().equals(Constantes.PK_MATERIAL_PISO_OTRO);
    }
    
    public Boolean getRenderIngresarOtroServicioSanitario(){
        return datosResidenciales.getPerTipoServicioSanitarioResidencial()!= null && datosResidenciales.getPerTipoServicioSanitarioResidencial().getTssPk().equals(Constantes.PK_SERVICIO_SANITARIO_OTRO);
    }
    

    public SgEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstudiante entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgMedioTransporte> getComboMedioTransporte() {
        return comboMedioTransporte;
    }

    public void setComboMedioTransporte(SofisComboG<SgMedioTransporte> comboMedioTransporte) {
        this.comboMedioTransporte = comboMedioTransporte;
    }

    public SofisComboG<SgOcupacion> getComboOcupacion() {
        return comboOcupacion;
    }

    public void setComboOcupacion(SofisComboG<SgOcupacion> comboOcupacion) {
        this.comboOcupacion = comboOcupacion;
    }

    public SofisComboG<SgDependenciaEconomica> getComboDependenciaEconomica() {
        return comboDependenciaEconomica;
    }

    public void setComboDependenciaEconomica(SofisComboG<SgDependenciaEconomica> comboDependenciaEconomica) {
        this.comboDependenciaEconomica = comboDependenciaEconomica;
    }

    public SofisComboG<SgTipoTrabajo> getComboTipoTrabajo() {
        return comboTipoTrabajo;
    }

    public void setComboTipoTrabajo(SofisComboG<SgTipoTrabajo> comboTipoTrabajo) {
        this.comboTipoTrabajo = comboTipoTrabajo;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgTipoSangre> getComboTipoSangre() {
        return comboTipoSangre;
    }

    public void setComboTipoSangre(SofisComboG<SgTipoSangre> comboTipoSangre) {
        this.comboTipoSangre = comboTipoSangre;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SofisComboG<SgTipoServicioSanitario> getComboTipoServicioSanitario() {
        return comboTipoServicioSanitario;
    }

    public void setComboTipoServicioSanitario(SofisComboG<SgTipoServicioSanitario> comboTipoServicioSanitario) {
        this.comboTipoServicioSanitario = comboTipoServicioSanitario;
    }

    public SgDatosResidencialesPersona getDatosResidenciales() {
        return datosResidenciales;
    }

    public void setDatosResidenciales(SgDatosResidencialesPersona datosResidenciales) {
        this.datosResidenciales = datosResidenciales;
    }

    public SofisComboG<SgFuenteAbastecimientoAgua> getComboFuentesAbastecimientoAgua() {
        return comboFuentesAbastecimientoAgua;
    }

    public void setComboFuentesAbastecimientoAgua(SofisComboG<SgFuenteAbastecimientoAgua> comboFuentesAbastecimientoAgua) {
        this.comboFuentesAbastecimientoAgua = comboFuentesAbastecimientoAgua;
    }

    public SofisComboG<SgMaterialPiso> getComboMaterialesPiso() {
        return comboMaterialesPiso;
    }

    public void setComboMaterialesPiso(SofisComboG<SgMaterialPiso> comboMaterialesPiso) {
        this.comboMaterialesPiso = comboMaterialesPiso;
    }

    public SofisComboG<SgCompaniaTelecomunicacion> getComboCompaniasTelecomunicacion() {
        return comboCompaniasTelecomunicacion;
    }

    public void setComboCompaniasTelecomunicacion(SofisComboG<SgCompaniaTelecomunicacion> comboCompaniasTelecomunicacion) {
        this.comboCompaniasTelecomunicacion = comboCompaniasTelecomunicacion;
    }
    
    
    

}
