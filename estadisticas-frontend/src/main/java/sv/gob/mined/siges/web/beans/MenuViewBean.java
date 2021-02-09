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

            item2 = new DefaultMenuItem(Etiquetas.getValue("inicio", locale), null, ConstantesPaginas.IR_A_INICIO);
            model.addElement(item2);

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_INDICADORES)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hindicadores", locale), null, ConstantesPaginas.IR_A_INDICADORES);
                model.addElement(item2);
            }
                  
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_EXTRACCIONES)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionExtraccion", locale), null, ConstantesPaginas.IR_A_EXTRACCIONES);
                model.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTADISTICAS)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hEstadisticas", locale), null, ConstantesPaginas.IR_A_ESTADISTICAS);
                model.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CARGAS_EXTERNAS)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionCargaExterna", locale), null, ConstantesPaginas.IR_A_CARGAS_EXTERNAS);
                model.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DATOS_EXTERNOS)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hdatosExternos", locale), null, ConstantesPaginas.IR_A_DATOS_EXTERNOS);
                model.addElement(item2);
            }

            model.generateUniqueIds();

        } catch (Exception ex) {

        }
    }

    public MenuModel getModel() {
        return model;
    }

}
