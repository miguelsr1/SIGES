/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import io.opentracing.Tracer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.enumerados.EnumOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.BienesDepreciablesBean;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.DepreciacionBean;
import sv.gob.mined.siges.negocio.servicios.DepreciacionMaestroBean;
import sv.gob.mined.siges.negocio.validaciones.DepreciacionMaestroValidacionMaestro;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;

@RequestScoped
@Path("/v1/maestrodepreciacion")
@Tag(name = "Maestro de Depreciacion de bienes API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_DEPRECIACION_MAESTRO,
    ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_MAESTRO,
    ConstantesOperaciones.ELIMINAR_DEPRECIACION_MAESTRO,
    ConstantesOperaciones.BUSCAR_DEPRECIACION_MAESTRO
})
public class DepreciacionMaestroRecurso {
    private static final Logger LOGGER = Logger.getLogger(DepreciacionMaestroRecurso.class.getName());

    @Inject
    private DepreciacionMaestroBean servicio;

    @Inject
    private DepreciacionBean depreciacionBean;
    
    @Inject
    private ConsultaHistoricoBean historico;

    @Inject
    private Tracer tracer;
    
    @Inject
    @ConfigProperty(name = "anio-limite-menor-admitido", defaultValue = "1960")
    private Integer anioLimiteMenorAdmitido;
    
    @Inject
    @ConfigProperty(name = "batch-size", defaultValue = "100")
    private Long batchSize;
    
