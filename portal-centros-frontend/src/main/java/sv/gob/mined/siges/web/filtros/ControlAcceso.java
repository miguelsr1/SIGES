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
import sv.gob.mined.siges.web.beans.SessionBean;
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
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {

            if (sessionBean.getUserIp() == null) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(httpRequest));
            }

            chain.doFilter(request, response);
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, t.getMessage(), t);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/pp/error.xhtml");
        }

    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
