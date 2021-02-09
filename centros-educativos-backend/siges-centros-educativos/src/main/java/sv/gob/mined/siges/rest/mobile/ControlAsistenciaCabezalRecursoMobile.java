/**
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
import javax.ws.rs.GET;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ControlAsistenciaCabezalBean;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumProcesoCreacion;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;

@RequestScoped
@Path("/m/v1/controlasistenciacabezal")
@Tag(name = "ControlAsistenciaCabezal M API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_CABEZAL,
    ConstantesOperaciones.ACTUALIZAR_CONTROL_ASISTENCIA_CABEZAL,
    ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL})
public class ControlAsistenciaCabezalRecursoMobile {

    private static final Logger LOGGER = Logger.getLogger(ControlAsistenciaCabezalRecursoMobile.class.getName());

    @Inject
    private ControlAsistenciaCabezalBean servicio;
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
     * @param controlAsistenciaCabezalId
     * @return Response -> SgControlAsistenciaCabezal
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgControlAsistenciaCabezal.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{controlAsistenciaCabezalId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL})
    //@Timed
    public Response obtenerPorId(@PathParam("controlAsistenciaCabezalId") Long controlAsistenciaCabezalId) {
        try {
            SgControlAsistenciaCabezal entity = servicio.obtenerPorId(controlAsistenciaCabezalId);
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
     * @param filtro
     * @return Response -> List
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgControlAsistenciaCabezal.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL})
    //@Timed
    public Response buscar(FiltroControlAsistenciaCabezal filtro) {
        try {
            if (filtro.getMaxResults() == null || filtro.getMaxResults() > allowedMaxResults) {
                filtro.setMaxResults(allowedMaxResults);
            }
            Boolean validarIncluirCampos = Boolean.TRUE; 
            if (filtro.getCacPk() != null){
                validarIncluirCampos = Boolean.FALSE;
            }
            if (filtro.getCacFecha() != null && filtro.getSecPk() != null){
                validarIncluirCampos = Boolean.FALSE;
            }

            if (validarIncluirCampos && (filtro.getIncluirCampos() == null || filtro.getIncluirCampos().length < 1)){
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL})
    //@Timed
    public Response buscarTotal(FiltroControlAsistenciaCabezal filtro) {
        try {
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
     * @param entity
     * @return Response -> SgControlAsistenciaCabezal
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgControlAsistenciaCabezal.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_CABEZAL})
    //@Timed
    public Response crear(SgControlAsistenciaCabezal entity) {
        try {
            entity.setCacPk(null);
            entity.setCacVersion(0);
            entity.setCacProcesoDeCreacion(EnumProcesoCreacion.MOV);
            for (SgAsistencia a : entity.getCacAsistencia()) {
                a.setAsiControl(entity);
            }
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(entity)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getControlAsistenciaViolationBusinessException(ex)).build();
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
     * @param id
     * @param entity
     * @return Response -> SgControlAsistenciaCabezal
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgControlAsistenciaCabezal.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{controlAsistenciaCabezalId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_CONTROL_ASISTENCIA_CABEZAL})
    //@Timed
    public Response actualizar(@PathParam("controlAsistenciaCabezalId") Long id, SgControlAsistenciaCabezal entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setCacPk(id);
                entity.setCacProcesoDeCreacion(EnumProcesoCreacion.MOV);
                for (SgAsistencia a : entity.getCacAsistencia()) {
                    a.setAsiControl(entity);
                }
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(entity)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getControlAsistenciaViolationBusinessException(ex)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }


}
