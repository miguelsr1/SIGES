/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Implemntación del método de mapeo de objetos.
 *
 * @author Sofis Solutions
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultMapper = new ObjectMapper();

    public ObjectMapperContextResolver() {
        defaultMapper.registerModule(new Hibernate5Module().disable(Feature.USE_TRANSIENT_ANNOTATION))
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new BlankStringsAsNullModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, true);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return defaultMapper;
    }

    public ObjectMapper getDefaultMapper() {
        return defaultMapper;
    }
}
