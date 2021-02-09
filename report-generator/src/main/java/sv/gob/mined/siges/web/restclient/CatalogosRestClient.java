/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import sv.gob.mined.siges.web.dto.SgCanton;
import sv.gob.mined.siges.web.dto.SgDepartamento;
import sv.gob.mined.siges.web.dto.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.dto.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.dto.SgZona;
import sv.gob.mined.siges.web.dto.SgEspecialidad;
import sv.gob.mined.siges.web.dto.SgModalidadEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidadAtencion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, TimeoutException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CatalogosRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = RestClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            client.close();
        }
    }

    public CatalogosRestClient() {
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgModalidadAtencion> buscarModalidadAtencion(FiltroModalidadAtencion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion/buscar");
        SgModalidadAtencion[] modalidadesAtencion = restClient.invokePost(webTarget, filtro, SgModalidadAtencion[].class);
        return Arrays.asList(modalidadesAtencion);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgModalidadAtencion obtenerPorIdModalidadAtencion(Long modalidadAtencionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
        webTarget = webTarget.path(modalidadAtencionPk.toString());
        return restClient.invokeGet(webTarget, SgModalidadAtencion.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgSubModalidadAtencion> buscarSubModalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/buscar");
        SgSubModalidadAtencion[] subModalidad = restClient.invokePost(webTarget, filtro, SgSubModalidadAtencion[].class);
        return Arrays.asList(subModalidad);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgSubModalidadAtencion obtenerPorIdSubModalidad(Long subModalidadesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidadesPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
        webTarget = webTarget.path(subModalidadesPk.toString());
        return restClient.invokeGet(webTarget, SgSubModalidadAtencion.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgTipoIdentificacion> buscarTipoIdentificacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion/buscar");
        SgTipoIdentificacion[] codiguera = restClient.invokePost(webTarget, filtro, SgTipoIdentificacion[].class);
        List<SgTipoIdentificacion> lista = Arrays.asList(codiguera);
        return lista;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgPais> buscarPais(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises/buscar");
        SgPais[] codiguera = restClient.invokePost(webTarget, filtro, SgPais[].class);
        List<SgPais> lista = Arrays.asList(codiguera);
        return lista;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgDepartamento> buscarDepartamento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/buscar");
        SgDepartamento[] codiguera = restClient.invokePost(webTarget, filtro, SgDepartamento[].class);
        List<SgDepartamento> lista = Arrays.asList(codiguera);
        return lista;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgMunicipio> buscarMunicipio(FiltroMunicipio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/buscar");
        SgMunicipio[] codiguera = restClient.invokePost(webTarget, filtro, SgMunicipio[].class);
        List<SgMunicipio> lista = Arrays.asList(codiguera);
        return lista;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCanton> buscarCanton(FiltroCanton filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/buscar");
        SgCanton[] codiguera = restClient.invokePost(webTarget, filtro, SgCanton[].class);
        List<SgCanton> lista = Arrays.asList(codiguera);
        return lista;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgZona> buscarZona(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/buscar");
        SgZona[] codiguera = restClient.invokePost(webTarget, filtro, SgZona[].class);
        List<SgZona> lista = Arrays.asList(codiguera);
        return lista;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgJornadaLaboral> buscarJornadasLaborales(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/buscar");
        SgJornadaLaboral[] codiguera = restClient.invokePost(webTarget, filtro, SgJornadaLaboral[].class);
        List<SgJornadaLaboral> list = Arrays.asList(codiguera);
        return list;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgModalidadEstudio> buscarModalidadEstudio(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesestudio/buscar");
        SgModalidadEstudio[] codiguera = restClient.invokePost(webTarget, filtro, SgModalidadEstudio[].class);
        List<SgModalidadEstudio> list = Arrays.asList(codiguera);
        return list;
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgEspecialidad obtenerPorIdEspecialidad(Long especialidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (especialidadPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades");
        webTarget = webTarget.path(especialidadPk.toString());
        return restClient.invokeGet(webTarget, SgEspecialidad.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgDepartamento obtenerPorIdDepartamento(Long Pk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (Pk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
        webTarget = webTarget.path(Pk.toString());
        return restClient.invokeGet(webTarget, SgDepartamento.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgMunicipio obtenerPorIdMunicipio(Long municipioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (municipioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios");
        webTarget = webTarget.path(municipioPk.toString());
        return restClient.invokeGet(webTarget, SgMunicipio.class);
    }
    
    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCanton obtenerPorIdCanton(Long cantonPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (cantonPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones");
        webTarget = webTarget.path(cantonPk.toString());
        return restClient.invokeGet(webTarget, SgCanton.class);
    }

    @Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgZona obtenerPorIdZona(Long zonaPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (zonaPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas");
        webTarget = webTarget.path(zonaPk.toString());
        return restClient.invokeGet(webTarget, SgZona.class);
    }    
    
}
