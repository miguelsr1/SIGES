/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgCajaChica;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.ProveedorRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped

public class LibroCajaChicaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LibroCajaChicaBean.class.getName());

    @Inject
    private ProveedorRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private SofisComboG<SgCajaChica> cajaChicaCombo;
    private SgCajaChica cuenta;
    private SgAnioFiscal anio;
    private SgSede sedeSeleccionadaFiltro;
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SsCategoriaPresupuestoEscolar componente;
    private SsGesPresEs subComponente;
    private FiltroMovimientoCuentaBancaria filtroMov = new FiltroMovimientoCuentaBancaria();
    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;

    @Inject
    private CajaChicaRestClient cuentasRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteRestClient;

    @Inject
    private GesPresEsRestClient subComponenteRestClient;

    private Locale locale;
    private Boolean reporte = Boolean.TRUE;

    /**
     * Constructor de la clase.
     */
    public LibroCajaChicaBean() {
    }

    /**
     * Método para incializar combos.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        anioFiscalCombo.setSelected(-1);
        cajaChicaCombo = new SofisComboG();
        sedeSeleccionadaFiltro = null;
        validarBtnReporte();

    }

    /**
     * Setea el valor de la cuenta Selecccionada devuelve el objeto cuenta.
     */
    public void setearCuenta() {
        JSFUtils.limpiarMensajesError();
        if (cajaChicaCombo.getSelectedT() != null) {
            cuenta = cajaChicaCombo.getSelectedT();
            validarBtnReporte();
        }
    }

    /**
     * Setea el valor del añor Selecccionada devuelve el objeto año.
     */
    public void setearAnio() {
        JSFUtils.limpiarMensajesError();
        if (anioFiscalCombo.getSelectedT() != null) {
            anio = anioFiscalCombo.getSelectedT();
            validarBtnReporte();
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            if (sedeSeleccionadaFiltro != null) {
                FiltroCajaChica filtroCaja = new FiltroCajaChica();
                filtroCaja.setBccSedeFk(sedeSeleccionadaFiltro.getSedPk());
                filtroCaja.setIncluirCampos(new String[]{
                    "bccPk",
                    "bccSedeFk.sedPk",
                    "bccSedeFk.sedCodigo",
                    "bccSedeFk.sedNombre",
                    "bccSedeFk.sedTipo",
                    "bccSedeFk.sedVersion",
                    "bccNumeroCuenta",
                    "bccVersion"});
                List<SgCajaChica> listCuenta = cuentasRestClient.buscar(filtroCaja);
                cajaChicaCombo = new SofisComboG(listCuenta, "bccNumeroCuenta");
                cajaChicaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            FiltroAnioFiscal filtroAnioFiscal = new FiltroAnioFiscal();
            filtroAnioFiscal.setIncluirCampos(new String[]{"aniPk", "aniAnio", "aniVersion"});
            List<SgAnioFiscal> listAnio = anioFiscalRestClient.buscar(filtroAnioFiscal);
            anioFiscalCombo = new SofisComboG(listAnio, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            cargarComboComponentes();
            validarBtnReporte();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el oncomplete de sedes en presupuestos escolares.
     */
    public List<SgSede> completeSedeFiltro(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Valida que todos los campos requeridos esten llenos
     */
    public void validarBtnReporte() {
        reporte = Boolean.TRUE;
        if (anioFiscalCombo.getSelectedT() != null && sedeSeleccionadaFiltro != null && cajaChicaCombo.getSelectedT() != null) {
            reporte = Boolean.FALSE;
        } else {
            reporte = Boolean.TRUE;
        }
    }

    /**
     * Setea el valor del componente Seleccionado.
     */
    public void setearComponente() {
        JSFUtils.limpiarMensajesError();
        if (comboComponente.getSelectedT() != null) {
            componente = comboComponente.getSelectedT();
            validarBtnReporte();
        }
    }

    /**
     * Setea el valor del subComponente Seleccionado.
     */
    public void setearSubComponente() {
        JSFUtils.limpiarMensajesError();
        if (comboSubComponente.getSelectedT() != null) {
            subComponente = comboSubComponente.getSelectedT();
            validarBtnReporte();
        }
    }

    /**
     * Carga el combo de filtro Componentes.
     */
    private void cargarComboComponentes() {
        try {
            FiltroCategoriaPresupuestoEscolar comp = new FiltroCategoriaPresupuestoEscolar();
            comp.setCpeHabilitado(Boolean.TRUE);
            comp.setOrderBy(new String[]{"cpeNombre"});
            comp.setIncluirCampos(new String[]{
                "cpeId",
                "cpeNombre",
                "cpeVersion"});
            comp.setAscending(new boolean[]{true});
            List<SsCategoriaPresupuestoEscolar> listComponente = componenteRestClient.buscarComponente(comp);
            comboComponente = new SofisComboG(listComponente, "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de filtro Subcomponente.
     */
    public void cargarComboSubcomponentes() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubCo = new FiltroGesPresEs();
                filtroSubCo.setCpeId(comboComponente.getSelectedT().getCpeId());
                filtroSubCo.setIncluirCampos(new String[]{
                    "gesVersion",
                    "gesNombre",
                    "gesCod"});
                filtroSubCo.setOrderBy(new String[]{"gesNombre"});
                comboSubComponente = new SofisComboG<>(subComponenteRestClient.buscarSubcomponente(filtroSubCo), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                setearComponente();
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Boolean getReporte() {
        return reporte;
    }

    public void setReporte(Boolean reporte) {
        this.reporte = reporte;
    }

    public ProveedorRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ProveedorRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SofisComboG<SgAnioFiscal> getAnioFiscalCombo() {
        return anioFiscalCombo;
    }

    public void setAnioFiscalCombo(SofisComboG<SgAnioFiscal> anioFiscalCombo) {
        this.anioFiscalCombo = anioFiscalCombo;
    }

    public SofisComboG<SgCajaChica> getCajaChicaCombo() {
        return cajaChicaCombo;
    }

    public void setCajaChicaCombo(SofisComboG<SgCajaChica> cajaChicaCombo) {
        this.cajaChicaCombo = cajaChicaCombo;
    }

    public SgCajaChica getCuenta() {
        return cuenta;
    }

    public void setCuenta(SgCajaChica cuenta) {
        this.cuenta = cuenta;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }

    public SsCategoriaPresupuestoEscolar getComponente() {
        return componente;
    }

    public void setComponente(SsCategoriaPresupuestoEscolar componente) {
        this.componente = componente;
    }

    public FiltroMovimientoCuentaBancaria getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoCuentaBancaria filtroMov) {
        this.filtroMov = filtroMov;
    }

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }

    public SgAnioFiscal getAnio() {
        return anio;
    }

    public void setAnio(SgAnioFiscal anio) {
        this.anio = anio;
    }

// </editor-fold>
}
