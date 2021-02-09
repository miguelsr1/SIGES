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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgDireccion;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgSiPromotor;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaSede;
import sv.gob.mined.siges.web.lazymodels.LazySistemaIntegradoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SistemaIntegradoRestClient;
import sv.gob.mined.siges.web.restclient.SistemaSedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SistemasIntegradosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SistemasIntegradosBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SistemaIntegradoRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private SistemaSedeRestClient sistemaSedeRestClient;

    @Inject
    private ApplicationBean applicationBean;

    private FiltroSistemaIntegrado filtro = new FiltroSistemaIntegrado();
    private SgSistemaIntegrado entidadEnEdicion = new SgSistemaIntegrado();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySistemaIntegradoDataModel sistemaIntegradoLazyModel;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SgSede sedeSeleccionada;
    private List<RevHistorico> historialEntidad = new ArrayList();
    private SofisComboG<SgSiPromotor> comboPromotor;

    public SistemasIntegradosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setDepartamento(comboDepartamentos != null && comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setMunicipio(comboMunicipio != null && comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setSede(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            filtro.setPromotor(comboPromotor != null && comboPromotor.getSelectedT() != null ? comboPromotor.getSelectedT().getProPk() : null);
            filtro.setIncluirCampos(new String[]{"sinCodigo", "sinNombre", "sinHabilitado",
                "sinUltModFecha", "sinUltModUsuario", "sinVersion", "sinPromotor.proNombre"});
            filtro.setIncluirTotalSedes(Boolean.TRUE);
            totalResultados = restClient.buscarTotal(filtro);
            sistemaIntegradoLazyModel = new LazySistemaIntegradoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"proNombre"});
            fc.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            List<SgSiPromotor> listaPromotores = restCatalogo.buscarPromotor(fc);
            comboPromotor = new SofisComboG<>(listaPromotores, "proNombre");
            comboPromotor.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public void limpiar() {
        comboPromotor.setSelected(-1);
        filtro = new FiltroSistemaIntegrado();
        sedeSeleccionada = null;
        limpiarCombos();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgSistemaIntegrado();
    }

    public void actualizar(SgSistemaIntegrado var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgSistemaIntegrado) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSinPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialEntidad = restClient.obtenerHistorialPorId(id);
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

                List<SgMunicipio> listMunicipio = restCatalogo.buscarMunicipio(fm);
                comboMunicipio = new SofisComboG(listMunicipio, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboMunicipio.ordenar();
            }
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            fil.setIncluirAdscritas(Boolean.FALSE);
            return sedeClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            return new ArrayList<>();
        }
    }

    public String getUrlMapa() {
        String urlMapa = "";
        try {
            if (entidadEnEdicion != null && entidadEnEdicion.getSinPk() != null) {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

                String scheme = request.getScheme();
                String serverName = request.getServerName();
                int serverPort = request.getServerPort();
                String contextPath = request.getContextPath();

                StringBuilder url = new StringBuilder();
                url.append(scheme).append("://").append(serverName);
                if (serverPort != 80 && serverPort != 443) {
                    url.append(":").append(serverPort);
                }
                url.append(contextPath).append("/mapa/mapaMultiple.html?");

                urlMapa = url.toString();
                String latitud = "";
                String longitud = "";
                String codigos = "";
                String nombres = "";
                String tipos = "";
                String departamentos = "";
                String municipios = "";
                String ids = "";

                FiltroSistemaSede fs = new FiltroSistemaSede();
                fs.setSistema(entidadEnEdicion.getSinPk());
                List<SgSede> sedes = sistemaSedeRestClient.buscar(fs);

                for (SgSede sed : sedes) {
                    SgDireccion dir = sed.getSedDireccion();
                    if (dir != null) {
                        if (dir.getDirLatitud() != null && dir.getDirLongitud() != null) {
                            latitud += dir.getDirLatitud() + "|";
                            longitud += dir.getDirLongitud() + "|";
                            ids += sed.getSedPk() + "|";
                            codigos += sed.getSedCodigo() + "|";
                            nombres += sed.getSedNombre() + "|";
                            tipos += sed.getSedTipo().getText() + "|";
                            departamentos += sed.getSedDireccion().getDirDepartamento().getDepNombre() + "|";
                            municipios += sed.getSedDireccion().getDirMunicipio().getMunNombre() + "|";
                        }
                    }
                }
                urlMapa = urlMapa + "lat=" + latitud + "&lon=" + longitud + "&cods=" + codigos + "&noms=" + nombres
                        + "&tipos=" + tipos + "&dptos=" + departamentos + "&muns=" + municipios + "&ids=" + ids + "&burl=" + this.applicationBean.getPortalUrl();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return urlMapa;
    }

    public FiltroSistemaIntegrado getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSistemaIntegrado filtro) {
        this.filtro = filtro;
    }

    public SgSistemaIntegrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSistemaIntegrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public LazySistemaIntegradoDataModel getSistemaIntegradoLazyModel() {
        return sistemaIntegradoLazyModel;
    }

    public void setSistemaIntegradoLazyModel(LazySistemaIntegradoDataModel sistemaIntegradoLazyModel) {
        this.sistemaIntegradoLazyModel = sistemaIntegradoLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<RevHistorico> getHistorialEntidad() {
        return historialEntidad;
    }

    public void setHistorialEntidad(List<RevHistorico> historialEntidad) {
        this.historialEntidad = historialEntidad;
    }

    public SofisComboG<SgSiPromotor> getComboPromotor() {
        return comboPromotor;
    }

    public void setComboPromotor(SofisComboG<SgSiPromotor> comboPromotor) {
        this.comboPromotor = comboPromotor;
    }

}
