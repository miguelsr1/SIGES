/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.CasUserProfile;
import sv.gob.mined.siges.dto.SgAuthRequest;
import sv.gob.mined.siges.dto.SgAuthResponse;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.AuthorizationBean;
import sv.gob.mined.siges.negocio.servicios.ConfiguracionBean;
import sv.gob.mined.siges.utils.JWTUtils;

@RequestScoped
@Path("/v1/auth")
public class AuthorizationManager {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationManager.class.getName());

    @Inject
    private AuthorizationBean servicio;
    
    @Inject
    private ConfiguracionBean configuracionBean;
    
    @PersistenceContext
    private EntityManager em;


    @POST
    @Path("/oauth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response oauth(SgAuthRequest req) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            
            if (!StringUtils.isBlank(req.getOauthToken())) {

                SSLContextBuilder builder = new SSLContextBuilder();

                builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                httpclient = HttpClients.custom().setSSLSocketFactory(
                        sslsf).build();

                URIBuilder ubuilder = new URIBuilder(System.getProperty("service.cas.baseUrl") + "oauth2.0/profile");
                ubuilder.setParameter("access_token", req.getOauthToken());
                HttpGet get = new HttpGet(ubuilder.build());
                
                response = httpclient.execute(get);
              
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                       
                    ObjectMapper objectMapper = new ObjectMapper();
                    CasUserProfile userProfile = objectMapper.readValue(response.getEntity().getContent(), CasUserProfile.class);                  
                    String token = this.generarToken(userProfile.getId(), req.getAddress(), req.getCategoriasOperacion(), userProfile.getClient_id());                
                    SgAuthResponse auth = new SgAuthResponse();
                    auth.setJwt(token);
                    return Response.status(HttpStatus.SC_OK).entity(auth).build();
                } else {
                    LOGGER.log(Level.SEVERE, "OAUTH CAS STATUS: " + response.getStatusLine().getStatusCode());
                    return Response.status(response.getStatusLine().getStatusCode()).build();
                }

            }

            return Response.status(HttpStatus.SC_UNAUTHORIZED).entity(Errores.ERROR_GENERAL).build();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @POST
    @Path("/basic")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response basic(SgAuthRequest request) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {

            SSLContextBuilder builder = new SSLContextBuilder();

            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();

            HttpPost post = new HttpPost(System.getProperty("service.cas.baseUrl") + "/v1/users/");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", request.getUsername()));
            params.add(new BasicNameValuePair("password", request.getPassword()));
            post.setEntity(new UrlEncodedFormEntity(params));
            
            
            ///cas/oauth2.0/accessToken?username=casuser&password=Mellon&grant_type=password&client_id=clientid

            response = httpclient.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {                
                String token = this.generarToken(request.getUsername(), request.getAddress(), request.getCategoriasOperacion(), request.getAudience());
                SgAuthResponse auth = new SgAuthResponse();
                auth.setJwt(token);
                return Response.status(HttpStatus.SC_OK).entity(auth).build();
            } else {
                LOGGER.log(Level.SEVERE, "AUTH CAS STATUS: " + response.getStatusLine().getStatusCode());
                return Response.status(response.getStatusLine().getStatusCode()).build();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    private String generarToken(String userCode, String address, List<Long> categoriasOp, String audience) throws Exception {
        List<String> operaciones = servicio.obtenerOperacionesPorUsuarioCodigo(userCode, categoriasOp);
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        if (audience.equals("SIGES")){
            return JWTUtils.generarToken(userCode, address, "/privateKey.pem", operaciones, 120, "SIGES", audience);
        } else {
            Integer maxResult=50;
            try{
                maxResult=configuracionBean.obtenerMaxResultPorAudience(audience);
            }catch(Exception ex){
                maxResult=50;
            }
            return JWTUtils.generarToken(userCode, address, "/privateKey.pem", operaciones, 50, "SIGES", audience, maxResult, true);
        }
    }

}
