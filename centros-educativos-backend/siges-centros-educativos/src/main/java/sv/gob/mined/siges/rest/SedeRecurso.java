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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import java.time.LocalDate;
import java.util.List;
import javax.json.JsonNumber;
import javax.ws.rs.QueryParam;
import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgCambioDomicilioSedeRequest;
import sv.gob.mined.siges.dto.SgCambioNominacionSedeRequest;
import sv.gob.mined.siges.dto.SgDatoGeneralCentroEducativo;
import sv.gob.mined.siges.dto.SgDocenteCentroEducativo;
import sv.gob.mined.siges.dto.SgRegistroSedeRequest;
import sv.gob.mined.siges.dto.SgRevocacionSedeRequest;
import sv.gob.mined.siges.dto.SgSeccionCentroEducativo;
import sv.gob.mined.siges.dto.SgSeccionIndicadores;
import sv.gob.mined.siges.dto.SgSeccionIndicadoresNota;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroSedes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.SedeBean;
import sv.gob.mined.siges.persistencia.entidades.SgCentroEducativo;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;

@RequestScoped
@Path("/v1/sedes")
@Tag(name = "Sede API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.ELIMINAR_SEDES,
    ConstantesOperaciones.BUSCAR_SEDES})
public class SedeRecurso {

    private static final Logger LOGGER = Logger.getLogger(SedeRecurso.class.getName());

    @Inject
    private SedeBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Inject
    private Tracer tracer;

    @Inject
    private JsonWebToken jwt;

