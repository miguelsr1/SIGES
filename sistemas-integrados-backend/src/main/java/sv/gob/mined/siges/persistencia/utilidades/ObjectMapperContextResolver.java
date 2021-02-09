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
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Sofis Solutions
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    final ObjectMapper mapper = new ObjectMapper();

    public ObjectMapperContextResolver() {
        mapper.registerModule(new Hibernate5Module().disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION))
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new BlankStringsAsNullModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, true);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}