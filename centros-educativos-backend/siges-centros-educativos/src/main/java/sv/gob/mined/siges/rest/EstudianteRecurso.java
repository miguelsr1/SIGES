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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import java.util.List;
import javax.json.JsonNumber;
import org.eclipse.microprofile.jwt.JsonWebToken;
import javax.ws.rs.QueryParam;
import org.eclipse.microprofile.config.inject.ConfigProperty;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.EstudianteBean;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgEstudianteServicioSocial;
import sv.gob.mined.siges.dto.SgPrediccionDesercionResponse;
import sv.gob.mined.siges.dto.SgRegistroFichaEstudiante;
import sv.gob.mined.siges.dto.SgUnirEstudiantes;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.negocio.servicios.ConfiguracionBean;
import sv.gob.mined.siges.negocio.validaciones.EstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.SgTelefono;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;

@RequestScoped
@Path("/v1/estudiantes")
@Tag(name = "Estudiante API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_ESTUDIANTE,
    ConstantesOperaciones.ACTUALIZAR_ESTUDIANTE,
    ConstantesOperaciones.ELIMINAR_ESTUDIANTE,
    ConstantesOperaciones.BUSCAR_ESTUDIANTES})
public class EstudianteRecurso {

    private static final Logger LOGGER = Logger.getLogger(EstudianteRecurso.class.getName());

    @Inject
    private EstudianteBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    private Tracer tracer;

