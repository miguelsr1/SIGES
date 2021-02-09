/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.datatypes.DataActividad;
import gob.mined.siap2.business.datatypes.DataMetaIndicador;
import gob.mined.siap2.business.datatypes.DataTipoIndicador;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
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
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "valoresDeIndicadoresCarga")
public class CargaValoresDeIndicadores implements Serializable {

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
    @Inject
    protected EntityManagementDelegate emd;

    private String idAnioFiscal;
    private String idUnidadTecnica;

    private List<UnidadTecnica> usuarioUnidadTecnicas;
    private List<DataTipoIndicador> indicadoresACargar;
    private List<DataActividad> actividadesACargar;
    private DataActividad actividadEnEdicion;
    private List<Boolean> valoresHabilitados = new LinkedList<>();
    
    private DataMetaIndicador metaEditando;

    @PostConstruct
    public void init() {
        usuarioUnidadTecnicas = userInfo.getUTDeUsuarioConOperacion(ConstantesEstandares.Operaciones.CARGA_VALORES_INDICADORES);
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

            List<Integer> unidadesTecnicas = new LinkedList();
            if (TextUtils.isEmpty(idUnidadTecnica)){
                for (UnidadTecnica ut : usuarioUnidadTecnicas) {
                    unidadesTecnicas.add(ut.getId());
                }            
            }else{
                unidadesTecnicas.add(Integer.valueOf(idUnidadTecnica));
            }

            indicadoresACargar = valoresIndicadoresDelegate.getValoresDeIndicadores(unidadesTecnicas, Integer.valueOf(idAnioFiscal));
            actividadesACargar = valoresIndicadoresDelegate.getActividadesParaCarga(unidadesTecnicas, Integer.valueOf(idAnioFiscal));
            
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Recargra los valores habilitados
     */
    public void reloadValorIndicador() {
        valoresHabilitados = valoresIndicadoresDelegate. getPosicionesHabilitadasParaCarga(metaEditando.getMetaIndicador().getTipoSeguimiento(), metaEditando.getProyecto().getTipoProyecto(), metaEditando.getMetaIndicador().getAnioFiscal().getId()); 
    }

    /**
     * Habilita o deshabilita la carga de un valor de indicador
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
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        try {
            valoresIndicadoresDelegate.guardar(metaEditando.getMetaIndicador());            
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
     * Setea el estado de no finalizado a una actividad
     */
    public void initActividad(){
        if (actividadEnEdicion.getActividad().getEstado() == null){
            actividadEnEdicion.getActividad().setEstado(EstadoActividadPOA.NO_FINALIZADA);
        }
    }
    
    /**
     * Guarda los cambios de una actividad
     */
    public void guardarActividad() {
        try {
            valoresIndicadoresDelegate.cambiarEstadoActividad(actividadEnEdicion.getActividad().getId(),actividadEnEdicion.getActividad().getEstado());
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#editarActividadModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Verifica si una actividad está finalizada o no
     * @param data
     * @return 
     */
    public boolean estaFinalizada(DataActividad data){
        if (data == null || data.getActividad() == null || data.getActividad().getEstado() == null){
            return false;
        }
        return data.getActividad().getEstado() == EstadoActividadPOA.FINALIZADA;
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

    public DataActividad getActividadEnEdicion() {
        return actividadEnEdicion;
    }

    public void setActividadEnEdicion(DataActividad actividadEnEdicion) {
        this.actividadEnEdicion = actividadEnEdicion;
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
    
    
    

    public DataMetaIndicador getMetaEditando() {
        return metaEditando;
    }

    public void setMetaEditando(DataMetaIndicador metaEditando) {
        this.metaEditando = metaEditando;
    }

    public List<DataActividad> getActividadesACargar() {
        return actividadesACargar;
    }

    public void setActividadesACargar(List<DataActividad> actividadesACargar) {
        this.actividadesACargar = actividadesACargar;
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
