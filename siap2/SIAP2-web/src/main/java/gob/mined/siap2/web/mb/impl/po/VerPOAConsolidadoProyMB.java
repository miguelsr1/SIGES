/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que se utiliza para ver el consolidado de un POA de proyecto.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "verPOAConsolidadoProyMB")
public class VerPOAConsolidadoProyMB extends POAConsolidadoProyMB implements Serializable {
        
    private Boolean actividadesPendientesParaCierreAnualPOA;
    private Boolean insumosPendientesParaCierreAnualPOA;
    private Boolean anioSeleccionadoFinalizado;

    String restriccionXTipoProyecto;
    EstadoActividadPOA filtroEstadoActividd = null;
    
    @Override
    public void initProyecto() {
        try {
            listPoa = null;
            if (!TextUtils.isEmpty(idAnioFiscal)) {
                listPoa = pOAProyectoDelegate.getPOAsTrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), EstadoPOA.TERMINO_CONSOLIDACION);
                utPendientes = pOAProyectoDelegate.getUTPendientesParaConsolidar(objeto.getId(), Integer.valueOf(idAnioFiscal));
                completoParaConsolidado = utPendientes.isEmpty() && (listPoa != null && !listPoa.isEmpty());
            }            
            if (TextUtils.isEmpty(restriccionXTipoProyecto)){
                restriccionXTipoProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("restriccionXTipoProyecto");
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    @Override
    public void guardar() {
    }

    
    /**
     * Este método verifica si una actividad cumple el filtro por el que se esta buscando
     * @param actividad
     * @return 
     */
    public boolean cumpleFiltroActividad(POActividadBase actividad){
        if (filtroEstadoActividd == null){
            return true;
        }
        return actividad.getEstado() == filtroEstadoActividd;
    }
    
    
    /**
     * Este método se utiliza para volver a la página anterior
     * @return 
     */
    public String volver() {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .put("restriccionXTipoProyecto", restriccionXTipoProyecto);
            return "consultaProyecto.xhtml?faces-redirect=true&restriccionXTipoProyecto=" + restriccionXTipoProyecto;
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
     * Este método se utiliza para inicializar los datos que se muestran al realizar el cierre anual
     */
    public void inicializarCierreAnualPOA() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)){
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ANIO_FISCAL);
                throw b;
            }
            if (listPoa == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_CERRAR_ANIO_POA_PARA_ANIO_FISCAL_SELECCIONADO);
                throw b;
            }
            Iterator<POAProyecto> itPOAs = listPoa.iterator();
            anioSeleccionadoFinalizado = true;
            actividadesPendientesParaCierreAnualPOA = true;
            insumosPendientesParaCierreAnualPOA = true;
            Boolean salir = false;
            while(itPOAs.hasNext() && !salir){
                POAProyecto poaProyecto = itPOAs.next();
                if(anioSeleccionadoFinalizado){
                    anioSeleccionadoFinalizado = versionDelegate.anioSeleccionadoEstaFinalizado(Integer.valueOf(idAnioFiscal));
                }
                if(actividadesPendientesParaCierreAnualPOA){
                    actividadesPendientesParaCierreAnualPOA = pOAProyectoDelegate.existenActividadesPendientesEnPOA(poaProyecto.getId());
                }
                if(insumosPendientesParaCierreAnualPOA){
                    insumosPendientesParaCierreAnualPOA = pOAProyectoDelegate.existenInsumosPendientesEnPOA(poaProyecto.getId());
                }
                if(!anioSeleccionadoFinalizado && !actividadesPendientesParaCierreAnualPOA &&!insumosPendientesParaCierreAnualPOA){
                    salir=true;
                }
            }
            RequestContext.getCurrentInstance().execute("$('#confirmarCierreAnualPOA').modal('show');");
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
     * Este método se utiliza para realizar el cierre anual del POA de proyecto
     */
    public void confirmarCierreAnualPOA() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ANIO_FISCAL);
                throw b;
            }
            pOAProyectoDelegate.confirmarCierreAnual(objeto.getId(), Integer.valueOf(idAnioFiscal));
            initProyecto();
            RequestContext.getCurrentInstance().execute("$('#confirmarCierreAnualPOA').modal('hide');");
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
     * Este método retorna si se muestra el botón del cierre anual 
     * @return 
     */
    public Boolean mostrarBotonCierreAnual(){
        if(listPoa!=null){
            for(POAProyecto poaProy : listPoa){
                if(poaProy.getCierreAnual()==null || !poaProy.getCierreAnual()){
                    return true;
                }
            }
        }
        return false;
    }

    public EstadoActividadPOA getFiltroEstadoActividd() {
        return filtroEstadoActividd;
    }

    public void setFiltroEstadoActividd(EstadoActividadPOA filtroEstadoActividd) {
        this.filtroEstadoActividd = filtroEstadoActividd;
    }

    public Boolean getActividadesPendientesParaCierreAnualPOA() {
        return actividadesPendientesParaCierreAnualPOA;
    }

    public void setActividadesPendientesParaCierreAnualPOA(Boolean actividadesPendientesParaCierreAnualPOA) {
        this.actividadesPendientesParaCierreAnualPOA = actividadesPendientesParaCierreAnualPOA;
    }

    public Boolean getInsumosPendientesParaCierreAnualPOA() {
        return insumosPendientesParaCierreAnualPOA;
    }

    public void setInsumosPendientesParaCierreAnualPOA(Boolean insumosPendientesParaCierreAnualPOA) {
        this.insumosPendientesParaCierreAnualPOA = insumosPendientesParaCierreAnualPOA;
    }

    public Boolean getAnioSeleccionadoFinalizado() {
        return anioSeleccionadoFinalizado;
    }

    public void setAnioSeleccionadoFinalizado(Boolean anioSeleccionadoFinalizado) {
        this.anioSeleccionadoFinalizado = anioSeleccionadoFinalizado;
    }
    
    
    
    
}
