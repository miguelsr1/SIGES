/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgArchivoCalificaciones;
import sv.gob.mined.siges.web.dto.SgConsultaCalificacionesPendientesSedesEnNivel;
import sv.gob.mined.siges.web.dto.SgDatosCalculoCalificaciones;
import sv.gob.mined.siges.web.dto.SgDatosCalculoCalificacionesPromocion;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacionEstCal;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasRequest;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionesPendientesSedeEnNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacionEstCal;

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
@Timeout(value = 30000L)
public class CalificacionRestClient implements Serializable {

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

    public CalificacionRestClient() {
    }
    
    
            
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgPeriodoCalificacionEstCal buscarEstudiantesCalificacionesPeriodo(FiltroPeriodoCalificacionEstCal filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/buscarestudiantescalificacionesperiodo");
        SgPeriodoCalificacionEstCal p = restClient.invokePost(webTarget, filtro, SgPeriodoCalificacionEstCal.class);
        return p;
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgCalificacionCE> buscar(FiltroCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/buscar");
        SgCalificacionCE[] calificaciones = restClient.invokePost(webTarget, filtro, SgCalificacionCE[].class);
        return Arrays.asList(calificaciones);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroCalificacion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgCalificacionCE guardar(SgCalificacionCE calificacion) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacion == null) {
            return null;
        }
        if (calificacion.getCalPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones");
            return restClient.invokePost(webTarget, calificacion, SgCalificacionCE.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones");
            webTarget = webTarget.path(calificacion.getCalPk().toString());
            return restClient.invokePut(webTarget, calificacion, SgCalificacionCE.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgCalificacionCE obtenerPorId(Long calificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones");
        webTarget = webTarget.path(calificacionPk.toString());
        return restClient.invokeGet(webTarget, SgCalificacionCE.class);
    }

    public void eliminar(Long calificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones");
        webTarget = webTarget.path(calificacionPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long calificacionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {       
        if (calificacionPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/historial");
        webTarget = webTarget.path(calificacionPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 12000L)
    public SgCalificacionCE obtenerEnRevision(Long calificacionPk, Long calRev) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (calificacionPk == null || calRev == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/revision");
        webTarget = webTarget.path(calificacionPk.toString());
        webTarget = webTarget.path(calRev.toString());
        return restClient.invokeGet(webTarget, SgCalificacionCE.class);
    }
    
    @Timeout(value = 60000L)
    public SgCalificacionCE importar(SgArchivoCalificaciones lista) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (lista == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/calificaciones/importar");
       return restClient.invokePost(webTarget, lista, SgCalificacionCE.class);
    }
    
    public List<SgPorcentajeAsistenciasResponse> datosHabilitacionPeriodoExtraordinario(SgPorcentajeAsistenciasRequest filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/datosHabilitacionPeriodoExtraordinario");
        SgPorcentajeAsistenciasResponse[] pruebas = restClient.invokePost(webTarget, filtro, SgPorcentajeAsistenciasResponse[].class);
        return Arrays.asList(pruebas);
    }
    
    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    @Timeout(value = 30000L)
    public List<SgConsultaCalificacionesPendientesSedesEnNivel> obtenerCalificacionesPendientesPorSedeEnNivel(FiltroCalificacionesPendientesSedeEnNivel filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/obtenerpendientesporsedeennivel");
        SgConsultaCalificacionesPendientesSedesEnNivel[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, SgConsultaCalificacionesPendientesSedesEnNivel[].class);
        return Arrays.asList(estadisticaEstudiante);
    }
    
    public Boolean calcularNotaInstitucional(SgDatosCalculoCalificaciones datoCalculoNotaInst) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoCalculoNotaInst.getSeccion() == null || datoCalculoNotaInst.getComponentePlanGrado()==null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/calcularNotaInstitucional");
        Boolean estadisticaEstudiante = restClient.invokePost(webTarget, datoCalculoNotaInst, Boolean.class);
        return estadisticaEstudiante;
    }
    
    public Boolean calcularAprobacion(SgDatosCalculoCalificaciones datoCalculoNotaInst) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoCalculoNotaInst.getSeccion() == null || datoCalculoNotaInst.getComponentePlanGrado()==null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/calcularAprobacion");
        Boolean estadisticaEstudiante = restClient.invokePost(webTarget, datoCalculoNotaInst, Boolean.class);
        return estadisticaEstudiante;
    }
    
    @Timeout(value = 5000000L)
    public Boolean calcularPromocion(SgDatosCalculoCalificacionesPromocion datoCalculoNotaInst) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (datoCalculoNotaInst.getSeccion() == null || datoCalculoNotaInst.getEsAsnicronica()==null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_CALIFICACIONES, "v1/calificaciones/calcularPromocion");
        Boolean estadisticaEstudiante = restClient.invokePost(webTarget, datoCalculoNotaInst, Boolean.class);
        return estadisticaEstudiante;
    }
    
    @Timeout(value = 5000000L)
    public void ejecutarProcesamientoPromociones() throws HttpClientException, HttpServerException, HttpServerUnavailableException, BusinessException{
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/calificaciones/procesarPromociones");
        restClient.invokeGet(webTarget,null);
    }

}
