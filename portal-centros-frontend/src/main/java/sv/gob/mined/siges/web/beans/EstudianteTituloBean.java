/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.restclient.RelInmuebleArchivoRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.validacion.SedesViewValidator;

@Named
@ViewScoped
public class EstudianteTituloBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudianteTituloBean.class.getName());

    @Inject
    private TituloRestClient restClient;

    @Inject
    private RelInmuebleArchivoRestClient relInmuebleArchivoRestClient;

    @Inject
    private SessionBean sessionBean;


    @Inject
    @Param(name = "estudianteTitulo")
    private Long estudianteImpresion;

    @Inject
    private SedeRestClient restSede;

    private Boolean soloLectura = Boolean.FALSE;
    private String tabActiveId;
    private SgTitulo entidadEnEdicion;
    private SedesViewValidator sedesViewValidator;
    private String ofertaJornadas = null;
    private List<SgJornadaLaboral> jornadasLaborales;
    private List<String> imagenes = new LinkedList<>();  
    private List<SgArchivo> imagenesInfra;

    public EstudianteTituloBean() {
    }

    @PostConstruct
    public void init() {
        try {      
      
            if (estudianteImpresion != null && estudianteImpresion > 0) {
                this.actualizar(restClient.obtenerPorId(estudianteImpresion));
                if(BooleanUtils.isTrue(entidadEnEdicion.getTitAnulada())){
                    JSFUtils.redirectNotFound();
                }
                soloLectura = true;
            }
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void actualizar(SgTitulo estudianteImpresion) {
        try {
            entidadEnEdicion = estudianteImpresion;
          
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

   

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

 

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SedesViewValidator getSedesViewValidator() {
        return sedesViewValidator;
    }

    public void setSedesViewValidator(SedesViewValidator sedesViewValidator) {
        this.sedesViewValidator = sedesViewValidator;
    }

    public List<SgJornadaLaboral> getJornadasLaborales() {
        return jornadasLaborales;
    }

    public void setJornadasLaborales(List<SgJornadaLaboral> jornadasLaborales) {
        this.jornadasLaborales = jornadasLaborales;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }
  
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
    
    public List<String> getGaleria(){
        return imagenes; 
    }

 
    public void setOfertaJornadas(String ofertaJornadas) {
        this.ofertaJornadas = ofertaJornadas;
    }

    public List<SgArchivo> getImagenesInfra() {
        return imagenesInfra;
    }

    public void setImagenesInfra(List<SgArchivo> imagenesInfra) {
        this.imagenesInfra = imagenesInfra;
    }

    public SgTitulo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTitulo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    
}
