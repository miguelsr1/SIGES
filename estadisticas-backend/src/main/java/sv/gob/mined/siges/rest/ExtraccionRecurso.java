/*
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Providers;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.ExtraccionBean;
import sv.gob.mined.siges.persistencia.entidades.SgExtraccion;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.RevHistorico;
import sv.gob.mined.siges.dto.SgDuplicarExtraccion;
import sv.gob.mined.siges.filtros.FiltroExtraccion;
import sv.gob.mined.siges.persistencia.entidades.SgAlcanceExtraccion;
import sv.gob.mined.siges.persistencia.entidades.SgGradoReportaEn;

/**
 *
 * @author Sofis Solutions
 */
@RequestScoped
@Path("/v1/extracciones")
@Tag(name = "Extracciones API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_EXTRACCIONES,
    ConstantesOperaciones.ACTUALIZAR_EXTRACCIONES,
    ConstantesOperaciones.ELIMINAR_EXTRACCIONES,
    ConstantesOperaciones.PUEDE_EXTRAER_FORMA_MANUAL})
public class ExtraccionRecurso {

    private static final Logger LOGGER = Logger.getLogger(ExtraccionRecurso.class.getName());

    @Inject
    private ExtraccionBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Context
    private Providers providers;

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgExtraccion
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{extraccionId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerPorId(@PathParam("extraccionId") Long id) {
        try {
            SgExtraccion ese = servicio.obtenerPorId(id);
            if (ese == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(ese).build();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgExtraccion
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response buscar(FiltroExtraccion filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerPorFiltro(filtro)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgExtraccion
     */
    @POST
    @Operation(summary = "Devuelve la cantidad total de elementos que satisfacen el criterio")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Long.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar/total")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response buscarTotal(FiltroExtraccion filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Duplica la extracción para otro año
     *
     * @param dup SgDuplicarExtraccion
     * @return Response SgExtraccion
     */
    @POST
    @Operation(summary = "Duplica la extracción para otro año.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_EXTRACCIONES})
    @Path("/duplicar")
    public Response duplicarExtraccionParaOtroAnio(SgDuplicarExtraccion dup) {
        try {
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.duplicarExtraccionParaOtroAnio(dup)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_EXTRACCION_DUPLICADA)).build();
            } else {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Crea el objeto indicado con id autogenerada
     *
     * @param ese SgExtraccion
     * @return Response SgExtraccion
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_EXTRACCIONES})
    public Response crear(SgExtraccion ese) {
        try {
            ese.setExtPk(null);
            ese.setExtVersion(0);
            if (ese.getExtAlcance() != null) {
                for (SgAlcanceExtraccion a : ese.getExtAlcance()) {
                    a.setAlcExtraccion(ese);
                }
            }
            if (ese.getExtGradoReportaEn() != null) {
                for (SgGradoReportaEn gre : ese.getExtGradoReportaEn()) {
                    gre.setRepExtraccion(ese);
                }
            }
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(ese)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_EXTRACCION_DUPLICADA)).build();
            } else {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Actualiza el objeto con la id indicada. Si no existe, lo crea con id
     * autogenerada.
     *
     * @param id Long
     * @param ese SgExtraccion
     * @return Response SgExtraccion
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class))),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{extraccionId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_EXTRACCIONES})
    public Response actualizar(@PathParam("extraccionId") Long id, SgExtraccion ese) {
        try {
            if (servicio.objetoExistePorId(id)) {
                ese.setExtPk(id);
                if (ese.getExtAlcance() != null) {
                    for (SgAlcanceExtraccion a : ese.getExtAlcance()) {
                        a.setAlcExtraccion(ese);
                    }
                }
                if (ese.getExtGradoReportaEn() != null) {
                    for (SgGradoReportaEn gre : ese.getExtGradoReportaEn()) {
                        gre.setRepExtraccion(ese);
                    }
                }
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(ese)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
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
        @APIResponse(responseCode = "200", description = "Eliminado satisfactorio."),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "422", description = "Entidad referenciada. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{extraccionId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_EXTRACCIONES})
    public Response eliminarPorId(@PathParam("extraccionId") Long id) {
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
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Devuelve historial del elemento con la id indicada ordenado por version
     *
     * @param id Long
     * @return Response Lista de SgExtraccion
     */
    @GET
    @Operation(summary = "Devuelve historial del elemento con la id indicada ordenado por version.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = RevHistorico.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/historial/{extraccionId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("extraccionId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgExtraccion.class, id)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve el elemento con la id y revision indicada
     *
     * @return Response SgExtraccion
     */
    @GET
    @Operation(summary = "Devuelve elemento con la id y revision indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/revision/{entityId:[1-9][0-9]*}/{entityRev:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerEnRevision(@PathParam("entityId") Long id, @PathParam("entityRev") Long rev) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerEnRevision(SgExtraccion.class, id, rev)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Procesa extracciones pendientes
     *
     * @param id Long
     * @return Response SgExtraccion
     */
    @GET
    @Operation(summary = "Procesa extracciones pendientes.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgExtraccion.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/procesarextraccionespendientes")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO, ConstantesOperaciones.PUEDE_EXTRAER_FORMA_MANUAL})
    public Response procesarExtraccionesPendientes() {
        try {
            servicio.procesarExtraccionesPendientes();
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
}
