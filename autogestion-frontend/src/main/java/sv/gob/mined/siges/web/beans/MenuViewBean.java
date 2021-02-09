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

    public MenuViewBean() {
    }

    @PostConstruct
    public void init() {
        try {

            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            model = new DefaultMenuModel();

            DefaultMenuItem item2;

            item2 = new DefaultMenuItem(Etiquetas.getValue("hperfil", locale), null, ConstantesPaginas.PERFIL);
            model.addElement(item2);

            item2 = new DefaultMenuItem(Etiquetas.getValue("hcambiarContrasenia", locale), null, ConstantesPaginas.CAMBIAR_CRENDENCIALES);
            model.addElement(item2);
            
            item2 = new DefaultMenuItem(Etiquetas.getValue("hcertificado", locale), null, ConstantesPaginas.CERTIFICADO);
            model.addElement(item2);


            //<<INSERTAR_SUB_MENU_VALORES>>
            model.generateUniqueIds();

        } catch (Exception ex) {

        }
    }

    public MenuModel getModel() {
        return model;
    }

}
