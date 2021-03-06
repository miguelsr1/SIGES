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
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;

@Named
@SessionScoped
public class MenuViewBean implements Serializable {

    private Locale locale;
    private MenuModel model;
   
    @Inject
    private SessionBean sessionBean;
    
    public MenuViewBean() {
    }

    @PostConstruct
    public void init() {
        try {

            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            model = new DefaultMenuModel();

            DefaultMenuItem item2;
            DefaultSubMenu submenu;

            

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ETIQUETAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionEtiqueta", locale), null, ConstantesPaginas.GESTION_ETIQUETAS);
                model.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_RH_PAGINA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionPagina", locale), null, ConstantesPaginas.GESTION_PAGINAS);
                model.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_INCORPORACIONES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionIncorporacion", locale), null, ConstantesPaginas.INCORPORACIONES);
                model.addElement(item2);
            }
            
            
            
//
//            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_TRASLADO_BIENES)) {
//                item2 = new DefaultMenuItem(Etiquetas.getValue("traslado", locale), null, ConstantesPaginas.TRASLADOS);
//                submenu.addElement(item2);
//            }

            
            
           

            model.generateUniqueIds();

        } catch (Exception ex) {

        }
    }

    public MenuModel getModel() {
        return model;
    }

    public String irA(String irA) {
        return irA;
    }
}

