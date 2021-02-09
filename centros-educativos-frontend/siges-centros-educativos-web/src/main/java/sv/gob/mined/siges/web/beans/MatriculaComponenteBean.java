/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMotivoRetiro;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgModificarFechasMatricula;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class MatriculaComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MatriculaComponenteBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MatriculaRestClient matriculaClient;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPEClient;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private EscolaridadEstudianteComponenteBean escolaridadEstudianteComponenteBean;

    private List<SgMatricula> listaMatricula = new ArrayList();
    private List<SgMatricula> listaMatriculaAnula = new ArrayList();
    private SgMatricula matriculaEnEdicion;
    private SgModificarFechasMatricula modFechasEdicion = new SgModificarFechasMatricula();
    private SofisComboG<SgMotivoRetiro> comboMotivoRetiro;
    private Boolean soloLectura = Boolean.FALSE;
    private SgEstudiante estEdicion;
    private Boolean buscarMatriculaAnulada = Boolean.FALSE;
    private SofisComboG<SgNivel> comboNivel;
    private Boolean aplicaReaperturaMatricula = Boolean.FALSE;
    private SgMatricula matReapertura = null;

    @PostConstruct
    public void init() {
        try {
            cargarCombo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombo() {
        try {
            FiltroMotivoRetiro fc = new FiltroMotivoRetiro();
            fc.setTraslado(Boolean.FALSE);
            fc.setCambioSeccion(Boolean.FALSE);
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"mreNombre"});
            List<SgMotivoRetiro> listMorRet = catalogoClient.buscarMotivoRetiro(fc);
            comboMotivoRetiro = new SofisComboG(listMorRet, "mreNombre");
            comboMotivoRetiro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroNivel fNivel = new FiltroNivel();
            fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
            fNivel.setNivHabilitado(Boolean.TRUE);
            fNivel.setOrderBy(new String[]{"nivOrden"});
            fNivel.setAscending(new boolean[]{true});
            List<SgNivel> niveles = restNivel.buscar(fNivel);
            comboNivel = new SofisComboG(niveles, "nivNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void limpiarCombo() {
        comboMotivoRetiro.setSelected(0);
    }

    public void validarMatricula() {
        try {
            Boolean sePermiteValidarXGrado = Boolean.FALSE;

            if (estEdicion.getEstPersona().getPerNie() == null) {
                FiltroRelGradoPlanEstudio frelGPE = new FiltroRelGradoPlanEstudio();
                frelGPE.setRgpGrado(matriculaEnEdicion.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                frelGPE.setIncluirCampos(new String[]{"rgpPermiteValidarMatriculaSinNIE"});
                SgRelGradoPlanEstudio rel = relGradoPEClient.buscar(frelGPE)
                        .stream()
                        .findFirst()
                        .orElse(null);
                if (rel != null) {
                    sePermiteValidarXGrado = rel.getRgpPermiteValidarMatriculaSinNIE();
                }
            } else {
                sePermiteValidarXGrado = Boolean.TRUE;
            }

            if (BooleanUtils.isTrue(sePermiteValidarXGrado)) {
                matriculaEnEdicion = matriculaClient.validarMatriculaProvisional(matriculaEnEdicion);
                PrimeFaces.current().executeScript("PF('itemDialogMatProvisional').hide()");
                actualizar(estEdicion.getEstPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_4, Mensajes.obtenerMensaje(Mensajes.ERROR_VALIDAR_MATRICULA), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_4, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_4, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgEstudiante est) {
        if (est != null) {
            estEdicion = est;
            this.actualizar(est.getEstPk());
            this.buscarMatriculaAnulada(est.getEstPk());
        }
    }

    public void actualizar(Long estPk) {
        try {
            aplicaReaperturaMatricula = Boolean.FALSE;
            matReapertura = null;
            if (estPk != null) {
                FiltroMatricula filtro = new FiltroMatricula();
                filtro.setMatEstudiantePk(estPk);
                filtro.setSecNivelFk(comboNivel != null && comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null);
                filtro.setAscending(new boolean[]{true, true});
                filtro.setOrderBy(new String[]{"matFechaIngreso", "matPk"});
                filtro.setIncluirCampos(new String[]{
                    "matSeccion.secAnioLectivo.aleAnio",
                    "matSeccion.secAnioLectivo.alePk",
                    "matSeccion.secAnioLectivo.aleVersion",
                    "matSeccion.secServicioEducativo.sduSede.sedNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "matSeccion.secServicioEducativo.sduSede.sedPk",
                    "matSeccion.secServicioEducativo.sduSede.sedVersion",
                    "matSeccion.secServicioEducativo.sduSede.sedTipo",
                    "matSeccion.secServicioEducativo.sduSede.sedTelefono",
                    "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivCodigo",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "matSeccion.secServicioEducativo.sduOpcion.opcNombre",
                    "matSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graNombre",
                    "matSeccion.secNombre",
                    "matSeccion.secPk",
                    "matSeccion.secVersion",
                    "matFechaHasta",
                    "matFechaIngreso",
                    "matAnulada",
                    "matEstado",
                    "matRetirada",
                    "matMotivoRetiro.mreNombre",
                    "matObservaciones",
                    "matValidacionAcademica",
                    "matProvisional",
                    "matObservacioneProvisional",
                    "matVersion",
                    "matPromocionGrado",
                    "matCerradoPorCierreAnio"
                });
                List<SgMatricula> matriculas = matriculaClient.buscar(filtro);
                listaMatricula = matriculas.stream().filter(c -> BooleanUtils.isNotTrue(c.getMatAnulada())).collect(Collectors.toList());

                if (!listaMatricula.isEmpty()) {
                    SgMatricula mat = listaMatricula.get(listaMatricula.size() - 1);
                    if (BooleanUtils.isTrue(mat.getMatCerradoPorCierreAnio()) && mat.getMatEstado().equals(EnumMatriculaEstado.CERRADO)) {
                        aplicaReaperturaMatricula = Boolean.TRUE;
                        matReapertura = mat;
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void buscarMatriculaAnulada(Long estPk) {
        try {
            if (estPk != null) {
                FiltroMatricula filtro = new FiltroMatricula();
                filtro.setMatEstudiantePk(estPk);
                filtro.setMatAnulada(Boolean.TRUE);
                filtro.setAscending(new boolean[]{true, true});
                filtro.setOrderBy(new String[]{"matFechaIngreso", "matPk"});
                filtro.setIncluirCampos(new String[]{
                    "matSeccion.secAnioLectivo.aleAnio",
                    "matSeccion.secAnioLectivo.alePk",
                    "matSeccion.secAnioLectivo.aleVersion",
                    "matSeccion.secServicioEducativo.sduSede.sedNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "matSeccion.secServicioEducativo.sduSede.sedPk",
                    "matSeccion.secServicioEducativo.sduSede.sedVersion",
                    "matSeccion.secServicioEducativo.sduSede.sedTipo",
                    "matSeccion.secServicioEducativo.sduSede.sedTelefono",
                    "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivCodigo",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "matSeccion.secServicioEducativo.sduOpcion.opcNombre",
                    "matSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graNombre",
                    "matSeccion.secNombre",
                    "matSeccion.secPk",
                    "matSeccion.secVersion",
                    "matFechaHasta",
                    "matFechaIngreso",
                    "matAnulada",
                    "matEstado",
                    "matRetirada",
                    "matMotivoRetiro.mreNombre",
                    "matObservaciones",
                    "matValidacionAcademica",
                    "matProvisional",
                    "matObservacioneProvisional",
                    "matVersion"
                });

                listaMatriculaAnula = matriculaClient.buscar(filtro);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void agregarElementosMatricula(Long matPk) {
        try {
            this.comboMotivoRetiro.setSelected(-1);
            matriculaEnEdicion = matriculaClient.obtenerPorId(matPk);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }

    }

    public void actualizarMatricula(Long matPk) {
        try {
            matriculaEnEdicion = matriculaClient.obtenerPorId(matPk);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void actualizarMatriculaParaModificacionFechas(SgMatricula m) {
        try {
            this.modFechasEdicion.setMatPk(m.getMatPk());
            this.modFechasEdicion.setFechaIngreso(m.getMatFechaIngreso());
            this.modFechasEdicion.setFechaHasta(m.getMatFechaHasta());
            this.modFechasEdicion.setEstudiantePk(estEdicion.getEstPk());
            this.modFechasEdicion.setRenderFechaHasta(EnumMatriculaEstado.CERRADO.equals(m.getMatEstado()));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void buscarMatricula() {
        if (estEdicion != null) {
            actualizar(estEdicion.getEstPk());
        }
    }

    public void buscarMatriculaAnulada() {
        if (estEdicion != null) {
            actualizar(estEdicion.getEstPk());
        }
    }

    public void limpiar() {
        buscarMatriculaAnulada = Boolean.FALSE;
        comboNivel.setSelected(-1);
        buscarMatricula();
    }

    public void guardar() {
        try {
            if (matriculaEnEdicion != null) {
                //Retirar el estudiante
                matriculaEnEdicion.setMatMotivoRetiro(comboMotivoRetiro.getSelectedT());
                matriculaEnEdicion = matriculaClient.retirar(matriculaEnEdicion);
                actualizar(estEdicion.getEstPk());
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popup_retirar_matricula_msg", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popup_retirar_matricula_msg", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarFecha() {
        try {
            if (modFechasEdicion != null) {
                matriculaClient.modificarFechas(modFechasEdicion);
                matriculaEnEdicion = null;
                actualizar(estEdicion.getEstPk());
                PrimeFaces.current().executeScript("PF('itemDialogFechaMatricula').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popup_fecha_matricula_msg", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popup_fecha_matricula_msg", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void anularMatricula() {
        try {
            if (matriculaEnEdicion != null) {
                matriculaEnEdicion = matriculaClient.anular(matriculaEnEdicion);
                actualizar(estEdicion.getEstPk());
                this.buscarMatriculaAnulada(estEdicion.getEstPk());
                PrimeFaces.current().executeScript("PF('itemDialogFechaMatricula').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void reabrirMatricula() {
        try {
            if (matriculaEnEdicion != null) {
                matriculaEnEdicion = matriculaClient.reabrirMatricula(matriculaEnEdicion);
                PrimeFaces.current().executeScript("PF('confirmDialogReabrirMatricula').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                actualizar(estEdicion.getEstPk());
                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_ESCOLARIDAD_ESTUDIANTE)) {
                    escolaridadEstudianteComponenteBean.buscar(estEdicion);
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        PrimeFaces.current().executeScript("PF('confirmDialogReabrirMatricula').hide()");
    }

    public void cargarCombos() {

    }

    public Boolean esVacioObsercacionPrevisional(SgMatricula mat) {
        return !(StringUtils.isBlank(mat.getMatObservacioneProvisional()));
    }

    public List<SgMatricula> getListaMatricula() {
        return listaMatricula;
    }

    public void setListaMatricula(List<SgMatricula> listaMatricula) {
        this.listaMatricula = listaMatricula;
    }

    public SgMatricula getMatriculaEnEdicion() {
        return matriculaEnEdicion;
    }

    public void setMatriculaEnEdicion(SgMatricula matriculaEnEdicion) {
        this.matriculaEnEdicion = matriculaEnEdicion;
    }

    public SofisComboG<SgMotivoRetiro> getComboMotivoRetiro() {
        return comboMotivoRetiro;
    }

    public void setComboMotivoRetiro(SofisComboG<SgMotivoRetiro> comboMotivoRetiro) {
        this.comboMotivoRetiro = comboMotivoRetiro;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getBuscarMatriculaAnulada() {
        return buscarMatriculaAnulada;
    }

    public void setBuscarMatriculaAnulada(Boolean buscarMatriculaAnulada) {
        this.buscarMatriculaAnulada = buscarMatriculaAnulada;
    }

    public SgEstudiante getEstEdicion() {
        return estEdicion;
    }

    public void setEstEdicion(SgEstudiante estEdicion) {
        this.estEdicion = estEdicion;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public List<SgMatricula> getListaMatriculaAnula() {
        return listaMatriculaAnula;
    }

    public void setListaMatriculaAnula(List<SgMatricula> listaMatriculaAnula) {
        this.listaMatriculaAnula = listaMatriculaAnula;
    }

    public Boolean getAplicaReaperturaMatricula() {
        return aplicaReaperturaMatricula;
    }

    public void setAplicaReaperturaMatricula(Boolean aplicaReaperturaMatricula) {
        this.aplicaReaperturaMatricula = aplicaReaperturaMatricula;
    }

    public SgMatricula getMatReapertura() {
        return matReapertura;
    }

    public void setMatReapertura(SgMatricula matReapertura) {
        this.matReapertura = matReapertura;
    }

    public SgModificarFechasMatricula getModFechasEdicion() {
        return modFechasEdicion;
    }

    public void setModFechasEdicion(SgModificarFechasMatricula modFechasEdicion) {
        this.modFechasEdicion = modFechasEdicion;
    }

}
