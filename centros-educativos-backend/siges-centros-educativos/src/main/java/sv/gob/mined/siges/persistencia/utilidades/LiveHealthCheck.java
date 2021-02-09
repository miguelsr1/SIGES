/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class LiveHealthCheck implements HealthCheck {
    
    
    @Override
    public HealthCheckResponse call() {
        try {
            return HealthCheckResponse.named("live").up().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}