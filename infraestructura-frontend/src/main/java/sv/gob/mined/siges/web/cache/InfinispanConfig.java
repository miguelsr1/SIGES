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
                
        //Catalogo
        manager.createCache(Constantes.AYUDA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.DEPARTAMENTO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MUNICIPIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CANTON_CACHE, mutableConfiguration);
        manager.createCache(Constantes.CASERIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ZONA_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_CONSTRUCCION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.FUENTE_FINANCIAMIENTO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.PROPIETARIOS_TERRENO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESPACIO_COMUN_CACHE, mutableConfiguration);
        manager.createCache(Constantes.SERVICIO_BASICO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESTADO_INMUEBLE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.ESTADO_PROCESO_LEGALIZACION_CACHE, mutableConfiguration);
        manager.createCache(Constantes.MINISTERIO_OTORGANTE_CACHE, mutableConfiguration);
        manager.createCache(Constantes.NATURALEZA_CONTRATO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.TIPO_SERVICIO_SANITARIO_CACHE, mutableConfiguration);
        manager.createCache(Constantes.GLOSARIOS, mutableConfiguration);
        manager.createCache(Constantes.TIPOS_CALLE, mutableConfiguration);
        manager.createCache(Constantes.TIPOLOGIA_CONSTRUCCION, mutableConfiguration);
	manager.createCache(Constantes.TIPO_IMAGEN, mutableConfiguration);
        manager.createCache(Constantes.ITEM_OBRA_EXTERIOR, mutableConfiguration);
        manager.createCache(Constantes.OBRA_EXTERIOR, mutableConfiguration);
        manager.createCache(Constantes.UNIDADES_AMINISTRATIVAS, mutableConfiguration);
        manager.createCache(Constantes.TIPO_DOCUMENTO, mutableConfiguration);
        manager.createCache(Constantes.TIPO_CONFIGURACION, mutableConfiguration);
        manager.createCache(Constantes.TIPO_MEJORA, mutableConfiguration);
        manager.createCache(Constantes.ACCESIBILIDAD, mutableConfiguration);
        manager.createCache(Constantes.TIPO_ABASTECIMIENTO, mutableConfiguration);
        manager.createCache(Constantes.MANEJO_DESECHOS, mutableConfiguration);
        manager.createCache(Constantes.TRATAMIENTO_AGUA, mutableConfiguration);
    }
    
    public CacheManager getManager() {
        return manager;
    }

}
