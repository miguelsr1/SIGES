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
            
            submenu = new DefaultSubMenu(Etiquetas.getValue("ingreso", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CARGO_BIENES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("cargo", locale), null, ConstantesPaginas.CARGOS);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DESCARGO_BIENES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("descargo", locale), null, ConstantesPaginas.DESCARGOS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_TRASLADO_BIENES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("traslado", locale), null, ConstantesPaginas.TRASLADOS);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(true);
                model.addElement(submenu);
            }
            
            submenu = new DefaultSubMenu(Etiquetas.getValue("proceso", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALCULAR_DEPRECIACION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("calcularDepreciacion", locale), null, ConstantesPaginas.CALCULAR_DEPRECIACION);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_LIQUIDACION_PROYECTOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("liquidacionProyectos", locale), null, ConstantesPaginas.LIQUIDACION_PROYECTOS);
                submenu.addElement(item2);
            }
            
            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(true);
                model.addElement(submenu);
            }
            
            submenu = new DefaultSubMenu(Etiquetas.getValue("reportes", locale));
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_SOLVENCIA)) { 
                item2 = new DefaultMenuItem(Etiquetas.getValue("solvencia", locale), null, ConstantesPaginas.REPORTE_SOLVENCIA);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_CONSTANCIA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("constancia", locale), null, ConstantesPaginas.REPORTE_CONSTANCIA);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_DESCARGO_BIENES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("descargo", locale), null, ConstantesPaginas.REPORTE_DESCARGOS);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_VEHICULOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("vehiculos", locale), null, ConstantesPaginas.REPORTE_VEHICULOS);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_ACTA_RESPONSABILIDAD)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("actaResponsabilidad", locale), null, ConstantesPaginas.REPORTE_ACTA_RESPONSABILIDAD);
                submenu.addElement(item2);
            }
            /**
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_CONTROL_CORRELATIVOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("controlCorrelativos", locale), null, ConstantesPaginas.REPORTE_CONTROL_CORRELATIVOS);
                submenu.addElement(item2);
            }**/
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_BIENES_SUBCUENTAS_CONTABLES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("reporteDetalleSubCuentasContables", locale), null, ConstantesPaginas.REPORTE_CUENTAS);
                submenu.addElement(item2);
            }
            /**
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_DEPRECIACION_BIEN)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("reporteDepreciacionPorBien", locale), null, ConstantesPaginas.REPORTE_DEPRECIACION_BIEN);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_DEPRECIACION_TIPO_BIEN)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("reporteDepreciacionPorTipoBien", locale), null, ConstantesPaginas.REPORTE_TIPO_BIENES);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_ACTUALIZACION_INVENTARIO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("reporteActualizacionInventario", locale), null, ConstantesPaginas.REPORTE_ACTUALIZACION_INVENTARIO);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_BIENES_INGRESADOS_EDITADOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("reporteBienesIngresadosOEditados", locale), null, ConstantesPaginas.REPORTE_BIENES_INGRESADOS_EDITADOS);
                submenu.addElement(item2);
            }**/
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_NOTIFICACION_CUMPLIMIENTO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("reporteNotificacionIncumplimiento", locale), null, ConstantesPaginas.REPORTE_NOTIFICACION_INCUMPLIMIENTO);
                submenu.addElement(item2);
            }
            
            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(true);
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

