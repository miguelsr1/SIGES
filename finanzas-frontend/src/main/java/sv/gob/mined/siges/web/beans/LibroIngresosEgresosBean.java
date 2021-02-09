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
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
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

public class LibroIngresosEgresosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LibroIngresosEgresosBean.class.getName());

    @Inject
    private ProveedorRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private SofisComboG<SgMovimientos> transferenciaCombo;
    private SgMovimientos transferencia;
    private SgAnioFiscal anio;
    private SgSede sedeSeleccionadaFiltro;
    private SgPresupuestoEscolar presupuesto;
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SsCategoriaPresupuestoEscolar componente;
    private SsGesPresEs subComponente;

    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;

    @Inject
    private CajaChicaRestClient cuentasRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private MovimientosRestClient movimientosRestClient;

    @Inject
    private PresupuestoEscolarRestCliente presupuestoEscolarClient;

    @Inject
    private GesPresEsRestClient subComponenteRestClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteRestClient;

    private Locale locale;
    private Boolean reporte = Boolean.TRUE;

    /**
     * Constructor de la clase.
     */
    public LibroIngresosEgresosBean() {
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
        anioFiscalCombo = new SofisComboG();
        transferenciaCombo = new SofisComboG();
        sedeSeleccionadaFiltro = null;
        validarBtnReporte();
        cargarCombos();
    }

    /**
     * Setea el valor de la cuenta Selecccionada devuelve el objeto cuenta.
     */
    public void setearTranasferencia() {
        JSFUtils.limpiarMensajesError();
        transferencia = null;
        if (transferenciaCombo.getSelectedT() != null) {
            transferencia = transferenciaCombo.getSelectedT();
        }
        validarBtnReporte();
    }

    /**
     * Setea el valor del añor Selecccionada devuelve el objeto año.
     */
    public void setearAnio() {
        JSFUtils.limpiarMensajesError();
        transferenciaCombo = new SofisComboG<>();
        if (anioFiscalCombo.getSelectedT() != null) {
            anio = anioFiscalCombo.getSelectedT();
            cargarCombos();
        }
        validarBtnReporte();
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {

            FiltroAnioFiscal filtroAnioFiscal = new FiltroAnioFiscal();
            filtroAnioFiscal.setIncluirCampos(new String[]{"aniPk", "aniAnio", "aniVersion"});
            List<SgAnioFiscal> listAnio = anioFiscalRestClient.buscar(filtroAnioFiscal);
            anioFiscalCombo = new SofisComboG(listAnio, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (sedeSeleccionadaFiltro != null && anio != null) {
                FiltroPresupuestoEscolar pres = new FiltroPresupuestoEscolar();
                pres.setIdSede(sedeSeleccionadaFiltro.getSedPk());
                pres.setAnioFiscal(anio.getAniAnio());
                pres.setPesEstado(EnumPresupuestoEscolarEstado.APROBADO);
                pres.setIncluirCampos(new String[]{
                    "pesNombre",
                    "pesPk",
                    "pesVersion"
                });
                List<SgPresupuestoEscolar> Listpresupuesto = presupuestoEscolarClient.buscar(pres);

                if (!Listpresupuesto.isEmpty()) {
                    presupuesto = Listpresupuesto.get(0);
                }
                if (!Listpresupuesto.isEmpty()) {
                    presupuesto = Listpresupuesto.get(0);
                    FiltroMovimientos movp = new FiltroMovimientos();
                    movp.setMovPresupuestoFk(presupuesto.getPesPk());
                    movp.setOrderBy(new String[]{"movPk"});
                    movp.setAscending(new boolean[]{true});
                    movp.setIncluirCampos(new String[]{
                        "movPk",
                        "movPresupuestoFk.pesPk",
                        "movPresupuestoFk.pesVersion",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeId",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeNombre",
                        "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeVersion",
                        "movTechoPresupuestal.subComponente.gesId",
                        "movTechoPresupuestal.subComponente.gesNombre",
                        "movTechoPresupuestal.subComponente.gesVersion",
                        "movTechoPresupuestal.subCuenta.cuCuentaPadre.cuId",
                        "movTechoPresupuestal.subCuenta.cuCuentaPadre.cuNombre",
                        "movTechoPresupuestal.subCuenta.cuCuentaPadre.cuVersion",
                        "movTechoPresupuestal.subCuenta.cuId",
                        "movTechoPresupuestal.subCuenta.cuNombre",
                        "movTechoPresupuestal.subCuenta.cuVersion",
                        "movNumMoviento",
                        "movOrigen",
                        "movTipo",
                        "movVersion"});
                    movp.setMovTipo(EnumMovimientosTipo.I);
                    movp.setMovOrigen(EnumMovimientosOrigen.T);
                    List<SgMovimientos> montop = movimientosRestClient.buscar(movp);
                    if (!montop.isEmpty()) {
                        transferenciaCombo = new SofisComboG(montop, "movFuenteIngreso");
                        transferenciaCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }
                }
            }
            cargarComboComponentes();
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
        if (anio != null && sedeSeleccionadaFiltro != null) {
            reporte = Boolean.FALSE;
        } else {
            reporte = Boolean.TRUE;
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
            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
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

    public SofisComboG<SgMovimientos> getTransferenciaCombo() {
        return transferenciaCombo;
    }

    public void setTransferenciaCombo(SofisComboG<SgMovimientos> transferenciaCombo) {
        this.transferenciaCombo = transferenciaCombo;
    }

    public SgMovimientos getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(SgMovimientos transferencia) {
        this.transferencia = transferencia;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SgAnioFiscal getAnio() {
        return anio;
    }

    public void setAnio(SgAnioFiscal anio) {
        this.anio = anio;
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

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }
    // </editor-fold>
}
