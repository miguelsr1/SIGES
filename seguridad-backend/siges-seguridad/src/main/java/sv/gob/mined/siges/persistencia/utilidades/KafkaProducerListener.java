package sv.gob.mined.siges.persistencia.utilidades;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import sv.gob.mined.siges.dto.SgCertificateRequest;
import sv.gob.mined.siges.dto.SgNotification;

@ApplicationScoped
public class KafkaProducerListener {
    
    private static final Logger LOGGER = Logger.getLogger(KafkaProducerListener.class.getName());
    
//    @Inject
//    private NotificationsProducer notificationsProducer;
//    
//    @Inject
//    private CertificatesProducer certificatesProducer;

    public void onSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) SgNotification not) {
        //notificationsProducer.send(not, System.getProperty("kafka.notifications.topic"));
    }
    
    public void onSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) SgCertificateRequest cert) {
        //certificatesProducer.send(cert, System.getProperty("kafka.certificates.topic"));
    }

    

}
