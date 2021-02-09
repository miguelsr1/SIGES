/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.entities.data.impl.TextoAyuda;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.TextoDelegate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class ApplicationTextMB implements Serializable {

    static final long CACHE_TEXTOS_MAX_SIZE = 5000;
    static final long CACHE_TEXTOS_EXPIRE_AFTER_WRITE = 10; //en horas

    @Inject
    private TextoDelegate textoDelegate;

    @Inject
    EntityManagementDelegate emd;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private Map<Integer, Cache> cacheText;
    private Map<Integer, Cache> cacheAyudaText;

    @PostConstruct
    public void init() {       
        cacheText = new HashMap();
        cacheAyudaText = new HashMap();
    }

    /**
     * Retorna el cache de los textos
     *
     * @param idIdioma
     * @return
     */
    public Cache obtenerCacheTextos(Integer idIdioma) {
        Cache<String, String> cache = cacheText.get(idIdioma);

        if (cache == null) {
            cache = new Cache2kBuilder<String, String>() {
            }
                //.name("Textos_Para_Idioma_" + idIdioma)
                .expireAfterWrite(CACHE_TEXTOS_EXPIRE_AFTER_WRITE, TimeUnit.HOURS)
                .entryCapacity(CACHE_TEXTOS_MAX_SIZE)
                .refreshAhead(true) // keep fresh when expiring
                .loader(new TextosCacheLoader(idIdioma)) // auto populating function
                .build();

            cacheText.put(idIdioma, cache);
        }

        return cache;

    }

    /**
     * Retorna el cache de los textos de ayuda
     *
     * @param idIdioma
     * @return
     */
    public Cache obtenerCacheTextosAyuda(Integer idIdioma) {
        Cache<String, String> cache = cacheAyudaText.get(idIdioma);

        if (cache == null) {
            cache = new Cache2kBuilder<String, String>() {
            }
                //.name("Textos_Ayuda_Para_Idioma_" + idIdioma)
                .expireAfterWrite(CACHE_TEXTOS_EXPIRE_AFTER_WRITE, TimeUnit.HOURS)
                .entryCapacity(CACHE_TEXTOS_MAX_SIZE)
                .refreshAhead(true) // keep fresh when expiring
                .loader(new TextosAyudaCacheLoader(idIdioma)) // auto populating function
                .build();

            cacheAyudaText.put(idIdioma, cache);
        }

        return cache;

    }

    
    /**
     * Encargado de eliminar un texto que ya fue cargado
     *
     * @param texto
     */
    public void limpiarCacheTexto(Texto texto) {
        if (texto.getTexIdiId() != null) {
            Cache<String, String> cache = cacheText.get(texto.getTexIdiId().getIdiId());
            if (cache != null) {
                cache.containsAndRemove(texto.getTexCodigo());
            }
        }
    }


    /**
     * Encargado de eliminar un texto que ya fue cargado
     *
     */
    public void limpiarCacheTextoAyuda(TextoAyuda texto) {
        if (texto.getIdioma() != null) {
            Cache<String, String> cache = cacheAyudaText.get(texto.getIdioma().getIdiId());
            if (cache != null) {
                cache.containsAndRemove(texto.getCodigo());
            }
        }
    }

    /**
     * Esta clase es la encargada de obtener el cache de los textos para un
     * idioma
     *
     * @param idIdioma
     * @return
     */
    class TextosCacheLoader extends CacheLoader<String, String> {

        private Integer idiomaId;

        public TextosCacheLoader(Integer idiomaId) {
            this.idiomaId = idiomaId;
        }

        @Override
        public String load(String codigo) throws Exception {
            if (TextUtils.isEmpty(codigo)) {
                return "";
            }
            return textoDelegate.obtenerCrearTextoPorCodigo(codigo, idiomaId);
        }
    }

    /**
     * Esta clase es la encargada de obtener el cache de los textos para un
     * idioma
     *
     * @param idIdioma
     * @return
     */
    class TextosAyudaCacheLoader extends CacheLoader<String, String> {

        private Integer idiomaId;

        public TextosAyudaCacheLoader(Integer idiomaId) {
            this.idiomaId = idiomaId;
        }

        @Override
        public String load(String codigo) throws Exception {
            if (TextUtils.isEmpty(codigo)) {
                return "";
            }
            return textoDelegate.obtenerCrearTextoAyudaPorCodigos(codigo, idiomaId);
        }
    }

}
