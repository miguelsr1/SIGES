/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAlerta;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAlerta;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AlertaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class AlertasEstudianteComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AlertasEstudianteComponenteBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AlertaRestClient alertaEstudianteRestClient;

    private SgEstudiante estudiante;

    private Integer paginado = 10;
    private List<SgAlerta> alertas;
    private Integer totalAlertas;

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }


    public void cargarCombos() {
        try {
            

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void buscarAlerta() {
        try {
            FiltroAlerta filtro = new FiltroAlerta();
            filtro.setEstPk(estudiante.getEstPk());
            filtro.setIncluirCampos(new String[]{"aleVariable", "aleRiesgo"});
            alertas = alertaEstudianteRestClient.buscar(filtro);
            totalAlertas = alertas.size();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgEstudiante getEntidadEnEdicion() {
        return estudiante;
    }

    public void setEntidadEnEdicion(SgEstudiante entidadEnEdicion) {
        this.estudiante = entidadEnEdicion;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public SgEstudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(SgEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public List<SgAlerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<SgAlerta> alertas) {
        this.alertas = alertas;
    }

    public Integer getTotalAlertas() {
        return totalAlertas;
    }

    public void setTotalAlertas(Integer totalAlertas) {
        this.totalAlertas = totalAlertas;
    }

    

}
