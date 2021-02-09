/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.HashMap;
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
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import sv.gob.mined.siges.client.excepciones.HttpServerException;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.catalogo.SgCanton;
import sv.gob.mined.siges.web.dto.catalogo.SgCaserio;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCalle;
import sv.gob.mined.siges.web.dto.catalogo.SgZona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgDireccion;
import sv.gob.mined.siges.web.filtros.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCanton;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCaserio;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DireccionComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DireccionComponenteBean.class.getName());

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient restCatalogo;

    private SgDireccion direccion = new SgDireccion();
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SofisComboG<SgCanton> comboCanton;
    private SofisComboG<SgCaserio> comboCaserio;
    private SofisComboG<SgZona> comboZona;
    private SofisComboG<SgTipoCalle> comboTiposCalle;

    private Boolean soloLectura = Boolean.TRUE;
    private MapModel mapaModel;

    private String urlMapa;

    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();
    private HashMap<SgMunicipio, List<SgCanton>> canCache = new HashMap<>();
    private HashMap<SgCanton, List<SgCaserio>> casCache = new HashMap<>();
    
    private Boolean verMapa = Boolean.TRUE;
    private Boolean verZona = Boolean.TRUE;
    private Boolean verCanton = Boolean.TRUE;
    private Boolean verCaserio = Boolean.TRUE;
    private Boolean verTipoCalle = Boolean.TRUE;

    @PostConstruct
    public void init() {
        try{
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            soloLectura = request.getAttribute("soloLecturaDireccion") != null ? (Boolean) request.getAttribute("soloLecturaDireccion") : soloLectura;
            if (!soloLectura) {
                cargarCombos();
            }
            mapaModel = new DefaultMapModel();
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgDireccion dir) {       
        //Si cambia la dirección, actualizo formulario
        try{
            if (!soloLectura) {
                if (comboDepartamento == null) {
                        cargarCombos();
                }
                if (dir != null ) {
                    limpiarCombos();
                    direccion =(SgDireccion) SerializationUtils.clone(dir); //TODO: verificar
                    if (dir.getDirDepartamento() != null) {
                        comboDepartamento.setSelectedT(dir.getDirDepartamento());
                        seleccionarDepartamento();
                        if (dir.getDirMunicipio() != null) {
                            comboMunicipio.setSelectedT(dir.getDirMunicipio());
                            seleccionarMunicipio();
                            if (dir.getDirCanton() != null) {
                                comboCanton.setSelectedT(dir.getDirCanton());
                                seleccionarCanton();
                                if (dir.getDirCaserio() != null) {
                                    comboCaserio.setSelectedT(dir.getDirCaserio());
                                }
                            }
                        }
                    }
                    if (dir.getDirZona() != null) {
                        comboZona.setSelectedT(dir.getDirZona());
                    }
                    if (dir.getDirTipoCalle() != null) {
                        comboTiposCalle.setSelectedT(dir.getDirTipoCalle());
                    }
                } else if (dir == null) {
                    limpiarCombos();
                }
            }
            direccion = dir;
            mostrarMapa();
        } catch (HttpServerException ex) {
            Logger.getLogger(DireccionComponenteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void limpiarCombos() {
        if (comboDepartamento != null) {
            comboDepartamento.setSelected(-1);
            comboZona.setSelected(-1);
            comboTiposCalle.setSelected(-1);
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCanton = new SofisComboG();
            comboCanton.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCaserio = new SofisComboG();
            comboCaserio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }

    public void cargarCombos() throws HttpServerException {
        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos;
            departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"zonNombre", "zonVersion"});
            fc.setOrderBy(new String[]{"zonNombre"});
            List<SgZona> zonas = restCatalogo.buscarZona(fc);
            comboZona = new SofisComboG(zonas, "zonNombre");
            comboZona.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setIncluirCampos(new String[]{"tllNombre", "tllVersion"});
            fc.setOrderBy(new String[]{"tllNombre"});
            comboTiposCalle = new SofisComboG(restCatalogo.buscarTiposCalle(fc), "tllNombre");
            comboTiposCalle.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboCanton = new SofisComboG();
            comboCanton.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboCaserio = new SofisComboG();
            comboCaserio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void mostrarMapa() {
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
        url.append(contextPath).append("/mapa/mapa.html?");

        urlMapa = url.toString();
        if (direccion != null) {
            if (direccion.getDirLatitud() != null && direccion.getDirLongitud() != null){
                urlMapa = urlMapa + "lat=" + direccion.getDirLatitud() + "&lon=" + direccion.getDirLongitud() + "&sl=" + this.soloLectura;
            } else {
                urlMapa = urlMapa + "sl=" + this.soloLectura;
            }
        }
    }

    public String getUrlMapa() {
        if (urlMapa == null) {
            mostrarMapa();
        }
        return urlMapa;
    }

    public void seleccionarDepartamento() {
        try {
            direccion.setDirMunicipio(null);
            direccion.setDirCanton(null);
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCanton = new SofisComboG();
            comboCanton.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCaserio = new SofisComboG();
            comboCaserio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());
                if (!munCache.containsKey(comboDepartamento.getSelectedT())) {
                    munCache.put(this.comboDepartamento.getSelectedT(), restCatalogo.buscarMunicipio(filtro));
                }
                comboMunicipio = new SofisComboG(munCache.get(this.comboDepartamento.getSelectedT()), "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            direccion.setDirDepartamento(comboDepartamento.getSelectedT());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarMunicipio() {
        try {
            direccion.setDirMunicipio(null);
            direccion.setDirCanton(null);
            comboCanton = new SofisComboG();
            comboCanton.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboCaserio = new SofisComboG();
            comboCaserio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboMunicipio.getSelectedT() != null) {
                FiltroCanton filtro = new FiltroCanton();
                filtro.setOrderBy(new String[]{"canNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setCanMunicipioFk(comboMunicipio.getSelectedT().getMunPk());
                if (!canCache.containsKey(comboMunicipio.getSelectedT())) {
                    canCache.put(this.comboMunicipio.getSelectedT(), restCatalogo.buscarCanton(filtro));
                }
                comboCanton = new SofisComboG(canCache.get(comboMunicipio.getSelectedT()), "canNombre");
                comboCanton.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            direccion.setDirMunicipio(comboMunicipio.getSelectedT());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarZona() {
        direccion.setDirZona(comboZona.getSelectedT());
    }

    public void seleccionarTipoCalle() {
        direccion.setDirTipoCalle(comboTiposCalle.getSelectedT());
    }

    public void seleccionarCanton() {
        try {
            direccion.setDirCanton(null);
            comboCaserio = new SofisComboG();
            comboCaserio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboCanton.getSelectedT() != null) {
                FiltroCaserio filtro = new FiltroCaserio();
                filtro.setOrderBy(new String[]{"casNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setCasCantonFk(comboCanton.getSelectedT().getCanPk());
                if (!casCache.containsKey(comboCanton.getSelectedT())) {
                    casCache.put(this.comboCanton.getSelectedT(), restCatalogo.buscarCaserio(filtro));
                }
                comboCaserio = new SofisComboG(casCache.get(comboCanton.getSelectedT()), "casNombre");
                comboCaserio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            direccion.setDirCanton(comboCanton.getSelectedT());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgDireccion getDireccion() {
        return direccion;
    }

    public void setDireccion(SgDireccion direccion) {
        this.direccion = direccion;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public SofisComboG<SgCanton> getComboCanton() {
        return comboCanton;
    }

    public void setComboCanton(SofisComboG<SgCanton> comboCanton) {
        this.comboCanton = comboCanton;
    }

    public SofisComboG<SgCaserio> getComboCaserio() {
        return comboCaserio;
    }

    public void setComboCaserio(SofisComboG<SgCaserio> comboCaserio) {
        this.comboCaserio = comboCaserio;
    }

    public SofisComboG<SgZona> getComboZona() {
        return comboZona;
    }

    public void setComboZona(SofisComboG<SgZona> comboZona) {
        this.comboZona = comboZona;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public MapModel getMapaModel() {
        return mapaModel;
    }

    public void setMapaModel(MapModel mapaModel) {
        this.mapaModel = mapaModel;
    }

    public SofisComboG<SgTipoCalle> getComboTiposCalle() {
        return comboTiposCalle;
    }

    public void setComboTiposCalle(SofisComboG<SgTipoCalle> comboTiposCalle) {
        this.comboTiposCalle = comboTiposCalle;
    }

    public Boolean getVerMapa() {
        return verMapa;
    }

    public void setVerMapa(Boolean verMapa) {
        this.verMapa = verMapa;
    }

    public Boolean getVerZona() {
        return verZona;
    }

    public void setVerZona(Boolean verZona) {
        this.verZona = verZona;
    }

    public Boolean getVerCanton() {
        return verCanton;
    }

    public void setVerCanton(Boolean verCanton) {
        this.verCanton = verCanton;
    }

    public Boolean getVerCaserio() {
        return verCaserio;
    }

    public void setVerCaserio(Boolean verCaserio) {
        this.verCaserio = verCaserio;
    }

    public Boolean getVerTipoCalle() {
        return verTipoCalle;
    }

    public void setVerTipoCalle(Boolean verTipoCalle) {
        this.verTipoCalle = verTipoCalle;
    }

}
