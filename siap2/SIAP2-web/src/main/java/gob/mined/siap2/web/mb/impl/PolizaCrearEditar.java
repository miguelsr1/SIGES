/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.FacturaPolizaConcentracion;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.enums.EstadoPoliza;
import gob.mined.siap2.entities.enums.TipoDestinoFactura;
import gob.mined.siap2.entities.enums.TipoFactura;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralPODelegate;
import gob.mined.siap2.web.delegates.impl.PolizaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "polizaCE")
public class PolizaCrearEditar extends SelectOneUTBean implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    GeneralPODelegate poaDelegate;
    @Inject
    TextMB textMB;
    ;
    @Inject
    PolizaDelegate polizaDelegate;

    private boolean update = false;
    private PolizaDeConcentracion objeto;

    private POInsumos insumoSelecionado;
    private Map<String, String> fuetnes;
    private String idFuente;
    private DualListModel<Impuesto> impuestos = new DualListModel(new LinkedList(), new LinkedList());
    private FacturaPolizaConcentracion tempFactura;
    private Boolean nuevaFactura;
    private String idPOMontoFuenteFCM;
    private String volverAConsulta;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        String idInsumoParaPoliza = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idInsumoParaPoliza");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = polizaDelegate.getPoliza(Integer.valueOf(id));
        } else {
            objeto = new PolizaDeConcentracion();
            objeto.setEstado(EstadoPoliza.INGRESADA);
            objeto.setImpuestos(new LinkedList<Impuesto>());
            //Si esto no es null, es porque viene desde el crear de la Consulta de Insumos No UACI
            if (!TextUtils.isEmpty(idInsumoParaPoliza)) {
                POInsumos poInsumo = poaDelegate.getPOInsumoByID(Integer.valueOf(idInsumoParaPoliza));
//                insumoSelecionado = poInsumo;
                initInsumoSelecionado(poInsumo);
            }
        }
        if (objeto.getFuente() != null) {
            initInsumoSelecionado(objeto.getFuente().getInsumo());
            idFuente = objeto.getFuente().getId().toString();
        }
        if (objeto.getPagoFuenteFCM() != null) {
            idPOMontoFuenteFCM = objeto.getPagoFuenteFCM().getId().toString();
            cargarPagosParaFuenteMeses();
        }

        //se cargan los impuestos
        List<Impuesto> disponibles = versionDelegate.getClasesGeneralCodiguera(Impuesto.class);
        List<Impuesto> enUso = new LinkedList<>();
        if (objeto.getId() == null) {
            for (Impuesto impuestoDisp : disponibles) {
                if (impuestoDisp.getPorDefectoEnContrato() != null && impuestoDisp.getPorDefectoEnContrato()) {
                    enUso.add(impuestoDisp);
                }
            }
            for (Impuesto impuestoEnUso : enUso) {
                disponibles.remove(impuestoEnUso);
            }
        }
        for (Impuesto i : objeto.getImpuestos()) {
            enUso.add(i);
            if (disponibles.contains(i)) {
                disponibles.remove(i);
            }
        }
        impuestos = new DualListModel(disponibles, enUso);
        volverAConsulta = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("volverAConsulta");
        
    }

    /**
     * Guarda el objeto en edición
     *
     * @return
     */
    public String guardar() {
        try {

            objeto.setImpuestos(new LinkedList(impuestos.getTarget()));

            objeto = polizaDelegate.guardarPoliza(objeto, Integer.parseInt(idPOMontoFuenteFCM));

            if (volverAConsulta != null && volverAConsulta.equals("true")) {
                return "consultaPoliza.xhtml?faces-redirect=true";
            } else {
                return "consultaInsumosNoUACI.xhtml?faces-redirect=true";
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    /**
     * Dirige el sitio a la página de consulta de Pólizas
     *
     * @return
     */
    public String cerrar() {
//        volverAConsulta = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("volverAConsulta");
        if (volverAConsulta != null && volverAConsulta.equals("true")) {
            return "consultaPoliza.xhtml?faces-redirect=true";
        } else {
            return "consultaInsumosNoUACI.xhtml?faces-redirect=true";
        }
    }

    /**
     * Cambia el estado de la póliza a: Aplicada
     */
    public void cambiarAAplicada() {
        objeto.setEstado(EstadoPoliza.APLICADA);
        objeto.setFechaEmision(new Date());
        RequestContext.getCurrentInstance().execute("$('#cambiarEstadoPoliza').modal('hide');");
    }

    /**
     * Cambia el estado de la póliza a: Revertida
     */
    public void cambiarARevertida() {
        objeto.setEstado(EstadoPoliza.REVERTIDA);
        objeto.setFechaEmision(null);
        RequestContext.getCurrentInstance().execute("$('#cambiarEstadoPoliza').modal('hide');");
    }

    /**
     * Al seleccionar un insumo obtiene todos sus datos para mostrarlos
     * inmediatamente
     *
     * @param event
     */
    public void onItemSelect(SelectEvent event) {
        POInsumos insumo = (POInsumos) event.getObject();
        if (insumo != null) {
            insumo = poaDelegate.getPOInsumoByID(insumo.getId());
        }
        initInsumoSelecionado(insumo);
    }

    /**
     * Inicializa los datos de las fuentes del insumo seleccionado
     *
     * @param pOInsumo
     */
    public void initInsumoSelecionado(POInsumos pOInsumo) {
        insumoSelecionado = pOInsumo;
        fuetnes = new LinkedHashMap();
        cargarPagosParaFuenteMeses();
        if (insumoSelecionado != null) {
            String codigoMoneda = textMB.obtenerTexto("labels.MonedaSistema");
            for (POMontoFuenteInsumo fuente : insumoSelecionado.getMontosFuentes()) {
                if (fuente.getCertificadoDisponibilidadPresupuestariaAprobada() != null && fuente.getCertificadoDisponibilidadPresupuestariaAprobada()) {
                    if(fuente.getMontosFuentesInsumosFCM()!=null && !fuente.getMontosFuentesInsumosFCM().isEmpty()){
                        String nombre = "";
                        if (fuente.getFuente() != null && fuente.getFuente().getFuenteRecursos() != null) {
                            nombre += fuente.getFuente().getFuenteRecursos().getCodigo() + " ";
                        }
                        if (fuente.getFuente() != null && fuente.getFuente().getCategoriaConvenio() != null) {
                            nombre += fuente.getFuente().getCategoriaConvenio().getCodigo();
                        }
                        nombre = nombre + " (" + codigoMoneda + NumberUtils.nomberToString(fuente.getCertificado()) + ")";
                        fuetnes.put(nombre, fuente.getId().toString());
                    }
                }
            }
        }
        idFuente = null;
    }

    /**
     * Este método inicializa una factura.
     *
     * @param factura
     */
    public void initTempFactura(FacturaPolizaConcentracion factura) {
        tempFactura = factura;
        if (tempFactura == null) {
            nuevaFactura = true;
            tempFactura = new FacturaPolizaConcentracion();
            tempFactura.setTipo(TipoFactura.FACTURA);
            tempFactura.setDestinoFactura(TipoDestinoFactura.POLIZA_CONCENTRACION);
        } else {
            nuevaFactura = false;
        }
    }

    /**
     * Este método corresponde al evento de eliminación de una factura.
     *
     * @param idFactura
     */
    public void eliminarFactura(FacturaPolizaConcentracion facturaEliminar) {
        try {
            if (objeto.getFacturas().contains(facturaEliminar)) {
                facturaEliminar.setPolizaConcentracion(null);
                objeto.getFacturas().remove(facturaEliminar);
            }

            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método corresponde al evento para guardar una factura.
     */
    public void guardarFactura() {
        try {
            if (nuevaFactura) {
                tempFactura = polizaDelegate.guardarFactura(tempFactura);
                if (objeto.getFacturas() == null) {
                    objeto.setFacturas(new LinkedList<FacturaPolizaConcentracion>());
                }
                tempFactura.setPolizaConcentracion(objeto);
                objeto.getFacturas().add(tempFactura);
            }
            RequestContext.getCurrentInstance().execute("$('#addFactura').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    public HashMap<String, String> cargarPagosParaFuenteMeses() {
        HashMap<String, String> mapFlujoCajaAniosMeses = new HashMap<String, String>();
        if (objeto.getFuente() != null) {
            //Si la fuente seleccionada está aprobada
            if (objeto.getFuente().getCertificadoDisponibilidadPresupuestariaAprobada() != null && objeto.getFuente().getCertificadoDisponibilidadPresupuestariaAprobada()) {
                if (objeto.getFuente().getMontosFuentesInsumosFCM() != null) {
                    for (int i = 0; i < objeto.getFuente().getMontosFuentesInsumosFCM().size(); i++) {
                        POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM = objeto.getFuente().getMontosFuentesInsumosFCM().get(i);
                        if (montoFuenteFCM.getMontoCertificado() != null && montoFuenteFCM.getMontoCertificado().compareTo(BigDecimal.ZERO) > 0) {
                            POFlujoCajaMenusal fcm = montoFuenteFCM.getFlujoCajaMensual();

                            String mes = UtilsMB.getTituloSeguimiento(TipoSeguimiento.MENSUAL, (fcm.getMes() - 1));
                            String montoCertParaFuenteYMes = NumberUtils.nomberToString(montoFuenteFCM.getMontoCertificado());
                            String codigoMoneda = textMB.obtenerTexto("labels.MonedaSistema");
                            String descripcionPago = mes + " - " + codigoMoneda + " " + montoCertParaFuenteYMes;
                            mapFlujoCajaAniosMeses.put(descripcionPago, String.valueOf(montoFuenteFCM.getId()));
                        }
                    }
                }
            }
        }
        return TextUtils.ordenarMapDeStringByValue(mapFlujoCajaAniosMeses);
    }

    public void asociarFuenteAPoliza(Integer idFuente) {
        if (idFuente != null) {
            POMontoFuenteInsumo fuente = poaDelegate.getMontoFuenteByID(idFuente);
            objeto.setFuente(fuente);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public static Logger getLogger() {
        return logger;
    }

    public PolizaDeConcentracion getObjeto() {
        return objeto;
    }

    public void setObjeto(PolizaDeConcentracion objeto) {
        this.objeto = objeto;
    }

    public POInsumos getInsumoSelecionado() {
        return insumoSelecionado;
    }

    public void setInsumoSelecionado(POInsumos insumoSelecionado) {
        this.insumoSelecionado = insumoSelecionado;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public Map<String, String> getFuetnes() {
        return fuetnes;
    }

    public void setFuetnes(Map<String, String> fuetnes) {
        this.fuetnes = fuetnes;
    }

    public String getIdFuente() {
        return idFuente;
    }

    public DualListModel<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(DualListModel<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }

    public void setIdFuente(String idFuente) {
        this.idFuente = idFuente;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public FacturaPolizaConcentracion getTempFactura() {
        return tempFactura;
    }

    public void setTempFactura(FacturaPolizaConcentracion tempFactura) {
        this.tempFactura = tempFactura;
    }

    public String getIdPOMontoFuenteFCM() {
        return idPOMontoFuenteFCM;
    }

    public void setIdPOMontoFuenteFCM(String idPOFlujoCajaMenusal) {
        this.idPOMontoFuenteFCM = idPOFlujoCajaMenusal;
    }

    // </editor-fold>
}
