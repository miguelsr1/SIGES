/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.kafka;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.kafka.common.serialization.Serializer;
//
//
//public class KafkaJsonSerializer implements Serializer {
//    
//    private static final Logger LOGGER = Logger.getLogger(KafkaJsonSerializer.class.getName());
//
//
//    @Override
//    public void configure(Map map, boolean b) {
//
//    }
//
//    @Override
//    public byte[] serialize(String s, Object o) {
//        byte[] retVal = null;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            retVal = objectMapper.writeValueAsBytes(o);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, e.getMessage());
//        }
//        return retVal;
//    }
//
//    @Override
//    public void close() {
//
//    }
//}