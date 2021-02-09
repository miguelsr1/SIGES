/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgRecursoEducativo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;

@RequestScoped
@Path("/recursoseducativos")
public class RecursosEducativosRecurso {

    private static final Logger LOGGER = Logger.getLogger(RecursosEducativosRecurso.class.getName());

    @Inject
    private CatalogosRestClient catalogosClient;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Response Lista de SgRecursoEducativo
     */
    @POST
    @Path("/buscar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(FiltroCodiguera filtro, String userCode) throws Exception {
        try {
            String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
            List<SgRecursoEducativo> rec = catalogosClient.buscarRecursosEducativos(filtro, userToken);
            HashMap<String, String> ret = new HashMap<>();
            for (SgRecursoEducativo r : rec){
                ret.put(r.getRedPk().toString(), r.getRedNombre());
            }
            return Response.status(HttpStatus.SC_OK).entity(ret).build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("ERROR").build();
        }
    }

 

   
}
