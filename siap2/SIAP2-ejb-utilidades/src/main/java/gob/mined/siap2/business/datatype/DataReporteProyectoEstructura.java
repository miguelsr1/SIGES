/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de la estructura del reporte de proyectos.
 * @author Sofis Solutions
 */
public class DataReporteProyectoEstructura {
    String numero="";
    String nombre="";
    String producto="";
    String importe="";
    String porcentaje="";    
    String unidadTecnica="";

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMacroActividad() {
        return nombre;
    }

    public void setMacroActividad(String macroActividad) {
        this.nombre = macroActividad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(String unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
  // </editor-fold>
    
}
