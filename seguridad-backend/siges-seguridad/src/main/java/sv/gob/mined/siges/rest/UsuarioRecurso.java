/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.rest;

import com.sofis.security.OperationSecurity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.filtros.FiltroUsuario;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConsultaHistoricoBean;
import sv.gob.mined.siges.negocio.servicios.UsuarioBean;
import sv.gob.mined.siges.persistencia.entidades.SgUsuario;
import sv.gob.mined.siges.persistencia.utilidades.PersistenceHelper;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgActualizarPerfil;
import sv.gob.mined.siges.dto.SgAuthRequest;
import sv.gob.mined.siges.dto.SgAuthResponse;
import sv.gob.mined.siges.dto.SgUsuarioContextoId;
import sv.gob.mined.siges.dto.UserChangePassword;
import sv.gob.mined.siges.utils.JWTUtils;

@RequestScoped
@Path("/v1/usuarios")
@Tag(name = "Usuario API V1")
@DeclareRoles({ConstantesOperaciones.AUTENTICADO,
    ConstantesOperaciones.CREAR_USUARIO,
    ConstantesOperaciones.ACTUALIZAR_USUARIO,
    ConstantesOperaciones.ELIMINAR_USUARIO,
    ConstantesOperaciones.AGREGAR_USUARIO_A_PENTAHO})
public class UsuarioRecurso {

    private static final Logger LOGGER = Logger.getLogger(UsuarioRecurso.class.getName());

    @Inject
    private UsuarioBean servicio;

    @Inject
    private ConsultaHistoricoBean historico;

    @Inject
    private JsonWebToken jwt;
    

