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
import sv.gob.mined.siges.web.constantes.Constantes;

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

        //Centros
        manager.createCache(Constantes.AYUDA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SEDES_LAZY_CACHE, mutableConfiguration);
        //Catalogo
        manager.createCache(Constantes.CONFIGURACION_CACHE, mutableConfiguration); 
        manager.createCache(Constantes.GLOSARIOS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SEXO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ETNIA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESTADO_CIVIL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PAIS_CACHE, mutableConfiguration);
        manager.createCache(Constantes.DEPARTAMENTO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MUNICIPIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CANTON_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CASERIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ZONA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_CALLE_CACHE, mutableConfiguration);
        
        
        //Centros cache
        manager.createCache(Constantes.NIVEL_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CICLO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MODALIDAD_CACHE, mutableConfiguration);
        manager.createCache(Constantes.OPCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.GRADO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ANIO_LECTIVO_CACHE, mutableConfiguration);
        
                   
    }

    public CacheManager getManager() {
        return manager;
    }

}
