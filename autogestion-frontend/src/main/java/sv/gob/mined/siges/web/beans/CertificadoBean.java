/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ConfiguracionRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
@DependsOn("SessionBean")
public class CertificadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CertificadoBean.class.getName());

    @Inject
    private UsuarioRestClient restClient;
    
    @Inject
    private ConfiguracionRestClient configClient;

    @Inject
    private SessionBean sessionBean;

    private String password;
    private String confirmaPassword;
    private String textoAyuda;

    public CertificadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            SgConfiguracion c = configClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_TEXTO_CERTIFICADO_AUTOGESTION);
            if (c != null){
                textoAyuda = c.getConValor();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public StreamedContent confirmar() {
        try {
            Boolean ok = Boolean.TRUE;
            String codigoUsuario = sessionBean.getUser().getName();

            if (StringUtils.isAllBlank(password)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIA_NUEVA_VACIA);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (StringUtils.isAllBlank(confirmaPassword)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONFIRMACION_CONTRASENIA_VACIA);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }

            if (password.length() < 8) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_LARGO_MINIMO_8), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (SofisStringUtils.cantidadDigitos(password) < 1) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CANTIDAD_MINIMA_DIGITOS_1), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (SofisStringUtils.cantidadLetrasMayusculas(password) < 1) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CANTIDAD_MINIMA_LETRAS_MAYUSCULAS_1), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }
            if (password.contains(codigoUsuario)) {
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIA_INCLUYE_USUARIO), FacesMessage.SEVERITY_ERROR);
                ok = Boolean.FALSE;
            }

            if (!ok) {
                return null;
            }

            if (password.length() > 0 && !password.equals(confirmaPassword)) {
                String validacion = Mensajes.obtenerMensaje(Mensajes.ERROR_CONTRASENIAS_NO_COINCIDEN);
                JSFUtils.mostrarMensaje(ConstantesComponentesId.ID_MSG_TEMPLATE, validacion, FacesMessage.SEVERITY_ERROR);
            } else {
                byte[] cert = restClient.generarMiCertificado(password);
                if (cert != null) {
                    InputStream targetStream = new ByteArrayInputStream(cert);
                    StreamedContent file = new DefaultStreamedContent(targetStream, "application/x-pkcs12", "certificado_" + sessionBean.getUser().getName() + "_SIGES" + ".p12");
                    return file;
                } else {
                   JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String getConfirmaPassword() {
        return confirmaPassword;
    }

    public void setConfirmaPassword(String confirmaPassword) {
        this.confirmaPassword = confirmaPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTextoAyuda() {
        return textoAyuda;
    }

    public void setTextoAyuda(String textoAyuda) {
        this.textoAyuda = textoAyuda;
    }

    
}
