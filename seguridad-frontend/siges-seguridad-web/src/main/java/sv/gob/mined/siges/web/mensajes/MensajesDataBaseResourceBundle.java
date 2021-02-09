/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.ListResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author Sofis Solutions
 */
public class MensajesDataBaseResourceBundle extends ListResourceBundle {

    private static final Logger LOGGER = Logger.getLogger(MensajesDataBaseResourceBundle.class.getName());
    
    public MensajesDataBaseResourceBundle(){
        
    }
    
    
    @Override
    protected Object[][] getContents() {
        return new Object[][]{};
//        try {   
//           List<String> operaciones = new ArrayList<>();
//            operaciones.add(ConstantesOperaciones.AUTENTICADO);
//            String token = JWTUtils.generarToken("SIGES-APP", "", "/privateKey.pem", operaciones, 10);
//            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/mensajes/obtenerpararesourcebundle");
//            return RestClient.invokeGet(webTarget, Object[][].class, token);
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//            return new Object[][]{};
//        }
    }
    
}
