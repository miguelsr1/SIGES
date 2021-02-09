/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPromRepMatriculas;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumPromocionGradoMatricula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AsignacionSeccionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AsignacionSeccionesBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private AnioLectivoRestClient anioLectivoClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private CalendarioRestClient calendarioRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private Integer paginado = 10;
    private Long totalResultados;

    private List<SgMatricula> listEstudiantesPromovidos;
    private List<SgMatricula> listEstudiantesRepetidores;
    private List<SgMatricula> listEstudiantesPromovidosSelected;
    private List<SgMatricula> listEstudiantesRepetidoresSelected;
    private List<SgMatricula> listEstudiantesMatriculadosDestino;
    private LocalDate matFechaIngreso;
    private SgSeccion seccionDestino;
    private String tabMatriculasSelected = null;
    private SgCalendario calendarioSede;

    private Boolean soloLectura = Boolean.FALSE;

    public AsignacionSeccionesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {

        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MATRICULA_SIGUIENTE_ANIO)) {
            JSFUtils.redirectToIndex();
        }

    }

    public void seleccionarSeccion(SgSeccion sec) {
        try {
            tabMatriculasSelected = null;
            listEstudiantesMatriculadosDestino = null;
            seccionDestino = sec;
            calendarioSede = null;
            if (seccionDestino != null) {

                FiltroCalendario fc = new FiltroCalendario();
                fc.setTipoCalendarioPk(seccionDestino.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
                fc.setAnioLectivoFk(seccionDestino.getSecAnioLectivo().getAlePk());
                fc.setHabilitado(Boolean.TRUE);
                fc.setAscending(new boolean[]{false});
                fc.setOrderBy(new String[]{"calAnioLectivo.aleAnio"});
                fc.setIncluirCampos(new String[]{"calAnioLectivo.aleAnio", "calPermiteMatricularSiguienteAnio", "calPermiteCierreAnio"});
                List<SgCalendario> cal = calendarioRestClient.buscar(fc);
                if (!cal.isEmpty()) {
                    calendarioSede = cal.get(0);
                    
                    if (BooleanUtils.isTrue(calendarioSede.getCalPermiteMatricularSiguienteAnio())) {
                        matFechaIngreso = catalogosClient.obtenerFechaIngresoMatriculasPorDefecto(
                                sec.getSecAnioLectivo().getAleAnio(), sec.getSecServicioEducativo().getSduSede().getSedTipoCalendario());

                        this.buscarEstudiantes(sec);
                    }
                }
            } else {
                matFechaIngreso = null;
                listEstudiantesPromovidos = null;
                listEstudiantesRepetidores = null;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            SgPromRepMatriculas dto = new SgPromRepMatriculas();
            dto.setFechaIngreso(matFechaIngreso);
            dto.setNuevaSeccion(seccionDestino);
            dto.setMatriculasPromovidas(this.listEstudiantesPromovidosSelected);
            dto.setMatriculasRepetidoras(this.listEstudiantesRepetidoresSelected);
            restMatricula.generarPromocionesYRepeticiones(dto);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

            buscarEstudiantes(seccionDestino);
            if (listEstudiantesMatriculadosDestino != null) {
                buscarEstudiantesMatriculadosDestino(this.seccionDestino);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void buscarEstudiantes(SgSeccion destino) throws Exception {
        listEstudiantesPromovidosSelected = new ArrayList<>();
        listEstudiantesRepetidoresSelected = new ArrayList<>();
        if (destino != null) {
            FiltroMatricula filtroMat = new FiltroMatricula();
            //Filtrar sede
            filtroMat.setSecGradoSiguienteFk(destino.getSecServicioEducativo().getSduGrado().getGraPk());
            filtroMat.setSecAnioLectivoAnio(destino.getSecAnioLectivo().getAleAnio() - 1);
            filtroMat.setSecSedeFk(destino.getSecServicioEducativo().getSduSede().getSedPk());
            filtroMat.setSecOpcionFk(destino.getSecServicioEducativo().getSduOpcion() != null ? destino.getSecServicioEducativo().getSduOpcion().getOpcPk() : null);
            filtroMat.setSecProgramaEducativoFk(destino.getSecServicioEducativo().getSduProgramaEducativo() != null ? destino.getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
            filtroMat.setMatPromocionGrado(EnumPromocionGradoMatricula.PROMOVIDO);
            filtroMat.setMatEstado(EnumMatriculaEstado.CERRADO);
            filtroMat.setIncluirCampos(new String[]{
                "matPk",
                "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre",
                "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
                "matProvisional", "matPromocionGrado",
                "matValidacionAcademica",
                "matSeccion.secPk", "matSeccion.secNombre", "matSeccion.secVersion", "matSeccion.secJornadaLaboral.jlaNombre",
                "matEstudiante.estPk", "matEstudiante.estVersion",
                "matEstudiante.estPersona.perPk", "matEstudiante.estPersona.perVersion",
                "matEstudiante.estPersona.perNie", "matEstudiante.estPersona.perSexo.sexPk",
                "matEstudiante.estPersona.perSexo.sexNombre", "matEstudiante.estPersona.perFechaNacimiento",
                "matEstudiante.estUltimaMatricula.matPk"
            });
            filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
                "matEstudiante.estPersona.perPrimerNombre"});
            filtroMat.setAscending(new boolean[]{true, true, true});
            listEstudiantesPromovidos = restMatricula.buscar(filtroMat).stream()
                    .filter(m -> m.getMatPk().equals(m.getMatEstudiante().getEstUltimaMatricula().getMatPk())).collect(Collectors.toList()); //Filtramos matriculas que no son la ult

            filtroMat = new FiltroMatricula();
            filtroMat.setSecGradoFk(destino.getSecServicioEducativo().getSduGrado().getGraPk());
            filtroMat.setSecAnioLectivoAnio(destino.getSecAnioLectivo().getAleAnio() - 1);
            filtroMat.setSecSedeFk(destino.getSecServicioEducativo().getSduSede().getSedPk());
            filtroMat.setSecOpcionFk(destino.getSecServicioEducativo().getSduOpcion() != null ? destino.getSecServicioEducativo().getSduOpcion().getOpcPk() : null);
            filtroMat.setSecProgramaEducativoFk(destino.getSecServicioEducativo().getSduProgramaEducativo() != null ? destino.getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
            filtroMat.setMatPromocionGrado(EnumPromocionGradoMatricula.NO_PROMOVIDO);
            filtroMat.setMatEstado(EnumMatriculaEstado.CERRADO);
            filtroMat.setIncluirCampos(new String[]{
                "matPk",
                "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre",
                "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
                "matProvisional", "matPromocionGrado",
                "matValidacionAcademica",
                "matSeccion.secPk", "matSeccion.secNombre", "matSeccion.secVersion", "matSeccion.secJornadaLaboral.jlaNombre",
                "matEstudiante.estPk", "matEstudiante.estVersion",
                "matEstudiante.estPersona.perPk", "matEstudiante.estPersona.perVersion",
                "matEstudiante.estPersona.perNie", "matEstudiante.estPersona.perSexo.sexPk",
                "matEstudiante.estPersona.perSexo.sexNombre", "matEstudiante.estPersona.perFechaNacimiento",
                "matEstudiante.estUltimaMatricula.matPk"
            });
            filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
                "matEstudiante.estPersona.perPrimerNombre"});
            filtroMat.setAscending(new boolean[]{true, true, true});
            listEstudiantesRepetidores = restMatricula.buscar(filtroMat).stream()
                    .filter(m -> m.getMatPk().equals(m.getMatEstudiante().getEstUltimaMatricula().getMatPk())).collect(Collectors.toList());
        }

    }

    public void buscarEstudiantesMatriculadosDestino(SgSeccion destino) throws Exception {
        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(destino.getSecPk());
        filtroMat.setMatRetirada(Boolean.FALSE);
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
            "matValidacionAcademica", "matProvisional", "matEstudiante.estPersona.perSexo.sexNombre",
            "matEstudiante.estVersion"});
        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
            "matEstudiante.estPersona.perPrimerNombre"});
        filtroMat.setAscending(new boolean[]{true, true, true});
        listEstudiantesMatriculadosDestino = restMatricula.buscar(filtroMat);
    }

    public void tabSelected() {
        try {
            if (listEstudiantesMatriculadosDestino == null) {
                buscarEstudiantesMatriculadosDestino(this.seccionDestino);
            }
            PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSeccion getSeccionDestino() {
        return seccionDestino;
    }

    public void setSeccionDestino(SgSeccion seccionDestino) {
        this.seccionDestino = seccionDestino;
    }

    public LocalDate getMatFechaIngreso() {
        return matFechaIngreso;
    }

    public void setMatFechaIngreso(LocalDate matFechaIngreso) {
        this.matFechaIngreso = matFechaIngreso;
    }

    public AnioLectivoRestClient getAnioLectivoClient() {
        return anioLectivoClient;
    }

    public void setAnioLectivoClient(AnioLectivoRestClient anioLectivoClient) {
        this.anioLectivoClient = anioLectivoClient;
    }

    public List<SgMatricula> getListEstudiantesPromovidos() {
        return listEstudiantesPromovidos;
    }

    public void setListEstudiantesPromovidos(List<SgMatricula> listEstudiantesPromovidos) {
        this.listEstudiantesPromovidos = listEstudiantesPromovidos;
    }

    public List<SgMatricula> getListEstudiantesRepetidores() {
        return listEstudiantesRepetidores;
    }

    public void setListEstudiantesRepetidores(List<SgMatricula> listEstudiantesRepetidores) {
        this.listEstudiantesRepetidores = listEstudiantesRepetidores;
    }

    public List<SgMatricula> getListEstudiantesPromovidosSelected() {
        return listEstudiantesPromovidosSelected;
    }

    public void setListEstudiantesPromovidosSelected(List<SgMatricula> listEstudiantesPromovidosSelected) {
        this.listEstudiantesPromovidosSelected = listEstudiantesPromovidosSelected;
    }

    public List<SgMatricula> getListEstudiantesRepetidoresSelected() {
        return listEstudiantesRepetidoresSelected;
    }

    public void setListEstudiantesRepetidoresSelected(List<SgMatricula> listEstudiantesRepetidoresSelected) {
        this.listEstudiantesRepetidoresSelected = listEstudiantesRepetidoresSelected;
    }

    public List<SgMatricula> getListEstudiantesMatriculadosDestino() {
        return listEstudiantesMatriculadosDestino;
    }

    public void setListEstudiantesMatriculadosDestino(List<SgMatricula> listEstudiantesMatriculadosDestino) {
        this.listEstudiantesMatriculadosDestino = listEstudiantesMatriculadosDestino;
    }

    public String getTabMatriculasSelected() {
        return tabMatriculasSelected;
    }

    public void setTabMatriculasSelected(String tabMatriculasSelected) {
        this.tabMatriculasSelected = tabMatriculasSelected;
    }

    public SgCalendario getCalendarioSede() {
        return calendarioSede;
    }

    public void setCalendarioSede(SgCalendario calendarioSede) {
        this.calendarioSede = calendarioSede;
    }

}
