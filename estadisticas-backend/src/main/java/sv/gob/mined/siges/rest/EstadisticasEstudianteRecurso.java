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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroEstadisticas;
import sv.gob.mined.siges.negocio.servicios.EstadisticasEstudiantesBean;

/**
 *
 * @author Sofis Solutions
 */
@RequestScoped
@Path("/v1/estadisticasestudiantes")
@Tag(name = "Estadisticas Estudiantes API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO})
public class EstadisticasEstudianteRecurso {

    private static final Logger LOGGER = Logger.getLogger(EstadisticasEstudianteRecurso.class.getName());

    @Inject
    private EstadisticasEstudiantesBean servicio;
    
    /* Método genérico que devuelve estadísticas para todos los indicadores*/
    @POST
    @Path("obtenerestadisticas")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerEstadisticas(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerEstadisticas(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("poblacionnoescolarizadaporedad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response poblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.poblacionNoEscolarizadaPorEdad(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajepoblacionnoescolarizadaporedad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajePoblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajePoblacionNoEscolarizadaPorEdad(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajeestudiantesdesertores")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeEstudiantesDesertores(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeEstudiantesDesertores(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("matriculaporniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response matriculaPorNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.matriculaPorNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasabrutaingresoprimergradoeducacionbasica")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaBrutaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaBrutaIngresoPrimerGradoEducacionBasica(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajederepetidores")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeRepetidores(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeRepetidores(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasaderepeticion")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaDeRepeticion(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaDeRepeticion(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasatransicionporciclo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaTransicionPorCiclo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaTransicionPorCiclo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasatransicionpornivel")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaTransicionPorNivel(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaTransicionPorNivel(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasadesercion")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaDesercion(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaDesercion(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajedetrabajadores")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeTrabajadores(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeTrabajadores(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajecondiscapacidad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeConDiscapacidad(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeConDiscapacidad(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("distribucionporcentualestudiantescondiscapacidad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response distribucionPorcentualEstudiantesConDiscapacidad(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.distribucionPorcentualEstudiantesConDiscapacidad(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    @POST
    @Path("distribucionporcentualestudiantessegunconvivenciafamiliar")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    

    @POST
    @Path("tasanetaingresoprimergradoeducacionbasica")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaNetaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaNetaIngresoPrimerGradoEducacionBasica(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasaespecificadeescolarizacionporedad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaEspecificaDeEscolarizacionPoredad(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaEspecificaDeEscolarizacionPorEdad(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajedeestudiantesdeprimergradoconexperienciaeneducacionparvularia")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEdeucacionParvularia(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEdeucacionParvularia(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("distribucionporcentualdelamatriculaporniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response distribucionPorcentualDeLaMatriculaPorNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.distribucionPorcentualDeLaMatriculaPorNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasabrutadematriculaporniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaBrutaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaBrutaDeMatriculaPorNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasanetadematriculaporniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaNetaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaNetaDeMatriculaPorNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("distribucionporcentualdeestudiantessegunactividadlaboral")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response distribucionPorcentualDeEstudiantesSegunActividadLaboral(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.distribucionPorcentualDeEstudiantesSegunActividadLaboral(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

//    @POST
//    @Path("tasabrutadeingresoalsextogrado")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
//    public Response tasaBrutaDeIngresoAlSextoGrado(FiltroEstadisticas filtro) {
//        try {
//            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaBrutaDeIngresoAlSextoGrado(filtro)).build();
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
//        }
//    }
//    @POST
//    @Path("tasadeingresoefectivoalseptimogrado")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
//    public Response tasaDeIngresoEfectivoAlSeptimoGrado(FiltroEstadisticas filtro) {
//        try {
//            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaDeIngresoEfectivoAlSeptimoGrado(filtro)).build();
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
//        }
//    }
    @POST
    @Path("porcentajeestudiantesaprobados")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeEstudiantesAprobados(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeEstudiantesAprobados(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasabrutaaprobacion")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaBrutaAprobacion(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaBrutaAprobacion(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasapromocion")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaPromocion(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaPromocion(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("centroseducativossegunniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response centrosEducativosSegunNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.centrosEducativosSegunNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajedeestudiantesconaccesoainternet")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeEstudiantesConAccesoAInternet(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeEstudiantesConAccesoAInternet(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajedeestudiantesconaccesoacomputadora")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeEstudiantesConAccesoAComputadora(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeEstudiantesConAccesoAComputadora(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasabrutaingresoporgrado")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaBrutaIngresoPorGrado(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaBrutaIngresoPorGrado(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("estudiantesporseccion")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response estudiantesPorSeccion(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.estudiantesPorSeccion(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajeconsobreedad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeConSobreedad(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeConSobreedad(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("tasaespecificamatriculaporgrado")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response tasaEspecificaMatriculaPorGrado(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.tasaEspecificaMatriculaPorGrado(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("distribucionporcentualdocentessegunaniosservicio")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response distribucionPorcentualDocentesSegunAniosServicio(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.distribucionPorcentualDocentesSegunAniosServicio(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("promedioestudiantespordocente")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response promedioEstudiantesPorDocente(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.promedioEstudiantesPorDocente(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajededocentesporgradoacademicoalcanzado")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeDocentesPorGradoAcademicoAlcanzado(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeDocentesPorGradoAcademicoAlcanzado(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajededocentesconaccesoainternet")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeDocentesConAccesoAInternet(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeDocentesConAccesoAInternet(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajedecentroseducativosconaccesoserviciosbasicos")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeCentrosEducativosConAccesoServiciosBasicos(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeCentrosEducativosConAccesoServiciosBasicos(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("porcentajededocentescertificadosporniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response porcentajeDeDocentesCertificadosPorNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.porcentajeDeDocentesCertificadosPorNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    @POST
    @Path("distribuciondedocentessegunniveleducativo")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response distribucionDeDocentesSegunNivelEducativo(FiltroEstadisticas filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.distribucionDeDocentesSegunNivelEducativo(filtro)).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
}
