/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
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

public class LibroDeBancoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LibroDeBancoBean.class.getName());

    @Inject
    private ProveedorRestClient restClient;
    @Inject
    private SessionBean sessionBean;

    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;
    @Inject
    private CuentasBancariasRestClient cuentasRestClient;
    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteRestClient;

    @Inject
    private GesPresEsRestClient subComponenteRestClient;

    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private SofisComboG<SgCuentasBancarias> cuentaBancariaCombo;
    private SgCuentasBancarias cuenta;
    private SsCategoriaPresupuestoEscolar componente;
    private SsGesPresEs subComponente;
    private SgAnioFiscal anio;
    private SgSede sedeSeleccionadaFiltro;

    private Locale locale;
    private Boolean reporte = Boolean.TRUE;

    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private FiltroMovimientoCuentaBancaria filtroMov = new FiltroMovimientoCuentaBancaria();
    private LocalDate currentDate = LocalDate.now();
    private LocalDateTime currentDateTime = LocalDateTime.now();

    /**
     * Constructor de la clase.
     */
    public LibroDeBancoBean() {
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
        cuentaBancariaCombo = new SofisComboG();
        sedeSeleccionadaFiltro = null;
        validarBtnReporte();

    }

    /**
     * Setea el valor de la cuenta Selecccionada devuelve el objeto cuenta.
     */
    public void setearCuenta() {
        JSFUtils.limpiarMensajesError();
        if (cuentaBancariaCombo.getSelectedT() != null) {
            cuenta = cuentaBancariaCombo.getSelectedT();
            validarBtnReporte();
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
     * Setea el valor de la fecha desde
     */
    public void setearDesde() {
        JSFUtils.limpiarMensajesError();

    }

    /**
     * Valida que todos los campos requeridos esten llenos
     */
    public void validarBtnReporte() {
        reporte = Boolean.TRUE;
        if (anio != null && sedeSeleccionadaFiltro != null && cuenta != null) {
            reporte = Boolean.FALSE;
        } else {
            reporte = Boolean.TRUE;
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            if (sedeSeleccionadaFiltro != null) {
                FiltroCuentasBancarias filtroCuenta = new FiltroCuentasBancarias();
                filtroCuenta.setCbcHabilitado(Boolean.TRUE);
                filtroCuenta.setCbcSedeFk(sedeSeleccionadaFiltro.getSedPk());
                filtroCuenta.setIncluirCampos(new String[]{
                    "cbcPk",
                    "cbcSedeFk.sedPk",
                    "cbcSedeFk.sedCodigo",
                    "cbcSedeFk.sedNombre",
                    "cbcSedeFk.sedTipo",
                    "cbcSedeFk.sedVersion",
                    "cbcNumeroCuenta",
                    "cbcVersion"});
                List<SgCuentasBancarias> listCuenta = cuentasRestClient.buscar(filtroCuenta);
                cuentaBancariaCombo = new SofisComboG(listCuenta, "cbcNumeroCuenta");
                cuentaBancariaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
     * Carga el filtro fecha para realizar las búsquedas de la siguiente forma:
     * filtroMov.getMccFiltroFecha().equals(1) : Carga los registros del mes
     * actual, sino, carga los registros según el rango de fecha establecido en
     * los filtros.
     */
    public void cambioFiltroFecha() {
        filtroMov.setMcbFecha(null);
        if (filtroMov.getMcbFiltroFecha() != null) {
            if (filtroMov.getMcbFiltroFecha().equals(1)) {
                filtroMov.setMcbFecha(currentDateTime);
                filtroMov.setMcbFechaDesdeS(null);
                filtroMov.setMcbFechaHastaS(null);
            } else {
                filtroMov.setMcbFechaDesdeS(currentDate);
                filtroMov.setMcbFechaHastaS(currentDate);
                filtroMov.setMcbFecha(null);
            }
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

    public SofisComboG<SgCuentasBancarias> getCuentaBancariaCombo() {
        return cuentaBancariaCombo;
    }

    public void setCuentaBancariaCombo(SofisComboG<SgCuentasBancarias> cuentaBancariaCombo) {
        this.cuentaBancariaCombo = cuentaBancariaCombo;
    }

    public SgCuentasBancarias getCuenta() {
        return cuenta;
    }

    public void setCuenta(SgCuentasBancarias cuenta) {
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

    public SgAnioFiscal getAnio() {
        return anio;
    }

    public void setAnio(SgAnioFiscal anio) {
        this.anio = anio;
    }

    public SsCategoriaPresupuestoEscolar getComponente() {
        return componente;
    }

    public void setComponente(SsCategoriaPresupuestoEscolar componente) {
        this.componente = componente;
    }

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }

    public FiltroMovimientoCuentaBancaria getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoCuentaBancaria filtroMov) {
        this.filtroMov = filtroMov;
    }
// </editor-fold>
}
