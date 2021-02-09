/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import sv.gob.mined.siges.negocio.mensajes.Errores;

/**
 * REST Web Service
 *
 */
@RequestScoped
@Path("/info")
@Tag(name = "Info API V1")
/**
 * Servicios REST para versión.
 */
public class InfoRecurso {

    private static final Logger LOGGER = Logger.getLogger(InfoRecurso.class.getName());

    /**
     * Devuelve la versión del proyecto
     * @return
     */
    @GET
    @Path("/version")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response getVersion() {
        try {
            return Response.status(HttpStatus.SC_OK).entity(System.getProperty("project.version")).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

}
