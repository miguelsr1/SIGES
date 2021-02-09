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
import sv.gob.mined.siges.filtros.FiltroLiquidacionProyectos;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.BienesDepreciablesBean;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.DepreciacionBean;
import sv.gob.mined.siges.negocio.servicios.LiquidacionProyectoBean;
import sv.gob.mined.siges.negocio.servicios.ProyectosBean;
import sv.gob.mined.siges.negocio.validaciones.LiquidacionProyectoValidacionNegocio;
import sv.gob.mined.siges.persistencia.entidades.SgAfLiquidacionProyecto;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfProyectos;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;

@RequestScoped
@Path("/v1/liquidacionproyectos")
@Tag(name = "Liquidación de proyectos API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_LIQUIDACION_PROYECTO,
    ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_PROYECTO,
    ConstantesOperaciones.ELIMINAR_LIQUIDACION_PROYECTO,
    ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS
})
public class LiquidacionProyectoRecurso {
    private static final Logger LOGGER = Logger.getLogger(LiquidacionProyectoRecurso.class.getName());

    @Inject
    private LiquidacionProyectoBean servicio;

    @Inject
    private DepreciacionBean depreciacionBean;
    
    @Inject
    private BienesDepreciablesBean bienes;
    
    @Inject
    @ConfigProperty(name = "anio-limite-menor-admitido", defaultValue = "1960")
    private Integer anioLimiteMenorAdmitido;
    @Inject
    @ConfigProperty(name = "batch-size-liquidacion", defaultValue = "10")
    private Long batchSizeLiquidacion;
    
    @Inject ProyectosBean proyectosBean;
    
    @Inject
    private ConsultaHistoricoBean historico;

