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
import sv.gob.mined.siges.dto.CodigosDTO;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.enumerados.TipoUnidad;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.filtros.FiltroLoteBienes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.BienesDepreciablesBean;
import sv.gob.mined.siges.negocio.servicios.LoteBienesBean;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;

@RequestScoped
@Path("/v1/lotes")
@Tag(name = "Lote Bienes API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.BUSCAR_LOTE_BIENES,
    ConstantesOperaciones.ACTUALIZAR_LOTE_BIENES,
    ConstantesOperaciones.ELIMINAR_LOTE_BIENES,
    ConstantesOperaciones.ELIMINAR_LOTE_BIENES
})
public class LoteBienesRecurso {
    
    private static final Logger LOGGER = Logger.getLogger(LoteBienesRecurso.class.getName());

    @Inject
    private LoteBienesBean servicio;
    
    @Inject
    private Tracer tracer;
    
    @Inject
    @ConfigProperty(name = "batch-size", defaultValue = "100")
    private Integer batchSize;
    
    @Inject
    private BienesDepreciablesBean bienes;
    
    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgAfLoteBienes
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLoteBienes.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{loteId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO})
    public Response obtenerPorId(@PathParam("loteId") Long id, @QueryParam("dataSecurity") Boolean dataSecurity) {
        try {
            SgAfLoteBienes entity = servicio.obtenerPorId(id);
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
     * @return Response Lista de SgAfLoteBienes
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLoteBienes.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO})
    public Response buscar(FiltroLoteBienes filtro) {
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
     * @param filtro FiltroLoteBienes
     * @return Response Lista de SgAfLoteBienes
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO})
    public Response buscarTotal(FiltroLoteBienes filtro) {
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
     * @param entity SgAfLoteBienes
     * @return Response SgAfLoteBienes
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLoteBienes.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_SOLICITUD_DESCARGO})
    public Response crear(SgAfLoteBienes entity) {
        try {
            entity.setLbiId(null);
            entity.setLbiVersion(0);
            return Response.status(HttpStatus.SC_CREATED).entity(servicio.guardar(entity)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            if (PersistenceHelper.isOptimisticException(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(Errores.ERROR_OPTIMISTIC_LOCK)).build();
            } else if (PersistenceHelper.isConstraintViolation(ex)) {
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getCodigoDescargoViolationBusinessException(ex)).build();
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
     * @param entity SgAfLoteBienes
     * @return Response SgAfLoteBienes
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAfLoteBienes.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{loteId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_SOLICITUD_DESCARGO})
    public Response actualizar(@PathParam("loteId") Long id, SgAfLoteBienes entity) {
        try {
            if (servicio.objetoExistePorId(id, Boolean.TRUE)) {
                entity.setLbiId(id);
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
                return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(PersistenceHelper.getCodigoDescargoViolationBusinessException(ex)).build();
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
    @RolesAllowed({ConstantesOperaciones.EJECUTAR_PROCESAMIENTO_LOTES})
    public Response procesarLotes() {
        Integer numeroCorrelativo = 0;
        try {
            FiltroLoteBienes filtro = new FiltroLoteBienes();
            filtro.setSecurityOperation(null);
            List<EnumEstadosProceso> estados = new ArrayList();
            filtro.setIncluirCampos(new String[]{"lbiId","lbiCodigoInventarioPadre","lbiPrimerCodInventario","lbiUltimoCodInventario","lbiUltimoCorrelativo","lbiEstado","lbiCantidadBienesReplicar",
                                            "lbiFechaInicioProcesamiento","lbiFechaFinalProcesamiento","lbiSede.sedPk","lbiSede.sedTipo","lbiSede.sedVersion", 
                                            "lbiUnidadesAdministrativas.uadPk","lbiUnidadesAdministrativas.uadVersion","lbiUltModFecha","lbiUltModUsuario","lbiVersion"});
            //Obtenemos todos los que no estan en estado PROCESADO
            estados.add(EnumEstadosProceso.PENDIENTE);
            estados.add(EnumEstadosProceso.EN_PROCESO);
            filtro.setEstados(estados);
            filtro.setSecurityOperation(null);
            List<SgAfLoteBienes> listaProcesar = servicio.obtenerPorFiltro(filtro);
            
            if(listaProcesar != null && !listaProcesar.isEmpty()) {
                for(SgAfLoteBienes lote : listaProcesar) { 
                    Boolean guardarFinal = Boolean.TRUE;
                    try {
                        Boolean procesar = Boolean.TRUE;
                        FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
                        filtroBienes.setSecurityOperation(null);

                        filtroBienes.setCodigoInventario(lote.getLbiCodigoInventarioPadre().trim());   
                        filtroBienes.setMaxResults(1L);
                        SgAfBienDepreciable bien = null;
                        List<SgAfBienDepreciable> listaBienes = bienes.obtenerPorFiltro(filtroBienes);
                        if(listaBienes != null && !listaBienes.isEmpty()) {
                            bien = listaBienes.get(0);
                        } else {
                            procesar = Boolean.FALSE;
                        }
                        
                        if(bien != null) {
                            filtroBienes = new FiltroBienesDepreciables();
                            if(bien.getBdeUnidadesAdministrativas() != null) {
                                filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                                filtroBienes.setUnidadAdministrativaId(bien.getBdeUnidadesAdministrativas().getUadPk());
                            } else if(bien.getBdeSede() != null) {
                                filtroBienes.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                                filtroBienes.setUnidadAdministrativaId(bien.getBdeSede().getSedPk());
                            }
                            if(lote.getLbiCantidadBienesReplicar() == null || lote.getLbiCantidadBienesReplicar() == 0) {
                                procesar = Boolean.FALSE;
                            } else {
                                //Se obtiene el ultimo correlativo del tipo de bien
                                numeroCorrelativo = bien.getBdeNumCorrelativo();
                            }
                        }

                        if(procesar) {
                            if(lote.getLbiFechaInicioProcesamiento() == null) {
                                lote.setLbiFechaInicioProcesamiento(LocalDateTime.now());
                            }
                            lote.setLbiEstado(EnumEstadosProceso.EN_PROCESO);
                            
                            lote = servicio.guardar(lote);

                            Integer registrosReplicar = 0;

                            Integer totalReplicar = lote.getLbiCantidadBienesReplicar();
                            Integer batch = (lote.getLbiCantidadBienesReplicar() / batchSize) + 1;

                            Integer procesados = 0;
                            for(Integer index = batch; index > 0; index --) {
                                if(batch.compareTo(1)==0) {
                                    registrosReplicar = totalReplicar;
                                } else {
                                    if(index.compareTo(1)==0) {
                                        registrosReplicar = totalReplicar - procesados;
                                    } else {
                                        registrosReplicar = batchSize;
                                    }
                                    
                                }
                                numeroCorrelativo = servicio.procesarLoteBienes(lote.getLbiId(), bien, registrosReplicar, numeroCorrelativo, Boolean.FALSE, filtroBienes);

                                procesados = procesados + registrosReplicar;
                            }  
                        }
                        lote.setLbiEstado(EnumEstadosProceso.PROCESADO);

                    } catch (BusinessException be) {
                        lote.setLbiEstado(EnumEstadosProceso.PROCESADO_CON_ERROR);
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
                            lote.setLbiEstado(EnumEstadosProceso.PROCESADO_CON_ERROR);
                            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                            tracer.activeSpan().log(ex.getMessage());
                            LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                        }
                    } finally {
                        if(guardarFinal) {
                            CodigosDTO codigos = null;
                            try {
                                codigos = bienes.obtenerRangoCodigosPorLote(lote.getLbiId());
                                lote.setLbiPrimerCodInventario(codigos.getPrimerCod());
                                lote.setLbiUltimoCodInventario(codigos.getUltimoCod());
                                lote.setLbiUltimoCorrelativo(codigos.getUltimoCorrelativo());
                            } catch (Exception ex) {
                                lote.setLbiUltimoCorrelativo(numeroCorrelativo - 1);
                                
                                tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
                                tracer.activeSpan().log(ex.getMessage());
                                LOGGER.log(Level.SEVERE, tracer.activeSpan().context().toString(), ex);
                            }
                            lote.setLbiFechaFinalProcesamiento(LocalDateTime.now());
                            servicio.guardar(lote);//Se guarda despues de procesar lote con error
                        }
                    }
                }
            }
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    } 
    
}
