/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.cache;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.jcache.embedded.JCachingProvider;
import sv.gob.mined.siges.web.constantes.ConstantesConfiguracion;

/**
 *
 * @author Sofis Solutions
 */
@ApplicationScoped
public class InfinispanConfig {

    private static final Logger LOGGER = Logger.getLogger(InfinispanConfig.class.getName());

    @Inject
    @ConfigProperty(name = "cache.catalogo.defaultExpiryMinutes", defaultValue = "5")
    private String durationMinutes;

    @Produces
    @ApplicationScoped
    private CacheManager manager;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) throws Exception {

        JCachingProvider cachingProvider = (JCachingProvider) Caching.getCachingProvider();
        manager = cachingProvider.getCacheManager();

        LOGGER.log(Level.INFO, "### Cache Provider started: " + cachingProvider.getDefaultURI());
        LOGGER.log(Level.INFO, "### Expiricy policy: " + durationMinutes + " minutes.");

        Duration duration = new Duration(TimeUnit.MINUTES, Long.parseLong(durationMinutes));

        Factory expiryPolicyFactory = CreatedExpiryPolicy.factoryOf(duration);
        MutableConfiguration mutableConfiguration = new MutableConfiguration();
        mutableConfiguration.setTypes(String.class, String.class);
        mutableConfiguration.setStoreByValue(true);
        mutableConfiguration.setExpiryPolicyFactory(expiryPolicyFactory);


        manager.createCache(ConstantesConfiguracion.AYUDA_CACHE, mutableConfiguration);
      
        
        
                   
    }

    public CacheManager getManager() {
        return manager;
    }

}