    @Inject
    private JsonWebToken jwt;

    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgEstudiante
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgEstudiante.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{estudianteId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ESTUDIANTES})
    //@Timed
    public Response obtenerPorId(@PathParam("estudianteId") Long id, @QueryParam("dataSecurity") Boolean dataSecurity) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                dataSecurity = Boolean.TRUE;
            }
            SgEstudiante entity = servicio.obtenerPorId(id, dataSecurity);
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
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgEstudiante
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgEstudiante.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ESTUDIANTES})
    //@Timed
    public Response buscar(FiltroEstudiante filtro) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_ESTUDIANTES);
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
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgEstudiante
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ESTUDIANTES})
    //@Timed
    public Response buscarTotal(FiltroEstudiante filtro) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_ESTUDIANTES);
            }
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Crea el objeto indicado con id autogenerada
     *
     * @param entity SgEstudiante
     * @return Response SgEstudiante
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgEstudiante.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_ESTUDIANTE})
    public Response crear(SgEstudiante entity) {
        try {
            entity.setEstPk(null);
            entity.setEstVersion(0);
            if (entity.getEstPersona().getPerTelefonos() != null) {
                for (SgTelefono tel : entity.getEstPersona().getPerTelefonos()) {
                    tel.setTelPersona(entity.getEstPersona());
                }
            }
            if (entity.getEstPersona().getPerIdentificaciones() != null) {
                for (SgIdentificacion ide : entity.getEstPersona().getPerIdentificaciones()) {
                    ide.setIdePersona(entity.getEstPersona());
                }
            }
            if (entity.getEstPersona().getPerAllegados() != null) {
                for (SgAllegado fam : entity.getEstPersona().getPerAllegados()) {
                    fam.setAllPersonaReferenciada(entity.getEstPersona());
                }
            }
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(entity, Boolean.TRUE)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getEstudianteViolationBusinessException(ex)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Crea la ficha del estudiante y su escolaridad asociada
     *
     * @param entity SgRegistroFichaEstudiante
     * @return Response
     */
    @POST
    @Operation(summary = "Crea la ficha del estudiante y su escolaridad asociada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registrofichaescolaridad")
    @RolesAllowed({ConstantesOperaciones.CREAR_ESTUDIANTE})
    public Response generarRegistroFichaEscolaridadEstudiante(SgRegistroFichaEstudiante entity) {
        try {
            servicio.generarRegistroFichaEscolaridadEstudiante(entity);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(PersistenceHelper.getEstudianteViolationBusinessException(ex).getErrores())).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Actualiza el objeto con la id indicada. Si no existe, lo crea con id
     * autogenerada.
     *
     * @param id Long
     * @param entity SgEstudiante
     * @return Response SgEstudiante
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgEstudiante.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{estudianteId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_ESTUDIANTE})
    //@Timed
    public Response actualizar(@PathParam("estudianteId") Long id, SgEstudiante entity) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
                entity.setEstPk(id);
                if (entity.getEstPersona().getPerTelefonos() != null) {
                    for (SgTelefono tel : entity.getEstPersona().getPerTelefonos()) {
                        tel.setTelPersona(entity.getEstPersona());
                    }
                }
                if (entity.getEstPersona().getPerIdentificaciones() != null) {
                    for (SgIdentificacion ide : entity.getEstPersona().getPerIdentificaciones()) {
                        ide.setIdePersona(entity.getEstPersona());
                    }
                }
                if (entity.getEstPersona().getPerAllegados() != null) {
                    for (SgAllegado fam : entity.getEstPersona().getPerAllegados()) {
                        fam.setAllPersonaReferenciada(entity.getEstPersona());
                    }
                }
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(entity, Boolean.TRUE)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getEstudianteViolationBusinessException(ex)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Actualiza el servicio social de los estudiantes recibidos
     *
     * @param List<entity> SgEstudiante
     * @return Response
     */
    @PUT
    @Operation(summary = "Actualiza el servicio social de los estudiantes recibidos.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgEstudiante.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/actualizarServicioSocial")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.EDITAR_SERVICIO_SOCIAL})
    //@Timed
    public Response actualizarServicioSocial(List<SgEstudianteServicioSocial> estudiantes) {
        try {
            servicio.actualizarServicioSocial(estudiantes);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getEstudianteViolationBusinessException(ex)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Valida el objeto indicado
     *
     * @param entity SgEstudiante
     * @return Response Boolean
     */
    @POST
    @Operation(summary = "Valida el objeto indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Entidad validada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Boolean.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/validar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response validar(SgEstudiante entity) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(EstudianteValidacionNegocio.validar(entity, emailPattern, configuracionBean)).build();
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
     * Copia estudiante origen a destino. Datos de origen tienen precedencia
     * sobre destino. Elimina estudiante origen
     *
     * @param SgUnirEstudiantes
     * @throws GeneralException
     */
    @POST
    @Operation(summary = "Copia estudiante origen a destino.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Entidad validada satisfactoriamente.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/unir")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.UNIR_ESTUDIANTES})
    public Response unirEstudiantes(SgUnirEstudiantes dto) throws GeneralException {
        try {
            servicio.unirEstudiantes(dto.getEstudianteOrigenPk(), dto.getEstudianteDestinoPk());
            return Response.status(HttpStatus.SC_OK).build();
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @return Response
     */
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
    @Path("/{estudianteId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_ESTUDIANTE})
    public Response eliminarPorId(@PathParam("estudianteId") Long id) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
                servicio.eliminar(id);
                return Response.status(HttpStatus.SC_OK).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
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
     * Devuelve historial del elemento con la id indicada ordenado por version
     *
     * @param id Long
     * @return Response Lista de SgEstudiante
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
    @Path("/historial/{estudianteId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("estudianteId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgEstudiante.class, id)).build();
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
     * @return Response SgEstudiante
     */
    @GET
    @Operation(summary = "Devuelve elemento con la id y revision indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgEstudiante.class)))
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
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerEnRevision(id, rev)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
     /**
     * Devuelve probabilidad de deserción del estudiante
     *
     * @return Response SgPrediccionDesercionResponse
     */
    @GET
    @Operation(summary = "Devuelve probabilidad de deserción del estudiante.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgPrediccionDesercionResponse.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/predicciondesercion/{entityId:[1-9][0-9]*}/")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response prediccionDesercion(@PathParam("entityId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.prediccionDesercion(id)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    


}
