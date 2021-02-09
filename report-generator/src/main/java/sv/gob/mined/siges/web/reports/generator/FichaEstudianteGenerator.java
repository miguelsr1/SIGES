/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgIdentificacion;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTitulo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AllegadoRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class FichaEstudianteGenerator {

    @Inject
    private EstudianteRestClient estudianteClient;

    @Inject
    private MatriculaRestClient matriculaClient;

    @Inject
    private TituloRestClient tituloRestClient;

    @Inject
    private AllegadoRestClient allegadoClient;

    @Inject
    private PersonaRestClient personaClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private SessionBean sessionBean;

    public MasterReport generarReporte(Long estId) throws Exception {
        try {
            BusinessException be = new BusinessException();
            if (estId == null) {
                be.addError("Debes ingresar la id del estudiante.");
                throw be;
            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

           
            SgEstudiante est = estudianteClient.obtenerPorId(estId);

            FiltroMatricula filMat = new FiltroMatricula();
            filMat.setMatEstudiantePk(estId);
            filMat.setOrderBy(new String[]{"matFechaIngreso"});
            filMat.setAscending(new boolean[]{true});
            filMat.setIncluirCampos(new String[]{
                "matSeccion.secServicioEducativo.sduSede.sedCodigo",
                "matSeccion.secServicioEducativo.sduSede.sedNombre",
                "matSeccion.secServicioEducativo.sduSede.sedTipo",
                "matSeccion.secAnioLectivo.aleAnio",
                "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuPk",
                "matSeccion.secServicioEducativo.sduGrado.graNombre",
                "matSeccion.secNombre",
                "matPromocionGrado"
            });

            SgMatricula ultimaMat = null;
            List<SgMatricula> mats = matriculaClient.buscar(filMat);
            if (!mats.isEmpty()) {
                ultimaMat = mats.get(mats.size() - 1);
            }
            
            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_FICHA_ESTUDIANTE,
                        ultimaMat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_FICHA_ESTUDIANTE + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("ficha_estudiante.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            report.getParameterValues().put("codigo_sede", ultimaMat != null ? ultimaMat.getMatSeccion().getSecServicioEducativo().getSduSede().getSedCodigo() : "");
            report.getParameterValues().put("nombre_sede", ultimaMat != null ? ultimaMat.getMatSeccion().getSecServicioEducativo().getSduSede().getSedNombre() : "");
            report.getParameterValues().put("identificaciones", this.getEstudianteIdentificacionesAsString(est.getEstPersona()));
            report.getParameterValues().put("primer_nombre", est.getEstPersona().getPerPrimerNombre());
            report.getParameterValues().put("segundo_nombre", est.getEstPersona().getPerSegundoNombre());
            report.getParameterValues().put("tercer_nombre", est.getEstPersona().getPerTercerNombre());
            report.getParameterValues().put("primer_apellido", est.getEstPersona().getPerPrimerApellido());
            report.getParameterValues().put("segundo_apellido", est.getEstPersona().getPerSegundoApellido());
            report.getParameterValues().put("tercer_apellido", est.getEstPersona().getPerTercerApellido());
            report.getParameterValues().put("nombre_partida", est.getEstPersona().getPerNombrePartidaNacimiento());
            report.getParameterValues().put("nacionalidad", est.getEstPersona().getPerNacionalidad().getNacNombre());
            report.getParameterValues().put("fecha_nacimiento", Generales.getDateFormat(est.getEstPersona().getPerFechaNacimiento()));
            report.getParameterValues().put("naturalizado", BooleanUtils.isTrue(est.getEstPersona().getPerNaturalizada()) ? "Sí" : "No");
            report.getParameterValues().put("estado_familiar", est.getEstPersona().getPerEstadoCivil() != null ? est.getEstPersona().getPerEstadoCivil().getEciNombre() : "");
            report.getParameterValues().put("discapacidades", est.getEstPersona().getDiscapacidadesAsString());
            report.getParameterValues().put("trastornos_aprendizaje", est.getEstPersona().getTrastornosAprendizajeAsString());
            report.getParameterValues().put("estado", est.getEstPersona().getPerEstado() != null ? est.getEstPersona().getPerEstado().getText() : "");
            report.getParameterValues().put("sexo", est.getEstPersona().getPerSexo().getSexNombre());
            report.getParameterValues().put("correo_electronico", est.getEstPersona().getPerEmail());
            report.getParameterValues().put("telefonos", est.getEstPersona().getTelefonosAsString());
            report.getParameterValues().put("zona", est.getEstPersona().getPerDireccion().getDirZona().getZonNombre());
            report.getParameterValues().put("departamento", est.getEstPersona().getPerDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("municipio", est.getEstPersona().getPerDireccion().getDirMunicipio().getMunNombre());
            report.getParameterValues().put("direccion", est.getEstPersona().getPerDireccion().getDirDireccion());
            report.getParameterValues().put("trabaja", BooleanUtils.isTrue(est.getEstPersona().getPerTrabaja()) ? "Sí" : "No");
            report.getParameterValues().put("dependencia_economica", est.getEstDependenciaEconomica() != null ? est.getEstDependenciaEconomica().getDecNombre() : "");
            report.getParameterValues().put("ocupacion", est.getEstPersona().getPerOcupacion() != null ? est.getEstPersona().getPerOcupacion().getOcuNombre() : "");
            report.getParameterValues().put("cantidad_hijos", est.getEstPersona().getPerCantidadHijos() != null ? est.getEstPersona().getPerCantidadHijos().toString() : "");
            report.getParameterValues().put("medio_transporte", est.getEstMedioTransporte() != null ? est.getEstMedioTransporte().getMtrNombre() : "");
            report.getParameterValues().put("distancia_sede", est.getEstDisKmCentro() != null ? (est.getEstDisKmCentro() + " km") : "");
            report.getParameterValues().put("direccion_completa", 
                    est.getEstPersona().getPerDireccion().getDirDireccion()+ " - "+est.getEstPersona().getPerDireccion().getDirMunicipio().getMunNombre()+", "
                            + est.getEstPersona().getPerDireccion().getDirDepartamento().getDepNombre()+" - Zona "+est.getEstPersona().getPerDireccion().getDirZona().getZonNombre());

            report.getParameterValues().put("codigo_nombre_sede", ultimaMat != null ? ultimaMat.getMatSeccion().getSecServicioEducativo().getSduSede().getSedCodigoNombre() : "");
            
            FiltroTitulo filtroTit = new FiltroTitulo();
            filtroTit.setTitEstudiante(estId);
            filtroTit.setTitNoAnulada(Boolean.TRUE);
            filtroTit.setIncluirCampos(new String[]{
                "titNombreEstudiante",
                "titNombreEstudiantePartida",
                "titNombreCertificado",
                "titFechaValidez",
                "titFechaEmision",
                "titAnio",
                "titSedeFk.sedPk",
                "titSedeFk.sedTipo",
                "titSedeFk.sedCodigo",
                "titSedeFk.sedNombre",
                "titSedeCodigo",
                "titSedeNombre",
                "titNumeroRegistro",
                "titReposicion"
            });
            List<SgTitulo> tit = tituloRestClient.buscar(filtroTit);

            FiltroAllegado filtroAll = new FiltroAllegado();
            filtroAll.setAllPersonaReferenciadaPk(est.getEstPersona().getPerPk());
            filtroAll.setIncluirCampos(new String[]{
                "allReferente",
                "allTipoParentesco.tpaNombre",
                "allPersona.perPk",
                "allViveConAllegado",});
            List<SgAllegado> all = allegadoClient.buscar(filtroAll);
            for (SgAllegado a : all) {
                a.setAllPersona(personaClient.obtenerPorId(a.getAllPersona().getPerPk())); //Obener por id para que cargue identificaciones y telefonos
            }

            report.setDataFactory(this.getDataFactory(all, mats, tit));

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<SgAllegado> all, List<SgMatricula> mat, List<SgTitulo> tit) throws Exception {

        TypedTableModel model = new TypedTableModel();

        model.addColumn("familiares", TypedTableModel.class);
        model.addColumn("matriculas", TypedTableModel.class);
        model.addColumn("titulos", TypedTableModel.class);

        TypedTableModel familiares = new TypedTableModel();

        familiares.addColumn("identificaciones", String.class);
        familiares.addColumn("primer_nombre", String.class);
        familiares.addColumn("segundo_nombre", String.class);
        familiares.addColumn("primer_apellido", String.class);
        familiares.addColumn("segundo_apellido", String.class);
        familiares.addColumn("parentesco", String.class);
        familiares.addColumn("vive_con", String.class);
        familiares.addColumn("referente", String.class);
        familiares.addColumn("estado", String.class);
        familiares.addColumn("correo_electronico", String.class);
        familiares.addColumn("telefonos", String.class);
        familiares.addColumn("zona", String.class);
        familiares.addColumn("departamento", String.class);
        familiares.addColumn("municipio", String.class);
        familiares.addColumn("direccion", String.class);
        familiares.addColumn("profesion", String.class);
        familiares.addColumn("escolaridad", String.class);
        familiares.addColumn("ocupacion", String.class);
        familiares.addColumn("direccion_completa", String.class);

        for (SgAllegado a : all) {

            familiares.addRow(
                    this.getAllegadoIdentificacionesAsString(a.getAllPersona()),
                    a.getAllPersona().getPerPrimerNombre(),
                    a.getAllPersona().getPerSegundoNombre(),
                    a.getAllPersona().getPerPrimerApellido(),
                    a.getAllPersona().getPerSegundoApellido(),
                    a.getAllTipoParentesco() != null ? a.getAllTipoParentesco().getTpaNombre() : "",
                    BooleanUtils.isTrue(a.getAllViveConAllegado()) ? "Sí" : "No",
                    BooleanUtils.isTrue(a.getAllReferente()) ? "Sí" : "No",
                    a.getAllPersona().getPerEstado() != null ? a.getAllPersona().getPerEstado().getText() : "",
                    a.getAllPersona().getPerEmail(),
                    a.getAllPersona().getTelefonosAsString(),
                    a.getAllPersona().getPerDireccion().getDirZona().getZonNombre(),
                    a.getAllPersona().getPerDireccion().getDirDepartamento().getDepNombre(),
                    a.getAllPersona().getPerDireccion().getDirMunicipio().getMunNombre(),
                    a.getAllPersona().getPerDireccion().getDirDireccion(),
                    a.getAllPersona().getPerProfesion() != null ? a.getAllPersona().getPerProfesion().getProNombre() : "",
                    a.getAllPersona().getPerEscolaridad() != null ? a.getAllPersona().getPerEscolaridad().getEscNombre() : "",
                    a.getAllPersona().getPerOcupacion() != null ? a.getAllPersona().getPerOcupacion().getOcuNombre() : "",
                    a.getAllPersona().getPerDireccion().getDirDireccion()+ " - "+a.getAllPersona().getPerDireccion().getDirMunicipio().getMunNombre()+", "
                            + a.getAllPersona().getPerDireccion().getDirDepartamento().getDepNombre()+" - Zona "+a.getAllPersona().getPerDireccion().getDirZona().getZonNombre()
            );

        }
        TypedTableModel matriculas = new TypedTableModel();
        matriculas.addColumn("mat_centro", String.class);
        matriculas.addColumn("mat_anio", String.class);
        matriculas.addColumn("mat_nivel", String.class);
        matriculas.addColumn("mat_grado", String.class);
        matriculas.addColumn("mat_seccion", String.class);
        matriculas.addColumn("mat_promocion", String.class);

        for (SgMatricula m : mat) {

            matriculas.addRow(
                    m.getMatSeccion().getSecServicioEducativo().getSduSede().getSedNombre(),
                    m.getMatSeccion().getSecAnioLectivo().getAleAnio(),
                    m.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivNombre(),
                    m.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraNombre(),
                    m.getMatSeccion().getSecNombre(),
                    m.getMatPromocionGrado() != null ? m.getMatPromocionGrado().getText() : ""
            );

        }

        TypedTableModel titulos = new TypedTableModel();
        titulos.addColumn("tit_centro", String.class);
        titulos.addColumn("tit_anio", Integer.class);
        titulos.addColumn("tit_fecha_emision", String.class);
        titulos.addColumn("tit_fecha_validez", String.class);
        titulos.addColumn("tit_titulo_obtenido", String.class);
        titulos.addColumn("tit_nro_registro", Integer.class);
        titulos.addColumn("tit_reposicion", String.class);
        titulos.addColumn("tit_centro_codigo_nombre", String.class);

        for (SgTitulo t : tit) {

            titulos.addRow(
                    t.getTitSedeFk() != null ? t.getTitSedeFk().getSedNombre() : t.getTitSedeNombre() != null ? t.getTitSedeNombre() : "",
                    t.getTitAnio(),
                    Generales.getDateFormat(t.getTitFechaEmision()),
                    Generales.getDateFormat(t.getTitFechaValidez()),
                    t.getTitNombreCertificado(),
                    t.getTitNumeroRegistro(),
                    BooleanUtils.isTrue(t.getTitReposicion()) ? "Sí" : "No",
                    t.getTitSedeFk() != null ? t.getTitSedeFk().getSedCodigoNombre(): t.getTitCentroCodigoNombre()!= null ? t.getTitCentroCodigoNombre() : ""
            );

        }

        model.addRow(familiares, matriculas, titulos);

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public String getEstudianteIdentificacionesAsString(SgPersona per) {
        StringBuilder s = new StringBuilder();
        if (per.getPerNie() != null) {
            s.append(Etiquetas.getValue("nie")).append(": ").append(per.getPerNie()).append(", ");
        }
        if (!StringUtils.isBlank(per.getPerDui())) {
            s.append(Etiquetas.getValue("dui")).append(": ").append(per.getPerDui()).append(", ");
        }
        if (per.getPerIdentificaciones() != null && !per.getPerIdentificaciones().isEmpty()) {
            for (SgIdentificacion i : per.getPerIdentificaciones()) {
                s.append(i.getIdeTipoDocumento().getTinNombre()).append(": ");
                s.append(i.getIdeNumeroDocumento()).append(" ").append(i.getIdePaisEmisor().getPaiNombre()).append(", ");
            }
        }
        if (s.length() > 1) {
            s.deleteCharAt(s.length() - 1);
            s.deleteCharAt(s.length() - 1);
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public String getAllegadoIdentificacionesAsString(SgPersona per) {
        StringBuilder s = new StringBuilder();
        if (!StringUtils.isBlank(per.getPerDui())) {
            s.append(Etiquetas.getValue("dui")).append(": ").append(per.getPerDui()).append(", ");
        }
        if (per.getPerIdentificaciones() != null && !per.getPerIdentificaciones().isEmpty()) {
            for (SgIdentificacion i : per.getPerIdentificaciones()) {
                s.append(i.getIdeTipoDocumento().getTinNombre()).append(": ");
                s.append(i.getIdeNumeroDocumento()).append(" ").append(i.getIdePaisEmisor().getPaiNombre()).append(", ");
            }
        }
        if (s.length() > 1) {
            s.deleteCharAt(s.length() - 1);
            s.deleteCharAt(s.length() - 1);
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

}
