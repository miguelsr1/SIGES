/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PoliticaContrasenia;
import gob.mined.siap2.web.mb.UsuarioInfo;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "controlContrasenia")
public class ControlContrasenia implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    private UsuarioInfo usuarioInfo;

    @PostConstruct
    public void init() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestServletPath().indexOf("cambioContrasenia") == -1) {//Si la pagina actual no es cambioContrasenia
                if ((usuarioInfo.getUsuario().getUsuCambioPassword() != null && usuarioInfo.getUsuario().getUsuCambioPassword())
                        || vencioPassword(usuarioInfo.getPol(), usuarioInfo.getUsuario().getUsuFechaPassword())) {
                    externalContext.redirect("cambioContrasenia.xhtml");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * Verifica si se venció una password
     *
     * @param pol
     * @param fechaCambioPasswd
     * @return
     */
    private boolean vencioPassword(PoliticaContrasenia pol, Date fechaCambioPasswd) {
        boolean respuesta = false;
        if (pol.getDiasCaducidad() != null && pol.getDiasCaducidad().intValue() > 0) {
            if (diferenciaDias(fechaCambioPasswd, new Date()) > pol.getDiasCaducidad().intValue()) {
                respuesta = true;
            }
        }
        return respuesta;
    }

    /**
     * Devuelve la cantidad de días de diferencia existentes entre 2 fechas
     *
     * @param f1
     * @param f2
     * @return
     */
    private int diferenciaDias(Date f1, Date f2) {
        long desde = f1.getTime();
        long hasta = f2.getTime();
        double dif = (double)hasta - (double)desde;
        return Math.round(Math.round((dif / (1000 * 60 * 60 * 24))));
    }

}
