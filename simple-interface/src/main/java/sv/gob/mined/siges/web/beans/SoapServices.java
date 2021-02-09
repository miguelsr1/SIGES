/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SpUnicaSedeWrapper;
import sv.gob.mined.siges.web.dto.SgCanton;
import sv.gob.mined.siges.web.dto.SgCentroEducativoOficial;
import sv.gob.mined.siges.web.dto.SgCentroEducativoPrivado;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.dto.SgDepartamento;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.SgMotivoLicencia;
import sv.gob.mined.siges.web.dto.SgMotivoPermuta;
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.SgRecursoEducativo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.seguridad.SgUsuario;
import sv.gob.mined.siges.web.dto.SgZona;
import sv.gob.mined.siges.web.dto.seguridad.SgUsuarioRol;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModalidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOpcionProgEd;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuarioRol;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CentroEducativoOficialRestClient;
import sv.gob.mined.siges.web.restclient.CentroEducativoPrivadoRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.web.restclient.PlanesEstudioRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.RelOpcionProgEdRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRolRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
@WebService
public class SoapServices implements Serializable {

    @Inject
    private CentroEducativoPrivadoRestClient cepClient;

    @Inject
    private CentroEducativoOficialRestClient ceoClient;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private NivelRestClient nivelClient;

    @Inject
    private CicloRestClient cicloClient;

    @Inject
    private ModalidadRestClient modalidadClient;

    @Inject
    private OpcionRestClient opcionClient;

    @Inject
    private RelOpcionProgEdRestClient relOpcionProgEdClient;

    @Inject
    private RelModEdModAtenRestClient relModEdModAtenClient;

    @Inject
    private PlanesEstudioRestClient planesEstudioClient;
    
    @Inject
    private GradoRestClient gradoClient;

    @Inject
    private ServicioEducativoRestClient servicioEducativoClient;

    @Inject
    private OrganizacionCurricularRestClient orgCurricularClient;

    @Inject
    private SeguridadRestClient seguridadClient;

    @Inject
    private UsuarioRestClient usuarioClient;
    
    @Inject
    private UsuarioRolRestClient usuarioRolClient;

    public SoapServices() {
    }

    @PostConstruct
    private void init() {
    }

