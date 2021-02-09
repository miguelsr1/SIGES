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
import org.primefaces.model.menu.MenuModel;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.mensajes.Etiquetas;


@Named
@SessionScoped
public class MenuViewBean implements Serializable {
    
    @Inject
    private SessionBean sessionBean;

    private Locale locale;
    private MenuModel model;
   
    public MenuViewBean() {
    }

    @PostConstruct
    public void init() {
        try {

            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            model = new DefaultMenuModel();

            DefaultMenuItem item2;

            /*item2 = new DefaultMenuItem("Administración");
            item2.setCommand("#{menuViewBean.irA('IR_A_administracion')}");
            model.addElement(item2);*/
            
            item2 = new DefaultMenuItem("Categorías de Operaciones");
            item2.setCommand("#{menuViewBean.irA('IR_A_categoriasOperacion')}");
            model.addElement(item2);
            
            item2 = new DefaultMenuItem("Operaciones");
            item2.setCommand("#{menuViewBean.irA('IR_A_operaciones')}");
            model.addElement(item2);
            
            item2 = new DefaultMenuItem("Roles");
            item2.setCommand("#{menuViewBean.irA('IR_A_roles')}");
            model.addElement(item2);
            
            item2 = new DefaultMenuItem("Personas Usuarias");
            item2.setCommand("#{menuViewBean.irA('IR_A_usuarios')}");
            model.addElement(item2);
            
            if (BooleanUtils.isTrue(sessionBean.getEntidadUsuario().getUsuSuperUsuario())) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionReglaContexto"));
                item2.setCommand("#{menuViewBean.irA('IR_A_reglas_contexto')}");
                model.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_AUDITORIA_LOGIN)) {
                item2 = new DefaultMenuItem("Auditoría Login");
                item2.setCommand("#{menuViewBean.irA('IR_A_auditoria_login')}");
                model.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_AUDITORIA)) {
                item2 = new DefaultMenuItem("Auditoría");
                item2.setCommand("#{menuViewBean.irA('IR_A_auditoria')}");
                model.addElement(item2);
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
