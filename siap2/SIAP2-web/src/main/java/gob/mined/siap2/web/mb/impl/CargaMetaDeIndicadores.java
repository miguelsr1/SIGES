/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.datatypes.DataTipoIndicador;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Esta clase implementa el backing bean correspondiente a la carga de metas de indicadores.
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "metaDeIndicadoresCarga")
public class CargaMetaDeIndicadores implements Serializable {

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
    TextMB textMB;

    private String idAnioFiscal;
    private String idUnidadTecnica;
    private List<Boolean> valoresHabilitados = new LinkedList<>();
    private List<UnidadTecnica> usuarioUnidadTecnicas;
    private List<DataTipoIndicador> indicadoresACargar;

    @PostConstruct
    public void init() {
        usuarioUnidadTecnicas = userInfo.getUTDeUsuarioConOperacion(ConstantesEstandares.Operaciones.CARGA_METAS_INDICADORES);
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
        idUnidadTecnica= null;  
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    /**
     * Este método implementa el evento correspondiente a la aplicación del filtro seleccionado.
     */
    public void filterTable() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)) {               
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }

            List<Integer> unidadesTecnicas = new LinkedList();
            if (TextUtils.isEmpty(idUnidadTecnica)){
                for (UnidadTecnica ut : usuarioUnidadTecnicas) {
                    unidadesTecnicas.add(ut.getId());
                }            
            }else{
                unidadesTecnicas.add(Integer.valueOf(idUnidadTecnica));
            }

            indicadoresACargar = valoresIndicadoresDelegate.getValoresDeIndicadores(unidadesTecnicas, Integer.valueOf(idAnioFiscal));
        
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Para mantener compatibilidad.
     */
    public void reloadValorIndicador() {
         
    }

    /**
     * Este método corresponde al evento que permite determinar si una fila está habilitada para cargar valores.
     * @param index
     * @return 
     */
    public boolean habilitadaCargaParaValor(int index) {
        if (index >= valoresHabilitados.size()) {
            return false;
        }
        return valoresHabilitados.get(index);

    }

    /**
     * Este método corresponde al evento de guardar un elemento que se está editando.
     */
    public void guardarEditando() {
        try {
                      
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#anadirIndicador').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método corresponde al evento de guardar los valores de los indicadores.
     */ 
    public void guardarTechos() {
        try {
            valoresIndicadoresDelegate.guardarTechos(indicadoresACargar);
            filterTable();

            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
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

    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    

    public List<UnidadTecnica> getUsuarioUnidadTecnicas() {
        return usuarioUnidadTecnicas;
    }

    public void setUsuarioUnidadTecnicas(List<UnidadTecnica> usuarioUnidadTecnicas) {
        this.usuarioUnidadTecnicas = usuarioUnidadTecnicas;
    }

    

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

    public void setUserInfo(UsuarioInfo userInfo) {
        this.userInfo = userInfo;
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

    public String getIdUnidadTecnica() {
        return idUnidadTecnica;
    }

    public void setIdUnidadTecnica(String idUnidadTecnica) {
        this.idUnidadTecnica = idUnidadTecnica;
    }

    
    public List<DataTipoIndicador> getIndicadoresACargar() {
        return indicadoresACargar;
    }

    public void setIndicadoresACargar(List<DataTipoIndicador> indicadoresACargar) {
        this.indicadoresACargar = indicadoresACargar;
    }

    public List<Boolean> getValoresHabilitados() {
        return valoresHabilitados;
    }

    public void setValoresHabilitados(List<Boolean> valoresHabilitados) {
        this.valoresHabilitados = valoresHabilitados;
    }

    // </editor-fold>
}
