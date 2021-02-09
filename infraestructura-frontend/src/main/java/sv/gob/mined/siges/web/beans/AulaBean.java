/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AulaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AulaBean.class.getName());

    @Inject
    private AulaRestClient restClient;
    
    @Inject
    private RelAulaEspacioBean relAulaEspacioBean;
    
    @Inject
    private RelAulaServicioBean relAulaServicioBean;

    @Inject
    @Param(name = "id")
    private Long aulaId;


    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgAula entidadEnEdicion = new SgAula();
    private Integer paginado = 10;
    private Long totalResultados;
    private String tabActiveId;
    private Boolean soloLectura = Boolean.FALSE;

    public AulaBean() {
    }

    @PostConstruct
    public void init() {
        try {
           
            if (aulaId != null && aulaId > 0) {
                this.actualizar(restClient.obtenerPorId(aulaId));
                soloLectura = editable != null ? !editable : soloLectura;
            }else{
                agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public String getNombreEntidadEdicion(){
        String nombre=entidadEnEdicion.getAulCodigo();
        if(!StringUtils.isBlank(entidadEnEdicion.getAulNombre())){
            nombre=nombre+" - "+entidadEnEdicion.getAulNombre();
        }
        return nombre;
    }

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
     
             
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }



    public void agregar() {
	JSFUtils.limpiarMensajesError();

        entidadEnEdicion = new SgAula();
    }

    public void actualizar(SgAula var) {
	JSFUtils.limpiarMensajesError();
      
        entidadEnEdicion =var;
    }


    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgAula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAula entidadEnEdicion) {
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

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }



}
