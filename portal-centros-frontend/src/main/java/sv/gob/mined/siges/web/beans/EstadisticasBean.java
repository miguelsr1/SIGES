/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
import sv.gob.mined.siges.web.restclient.IndicadorRestClient;

@Named
@ViewScoped
public class EstadisticasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstadisticasBean.class.getName());

    @Inject
    private IndicadorRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoClient;


    private FiltroIndicadores filtro = new FiltroIndicadores();
    private List<SgEstIndicador> listIndicadores=new ArrayList();
    private List<SgEstCategoriaIndicador> listCategoriaIndicador=new ArrayList();
    private HashMap<SgEstCategoriaIndicador, List<SgEstIndicador> > hashCategoria = new HashMap<>();
    private Integer paginado = 100;
    private Long totalResultados;

    public EstadisticasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

  

    public String buscar() {
        try {
            hashCategoria = new HashMap<>();
            
            filtro =new FiltroIndicadores();
            filtro.setEsPublico(Boolean.TRUE);
            filtro.setIncluirCampos(new String[]{"indCategoria.cinPk","indCategoria.cinCodigo","indCategoria.cinNombre","indCategoria.cinVersion",
                "indCodigo","indNombre","indVersion"});
            listIndicadores=restClient.buscar(filtro);
            listCategoriaIndicador=listIndicadores.stream().map(c->c.getIndCategoria()).distinct().collect(Collectors.toList());
            Collections.sort(listCategoriaIndicador, (o1,o2)->o1.getCinNombre().compareTo(o2.getCinNombre()));
            totalResultados=Long.valueOf(listCategoriaIndicador.size());
            for(SgEstCategoriaIndicador categoria:listCategoriaIndicador){
                if (!hashCategoria.containsKey(categoria)) {
                    List<SgEstIndicador> indicadores=listIndicadores.stream().filter(c->c.getIndCategoria().getCinPk().equals(categoria.getCinPk())).collect(Collectors.toList());
                    hashCategoria.put(categoria, indicadores);
                }
                
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }        

    public void limpiar() {
        filtro = new FiltroIndicadores();
        
    }

    public List<SgEstIndicador> obtenerIndicadores(SgEstCategoriaIndicador categoria){
        return hashCategoria.get(categoria);
    }


    public CatalogosRestClient getCatalogoClient() {
        return catalogoClient;
    }

    public void setCatalogoClient(CatalogosRestClient catalogoClient) {
        this.catalogoClient = catalogoClient;
    }

   
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public IndicadorRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(IndicadorRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroIndicadores getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroIndicadores filtro) {
        this.filtro = filtro;
    }

    public List<SgEstIndicador> getListIndicadores() {
        return listIndicadores;
    }

    public void setListIndicadores(List<SgEstIndicador> listIndicadores) {
        this.listIndicadores = listIndicadores;
    }

    public List<SgEstCategoriaIndicador> getListCategoriaIndicador() {
        return listCategoriaIndicador;
    }

    public void setListCategoriaIndicador(List<SgEstCategoriaIndicador> listCategoriaIndicador) {
        this.listCategoriaIndicador = listCategoriaIndicador;
    }

    public HashMap<SgEstCategoriaIndicador, List<SgEstIndicador>> getHashCategoria() {
        return hashCategoria;
    }

    public void setHashCategoria(HashMap<SgEstCategoriaIndicador, List<SgEstIndicador>> hashCategoria) {
        this.hashCategoria = hashCategoria;
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

}
