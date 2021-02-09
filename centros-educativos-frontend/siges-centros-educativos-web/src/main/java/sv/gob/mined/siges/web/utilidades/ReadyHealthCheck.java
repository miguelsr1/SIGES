/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.StaticRestClient;

@Health
@ApplicationScoped
public class ReadyHealthCheck implements HealthCheck {
    
   
    @Override
    public HealthCheckResponse call() {
        Client client = null;
        try {
            client = ClientBuilder.newClient().register(ObjectMapperContextResolver.class);
            List<String> operaciones = new ArrayList<>();
            operaciones.add(ConstantesOperaciones.AUTENTICADO);
            String token = JWTUtils.generarToken("SIGES-APP", "", "/privateKey.pem", operaciones, 10);
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setFirst(0L);
            filtro.setMaxResults(1L);     
            WebTarget webTarget = StaticRestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
            SgAyuda[] ayudas = StaticRestClient.invokePost(webTarget, filtro, SgAyuda[].class, token);
            return ayudas != null ? HealthCheckResponse.named("ready").up().build() : HealthCheckResponse.named("ready").down().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null){
                client.close();
            }
        }
    }
}