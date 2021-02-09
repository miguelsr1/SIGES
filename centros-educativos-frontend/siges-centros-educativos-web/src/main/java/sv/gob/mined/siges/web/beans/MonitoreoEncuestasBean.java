/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.graficos.GraficoColumnas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.GraficosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class MonitoreoEncuestasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MonitoreoEncuestasBean.class.getName());
    
    @Inject
    private GraficosRestClient graficosClient;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient catalogosClient;
    
    private GraficoColumnas graficoMatriculasYEncuestas = new GraficoColumnas();
    private GraficoColumnas graficoEncuestasPorSexo = new GraficoColumnas();
    
    private Boolean renderGraficoEncuestasPorSexo = Boolean.FALSE;
    
    
    private SofisComboG<SgDepartamento> comboDepartamentos;


    public MonitoreoEncuestasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            if (!validarAcceso()){
                return;
            }
            cargarCombos();
            cargarGraficoEncuestasPorSexo();
            cargarGraficoMatriculasYEncuestas();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MONITOREO_ENCUESTAS_ESTUDIANTES)) {
            JSFUtils.redirectToIndex();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    private void cargarCombos(){
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogosClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void recargarYRenderizarGraficas() {
        try {
            cargarGraficoEncuestasPorSexo();
            cargarGraficoMatriculasYEncuestas();
            PrimeFaces.current().executeScript("renderizarEncuestasPorSexo()");
            PrimeFaces.current().executeScript("renderizarMatriculasEncuestas()");
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    
    private void cargarGraficoEncuestasPorSexo() {
        try {
            Long depPk = this.comboDepartamentos.getSelectedT() != null ? this.comboDepartamentos.getSelectedT().getDepPk() : null;
            graficoEncuestasPorSexo = graficosClient.obtenerEvolucionEncuestasPorSexo(depPk);
            
            if (graficoEncuestasPorSexo.getValores() != null 
                    && graficoEncuestasPorSexo.getValores().size() > 0 
                    && graficoEncuestasPorSexo.getEtiquetasValor() != null
                    && graficoEncuestasPorSexo.getEtiquetasValor().length > 0) {
                renderGraficoEncuestasPorSexo = Boolean.TRUE;
            } else {
                renderGraficoEncuestasPorSexo = Boolean.FALSE;
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
   
    
    private void cargarGraficoMatriculasYEncuestas() {
        try {
            Long depPk = this.comboDepartamentos.getSelectedT() != null ? this.comboDepartamentos.getSelectedT().getDepPk() : null;
            graficoMatriculasYEncuestas = graficosClient.obtenerEvolucionEncuestasYMatriculas(depPk);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }
    

    public GraficoColumnas getGraficoMatriculasYEncuestas() {
        return graficoMatriculasYEncuestas;
    }

    public void setGraficoMatriculasYEncuestas(GraficoColumnas graficoMatriculasYEncuestas) {
        this.graficoMatriculasYEncuestas = graficoMatriculasYEncuestas;
    }

    public GraficoColumnas getGraficoEncuestasPorSexo() {
        return graficoEncuestasPorSexo;
    }

    public void setGraficoEncuestasPorSexo(GraficoColumnas graficoEncuestasPorSexo) {
        this.graficoEncuestasPorSexo = graficoEncuestasPorSexo;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public Boolean getRenderGraficoEncuestasPorSexo() {
        return renderGraficoEncuestasPorSexo;
    }

    public void setRenderGraficoEncuestasPorSexo(Boolean renderGraficoEncuestasPorSexo) {
        this.renderGraficoEncuestasPorSexo = renderGraficoEncuestasPorSexo;
    }
    
    
    
    

}