/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.impl.ValoresDeIndicadoresDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que se utiliza para el reporte de proyecto administrativo.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaReporteProyectoAdministrativo")
public class POAReporteProyectoAdministrativo implements Serializable {//extends POProyectoConLineasAbstract implements Serializable {


    @Inject
    ProyectoDelegate proyectoDelegate;
    @Inject
    POAProyectoDelegate pOAProyectoDelegate;
    @Inject
    ValoresDeIndicadoresDelegate indicadoresDelegate;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    protected String idProyecto;
    protected String idAnioFiscal;

    protected Proyecto objeto;
    
    private List<HashMap> registros = new ArrayList<>();

    @PostConstruct
    public void init() {
        idProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(idProyecto)) {
            objeto = proyectoDelegate.getProyecto(Integer.valueOf(idProyecto));
        }
    }

   
    /**
     * Este método retorna los posbie
     * @return 
     */
    public Map<String, String> getPosiblesAniosPOA() {
        Map<String, String> m = new LinkedHashMap<>();
        if (objeto != null) {
            for (AnioFiscal anio : pOAProyectoDelegate.getTodosAniosPOA(objeto.getId())){
                  m.put(String.valueOf(anio.getAnio()), String.valueOf(anio.getId()));
            }
        }
        return m;
    }
    

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        idAnioFiscal = null;
    }


    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            if (idAnioFiscal == null || idAnioFiscal.trim().length() == 0) {
                FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, textMB.obtenerTexto(ConstantesErrores.ERR_CAMPO_REQUERIDO), null);
                FacesContext.getCurrentInstance().addMessage("formfiltro:anioFiscal", msj);
                return;
            }
            registros = indicadoresDelegate.obtenerInfoReportePoaProyectoAdministrativo(new Integer(idProyecto),new Integer(idAnioFiscal)); 
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
    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public List<HashMap> getRegistros() {
        return registros;
    }

    public void setRegistros(List<HashMap> registros) {
        this.registros = registros;
    }
    
    
    public Proyecto getObjeto() {
        return objeto;
    }

    public void setObjeto(Proyecto objeto) {
        this.objeto = objeto;
    }


    // </editor-fold>


}
