/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.WebTarget;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.restclient.RestClient;

/**
 *
 * @author Sofis Solutions
 */
public class EtiquetasDataBaseResourceBundle extends ListResourceBundle {

    private static final Logger LOGGER = Logger.getLogger(EtiquetasDataBaseResourceBundle.class.getName());
    
    public EtiquetasDataBaseResourceBundle(){
        
    }
    
    
    @Override
    protected Object[][] getContents() {
        try {   
           List<String> operaciones = new ArrayList<>();
            operaciones.add(ConstantesOperaciones.AUTENTICADO);
            String token = JWTUtils.generarToken("SIGES-APP", "", "/privateKey.pem", operaciones, 10);
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etiquetas/obtenerpararesourcebundle");
            return RestClient.invokeGet(webTarget, Object[][].class, token);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return new Object[][]{};
        }
    }
    
}
