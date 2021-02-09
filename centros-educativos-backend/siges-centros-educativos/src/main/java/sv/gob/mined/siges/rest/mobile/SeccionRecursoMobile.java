/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest.mobile;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import io.opentracing.Tracer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.SeccionBean;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;

@RequestScoped
@Path("/m/v1/secciones")
@Tag(name = "Seccion M API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.BUSCAR_SECCION})
public class SeccionRecursoMobile {

    private static final Logger LOGGER = Logger.getLogger(SeccionRecursoMobile.class.getName());

    @Inject
    private SeccionBean servicio;

    @Inject
    private Tracer tracer;
    
    @Inject
    private JsonWebToken jwt;
    
    @Inject
    @ConfigProperty(name = "external-api.max-results", defaultValue = "50")
    private Long allowedMaxResults;

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgSeccion
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSeccion.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{seccionId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SECCION})
    //@Timed
    public Response obtenerPorId(@PathParam("seccionId") Long id) {
        try {
            SgSeccion entity = servicio.obtenerPorId(id);
            if (entity == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(entity).build();
            }
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroSeccion
     * @return Response Lista de SgSeccion
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSeccion.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SECCION})
    //@Timed
    public Response buscar(FiltroSeccion filtro) {
        try {
            filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SECCION);
            if (filtro.getMaxResults() == null || filtro.getMaxResults() > allowedMaxResults) {
                filtro.setMaxResults(allowedMaxResults);
            }
            Boolean validarIncluirCampos = Boolean.TRUE; 
            if (filtro.getSecPk() != null){
                validarIncluirCampos = Boolean.FALSE;
            }
            if (validarIncluirCampos && (filtro.getIncluirCampos() == null || filtro.getIncluirCampos().length < 1)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_INCLUIR_CAMPOS_VACIO)).build();
            }
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroSeccion
     * @return Response Lista de SgSeccion
     */
    @POST
    @Operation(summary = "Devuelve la cantidad total de elementos que satisfacen el criterio")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Long.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar/total")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SECCION})
    //@Timed
    public Response buscarTotal(FiltroSeccion filtro) {
        try {
            filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SECCION);
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }


}
