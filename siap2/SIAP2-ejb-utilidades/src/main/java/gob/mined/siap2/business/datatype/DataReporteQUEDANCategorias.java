/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de las categorías del Quedan
 *
 * @author rgonzalez
 */
public class DataReporteQUEDANCategorias {

    private String categoria;
    private String prestamo;
    private String goes;
    private String total;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(String prestamo) {
        this.prestamo = prestamo;
    }

    public String getGoes() {
        return goes;
    }

    public void setGoes(String goes) {
        this.goes = goes;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

     // </editor-fold>
}
