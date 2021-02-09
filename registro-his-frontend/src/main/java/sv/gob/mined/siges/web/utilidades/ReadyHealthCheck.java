/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.StaticRestClient;

@Health
@ApplicationScoped
public class ReadyHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        try {
            List<String> operaciones = new ArrayList<>();
            operaciones.add(ConstantesOperaciones.AUTENTICADO);
            String token = JWTUtils.generarToken("SIGES-APP", "", "/privateKey.pem", operaciones, 10);
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setFirst(0L);
            filtro.setMaxResults(1L);
            WebTarget webTarget = StaticRestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
            //TODO: llamar a algo del backend a ver si responde
//            SgAyuda[] ayudas = StaticRestClient.invokePost(webTarget, filtro, SgAyuda[].class, token);
//            return ayudas != null ? HealthCheckResponse.named("ready").up().build() : HealthCheckResponse.named("ready").down().build();
            return HealthCheckResponse.named("ready").up().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
