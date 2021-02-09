/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import sv.gob.mined.siges.negocio.servicios.HealthBean;

@Health
@ApplicationScoped
public class ReadyHealthCheck implements HealthCheck {
    
    @Inject
    private HealthBean healthBean;
    
    @Override
    public HealthCheckResponse call() {
        try {
            return healthBean.checkDatabaseConnection() ? HealthCheckResponse.named("ready").up().build() : HealthCheckResponse.named("ready").down().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}