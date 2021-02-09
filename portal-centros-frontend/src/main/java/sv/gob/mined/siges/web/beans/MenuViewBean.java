/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;

@Named
@SessionScoped
public class MenuViewBean implements Serializable {

    private Locale locale;
    private MenuModel model;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private ApplicationBean applicationBean;

    public MenuViewBean() {
    }

    @PostConstruct
    public void init() {
        try {

            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            model = new DefaultMenuModel();

            DefaultMenuItem item2;
            DefaultSubMenu submenu;

            /*item2 = new DefaultMenuItem(Etiquetas.getValue("inicio", locale), null, ConstantesPaginas.IR_A_INICIO);
            model.addElement(item2);*/
            
            item2 = new DefaultMenuItem(Etiquetas.getValue("portalSede", locale), null, ConstantesPaginas.SEDES);
            model.addElement(item2);
            
            if(BooleanUtils.isTrue(sessionBean.getMostrarBotonEstadisticas())){
                item2 = new DefaultMenuItem(Etiquetas.getValue("estadisticas", locale), null, ConstantesPaginas.ESTADISTICAS);
                model.addElement(item2);
            }
            if(BooleanUtils.isTrue(sessionBean.getMostrarBotonDatosAbiertos())){
                item2 = new DefaultMenuItem(Etiquetas.getValue("datosabiertos", locale), null, applicationBean.getCkan());                
                model.addElement(item2);
            }
                       
            
            if(BooleanUtils.isTrue(sessionBean.getMostrarBotonQGIS())){
                item2 = new DefaultMenuItem(Etiquetas.getValue("mapaSedesEducativas", locale), null, applicationBean.getQgisUrl());
                item2.setTarget("_blank");
                model.addElement(item2);
            }
            
            item2 = new DefaultMenuItem(Etiquetas.getValue("portalSiges", locale), null, applicationBean.getPortalSiges());
            item2.setTarget("_blank");
            model.addElement(item2);
            
            model.generateUniqueIds();

        } catch (Exception ex) {

        }
    }

    public MenuModel getModel() {
        return model;
    }

}
