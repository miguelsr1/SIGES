/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.web.delegates.impl.GanttDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "cronogramaDelProceso")
public class CronogramaDelProceso implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private ProcesoAdquisicionDelegate delegate;
    @Inject
    private GanttDelegate ganttDelegate;
    @Inject
    private TextMB textMB;
    
    @Inject
    private ProcesoAdqMain mainBean;

    private String ganttJson;

    private Date fechaInicioGantt;
    private String selectedMetodoId;

    @PostConstruct
    public void init() {
    }
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public void setGanttJson(String ganttJson) {
        this.ganttJson = ganttJson;
    }

    public Date getFechaInicioGantt() {
        return fechaInicioGantt;
    }

    public void setFechaInicioGantt(Date fechaInicioGantt) {
        this.fechaInicioGantt = fechaInicioGantt;
    }


    public String getGanttJson() {
        return ganttJson;
    }

    public ProcesoAdquisicionDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ProcesoAdquisicionDelegate delegate) {
        this.delegate = delegate;
    }

    public GanttDelegate getGanttDelegate() {
        return ganttDelegate;
    }

    public void setGanttDelegate(GanttDelegate ganttDelegate) {
        this.ganttDelegate = ganttDelegate;
    }

    public String getSelectedMetodoId() {
        return selectedMetodoId;
    }

    public void setSelectedMetodoId(String selectedMetodoId) {
        this.selectedMetodoId = selectedMetodoId;
    }

    public ProcesoAdqMain getMainBean() {
        return mainBean;
    }

    public void setMainBean(ProcesoAdqMain mainBean) {
        this.mainBean = mainBean;
    }

    // </editor-fold>
}
