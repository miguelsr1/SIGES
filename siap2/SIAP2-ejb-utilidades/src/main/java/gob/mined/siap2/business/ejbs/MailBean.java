/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Esta clase incluye el manejo de correo electrónico.
 * @author Sofis Solutions
 */
@Stateless(name = "MailBean")
@LocalBean
public class MailBean {

    @Resource(mappedName = "java:jboss/mail/SIAPMailService")
    private Session mailSession;
    @Inject
    private ConfiguracionBean cnfBean;
    private static final Logger logger
            = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Constructor por defecto.
     */
    public MailBean() {
    }
    
    
    /**
     * Envío del correo electrónico a un usuario de que se ha creado el mismo.
     * @param mail - casilla de correo
     * @param codigo - código del usuario
     * @param password  - nueva contraseña
     */
    @Asynchronous
    public void enviarNuevoUsuario(String mail, String codigo, String password) {
        try {
            List<String> correos = new LinkedList();
            correos.add(mail);

            String mensaje = cnfBean.obtenerCnfPorCodigo(ConstantesConfiguracion.TEMPLATE_MENSAJE_MAIL_NUEVO_USUARIO).getCnfValor();
            Map map = new HashMap();
            map.put("usuario", codigo);
            map.put("password",password);
            mensaje = TextUtils.replaceTokens(mensaje, map);

            String asuto = cnfBean.obtenerCnfPorCodigo(ConstantesConfiguracion.ASUNTO_MAIL_NUEVO_USUARIO).getCnfValor();

            enviarMail(asuto, correos, mensaje);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    
    
 
    /**
     * Este método envía un mail 
     * @param subject asunto del mail
     * @param recipients - destinatarios.
     * @param message - contenido del mensaje.
     */
    @Asynchronous
    public void enviarMail(String subject, List<String> recipients, String message) {
        try {

            boolean enviar = true;
            if (StringsUtils.isEmpty(message)) {
                logger.log(Level.SEVERE, "No se envia correo ya que el mensaje es vacío");
                enviar = false;
            }
            if (recipients == null || recipients.size() <= 0) {
                logger.log(Level.SEVERE, "No se envia correo ya que no hay destinatarios");
                enviar = false;
            }
            if (enviar) {
                MimeMessage m = new MimeMessage(mailSession);
                Address from = new InternetAddress(mailSession.getProperty("mail.from"));
                Address[] to = new InternetAddress[recipients.size()];

                int i = 0;
                for (String dir : recipients) {
                    to[i] = new InternetAddress(dir.trim());
                    i++;
                }
                m.setFrom(from);
                m.setRecipients(Message.RecipientType.TO, to);
                m.setSubject(subject);
                m.setSentDate(new java.util.Date());
                m.setContent(message, "text/html");
                Transport.send(m);

            }
        } catch (GeneralException | MessagingException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
