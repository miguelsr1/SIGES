/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.mensajes.Mensajes;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgEncuestaEstudiante;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncuestaEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyEncuestaEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EncuestaEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EncuestasEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EncuestasEstudianteBean.class.getName());

    @Inject
    private EncuestaEstudianteRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosClient;
    
    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroEncuestaEstudiante filtro = new FiltroEncuestaEstudiante();
    private LazyEncuestaEstudianteDataModel lazyDataModel;
    private List<RevHistorico> historial = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private SgEncuestaEstudiante entidadEnEdicion;

    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgSede sedeSeleccionada;

    public EncuestasEstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ENCUESTAS_ESTUDIANTES)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void actualizar(SgEncuestaEstudiante var) {
        entidadEnEdicion = var;
    }

    public String buscar() {
        try {
            filtro.setSedePk(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            filtro.setDepartamentoPk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setMunicipioPk(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);     
            filtro.setIncluirCampos(new String[]{"encEstudiante.estPersona.perNie", "encEstudiante.estPersona.perFechaNacimiento",
                "encEstudiante.estPersona.perPrimerNombre", "encEstudiante.estPersona.perSegundoNombre",
                "encEstudiante.estPersona.perPrimerApellido", "encEstudiante.estPersona.perSegundoApellido",
                "encEstudiante.estPk", "encEstudiante.estPersona.perSexo.sexNombre",
                "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTelefono",
                "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedCodigo",
                "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipo",
                "encEstudiante.estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedNombre",              
                "encFechaIngreso", "encUltModFecha"
            });
            totalResultados = restClient.buscarTotal(filtro);
            lazyDataModel = new LazyEncuestaEstudianteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public StreamedContent exportarConsulta() {
        try {
            if (totalResultados < 1000){
                byte[] cert = restClient.exportarObtenerPorFiltroExcel(filtro);
                if (cert != null) {
                    InputStream targetStream = new ByteArrayInputStream(cert);
                    StreamedContent file = new DefaultStreamedContent(targetStream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "consulta_encuestas_SIGES" + ".xlsx");
                    return file;
                } else {
                   JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
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
            List<SgDepartamento> listaDepartamentos = catalogosClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void departamentoElegido() {
        try {
            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio fm = new FiltroMunicipio();
                fm.setOrderBy(new String[]{"munNombre"});
                fm.setAscending(new boolean[]{true});
                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fm.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());

                List<SgMunicipio> listMunicipio = catalogosClient.buscarMunicipio(fm);
                comboMunicipio = new SofisComboG(listMunicipio, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboMunicipio.ordenar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }


    public String limpiar() {
        filtro = new FiltroEncuestaEstudiante();
        filtroNombreCompleto = new FiltroNombreCompleto();
        totalResultados = null;
        lazyDataModel = null;
        comboDepartamentos.setSelected(-1);
        comboMunicipio = new SofisComboG();
        comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        return null;
    }

    public void filtroNombreCompletoSeleccionar(FiltroNombreCompleto filtroNombre) {
        if (filtroNombre != null) {
            filtro.setPerPrimerNombre(filtroNombre.getPerPrimerNombre());
            filtro.setPerSegundoNombre(filtroNombre.getPerSegundoNombre());
            filtro.setPerTercerNombre(filtroNombre.getPerTercerNombre());
            filtro.setPerPrimerApellido(filtroNombre.getPerPrimerApellido());
            filtro.setPerSegundoApellido(filtroNombre.getPerSegundoApellido());
            filtro.setPerTercerApellido(filtroNombre.getPerTercerApellido());
            if (!StringUtils.isBlank(filtroNombre.getNombreCompleto())) {
                filtro.setPerNombreCompleto(null);
            }
        }
        PrimeFaces.current().ajax().update("form:pnlSearch");
    }

    public String cargarHistorial(Long id) {
        try {
            historial = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEncPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroEncuestaEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEncuestaEstudiante filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
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

    public LazyEncuestaEstudianteDataModel getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyEncuestaEstudianteDataModel lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public CatalogosRestClient getCatalogosClient() {
        return catalogosClient;
    }

    public void setCatalogosClient(CatalogosRestClient catalogosClient) {
        this.catalogosClient = catalogosClient;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    
    
}
