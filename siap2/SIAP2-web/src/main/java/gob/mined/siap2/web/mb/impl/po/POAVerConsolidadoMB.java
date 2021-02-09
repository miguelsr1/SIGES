/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que se utiliza para ver el consolidado del los POA con actividades
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "verPOAConsolidadoMB")
public class POAVerConsolidadoMB extends POAConsolidadoMB implements Serializable {

    EstadoActividadPOA filtroEstadoActividd = null;

    private Boolean actividadesPendientesParaCierreAnualPOA;
    private Boolean insumosPendientesParaCierreAnualPOA;
    private Boolean anioSeleccionadoFinalizado;

    @Override
    public void initConMontoPorAnio() {
        try {
            if (!TextUtils.isEmpty(idAnioFiscal)) {
                listPoa = pOAConActividadesDelegate.getPOAsTrabajo(objeto.getId(), Integer.valueOf(idAnioFiscal), EstadoPOA.TERMINO_CONSOLIDACION);
                completoParaConsolidado = pOAConActividadesDelegate.isCompletoParaConsolidado(objeto.getId(), Integer.valueOf(idAnioFiscal)) && (listPoa != null && !listPoa.isEmpty());

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

    /**
     * Este método se utiliza para volver a la página anterior
     * 
     * @return 
     */
    public String volver() {
        try {
            if (tmpa.equals(TipoMontoPorAnio.ACCION_CENTRAL)) {
                return "consultaAccionCentral.xhtml?faces-redirect=true";
            } else if (tmpa.equals(TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE)) {
                return "consultaAsignacionesNoProgramables.xhtml?faces-redirect=true";
            }
            return null;
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
     * Este método se utiliza para verificar si la actividad cumple con el filtro aplicado
     * @param actividad
     * @return 
     */
    public boolean cumpleFiltroActividad(POActividadBase actividad) {
        if (filtroEstadoActividd == null) {
            return true;
        }
        return actividad.getEstado() == filtroEstadoActividd;
    }

    /**
     * Este método se utiliza para realizar la inicialización de los datos de cierre que se van a mostrar a modo informativo
     */
    public void inicializarCierreAnualPOA() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ANIO_FISCAL);
                throw b;
            }
            if (listPoa == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_CERRAR_ANIO_POA_PARA_ANIO_FISCAL_SELECCIONADO);
                throw b;
            }
            Iterator<POAConActividades> itPOAs = listPoa.iterator();
            anioSeleccionadoFinalizado = true;
            actividadesPendientesParaCierreAnualPOA = true;
            insumosPendientesParaCierreAnualPOA = true;
            Boolean salir = false;
            while(itPOAs.hasNext() && !salir){
                POAConActividades poaProyecto = itPOAs.next();
                if(anioSeleccionadoFinalizado){
                    anioSeleccionadoFinalizado = versionDelegate.anioSeleccionadoEstaFinalizado(Integer.valueOf(idAnioFiscal));
                }
                if(actividadesPendientesParaCierreAnualPOA){
                    actividadesPendientesParaCierreAnualPOA = pOAConActividadesDelegate.existenActividadesPendientesEnPOA(poaProyecto.getId());
                }
                if(insumosPendientesParaCierreAnualPOA){
                    insumosPendientesParaCierreAnualPOA = pOAConActividadesDelegate.existenInsumosPendientesEnPOA(poaProyecto.getId());
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
     * Este método es el encargado de realizar el cierre anual
     */
    public void confirmarCierreAnualPOA() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_ANIO_FISCAL);
                throw b;
            }
            pOAConActividadesDelegate.confirmarCierreAnual(objeto.getId(), Integer.valueOf(idAnioFiscal));
            initConMontoPorAnio();
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
     * Este método retorna si ya se realizó el cierre anual
     * @return 
     */
    public Boolean mostrarBotonCierreAnual(){
        if(listPoa!=null){
            for(POAConActividades poaConAct : listPoa){
                if(poaConAct.getCierreAnual()==null || !poaConAct.getCierreAnual()){
                    return true;
                }
            }
        }
        return false;
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
    
    public EstadoActividadPOA getFiltroEstadoActividd() {
        return filtroEstadoActividd;
    }

    public void setFiltroEstadoActividd(EstadoActividadPOA filtroEstadoActividd) {
        this.filtroEstadoActividd = filtroEstadoActividd;
    }


}
