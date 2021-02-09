/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.EstGenerica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadisticas;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Timeout(value = 45000L)
public class EstadisticasEstudiantesRestClient implements Serializable {

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

    public EstadisticasEstudiantesRestClient() {
    }

    public List<EstGenerica> poblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/poblacionnoescolarizadaporedad");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajePoblacionNoEscolarizadaPorEdad(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajepoblacionnoescolarizadaporedad");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeEstudiantesDesertores(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajeestudiantesdesertores");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> matriculaPorNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/matriculaporniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaBrutaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasabrutaingresoprimergradoeducacionbasica");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeRepetidores(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajederepetidores");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaDeRepeticion(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasaderepeticion");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaTransicionPorCiclo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasatransicionporciclo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaTransicionPorNivel(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasatransicionpornivel");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaDesercion(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasadesercion");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeTrabajadores(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajedetrabajadores");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeConDiscapacidad(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajecondiscapacidad");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> distribucionPorcentualEstudiantesConDiscapacidad(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/distribucionporcentualestudiantescondiscapacidad");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }
    
    
    public List<EstGenerica> distribucionPorcentualEstudiantesSegunConvivenciaFamiliar(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/distribucionporcentualestudiantessegunconvivenciafamiliar");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }
    

    
    public List<EstGenerica> tasaNetaIngresoPrimerGradoEducacionBasica(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasanetaingresoprimergradoeducacionbasica");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaEspecíficaDeEscolarizaciónPorEdad(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasaespecificadeescolarizacionporedad");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeEstudiantesDePrimerGradoConExperienciaEnEdeucacionParvularia(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajedeestudiantesdeprimergradoconexperienciaeneducacionparvularia");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> distribucionPorcentualDeLaMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/distribucionporcentualdelamatriculaporniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaBrutaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasabrutadematriculaporniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaNetaDeMatriculaPorNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasanetadematriculaporniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> distribucionPorcentualDeEstudiantesSegunActividadLaboral(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/distribucionporcentualdeestudiantessegunactividadlaboral");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> estudiantesPorSeccion(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/estudiantesporseccion");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeEstudiantesAprobados(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajeestudiantesaprobados");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaBrutaAprobacion(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasabrutaaprobacion");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaPromocion(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasapromocion");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> centrosEducativosSegunNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/centroseducativossegunniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeEstudiantesConAccesoAInternet(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajedeestudiantesconaccesoainternet");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeEstudiantesConAccesoAComputadora(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajedeestudiantesconaccesoacomputadora");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaBrutaIngresoPorGrado(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasabrutaingresoporgrado");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeConSobreedad(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajeconsobreedad");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> tasaEspecificaMatriculaPorGrado(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/tasaespecificamatriculaporgrado");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> distribucionPorcentualDocentesSegunAniosServicio(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/distribucionporcentualdocentessegunaniosservicio");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> promedioEstudiantesPorDocente(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/promedioestudiantespordocente");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeDocentesPorGradoAcademicoAlcanzado(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajededocentesporgradoacademicoalcanzado");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeDocentesConAccesoAInternet(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajededocentesconaccesoainternet");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeCentrosEducativosConAccesoServiciosBasicos(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajedecentroseducativosconaccesoserviciosbasicos");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> porcentajeDeDocentesCertificadosPorNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/porcentajededocentescertificadosporniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }

    
    public List<EstGenerica> distribucionDeDocentesSegunNivelEducativo(FiltroEstadisticas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.ESTADISTICAS, "v1/estadisticasestudiantes/distribuciondedocentessegunniveleducativo");
        EstGenerica[] estadisticaEstudiante = restClient.invokePost(webTarget, filtro, EstGenerica[].class);
        return Arrays.asList(estadisticaEstudiante);
    }
}
