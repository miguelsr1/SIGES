package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrar;
import sv.gob.mined.siges.web.utilidades.ObjectMapperContextResolver;

/**
 *
 * @author fabricio
 */
@RequestScoped
@Named
public class RestClientManager implements Serializable {
    
    private Client client;
        
    @PostConstruct
    public void init() {
        ClientBuilder cliBuilder = ClientBuilder.newBuilder();
        client = ClientTracingRegistrar.configure(cliBuilder).register(ObjectMapperContextResolver.class).build();
 
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }

    
    
}