    @POST
    @Operation(summary = "Autentica usuario y devuelve JWT.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgAuthResponse.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response autenticar(SgAuthRequest request) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {

            SSLContextBuilder builder = new SSLContextBuilder();

            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();

            HttpPost post = new HttpPost(System.getProperty("service.cas.api-auth-url"));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", request.getUsername()));
            params.add(new BasicNameValuePair("password", request.getPassword()));
            post.setEntity(new UrlEncodedFormEntity(params));

            response = httpclient.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (request.getTokenExpirationMinutes() == null) {
                    request.setTokenExpirationMinutes(20);
                }

                List<String> operaciones = servicio.obtenerOperacionesPorUsuarioCodigo(request.getUsername(), request.getCategoriasOperacion());
                operaciones.add(ConstantesOperaciones.AUTENTICADO);
                String token = JWTUtils.generarToken(request.getUsername(), request.getAddress(), "/privateKey.pem", operaciones, request.getTokenExpirationMinutes());
                SgAuthResponse auth = new SgAuthResponse();
                auth.setJwt(token);
                return Response.status(HttpStatus.SC_OK).entity(auth).build();
            } else {
                LOGGER.log(Level.SEVERE, "AUTH CAS STATUS: " + response.getStatusLine().getStatusCode());
                return Response.status(response.getStatusLine().getStatusCode()).build();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Devuelve el objeto que tiene el id indicado
     *
     * @param id Long
     * @return Response SgUsuario
     */
    @GET
    @Operation(summary = "Devuelve el objeto que tiene el id indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{usuarioId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerPorId(@PathParam("usuarioId") Long id) {
        try {
            SgUsuario entity = servicio.obtenerPorId(id);
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
     * Devuelve los codigos de las operaciones del usuario con el codigo
     * indicado
     *
     * @param codigo String
     * @return Response Lista de String
     */
    @GET
    @Operation(summary = "Devuelve los codigos de las operaciones del usuario con el codigo indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/operaciones/{usuarioCodigo}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    @Deprecated
    public Response obtenerOperacionesUsuarioToken(@PathParam("usuarioCodigo") String codigo, @QueryParam("categoriasOperacionPk") List<Long> categoriasOperacionPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerOperacionesPorUsuarioCodigo(jwt.getSubject(), categoriasOperacionPk)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve los codigos de las operaciones para usuario de JWT.
     * indicado
     *
     * @param codigo String
     * @return Response Lista de String
     */
    @GET
    @Operation(summary = "Devuelve los codigos de las operaciones del usuario con el codigo indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/me/operaciones")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerOperacionesUsuarioToken(@QueryParam("categoriasOperacionPk") List<Long> categoriasOperacionPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerOperacionesPorUsuarioCodigo(jwt.getSubject(), categoriasOperacionPk)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve lista de OperationSecurity para usuario de JWT.
     *
     * @return Response lista de OperationSecurity
     */
    @GET
    @Operation(summary = "Devuelve lista de OperationSecurity para usuario de JWT.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = OperationSecurity.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/me/operacionesSeguridad")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerOperacionesSeguridadUsuarioToken(@QueryParam("categoriasOperacionPk") List<Long> categoriasOperacionPk,
            @QueryParam("operacionCodigo") String operacion) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerOperacionesSeguridad(operacion, jwt.getSubject(), categoriasOperacionPk)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve lista de OperationSecurity para usuario y operacion dados
     * indicado
     *
     * @param operacion String
     * @param codigousuario String
     * @return Response lista de OperationSecurity
     */
    @GET
    @Operation(summary = "Devuelve lista de OperationSecurity para usuario y operacion dados.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = OperationSecurity.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/operacionesSeguridad/{usuarioCodigo}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    @Deprecated
    public Response obtenerOperacionesSeguridadUsuarioToken(@PathParam("usuarioCodigo") String codigoUsuario,
            @QueryParam("operacionCodigo") String operacion,
            @QueryParam("categoriasOperacionPk") List<Long> categoriasOperacionPk) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerOperacionesSeguridad(operacion, jwt.getSubject(), categoriasOperacionPk)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroUsuario
     * @return Response Lista de SgUsuario
     */
    @POST
    @Operation(summary = "Realiza la consulta de los elementos que satisfacen el criterio.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    // NO EXPONER ESTE METODO HACIA AFUERA
    public Response buscar(FiltroUsuario filtro) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.obtenerPorFiltro(filtro)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Devuelve entidad usuario del JWT Subject
     *
     * @return Response SgUsuario
     */
    @GET
    @Operation(summary = "Devuelve entidad usuario del JWT Subject")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/me")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerMiUsuario() {
        try {
            FiltroUsuario filtro = new FiltroUsuario();
            filtro.setCodigoExacto(jwt.getSubject());
            List<SgUsuario> usuarios = servicio.obtenerPorFiltro(filtro);
            if (!usuarios.isEmpty()) {
                return Response.status(HttpStatus.SC_OK).entity(usuarios.get(0)).build();
            } else {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Cambia la contraseña del usuario del JWT Subject
     *
     * @param passwordActual Contraseña actual hasheada.
     * @param passwordNueva Nueva contraseña a asignar hasheada.
     * @return
     */
    @PUT
    @Operation(summary = "Cambia la contraseña del usuario del JWT Subject.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/me/cambiarpassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response cambiarPassword(UserChangePassword passwords) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.cambiarPassword(jwt.getSubject(), passwords.getPasswordActual(), passwords.getPasswordNueva())).build();
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroUsuario
     * @return Response Lista de SgUsuario
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
    public Response buscarTotal(FiltroUsuario filtro) {
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
     * @param entity SgUsuario
     * @return Response SgUsuario
     */
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_USUARIO})
    public Response crear(SgUsuario entity) {
        try {
            entity.setUsuPk(null);
            entity.setUsuVersion(0);
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
    
    @POST
    @Path("/me/actualizarperfil")
    @DenyAll //No se puede actualizar mas el nombre o email
    @Operation(summary = "Actualiza perfil del usuario del JWT.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad actualizada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarMiPerfil(SgActualizarPerfil perfil) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.actualizarMiPerfil(perfil, jwt.getSubject())).build();
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
    
    @POST
    @Path("/me/aceptarterminos")
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    @Operation(summary = "Acepta terminos de usuario indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad actualizada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response aceptarMisTerminos() {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.aceptarTerminos(jwt.getSubject())).build();
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
    
    @POST
    @Path("/me/rechazarterminos")
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    @Operation(summary = "Rechaza terminos de usuario indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad actualizada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rechazarMisTerminos() {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.rechazarTerminos(jwt.getSubject())).build();
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

    @POST
    @Path("/enviarUsuarioPentaho")
    @RolesAllowed({ConstantesOperaciones.AGREGAR_USUARIO_A_PENTAHO})
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response enviarAPentaho(SgUsuario usuario) {
        try {
            servicio.enviarAPentaho(usuario);
            return Response.status(HttpStatus.SC_CREATED).build();
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

    @POST
    @Path("/eliminarUsuarioPentaho")
    @RolesAllowed({ConstantesOperaciones.AGREGAR_USUARIO_A_PENTAHO})
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarDePentaho(SgUsuario usuario) {
        try {
            servicio.eliminarDePentaho(usuario);
            return Response.status(HttpStatus.SC_CREATED).build();
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
     * @param entity SgUsuario
     * @return Response SgUsuario
     */
    @PUT
    @Operation(summary = "Actualiza el objeto con la id indicada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/{usuarioId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ACTUALIZAR_USUARIO})
    public Response actualizar(@PathParam("usuarioId") Long id, SgUsuario entity) {
        try {
            if (servicio.objetoExistePorId(id)) {
                entity.setUsuPk(id);
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
     * Cambia la contraseña del usuario con código indicado.
     *
     * @param codigoUsuario Código del usuario.
     * @param passwordActual Contraseña actual hasheada.
     * @param passwordNueva Nueva contraseña a asignar hasheada.
     * @return
     */
    @PUT
    @Operation(summary = "Cambia la contraseña del usuario con código indicado.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Actualizado satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class)))
        ,
        @APIResponse(responseCode = "404", description = "Objeto no encontrado.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    //No exponer este metodo hacia el exterior
    //Solo exponer /me/cambiarpassword
    //Borrar este luego de que no se use más. Actualmente se usa desde autogestion
    @Path("/cambiarpassword/{usuarioCod}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response cambiarPassword(@PathParam("usuarioCod") String codigoUsuario, UserChangePassword passwords) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(servicio.cambiarPassword(codigoUsuario, passwords.getPasswordActual(), passwords.getPasswordNueva())).build();
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
    @Path("/{usuarioId:[1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.ELIMINAR_USUARIO})
    public Response eliminarPorId(@PathParam("usuarioId") Long id) {
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
     * @return Response Lista de SgUsuario
     */
    @GET
    @Operation(summary = "Devuelve historial del elemento con la id indicada ordenado por version.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SgUsuario.class, type = SchemaType.ARRAY)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/historial/{usuarioId:[1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response obtenerHistorialPorId(@PathParam("usuarioId") Long id) {
        try {
            return Response.status(HttpStatus.SC_OK).entity(historico.obtenerHistorialRevisionesPorId(SgUsuario.class, id)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }

    /**
     * Crea rol en contexto para usuarios
     *
     * @param entity SgUsuarioContextoId
     * @return Response Lista de SgUsuario
     */
    @POST
    @Operation(summary = "Crea rol en contexto para usuarios.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/crearUsuarioContexto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.CREAR_USUARIO})
    public Response crearUsuarioContextoId(SgUsuarioContextoId entity) {
        try {
            if (entity != null) {
                List<SgUsuario> usuarios = new ArrayList();
                for (SgUsuario usu : entity.getUscUsuarios()) {

                    FiltroUsuario fusu = new FiltroUsuario();
                    fusu.setCodigo(usu.getUsuCodigo());
                    List<SgUsuario> listusu = servicio.obtenerPorFiltro(fusu);
                    if (!listusu.isEmpty()) {
                        usu = listusu.get(0);
                        usuarios.add(usu);
                    } else {
                        usu = servicio.guardar(usu);
                        usuarios.add(usu);
                    }

                }
                entity.setUscUsuarios(usuarios);
                servicio.crearUsuarioContextoId(entity);
            }
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve byte[] con certificado de usuario
     *
     * @param String certificatePassword
     * @return byte[]
     */
    @POST
    @Operation(summary = "Devuelve byte[] con certificado de usuario.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/me/generarcertificado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
    @RolesAllowed({ConstantesOperaciones.AUTENTICADO})
    public Response generarCertificadoUsuario(String certificatePassword) {
        try { 
            SgUsuario u = servicio.obtenerPorCodigo(jwt.getSubject());
            return Response.ok(servicio.generarCertificado(u, certificatePassword), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + "certificado_"+u.getUsuCodigo()+"_SIGES" + "\"" )
                    .build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).type(MediaType.APPLICATION_JSON).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(Errores.ERROR_GENERAL).build();
        }
    }
    
    /**
     * Devuelve byte[] con certificado de usuario
     *
     * @param String certificatePassword
     * @return byte[]
     */
    @POST
    @Operation(summary = "Devuelve byte[] con certificado de usuario.")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Consumo satisfactorio.")
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Path("/revocarcertificados")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ConstantesOperaciones.REVOCAR_CERTIFICADOS_USUARIO})
    public Response revocarCertificadosUsuario(String usercode) {
        try { 
            servicio.revocarCertificadosUsuario(usercode);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (BusinessException be) {
            return Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).entity(new BusinessReturnedException(be.getErrores())).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(Errores.ERROR_GENERAL).build();
        }
    }
    

}
