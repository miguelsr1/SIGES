/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.filtros.FiltroAcumulador;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.ConsultasAcumuladoresBean;
import sv.gob.mined.siges.persistencia.entidades.SgRubros;


/**
 *
 * @author Sofis Solutions
 */
@RequestScoped
@Path("/v1/consultas")
@Tag(name = "Presupuesto Escolar API V1")
/**
 * Servicios REST para los datos de las consultas acumuladas.
 */
public class ConsultasAcumuladorBean {

    private static final Logger LOGGER = Logger.getLogger(ConsultasAcumuladorBean.class.getName());

    @Inject
    private ConsultasAcumuladoresBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Context
    private Providers providers;

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response List<ResultadoConsultaDTO>
     */
    @POST
    @Operation(summary = "Realiza la consulta de valores acumulados")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgRubros.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/consultar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response buscar(FiltroAcumulador filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerAcumulados(filtro)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

}
