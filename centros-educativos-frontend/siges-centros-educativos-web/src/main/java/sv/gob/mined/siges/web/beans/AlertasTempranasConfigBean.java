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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCiclo;
import sv.gob.mined.siges.web.dto.SgConfigAlerta;
import sv.gob.mined.siges.web.dto.SgConfigAlertaAsistencia;
import sv.gob.mined.siges.web.dto.SgConfigAlertaDesempenio;
import sv.gob.mined.siges.web.dto.SgConfigAlertaManifestacionViolencia;
import sv.gob.mined.siges.web.dto.SgConfigAlertaSobreedad;
import sv.gob.mined.siges.web.dto.SgConfigAlertaTrabajo;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaViolencia;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoManifestacion;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.web.enumerados.EnumCategoriaAlertaTrabajo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTiposManifestacionViolencia;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AlertaConfigRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AlertasTempranasConfigBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AlertasTempranasConfigBean.class.getName());

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private AlertaConfigRestClient alertaConfigClient;

    @Inject
    private CicloRestClient cicloClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private Integer paginado = 10;
    private Long totalResultados;

    private SgConfigAlerta config;

    public AlertasTempranasConfigBean() {
    }

    @PostConstruct
    public void init() {
        try {

            cargarCombos();
            cargar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargar() {
        try {

            config = alertaConfigClient.buscar(new FiltroCodiguera()).get(0);
            if (config.getCnfAlertaManifestacionViolencia() == null) {
                config.setCnfAlertaManifestacionViolencia(new ArrayList<>());
            }

            if (config.getCnfAlertaTrabajo() == null) {
                config.setCnfAlertaTrabajo(new ArrayList<>());
            }

            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"cavNombre"});
            filtro.setAscending(new boolean[]{true});

            for (SgCategoriaViolencia e : catalogoClient.buscarCategoriaViolencia(filtro)) {
                if (config.getCnfAlertaManifestacionViolencia().stream().anyMatch(m -> m.getCnfCategoria().equals(e))) {
                    continue;
                }
                SgConfigAlertaManifestacionViolencia c = new SgConfigAlertaManifestacionViolencia();
                c.setCnfCategoria(e);
                config.getCnfAlertaManifestacionViolencia().add(c);
            }

            for (EnumCategoriaAlertaTrabajo e : EnumCategoriaAlertaTrabajo.values()) {
                if (config.getCnfAlertaTrabajo().stream().anyMatch(m -> m.getCnfCategoria().equals(e))) {
                    continue;
                }
                SgConfigAlertaTrabajo c = new SgConfigAlertaTrabajo();
                c.setCnfCategoria(e);
                config.getCnfAlertaTrabajo().add(c);
            }

            FiltroCiclo fCiclo = new FiltroCiclo();
            fCiclo.setOrgAplicaAlertas(Boolean.TRUE);
            fCiclo.setOrderBy(new String[]{"cicNivel.nivOrden", "cicOrden"});
            fCiclo.setAscending(new boolean[]{true, true});
            fCiclo.setIncluirCampos(new String[]{"cicNombre", "cicVersion", "cicNivel.nivNombre", "cicNivel.nivPk", "cicNivel.nivVersion"});
            
            List<SgCiclo> ciclos = cicloClient.buscar(fCiclo);
            
            for (SgCiclo cic : ciclos) {
                if (config.getCnfAlertaAsistencia().stream().anyMatch(m -> m.getCnfCategoria().equals(cic))) {
                    continue;
                }
                SgConfigAlertaAsistencia c = new SgConfigAlertaAsistencia();
                c.setCnfCategoria(cic);
                config.getCnfAlertaAsistencia().add(c); 
            }
            
            for (SgCiclo cic : ciclos) {
                if (config.getCnfAlertaSobreedad().stream().anyMatch(m -> m.getCnfCategoria().equals(cic))) {
                    continue;
                }
                SgConfigAlertaSobreedad c = new SgConfigAlertaSobreedad();
                c.setCnfCategoria(cic);
                config.getCnfAlertaSobreedad().add(c); 
            }
            
            for (SgCiclo cic : ciclos) {
                if (config.getCnfAlertaDesempenio().stream().anyMatch(m -> m.getCnfCategoria().equals(cic))) {
                    continue;
                }
                SgConfigAlertaDesempenio c = new SgConfigAlertaDesempenio();
                c.setCnfCategoria(cic);
                config.getCnfAlertaDesempenio().add(c); 
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public List<SgTipoManifestacion> completeTipoManifestacion(String query) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            SgConfigAlertaManifestacionViolencia config = (SgConfigAlertaManifestacionViolencia) UIComponent.getCurrentComponent(context).getAttributes().get("cnf");

            FiltroTiposManifestacionViolencia fil = new FiltroTiposManifestacionViolencia();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setTmaCategoriaViolenciaPk(config.getCnfCategoria().getCavPk());
            fil.setOrderBy(new String[]{"tmaNombre"});
            fil.setAscending(new boolean[]{false});
            return catalogoClient.buscarTipoManifestacion(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgTipoTrabajo> completeTipoTrabajo(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"ttrNombre"});
            fil.setAscending(new boolean[]{false});
            return catalogoClient.buscarTipoTrabajo(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void guardar() {
        try {
            alertaConfigClient.guardar(config);
            cargar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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


    public SgConfigAlerta getConfig() {
        return config;
    }

    public void setConfig(SgConfigAlerta config) {
        this.config = config;
    }

}
