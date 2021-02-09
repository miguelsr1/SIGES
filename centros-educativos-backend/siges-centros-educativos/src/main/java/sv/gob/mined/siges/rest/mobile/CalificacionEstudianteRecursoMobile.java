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
import sv.gob.mined.siges.negocio.servicios.CalificacionEstudianteBean;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgUltimaCalificacionComponenteEstudiante;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;

/**
 *
 * @author Sofis Solutions
 */
@RequestScoped
@Path("/m/v1/calificacionesestudiante")
@Tag(name = "Calificaciones Estudiante API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_CALIFICACIONES_ESTUDIANTE,
    ConstantesOperaciones.ACTUALIZAR_CALIFICACIONES_ESTUDIANTE,
    ConstantesOperaciones.ELIMINAR_CALIFICACIONES_ESTUDIANTE,
    ConstantesOperaciones.BUSCAR_CALIFICACIONES_ESTUDIANTE})
public class CalificacionEstudianteRecursoMobile {

    private static final Logger LOGGER = Logger.getLogger(CalificacionEstudianteRecursoMobile.class.getName());

    @Inject
    private CalificacionEstudianteBean servicio;

    @Inject
    private Tracer tracer;

    @Inject
    private JsonWebToken jwt;
    
    @Inject
    @ConfigProperty(name = "external-api.max-results", defaultValue = "50")
    private Long allowedMaxResults;

    /**
     * Devuelve las últimas calificaciones por componente para un estudiante en
     * un año.
     *
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgCalificacionEstudiante
     */
    @GET
    @Operation(summary = "Devuelve las últimas calificaciones por componente para un estudiante en un año..")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUltimaCalificacionComponenteEstudiante.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/ultimas/{anio:[1-9][0-9]*}/{pkEstudiante:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    //@Timed
    public Response obtenerUltimasCalificacionesEstudiante(@PathParam("anio") Integer anio, @PathParam("pkEstudiante") Long pkEstudiante) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerUltimasCalificacionesEstudiante(anio, pkEstudiante)).build();
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
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgCalificacionEstudiante
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgCalificacionEstudiante.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_CALIFICACIONES_ESTUDIANTE})
    //@Timed
    public Response buscar(FiltroCalificacionEstudiante filtro) {
        try {
            if (filtro.getMaxResults() == null || filtro.getMaxResults() > allowedMaxResults) {
                filtro.setMaxResults(allowedMaxResults);
            }
            if (filtro.getIncluirCampos() == null || filtro.getIncluirCampos().length < 1) {
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
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgCalificacionEstudiante
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_CALIFICACIONES_ESTUDIANTE})
    //@Timed
    public Response buscarTotal(FiltroCalificacionEstudiante filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

}