    @WebMethod
    public List<SgSede> obtenerSedes(FiltroSedes filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        if (filtro.getIncluirCampos() == null) {
            filtro.setIncluirCampos(new String[]{"sedNombre", "sedCodigo", "sedTipo", "sedVersion"});
        }
        return sedeClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgOrganizacionCurricular> obtenerOrganizacionesCurriculares(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        if (filtro.getIncluirCampos() == null) {
            filtro.setIncluirCampos(new String[]{"ocuNombre", "ocuCodigo", "ocuVersion"});
        }
        return orgCurricularClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgNivel> obtenerNiveles(FiltroNivel filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        if (filtro.getIncluirCampos() == null) {
            filtro.setIncluirCampos(new String[]{"nivNombre", "nivCodigo", "nivVersion", "nivOrganizacionCurricular.ocuNombre"});
        }
        return nivelClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgCiclo> obtenerCiclos(FiltroCiclo filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return cicloClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgModalidad> obtenerModalidades(FiltroModalidad filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return modalidadClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgOpcion> obtenerOpciones(FiltroOpciones filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        List<SgOpcion> ops = opcionClient.buscar(filtro, userToken);
        if (ops.isEmpty()) {
            SgOpcion o = new SgOpcion();
            o.setOpcPk(-1L);
            o.setOpcCodigo("NA");
            o.setOpcNombre("No aplica");
            ops.add(o);
        }
        return ops;
    }

    @WebMethod
    public List<SgProgramaEducativo> obtenerProgramasEducativos(FiltroRelOpcionProgEd filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        List<SgProgramaEducativo> progs = relOpcionProgEdClient.buscar(filtro, userToken).stream().map(rel -> rel.getRoeProgramaEducativo())
                .sorted(Comparator.comparing(SgProgramaEducativo::getPedNombre, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        if (progs.isEmpty()) {
            SgProgramaEducativo p = new SgProgramaEducativo();
            p.setPedPk(-1L);
            p.setPedCodigo("NA");
            p.setPedNombre("No aplica");
            progs.add(p);
        }
        return progs;
    }

    @WebMethod
    public List<SgModalidadAtencion> obtenerModalidadesAtencion(FiltroRelModEdModAten filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return relModEdModAtenClient.buscar(filtro, userToken).stream()
                .map(rel -> rel.getReaModalidadAtencion())
                .sorted(Comparator.comparing(SgModalidadAtencion::getMatNombre, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        
    }

    @WebMethod
    public List<SgSubModalidadAtencion> obtenerSubModalidadesAtencion(FiltroRelModEdModAten filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        List<SgSubModalidadAtencion> ret = relModEdModAtenClient.buscar(filtro, userToken).stream().map(rel -> rel.getReaSubModalidadAtencion())
                .filter(s -> s != null)
                .sorted(Comparator.comparing(SgSubModalidadAtencion::getSmoNombre, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        if (ret.isEmpty()) {
            SgSubModalidadAtencion o = new SgSubModalidadAtencion();
            o.setSmoPk(-1L);
            o.setSmoCodigo("NA");
            o.setSmoNombre("No aplica");
            ret.add(o);
        }
        return ret;
    }
    
    @WebMethod
    public List<SgPlanEstudio> obtenerPlanesEstudio(FiltroPlanEstudio filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        if (filtro.getProgramaEducativo() != null && filtro.getProgramaEducativo() < 1L){
            filtro.setProgramaEducativo(null);
        }
        if (filtro.getOpcion() != null && filtro.getOpcion() < 1L){
            filtro.setOpcion(null);
        }
        if (filtro.getGradoPk() != null && filtro.getGradoPk() < 1L){
            filtro.setGradoPk(null);
        }
        return planesEstudioClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgGrado> obtenerGrados(FiltroGrado filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        if (filtro.getSubModAtencionPk() != null && filtro.getSubModAtencionPk().equals(-1L)){
            filtro.setSubModAtencionPk(null);
        }
        return gradoClient.buscar(filtro, userToken);
    }

    @WebMethod
    public SgSede guardarCentroEducativoOficial(SgCentroEducativoOficial entity, @WebParam(name = "userCode") String userCode) throws BusinessException, Exception {
        entity.setCedLegalizado(Boolean.FALSE);
        entity.setSedHabilitado(Boolean.TRUE);
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return ceoClient.guardar(entity, userToken);
    }

    @WebMethod
    public SgSede guardarCentroEducativoPrivado(SgCentroEducativoPrivado entity, @WebParam(name = "userCode") String userCode) throws BusinessException, Exception {
        entity.setCedLegalizado(Boolean.FALSE);
        entity.setSedHabilitado(Boolean.TRUE);
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return cepClient.guardar(entity, userToken);
    }

    @WebMethod
    public SgServicioEducativo guardarServicioEducativo(SgServicioEducativo entity, @WebParam(name = "userCode") String userCode) throws BusinessException, Exception {
        if (entity.getSduOpcion() != null && entity.getSduOpcion().getOpcPk().equals(-1L)) {
            entity.setSduOpcion(null);
        }
        if (entity.getSduProgramaEducativo() != null && entity.getSduProgramaEducativo().getPedPk().equals(-1L)) {
            entity.setSduProgramaEducativo(null);
        }
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return servicioEducativoClient.guardar(entity, userToken);
    }

    @WebMethod
    public SgUsuario obtenerUsuarioPorCodigo(@WebParam(name = "userCode") String userCode) throws BusinessException, Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        return usuarioClient.obtenerPorCodigo(userCode, userToken);
    }

    @WebMethod
    public List<SgJornadaLaboral> obtenerJornadasLaborales(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"jlaNombre", "jlaCodigo", "jlaVersion"});
        return catalogosClient.buscarJornadasLaborales(filtro, userToken);
    }

    @WebMethod
    public List<SgZona> obtenerZonas(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"zonNombre", "zonCodigo", "zonVersion"});
        return catalogosClient.buscarZona(filtro, userToken);
    }

    @WebMethod
    public List<SgDepartamento> obtenerDepartamentos(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"depNombre", "depCodigo", "depVersion"});
        return catalogosClient.buscarDepartamento(filtro, userToken);
    }

    @WebMethod
    public List<SgMunicipio> obtenerMunicipios(FiltroMunicipio filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"munNombre", "munCodigo", "munVersion"});
        return catalogosClient.buscarMunicipio(filtro, userToken);
    }

    @WebMethod
    public List<SgCanton> obtenerCantones(FiltroCanton filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"canNombre", "canCodigo", "canVersion"});
        return catalogosClient.buscarCanton(filtro, userToken);
    }

    @WebMethod
    public List<SgTipoCalendario> obtenerTiposCalendario(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"tceCodigo", "tceNombre", "tceVersion"});
        return catalogosClient.buscar(filtro, userToken);
    }

    @WebMethod
    public List<SgRecursoEducativo> obtenerRecursosEducativos(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"tceCodigo", "tceNombre", "tceVersion"});
        return catalogosClient.buscarRecursosEducativos(filtro, userToken);
    }

    @WebMethod
    public List<SgMotivoLicencia> obtenerMotivosLicencia(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"tceCodigo", "tceNombre", "tceVersion"});
        return catalogosClient.buscarMotivosLicencia(filtro, userToken);
    }

    @WebMethod
    public List<SgMotivoPermuta> obtenerMotivosPermuta(FiltroCodiguera filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        filtro.setIncluirCampos(new String[]{"tceCodigo", "tceNombre", "tceVersion"});
        return catalogosClient.buscarMotivosPermuta(filtro, userToken);
    }

    @WebMethod
    public SpUnicaSedeWrapper usuarioTieneUnicaSede(@WebParam(name = "userCode") String userCode) throws Exception {
        FiltroSedes filtro = new FiltroSedes();
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", seguridadClient.obtenerOperacionesPorCodigoUsuario(userCode), 20);
        SpUnicaSedeWrapper cw = new SpUnicaSedeWrapper();
        filtro.setMaxResults(2L);
        filtro.setIncluirCampos(new String[]{"sedNombre", "sedCodigo", "sedTipo", "sedVersion",
            "sedDireccion.dirDepartamento.depPk", "sedDireccion.dirDepartamento.depNombre",
            "sedDireccion.dirMunicipio.munPk", "sedDireccion.dirMunicipio.munNombre"});
        List<SgSede> sedes = sedeClient.buscar(filtro, userToken);
        if (sedes.size() == 1) {
            cw.setSede(sedes.get(0));
            cw.setCodigo("1");
        } else {
            cw.setCodigo("0");
        }
        return cw;
    }

    @WebMethod
    public String obtenerConfiguracion(@WebParam(name = "code") String configCode, @WebParam(name = "userCode") String userCode) throws Exception {
        if (StringUtils.isNotBlank(configCode)) {
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setCodigoExacto(configCode);
            String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
            List<SgConfiguracion> configs = catalogosClient.buscarConfiguraciones(filtro, userToken);
            if (!configs.isEmpty()) {
                return configs.get(0).getConValor();
            }
        }
        return null;
    }
    
    
    @WebMethod
    public List<SgUsuarioRol> obtenerRolesUsuario(FiltroUsuarioRol filtro, @WebParam(name = "userCode") String userCode) throws Exception {
        String userToken = JWTUtils.generarToken(userCode, "SIMPLE", "/privateKey.pem", Arrays.asList(new String[]{ConstantesOperaciones.AUTENTICADO}), 20);
        if (filtro.getIncluirCampos() == null || filtro.getIncluirCampos().length == 0){
            throw new Exception("Utilizar incluir campos");
        }
        return usuarioRolClient.buscarFiltroUsuarioRol(filtro, userToken);
    }

}
