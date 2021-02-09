/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyMatriculaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudiantesValidacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudiantesValidacionBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    private CatalogosRestClient catalogoClient;

    private FiltroMatricula filtro = new FiltroMatricula();
    private LazyMatriculaDataModel matriculasLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgDepartamento> comboDepartamentoPartida;
    private SofisComboG<SgMunicipio> comboMunicipioPartida;

    private SgMatricula matriculaEdicion;
    private SgMatricula matriculaEdicionNombreModificada;
    private SgMatricula matriculaEdicionPartidaModificada;

    public EstudiantesValidacionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            filtro.setMatEstado(EnumMatriculaEstado.ABIERTO);
            filtro.setMatValidadaAcademicamente(Boolean.FALSE);
            validarAcceso();
            cargarCombos();
            inicializarFiltrosSeccion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void inicializarFiltrosSeccion() {
        this.filtrosSeccion.setRenderProgramaOpcion(Boolean.TRUE);
        this.filtrosSeccion.setFiltro(this.filtro);
        this.filtrosSeccion.setSoloAniosLectivosAbiertos(Boolean.TRUE);
        this.filtrosSeccion.setRetornarSoloSeccionesAbiertas(Boolean.TRUE);
        this.filtrosSeccion.cargarCombos();
        this.filtrosSeccion.seleccionarUltimoAnio();
    }

    public void forceInit() {
        //Utilizado para forzar init de EstudiantesValidacionBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTUDIANTES_VALIDACION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void actualizarMatricula(SgMatricula m) {
        this.matriculaEdicion = m;
        this.matriculaEdicionNombreModificada = (SgMatricula) SerializationUtils.clone(m);
    }

    public void actualizarMatriculaParaPartida(SgMatricula m) {
        this.matriculaEdicion = m;
        this.matriculaEdicionPartidaModificada = (SgMatricula) SerializationUtils.clone(m);
        this.comboDepartamentoPartida.setSelected(-1);
        this.comboMunicipioPartida.setSelected(-1);

        SgDepartamento dep = m.getMatEstudiante().getEstPersona().getPerPartidaDepartamento();
        SgMunicipio mun = m.getMatEstudiante().getEstPersona().getPerPartidaMunicipio();
        if (dep != null) {
            this.comboDepartamentoPartida.setSelectedT(dep);
            seleccionarDepartamentoPartida();
            if (mun != null) {
                this.comboMunicipioPartida.setSelectedT(mun);
            }
        }

    }
    
    public void aceptarModificacionNombres(){
        SgPersona p = matriculaEdicion.getMatEstudiante().getEstPersona();
        SgPersona pnew = matriculaEdicionNombreModificada.getMatEstudiante().getEstPersona();
        p.setPerPrimerNombre(pnew.getPerPrimerNombre());
        p.setPerSegundoNombre(pnew.getPerSegundoNombre());
        p.setPerTercerNombre(pnew.getPerTercerNombre());
        p.setPerPrimerApellido(pnew.getPerPrimerApellido());
        p.setPerSegundoApellido(pnew.getPerSegundoApellido());
        p.setPerTercerApellido(pnew.getPerTercerApellido());
    }
    
    public void aceptarModificacionPartida(){
        SgPersona p = matriculaEdicion.getMatEstudiante().getEstPersona();
        SgPersona pnew = matriculaEdicionPartidaModificada.getMatEstudiante().getEstPersona();
        p.setPerPartidaNacimiento(pnew.getPerPartidaNacimiento());
        p.setPerPartidaNacimientoAnio(pnew.getPerPartidaNacimientoAnio());
        p.setPerPartidaNacimientoComplemento(pnew.getPerPartidaNacimientoComplemento());
        p.setPerPartidaNacimientoFolio(pnew.getPerPartidaNacimientoFolio());
        p.setPerPartidaNacimientoLibro(pnew.getPerPartidaNacimientoLibro());
        p.setPerPartidaDepartamento(pnew.getPerPartidaDepartamento());
        p.setPerPartidaMunicipio(pnew.getPerPartidaMunicipio());
    }

    public void validarMatricula(SgMatricula m) {
        try {
            restMatricula.validarAcademicamenteMatricula(m);
            m.setMatValidacionAcademica(Boolean.TRUE);
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void anularValidacionMatricula(SgMatricula m) {
        try {
            restMatricula.anularValidacionAcademica(m);
            m.setMatValidacionAcademica(Boolean.FALSE);
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {

            filtro.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie",
                "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perTercerNombre",
                "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perTercerApellido",
                "matEstudiante.estPersona.perPartidaNacimiento", "matEstudiante.estPersona.perPartidaNacimientoFolio", "matEstudiante.estPersona.perPartidaNacimientoLibro",
                "matEstudiante.estPersona.perFechaNacimiento", "matEstudiante.estPersona.perPk", "matEstudiante.estPersona.perPartidaNacimientoComplemento",
                "matEstudiante.estPersona.perPartidaNacimientoAnio",
                "matEstudiante.estPersona.perPartidaDepartamento.depPk", "matEstudiante.estPersona.perPartidaMunicipio.munPk",
                "matEstudiante.estPersona.perPartidaDepartamento.depNombre", "matEstudiante.estPersona.perPartidaMunicipio.munNombre",
                "matEstudiante.estPersona.perPartidaDepartamento.depVersion", "matEstudiante.estPersona.perPartidaMunicipio.munVersion",
                "matEstudiante.estPersona.perPartidaNacimientoArchivo.achPk",
                "matEstudiante.estPersona.perPartidaNacimientoArchivo.achNombre",
                "matEstudiante.estPersona.perPartidaNacimientoArchivo.achContentType",
                "matEstudiante.estPersona.perPartidaNacimientoArchivo.achExt",    
                "matEstudiante.estPersona.perNacionalidad", "matEstudiante.estPersona.perNacionalidad.nacCodigo",
                "matEstudiante.estPersona.perSexo.sexNombre", "matEstudiante.estHabilitado", "matEstudiante.estPersona.perHabilitado",
                "matSeccion.secServicioEducativo.sduSede.sedNombre", "matSeccion.secServicioEducativo.sduSede.sedCodigo", "matSeccion.secServicioEducativo.sduSede.sedTipo",
                "matSeccion.secServicioEducativo.sduGrado.graNombre", "matValidacionAcademica",
                "matSeccion.secNombre", "matSeccion.secJornadaLaboral.jlaNombre"});
            if(filtro.getSecSedeFk()!=null || sessionBean.getSedeDefecto()!=null){
                filtro.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", 
                    "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre"});
                filtro.setAscending(new boolean[]{true,true,true,true});   
            }
            totalResultados = restMatricula.buscarTotal(filtro);
            matriculasLazyModel = new LazyMatriculaDataModel(restMatricula, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> depto = catalogoClient.buscarDepartamento(fc);

            comboDepartamentoPartida = new SofisComboG(depto, "depNombre");
            comboDepartamentoPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipioPartida = new SofisComboG();
            comboMunicipioPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String limpiar() {
        filtro = new FiltroMatricula();
        filtrosSeccion.setFiltro(this.filtro);
        filtrosSeccion.limpiarCombos();
        filtro.setMatEstado(EnumMatriculaEstado.ABIERTO);
        filtro.setMatValidadaAcademicamente(Boolean.FALSE);
        totalResultados = null;
        matriculasLazyModel = null;
        return null;
    }

    public void seleccionarDepartamentoPartida(SgPersona per) {
        try {
            per.setPerPartidaDepartamento(comboDepartamentoPartida.getSelectedT());
            per.setPerPartidaMunicipio(null);
            seleccionarDepartamentoPartida();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamentoPartida() {
        try {
            comboMunicipioPartida = new SofisComboG();
            comboMunicipioPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamentoPartida.getSelectedT() != null) {
                FiltroMunicipio filtroM = new FiltroMunicipio();
                filtroM.setOrderBy(new String[]{"munNombre"});
                filtroM.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                filtroM.setAscending(new boolean[]{true});
                filtroM.setMunDepartamentoFk(comboDepartamentoPartida.getSelectedT().getDepPk());
                comboMunicipioPartida = new SofisComboG(catalogoClient.buscarMunicipio(filtroM), "munNombre");
                comboMunicipioPartida.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarMunicipioPartida(SgPersona per) {
        per.setPerPartidaMunicipio(comboMunicipioPartida.getSelectedT());
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public FiltroMatricula getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMatricula filtro) {
        this.filtro = filtro;
    }

    public LazyMatriculaDataModel getMatriculasLazyModel() {
        return matriculasLazyModel;
    }

    public void setMatriculasLazyModel(LazyMatriculaDataModel matriculasLazyModel) {
        this.matriculasLazyModel = matriculasLazyModel;
    }

    public SofisComboG<SgMunicipio> getComboMunicipioPartida() {
        return comboMunicipioPartida;
    }

    public void setComboMunicipioPartida(SofisComboG<SgMunicipio> comboMunicipioPartida) {
        this.comboMunicipioPartida = comboMunicipioPartida;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoPartida() {
        return comboDepartamentoPartida;
    }

    public void setComboDepartamentoPartida(SofisComboG<SgDepartamento> comboDepartamentoPartida) {
        this.comboDepartamentoPartida = comboDepartamentoPartida;
    }

    public SgMatricula getMatriculaEdicion() {
        return matriculaEdicion;
    }

    public SgMatricula getMatriculaEdicionNombreModificada() {
        return matriculaEdicionNombreModificada;
    }

    public void setMatriculaEdicionNombreModificada(SgMatricula matriculaEdicionNombreModificada) {
        this.matriculaEdicionNombreModificada = matriculaEdicionNombreModificada;
    }

    public SgMatricula getMatriculaEdicionPartidaModificada() {
        return matriculaEdicionPartidaModificada;
    }

    public void setMatriculaEdicionPartidaModificada(SgMatricula matriculaEdicionPartidaModificada) {
        this.matriculaEdicionPartidaModificada = matriculaEdicionPartidaModificada;
    }

    
    

}
