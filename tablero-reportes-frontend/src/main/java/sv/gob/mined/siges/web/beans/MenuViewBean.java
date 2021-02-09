/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgMenuPentaho;
import sv.gob.mined.siges.web.enumerados.EnumTipoComponentePentaho;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.MenuPentahoRestClient;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMenuPentaho;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@SessionScoped
public class MenuViewBean implements Serializable {

    private Locale locale;
    private MenuModel model;
    
    @Inject
    private ApplicationBean aplicacionBean;    

    @Inject
    private MenuPentahoRestClient menuPentahoRestClient;
    
    @Inject
    private SessionBean sessionBean;
    
    private static final Logger LOGGER = Logger.getLogger(MenuViewBean.class.getName());


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
            
            List<SgMenuPentaho> menuPentaho = this.obtenerComponentes();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(sessionBean.getOperaciones());
            LOGGER.log(Level.INFO, json);

            for(EnumTipoComponentePentaho obj: EnumTipoComponentePentaho.values()){
                submenu = new DefaultSubMenu(obj.getText());
                submenu.setExpanded(false);
                submenu.setRendered(false);
                
                List<SgMenuPentaho> filterMenuPentaho= menuPentaho.stream()
                        .filter(menu -> menu.getMpeTipo().toString().equals(obj.name()))
                        .collect(Collectors.toList());
                
                for(SgMenuPentaho menu : filterMenuPentaho) {
                    if (sessionBean.getOperaciones().contains(menu.getMpeOperacionFk().getOpeCodigo()) || sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_MENU_PENTAHO)) {         
                        String urlComponente =  ConstantesPaginas.IR_A_PENTAHO+"?componente="+aplicacionBean.getPentahoUrl()+menu.getMpeRuta();
                        if (obj.name().equals(Constantes.COMPONENTE_TARGET_BLANK)){
                            urlComponente = aplicacionBean.getPentahoUrl()+menu.getMpeRuta();
                        }
                        item2 = new DefaultMenuItem(menu.getMpeNombre(), null, urlComponente);
                        if (obj.name().equals(Constantes.COMPONENTE_TARGET_BLANK)){
                            item2.setTarget("_blank");
                        }
                        submenu.addElement(item2);
                    }
                }
                if (submenu.getElements().size() > 0) {
                    submenu.setExpanded(true);
                    submenu.setRendered(true);
                }
                model.addElement(submenu);
            }

            model.generateUniqueIds();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");

        }
    }

    public MenuModel getModel() {
        return model;
    }
    
    private List<SgMenuPentaho> obtenerComponentes() {
        try {
            FiltroMenuPentaho fil = new FiltroMenuPentaho();
            fil.setOrderBy(new String[]{"mpeTipo","mpeNombre"});
            fil.setAscending(new boolean[]{true,true});
            return menuPentahoRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }


}
