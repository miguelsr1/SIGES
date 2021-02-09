/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sv.gob.mined.siges.web.beans.ConstantesOperaciones;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.utilidades.JWTUtils;

@RequestScoped
@Path("/v1/tokengenerator")
public class TokenGeneratorRecurso {

    private static final Logger LOGGER = Logger.getLogger(TokenGeneratorRecurso.class.getName());

    @Inject
    private SeguridadRestClient seguridadClient;
    
    private List<String> operaciones;
    
    @GET
    @Path("/generate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerToken(@QueryParam("user") String usuario, @QueryParam("ip") String ip, @QueryParam("expirationMinutes") Integer expirationTimeInMinutes) {
        try {
            if (usuario == null) {
                usuario = "casuser";
            }
            if (ip == null) {
                ip = "192.168.1.5";
            }
            if (expirationTimeInMinutes == null) {
                expirationTimeInMinutes = 20;
            }

            cargarOperaciones(usuario);

            String token = JWTUtils.generarToken(usuario, ip, "/privateKey.pem", operaciones, expirationTimeInMinutes);
            return Response.status(200).entity(token).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(500).entity("Error interno").build();
        }
    }
    
    public void cargarOperaciones(String user) {
        operaciones = new ArrayList<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        try {
            operaciones.addAll(seguridadClient.obtenerOperacionesPorCodigoUsuario(user));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
