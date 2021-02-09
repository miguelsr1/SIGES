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
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
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

/**
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "lineasMetasIndicadoresConsulta")
public class LineasMetasIndicadoresConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    
    @Inject
    ValoresDeIndicadoresDelegate valoresDeIndicadores;
    
    private String lineaEstrategicaId;
    private String anioFiscalId;
    
    private List<HashMap> registros = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);


    @PostConstruct
    public void init() {
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        lineaEstrategicaId = null;
        anioFiscalId = null;
    }


    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            boolean salir = false;
            if (lineaEstrategicaId == null || lineaEstrategicaId.trim().length() == 0) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_CAMPO_REQUERIDO), null);
                FacesContext.getCurrentInstance().addMessage("formfiltro:lineaId", msj);
                salir = true;
            }
            if (anioFiscalId == null || anioFiscalId.trim().length() == 0) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_CAMPO_REQUERIDO), null);
                FacesContext.getCurrentInstance().addMessage("formfiltro:anioId", msj);
                salir = true;
            }
            if (salir){
                return;
            }
            
            registros = valoresDeIndicadores.obtenerLineasMetasIndicadores(new Integer(lineaEstrategicaId), new Integer(anioFiscalId));
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

    
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   

    public String getLineaEstrategicaId() {
        return lineaEstrategicaId;
    }

    public void setLineaEstrategicaId(String lineaEstrategicaId) {
        this.lineaEstrategicaId = lineaEstrategicaId;
    }

    public String getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(String anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public List<HashMap> getRegistros() {
        return registros;
    }

    public void setRegistros(List<HashMap> registros) {
        this.registros = registros;
    }


    // </editor-fold>
}
