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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.SgArchivoBitMatricula;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgMatriculaParcial;
import sv.gob.mined.siges.web.dto.SgModificarFechasMatricula;
import sv.gob.mined.siges.web.dto.SgPromRepMatriculas;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.utilidades.ViewIdUtils;

@Named
@RequestScoped
@Traced
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class}, delay = 1000)
@Timeout(value = 20000L)
public class MatriculaRestClient implements Serializable {

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

    public MatriculaRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgMatricula> buscar(FiltroMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/buscar");
        SgMatricula[] matriculas = restClient.invokePost(webTarget, filtro, SgMatricula[].class);
        return new ArrayList<>(Arrays.asList(matriculas));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgMatriculaParcial> buscarMatriculasParciales(FiltroMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/parciales/buscar");
        SgMatriculaParcial[] matriculas = restClient.invokePost(webTarget, filtro, SgMatriculaParcial[].class);
        return new ArrayList<>(Arrays.asList(matriculas));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroMatricula filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public SgMatriculaParcial guardarParcial(SgMatriculaParcial matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/parcial");
        return restClient.invokePost(webTarget, matricula, SgMatriculaParcial.class);
    }

    @Timeout(value = 12000L)
    public SgMatricula guardar(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        ViewIdUtils.clearChildViewIds(matricula.getMatEstudiante());
        ViewIdUtils.clearChildViewIds(matricula.getMatEstudiante().getEstPersona());
        SgAllegado all = matricula.getMatEstudiante().getEstPersona().getPerResponsable();
        if (all != null) {
            ViewIdUtils.clearChildViewIds(all);
            ViewIdUtils.clearChildViewIds(matricula.getMatEstudiante().getEstPersona().getPerResponsable().getAllPersona());
        }
        if (matricula.getMatPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas");
            return restClient.invokePost(webTarget, matricula, SgMatricula.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas");
            webTarget = webTarget.path(matricula.getMatPk().toString());
            return restClient.invokePut(webTarget, matricula, SgMatricula.class);
        }
    }

    @Timeout(value = 12000L)
    public SgArchivoBitMatricula importar(SgArchivoBitMatricula lista) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (lista == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.FILE_PROCESSOR, "v1/matriculas/importar");
       return restClient.invokePost(webTarget, lista, SgArchivoBitMatricula.class);
    }

    public void cambiarSeccion(SgMatricula[][] matriculas) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matriculas == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/cambiar");
        restClient.invokePost(webTarget, matriculas, null);
    }

    public void cambioMasivoSeccion(SgMatricula[] matriculas) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matriculas == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/cambiomasivo");
        restClient.invokePost(webTarget, matriculas, null);
    }
    
    public void generarPromocionesYRepeticiones(SgPromRepMatriculas dto) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (dto == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/generarpromocionesyrepeticiones");
        restClient.invokePost(webTarget, dto, null);
    }

    public Boolean validar(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/validar");
        return restClient.invokePost(webTarget, matricula, Boolean.class);
    }
    
    public SgMatricula modificarFechas(SgModificarFechasMatricula mod) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (mod == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/modificarfechas");
        return restClient.invokePut(webTarget, mod, SgMatricula.class);
    }
    
    public SgMatricula retirar(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/retirar");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);
    }
    
    public SgMatricula anular(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/anular");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);
    }
    
    public SgMatricula reabrirMatricula(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/reapertura");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);
    }

    public SgMatricula validarMatriculaProvisional(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/matriculaprovisional");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);

    }

    public SgMatricula anularRetiro(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/anularretiro");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);
    }

    public SgMatricula validarAcademicamenteMatricula(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/validaracademicamente");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);
    }

    public SgMatricula anularValidacionAcademica(SgMatricula matricula) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matricula == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/anularvalidacionacademica");
        webTarget = webTarget.path(matricula.getMatPk().toString());
        return restClient.invokePut(webTarget, matricula, SgMatricula.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgMatricula obtenerPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        return restClient.invokeGet(webTarget, SgMatricula.class);
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgMatriculaParcial obtenerMatriculaParcialPorId(Long matPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matPk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/parciales");
        webTarget = webTarget.path(matPk.toString());
        return restClient.invokeGet(webTarget, SgMatriculaParcial.class);
    }

    public void eliminar(Long matPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas");
        webTarget = webTarget.path(matPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public void eliminarMatriculaParcial(Long matPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (matPk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/parciales");
        webTarget = webTarget.path(matPk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long organizacionCurricularPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (organizacionCurricularPk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS_MATRICULAS, "v1/matriculas/historial");
        webTarget = webTarget.path(organizacionCurricularPk.toString());
        RevHistorico[] organizacionesCurricular = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(organizacionesCurricular);
    }

}
