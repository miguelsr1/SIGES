/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.kafka;

//import sv.gob.mined.siges.dto.SgNotification;
//import java.util.Properties;
//import java.util.concurrent.Future;
//import java.util.logging.Logger;
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.ejb.Startup;
//import javax.enterprise.context.ApplicationScoped;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.serialization.StringSerializer;
//
///**
// *
// * @author Sofis Solutions
// * Una sola instancia por aplicación.
// */
//@ApplicationScoped
//@Startup
//public class NotificationsProducer {
//
//    private static final Logger LOGGER = Logger.getLogger(NotificationsProducer.class.getName());
//    
//    private Producer<String, SgNotification> producer;
//    
//    @PostConstruct
//    public void init(){
//        producer = this.createProducer();
//    }
//
//    private Producer<String, SgNotification> createProducer() {
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("kafka.server"));
//        //props.put(ProducerConfig.CLIENT_ID_CONFIG, System.getProperty("project.stage"));
//        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
//        return new KafkaProducer<>(props, new StringSerializer(), new KafkaJsonSerializer());
//
//    }
//
//    public Future<RecordMetadata> send(SgNotification not, String topic) {
//        ProducerRecord<String, SgNotification> record = new ProducerRecord<String, SgNotification>(topic, not);
//        Future<RecordMetadata> future = producer.send(record);
//        return future;
//    }
//    
//    @PreDestroy
//    public void close(){
//        if (producer != null){
//            producer.close();
//        }
//    }
//
//}
