/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.InfTratamientoAguaBean;
import sv.gob.mined.siges.persistencia.entidades.SgInfTratamientoAgua;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;

/**
 *
 * @author Sofis Solutions
 */
@RequestScoped
@Path("/v1/tratamientoagua")
@Tag(name = "Tratamiento Agua API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_TRATAMIENTO_AGUA,
    ConstantesOperaciones.ACTUALIZAR_TRATAMIENTO_AGUA,
    ConstantesOperaciones.ELIMINAR_TRATAMIENTO_AGUA})
public class InfTratamientoAguaRecurso {

    private static final Logger LOGGER = Logger.getLogger(InfTratamientoAguaRecurso.class.getName());

    @Inject
    private InfTratamientoAguaBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;
	
    @Context
    private Providers providers;	

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgInfTratamientoAgua
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description  = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInfTratamientoAgua.class)))
        ,
        @APIResponse(responseCode = "400", description  = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description  = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description  = "Error interno del servidor.")}
    )
    @Path("/{infTratamientoAguaId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerPorId(@PathParam("infTratamientoAguaId") Long id) {
        try {
            SgInfTratamientoAgua tra = servicio.obtenerPorId(id);
            if (tra == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(tra).build();
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
     * @return Response Lista de SgInfTratamientoAgua
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInfTratamientoAgua.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response buscar(FiltroCodiguera filtro) {
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
     * @return Response Lista de SgInfTratamientoAgua
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
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response buscarTotal(FiltroCodiguera filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Crea el objeto indicado con id autogenerada
     *
     * @param tra SgInfTratamientoAgua
     * @return Response  SgInfTratamientoAgua
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInfTratamientoAgua.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_TRATAMIENTO_AGUA})
    public Response crear(SgInfTratamientoAgua tra) {
        try {
            tra.setTraPk(null);
            tra.setTraVersion(0);
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(tra)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
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
     * @param tra SgInfTratamientoAgua
     * @return Response SgInfTratamientoAgua
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInfTratamientoAgua.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{infTratamientoAguaId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_TRATAMIENTO_AGUA})
    public Response actualizar(@PathParam("infTratamientoAguaId") Long id, SgInfTratamientoAgua tra) {
        try {
            if (servicio.objetoExistePorId(id)) {
                tra.setTraPk(id);
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(tra)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)){
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
    @Path("/{infTratamientoAguaId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_TRATAMIENTO_AGUA})
    public Response eliminarPorId(@PathParam("infTratamientoAguaId") Long id) {
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
     * @return Response Lista de SgInfTratamientoAgua
     */
    @GET
    @Operation(summary = "Devuelve historial del elemento con la id indicada ordenado por version.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInfTratamientoAgua.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/historial/{infTratamientoAguaId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("infTratamientoAguaId") Long id) {
        try {
	    
		ObjectMapper mapper = providers.getContextResolver(ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE).getContext(ConsultaHistoricoBean.class);
            	return Response.status(HttpStatus.SC_OK).entity(mapper.writeValueAsString(historico.obtenerHistorialPorId(SgInfTratamientoAgua.class, id))).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
	
	/**
     * Devuelve el elemento con la id y revision indicada
     *
     * @return Response SgInfTratamientoAgua
     */
    @GET
    @Operation(summary = "Devuelve elemento con la id y revision indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInfTratamientoAgua.class)))
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
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerEnRevision(SgInfTratamientoAgua.class, id, rev)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

}
