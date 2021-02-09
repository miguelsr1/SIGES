/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza el reporte de seguimiento de proyectos administrativos.
 * 
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "reporteSeguimientoProyectoAministrativo")
public class ReporteSeguimientoProyectoAministrativo extends SelectOneUTBean implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;

    @Inject
    ValoresDeIndicadoresDelegate valoresDeIndicadores;
    @Inject
    private ReporteDelegate reporteDelegate;
    
    private String ut;
    private String anioFiscalId;
    private String tipoSeguimiento;//M->Mensual, T->Trimestral
    
    private List<HashMap> registros = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);


    @PostConstruct
    public void init() {
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        anioFiscalId = null;
        tipoSeguimiento = null;
        unidadTecnicaSelecionada = null;
    }


    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            boolean salir = false;
            if (unidadTecnicaSelecionada == null || unidadTecnicaSelecionada.getId() == null) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_CAMPO_REQUERIDO), null);
                FacesContext.getCurrentInstance().addMessage("utId", msj);
                salir = true;
            }
            if (anioFiscalId == null || anioFiscalId.trim().length() == 0) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_CAMPO_REQUERIDO), null);
                FacesContext.getCurrentInstance().addMessage("anioId", msj);
                salir = true;
            }
            if (tipoSeguimiento == null || tipoSeguimiento.trim().length() == 0) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_CAMPO_REQUERIDO), null);
                FacesContext.getCurrentInstance().addMessage("tipoSeguimiento", msj);
                salir = true;
            }
            if (salir){
                return;
            }
            
            registros = valoresDeIndicadores.obtenerInfoReporteSeguimientoProyectoAdministrativo(unidadTecnicaSelecionada.getId(), new Integer(anioFiscalId),tipoSeguimiento);
            
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
        registros.clear();
    }
    
    /**
     * Genera el reporte de seguimiento de proyecto administrativo
     */
    public void generarPDF() {
        try {

            if (tipoSeguimiento.equals("M")) {
                byte[] bytespdf = reporteDelegate.generarSeguimientoProyectoAdministrativoMensual(registros);
                ArchivoUtils.downloadPdfFromBytes(bytespdf, "SeguimientoProyectoAdministrativo.pdf");
            }else{
                byte[] bytespdf = reporteDelegate.generarSeguimientoProyectoAdministrativoTrimestral(registros);
                ArchivoUtils.downloadPdfFromBytes(bytespdf, "SeguimientoProyectoAdministrativo.pdf");
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("formCreatePlantilla");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("formCreatePlantilla");
        }
    }

    
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   

    public String getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(String anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }
    
    


    public String getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(String tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }
    

    public List<HashMap> getRegistros() {
        return registros;
    }

    public void setRegistros(List<HashMap> registros) {
        this.registros = registros;
    }


    // </editor-fold>
}