    @Inject
    private BienesDepreciablesBean bienes;
    
    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgAfDepreciacionMaestro
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfDepreciacionMaestro.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{maestrodepId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_DEPRECIACION_MAESTRO})
    public Response obtenerPorId(@PathParam("maestrodepId") Long id, @QueryParam("dataSecurity") Boolean dataSecurity) {
        try {
            SgAfDepreciacionMaestro entity = servicio.obtenerPorId(id, dataSecurity);
            if (entity == null) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(entity).build();
            }
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Response Lista de SgAfDepreciacionMaestro
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfDepreciacionMaestro.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_DEPRECIACION_MAESTRO})
    public Response buscar(FiltroBienesDepreciables filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Response Lista de SgAfDepreciacionMaestro
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_DEPRECIACION_MAESTRO})
    public Response buscarTotal(FiltroBienesDepreciables filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Crea el objeto indicado con id autogenerada
     *
     * @param entity SgAfDepreciacionMaestro
     * @return Response SgAfDepreciacionMaestro
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfDepreciacionMaestro.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_DEPRECIACION_MAESTRO})
    public Response crear(SgAfDepreciacionMaestro entity) {
        try {
            entity.setDmaPk(null);
            entity.setDmaVersion(0);
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(entity, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }
    
    
    /**
     * Actualiza el objeto con la id indicada. Si no existe, lo crea con id
     * autogenerada.
     *
     * @param id Long
     * @param entity SgAfDescargo
     * @return Response SgAfDescargo
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfDepreciacionMaestro.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{maestrodepId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_MAESTRO})
    public Response actualizar(@PathParam("maestrodepId") Long id, SgAfDepreciacionMaestro entity) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
                entity.setDmaPk(id);
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(entity, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
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
    @Path("/{maestrodepId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_DEPRECIACION_MAESTRO})
    public Response eliminarPorId(@PathParam("maestrodepId") Long id) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
                servicio.eliminar(id);
                return Response.status(HttpStatus.SC_OK).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        }   catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        }   catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_ENTIDAD_REFERENCIADA)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }
    
    /**
     * Devuelve historial del elemento con la id indicada ordenado por version
     *
     * @param id Long
     * @return Response Lista de SgAfDepreciacionMaestro
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
    @Path("/historial/{maestrodepId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("maestrodepId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgAfDepreciacionMaestro.class, id)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Valida el objeto indicado
     *
     * @param entity SgAfDepreciacionMaestro
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
    @Path("/validarDuplicado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response validar(SgAfDepreciacionMaestro entity) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.validarExistentes(entity)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    
    /**
     * Procesa una lista de tareas
     * @return 
     */
    @POST
    @Operation(summary = "Procesa las tareas pendientes.")
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/procesartareas")
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_MAESTRO, ConstantesOperaciones.CREAR_DEPRECIACION_BIEN, ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_BIEN})
    public Response procesarTareas() {
        try {
            procesarListaTareasDepreciacion(null);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }
    
    /**
     * Procesa un tarea indicada
     *
     * @param entity SgAfDepreciacionMaestro
     * @return Response
     */
    @POST
    @Operation(summary = "Procesa la tarea que se pasa como parametro.")
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/procesartarea")
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_MAESTRO, ConstantesOperaciones.CREAR_DEPRECIACION_BIEN, ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_BIEN})
    public Response procesarTarea(SgAfDepreciacionMaestro entity) {
        try {
            procesarListaTareasDepreciacion(entity);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }
    
    
    @GET
    @Path("/procesar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.EJECUTAR_PROCESAMIENTO_DEPRECIACION})
    public Response procesarTareasDepreciacion() {
        try {
            procesarListaTareasDepreciacion(null);
            return Response.status(HttpStatus.SC_OK).entity(Boolean.TRUE).build(); 

        } catch (BusinessException be) {
             LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), be.getMessage());
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else {
                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                tracer.activeSpan().log(ex.getMessage());
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }

    
    public void procesarListaTareasDepreciacion(SgAfDepreciacionMaestro maestro) {
        
        
        //ANTES DE TODO, ACTUALIZO LOS REGISTROS CON OPERACION U Y ELIMINO LOS REGISTROS CON PERACION D(ESTO ES POR SI ALGUN  PROCESO QUEDÓ PENDIENTE Y 
        //ES NECESARIO ACTUALIZAR LOS REGISTROS
        depreciacionBean.actualizarDepreciacion(EnumOperaciones.UPDATE.getText());
        depreciacionBean.eliminarListado(EnumOperaciones.DELETE.getText());
        
        List<SgAfDepreciacionMaestro> listaProcesar = new ArrayList();
        //Si se pasa el registro maestro como parametro y no ha sido procesado se agrega a la lista y será el único a procesar
        //de lo contrario se obtienen todos los que no han sido procesados.
        if(maestro != null) {
            if(!maestro.getDmaEstado().equals(EnumEstadosProceso.PROCESADO.getText())) {
                listaProcesar.add(maestro);
            }
        } else {
            FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
            filtro.setIncluirCampos(new String[]{"dmaPk","dmaVersion","dmaAnioProceso","dmaMesProceso","dmaFuenteFinanciamientoFk.ffiPk","dmaFuenteFinanciamientoFk.ffiVersion",
                                        "dmaProyectoFk.proPk","dmaProyectoFk.proVersion","dmaCodigoInventario","dmaFechaCreacion","dmaFechaInicioProcesamiento","dmaFechaFinalProcesamiento",
                                        "dmaEstado","dmaUltModFecha","dmaUltModUsuario"});
            //Se ejecutan los procesos pendientes o en proceso.
            List<EnumEstadosProceso> estados = new ArrayList();
            estados.add(EnumEstadosProceso.PENDIENTE);
            estados.add(EnumEstadosProceso.EN_PROCESO);
            filtro.setEstados(estados);
            filtro.setSecurityOperation(null);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"dmaAnioProceso"});
            listaProcesar = servicio.obtenerPorFiltro(filtro);
        }
        Boolean procesadoExito = Boolean.FALSE;
        Boolean procesadoConError = Boolean.FALSE;
        //Obtenemos todos los que no estan en estado PROCESADO
        if(listaProcesar != null && !listaProcesar.isEmpty()) {
            for(SgAfDepreciacionMaestro tarea : listaProcesar) {
                Boolean guardarFinal = Boolean.TRUE;
                procesadoExito = Boolean.FALSE;
                procesadoConError = Boolean.FALSE;
                try {
                    if(tarea.getDmaFechaInicioProcesamiento() == null) {
                        tarea.setDmaFechaInicioProcesamiento(LocalDateTime.now());
                    }
                    tarea.setDmaEstado(EnumEstadosProceso.EN_PROCESO);
                    //LOGGER.info("Primer guardado de la tarea con id: " + tarea.getDmaPk());
                    tarea = servicio.guardar(tarea, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);//Se guarda antes de procesar
                    //LOGGER.info("Tarea guardada.." );
                    if (DepreciacionMaestroValidacionMaestro.validar(tarea,anioLimiteMenorAdmitido, Boolean.TRUE)) {
                         // Se obtienen la lista de bienen que cumplen el criterio de la tarea a procesar. Esto solo para los bienes que son activos fijos
                        FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();

                        if(tarea.getDmaAnioProceso() != null) {
                            filtroBienes.setMenorIgualAnio(Boolean.TRUE); 
                            filtroBienes.setAnio(tarea.getDmaAnioProceso().longValue() );

                        }

                        if(tarea.getDmaMesProceso() != null) {
                            filtroBienes.setMenorIgualMes(Boolean.TRUE);
                            filtroBienes.setMes(tarea.getDmaMesProceso().longValue());
                        }

                        filtroBienes.setFuenteId(tarea.getDmaFuenteFinanciamientoFk() != null ? tarea.getDmaFuenteFinanciamientoFk().getFfiPk(): null);
                        filtroBienes.setProyectoId(tarea.getDmaProyectoFk() != null ? tarea.getDmaProyectoFk().getProPk() : null);
                        filtroBienes.setCodigoInventario(tarea.getDmaCodigoInventario());
                        filtroBienes.setSecurityOperation(null);

                        filtroBienes.setDepreciadoCompleto(Boolean.FALSE);
                        filtroBienes.setActivos(Boolean.TRUE);
                        filtroBienes.setParaDepreciar(Boolean.TRUE);
                        filtroBienes.setOrderBy(new String[]{"bdeNumCorrelativo"});
                        filtroBienes.setAscending(new boolean[]{true});
                        
                        Integer resultado = bienes.obtenerTotalPorFiltro(filtroBienes).intValue();
                       // LOGGER.info("total de bienes a procesar:"  + resultado);
                        Long batch = resultado / batchSize;
                        batch += resultado%batchSize==0 ? 0 : 1;
                        
                        Long primero = 0L;
                        //LOGGER.info("Inicia procesamiento de tarea:"  + tarea.getDmaPk());
                        for(Integer index = 1; index.compareTo(batch.intValue())<=0; index ++) {
                            //Se calcula la depreciación del bloque de bienes
                            servicio.procesar(tarea, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null,null, null, null, primero, batchSize);
                            
                            primero += batchSize;
                        }
                        //LOGGER.info("Finaliza procesamiento de tarea:"  + tarea.getDmaPk());
                    } 
                    procesadoExito = Boolean.TRUE;
                } catch (BusinessException be) {
                    procesadoConError = Boolean.TRUE;
                    tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                    tracer.activeSpan().log(be.getMessage());
                    LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), be);
                    
                } catch (Exception ex) {
                    if (PersistenceHelper.isOptimisticException(ex)) {
                        //Si es porque el registro estaba siendo ocupado no se hará nada, el registros no actualizará estado para que pueda ser procesado posteriormente
                        guardarFinal = Boolean.FALSE;
                        
                        tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                        tracer.activeSpan().log(ex.getMessage());
                        LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                    } else {
                        procesadoConError = Boolean.TRUE;
                        tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                        tracer.activeSpan().log(ex.getMessage());
                        LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                    }
                } finally {
                    try {
                        //Actualizamos los registros que tengan la operación U
                        LOGGER.info("Actualizando registros de depreciación");
                        depreciacionBean.actualizarDepreciacion(EnumOperaciones.UPDATE.getText());
                        //Eliminamos los registros que tengan la operación D
                         LOGGER.info("Eliminando registros de depreciación");
                        depreciacionBean.eliminarListado(EnumOperaciones.DELETE.getText());
                       
                        tarea = servicio.obtenerPorId(tarea.getDmaPk(), Boolean.FALSE);
                     
                        //tarea.setDmaEstado(estado);
                        if(guardarFinal) {
                            if(procesadoExito != null && procesadoExito) {
                                tarea.setDmaEstado(EnumEstadosProceso.PROCESADO);
                            } else if(procesadoConError != null && procesadoConError) {
                                tarea.setDmaEstado(EnumEstadosProceso.PROCESADO_CON_ERROR);
                            }
                            tarea.setDmaFechaFinalProcesamiento(LocalDateTime.now());  
                        }
                        
                        tarea = servicio.guardar(tarea, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);//Se guarda despues de procesar
                        LOGGER.info("Tarea guardada con estado: " + tarea.getDmaEstado());
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                    }
                    
                }
                
            }
        }
    }
}