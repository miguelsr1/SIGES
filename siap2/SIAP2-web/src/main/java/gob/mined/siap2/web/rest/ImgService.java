/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.rest;

import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author Sofis Solutions
 */
@Path("/images")
@RequestScoped
public class ImgService {

    @Context
    private UriInfo context;


    @Inject
    ArchivoDelegate archivoDelegate;

    @GET
    @Path("/{file}/{token}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response doGet(@PathParam("file") String file, @PathParam("token") String token) throws ServletException, IOException {
        if (TextUtils.isEmpty(file)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Archivo a = archivoDelegate.getArchivo(file, token);
        if (a == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(archivoDelegate.getFile(a), MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + a.getNombreOriginal() + "\"")
                .header("Content-Type", a.getContentType())
                .header("Content-Length", String.valueOf(file.length()))
                .build();
    }

}
