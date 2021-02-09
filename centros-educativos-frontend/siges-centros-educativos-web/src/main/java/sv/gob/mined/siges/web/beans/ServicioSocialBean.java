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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ServicioSocialBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ServicioSocialBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MatriculaRestClient restMatricula;
    
    @Inject
    private EstudianteRestClient estudiantesRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private Integer paginado = 10;
    private Long totalResultados;

    private List<SgEstudiante> listEstudiantes;
    private SgSeccion seccionEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean debeIngresarServicioSocial = Boolean.TRUE;

    private List<SgCalificacionEstudiante> otrasCalificaciones;

    public ServicioSocialBean() {
    }

    @PostConstruct
    public void init() {
        try {
            this.agregar();
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        //Control de seguridad
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SERVICIO_SOCIAL)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.MENU_SERVICIO_SOCIAL);
            JSFUtils.redirectToIndex();
        }
    }


    public List<SgEstudiante> buscarEstudiantes() throws Exception {
        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(seccionEnEdicion.getSecPk());
      //  filtroMat.setMatEstado(EnumMatriculaEstado.ABIERTO);
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
            "matValidacionAcademica", "matProvisional", "matEstudiante.estRealizoServicioSocial", "matEstudiante.estFechaServicioSocial", 
            "matEstudiante.estCantidadHorasServicioSocial", "matEstudiante.estDescripcionServicioSocial",
            "matEstudiante.estVersion"});  
        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
            "matEstudiante.estPersona.perPrimerNombre"});
        filtroMat.setAscending(new boolean[]{true, true, true});
        List<SgMatricula> matriculas = restMatricula.buscar(filtroMat);
        List<SgEstudiante> estudiantes = matriculas.stream()
                .map(c -> {
                    c.getMatEstudiante().setEstUltimaMatricula(c);
                    return c.getMatEstudiante();
                })
                .collect(Collectors.toList());
        return estudiantes;
    }

   

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public void seccionSeleccionada(SgSeccion sec) {
        try {
            seccionEnEdicion = sec;
            if (seccionEnEdicion == null) {
                return;
            }
            if (seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivCodigo().equals(Constantes.CODIGO_NIVEL_EDUCACION_MEDIA)){
                debeIngresarServicioSocial = Boolean.TRUE;
                listEstudiantes = buscarEstudiantes();
            } else {
                listEstudiantes = new ArrayList<>();
                debeIngresarServicioSocial = Boolean.FALSE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);

        }
    }

   
    public void seleccionadoServicioSocial(SgEstudiante est){
        if(!est.getEstRealizoServicioSocial()){
            est.setEstCantidadHorasServicioSocial(null);
            est.setEstDescripcionServicioSocial(null);
            est.setEstFechaServicioSocial(null);
        }
    }

    public void agregar() {

    }

    public void guardar() {
        try {
            estudiantesRestClient.actualizarServicioSocial(listEstudiantes);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public String colorRow(SgEstudiante est) {
       
        if (est.getEstRealizoServicioSocial() != null && est.getEstRealizoServicioSocial().equals(Boolean.TRUE)) {      
      
                if(est.getEstFechaServicioSocial() != null 
                        && est.getEstFechaServicioSocial().compareTo(LocalDate.now())<=0 
                        && est.getEstCantidadHorasServicioSocial() != null
                        && !StringUtils.isBlank(est.getEstDescripcionServicioSocial())){
                        return "aprobado"; 
                }
            
                        
        }
        return null;
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

    public List<SgEstudiante> getListEstudiantes() {
        return listEstudiantes;
    }

    public void setListEstudiantes(List<SgEstudiante> listEstudiantes) {
        this.listEstudiantes = listEstudiantes;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgCalificacionEstudiante> getOtrasCalificaciones() {
        return otrasCalificaciones;
    }

    public void setOtrasCalificaciones(List<SgCalificacionEstudiante> otrasCalificaciones) {
        this.otrasCalificaciones = otrasCalificaciones;
    }

    public Boolean getDebeIngresarServicioSocial() {
        return debeIngresarServicioSocial;
    }
    
    
}
