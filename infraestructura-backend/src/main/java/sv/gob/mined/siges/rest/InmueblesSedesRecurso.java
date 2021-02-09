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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.filtros.FiltroInmueblesSedes;
import sv.gob.mined.siges.negocio.servicios.InmueblesSedesBean;
import sv.gob.mined.siges.negocio.validaciones.InmueblesSedesValidacionNegocio;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteEspacio;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteObraExterior;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteOtroServicio;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedesLiteServicio;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesVulnerabilidades;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAccesibilidad;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAlmacenamientoAgua;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleEspacioComun;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleItemObraExterior;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleManejoDesechos;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleServicioBasico;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleServicioSanitario;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleTipoDrenaje;

@RequestScoped
@Path("/v1/inmueblesede")
@Tag(name = "Inmuebles de la Sede API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_INMUEBLE_O_TERRENO,
    ConstantesOperaciones.ACTUALIZAR_INMUEBLE_O_TERRENO,
    ConstantesOperaciones.ELIMINAR_INMUEBLE_O_TERRENO,
    ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO})
public class InmueblesSedesRecurso {
    private static final Logger LOGGER = Logger.getLogger(InmueblesSedesRecurso.class.getName());

    @Inject
    private InmueblesSedesBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Context
    private Providers providers;
    
    @Inject
    private JsonWebToken jwt;

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgIdiomaRealizado
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{inmuebleId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO})
    public Response obtenerPorId(@PathParam("inmuebleId") Long id) {
        try {
            SgInmueblesSedes entity = servicio.obtenerPorId(id);
            if (entity == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(entity).build();
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
     * @return Response Lista de SgInmueblesSedes
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO})
    public Response buscar(FiltroInmueblesSedes filtro) {
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
     * @return Response Lista de SgInmueblesSedes
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO})
    public Response buscarTotal(FiltroInmueblesSedes filtro) {
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
     * @param entity SgInmueblesSedes
     * @return Response SgInmueblesSedes
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_INMUEBLE_O_TERRENO})
    public Response crear(SgInmueblesSedes entity) {
        try {
            entity.setTisPk(null);
            entity.setTisVersion(0);
            if (entity.getIvuInmueblesSede()!= null) {
                for (SgInmueblesVulnerabilidades vulnerabilidad : entity.getIvuInmueblesSede()) {
                    vulnerabilidad.setIvuInmueblesSedesFK(entity);
                }
            }
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(entity)).build();
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
     * @param entity SgInmueblesSedes
     * @return Response SgInmueblesSedes
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{inmuebleId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_INMUEBLE_O_TERRENO})
    public Response actualizar(@PathParam("inmuebleId") Long id, SgInmueblesSedes entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setTisPk(id);
                if (entity.getIvuInmueblesSede()!= null) {
                    for (SgInmueblesVulnerabilidades vulnerabilidad : entity.getIvuInmueblesSede()) {
                        vulnerabilidad.setIvuInmueblesSedesFK(entity);
                    }
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
    @Path("/{inmuebleId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_INMUEBLE_O_TERRENO})
    public Response eliminarPorId(@PathParam("inmuebleId") Long id) {
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
     * @return Response Lista de SgInmueblesSedes
     */
    @GET
    @Operation(summary = "Devuelve historial del elemento con la id indicada ordenado por revision.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/historial/{inmuebleId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("inmuebleId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgInmueblesSedes.class, id)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve el elemento con la id y revision indicada
     *
     * @return Response SgInmueblesSedes
     */
    @GET
    @Operation(summary = "Devuelve elemento con la id y revision indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
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
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerEnRevision(SgInmueblesSedes.class, id, rev)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Valida el objeto indicado
     *
     * @param entity SgInmueblesSedes
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
    public Response validar(SgInmueblesSedes entity) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(InmueblesSedesValidacionNegocio.validar(entity)).build();
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
     * @param entity SgInmueblesSedes
     * @return Response SgInmueblesSedes
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("actualizarListaEspacio/{inmuebleId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_INMUEBLE_O_TERRENO})
    public Response actualizarListaRelEspacioComun(@PathParam("inmuebleId") Long id, SgInmueblesSedesLiteEspacio entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setTisPk(id);
                if (entity.getTisRelInmuebleEspacioComun()!= null) {
                    for (SgRelInmuebleEspacioComun espacioComun : entity.getTisRelInmuebleEspacioComun()) {
                        espacioComun.setRieInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));
                      
                    }
                }
                SgInmueblesSedesLiteEspacio resp=servicio.guardarLiteEspacio(entity);
                resp.setTisRelInmuebleEspacioComun(null);
                return Response.status(HttpStatus.SC_OK).entity(resp).build();
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
     * @param entity SgInmueblesSedes
     * @return Response SgInmueblesSedes
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("actualizarListaServicio/{inmuebleId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_INMUEBLE_O_TERRENO})
    public Response actualizarListaRelServicio(@PathParam("inmuebleId") Long id, SgInmueblesSedesLiteServicio entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setTisPk(id);
                 if (entity.getTisServicioSanitario()!= null) {
                    for (SgRelInmuebleServicioSanitario espacioComun : entity.getTisServicioSanitario()) {
                        espacioComun.setRitInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                }
                 if(entity.getTisAccesibilidad()!=null){
                     for (SgRelInmuebleAccesibilidad espacioComun : entity.getTisAccesibilidad()) {
                        espacioComun.setIacInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                 }
                 if(entity.getTisAbastecimientoAgua()!=null){
                     for (SgRelInmuebleAbastecimientoAgua espacioComun : entity.getTisAbastecimientoAgua()) {
                        espacioComun.setIaaInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                 }
                 if(entity.getTisAlmacenamientoAgua()!=null){
                     for (SgRelInmuebleAlmacenamientoAgua espacioComun : entity.getTisAlmacenamientoAgua()) {
                        espacioComun.setIalInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                 }
                 if(entity.getTisTipoDrenaje()!=null){
                     for (SgRelInmuebleTipoDrenaje espacioComun : entity.getTisTipoDrenaje()) {
                        espacioComun.setItdInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                 }
                 if(entity.getTisInmuebleManejoDesechos()!=null){
                     for (SgRelInmuebleManejoDesechos espacioComun : entity.getTisInmuebleManejoDesechos()) {
                        espacioComun.setImdInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                     
                 }
                SgInmueblesSedesLiteServicio resp=servicio.guardarLiteServicio(entity);
                resp.setTisServicioSanitario(null);
                resp.setTisAccesibilidad(null);
                resp.setTisAbastecimientoAgua(null);
                resp.setTisAlmacenamientoAgua(null);
                resp.setTisTipoDrenaje(null);
                resp.setTisInmuebleManejoDesechos(null);
                return Response.status(HttpStatus.SC_OK).entity(resp).build();
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
     * @param entity SgInmueblesSedes
     * @return Response SgInmueblesSedes
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("actualizarListaOtroServicio/{inmuebleId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_INMUEBLE_O_TERRENO})
    public Response actualizarListaRelOtroServicio(@PathParam("inmuebleId") Long id, SgInmueblesSedesLiteOtroServicio entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setTisPk(id);
                if (entity.getTisServicioBasico()!= null) {
                    for (SgRelInmuebleServicioBasico espacioComun : entity.getTisServicioBasico()) {
                        espacioComun.setRisInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                }
                
                SgInmueblesSedesLiteOtroServicio resp=servicio.guardarLiteOtroServicio(entity);
                resp.setTisServicioBasico(null);
                return Response.status(HttpStatus.SC_OK).entity(resp).build();
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
     * @param entity SgInmueblesSedes
     * @return Response SgInmueblesSedes
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgInmueblesSedes.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("actualizarListaRelObraExterior/{inmuebleId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_INMUEBLE_O_TERRENO})
    public Response actualizarListaReObraExterior(@PathParam("inmuebleId") Long id, SgInmueblesSedesLiteObraExterior entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setTisPk(id);
                if (entity.getTisObraExterior()!= null) {
                    for (SgRelInmuebleItemObraExterior espacioComun : entity.getTisObraExterior()) {
                        espacioComun.setRixInmuebleSede(new SgInmueblesSedes(entity.getTisPk(), entity.getTisVersion()));                       
                    }
                }
                
                SgInmueblesSedesLiteObraExterior resp=servicio.guardarLiteObraExterior(entity);
                resp.setTisObraExterior(null);
                return Response.status(HttpStatus.SC_OK).entity(resp).build();
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
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

}
