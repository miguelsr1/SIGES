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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgModalidad;
import sv.gob.mined.siges.web.dto.SgOpcion;
import sv.gob.mined.siges.web.dto.SgRelOpcionProgEd;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgSectorEconomico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOpciones;
import sv.gob.mined.siges.web.lazymodels.LazyOpcionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.OpcionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class OpcionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OpcionBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long modId;

    @Inject
    private OpcionRestClient restClient;

    @Inject
    private ModalidadRestClient restModalidad;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;
    

            

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroOpciones filtroOpciones = new FiltroOpciones();
    private SgOpcion entidadEnEdicion = new SgOpcion();
    private List<RevHistorico> historialOpcion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyOpcionDataModel opcionLazyModel;
    private SgModalidad modalidadEnEdicion;
    private List<SgOpcion> listaOpciones;
    private SofisComboG<SgSectorEconomico> comboSectorEconomico;
    private SofisComboG<SgSectorEconomico> comboFiltroSectorEconomico;
    private List<SgProgramaEducativo> listaProgramasEducativos;
    private List<SgProgramaEducativo> listaProgramasEducativosSelect;
    private SofisComboG<SgProgramaEducativo> comboFiltroProgramasEducativos;

    private SgSectorEconomico SectorEconomico;

    public OpcionBean() {
    }

    @PostConstruct
    public void init() {
        try {

            validarAcceso();
            if (modId == null || modId <= 0) {
                redirectToIndex();
            }
            modalidadEnEdicion = restModalidad.obtenerPorIdLazy(modId);
            cargarCombos();
            buscar();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_OPCION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void buscar() {
        try {
            filtroOpciones.setFirst(new Long(0));
            filtroOpciones.setOpcModalidadPk(modId);
            filtroOpciones.setInicializarProgramaEducativo(Boolean.TRUE);
            if (comboFiltroSectorEconomico.getSelectedT() != null) {
                filtroOpciones.setOpcSectorEconomicoPk(comboFiltroSectorEconomico.getSelectedT().getSecPk());
            }
            listaOpciones = restClient.buscar(filtroOpciones);

            if (comboFiltroProgramasEducativos.getSelectedT() != null) {
                List<SgOpcion> listop = new ArrayList();
                for (SgOpcion op : listaOpciones) {
                    if (op.getOpcRelacionOpcionProgramaEdu() != null) {
                        for (SgRelOpcionProgEd rel : op.getOpcRelacionOpcionProgramaEdu()) {
                            if (rel.getRoeProgramaEducativo().getPedPk().equals(comboFiltroProgramasEducativos.getSelectedT().getPedPk())) {
                                listop.add(op);
                                break;
                            }
                        }
                    }
                }
                listaOpciones = listop;
            }

            totalResultados = Long.valueOf(listaOpciones.size());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);

            List<SgSectorEconomico> listaSectorEconomico = restCatalogo.buscar(fc);
            comboSectorEconomico = new SofisComboG(listaSectorEconomico, "secNombre");
            comboSectorEconomico.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboFiltroSectorEconomico = new SofisComboG(listaSectorEconomico, "secNombre");
            comboFiltroSectorEconomico.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboFiltroSectorEconomico.ordenar();

            listaProgramasEducativos = restCatalogo.buscarProgramasEducativos(fc);
            comboFiltroProgramasEducativos = new SofisComboG(listaProgramasEducativos, "pedNombre");
            comboFiltroProgramasEducativos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboFiltroProgramasEducativos.ordenar();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboSectorEconomico.setSelected(-1);

    }

    public void limpiar() {
        filtroOpciones = new FiltroOpciones();
        comboFiltroSectorEconomico.setSelected(-1);
        comboFiltroProgramasEducativos.setSelected(-1);
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        limpiar();
        entidadEnEdicion = new SgOpcion();
        listaProgramasEducativosSelect = new ArrayList<>();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgOpcion var) {
        try {
            limpiar();
            entidadEnEdicion = restClient.obtenerPorId(var.getOpcPk());
            comboSectorEconomico.setSelectedT(entidadEnEdicion.getOpcSectorEconomico());
            listaProgramasEducativosSelect = new ArrayList<>();
            if (entidadEnEdicion.getOpcRelacionOpcionProgramaEdu() != null) {
                for (SgRelOpcionProgEd relop : entidadEnEdicion.getOpcRelacionOpcionProgramaEdu()) {
                    listaProgramasEducativosSelect.add(relop.getRoeProgramaEducativo());
                }
            }
            LOGGER.log(Level.SEVERE, "id opcion:" + entidadEnEdicion.getOpcPk());
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            List<SgProgramaEducativo> listaAux = listaProgramasEducativosSelect;
            List<SgProgramaEducativo> listaAuxBorrar = listaProgramasEducativosSelect;
            List<SgRelOpcionProgEd> listaRelEntidad = entidadEnEdicion.getOpcRelacionOpcionProgramaEdu();
            List<SgRelOpcionProgEd> listaRelNueva = new ArrayList<>();
            if (listaProgramasEducativosSelect != null) {
                for (SgRelOpcionProgEd relEntidad : listaRelEntidad) {
                    SgProgramaEducativo progEduAux = relEntidad.getRoeProgramaEducativo();
                    if (listaAux.contains(progEduAux)) {
                        listaRelNueva.add(relEntidad);
                        listaAuxBorrar.remove(progEduAux);
                    }
                }
                for (SgProgramaEducativo progEdu : listaAuxBorrar) {
                    SgRelOpcionProgEd relop = new SgRelOpcionProgEd();
                    relop.setRoeProgramaEducativo(progEdu);
                    listaRelNueva.add(relop);
                }
            }
            entidadEnEdicion.setOpcRelacionOpcionProgramaEdu(listaRelNueva);
            entidadEnEdicion.setOpcSectorEconomico(comboSectorEconomico.getSelectedT());
            entidadEnEdicion.setOpcModalidad(modalidadEnEdicion);
            restClient.guardar(entidadEnEdicion);
            listaProgramasEducativosSelect = new ArrayList<>();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getOpcPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialOpcion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgOpcion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgOpcion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialOpcion() {
        return historialOpcion;
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

    public LazyOpcionDataModel getOpcionLazyModel() {
        return opcionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyOpcionDataModel opcionLazyModel) {
        this.opcionLazyModel = opcionLazyModel;
    }

    public Long getModId() {
        return modId;
    }

    public void setModId(Long modId) {
        this.modId = modId;
    }

    public SgModalidad getModalidadEnEdicion() {
        return modalidadEnEdicion;
    }

    public void setModalidadEnEdicion(SgModalidad modalidadEnEdicion) {
        this.modalidadEnEdicion = modalidadEnEdicion;
    }

    public SgSectorEconomico getSectorEconomico() {
        return SectorEconomico;
    }

    public void setSectorEconomico(SgSectorEconomico SectorEconomico) {
        this.SectorEconomico = SectorEconomico;
    }

    public SofisComboG<SgSectorEconomico> getComboSectorEconomico() {
        return comboSectorEconomico;
    }

    public void setComboSectorEconomico(SofisComboG<SgSectorEconomico> comboSectorEconomico) {
        this.comboSectorEconomico = comboSectorEconomico;
    }

    public FiltroOpciones getFiltroOpciones() {
        return filtroOpciones;
    }

    public void setFiltroOpciones(FiltroOpciones filtroOpciones) {
        this.filtroOpciones = filtroOpciones;
    }

    public List<SgOpcion> getListaOpciones() {
        return listaOpciones;
    }

    public void setListaOpciones(List<SgOpcion> listaOpciones) {
        this.listaOpciones = listaOpciones;
    }

    public List<SgProgramaEducativo> getListaProgramasEducativos() {
        return listaProgramasEducativos;
    }

    public void setListaProgramasEducativos(List<SgProgramaEducativo> listaProgramasEducativos) {
        this.listaProgramasEducativos = listaProgramasEducativos;
    }

    public List<SgProgramaEducativo> getListaProgramasEducativosSelect() {
        return listaProgramasEducativosSelect;
    }

    public void setListaProgramasEducativosSelect(List<SgProgramaEducativo> listaProgramasEducativosSelect) {
        this.listaProgramasEducativosSelect = listaProgramasEducativosSelect;
    }

    public SofisComboG<SgSectorEconomico> getComboFiltroSectorEconomico() {
        return comboFiltroSectorEconomico;
    }

    public void setComboFiltroSectorEconomico(SofisComboG<SgSectorEconomico> comboFiltroSectorEconomico) {
        this.comboFiltroSectorEconomico = comboFiltroSectorEconomico;
    }

    public SofisComboG<SgProgramaEducativo> getComboFiltroProgramasEducativos() {
        return comboFiltroProgramasEducativos;
    }

    public void setComboFiltroProgramasEducativos(SofisComboG<SgProgramaEducativo> comboFiltroProgramasEducativos) {
        this.comboFiltroProgramasEducativos = comboFiltroProgramasEducativos;
    }

}
