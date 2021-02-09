/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataInvitacionProveedores {
    private String usuarioProceso;
    private String autorizadoMINED;
    private List<DataProveedorItems> proveedoresItems;

    public String getUsuarioProceso() {
        return usuarioProceso;
    }

    public void setUsuarioProceso(String usuarioProceso) {
        this.usuarioProceso = usuarioProceso;
    }

    public String getAutorizadoMINED() {
        return autorizadoMINED;
    }

    public void setAutorizadoMINED(String autorizadoMINED) {
        this.autorizadoMINED = autorizadoMINED;
    }

    public List<DataProveedorItems> getProveedoresItems() {
        return proveedoresItems;
    }

    public void setProveedoresItems(List<DataProveedorItems> proveedoresItems) {
        this.proveedoresItems = proveedoresItems;
    }
    
    
}
