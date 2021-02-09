/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.restclient;

import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.client.excepciones.HttpClientException;
import sv.gob.mined.siges.client.excepciones.HttpServerUnavailableException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrar;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.persistencia.utilidades.ObjectMapperContextResolver;

@Named
@RequestScoped
public class RestClient implements Serializable {

   
    @Inject
    private JsonWebToken callerPrincipal;
    
    private String userToken;
    
    @PostConstruct
    public void init() {
        try {
            userToken = callerPrincipal.getRawToken();
        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

    public RestClient() {
    }

    public static WebTarget getWebTarget(Client client, String service, Object... path) {
        WebTarget target = client.target(System.getProperty("service." + service + ".baseUrl"));
        for (Object part : path) {
            target = target.path(String.valueOf(part));
        }
        return target;
    }

    public static Client getClient() {
        ClientBuilder cliBuilder = ClientBuilder.newBuilder();
        Client client = ClientTracingRegistrar.configure(cliBuilder).register(ObjectMapperContextResolver.class).build();
        return client;
    }

    public <T> T invokeGet(WebTarget webTarget, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, callerPrincipal.getName() + " | " +  "Invocando get: " + webTarget.getUri().toString());
        Response response = webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .get();
        return respond(response, responseType);
    }

    public <S, T> T invokePost(WebTarget webTarget, S request, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, callerPrincipal.getName() + " | " +   "Invocando post: " + webTarget.getUri().toString());
        Entity<S> requestEntity = Entity.entity(request, MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .post(requestEntity);
        return respond(response, responseType);
    }
    
    public <S, T> T invokePut(WebTarget webTarget, S request, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, callerPrincipal.getName() + " | " +  "Invocando put: " + webTarget.getUri().toString());
        Entity<S> requestEntity = Entity.entity(request, MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .put(requestEntity);
        return respond(response, responseType);
    }

    public void invokeDelete(WebTarget webTarget) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, callerPrincipal.getName() + " | " +  "Invocando delete: " + webTarget.getUri().toString());
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .delete();
        respond(response, null);
    }

    private static <T> T respond(Response response, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        if (response.getStatus() >= 400) {

            if (response.getStatus() >= 500) {

                if (response.getStatus() == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                    HttpServerUnavailableException seu = new HttpServerUnavailableException(response);
                    throw seu;
                } else {
                    HttpServerException se = new HttpServerException(response);
                    throw se;
                }

            } else {

                if (response.getStatus() == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
                    BusinessException be = response.readEntity(BusinessException.class);
                    throw be;
                } else if (response.getStatus() == HttpStatus.SC_FORBIDDEN) {
                    BusinessException be = new BusinessException();
                    be.addError("ERROR_SEGURIDAD");
                    throw be;
                } else {
                    HttpClientException ce = new HttpClientException(response);
                    throw ce;
                }

            }

        } else if (responseType == null) {
            return null;
        } else if (responseType.isArray()) {
            return response.readEntity(new GenericType<>(responseType));
        } else {
            return response.readEntity(responseType);
        }
    }
}