    /**
     * Devuelve el objeto que tiene el id indicado. Inicializa las colecciones.
     *
     * @param entityId
     * @return Response SgSede
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado. Inicializa las colecciones.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSede.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{entityId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SEDES})
    //@Timed
    public Response obtenerPorId(@PathParam("entityId") Long entityId, @QueryParam("dataSecurity") Boolean dataSecurity) {
        try {
            if (dataSecurity == null) {
                dataSecurity = Boolean.TRUE;
            }
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                dataSecurity = Boolean.TRUE;
            }
            SgSede sede = servicio.obtenerPorId(entityId, dataSecurity);
            if (sede == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(sede).build();
            }
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param entityId
     * @return Response SgSede
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSede.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/lazy/{entityId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SEDES})
    //@Timed
    public Response obtenerPorIdLazy(@PathParam("entityId") Long entityId) {
        try {
            SgSede sede = servicio.obtenerPorIdLazy(entityId);
            if (sede == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(sede).build();
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
     * @param filtro
     * @return Response -> List
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSede.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SEDES})
    //@Timed
    public Response buscar(FiltroSedes filtro) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
            }
            if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null && (filtro.getMaxResults()==null || filtro.getMaxResults()>((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue())) {
                filtro.setMaxResults(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
            }
            if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null
                    && (filtro.getIncluirCampos() == null || filtro.getIncluirCampos().length == 0)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_INCLUIR_CAMPOS_REQUERIDO);
                throw be;
            }
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerPorFiltro(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
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
     * @param filtro
     * @return Response -> List
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SEDES})
    //@Timed
    public Response buscarTotal(FiltroSedes filtro) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
            }
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @DELETE
    @Operation(summary = "Elimina el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Eliminado satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad referenciada. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{entityId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_PLANES_ESTUDIO})
    public Response eliminarPorId(@PathParam("entityId") Long id) {
        try {
            if (servicio.objetoExistePorId(id)) {
                servicio.eliminar(id);
                return Response.status(HttpStatus.SC_OK).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_ENTIDAD_REFERENCIADA)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Devuelve historial del elemento con la id indicada ordenado por revision
     *
     * @return Response List
     */
    @GET
    @Operation(summary = "Devuelve historial del elemento con la id indicada ordenado por revision.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = RevHistorico.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/historial/{entityId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("entityId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgSede.class, id)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve el elemento con la id y revision indicada
     *
     * @return Response SgSede
     */
    @GET
    @Operation(summary = "Devuelve elemento con la id y revision indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSede.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/revision/{entityId:[1-9][0-9]*}/{entityRev:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerEnRevision(@PathParam("entityId") Long id, @PathParam("entityRev") Long rev) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerEnRevision(SgSede.class, id, rev)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve el año de la última matrícula de la sede
     *
     * @param entityId
     * @return Response SgSede
     */
    @GET
    @Operation(summary = "Devuelve el año de la última matrícula de la sede.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSede.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/anioultimamatricula/{sedeId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    //@Timed
    public Response obtenerAnioUltimaMatricula(@PathParam("sedeId") Long sedeId) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerAnioUltimaMatricula(sedeId)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve dto con datos generales del centro educativo Consumido por
     * cliente externo a siges
     *
     * @param codigoCentro
     * @return Response SgDatoGeneralCentroEducativo
     */
    @GET
    @Operation(summary = "Devuelve dto con datos generales del centro educativo.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgDatoGeneralCentroEducativo.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/datosgenerales/{codigoCentro}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.OBTENER_DATOS_GENERALES_CENTRO_EDUCATIVO})
    //@Timed
    public Response obtenerDatosGeneralesCentroEducativo(@PathParam("codigoCentro") String codigoCentro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerDatosGeneralesCentroEducativo(codigoCentro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve lista de docentes, directores y subdirectores del centro para el
     * año Consumido por cliente externo a siges
     *
     * @param codigoCentro
     * @param anio
     * @return Response List<SgDocenteCentroEducativo>
     */
    @GET
    @Operation(summary = "Devuelve lista de docentes, directores y subdirectores del centro para el año.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgDocenteCentroEducativo.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/plantadocente/{codigoCentro}/{anio:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.OBTENER_PLANTA_CENTRO_EDUCATIVO})
    //@Timed
    public Response obtenerPlantaCentroEducativo(@PathParam("codigoCentro") String codigoCentro, @PathParam("anio") Integer anio) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerPlantaCentroEducativo(codigoCentro, anio)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve lista de secciones del centro educativo para el año. Consumido
     * por cliente externo a siges
     *
     * @param codigoCentro
     * @param anio
     * @return Response List<SgSeccionCentroEducativo>
     */
    @GET
    @Operation(summary = "Devuelve lista de secciones del centro educativo para el año")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSeccionCentroEducativo.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/secciones/{codigoCentro}/{anio:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.OBTENER_SECCIONES_CENTRO_EDUCATIVO})
    //@Timed
    public Response obtenerSeccionesCentroEducativo(@PathParam("codigoCentro") String codigoCentro, @PathParam("anio") Integer anio) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerSeccionesCentroEducativo(codigoCentro, anio)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve lista de secciones con indicadores. Consumido por cliente externo
     * a siges
     *
     * @param codigoCentro
     * @param fecha
     * @return Response List<SgSeccionIndicadores>
     */
    @GET
    @Operation(summary = "Devuelve lista de secciones del centro educativo con indicadores matrícula, sobreedad, repitencia.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSeccionIndicadores.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/indicadores/{codigoCentro}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.OBTENER_INDICADORES_CENTRO_EDUCATIVO})
    //@Timed
    public Response obtenerIndicadoresCentroEducativo(@PathParam("codigoCentro") String codigoCentro, @PathParam("fecha") LocalDate fecha) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerIndicadoresCentroEducativo(codigoCentro, fecha)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve lista de secciones con indicadores de notas. Consumido
     * por cliente externo a siges
     *
     * @param codigoCentro
     * @param anio
     * @param codigoPeriodo
     * @return Response List<SgSeccionIndicadoresNota>
     */
    @GET
    @Operation(summary = "Devuelve lista de secciones del centro educativo con indicadores de notas")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgSeccionIndicadoresNota.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/indicadoresnota/{codigoCentro}/{anio}/{codigoPeriodo}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.OBTENER_INDICADORES_CENTRO_EDUCATIVO})
    //@Timed
    public Response obtenerIndicadoresNotaCentroEducativo(@PathParam("codigoCentro") String codigoCentro, @PathParam("anio") Integer anio, @PathParam("codigoPeriodo") String codigoPeriodo) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerIndicadoresNotaCentroEducativo(codigoCentro, anio, codigoPeriodo)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    
      
    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgCentroEducativo
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgCentroEducativo.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
        @Path("/obtener/centroeducativo/{sedeId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerTipoOae(@PathParam("sedeId") Long id) {
        try {
            if(id!=null){
                List<SgCentroEducativo> list = servicio.obtenerTipoOaePorSede(id);
                
                if(list!=null && !list.isEmpty()){
                    return Response.status(HttpStatus.SC_OK).entity(list.get(0)).build();
                }
                else{
                    return Response.status(HttpStatus.SC_NOT_FOUND).build();
                }
            }
            else{
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Activar sede
     *
     * @param req SgRevocacionSedeRequest
     * @return Response
     */
    @POST
    @Operation(summary = "Activar sede")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/activar/{entityId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTIVAR_SEDE})
    public Response activar(@PathParam("entityId") Long id) {
        try {
            servicio.activar(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    
    /**
     * Revocar sede
     *
     * @param req SgRevocacionSedeRequest
     * @return Response
     */
    @POST
    @Operation(summary = "Revocar sede")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/revocar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.REVOCAR_SEDE})
    public Response revocar(SgRevocacionSedeRequest req) {
        try {
            servicio.revocar(req);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Cambiar domicilio de sede
     *
     * @param req SgCambioDomicilioSedeRequest
     * @return Response
     */
    @POST
    @Operation(summary = "Cambiar domicilio de sede")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/cambiardomicilio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CAMBIAR_DOMICILIO_SEDE})
    public Response cambiarDomicilio(SgCambioDomicilioSedeRequest req) {
        try {
            servicio.cambiarDomicilio(req);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Cambiar nominación de sede
     *
     * @param req SgCambioNominacionSedeRequest
     * @return Response
     */
    @POST
    @Operation(summary = "Cambiar nominación de sede")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/cambiarnominacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CAMBIAR_NOMINACION_SEDE})
    public Response cambiarNominacion(SgCambioNominacionSedeRequest req) {
        try {
            servicio.cambiarNominacion(req);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Registra nueva sede con servicios educativos
     *
     * @param req SgRegistroSedeRequest
     * @return Response
     */
    @POST
    @Operation(summary = "Registra nueva sede con servicios educativos")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/registro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_OFICIAL,
        ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_PRIVADO,
        ConstantesOperaciones.CREAR_SEDE_CIRCULO_ALFABETIZACION,
    ConstantesOperaciones.CREAR_SEDE_CIRCULO_INFANCIA,
    ConstantesOperaciones.CREAR_EDUCAME,}) 
    public Response registroSede(SgRegistroSedeRequest req) {
        try {
            servicio.registroSede(req);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }
    
    /**
     * Registra nueva sede con servicios educativos
     *
     * @param req SgRegistroSedeRequest
     * @return Response
     */
    @POST
    @Operation(summary = "Registra nueva sede con servicios educativos")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/cerrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CERRAR_SEDE}) 
    public Response cerrarSede(SgSede sed) {
        try {
            servicio.cerrarSede(sed);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                LOGGER.log(Level.SEVERE, jwt.getSubject(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }



}
