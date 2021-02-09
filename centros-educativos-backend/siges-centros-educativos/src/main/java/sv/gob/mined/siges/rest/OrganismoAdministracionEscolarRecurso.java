/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import io.opentracing.Tracer;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonNumber;
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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.OrganismoAdministracionEscolarBean;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgOAEyMiembros;
import sv.gob.mined.siges.filtros.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgRelOAEIdentificacionOAE;

/**
 *
 * @author Sofis Solutions
 */
@RequestScoped
@Path("/v1/organismoadministracionescolar")
@Tag(name = "Organismos de Administración Escolar API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_ORGANISMO_ADMINISTRACION_ESCOLAR,
    ConstantesOperaciones.ACTUALIZAR_ORGANISMO_ADMINISTRACION_ESCOLAR,
    ConstantesOperaciones.ELIMINAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
public class OrganismoAdministracionEscolarRecurso {

    private static final Logger LOGGER = Logger.getLogger(OrganismoAdministracionEscolarRecurso.class.getName());

    @Inject
    private OrganismoAdministracionEscolarBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Inject
    private Tracer tracer;

    @Inject
    private JsonWebToken jwt;

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgOrganismoAdministracionEscolar
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{organismoAdministracionEscolarId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response obtenerPorId(@PathParam("organismoAdministracionEscolarId") Long id) {
        try {
            SgOrganismoAdministracionEscolar oae = servicio.obtenerPorId(id);
            if (oae == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(oae).build();
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
     * @param filtro FiltroOrganismoAdministrativoEscolar
     * @return Response Lista de SgOrganismoAdministracionEscolar
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response buscar(FiltroOrganismoAdministrativoEscolar filtro) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR);
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
     * @return Response Lista de SgOrganismoAdministracionEscolar
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response buscarTotal(FiltroOrganismoAdministrativoEscolar filtro) {
        try {
            if (!jwt.getAudience().contains(Constantes.JWT_AUDIENCE_SIGES)) {
                filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR);
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
     * @param oae SgOrganismoAdministracionEscolar
     * @return Response SgOrganismoAdministracionEscolar
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response crear(SgOrganismoAdministracionEscolar oae) {
        try {
            oae.setOaePk(null);
            oae.setOaeVersion(0);
            if (oae.getOaeIdentificaciones() != null && !oae.getOaeIdentificaciones().isEmpty()) {
                for (SgRelOAEIdentificacionOAE id : oae.getOaeIdentificaciones()) {
                    id.setRioOrganismoAdministracionEscolarFk(oae);
                }
            }
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(oae)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    
    /**
     * Crea el objeto indicado con id autogenerada
     *
     * @param oaeMiembros
     * @param oae SgOrganismoAdministracionEscolar
     * @return Response SgOrganismoAdministracionEscolar
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/guardarRenovarMiembro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.RENOVAR_MIEMBROS})
    public Response guardarRenovarMiembro(SgOAEyMiembros oaeMiembros) {
        try {
            oaeMiembros.getOrganismo().setOaePk(null);
            oaeMiembros.getOrganismo().setOaeVersion(0);
            if (oaeMiembros.getOrganismo().getOaeIdentificaciones() != null && !oaeMiembros.getOrganismo().getOaeIdentificaciones().isEmpty()) {
                for (SgRelOAEIdentificacionOAE id : oaeMiembros.getOrganismo().getOaeIdentificaciones()) {
                    id.setRioOrganismoAdministracionEscolarFk(oaeMiembros.getOrganismo());
                }
            }
            if(oaeMiembros.getListMiembros()!=null && !oaeMiembros.getListMiembros().isEmpty()){             
         
                oaeMiembros.getListMiembros().forEach(c->{c.setPoaPk(null);c.setPoaVersion(0);});
            }
            
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardarRenovarMiembro(oaeMiembros)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
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
     * @param oae SgOrganismoAdministracionEscolar
     * @return Response SgOrganismoAdministracionEscolar
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class))),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{organismoAdministracionEscolarId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response actualizar(@PathParam("organismoAdministracionEscolarId") Long id, SgOrganismoAdministracionEscolar oae) {
        try {
            if (servicio.objetoExistePorId(id)) {
                oae.setOaePk(id);
                if (oae.getOaeIdentificaciones() != null && !oae.getOaeIdentificaciones().isEmpty()) {
                    for (SgRelOAEIdentificacionOAE idoae : oae.getOaeIdentificaciones()) {
                        idoae.setRioOrganismoAdministracionEscolarFk(oae);
                    }
                }
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(oae)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    /**
     * Retira la matricula con la id indicada.
     *
     * @param id Long
     * @param entity SgMatricula
     * @return Response SgMatricula
     */
    @POST
    @Operation(summary = "Retira la matricula con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio."),
        @APIResponse(responseCode = "404", description = "Objeto no encontrado."),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/enviar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response enviar(Long id) {
        try {
            if (servicio.objetoExistePorId(id)) {
                servicio.enviar(id);
                return Response.status(HttpStatus.SC_OK).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
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
    @Path("/{organismoAdministracionEscolarId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_ORGANISMO_ADMINISTRACION_ESCOLAR})
    public Response eliminarPorId(@PathParam("organismoAdministracionEscolarId") Long id) {
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
     * Devuelve historial del elemento con la id indicada ordenado por version
     *
     * @param id Long
     * @return Response Lista de SgOrganismoAdministracionEscolar
     */
    @GET
    @Operation(summary = "Devuelve historial del elemento con la id indicada ordenado por version.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/historial/{organismoAdministracionEscolarId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("organismoAdministracionEscolarId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgOrganismoAdministracionEscolar.class, id)).build();
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
     * @return Response SgOrganismoAdministracionEscolar
     */
    @GET
    @Operation(summary = "Devuelve elemento con la id y revision indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgOrganismoAdministracionEscolar.class))),
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta."),
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/revision/{entityId:[1-9][0-9]*}/{entityRev:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerEnRevision(@PathParam("entityId") Long id, @PathParam("entityRev") Long rev) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerEnRevision(SgOrganismoAdministracionEscolar.class,id, rev)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

}
