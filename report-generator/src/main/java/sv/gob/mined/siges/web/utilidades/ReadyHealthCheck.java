/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Health
@ApplicationScoped
public class ReadyHealthCheck implements HealthCheck {
            
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("ready").up().build();
    }
}