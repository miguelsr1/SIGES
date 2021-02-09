/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class BusquedaNombreComponenteBean2 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(BusquedaNombreComponenteBean2.class.getName());
    private FiltroNombreCompleto filtroNombreCompleto2;

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            filtroNombreCompleto2 = (FiltroNombreCompleto) request.getAttribute("filtroNombreCompleto2");
            if (filtroNombreCompleto2 == null) {
                filtroNombreCompleto2 = new FiltroNombreCompleto();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void actualizarFiltro2() {
        String nombreCompleto = "";
        if (!StringUtils.isBlank(filtroNombreCompleto2.getPerPrimerNombre())) {
            nombreCompleto = filtroNombreCompleto2.getPerPrimerNombre() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto2.getPerSegundoNombre())) {
            nombreCompleto += filtroNombreCompleto2.getPerSegundoNombre() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto2.getPerTercerNombre())) {
            nombreCompleto += filtroNombreCompleto2.getPerTercerNombre() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto2.getPerPrimerApellido())) {
            nombreCompleto += filtroNombreCompleto2.getPerPrimerApellido() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto2.getPerSegundoApellido())) {
            nombreCompleto += filtroNombreCompleto2.getPerSegundoApellido() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto2.getPerTercerApellido())) {
            nombreCompleto += filtroNombreCompleto2.getPerTercerApellido();
        }
        if (!StringUtils.isBlank(nombreCompleto)) {
            filtroNombreCompleto2.setNombreCompleto(nombreCompleto.trim());
        } else {
            filtroNombreCompleto2.setNombreCompleto(null);
        }

        String expr = "#{controllerParam2[actionParam2](busquedaNombreComponenteBean2.filtroNombreCompleto2)}";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
    }

    public FiltroNombreCompleto getFiltroNombreCompleto2() {
        return filtroNombreCompleto2;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto2) {
        this.filtroNombreCompleto2 = filtroNombreCompleto2;
    }

}
