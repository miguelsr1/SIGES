/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.restclient.centros_educativos;

import sv.gob.mined.siges.web.constantes.ConstantesServiciosRest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import sv.gob.mined.siges.web.dto.activo_fijo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.activo_fijo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centros_educativos.SgNacionalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.dto.catalogo.SgCanton;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgCaserio;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgClasificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.web.dto.catalogo.SgEtnia;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgOcupacion;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgProfesion;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.dto.catalogo.SgSistemaGestionContenido;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoManifestacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoNombramiento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoSangre;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTelefono;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.dto.centros_educativos.SgGlosario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCaserio;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroModalidadAtencion;
import sv.gob.mined.siges.web.restclient.RestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@Bulkhead
@CircuitBreaker
@Retry(maxRetries = 2, retryOn = {HttpServerUnavailableException.class, HttpServerException.class}, delay = 1000)
@Timeout(value = 10000L)
public class CatalogosRestClient implements Serializable {

    @Inject
    @Named("userToken")
    private String userToken;

    public CatalogosRestClient() {
    }

    public List<SgModalidadAtencion> buscarModalidadAtencion(FiltroModalidadAtencion filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion/buscar");
        SgModalidadAtencion[] modalidadesAtencion = RestClient.invokePost(webTarget, filtro, SgModalidadAtencion[].class, userToken);
        return Arrays.asList(modalidadesAtencion);
    }

    public SgModalidadAtencion obtenerPorIdModalidadAtencion(Long modalidadAtencionPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (modalidadAtencionPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/modalidadesatencion");
        webTarget = webTarget.path(modalidadAtencionPk.toString());
        return RestClient.invokeGet(webTarget, SgModalidadAtencion.class, userToken);
    }

    public List<SgSubModalidadAtencion> buscarSubModalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/buscar");
        SgSubModalidadAtencion[] subModalidad = RestClient.invokePost(webTarget, filtro, SgSubModalidadAtencion[].class, userToken);
        return Arrays.asList(subModalidad);
    }

    public Long buscarTotalSubModalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public SgSubModalidadAtencion obtenerPorIdSubModalidad(Long subModalidadesPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (subModalidadesPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/submodalidades");
        webTarget = webTarget.path(subModalidadesPk.toString());
        return RestClient.invokeGet(webTarget, SgSubModalidadAtencion.class, userToken);
    }

    public List<SgProgramaEducativo> buscarProgramasEducativos(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/programaseducativos/buscar");
        SgProgramaEducativo[] programasEducativos = RestClient.invokePost(webTarget, filtro, SgProgramaEducativo[].class, userToken);
        return Arrays.asList(programasEducativos);
    }

    public List<SgClasificacion> buscarClasificacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/clasificaciones/buscar");
        SgClasificacion[] codiguera = RestClient.invokePost(webTarget, filtro, SgClasificacion[].class, userToken);
        List<SgClasificacion> se = Arrays.asList(codiguera);
        return se;
    }

