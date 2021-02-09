/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.restclient.ConfiguracionRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TermsBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TermsBean.class.getName());

    @Inject
    private ConfiguracionRestClient restClient;

    private SgConfiguracion entidadEnEdicion;



    public TermsBean() {
    }

    @PostConstruct
    public void init() {
        try {
            entidadEnEdicion = restClient.obtenerPorCodigo(Constantes.CONFIG_CODIGO_TERMINOS_Y_CONDICIONES);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public SgConfiguracion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgConfiguracion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }
}
