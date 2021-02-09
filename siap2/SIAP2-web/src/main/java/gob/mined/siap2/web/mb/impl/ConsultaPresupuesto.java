/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EstadosInsumosDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "consultaPresupuesto")
public class ConsultaPresupuesto implements Serializable {

    @Inject
    InsumoDelegate insumoDelegate;
    @Inject
    ContratoDelegate contratoDelegate;
    @Inject
    JSFUtils jSFUtils;

    private LazyDataModel<POInsumos> lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String idInsumo;
    private String idAnioFiscal;

    private String idPOA;
    private String idProyecto;
    private String idProcesoAdq;
    private String idObjetoEspecificoGasto;
    private String uaci;
    private String codigoSIIP;
    private String idProgramaPresupuestario;
    private String idSubProgramaPresupuestario;

    private BigDecimal totalBase;
    private BigDecimal totalEstimado;
    private BigDecimal totalCertificado;
    private BigDecimal totalAdjudicado;
    private BigDecimal totalComprometido;
    private BigDecimal totalPagado;
    private BigDecimal totalSaldo;
    

    @PostConstruct
    public void init() {
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        idInsumo = null;
        idAnioFiscal = null;
        idPOA = null;
        idProyecto = null;
        idProcesoAdq = null;
        idObjetoEspecificoGasto = null;
        uaci = null;
        codigoSIIP = null;
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            Integer mIdInsumo = NumberUtils.getIntegerONull(idInsumo);
            Integer mIdAnioFiscal = NumberUtils.getIntegerONull(idAnioFiscal);
            Integer mIdPOA = NumberUtils.getIntegerONull(idPOA);
            Integer mIdProyecto = NumberUtils.getIntegerONull(idProyecto);
            Integer mIdProcesoAdq = NumberUtils.getIntegerONull(idProcesoAdq);
            Integer miObjClasificador = NumberUtils.getIntegerONull(idObjetoEspecificoGasto);            
            Integer mIdProgramaPresupuestario= NumberUtils.getIntegerONull(idProgramaPresupuestario);
            Integer mIdSubProgramaPresupuestario = NumberUtils.getIntegerONull(idSubProgramaPresupuestario);
            Boolean miNoUACI = null;
            if (uaci != null && uaci.trim().length() > 0) {
                if (uaci.equals("NOUACI")) {
                    miNoUACI = true;
                } else {
                    miNoUACI = false;
                }
            }
            String miCodigoSIIP = null;
            if (codigoSIIP != null && codigoSIIP.trim().length() > 0) {
                miCodigoSIIP = codigoSIIP;
            }

            totalBase = insumoDelegate.sumarEstadosInsumos("montoPepOriginal", miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario);
            if (totalBase == null) {
                totalBase = BigDecimal.ZERO;
            }

            totalEstimado = insumoDelegate.sumarEstadosInsumos("montoTotal", miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario);
            if (totalEstimado == null) {
                totalEstimado = BigDecimal.ZERO;
            }

            totalCertificado = insumoDelegate.sumarEstadosInsumos("montoTotalCertificado", miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario);
            if (totalCertificado == null) {
                totalCertificado = BigDecimal.ZERO;
            }

            totalAdjudicado = insumoDelegate.sumarEstadosInsumos("procesoInsumo.montoTotalAdjudicado", miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario);
            if (totalAdjudicado == null) {
                totalAdjudicado = BigDecimal.ZERO;
            }

            totalComprometido = insumoDelegate.sumarEstadosInsumos("montoComprometido", miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario);
            if (totalComprometido == null) {
                totalComprometido = BigDecimal.ZERO;
            }

            totalPagado = insumoDelegate.sumarPagadoInsumos(miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario);
            if (totalPagado == null) {
                totalPagado = BigDecimal.ZERO;
            }

            if (totalBase != null && totalAdjudicado != null) {
                totalSaldo = totalBase.subtract(totalAdjudicado);
            }

            String[] orderBy = {"id"};
            boolean[] asc = {false};

            IDataProvider dataProviderG = new EstadosInsumosDataProvider(insumoDelegate, miNoUACI, miCodigoSIIP, miObjClasificador, mIdInsumo, mIdAnioFiscal, mIdPOA, mIdProyecto, mIdProcesoAdq, mIdProgramaPresupuestario, mIdSubProgramaPresupuestario,orderBy, asc);
            LazyDataModel var = new GeneralLazyDataModel(dataProviderG);

            lazyModel = var;

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    /**
     * Este método permite obtener el monto pagado por insumo
     *
     * @param idPOInsumo
     * @return
     */
    public BigDecimal getMontoPagado(Integer idPOInsumo) {
        return contratoDelegate.getMontoPagadoPorInsumo(idPOInsumo);
    }

    /**
     * Devuelve monto adjudicado
     *
     * @return
     */
    public BigDecimal getAdjudicado() {
        try {
            return BigDecimal.ZERO;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Devuelve monto comprometido
     *
     * @return
     */
    public BigDecimal getComprometido() {
        try {
            return BigDecimal.ZERO;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Devuelve monto pagado
     *
     * @return
     */
    public BigDecimal getPagado() {
        try {
            return BigDecimal.ZERO;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcula y retorna el monto del insumo asociado a Pólizas de concentración
     * aprobadas
     *
     * @return
     */
    public BigDecimal calcularMontoEnPolizasAprobadas(Integer idPoInsumo) {
        return insumoDelegate.calcularMontoEnPolizasAprobadas(idPoInsumo);
    }
    
    /**
     * Calcula la diferencia entre el monto estimado y el certificado de un insumo
     * @param insumo
     * @return 
     */
    public BigDecimal calcularDiferenciaEntreEstimadoYCertificado (POInsumos insumo){
        return insumoDelegate.calcularDiferenciaEntreEstimadoYCertificado(insumo);
    }
    
    /**
     * Calcula la suma de los montos del insumo en las actas de recepción aprobadas
     * @return 
     */
    public BigDecimal calcularMontoActasRecepcionAprobadas (Integer idPoInsumo){
        return insumoDelegate.calcularMontoActasRecepcionAprobadas(idPoInsumo);
    }

   /**
    * Valor de PEP + valor de reprogramaciones
    * @param insumo
    * @return 
    */
    public BigDecimal calcularDisponibleModificado (POInsumos insumo){
        return insumoDelegate.calcularDisponibleModificado(insumo);        
    }
    
    /**
     * Suma de montos descertificados de las fuentes del insumo
     * @param insumo
     * @return 
     */
    public BigDecimal calcularMontoDescertificado (POInsumos insumo){
        return insumoDelegate.calcularMontoDescertificado(insumo);
    }
    
    /**
     * Monto disponible modificado - monto certificado + monto descertificado
     * @param insumo
     * @return 
     */
    public BigDecimal calcularMontoDisponible (POInsumos insumo){
        return insumoDelegate.calcularMontoDisponible(insumo);
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel<POInsumos> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<POInsumos> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getIdInsumo() {
        return idInsumo;
    }

    public String getIdPOA() {
        return idPOA;
    }

    public void setIdPOA(String idPOA) {
        this.idPOA = idPOA;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(String idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public InsumoDelegate getInsumoDelegate() {
        return insumoDelegate;
    }

    public void setInsumoDelegate(InsumoDelegate insumoDelegate) {
        this.insumoDelegate = insumoDelegate;
    }

    public ContratoDelegate getContratoDelegate() {
        return contratoDelegate;
    }

    public void setContratoDelegate(ContratoDelegate contratoDelegate) {
        this.contratoDelegate = contratoDelegate;
    }

    public String getIdObjetoEspecificoGasto() {
        return idObjetoEspecificoGasto;
    }

    public void setIdObjetoEspecificoGasto(String idObjetoEspecificoGasto) {
        this.idObjetoEspecificoGasto = idObjetoEspecificoGasto;
    }

    public String getUaci() {
        return uaci;
    }

    public void setUaci(String uaci) {
        this.uaci = uaci;
    }

    public BigDecimal getTotalBase() {
        return totalBase;
    }

    public void setTotalBase(BigDecimal totalBase) {
        this.totalBase = totalBase;
    }

    public BigDecimal getTotalEstimado() {
        return totalEstimado;
    }

    public void setTotalEstimado(BigDecimal totalEstimado) {
        this.totalEstimado = totalEstimado;
    }

    public BigDecimal getTotalCertificado() {
        return totalCertificado;
    }

    public void setTotalCertificado(BigDecimal totalCertificado) {
        this.totalCertificado = totalCertificado;
    }

    public BigDecimal getTotalAdjudicado() {
        return totalAdjudicado;
    }

    public void setTotalAdjudicado(BigDecimal totalAdjudicado) {
        this.totalAdjudicado = totalAdjudicado;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(BigDecimal totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    public String getCodigoSIIP() {
        return codigoSIIP;
    }

    public void setCodigoSIIP(String codigoSIIP) {
        this.codigoSIIP = codigoSIIP;
    }
    
    public String getIdProgramaPresupuestario() {
        return idProgramaPresupuestario;
    }

    public void setIdProgramaPresupuestario(String idProgramaPresupuestario) {
        this.idProgramaPresupuestario = idProgramaPresupuestario;
    }
    
    public String getIdSubProgramaPresupuestario() {
        return idSubProgramaPresupuestario;
    }

    public void setIdSubProgramaPresupuestario(String idSubProgramaPresupuestario) {
        this.idSubProgramaPresupuestario = idSubProgramaPresupuestario;
    }
    // </editor-fold>

}
