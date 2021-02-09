/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.cierre;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.impl.CierreDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UtilsMB;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;


/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza el cierre de inversión
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cierreInversion")
public class CierreInversion implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    JSFUtils jSFUtils;
    @Inject
    UtilsMB utilsMB;
    @Inject
    TextMB textMB;
    @Inject
    CierreDelegate cierreDelegate;

    private String idAnioFiscal;

    @PostConstruct
    public void init() {
        idAnioFiscal = null;
    }

    /**
     * Este método se utiliza para cerrar el año fiscal
     */
    public void cerrarAnioFiscal() {
        try {
            if (TextUtils.isEmpty(idAnioFiscal)) {
                RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            Integer idAnio = Integer.parseInt(idAnioFiscal);
            cierreDelegate.cerrarAnioFiscal(idAnio);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(String idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
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

    public TextMB getTextMB() {
        return textMB;
    }

    public void setTextMB(TextMB textMB) {
        this.textMB = textMB;
    }

    // </editor-fold>
}
