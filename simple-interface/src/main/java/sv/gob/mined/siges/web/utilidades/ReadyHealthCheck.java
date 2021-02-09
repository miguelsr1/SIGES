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
        try {
            return HealthCheckResponse.named("ready").up().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}