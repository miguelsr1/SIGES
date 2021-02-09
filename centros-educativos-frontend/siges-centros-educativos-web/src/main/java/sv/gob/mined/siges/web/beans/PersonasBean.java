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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.lazymodels.LazyLucenePersonaDataModel;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class PersonasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PersonasBean.class.getName());

    @Inject
    private PersonaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    private Boolean luceneEnabled = Boolean.FALSE;
    private FiltroPersona filtro = new FiltroPersona();
    private LazyLucenePersonaDataModel lazyDataModel;
    private List<RevHistorico> historial = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();

    public PersonasBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PERSONA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"perNie", "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido", "perEstudiante.estPk"});
            totalResultados = luceneEnabled ? restClient.buscarTotalLucene(filtro) : restClient.buscarTotal(filtro);
            lazyDataModel = new LazyLucenePersonaDataModel(restClient, filtro, totalResultados, paginado);
            lazyDataModel.setLuceneEnabled(luceneEnabled);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {

            
            SgConfiguracion c = catalogosClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_BUSQUEDA_PERSONAS_LUCENE_HABILITADA);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                luceneEnabled = Boolean.parseBoolean(c.getConValor());
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    private void limpiarCombos() {

    }

    public String limpiar() {
        filtro = new FiltroPersona();
        filtroNombreCompleto = new FiltroNombreCompleto();
        totalResultados = null;
        lazyDataModel = null;
        return null;
    }

    public String historial(Long id) {
        try {
            historial = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public FiltroPersona getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPersona filtro) {
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

    public LazyLucenePersonaDataModel getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyLucenePersonaDataModel lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

}
