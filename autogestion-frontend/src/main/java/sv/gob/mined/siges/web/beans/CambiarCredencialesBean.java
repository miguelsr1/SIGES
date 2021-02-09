/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CambiarCredencialesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CambiarCredencialesBean.class.getName());

    @Inject
    private UsuarioRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    private String passwordActual;
    private String nuevaPassword;
    private String confirmaPassword;

    public CambiarCredencialesBean() {
    }

    @PostConstruct
    public void init() {
        try {

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public String confirmar() {
        try {
            Boolean ok = Boolean.TRUE;
            String codigoUsuario = sessionBean.getUser().getName();
            if (StringUtils.isAllBlank(passwordActual)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIA_ACTUAL_VACIA);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (StringUtils.isAllBlank(nuevaPassword)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIA_NUEVA_VACIA);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (StringUtils.isAllBlank(confirmaPassword)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONFIRMACION_CONTRASENIA_VACIA);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }        

            if (nuevaPassword.length() < 8) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LARGO_MINIMO_8), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (SofisStringUtils.cantidadDigitos(nuevaPassword) < 1) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CANTIDAD_MINIMA_DIGITOS_1), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (SofisStringUtils.cantidadLetrasMayusculas(nuevaPassword) < 1) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CANTIDAD_MINIMA_LETRAS_MAYUSCULAS_1), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (nuevaPassword.contains(codigoUsuario)) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIA_INCLUYE_USUARIO), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            
            if (!ok){
                return null;
            }

            if (nuevaPassword.length() > 0 && !nuevaPassword.equals(confirmaPassword)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIAS_NO_COINCIDEN);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
            } else {
                restClient.cambiarpassword(codigoUsuario, SofisStringUtils.encodeSHA256Hex(passwordActual), SofisStringUtils.encodeSHA256Hex(nuevaPassword));
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.CAMBIO_CONTRASENIA_CORRECTO), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }

    public String getConfirmaPassword() {
        return confirmaPassword;
    }

    public void setConfirmaPassword(String confirmaPassword) {
        this.confirmaPassword = confirmaPassword;
    }

}
