/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest.mobile;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import io.opentracing.Tracer;
import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.RequestScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.AlertaBean;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroAlerta;
import sv.gob.mined.siges.persistencia.entidades.SgAlerta;

@RequestScoped
@Path("/m/v1/alertas")
@Tag(name = "Alertas API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA})
public class AlertaRecursoMobile {

    private static final Logger LOGGER = Logger.getLogger(AlertaRecursoMobile.class.getName());

    @Inject
    private AlertaBean servicio;

    @Inject
    private Tracer tracer;

    @Inject
    private JsonWebToken jwt;
    
    @Inject
    @ConfigProperty(name = "external-api.max-results", defaultValue = "50")
    private Long allowedMaxResults;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgAllegado
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAlerta.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA})
    //@Timed
    public Response buscar(FiltroAlerta filtro) {
        try {
            if (filtro.getMaxResults() == null || filtro.getMaxResults() > allowedMaxResults) {
                filtro.setMaxResults(allowedMaxResults);
            }
            if (filtro.getIncluirCampos() == null || filtro.getIncluirCampos().length < 1){
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
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgAllegado
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
    @RolesAllowed({ConstantesOperaciones.BUSCAR_ALERTA_TEMPRANA})
    //@Timed
    public Response buscarTotal(FiltroAlerta filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerTotalPorFiltro(filtro)).build();
        } catch (Exception ex) {
            tracer.activeSpan().setTag(Constantes.OPENTRACING_ERROR_TAG, true);
            tracer.activeSpan().log(ex.getMessage());
            LOGGER.log(Level.SEVERE, jwt.getSubject() + " | " + tracer.activeSpan().context().toString(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

}
