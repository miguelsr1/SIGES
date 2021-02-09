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
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EstadosInsumosDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.dataProvider.InsumosNoUACIDataProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
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
@Named(value = "consultaInsumosNoUACI")
public class ConsultaInsumosNoUACI implements Serializable {

    @Inject
    InsumoDelegate insumoDelegate;
    @Inject
    ContratoDelegate contratoDelegate;
    @Inject
    JSFUtils jSFUtils;

    private LazyDataModel<POInsumos> lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String idProyecto;
    private String idAnioFiscal;
    private String idAC;
    private String idANP;
    private String codigoInterno;
    private String codigo;
    private String idUT;
    private List<PolizaDeConcentracion> listaPolizasDelInsumo;   
    
    @PostConstruct
    public void init() {
        initFilter();
        filterTable();
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        setIdAnioFiscal(null);
        setIdProyecto(null);
        setIdAC(null);
        setIdANP(null);
        setIdUT(null);
        setCodigo(null);
        setCodigoInterno(null);

    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            Integer mIdAnioFiscal = NumberUtils.getIntegerONull(idAnioFiscal);
            Integer mIdProyecto = NumberUtils.getIntegerONull(idProyecto);
            Integer mIdAC = NumberUtils.getIntegerONull(idAC);
            Integer mIdANP = NumberUtils.getIntegerONull(idANP);
            Integer midUT = NumberUtils.getIntegerONull(idUT);
            String mCodigo = !TextUtils.isEmpty(codigo) ? codigo : null;
            String mCodigoInterno = !TextUtils.isEmpty(codigoInterno) ? codigoInterno : null;

            String[] orderBy = {"id"};
            boolean[] asc = {false};

            IDataProvider dataProviderG = new InsumosNoUACIDataProvider(insumoDelegate, mIdAnioFiscal, mIdProyecto, mIdAC, mIdANP, midUT, mCodigo, mCodigoInterno, orderBy, asc);
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

    public void borrarOtrosFiltros(String filtroAplicado) {
        switch (filtroAplicado) {
            case "P":
                idAC = null;
                idANP = null;
                break;
            case "AC":
                idProyecto = null;
                idANP = null;
                break;
            case "ANP":
                idProyecto = null;
                idAC = null;
                break;
        }
    }
    
    public void cargarlistaPolizasParaVer(Integer idPoInsumo){
        try{
            listaPolizasDelInsumo = insumoDelegate.getPolizasDePoInsumo(idPoInsumo);
            
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
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

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public String getIdAC() {
        return idAC;
    }

    public void setIdAC(String idAC) {
        this.idAC = idAC;
    }

    public String getIdANP() {
        return idANP;
    }

    public void setIdANP(String idANP) {
        this.idANP = idANP;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdUT() {
        return idUT;
    }

    public void setIdUT(String idUT) {
        this.idUT = idUT;
    }
    public List<PolizaDeConcentracion> getListaPolizasDelInsumo() {
        return listaPolizasDelInsumo;
    }

    public void setListaPolizasDelInsumo(List<PolizaDeConcentracion> listaPolizasDelInsumo) {
        this.listaPolizasDelInsumo = listaPolizasDelInsumo;
    }
    // </editor-fold>

}
