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

            item2 = new DefaultMenuItem("Administración", null, ConstantesPaginas.ADMINISTRACION);
            model.addElement(item2);

            DefaultMenuItem item3;
            item3 = new DefaultMenuItem("Configuración", null, ConstantesPaginas.GESTION_CONFIGURACIONES);
            model.addElement(item3);

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CONFIGURAR_FIRMAS_ELECTRONICAS)) {

                DefaultSubMenu submenu = new DefaultSubMenu(Etiquetas.getValue("firmaElectronica"));

                item3 = new DefaultMenuItem(Etiquetas.getValue("hconfiguracionParametros"), null, ConstantesPaginas.GESTION_CONFIGURACIONES_PFEA);
                submenu.addElement(item3);

                item3 = new DefaultMenuItem(Etiquetas.getValue("hconfiguracionComponentes"), null, ConstantesPaginas.GESTION_FIRMA_ELECTRONICA);
                submenu.addElement(item3);
                
                item3 = new DefaultMenuItem(Etiquetas.getValue("hconfiguracionTextos"), null, ConstantesPaginas.GESTION_TEXTOS_PFEA);
                submenu.addElement(item3);
                
                
                model.addElement(submenu);

            }

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
