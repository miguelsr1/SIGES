/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgCountAlertas;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.web.enumerados.EnumVariableAlerta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAlerta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyAlertaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AlertaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class AlertasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AlertasBean.class.getName());

    @Inject
    private AlertaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    @Param(name = "riesgo")
    private String riesgoFiltro;

    private FiltroAlerta filtro = new FiltroAlerta();
    private LazyAlertaDataModel alertaLazyModel;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private SgCountAlertas totalPorRiesgos;
    private Long totalResultados;
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private SofisComboG<SgSexo> comboSexos;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private SofisComboG<SgDepartamento> comboDepartamentoMatricula;
    private SofisComboG<SgMunicipio> comboMunicipioMatricula;
    private SofisComboG<EnumVariableAlerta> comboVariablesAlerta;
    private SofisComboG<EnumRiesgoAlerta> comboRiesgosAlerta;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado;

    public AlertasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
            validarAcceso();
            cargarCombos();
            try {
                if (!StringUtils.isBlank(riesgoFiltro)) {
                    EnumRiesgoAlerta r = EnumRiesgoAlerta.valueOf(riesgoFiltro.toUpperCase());
                    this.comboRiesgosAlerta.setSelectedT(r);
                    buscar();
                }
            } catch (Exception ex) {
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ALERTAS_TEMPRANAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            List<EnumVariableAlerta> variables = new ArrayList<>();
            if (this.comboVariablesAlerta.getSelectedT() != null) {
                variables.add(this.comboVariablesAlerta.getSelectedT());
            }
            List<EnumRiesgoAlerta> riesgos = new ArrayList<>();
            if (this.comboRiesgosAlerta.getSelectedT() != null) {
                riesgos.add(this.comboRiesgosAlerta.getSelectedT());
            }
            filtro.setAleRiesgos(riesgos);
            filtro.setAleVariables(variables);
            filtro.setEstDepartamentoMatricula(comboDepartamentoMatricula != null ? comboDepartamentoMatricula.getSelectedT() != null ? comboDepartamentoMatricula.getSelectedT().getDepPk() : null : null);
            filtro.setEstMunicipioMatricula(comboMunicipioMatricula != null ? comboMunicipioMatricula.getSelectedT() != null ? comboMunicipioMatricula.getSelectedT().getMunPk() : null : null);
            filtro.setPerSexoPk(comboSexos.getSelectedT() != null ? comboSexos.getSelectedT().getSexPk() : null);
            filtro.setPerDepartamentoPk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setPerMunicipioPk(comboMunicipio.getSelectedT() != null ? comboMunicipio.getSelectedT().getMunPk() : null);
            filtro.setIncluirCampos(new String[]{
                "aleRiesgo", "aleVariable", "aleEstudiante.estPk",
                "aleEstudiante.estPersona.perNie", "aleEstudiante.estPersona.perSexo.sexNombre",
                "aleEstudiante.estPersona.perPrimerNombre", "aleEstudiante.estPersona.perSegundoNombre",
                "aleEstudiante.estPersona.perPrimerApellido", "aleEstudiante.estPersona.perSegundoApellido", "aleEstudiante.estPersona.perFechaNacimiento"});
            filtro.setOrderBy(new String[]{"aleEstudiante.estPersona.perPrimerApellido", "aleEstudiante.estPersona.perSegundoApellido", "aleEstudiante.estPersona.perPrimerNombre", "aleEstudiante.estPersona.perSegundoNombre"});
            filtro.setAscending(new boolean[]{true, true, true, true});

            
            totalPorRiesgos = restClient.buscarTotalRiesgos(filtro);
            
            totalResultados = totalPorRiesgos.getTotal(); //restClient.buscarTotal(filtro);

            alertaLazyModel = new LazyAlertaDataModel(restClient, filtro, totalResultados, paginado);
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
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDepartamentoMatricula = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentoMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"sexNombre"});
            fc.setIncluirCampos(new String[]{"sexNombre", "sexVersion"});
            List<SgSexo> sexos = restCatalogo.buscarSexo(fc);
            comboSexos = new SofisComboG(sexos, "sexNombre");
            comboSexos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipioMatricula = new SofisComboG();
            comboMunicipioMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboVariablesAlerta = new SofisComboG<>(Arrays.asList(EnumVariableAlerta.values()), "text");
            comboVariablesAlerta.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            List<EnumRiesgoAlerta> listriesgo = new ArrayList<>();
            listriesgo.add(EnumRiesgoAlerta.MUY_ALTO);
            listriesgo.add(EnumRiesgoAlerta.ALTO);
            listriesgo.add(EnumRiesgoAlerta.MEDIO);
            comboRiesgosAlerta = new SofisComboG<>(listriesgo, "text");
            comboRiesgosAlerta.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
        }
        inicializarFiltrosSeccion();
    }

    public void inicializarFiltrosSeccion() {
        if (this.filtrosSeccion.getFiltro() == null) {
            this.filtrosSeccion.setFiltro(this.filtro);
            this.filtrosSeccion.cargarCombos();
        }
    }

    public String limpiar() {
        filtro = new FiltroAlerta();
        alertaLazyModel = null;
        comboSexos.setSelected(-1);
        comboDepartamentos.setSelected(-1);
        comboDepartamentoMatricula.setSelected(-1);
        comboMunicipio = new SofisComboG();
        comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboMunicipioMatricula = new SofisComboG();
        comboMunicipioMatricula.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        if (this.filtrosSeccion.getFiltro() != null) {
            this.filtrosSeccion.limpiarCombos();
        }
        return null;
    }

    public FiltroAlerta getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAlerta filtro) {
        this.filtro = filtro;
    }

    public LazyAlertaDataModel getAlertaLazyModel() {
        return alertaLazyModel;
    }

    public void setAlertaLazyModel(LazyAlertaDataModel alertaLazyModel) {
        this.alertaLazyModel = alertaLazyModel;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public SofisComboG<SgSexo> getComboSexos() {
        return comboSexos;
    }

    public void setComboSexos(SofisComboG<SgSexo> comboSexos) {
        this.comboSexos = comboSexos;
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

    public Boolean getPanelAvanzado() {
        return panelAvanzado;
    }

    public void setPanelAvanzado(Boolean panelAvanzado) {
        this.panelAvanzado = panelAvanzado;
    }

    public String getTxtFiltroAvanzado() {
        return txtFiltroAvanzado;
    }

    public void setTxtFiltroAvanzado(String txtFiltroAvanzado) {
        this.txtFiltroAvanzado = txtFiltroAvanzado;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoMatricula() {
        return comboDepartamentoMatricula;
    }

    public void setComboDepartamentoMatricula(SofisComboG<SgDepartamento> comboDepartamentoMatricula) {
        this.comboDepartamentoMatricula = comboDepartamentoMatricula;
    }

    public SofisComboG<SgMunicipio> getComboMunicipioMatricula() {
        return comboMunicipioMatricula;
    }

    public void setComboMunicipioMatricula(SofisComboG<SgMunicipio> comboMunicipioMatricula) {
        this.comboMunicipioMatricula = comboMunicipioMatricula;
    }

    public SofisComboG<EnumVariableAlerta> getComboVariablesAlerta() {
        return comboVariablesAlerta;
    }

    public void setComboVariablesAlerta(SofisComboG<EnumVariableAlerta> comboVariablesAlerta) {
        this.comboVariablesAlerta = comboVariablesAlerta;
    }

    public SofisComboG<EnumRiesgoAlerta> getComboRiesgosAlerta() {
        return comboRiesgosAlerta;
    }

    public void setComboRiesgosAlerta(SofisComboG<EnumRiesgoAlerta> comboRiesgosAlerta) {
        this.comboRiesgosAlerta = comboRiesgosAlerta;
    }

    public SgCountAlertas getTotalPorRiesgos() {
        return totalPorRiesgos;
    }

    
    

}
