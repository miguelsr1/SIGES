/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.SgCategoriaCurso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 250)
@Timeout(value = 3000L)
public class CategoriaCursoRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;
    
    public CategoriaCursoRestClient() {
    }


	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCategoriaCurso> buscar(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos/buscar");
        SgCategoriaCurso[] categoriasCursos = RestClient.invokePost(webTarget, filtro, SgCategoriaCurso[].class, userToken);
        return Arrays.asList(categoriasCursos);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public Long buscarTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgCategoriaCurso guardar(SgCategoriaCurso categoriaCurso) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaCurso == null || userToken == null) {
            return null;
        }
        if (categoriaCurso.getCcuPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos");
            return RestClient.invokePost(webTarget, categoriaCurso, SgCategoriaCurso.class, userToken);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos");
            webTarget = webTarget.path(categoriaCurso.getCcuPk().toString());
            return RestClient.invokePut(webTarget, categoriaCurso, SgCategoriaCurso.class, userToken);
        }
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public SgCategoriaCurso obtenerPorId(Long categoriaCursoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaCursoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos");
        webTarget = webTarget.path(categoriaCursoPk.toString());
        return RestClient.invokeGet(webTarget, SgCategoriaCurso.class, userToken);
    }

    public void eliminar(Long categoriaCursoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaCursoPk == null || userToken == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos");
        webTarget = webTarget.path(categoriaCursoPk.toString());
        RestClient.invokeDelete(webTarget, userToken);
    }

	@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 250)
    public List<SgCategoriaCurso> obtenerHistorialPorId(Long categoriaCursoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (categoriaCursoPk == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getHistoricResultsWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/categoriascursos/historial");
        webTarget = webTarget.path(categoriaCursoPk.toString());
        SgCategoriaCurso[] categoriasCursos = RestClient.invokeGet(webTarget, SgCategoriaCurso[].class, userToken);
        return Arrays.asList(categoriasCursos);
    }
    

}
