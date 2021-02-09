/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyGlosarioDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class AyudaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AyudaBean.class.getName());
    
    @Inject
    private CatalogosRestClient catalogoClient;
    
    private Integer paginadoGlosario = 10;
    private Long glosariosTotalResultados;
    private LazyGlosarioDataModel glosariosLazyModel;
    private FiltroCodiguera filtroGlosario = new FiltroCodiguera();
    private Boolean rendered = Boolean.FALSE;

    public AyudaBean() {
    }

    public SgAyuda getAyuda(String codigo) {
        try {
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setCodigoExacto(codigo);
            filtro.setHabilitado(Boolean.TRUE);
            List<SgAyuda> ayudas = catalogoClient.buscarAyudas(filtro);
            if (!ayudas.isEmpty()) {
                return ayudas.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
    
    public void showSidebar(){
        this.rendered = Boolean.TRUE;
    }
    
    
    public void buscarGlosario() {
        try {
            filtroGlosario.setFirst(new Long(0));
            glosariosTotalResultados = catalogoClient.buscarGlosariosTotal(filtroGlosario);
            glosariosLazyModel = new LazyGlosarioDataModel(catalogoClient, filtroGlosario, glosariosTotalResultados, paginadoGlosario);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public LazyGlosarioDataModel getGlosariosLazyModel() {
        return glosariosLazyModel;
    }

    public void setGlosariosLazyModel(LazyGlosarioDataModel glosariosLazyModel) {
        this.glosariosLazyModel = glosariosLazyModel;
    }

    public Integer getPaginadoGlosario() {
        return paginadoGlosario;
    }

    public void setPaginadoGlosario(Integer paginadoGlosario) {
        this.paginadoGlosario = paginadoGlosario;
    }

    public Long getGlosariosTotalResultados() {
        return glosariosTotalResultados;
    }

    public void setGlosariosTotalResultados(Long glosariosTotalResultados) {
        this.glosariosTotalResultados = glosariosTotalResultados;
    }

    public FiltroCodiguera getFiltroGlosario() {
        return filtroGlosario;
    }

    public void setFiltroGlosario(FiltroCodiguera filtroGlosario) {
        this.filtroGlosario = filtroGlosario;
    }

    public Boolean getRendered() {
        return rendered;
    }
    
    
   
    
}
