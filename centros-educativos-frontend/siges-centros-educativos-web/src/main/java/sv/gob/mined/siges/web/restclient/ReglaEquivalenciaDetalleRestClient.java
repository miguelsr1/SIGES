/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.SgReglaEquivalenciaDetalle;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumOperacionReglaEquivalencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReglaEquivalenciaDetalle;

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
@Timeout(value = 10000L)
public class ReglaEquivalenciaDetalleRestClient implements Serializable {

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

    public ReglaEquivalenciaDetalleRestClient() {
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public List<SgReglaEquivalenciaDetalle> buscar(FiltroReglaEquivalenciaDetalle filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle/buscar");
        SgReglaEquivalenciaDetalle[] rangosFecha = restClient.invokePost(webTarget, filtro, SgReglaEquivalenciaDetalle[].class);
        return new ArrayList<>(Arrays.asList(rangosFecha));
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public Long buscarTotal(FiltroReglaEquivalenciaDetalle filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle/buscar/total");
        return restClient.invokePost(webTarget, filtro, Long.class);
    }

    public List<SgReglaEquivalenciaDetalle> buscarEquivalenciasPermitidas(SgGrado gradoOrigen, SgPlanEstudio planEstudioOrigen, SgOpcion opcionOrigen, SgProgramaEducativo programaOrigen) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        List<SgReglaEquivalenciaDetalle> ret = new ArrayList<>();

        if (gradoOrigen == null || planEstudioOrigen == null){
            return ret;
        }
        
        FiltroReglaEquivalenciaDetalle filtroEqDet = new FiltroReglaEquivalenciaDetalle();
        filtroEqDet.setHabilitado(Boolean.TRUE);
        filtroEqDet.setReglaHabilitada(Boolean.TRUE);
        filtroEqDet.setOperacion(EnumOperacionReglaEquivalencia.ORIGEN);

        filtroEqDet.setGradoId(gradoOrigen.getGraPk());
        filtroEqDet.setOpcion(opcionOrigen != null ? opcionOrigen.getOpcPk() :  null);
        filtroEqDet.setProgramaEducativo(programaOrigen != null ? programaOrigen.getPedPk(): null);
        filtroEqDet.setPlanEstudioId(planEstudioOrigen.getPesPk());

        filtroEqDet.setIncluirCampos(new String[]{"redReglaEquivalencia.reqPk", "redReglaEquivalencia.reqVersion"});
        List<SgReglaEquivalenciaDetalle> lreglasDetalle = this.buscar(filtroEqDet);

        if (!lreglasDetalle.isEmpty()) {
            //Se consultan los destinos permitidos
            filtroEqDet = new FiltroReglaEquivalenciaDetalle();
            filtroEqDet.setHabilitado(Boolean.TRUE);
            filtroEqDet.setReglaHabilitada(Boolean.TRUE);
            filtroEqDet.setReglaPk(lreglasDetalle.stream().map(c -> c.getRedReglaEquivalencia().getReqPk()).collect(Collectors.toList()));
            filtroEqDet.setOperacion(EnumOperacionReglaEquivalencia.DESTINO);
            filtroEqDet.setIncluirCampos(new String[]{
                "redPk", "redVersion",
                "redGrado.graPk",
                "redGrado.graVersion",
                "redGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                "redGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivVersion",
                "redGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk",
                "redGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicVersion",
                "redGrado.graRelacionModalidad.reaModalidadEducativa.modPk",
                "redGrado.graRelacionModalidad.reaModalidadEducativa.modVersion",
                "redGrado.graRelacionModalidad.reaModalidadAtencion.matPk",
                "redGrado.graRelacionModalidad.reaModalidadAtencion.matVersion",
                "redOpcion.opcPk",
                "redOpcion.opcVersion",
                "redProgramaEducativo.pedPk",
                "redProgramaEducativo.pedVersion"});
            ret = new ArrayList(this.buscar(filtroEqDet));
        }
        SgReglaEquivalenciaDetalle defecto = new SgReglaEquivalenciaDetalle();
        defecto.setRedGrado(gradoOrigen);
        defecto.setRedOpcion(opcionOrigen);
        defecto.setRedProgramaEducativo(programaOrigen);
        ret.add(defecto);

        return ret;
    }

    public SgReglaEquivalenciaDetalle guardar(SgReglaEquivalenciaDetalle regla) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (regla == null) {
            return null;
        }
        if (regla.getRedPk() == null) {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle");
            return restClient.invokePost(webTarget, regla, SgReglaEquivalenciaDetalle.class);
        } else {
            WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle");
            webTarget = webTarget.path(regla.getRedPk().toString());
            return restClient.invokePut(webTarget, regla, SgReglaEquivalenciaDetalle.class);
        }
    }

    @Retry(maxRetries = 1, retryOn = {HttpServerUnavailableException.class, HttpServerException.class, TimeoutException.class}, delay = 1000)
    public SgReglaEquivalenciaDetalle obtenerPorId(Long reglaDetallePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reglaDetallePk == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle");
        webTarget = webTarget.path(reglaDetallePk.toString());
        return restClient.invokeGet(webTarget, SgReglaEquivalenciaDetalle.class);
    }

    public void eliminar(Long reglaDetallePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reglaDetallePk == null) {
            return;
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle");
        webTarget = webTarget.path(reglaDetallePk.toString());
        restClient.invokeDelete(webTarget);
    }

    public List<RevHistorico> obtenerHistorialPorId(Long reglaDetallePk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (reglaDetallePk == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(client, ConstantesServiciosRest.SERVICIO_CENTROS_EDUCATIVOS, "v1/reglaeqdetalle/historial");
        webTarget = webTarget.path(reglaDetallePk.toString());
        RevHistorico[] reglas = restClient.invokeGet(webTarget, RevHistorico[].class);
        return Arrays.asList(reglas);
    }
}
