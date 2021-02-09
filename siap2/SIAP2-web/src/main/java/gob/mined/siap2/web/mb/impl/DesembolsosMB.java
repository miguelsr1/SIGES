package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.DesembolsosBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasComponenteBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siges.entities.data.impl.SgDesembolso;
import gob.mined.siap2.entities.data.impl.DesembolsoTransferenciaComponente;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.enums.EstadoDesembolso;
import gob.mined.siap2.entities.enums.EstadoDesembolsoTransferenciaComponente;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "desembolsosMB")
public class DesembolsosMB implements Serializable{
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private DesembolsoTransferenciaComponente desembolso;
    private TransferenciasComponente transferenciasComponente;
    private List<DesembolsoTransferenciaComponente> listaDesembolsoHistorico;
    
    private LazyDataModel lazyModel;
    private String idTransferenciaComponente;
    
    
    private SofisComboG<TransferenciasComponente> comboTransferenciasComponente = new SofisComboG<TransferenciasComponente>();
    private SofisComboG<EstadoDesembolsoTransferenciaComponente> comboEstado = new SofisComboG<EstadoDesembolsoTransferenciaComponente>();
    
    @Inject
    private TopePresupuestalBean topePresupuestalBean;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private DesembolsosBean desembolsosBean;
    @Inject
    private TransferenciasComponenteBean transferenciaBean;
    
    
    
    
    
    
    @PostConstruct
    public void init(){
        cleanObjects();
        cargarEstados();
        
        idTransferenciaComponente = buscarIdentificador("id");
        if (!TextUtils.isEmpty(idTransferenciaComponente)) {
            this.transferenciasComponente = transferenciaBean.getTransferenciaComponenteById(Integer.parseInt(idTransferenciaComponente));
        }
        filterTable();
    }
    
    
    /**
     * Este método limpia los objetos de filtrado y creacion, y reinicia la tabla de resultados
     */
    public void cleanObjects(){
        this.desembolso = new DesembolsoTransferenciaComponente();
        filterTable();
        comboEstado.clearSelectedT();
    }
    