    @Inject
    private Tracer tracer;
    

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgAfLiquidacionProyecto
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLiquidacionProyecto.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{liqproId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS})
    public Response obtenerPorId(@PathParam("liqproId") Long id, @QueryParam("dataSecurity") Boolean dataSecurity) {
        try {
            SgAfLiquidacionProyecto entity = servicio.obtenerPorId(id, dataSecurity);
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
     * @param filtro FiltroLiquidacionProyectos
     * @return Response Lista de SgAfLiquidacionProyecto
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLiquidacionProyecto.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS})
    public Response buscar(FiltroLiquidacionProyectos filtro) {
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
     * @param filtro FiltroLiquidacionProyectos
     * @return Response Lista de SgAfLiquidacionProyecto
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS})
    public Response buscarTotal(FiltroLiquidacionProyectos filtro) {
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
     * @param entity SgAfLiquidacionProyecto
     * @return Response SgAfLiquidacionProyecto
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLiquidacionProyecto.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_LIQUIDACION_PROYECTO})
    public Response crear(SgAfLiquidacionProyecto entity) {
        try {
            entity.setLprPk(null);
            entity.setLprVersion(0);
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(entity, Boolean.TRUE, Boolean.TRUE)).build();
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
                schema = @Schema(implementation = SgAfLiquidacionProyecto.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{liqproId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_PROYECTO})
    public Response actualizar(@PathParam("liqproId") Long id, SgAfLiquidacionProyecto entity) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
                entity.setLprPk(id);
                return Response.status(HttpStatus.SC_OK).entity(servicio.guardar(entity, Boolean.TRUE, Boolean.TRUE)).build();
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
    @Path("/{liqproId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_LIQUIDACION_PROYECTO})
    public Response eliminarPorId(@PathParam("liqproId") Long id) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
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
                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
            }
        }
    }
    
    /**
     * Devuelve historial del elemento con la id indicada ordenado por version
     *
     * @param id Long
     * @return Response Lista de SgAfLiquidacionProyecto
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
    @Path("/historial/{liqproId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("liqproId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgAfLiquidacionProyecto.class, id)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
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
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_PROYECTO, ConstantesOperaciones.CREAR_DEPRECIACION_BIEN, ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_BIEN})
    public Response procesarTareas() {
        try {
            procesarListaTareasLiquidacionProyectos(null);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    
    /**
     * Procesa un tarea indicada
     *
     * @param entity SgAfLiquidacionProyecto
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
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_PROYECTO, ConstantesOperaciones.CREAR_DEPRECIACION_BIEN, ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_BIEN})
    public Response procesarTarea(SgAfLiquidacionProyecto entity) {
        try {
            procesarListaTareasLiquidacionProyectos(entity);
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
    @RolesAllowed({ConstantesOperaciones.EJECUTAR_PROCESAMIENTO_LIQUIDACION_PROYECTOS})
    public Response procesarLiquidacionProyectos() {
        try {
            procesarListaTareasLiquidacionProyectos(null);
            return Response.status(HttpStatus.SC_OK).build();
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
    
    public void procesarListaTareasLiquidacionProyectos(SgAfLiquidacionProyecto maestro) {
        
        //ANTES DE TODO, ACTUALIZO LOS REGISTROS CON OPERACION U Y ELIMINO LOS REGISTROS CON PERACION D(ESTO ES POR SI ALGUN  PROCESO QUEDÓ PENDIENTE Y 
        //ES NECESARIO ACTUALIZAR LOS REGISTROS
        depreciacionBean.actualizarDepreciacion(EnumOperaciones.UPDATE.getText());
        depreciacionBean.eliminarListado(EnumOperaciones.DELETE.getText());
        
        List<SgAfLiquidacionProyecto> listaProcesar = new ArrayList();
        //Si se pasa el registro maestro como parametro y no ha sido procesado se agrega a la lista y será el único a procesar
        //de lo contrario se obtienen todos los que no han sido procesados.
        if(maestro != null) {
            if(!maestro.getLprEstado().equals(EnumEstadosProceso.PROCESADO.getText())) {
                listaProcesar.add(maestro);
            }
        } else {
            FiltroLiquidacionProyectos filtro = new FiltroLiquidacionProyectos();
            filtro.setIncluirCampos(new String[]{"lprPk" ,"lprFechaLiquidacion","lprFuenteFinanciamientoOrigenFk.ffiPk","lprFuenteFinanciamientoOrigenFk.ffiVersion","lprProyectoFk.proPk","lprProyectoFk.proVersion",
                                            "lprFuenteFinanciamientoDestinoFk.ffiPk","lprFuenteFinanciamientoDestinoFk.ffiVersion","lprFechaCreacion","lprFechaInicioProcesamiento","lprFechaFinalProcesamiento",
                                            "lprEstado","lprUltModFecha","lprUltModUsuario","lprVersion"});
            //Se ejecutan los procesos pendientes o en proceso.
            List<EnumEstadosProceso> estados = new ArrayList();
            estados.add(EnumEstadosProceso.PENDIENTE);
            estados.add(EnumEstadosProceso.EN_PROCESO);
            filtro.setEstados(estados);
            filtro.setSecurityOperation(ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS);
            listaProcesar = servicio.obtenerPorFiltro(filtro);
        }

        if(listaProcesar != null && !listaProcesar.isEmpty()) {
            for(SgAfLiquidacionProyecto liq : listaProcesar) {       
                Boolean procesado = Boolean.FALSE;
                Boolean guardarFinal = Boolean.TRUE;
                try {
                    if(liq.getLprFechaInicioProcesamiento() == null) {
                        liq.setLprFechaInicioProcesamiento(LocalDateTime.now());
                    }
                
                    liq.setLprEstado(EnumEstadosProceso.EN_PROCESO);
                    liq = servicio.guardar(liq, Boolean.FALSE, Boolean.FALSE);
                    
                    if (LiquidacionProyectoValidacionNegocio.validar(liq, anioLimiteMenorAdmitido)) {
                        FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
                        
                        filtroBienes.setFuenteId(liq.getLprFuenteFinanciamientoOrigenFk() != null ? liq.getLprFuenteFinanciamientoOrigenFk().getFfiPk() : null);
                        filtroBienes.setProyectoId(liq.getLprProyectoFk() != null ? liq.getLprProyectoFk().getProPk() : null);
                        
                        filtroBienes.setSecurityOperation(null);
     
                        filtroBienes.setOrderBy(new String[]{"bdeNumCorrelativo"});
                        filtroBienes.setAscending(new boolean[]{true});

                        Integer resultado = bienes.obtenerTotalPorFiltro(filtroBienes).intValue();
                        
                        Long batch = resultado / batchSizeLiquidacion;
                        batch += resultado%batchSizeLiquidacion==0 ? 0 : 1;

                        Long primero = 0L;

                        for(Integer index = 1; index.compareTo(batch.intValue())<=0; index ++) {
                            //Se calcula la depreciación del bloque de bienes
                            procesado = servicio.procesar(liq, Boolean.FALSE, primero, batchSizeLiquidacion);
                        }
                    }
                    if(procesado) {
                        liq.setLprEstado(EnumEstadosProceso.PROCESADO);

                       //Si hay proyecto, se actualiza estado de liquidado.
                       if(liq.getLprProyectoFk() != null) {  
                            SgAfProyectos proyecto = proyectosBean.obtenerPorId(liq.getLprProyectoFk().getProPk());
                            proyecto.setProProyectoLiquidado(Boolean.TRUE);
                            proyecto = proyectosBean.guardar(proyecto);
                            liq.setLprProyectoFk(proyecto);
                       }
                    }
                    
                } catch (BusinessException be) {
                    liq.setLprEstado(EnumEstadosProceso.PROCESADO_CON_ERROR);
                     
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
                            liq.setLprEstado(EnumEstadosProceso.PROCESADO_CON_ERROR);
                     
                            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                            tracer.activeSpan().log(ex.getMessage());
                            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                        }
                } finally {
                    //Actualizamos los registros que tengan la operación U
                    LOGGER.info("Actualizando registros de depreciación");
                    depreciacionBean.actualizarDepreciacion(EnumOperaciones.UPDATE.getText());
                    //Eliminamos los registros que tengan la operación D
                     LOGGER.info("Eliminando registros de depreciación");
                    depreciacionBean.eliminarListado(EnumOperaciones.DELETE.getText());
                    if(guardarFinal) {
                        liq = servicio.guardar(liq, Boolean.FALSE, Boolean.FALSE);//Se guarda despues de procesar
                        LOGGER.info("Tarea guardada con estado: " + liq.getLprEstado());
                    }
                }
            }
        }
    }

}