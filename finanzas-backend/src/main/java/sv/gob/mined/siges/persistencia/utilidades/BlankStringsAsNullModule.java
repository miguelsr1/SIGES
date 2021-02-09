/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * Gestión para tratar los strings nulos.
 *
 * @author Sofis Solutions
 */
public class BlankStringsAsNullModule extends SimpleModule {

    public BlankStringsAsNullModule() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                    JsonProcessingException {
                String s = jp.getValueAsString();
                if (!StringUtils.isBlank(s)) {
                    return s;
                }
                return null;
            }
        });
    }
}
