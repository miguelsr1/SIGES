/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtros;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@WebFilter()
public class ControlAcceso implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ControlAcceso.class.getName());

    private FilterConfig filterConfig = null;

    @Inject
    private SessionBean sessionBean;

    public ControlAcceso() {
    }

    /**
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     *
     * @exception IOException Excepción si ocurre un error de entrada / salida
     * @exception ServletException Excepción si ocurre un error de servlet
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            
            Boolean primeraVez = Boolean.FALSE;
            if (sessionBean.getUser() == null) {
                primeraVez = Boolean.TRUE;
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(httpRequest));
                sessionBean.setUser(httpRequest.getUserPrincipal());
            }

            if (BooleanUtils.isNotTrue(sessionBean.getEntidadUsuario().getUsuAceptaTerminos()) || !sessionBean.getOperaciones().contains(ConstantesOperaciones.TIENE_ACCESO_CENTROS_EDUCATIVOS)) {

                //Recargar usuario para verificar si aceptó terminos o se le agregó la operación
                if (!primeraVez) {
                    sessionBean.setUserIp(JSFUtils.getRemoteAddress(httpRequest));
                    sessionBean.setUser(httpRequest.getUserPrincipal());
                }
               
                if (BooleanUtils.isNotTrue(sessionBean.getEntidadUsuario().getUsuAceptaTerminos()) || !sessionBean.getOperaciones().contains(ConstantesOperaciones.TIENE_ACCESO_CENTROS_EDUCATIVOS)) {
                    httpResponse.sendRedirect(System.getProperty("service.welcome.baseUrl"));
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/pp/error.xhtml");
        }

    }

    /**
     * Método de obtención del filtro de configuración
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Método de asignación de un valor al objeto filtro de configuración.
     *
     * @param filterConfig Objeto filtro de configuración
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Método de destrucción para el objeto filtro de configuración
     */
    public void destroy() {
    }

    /**
     * Método de inicialización del filtro de configuración
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