    public List<SgSexo> buscarSexo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sexos/buscar");
        SgSexo[] codiguera = RestClient.invokePost(webTarget, filtro, SgSexo[].class, userToken);
        List<SgSexo> se = Arrays.asList(codiguera);
        return se;
    }

    public List<SgEtnia> buscarEtnia(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/etnias/buscar");
        SgEtnia[] codiguera = RestClient.invokePost(webTarget, filtro, SgEtnia[].class, userToken);
        List<SgEtnia> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgEstadoCivil> buscarEstadoCivil(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/estadoscivil/buscar");
        SgEstadoCivil[] codiguera = RestClient.invokePost(webTarget, filtro, SgEstadoCivil[].class, userToken);
        List<SgEstadoCivil> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgTipoTelefono> buscarTipoTelefono(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostelefono/buscar");
        SgTipoTelefono[] codiguera = RestClient.invokePost(webTarget, filtro, SgTipoTelefono[].class, userToken);
        List<SgTipoTelefono> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgTipoIdentificacion> buscarTipoIdentificacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposidentificacion/buscar");
        SgTipoIdentificacion[] codiguera = RestClient.invokePost(webTarget, filtro, SgTipoIdentificacion[].class, userToken);
        List<SgTipoIdentificacion> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgPais> buscarPais(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/paises/buscar");
        SgPais[] codiguera = RestClient.invokePost(webTarget, filtro, SgPais[].class, userToken);
        List<SgPais> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgDepartamento> buscarDepartamento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos/buscar");
        SgDepartamento[] codiguera = RestClient.invokePost(webTarget, filtro, SgDepartamento[].class, userToken);
        List<SgDepartamento> lista = Arrays.asList(codiguera);
        return lista;
    }

    public SgDepartamento obtenerPorIdDepartamento(Long departamentoPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (departamentoPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/departamentos");
        webTarget = webTarget.path(departamentoPk.toString());
        return RestClient.invokeGet(webTarget, SgDepartamento.class, userToken);
    }

    public List<SgMunicipio> buscarMunicipio(FiltroMunicipio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/municipios/buscar");
        SgMunicipio[] codiguera = RestClient.invokePost(webTarget, filtro, SgMunicipio[].class, userToken);
        List<SgMunicipio> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgCanton> buscarCanton(FiltroCanton filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cantones/buscar");
        SgCanton[] codiguera = RestClient.invokePost(webTarget, filtro, SgCanton[].class, userToken);
        List<SgCanton> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgCaserio> buscarCaserio(FiltroCaserio filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/caserios/buscar");
        SgCaserio[] codiguera = RestClient.invokePost(webTarget, filtro, SgCaserio[].class, userToken);
        List<SgCaserio> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgZona> buscarZona(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/zonas/buscar");
        SgZona[] codiguera = RestClient.invokePost(webTarget, filtro, SgZona[].class, userToken);
        List<SgZona> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgTipoManifestacion> buscarTipoManifestacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposmanifestacion/buscar");
        SgTipoManifestacion[] codiguera = RestClient.invokePost(webTarget, filtro, SgTipoManifestacion[].class, userToken);
        List<SgTipoManifestacion> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgJornadaLaboral> buscarJornadasLaborales(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/jornadaslaborales/buscar");
        SgJornadaLaboral[] codiguera = RestClient.invokePost(webTarget, filtro, SgJornadaLaboral[].class, userToken);
        List<SgJornadaLaboral> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgCargo> buscarCargo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/cargo/buscar");
        SgCargo[] codiguera = RestClient.invokePost(webTarget, filtro, SgCargo[].class, userToken);
        List<SgCargo> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgTipoNombramiento> buscarTipoNombramiento(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiponombramiento/buscar");
        SgTipoNombramiento[] codiguera = RestClient.invokePost(webTarget, filtro, SgTipoNombramiento[].class, userToken);
        List<SgTipoNombramiento> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgSistemaGestionContenido> buscarSGC(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/sistemasgestioncontenido/buscar");
        SgSistemaGestionContenido[] codiguera = RestClient.invokePost(webTarget, filtro, SgSistemaGestionContenido[].class, userToken);
        List<SgSistemaGestionContenido> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgEscolaridad> buscarEscolaridad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/escolaridades/buscar");
        SgEscolaridad[] codiguera = RestClient.invokePost(webTarget, filtro, SgEscolaridad[].class, userToken);
        List<SgEscolaridad> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgEspecialidad> buscarEspecialidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/especialidades/buscar");
        SgEspecialidad[] codiguera = RestClient.invokePost(webTarget, filtro, SgEspecialidad[].class, userToken);
        List<SgEspecialidad> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgProfesion> buscarProfesion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/profesiones/buscar");
        SgProfesion[] codiguera = RestClient.invokePost(webTarget, filtro, SgProfesion[].class, userToken);
        List<SgProfesion> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgTipoTrabajo> buscarTipoTrabajo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipostrabajo/buscar");
        SgTipoTrabajo[] codiguera = RestClient.invokePost(webTarget, filtro, SgTipoTrabajo[].class, userToken);
        List<SgTipoTrabajo> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgTipoSangre> buscarTipoSangre(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tipossangre/buscar");
        SgTipoSangre[] codiguera = RestClient.invokePost(webTarget, filtro, SgTipoSangre[].class, userToken);
        List<SgTipoSangre> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgOcupacion> buscarOcupacion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ocupaciones/buscar");
        SgOcupacion[] codiguera = RestClient.invokePost(webTarget, filtro, SgOcupacion[].class, userToken);
        List<SgOcupacion> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgConfiguracion> buscarConfiguracion(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/configuraciones/buscar");
        SgConfiguracion[] codiguera = RestClient.invokePost(webTarget, filtro, SgConfiguracion[].class, userToken);
        List<SgConfiguracion> list = Arrays.asList(codiguera);
        return list;
    }

    public List<SgMotivoRetiro> buscarMotivoRetiro(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/motivosretiro/buscar");
        SgMotivoRetiro[] motivosRetiro = RestClient.invokePost(webTarget, filtro, SgMotivoRetiro[].class, userToken);
        return Arrays.asList(motivosRetiro);
    }

    public List<SgTipoParentesco> buscarTipoParentesco(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/tiposparentesco/buscar");
        SgTipoParentesco[] tiposParentesco = RestClient.invokePost(webTarget, filtro, SgTipoParentesco[].class, userToken);
        return Arrays.asList(tiposParentesco);
    }

    public List<SgNacionalidad> buscarNacionalidad(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/nacionalidades/buscar");
        SgNacionalidad[] nacionalidades = RestClient.invokePost(webTarget, filtro, SgNacionalidad[].class, userToken);
        return Arrays.asList(nacionalidades);
    }

    public List<SgAyuda> buscarAyudas(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/ayudas/buscar");
        SgAyuda[] ayudas = RestClient.invokePost(webTarget, filtro, SgAyuda[].class, userToken);
        return Arrays.asList(ayudas);
    }

    public Long buscarGlosariosTotal(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return 0L;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/glosarios/buscar/total");
        return RestClient.invokePost(webTarget, filtro, Long.class, userToken);
    }

    public List<SgGlosario> buscarGlosarios(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/glosarios/buscar");
        SgGlosario[] glosarios = RestClient.invokePost(webTarget, filtro, SgGlosario[].class, userToken);
        return Arrays.asList(glosarios);
    }

    public List<SgAfUnidadesActivoFijo> buscarUnidadesActivoFijo(FiltroCodiguera filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAF/buscar");
        SgAfUnidadesActivoFijo[] codiguera = RestClient.invokePost(webTarget, filtro, SgAfUnidadesActivoFijo[].class, userToken);
        List<SgAfUnidadesActivoFijo> lista = Arrays.asList(codiguera);
        return lista;
    }

    public List<SgAfUnidadesAdministrativas> buscarUnidadesAdministrativos(FiltroUnidadesAdministrativas filtro) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (filtro == null || userToken == null) {
            return new ArrayList<>();
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD/buscar");
        SgAfUnidadesAdministrativas[] codiguera = RestClient.invokePost(webTarget, filtro, SgAfUnidadesAdministrativas[].class, userToken);
        List<SgAfUnidadesAdministrativas> lista = Arrays.asList(codiguera);
        return lista;
    }

    public SgAfUnidadesAdministrativas obtenerPorIdUnidadesAdministrativas(Long unidadPk) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if (unidadPk == null || userToken == null) {
            return null;
        }
        WebTarget webTarget = RestClient.getWebTarget(ConstantesServiciosRest.SERVICIO_CATALOGO, "v1/unidadesAD");
        webTarget = webTarget.path(unidadPk.toString());
        return RestClient.invokeGet(webTarget, SgAfUnidadesAdministrativas.class, userToken);
    }
}
