/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.graficos.GraficoColumnas;
import sv.gob.mined.siges.web.graficos.GraficoRadar;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 15000L)
public class GraficosRestClient implements Serializable {

    @Inject
    private RestClient restClient;

    private Client client = null;

    @PostConstruct
    public void init() {
        client = restClient.getClient();
    }

    @PreDestroy
    public void preDestroy() {
        if (client != null) {
            //client.close();
        }
    }

    public GraficosRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_EVOLUCION_ENCUESTAS_MATRICULAS)
    public GraficoColumnas obtenerEvolucionEncuestasYMatriculas(Long depPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/encuestascontramatriculas");
        if (depPk != null) {
            webTarget = webTarget.queryParam("depPk", depPk);
        }
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_EVOLUCION_ENCUESTAS_SEXO)
    public GraficoColumnas obtenerEvolucionEncuestasPorSexo(Long depPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/encuestasporsexo");
        if (depPk != null) {
            webTarget = webTarget.queryParam("depPk", depPk);
        }
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_ALERTAS_ESTUDIANTE)
    public GraficoRadar obtenerAlertasEstudiante(Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/alertas");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        return restClient.invokeGet(webTarget, GraficoRadar.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_ASISTENCIAS_ESTUDIANTE_POR_ANIO)
    public GraficoColumnas obtenerAsistenciasEstudiantePorAnio(Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/asistencias/poranio");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_ASISTENCIAS_ESTUDIANTE_POR_MES_EN_ANIO)
    public GraficoColumnas obtenerAsistenciasPorMesEnAnio(Long estudiantePk, Integer anio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/asistencias/pormes");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        webTarget = webTarget.queryParam("anio", anio);
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_MOT_INASISTENCIAS_ESTUDIANTE_POR_ANIO)
    public GraficoColumnas obtenerMotivosInasistenciasPorMesEnAnio(Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/motinasistencias/poranio");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_MOT_INASISTENCIAS_ESTUDIANTE_POR_MES_EN_ANIO)
    public GraficoColumnas obtenerMotivosInasistenciasPorMesEnAnio(Long estudiantePk, Integer anio) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/motinasistencias/pormes");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        webTarget = webTarget.queryParam("anio", anio);
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_CALIFICACIONES_APR_NUMERICAS_ESTUDIANTE_POR_GRADO_CONTRA_PROMEDIO_NACIONAL)
    public GraficoColumnas obtenerCalificacionesNumericasAprobacionEstudiantePorGradoComparadoContraPromedioNacionalYSede(Long estudiantePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/calificaciones/aprnumericasporgradocontrapromedionacionalysede");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_CALIFICACIONES_ORD_ACTUALES_NUMERICAS_ESTUDIANTE_POR_GRADO_Y_COMPONENTE)
    public GraficoColumnas obtenerCalificacionesNumericasOrdinariasActualesEstudiantePorGradoYComponente(Long estudiantePk, Long componentePlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null || componentePlanEstudioPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/calificaciones/ordnumericasporgradoycomponente");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        webTarget = webTarget.queryParam("componentePlanEstudioPk", componentePlanEstudioPk);
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_CALIFICACIONES_APR_NUMERICAS_ESTUDIANTE_POR_GRADO_Y_COMPONENTE)
    public GraficoColumnas obtenerCalificacionesAPRNumericasEstudiantePorGradoYComponente(Long estudiantePk, Long componentePlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/calificaciones/aprnumericasporgradoycomponente");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        if (componentePlanEstudioPk != null) {
            webTarget = webTarget.queryParam("componentePlanEstudioPk", componentePlanEstudioPk);
        }
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @CacheResult(cacheName = Constantes.GRAFICO_CALIFICACIONES_APR_CONCEPTUALES_ESTUDIANTE_POR_GRADO_Y_COMPONENTE)
    public GraficoColumnas obtenerCalificacionesAPRConceptualesEstudiantePorGradoYComponente(Long estudiantePk, Long componentePlanEstudioPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (estudiantePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/graficos/calificaciones/aprconceptualesporgradoycomponente");
        webTarget = webTarget.queryParam("estPk", estudiantePk);
        if (componentePlanEstudioPk != null) {
            webTarget = webTarget.queryParam("componentePlanEstudioPk", componentePlanEstudioPk);
        }
        return restClient.invokeGet(webTarget, GraficoColumnas.class);
    }

}
