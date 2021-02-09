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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgIdentificacion;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.IdentificacionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@ViewScoped
public class IdentificacionComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(IdentificacionComponenteBean.class.getName());

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private IdentificacionRestClient restIdentificacionClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private PersonaBean personaBean;

    private SgPersona personaEdicion;
    private Boolean soloLectura;
    private Boolean renderIdentPersonalSede = Boolean.FALSE;
    private Boolean renderOpcionTieneIdentificacion = Boolean.TRUE;
    private SofisComboG<SgPais> comboPais = new SofisComboG();
    private SofisComboG<SgTipoIdentificacion> comboTiposIdentificacion = new SofisComboG();

    private Boolean consumoServicioDUIRNPNHabilitado = Boolean.FALSE;
    private Boolean consumoServicioCUNRNPNHabilitado = Boolean.FALSE;

    private Boolean tieneNIE = Boolean.FALSE;
    private Boolean tieneDUI = Boolean.FALSE;
    private Boolean tieneNIT = Boolean.FALSE;
    private Boolean tieneCUN = Boolean.FALSE;
    private Boolean tieneNIP = Boolean.FALSE;
    private Boolean tieneINPEP = Boolean.FALSE;
    private Boolean tieneISSS = Boolean.FALSE;
    private Boolean tieneNUP = Boolean.FALSE;
    private Boolean tieneOtra = Boolean.FALSE;
    private SgIdentificacion identificacionEnEdicion = new SgIdentificacion();
    private String nipString;
    private Boolean verNIP = Boolean.TRUE;

    @PostConstruct
    public void init() {
        try {
            SgConfiguracion ccun = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_CUN_RNPN_HABILITADO);
            SgConfiguracion cdui = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_DUI_RNPN_HABILITADO);

            if (ccun != null && ccun.getConValor() != null) {
                consumoServicioCUNRNPNHabilitado = Boolean.parseBoolean(ccun.getConValor());
            }

            if (cdui != null && cdui.getConValor() != null) {
                consumoServicioDUIRNPNHabilitado = Boolean.parseBoolean(cdui.getConValor());
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public void actualizar(SgPersona per) {
        //Si cambia la persona, actualizo formulario

        if (per != null && !per.equals(this.personaEdicion)) {
            tieneNIE = Boolean.FALSE;
            tieneDUI = Boolean.FALSE;
            tieneCUN = Boolean.FALSE;
            tieneNIP = Boolean.FALSE;
            tieneNIT = Boolean.FALSE;
            tieneOtra = Boolean.FALSE;

            if (per.getPerTieneIdentificacion() == null) {
                per.setPerTieneIdentificacion(Boolean.FALSE);
            }

            if (per.getPerNie() != null) {
                tieneNIE = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (!StringUtils.isBlank(per.getPerDui())) {
                tieneDUI = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (per.getPerNip() != null) {
                tieneNIP = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (!StringUtils.isBlank(per.getPerNit())) {
                tieneNIT = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (per.getPerCun() != null) {
                tieneCUN = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (per.getPerIdentificaciones() != null && !per.getPerIdentificaciones().isEmpty()) {
                tieneOtra = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }

            if (!StringUtils.isBlank(per.getPerInpep())) {
                tieneINPEP = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (!StringUtils.isBlank(per.getPerIsss())) {
                tieneISSS = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
            if (!StringUtils.isBlank(per.getPerNup())) {
                tieneNUP = Boolean.TRUE;
                per.setPerTieneIdentificacion(Boolean.TRUE);
            }
        }
        personaEdicion = per;
    }

    public void tieneIdentificacionSelected() {
        //
    }

    public void limpiarCombosIdent() {
        if (comboPais != null) {
            comboPais.setSelected(-1);
        }
        if (comboTiposIdentificacion != null) {
            comboTiposIdentificacion.setSelected(-1);
        }
    }

    public void cargarCombosOtraIdentificacion() {
        try {
            if (comboPais == null || comboPais.getItems().isEmpty()) {
                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setAscending(new boolean[]{true});
                fc.setOrderBy(new String[]{"paiNombre"});
                fc.setIncluirCampos(new String[]{"paiNombre", "paiVersion", "paiCodigo"});
                List<SgPais> paises = restCatalogo.buscarPais(fc);
                comboPais = new SofisComboG(new ArrayList(paises), "paiNombre");
                comboPais.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                fc.setOrderBy(new String[]{"tinNombre"});
                fc.setIncluirCampos(new String[]{"tinCodigo", "tinNombre", "tinVersion"});
                fc.setHabilitado(Boolean.TRUE);
                List<SgTipoIdentificacion> tiposIdentificaciones = restCatalogo.buscarTipoIdentificacion(fc);
                comboTiposIdentificacion = new SofisComboG(new ArrayList(tiposIdentificaciones), "tinNombre");
                comboTiposIdentificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void agregarIdentificacionAPersona() {
        try {
            ViewIdUtils.generateViewId(identificacionEnEdicion, personaEdicion.getPerIdentificaciones());
            restIdentificacionClient.validar(identificacionEnEdicion);
            if (personaEdicion.getPerIdentificaciones() == null) {
                personaEdicion.setPerIdentificaciones(new ArrayList<>());
            }
            if (personaEdicion.getPerIdentificaciones().contains(identificacionEnEdicion)) {
                personaEdicion.getPerIdentificaciones().set(personaEdicion.getPerIdentificaciones().indexOf(identificacionEnEdicion), identificacionEnEdicion);
            } else {
                personaEdicion.getPerIdentificaciones().add(identificacionEnEdicion);
            }
            PrimeFaces.current().executeScript("PF('identificacionDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarIdentifiacion() {
        limpiarCombosIdent();
        cargarCombosOtraIdentificacion();
        identificacionEnEdicion = new SgIdentificacion();
        comboTiposIdentificacion.setSelected(-1);
        comboPais.setSelected(-1);
    }

    public void editarIdentificacion(SgIdentificacion identificacion) {
        limpiarCombosIdent();
        cargarCombosOtraIdentificacion();
        identificacionEnEdicion = (SgIdentificacion) SerializationUtils.clone(identificacion);
        comboTiposIdentificacion.setSelectedT(identificacionEnEdicion.getIdeTipoDocumento());
        comboPais.setSelectedT(identificacionEnEdicion.getIdePaisEmisor());

    }
    
    public void validarDUI() {
        personaBean.validarDUI();
    }

    public void validarCUN() {
        personaBean.validarCUN();
    }

    public Boolean getPuedeEditarDUI() {
        if (this.getSoloLectura()) {
            return Boolean.FALSE;
        }
        if (this.personaEdicion.getPerPk() != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        return BooleanUtils.isNotTrue(this.personaEdicion.getPerDuiValidadoRNPN());
    }

    public Boolean getPuedeValidarDUI() {
        if (this.getSoloLectura()) {
            return Boolean.FALSE;
        }
        if (BooleanUtils.isNotTrue(this.consumoServicioDUIRNPNHabilitado)) {
            return Boolean.FALSE;
        }
        if (this.personaEdicion.getPerPk() == null){
            return Boolean.FALSE;
        }
        if (BooleanUtils.isTrue(this.personaEdicion.getPerDuiPendienteValidacionRNPN())){
            return Boolean.FALSE;
        }
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VALIDAR_DUI_PERSONA_RNPN)) {
            return Boolean.FALSE;
        }
        if (StringUtils.isBlank(this.personaEdicion.getPerDui())) {
            return Boolean.FALSE;
        }
        return BooleanUtils.isNotTrue(this.personaEdicion.getPerDuiValidadoRNPN());
    }

    public Boolean getPuedeEditarCUN() {
        if (this.getSoloLectura()) {
            return Boolean.FALSE;
        }
        if (this.personaEdicion.getPerPk() != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA)) {
            return Boolean.TRUE;
        }
        return BooleanUtils.isNotTrue(this.personaEdicion.getPerCunValidadoRNPN());
    }

    public Boolean getPuedeValidarCUN() {
        if (this.getSoloLectura()) {
            return Boolean.FALSE;
        }
        if (BooleanUtils.isNotTrue(this.consumoServicioCUNRNPNHabilitado)) {
            return Boolean.FALSE;
        }
        if (this.personaEdicion.getPerPk() == null){
            return Boolean.FALSE;
        }
        if (BooleanUtils.isTrue(this.personaEdicion.getPerCunPendienteValidacionRNPN())){
            return Boolean.FALSE;
        }
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VALIDAR_CUN_PERSONA_RNPN)) {
            return Boolean.FALSE;
        }
        if (StringUtils.isNotBlank(this.personaEdicion.getPerDui()) 
                || BooleanUtils.isTrue(this.personaEdicion.getPerDuiValidadoRNPN())
                || BooleanUtils.isTrue(this.personaEdicion.getPerDuiPendienteValidacionRNPN())) {
            return Boolean.FALSE;
        }
        if (this.personaEdicion.getPerCun() == null) {
            return Boolean.FALSE;
        }
        return BooleanUtils.isNotTrue(this.personaEdicion.getPerCunValidadoRNPN());
    }

    public void eliminarIdentificacion(SgIdentificacion identificacion) {
        this.personaEdicion.getPerIdentificaciones().remove(identificacion);
    }

    public void seleccionarTipoIdentificacion() {
        this.identificacionEnEdicion.setIdeTipoDocumento(this.comboTiposIdentificacion.getSelectedT());
    }

    public void seleccionarPais() {
        this.identificacionEnEdicion.setIdePaisEmisor(this.comboPais.getSelectedT());
    }

    public void tieneOtraIdenSelected() {
        if (!this.tieneOtra) {
            this.personaEdicion.setPerIdentificaciones(new ArrayList<>());
        }
    }

    public void tieneDUISelected() {
        if (!this.tieneDUI) {
            this.personaEdicion.setPerDui(null);
        }
    }

    public void tieneCUNSelected() {
        if (!this.tieneCUN) {
            this.personaEdicion.setPerCun(null);
        }
    }

    public void tieneNIPSelected() {
        if (!this.tieneNIP) {
            this.personaEdicion.setPerNip(null);
        }
    }

    public void tieneNITSelected() {
        if (!this.tieneNIT) {
            this.personaEdicion.setPerNit(null);
        }
    }

    public void tieneNIESelected() {
        if (!this.tieneNIE) {
            this.personaEdicion.setPerNie(null);
        }
    }

    public void tieneNUPSelected() {
        if (!this.tieneNUP) {
            this.personaEdicion.setPerNup(null);
        }
    }

    public void tieneISSSSelected() {
        if (!this.tieneISSS) {
            this.personaEdicion.setPerIsss(null);
        }
    }

    public void tieneINPEPSelected() {
        if (!this.tieneINPEP) {
            this.personaEdicion.setPerInpep(null);
        }
    }

    public Boolean getSoloLectura() {
        if (BooleanUtils.isTrue(this.personaEdicion.getPerSePermiteModificarIdentificacion())) {
            return !sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_IDENTIDAD_PERSONA);
        } else {
            return soloLectura
                    || (this.personaEdicion.getPerPk() != null && !sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_IDENTIDAD_PERSONA))
                    || BooleanUtils.isTrue(this.personaEdicion.getPerSeBuscoEnBd());
        }
    }

    public Boolean getRenderNIP() {
        return renderIdentPersonalSede && verNIP;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgPersona getPersonaEdicion() {
        return personaEdicion;
    }

    public void setPersonaEdicion(SgPersona personaEdicion) {
        this.personaEdicion = personaEdicion;
    }

    public SofisComboG<SgPais> getComboPais() {
        return comboPais;
    }

    public void setComboPais(SofisComboG<SgPais> comboPais) {
        this.comboPais = comboPais;
    }

    public SofisComboG<SgTipoIdentificacion> getComboTiposIdentificacion() {
        return comboTiposIdentificacion;
    }

    public void setComboTiposIdentificacion(SofisComboG<SgTipoIdentificacion> comboTiposIdentificacion) {
        this.comboTiposIdentificacion = comboTiposIdentificacion;
    }

    public Boolean getTieneNIE() {
        return tieneNIE;
    }

    public void setTieneNIE(Boolean tieneNIE) {
        this.tieneNIE = tieneNIE;
    }

    public Boolean getTieneDUI() {
        return tieneDUI;
    }

    public void setTieneDUI(Boolean tieneDUI) {
        this.tieneDUI = tieneDUI;
    }

    public Boolean getTieneCUN() {
        return tieneCUN;
    }

    public void setTieneCUN(Boolean tieneCUN) {
        this.tieneCUN = tieneCUN;
    }

    public Boolean getTieneOtra() {
        return tieneOtra;
    }

    public void setTieneOtra(Boolean tieneOtra) {
        this.tieneOtra = tieneOtra;
    }

    public SgIdentificacion getIdentificacionEnEdicion() {
        return identificacionEnEdicion;
    }

    public void setIdentificacionEnEdicion(SgIdentificacion identificacionEnEdicion) {
        this.identificacionEnEdicion = identificacionEnEdicion;
    }

    public Boolean getTieneNIP() {
        return tieneNIP;
    }

    public void setTieneNIP(Boolean tieneNIP) {
        this.tieneNIP = tieneNIP;
    }

    public Boolean getTieneNIT() {
        return tieneNIT;
    }

    public void setTieneNIT(Boolean tieneNIT) {
        this.tieneNIT = tieneNIT;
    }

    public Boolean getRenderIdentPersonalSede() {
        return renderIdentPersonalSede;
    }

    public void setRenderIdentPersonalSede(Boolean renderIdentPersonalSede) {
        if (renderIdentPersonalSede == null) {
            return;
        }
        this.renderIdentPersonalSede = renderIdentPersonalSede;
    }

    public Boolean getTieneINPEP() {
        return tieneINPEP;
    }

    public void setTieneINPEP(Boolean tieneINPEP) {
        this.tieneINPEP = tieneINPEP;
    }

    public Boolean getTieneISSS() {
        return tieneISSS;
    }

    public void setTieneISSS(Boolean tieneISSS) {
        this.tieneISSS = tieneISSS;
    }

    public Boolean getTieneNUP() {
        return tieneNUP;
    }

    public void setTieneNUP(Boolean tieneNUP) {
        this.tieneNUP = tieneNUP;
    }

    public Boolean getRenderOpcionTieneIdentificacion() {
        return renderOpcionTieneIdentificacion;
    }

    public void setRenderOpcionTieneIdentificacion(Boolean renderOpcionTieneIdentificacion) {
        if (renderOpcionTieneIdentificacion == null) {
            return;
        }
        this.renderOpcionTieneIdentificacion = renderOpcionTieneIdentificacion;
    }

    public Boolean getVerNIP() {
        return verNIP;
    }

    public void setVerNIP(Boolean verNIP) {
        this.verNIP = verNIP;
    }

}
