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
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.PACDelegate;
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
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "pacReporteInsumos")
public class PACReporteInsumos implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    @Inject
    PACDelegate pacDelegate;

    private String nroPAC;
    private String nombrePAC;
    private List<HashMap> registros = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @PostConstruct
    public void init() {
        initFilter();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nroPAC = null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            if ( !TextUtils.isEmpty(nroPAC) && !TextUtils.isInteger(nroPAC)   ) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_NUMERO_PAC_INCORRECTO), null);
                FacesContext.getCurrentInstance().addMessage("formfiltro:nroPAC", msj);
                nroPAC = null;
            }
            
            Integer idPAC = NumberUtils.getIntegerONull(nroPAC);
            registros = pacDelegate.obtenerInsumosDelPAC(idPAC, nombrePAC);
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
     * Verifica si un texto es numerico
     * @param cadena
     * @return 
     */
    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public String getNroPAC() {
        return nroPAC;
    }

    public String getNombrePAC() {
        return nombrePAC;
    }

    public void setNombrePAC(String nombrePAC) {
        this.nombrePAC = nombrePAC;
    }

    
    public void setnroPAC(String nroPAC) {
        this.nroPAC = nroPAC;
    }

    public List<HashMap> getRegistros() {
        return registros;
    }

    public void setRegistros(List<HashMap> registros) {
        this.registros = registros;
    }

    // </editor-fold>
}