   /**
     * Metodo utilizado para buscar un registro de TransferenciasComponente, filtrado por ID
     * @param id
     */
    public void cargarTransferenciaComponente(Integer id) {
        try {
            List<TransferenciasComponente> lista = new ArrayList<TransferenciasComponente>();
            this.transferenciasComponente = transferenciaBean.getTransferenciaComponenteById(id);
            lista.add(transferenciasComponente);
            comboTransferenciasComponente = new SofisComboG(lista, "id");
            comboTransferenciasComponente.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para cargar un listado de estados de Desembolso
     */
    public void cargarEstados() {
        try {
            comboEstado = new SofisComboG(Arrays.asList(EstadoDesembolsoTransferenciaComponente.values()) , "label");
            comboEstado.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    
   
    /**
     * Este método busca la llave de los parametros enviados desde la vista y obtiene su valor
     * @param parametro
     * @return 
     */
    public String buscarIdentificador(String parametro){
        try {
            String valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parametro);
            if(valor == null){
                valor = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parametro);
            }
            return valor;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
      /**
     * Este método busca un registro de desembolso, filtrado por ID
     * @param id 
     */
    public void findDesembolso(Integer id){
        try {
            cleanObjects();
            setDesembolso(desembolsosBean.getDesembolsoById(id));
            if(getDesembolso() != null){
                comboEstado.setSelectedT(getDesembolso().getEstado());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    
    
    
    /**
     * Metodo utilizado para guardar y/o actualizar un registro de Desembolso
     */
    public void guardarActualizar() {
        try {
            getDesembolso().setEstado(this.comboEstado.getSelectedT());
            getDesembolso().setTransferenciasComponente(this.transferenciasComponente);
            if(verificarLimiteDesembolsos()){
                desembolsosBean.crearActualizarMovimiento(getDesembolso());
                filterTable();
                RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
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
    }
    
    /**
     * Metodo utilizado para verificar si la sumatoria de porcentajes de un desembolso no supera el 100%
     * @return 
     */
    public boolean verificarLimiteDesembolsos(){
        try {
            List<DesembolsoTransferenciaComponente> lista = desembolsosBean.getAllDesembolsByTransferenciaComponente(this.transferenciasComponente.getId());
            BigDecimal bd = BigDecimal.ZERO;
            for(DesembolsoTransferenciaComponente des : lista){
                if(des.getPorcentaje() != null){
                    bd = bd.add(des.getPorcentaje());
                }
            }
            bd = bd.add(getDesembolso().getPorcentaje());
            if(bd.compareTo(new BigDecimal(100))  >= 0){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_LIMITE_PORCENTAJES_ALCANZADO);
                return false;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$(window).scrollTop(0);");
        }
        return true;
    }

    /**
     * Metodo utilizado para eliminar un registro de Desembolso
     * @param id ID del registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            desembolsosBean.eliminar(id);
            filterTable();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        }
    }
    
    
    
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
  
            if (this.idTransferenciaComponente != null && !this.idTransferenciaComponente.isEmpty()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "transferenciasComponente.id", Integer.parseInt(idTransferenciaComponente));
                criterios.add(criterio);
            }
            
            CriteriaTO condicion;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "transferenciasComponente.id", "porcentaje", "estado", "fechaDesembolso"};
            String className = DesembolsoTransferenciaComponente.class.getName();
            String[] orderBy = {"id"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método carga el listado historico del registro seleccionado
     * @param id 
     */
    public void cargarHistorico(Integer id) {
        this.listaDesembolsoHistorico = emd.obtenerHistorico(DesembolsoTransferenciaComponente.class, id);
    }

    
    /**
     * Dirige al sitio de TransferenciasComponente
     * @return
     */
    public String cerrar() {
        return "consultaTransferenciaComponente.xhtml?faces-redirect=true";
    }
    
    
    //******* GENERAR DESEMBOLSO POR CENTRO *******
    
    /**
     * Método utilizado para generar registros de desembolsos por cada techoPresupuestal
     */
    public void generarDesembolsoPorCentro(){
        try {
            List<TopePresupuestal> lista = topePresupuestalBean.generarTransferenciasCentroEducativo(
                    getTransferenciasComponente().getComponente().getId(),
                    getTransferenciasComponente().getSubcomponente().getId(),
                    getTransferenciasComponente().getUnidadPresupuestaria().getId(),
                    getTransferenciasComponente().getLineaPresupuestaria().getId());
            
            if(lista == null || lista.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_DESEMBOLSO_SIN_TECHOS);
                return;
            }
            
            SgDesembolso des;
            List<SgDesembolso> listaDesembolso = new ArrayList<SgDesembolso>();
            for(TopePresupuestal tope : lista){
                des = new SgDesembolso();
                des.setEstado(EstadoDesembolso.P);
                des.setMonto(tope.getMonto());
                des.setPorcentaje(getTransferenciasComponente().getPorcentaje());
                listaDesembolso.add(des);
            }
            desembolsosBean.generarDesembolsoPorCentro(listaDesembolso);
            jSFUtils.mostrarMensaje("Desembolsos generados correctamente", FacesMessage.SEVERITY_INFO);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    

    
    
    
    
    
    
    
  
    
    public DesembolsoTransferenciaComponente getDesembolso() {
        return desembolso;
    }

    public void setDesembolso(DesembolsoTransferenciaComponente desembolso) {
        this.desembolso = desembolso;
    }

    public List<DesembolsoTransferenciaComponente> getListaDesembolsoHistorico() {
        return listaDesembolsoHistorico;
    }

    public void setListaDesembolsoHistorico(List<DesembolsoTransferenciaComponente> listaDesembolsoHistorico) {
        this.listaDesembolsoHistorico = listaDesembolsoHistorico;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public SofisComboG<TransferenciasComponente> getComboTransferenciasComponente() {
        return comboTransferenciasComponente;
    }

    public void setComboTransferenciasComponente(SofisComboG<TransferenciasComponente> comboTransferenciasComponente) {
        this.comboTransferenciasComponente = comboTransferenciasComponente;
    }

    public SofisComboG<EstadoDesembolsoTransferenciaComponente> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EstadoDesembolsoTransferenciaComponente> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public TransferenciasComponente getTransferenciasComponente() {
        return transferenciasComponente;
    }

    public void setTransferenciasComponente(TransferenciasComponente transferenciasComponente) {
        this.transferenciasComponente = transferenciasComponente;
    }

    public String getIdTransferenciaComponente() {
        return idTransferenciaComponente;
    }

    public void setIdTransferenciaComponente(String idTransferenciaComponente) {
        this.idTransferenciaComponente = idTransferenciaComponente;
    }
    
    
}
