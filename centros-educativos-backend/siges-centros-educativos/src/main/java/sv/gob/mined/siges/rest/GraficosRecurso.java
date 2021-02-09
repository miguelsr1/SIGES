/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import javax.ws.rs.QueryParam;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.graficos.GraficoColumnas;
import sv.gob.mined.siges.graficos.GraficoRadar;
import sv.gob.mined.siges.negocio.servicios.GraficosBean;

@RequestScoped
@Path("/v1/graficos")
@Tag(name = "Graficos API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO})
public class GraficosRecurso {

    private static final Logger LOGGER = Logger.getLogger(GraficosRecurso.class.getName());

    @Inject
    private GraficosBean servicio;
    
    @Inject
    private JsonWebToken jwt;
    
    /**
     * Devuelve evolución de encuestas comparadas con matrículas
     *
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve evolución de encuestas comparadas con matrículas")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoRadar.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/encuestascontramatriculas")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerEvolucionEncuestasYMatriculas(@QueryParam("depPk") Long depPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerEvolucionEncuestasYMatriculas(depPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
            
    /**
     * Devuelve evolución de encuestas por sexo
     *
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve evolución de encuestas por sexo")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoRadar.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/encuestasporsexo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerEvolucionEncuestasPorSexo(@QueryParam("depPk") Long depPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerEvolucionEncuestasPorSexo(depPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve alertas actuales de estudiante
     *
     * @param estPk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve alertas actuales de estudiante.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoRadar.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/alertas")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerAlertasActuales(@QueryParam("estPk") Long estPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerAlertasActuales(estPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve asistencias de estudiante agrupadas por año
     *
     * @param estPk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve asistencias de estudiante agrupadas por año.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/asistencias/poranio")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerAsistenciasPorAnio(@QueryParam("estPk") Long estPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerAsistenciasPorAnio(estPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve asistencias de estudiante agrupadas por mes en año
     *
     * @param estPk Long
     * @param anio Integer
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve asistencias de estudiante agrupadas por mes en año.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/asistencias/pormes")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerAsistenciasPorMesEnAnio(@QueryParam("estPk") Long estPk, @QueryParam("anio") Integer anio) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerAsistenciasPorMesEnAnio(estPk, anio)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
     /**
     * Devuelve motivos de inasistencias de estudiante agrupadas por año
     *
     * @param estPk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve motivos de inasistencias de estudiante agrupadas por año.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/motinasistencias/poranio")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerMotivosInasistenciasPorAnio(@QueryParam("estPk") Long estPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerMotivosInasistenciasPorAnio(estPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve motivos de inasistencia de estudiante agrupadas por mes en año
     *
     * @param estPk Long
     * @param anio Integer
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve motivos de inasistencias de estudiante agrupadas por mes en año.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/motinasistencias/pormes")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerMotivosInasistenciasPorMesEnAnio(@QueryParam("estPk") Long estPk, @QueryParam("anio") Integer anio) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerMotivosInasistenciasPorMesEnAnio(estPk, anio)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    
    
     /**
     * Devuelve histórico de calificaciones APR numéricas para estudiante por grado
     *
     * @param estPk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve histórico de calificaciones APR numéricas para estudiante por grado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/calificaciones/aprnumericasporgrado")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerCalificacionesNumericasAprobacionEstudiantePorGrado(@QueryParam("estPk") Long estPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerCalificacionesNumericasAprobacionEstudiantePorGrado(estPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve histórico de calificaciones APR numéricas para estudiante por grado comparadas contra promedio nacional
     *
     * @param estPk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve histórico de calificaciones APR numéricas para estudiante por grado comparadas contra promedio nacional.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/calificaciones/aprnumericasporgradocontrapromedionacionalysede")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerCalificacionesNumericasAprobacionEstudiantePorGradoComparadoContraPromedioNacionalYSede(@QueryParam("estPk") Long estPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerCalificacionesNumericasAprobacionEstudiantePorGradoComparadoContraPromedioNacionalYSede(estPk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Obtener calificaciones ORD numéricas actuales para estudiante por
     * grado y componente
     *
     * @param estPk Long
     * @param componentePk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Obtener calificaciones ORD numéricas actuales para estudiante por grado y componente.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/calificaciones/ordnumericasporgradoycomponente")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerCalificacionesNumericasOrdinariasActualesEstudiantePorGradoYComponente(@QueryParam("estPk") Long estPk, @QueryParam("componentePlanEstudioPk") Long componentePk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerCalificacionesNumericasOrdinariasActualesEstudiantePorGradoYComponente(estPk, componentePk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve histórico de calificaciones APR numéricas para estudiante por grado y componente
     *
     * @param estPk Long
     * @param componentePk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve histórico de calificaciones APR numéricas para estudiante por grado y componente.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/calificaciones/aprnumericasporgradoycomponente")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerCalificacionesNumericasAprobacionEstudiantePorGradoYComponente(@QueryParam("estPk") Long estPk, @QueryParam("componentePlanEstudioPk") Long componentePk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerCalificacionesNumericasAprobacionEstudiantePorGradoYComponente(estPk, componentePk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve histórico de calificaciones APR conceptuales para estudiante por grado y componente
     *
     * @param estPk Long
     * @param componentePk Long
     * @return Response -> GraficoColumnas
     */
    @GET
    @Operation(summary = "Devuelve histórico de calificaciones APR conceptuales para estudiante por grado y componente.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = GraficoColumnas.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/calificaciones/aprconceptualesporgradoycomponente")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerCalificacionesConceptualesAprobacionEstudiantePorGradoYComponente(@QueryParam("estPk") Long estPk, @QueryParam("componentePlanEstudioPk") Long componentePk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerCalificacionesConceptualesAprobacionEstudiantePorGradoYComponente(estPk, componentePk)).build(); 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    
}
