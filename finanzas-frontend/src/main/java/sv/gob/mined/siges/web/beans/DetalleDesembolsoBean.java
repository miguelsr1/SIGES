/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.DesembolsoCE;
import sv.gob.mined.siges.web.dto.DesembolsoMovimiento;
import sv.gob.mined.siges.web.dto.SgDesembolso;
import sv.gob.mined.siges.web.dto.SgDesembolsoCed;
import sv.gob.mined.siges.web.dto.SgDetalleDesembolso;
import sv.gob.mined.siges.web.dto.SgReqFondoCed;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoDesembolso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDesembolsoCed;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetalleDesembolso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReqFondoCed;
import sv.gob.mined.siges.web.lazymodels.LazyDesembolsoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DesembolsoCedRestClient;
import sv.gob.mined.siges.web.restclient.DetalleDesembolsosRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.ReqFondoCedRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
/**
 * Bean para la gestión de desembolsos.
 *
 * @author Sofis Solutions
 */
public class DetalleDesembolsoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DetalleDesembolsoBean.class.getName());

    @Inject
    private DetalleDesembolsosRestClient restDetDesembolso;

    @Inject
    private ReqFondoCedRestClient restReqFondoCed;

    @Inject
    private DesembolsoCedRestClient restDesembolsoCed;

    @Inject
    private MovimientoCuentaBancariaRestClient restMovCuentaBC;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id")
    private Long detDesembolsoId;

    private SgDetalleDesembolso entidadEnEdicion = new SgDetalleDesembolso();
    private FiltroDetalleDesembolso filtro = new FiltroDetalleDesembolso();
    private FiltroReqFondoCed filtroReqFondoCed = new FiltroReqFondoCed();
    private FiltroDesembolsoCed filtroDesembolsoCed = new FiltroDesembolsoCed();

    private Long totalResultados;
    private Double porcentajeAplicar;
    private BigDecimal montoAplicar;
    private Integer paginado = 10;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean desembolsosGuardados = Boolean.FALSE;
    private Boolean desembolsosAutorizados = Boolean.FALSE;
    private Boolean desembolsosDepositados = Boolean.FALSE;
    private Boolean saving = Boolean.FALSE;
    private BigDecimal totalDesembolsos = BigDecimal.ZERO;
    private LocalDateTime fechaMovs = LocalDateTime.now();
    private List<SgReqFondoCed> listReqFondoCed = new ArrayList();
    private List<DesembolsoCE> listDesNoDepositados = new ArrayList();
    private List<SgDesembolsoCed> listDesembolsoCed = new ArrayList();
    private List<EnumTipoDesembolso> tipoDesembolso;
    private Boolean monto = Boolean.FALSE;
    private Boolean porcentaje = Boolean.FALSE;
    private Boolean remanente = Boolean.FALSE;

    private LazyDesembolsoDataModel desembolsoDataModel;

    public DetalleDesembolsoBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            if (detDesembolsoId != null && detDesembolsoId > 0) {

                filtro.setDedPk(detDesembolsoId);
                filtro.setIncluirCampos(new String[]{
                    "dedPk", "dedPorcentaje", "dedMonto", "dedVersion",
                    "dedReqFondoFk.strPk", "dedReqFondoFk.strVersion",
                    "dedFuenteRecursosFk.id", "dedFuenteRecursosFk.nombre", "dedFuenteRecursosFk.version",
                    "dedFuenteRecursosFk.fuenteFinanciamiento.nombre",
                    "dedDesembolsoFk.dsbPk", "dedDesembolsoFk.dsbEstado", "dedDesembolsoFk.dsbVersion"
                });

                List<SgDetalleDesembolso> list = restDetDesembolso.buscar(filtro);
                if (!list.isEmpty()) {
                    entidadEnEdicion = list.get(0);
                    if (entidadEnEdicion != null) {
                        cargarCombos();
                        buscar();
                        porcentajeAplicar = entidadEnEdicion.getDedPorcentaje();
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de filtro
     */
    public void cargarCombos() {
        try {
            tipoDesembolso = new ArrayList(Arrays.asList(EnumTipoDesembolso.values()));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {

            filtroDesembolsoCed.setFirst(new Long(0));
            filtroDesembolsoCed.setOrderBy(new String[]{"dcePk"});
            filtroDesembolsoCed.setIncluirCampos(new String[]{
                "dcePk",
                "dceDetDesembolsoFk.dedPk",
                "dceDetDesembolsoFk.dedVersion",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.recibo.recPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedCodigo",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedNombre",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedTipo",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedVersion",
                "dceReqFondoCedFk.rfcMonto",
                "dceEstado", "dceMonto", "dceVersion"});
            filtroDesembolsoCed.setAscending(new boolean[]{false});
            filtroDesembolsoCed.setDceDetDesembolsoFk(entidadEnEdicion.getDedPk());
            listDesembolsoCed = restDesembolsoCed.buscar(filtroDesembolsoCed);
            desembolsosGuardados = Boolean.TRUE;

            if (listDesembolsoCed.isEmpty()) {
                listDesNoDepositados = new ArrayList();
                Double porcentaje = entidadEnEdicion.getDedPorcentaje() / 100;
                listDesNoDepositados = restDesembolsoCed.obtenerDesembolsosNoDep(entidadEnEdicion.getDedReqFondoFk().getStrPk(), entidadEnEdicion.getDedFuenteRecursosFk().getId());

                listDesNoDepositados.stream().forEach(d -> {
                    d.setDedPk(entidadEnEdicion.getDedPk());
                    d.setMontoADesembolsar(d.getMontoAutorizado().multiply(BigDecimal.valueOf(porcentaje)));
                });
                totalResultados = new Long(listDesNoDepositados.size());
                totalDesembolsos = listDesNoDepositados.stream().map(l -> l.getMontoADesembolsar()).reduce(BigDecimal.ZERO, BigDecimal::add);
                desembolsosGuardados = Boolean.FALSE;
            } else {
                if (!listDesembolsoCed.isEmpty()) {
                    totalResultados = Long.valueOf(String.valueOf(listDesembolsoCed.size()));
                    totalDesembolsos();
                    if (listDesembolsoCed.get(0).getDceEstado().equals(EnumDesembolsoEstado.DEPOSITADO)) {
                        desembolsosDepositados = Boolean.TRUE;
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Aplicar porcentaje a los monto de desembolsos
     */
    public void aplicarPorcentaje() {
        try {
            if (porcentajeAplicar != null) {
                Double porc = porcentajeAplicar / 100;
                listDesembolsoCed.stream().forEach(d -> {
                    d.setDceMonto(d.getDceReqFondoCedFk().getRfcMonto().multiply(BigDecimal.valueOf(porc)));
                });
                listDesNoDepositados.stream().forEach(d -> {
                    d.setMontoADesembolsar(d.getMontoAutorizado().multiply(BigDecimal.valueOf(porc)));
                });
                totalDesembolsos = listDesNoDepositados.stream().map(l -> l.getMontoADesembolsar()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Aplicar los montos a los desembolsos.
     */
    public void aplicarMonto() {
        try {
            if (montoAplicar != null) {
                for (int i = 0; i < listDesNoDepositados.size(); i++) {

                    DesembolsoCE desembolso = listDesNoDepositados.get(i);
                    if (montoAplicar.compareTo(desembolso.getMontoAutorizado()) == 1) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_MONTO_AUTORIZADO_MENOR), "");
                        break;
                    } else {
                        desembolso.setMontoADesembolsar(montoAplicar);
                    }
                }
                totalDesembolsos = listDesNoDepositados.stream().map(l -> l.getMontoADesembolsar()).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Aplicar los montos a los desembolsos.
     */
    public void aplicarRemanente() {
        try {

//             listDesembolsoCed.stream().forEach(d -> {
//                    d.setDceMonto(d.getDceReqFondoCedFk().getRfcMonto().multiply(BigDecimal.valueOf(porc)));
//                });
            listDesNoDepositados.stream().forEach(d -> {
                d.setMontoADesembolsar(d.getMontoAutorizado().subtract(d.getMontoDesembolsado()));
            });
            totalDesembolsos = listDesNoDepositados.stream().map(l -> l.getMontoADesembolsar()).reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Ajax tipo de desembolso.
     */
    public void tipoDesembolso() {
        monto = Boolean.FALSE;
        porcentaje = Boolean.FALSE;
        remanente = Boolean.FALSE;
        if (entidadEnEdicion.getDedTipoDesembolso() != null) {
            if (entidadEnEdicion.getDedTipoDesembolso().equals(EnumTipoDesembolso.MONTO)) {
                monto = Boolean.TRUE;
            }
            if (entidadEnEdicion.getDedTipoDesembolso().equals(EnumTipoDesembolso.PORCENTAJE)) {
                porcentaje = Boolean.TRUE;
            }
            if (entidadEnEdicion.getDedTipoDesembolso().equals(EnumTipoDesembolso.REMANENTE)) {
                remanente = Boolean.TRUE;
            }
        }
    }

    /**
     * Suma total de los desembolsos guardados
     */
    public void totalDesembolsos() {
        try {
            totalDesembolsos = listDesembolsoCed.stream().map(d -> d.getDceMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Carga los datos de un desembolso para poder ser editados.
     *
     * @param req
     */
    public void actualizar(SgDesembolso req) {
        try {
            //entidadEnEdicion = req;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            //historialMovimientos = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        //filtro = new FiltroDetalleDesembolso();
        buscar();
    }

    /**
     * Crea o actualiza los desembolsos a ced
     */
    public void guardarDesembolsosCed() {
        try {
            restDesembolsoCed.guardarLitado(listDesNoDepositados);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Generar movimientos a cuentas bancarias de ced
     */
    public void generarMovimientos() {
        try {
            DesembolsoMovimiento desMov = new DesembolsoMovimiento();
            desMov.setDesembolsoPk(entidadEnEdicion.getDedPk());
            desMov.setFechaMov(fechaMovs);
            restDesembolsoCed.generarMovimientos(desMov);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            PrimeFaces.current().executeScript("PF('generarMovsDialog').hide()");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Inicialización de las variables para generación de movimientos
     */
    public void nuevosMovs() {
        saving = Boolean.FALSE;
    }

    /**
     * Render en la generación de movimientos a cuenta bancaria
     */
    public void renderLoadingGenerarMovs() {
        if (saving) {
            saving = Boolean.FALSE;
            generarMovimientos();
        } else {
            saving = Boolean.TRUE;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public void setEntidadEnEdicion(SgDesembolso entidadEnEdicion) {
        //this.entidadEnEdicion = entidadEnEdicion;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public LazyDesembolsoDataModel getDesembolsoDataModel() {
        return desembolsoDataModel;
    }

    public void setDesembolsoDataModel(LazyDesembolsoDataModel desembolsoDataModel) {
        this.desembolsoDataModel = desembolsoDataModel;
    }

    public FiltroReqFondoCed getFiltroReqFondoCed() {
        return filtroReqFondoCed;
    }

    public void setFiltroReqFondoCed(FiltroReqFondoCed filtroReqFondoCed) {
        this.filtroReqFondoCed = filtroReqFondoCed;
    }

    public List<SgDesembolsoCed> getListDesembolsoCed() {
        return listDesembolsoCed;
    }

    public void setListDesembolsoCed(List<SgDesembolsoCed> listDesembolsoCed) {
        this.listDesembolsoCed = listDesembolsoCed;
    }

    public SgDetalleDesembolso getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDetalleDesembolso entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getDesembolsosGuardados() {
        return desembolsosGuardados;
    }

    public void setDesembolsosGuardados(Boolean desembolsosGuardados) {
        this.desembolsosGuardados = desembolsosGuardados;
    }

    public Boolean getDesembolsosAutorizados() {
        return desembolsosAutorizados;
    }

    public void setDesembolsosAutorizados(Boolean desembolsosAutorizados) {
        this.desembolsosAutorizados = desembolsosAutorizados;
    }

    public FiltroDesembolsoCed getFiltroDesembolsoCed() {
        return filtroDesembolsoCed;
    }

    public void setFiltroDesembolsoCed(FiltroDesembolsoCed filtroDesembolsoCed) {
        this.filtroDesembolsoCed = filtroDesembolsoCed;
    }

    public BigDecimal getTotalDesembolsos() {
        return totalDesembolsos;
    }

    public void setTotalDesembolsos(BigDecimal totalDesembolsos) {
        this.totalDesembolsos = totalDesembolsos;
    }

    public Double getPorcentajeAplicar() {
        return porcentajeAplicar;
    }

    public void setPorcentajeAplicar(Double porcentajeAplicar) {
        this.porcentajeAplicar = porcentajeAplicar;
    }

    public Boolean getDesembolsosDepositados() {
        return desembolsosDepositados;
    }

    public void setDesembolsosDepositados(Boolean desembolsosDepositados) {
        this.desembolsosDepositados = desembolsosDepositados;
    }

    public LocalDateTime getFechaMovs() {
        return fechaMovs;
    }

    public void setFechaMovs(LocalDateTime fechaMovs) {
        this.fechaMovs = fechaMovs;
    }

    public Boolean getSaving() {
        return saving;
    }

    public void setSaving(Boolean saving) {
        this.saving = saving;
    }

    public List<EnumTipoDesembolso> getTipoDesembolso() {
        return tipoDesembolso;
    }

    public void setTipoDesembolso(List<EnumTipoDesembolso> tipoDesembolso) {
        this.tipoDesembolso = tipoDesembolso;
    }

    public BigDecimal getMontoAplicar() {
        return montoAplicar;
    }

    public void setMontoAplicar(BigDecimal montoAplicar) {
        this.montoAplicar = montoAplicar;
    }

    public List<DesembolsoCE> getListDesNoDepositados() {
        return listDesNoDepositados;
    }

    public void setListDesNoDepositados(List<DesembolsoCE> listDesNoDepositados) {
        this.listDesNoDepositados = listDesNoDepositados;
    }

    public Boolean getMonto() {
        return monto;
    }

    public void setMonto(Boolean monto) {
        this.monto = monto;
    }

    public Boolean getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Boolean porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getRemanente() {
        return remanente;
    }

    public void setRemanente(Boolean remanente) {
        this.remanente = remanente;
    }

    // </editor-fold>
}
