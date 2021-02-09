package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.CuentasBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "cuentasMB")
public class CuentasMB implements Serializable{
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private Cuentas cuenta;
    private Cuentas cuentaFiltro;
    private Cuentas subCuenta;
    private Cuentas cuentaEliminar;
    
    private List<Cuentas> listaCuentasHijos;
    private List<Cuentas> listaCuentasHistorico;
    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private SofisComboG<AnioFiscal> comboAnioFiscalOrigen;
    private SofisComboG<AnioFiscal> comboAnioFiscalDestino;
    protected LazyDataModel lazyModel;
    private String filtroHabilitado;
    private boolean flagEliminar;
    
    @Inject
    private GeneralBean generalBean;
    @Inject
    private CuentasBean cuentasBean;
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private EntityManagementDelegate emd;
    
    
    
    
    @PostConstruct
    public void init() {
        String id = buscarIdentificador("id");
        String sub = buscarIdentificador("sub");
        cargarComboAnioFiscal();
        
        cleanObject();
        if (!TextUtils.isEmpty(id)) {
            asignarEdicion(Integer.parseInt(id));
            findListaSubCuentasByIdPadre(Integer.parseInt(id));
        }if (!TextUtils.isEmpty(sub)) {
            asignarEdicionSubCuenta(Integer.parseInt(sub));
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
     * Este método inicializa los objetos base del mantenimiento
     */
    public void cleanObject() {
        this.comboAnioFiscal.clearSelectedT();
        this.comboAnioFiscalOrigen.clearSelectedT();
        this.comboAnioFiscalDestino.clearSelectedT();
        this.cuenta = inicializarObjetos(null);
        this.subCuenta = inicializarObjetos(null);
        this.cuentaFiltro = inicializarObjetos(null);
        filterTable();
    }
    
    /**
     * Este método llena el objeto Combo para los aniosFiscales
     */
    public void cargarComboAnioFiscal(){
        try {
            List<AnioFiscal> lista = generalBean.getAniosFiscalesPlanificacion();
            this.comboAnioFiscal = new SofisComboG<>(lista, "nombre");
            this.comboAnioFiscal.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            this.comboAnioFiscalOrigen = new SofisComboG<>(lista, "nombre");
            this.comboAnioFiscalOrigen.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
            this.comboAnioFiscalDestino = new SofisComboG<>(lista, "nombre");
            this.comboAnioFiscalDestino.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    public void nuevaCopia() {
        this.comboAnioFiscalOrigen.clearSelectedT();
        this.comboAnioFiscalDestino.clearSelectedT();
    }
    
    /**
     * Este método inicializa un objeto de Cuentas
     * @param local
     * @return 
     */
    public Cuentas inicializarObjetos(Cuentas local){
        if(local == null){
            local = new Cuentas();
        }
        inicializarRelaciones(local);
        return local;
    }

    /**
     * Este metodo inicializa las relaciones de Cuentas
     * @param local 
     */
    public void inicializarRelaciones(Cuentas local){
        if(local.getCuentaPadre() == null)
            local.setCuentaPadre(new Cuentas());
    }
    

    
    
    
    
    /**
     * Metodo utilizado para buscar un registro de Cuentas, filtrado por ID
     * @param id
     */
    public void findCuentaById(Integer id) {
        try {
            setCuenta(cuentasBean.getCuentaById(id));
            inicializarRelaciones(getCuenta());
            this.comboAnioFiscal.setSelectedT(getCuenta().getAnioFiscal());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para buscar un registro de SubCuenta, filtrado por ID
     * @param id
     */
    public void findSubCuentaById(Integer id) {
        try {
            setSubCuenta(cuentasBean.getCuentaById(id));
            inicializarRelaciones(getSubCuenta());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo utilizado para buscar todos los registros de Cuentas que pertenezcan al mismo padre
     * @param id
     */
    public void findListaSubCuentasByIdPadre(Integer id) {
        try {
            setListaCuentasHijos(cuentasBean.getCuentasByIdPadre(id));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    
    
    
    
    /**
     * Este método busca un registro de áreas de inversión por su ID
     * y asigna el resultado a un contexto de edición
     * @param id 
     */
    public void asignarEdicion(Integer id) {
        findCuentaById(id);
    }
    
    /**
     * Este método busca un registro de áreas de inversión por su ID
     * y asigna el resultado a un contexto de edición
     * @param id 
     */
    public void asignarEdicionSubCuenta(Integer id) {
        findSubCuentaById(id);
    }
    
    
    
    
    
    /**
     * Metodo utilizado para crear y/o actualizar un registro de Cuentas
     * @return
     */
    public String guardarActualizar() {
        try {
            this.cuenta.setAnioFiscal(this.comboAnioFiscal.getSelectedT());
            verificarRelaciones();
            cuentasBean.crearActualizar(getCuenta());
            cleanObject();
            return "consultaCuentas.xhtml?faces-redirect=true";
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
     * Metodo utilizado para crear y/o actualizar un registro de Cuentas
     * @return
     */
    public String guardarActualizarSubCuenta() {
        try {
            getSubCuenta().setCuentaPadre(getCuenta());
            cuentasBean.crearActualizar(getSubCuenta());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("id", getCuenta().getId().toString());
            
            jSFUtils.agregarInfo("Registro guardado correctamente");
            return "crearEditarCuentas.xhtml?faces-redirect=true";
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
     * Metodo utilizado para eliminar un registro de Cuentas
     */
    public void eliminar() {
        try {
            if(!flagEliminar){
                cuentasBean.eliminar(cuentaEliminar.getId());
                findListaSubCuentasByIdPadre(getCuenta().getId());
                RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
                RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_NO_POSIBLE_ELIMINAR);
            }
            
            
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } /**finally {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        }**/
    }

    
    
    
    
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            MatchCriteriaTO criterio0 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "cuentaPadre.id", 1);
            criterios.add(criterio0);
            
            if (!TextUtils.isEmpty(getCuentaFiltro().getCodigo())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", getCuentaFiltro().getCodigo());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getCuentaFiltro().getNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", getCuentaFiltro().getNombre());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getCuentaFiltro().getDescripcion())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "descripcion", getCuentaFiltro().getDescripcion());
                criterios.add(criterio);
            }
            if (this.comboAnioFiscal != null && this.comboAnioFiscal.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "anioFiscal.id", this.comboAnioFiscal.getSelectedT().getId());
                criterios.add(criterio);
            }
            
 
            if (getFiltroHabilitado() != null && !getFiltroHabilitado().isEmpty()) {
                if (getFiltroHabilitado().equals("true")) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", true);
                    criterios.add(criterio);
                } else if(getFiltroHabilitado().equals("false")){
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", false);
                    criterios.add(criterio);
                }
            }

            CriteriaTO condicion = null;
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

            String[] propiedades = {"id", "codigo", "nombre", "descripcion", "habilitado", "orden", "anioFiscal.anio"};
            String className = Cuentas.class.getName();
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
        this.listaCuentasHistorico = emd.obtenerHistorico(Cuentas.class, id);
    }
    
    /**
     * Este método verifica que el contenido de los objetos que son relaciones de un área de Inversión
     */
    public void verificarRelaciones(){
        if(getCuenta().getCuentaPadre()!= null && (getCuenta().getCuentaPadre().getId() == null || getCuenta().getCuentaPadre().getId() == 0)){
            getCuenta().setCuentaPadre(null);
        }
    }
    
    /**
     * Dirige al sitio de Cuentas
     * @return
     */
    public String cerrar() {
        return "consultaCuentas.xhtml?faces-redirect=true";
    }
    
    /**
     * Dirige al sitio de Cuentas
     * @return
     */
    public String cerrarSubCuenta() {
        return "crearEditarCuentas.xhtml?faces-redirect=true";
    }
    
    /**
     * Este método verifica las relaciones que tiene el registro de cuentas
     * 
     * @param idCuenta
     */
    public void verificarRelacionCuentas(Integer idCuenta){
        try {
            this.cuentaEliminar = cuentasBean.getCuentaById(idCuenta);
            List<Cuentas> listaC =  cuentasBean.getCuentaRelacionComponente(idCuenta);
            if(listaC != null && !listaC.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CUENTA_RELACION_COMPONENTE);
                flagEliminar = true;
            }
            
            List<TransferenciasComponente> listaTC = cuentasBean.getCuentaTransferenciaComponente(idCuenta);
            if(listaTC != null && !listaTC.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CUENTA_RELACION_TRANSFERENCIA);
                flagEliminar = true;
            }
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('show');");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    
    
    public void crearCopiaUnidadesPresupuestales() {
        try {
            cuentasBean.crearCopiasUnidadesPresupuestariasPorAnio(comboAnioFiscalOrigen.getSelectedT(), comboAnioFiscalDestino.getSelectedT());
            RequestContext.getCurrentInstance().execute("$('#duplicarModal').modal('hide');");
            filterTable();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    

    public Cuentas getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuentas cuenta) {
        this.cuenta = cuenta;
    }

    public Cuentas getCuentaFiltro() {
        return cuentaFiltro;
    }

    public void setCuentaFiltro(Cuentas cuentaFiltro) {
        this.cuentaFiltro = cuentaFiltro;
    }

    public Cuentas getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(Cuentas subCuenta) {
        this.subCuenta = subCuenta;
    }

    public List<Cuentas> getListaCuentasHijos() {
        return listaCuentasHijos;
    }

    public void setListaCuentasHijos(List<Cuentas> listaCuentasPadre) {
        this.listaCuentasHijos = listaCuentasPadre;
    }

    public List<Cuentas> getListaCuentasHistorico() {
        return listaCuentasHistorico;
    }

    public void setListaCuentasHistorico(List<Cuentas> listaCuentasHistorico) {
        this.listaCuentasHistorico = listaCuentasHistorico;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getFiltroHabilitado() {
        return filtroHabilitado;
    }

    public void setFiltroHabilitado(String filtroHabilitado) {
        this.filtroHabilitado = filtroHabilitado;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public Cuentas getCuentaEliminar() {
        return cuentaEliminar;
    }

    public void setCuentaEliminar(Cuentas cuentaEliminar) {
        this.cuentaEliminar = cuentaEliminar;
    }

    public boolean isFlagEliminar() {
        return flagEliminar;
    }

    public void setFlagEliminar(boolean flagEliminar) {
        this.flagEliminar = flagEliminar;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscalOrigen() {
        return comboAnioFiscalOrigen;
    }

    public void setComboAnioFiscalOrigen(SofisComboG<AnioFiscal> comboAnioFiscalOrigen) {
        this.comboAnioFiscalOrigen = comboAnioFiscalOrigen;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscalDestino() {
        return comboAnioFiscalDestino;
    }

    public void setComboAnioFiscalDestino(SofisComboG<AnioFiscal> comboAnioFiscalDestino) {
        this.comboAnioFiscalDestino = comboAnioFiscalDestino;
    }

   

    
    
    
    
    
    
    
    
    
    
}
