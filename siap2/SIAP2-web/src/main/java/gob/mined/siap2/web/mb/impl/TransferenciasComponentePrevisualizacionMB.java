/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.DireccionDepartamentalBean;
import gob.mined.siap2.business.ejbs.impl.TopePresupuestalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciaDireccionDepartamentalBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasACedBean;
import gob.mined.siap2.business.ejbs.impl.TransferenciasComponenteBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.EstadoTransferenciaACed;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;

@ViewScoped
@Named(value = "transferenciasComponentePrevisualizacionMB")
public class TransferenciasComponentePrevisualizacionMB implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    private TransferenciasComponente transferenciasComponente;
    private List<TransferenciasACed> listaSedes;
    
    
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private TransferenciasComponenteBean transferenciasComponenteBean;
    @Inject
    private DireccionDepartamentalBean direccionDepartamentalBean;
    @Inject
    private TransferenciasACedBean transferenciasACedBean;
    @Inject
    private TransferenciaDireccionDepartamentalBean transferenciaDireccionDepartamentalBean;
    @Inject
    private TopePresupuestalBean presupuestalBean;
    
    
    
    
    @PostConstruct
    public void init(){
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            findTransferenciaComponenteById(Integer.parseInt(id));
            generarTransferenciasCentrosEducativos();
        }
    }
    
    /**
     * Metodo utilizado para buscar un registro de TransferenciaComponente, filtrado por ID
     * @param id
     */
    public void findTransferenciaComponenteById(Integer id) {
        try {
            this.transferenciasComponente = transferenciasComponenteBean.getTransferenciaComponenteById(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
            if(valor == null || valor.isEmpty()){
                Object val = FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parametro);
                return valor != null ? (String) val : null;
            }
            return valor;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Metodo utilizado para obtener la cantidad de registros de transferencias a sedes
     * @return 
     */
    public int resultadosLista(){
        return (listaSedes != null && !listaSedes.isEmpty()) ? listaSedes.size() : 0;
    }
    
    /**
     * Dirige al sitio de TransferenciaComponente
     *
     * @return
     */
    public String cerrar() {
        return "consultaTransferenciaComponente.xhtml?faces-redirect=true";
    }
    
    
    
    
    
    
    //******** GENERAR TRANSFERENCIAS A CENTROS EDUCATIVOS ********
    
    /**
     * Este método es utilizado para generar registros de TransferenciaACed y TransferenciaDireccionDepartamental segun la actualización de estado
     * de un registro de TransferenciaComponente
     */
    public void generarTransferenciasCentrosEducativos(){
        try {
            BigDecimal montoTotal = BigDecimal.ZERO;
            
            List<TopePresupuestal> listaTopes = buscarListaTopes();
            if(listaTopes == null || listaTopes.isEmpty()){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_TECHOS_NO_ENCONTRADOS);
                return;
            }
            
            //Se clasifican los topes por TDD
            HashMap<TransferenciaDireccionDepartamental, List<TopePresupuestal>> topesPorDepartamento = new HashMap<TransferenciaDireccionDepartamental, List<TopePresupuestal>>();
            for(TopePresupuestal tope : listaTopes){
                DireccionDepartamental ddp = direccionDepartamentalBean.getDireccionDepartamentalByIdDepartamento(tope.getSede().getDireccion().getDepartamento().getPk());
                TransferenciaDireccionDepartamental tdd = transferenciaDireccionDepartamentalBean.getTransferenciasDirDepaByTransfCompYDirDepa(getTransferenciasComponente().getId(), ddp.getPk());
                if(tdd == null){
                    tdd = nuevoTDD(ddp);
                }
                if(topesPorDepartamento.containsKey(tdd)){
                    topesPorDepartamento.get(tdd).add(tope);
                }else{
                    topesPorDepartamento.put(tdd, new ArrayList<TopePresupuestal>());
                    topesPorDepartamento.get(tdd).add(tope);
                }
            }
            
            HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD = new HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>>();
            for(Map.Entry<TransferenciaDireccionDepartamental, List<TopePresupuestal>> item : topesPorDepartamento.entrySet()){
                TransferenciasACed aced = null;
                for(TopePresupuestal tope: item.getValue()){
                    aced = transferenciasACedBean.getTransferenciaACedByComponenteSedeTDD(getTransferenciasComponente().getId(), tope.getSede().getId(), item.getKey().getPk());
                    if(aced == null){
                        aced = crearNuevaTransferenciaACed(tope);
                    }
                    montoTotal = montoTotal.add(aced.getMontoAutorizado());
                    if(sedesPorTDD.containsKey(item.getKey())){
                        sedesPorTDD.get(item.getKey()).add(aced);
                    }else{
                        sedesPorTDD.put(item.getKey(), new ArrayList<TransferenciasACed>());
                        sedesPorTDD.get(item.getKey()).add(aced);
                    }
                }
                item.getKey().setMontoAutorizado(montoTotal);
            }
            obtenerPrevisualizacion(sedesPorTDD);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para crear un nuevo registro de TransferenciaDireccionDepartamental
     * 
     * @param ddp
     * @return 
     */
    public TransferenciaDireccionDepartamental nuevoTDD(DireccionDepartamental ddp){
        TransferenciaDireccionDepartamental tdd = new TransferenciaDireccionDepartamental();
        tdd.setDireccionDep(ddp);
        tdd.setTransferenciasComponente(getTransferenciasComponente());
        tdd.setEstado(EstadoTransferenciaACed.TRANSFERIDO);
        return tdd;
    }
    
    /**
     * Metodo utilizado para buscar una lista de TechoPresupuestal filtrada
     * @return 
     */
    public List<TopePresupuestal> buscarListaTopes(){
        try {
            FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();
            filtro.setComponentePresupuestoEscolarId(getTransferenciasComponente().getSubcomponente().getId());
            filtro.setCuentaId(getTransferenciasComponente().getLineaPresupuestaria().getId());
            filtro.setEstado(EstadoTopePresupuestal.APROBACION);
            filtro.setIncluirCampos(new String[]{"sede.id","sede.nombre", "sede.codigo", "montoAprobado",
                "sede.direccion.departamento.pk", "sede.direccion.departamento.nombre"});
            return presupuestalBean.getTopesPresupuestalesByFiltro(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Método utilizado para obtener el porcentaje del monto aprobado de un topePresupuestal, según la transferenciaComponente
     * 
     * @param tope
     * @return 
     */
    public BigDecimal obtenerPorcentajeSede(TopePresupuestal tope){
        BigDecimal monto = null;
        try {
            if(tope.getMontoAprobado() != null && tope.getMontoAprobado().compareTo(BigDecimal.ZERO) > 0){
                if(getTransferenciasComponente().getPorcentaje()!= null && getTransferenciasComponente().getPorcentaje().compareTo(BigDecimal.ZERO) > 0){
                    monto = tope.getMontoAprobado().multiply(getTransferenciasComponente().getPorcentaje().divide(new BigDecimal(100)));
                }else{
                    jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_PORCENTAJE_INGRESADO_NO_VALIDO);
                }
            }else{
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_MONTO_APROBADO_NO_VALIDO);
            }
            if(tope.getSede() == null){
                jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CENTRO_EDUCATIVO_NO_ENCONTRADO);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return monto;
    }
    
    /**
     * Método utilizado para crear un nuevo objeto de TransferenciaACed
     * 
     * @param tope
     * @return 
     */
    public TransferenciasACed crearNuevaTransferenciaACed(TopePresupuestal tope) {
        TransferenciasACed aCed = new TransferenciasACed();
        aCed.setCed(tope.getSede());
        aCed.setEstado(EstadoTransferenciaACed.AUTORIZADO);
        aCed.setTransferencia(getTransferenciasComponente());
        aCed.setMontoAutorizado(obtenerPorcentajeSede(tope));
        return aCed;
    }

    /**
     * Metodo utilizado mover los datos de previsualizaciones a una lista, para facilitar su lectura desde la vista
     * @param sedesPorTDD
     */
    public void obtenerPrevisualizacion(HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD) {
        listaSedes = new ArrayList<TransferenciasACed>();
        for(Map.Entry<TransferenciaDireccionDepartamental, List<TransferenciasACed>> item : sedesPorTDD.entrySet()){
            listaSedes.addAll(item.getValue());
        }
    }
    
    
    
    
    
    
    
 
    public TransferenciasComponente getTransferenciasComponente() {
        return transferenciasComponente;
    }

    public void setTransferenciasComponente(TransferenciasComponente transferenciasComponente) {
        this.transferenciasComponente = transferenciasComponente;
    }

    public List<TransferenciasACed> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<TransferenciasACed> listaSedes) {
        this.listaSedes = listaSedes;
    }

}
