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
public class BusquedaNombreComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(BusquedaNombreComponenteBean.class.getName());
    private FiltroNombreCompleto filtroNombreCompleto;

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            filtroNombreCompleto = (FiltroNombreCompleto) request.getAttribute("filtroNombreCompleto");
            if (filtroNombreCompleto == null) {
                filtroNombreCompleto = new FiltroNombreCompleto();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void actualizarFiltro() {
        String nombreCompleto = "";
        if (!StringUtils.isBlank(filtroNombreCompleto.getPerPrimerNombre())) {
            nombreCompleto = filtroNombreCompleto.getPerPrimerNombre() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto.getPerSegundoNombre())) {
            nombreCompleto += filtroNombreCompleto.getPerSegundoNombre() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto.getPerTercerNombre())) {
            nombreCompleto += filtroNombreCompleto.getPerTercerNombre() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto.getPerPrimerApellido())) {
            nombreCompleto += filtroNombreCompleto.getPerPrimerApellido() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto.getPerSegundoApellido())) {
            nombreCompleto += filtroNombreCompleto.getPerSegundoApellido() + " ";
        }
        if (!StringUtils.isBlank(filtroNombreCompleto.getPerTercerApellido())) {
            nombreCompleto += filtroNombreCompleto.getPerTercerApellido();
        }
        if (!StringUtils.isBlank(nombreCompleto)) {
            filtroNombreCompleto.setNombreCompleto(nombreCompleto.trim());
        } else {
            filtroNombreCompleto.setNombreCompleto(null);
        }

        String expr = "#{controllerParam[actionParam](busquedaNombreComponenteBean.filtroNombreCompleto)}";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

}
