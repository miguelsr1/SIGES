/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.utilidades.ObjectMapperContextResolver;

@Named
@RequestScoped
public class RestClient implements Serializable {


    @Inject
    @Named("userToken")
    private String userToken;

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
        Client client = cliBuilder.register(ObjectMapperContextResolver.class).build();
        return client;
    }

    public <T> T invokeGet(WebTarget webTarget, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, "Invocando get: " + webTarget.getUri().toString());
        Response response = webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .get();
        return respond(response, responseType);
    }

    public <S, T> T invokePost(WebTarget webTarget, S request, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, "Invocando post: " + webTarget.getUri().toString());
        Entity<S> requestEntity = Entity.entity(request, MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .post(requestEntity);
        return respond(response, responseType);
    }
    
    public <S, T> T invokePut(WebTarget webTarget, S request, Class<T> responseType) throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException {
        LOGGER.log(Level.INFO, "Invocando put: " + webTarget.getUri().toString());
        Entity<S> requestEntity = Entity.entity(request, MediaType.APPLICATION_JSON_TYPE);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .put(requestEntity);
        return respond(response, responseType);
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
                    be.addError(Mensajes.obtenerMensaje(Mensajes.ERROR_SEGURIDAD));
                    throw be;
                } else if (response.getStatus() == HttpStatus.SC_NOT_FOUND){
                    BusinessException be = new BusinessException();
                    be.addError(Mensajes.obtenerMensaje(Mensajes.NOT_FOUND));
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