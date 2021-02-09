/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.datatypes.DataVerIndicadorPOA;
import gob.mined.siap2.business.datatypes.DataVerIndicadorTipo;
import gob.mined.siap2.business.datatypes.DataVerValoresValorPOA;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresDelegate;
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresPOADelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * de ver metas de indicadores
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "metaDeIndicadoresVerPOA")
public class VerMetaDeIndicadoresPOA implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    JSFUtils jSFUtils;
    @Inject
    UtilsMB utilsMB;
    @Inject
    UsuarioInfo userInfo;
    @Inject
    ValoresDeIndicadoresDelegate valoresIndicadoresDelegate;
    
    @Inject
    ValoresDeIndicadoresPOADelegate valoresIndicadoresPOADelegate;
    
    
    @Inject
    TextMB textMB;

    private String idAnioFiscal;
    private String idProgramaInstitucional;
    
    List<DataVerIndicadorPOA> tiposDeIndicadoresPOA;
    
    private DataVerValoresValorPOA valorVisualizando;

    @PostConstruct
    public void init() {
        initFilter();
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        AnioFiscal anioFiscal = utilsMB.getAnioFiscalActual();
        if (anioFiscal != null) {
            idAnioFiscal = anioFiscal.getId().toString();
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
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }

            Integer mIdProgInstitucional = null;
            if (!TextUtils.isEmpty(idProgramaInstitucional)){
                mIdProgInstitucional = Integer.valueOf(idProgramaInstitucional);
            }
            //tiposDeIndicadores = valoresIndicadoresDelegate.getVisualizacionDeValoresDeIndicadores(Integer.valueOf(idAnioFiscal), mIdProgInstitucional, mIdProgPresupuestario);

            tiposDeIndicadoresPOA = valoresIndicadoresPOADelegate.getVisualizacionDeValoresDeIndicadoresPOA(Integer.valueOf(idAnioFiscal), mIdProgInstitucional);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Este método retorna el valor del texto que se usara en el tooltip
     * 
     * @param valor
     * @return 
     */
    public String getTextoTooltip(DataVerValoresValorPOA valor){
        String[] params ={valor.getAlcanceYLimitante(), valor.getMedioVerificacion()};
        String texto = textMB.obtenerTextoConParams("labels.ALCANCE_0_VERIFICACION_1", params);
        return texto;
    }
    
    /**
     * Este método retorna el valor de un numérico con el formato adecuado
     * @param v
     * @return 
     */
    public String getValorToString(DataVerValoresValorPOA v){
        if (v == null ){
            return "";
        }
        if (v.getValor() == null ){
            return "";
        }
        return String.valueOf(v.getValor());
    }
    
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
   

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public UtilsMB getUtilsMB() {
        return utilsMB;
    }

    public void setUtilsMB(UtilsMB utilsMB) {
        this.utilsMB = utilsMB;
    }

    public UsuarioInfo getUserInfo() {
        return userInfo;
    }

    public String getIdProgramaInstitucional() {
        return idProgramaInstitucional;
    }

    public void setIdProgramaInstitucional(String idProgramaInstitucional) {
        this.idProgramaInstitucional = idProgramaInstitucional;
    }
    
    public void setUserInfo(UsuarioInfo userInfo) {
        this.userInfo = userInfo;
    }

    public DataVerValoresValorPOA getValorVisualizando() {
        return valorVisualizando;
    }

    public void setValorVisualizando(DataVerValoresValorPOA valorVisualizando) {
        this.valorVisualizando = valorVisualizando;
    }

    

    public ValoresDeIndicadoresDelegate getValoresIndicadoresDelegate() {
        return valoresIndicadoresDelegate;
    }

    public void setValoresIndicadoresDelegate(ValoresDeIndicadoresDelegate valoresIndicadoresDelegate) {
        this.valoresIndicadoresDelegate = valoresIndicadoresDelegate;
    }

    public TextMB getTextMB() {
        return textMB;
    }

    public void setTextMB(TextMB textMB) {
        this.textMB = textMB;
    }
    
     public List<DataVerIndicadorPOA> getTiposDeIndicadoresPOA() {
        return tiposDeIndicadoresPOA;
    }

    public void setTiposDeIndicadoresPOA(List<DataVerIndicadorPOA> tiposDeIndicadoresPOA) {
        this.tiposDeIndicadoresPOA = tiposDeIndicadoresPOA;
    }
    
    // </editor-fold>

   
}
